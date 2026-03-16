package com.ruoyi.gym.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.gym.mapper.GymCoachMapper;
import com.ruoyi.gym.domain.GymCoach;
import com.ruoyi.gym.service.IGymCoachService;

/**
 * 教练信息Service业务层处理
 * 
 * @author zzw
 * @date 2025-12-24
 */
@Service
public class GymCoachServiceImpl implements IGymCoachService 
{
    @Autowired
    private GymCoachMapper gymCoachMapper;

    /**
     * 查询教练信息
     * 
     * @param coachId 教练信息主键
     * @return 教练信息
     */
    @Override
    public GymCoach selectGymCoachByCoachId(Long coachId)
    {
        return gymCoachMapper.selectGymCoachByCoachId(coachId);
    }

    /**
     * 查询教练信息列表
     * 
     * @param gymCoach 教练信息
     * @return 教练信息
     */
    @Override
    public List<GymCoach> selectGymCoachList(GymCoach gymCoach)
    {
        return gymCoachMapper.selectGymCoachList(gymCoach);
    }

    /**
     * 新增教练信息
     * 
     * @param gymCoach 教练信息
     * @return 结果
     */
    @Override
    public int insertGymCoach(GymCoach gymCoach)
    {
        gymCoach.setCreateTime(DateUtils.getNowDate());
        return gymCoachMapper.insertGymCoach(gymCoach);
    }

    /**
     * 修改教练信息
     * 
     * @param gymCoach 教练信息
     * @return 结果
     */
    @Override
    public int updateGymCoach(GymCoach gymCoach)
    {
        gymCoach.setUpdateTime(DateUtils.getNowDate());
        return gymCoachMapper.updateGymCoach(gymCoach);
    }

    /**
     * 批量删除教练信息
     * 
     * @param coachIds 需要删除的教练信息主键
     * @return 结果
     */
    @Override
    public int deleteGymCoachByCoachIds(Long[] coachIds)
    {
        return gymCoachMapper.deleteGymCoachByCoachIds(coachIds);
    }

    /**
     * 删除教练信息信息
     * 
     * @param coachId 教练信息主键
     * @return 结果
     */
    @Override
    public int deleteGymCoachByCoachId(Long coachId)
    {
        return gymCoachMapper.deleteGymCoachByCoachId(coachId);
    }

}
