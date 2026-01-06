<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :span="24">
        <div class="welcome-card">
          <el-card>
            <div class="welcome-content">
              <h2>欢迎使用企业排班管理系统</h2>
              <p>一个高效、灵活的排班管理解决方案，助力企业优化人力资源配置</p>
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="mt-20">
      <el-col :span="6">
        <el-card class="statistic-card">
          <div class="statistic-item">
            <div class="statistic-icon bg-blue">
              <el-icon><User /></el-icon>
            </div>
            <div class="statistic-info">
              <p class="statistic-number">{{ employeeCount }}</p>
              <p class="statistic-label">员工总数</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistic-card">
          <div class="statistic-item">
            <div class="statistic-icon bg-green">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="statistic-info">
              <p class="statistic-number">{{ scheduleCount }}</p>
              <p class="statistic-label">排班计划</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistic-card">
          <div class="statistic-item">
            <div class="statistic-icon bg-orange">
              <el-icon><Calendar /></el-icon>
            </div>
            <div class="statistic-info">
              <p class="statistic-number">{{ shiftCount }}</p>
              <p class="statistic-label">班次类型</p>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistic-card">
          <div class="statistic-item">
            <div class="statistic-icon bg-purple">
              <el-icon><Document /></el-icon>
            </div>
            <div class="statistic-info">
              <p class="statistic-number">{{ requestCount }}</p>
              <p class="statistic-label">待处理请求</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="mt-20">
      <el-col :span="16">
        <el-card class="chart-card">
          <template #header>
            <span>排班统计图表</span>
          </template>
          <div id="scheduleChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card">
          <template #header>
            <span>部门分布</span>
          </template>
          <div id="departmentChart" style="height: 400px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 系统信息和快捷操作 -->
    <el-row :gutter="20" class="mt-20">
      <el-col :span="12">
        <el-card class="info-card">
          <template #header>
            <span>系统信息</span>
          </template>
          <ul class="system-info-list">
            <li><span>系统版本:</span><span>v{{ version }}</span></li>
            <li><span>当前时间:</span><span>{{ currentTime }}</span></li>
            <li><span>运行环境:</span><span>{{ env }}</span></li>
            <li><span>服务器状态:</span><span :class="serverStatusClass">{{ serverStatus }}</span></li>
          </ul>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="quick-operation-card">
          <template #header>
            <span>快捷操作</span>
          </template>
          <div class="quick-operation-list">
            <el-button type="primary" @click="goTarget('/layout/scheduling')">创建排班</el-button>
            <el-button type="success" @click="goTarget('/layout/employees')">员工管理</el-button>
            <el-button type="info" @click="goTarget('/layout/shifts')">班次设置</el-button>
            <el-button type="warning" @click="goTarget('/layout/requests')">请假申请</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
import { ref, onMounted, computed, nextTick } from 'vue'
import { User, Clock, Calendar, Document } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { listEmployees } from '@/api/employees/employees'
import { listScheduling } from '@/api/scheduling/scheduling'
import { listShifts } from '@/api/shifts/shifts'
import { listRequests } from '@/api/requests/requests'
import { listUser } from '@/api/system/user'

const version = ref('3.9.0')
const currentTime = ref('')
const env = ref('Production')
const serverStatus = ref('正常')
const employeeCount = ref(0)
const scheduleCount = ref(0)
const shiftCount = ref(0)
const requestCount = ref(0)

// 图表实例
let scheduleChart = null
let departmentChart = null

// 服务器状态类
const serverStatusClass = computed(() => {
  return serverStatus.value === '正常' ? 'status-success' : 'status-error'
})

// 更新当前时间
function updateCurrentTime() {
  const now = new Date()
  currentTime.value = now.toLocaleString('zh-CN')
}

// 获取员工总数
async function fetchEmployeeCount() {
  try {
    const response = await listEmployees({ pageSize: 1, pageNum: 1 }) // 获取分页信息以获得总数
    employeeCount.value = response.total || 0
  } catch (error) {
    console.error('获取员工数据失败:', error)
    employeeCount.value = 0 // 设置默认值
  }
}

// 获取排班计划数
async function fetchScheduleCount() {
  try {
    const response = await listScheduling({ pageSize: 1, pageNum: 1 })
    scheduleCount.value = response.total || 0
  } catch (error) {
    console.error('获取排班数据失败:', error)
    scheduleCount.value = 0 // 设置默认值
  }
}

// 获取班次类型数
async function fetchShiftCount() {
  try {
    const response = await listShifts({ pageSize: 1, pageNum: 1 })
    shiftCount.value = response.total || 0
  } catch (error) {
    console.error('获取班次数据失败:', error)
    shiftCount.value = 0 // 设置默认值
  }
}

