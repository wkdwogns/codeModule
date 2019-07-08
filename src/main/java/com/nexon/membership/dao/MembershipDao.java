package com.nexon.membership.dao;

import com.nexon.membership.dto.model.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Mapper
public interface MembershipDao
{
    UserInfoVO selectUserInfo(Map req);
    UserInfoVO selectUser(Map req);

    void insertUser(Map userData);
    void insertProfile(Map profileData);
    int getUserMnNoCnt(int authority);
    void updateUser(Map userData);
    Map selectProfile(Map params);
    void updateProfile(Map params);
}
