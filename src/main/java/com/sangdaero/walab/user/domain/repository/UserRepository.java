package com.sangdaero.walab.user.domain.repository;

import com.sangdaero.walab.common.entity.User;
import com.sangdaero.walab.user.application.dto.SimpleUser;

import com.sangdaero.walab.user.application.dto.VolunteerRanking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<SimpleUser> findAllByOrderByName();

//    List<SimpleUser> findTop5ByOrderByVolunteerTimeDesc();

    List<User> findTop5ByOrderByVolunteerTimeDesc();
    
    List<SimpleUser> findAllByUserTypeOrderByName(byte userType);

	User findBySocialId(String socialId);
	
	Page<User> findAllByNameContaining(String name, Pageable pageable);

    Page<User> findAllByNicknameContaining(String nickname, Pageable pageable);
    
    List<User> findAllByNameContaining(String keyword);
    
    @EntityGraph(attributePaths = { "userInterestList", "fundraising" })
	Optional<User> getById(Long userId);
}
