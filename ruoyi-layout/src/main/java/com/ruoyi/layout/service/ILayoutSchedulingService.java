package com.ruoyi.layout.service;

import java.util.List;
import com.ruoyi.layout.domain.LayoutScheduling;

/**
 * 排班情况Service接口
 * 
 * @author 焦阳
 * @date 2025-11-25
 */
public interface ILayoutSchedulingService 
{
    /**
     * 查询排班情况
     * 
     * @param id 排班情况主键
     * @return 排班情况
     */
    public LayoutScheduling selectLayoutSchedulingById(Long id);

    /**
     * 查询排班情况列表
     * 
     * @param layoutScheduling 排班情况
     * @return 排班情况集合
     */
    public List<LayoutScheduling> selectLayoutSchedulingList(LayoutScheduling layoutScheduling);

    /**
     * 新增排班情况
     * 
     * @param layoutScheduling 排班情况
     * @return 结果
     */
    public int insertLayoutScheduling(LayoutScheduling layoutScheduling);

    /**
     * 修改排班情况
     * 
     * @param layoutScheduling 排班情况
     * @return 结果
     */
    public int updateLayoutScheduling(LayoutScheduling layoutScheduling);

    /**
     * 批量删除排班情况
     * 
     * @param ids 需要删除的排班情况主键集合
     * @return 结果
     */
    public int deleteLayoutSchedulingByIds(Long[] ids);

    /**
     * 删除排班情况信息
     * 
     * @param id 排班情况主键
     * @return 结果
     */
    public int deleteLayoutSchedulingById(Long id);
}
