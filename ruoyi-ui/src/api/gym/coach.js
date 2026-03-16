import request from '@/utils/request'

// 查询教练信息列表
export function listCoach(query) {
  return request({
    url: '/gym/coach/list',
    method: 'get',
    params: query
  })
}

// 查询教练信息详细
export function getCoach(coachId) {
  return request({
    url: '/gym/coach/' + coachId,
    method: 'get'
  })
}

// 新增教练信息
export function addCoach(data) {
  return request({
    url: '/gym/coach',
    method: 'post',
    data: data
  })
}

// 修改教练信息
export function updateCoach(data) {
  return request({
    url: '/gym/coach',
    method: 'put',
    data: data
  })
}

// 删除教练信息
export function delCoach(coachId) {
  return request({
    url: '/gym/coach/' + coachId,
    method: 'delete'
  })
}
