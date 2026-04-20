package com.SpringBoot_essentials.curso.integration;
import com.SpringBoot_essentials.curso.domain.Anime;
import com.SpringBoot_essentials.curso.repository.AnimeRepository;
import com.SpringBoot_essentials.curso.util.AnimeCreator;
import com.SpringBoot_essentials.curso.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
class AnimeControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @LocalServerPort
    private int port;
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("list returns list of animes inside page object when successfull")
    void list_ReturnsListOfAnimesInsidePageObject_whenSuccessful(){

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
}
