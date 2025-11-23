package com.example.itsec_test.article.controller;

import com.example.itsec_test.article.dto.ArticleResponse;
import com.example.itsec_test.article.dto.CreateArticleRequest;
import com.example.itsec_test.article.dto.UpdateArticleRequest;
import com.example.itsec_test.article.service.ArticleService;
import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.common.exception.ForbiddenRequestException;
import com.example.itsec_test.common.dto.MessageResponse;
import com.example.itsec_test.common.dto.PaginationRequest;
import com.example.itsec_test.common.dto.PaginationResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            throw new ForbiddenRequestException("User not found in request");
        }
        return articleService.createArticle(request, user);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ArticleResponse getArticleById(@NonNull @PathVariable Integer id, HttpServletRequest httpRequest) {
        User user = (User) httpRequest.getAttribute("user");
        if (user == null) {
            throw new ForbiddenRequestException("User not found in request");
        }
        return articleService.getArticleById(id, user);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public PaginationResponse<ArticleResponse> getArticles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {
        User user = (User) httpRequest.getAttribute("user");
        if (user == null) {
            throw new ForbiddenRequestException("User not found in request");
        }

        PaginationRequest paginationRequest = PaginationRequest.builder()
                .page(page)
                .size(size)
                .build();
        return articleService.getArticles(paginationRequest, user);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'EDITOR', 'CONTRIBUTOR')")
    public ArticleResponse updateArticle(@Valid @RequestBody UpdateArticleRequest request,
            HttpServletRequest httpRequest) {
        User user = (User) httpRequest.getAttribute("user");
        if (user == null) {
            throw new ForbiddenRequestException("User not found in request");
        }

        return articleService.updateArticle(request, user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'EDITOR', 'CONTRIBUTOR')")
    public MessageResponse deleteArticle(@NonNull @PathVariable Integer id, HttpServletRequest httpRequest) {
        User user = (User) httpRequest.getAttribute("user");
        if (user == null) {
            throw new ForbiddenRequestException("User not found in request");
        }
        articleService.deleteArticle(id, user);

        return MessageResponse.builder()
                .message("Article deleted successfully")
                .build();
    }
}
