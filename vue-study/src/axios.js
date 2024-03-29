import axios from "axios";
import router from "../src/router";
import {Message} from "element-ui";

let instance = axios.create({
    baseURL: "https://localhost:8484/electricity",
    timeout: 5000
})

/*前置拦截*/
instance.interceptors.request.use(config => {
    const token = window.localStorage.getItem("token")
    if (token != null)
        config.headers.Authorization = token
    return config
})

instance.interceptors.response.use(response => {
        const res = response.data
        if (res.code === 200 && res.success === true) {
            return response
        } else {
            // 弹窗异常信息
            Message.error(res.msg)
            // 直接拒绝往下面返回结果信息
            return Promise.reject(res.msg)
        }
    },
    error => {
        Message.error(error)// for debug
        if (error.response.data) {
            error.message = error.response.data.msg
        }
        // 根据请求状态觉得是否登录或者提示其他
        if (error.response.status === 401||error.response.status === 500) {
            window.localStorage.removeItem("token")
            window.localStorage.removeItem("userInfo")
            router.push({
                path: '/login'
            });
            error.message = '请重新登录';
        }
        if (error.response.status === 403) {
            error.message = '权限不足，无法访问';
        }
        Element.Message({
            message: error.message,
            type: 'error',
            duration: 3 * 1000
        })
        return Promise.reject(error)
}
)

export default instance