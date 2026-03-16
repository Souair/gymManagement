package com.ruoyi.gym.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.gym.domain.GymCoach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.gym.mapper.GymPrivateBookingMapper;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.gym.domain.GymPrivateBooking;
import com.ruoyi.gym.mapper.GymCoachMapper;
import com.ruoyi.gym.service.IGymPrivateBookingService;
import com.ruoyi.gym.domain.GymMember;
import com.ruoyi.gym.mapper.GymMemberMapper;

/**
 * 私教预约Service业务层处理
 * 
 * @author zzw
 * @date 2025-12-24
 */
@Service
public class GymPrivateBookingServiceImpl implements IGymPrivateBookingService 
{
    @Autowired
    private GymPrivateBookingMapper gymPrivateBookingMapper;

    @Autowired
    private GymCoachMapper gymCoachMapper;

    // 👇 2. 新增：注入会员Mapper，我们要用它查余额、扣余额
    @Autowired
    private GymMemberMapper gymMemberMapper;

    /**
     * 查询私教预约
     * 
     * @param id 私教预约主键
     * @return 私教预约
     */
    @Override
    public GymPrivateBooking selectGymPrivateBookingById(Long id)
    {
        return gymPrivateBookingMapper.selectGymPrivateBookingById(id);
    }

    /**
     * 查询私教预约列表
     * 
     * @param gymPrivateBooking 私教预约
     * @return 私教预约
     */
    @Override
    public List<GymPrivateBooking> selectGymPrivateBookingList(GymPrivateBooking gymPrivateBooking)
    {
        return gymPrivateBookingMapper.selectGymPrivateBookingList(gymPrivateBooking);
    }

    /**
     * 新增私教预约
     * 
     * @param gymPrivateBooking 私教预约
     * @return 结果
     */
    @Override
    public int insertGymPrivateBooking(GymPrivateBooking gymPrivateBooking)
    {
        // 👇 2. 新增前进行校验
        checkBookingValid(gymPrivateBooking);

        // A. 获取会员信息
        Long memberId = gymPrivateBooking.getMemberId();
        GymMember member = gymMemberMapper.selectGymMemberByMemberId(memberId);

        if (member == null) {
            throw new ServiceException("预约失败：会员不存在");
        }

        // B. 计算本次消耗课时 (把 BigDecimal 转成 Long，假设1小时=1课时)
        // 注意：如果你的duration是1.5小时，这里取整可能不准。
        // 为了简单，我们这里假设只支持整数课时，向下取整。
        long cost = gymPrivateBooking.getDuration().longValue();
        if (cost <= 0) {
            cost = 1; // 防止填了0.5小时不做扣减，最少扣1课时
        }

        // C. 检查余额
        Long currentHours = member.getClassHours();
        // 防止空指针
        if (currentHours == null) {
            currentHours = 0L;
        }

        if (currentHours < cost) {
            throw new ServiceException("预约失败：会员 [" + member.getName() + "] 剩余课时不足！当前剩余: " + currentHours);
        }

        // D. 扣减余额并更新
        member.setClassHours(currentHours - cost);
        gymMemberMapper.updateGymMember(member);

        // ================== ⭐ 新增：课时扣减逻辑结束 ==================

        gymPrivateBooking.setCreateTime(DateUtils.getNowDate());
        return gymPrivateBookingMapper.insertGymPrivateBooking(gymPrivateBooking);
    }

    /**
     * 修改私教预约
     * 
     * @param gymPrivateBooking 私教预约
     * @return 结果
     */
    @Override
    public int updateGymPrivateBooking(GymPrivateBooking gymPrivateBooking)
    {
        // 👇 3. 修改前也要校验
        checkBookingValid(gymPrivateBooking);
        gymPrivateBooking.setUpdateTime(DateUtils.getNowDate());
        return gymPrivateBookingMapper.updateGymPrivateBooking(gymPrivateBooking);
    }

