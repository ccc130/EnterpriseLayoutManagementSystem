package com.ruoyi.layout.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.layout.mapper.LayoutShiftSwapRequestsMapper;
import com.ruoyi.layout.domain.LayoutShiftSwapRequests;
import com.ruoyi.layout.service.ILayoutShiftSwapRequestsService;

/**
 * 替换申请Service业务层处理
 * 
 * @author 焦阳
 * @date 2025-11-25
 */
@Service
public class LayoutShiftSwapRequestsServiceImpl implements ILayoutShiftSwapRequestsService 
{
    @Autowired
    private LayoutShiftSwapRequestsMapper layoutShiftSwapRequestsMapper;

    /**
     * 查询替换申请
     * 
     * @param id 替换申请主键
     * @return 替换申请
     */
    @Override
    public LayoutShiftSwapRequests selectLayoutShiftSwapRequestsById(Long id)
    {
        return layoutShiftSwapRequestsMapper.selectLayoutShiftSwapRequestsById(id);
    }

    /**
     * 查询替换申请列表
     * 
     * @param layoutShiftSwapRequests 替换申请
     * @return 替换申请
     */
    @Override
    public List<LayoutShiftSwapRequests> selectLayoutShiftSwapRequestsList(LayoutShiftSwapRequests layoutShiftSwapRequests)
    {
        return layoutShiftSwapRequestsMapper.selectLayoutShiftSwapRequestsList(layoutShiftSwapRequests);
    }

    /**
     * 新增替换申请
     * 
     * @param layoutShiftSwapRequests 替换申请
     * @return 结果
     */
    @Override
    public int insertLayoutShiftSwapRequests(LayoutShiftSwapRequests layoutShiftSwapRequests)
    {
        return layoutShiftSwapRequestsMapper.insertLayoutShiftSwapRequests(layoutShiftSwapRequests);
    }

    /**
     * 修改替换申请
     * 
     * @param layoutShiftSwapRequests 替换申请
     * @return 结果
     */
    @Override
    public int updateLayoutShiftSwapRequests(LayoutShiftSwapRequests layoutShiftSwapRequests)
    {
        return layoutShiftSwapRequestsMapper.updateLayoutShiftSwapRequests(layoutShiftSwapRequests);
    }

    /**
     * 批量删除替换申请
     * 
     * @param ids 需要删除的替换申请主键
     * @return 结果
     */
    @Override
    public int deleteLayoutShiftSwapRequestsByIds(Long[] ids)
    {
        return layoutShiftSwapRequestsMapper.deleteLayoutShiftSwapRequestsByIds(ids);
    }

    /**
     * 删除替换申请信息
     * 
     * @param id 替换申请主键
     * @return 结果
     */
    @Override
    public int deleteLayoutShiftSwapRequestsById(Long id)
    {
        return layoutShiftSwapRequestsMapper.deleteLayoutShiftSwapRequestsById(id);
    }
}
