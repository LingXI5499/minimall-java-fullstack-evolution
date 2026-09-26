import axios from 'axios'

const http = axios.create({ baseURL: '/api', timeout: 10000 })

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
    return Promise.reject(new Error(message))
  }
)

export default http
