package com.example.itsec_test.article.model;

import org.hibernate.annotations.SQLRestriction;

import com.example.itsec_test.auth.model.User;
import com.example.itsec_test.common.model.BaseMutableModel;

import lombok.*;
import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"author"})
@Entity
@Table(name = "article")
@SQLRestriction("deleted_at IS NULL")
public class Article extends BaseMutableModel {
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isPublished = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
}
