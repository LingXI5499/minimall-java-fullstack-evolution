<script setup>
import { onMounted, reactive, ref } from 'vue'
import axios from 'axios'

// ====================
// 商品列表
// ====================

const products = ref([])

// ====================
// 根据 ID 查询
// ====================

const queryId = ref('')
const queryResult = ref(null)

// ====================
// 新增 / 修改表单
// ====================

const editingId = ref(null)

const form = reactive({
  name: '',
  price: '',
  stock: ''
})

// ====================
// 页面提示
// ====================

const message = ref('')
const messageType = ref('success')


// ====================
// 查询全部商品
// ====================

async function loadProducts() {
  try {
    const response = await axios.get('/api/products')

    products.value = response.data
  } catch (error) {
    console.error(error)

    showMessage('加载商品列表失败', 'error')
  }
}


// ====================
// 根据 ID 查询商品
// ====================

async function queryProductById() {

  if (!queryId.value) {
    showMessage('请输入商品 ID', 'error')
    return
  }

  try {

    const response = await axios.get(
      `/api/products/${queryId.value}`
    )

    if (!response.data) {

      queryResult.value = null

      showMessage(
        `没有找到 ID=${queryId.value} 的商品`,
        'error'
      )

      return
    }

    queryResult.value = response.data

    showMessage('商品查询成功', 'success')

  } catch (error) {

    console.error(error)

    queryResult.value = null

    showMessage('查询商品失败', 'error')
  }
}


// ====================
// 清空 ID 查询
// ====================

function clearQuery() {

  queryId.value = ''
  queryResult.value = null
}


// ====================
// 新增 / 修改商品
// ====================

async function saveProduct() {

  if (!form.name) {
    showMessage('请输入商品名称', 'error')
    return
  }

  if (form.price === '') {
    showMessage('请输入商品价格', 'error')
    return
  }

  if (form.stock === '') {
    showMessage('请输入商品库存', 'error')
    return
  }

  const data = {
    name: form.name,
    price: Number(form.price),
    stock: Number(form.stock)
  }

  try {

    if (editingId.value === null) {

      await axios.post(
        '/api/products',
        data
      )

      showMessage('新增商品成功', 'success')

    } else {

      await axios.put(
        `/api/products/${editingId.value}`,
        data
      )

      showMessage('修改商品成功', 'success')
    }

    resetForm()

    await loadProducts()

  } catch (error) {

    console.error(error)

    showMessage('保存商品失败', 'error')
  }
}


// ====================
// 点击编辑
// ====================

function editProduct(product) {

  editingId.value = product.id

  form.name = product.name
  form.price = product.price
  form.stock = product.stock

  showMessage(
    `正在编辑商品 ID=${product.id}`,
    'info'
  )

  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}


// ====================
// 把“按 ID 查询结果”放入编辑表单
// ====================

function editQueryResult() {

  if (!queryResult.value) {
    return
  }

  editProduct(queryResult.value)
}


// ====================
// 删除商品
// ====================

async function deleteProduct(product) {

  const confirmed = window.confirm(
    `确定删除商品「${product.name}」吗？`
  )

  if (!confirmed) {
    return
  }

  try {

    await axios.delete(
      `/api/products/${product.id}`
    )

    showMessage('删除商品成功', 'success')

    if (
      queryResult.value &&
      queryResult.value.id === product.id
    ) {
      queryResult.value = null
    }

    await loadProducts()

  } catch (error) {

    console.error(error)

    showMessage('删除商品失败', 'error')
  }
}


// ====================
// 清空表单
// ====================

function resetForm() {

  form.name = ''
  form.price = ''
  form.stock = ''

  editingId.value = null
}


// ====================
// 页面消息
// ====================

function showMessage(text, type) {

  message.value = text
  messageType.value = type
}


// ====================
// 金额格式化
// ====================

function formatPrice(price) {

  if (price === null || price === undefined) {
    return '-'
  }

  return Number(price).toFixed(2)
}


// ====================
// 页面加载
// ====================

onMounted(() => {
  loadProducts()
})
</script>

