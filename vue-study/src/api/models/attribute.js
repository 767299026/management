import axios from "../../axios";

export default {

    /*获取分类参数列表*/
    getCategoryAttributes(id, selected) {
        return axios.get('/attribute/' + id, {params: {selected: selected}})
    },
    /*获取参数信息*/
    getAttributes(id) {
        return axios.get('/attribute/attributeInfo/' + id)
    },
    /*添加参数*/
    addAttributes(params) {
        return axios.post('/attribute/add', params)
    },
    /*编辑参数*/
    editAttributes(params) {
        return axios.put('attribute/edit', params)
    },
    /*编辑参数的Tag*/
    editAttributesTag(params) {
        return axios.put('attribute/editTag', params)
    },
    /*删除参数*/
    deleteAttributes(id) {
        return axios.delete('attribute/delete/' + id)
    },

}