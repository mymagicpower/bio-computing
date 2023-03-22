package me.aias.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 信息返回对象
 * Mol Info Res
 */
@Data
@ApiModel(value = "MolInfoRes", description = "MolInfoRes")
public class MolInfoRes {
    @ApiModelProperty(value = "id", name = "id")
    private Long id;

    @ApiModelProperty(value = "Score", name = "score")
    private Float score;

    @ApiModelProperty(value = "smiles", name = "smiles")
    private String smiles;

    @ApiModelProperty(value = "url", name = "url")
    private String url;
}
