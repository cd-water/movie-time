package com.cdwater.movietimemovie.service;

import com.cdwater.movietimemovie.model.dto.ReviewPageDTO;
import com.cdwater.movietimemovie.model.vo.ReviewPageVO;
import com.github.pagehelper.PageInfo;

public interface ReviewService {

    PageInfo<ReviewPageVO> page(ReviewPageDTO reviewPageDTO);

    void goDead(Long id);

    void goAlive(Long id);
}
