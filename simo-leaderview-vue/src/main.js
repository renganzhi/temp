// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import ElementUI, {Notification} from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import Vue from 'vue'
import App from './App'
import router from './router'
import store from './vuex/store'

import VueAxios from 'vue-axios'
import VCharts from 'v-charts'
import axios from './../config/axios-init'
import '#/icon/iconfont.css'
import '#/font/asn/icon.css'
import { gbs } from '@/config/settings'
import { VTooltip } from 'v-tooltip'
// import { Slider, Notification, Tooltip, Dialog } from 'element-ui'
Vue.use(VueAxios, axios)
Vue.use(VCharts)
Vue.use(ElementUI)
// https://github.com/Akryum/v-tooltip#usage
Vue.directive('tooltip', VTooltip)
Vue.config.productionTip = false
Vue.config.performance = true
if (gbs.inDev) {
  Vue.prototype.$notify = Notification
} else {
  Vue.prototype.$notify = function (obj) {
    tooltip(' ', obj.message, (/error/g).test(obj.customClass) ? 'error' : 'success')
  }
}

Vue.prototype.$EventBus = new Vue()
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
