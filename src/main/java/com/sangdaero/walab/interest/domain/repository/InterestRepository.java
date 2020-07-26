package com.sangdaero.walab.interest.domain.repository;

import com.sangdaero.walab.common.entity.InterestCategory;
import com.sangdaero.walab.interest.application.dto.InterestName;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterestRepository extends JpaRepository<InterestCategory, Long> {
    List<InterestCategory> findByTypeEquals(Byte type);

    InterestCategory findByNameEquals(String name);

    List<InterestName> findAllByOrderByName();

    @Query(value = "SELECT i FROM InterestCategory as i WHERE i.on_off=?1")
    List<InterestCategory> findAllByOn_offEquals(byte value);

    InterestCategory findByName(String interestName);

    boolean existsByName(String name);

    InterestCategory findByNameContaining(String name);
}
