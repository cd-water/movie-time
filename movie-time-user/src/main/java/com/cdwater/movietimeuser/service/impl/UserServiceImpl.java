package com.cdwater.movietimeuser.service.impl;

import com.cdwater.movietimeuser.mapper.UserMapper;
import com.cdwater.movietimeuser.model.dto.UserPageDTO;
import com.cdwater.movietimeuser.model.vo.UserPageVO;
import com.cdwater.movietimeuser.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public PageInfo<UserPageVO> page(UserPageDTO userPageDTO) {
        PageHelper.startPage(userPageDTO.getPageNum(), userPageDTO.getPageSize());
        List<UserPageVO> list = userMapper.selectPage(userPageDTO);
        return PageInfo.of(list);
    }

    @Override
    public Long count() {
        return userMapper.count();
    }
}
