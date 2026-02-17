package com.cdwater.movietimecinema.service.impl;

import com.cdwater.movietimecinema.mapper.CinemaMapper;
import com.cdwater.movietimecinema.model.dto.CinemaAddDTO;
import com.cdwater.movietimecinema.model.dto.CinemaPageDTO;
import com.cdwater.movietimecinema.model.dto.CinemaUpdateDTO;
import com.cdwater.movietimecinema.model.entity.Cinema;
import com.cdwater.movietimecinema.model.vo.CinemaPageVO;
import com.cdwater.movietimecinema.model.vo.CinemaQueryVO;
import com.cdwater.movietimecinema.service.CinemaService;
import com.cdwater.movietimecommon.constants.RedisConstant;
import com.cdwater.movietimecommon.enums.ReturnEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import com.cdwater.movietimecommon.utils.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaServiceImpl implements CinemaService {

    @Resource
    private CinemaMapper cinemaMapper;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void add(CinemaAddDTO cinemaAddDTO) {
        Cinema cinema = Cinema.builder()
                .name(cinemaAddDTO.getName())
                .address(cinemaAddDTO.getAddress())
                .district(cinemaAddDTO.getDistrict())
                .longitude(cinemaAddDTO.getLongitude())
                .latitude(cinemaAddDTO.getLatitude())
                .phone(cinemaAddDTO.getPhone())
                .minPrice(cinemaAddDTO.getMinPrice())
                .tags(cinemaAddDTO.getTags())
                .build();
        //mysql新增
        cinemaMapper.insert(cinema);
        //redis写入对于geo缓存
        redisUtil.addGeo(RedisConstant.CINEMA_GEO, cinema.getId(),
                cinema.getLongitude().doubleValue(),
                cinema.getLatitude().doubleValue());
    }

    @Override
    public void delete(Long id) {
        //mysql删除
        cinemaMapper.deleteById(id);
        //redis清除对应geo缓存
        redisUtil.removeGeo(RedisConstant.CINEMA_GEO, id);
    }

    @Override
    public void update(CinemaUpdateDTO cinemaUpdateDTO) {
        Cinema cinema = Cinema.builder()
                .id(cinemaUpdateDTO.getId())
                .name(cinemaUpdateDTO.getName())
                .address(cinemaUpdateDTO.getAddress())
                .district(cinemaUpdateDTO.getDistrict())
                .longitude(cinemaUpdateDTO.getLongitude())
                .latitude(cinemaUpdateDTO.getLatitude())
                .phone(cinemaUpdateDTO.getPhone())
                .minPrice(cinemaUpdateDTO.getMinPrice())
                .tags(cinemaUpdateDTO.getTags())
                .build();
        //mysql更新
        cinemaMapper.updateById(cinema);
        //redis清除对应geo缓存
        redisUtil.removeGeo(RedisConstant.CINEMA_GEO, cinema.getId());
    }

    @Override
    public CinemaQueryVO query(Long id) {
        Cinema cinema = cinemaMapper.selectById(id);
        if (cinema == null) {
            //影院不存在
            throw new BusinessException(ReturnEnum.NOT_FOUND);
        }
        return CinemaQueryVO.builder()
                .id(cinema.getId())
                .name(cinema.getName())
                .address(cinema.getAddress())
                .district(cinema.getDistrict())
                .longitude(cinema.getLongitude())
                .latitude(cinema.getLatitude())
                .phone(cinema.getPhone())
                .minPrice(cinema.getMinPrice())
                .tags(cinema.getTags())
                .build();
    }

    @Override
    public PageInfo<CinemaPageVO> page(CinemaPageDTO cinemaPageDTO) {
        PageHelper.startPage(cinemaPageDTO.getPageNum(), cinemaPageDTO.getPageSize());
        List<CinemaPageVO> list = cinemaMapper.selectPage(cinemaPageDTO);
        return PageInfo.of(list);
    }

    @Override
    public Long count() {
        return cinemaMapper.count();
    }
}
