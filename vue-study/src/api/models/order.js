import axios from "../../axios";

export default {
    /*获取订单列表*/
    getOrderList(params) {
        return axios.get('/orders/orders', { params: params })
    },

}