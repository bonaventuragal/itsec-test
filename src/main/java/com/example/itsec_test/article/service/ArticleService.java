package com.example.itsec_test.article.service;

import com.example.itsec_test.article.model.Article;
import com.example.itsec_test.article.repository.ArticleRepository;
import com.example.itsec_test.article.dto.ArticleResponse;
import com.example.itsec_test.article.dto.AuthorResponse;
import com.example.itsec_test.article.dto.CreateArticleRequest;
import com.example.itsec_test.auth.model.User;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
	private final ArticleRepository articleRepository;

	public ArticleService(ArticleRepository articleRepository) {
		this.articleRepository = articleRepository;
	}

	public ArticleResponse createArticle(CreateArticleRequest request, User user) {
		Article article = new Article();
		article.setTitle(request.getTitle());
		article.setContent(request.getContent());
		article.setAuthor(user);
        article.setPublished(request.isPublished());
		Article savedArticle = articleRepository.save(article);

        ArticleResponse response = ArticleResponse.builder()
                .id(savedArticle.getId())
                .title(savedArticle.getTitle())
                .content(savedArticle.getContent())
                .isPublished(savedArticle.isPublished())
                .author(AuthorResponse.builder()
                        .id(user.getId())
                        .name(user.getFullName())
                        .build())
                .build();

        return response;
	}
    
}
