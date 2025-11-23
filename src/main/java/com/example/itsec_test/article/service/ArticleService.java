package com.example.itsec_test.article.service;

import com.example.itsec_test.article.model.Article;
import com.example.itsec_test.article.repository.ArticleRepository;
import com.example.itsec_test.article.dto.ArticleResponse;
import com.example.itsec_test.article.dto.AuthorResponse;
import com.example.itsec_test.article.dto.CreateArticleRequest;
import com.example.itsec_test.auth.constant.UserRole;
import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.common.exception.BadRequestException;
import com.example.itsec_test.common.exception.ForbiddenRequestException;

import java.util.Optional;

import org.springframework.lang.NonNull;
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

        return mapToResponse(savedArticle);
    }

    public ArticleResponse getArticleById(@NonNull Integer id, User user) {
        Optional<Article> articleOpt = articleRepository.findById(id);
        if (articleOpt.isEmpty()) {
            throw new BadRequestException("Article not found");
        }

        Article article = articleOpt.get();
        if (user.getRole().equals(UserRole.VIEWER) && !article.isPublished()) {
            throw new ForbiddenRequestException("Article not published");
        }

        return mapToResponse(article);
    }

    private ArticleResponse mapToResponse(Article article) {
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .isPublished(article.isPublished())
                .author(AuthorResponse.builder()
                        .id(article.getAuthor().getId())
                        .name(article.getAuthor().getFullName())
                        .build())
                .build();
    }

}
