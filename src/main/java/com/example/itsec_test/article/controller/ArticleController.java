package com.example.itsec_test.article.controller;

import com.example.itsec_test.article.dto.ArticleResponse;
import com.example.itsec_test.article.dto.CreateArticleRequest;
import com.example.itsec_test.article.service.ArticleService;
import com.example.itsec_test.auth.model.User;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/articles")
@Tag(name = "Article")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'EDITOR', 'CONTRIBUTOR')")
    public ArticleResponse createArticle(@Valid @RequestBody CreateArticleRequest request,
            HttpServletRequest httpRequest) {
        User user = (User) httpRequest.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("User not found in request");
        }
        return articleService.createArticle(request, user);
    }
}
