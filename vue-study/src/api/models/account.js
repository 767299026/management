import axios from "../../axios";

export default {
    /*登录*/
    login(params) {
        return axios.post('/login',params)
    },

}