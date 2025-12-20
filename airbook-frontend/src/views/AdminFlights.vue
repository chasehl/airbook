<template>
  <div>
    <el-card shadow="never" class="toolbar">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-input v-model="keyword" placeholder="搜索航班号/城市..." clearable @clear="loadData" />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button type="success" @click="openDialog(null)">新增航班</el-button>
        </el-col>
      </el-row>
    </el-card>

    <el-card shadow="never" style="margin-top: 20px">
      <el-table :data="tableData" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="flightNo" label="航班号" width="100">
          <template #default="scope">
            <el-tag effect="dark">{{ scope.row.flightNo }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="航线" width="180">
          <template #default="scope">
            {{ scope.row.departure }} <el-icon><Right /></el-icon> {{ scope.row.destination }}
          </template>
        </el-table-column>
        <el-table-column label="起飞时间" width="180">
          <template #default="scope">{{ formatTime(scope.row.startTime) }}</template>
        </el-table-column>
        <el-table-column prop="airline" label="航空公司" width="120" />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="scope">¥ {{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column label="座位情况" width="150">
          <template #default="scope">
            <el-progress 
              :percentage="Math.floor(((scope.row.totalSeats - scope.row.availableSeats) / scope.row.totalSeats) * 100)" 
              :status="scope.row.availableSeats === 0 ? 'exception' : ''"
            />
            <span style="font-size: 12px; color: #999">余 {{ scope.row.availableSeats }} / 总 {{ scope.row.totalSeats }}</span>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" fixed="right" width="180">
          <template #default="scope">
            <el-button type="primary" link @click="openDialog(scope.row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑航班' : '新增航班'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="航班号">
          <el-input v-model="form.flightNo" placeholder="如: CA1234" />
        </el-form-item>
        <el-form-item label="航空公司">
          <el-input v-model="form.airline" placeholder="如: 中国国航" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="出发地">
              <el-input v-model="form.departure" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目的地">
              <el-input v-model="form.destination" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="起飞时间">
          <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="到达时间">
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择时间" style="width: 100%" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="价格">
              <el-input-number v-model="form.price" :min="0" :step="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总座位">
              <el-input-number v-model="form.totalSeats" :min="10" :step="10" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Right } from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const tableData = ref([])
const keyword = ref('')
const dialogVisible = ref(false)

// 表单数据
const form = reactive({
  id: null,
  flightNo: '',
  airline: '',
  departure: '',
  destination: '',
  startTime: '',
  endTime: '',
  price: 0,
  totalSeats: 100
})

onMounted(() => {
  loadData()
})

// 加载数据
const loadData = async () => {
  loading.value = true
  const res = await axios.get('http://localhost:8080/api/flight/list', {
    params: { keyword: keyword.value }
  })
  if (res.data.code === 200) {
    tableData.value = res.data.data
  }
  loading.value = false
}

// 打开弹窗 (如果是编辑，回填数据；如果是新增，重置表单)
const openDialog = (row) => {
  if (row) {
    // 编辑模式：复制对象
    Object.assign(form, row)
  } else {
    // 新增模式：清空
    form.id = null
    form.flightNo = ''
    form.airline = ''
    form.departure = ''
    form.destination = ''
    form.startTime = ''
    form.endTime = ''
    form.price = 500
    form.totalSeats = 100
  }
  dialogVisible.value = true
}

// 保存 (新增或修改)
const handleSave = async () => {
  // 加一个 loading 状态，防止重复点击
  loading.value = true
  try {
    const res = await axios.post('http://localhost:8080/api/flight/save', form)
    
    if (res.data.code === 200) {
      ElMessage.success('保存成功！')
      dialogVisible.value = false
      loadData() // 刷新表格
    } else {
      ElMessage.error('保存失败: ' + res.data.msg)
    }
  } catch (err) {
    console.error(err) // 在控制台打印详细错误
    ElMessage.error('系统错误，请检查后端控制台')
  } finally {
    loading.value = false
  }
}

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除该航班吗？', '警告', { type: 'warning' })
    .then(async () => {
      const res = await axios.post('http://localhost:8080/api/flight/delete', { id })
      if (res.data.code === 200) {
        ElMessage.success('删除成功')
        loadData()
      }
    })
}

const formatTime = (timeStr) => {
  if(!timeStr) return ''
  return timeStr.replace('T', ' ').substring(0, 16)
}
</script>