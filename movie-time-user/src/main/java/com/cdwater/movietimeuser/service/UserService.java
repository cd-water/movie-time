package com.cdwater.movietimeuser.service;

import com.cdwater.movietimeuser.model.dto.UserPageDTO;
import com.cdwater.movietimeuser.model.vo.UserPageVO;
import com.github.pagehelper.PageInfo;

public interface UserService {

    PageInfo<UserPageVO> page(UserPageDTO userPageDTO);

    Long count();
}
