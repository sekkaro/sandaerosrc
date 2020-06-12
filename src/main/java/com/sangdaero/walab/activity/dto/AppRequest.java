package com.sangdaero.walab.activity.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AppRequest {

    private String email;

    private String name;

    private Long id;
    
    private Byte type;

}
