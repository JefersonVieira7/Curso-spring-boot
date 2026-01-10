package com.SpringBoot_essentials.curso.service;

import com.SpringBoot_essentials.curso.domain.Anime;
import com.SpringBoot_essentials.curso.exception.BadRequestException;
import com.SpringBoot_essentials.curso.mapper.AnimeMapper;
import com.SpringBoot_essentials.curso.repository.AnimeRepository;
import com.SpringBoot_essentials.curso.requests.AnimePostRequestBody;
import com.SpringBoot_essentials.curso.requests.AnimePutResquestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeRepository animeRepository;

    public List<Anime> listAll() {
        return animeRepository.findAll();
    }

    public List<Anime> findbyName(String name) {
        return animeRepository.findByName(name);
    }

    public Anime findByIdOrThrowBadRequestException(long id) {
        return animeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Anime not Found"));
    }

    public Anime save(AnimePostRequestBody animePostRequestBody) {
        return animeRepository.save(AnimeMapper.INSTANCE.toAnime(animePostRequestBody));
    }

    public void delete(long id) {
        animeRepository.delete(findByIdOrThrowBadRequestException(id));
    }

    public void replace(AnimePutResquestBody animePutResquestBody) {
        Anime savedAnime = findByIdOrThrowBadRequestException(animePutResquestBody.getId());
        Anime anime = AnimeMapper.INSTANCE.toAnime(animePutResquestBody);
        anime.setId(savedAnime.getId());
        animeRepository.save(anime);
    }
}
