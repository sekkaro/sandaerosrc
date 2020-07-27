package com.sangdaero.walab.common.notification.repository;

import com.sangdaero.walab.common.entity.Notification;
import com.sangdaero.walab.common.entity.Request;
import com.sangdaero.walab.common.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @EntityGraph(attributePaths = { "user" })
    List<Notification> findAllByUserOrderByRegDateDesc(User entity);

    Notification findByRequestAndMessageContaining(Request request, String message);
}
