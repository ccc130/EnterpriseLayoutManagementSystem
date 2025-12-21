package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.LayoutEmployeesMapper;
import com.ruoyi.system.domain.LayoutEmployees;
import com.ruoyi.system.service.ILayoutEmployeesService;

/**
 * 员工信息Service业务层处理
 * 
 * @author 焦阳
 * @date 2025-11-27
 */
@Service
public class LayoutEmployeesServiceImpl implements ILayoutEmployeesService 
{
    @Autowired
    private LayoutEmployeesMapper layoutEmployeesMapper;

    /**
     * 查询员工信息
     * 
     * @param id 员工信息主键
     * @return 员工信息
     */
    @Override
    public LayoutEmployees selectLayoutEmployeesById(Long id)
    {
        return layoutEmployeesMapper.selectLayoutEmployeesById(id);
    }

    /**
     * 查询员工信息列表
     * 
     * @param layoutEmployees 员工信息
     * @return 员工信息
     */
    @Override
    public List<LayoutEmployees> selectLayoutEmployeesList(LayoutEmployees layoutEmployees)
    {
        return layoutEmployeesMapper.selectLayoutEmployeesList(layoutEmployees);
    }

    /**
     * 新增员工信息
     * 
     * @param layoutEmployees 员工信息
     * @return 结果
     */
    @Override
    public int insertLayoutEmployees(LayoutEmployees layoutEmployees)
    {
        return layoutEmployeesMapper.insertLayoutEmployees(layoutEmployees);
    }

    /**
     * 修改员工信息
     * 
     * @param layoutEmployees 员工信息
     * @return 结果
     */
    @Override
    public int updateLayoutEmployees(LayoutEmployees layoutEmployees)
    {
        return layoutEmployeesMapper.updateLayoutEmployees(layoutEmployees);
    }

    /**
     * 批量删除员工信息
     * 
     * @param ids 需要删除的员工信息主键
     * @return 结果
     */
    @Override
    public int deleteLayoutEmployeesByIds(Long[] ids)
    {
        return layoutEmployeesMapper.deleteLayoutEmployeesByIds(ids);
    }

    /**
     * 删除员工信息信息
     * 
     * @param id 员工信息主键
     * @return 结果
     */
    @Override
    public int deleteLayoutEmployeesById(Long id)
    {
        return layoutEmployeesMapper.deleteLayoutEmployeesById(id);
    }
}
