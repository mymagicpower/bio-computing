package me.aias.service;

import me.aias.domain.MolInfoDto;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 文本服务接口
 * Text Service Interface
 *
 * @author Calvin
 * @date 2021-12-19
 **/
public interface MolService {
    /**
     * 根据ID查询
     * Find mol by ID
     */
    MolInfoDto findById(Long id);

    /**
     * 添加文本
     * add text
     */
    public void addText(MolInfoDto textInfoDto);

    /**
     * 添加文本
     * add text
     */
    public void addTexts(List<MolInfoDto> texts);

    /**
     * 获取分子清单
     * get mol list
     */
    ConcurrentHashMap<Long, MolInfoDto> getMap();
}