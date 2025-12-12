package com.SpringBoot_essentials.curso.repository;

import com.SpringBoot_essentials.curso.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, Long> {

}
