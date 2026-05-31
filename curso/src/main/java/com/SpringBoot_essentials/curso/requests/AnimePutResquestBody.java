package com.SpringBoot_essentials.curso.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimePutResquestBody {
    private Long id;
    private String name;
}
