package com.xht.community.mapper;

import com.xht.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Insert("insert into user (account_id,name," +
            "token,create_time," +
            "modified_time) values (#{accountId},#{name},#{token},#{createTime},#{modifiedTime})")
    Integer insertUser(User user);

    @Select("select * from user where token=#{token}")
    User findUserByToken(@Param("token") String token);
}
