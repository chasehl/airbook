<template>
  <div class="admin-layout">
    <el-container class="main-container">
      
      <el-aside width="220px" class="aside">
        <div class="brand">
          <el-icon><Monitor /></el-icon>
          <span>Airbook Admin</span>
        </div>
        
        <el-menu
          active-text-color="#409EFF"
          background-color="#304156"
          text-color="#bfcbd9"
          :default-active="activeMenu"
          class="el-menu-vertical"
          router
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><Odometer /></el-icon>
            <span>数据概览</span>
          </el-menu-item>

          <el-menu-item index="/admin/flights">
            <el-icon><Promotion /></el-icon>
            <span>航班管理</span>
          </el-menu-item>

          <el-menu-item index="/admin/orders">
            <el-icon><List /></el-icon>
            <span>订单中心</span>
          </el-menu-item>

          <el-menu-item index="/home">
            <el-icon><HomeFilled /></el-icon>
            <span>返回前台</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-container>
        <el-header class="header">
          <div class="breadcrumb">
            后台管理系统 / {{ currentPathName }}
          </div>
          <div class="header-right">
            <el-avatar :size="32" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
            <span style="margin-left: 10px">管理员</span>
            <el-button type="text" style="color: #f56c6c; margin-left: 15px" @click="logout">退出</el-button>
          </div>
        </el-header>

        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Monitor, Odometer, Promotion, List, HomeFilled } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()

// 高亮当前菜单
const activeMenu = computed(() => route.path)

// 显示当前路径名
const currentPathName = computed(() => {
  if (route.path.includes('dashboard')) return '数据概览'
  if (route.path.includes('flights')) return '航班管理'
  if (route.path.includes('orders')) return '订单中心'
  return '首页'
})

const logout = () => {
  localStorage.removeItem('user')
  router.push('/')
}
</script>

<style scoped>
.admin-layout { height: 100vh; display: flex; }
.main-container { width: 100%; height: 100%; }

/* 侧边栏样式 */
.aside {
  background-color: #304156;
  color: white;
  display: flex; flex-direction: column;
}
.brand {
  height: 60px; line-height: 60px; font-size: 20px; font-weight: bold;
  display: flex; align-items: center; justify-content: center; gap: 10px;
  background-color: #2b3649;
}
.el-menu-vertical { border-right: none; }

/* 顶部栏样式 */
.header {
  background-color: white;
  border-bottom: 1px solid #e6e6e6;
  display: flex; justify-content: space-between; align-items: center;
}
.header-right { display: flex; align-items: center; font-size: 14px; }

/* 内容区背景 */
.main-content { background-color: #f0f2f5; padding: 20px; }
</style>