package com.sangdaero.walab.user.application.dto;

public interface SimpleUser {

    Long getId();

    String getName();

    String getNickname();

    Byte getUserType();

    Integer getVolunteerTime();

    void setId(Long id);

    void setName(String name);

    void setNickname(String nickname);

    void setUserType(Byte type);

    void setVolunteerTime(Integer time);
}
