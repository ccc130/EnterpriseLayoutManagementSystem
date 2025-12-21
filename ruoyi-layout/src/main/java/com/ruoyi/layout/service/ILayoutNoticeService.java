package com.ruoyi.layout.service;

import java.util.List;
import com.ruoyi.layout.domain.LayoutNotice;

/**
 * 通知公告Service接口
 * 
 * @author 焦阳1
 * @date 2025-11-28
 */
public interface ILayoutNoticeService 
{
    /**
     * 查询通知公告
     * 
     * @param noticeId 通知公告主键
     * @return 通知公告
     */
    public LayoutNotice selectLayoutNoticeByNoticeId(Long noticeId);

    /**
     * 查询通知公告列表
     * 
     * @param layoutNotice 通知公告
     * @return 通知公告集合
     */
    public List<LayoutNotice> selectLayoutNoticeList(LayoutNotice layoutNotice);

    /**
     * 新增通知公告
     * 
     * @param layoutNotice 通知公告
     * @return 结果
     */
    public int insertLayoutNotice(LayoutNotice layoutNotice);

    /**
     * 修改通知公告
     * 
     * @param layoutNotice 通知公告
     * @return 结果
     */
    public int updateLayoutNotice(LayoutNotice layoutNotice);

    /**
     * 批量删除通知公告
     * 
     * @param noticeIds 需要删除的通知公告主键集合
     * @return 结果
     */
    public int deleteLayoutNoticeByNoticeIds(Long[] noticeIds);

    /**
     * 删除通知公告信息
     * 
     * @param noticeId 通知公告主键
     * @return 结果
     */
    public int deleteLayoutNoticeByNoticeId(Long noticeId);
}
