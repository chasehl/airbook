<template>
  <div class="home-container">
    <div class="nav-header">
      <div class="logo">
        <el-icon class="icon-spin"><Promotion /></el-icon> Airbook
      </div>
      <div class="user-area">
        <el-dropdown>
          <div class="el-dropdown-link" style="color: white; cursor: pointer; display: flex; align-items: center;">
            <el-avatar :size="32" :src="`https://api.dicebear.com/7.x/adventurer/svg?seed=${user.uname}&backgroundColor=b6e3f4`" style="margin-right: 8px" />
            <span style="font-weight: bold; font-size: 16px;">{{ user.uname }}</span>
            <el-icon class="el-icon--right"><arrow-down /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/profile')">👤 个人中心</el-dropdown-item>
              <el-dropdown-item @click="router.push('/orders')">📦 我的订单</el-dropdown-item>
              <el-dropdown-item divided @click="logout" style="color: #f56c6c">🚪 退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <div class="hero-section">
      <el-carousel trigger="click" height="400px" :interval="3000">
        <el-carousel-item v-for="(img, index) in bannerImages" :key="index">
          <div class="banner-image" :style="{ backgroundImage: `url(${img})` }">
            <div class="banner-mask"></div>
            <div class="banner-content">
              <h1>{{ bannerTitles[index] }}</h1>
              <p>探索未知的世界，发现更好的自己</p>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>

      <div class="search-card-wrapper">
        <el-card class="search-card" shadow="hover">
          <div class="search-form">
            <div class="form-item">
              <span class="label">出发地</span>
              <el-input v-model="searchForm.departure" placeholder="例如: 北京" :prefix-icon="Location" clearable />
            </div>
            
            <div class="exchange-icon" @click="swapCity">
              <el-icon><Switch /></el-icon>
            </div>

            <div class="form-item">
              <span class="label">目的地</span>
              <el-input v-model="searchForm.destination" placeholder="例如: 上海" :prefix-icon="Location" clearable />
            </div>

            <div class="form-item date-item">
              <span class="label">出发日期</span>
              <el-date-picker 
                v-model="searchForm.date" 
                type="date" 
                placeholder="选择日期" 
                style="width: 100%" 
                :disabled-date="disabledDate"
              />
            </div>

            <el-button type="primary" size="large" class="search-btn" @click="handleSearch" :loading="loading">
              <el-icon style="margin-right: 5px"><Search /></el-icon> 搜索航班
            </el-button>
          </div>

          <div class="filter-row">
            <span class="filter-label">价格预算: <span style="color:#409EFF">¥0 - ¥{{ searchForm.priceRange }}</span></span>
            <el-slider v-model="searchForm.priceRange" :max="5000" :step="100" show-input style="width: 300px; margin-left: 20px;" />
          </div>
        </el-card>
      </div>
    </div>

    <div class="main-content">
      
      <div class="section-title">🔥 本季热门目的地</div>
      <el-row :gutter="20" class="recommend-row">
        <el-col :span="6" v-for="item in recommendList" :key="item.name">
          <el-card :body-style="{ padding: '0px' }" shadow="hover" class="recommend-card" @click="quickSearch(item.name)">
            <img :src="item.img" class="recommend-image"/>
            <div style="padding: 14px">
              <span class="recommend-name">{{ item.name }}</span>
              <div class="bottom">
                <span class="price">¥{{ item.price }} 起</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <div class="section-title" id="flight-list-anchor">✈️ 航班查询结果</div>
      <el-card class="table-card">
        <el-table :data="filteredFlightList" style="width: 100%" stripe size="large" v-loading="loading">
          <el-table-column width="80">
            <template #default>
              <el-icon color="#409EFF"><Promotion /></el-icon>
            </template>
          </el-table-column>
          
          <el-table-column prop="flightNo" label="航班号" width="120" sortable>
            <template #default="scope">
              <el-tag effect="dark">{{ scope.row.flightNo }}</el-tag>
            </template>
          </el-table-column>
          
          <el-table-column label="行程" width="250">
            <template #default="scope">
              <div class="route-info">
                <div class="city">{{ scope.row.departure }}</div>
                <div class="arrow">
                  <span class="time">{{ formatTime(scope.row.startTime).split(' ')[1] }}</span>
                  <el-icon><Right /></el-icon>
                  <span class="duration">直飞</span>
                </div>
                <div class="city">{{ scope.row.destination }}</div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="日期" width="150" sortable>
            <template #default="scope">
              {{ formatTime(scope.row.startTime).split(' ')[0] }}
            </template>
          </el-table-column>

          <el-table-column prop="airline" label="航空公司" />
          
          <el-table-column label="价格" width="150" sortable prop="price">
            <template #default="scope">
              <span class="price-text">¥ {{ scope.row.price }}</span>
            </template>
          </el-table-column>

          <el-table-column label="余票" width="120">
             <template #default="scope">
               <span :class="scope.row.availableSeats < 10 ? 'red-text' : 'green-text'">
                 {{ scope.row.availableSeats }} 张
               </span>
             </template>
          </el-table-column>

          <el-table-column label="操作" fixed="right" width="140">
            <template #default="scope">
              <el-button type="primary" round @click="openSeatSelection(scope.row)">选座预订</el-button>
            </template>
          </el-table-column>
        </el-table>
        
        <el-empty v-if="filteredFlightList.length === 0" description="暂无符合条件的航班，试试其他搜索条件？" />
      </el-card>

    </div>

    <el-dialog v-model="seatVisible" title="请选择您的座位" width="600px" center destroy-on-close align-center>
      <div class="seat-container">
        <div class="cockpit"></div>
        
        <div class="fuselage">
          <div class="seat-row-letters">
            <span>A</span><span>B</span><span>C</span>
            <span class="aisle-spacer">过道</span>
            <span>D</span><span>E</span><span>F</span>
          </div>

          <div class="seat-row" v-for="row in seatRows" :key="row">
            <div 
              class="seat" 
              v-for="col in ['A','B','C']" 
              :key="row+col"
              :class="getSeatClass(row, col)"
              @click="selectSeat(row, col)"
            >
              {{ row }}{{ col }}
            </div>
            
            <div class="aisle-num">{{ row }}</div>

            <div 
              class="seat" 
              v-for="col in ['D','E','F']" 
              :key="row+col"
              :class="getSeatClass(row, col)"
              @click="selectSeat(row, col)"
            >
              {{ row }}{{ col }}
            </div>
          </div>
        </div>
        
        <div class="seat-legend">
          <div class="legend-item"><div class="seat available"></div>可选</div>
          <div class="legend-item"><div class="seat occupied"></div>已售</div>
          <div class="legend-item"><div class="seat selected"></div>已选</div>
        </div>
        
        <div class="selected-info" v-if="currentSeat">
          已选择座位：<span style="color: #409EFF; font-size: 20px; font-weight: bold">{{ currentSeat }}</span>
        </div>
      </div>
      <template #footer>
        <el-button @click="seatVisible = false">取消</el-button>
        <el-button type="primary" :disabled="!currentSeat" @click="confirmSeat">确认选座并支付</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="payVisible" title="收银台" width="400px" center destroy-on-close align-center>
      <div style="text-align: center">
        <p style="font-size: 16px; margin-bottom: 10px">订单金额：<span style="color: #f56c6c; font-size: 24px; font-weight: bold">¥{{ currentFlight.price }}</span></p>
        <p style="font-size: 14px; margin-bottom: 20px; background: #f0f9eb; color: #67c23a; padding: 5px;">已锁定座位：<b>{{ currentSeat }}</b></p>
        <img src="https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=AirbookPayment" style="border: 1px solid #eee; padding: 10px; border-radius: 8px" />
        <p style="color: #909399; font-size: 12px; margin-top: 10px">请使用 微信/支付宝 扫码支付</p>
      </div>
      <template #footer>
        <el-button @click="payVisible = false">取消</el-button>
        <el-button type="primary" :loading="payLoading" @click="confirmPay">模拟支付成功</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="ticketVisible" title="🎉 预订成功！您的电子登机牌" width="500px" align-center class="ticket-dialog">
      <div class="boarding-pass">
        <div class="pass-header">
          <div class="pass-airline">AIRBOOK AIRLINES</div>
          <div class="pass-class">ECONOMY</div>
        </div>
        <div class="pass-body">
          <div class="pass-row">
            <div class="pass-item">
              <span class="label">PASSENGER</span>
              <span class="value">{{ user.uname }}</span>
            </div>
            <div class="pass-item">
              <span class="label">FLIGHT</span>
              <span class="value">{{ currentFlight.flightNo }}</span>
            </div>
          </div>
          <div class="pass-row city-row">
            <div class="city-code">{{ currentFlight.departure }}</div>
            <el-icon class="plane-icon"><Promotion /></el-icon>
            <div class="city-code">{{ currentFlight.destination }}</div>
          </div>
          <div class="pass-row">
             <div class="pass-item">
              <span class="label">DATE</span>
              <span class="value">{{ formatTime(currentFlight.startTime).split(' ')[0] }}</span>
            </div>
            <div class="pass-item">
              <span class="label">TIME</span>
              <span class="value">{{ formatTime(currentFlight.startTime).split(' ')[1] }}</span>
            </div>
            <div class="pass-item">
              <span class="label">SEAT</span>
              <span class="value" style="color: #f56c6c; font-size: 22px;">{{ currentSeat }}</span>
            </div>
          </div>
        </div>
        <div class="pass-footer">
          <div class="barcode">|| | ||| | || |||| || || | |||</div>
          <div class="ticket-no">TICKET NO. {{ Date.now() }}</div>
        </div>
      </div>
      <template #footer>
        <el-button type="primary" size="large" style="width: 100%" @click="ticketVisible = false">保存并关闭</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { Promotion, Right, Location, Switch, Search, ArrowDown } from '@element-plus/icons-vue'
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const router = useRouter()
const user = ref({})
const loading = ref(false)
const rawFlightList = ref([]) // 原始数据