<template>

  <main class="page">

    <!-- ============================= -->
    <!-- 页面标题 -->
    <!-- ============================= -->

    <header class="page-header">

      <div>
        <h1>MiniMall</h1>
        <p>Java 全栈 CRUD · 商品管理控制台</p>
      </div>

      <button
        class="refresh-button"
        @click="loadProducts"
      >
        刷新数据
      </button>

    </header>


    <!-- ============================= -->
    <!-- 消息提示 -->
    <!-- ============================= -->

    <div
      v-if="message"
      class="message"
      :class="messageType"
    >
      {{ message }}
    </div>


    <!-- ============================= -->
    <!-- 根据 ID 查询 -->
    <!-- ============================= -->

    <section class="card">

      <div class="card-title">

        <div>
          <h2>根据 ID 查询</h2>
          <p>GET /api/products/{id}</p>
        </div>

      </div>


      <div class="query-row">

        <input
          v-model="queryId"
          type="number"
          placeholder="请输入商品 ID"
        />

        <button
          class="primary-button"
          @click="queryProductById"
        >
          查询
        </button>

        <button
          class="secondary-button"
          @click="clearQuery"
        >
          清空
        </button>

      </div>


      <!-- 查询结果 -->

      <div
        v-if="queryResult"
        class="query-result"
      >

        <div class="query-result-main">

          <div class="product-id">
            #{{ queryResult.id }}
          </div>

          <div>

            <h3>
              {{ queryResult.name }}
            </h3>

            <p>
              商品价格：
              ¥{{ formatPrice(queryResult.price) }}
            </p>

            <p>
              商品库存：
              {{ queryResult.stock }}
            </p>

          </div>

        </div>


        <div class="query-result-actions">

          <span
            class="status"
            :class="queryResult.status === 1
              ? 'enabled'
              : 'disabled'"
          >
            {{
              queryResult.status === 1
                ? '启用'
                : '停用'
            }}
          </span>

          <button
            class="secondary-button"
            @click="editQueryResult"
          >
            编辑这个商品
          </button>

        </div>

      </div>

    </section>


    <!-- ============================= -->
    <!-- 新增 / 修改 -->
    <!-- ============================= -->

    <section class="card">

      <div class="card-title">

        <div>

          <h2>
            {{
              editingId === null
                ? '新增商品'
                : '修改商品'
            }}
          </h2>

          <p v-if="editingId === null">
            POST /api/products
          </p>

          <p v-else>
            PUT /api/products/{{ editingId }}
          </p>

        </div>


        <span
          v-if="editingId !== null"
          class="editing-badge"
        >
          正在编辑 ID {{ editingId }}
        </span>

      </div>


      <form
        class="product-form"
        @submit.prevent="saveProduct"
      >

        <div class="field">

          <label>商品名称</label>

          <input
            v-model="form.name"
            type="text"
            placeholder="例如：Mechanical Keyboard"
          />

        </div>


        <div class="field">

          <label>商品价格</label>

          <input
            v-model="form.price"
            type="number"
            step="0.01"
            min="0"
            placeholder="0.00"
          />

        </div>


        <div class="field">

          <label>商品库存</label>

          <input
            v-model="form.stock"
            type="number"
            min="0"
            placeholder="0"
          />

        </div>


        <div class="form-actions">

          <button
            class="primary-button"
            type="submit"
          >
            {{
              editingId === null
                ? '新增商品'
                : '保存修改'
            }}
          </button>


          <button
            v-if="editingId !== null"
            class="secondary-button"
            type="button"
            @click="resetForm"
          >
            取消修改
          </button>

        </div>

      </form>

    </section>


    <!-- ============================= -->
    <!-- 商品列表 -->
    <!-- ============================= -->

    <section class="card">

      <div class="card-title">

        <div>

          <h2>全部商品</h2>

          <p>
            GET /api/products
          </p>

        </div>


        <span class="count">
          共 {{ products.length }} 条
        </span>

      </div>


      <div class="table-wrapper">

        <table>

          <thead>

            <tr>
              <th>ID</th>
              <th>商品名称</th>
              <th>价格</th>
              <th>库存</th>
              <th>状态</th>
              <th>更新时间</th>
              <th>操作</th>
            </tr>

          </thead>


          <tbody>

            <tr
              v-for="product in products"
              :key="product.id"
            >

              <td class="id-cell">
                #{{ product.id }}
              </td>

              <td class="name-cell">
                {{ product.name }}
              </td>

              <td>
                ¥{{ formatPrice(product.price) }}
              </td>

              <td>
                {{ product.stock }}
              </td>

              <td>

                <span
                  class="status"
                  :class="product.status === 1
                    ? 'enabled'
                    : 'disabled'"
                >
                  {{
                    product.status === 1
                      ? '启用'
                      : '停用'
                  }}
                </span>

              </td>

              <td>
                {{ product.updateTime || '-' }}
              </td>

              <td>

                <div class="table-actions">

                  <button
                    class="edit-button"
                    @click="editProduct(product)"
                  >
                    编辑
                  </button>

                  <button
                    class="delete-button"
                    @click="deleteProduct(product)"
                  >
                    删除
                  </button>

                </div>

              </td>

            </tr>


            <tr v-if="products.length === 0">

              <td
                colspan="7"
                class="empty"
              >
                暂无商品数据
              </td>

            </tr>

          </tbody>

        </table>

      </div>

    </section>

  </main>

</template>


<style scoped>

* {
  box-sizing: border-box;
}

.page {
  max-width: 1180px;
  margin: 0 auto;
  padding: 48px 24px 80px;
  font-family:
    Inter,
    "Microsoft YaHei",
    Arial,
    sans-serif;
  color: #172033;
}


