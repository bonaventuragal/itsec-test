package com.example.itsec_test.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.itsec_test.article.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    
}
