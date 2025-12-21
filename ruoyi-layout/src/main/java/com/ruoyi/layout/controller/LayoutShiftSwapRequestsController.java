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
import com.ruoyi.layout.domain.LayoutShiftSwapRequests;
import com.ruoyi.layout.service.ILayoutShiftSwapRequestsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 替换申请Controller
 * 
 * @author 焦阳
 * @date 2025-11-25
 */
@RestController
@RequestMapping("/requests/requests")
public class LayoutShiftSwapRequestsController extends BaseController
{
    @Autowired
    private ILayoutShiftSwapRequestsService layoutShiftSwapRequestsService;

    /**
     * 查询替换申请列表
     */
    @PreAuthorize("@ss.hasPermi('requests:requests:list')")
    @GetMapping("/list")
    public TableDataInfo list(LayoutShiftSwapRequests layoutShiftSwapRequests)
    {
        startPage();
        List<LayoutShiftSwapRequests> list = layoutShiftSwapRequestsService.selectLayoutShiftSwapRequestsList(layoutShiftSwapRequests);
        return getDataTable(list);
    }

    /**
     * 导出替换申请列表
     */
    @PreAuthorize("@ss.hasPermi('requests:requests:export')")
    @Log(title = "替换申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LayoutShiftSwapRequests layoutShiftSwapRequests)
    {
        List<LayoutShiftSwapRequests> list = layoutShiftSwapRequestsService.selectLayoutShiftSwapRequestsList(layoutShiftSwapRequests);
        ExcelUtil<LayoutShiftSwapRequests> util = new ExcelUtil<LayoutShiftSwapRequests>(LayoutShiftSwapRequests.class);
        util.exportExcel(response, list, "替换申请数据");
    }

    /**
     * 获取替换申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('requests:requests:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(layoutShiftSwapRequestsService.selectLayoutShiftSwapRequestsById(id));
    }

    /**
     * 新增替换申请
     */
    @PreAuthorize("@ss.hasPermi('requests:requests:add')")
    @Log(title = "替换申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LayoutShiftSwapRequests layoutShiftSwapRequests)
    {
        return toAjax(layoutShiftSwapRequestsService.insertLayoutShiftSwapRequests(layoutShiftSwapRequests));
    }

    /**
     * 修改替换申请
     */
    @PreAuthorize("@ss.hasPermi('requests:requests:edit')")
    @Log(title = "替换申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LayoutShiftSwapRequests layoutShiftSwapRequests)
    {
        return toAjax(layoutShiftSwapRequestsService.updateLayoutShiftSwapRequests(layoutShiftSwapRequests));
    }

    /**
     * 删除替换申请
     */
    @PreAuthorize("@ss.hasPermi('requests:requests:remove')")
    @Log(title = "替换申请", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(layoutShiftSwapRequestsService.deleteLayoutShiftSwapRequestsByIds(ids));
    }
}
