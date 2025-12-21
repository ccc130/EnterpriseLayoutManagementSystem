import request from '@/utils/request'

// 查询排班情况列表
export function listScheduling(query) {
  return request({
    url: '/scheduling/scheduling/list',
    method: 'get',
    params: query
  })
}

// 查询排班情况详细
export function getScheduling(id) {
  return request({
    url: '/scheduling/scheduling/' + id,
    method: 'get'
  })
}

// 新增排班情况
export function addScheduling(data) {
  return request({
    url: '/scheduling/scheduling',
    method: 'post',
    data: data
  })
}

// 修改排班情况
export function updateScheduling(data) {
  return request({
    url: '/scheduling/scheduling',
    method: 'put',
    data: data
  })
}

// 删除排班情况
export function delScheduling(id) {
  return request({
    url: '/scheduling/scheduling/' + id,
    method: 'delete'
  })
}
