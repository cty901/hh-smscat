package com.hh.smscat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hh.smscat.entity.User;

import java.util.HashMap;
import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    List<HashMap<String, Object>> checkUserExists(String name);

    boolean updateUser(String pwd);

    List<String> getCount();

}
