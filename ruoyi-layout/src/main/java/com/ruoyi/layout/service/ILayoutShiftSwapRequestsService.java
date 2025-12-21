package com.ruoyi.layout.service;

import java.util.List;
import com.ruoyi.layout.domain.LayoutShiftSwapRequests;

/**
 * 替换申请Service接口
 * 
 * @author 焦阳
 * @date 2025-11-25
 */
public interface ILayoutShiftSwapRequestsService 
{
    /**
     * 查询替换申请
     * 
     * @param id 替换申请主键
     * @return 替换申请
     */
    public LayoutShiftSwapRequests selectLayoutShiftSwapRequestsById(Long id);

    /**
     * 查询替换申请列表
     * 
     * @param layoutShiftSwapRequests 替换申请
     * @return 替换申请集合
     */
    public List<LayoutShiftSwapRequests> selectLayoutShiftSwapRequestsList(LayoutShiftSwapRequests layoutShiftSwapRequests);

    /**
     * 新增替换申请
     * 
     * @param layoutShiftSwapRequests 替换申请
     * @return 结果
     */
    public int insertLayoutShiftSwapRequests(LayoutShiftSwapRequests layoutShiftSwapRequests);

    /**
     * 修改替换申请
     * 
     * @param layoutShiftSwapRequests 替换申请
     * @return 结果
     */
    public int updateLayoutShiftSwapRequests(LayoutShiftSwapRequests layoutShiftSwapRequests);

    /**
     * 批量删除替换申请
     * 
     * @param ids 需要删除的替换申请主键集合
     * @return 结果
     */
    public int deleteLayoutShiftSwapRequestsByIds(Long[] ids);

    /**
     * 删除替换申请信息
     * 
     * @param id 替换申请主键
     * @return 结果
     */
    public int deleteLayoutShiftSwapRequestsById(Long id);
}