// 弹窗控制状态
const seatVisible = ref(false)
const payVisible = ref(false)
const payLoading = ref(false)
const ticketVisible = ref(false)

// 当前操作的数据
const currentFlight = ref({}) 
const currentSeat = ref('') // 当前选中的座位号
const occupiedSeats = ref([]) // 当前航班已被占的座位
const seatRows = [1, 2, 3, 4, 5, 6] // 假设每个航班有6排座位

// 搜索表单数据
const searchForm = reactive({
  departure: '',
  destination: '',
  date: '',
  priceRange: 5000 
})

// 轮播图数据
const bannerImages = [
  'https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?q=80&w=2070',
  'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?q=80&w=2073',
  'https://images.unsplash.com/photo-1499591934245-40b55745b905?q=80&w=2072'
]
const bannerTitles = ['瑞士 · 阿尔卑斯山', '马尔代夫 · 梦幻海岛', '希腊 · 圣托里尼']

// 热门推荐数据
const recommendList = [
  { name: '北京', price: 680, img: 'https://images.unsplash.com/photo-1508804185872-d7badad00f7d?q=80&w=800' },
  { name: '上海', price: 550, img: 'https://se-feed-bucket.bj.bcebos.com/produce-record1881312/fcc735f8c227809c8193b45901e33062.jpg' },
  { name: '成都', price: 890, img: 'https://img2.baidu.com/it/u=4158605532,2920385711&fm=253&app=138&f=JPEG?w=800&h=954' },
  { name: '三亚', price: 1200, img: 'https://qcloud.dpfile.com/pc/c8M2oZ6OfghKF0F30NpKK-saTIrbzYK2kVQTm-Zgbv0FHFxGOJtZhoAD7l3hnmGC.jpg' }
]

onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    user.value = JSON.parse(userStr)
    handleSearch()
  } else {
    router.push('/')
  }
})

// 获取航班
const handleSearch = async () => {
  loading.value = true
  try {
    const keyword = searchForm.destination || searchForm.departure || ''
    const res = await axios.get('http://localhost:8080/api/flight/list', {
      params: { keyword: keyword }
    })
    if (res.data.code === 200) {
      rawFlightList.value = res.data.data
    }
  } catch (err) {
    ElMessage.error('获取航班失败')
  } finally {
    loading.value = false
  }
}

// 过滤航班
const filteredFlightList = computed(() => {
  return rawFlightList.value.filter(flight => {
    if (flight.price > searchForm.priceRange) return false
    if (searchForm.departure && !flight.departure.includes(searchForm.departure)) return false
    if (searchForm.destination && !flight.destination.includes(searchForm.destination)) return false
    if (searchForm.date) {
      const flightDate = formatTime(flight.startTime).split(' ')[0]
      const searchDate = searchForm.date.toISOString().split('T')[0]
      if (flightDate !== searchDate) return false
    }
    return true
  })
})

const quickSearch = (city) => {
  searchForm.destination = city
  searchForm.departure = ''
  handleSearch()
  document.getElementById('flight-list-anchor').scrollIntoView({ behavior: 'smooth' })
}

const swapCity = () => {
  const temp = searchForm.departure
  searchForm.departure = searchForm.destination
  searchForm.destination = temp
}

// === 核心功能 1: 打开选座弹窗 ===
// (替代了之前的 handleBook 直接弹支付)
const openSeatSelection = (row) => {
  currentFlight.value = row
  currentSeat.value = '' // 重置选择
  
  // 解析数据库中的 occupied_seats 字符串 (如 "1A,2B") -> 数组 ['1A', '2B']
  if (row.occupiedSeats) {
    occupiedSeats.value = row.occupiedSeats.split(',')
  } else {
    occupiedSeats.value = []
  }
  
  seatVisible.value = true
}

