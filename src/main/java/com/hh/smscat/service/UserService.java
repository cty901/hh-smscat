package com.hh.smscat.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hh.smscat.entity.User;

import java.util.HashMap;
import java.util.List;

public interface UserService extends IService<User> {
    List<HashMap<String, Object>> checkUserExists(String name);

    boolean testTransactional(String pwd);

    List<String> getCount();

    List<User> getCount2();

    IPage<User> getCount3(Page<User> userPage);
}
