import http from './http'

/** 读取后端真实分类，页面不会把分类 ID 写死在代码里。 */
export const categoryApi = { list: () => http.get('/categories') }
