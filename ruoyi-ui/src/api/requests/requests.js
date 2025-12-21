import request from '@/utils/request'

// 查询替换申请列表
export function listRequests(query) {
  return request({
    url: '/requests/requests/list',
    method: 'get',
    params: query
  })
}

// 查询替换申请详细
export function getRequests(id) {
  return request({
    url: '/requests/requests/' + id,
    method: 'get'
  })
}

// 新增替换申请
export function addRequests(data) {
  return request({
    url: '/requests/requests',
    method: 'post',
    data: data
  })
}

// 修改替换申请
export function updateRequests(data) {
  return request({
    url: '/requests/requests',
    method: 'put',
    data: data
  })
}

// 删除替换申请
export function delRequests(id) {
  return request({
    url: '/requests/requests/' + id,
    method: 'delete'
  })
}
