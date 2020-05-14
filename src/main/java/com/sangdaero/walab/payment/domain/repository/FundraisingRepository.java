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
	
	// find a single fundraising with composite key, (eventId, userId)
	@Query(nativeQuery = true, value="SELECT * FROM fundraising WHERE event_id = :eventId AND user_id = :userId")
	FundraisingEntity findByEventIdAndUserId(@Param("eventId")EventEntity eventId, @Param("userId")User userId);
	
	// find all
	List<FundraisingEntity> findAll();
	
	// find all with only event_id
	List<FundraisingEntity> findAllByEventId(EventEntity eventId);
	
	// delete all with only event_id
	List<FundraisingEntity> deleteAllByEventId(EventEntity eventId);
	
	List<FundraisingEntity> deleteByEventIdAndUserId(EventEntity eventId, User userId);
	
}
