<template>
  <div class="dashboard-editor-container">
    <el-row :gutter="40" class="panel-group">
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
          <div class="card-panel-icon-wrapper icon-people">
            <i class="el-icon-user-solid card-panel-icon"></i>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">会员总数</div>
            <<count-to :start-val="0" :end-val="stats.memberCount" :duration="2600" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('messages')">
          <div class="card-panel-icon-wrapper icon-message">
            <i class="el-icon-s-custom card-panel-icon"></i>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">在职教练</div>
            <count-to :start-val="0" :end-val="stats.coachCount" :duration="3000" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('purchases')">
          <div class="card-panel-icon-wrapper icon-money">
            <i class="el-icon-date card-panel-icon"></i>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">今日预约</div>
            <count-to :start-val="0" :end-val="stats.todayBooking" :duration="3200" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('shoppings')">
          <div class="card-panel-icon-wrapper icon-shopping">
            <i class="el-icon-trophy card-panel-icon"></i>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">本月营收(预估)</div>
            <count-to :start-val="0" :end-val="stats.monthRevenue" :duration="3600" class="card-panel-num" />
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="16">
        <div class="chart-wrapper">
          <div class="chart-title">📅 过去七天预约热度趋势</div>
          <div id="lineChart" style="height: 350px; width: 100%"></div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <div class="chart-title">🎯 会员训练目标分布</div>
          <div id="radarChart" style="height: 350px; width: 100%"></div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="32" style="margin-top:32px;">
      <el-col :span="24">
        <el-card shadow="hover">
          <div slot="header" class="clearfix">
            <span style="font-weight:bold; color:#F56C6C"><i class="el-icon-warning"></i> 智能流失预警</span>
            <el-tag size="mini" type="danger" style="float: right;">系统自动分析</el-tag>
          </div>
          <el-table :data="warningList" style="width: 100%" :show-header="false">
            <el-table-column width="50">
              <template><i class="el-icon-warning-outline" style="color:red"></i></template>
            </el-table-column>
            <el-table-column prop="content"></el-table-column>
            <el-table-column prop="date" width="180" align="right" style="color:#999"></el-table-column>
            <el-table-column width="100" align="right">
              <template><el-button type="text">去联系</el-button></template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import CountTo from 'vue-count-to' // 若依自带的数字滚动组件
import { getDashboardData } from "@/api/gym/report"

export default {
  name: 'Index',
  components: {
    CountTo
  },
  data() {
    return {
      stats: {
        memberCount: 0,
        coachCount: 0,
        todayBooking: 0,
        monthRevenue: 0,
        dateList: [],
        bookingTrend: [],
        specialtyData: []
      },
      warningList: [
        { content: "会员 [张三] 已经 35 天没有来健身了，存在流失风险", date: "系统自动检测" },
        { content: "会员 [李四] 的年卡将于 7 天后到期", date: "系统自动检测" },
        { content: "教练 [王五] 本周预约量低于平均水平 20%", date: "绩效分析" }
      ],
      lineChart: null,
      radarChart: null
    }
  },
  mounted() {
    this.getData(); // 👈 页面加载时请求数据

    // 监听窗口变化 (保持不变)
    window.addEventListener('resize', this.resizeCharts)
  },
  beforeDestroy() {
    if (this.lineChart) {
      this.lineChart.dispose()
    }
    if (this.radarChart) {
      this.radarChart.dispose()
    }
    window.removeEventListener('resize', this.resizeCharts)
  },
  methods: {
    // 👇 新增：获取数据方法
    getData() {
      getDashboardData().then(response => {
        const data = response.data;
        // 1. 填充顶部卡片数据
        this.stats.memberCount = data.memberCount;
        this.stats.coachCount = data.coachCount;
        this.stats.todayBooking = data.todayBooking;
        this.stats.monthRevenue = data.monthRevenue;

        // 2. 保存图表数据
        this.stats.dateList = data.dateList;
        this.stats.bookingTrend = data.bookingTrend;
        this.stats.specialtyData = data.specialtyData;

        // 3. 数据回来后，再初始化图表
        this.$nextTick(() => {
          this.initLineChart();
          this.initRadarChart();
        });
      });
    },
    resizeCharts() {
      this.lineChart && this.lineChart.resize()
      this.radarChart && this.radarChart.resize()
    },
    // 初始化折线图
    initLineChart() {
      this.lineChart = echarts.init(document.getElementById('lineChart'))
      this.lineChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'cross' } },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.stats.dateList // 👈 使用后端返回的日期
        },
        yAxis: { type: 'value' },
        series: [{
          name: '预约人数',
          type: 'line',
          smooth: true,
          itemStyle: { color: '#409EFF' },
          areaStyle: { color: '#ecf5ff' },
          data: this.stats.bookingTrend // 👈 使用后端返回的数量
        }]
      })
    },
    // 初始化雷达图
    initRadarChart() {
      this.radarChart = echarts.init(document.getElementById('radarChart'))
      this.radarChart.setOption({
        tooltip: {},
        radar: {
          indicator: [
            { name: '增肌', max: 50 }, // max值可以根据实际情况动态调整或写死一个较大值
            { name: '减脂', max: 50 },
            { name: '塑形', max: 50 },
            { name: '康复', max: 50 },
            { name: '拳击', max: 50 },
            { name: '瑜伽', max: 50 }
          ],
          center: ['50%', '50%'],
          radius: '65%'
        },
        series: [{
          name: '会员偏好',
          type: 'radar',
          areaStyle: { opacity: 0.3 },
          data: [
            {
              value: this.stats.specialtyData, // 👈 使用后端返回的数据数组
              name: '热门领域',
              itemStyle: { color: '#67C23A' }
            }
          ]
        }]
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: #f0f2f5;
  position: relative;
  min-height: 100vh;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
  }

  .chart-title {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    margin-bottom: 15px;
    padding-left: 10px;
    border-left: 4px solid #409EFF;
  }
}

.panel-group {
  margin-top: 18px;
  .card-panel-col {
    margin-bottom: 32px;
  }
  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
    border-radius: 6px;
    display: flex;
    align-items: center;

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }
    .card-panel-icon {
      float: left;
      font-size: 48px;
    }
    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px 26px 26px 0;
      margin-left: auto;
      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }
      .card-panel-num {
        font-size: 20px;
      }
    }

    // 鼠标悬停特效
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }
      .icon-people { background: #40c9c6; }
      .icon-message { background: #36a3f7; }
      .icon-money { background: #f4516c; }
      .icon-shopping { background: #34bfa3; }
    }

    .icon-people { color: #40c9c6; }
    .icon-message { color: #36a3f7; }
    .icon-money { color: #f4516c; }
    .icon-shopping { color: #34bfa3; }
  }
}
</style>
