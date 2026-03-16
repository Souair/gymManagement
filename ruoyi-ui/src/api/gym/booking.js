import request from '@/utils/request'

// 查询私教预约列表
export function listBooking(query) {
  return request({
    url: '/gym/booking/list',
    method: 'get',
    params: query
  })
}

// 查询私教预约详细
export function getBooking(id) {
  return request({
    url: '/gym/booking/' + id,
    method: 'get'
  })
}

// 智能推荐教练
export function recommendCoach(query) {
  return request({
    // 👇 重点：必须加 /action/ 或者其他不一样的词
    url: '/gym/booking/action/recommend',
    method: 'get',
    params: query
  })
}

// 新增私教预约
export function addBooking(data) {
  return request({
    url: '/gym/booking',
    method: 'post',
    data: data
  })
}

// 修改私教预约
export function updateBooking(data) {
  return request({
    url: '/gym/booking',
    method: 'put',
    data: data
  })
}

// 删除私教预约
export function delBooking(id) {
  return request({
    url: '/gym/booking/' + id,
    method: 'delete'
  })
}
