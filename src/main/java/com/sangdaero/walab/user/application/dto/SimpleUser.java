package com.sangdaero.walab.user.application.dto;

import java.util.List;

import com.sangdaero.walab.common.entity.Device;

public interface SimpleUser {

    Long getId();

    String getName();

    String getNickname();

    Byte getUserType();

    String getPhone();

    Integer getVolunteerTime();
    
    List<Device> getDevices();

}
