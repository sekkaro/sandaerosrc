package com.sangdaero.walab.common.file.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sangdaero.walab.common.entity.FileEntity;
import com.sangdaero.walab.common.file.repository.FileRepository;

@Service
public class FileService {

	private FileRepository mFileRepository;
	
	public FileService(FileRepository fileRepository) {
		mFileRepository = fileRepository;
	}
	
	public List<String> getFiles(Long eventId) {
		List<String> fileNameList = new ArrayList<>();
		
		List<FileEntity> fileEntityList = mFileRepository.findAllByEventId(eventId);
		
		if(fileEntityList!=null) {
			for(FileEntity fileEntity: fileEntityList) {
				fileNameList.add(fileEntity.getTitle());
			}
		}
		
		return fileNameList;
	}
	
}
