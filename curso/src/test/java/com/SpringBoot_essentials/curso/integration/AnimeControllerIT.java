package com.SpringBoot_essentials.curso.integration;

import com.SpringBoot_essentials.curso.domain.Anime;
import com.SpringBoot_essentials.curso.repository.AnimeRepository;
import com.SpringBoot_essentials.curso.requests.AnimePostRequestBody;
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

    @Autowired
    private AnimeRepository animeRepository;

    private TestRestTemplate adminRestTemplate() {
        return testRestTemplate.withBasicAuth("jeferson", "academy");
    }

    private TestRestTemplate userRestTemplate() {
        return testRestTemplate.withBasicAuth("comum", "comum");
    }

    @BeforeEach
    void setUp() {
        animeRepository.deleteAll();
    }

    @Test
    @DisplayName("list retorna página de animes quando bem-sucedido")
    void list_ReturnsListOfAnimesInsidePageObject_whenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        PageableResponse<Anime> animePage = userRestTemplate().exchange(
                "/animes",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {}
        ).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName())
                .isEqualTo(savedAnime.getName());
    }

    @Test
    @DisplayName("listAll retorna lista de animes quando bem-sucedido")
    void listAll_ReturnsListOfAnimes_whenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        List<Anime> animes = userRestTemplate().exchange(
                "/animes/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {}
        ).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animes.get(0).getName())
                .isEqualTo(savedAnime.getName());
    }

    @Test
    @DisplayName("findById retorna anime quando bem-sucedido")
    void findById_ReturnsAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        Anime anime = userRestTemplate()
                .getForObject("/animes/{id}", Anime.class, savedAnime.getId());

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(savedAnime.getId());
    }

    @Test
    @DisplayName("findByName retorna lista de animes quando bem-sucedido")
    void findByName_ReturnsListOfAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String url = String.format("/animes/name/%s", savedAnime.getName());

        List<Anime> animes = userRestTemplate().exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {}
        ).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    @DisplayName("save retorna anime quando bem-sucedido")
    void save_ReturnsAnime_whenSuccessful() {
        AnimePostRequestBody postRequestBody = AnimePostRequestBodyCreator.creatAnimePostRequestBody();

        ResponseEntity<Anime> response = adminRestTemplate()
                .postForEntity("/animes", postRequestBody, Anime.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("save retorna 403 quando usuário não tem role ADMIN")
    void save_Returns403_whenUserIsNotAdmin() {
        AnimePostRequestBody postRequestBody = AnimePostRequestBodyCreator.creatAnimePostRequestBody();
        ResponseEntity<Anime> response = userRestTemplate()
                .postForEntity("/animes", postRequestBody, Anime.class);

        Assertions.assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("delete remove anime quando bem-sucedido")
    void delete_RemovesAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        ResponseEntity<Void> response = adminRestTemplate().exchange(
                "/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedAnime.getId()
        );

        Assertions.assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete retorna 403 quando usuário não tem role ADMIN")
    void delete_Returns403_whenUserIsNotAdmin() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());

        ResponseEntity<Void> response = userRestTemplate().exchange(
                "/animes/{id}",
                HttpMethod.DELETE,
                null,
                Void.class,
                savedAnime.getId()
        );

        Assertions.assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("replace atualiza anime quando bem-sucedido")
    void replace_UpdatesAnime_whenSuccessful() {
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        savedAnime.setName("novo nome");
        ResponseEntity<Void> response = adminRestTemplate().exchange(
                "/animes",
                HttpMethod.PUT,
                new HttpEntity<>(savedAnime),
                Void.class
        );

        Assertions.assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("qualquer endpoint retorna 401 quando sem credenciais")
    void anyEndpoint_Returns401_whenNoCredentials() {
        ResponseEntity<Void> response = testRestTemplate.exchange(
                "/animes",
                HttpMethod.GET,
                null,
                Void.class
        );

        Assertions.assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}