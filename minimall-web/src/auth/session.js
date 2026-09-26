import { reactive } from 'vue'

// 教学项目使用 sessionStorage，刷新页面后仍能继续访问；关闭浏览器会清除会话。
let stored = null
try { stored = JSON.parse(sessionStorage.getItem('minimall-session') || 'null') } catch { stored = null }
export const session = reactive({ token: stored?.token || '', username: stored?.username || '', role: stored?.role || '' })

export function saveSession(login) {
  session.token = login.token
  session.username = login.username
  session.role = login.role
  sessionStorage.setItem('minimall-session', JSON.stringify(login))
}

export function clearSession() {
  session.token = ''
  session.username = ''
  session.role = ''
  sessionStorage.removeItem('minimall-session')
}
