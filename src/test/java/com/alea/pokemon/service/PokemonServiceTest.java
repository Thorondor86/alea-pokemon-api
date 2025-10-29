package com.alea.pokemon.service;

import com.alea.pokemon.client.PokeApiClient;
import com.alea.pokemon.model.PokemonDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PokemonServiceTest {

    @Test
    void testTopHeaviest() {
        PokeApiClient client = Mockito.mock(PokeApiClient.class);
        Mockito.when(client.fetchPokemons()).thenReturn(List.of(
                new PokemonDto(1,"a",10,5,50,""),
                new PokemonDto(2,"b",30,10,20,""),
                new PokemonDto(3,"c",20,15,30,"")
        ));
        PokemonService service = new PokemonService(client);
        List<PokemonDto> top = service.topHeaviest(2);
        assertThat(top).hasSize(2);
        assertThat(top.get(0).id()).isEqualTo(2);
        assertThat(top.get(1).id()).isEqualTo(3);
    }

    @Test
    void testTopTallest() {
        PokeApiClient client = Mockito.mock(PokeApiClient.class);
        Mockito.when(client.fetchPokemons()).thenReturn(List.of(
                new PokemonDto(1,"a",10,5,50,""),
                new PokemonDto(2,"b",30,10,20,""),
                new PokemonDto(3,"c",20,15,30,"")
        ));
        PokemonService service = new PokemonService(client);
        List<PokemonDto> top = service.topTallest(1);
        assertThat(top).hasSize(1);
        assertThat(top.get(0).id()).isEqualTo(3);
    }

    @Test
    void testTopExperienced() {
        PokeApiClient client = Mockito.mock(PokeApiClient.class);
        Mockito.when(client.fetchPokemons()).thenReturn(List.of(
                new PokemonDto(1,"a",10,5,50,""),
                new PokemonDto(2,"b",30,10,20,""),
                new PokemonDto(3,"c",20,15,30,"")
        ));
        PokemonService service = new PokemonService(client);
        List<PokemonDto> top = service.topByBaseExperience(3);
        assertThat(top.get(0).id()).isEqualTo(1);
    }
}
