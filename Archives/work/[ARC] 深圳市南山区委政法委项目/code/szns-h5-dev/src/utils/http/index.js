import Axios from "axios";
import NProgress from "../progress";
import "vant/es/toast/style";
import {
  useUserStoreHook
} from "@/store/modules/user";

// 默认 axios 实例请求配置
const configDefault = {
  timeout: 0,
  baseURL: import.meta.env.VITE_BASE_API,
  data: {}
};

class Http {
  constructor(config) {
    Http.axiosConfigDefault = config;
    Http.axiosInstance = Axios.create(config);
    this.httpInterceptorsRequest();
    this.httpInterceptorsResponse();
  }
  // 当前实例
  static axiosInstance;
  // 请求配置
  static axiosConfigDefault;

  // 请求拦截
  httpInterceptorsRequest() {
    Http.axiosInstance.interceptors.request.use(
      config => {
        if (config.url.indexOf('hasNewEvent') < 0) {
          NProgress.start();
        }
        // 发送请求前，可在此携带 token
        const token = useUserStoreHook().token
        if (token) {
          config.headers['Authorization'] = token
        }
        return config;
      },
      error => {
        showFailToast(error.message);
        return Promise.reject(error);
      }
    );
  }

  // 响应拦截
  httpInterceptorsResponse() {
    Http.axiosInstance.interceptors.response.use(
      response => {
        closeToast()
        NProgress.done();
        // 刷新token
        if (response.headers['X-Refresh-Token']) {
          useUserStoreHook().refreshToken()
        }
        // 退出登录
        if (response.headers['X-Log-Out']) {
          useUserStoreHook().logOut()
        }
        // 与后端协定的返回字段
        const {
          code,
          msg,
          result
        } = response.data;
        // 判断请求是否成功 （code 200 请求成功）
        const isSuccess =
          Reflect.has(response.data, "code") && code === 0;
        if (isSuccess) {
          return response.data;
        } else {
          // 处理请求错误
          if (code === 401) {
            useUserStoreHook().logOut()
          }
            if (response.config.url.indexOf('hasNewEvent') < 0) {
              showFailToast(msg);
            }
          return Promise.reject(response.data);
        }
      },
      error => {
        closeToast()
        NProgress.done();
        // 处理 HTTP 网络错误
        let message = "";
        // HTTP 状态码
        const status = error.response ? error.response.status : '';
        console.log(error, 999)
        switch (status) {
          case 400:
            message = "请求错误";
            break;
          case 401:
            message = "未授权，请登录";
            if (response.headers['X-Log-Out']) {
              useUserStoreHook().logOut()
            }
            break;
          case 403:
            message = "拒绝访问";
            break;
          case 404:
            message = `请求地址出错: ${error.response?.config?.url}`;
            break;
          case 408:
            message = "请求超时";
            break;
          case 500:
            message = "服务器内部错误";
            break;
          case 501:
            message = "服务未实现";
            break;
          case 502:
            message = "网关错误";
            break;
          case 503:
            message = "服务不可用";
            break;
          case 504:
            message = "网关超时";
            break;
          case 505:
            message = "HTTP版本不受支持";
            break;
          default:
            message = "网络连接故障";
        }

        showFailToast(message);
        return Promise.reject(error);
      }
    );
  }

  // 通用请求函数
  request(paramConfig) {
    const config = {
      ...Http.axiosConfigDefault,
      ...paramConfig
    };
    return new Promise((resolve, reject) => {
      Http.axiosInstance
        .request(config)
        .then(response => {
          resolve(response);
        })
        .catch(error => {
          reject(error);
        });
    });
  }
}

export const http = new Http(configDefault);