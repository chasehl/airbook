<template>
  <div>
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="6">
        <el-card shadow="hover" class="data-card">
          <div class="card-title">总销售额</div>
          <div class="card-num" style="color: #409EFF">¥ 128,500</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="data-card">
          <div class="card-title">总订单数</div>
          <div class="card-num" style="color: #67c23a">1,024</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="data-card">
          <div class="card-title">累计用户</div>
          <div class="card-num" style="color: #e6a23c">852</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="data-card">
          <div class="card-title">今日航班</div>
          <div class="card-num" style="color: #f56c6c">12</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="hover">
      <template #header>
        <span style="font-weight: bold">📈 近七日销售趋势</span>
      </template>
      <div id="mainChart" style="width: 100%; height: 400px;"></div>
    </el-card>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import * as echarts from 'echarts' // 引入 ECharts

onMounted(() => {
  initChart()
})

const initChart = () => {
  // 1. 获取 DOM
  const chartDom = document.getElementById('mainChart')
  const myChart = echarts.init(chartDom)
  
  // 2. 配置项 (假数据模拟)
  const option = {
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: { type: 'value' },
    series: [
      {
        name: '销售额',
        type: 'line',
        smooth: true, // 平滑曲线
        data: [820, 932, 901, 934, 1290, 1330, 1320],
        itemStyle: { color: '#409EFF' },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64,158,255,0.5)' },
            { offset: 1, color: 'rgba(64,158,255,0.1)' }
          ])
        }
      }
    ]
  }

  // 3. 渲染
  myChart.setOption(option)
  
  // 自适应窗口大小
  window.addEventListener('resize', () => myChart.resize())
}
</script>

<style scoped>
.data-card { text-align: center; }
.card-title { color: #909399; font-size: 14px; margin-bottom: 10px; }
.card-num { font-size: 24px; font-weight: bold; }
</style>