package com.sangdaero.walab.common.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// id class for composite key, 'EventEntity' and 'User'
@Getter
@Setter
@NoArgsConstructor
public class FundraisingId implements Serializable {
	
	private Long eventId;
	private Long userId;
	
	// constructor
	public FundraisingId(Long eventId, Long userId) {
		this.eventId = eventId;
		this.userId = userId;
	}
	
}
