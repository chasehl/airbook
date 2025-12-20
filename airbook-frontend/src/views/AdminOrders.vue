<template>
  <div>
    <el-card shadow="never">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; font-size: 16px">📋 全平台订单监控</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="loadData">刷新数据</el-button>
      </div>

      <el-table :data="tableData" border stripe v-loading="loading" style="margin-top: 20px">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="orderNo" label="订单号" width="150" />
        
        <el-table-column prop="userId" label="用户ID" width="80" />
        
        <el-table-column prop="flightInfo" label="航班信息" />
        
        <el-table-column prop="seatNo" label="座位" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.seatNo" type="warning">{{ scope.row.seatNo }}</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>

        <el-table-column prop="price" label="金额" width="100">
          <template #default="scope">¥ {{ scope.row.price }}</template>
        </el-table-column>

        <el-table-column prop="createTime" label="下单时间" width="180">
          <template #default="scope">{{ formatTime(scope.row.createTime) }}</template>
        </el-table-column>

        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 0 ? 'success' : 'info'">
              {{ scope.row.status === 0 ? '正常' : '已退票' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" fixed="right" width="120">
          <template #default="scope">
            <el-popconfirm title="确定强制退掉这张票吗？" @confirm="handleCancel(scope.row.id)">
              <template #reference>
                <el-button 
                  type="danger" 
                  size="small" 
                  :disabled="scope.row.status === 1"
                >
                  强制退票
                </el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const tableData = ref([])

onMounted(() => {
  loadData()
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await axios.get('http://localhost:8080/api/order/all')
    if (res.data.code === 200) {
      tableData.value = res.data.data
    }
  } catch (err) {
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 强制退票 (复用之前的退票接口即可)
const handleCancel = async (orderId) => {
  try {
    const res = await axios.post('http://localhost:8080/api/order/cancel', { orderId })
    if (res.data.code === 200) {
      ElMessage.success('操作成功')
      loadData() // 刷新
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (err) {
    ElMessage.error('操作失败')
  }
}

const formatTime = (timeStr) => {
  if(!timeStr) return ''
  return timeStr.replace('T', ' ').substring(0, 16)
}
</script>