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
    List<SimpleUser> findAllByUserTypeNotOrderByName(Byte userType);

//    List<SimpleUser> findTop5ByOrderByVolunteerTimeDesc();

    List<User> findTop5ByOrderByVolunteerTimeDesc();
    
    List<SimpleUser> findAllByUserTypeOrderByName(byte userType);

	User findBySocialId(String socialId);

    Page<User> findAllByNameContainingAndUserTypeNot(String name, Byte userType, Pageable pageable);

    Page<User> findAllByNicknameContainingAndUserTypeNot(String nickname, Byte userType, Pageable pageable);

    Page<User> findAllByUserTypeNot(Byte userType, Pageable pageable);

    List<User> findAllByNameContainingAndUserTypeNot(String keyword, Byte userType);
    
    @EntityGraph(attributePaths = { "userInterestList", "fundraising" })
	Optional<User> getById(Long userId);

    boolean existsByPhone(String phone);

    User findByPhone(String phone);

    User findByNickname(String nickname);

    boolean existsByNickname(String nickname);

    List<User> findAllByNameContaining(String keyword);

    List<SimpleUser> findAllByOrderByName();
}
