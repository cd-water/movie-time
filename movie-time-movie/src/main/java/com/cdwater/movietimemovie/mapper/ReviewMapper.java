package com.cdwater.movietimemovie.mapper;

import com.cdwater.movietimemovie.model.dto.ReviewPageDTO;
import com.cdwater.movietimemovie.model.vo.ReviewPageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {

    List<ReviewPageVO> selectPage(ReviewPageDTO reviewPageDTO);

    void goDead(Long id);

    void goAlive(Long id);
}
