import axios from "axios";

let instance = axios.create({
    baseURL:"https://localhost:8484/vue",
    timeout:5000
})

instance.interceptors.request.use()

instance.interceptors.response.use()

export default instance