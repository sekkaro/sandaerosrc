package com.sangdaero.walab.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.sangdaero.walab.common.entity.TimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DynamicInsert
@Table(name = "paymentNotice") // DB table name
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentNoticeEntity extends TimeEntity {

	@Id 
	@ColumnDefault("0") // only single data in this table.
	private Long id;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	@Builder
	public PaymentNoticeEntity(Long id, String content) {
		super();
		this.id = id;
		this.content = content;
	}
	
}
