package com.xmcc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderQueryDto implements Serializable {
    @NotBlank(message = "用户微信不能为空")
    @ApiModelProperty(value = "openid")
    private String openid;

    @NotBlank(message = "订单id不能为空")
    @ApiModelProperty(value = "orderId")
    private String orderId;
}
