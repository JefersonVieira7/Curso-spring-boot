package com.SpringBoot_essentials.curso.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnimePostRequestBody {
    @NotBlank(message = "The anime name cannot be empty!")
    private String name;
}
