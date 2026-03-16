package com.ruoyi.gym.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.gym.mapper.GymMemberMapper;
import com.ruoyi.gym.domain.GymMember;
import com.ruoyi.gym.service.IGymMemberService;

/**
 * 会员信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
@Service
public class GymMemberServiceImpl implements IGymMemberService 
{
    @Autowired
    private GymMemberMapper gymMemberMapper;

    /**
     * 查询会员信息
     * 
     * @param memberId 会员信息主键
     * @return 会员信息
     */
    @Override
    public GymMember selectGymMemberByMemberId(Long memberId)
    {
        return gymMemberMapper.selectGymMemberByMemberId(memberId);
    }

    /**
     * 查询会员信息列表
     * 
     * @param gymMember 会员信息
     * @return 会员信息
     */
    @Override
    public List<GymMember> selectGymMemberList(GymMember gymMember)
    {
        return gymMemberMapper.selectGymMemberList(gymMember);
    }

    /**
     * 新增会员信息
     * 
     * @param gymMember 会员信息
     * @return 结果
     */
    @Override
    public int insertGymMember(GymMember gymMember)
    {
        gymMember.setCreateTime(DateUtils.getNowDate());
        return gymMemberMapper.insertGymMember(gymMember);
    }

    /**
     * 修改会员信息
     * 
     * @param gymMember 会员信息
     * @return 结果
     */
    @Override
    public int updateGymMember(GymMember gymMember)
    {
        gymMember.setUpdateTime(DateUtils.getNowDate());
        return gymMemberMapper.updateGymMember(gymMember);
    }

    /**
     * 批量删除会员信息
     * 
     * @param memberIds 需要删除的会员信息主键
     * @return 结果
     */
    @Override
    public int deleteGymMemberByMemberIds(Long[] memberIds)
    {
        return gymMemberMapper.deleteGymMemberByMemberIds(memberIds);
    }

    /**
     * 删除会员信息信息
     * 
     * @param memberId 会员信息主键
     * @return 结果
     */
    @Override
    public int deleteGymMemberByMemberId(Long memberId)
    {
        return gymMemberMapper.deleteGymMemberByMemberId(memberId);
    }
}
