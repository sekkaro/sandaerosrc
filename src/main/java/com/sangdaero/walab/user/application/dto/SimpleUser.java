package com.sangdaero.walab.user.application.dto;

public interface SimpleUser {

    Long getId();

    String getName();

    String getPhone();
    
    String getNickname();

    Byte getUserType();

    Integer getVolunteerTime();

}
