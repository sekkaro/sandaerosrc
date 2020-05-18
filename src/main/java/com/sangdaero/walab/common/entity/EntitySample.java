package com.sangdaero.walab.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class EntitySample extends TimeEntity {

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
    
    @Column(nullable = false)
    private Long scope;
    
    @Column(columnDefinition="TINYINT", length = 1)
    @ColumnDefault("1")
    private Byte status;
    
    @Column(name="top_catogory", columnDefinition="TINYINT", length = 1, nullable = false)
    private Byte topCategory;
    
    @Column(name="sub_catogory", nullable = false)
    private Long subCategory;
    
    @Column(columnDefinition="TINYINT", length = 1)
    @ColumnDefault("0")
    private Byte qna;

    @Builder
    public EntitySample(Long id, String title, String content, String writer, Long scope, Byte topCategory, Long subCategory) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.scope = scope;
        this.topCategory = topCategory;
        this.subCategory = subCategory;
    }
}