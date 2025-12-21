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
import com.ruoyi.layout.domain.LayoutScheduling;
import com.ruoyi.layout.service.ILayoutSchedulingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 排班情况Controller
 * 
 * @author 焦阳
 * @date 2025-11-25
 */
@RestController
@RequestMapping("/scheduling/scheduling")
public class LayoutSchedulingController extends BaseController
{
    @Autowired
    private ILayoutSchedulingService layoutSchedulingService;

    /**
     * 查询排班情况列表
     */
    @PreAuthorize("@ss.hasPermi('scheduling:scheduling:list')")
    @GetMapping("/list")
    public TableDataInfo list(LayoutScheduling layoutScheduling)
    {
        startPage();
        List<LayoutScheduling> list = layoutSchedulingService.selectLayoutSchedulingList(layoutScheduling);
        return getDataTable(list);
    }

    /**
     * 导出排班情况列表
     */
    @PreAuthorize("@ss.hasPermi('scheduling:scheduling:export')")
    @Log(title = "排班情况", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, LayoutScheduling layoutScheduling)
    {
        List<LayoutScheduling> list = layoutSchedulingService.selectLayoutSchedulingList(layoutScheduling);
        ExcelUtil<LayoutScheduling> util = new ExcelUtil<LayoutScheduling>(LayoutScheduling.class);
        util.exportExcel(response, list, "排班情况数据");
    }

    /**
     * 获取排班情况详细信息
     */
    @PreAuthorize("@ss.hasPermi('scheduling:scheduling:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(layoutSchedulingService.selectLayoutSchedulingById(id));
    }

    /**
     * 新增排班情况
     */
    @PreAuthorize("@ss.hasPermi('scheduling:scheduling:add')")
    @Log(title = "排班情况", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody LayoutScheduling layoutScheduling)
    {
        return toAjax(layoutSchedulingService.insertLayoutScheduling(layoutScheduling));
    }

    /**
     * 修改排班情况
     */
    @PreAuthorize("@ss.hasPermi('scheduling:scheduling:edit')")
    @Log(title = "排班情况", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody LayoutScheduling layoutScheduling)
    {
        return toAjax(layoutSchedulingService.updateLayoutScheduling(layoutScheduling));
    }

    /**
     * 删除排班情况
     */
    @PreAuthorize("@ss.hasPermi('scheduling:scheduling:remove')")
    @Log(title = "排班情况", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(layoutSchedulingService.deleteLayoutSchedulingByIds(ids));
    }
}
