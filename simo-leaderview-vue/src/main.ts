// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
<<<<<<< HEAD:simo-leaderview-vue/src/main.js
import ElementUI, {Notification} from 'element-ui'
=======
import ElementUI, { Notification } from 'element-ui'
>>>>>>> UXDV-2.0-leaderview_3.0.4.3_98855:simo-leaderview-vue/src/main.ts
import 'element-ui/lib/theme-chalk/index.css'

import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './vuex/store'

// import VueAxios from 'vue-axios'
// import axios from '../config/axios-init'
import './plugins/axios-init'
import './plugins/element'
// import VCharts from 'v-charts'
import '#/icon/iconfont.css'
import '#/font/asn/icon.css'
import { gbs } from '@/config/settings'
import { VTooltip } from 'v-tooltip'

// Vue.use(VueAxios, axios)
// Vue.use(VCharts)
// Vue.use(ElementUI)
// https://github.com/Akryum/v-tooltip#usage
Vue.directive('tooltip', VTooltip)
Vue.config.productionTip = false
Vue.config.performance = true
if (gbs.inDev) {
  Vue.prototype.$notify = Notification
} else {
  Vue.prototype.$notify = function (obj) {

    // @ts-ignore
    tooltip(' ', obj.message, (/error/g).test(obj.customClass) ? 'error' : 'success')
  }
}

Vue.prototype.$EventBus = new Vue()
/* eslint-disable no-new */
;(window as any).vm = new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
