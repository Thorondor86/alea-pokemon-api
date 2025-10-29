package com.alea.pokemon.controller;

import com.alea.pokemon.model.PokemonDto;
import com.alea.pokemon.service.PokemonService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@WebFluxTest(controllers = PokemonController.class)
public class PokemonControllerTest {

    @Autowired
    WebTestClient webClient;

    @MockBean
    PokemonService service;

    @Test
    void testHeaviestEndpoint() {
        Mockito.when(service.topHeaviest(5)).thenReturn(List.of(
                new PokemonDto(2,"b",30,10,20,""),
                new PokemonDto(3,"c",20,15,30,"")
        ));

        webClient.get().uri("/api/pokemons/heaviest")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PokemonDto.class)
                .hasSize(2);
    }

    @Test
    void testTallestEndpoint() {
        Mockito.when(service.topTallest(5)).thenReturn(List.of(
                new PokemonDto(2,"b",30,10,20,""),
                new PokemonDto(3,"c",20,15,30,"")
        ));

        webClient.get().uri("/api/pokemons/tallest")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PokemonDto.class)
                .hasSize(2);
    }

    @Test
    void testExperiencedEndpoint() {
        Mockito.when(service.topByBaseExperience(5)).thenReturn(List.of(
                new PokemonDto(2,"b",30,10,20,""),
                new PokemonDto(3,"c",20,15,30,"")
        ));

        webClient.get().uri("/api/pokemons/experienced")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PokemonDto.class)
                .hasSize(2);
    }
}
