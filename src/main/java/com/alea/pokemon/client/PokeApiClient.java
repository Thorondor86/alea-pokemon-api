package com.alea.pokemon.client;

import com.alea.pokemon.model.PokemonDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PokeApiClient {

    private final WebClient webClient;
    private final String baseUrl;
    private final int fetchLimit;

    public PokeApiClient(WebClient.Builder builder,
                         @Value("${pokeapi.base-url}") String baseUrl,
                         @Value("${pokeapi.fetch-limit}") int fetchLimit) {
        this.webClient = builder.baseUrl(baseUrl).build();
        this.baseUrl = baseUrl;
        this.fetchLimit = fetchLimit;
    }

    public List<PokemonDto> fetchPokemons() {
        // 1) fetch list of pokemon references
        PokemonListResponse listResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/pokemon")
                        .queryParam("limit", fetchLimit)
                        .build())
                .retrieve()
                .bodyToMono(PokemonListResponse.class)
                .block();

        if (listResponse == null || listResponse.results == null) return List.of();

        // 2) fetch details in parallel (reactive)
        Flux<PokemonDetailResponse> detailsFlux = Flux.fromIterable(listResponse.results)
                .flatMap(r -> webClient.get()
                        .uri(r.url.replace(baseUrl, "")) // use relative path
                        .retrieve()
                        .bodyToMono(PokemonDetailResponse.class)
                        .onErrorResume(e -> Mono.empty()));

        List<PokemonDto> dtos = detailsFlux.map(d -> new PokemonDto(
                d.id,
                d.name,
                d.weight,
                d.height,
                d.base_experience,
                baseUrl + "/pokemon/" + d.id
        )).collectList().block();

        return dtos == null ? List.of() : dtos;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PokemonListResponse {
        public int count;
        public String next;
        public String previous;
        public List<Result> results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        public String name;
        public String url;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PokemonDetailResponse {
        public int id;
        public String name;
        public int weight;
        public int height;
        @JsonProperty("base_experience")
        public int base_experience;
    }
}
