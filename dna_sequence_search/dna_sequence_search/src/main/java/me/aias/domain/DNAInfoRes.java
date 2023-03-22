package me.aias.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 信息返回对象
 * Info result object
 */
@Data
@ApiModel(value = "DNAInfoRes", description = "Info result object")
public class DNAInfoRes {
    @ApiModelProperty(value = "id", name = "id")
    private Long id;

    @ApiModelProperty(value = "Score", name = "score")
    private Float score;

    @ApiModelProperty(value = "label", name = "label")
    private String label;

    @ApiModelProperty(value = "sequence", name = "sequence")
    private String sequence;
}
