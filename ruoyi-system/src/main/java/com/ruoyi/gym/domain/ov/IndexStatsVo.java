package com.ruoyi.gym.domain.vo;

import java.util.List;

public class IndexStatsVo {
    // 1. 顶部卡片数据
    private int memberCount;      // 会员总数
    private int coachCount;       // 教练总数
    private int todayBooking;     // 今日预约数
    private double monthRevenue;  // 本月营收

    // 2. 折线图数据 (过去7天)
    private List<String> dateList; //日期数组 ['周一', '周二'...]
    private List<Integer> bookingTrend; // 数量数组 [10, 20...]

    // 3. 雷达图数据 (各项训练目标占比)
    private List<Integer> specialtyData; // [增肌数, 减脂数...]

    // Getters and Setters ... (请使用 IDE 自动生成或 Lombok @Data)
    public int getMemberCount() { return memberCount; }
    public void setMemberCount(int memberCount) { this.memberCount = memberCount; }
    public int getCoachCount() { return coachCount; }
    public void setCoachCount(int coachCount) { this.coachCount = coachCount; }
    public int getTodayBooking() { return todayBooking; }
    public void setTodayBooking(int todayBooking) { this.todayBooking = todayBooking; }
    public double getMonthRevenue() { return monthRevenue; }
    public void setMonthRevenue(double monthRevenue) { this.monthRevenue = monthRevenue; }
    public List<String> getDateList() { return dateList; }
    public void setDateList(List<String> dateList) { this.dateList = dateList; }
    public List<Integer> getBookingTrend() { return bookingTrend; }
    public void setBookingTrend(List<Integer> bookingTrend) { this.bookingTrend = bookingTrend; }
    public List<Integer> getSpecialtyData() { return specialtyData; }
    public void setSpecialtyData(List<Integer> specialtyData) { this.specialtyData = specialtyData; }
}