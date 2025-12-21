package com.ruoyi.layout.mapper;

import java.util.List;
import com.ruoyi.layout.domain.LayoutShiftSwapRequests;

/**
 * 替换申请Mapper接口
 * 
 * @author 焦阳
 * @date 2025-11-25
 */
public interface LayoutShiftSwapRequestsMapper 
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
     * 删除替换申请
     * 
     * @param id 替换申请主键
     * @return 结果
     */
    public int deleteLayoutShiftSwapRequestsById(Long id);

    /**
     * 批量删除替换申请
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLayoutShiftSwapRequestsByIds(Long[] ids);
}
