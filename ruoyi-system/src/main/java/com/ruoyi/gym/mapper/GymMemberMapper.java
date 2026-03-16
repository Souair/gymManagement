package com.ruoyi.gym.mapper;

import java.util.List;
import com.ruoyi.gym.domain.GymMember;

/**
 * 会员信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public interface GymMemberMapper 
{
    /**
     * 查询会员信息
     * 
     * @param memberId 会员信息主键
     * @return 会员信息
     */
    public GymMember selectGymMemberByMemberId(Long memberId);

    /**
     * 查询会员信息列表
     * 
     * @param gymMember 会员信息
     * @return 会员信息集合
     */
    public List<GymMember> selectGymMemberList(GymMember gymMember);

    /**
     * 新增会员信息
     * 
     * @param gymMember 会员信息
     * @return 结果
     */
    public int insertGymMember(GymMember gymMember);

    /**
     * 修改会员信息
     * 
     * @param gymMember 会员信息
     * @return 结果
     */
    public int updateGymMember(GymMember gymMember);

    /**
     * 删除会员信息
     * 
     * @param memberId 会员信息主键
     * @return 结果
     */
    public int deleteGymMemberByMemberId(Long memberId);

    /**
     * 批量删除会员信息
     * 
     * @param memberIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGymMemberByMemberIds(Long[] memberIds);
}
