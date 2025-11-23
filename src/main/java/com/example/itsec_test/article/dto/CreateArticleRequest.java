package com.example.itsec_test.article.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateArticleRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private boolean isPublished = false;
}
