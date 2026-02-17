package com.cdwater.movietimeorder.mapper;

import com.cdwater.movietimeorder.model.dto.OrderPageDTO;
import com.cdwater.movietimeorder.model.vo.OrderPageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<OrderPageVO> selectPage(OrderPageDTO orderPageDTO);

    Long count();
}
