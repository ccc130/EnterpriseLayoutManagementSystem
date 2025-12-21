package com.ruoyi.layout.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.layout.mapper.LayoutShiftsMapper;
import com.ruoyi.layout.domain.LayoutShifts;
import com.ruoyi.layout.service.ILayoutShiftsService;

/**
 * 班次情况Service业务层处理
 * 
 * @author 焦阳
 * @date 2025-11-25
 */
@Service
public class LayoutShiftsServiceImpl implements ILayoutShiftsService 
{
    @Autowired
    private LayoutShiftsMapper layoutShiftsMapper;

    /**
     * 查询班次情况
     * 
     * @param id 班次情况主键
     * @return 班次情况
     */
    @Override
    public LayoutShifts selectLayoutShiftsById(Long id)
    {
        return layoutShiftsMapper.selectLayoutShiftsById(id);
    }

    /**
     * 查询班次情况列表
     * 
     * @param layoutShifts 班次情况
     * @return 班次情况
     */
    @Override
    public List<LayoutShifts> selectLayoutShiftsList(LayoutShifts layoutShifts)
    {
        return layoutShiftsMapper.selectLayoutShiftsList(layoutShifts);
    }

    /**
     * 新增班次情况
     * 
     * @param layoutShifts 班次情况
     * @return 结果
     */
    @Override
    public int insertLayoutShifts(LayoutShifts layoutShifts)
    {
        return layoutShiftsMapper.insertLayoutShifts(layoutShifts);
    }

    /**
     * 修改班次情况
     * 
     * @param layoutShifts 班次情况
     * @return 结果
     */
    @Override
    public int updateLayoutShifts(LayoutShifts layoutShifts)
    {
        return layoutShiftsMapper.updateLayoutShifts(layoutShifts);
    }

    /**
     * 批量删除班次情况
     * 
     * @param ids 需要删除的班次情况主键
     * @return 结果
     */
    @Override
    public int deleteLayoutShiftsByIds(Long[] ids)
    {
        return layoutShiftsMapper.deleteLayoutShiftsByIds(ids);
    }

    /**
     * 删除班次情况信息
     * 
     * @param id 班次情况主键
     * @return 结果
     */
    @Override
    public int deleteLayoutShiftsById(Long id)
    {
        return layoutShiftsMapper.deleteLayoutShiftsById(id);
    }
}
