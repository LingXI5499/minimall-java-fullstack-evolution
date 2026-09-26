<script setup>
import { ref } from 'vue'
import axios from 'axios'

const message = ref('')
const response = ref('')
const error = ref('')
async function send() {
  error.value = ''
  try { response.value = (await axios.post('/api/echo', { message: message.value })).data.message }
  catch (e) { error.value = e.message }
}
</script>

<template>
  <main>
    <h1>MiniMall · V0 请求链</h1>
    <form @submit.prevent="send"><label>输入消息 <input v-model="message" required /></label><button>发送到 Java</button></form>
    <p v-if="response">后端返回：{{ response }}</p><p v-if="error" role="alert">{{ error }}</p>
  </main>
</template>

<style>body{font-family:system-ui,sans-serif;margin:3rem;color:#243047}main{max-width:40rem;margin:auto}input,button{padding:.6rem;margin:.4rem}</style>
