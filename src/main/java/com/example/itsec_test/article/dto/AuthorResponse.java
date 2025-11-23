package com.example.itsec_test.article.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorResponse {
    private Integer id;
    private String name;
}