/* =========================
   页面标题
========================= */

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 28px;
}

.page-header h1 {
  margin: 0;
  font-size: 36px;
  letter-spacing: -1px;
}

.page-header p {
  margin: 8px 0 0;
  color: #7b8495;
}


/* =========================
   卡片
========================= */

.card {
  margin-bottom: 24px;
  padding: 28px;
  background: #ffffff;
  border: 1px solid #e6e8ec;
  border-radius: 14px;
  box-shadow:
    0 8px 30px rgba(0, 0, 0, 0.04);
}

.card-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.card-title h2 {
  margin: 0;
  font-size: 21px;
}

.card-title p {
  margin: 6px 0 0;
  font-size: 13px;
  color: #8c95a5;
}


/* =========================
   消息
========================= */

.message {
  margin-bottom: 20px;
  padding: 12px 16px;
  border-radius: 8px;
}

.message.success {
  background: #ecfdf3;
  color: #16794b;
}

.message.error {
  background: #fff0f0;
  color: #c83b3b;
}

.message.info {
  background: #eef5ff;
  color: #2468c9;
}


/* =========================
   查询区域
========================= */

.query-row {
  display: flex;
  gap: 10px;
}

.query-row input {
  width: 280px;
  padding: 11px 13px;
  border: 1px solid #d8dce4;
  border-radius: 8px;
  outline: none;
}

.query-row input:focus {
  border-color: #2563eb;
}

.query-result {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 22px;
  padding: 20px;
  background: #f8fafc;
  border-radius: 10px;
}

.query-result-main {
  display: flex;
  align-items: center;
  gap: 18px;
}

.query-result h3 {
  margin: 0 0 8px;
}

.query-result p {
  margin: 4px 0;
  color: #626d7e;
}

.product-id {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 62px;
  height: 62px;
  border-radius: 12px;
  background: #e8f0ff;
  color: #2563eb;
  font-weight: bold;
}

.query-result-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}


/* =========================
   表单
========================= */

.product-form {
  display: grid;
  grid-template-columns:
    repeat(3, 1fr);
  gap: 18px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.field label {
  font-size: 14px;
  font-weight: 600;
}

.field input {
  padding: 11px 13px;
  border: 1px solid #d8dce4;
  border-radius: 8px;
  outline: none;
}

.field input:focus {
  border-color: #2563eb;
}

.form-actions {
  grid-column: 1 / -1;
  margin-top: 4px;
}

.editing-badge {
  padding: 6px 10px;
  border-radius: 20px;
  background: #fff6dd;
  color: #a86700;
  font-size: 13px;
}


/* =========================
   按钮
========================= */

button {
  cursor: pointer;
  border-radius: 7px;
  font-size: 14px;
  transition: 0.15s;
}

.primary-button {
  padding: 10px 18px;
  border: none;
  background: #2563eb;
  color: white;
}

.primary-button:hover {
  background: #1d4ed8;
}

.secondary-button {
  padding: 9px 16px;
  border: 1px solid #d6dae2;
  background: white;
  color: #404859;
}

.secondary-button:hover {
  background: #f5f6f8;
}

.refresh-button {
  padding: 10px 16px;
  border: 1px solid #d6dae2;
  background: white;
}

.edit-button,
.delete-button {
  padding: 7px 12px;
}

.edit-button {
  border: 1px solid #bdd1ff;
  background: #eef4ff;
  color: #285fb8;
}

.delete-button {
  border: 1px solid #ffcaca;
  background: #fff3f3;
  color: #c43a3a;
}


/* =========================
   表格
========================= */

.table-wrapper {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th {
  padding: 13px 12px;
  background: #f7f8fa;
  color: #60697a;
  font-size: 13px;
  text-align: left;
  border-bottom: 1px solid #e2e5ea;
}

td {
  padding: 15px 12px;
  border-bottom: 1px solid #edf0f3;
}

tbody tr:hover {
  background: #fafbfc;
}

.id-cell {
  color: #7c8798;
}

.name-cell {
  font-weight: 600;
}

.table-actions {
  display: flex;
  gap: 8px;
}


/* =========================
   状态
========================= */

.status {
  display: inline-block;
  padding: 4px 9px;
  border-radius: 20px;
  font-size: 12px;
}

.status.enabled {
  background: #e9f9ef;
  color: #17874b;
}

.status.disabled {
  background: #f2f3f5;
  color: #737b89;
}

.count {
  color: #747e8e;
  font-size: 14px;
}

.empty {
  padding: 40px;
  text-align: center;
  color: #959eac;
}


/* =========================
   小屏幕
========================= */

@media (max-width: 800px) {

  .product-form {
    grid-template-columns: 1fr;
  }

  .query-result {
    flex-direction: column;
    align-items: flex-start;
    gap: 18px;
  }

}

</style>
