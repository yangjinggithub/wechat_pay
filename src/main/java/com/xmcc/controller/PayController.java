package com.xmcc.controller;

import com.lly835.bestpay.model.PayResponse;
import com.xmcc.entity.OrderMaster;
import com.xmcc.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
@CrossOrigin
@Controller
@RequestMapping("pay")
@Slf4j
public class PayController {
    @Autowired
    private PayService payService;

    @RequestMapping("create")
    /**
     * 根据API文档创建接口
     * orderId: 订单ID 这里只能传递一个ID 防止别人传入非法的金额
     * returnUrl: 回调地址
     */
    public ModelAndView create(@RequestParam("orderId")String orderId,
                               @RequestParam("returnUrl")String returnUrl,
                               Map map){
        //根据id查询订单
        OrderMaster orderMaster = payService.findOrderById(orderId);
        //根据订单创建支付
        PayResponse response = payService.create(orderMaster);
        //将参数设置到map中
        map.put("payResponse",response);
        map.put("returnUrl",returnUrl);
        return new ModelAndView("weixin/pay",map);
    }
    @RequestMapping("test")
    public void test(){
        log.info("异步回调OK");
    }
}
