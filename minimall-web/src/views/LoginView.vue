<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { authApi } from '../api/auth'
import { saveSession } from '../auth/session'

const router = useRouter()
const form = reactive({ username: '', password: '' })
const error = ref('')
const loading = ref(false)
async function login() {
  loading.value = true
  error.value = ''
  try {
    const response = await authApi.login(form)
    saveSession(response.data)
    await router.replace('/products')
  } catch (e) { error.value = e.message }
  finally { loading.value = false }
}
</script>

<template>
  <main><h1>登录 MiniMall</h1><p>账号由服务端环境变量配置。</p>
    <form @submit.prevent="login">
      <label>用户名 <input v-model="form.username" autocomplete="username" required /></label>
      <label>密码 <input v-model="form.password" type="password" autocomplete="current-password" required /></label>
      <button :disabled="loading">{{ loading ? '登录中…' : '登录' }}</button>
    </form><p v-if="error" role="alert">{{ error }}</p>
  </main>
</template>

<style scoped>
main{max-width:420px;margin:4rem auto;padding:2rem;background:white;border-radius:12px}form,label{display:grid;gap:.6rem}form{gap:1rem}input,button{padding:.7rem}button{background:#2563eb;color:white;border:0;border-radius:6px}p[role=alert]{color:#b42318}
</style>
