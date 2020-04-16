package com.sangdaero.walab.user.domain.repository;

import com.sangdaero.walab.common.entity.User;
import com.sangdaero.walab.user.application.dto.SimpleUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<SimpleUser> findAllByOrderByName();

    List<SimpleUser> findTop5ByOrderByVolunteerTimeDesc();

    List<SimpleUser> findAllByOrderByVolunteerTimeDesc();
    
    List<SimpleUser> findAllByUserTypeOrderByName(byte userType);

	User findBySocialId(String socialId);
	
	Page<User> findAllByNameContaining(String name, Pageable pageable);

    Page<User> findAllByNicknameContaining(String nickname, Pageable pageable);
}
