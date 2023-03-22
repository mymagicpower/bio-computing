package me.aias.domain;

import lombok.Getter;

/**
 * 状态枚举
 * Status enumeration
 */
@Getter
public enum ResEnum {
    INFO_NOT_FOUND("0001","No similar text found"),
    MODEL_ERROR("0002","Model inference error"),
    MILVUS_CONNECTION_ERROR("0003","Vector engine connection error"),
    SYSTEM_ERROR("1001","Internal system error");
    public String KEY;
    public String VALUE;

    private ResEnum(String key, String value) {
        this.KEY = key;
        this.VALUE = value;
    }
}
