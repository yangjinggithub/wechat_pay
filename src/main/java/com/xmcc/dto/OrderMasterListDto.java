package com.xmcc.dto;

import com.xmcc.entity.OrderMaster;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderMasterListDto extends OrderMaster implements Serializable {

    private List orderDetailList;

    public static OrderMasterListDto adapter(OrderMaster orderMaster){
        OrderMasterListDto dto = new OrderMasterListDto();
        //拷贝字段
        BeanUtils.copyProperties(orderMaster,dto);
        return dto;
    }
}
