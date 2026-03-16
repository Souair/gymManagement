package com.ruoyi.gym.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ruoyi.gym.domain.GymCoach;
import com.ruoyi.gym.domain.GymPrivateBooking;

/**
 * 私教预约Service接口
 * 
 * @author zzw
 * @date 2025-12-24
 */
public interface IGymPrivateBookingService 
{
    /**
     * 查询私教预约
     * 
     * @param id 私教预约主键
     * @return 私教预约
     */
    public GymPrivateBooking selectGymPrivateBookingById(Long id);

    /**
     * 查询私教预约列表
     * 
     * @param gymPrivateBooking 私教预约
     * @return 私教预约集合
     */
    public List<GymPrivateBooking> selectGymPrivateBookingList(GymPrivateBooking gymPrivateBooking);

    /**
     * 新增私教预约
     * 
     * @param gymPrivateBooking 私教预约
     * @return 结果
     */
    public int insertGymPrivateBooking(GymPrivateBooking gymPrivateBooking);

    /**
     * 修改私教预约
     * 
     * @param gymPrivateBooking 私教预约
     * @return 结果
     */
    public int updateGymPrivateBooking(GymPrivateBooking gymPrivateBooking);

    /**
     * 批量删除私教预约
     * 
     * @param ids 需要删除的私教预约主键集合
     * @return 结果
     */
    public int deleteGymPrivateBookingByIds(Long[] ids);

    /**
     * 删除私教预约信息
     * 
     * @param id 私教预约主键
     * @return 结果
     */
    public int deleteGymPrivateBookingById(Long id);
    /**
     * 智能推荐教练
     * @param specialty 训练目标
     * @param bookingDate 预约时间
     * @param duration 时长
     * @return 推荐的教练对象
     */
    public GymCoach recommendCoach(String specialty, Date bookingDate, BigDecimal duration);
}
