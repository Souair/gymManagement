package com.ruoyi.gym.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 教练信息对象 gym_coach
 * 
 * @author zzw
 * @date 2025-12-24
 */
public class GymCoach extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long coachId;

    /** 教练编号 */
    @Excel(name = "教练编号")
    private String coachNo;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 照片 */
    @Excel(name = "照片")
    private String avatar;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 年龄 */
    @Excel(name = "年龄")
    private Long age;

    /** 手机 */
    @Excel(name = "手机")
    private String phone;

    /** 身高 */
    @Excel(name = "身高")
    private Long height;

    /** 体重 */
    @Excel(name = "体重")
    private Long weight;

    /** 专业领域 */
    @Excel(name = "专业领域")
    private String specialty;

    /** 入职日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "入职日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date entryDate;

    /** 离职日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "离职日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date resignDate;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 删除标志 */
    private String delFlag;

    public void setCoachId(Long coachId) 
    {
        this.coachId = coachId;
    }

    public Long getCoachId() 
    {
        return coachId;
    }

    public void setCoachNo(String coachNo) 
    {
        this.coachNo = coachNo;
    }

    public String getCoachNo() 
    {
        return coachNo;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setAvatar(String avatar) 
    {
        this.avatar = avatar;
    }

    public String getAvatar() 
    {
        return avatar;
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

    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }

    public void setHeight(Long height) 
    {
        this.height = height;
    }

    public Long getHeight() 
    {
        return height;
    }

    public void setWeight(Long weight) 
    {
        this.weight = weight;
    }

    public Long getWeight() 
    {
        return weight;
    }

    public void setSpecialty(String specialty) 
    {
        this.specialty = specialty;
    }

    public String getSpecialty() 
    {
        return specialty;
    }

    public void setEntryDate(Date entryDate) 
    {
        this.entryDate = entryDate;
    }

    public Date getEntryDate() 
    {
        return entryDate;
    }

    public void setResignDate(Date resignDate) 
    {
        this.resignDate = resignDate;
    }

    public Date getResignDate() 
    {
        return resignDate;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
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
            .append("coachId", getCoachId())
            .append("coachNo", getCoachNo())
            .append("name", getName())
            .append("avatar", getAvatar())
            .append("gender", getGender())
            .append("age", getAge())
            .append("phone", getPhone())
            .append("height", getHeight())
            .append("weight", getWeight())
            .append("specialty", getSpecialty())
            .append("entryDate", getEntryDate())
            .append("resignDate", getResignDate())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
