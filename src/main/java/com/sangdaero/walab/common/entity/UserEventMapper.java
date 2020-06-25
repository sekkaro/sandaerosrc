package com.sangdaero.walab.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.sangdaero.walab.mapper.id.UserEventId;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "userevent_mapper")
@IdClass(UserEventId.class)
public class UserEventMapper extends TimeEntity {
	
	@Id
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Id
	@ManyToOne
	@JoinColumn(name="event_id")
	private EventEntity event;
	
	@Column(name="user_type", nullable=false)
	@ColumnDefault("0")
	private Byte userType;
	
	@Column(name="status", nullable = false)
	@ColumnDefault("0")
	private Byte status;
	
	@Column(name="phone_agree", nullable = false)
	@ColumnDefault("0")
	private Byte phoneAgree;
	
	@Column(name="location_agree", nullable = false)
	@ColumnDefault("0")
	private Byte locationAgree;
	
	@Column(name="start_image", nullable = true)
	private String startImage;
	
	@Column(name="end_image", nullable = true)
	private String endImage;

	@Column(name="volunteer_time", nullable = false)
	@ColumnDefault("0")
	private Integer volunteerTime;
	
}
