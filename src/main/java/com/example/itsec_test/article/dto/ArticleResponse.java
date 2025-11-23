package com.example.itsec_test.article.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleResponse {
    private Integer id;
    private String title;
    private String content;
    private boolean isPublished;
    private AuthorResponse author;
}