// === 核心功能 2: 选座逻辑 ===
const getSeatClass = (row, col) => {
  const seatId = row + col
  if (occupiedSeats.value.includes(seatId)) return 'occupied' // 红色 (已占)
  if (currentSeat.value === seatId) return 'selected' // 绿色 (选中)
  return 'available' // 白色 (可选)
}

const selectSeat = (row, col) => {
  const seatId = row + col
  // 如果已占，不可点击
  if (occupiedSeats.value.includes(seatId)) return
  // 如果点击的是当前已选的，则取消；否则选中新的
  currentSeat.value = (currentSeat.value === seatId) ? '' : seatId
}

// === 核心功能 3: 确认座位 -> 去支付 ===
const confirmSeat = () => {
  seatVisible.value = false
  payVisible.value = true // 打开支付弹窗
}

// === 核心功能 4: 支付并提交 (带座位号) ===
const confirmPay = async () => {
  payLoading.value = true
  setTimeout(async () => {
    try {
      // 发送请求，包含座位号 seatNo
      const res = await axios.post('http://localhost:8080/api/order/create', {
        userId: user.value.id,
        flightId: currentFlight.value.id,
        seatNo: currentSeat.value
      })
      if (res.data.code === 200) {
        ElMessage.success(`预订成功！座位号: ${currentSeat.value}`)
        payVisible.value = false // 关支付
        handleSearch() // 刷新列表 (更新库存)
        ticketVisible.value = true // 开登机牌
      } else {
        ElMessage.error(res.data.msg)
      }
    } catch (err) {
      ElMessage.error('支付系统繁忙')
    } finally {
      payLoading.value = false
    }
  }, 1000)
}

const formatTime = (timeStr) => {
  if(!timeStr) return ''
  return timeStr.replace('T', ' ').substring(0, 16)
}

const disabledDate = (time) => {
  return time.getTime() < Date.now() - 8.64e7 
}

const logout = () => {
  localStorage.removeItem('user')
  router.push('/')
}
</script>

