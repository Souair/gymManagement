package com.ruoyi.gym.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 会员信息对象 gym_member
 * 
 * @author ruoyi
 * @date 2025-12-24
 */
public class GymMember extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会员ID */
    private Long memberId;

    /** 会员卡号 */
    @Excel(name = "会员卡号")
    private String cardNo;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 手机号码 */
    @Excel(name = "手机号码")
    private String phone;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 年龄 */
    @Excel(name = "年龄")
    private Long age;

    /** 身高 */
    @Excel(name = "身高")
    private BigDecimal height;

    /** 体重 */
    @Excel(name = "体重")
    private BigDecimal weight;

    /** 健身目标 */
    @Excel(name = "健身目标")
    private String target;

    /** 办卡类型 */
    @Excel(name = "办卡类型")
    private String cardType;

    /** 私教课时 */
    @Excel(name = "私教课时")
    private Long classHours;

    /** 有效期开始 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效期开始", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 有效期结束 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效期结束", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 删除标志 */
    private String delFlag;

    public void setMemberId(Long memberId) 
    {
        this.memberId = memberId;
    }

    public Long getMemberId() 
    {
        return memberId;
    }

    public void setCardNo(String cardNo) 
    {
        this.cardNo = cardNo;
    }

    public String getCardNo() 
    {
        return cardNo;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setGender(String gender) 
    {
        this.gender = gender;
    }

    public String getGender() 
    {
        return gender;
    }

    public void setAge(Long age) 
    {
        this.age = age;
    }

    public Long getAge() 
    {
        return age;
    }

    public void setHeight(BigDecimal height) 
    {
        this.height = height;
    }

    public BigDecimal getHeight() 
    {
        return height;
    }

    public void setWeight(BigDecimal weight) 
    {
        this.weight = weight;
    }

    public BigDecimal getWeight() 
    {
        return weight;
    }

    public void setTarget(String target) 
    {
        this.target = target;
    }

    public String getTarget() 
    {
        return target;
    }

    public void setCardType(String cardType) 
    {
        this.cardType = cardType;
    }

    public String getCardType() 
    {
        return cardType;
    }

    public void setClassHours(Long classHours) 
    {
        this.classHours = classHours;
    }

    public Long getClassHours() 
    {
        return classHours;
    }

    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }

    public Date getStartDate() 
    {
        return startDate;
    }

    public void setEndDate(Date endDate) 
    {
        this.endDate = endDate;
    }

    public Date getEndDate() 
    {
        return endDate;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("memberId", getMemberId())
            .append("cardNo", getCardNo())
            .append("name", getName())
            .append("phone", getPhone())
            .append("gender", getGender())
            .append("age", getAge())
            .append("height", getHeight())
            .append("weight", getWeight())
            .append("target", getTarget())
            .append("cardType", getCardType())
            .append("classHours", getClassHours())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
