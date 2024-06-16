// 配置axios
import axios from 'axios'
import router from "@/router";

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建实例
const instance = axios.create({
    baseURL: '/api', // 通过/api别名指定后端路由
    timeout: 10000,
})
// 请求拦截器
instance.interceptors.request.use(config => {
    const tokenStr = window.sessionStorage.getItem("token");
    console.log("token="+tokenStr);
    if(tokenStr){
        config.headers['Authorization'] = 'Bearer '+tokenStr;
    }
    return config;
}, error => {
    Promise.reject(error);
});
// 响应拦截器
instance.interceptors.response.use(response => {
    return response.data;
}, error => {
    return Promise.reject(error);
});
export default instance
