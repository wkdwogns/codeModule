package com.nexon.membership.dto.model;

import lombok.Data;

@Data
public class UserTokenVO {

    // User
    private Integer userSeq;  // 유저 번호
    private String refreshToken;   // 토큰 번호

}