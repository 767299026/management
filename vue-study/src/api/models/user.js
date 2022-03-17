import axios from "../../axios";

export default {
    /*获取菜单列表*/
    getUerList() {
        return axios.get('/user/users')
    }
}