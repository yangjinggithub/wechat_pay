package com.xmcc.service;

import com.xmcc.common.ResultResponse;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.dto.OrderPageDto;
import com.xmcc.dto.OrderQueryDto;

public interface OrderMasterService {

    ResultResponse insertOrder(OrderMasterDto orderMasterDto);

    ResultResponse queryList(OrderPageDto orderPageDto);

    ResultResponse queryDetail(OrderQueryDto orderQueryDto);
}
