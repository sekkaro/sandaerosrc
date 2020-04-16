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
public class BoardCategory extends TimeEntity {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name="top_category", columnDefinition="TINYINT", length = 1, nullable = false)
    private Byte topCategory;
    
    @Column(name="status")
    @ColumnDefault("1")
    private Byte status;
	
    @Column(length = 255)
	private String memo;
	
    @Column(length = 10, name="community_manager", nullable = false)
	private String communityManager;
	
    @Builder
    public BoardCategory(Long id, Byte topCategory, Byte status, String memo, String communityManager) {
    	this.id = id;
    	this.topCategory = topCategory;
    	this.status = status;
    	this.memo = memo;
    	this.communityManager = communityManager;
    }
}
