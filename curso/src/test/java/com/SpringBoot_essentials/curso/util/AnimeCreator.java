package com.SpringBoot_essentials.curso.util;

import com.SpringBoot_essentials.curso.domain.Anime;
import com.SpringBoot_essentials.curso.service.AnimeService;

public class AnimeCreator {
    public static Anime createAnimeToBeSaved(){
        return Anime.builder()
                .name("Solo Leveling")
                .build();
    }

    public static Anime createValidAnime(){
        return Anime.builder()
                .name("Solo Leveling")
                .id(1L)
                .build();
    }

    public static Anime createValidUpdateAnime(){
        return Anime.builder()
                .name("Solo Leveling 2")
                .id(1L)
                .build();
    }
}
