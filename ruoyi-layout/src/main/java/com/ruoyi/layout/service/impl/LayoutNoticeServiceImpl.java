package com.ruoyi.layout.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.layout.mapper.LayoutNoticeMapper;
import com.ruoyi.layout.domain.LayoutNotice;
import com.ruoyi.layout.service.ILayoutNoticeService;

/**
 * 通知公告Service业务层处理
 * 
 * @author 焦阳1
 * @date 2025-11-28
 */
@Service
public class LayoutNoticeServiceImpl implements ILayoutNoticeService 
{
    @Autowired
    private LayoutNoticeMapper layoutNoticeMapper;

    /**
     * 查询通知公告
     * 
     * @param noticeId 通知公告主键
     * @return 通知公告
     */
    @Override
    public LayoutNotice selectLayoutNoticeByNoticeId(Long noticeId)
    {
        return layoutNoticeMapper.selectLayoutNoticeByNoticeId(noticeId);
    }

    /**
     * 查询通知公告列表
     * 
     * @param layoutNotice 通知公告
     * @return 通知公告
     */
    @Override
    public List<LayoutNotice> selectLayoutNoticeList(LayoutNotice layoutNotice)
    {
        return layoutNoticeMapper.selectLayoutNoticeList(layoutNotice);
    }

    /**
     * 新增通知公告
     * 
     * @param layoutNotice 通知公告
     * @return 结果
     */
    @Override
    public int insertLayoutNotice(LayoutNotice layoutNotice)
    {
        layoutNotice.setCreateTime(DateUtils.getNowDate());
        return layoutNoticeMapper.insertLayoutNotice(layoutNotice);
    }

    /**
     * 修改通知公告
     * 
     * @param layoutNotice 通知公告
     * @return 结果
     */
    @Override
    public int updateLayoutNotice(LayoutNotice layoutNotice)
    {
        return layoutNoticeMapper.updateLayoutNotice(layoutNotice);
    }

    /**
     * 批量删除通知公告
     * 
     * @param noticeIds 需要删除的通知公告主键
     * @return 结果
     */
    @Override
    public int deleteLayoutNoticeByNoticeIds(Long[] noticeIds)
    {
        return layoutNoticeMapper.deleteLayoutNoticeByNoticeIds(noticeIds);
    }

    /**
     * 删除通知公告信息
     * 
     * @param noticeId 通知公告主键
     * @return 结果
     */
    @Override
    public int deleteLayoutNoticeByNoticeId(Long noticeId)
    {
        return layoutNoticeMapper.deleteLayoutNoticeByNoticeId(noticeId);
    }
}
