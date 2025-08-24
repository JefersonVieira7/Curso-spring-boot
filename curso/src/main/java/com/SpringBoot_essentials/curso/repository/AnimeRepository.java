package com.SpringBoot_essentials.curso.repository;

import com.SpringBoot_essentials.curso.domain.Anime;

import java.util.List;

public interface AnimeRepository {
    List<Anime> listAll();
}
