package com.example.itsec_test.article.service;

import com.example.itsec_test.article.model.Article;
import com.example.itsec_test.article.repository.ArticleRepository;
import com.example.itsec_test.article.dto.CreateArticleRequest;
import com.example.itsec_test.article.dto.ArticleResponse;
import com.example.itsec_test.auth.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
}
