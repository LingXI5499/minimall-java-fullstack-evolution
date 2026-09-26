import { createApp } from 'vue'
import App from './App.vue'
import { createRouter, createWebHistory } from 'vue-router'
import ProductView from './views/ProductView.vue'
import OrderView from './views/OrderView.vue'

// 地址栏可直接访问 /products 或 /orders；生产 Nginx 需回退到 index.html。
const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/products' },
    { path: '/products', component: ProductView },
    { path: '/orders', component: OrderView }
  ]
})

createApp(App).use(router).mount('#app')
