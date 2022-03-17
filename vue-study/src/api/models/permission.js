import axios from "../../axios";

export default {
    /*获取菜单列表*/
    getMenuList() {
        return axios.get('/permission/menus')
    }
}

