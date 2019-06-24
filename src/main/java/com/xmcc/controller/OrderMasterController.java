package com.xmcc.controller;

import com.google.common.collect.Maps;
import com.xmcc.common.ResultResponse;
import com.xmcc.dto.OrderCancelDto;
import com.xmcc.dto.OrderMasterDto;
import com.xmcc.dto.OrderPageDto;
import com.xmcc.dto.OrderQueryDto;
import com.xmcc.service.OrderMasterService;
import com.xmcc.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@CrossOrigin//处理 No 'Access-Control-Allow-Origin' header is present on the requested resource 问题
@RestController
@RequestMapping("buyer/order")
@Api(value = "订单相关接口",description = "完成订单的增删改查")
public class OrderMasterController {
    @Autowired
    private OrderMasterService orderMasterService;

    @PostMapping("create")
    @ApiOperation(value = "创建订单接口", httpMethod = "POST", response = ResultResponse.class)
    public ResultResponse create(@Valid@ApiParam(name = "订单对象",value = "传入json格式",required = true)
                                 OrderMasterDto orderMasterDto, BindingResult bindingResult){
        Map<String,String> map = Maps.newHashMap();
        //参数校检
        if(bindingResult.hasErrors()) {
            List<String> errList = bindingResult.getFieldErrors().stream().map(err ->
                    err.getDefaultMessage()).collect(Collectors.toList());
            map.put("参数校验错误", JsonUtil.object2string(errList));
            //将参数校验的错误信息返回给前台
            return ResultResponse.fail(map);
        }
        return orderMasterService.insertOrder(orderMasterDto);
    }

    @GetMapping("list")
    @ApiOperation(value = "查询订单列表")
    public ResultResponse list(OrderPageDto orderPageDto){
        return orderMasterService.queryList(orderPageDto);
    }

    @GetMapping("detail")
    @ApiOperation(value = "查询订单详情")
    public ResultResponse detail(OrderQueryDto orderQueryDto){
        return orderMasterService.queryDetail(orderQueryDto);
    }

    @PostMapping("cancel")
    @ApiOperation(value = "取消订单")
    public ResultResponse cancel(OrderCancelDto orderCancelDto){
        return ResultResponse.success();
    }
}
