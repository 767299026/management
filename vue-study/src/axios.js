import axios from "axios";

let instance = axios.create({
    baseURL:"https://localhost:8484/vue",
    timeout:5000
})

/*前置拦截*/
instance.interceptors.request.use(config => {
    config.headers.Authorization = window.localStorage.getItem("token")
    return config
})

instance.interceptors.response.use()

export default instance