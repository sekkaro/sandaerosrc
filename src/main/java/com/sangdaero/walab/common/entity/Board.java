package com.sangdaero.walab.common.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Board extends TimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 255, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
    
    @Column(length = 10, nullable = false)
    private String writer;
    
    @Column
    @ColumnDefault("0")
    private Long view;
    
    @Column(columnDefinition="TINYINT", length = 1)
    @ColumnDefault("1")
    private Byte status;
    
    @Column(name="top_category", columnDefinition="TINYINT", length = 1, nullable = false)
    private Byte topCategory;
    
    // many to one
    @Column(name="category_id", nullable = false)
    private Long categoryId;
    
    @Column(columnDefinition="TINYINT", length = 1)
    @ColumnDefault("0")
    private Byte qna;

    @Builder
    public Board(Long id, String title, String content, String writer, Long view, Byte topCategory, Long categoryId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.view = view;
        this.topCategory = topCategory;
        this.categoryId = categoryId;
        this.status = 1;
        this.qna = 0;
    }

}
