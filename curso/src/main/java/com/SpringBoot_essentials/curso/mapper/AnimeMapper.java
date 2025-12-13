package com.SpringBoot_essentials.curso.mapper;

import com.SpringBoot_essentials.curso.domain.Anime;
import com.SpringBoot_essentials.curso.requests.AnimePostRequestBody;
import com.SpringBoot_essentials.curso.requests.AnimePutResquestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class AnimeMapper {
    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);
    public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);
    public abstract Anime toAnime(AnimePutResquestBody animePostRequestBody);
}
