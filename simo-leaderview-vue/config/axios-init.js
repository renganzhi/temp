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
    return Promise.resolve(res.data)
  },
  error => {
    $('#screen').hide()
    if (error.response && error.response.config.url.includes('findAlertLevelList')) {
      return Promise.reject(error)
    }
    if (gbs.inDev) {
      Notification({
        message: '连接错误！',
        position: 'bottom-right',
        customClass: 'toast toast-error'
      })
      if (error.toString().includes('776')) {
        window.location.href = window.location.origin + '/#/login'
      }
    } else {
      if (error.toString().includes('776')) {
        window.location.href = window.location.origin + '/loginPage'
      }
      tooltip('', '连接错误！', 'error')
    }
    return Promise.reject(error)
  }
)

export default axios
