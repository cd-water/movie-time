package com.cdwater.movietimeorder.service;

import com.cdwater.movietimeorder.model.dto.OrderPageDTO;
import com.cdwater.movietimeorder.model.vo.OrderPageVO;
import com.github.pagehelper.PageInfo;

public interface OrderService {

    PageInfo<OrderPageVO> page(OrderPageDTO orderPageDTO);

    Long count();
}
