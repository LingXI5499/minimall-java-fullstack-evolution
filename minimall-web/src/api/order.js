import http from './http'

/** 订单页面只关心创建、列表和详情；HTTP 错误由 http.js 统一处理。 */
export const orderApi = {
  list: () => http.get('/orders'),
  detail: id => http.get(`/orders/${id}`),
  create: items => http.post('/orders', { items })
}
