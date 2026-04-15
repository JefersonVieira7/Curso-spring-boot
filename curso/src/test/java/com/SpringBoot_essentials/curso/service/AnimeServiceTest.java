package com.SpringBoot_essentials.curso.service;

import com.SpringBoot_essentials.curso.domain.Anime;
import com.SpringBoot_essentials.curso.exception.BadRequestException;
import com.SpringBoot_essentials.curso.repository.AnimeRepository;
import com.SpringBoot_essentials.curso.util.AnimeCreator;
import com.SpringBoot_essentials.curso.util.AnimePostRequestBodyCreator;
import com.SpringBoot_essentials.curso.util.AnimePutRequestBodyCreator;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)

class AnimeServiceTest {

    @InjectMocks
    private AnimeService animeService;
    @Mock
    private AnimeRepository animeRepositoryMock;

    @BeforeEach
    void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeCreator.createValidAnime()));
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))
                .thenReturn(animePage);

        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeCreator.createValidAnime()));

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeCreator.createValidAnime());


        BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    @DisplayName("listAll returns list of animes inside page object when successfull")
    void listAll_ReturnsListOfAnimesInsidePageObject_whenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();
        Page<Anime> animePage = animeService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAllNonPageable returns list of animes when successfull")
    void listAllNonPageable_ReturnsListOfAnimes_whenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animes = animeService.listAllNonPageable();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);


        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException returns anime when successfull")
    void findByIdOrThrowBadRequestException_ReturnsAnime_whenSuccessful(){
        Long expectedId = AnimeCreator.createValidAnime().getId();

        Anime anime = animeService.findByIdOrThrowBadRequestException(1);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when anime is not found")
    void findByIdOrThrowBadRequestException_throwsBadRequestException_whenAnimeIsNotFound(){
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(()-> animeService.findByIdOrThrowBadRequestException(1));

    }

    @Test
    @DisplayName("findByName returns a list of anime when successfull")
    void findByname_ReturnsListOfAnime_whenSuccessful(){
        String expectedName = AnimeCreator.createValidAnime().getName();

        List<Anime> animes = animeService.findByName("anime");

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);


        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findByName returns an empty list of anime when anime is not found")
    void findByname_ReturnsListOfAnime_whenAnimeIsNotFound(){
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());


        List<Anime> animes = animeService.findByName("anime");

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save returns anime when successfull")
    void save_ReturnsAnime_whenSuccessfull() throws InterruptedException {

        Anime anime = animeService.save(AnimePostRequestBodyCreator.creatAnimePostRequestBody());

        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeCreator.createValidAnime());
    }

    @Test
    @DisplayName("replace returns anime when successfull")
    void replace_UpdatesAnime_whenSuccessfull(){

        Assertions.assertThatCode(() -> animeService.replace(AnimePutRequestBodyCreator.creatAnimePutResquestBody()))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("delete returns anime when successfull")
    void delete_RemovesAnime_whenSuccessfull(){

        Assertions.assertThatCode(() -> animeService.delete(1L))
                .doesNotThrowAnyException();
    }
}
