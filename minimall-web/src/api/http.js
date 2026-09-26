import axios from 'axios'
import { clearSession, session } from '../auth/session'

const http = axios.create({ baseURL: '/api', timeout: 10000 })

// JWT 在 HTTP 请求头中传递；不要把它放进 URL 或日志。
http.interceptors.request.use(config => {
  if (session.token) config.headers.Authorization = `Bearer ${session.token}`
  return config
})

// 后端统一返回 {code,message,data}；页面只需要真正的 data。
http.interceptors.response.use(
  response => {
    const body = response.data
    if (body?.code !== response.status) throw new Error(body?.message || '响应格式错误')
    return { data: body.data }
  },
  error => {
    // 参数错误等非 2xx 响应也带有后端 message，直接展示给用户。
    const message = error.response?.data?.message || error.message || '网络请求失败'
    if (error.response?.status === 401 && !error.config?.url?.endsWith('/auth/login')) {
      clearSession()
      location.assign('/login')
    }
    return Promise.reject(new Error(message))
  }
)

export default http
