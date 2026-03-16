package com.ruoyi.gym.mapper;

import java.util.List;
import com.ruoyi.gym.domain.GymPrivateBooking;

/**
 * 私教预约Mapper接口
 * 
 * @author zzw
 * @date 2025-12-24
 */
public interface GymPrivateBookingMapper 
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
     * 删除私教预约
     * 
     * @param id 私教预约主键
     * @return 结果
     */
    public int deleteGymPrivateBookingById(Long id);

    /**
     * 批量删除私教预约
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGymPrivateBookingByIds(Long[] ids);
}
