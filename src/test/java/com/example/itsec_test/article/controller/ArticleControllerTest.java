package com.example.itsec_test.article.controller;

import com.example.itsec_test.article.dto.CreateArticleRequest;
import com.example.itsec_test.article.dto.ArticleResponse;
import com.example.itsec_test.auth.model.User;

import jakarta.servlet.http.HttpServletRequest;

import com.example.itsec_test.article.service.ArticleService;
import com.example.itsec_test.auth.filter.JwtUserFilter;
import com.example.itsec_test.audit.filter.RequestLoggingFilter;
import com.example.itsec_test.common.dto.PaginationRequest;
import com.example.itsec_test.common.dto.PaginationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(ArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ArticleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RequestLoggingFilter requestLoggingFilter;

    @MockitoBean
    private JwtUserFilter jwtUserFilter;

    @MockitoBean
    private ArticleService articleService;

    @Test
    void testCreateArticle() throws Exception {
        User user = new User();
        user.setId(1);
        user.setFullName("User Name");

        ArticleResponse response = ArticleResponse.builder()
                .id(1)
                .title("Test Title")
                .content("Test Content")
                .isPublished(true)
                .author(null)
                .build();

        when(articleService.createArticle(any(CreateArticleRequest.class), any(User.class))).thenReturn(response);

        String json = "{\"title\":\"Test Title\",\"content\":\"Test Content\",\"published\":true}";

        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        when(httpRequest.getAttribute("user")).thenReturn(user);

        mockMvc.perform(post("/api/v1/articles")
                .requestAttr("user", user)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.content").value(response.getContent()))
                .andExpect(jsonPath("$.published").value(response.isPublished()));
    }

    @Test
    void testGetArticleById() throws Exception {
        User user = new User();
        user.setId(1);
        user.setFullName("User Name");

        ArticleResponse response = ArticleResponse.builder()
                .id(1)
                .title("Test Title")
                .content("Test Content")
                .isPublished(true)
                .author(null)
                .build();

        when(articleService.getArticleById(eq(1), any(User.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/articles/1")
                .requestAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.content").value(response.getContent()))
                .andExpect(jsonPath("$.published").value(response.isPublished()));
    }

    @Test
    void testGetArticles() throws Exception {
        User user = new User();
        user.setId(1);
        user.setFullName("User Name");

        ArticleResponse article1 = ArticleResponse.builder()
                .id(1)
                .title("Article 1")
                .content("Content 1")
                .isPublished(true)
                .author(null)
                .build();
        ArticleResponse article2 = ArticleResponse.builder()
                .id(2)
                .title("Article 2")
                .content("Content 2")
                .isPublished(true)
                .author(null)
                .build();

        PaginationResponse<ArticleResponse> response = PaginationResponse.<ArticleResponse>builder()
                .page(1)
                .size(10)
                .totalElements(2)
                .totalPages(1)
                .items(List.of(article1, article2))
                .build();

        when(articleService.getArticles(any(PaginationRequest.class), any(User.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/articles?page=1&size=10")
                .requestAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].id").value(article1.getId()))
                .andExpect(jsonPath("$.items[0].title").value(article1.getTitle()))
                .andExpect(jsonPath("$.items[1].id").value(article2.getId()))
                .andExpect(jsonPath("$.items[1].title").value(article2.getTitle()));
    }
}
