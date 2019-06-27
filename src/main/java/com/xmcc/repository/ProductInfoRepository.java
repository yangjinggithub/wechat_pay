package com.xmcc.repository;

import com.xmcc.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * jpaRepository就提供了常用的增删改查方法
 */
//第一个参数 是实体类名称  第二个参数是主键类型
public interface ProductInfoRepository extends JpaRepository<ProductInfo,String> {
    //根据类目编号 查询正常上架的商品
    List<ProductInfo> findByProductStatusAndCategoryTypeIn(Integer status, List<Integer> typeList);

    ProductInfo queryByProductId(String productId);

}
