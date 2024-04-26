import axios from "axios";
import router from "@/router";

const request = axios.create({
    baseURL: process.env.VUE_APP_BASEURL, //后端的接口地址
    timeout: 30000
})

//request 拦截器
//可以自请求发送前对请求做一些处理

request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let user = JSON.parse(localStorage.getItem("honey-user") || '{}')
    config.headers['token'] = user.token //设置请求头
    return config
}, error =>{
    console.error('request error: ' + error) //for debug
    return Promise.reject(error)
    }
);

//response 拦截器
//可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;

        //兼容服务器返回的字符串数据
        if(typeof res === 'string'){
            res = res ? JSON.parse(res) : res
        }
        // //兼容服务器返回的字符串数据
        // if (typeof res === 'string' && (res.startsWith('{') || res.startsWith('[')) ) {
        //     try {
        //         res = JSON.parse(res);
        //     } catch (error) {
        //         console.error('Error parsing JSON data:', error);
        //     }
        // }
        if(res.code === '401'){
            router.push('/login')
        }
        return res;
    },
    error => {
        console.error('response error: ' + error) //for debug
        return Promise.reject(error)
    }
)

export default request