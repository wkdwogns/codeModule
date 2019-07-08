package com.nexon.membership.dao;

import com.nexon.membership.dto.model.UserTokenVO;
import com.nexon.membership.dto.req.InsertMemberTokenReq;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MemberTokenDao
{
    UserTokenVO selectMemberToken(String req);

    void insertUserToken(InsertMemberTokenReq req);
}
