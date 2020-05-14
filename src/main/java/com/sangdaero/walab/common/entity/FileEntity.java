package com.sangdaero.walab.common.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "file")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class FileEntity extends TimeEntity {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 255, nullable = false)
	private String title;
	
	@ManyToOne
	@JoinColumn(name="event_id", nullable=false)
	private EventEntity event;
	
	@Column(length = 255, nullable = false)
	private String url;

	@Builder
	public FileEntity(Long id, String title, EventEntity event, String url) {
		this.id = id;
		this.title = title;
		this.event = event;
		this.url = url;
	}
	
}
