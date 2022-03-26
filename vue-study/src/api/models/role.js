import axios from "../../axios";

export default {

    /*获取角色列表*/
    getRoles() {
        return axios.get("/role/roles")
    },
    /*获取角色名列表*/
    getRolesName() {
        return axios.get("/role/rolesName")
    },
    /*根据Id获取用户信息*/
    getRoleInfo(id) {
        return axios.get('/role/getRoleInfo/' + id)
    },
    /*添加角色*/
    addRoles(params) {
        return axios.post("/role/add", params)
    },
    /*编辑角色*/
    editRoles(params) {
        return axios.put('/role/edit', params)
    },
    /*删除角色*/
    deleteRoles(id) {
        return axios.delete('/role/delete/' + id)
    },
    /*分配权限*/
    allotRights(id, params) {
        return axios.put('/role/allot/' + id, {rights: params})
    }
}