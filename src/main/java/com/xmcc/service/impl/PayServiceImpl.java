package com.xmcc.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.xmcc.common.Constant;
import com.xmcc.common.OrderEnums;
import com.xmcc.common.PayEnums;
import com.xmcc.entity.OrderMaster;
import com.xmcc.exception.CustomException;
import com.xmcc.repository.OrderMasterRepository;
import com.xmcc.service.PayService;
import com.xmcc.util.BigDecimalUtil;
import com.xmcc.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
public class PayServiceImpl implements PayService {
    @Autowired
    private BestPayService bestPayService;
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    //获取订单
    public OrderMaster findOrderById(String orderId) {
        Optional<OrderMaster> byId = orderMasterRepository.findById(orderId);
        if(!byId.isPresent()){
            throw new CustomException(OrderEnums.ORDER_NOT_EXITS.getMsg());
        }
        return byId.get();
    }

    @Override
    //预支付
    public PayResponse create(OrderMaster orderMaster) {
        PayRequest payRequest = new PayRequest();
        //微信用户OPenid
        payRequest.setOpenid(orderMaster.getBuyerOpenid());
        //订单金额
        payRequest.setOrderAmount(orderMaster.getOrderAmount().doubleValue());
        //订单ID
        payRequest.setOrderId(orderMaster.getOrderId());
        //订单名字
        payRequest.setOrderName(Constant.orderName);
        //支付类型
        payRequest.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("微信支付的请求:{}", JsonUtil.object2string(payRequest));
        PayResponse response = bestPayService.pay(payRequest);
        log.info("微信支付的返回结果为:{}", JsonUtil.object2string(response));
        return response;
    }

    @Override
    public void weixin_notify(String notifyData) {
        //调用API 会自动完成支付状态签名等验证
        PayResponse response = bestPayService.asyncNotify(notifyData);
        //根据订单id查询订单
        OrderMaster orderMaster = findOrderById(response.getOrderId());
        //比较金额  这里注意 orderMaster中是BigDecimal 而 response里面是double
        //还需要注意的点 new BigDecimal的时候只能用字符串类型，不然精度会丢失
        if(!BigDecimalUtil.equals2(orderMaster.getOrderAmount(),new BigDecimal(String.valueOf(response.getOrderAmount())))){
            //有异常的地方必须打印日志
            log.error("微信支付回调，订单金额不一致.微信:{},数据库:{}",response.getOrderAmount(),orderMaster.getOrderAmount());
            throw new CustomException(OrderEnums.AMOUNT_CHECK_ERROR.getMsg());
        }
        //判断支付状态是否为可支付（ 等待支付才能支付） 避免重复通知等其他因素
        if(!(orderMaster.getPayStatus()== PayEnums.WAIT.getCode())){
            log.error("微信回调,订单状态异常：{}",orderMaster.getPayStatus());
            throw new CustomException(PayEnums.STATUS_ERROR.getMsg());
        }
        //比较结束以后 完成订单支付状态的修改
        //实际项目中 这儿还需要把交易流水号与订单的对应关系存入数据库，比较简单，这儿不做了,大家需要知道
        orderMaster.setPayStatus(PayEnums.FINISH.getCode());
        //注意:这儿只是支付状态OK  订单状态的修改 需要其他业务流程，发货，用户确认收货
        //修改支付状态
        orderMasterRepository.save(orderMaster);
        log.info("微信支付异步回调,订单支付状态修改完成");
    }
}
