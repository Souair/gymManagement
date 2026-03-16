package com.ruoyi.gym.mapper;

import java.util.List;
import com.ruoyi.gym.domain.GymCoach;

/**
 * 教练信息Mapper接口
 * 
 * @author zzw
 * @date 2025-12-24
 */
public interface GymCoachMapper 
{
    /**
     * 查询教练信息
     * 
     * @param coachId 教练信息主键
     * @return 教练信息
     */
    public GymCoach selectGymCoachByCoachId(Long coachId);

    /**
     * 查询教练信息列表
     * 
     * @param gymCoach 教练信息
     * @return 教练信息集合
     */
    public List<GymCoach> selectGymCoachList(GymCoach gymCoach);

    /**
     * 根据专业筛选在职教练 (这是刚才为了智能派单新增的)
     */
    public List<GymCoach> selectCoachBySpecialty(String specialty);

    /**
     * 新增教练信息
     * 
     * @param gymCoach 教练信息
     * @return 结果
     */
    public int insertGymCoach(GymCoach gymCoach);

    /**
     * 修改教练信息
     * 
     * @param gymCoach 教练信息
     * @return 结果
     */
    public int updateGymCoach(GymCoach gymCoach);

    /**
     * 删除教练信息
     * 
     * @param coachId 教练信息主键
     * @return 结果
     */
    public int deleteGymCoachByCoachId(Long coachId);

    /**
     * 批量删除教练信息
     * 
     * @param coachIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGymCoachByCoachIds(Long[] coachIds);
}
