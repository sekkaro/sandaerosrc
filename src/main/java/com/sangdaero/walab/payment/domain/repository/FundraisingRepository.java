package com.sangdaero.walab.payment.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sangdaero.walab.common.entity.EventEntity;
import com.sangdaero.walab.common.entity.FundraisingEntity;
import com.sangdaero.walab.common.entity.FundraisingId;
import com.sangdaero.walab.common.entity.User;

public interface FundraisingRepository extends JpaRepository<FundraisingEntity, FundraisingId> {
	
	@Query(nativeQuery = true, value="SELECT * FROM fundraising WHERE event_id = :eventId AND user_id = :userId")
	List<EventEntity> findByEventIdAndUserId(@Param("eventId")Long eventId, @Param("userId")User userId);
	
	// find all
	List<FundraisingEntity> findAll();
	
	// find specific with only event_id
	List<FundraisingEntity> findAllByEventId(EventEntity eventId);
	
}
