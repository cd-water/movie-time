package com.cdwater.movietimecinema.service;

import com.cdwater.movietimecinema.model.dto.ScreeningAddDTO;
import com.cdwater.movietimecinema.model.dto.ScreeningPageDTO;
import com.cdwater.movietimecinema.model.dto.ScreeningUpdateDTO;
import com.cdwater.movietimecinema.model.vo.ScreeningPageVO;
import com.cdwater.movietimecinema.model.vo.ScreeningQueryVO;
import com.github.pagehelper.PageInfo;

public interface ScreeningService {

    void add(ScreeningAddDTO screeningAddDTO);

    void delete(Long id);

    void update(ScreeningUpdateDTO screeningUpdateDTO);

    ScreeningQueryVO query(Long id);

    PageInfo<ScreeningPageVO> page(ScreeningPageDTO screeningPageDTO);
}
