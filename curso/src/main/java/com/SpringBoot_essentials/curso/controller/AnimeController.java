package com.SpringBoot_essentials.curso.controller;

import com.SpringBoot_essentials.curso.domain.Anime;
import com.SpringBoot_essentials.curso.requests.AnimePostRequestBody;
import com.SpringBoot_essentials.curso.requests.AnimePutResquestBody;
import com.SpringBoot_essentials.curso.service.AnimeService;
import com.SpringBoot_essentials.curso.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("animes")
@Log4j2
@RequiredArgsConstructor
public class AnimeController {
    private final DateUtil dateUtil;
    private final AnimeService animeService;


    @GetMapping
    public ResponseEntity<List<Anime>> list() {
        log.info(dateUtil.formatLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return ResponseEntity.ok(animeService.listAll());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id) {
        return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }

    @GetMapping(path = "/{name}")
    public ResponseEntity<List<Anime>> findById(@RequestParam String name) {
        return ResponseEntity.ok(animeService.findbyName(name));
    }

    @PostMapping
    public ResponseEntity<Anime> save(@RequestBody AnimePostRequestBody animePostRequestBody) throws InterruptedException {
        return new ResponseEntity<>(animeService.save(animePostRequestBody), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> replace(@RequestBody AnimePutResquestBody animePutResquestBody) {
        animeService.replace(animePutResquestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}