package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 员工信息对象 layout_employees
 * 
 * @author 焦阳
 * @date 2025-11-27
 */
public class LayoutEmployees extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 员工id */
    private Long id;

    /** 关联用户表id */
    @Excel(name = "关联用户表id")
    private Long userId;

    /** 员工照片 */
    @Excel(name = "员工照片")
    private String picture;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long departmentId;

    /** 入职日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入职日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date hireDate;

    /** 每周工作时长(小时) */
    @Excel(name = "每周工作时长(小时)")
    private Long workHoursPerWeek;

    /** 最大连续工作天数 */
    @Excel(name = "最大连续工作天数")
    private Long maxConsecutiveDays;

    /** 周末出勤偏好 */
    @Excel(name = "周末出勤偏好")
    private String weekendPreference;

    /** 技能标签 */
    @Excel(name = "技能标签")
    private String skills;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setPicture(String picture) 
    {
        this.picture = picture;
    }

    public String getPicture() 
    {
        return picture;
    }

    public void setDepartmentId(Long departmentId) 
    {
        this.departmentId = departmentId;
    }

    public Long getDepartmentId() 
    {
        return departmentId;
    }

    public void setHireDate(Date hireDate) 
    {
        this.hireDate = hireDate;
    }

    public Date getHireDate() 
    {
        return hireDate;
    }

    public void setWorkHoursPerWeek(Long workHoursPerWeek) 
    {
        this.workHoursPerWeek = workHoursPerWeek;
    }

    public Long getWorkHoursPerWeek() 
    {
        return workHoursPerWeek;
    }

    public void setMaxConsecutiveDays(Long maxConsecutiveDays) 
    {
        this.maxConsecutiveDays = maxConsecutiveDays;
    }

    public Long getMaxConsecutiveDays() 
    {
        return maxConsecutiveDays;
    }

    public void setWeekendPreference(String weekendPreference) 
    {
        this.weekendPreference = weekendPreference;
    }

    public String getWeekendPreference() 
    {
        return weekendPreference;
    }

    public void setSkills(String skills) 
    {
        this.skills = skills;
    }

    public String getSkills() 
    {
        return skills;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("picture", getPicture())
            .append("departmentId", getDepartmentId())
            .append("hireDate", getHireDate())
            .append("workHoursPerWeek", getWorkHoursPerWeek())
            .append("maxConsecutiveDays", getMaxConsecutiveDays())
            .append("weekendPreference", getWeekendPreference())
            .append("skills", getSkills())
            .toString();
    }
}
