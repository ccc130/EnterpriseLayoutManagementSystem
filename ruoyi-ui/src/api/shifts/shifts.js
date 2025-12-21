import request from '@/utils/request'

// 查询班次情况列表
export function listShifts(query) {
  return request({
    url: '/shifts/shifts/list',
    method: 'get',
    params: query
  })
}

// 查询班次情况详细
export function getShifts(id) {
  return request({
    url: '/shifts/shifts/' + id,
    method: 'get'
  })
}

// 新增班次情况
export function addShifts(data) {
  return request({
    url: '/shifts/shifts',
    method: 'post',
    data: data
  })
}

// 修改班次情况
export function updateShifts(data) {
  return request({
    url: '/shifts/shifts',
    method: 'put',
    data: data
  })
}

// 删除班次情况
export function delShifts(id) {
  return request({
    url: '/shifts/shifts/' + id,
    method: 'delete'
  })
}
