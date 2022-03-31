import axios from "../../axios";

export default {
    /*获取商品列表*/
    getGoods(params) {
        return axios.get('/goods/goods',{ params: params })
    },
    getGoodsInfo(id) {
        return axios.get('/goods/goods/'+id)
    },
    /*添加商品*/
    addGoods(params) {
        return axios.post('/goods/add',params)
    },
    /*编辑商品*/
    editGoods(params) {
        return axios.put('/goods/edit',params)
    },
    /*删除商品*/
    deleteGoods(id) {
        return axios.delete('/goods/delete/'+id)
    }
}