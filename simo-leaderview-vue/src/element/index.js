// 导入项目中需要的element-ui组件
import { Slider, Notification, Tooltip, Dialog } from 'element-ui'
const element = {
  install: function (Vue) {
    Vue.use(Slider)
    Vue.use(Notification)
    Vue.use(Tooltip)
    Vue.use(Dialog)
  }
}
export default element
