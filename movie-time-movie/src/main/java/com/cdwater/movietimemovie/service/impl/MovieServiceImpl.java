package com.cdwater.movietimemovie.service.impl;

import com.cdwater.movietimecommon.constants.TextConstant;
import com.cdwater.movietimecommon.enums.ReturnEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import com.cdwater.movietimemovie.mapper.MovieMapper;
import com.cdwater.movietimemovie.model.dto.MovieAddDTO;
import com.cdwater.movietimemovie.model.dto.MoviePageDTO;
import com.cdwater.movietimemovie.model.dto.MovieUpdateDTO;
import com.cdwater.movietimemovie.model.entity.Movie;
import com.cdwater.movietimemovie.model.vo.MoviePageVO;
import com.cdwater.movietimemovie.model.vo.MovieQueryVO;
import com.cdwater.movietimemovie.model.vo.MovieShowingVO;
import com.cdwater.movietimemovie.service.MovieService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Resource
    private MovieMapper movieMapper;

    @Override
    public void add(MovieAddDTO movieAddDTO) {
        Movie movie = Movie.builder()
                .name(movieAddDTO.getName())
                .cover(movieAddDTO.getCover())
                .type(movieAddDTO.getType())
                .releaseDate(movieAddDTO.getReleaseDate())
                .duration(movieAddDTO.getDuration())
                .description(movieAddDTO.getDescription())
                .director(movieAddDTO.getDirector())
                .actors(movieAddDTO.getActors())
                .status(movieAddDTO.getStatus())
                .build();
        movieMapper.insert(movie);
    }

    @Override
    public void update(MovieUpdateDTO movieUpdateDTO) {
        Movie movie = Movie.builder()
                .id(movieUpdateDTO.getId())
                .name(movieUpdateDTO.getName())
                .cover(movieUpdateDTO.getCover())
                .type(movieUpdateDTO.getType())
                .releaseDate(movieUpdateDTO.getReleaseDate())
                .duration(movieUpdateDTO.getDuration())
                .description(movieUpdateDTO.getDescription())
                .director(movieUpdateDTO.getDirector())
                .actors(movieUpdateDTO.getActors())
                .status(movieUpdateDTO.getStatus())
                .build();
        movieMapper.updateById(movie);
    }

    @Override
    public MovieQueryVO query(Long id) {
        Movie movie = movieMapper.selectById(id);
        if (movie == null) {
            //电影不存在
            throw new BusinessException(ReturnEnum.NOT_FOUND);
        }
        return MovieQueryVO.builder()
                .id(movie.getId())
                .name(movie.getName())
                .cover(movie.getCover())
                .type(movie.getType())
                .releaseDate(movie.getReleaseDate())
                .duration(movie.getDuration())
                .description(movie.getDescription())
                .director(movie.getDirector())
                .actors(movie.getActors())
                .status(movie.getStatus())
                .build();
    }

    @Override
    public PageInfo<MoviePageVO> page(MoviePageDTO moviePageDTO) {
        PageHelper.startPage(moviePageDTO.getPageNum(), moviePageDTO.getPageSize());
        List<MoviePageVO> list = movieMapper.selectPage(moviePageDTO);
        return PageInfo.of(list);
    }

    @Override
    public List<MovieShowingVO> showing() {
        //热映中
        Integer status = TextConstant.MOVIE_STATUS_SHOWING;
        List<Movie> list = movieMapper.selectByStatus(status);
        return list.stream().map(movie -> MovieShowingVO.builder()
                .id(movie.getId())
                .name(movie.getName())
                .build()).toList();
    }

    @Override
    public Long count() {
        return movieMapper.count();
    }
}
