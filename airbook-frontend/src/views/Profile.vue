<template>
  <div class="profile-container">
    <el-page-header @back="router.push('/home')" content="个人中心" class="page-header" />

    <div class="content-wrapper">
      <el-card class="box-card user-card">
        <div class="avatar-wrapper">
          <img :src="`https://api.dicebear.com/7.x/adventurer/svg?seed=${form.uname}&backgroundColor=b6e3f4`" class="avatar-img" />
        </div>
        <div class="user-name">{{ form.uname }}</div>
        <div class="user-role">
          <el-tag effect="dark" round>{{ form.role === 1 ? '管理员' : '尊贵会员' }}</el-tag>
        </div>
        <div class="stats">
          <div class="stat-item">
            <div class="num">0</div> <div class="txt">订单</div>
          </div>
          <div class="stat-item">
            <div class="num">0</div>
            <div class="txt">积分</div>
          </div>
          <div class="stat-item">
            <div class="num">{{ form.ucity || '未知' }}</div>
            <div class="txt">城市</div>
          </div>
        </div>
      </el-card>

      <el-card class="box-card form-card">
        <template #header>
          <div class="card-header">
            <span>✏️ 编辑资料</span>
          </div>
        </template>
        
        <el-form :model="form" label-width="80px" size="large">
          <el-form-item label="用户ID">
            <el-input v-model="form.id" disabled />
          </el-form-item>
          
          <el-form-item label="账号">
            <el-input v-model="form.uname" disabled />
          </el-form-item>

          <el-form-item label="真实姓名">
            <el-input v-model="form.realName" placeholder="请输入真实姓名" />
          </el-form-item>

          <el-form-item label="所在城市">
            <el-input v-model="form.ucity" placeholder="例如：北京" />
          </el-form-item>

          <el-form-item label="性别">
            <el-radio-group v-model="form.usex">
              <el-radio label="男">男</el-radio>
              <el-radio label="女">女</el-radio>
              <el-radio label="保密">保密</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleUpdate" :loading="loading">保存修改</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const form = ref({
  id: '',
  uname: '',
  realName: '',
  ucity: '',
  usex: '',
  role: 0
})

onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    // 初始化表单数据
    const user = JSON.parse(userStr)
    // 复制数据到 form，避免直接修改 localstorage 里的对象
    form.value = { ...user }
  } else {
    router.push('/')
  }
})

const handleUpdate = async () => {
  loading.value = true
  try {
    const res = await axios.post('http://localhost:8080/api/user/update', form.value)
    
    if (res.data.code === 200) {
      ElMessage.success('保存成功！')
      // 更新本地缓存，这样回到首页名字也会变
      localStorage.setItem('user', JSON.stringify(res.data.data))
      // 更新页面显示
      form.value = res.data.data
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (err) {
    ElMessage.error('保存失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 20px;
}
.page-header { margin-bottom: 30px; }

.content-wrapper {
  max-width: 1000px;
  margin: 0 auto;
  display: flex;
  gap: 20px;
}

/* 左侧卡片 */
.user-card { flex: 1; text-align: center; height: fit-content; }
.avatar-wrapper {
  width: 120px; height: 120px; margin: 0 auto 20px;
  border-radius: 50%; border: 4px solid #fff;
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
  overflow: hidden; background: #b6e3f4;
}
.avatar-img { width: 100%; height: 100%; object-fit: cover; }
.user-name { font-size: 24px; font-weight: bold; color: #303133; margin-bottom: 10px; }
.stats {
  margin-top: 30px; border-top: 1px solid #eee; padding-top: 20px;
  display: flex; justify-content: space-around;
}
.stat-item .num { font-size: 20px; font-weight: bold; color: #409EFF; }
.stat-item .txt { font-size: 12px; color: #909399; margin-top: 5px; }

/* 右侧表单 */
.form-card { flex: 2; }
.card-header { font-weight: bold; }
</style>