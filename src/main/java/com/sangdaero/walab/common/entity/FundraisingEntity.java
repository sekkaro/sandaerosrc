package com.sangdaero.walab.common.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.sangdaero.walab.common.entity.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// many to many entity between "eventEntity" and "User"
@Getter
@Setter
@DynamicInsert
@Table(name = "fundraising")
@IdClass(FundraisingId.class)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FundraisingEntity extends TimeEntity {

	// by implementing class 'FundraisingId',
	// both 'event_id' and 'user_id', they become composite key, like (eventId, userId).
	
	@Id
	@ManyToOne
	@JoinColumn(name="event_id", nullable = false)
	private EventEntity eventId;
	
	@Id
	@ManyToOne
	@JoinColumn(name="user_id", nullable = false)
	private User userId;
	
	// extra information about personal payment
	
	@Column(name="title", nullable=true)
	private String title;
	
	@Column(name = "memo", columnDefinition = "TEXT", nullable=true)
	private String memo;

	@Column(name="personal_pay", nullable=true)
	private Integer personalPayAmount;

	@Column(length = 255, nullable = true)
	private String donator;

	@Column(name="bill_type", nullable = true)
	@ColumnDefault("0")
	private Byte billType;

	@Column(name="payment_status", nullable = true)
	@ColumnDefault("0")
	private Byte paymentStatus;

	@Column(length = 255, name="donator_phone", nullable = true)
	private String donatorPhone;

	@Column(length = 255, name="business_picture", nullable = true)
	private String businessPicture;

	@Builder
	public FundraisingEntity(EventEntity eventId, User userId, String title, String memo, Integer personalPayAmount,
			String donator, Byte billType, Byte paymentStatus, String donatorPhone, String businessPicture) {

		this.eventId = eventId;
		this.userId = userId;
		this.title = title;
		this.memo = memo;
		this.personalPayAmount = personalPayAmount;
		this.donator = donator;
		this.billType = billType;
		this.paymentStatus = paymentStatus;
		this.donatorPhone = donatorPhone;
		this.businessPicture = businessPicture;
	}

}

