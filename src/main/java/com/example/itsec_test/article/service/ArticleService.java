package com.example.itsec_test.article.service;

import com.example.itsec_test.article.model.Article;
import com.example.itsec_test.article.repository.ArticleRepository;
import com.example.itsec_test.article.dto.ArticleResponse;
import com.example.itsec_test.article.dto.AuthorResponse;
import com.example.itsec_test.article.dto.CreateArticleRequest;
import com.example.itsec_test.article.dto.UpdateArticleRequest;
import com.example.itsec_test.auth.constant.UserRole;
import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.common.exception.BadRequestException;
import com.example.itsec_test.common.exception.ForbiddenRequestException;
import com.example.itsec_test.common.dto.PaginationRequest;
import com.example.itsec_test.common.dto.PaginationResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public ArticleResponse getArticleById(@NonNull Integer id, User user) {
        Optional<Article> articleOpt = this.articleRepository.findById(id);
        if (articleOpt.isEmpty()) {
            throw new BadRequestException("Article not found");
        }

        Article article = articleOpt.get();
        if (user.getRole().equals(UserRole.VIEWER) && !article.isPublished()) {
            throw new ForbiddenRequestException("Article not published");
        }

        return mapToResponse(article);
    }

    public PaginationResponse<ArticleResponse> getArticles(PaginationRequest paginationRequest, User user) {
        Pageable pageable = PageRequest.of(
                Math.max(paginationRequest.getPage() - 1, 0),
                Math.max(paginationRequest.getSize(), 1));

        Page<Article> page;
        if (user.getRole().equals(UserRole.VIEWER)) {
            page = this.articleRepository.findByIsPublished(true, pageable);
        } else {
            page = this.articleRepository.findAll(pageable);
        }

        return PaginationResponse.<ArticleResponse>builder()
                .page(paginationRequest.getPage())
                .size(paginationRequest.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .items(page.getContent().stream().map(this::mapToResponse).toList())
                .build();
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

    @SuppressWarnings("null")
    public ArticleResponse updateArticle(UpdateArticleRequest request, User user) {
        if (user.getRole().equals(UserRole.VIEWER)) {
            throw new ForbiddenRequestException("You do not have permission to update articles");
        }

        Optional<Article> articleOpt = this.articleRepository.findById(request.getId());
        if (articleOpt.isEmpty()) {
            throw new BadRequestException("Article not found");
        }

        Article article = articleOpt.get();
        User author = article.getAuthor();

        if (!user.getRole().equals(UserRole.SUPER_ADMIN)) {
            if (!author.getId().equals(user.getId())) {
                throw new ForbiddenRequestException("You do not have permission to update this article");
            }
        }

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setPublished(request.isPublished());
        Article updatedArticle = this.articleRepository.save(article);

        return mapToResponse(updatedArticle);
    }

    public void deleteArticle(@NonNull Integer id, User user) {
        if (user.getRole().equals(UserRole.VIEWER) || user.getRole().equals(UserRole.CONTRIBUTOR)) {
            throw new ForbiddenRequestException("You do not have permission to delete articles");
        }

        Optional<Article> articleOpt = this.articleRepository.findById(id);
        if (articleOpt.isEmpty()) {
            throw new BadRequestException("Article not found");
        }

        Article article = articleOpt.get();
        User author = article.getAuthor();

        if (!user.getRole().equals(UserRole.SUPER_ADMIN)) {
            if (!author.getId().equals(user.getId())) {
                throw new ForbiddenRequestException("You do not have permission to delete this article");
            }
        }

        article.setDeletedAt(LocalDateTime.now());
        this.articleRepository.save(article);
    }

    public ArticleResponse createArticle(CreateArticleRequest request, User user) {
        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setAuthor(user);
        article.setPublished(request.isPublished());
        Article savedArticle = this.articleRepository.save(article);

        return mapToResponse(savedArticle);
    }
}
