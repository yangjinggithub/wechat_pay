package com.xmcc.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelDto implements Serializable {

    @NotBlank(message = "用户微信不能为空")
    @ApiModelProperty(value = "openid",dataType = "String")
    private String openid;

    @NotBlank(message = "订单id不能为空")
    @ApiModelProperty(value = "orderId",dataType = "String")
    private String orderId;

}
