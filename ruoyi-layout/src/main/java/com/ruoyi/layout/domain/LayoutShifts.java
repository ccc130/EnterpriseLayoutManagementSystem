package com.ruoyi.layout.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.time.LocalTime;

/**
 * 班次情况对象 layout_shifts
 * 
 * @author 焦阳
 * @date 2025-11-25
 */
public class LayoutShifts extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 班次id */
    private Long id;

    /** 班次名称(如早班、晚班) */
    @Excel(name = "班次名称(如早班、晚班)")
    private String shiftName;

    /** 开始时间 */
    @JsonFormat(pattern = "HH:mm")
    @Excel(name = "开始时间", width = 30, dateFormat = "HH:mm")
    private LocalTime starttime;

    /** 结束时间 */
    @JsonFormat(pattern = "HH:mm")
    @Excel(name = "结束时间", width = 30, dateFormat = "HH:mm")
    private LocalTime endtime;

    /** 班次类型(如正常班、夜班) */
    @Excel(name = "班次类型(如正常班、夜班)")
    private String shiftType;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setShiftName(String shiftName) 
    {
        this.shiftName = shiftName;
    }

    public String getShiftName() 
    {
        return shiftName;
    }

    public void setStarttime(LocalTime starttime)
    {
        this.starttime = starttime;
    }

    public LocalTime getStarttime()
    {
        return starttime;
    }

    public void setEndtime(LocalTime endtime)
    {
        this.endtime = endtime;
    }

    public LocalTime getEndtime()
    {
        return endtime;
    }

    public void setShiftType(String shiftType) 
    {
        this.shiftType = shiftType;
    }

    public String getShiftType() 
    {
        return shiftType;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("shiftName", getShiftName())
            .append("starttime", getStarttime())
            .append("endtime", getEndtime())
            .append("shiftType", getShiftType())
            .append("description", getDescription())
            .toString();
    }
}
