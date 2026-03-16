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
import com.ruoyi.gym.domain.GymMember;
import com.ruoyi.gym.service.IGymMemberService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 会员信息Controller
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
@RestController
@RequestMapping("/gym/member")
public class GymMemberController extends BaseController
{
    @Autowired
    private IGymMemberService gymMemberService;

    /**
     * 查询会员信息列表
     */
    @PreAuthorize("@ss.hasPermi('gym:member:list')")
    @GetMapping("/list")
    public TableDataInfo list(GymMember gymMember)
    {
        startPage();
        List<GymMember> list = gymMemberService.selectGymMemberList(gymMember);
        return getDataTable(list);
    }

    /**
     * 导出会员信息列表
     */
    @PreAuthorize("@ss.hasPermi('gym:member:export')")
    @Log(title = "会员信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GymMember gymMember)
    {
        List<GymMember> list = gymMemberService.selectGymMemberList(gymMember);
        ExcelUtil<GymMember> util = new ExcelUtil<GymMember>(GymMember.class);
        util.exportExcel(response, list, "会员信息数据");
    }

    /**
     * 获取会员信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('gym:member:query')")
    @GetMapping(value = "/{memberId}")
    public AjaxResult getInfo(@PathVariable("memberId") Long memberId)
    {
        return success(gymMemberService.selectGymMemberByMemberId(memberId));
    }

    /**
     * 新增会员信息
     */
    @PreAuthorize("@ss.hasPermi('gym:member:add')")
    @Log(title = "会员信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GymMember gymMember)
    {
        return toAjax(gymMemberService.insertGymMember(gymMember));
    }

    /**
     * 修改会员信息
     */
    @PreAuthorize("@ss.hasPermi('gym:member:edit')")
    @Log(title = "会员信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GymMember gymMember)
    {
        return toAjax(gymMemberService.updateGymMember(gymMember));
    }

    /**
     * 删除会员信息
     */
    @PreAuthorize("@ss.hasPermi('gym:member:remove')")
    @Log(title = "会员信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{memberIds}")
    public AjaxResult remove(@PathVariable Long[] memberIds)
    {
        return toAjax(gymMemberService.deleteGymMemberByMemberIds(memberIds));
    }
}
