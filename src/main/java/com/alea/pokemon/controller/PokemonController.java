package com.alea.pokemon.controller;

import com.alea.pokemon.model.PokemonDto;
import com.alea.pokemon.service.PokemonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pokemons")
public class PokemonController {

    private final PokemonService service;

    public PokemonController(PokemonService service) {
        this.service = service;
    }

    @GetMapping("/heaviest")
    public List<PokemonDto> heaviest(@RequestParam(name= "limit", defaultValue = "5") int limit) {
        return service.topHeaviest(limit);
    }

    @GetMapping("/tallest")
    public List<PokemonDto> tallest(@RequestParam(name= "limit", defaultValue = "5") int limit) {
        return service.topTallest(limit);
    }

    @GetMapping("/experienced")
    public List<PokemonDto> experienced(@RequestParam(name= "limit", defaultValue = "5") int limit) {
        return service.topByBaseExperience(limit);
    }
}
