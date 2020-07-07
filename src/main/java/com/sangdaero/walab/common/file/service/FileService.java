package com.sangdaero.walab.common.file.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sangdaero.walab.common.entity.FileEntity;
import com.sangdaero.walab.common.file.repository.FileRepository;
import org.springframework.web.multipart.MultipartFile;

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

	public String saveFile(MultipartFile file) {
		Path currentPath = Paths.get("");
		Path absolutePath = currentPath.toAbsolutePath();

		String url = "/tomcat/webapps/ROOT/WEB-INF/classes/static/images/";

//	    String url = "/src/main/resources/static/images/";

		String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + file.getOriginalFilename();
		Path fileNameAndPath = Paths.get(absolutePath + url, fileName);
		try {
			Files.write(fileNameAndPath, file.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileName;
	}
}
