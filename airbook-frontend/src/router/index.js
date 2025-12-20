import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import MyOrders from '../views/MyOrders.vue'
import Profile from '../views/Profile.vue'
import Admin from '../views/Admin.vue'
import AdminDashboard from '../views/AdminDashboard.vue'
import AdminFlights from '../views/AdminFlights.vue'
import AdminOrders from '../views/AdminOrders.vue'

const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login
  },
  {
    path: '/home',
    name: 'Home',
    component: Home
  },
  {
    path: '/orders',
    name: 'MyOrders',
    component: MyOrders
  },
  {
    path:'/Profile',
    name:'Profile',
    component:Profile
  },
  // 后台路由配置 
  {
    path: '/admin',
    name: 'Admin',
    component: Admin,
    redirect: '/admin/dashboard', // 默认跳到仪表盘
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: AdminDashboard
      },
      {
        path: 'flights',
        name: 'AdminFlights',
        component:AdminFlights
      },
      {
        path:'orders',
        name:'AdminOrders',
        component:AdminOrders
      }
    ]
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router