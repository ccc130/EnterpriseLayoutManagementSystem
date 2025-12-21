package com.ruoyi.layout.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 替换申请对象 layout_shift_swap_requests
 * 
 * @author 焦阳
 * @date 2025-11-25
 */
public class LayoutShiftSwapRequests extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 申请id */
    private Long id;

    /** 申请人id */
    @Excel(name = "申请人id")
    private Long requesterId;

    /** 目标人id */
    @Excel(name = "目标人id")
    private Long targetId;

    /** 选择换班、替班 */
    @Excel(name = "选择换班、替班")
    private Long model;

    /** 请求替换的班次id */
    @Excel(name = "请求替换的班次id")
    private Long requestedShiftId;

    /** 替换的班次id */
    @Excel(name = "替换的班次id")
    private Long swappedShiftId;

    /** 申请日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "申请日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date requestDate;

    /** 状态(待审批、已批准、已拒绝) */
    @Excel(name = "状态(待审批、已批准、已拒绝)")
    private String status;

    /** 申请原因 */
    @Excel(name = "申请原因")
    private String reason;

    /** 审批人id */
    @Excel(name = "审批人id")
    private Long approvedBy;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审批时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approvedAt;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setRequesterId(Long requesterId) 
    {
        this.requesterId = requesterId;
    }

    public Long getRequesterId() 
    {
        return requesterId;
    }

    public void setTargetId(Long targetId) 
    {
        this.targetId = targetId;
    }

    public Long getTargetId() 
    {
        return targetId;
    }

    public void setModel(Long model) 
    {
        this.model = model;
    }

    public Long getModel() 
    {
        return model;
    }

    public void setRequestedShiftId(Long requestedShiftId) 
    {
        this.requestedShiftId = requestedShiftId;
    }

    public Long getRequestedShiftId() 
    {
        return requestedShiftId;
    }

    public void setSwappedShiftId(Long swappedShiftId) 
    {
        this.swappedShiftId = swappedShiftId;
    }

    public Long getSwappedShiftId() 
    {
        return swappedShiftId;
    }

    public void setRequestDate(Date requestDate) 
    {
        this.requestDate = requestDate;
    }

    public Date getRequestDate() 
    {
        return requestDate;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setReason(String reason) 
    {
        this.reason = reason;
    }

    public String getReason() 
    {
        return reason;
    }

    public void setApprovedBy(Long approvedBy) 
    {
        this.approvedBy = approvedBy;
    }

    public Long getApprovedBy() 
    {
        return approvedBy;
    }

    public void setApprovedAt(Date approvedAt) 
    {
        this.approvedAt = approvedAt;
    }

    public Date getApprovedAt() 
    {
        return approvedAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("requesterId", getRequesterId())
            .append("targetId", getTargetId())
            .append("model", getModel())
            .append("requestedShiftId", getRequestedShiftId())
            .append("swappedShiftId", getSwappedShiftId())
            .append("requestDate", getRequestDate())
            .append("status", getStatus())
            .append("reason", getReason())
            .append("approvedBy", getApprovedBy())
            .append("approvedAt", getApprovedAt())
            .toString();
    }
}
