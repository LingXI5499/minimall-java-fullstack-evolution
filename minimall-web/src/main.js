import { createApp } from 'vue'
import App from './App.vue'
import { createRouter, createWebHistory } from 'vue-router'
import ProductView from './views/ProductView.vue'
import OrderView from './views/OrderView.vue'
import LoginView from './views/LoginView.vue'
import { session } from './auth/session'

// 地址栏可直接访问 /products 或 /orders；生产 Nginx 需回退到 index.html。
const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/products' },
    { path: '/login', component: LoginView },
    { path: '/products', component: ProductView },
    { path: '/orders', component: OrderView }
  ]
})

router.beforeEach(to => {
  if (to.path === '/login') return session.token ? '/products' : true
  return session.token ? true : '/login'
})

createApp(App).use(router).mount('#app')