    @Override
    public GymCoach recommendCoach(String specialty, Date bookingDate, BigDecimal duration) {
        // 1. 先进行基础的时间合法性校验（借用之前的逻辑，简化版）
        // 如果用户选了午休时间，直接告诉前端没人
        Calendar cal = Calendar.getInstance();
        cal.setTime(bookingDate);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour < 9 || hour >= 21 || (hour >= 12 && hour < 14) || (hour >= 18 && hour < 19)) {
            throw new ServiceException("当前选择的时间是休息/非工作时间，无法推荐");
        }

        // 2. 找出所有“专业对口”的教练
        List<GymCoach> candidates = gymCoachMapper.selectCoachBySpecialty(specialty);
        if (candidates.isEmpty()) {
            throw new ServiceException("没有找到擅长 [" + specialty + "] 的教练");
        }

        // 3. 遍历候选人，寻找有空的，并记录他们的今日单量
        GymCoach bestCoach = null;
        int minBookings = Integer.MAX_VALUE;

        for (GymCoach coach : candidates) {
            // 3.1 检查该教练在这个时间段是否有空
            if (isCoachAvailable(coach.getCoachId(), bookingDate, duration)) {

                // 3.2 负载均衡：查询该教练今天的预约数 (这里简化写，实际建议写SQL count)
                GymPrivateBooking query = new GymPrivateBooking();
                query.setCoachId(coach.getCoachId());
                // 设置查询当天的范围... (此处省略详细日期范围代码，简化为查总数演示)
                // 简单策略：直接返回第一个匹配的，或者你可以继续完善比对谁的单子少
                return coach; // 找到第一个有空的直接返回
            }
        }

