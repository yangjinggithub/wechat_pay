package com.xmcc.service;

import com.lly835.bestpay.model.PayResponse;
import com.xmcc.entity.OrderMaster;

/**
 * 支付service
 */
public interface PayService {
    //根据订单id查询订单
    OrderMaster findOrderById(String orderId);

    PayResponse create(OrderMaster orderMaster);

    void weixin_notify(String notifyData);
}
