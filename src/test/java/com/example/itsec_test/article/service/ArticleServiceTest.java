package com.example.itsec_test.article.service;

import com.example.itsec_test.article.model.Article;
import com.example.itsec_test.article.repository.ArticleRepository;
import com.example.itsec_test.article.dto.CreateArticleRequest;
import com.example.itsec_test.article.dto.UpdateArticleRequest;
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

        when(articleRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> articleService.getArticleById(2, user));
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

    @SuppressWarnings("null")
    @Test
    void testUpdateArticleSuccessByAuthor() {
        User author = new User();
        author.setId(1);
        author.setRole(UserRole.CONTRIBUTOR);

        Article article = new Article();
        article.setId(1);
        article.setTitle("Old Title");
        article.setContent("Old Content");
        article.setAuthor(author);
        article.setPublished(false);

        UpdateArticleRequest request = new UpdateArticleRequest();
        request.setId(1);
        request.setTitle("New Title");
        request.setContent("New Content");
        request.setPublished(true);

        when(articleRepository.findById(1)).thenReturn(Optional.of(article));
        when(articleRepository.save(any(Article.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ArticleResponse response = articleService.updateArticle(request, author);

        assertNotNull(response);
        assertEquals(request.getTitle(), response.getTitle());
        assertEquals(request.getContent(), response.getContent());
        assertTrue(response.isPublished());
        verify(articleRepository, times(1)).findById(1);
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void testUpdateArticleForbiddenForViewer() {
        User viewer = new User();
        viewer.setId(1);
        viewer.setRole(UserRole.VIEWER);

        UpdateArticleRequest request = new UpdateArticleRequest();
        request.setId(1);
        request.setTitle("New Title");
        request.setContent("New Content");
        request.setPublished(true);

        assertThrows(ForbiddenRequestException.class,
                () -> articleService.updateArticle(request, viewer));
    }

    @Test
    void testUpdateArticleForbiddenForNonAuthor() {
        User author = new User();
        author.setId(1);
        author.setRole(UserRole.CONTRIBUTOR);

        User otherUser = new User();
        otherUser.setId(2);
        otherUser.setRole(UserRole.CONTRIBUTOR);

        Article article = new Article();
        article.setId(1);
        article.setTitle("Old Title");
        article.setContent("Old Content");
        article.setAuthor(author);
        article.setPublished(false);

        UpdateArticleRequest request = new UpdateArticleRequest();
        request.setId(1);
        request.setTitle("New Title");
        request.setContent("New Content");
        request.setPublished(true);

        when(articleRepository.findById(1)).thenReturn(Optional.of(article));

        assertThrows(ForbiddenRequestException.class,
                () -> articleService.updateArticle(request, otherUser));
        verify(articleRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateArticleNotFound() {
        User user = new User();
        user.setId(1);
        user.setRole(UserRole.SUPER_ADMIN);

        UpdateArticleRequest request = new UpdateArticleRequest();
        request.setId(2);
        request.setTitle("Title");
        request.setContent("Content");
        request.setPublished(true);

        when(articleRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> articleService.updateArticle(request, user));
        verify(articleRepository, times(1)).findById(2);
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteArticleSuccessByAuthor() {
        User author = new User();
        author.setId(1);
        author.setRole(UserRole.EDITOR);

        Article article = new Article();
        article.setId(1);
        article.setAuthor(author);

        when(articleRepository.findById(1)).thenReturn(Optional.of(article));
        when(articleRepository.save(any(Article.class))).thenReturn(article);

        articleService.deleteArticle(1, author);

        verify(articleRepository, times(1)).findById(1);
        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    void testDeleteArticleForbiddenForViewer() {
        User viewer = new User();
        viewer.setId(1);
        viewer.setRole(UserRole.VIEWER);

        assertThrows(ForbiddenRequestException.class,
                () -> articleService.deleteArticle(1, viewer));
    }

    @Test
    void testDeleteArticleForbiddenForContributor() {
        User contributor = new User();
        contributor.setId(1);
        contributor.setRole(UserRole.CONTRIBUTOR);

        assertThrows(ForbiddenRequestException.class,
                () -> articleService.deleteArticle(1, contributor));
    }

    @Test
    void testDeleteArticleForbiddenForNonAuthor() {
        User author = new User();
        author.setId(1);
        author.setRole(UserRole.EDITOR);

        User otherUser = new User();
        otherUser.setId(2);
        otherUser.setRole(UserRole.EDITOR);

        Article article = new Article();
        article.setId(1);
        article.setAuthor(author);

        when(articleRepository.findById(1)).thenReturn(Optional.of(article));

        assertThrows(ForbiddenRequestException.class,
                () -> articleService.deleteArticle(1, otherUser));
        verify(articleRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteArticleNotFound() {
        User user = new User();
        user.setId(1);
        user.setRole(UserRole.EDITOR);

        when(articleRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> articleService.deleteArticle(2, user));
        verify(articleRepository, times(1)).findById(2);
    }
}
