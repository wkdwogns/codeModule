package com.jjh.membership.dao;

import com.jjh.membership.dto.model.UserTokenVO;
import com.jjh.membership.dto.req.InsertMemberTokenReq;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface MemberTokenDao
{
    UserTokenVO selectMemberToken(String req);

    void insertUserToken(InsertMemberTokenReq req);
}
