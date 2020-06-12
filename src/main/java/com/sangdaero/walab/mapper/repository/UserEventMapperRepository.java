package com.sangdaero.walab.mapper.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.sangdaero.walab.common.entity.UserEventMapper;
import com.sangdaero.walab.mapper.id.UserEventId;

public interface UserEventMapperRepository extends JpaRepository<UserEventMapper, UserEventId> {

	List<UserEventMapper> findAllByEventId(Long id);

	@Transactional
	void deleteByEventId(Long eventId);

	List<UserEventMapper> findAllByUserTypeAndEvent_id(byte userType, Long id);
	
	@Transactional
	void deleteByEventIdAndUserType(Long id, byte userType);

	UserEventMapper findByEventIdAndUserId(Long id, Long userId);

	UserEventMapper findByEventIdAndUserIdAndUserType(Long id, Long userId, byte userType);

	List<UserEventMapper> findAllByEventIdAndUserType(Long id, byte userType);

	@Transactional
	void deleteByEventIdAndUserIdAndUserType(Long id, Long userId, byte userType);

	List<UserEventMapper> findAllByUserId(Long id);

}
