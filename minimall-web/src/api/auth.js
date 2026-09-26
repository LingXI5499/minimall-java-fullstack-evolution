import http from './http'

/** 登录接口公开；票据接口仍通过 http.js 自动携带管理员 JWT。 */
export const authApi = {
  login: credentials => http.post('/auth/login', credentials),
  wsTicket: () => http.post('/auth/ws-ticket')
}
