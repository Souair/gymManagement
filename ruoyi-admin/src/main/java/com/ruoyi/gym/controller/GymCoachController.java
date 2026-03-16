package com.ruoyi.gym.controller;

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
import com.ruoyi.gym.domain.GymCoach;
import com.ruoyi.gym.service.IGymCoachService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 教练信息Controller
 * 
 * @author zzw
 * @date 2025-12-24
 */

@RestController
@RequestMapping("/gym/coach")
public class GymCoachController extends BaseController
{
    @Autowired
    private IGymCoachService gymCoachService;

    /**
     * 查询教练信息列表
     */
    @PreAuthorize("@ss.hasPermi('gym:coach:list')")
    @GetMapping("/list")
    public TableDataInfo list(GymCoach gymCoach)
    {
        startPage();
        List<GymCoach> list = gymCoachService.selectGymCoachList(gymCoach);
        return getDataTable(list);
    }

    /**
     * 导出教练信息列表
     */
    @PreAuthorize("@ss.hasPermi('gym:coach:export')")
    @Log(title = "教练信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GymCoach gymCoach)
    {
        List<GymCoach> list = gymCoachService.selectGymCoachList(gymCoach);
        ExcelUtil<GymCoach> util = new ExcelUtil<GymCoach>(GymCoach.class);
        util.exportExcel(response, list, "教练信息数据");
    }

    /**
     * 获取教练信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('gym:coach:query')")
    @GetMapping(value = "/{coachId}")
    public AjaxResult getInfo(@PathVariable("coachId") Long coachId)
    {
        return success(gymCoachService.selectGymCoachByCoachId(coachId));
    }

    /**
     * 新增教练信息
     */
    @PreAuthorize("@ss.hasPermi('gym:coach:add')")
    @Log(title = "教练信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GymCoach gymCoach)
    {
        return toAjax(gymCoachService.insertGymCoach(gymCoach));
    }

    /**
     * 修改教练信息
     */
    @PreAuthorize("@ss.hasPermi('gym:coach:edit')")
    @Log(title = "教练信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GymCoach gymCoach)
    {
        return toAjax(gymCoachService.updateGymCoach(gymCoach));
    }

    /**
     * 删除教练信息
     */
    @PreAuthorize("@ss.hasPermi('gym:coach:remove')")
    @Log(title = "教练信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{coachIds}")
    public AjaxResult remove(@PathVariable Long[] coachIds)
    {
        return toAjax(gymCoachService.deleteGymCoachByCoachIds(coachIds));
    }
}
