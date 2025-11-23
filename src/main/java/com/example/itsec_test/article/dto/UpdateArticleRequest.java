package com.example.itsec_test.article.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateArticleRequest {
    private Integer id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private boolean isPublished = false;
}
