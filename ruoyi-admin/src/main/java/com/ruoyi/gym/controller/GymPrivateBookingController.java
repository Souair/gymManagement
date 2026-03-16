package com.ruoyi.gym.controller;

import java.util.List;
import java.math.BigDecimal;
import java.util.Date;
import com.ruoyi.common.utils.DateUtils; // 若依自带的日期工具
import com.ruoyi.gym.domain.GymCoach;   // 教练实体
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
import com.ruoyi.gym.domain.GymPrivateBooking;
import com.ruoyi.gym.service.IGymPrivateBookingService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 私教预约Controller
 * 
 * @author zzw
 * @date 2025-12-24
 */
@RestController
@RequestMapping("/gym/booking")
public class GymPrivateBookingController extends BaseController
{
    @Autowired
    private IGymPrivateBookingService gymPrivateBookingService;

    /**
     * 查询私教预约列表
     */
    @PreAuthorize("@ss.hasPermi('gym:booking:list')")
    @GetMapping("/list")
    public TableDataInfo list(GymPrivateBooking gymPrivateBooking)
    {
        startPage();
        List<GymPrivateBooking> list = gymPrivateBookingService.selectGymPrivateBookingList(gymPrivateBooking);
        return getDataTable(list);
    }

    /**
     * 导出私教预约列表
     */
    @PreAuthorize("@ss.hasPermi('gym:booking:export')")
    @Log(title = "私教预约", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GymPrivateBooking gymPrivateBooking)
    {
        List<GymPrivateBooking> list = gymPrivateBookingService.selectGymPrivateBookingList(gymPrivateBooking);
        ExcelUtil<GymPrivateBooking> util = new ExcelUtil<GymPrivateBooking>(GymPrivateBooking.class);
        util.exportExcel(response, list, "私教预约数据");
    }

    /**
     * 获取私教预约详细信息
     */
    @PreAuthorize("@ss.hasPermi('gym:booking:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(gymPrivateBookingService.selectGymPrivateBookingById(id));
    }

    /**
     * 新增私教预约
     */
    @PreAuthorize("@ss.hasPermi('gym:booking:add')")
    @Log(title = "私教预约", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GymPrivateBooking gymPrivateBooking)
    {
        return toAjax(gymPrivateBookingService.insertGymPrivateBooking(gymPrivateBooking));
    }

    /**
     * 修改私教预约
     */
    @PreAuthorize("@ss.hasPermi('gym:booking:edit')")
    @Log(title = "私教预约", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GymPrivateBooking gymPrivateBooking)
    {
        return toAjax(gymPrivateBookingService.updateGymPrivateBooking(gymPrivateBooking));
    }

    /**
     * 删除私教预约
     */
    @PreAuthorize("@ss.hasPermi('gym:booking:remove')")
    @Log(title = "私教预约", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(gymPrivateBookingService.deleteGymPrivateBookingByIds(ids));
    }

    /**
     * 智能推荐教练
     * 路径使用 /action/recommend 避免与 getInfo(@PathVariable Long id) 冲突
     */
    @GetMapping("/action/recommend")
    public AjaxResult recommend(String specialty, String bookingDate, BigDecimal duration)
    {
        // 1. 将前端传来的 String 时间转为 Date 对象
        Date date = DateUtils.parseDate(bookingDate);

        // 2. 调用 Service 层进行计算
        GymCoach coach = gymPrivateBookingService.recommendCoach(specialty, date, duration);

        // 3. 返回结果
        return AjaxResult.success(coach);
    }
}
