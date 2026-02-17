package com.cdwater.movietimeuser.service;

import com.cdwater.movietimeuser.model.dto.AdminCreateDTO;
import com.cdwater.movietimeuser.model.dto.AdminPageDTO;
import com.cdwater.movietimeuser.model.dto.AdminUpdateDTO;
import com.cdwater.movietimeuser.model.vo.AdminPageVO;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;

public interface AdminService {

    void create(@Valid AdminCreateDTO adminCreateDTO);

    void delete(Long id);

    void edit(@Valid AdminUpdateDTO adminUpdateDTO);

    PageInfo<AdminPageVO> page(AdminPageDTO adminPageDTO);
}
