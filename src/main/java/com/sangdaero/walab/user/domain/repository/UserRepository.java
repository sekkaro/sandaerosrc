package com.sangdaero.walab.user.domain.repository;

import com.sangdaero.walab.common.entity.User;
import com.sangdaero.walab.user.application.dto.SimpleUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<SimpleUser> findAllByOrderByName();

    List<SimpleUser> findTop5ByOrderByVolunteerTimeDesc();

    List<SimpleUser> findAllByOrderByVolunteerTimeDesc();

	User findBySocialId(String socialId);

	
}
