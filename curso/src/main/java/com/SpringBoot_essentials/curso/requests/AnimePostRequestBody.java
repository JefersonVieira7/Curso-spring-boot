package com.SpringBoot_essentials.curso.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimePostRequestBody {
    @NotBlank(message = "The anime name cannot be empty!")
    private String name;
}