// 获取待处理请求数 - 仅统计status=0的请求
async function fetchRequestCount() {
  try {
    // 仅获取status为0（待处理）的请求
    const response = await listRequests({ pageSize: 1, pageNum: 1, status: '0' })
    requestCount.value = response.total || 0
  } catch (error) {
    console.error('获取请求数据失败:', error)
    requestCount.value = 0 // 设置默认值
  }
}

// 获取图表数据 - 从实际API获取
async function fetchScheduleChartData() {
  try {
    // 获取排班数据
    const response = await listScheduling({ pageSize: 1000, pageNum: 1 })
    const schedulingData = response.rows || []
    
    // 按日期分组数据
    const dateMap = {}
    schedulingData.forEach(item => {
      // 使用排班日期字段
      const dateStr = item.scheduleDate ? new Date(item.scheduleDate).getDate() + '号' : '未知'
      if (!dateMap[dateStr]) {
        dateMap[dateStr] = { scheduled: 0, pending: 0, exceptional: 0 }
      }
      
      // 根据排班状态进行分类统计
      if (item.status === '0' || item.status === '已确认') {
        dateMap[dateStr].scheduled++
      } else if (item.status === '2' || item.status === '待确认') {
        dateMap[dateStr].pending++
      }
      // 不再统计异常状态，移除异常折线
    })
    
    // 生成最近7天的数据
    const dates = []
    const scheduledData = []
    const pendingData = []
    const exceptionalData = []
    
    // 生成最近7天的日期
    for (let i = 6; i >= 0; i--) {
      const date = new Date()
      date.setDate(date.getDate() - i)
      const dateStr = date.getDate() + '号'
      dates.push(dateStr)
      
      // 检查是否有对应日期的数据
      if (dateMap[dateStr]) {
        scheduledData.push(dateMap[dateStr].scheduled)
        pendingData.push(dateMap[dateStr].pending)
        exceptionalData.push(dateMap[dateStr].exceptional)
      } else {
        // 如果没有数据，则返回0
        scheduledData.push(0)
        pendingData.push(0)
        exceptionalData.push(0)
      }
    }
    
    return {
      dates: dates,
      scheduled: scheduledData,
      pending: pendingData,
      exceptional: exceptionalData
    }
  } catch (error) {
    console.error('获取排班图表数据失败:', error)
    // 如果API调用失败，返回空数据
    return {
      dates: ['1号', '2号', '3号', '4号', '5号', '6号', '7号'],
      scheduled: [0, 0, 0, 0, 0, 0, 0],
      pending: [0, 0, 0, 0, 0, 0, 0],
      exceptional: [0, 0, 0, 0, 0, 0, 0]
    }
  }
}

async function fetchDepartmentChartData() {
  try {
    // 获取所有员工数据用于部门分布统计
    const response = await listEmployees({ pageSize: 1000, pageNum: 1 })
    const employeesData = response.rows || []
    
    // 获取用户信息以获取部门名称
    const deptMap = {}
    const userIds = [...new Set(employeesData.map(emp => emp.userId))]
    
    // 批量获取用户信息
    const userPromises = userIds.map(userId => listUser({ userId: userId, pageSize: 1, pageNum: 1 }))
    const userResponses = await Promise.allSettled(userPromises)
    
    // 整合员工和用户数据
    employeesData.forEach(employee => {
      const userResponse = userResponses.find((res, idx) => userIds[idx] === employee.userId)
      if (userResponse && userResponse.status === 'fulfilled' && userResponse.value.rows && userResponse.value.rows.length > 0) {
        const user = userResponse.value.rows[0]
        const deptName = user.dept ? user.dept.deptName : '未分配部门'
        if (!deptMap[deptName]) {
          deptMap[deptName] = 0
        }
        deptMap[deptName]++
      } else {
        const deptName = '未分配部门'
        if (!deptMap[deptName]) {
          deptMap[deptName] = 0
        }
        deptMap[deptName]++
      }
    })
    
    // 转换为ECharts需要的格式
    const deptData = Object.keys(deptMap).map(deptName => ({
      value: deptMap[deptName],
      name: deptName
    }))
    
    // 如果没有数据，返回默认值
    if (deptData.length === 0) {
      return [
        { value: 0, name: '技术部' },
        { value: 0, name: '销售部' },
        { value: 0, name: '人事部' },
        { value: 0, name: '财务部' },
        { value: 0, name: '运营部' }
      ]
    }
    
    return deptData
  } catch (error) {
    console.error('获取部门图表数据失败:', error)
    // 如果API调用失败，返回空数据
    return [
      { value: 0, name: '技术部' },
      { value: 0, name: '销售部' },
      { value: 0, name: '人事部' },
      { value: 0, name: '财务部' },
      { value: 0, name: '运营部' }
    ]
  }
}

