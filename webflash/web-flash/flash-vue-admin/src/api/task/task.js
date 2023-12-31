import request from '@/utils/request'

export function getList(params) {
    return request({
        url: '/task/list',
        method: 'get',
        params
    })
}


export function save(params) {
    return request({
        url: '/task',
        method: 'post',
        params
    })
}

export function remove(id) {
    return request({
        url: '/task',
        method: 'delete',
        params: {
            id: id
        }
    })
}
