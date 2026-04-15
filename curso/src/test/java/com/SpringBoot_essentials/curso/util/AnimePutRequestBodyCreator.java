package com.SpringBoot_essentials.curso.util;
import com.SpringBoot_essentials.curso.requests.AnimePutResquestBody;

public class AnimePutRequestBodyCreator {
    public static AnimePutResquestBody creatAnimePutResquestBody(){
        return AnimePutResquestBody.builder()
                .id(AnimeCreator.createValidUpdateAnime().getId())
                .name(AnimeCreator.createValidUpdateAnime().getName())
                .build();
    }
}
