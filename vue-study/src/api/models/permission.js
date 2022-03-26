import axios from "../../axios";

export default {
    /*获取菜单列表*/
    getMenuList() {
        return axios.get('/permission/menus')
    },
    /*获取权限列表*/
    getRightsList() {
        return axios.get('/permission/rights/list')
    },
    getRightsTree() {
        return axios.get('/permission/rights/tree')
    }
}

