package com.ruoyi.layout.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.layout.domain.LayoutNotice;
import com.ruoyi.layout.service.ILayoutNoticeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 通知公告Controller
 * 
 * @author 焦阳1
 * @date 2025-11-28
 */
@RestController
@RequestMapping("/notice/notice")
public class LayoutNoticeController extends BaseController
{
    @Autowired
    private ILayoutNoticeService layoutNoticeService;

    /**
     * 查询通知公告列表
     */
    @PreAuthorize("@ss.hasPermi('notice:notice:list')")
    @GetMapping("/list")
    public TableDataInfo list(LayoutNotice layoutNotice)
    {
        startPage();
        List<LayoutNotice> list = layoutNoticeService.selectLayoutNoticeList(layoutNotice);
        return getDataTable(list);
    }

    /**
     * 导出通知公告列表
     */
    @PreAuthorize("@ss.hasPermi('notice:notice:export')")
    @Log(title = "通知公告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LayoutNotice layoutNotice)
    {
        List<LayoutNotice> list = layoutNoticeService.selectLayoutNoticeList(layoutNotice);
        ExcelUtil<LayoutNotice> util = new ExcelUtil<LayoutNotice>(LayoutNotice.class);
        util.exportExcel(response, list, "通知公告数据");
    }

    /**
     * 获取通知公告详细信息
     */
    @PreAuthorize("@ss.hasPermi('notice:notice:query')")
    @GetMapping(value = "/{noticeId}")
    public AjaxResult getInfo(@PathVariable("noticeId") Long noticeId)
    {
        return success(layoutNoticeService.selectLayoutNoticeByNoticeId(noticeId));
    }

    /**
     * 新增通知公告
     */
    @PreAuthorize("@ss.hasPermi('notice:notice:add')")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LayoutNotice layoutNotice)
    {
        return toAjax(layoutNoticeService.insertLayoutNotice(layoutNotice));
    }

    /**
     * 修改通知公告
     */
    @PreAuthorize("@ss.hasPermi('notice:notice:edit')")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LayoutNotice layoutNotice)
    {
        return toAjax(layoutNoticeService.updateLayoutNotice(layoutNotice));
    }

    /**
     * 删除通知公告
     */
    @PreAuthorize("@ss.hasPermi('notice:notice:remove')")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{noticeIds}")
    public AjaxResult remove(@PathVariable Long[] noticeIds)
    {
        return toAjax(layoutNoticeService.deleteLayoutNoticeByNoticeIds(noticeIds));
    }
}
