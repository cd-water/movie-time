package com.cdwater.movietimemovie.service.impl;

import com.cdwater.movietimemovie.mapper.ReviewMapper;
import com.cdwater.movietimemovie.model.dto.ReviewPageDTO;
import com.cdwater.movietimemovie.model.vo.ReviewPageVO;
import com.cdwater.movietimemovie.service.ReviewService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Resource
    private ReviewMapper reviewMapper;

    @Override
    public PageInfo<ReviewPageVO> page(ReviewPageDTO reviewPageDTO) {
        PageHelper.startPage(reviewPageDTO.getPageNum(), reviewPageDTO.getPageSize());
        List<ReviewPageVO> list = reviewMapper.selectPage(reviewPageDTO);
        return PageInfo.of(list);
    }

    @Override
    public void goDead(Long id) {
        reviewMapper.goDead(id);
    }

    @Override
    public void goAlive(Long id) {
        reviewMapper.goAlive(id);
    }
}
