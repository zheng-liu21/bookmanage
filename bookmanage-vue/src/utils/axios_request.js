// 配置axios
import axios from 'axios'
import store from  '@/store'
import router from "@/router";

// 创建实例
const instance = axios.create({
    baseURL: '/api', // 通过/api别名指定后端路由
    timeout: 5000,
    //设置请求头
    headers:{
        get: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
            // 在开发中，一般还需要单点登录或者其他功能的通用请求头，可以一并配置进来
        },
        post: {
            'Content-Type': 'application/json;charset=utf-8'
        }
    },
})
// 请求拦截器
instance.interceptors.request.use(config => {

    // 每次发送请求之前都检测vuex是否存有token,放在请求头发送给服务器
    // Authorization是根据后端自定义字段
    config.headers.Authorization = store.getters.getToken;
    return config;
}, error => {
    console.log("在request拦截器显示错误：", error.response)
    return Promise.reject(error);
});
// 响应拦截器
instance.interceptors.response.use(response => {
    // 在status正确的情况下，code不正确则返回对应的错误信息（后台自定义为200是正确，并且将错误信息写在message），正确则返回响应
    return response.data.code == 200 ? response : Promise.reject(response.data.message);
}, error => {
    // 在status不正确的情况下，判别status状态码给出对应响应
    if (error.response) {
        console.log("在respone拦截器显示错误：", error.response)
        switch (error.response.status) {
            case 401:
                //可能是token过期，清除它
                store.commit("delToken");

                router.replace({ //跳转到登录页面
                    path: '/login',
                    // 将跳转的路由path作为参数，登录成功后跳转到该路由
                    query: { redirect: router.currentRoute.fullPath }
                });
        }
    }
    return Promise.reject(error.response.data);
});
export default instance
