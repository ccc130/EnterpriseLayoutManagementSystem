package com.ruoyi.layout.mapper;

import java.util.List;
import com.ruoyi.layout.domain.LayoutNotice;

/**
 * 通知公告Mapper接口
 * 
 * @author 焦阳1
 * @date 2025-11-28
 */
public interface LayoutNoticeMapper 
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
     * 删除通知公告
     * 
     * @param noticeId 通知公告主键
     * @return 结果
     */
    public int deleteLayoutNoticeByNoticeId(Long noticeId);

    /**
     * 批量删除通知公告
     * 
     * @param noticeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLayoutNoticeByNoticeIds(Long[] noticeIds);
}
