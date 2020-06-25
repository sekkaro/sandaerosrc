package com.sangdaero.walab.common.notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sangdaero.walab.common.entity.Notification;
import com.sangdaero.walab.common.entity.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@EntityGraph(attributePaths = { "user" })
	List<Notification> findAllByUserOrderByRegDateDesc(User entity);

}
