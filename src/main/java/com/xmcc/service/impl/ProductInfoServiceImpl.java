package com.xmcc.service.impl;

import com.xmcc.common.ResultEnums;
import com.xmcc.common.ResultResponse;
import com.xmcc.dto.ProductCategoryDto;
import com.xmcc.dto.ProductInfoDto;
import com.xmcc.entity.ProductCategory;
import com.xmcc.entity.ProductInfo;
import com.xmcc.repository.ProductCategoryRepository;
import com.xmcc.repository.ProductInfoRepository;
import com.xmcc.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public ResultResponse queryList() {
        System.out.println("正在获取商品列表");
        //查询所有分类
        List<ProductCategory> all = productCategoryRepository.findAll();
        //转成dto
        List<ProductCategoryDto> productCategoryDtoList
                = all.stream().map(productCategory ->
                ProductCategoryDto.build(productCategory)).collect(Collectors.toList());
        //判断是否为空
        if(CollectionUtils.isEmpty(productCategoryDtoList)){
            return ResultResponse.fail();
        }
        //获取类目编号的集合
        List<Integer> typeList
                = productCategoryDtoList.stream().map(productCategoryDto ->
                productCategoryDto.getCategoryType()).collect(Collectors.toList());
        //根据typeList查询商品列表
        List<ProductInfo> productInfoList
                = productInfoRepository.findByProductStatusAndCategoryTypeIn(ResultEnums.PRODUCT_UP.getCode(), typeList);
        //对productCategoryDtoList目标集合进行遍历，取出每个商品的编号，设置进目录中
        //将productinfo设置到foods中
        //过滤：不同的type 不同封装
        //将productinfo转成dto
        List<ProductCategoryDto> productCategoryDtos
                = productCategoryDtoList.parallelStream().map(productCategoryDto -> {
                productCategoryDto.setProductInfoDtoList(productInfoList.stream()
                .filter(productInfo -> productInfo.getCategoryType() == productCategoryDto.getCategoryType())
                .map(productInfo -> ProductInfoDto.build(productInfo)).collect(Collectors.toList()));
            return productCategoryDto;
        }).collect(Collectors.toList());
        return ResultResponse.success(productCategoryDtos);
    }
}
