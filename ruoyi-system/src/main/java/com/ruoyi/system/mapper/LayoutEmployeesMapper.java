package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.LayoutEmployees;

/**
 * 员工信息Mapper接口
 * 
 * @author 焦阳
 * @date 2025-11-27
 */
public interface LayoutEmployeesMapper 
{
    /**
     * 查询员工信息
     * 
     * @param id 员工信息主键
     * @return 员工信息
     */
    public LayoutEmployees selectLayoutEmployeesById(Long id);

    /**
     * 查询员工信息列表
     * 
     * @param layoutEmployees 员工信息
     * @return 员工信息集合
     */
    public List<LayoutEmployees> selectLayoutEmployeesList(LayoutEmployees layoutEmployees);

    /**
     * 新增员工信息
     * 
     * @param layoutEmployees 员工信息
     * @return 结果
     */
    public int insertLayoutEmployees(LayoutEmployees layoutEmployees);

    /**
     * 修改员工信息
     * 
     * @param layoutEmployees 员工信息
     * @return 结果
     */
    public int updateLayoutEmployees(LayoutEmployees layoutEmployees);

    /**
     * 删除员工信息
     * 
     * @param id 员工信息主键
     * @return 结果
     */
    public int deleteLayoutEmployeesById(Long id);

    /**
     * 批量删除员工信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLayoutEmployeesByIds(Long[] ids);
}
