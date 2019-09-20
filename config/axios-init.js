import axios from 'axios'
import { Notification } from 'element-ui'
import { gbs } from '@/config/settings'
axios.defaults.baseURL = gbs.host // 配置axios请求的地址
axios.defaults.headers.post['Content-Type'] = 'application/json; charset=utf-8'
axios.defaults.crossDomain = true
axios.defaults.withCredentials = true // 设置cross跨域 并设置访问权限 允许跨域携带cookie信息

// 配置发送请求前的拦截器 可以设置token信息
axios.interceptors.request.use(
  config => {
    // 这里配置全局loading
    if (!(/\.json/.test(config.url))) {
      $('#screen').show()
    }

    return config
    // return Promise.resolve(config)
  },
  error => {
    return Promise.reject(error)
  }
)

axios.interceptors.response.use(
  res => {
    // loading结束
    $('#screen').hide()
    if (res.status == 776) {
      window.location.href = window.location.origin + '/loginPage'
    }
    return Promise.resolve(res.data)
  },
  error => {
    $('#screen').hide()
    Notification({
      message: '连接错误！',
      position: 'bottom-right',
      customClass: 'toast toast-error'
    })
    return Promise.reject(error)
  }
)

export default axios
