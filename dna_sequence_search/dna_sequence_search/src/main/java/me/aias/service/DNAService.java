package me.aias.service;

import me.aias.domain.DNAInfoDto;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文本服务接口
 * Text Service Interface
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public interface DNAService {
    /**
     * 根据ID查询
     * Find dna by ID
     */
    DNAInfoDto findById(Long id);

    /**
     * 添加文本
     * add text
     */
    public void addText(DNAInfoDto textInfoDto);

    /**
     * 添加文本
     * add texts
     */
    public void addTexts(List<DNAInfoDto> texts);

    /**
     * 获取DNA清单
     * get dna list
     */
    ConcurrentHashMap<Long, DNAInfoDto> getMap();
}