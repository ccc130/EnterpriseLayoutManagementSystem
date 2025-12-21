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
import com.ruoyi.layout.domain.LayoutShifts;
import com.ruoyi.layout.service.ILayoutShiftsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 班次情况Controller
 * 
 * @author 焦阳
 * @date 2025-11-25
 */
@RestController
@RequestMapping("/shifts/shifts")
public class LayoutShiftsController extends BaseController
{
    @Autowired
    private ILayoutShiftsService layoutShiftsService;

    /**
     * 查询班次情况列表
     */
    @PreAuthorize("@ss.hasPermi('shifts:shifts:list')")
    @GetMapping("/list")
    public TableDataInfo list(LayoutShifts layoutShifts)
    {
        startPage();
        List<LayoutShifts> list = layoutShiftsService.selectLayoutShiftsList(layoutShifts);
        return getDataTable(list);
    }

    /**
     * 导出班次情况列表
     */
    @PreAuthorize("@ss.hasPermi('shifts:shifts:export')")
    @Log(title = "班次情况", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LayoutShifts layoutShifts)
    {
        List<LayoutShifts> list = layoutShiftsService.selectLayoutShiftsList(layoutShifts);
        ExcelUtil<LayoutShifts> util = new ExcelUtil<LayoutShifts>(LayoutShifts.class);
        util.exportExcel(response, list, "班次情况数据");
    }

    /**
     * 获取班次情况详细信息
     */
    @PreAuthorize("@ss.hasPermi('shifts:shifts:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(layoutShiftsService.selectLayoutShiftsById(id));
    }

    /**
     * 新增班次情况
     */
    @PreAuthorize("@ss.hasPermi('shifts:shifts:add')")
    @Log(title = "班次情况", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LayoutShifts layoutShifts)
    {
        return toAjax(layoutShiftsService.insertLayoutShifts(layoutShifts));
    }

    /**
     * 修改班次情况
     */
    @PreAuthorize("@ss.hasPermi('shifts:shifts:edit')")
    @Log(title = "班次情况", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LayoutShifts layoutShifts)
    {
        return toAjax(layoutShiftsService.updateLayoutShifts(layoutShifts));
    }

    /**
     * 删除班次情况
     */
    @PreAuthorize("@ss.hasPermi('shifts:shifts:remove')")
    @Log(title = "班次情况", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(layoutShiftsService.deleteLayoutShiftsByIds(ids));
    }
}
