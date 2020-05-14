package com.sangdaero.walab.interest.domain.repository;

import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.interest.application.dto.InterestName;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<InterestCategory, Long> {
    List<InterestCategory> findByTypeEquals(Byte type);

    InterestCategory findByNameEquals(String name);

    List<InterestName> findAllByOrderByName();

    InterestCategory findByName(String interestName);
}
