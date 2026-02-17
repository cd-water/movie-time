package com.cdwater.movietimeorder.service.impl;

import com.cdwater.movietimeorder.mapper.OrderMapper;
import com.cdwater.movietimeorder.model.dto.OrderPageDTO;
import com.cdwater.movietimeorder.model.vo.OrderPageVO;
import com.cdwater.movietimeorder.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Override
    public PageInfo<OrderPageVO> page(OrderPageDTO orderPageDTO) {
        PageHelper.startPage(orderPageDTO.getPageNum(), orderPageDTO.getPageSize());
        List<OrderPageVO> list = orderMapper.selectPage(orderPageDTO);
        return PageInfo.of(list);
    }

    @Override
    public Long count() {
        return orderMapper.count();
    }
}
