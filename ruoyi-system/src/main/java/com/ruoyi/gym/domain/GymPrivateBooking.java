package com.ruoyi.gym.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 私教预约对象 gym_private_booking
 * 
 * @author zzw
 * @date 2025-12-24
 */
public class GymPrivateBooking extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 教练ID */
    @Excel(name = "教练ID")
    private Long coachId;

    /** 会员ID */
    @Excel(name = "会员ID")
    private Long memberId;

    /** 预约类型 */
    @Excel(name = "预约类型")
    private String bookingType;

    /** 预约时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Excel(name = "预约时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date bookingDate;

    /** 时长 */
    @Excel(name = "时长")
    private BigDecimal duration;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 删除标志 */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setCoachId(Long coachId) 
    {
        this.coachId = coachId;
    }

    public Long getCoachId() 
    {
        return coachId;
    }

    public void setMemberId(Long memberId) 
    {
        this.memberId = memberId;
    }

    public Long getMemberId() 
    {
        return memberId;
    }

    public void setBookingType(String bookingType) 
    {
        this.bookingType = bookingType;
    }

    public String getBookingType() 
    {
        return bookingType;
    }

    public void setBookingDate(Date bookingDate) 
    {
        this.bookingDate = bookingDate;
    }

    public Date getBookingDate() 
    {
        return bookingDate;
    }

    public void setDuration(BigDecimal duration) 
    {
        this.duration = duration;
    }

    public BigDecimal getDuration() 
    {
        return duration;
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
            .append("id", getId())
            .append("coachId", getCoachId())
            .append("memberId", getMemberId())
            .append("bookingType", getBookingType())
            .append("bookingDate", getBookingDate())
            .append("duration", getDuration())
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