// 更新图表数据
async function updateCharts() {
  // 更新排班统计图表
  const scheduleData = await fetchScheduleChartData()
  scheduleChart.setOption({
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: scheduleData.dates
    },
    series: [
      {
        name: '已安排',
        type: 'line',
        stack: 'Total',
        data: scheduleData.scheduled
      },
      {
        name: '待安排',
        type: 'line',
        stack: 'Total',
        data: scheduleData.pending
      }
    ]
  })

  // 更新部门分布图表
  const departmentData = await fetchDepartmentChartData()
  departmentChart.setOption({
    series: [{
      data: departmentData
    }]
  })
}

// 获取所有统计数据
async function fetchAllStats() {
  await Promise.all([
    fetchEmployeeCount(),
    fetchScheduleCount(),
    fetchShiftCount(),
    fetchRequestCount()
  ])
}

onMounted(async () => {
  // 初始化时间
  updateCurrentTime()
  // 每秒更新时间
  setInterval(updateCurrentTime, 1000)
  
  // 获取统计数据
  await fetchAllStats()
  
  // 等待DOM更新完成后再初始化图表
  await nextTick()
  
  // 初始化图表
  initCharts()
  
  // 定期更新图表数据（每分钟更新一次）
  setInterval(updateCharts, 60000)
})

function goTarget(url) {
  window.open(url, '__blank')
}

function initCharts() {
  // 排班统计图表
  scheduleChart = echarts.init(document.getElementById('scheduleChart'))
  const scheduleOption = {
    title: {
      text: '月度排班统计',
      subtext: '近30天排班情况'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['已安排', '待安排']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    toolbox: {
      feature: {
        saveAsImage: {}
      }
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['加载中...']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '已安排',
        type: 'line',
        stack: 'Total',
        data: []
      },
      {
        name: '待安排',
        type: 'line',
        stack: 'Total',
        data: []
      }
    ]
  }
  scheduleChart.setOption(scheduleOption)

  // 部门分布图表
  departmentChart = echarts.init(document.getElementById('departmentChart'))
  const departmentOption = {
    title: {
      text: '部门员工分布',
      subtext: '各部门员工数量占比',
      left: 'center'
    },
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b} : {c} ({d}%)'
    },
    legend: {
      type: 'scroll',
      orient: 'vertical',
      right: 10,
      top: 20,
      bottom: 20
    },
    series: [
      {
        name: '员工数量',
        type: 'pie',
        radius: '50%',
        center: ['40%', '50%'],
        data: [],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  departmentChart.setOption(departmentOption)

  // 监听窗口大小变化，调整图表大小
  window.addEventListener('resize', () => {
    scheduleChart.resize()
    departmentChart.resize()
  })
  
  // 初始加载图表数据
  updateCharts()
}
</script>

<style scoped lang="scss">
.home {
  padding: 20px;

  .welcome-card {
    .welcome-content {
      text-align: center;
      padding: 20px 0;

      h2 {
        margin-bottom: 10px;
        color: #303133;
        font-size: 24px;
        font-weight: 500;
      }

      p {
        color: #606266;
        font-size: 16px;
      }
    }
  }

  .mt-20 {
    margin-top: 20px;
  }

  .statistic-card {
    height: 100px;
    display: flex;
    align-items: center;

    .statistic-item {
      display: flex;
      width: 100%;
      align-items: center;

      .statistic-icon {
        width: 60px;
        height: 60px;
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 15px;
        color: white;
        font-size: 24px;

        &.bg-blue {
          background: linear-gradient(45deg, #3498db, #1abc9c);
        }

        &.bg-green {
          background: linear-gradient(45deg, #2ecc71, #1abc9c);
        }

        &.bg-orange {
          background: linear-gradient(45deg, #f39c12, #e67e22);
        }

        &.bg-purple {
          background: linear-gradient(45deg, #9b59b6, #8e44ad);
        }
      }

      .statistic-info {
        flex: 1;
        text-align: center;

        .statistic-number {
          font-size: 24px;
          font-weight: bold;
          color: #303133;
          margin: 0 0 5px 0;
        }

        .statistic-label {
          font-size: 14px;
          color: #909399;
          margin: 0;
        }
      }
    }
  }

  .chart-card {
    min-height: 450px;
  }

  .info-card, .quick-operation-card {
    min-height: 200px;

    .system-info-list {
      list-style: none;
      padding: 0;
      margin: 0;

      li {
        display: flex;
        justify-content: space-between;
        padding: 8px 0;
        border-bottom: 1px solid #ebeef5;
        font-size: 14px;

        &:last-child {
          border-bottom: none;
        }

        span:first-child {
          color: #909399;
        }

        span:last-child {
          color: #606266;
        }

        .status-success {
          color: #67c23a;
        }
        
        .status-error {
          color: #f56c6c;
        }
      }
    }

    .quick-operation-list {
      display: flex;
      flex-wrap: wrap;
      gap: 10px;

      .el-button {
        margin: 0;
      }
    }
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }
}
</style>