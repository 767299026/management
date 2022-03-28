import axios from "../../axios";

export default {
    /*获取用户列表*/
    getUerList(params) {
        return axios.get('/user/users',{ params: params })
    },
    /*根据Id获取用户信息*/
    getUserInfo(id) {
        return axios.get('/user/getUserInfo/'+id)
    },
    /*修改用户状态*/
    changeUserStatus(id) {
        return axios.put('/user/changeStatus/'+id)
    },
    /*添加用户*/
    addUser(params) {
        return axios.post('/user/add', params)
    },
    /*编辑用户*/
    editUser(params) {
        return axios.put('/user/edit', params)
    },
    /*删除用户*/
    deleteUser(id) {
        return axios.delete('/user/delete/' + id)
    },
    /*分配用户角色*/
    allotRole(params, roleId) {
        return axios.put('/user/allotRole', {userInfo: params, roleId})
    }
}