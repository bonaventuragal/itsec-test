package com.example.itsec_test.article.controller;

import com.example.itsec_test.article.dto.CreateArticleRequest;
import com.example.itsec_test.article.dto.ArticleResponse;
import com.example.itsec_test.auth.model.User;

import jakarta.servlet.http.HttpServletRequest;

import com.example.itsec_test.article.service.ArticleService;
import com.example.itsec_test.auth.filter.JwtUserFilter;
import com.example.itsec_test.audit.filter.RequestLoggingFilter;
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
        user.setFullName("Author Name");

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
        user.setFullName("Viewer Name");

        ArticleResponse response = ArticleResponse.builder()
                .id(2)
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
}
