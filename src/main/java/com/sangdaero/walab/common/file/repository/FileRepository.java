package com.sangdaero.walab.common.file.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sangdaero.walab.common.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

	List<FileEntity> findAllByEventId(Long eventId);
	
}
