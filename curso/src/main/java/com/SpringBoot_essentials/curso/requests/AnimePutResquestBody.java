package com.SpringBoot_essentials.curso.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnimePutResquestBody {
    private Long id;
    private String name;
}