<style scoped>
.home-container { background-color: #f5f7fa; min-height: 100vh; }

/* 导航 */
.nav-header {
  position: absolute;
  top: 0; width: 100%; z-index: 10;
  display: flex; justify-content: space-between; align-items: center;
  padding: 15px 40px;
  background: linear-gradient(to bottom, rgba(0,0,0,0.6), transparent);
  box-sizing: border-box;
}
.logo { font-size: 24px; font-weight: bold; color: white; display: flex; align-items: center; gap: 8px; }
.icon-spin { animation: rotate 2s linear infinite; }
@keyframes rotate { 100% { transform: rotate(360deg); } }
.welcome-text { color: white; margin-right: 15px; font-weight: 500; text-shadow: 0 1px 2px rgba(0,0,0,0.3); }

/* 轮播图 */
.hero-section { position: relative; margin-bottom: 60px; }
.banner-image {
  height: 100%; background-size: cover; background-position: center;
  position: relative; display: flex; align-items: center; justify-content: center;
}
.banner-mask { position: absolute; inset: 0; background: rgba(0,0,0,0.2); }
.banner-content { position: relative; z-index: 1; text-align: center; color: white; margin-top: -50px; }
.banner-content h1 { font-size: 48px; margin-bottom: 10px; text-shadow: 0 4px 8px rgba(0,0,0,0.4); }
.banner-content p { font-size: 18px; letter-spacing: 2px; }

/* 搜索卡片 */
.search-card-wrapper {
  position: absolute; bottom: -50px; left: 50%; transform: translateX(-50%);
  width: 1000px; z-index: 5;
}
.search-card { border-radius: 12px; border: none; box-shadow: 0 10px 30px rgba(0,0,0,0.15); }
.search-form { display: flex; align-items: flex-end; gap: 15px; margin-bottom: 20px; }
.form-item { flex: 1; display: flex; flex-direction: column; gap: 8px; }
.label { font-size: 12px; color: #909399; font-weight: bold; text-transform: uppercase; }
.exchange-icon { 
  width: 40px; height: 40px; border-radius: 50%; background: #f2f6fc; 
  display: flex; align-items: center; justify-content: center; cursor: pointer;
  margin-bottom: 2px; transition: all 0.3s;
}
.exchange-icon:hover { transform: rotate(180deg); background: #e6e8eb; }
.search-btn { height: 45px; font-weight: bold; font-size: 16px; min-width: 140px; }
.filter-row { display: flex; align-items: center; padding-top: 10px; border-top: 1px solid #eee; }
.filter-label { font-size: 14px; color: #606266; font-weight: 500; }

/* 内容区 */
.main-content { max-width: 1200px; margin: 0 auto; padding: 0 20px 50px 20px; }
.section-title { font-size: 20px; font-weight: bold; margin: 40px 0 20px; color: #303133; border-left: 4px solid #409EFF; padding-left: 10px; }

/* 推荐卡片 */
.recommend-card { cursor: pointer; transition: transform 0.3s; border: none; overflow: hidden; }
.recommend-card:hover { transform: translateY(-5px); box-shadow: 0 8px 20px rgba(0,0,0,0.1); }
.recommend-image { width: 100%; height: 160px; object-fit: cover; }
.recommend-name { font-size: 18px; font-weight: bold; color: #333; }
.bottom { margin-top: 10px; display: flex; justify-content: space-between; align-items: center; }
.price { color: #f56c6c; font-size: 20px; font-weight: 800; }

/* 表格 */
.table-card { border-radius: 8px; }
.route-info { display: flex; align-items: center; justify-content: space-between; width: 100%; }
.city { font-size: 16px; font-weight: bold; }
.arrow { display: flex; flex-direction: column; align-items: center; color: #909399; font-size: 12px; margin: 0 10px; }
.price-text { font-size: 18px; color: #f56c6c; font-weight: bold; }
.red-text { color: #f56c6c; font-weight: bold; }
.green-text { color: #67c23a; }

/* 登机牌样式 */
.boarding-pass {
  background: white; border-radius: 12px; overflow: hidden;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1); border: 1px solid #e4e7ed;
}
.pass-header {
  background: #409EFF; color: white; padding: 15px 20px;
  display: flex; justify-content: space-between; align-items: center;
}
.pass-airline { font-weight: bold; letter-spacing: 2px; font-size: 16px; }
.pass-class { font-size: 12px; opacity: 0.9; border: 1px solid white; padding: 2px 6px; border-radius: 4px; }
.pass-body { padding: 20px; }
.pass-row { display: flex; justify-content: space-between; margin-bottom: 20px; }
.city-row { align-items: center; color: #409EFF; border-bottom: 2px dashed #eee; padding-bottom: 20px; }
.city-code { font-size: 24px; font-weight: bold; }
.plane-icon { font-size: 24px; transform: rotate(45deg); color: #dcdfe6; }
.pass-item { display: flex; flex-direction: column; }
.pass-item .label { font-size: 10px; color: #909399; letter-spacing: 1px; margin-bottom: 4px; }
.pass-item .value { font-size: 16px; font-weight: bold; color: #303133; }
.pass-footer { background: #f5f7fa; padding: 15px; text-align: center; border-top: 1px solid #eee; }
.barcode {
  font-family: 'Courier New', Courier, monospace; font-weight: 900; font-size: 24px;
  letter-spacing: 4px; transform: scaleY(0.6); color: #333; margin-bottom: 5px;
}
.ticket-no { font-size: 10px; color: #909399; }

/* === 新增：选座样式 === */
.seat-container { padding: 0 20px; }
.cockpit { height: 30px; border-radius: 50% 50% 0 0; background: #eee; margin: 0 auto 10px; width: 60%; }
.fuselage { border: 2px solid #eee; border-radius: 20px; padding: 20px; background: #fafafa; }
.seat-row-letters { display: flex; justify-content: space-between; padding: 0 10px; margin-bottom: 10px; font-weight: bold; color: #909399; }
.aisle-spacer { width: 40px; text-align: center; font-size: 12px; font-weight: normal; }
.seat-row { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
.aisle-num { width: 40px; text-align: center; color: #ccc; font-size: 14px; }
.seat { 
  width: 40px; height: 40px; border-radius: 6px; display: flex; align-items: center; justify-content: center; 
  cursor: pointer; font-size: 12px; transition: all 0.2s; border: 1px solid #dcdfe6; background: white;
}
.seat.available:hover { border-color: #409EFF; color: #409EFF; transform: scale(1.1); }
.seat.selected { background: #67c23a; color: white; border-color: #67c23a; }
.seat.occupied { background: #f56c6c; color: white; border-color: #f56c6c; cursor: not-allowed; opacity: 0.6; }
.seat-legend { display: flex; justify-content: center; gap: 20px; margin-top: 20px; border-top: 1px solid #eee; padding-top: 20px; }
.legend-item { display: flex; align-items: center; gap: 5px; font-size: 12px; }
.legend-item .seat { width: 20px; height: 20px; cursor: default; }
.selected-info { text-align: center; margin-top: 20px; font-size: 16px; }
</style>