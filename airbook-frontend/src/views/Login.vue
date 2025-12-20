<template>
  <div class="landing-container">
    <div class="bg-overlay"></div>
    
    <div class="welcome-content">
      <div class="brand animated-down">
        <el-icon class="brand-icon"><Promotion /></el-icon>
        <span>Airbook</span>
      </div>
      <h1 class="slogan animated-up">探索世界，<br>从一张机票开始。</h1>
      <p class="sub-slogan animated-up delay-1">安全 · 便捷 · 智能的全球航旅服务商</p>
      
      <button class="start-btn animated-up delay-2" @click="dialogVisible = true">
        开启您的旅程 <el-icon style="vertical-align: middle"><Right /></el-icon>
      </button>
    </div>

    <el-dialog
      v-model="dialogVisible"
      width="400px"
      :show-close="false"
      class="custom-login-dialog"
      align-center
    >
      <div class="login-box">
        <div class="form-title">
          {{ isRegister ? '注册会员' : '欢迎回来' }}
        </div>
        
        <div class="form-subtitle">
          {{ isRegister ? '填写信息加入我们' : '登录以管理您的行程' }}
        </div>

        <el-form :model="form" size="large" class="login-form">
          <el-form-item>
            <el-input v-model="form.uname" placeholder="用户名/账号" :prefix-icon="User" />
          </el-form-item>
          
          <el-form-item>
            <el-input v-model="form.upass" type="password" placeholder="密码" show-password :prefix-icon="Lock" />
          </el-form-item>

          <el-form-item v-if="isRegister">
            <el-input v-model="form.ucity" placeholder="常居城市 (例如: 上海)" :prefix-icon="House" />
          </el-form-item>

          <el-button type="primary" class="submit-btn" @click="handleSubmit" :loading="loading" round>
            {{ isRegister ? '立 即 注 册' : '登 录 系 统' }}
          </el-button>
        </el-form>
        
        <div class="form-footer">
          <span v-if="!isRegister">
            还没有账号？ <span class="link" @click="isRegister = true">去注册</span>
          </span>
          <span v-else>
            已有账号？ <span class="link" @click="isRegister = false">去登录</span>
          </span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock, Promotion, House, Right } from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const router = useRouter()
const dialogVisible = ref(false) // 控制弹窗显示
const isRegister = ref(false)    // 控制登录/注册切换
const loading = ref(false)

const form = reactive({
  uname: '',
  upass: '',
  ucity: ''
})

const handleSubmit = async () => {
  if (!form.uname || !form.upass) {
    ElMessage.warning('请输入完整的账号和密码')
    return
  }

  loading.value = true
  try {
    const url = isRegister.value 
      ? 'http://localhost:8080/api/user/register' 
      : 'http://localhost:8080/api/user/login'

    const res = await axios.post(url, form)

    if (res.data.code === 200) {
      if (isRegister.value) {
        ElMessage.success('注册成功！请登录')
        isRegister.value = false
        form.ucity = ''
      } else {
        ElMessage.success('登录成功')
        // 只有登录成功才关闭弹窗并跳转
        dialogVisible.value = false 
        localStorage.setItem('user', JSON.stringify(res.data.data))
        router.push('/home')
      }
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (err) {
    ElMessage.error('无法连接到服务器')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* 1. 背景样式 */
.landing-container {
  height: 100vh;
  width: 100vw;
  /* 使用 Unsplash 的高质量飞机/天空图片 */
  background-image: url('https://images.unsplash.com/photo-1436491865332-7a61a109cc05?q=80&w=2074&auto=format&fit=crop');
  background-size: cover;
  background-position: center;
  position: relative;
  overflow: hidden;
}

/* 黑色半透明遮罩，让文字更清晰 */
.bg-overlay {
  position: absolute;
  top: 0; left: 0; width: 100%; height: 100%;
  background: rgba(0, 0, 0, 0.4);
  z-index: 1;
}

/* 2. 欢迎内容布局 */
.welcome-content {
  position: relative;
  z-index: 2;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding-left: 10%; /* 左侧对齐布局 */
  color: white;
}

.brand {
  font-size: 24px;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  color: #409EFF; /* 品牌色 */
}
.brand-icon { font-size: 30px; }

.slogan {
  font-size: 60px;
  line-height: 1.2;
  font-weight: 800;
  margin: 0;
  text-shadow: 0 4px 10px rgba(0,0,0,0.3);
}

.sub-slogan {
  font-size: 20px;
  margin-top: 20px;
  opacity: 0.9;
  font-weight: 300;
}

/* 开启旅程按钮 */
.start-btn {
  margin-top: 40px;
  padding: 15px 40px;
  font-size: 18px;
  font-weight: bold;
  color: white;
  background: #409EFF;
  border: none;
  border-radius: 50px;
  cursor: pointer;
  width: fit-content;
  transition: all 0.3s ease;
  box-shadow: 0 10px 20px rgba(64, 158, 255, 0.4);
}
.start-btn:hover {
  background: #66b1ff;
  transform: translateY(-2px);
  box-shadow: 0 15px 25px rgba(64, 158, 255, 0.5);
}

/* 3. 弹窗内样式 */
.login-box {
  padding: 10px 10px;
  text-align: center;
}
.form-title { font-size: 26px; font-weight: bold; color: #333; margin-bottom: 5px; }
.form-subtitle { font-size: 14px; color: #999; margin-bottom: 30px; }
.submit-btn { width: 100%; font-weight: bold; margin-top: 10px; height: 45px; }
.form-footer { margin-top: 20px; font-size: 14px; color: #666; }
.link { color: #409EFF; cursor: pointer; font-weight: bold; }
.link:hover { text-decoration: underline; }

/* 4. 入场动画 Keyframes */
.animated-up { animation: fadeInUp 1s ease-out forwards; opacity: 0; transform: translateY(30px); }
.animated-down { animation: fadeInDown 1s ease-out forwards; opacity: 0; transform: translateY(-30px); }
.delay-1 { animation-delay: 0.2s; }
.delay-2 { animation-delay: 0.4s; }

@keyframes fadeInUp {
  to { opacity: 1; transform: translateY(0); }
}
@keyframes fadeInDown {
  to { opacity: 1; transform: translateY(0); }
}

/* 5. 覆盖 Element Plus Dialog 默认样式，做成圆角卡片 */
:deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
}
:deep(.el-dialog__header) { display: none; /* 隐藏默认头部 */ }
:deep(.el-dialog__body) { padding: 30px; }
</style>