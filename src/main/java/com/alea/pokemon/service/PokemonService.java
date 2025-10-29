package com.alea.pokemon.service;

import com.alea.pokemon.client.PokeApiClient;
import com.alea.pokemon.model.PokemonDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PokemonService {

    private final PokeApiClient client;

    public PokemonService(PokeApiClient client) {
        this.client = client;
    }

    @Cacheable("pokemons")
    public List<PokemonDto> fetchPokemons() {
        return client.fetchPokemons();
    }

    public List<PokemonDto> topHeaviest(int topN) {
        return fetchPokemons().stream()
                .sorted(Comparator.comparingInt(PokemonDto::weight).reversed()
                        .thenComparingInt(PokemonDto::id))
                .limit(topN)
                .collect(Collectors.toList());
    }

    public List<PokemonDto> topTallest(int topN) {
        return fetchPokemons().stream()
                .sorted(Comparator.comparingInt(PokemonDto::height).reversed()
                        .thenComparingInt(PokemonDto::id))
                .limit(topN)
                .collect(Collectors.toList());
    }

    public List<PokemonDto> topByBaseExperience(int topN) {
        return fetchPokemons().stream()
                .sorted(Comparator.comparingInt(PokemonDto::baseExperience).reversed()
                        .thenComparingInt(PokemonDto::id))
                .limit(topN)
                .collect(Collectors.toList());
    }
}
