import http from './http'

/** 商品接口集中在这里，页面不再自己拼每个请求地址。 */
export const productApi = {
  list: params => http.get('/products', { params }),
  getById: id => http.get(`/products/${id}`),
  create: data => http.post('/products', data),
  update: (id, data) => http.put(`/products/${id}`, data),
  remove: id => http.delete(`/products/${id}`)
}
