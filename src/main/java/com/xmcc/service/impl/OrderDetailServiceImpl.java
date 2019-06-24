package com.xmcc.service.impl;

import com.xmcc.dao.BatchDaoImpl;
import com.xmcc.entity.OrderDetail;
import com.xmcc.service.OrderDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderDetailServiceImpl extends BatchDaoImpl<OrderDetail> implements OrderDetailService {

    //批量插入
    @Override
    @Transactional//增删改都写事务管理
    public void batchInsert(List<OrderDetail> list){
        super.batchInsert(list);
    }
}
