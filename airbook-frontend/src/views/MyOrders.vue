<template>
  <div class="container">
    <el-page-header @back="goBack" content="我的订单" style="margin-bottom: 20px;" />
    
    <el-card>
      <el-table :data="orderList" stripe style="width: 100%">
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="flightInfo" label="航班信息" />
        <el-table-column label="价格" width="120">
          <template #default="scope">¥ {{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column label="下单时间" width="180">
          <template #default="scope">{{ formatTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 0 ? 'success' : 'info'">
              {{ scope.row.status === 0 ? '已出票' : '已退票' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button 
              v-if="scope.row.status === 0" 
              type="danger" 
              size="small" 
              @click="handleCancel(scope.row.id)"
            >
              退票
            </el-button>
            <span v-else style="color: #999; font-size: 12px">不可操作</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const orderList = ref([])
const user = ref({})

onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    user.value = JSON.parse(userStr)
    getOrders()
  } else {
    router.push('/')
  }
})

const getOrders = async () => {
  const res = await axios.get('http://localhost:8080/api/order/list', {
    params: { userId: user.value.id }
  })
  if (res.data.code === 200) {
    orderList.value = res.data.data
  }
}

const handleCancel = (orderId) => {
  ElMessageBox.confirm('确定要退掉这张票吗？退款将原路返回。', '退票确认', {
    confirmButtonText: '确定退票',
    cancelButtonText: '再想想',
    type: 'warning'
  }).then(async () => {
    const res = await axios.post('http://localhost:8080/api/order/cancel', { orderId })
    if (res.data.code === 200) {
      ElMessage.success('退票成功')
      getOrders() // 刷新列表
    } else {
      ElMessage.error(res.data.msg)
    }
  })
}

const goBack = () => router.push('/home')
const formatTime = (t) => t ? t.replace('T', ' ').substring(0, 16) : ''
</script>

<style scoped>
.container { padding: 20px; max-width: 1000px; margin: 0 auto; }
</style>