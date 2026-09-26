<script setup>
import { onMounted, ref } from 'vue'
import { orderApi } from '../api/order'

const orders = ref([])
const detail = ref(null)
const productId = ref('')
const quantity = ref(1)
const message = ref('')
const error = ref('')

// 创建成功后重新加载列表，让页面显示数据库实际保存的订单。
async function load() {
  try { orders.value = (await orderApi.list()).data }
  catch (e) { error.value = e.message }
}
async function show(id) {
  try { detail.value = (await orderApi.detail(id)).data; error.value = '' }
  catch (e) { error.value = e.message }
}
async function create() {
  try {
    // 价格不从表单提交；后端按数据库里的商品价格计算总额。
    const result = await orderApi.create([{ productId: Number(productId.value), quantity: Number(quantity.value) }])
    message.value = `订单 #${result.data.order.id} 创建成功`
    error.value = ''
    detail.value = result.data
    await load()
  } catch (e) { error.value = e.message }
}
onMounted(load)
</script>

<template>
  <main>
    <header><h1>订单管理</h1><p>下单事务：订单 → 明细 → 扣减库存</p></header>
    <p v-if="message" class="success">{{ message }}</p><p v-if="error" role="alert" class="error">{{ error }}</p>
    <section>
      <h2>创建订单</h2>
      <form @submit.prevent="create">
        <label>商品 ID <input v-model="productId" type="number" min="1" required /></label>
        <label>数量 <input v-model="quantity" type="number" min="1" required /></label>
        <button>提交订单</button>
      </form>
    </section>
    <section>
      <h2>最近订单</h2><button @click="load">刷新</button>
      <table><thead><tr><th>ID</th><th>状态</th><th>总金额</th><th>创建时间</th><th>操作</th></tr></thead>
        <tbody><tr v-for="order in orders" :key="order.id"><td>#{{ order.id }}</td><td>{{ order.status }}</td><td>¥{{ order.totalAmount }}</td><td>{{ order.createTime }}</td><td><button @click="show(order.id)">查看明细</button></td></tr></tbody></table>
    </section>
    <section v-if="detail"><h2>订单 #{{ detail.order.id }}</h2><p>状态：{{ detail.order.status }} · 总金额：¥{{ detail.order.totalAmount }}</p>
      <ul><li v-for="item in detail.items" :key="item.id">{{ item.productName }} × {{ item.quantity }} · ¥{{ item.subtotal }} · 分类：{{ item.categoryName || '未分类' }}</li></ul></section>
  </main>
</template>

<style scoped>
main{max-width:1100px;margin:auto;padding:2rem}header p{color:#667085}section{background:white;border:1px solid #e6e8ec;border-radius:12px;padding:1.5rem;margin:1.5rem 0}form{display:flex;gap:1rem;align-items:end;flex-wrap:wrap}label{display:grid;gap:.4rem}input,button{padding:.6rem}table{width:100%;border-collapse:collapse;margin-top:1rem}td,th{text-align:left;padding:.7rem;border-bottom:1px solid #eee}.error{color:#a11}.success{color:#16794b}
</style>
