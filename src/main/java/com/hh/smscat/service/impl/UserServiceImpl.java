package com.hh.smscat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hh.smscat.base.common.RollBackException;
import com.hh.smscat.base.dbchange.DSNames;
import com.hh.smscat.base.dbchange.ToggleDataSource;
import com.hh.smscat.entity.User;
import com.hh.smscat.mapper.UserMapper;
import com.hh.smscat.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl  extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Transactional
    public List<HashMap<String, Object>> checkUserExists(String name) {
        try {
            return userMapper.checkUserExists(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    public boolean testTransactional(String pwd) {
        boolean b = userMapper.updateUser(pwd);
        System.out.println(b);
        List<?> l = null;
        if (l == null) {
            throw new RollBackException("测试事务回滚");
        }
        return false;
    }


    @Override
    public List<String> getCount() {
        return userMapper.getCount();
    }

    @ToggleDataSource(DSNames.ClusterDataSource)
    @Override
    public List<User> getCount2() {
        return  getBaseMapper().selectList(new QueryWrapper<User>().isNotNull("id"));
    }

    @ToggleDataSource(DSNames.ClusterDataSource)
    @Override
    public IPage<User> getCount3(Page<User> userPage) {
        return getBaseMapper().selectPage(userPage,new QueryWrapper<>());
    }

}
