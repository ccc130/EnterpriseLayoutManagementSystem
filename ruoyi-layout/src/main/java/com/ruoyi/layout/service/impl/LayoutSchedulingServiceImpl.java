package com.ruoyi.layout.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.layout.mapper.LayoutSchedulingMapper;
import com.ruoyi.layout.domain.LayoutScheduling;
import com.ruoyi.layout.service.ILayoutSchedulingService;

/**
 * 排班情况Service业务层处理
 * 
 * @author 焦阳
 * @date 2025-11-25
 */
@Service
public class LayoutSchedulingServiceImpl implements ILayoutSchedulingService 
{
    @Autowired
    private LayoutSchedulingMapper layoutSchedulingMapper;

    /**
     * 查询排班情况
     * 
     * @param id 排班情况主键
     * @return 排班情况
     */
    @Override
    public LayoutScheduling selectLayoutSchedulingById(Long id)
    {
        return layoutSchedulingMapper.selectLayoutSchedulingById(id);
    }

    /**
     * 查询排班情况列表
     * 
     * @param layoutScheduling 排班情况
     * @return 排班情况
     */
    @Override
    public List<LayoutScheduling> selectLayoutSchedulingList(LayoutScheduling layoutScheduling)
    {
        return layoutSchedulingMapper.selectLayoutSchedulingList(layoutScheduling);
    }

    /**
     * 新增排班情况
     * 
     * @param layoutScheduling 排班情况
     * @return 结果
     */
    @Override
    public int insertLayoutScheduling(LayoutScheduling layoutScheduling)
    {
        return layoutSchedulingMapper.insertLayoutScheduling(layoutScheduling);
    }

    /**
     * 修改排班情况
     * 
     * @param layoutScheduling 排班情况
     * @return 结果
     */
    @Override
    public int updateLayoutScheduling(LayoutScheduling layoutScheduling)
    {
        return layoutSchedulingMapper.updateLayoutScheduling(layoutScheduling);
    }

    /**
     * 批量删除排班情况
     * 
     * @param ids 需要删除的排班情况主键
     * @return 结果
     */
    @Override
    public int deleteLayoutSchedulingByIds(Long[] ids)
    {
        return layoutSchedulingMapper.deleteLayoutSchedulingByIds(ids);
    }

    /**
     * 删除排班情况信息
     * 
     * @param id 排班情况主键
     * @return 结果
     */
    @Override
    public int deleteLayoutSchedulingById(Long id)
    {
        return layoutSchedulingMapper.deleteLayoutSchedulingById(id);
    }
}
