package com.SpringBoot_essentials.curso.integration;

import com.SpringBoot_essentials.curso.domain.Anime;
import com.SpringBoot_essentials.curso.repository.AnimeRepository;
import com.SpringBoot_essentials.curso.requests.AnimePostRequestBody;
import com.SpringBoot_essentials.curso.requests.AnimePutResquestBody;
import com.SpringBoot_essentials.curso.util.AnimeCreator;
import com.SpringBoot_essentials.curso.util.AnimePostRequestBodyCreator;
import com.SpringBoot_essentials.curso.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class AnimeControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private AnimeRepository animeRepository;

    @BeforeEach
    void setUp(){
        animeRepository.deleteAll();
    }

    @Test
    @DisplayName("list returns list of animes inside page object when successfull")
    void list_ReturnsListOfAnimesInsidePageObject_whenSuccessful() {

        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        PageableResponse<Anime> animePage = testRestTemplate.exchange("/animes",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        Assertions.assertThat(animePage).isNotNull();

        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("listAll returns list of animes when successfull")
    void listAll_ReturnsListOfAnimes_whenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        List<Anime> animes = testRestTemplate.exchange("/animes/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("findById returns anime when successfull")
    void findById_ReturnsAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        Long expectedId = savedAnime.getId();

        Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);

        Assertions.assertThat(anime).isNotNull();

        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByName returns a list of anime when successfull")
    void findByname_ReturnsListOfAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        String expectedName = savedAnime.getName();

        String url = String.format("/animes/name/%s", expectedName);

        List<Anime> animes = testRestTemplate.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);


        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
   }

    @Test
    @DisplayName("findByName returns an empty list of anime when anime is not found")
    void findByname_ReturnsListOfAnime_whenAnimeIsNotFound(){

            List<Anime> animes = testRestTemplate.exchange("/animes/name/dragon Ball Z",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Anime>>() {
                    }).getBody();

            Assertions.assertThat(animes)
                    .isNotNull()
                    .isEmpty();
    }
//
    @Test
    @DisplayName("save returns anime when successfull")
    void save_ReturnsAnime_whenSuccessfull() throws InterruptedException {

        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.creatAnimePostRequestBody();
        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity(
                "/animes",
                animePostRequestBody,
                Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("replace returns anime when successfull")
    void replace_UpdatesAnime_whenSuccessfull() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        AnimePutResquestBody putRequestBody = AnimePutResquestBody.builder()
                .id(savedAnime.getId())
                .name("new name")
                .build();

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange(
                "/animes",
                HttpMethod.PUT,
                new HttpEntity<>(putRequestBody),
                Void.class
        );

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete returns anime when successfull")
    void delete_RemovesAnime_whenSuccessfull(){
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());


        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange(
                "/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class
                ,savedAnime.getId());


        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}
