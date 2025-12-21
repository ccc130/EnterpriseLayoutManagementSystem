import request from '@/utils/request'

// 查询通知公告列表
export function listLayoutNotice(query) {
  return request({
    url: '/notice/notice/list',
    method: 'get',
    params: query
  })
}

// 查询通知公告详细
export function getLayoutNotice(noticeId) {
  return request({
    url: '/notice/notice/' + noticeId,
    method: 'get'
  })
}

// 新增通知公告
export function addLayoutNotice(data) {
  return request({
    url: '/notice/notice',
    method: 'post',
    data: data
  })
}

// 修改通知公告
export function updateLayoutNotice(data) {
  return request({
    url: '/notice/notice',
    method: 'put',
    data: data
  })
}

// 删除通知公告
export function delLayoutNotice(noticeId) {
  return request({
    url: '/notice/notice/' + noticeId,
    method: 'delete'
  })
}
