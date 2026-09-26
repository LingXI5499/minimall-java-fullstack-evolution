import axios from 'axios'

const http = axios.create({ baseURL: '/api', timeout: 10000 })

http.interceptors.response.use(
  response => {
    const body = response.data
    if (body?.code !== response.status) throw new Error(body?.message || '响应格式错误')
    return { data: body.data }
  },
  error => {
    const message = error.response?.data?.message || error.message || '网络请求失败'
    return Promise.reject(new Error(message))
  }
)

export default http
