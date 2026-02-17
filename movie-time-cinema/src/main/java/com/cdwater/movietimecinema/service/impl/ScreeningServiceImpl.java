package com.cdwater.movietimecinema.service.impl;

import com.cdwater.movietimecinema.mapper.ScreeningMapper;
import com.cdwater.movietimecinema.model.dto.ScreeningAddDTO;
import com.cdwater.movietimecinema.model.dto.ScreeningPageDTO;
import com.cdwater.movietimecinema.model.dto.ScreeningUpdateDTO;
import com.cdwater.movietimecinema.model.entity.Screening;
import com.cdwater.movietimecinema.model.vo.ScreeningPageVO;
import com.cdwater.movietimecinema.model.vo.ScreeningQueryVO;
import com.cdwater.movietimecinema.service.ScreeningService;
import com.cdwater.movietimecommon.enums.ReturnEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    @Resource
    private ScreeningMapper screeningMapper;

    @Override
    public void add(ScreeningAddDTO screeningAddDTO) {
        Screening screening = Screening.builder()
                .movieId(screeningAddDTO.getMovieId())
                .cinemaId(screeningAddDTO.getCinemaId())
                .hallName(screeningAddDTO.getHallName())
                .rowCount(screeningAddDTO.getRowCount())
                .colCount(screeningAddDTO.getColCount())
                .startTime(screeningAddDTO.getStartTime())
                .endTime(screeningAddDTO.getEndTime())
                .price(screeningAddDTO.getPrice())
                .build();
        screeningMapper.insert(screening);
    }

    @Override
    public void delete(Long id) {
        screeningMapper.delete(id);
    }

    @Override
    public void update(ScreeningUpdateDTO screeningUpdateDTO) {
        Screening screening = Screening.builder()
                .id(screeningUpdateDTO.getId())
                .movieId(screeningUpdateDTO.getMovieId())
                .hallName(screeningUpdateDTO.getHallName())
                .rowCount(screeningUpdateDTO.getRowCount())
                .colCount(screeningUpdateDTO.getColCount())
                .startTime(screeningUpdateDTO.getStartTime())
                .endTime(screeningUpdateDTO.getEndTime())
                .price(screeningUpdateDTO.getPrice())
                .build();
        screeningMapper.updateById(screening);
    }

    @Override
    public ScreeningQueryVO query(Long id) {
        Screening screening = screeningMapper.selectById(id);
        if (screening == null) {
            //场次不存在
            throw new BusinessException(ReturnEnum.NOT_FOUND);
        }
        return ScreeningQueryVO.builder()
                .id(screening.getId())
                .movieId(screening.getMovieId())
                .hallName(screening.getHallName())
                .rowCount(screening.getRowCount())
                .colCount(screening.getColCount())
                .startTime(screening.getStartTime())
                .endTime(screening.getEndTime())
                .price(screening.getPrice())
                .build();
    }

    @Override
    public PageInfo<ScreeningPageVO> page(ScreeningPageDTO screeningPageDTO) {
        PageHelper.startPage(screeningPageDTO.getPageNum(), screeningPageDTO.getPageSize());
        List<ScreeningPageVO> list = screeningMapper.selectPage(screeningPageDTO);
        return PageInfo.of(list);
    }
}
