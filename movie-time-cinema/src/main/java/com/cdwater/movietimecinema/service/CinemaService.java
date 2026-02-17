package com.cdwater.movietimecinema.service;

import com.cdwater.movietimecinema.model.dto.CinemaAddDTO;
import com.cdwater.movietimecinema.model.dto.CinemaPageDTO;
import com.cdwater.movietimecinema.model.dto.CinemaUpdateDTO;
import com.cdwater.movietimecinema.model.vo.CinemaPageVO;
import com.cdwater.movietimecinema.model.vo.CinemaQueryVO;
import com.github.pagehelper.PageInfo;

public interface CinemaService {

    void add(CinemaAddDTO cinemaAddDTO);

    void delete(Long id);

    void update(CinemaUpdateDTO cinemaUpdateDTO);

    CinemaQueryVO query(Long id);

    PageInfo<CinemaPageVO> page(CinemaPageDTO cinemaPageDTO);

    Long count();
}
