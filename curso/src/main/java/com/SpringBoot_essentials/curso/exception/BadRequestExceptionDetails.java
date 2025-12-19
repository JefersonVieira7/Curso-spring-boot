package com.SpringBoot_essentials.curso.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BadRequestExceptionDetails {
    private String title;
    private int status;
    private String detalis;
    private String developerMessage;
    private LocalDateTime timestamp;
}