        if (bestCoach == null) {
            throw new ServiceException("该时段所有擅长 [" + specialty + "] 的教练都已约满");
        }
        return bestCoach;
    }

    /**
     * 辅助方法：检查某教练某时段是否可用 (返回布尔值，不抛异常)
     */
    private boolean isCoachAvailable(Long coachId, Date startTime, BigDecimal duration) {
        // 计算结束时间
        long durationMillis = (long) (duration.doubleValue() * 60 * 60 * 1000);
        Date endTime = new Date(startTime.getTime() + durationMillis);
        long newStart = startTime.getTime();
        long newEnd = endTime.getTime();

        // 查询该教练的所有预约
        GymPrivateBooking query = new GymPrivateBooking();
        query.setCoachId(coachId);
        List<GymPrivateBooking> bookings = gymPrivateBookingMapper.selectGymPrivateBookingList(query);

        for (GymPrivateBooking exist : bookings) {
            // 排除已取消
            if ("2".equals(exist.getStatus())) continue;

            Date existStart = exist.getBookingDate();
            long existDurationMillis = (long) (exist.getDuration().doubleValue() * 60 * 60 * 1000);
            Date existEnd = new Date(existStart.getTime() + existDurationMillis);

            // 检查重叠
            if (newStart < existEnd.getTime() && newEnd > existStart.getTime()) {
                return false; // 冲突
            }
        }
        return true; // 可用
    }

    /**
     * 核心校验逻辑：上班时间、专业匹配、时间冲突
     */
    private void checkBookingValid(GymPrivateBooking booking) {
        // A. 校验上班时间 (9:00 - 21:00)
        Date startTime = booking.getBookingDate();
        BigDecimal duration = booking.getDuration(); // 小时

        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        int startHour = cal.get(Calendar.HOUR_OF_DAY);

        // 计算结束时间
        long durationMillis = (long) (duration.doubleValue() * 60 * 60 * 1000);
        Date endTime = new Date(startTime.getTime() + durationMillis);
        cal.setTime(endTime);
        int endHour = cal.get(Calendar.HOUR_OF_DAY);
        int endMinute = cal.get(Calendar.MINUTE);

        // 判断：开始时间早于9点，或者结束时间晚于21点 (21:00整是可以的，超过就不行)
        if (startHour < 9) {
            throw new ServiceException("预约失败：教练上班时间为 09:00 - 21:00");
        }
        if (endHour > 21 || (endHour == 21 && endMinute > 0)) {
            throw new ServiceException("预约失败：课程结束时间不能超过 21:00");
        }

        // B. 校验专业匹配
        GymCoach coach = gymCoachMapper.selectGymCoachByCoachId(booking.getCoachId());
        if (coach == null) {
            throw new ServiceException("预约失败：教练不存在");
        }
        // 如果预约的类型（目标）和教练的专业不包含关系 (假设简单字符串匹配)
        // 注意：这里要做空指针判断
        String target = booking.getBookingType(); // 比如 "增肌"
        String specialty = coach.getSpecialty();  // 比如 "增肌,减脂"

        if (target != null && specialty != null && !specialty.contains(target)) {
            throw new ServiceException("预约失败：该教练擅长[" + specialty + "]，不负责[" + target + "]项目");
        }

        // C. 校验同一时段是否冲突
        // 查询该教练的所有预约（为了简单，这里查出该教练当天的所有预约进行比对）
        GymPrivateBooking query = new GymPrivateBooking();
        query.setCoachId(booking.getCoachId());
        // 这里最好只查当天的，防止数据量太大。实际项目建议在Mapper写SQL查重叠，这里用Java过滤演示逻辑：
        List<GymPrivateBooking> existingBookings = gymPrivateBookingMapper.selectGymPrivateBookingList(query);

        long newStart = startTime.getTime();
        long newEnd = endTime.getTime();

        for (GymPrivateBooking exist : existingBookings) {
            // 排除掉自己（修改的时候）
            if (exist.getId().equals(booking.getId())) {
                continue;
            }
            // 排除已取消/已删除的订单 (假设status '2'是取消，根据你的业务调整)
            // if ("2".equals(exist.getStatus())) continue;

            Date existStart = exist.getBookingDate();
            long existDurationMillis = (long) (exist.getDuration().doubleValue() * 60 * 60 * 1000);
            Date existEnd = new Date(existStart.getTime() + existDurationMillis);

            long oldStart = existStart.getTime();
            long oldEnd = existEnd.getTime();

            // 判断时间重叠公式：!(新结束 <= 旧开始 || 新开始 >= 旧结束)
            // 即：新开始 < 旧结束 && 新结束 > 旧开始
            if (newStart < oldEnd && newEnd > oldStart) {
                throw new ServiceException("预约失败：该时段教练已有预约 (" +
                        DateUtils.parseDateToStr("HH:mm", existStart) + "-" +
                        DateUtils.parseDateToStr("HH:mm", existEnd) + ")");
            }
        }
    }
    /**
     * 批量删除私教预约
     * 
     * @param ids 需要删除的私教预约主键
     * @return 结果
     */
    @Override
    public int deleteGymPrivateBookingByIds(Long[] ids)
    {
        // 遍历所有要删除的预约，把课时退回去
        for (Long id : ids) {
            GymPrivateBooking booking = gymPrivateBookingMapper.selectGymPrivateBookingById(id);
            if (booking != null) {
                // 1. 找到该预约的会员
                GymMember member = gymMemberMapper.selectGymMemberByMemberId(booking.getMemberId());
                if (member != null) {
                    // 2. 计算要退还的课时
                    long refund = booking.getDuration().longValue();
                    if (refund <= 0) refund = 1;

                    // 3. 加回去
                    Long current = member.getClassHours() == null ? 0L : member.getClassHours();
                    member.setClassHours(current + refund);
                    gymMemberMapper.updateGymMember(member);
                }
            }
        }
        return gymPrivateBookingMapper.deleteGymPrivateBookingByIds(ids);
    }

    /**
     * 删除私教预约信息
     * 
     * @param id 私教预约主键
     * @return 结果
     */
    @Override
    public int deleteGymPrivateBookingById(Long id)
    {
        return gymPrivateBookingMapper.deleteGymPrivateBookingById(id);
    }
}
