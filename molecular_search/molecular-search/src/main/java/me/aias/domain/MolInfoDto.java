package me.aias.domain;

import lombok.Data;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * 分子对象
 * Mol object
 *
 * @author Calvin
 * @date 2021-12-12
 **/
@Data
public class MolInfoDto implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 分子结构
     * molecular structure
     *
     * 简化分子线性输入规范
     * SMILES（Simplified molecular input line entry system）
     */
    private String smiles;
    /**
     * 特征向量
     * feature vector
     */
    ByteBuffer feature;
}