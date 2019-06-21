package com.xmcc.repository;

import com.xmcc.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * jpaRepository就提供了常用的增删改查方法
 */
//第一个参数 是实体类名称  第二个参数是主键类型
public interface ProductCategoryRepository extends JpaRepository<ProductCategory,Integer> {
    @Query(value = "select category_name from product_category where category_id=?" +
            "1 and category_id=?2",nativeQuery = true)
    List<String> queryNameByIdAndType(Integer id,Integer type);
}
