import axios from "../../axios";

export default {
    /*获取分类列表*/
    Categories() {
        return axios.get('/category/cate')
    },
    /*获取分类列表*/
    getCategories(params) {
        return axios.get('/category/cate', {params: params})
    },
    /*获取父分类列表*/
    getParentCategories(params) {
        return axios.get('/category/cate', {params: {type: params}})
    },
    /*获取分类信息*/
    getCategoryInfo(id) {
        return axios.get('/category/cate/' + id)
    },
    /*添加分类*/
    addGoodsCategory(params) {
        return axios.post('/category/add', params)
    },
    /*编辑分类*/
    editGoodsCategory(params) {
        return axios.put('/category/edit', params)
    },
    /*删除分类*/
    deleteGoodsCategory(id) {
        return axios.delete('/category/delete/' + id)
    },
}