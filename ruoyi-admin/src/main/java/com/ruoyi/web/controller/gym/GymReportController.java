package com.ruoyi.web.controller.gym;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.gym.domain.GymPrivateBooking;
import com.ruoyi.gym.domain.vo.IndexStatsVo;
import com.ruoyi.gym.mapper.GymCoachMapper;
import com.ruoyi.gym.mapper.GymMemberMapper;
import com.ruoyi.gym.mapper.GymPrivateBookingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/gym/report")
public class GymReportController extends BaseController {

    @Autowired
    private GymMemberMapper memberMapper;
    @Autowired
    private GymCoachMapper coachMapper;
    @Autowired
    private GymPrivateBookingMapper bookingMapper;

    /**
     * 获取首页统计数据
     */
    @GetMapping("/dashboard")
    public AjaxResult getDashboardData() {
        IndexStatsVo stats = new IndexStatsVo();

        // 1. 获取基础数量
        // 注意：selectGymMemberList 需要传参，传 null 或 new 对象查全部
        stats.setMemberCount(memberMapper.selectGymMemberList(null).size());
        stats.setCoachCount(coachMapper.selectGymCoachList(null).size());

        // 2. 获取今日预约数
        GymPrivateBooking todayQuery = new GymPrivateBooking();
        // 这里只是简单演示，严谨写法需要处理成 "2023-12-01 00:00:00" 到 "23:59:59" 的范围查询
        // 或者直接在 SQL 也就是 XML 里写 where date(booking_date) = curdate()
        // 这里假设你有办法查今天的，先暂时模拟查询全部作为演示
        List<GymPrivateBooking> allBookings = bookingMapper.selectGymPrivateBookingList(null);

        // Java层面过滤今日数据 (实际建议写SQL)
        String todayStr = DateUtils.getDate();
        long todayCount = allBookings.stream()
                .filter(b -> DateUtils.parseDateToStr("yyyy-MM-dd", b.getBookingDate()).equals(todayStr))
                .count();
        stats.setTodayBooking((int) todayCount);

        // 3. 计算本月营收 (假设 1课时 = 200元，这里演示逻辑)
        // 实际你应该统计本月的 booking duration 总和 * 单价
        stats.setMonthRevenue(allBookings.size() * 200.0);

        // 4. 构建图表数据 (过去7天趋势)
        List<String> dateList = new ArrayList<>();
        List<Integer> trendData = new ArrayList<>();
        // 简单模拟过去7天，实际需要写 SQL Group By Date
        for (int i = 6; i >= 0; i--) {
            // 获取日期字符串
            // 填充假数据或真实查询...
            dateList.add(DateUtils.parseDateToStr("MM-dd", DateUtils.addDays(DateUtils.getNowDate(), -i)));
            trendData.add(new Random().nextInt(20)); // 这里先用随机数占位，你需要写 SQL 按天分组查询
        }
        stats.setDateList(dateList);
        stats.setBookingTrend(trendData);

        // 5. 构建雷达图数据 (按 bookingType 分组)
        // 顺序对应前端: [增肌, 减脂, 塑形, 康复, 拳击, 瑜伽]
        List<Integer> radarData = Arrays.asList(10, 20, 15, 5, 8, 12); // 这里替换为 select count group by booking_type
        stats.setSpecialtyData(radarData);

        return AjaxResult.success(stats);
    }
}