package me.aias.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 文本对象
 * Text object
 *
 * @author Calvin
 * @date 2021-12-12
 **/
@Data
public class DNAInfoDto implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * label
     */
    private String label;
    /**
     * dna sequence
     */
    private String sequence;
    /**
     * 特征向量
     * feature
     */
    List<Float> feature;
}