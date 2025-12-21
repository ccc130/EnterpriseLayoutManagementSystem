package com.ruoyi.layout.service;

import java.util.List;
import com.ruoyi.layout.domain.LayoutShifts;

/**
 * 班次情况Service接口
 * 
 * @author 焦阳
 * @date 2025-11-25
 */
public interface ILayoutShiftsService 
{
    /**
     * 查询班次情况
     * 
     * @param id 班次情况主键
     * @return 班次情况
     */
    public LayoutShifts selectLayoutShiftsById(Long id);

    /**
     * 查询班次情况列表
     * 
     * @param layoutShifts 班次情况
     * @return 班次情况集合
     */
    public List<LayoutShifts> selectLayoutShiftsList(LayoutShifts layoutShifts);

    /**
     * 新增班次情况
     * 
     * @param layoutShifts 班次情况
     * @return 结果
     */
    public int insertLayoutShifts(LayoutShifts layoutShifts);

    /**
     * 修改班次情况
     * 
     * @param layoutShifts 班次情况
     * @return 结果
     */
    public int updateLayoutShifts(LayoutShifts layoutShifts);

    /**
     * 批量删除班次情况
     * 
     * @param ids 需要删除的班次情况主键集合
     * @return 结果
     */
    public int deleteLayoutShiftsByIds(Long[] ids);

    /**
     * 删除班次情况信息
     * 
     * @param id 班次情况主键
     * @return 结果
     */
    public int deleteLayoutShiftsById(Long id);
}
