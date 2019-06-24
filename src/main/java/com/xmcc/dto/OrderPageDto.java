package com.xmcc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPageDto implements Serializable {

    @NotBlank(message = "用户微信不能为空")
    @ApiModelProperty(value = "openid",dataType = "String")
    private String openid;

    @NotNull(message = "page不能为空")
    @ApiModelProperty(value = "page",dataType = "Integer")
    private Integer page;

    @NotNull(message = "size不能为空")
    @ApiModelProperty(value = "size",dataType = "Integer")
    private Integer size;
}
