package com.example.itsec_test.article.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateArticleRequest {
    @NotNull
    private String title;

    @NotNull
    private String content;

    private boolean isPublished = false;
}
