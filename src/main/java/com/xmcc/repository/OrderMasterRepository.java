package com.xmcc.repository;

import com.xmcc.entity.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String>, JpaSpecificationExecutor {

    List<OrderMaster> findByBuyerOpenid(String openid);

    OrderMaster findByBuyerOpenidAndOrderId(String openid,String orderId);
}
