import request from '@/utils/request'

// 查询员工信息列表
export function listEmployees(query) {
  return request({
    url: '/employees/employees/list',
    method: 'get',
    params: query
  })
}

// 查询员工信息详细
export function getEmployees(id) {
  return request({
    url: '/employees/employees/' + id,
    method: 'get'
  })
}

// 新增员工信息
export function addEmployees(data) {
  return request({
    url: '/employees/employees',
    method: 'post',
    data: data
  })
}

// 修改员工信息
export function updateEmployees(data) {
  return request({
    url: '/employees/employees',
    method: 'put',
    data: data
  })
}

// 删除员工信息
export function delEmployees(id) {
  return request({
    url: '/employees/employees/' + id,
    method: 'delete'
  })
}
