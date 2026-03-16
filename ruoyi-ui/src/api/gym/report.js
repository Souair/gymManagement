import request from '@/utils/request'

// 获取首页统计数据
export function getDashboardData() {
  return request({
    url: '/gym/report/dashboard',
    method: 'get'
  })
}
