package com.xmcc.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xmcc.common.*;
import com.xmcc.dto.*;
import com.xmcc.entity.OrderDetail;
import com.xmcc.entity.OrderMaster;
import com.xmcc.entity.ProductInfo;
import com.xmcc.exception.CustomException;
import com.xmcc.repository.OrderDetailRepository;
import com.xmcc.repository.OrderMasterRepository;
import com.xmcc.repository.ProductInfoRepository;
import com.xmcc.service.OrderDetailService;
import com.xmcc.service.OrderMasterService;
import com.xmcc.service.ProductInfoService;
import com.xmcc.util.BigDecimalUtil;
import com.xmcc.util.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderMasterServiceImpl implements OrderMasterService {
    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    public ResultResponse insertOrder(OrderMasterDto orderMasterDto) {
        /**
         * Valid:用于配合JSR303注解 验证参数，只能在controller层进行验证
         * Validetor:在service层验证
         */
        //取出订单项
        List<OrderDetailDto> items = orderMasterDto.getItems();
        //创建OrderDetail的集合
        List<OrderDetail> orderDetailList = Lists.newArrayList();
        //初始化订单的总金额
        BigDecimal totalPrice = new BigDecimal("0");
        //遍历订单项，获取商品详情
        for (OrderDetailDto orderDetailDto:items) {
            //查询订单
            ResultResponse<ProductInfo> resultResponse = productInfoService.queryById(orderDetailDto.getProductId());
            //判断ResultResponse的code即可
            if(resultResponse.getCode()== ResultEnums.FAIL.getCode()){
                throw new CustomException(resultResponse.getMsg());
            }
            //得到商品
            ProductInfo productInfo = resultResponse.getData();
            //比较库存
            if(productInfo.getProductStock()<orderDetailDto.getProductQuantity()){
                throw new CustomException(ProductEnums.PRODUCT_NOT_ENOUGH.getMsg());
            }
            //创建订单项
            OrderDetail orderDetail = OrderDetail.builder().detailId(IDUtil.createIdbyUUID()).productIcon(productInfo.getProductIcon())
                    .productId(orderDetailDto.getProductId()).productName(productInfo.getProductName())
                    .productPrice(productInfo.getProductPrice()).productQuantity(orderDetailDto.getProductQuantity())
                    .build();
            //添加到订单项集合中
            orderDetailList.add(orderDetail);
            //减少库存
            productInfo.setProductStock(productInfo.getProductStock()-orderDetailDto.getProductQuantity());
            //跟新商品数据
            productInfoService.updateProduct(productInfo);
            //计算价格
            totalPrice = BigDecimalUtil.add(totalPrice,BigDecimalUtil.multi(productInfo.getProductPrice(),orderDetailDto.getProductQuantity()));
        }
        //生成订单ID
        String idbyUUID = IDUtil.createIdbyUUID();
        //构建订单信息
        OrderMaster orderMaster = OrderMaster.builder()
                .orderId(idbyUUID).buyerAddress(orderMasterDto.getAddress())
                .buyerName(orderMasterDto.getName()).buyerOpenid(orderMasterDto.getOpenid())
                .buyerPhone(orderMasterDto.getPhone()).orderAmount(totalPrice)
                .orderStatus(OrderEnums.NEW.getCode())
                .payStatus(PayEnums.WAIT.getCode()).build();
        //将订单id设置到订单项中
        List<OrderDetail> orderDetails = orderDetailList.stream().map(orderDetail -> {
            orderDetail.setOrderId(idbyUUID);
            return orderDetail;
        }).collect(Collectors.toList());
        //批量插入订单项
        orderDetailService.batchInsert(orderDetails);
        //插入订单
        orderMasterRepository.save(orderMaster);
        HashMap<String,String> map = Maps.newHashMap();
        map.put("orderId",idbyUUID);
        return ResultResponse.success(map);
    }

    @Override
    public ResultResponse queryList(OrderPageDto orderPageDto) {

        String openid = orderPageDto.getOpenid();
        Integer page = orderPageDto.getPage();
        Integer size = orderPageDto.getSize();

        PageRequest pageRequest = PageRequest.of(page, size);
        Specification<String> confusion = new Specification<String>(){
            @Override
            public Predicate toPredicate(Root<String> root, CriteriaQuery<?> criteriaQuery,
                                         CriteriaBuilder criteriaBuilder) {
                Path<String> buyer_openid = root.get("buyerOpenid");
                return criteriaBuilder.equal(buyer_openid,openid);
            }
        };
        Page all = orderMasterRepository.findAll(confusion, pageRequest);
        return ResultResponse.success(all);
    }

    @Override
    public ResultResponse queryDetail(OrderQueryDto orderQueryDto) {
        String orderId = orderQueryDto.getOrderId();
        String openid = orderQueryDto.getOpenid();

        OrderMaster orderMaster = orderMasterRepository.findByBuyerOpenidAndOrderId(openid, orderId);
        if(orderMaster==null){
            throw new CustomException(ResultEnums.PARAM_ERROR.getMsg());
        }
        OrderMasterListDto orderMasterListDto = OrderMasterListDto.adapter(orderMaster);
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        if(orderDetails.size()==0){
            throw new CustomException(ResultEnums.PARAM_ERROR.getMsg());
        }
        orderMasterListDto.setOrderDetailList(orderDetails);
        return ResultResponse.success(orderMasterListDto);
    }
}
