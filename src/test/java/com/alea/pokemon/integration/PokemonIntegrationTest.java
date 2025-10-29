package com.alea.pokemon.integration;

import com.alea.pokemon.AleaBackendTestApplication;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = AleaBackendTestApplication.class
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PokemonIntegrationTest {

    private WireMockServer wireMockServer;

    @LocalServerPort
    int port;

    @Autowired
    WebClient.Builder webClientBuilder;

    @DynamicPropertySource
    static void configureProps(DynamicPropertyRegistry registry) {
        registry.add("pokeapi.base-url", () -> "http://localhost:9561/api/v2");
        registry.add("pokeapi.fetch-limit", () -> "2");
    }

    @BeforeAll
    void startServer() {
        wireMockServer = new WireMockServer(9561);
        wireMockServer.start();

        configureFor("localhost", 9561);

        stubFor(get(urlEqualTo("/api/v2/pokemon?limit=2"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
            {
              "results": [
                {"name": "bulbasaur", "url": "http://localhost:9561/api/v2/pokemon/1"},
                {"name": "ivysaur", "url": "http://localhost:9561/api/v2/pokemon/2"}
              ]
            }
        """)));

        stubFor(get(urlEqualTo("/api/v2/pokemon/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {"id":1,"name":"bulbasaur","weight":69,"height":7,"base_experience":64}
                        """)));

        stubFor(get(urlEqualTo("/api/v2/pokemon/2"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {"id":2,"name":"ivysaur","weight":130,"height":10,"base_experience":142}
                        """)));
    }

    @AfterAll
    void stopServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    void shouldReturnHeaviestPokemons() {
        var client = webClientBuilder.build();
        var response = client.get()
                .uri("http://localhost:" + port + "/api/pokemons/heaviest")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        assertThat(response).contains("ivysaur");
    }
}
