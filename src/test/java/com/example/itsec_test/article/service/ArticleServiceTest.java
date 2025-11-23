package com.example.itsec_test.article.service;

import com.example.itsec_test.article.model.Article;
import com.example.itsec_test.article.repository.ArticleRepository;
import com.example.itsec_test.article.dto.CreateArticleRequest;
import com.example.itsec_test.article.dto.ArticleResponse;
import com.example.itsec_test.auth.constant.UserRole;
import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.common.exception.BadRequestException;
import com.example.itsec_test.common.exception.ForbiddenRequestException;
import com.example.itsec_test.common.dto.PaginationRequest;
import com.example.itsec_test.common.dto.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

class ArticleServiceTest {
    private ArticleRepository articleRepository;
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        articleRepository = mock(ArticleRepository.class);
        articleService = new ArticleService(articleRepository);
    }

    @SuppressWarnings("null")
    @Test
    void testCreateArticle() {
        User user = new User();
        user.setId(1);
        user.setFullName("Author Name");

        Article article = new Article();
        article.setId(1);
        article.setTitle("Test Title");
        article.setContent("Test Content");
        article.setAuthor(user);
        article.setPublished(true);

        when(articleRepository.save(any(Article.class))).thenReturn(article);

        CreateArticleRequest request = new CreateArticleRequest();
        request.setTitle("Test Title");
        request.setContent("Test Content");
        request.setPublished(true);

        ArticleResponse response = articleService.createArticle(request, user);

        assertNotNull(response);
        assertEquals(article.getId(), response.getId());
        assertEquals(article.getTitle(), response.getTitle());
        assertEquals(article.getContent(), response.getContent());
        assertTrue(response.isPublished());
        assertNotNull(response.getAuthor());
        assertEquals(user.getId(), response.getAuthor().getId());
        assertEquals(user.getFullName(), response.getAuthor().getName());
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void testGetArticleByIdSuccess() {
        User user = new User();
        user.setId(1);
        user.setFullName("Author Name");
        user.setRole(UserRole.EDITOR);

        Article article = new Article();
        article.setId(1);
        article.setTitle("Test Title");
        article.setContent("Test Content");
        article.setAuthor(user);
        article.setPublished(true);

        when(articleRepository.findById(1)).thenReturn(Optional.of(article));

        ArticleResponse response = articleService.getArticleById(1, user);

        assertNotNull(response);
        assertEquals(article.getId(), response.getId());
        assertEquals(article.getTitle(), response.getTitle());
        assertEquals(article.getContent(), response.getContent());
        assertTrue(response.isPublished());
        assertNotNull(response.getAuthor());
        assertEquals(user.getId(), response.getAuthor().getId());
        assertEquals(user.getFullName(), response.getAuthor().getName());
        verify(articleRepository, times(1)).findById(1);
    }

    @Test
    void testGetArticleByIdNotFound() {
        User user = new User();
        user.setId(1);
        user.setRole(UserRole.EDITOR);

        when(articleRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> articleService.getArticleById(99, user));
    }

    @Test
    void testGetArticleByIdViewerForbidden() {
        User user = new User();
        user.setId(1);
        user.setRole(UserRole.VIEWER);

        Article article = new Article();
        article.setId(1);
        article.setTitle("Test Title");
        article.setContent("Test Content");
        article.setAuthor(user);
        article.setPublished(false);

        when(articleRepository.findById(1)).thenReturn(Optional.of(article));

        assertThrows(ForbiddenRequestException.class,
                () -> articleService.getArticleById(1, user));
    }

    @SuppressWarnings("null")
    @Test
    void testGetAllArticles() {
        User user = new User();
        user.setId(1);
        user.setRole(UserRole.EDITOR);

        Article publishedArticle = new Article();
        publishedArticle.setId(1);
        publishedArticle.setTitle("Published");
        publishedArticle.setContent("Content");
        publishedArticle.setAuthor(user);
        publishedArticle.setPublished(true);

        Article unpublishedArticle = new Article();
        unpublishedArticle.setId(2);
        unpublishedArticle.setTitle("Unpublished");
        unpublishedArticle.setContent("Content");
        unpublishedArticle.setAuthor(user);
        unpublishedArticle.setPublished(false);

        Page<Article> page = new PageImpl<>(List.of(publishedArticle, unpublishedArticle));
        when(articleRepository.findAll(any(Pageable.class))).thenReturn(page);

        PaginationRequest paginationRequest = PaginationRequest.builder()
                .page(1)
                .size(10)
                .build();
        PaginationResponse<ArticleResponse> response = articleService.getArticles(paginationRequest, user);

        assertNotNull(response);
        assertEquals(2, response.getItems().size());
        verify(articleRepository, times(1)).findAll(any(Pageable.class));
    }

    @SuppressWarnings("null")
    @Test
    void testGetArticlesViewerOnlyPublished() {
        User user = new User();
        user.setId(1);
        user.setRole(UserRole.VIEWER);

        Article publishedArticle = new Article();
        publishedArticle.setId(1);
        publishedArticle.setTitle("Published");
        publishedArticle.setContent("Content");
        publishedArticle.setAuthor(user);
        publishedArticle.setPublished(true);

        Article unpublishedArticle = new Article();
        unpublishedArticle.setId(2);
        unpublishedArticle.setTitle("Unpublished");
        unpublishedArticle.setContent("Content");
        unpublishedArticle.setAuthor(user);
        unpublishedArticle.setPublished(false);

        Page<Article> page = new PageImpl<>(List.of(publishedArticle));
        when(articleRepository.findByIsPublished(eq(true), any(Pageable.class))).thenReturn(page);

        PaginationRequest paginationRequest = PaginationRequest.builder()
                .page(1)
                .size(10)
                .build();
        PaginationResponse<ArticleResponse> response = articleService.getArticles(paginationRequest, user);

        assertNotNull(response);
        assertEquals(1, response.getItems().size());
        assertEquals(true, response.getItems().get(0).isPublished());
        verify(articleRepository, times(1)).findByIsPublished(eq(true), any(Pageable.class));
    }
}
