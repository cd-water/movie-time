package com.cdwater.movietimemovie.service;

import com.cdwater.movietimemovie.model.dto.MovieAddDTO;
import com.cdwater.movietimemovie.model.dto.MoviePageDTO;
import com.cdwater.movietimemovie.model.dto.MovieUpdateDTO;
import com.cdwater.movietimemovie.model.vo.MoviePageVO;
import com.cdwater.movietimemovie.model.vo.MovieQueryVO;
import com.cdwater.movietimemovie.model.vo.MovieShowingVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface MovieService {

    void add(MovieAddDTO movieAddDTO);

    void update(MovieUpdateDTO movieUpdateDTO);

    MovieQueryVO query(Long id);

    PageInfo<MoviePageVO> page(MoviePageDTO moviePageDTO);

    List<MovieShowingVO> showing();

    Long count();
}
