package com.nexon.membership.dto.res;

import lombok.Data;

import java.util.List;

@Data
public class SessionRes {
    private int userSeq;
    List<String> authorityStr;
    int authorityLevel;
    boolean isSuspended;
    private String userId;
    private String userName;

    private long exp;
}

