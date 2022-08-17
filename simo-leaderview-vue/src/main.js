// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import ElementUI, { Notification } from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import Vue from 'vue'
// import ''
import JSMpeg from '../static/js/jsmpeg'

import usRules from './config/validate' // 表单验证规则

import ViewUI from 'view-design'
import 'view-design/dist/styles/iview.css'

import '../static/css/theme.scss'
import '../static/css/normal.scss'
import ensureModal from '../src/navBar/ensureModal' // 确认提示框
// import VueAxios from 'vue-axios'
// import axios from '../config/axios-init'
import './plugins/axios-init'
import './plugins/element'
// import VCharts from 'v-charts'
import '#/font/asn/icon.css'
import { gbs } from '@/config/settings'
import { VTooltip } from 'v-tooltip'
import App from './App.vue'
import router from './router'
import store from './vuex/store'
// Vue.use(VueAxios, axios)
// Vue.use(VCharts)
// Vue.use(ElementUI)
// https://github.com/Akryum/v-tooltip#usage
Vue.use(ViewUI)
Vue.directive('tooltip', VTooltip)
Vue.config.productionTip = false
Vue.prototype.$rules = usRules
Vue.prototype.$ensureModal = ensureModal

Vue.config.performance = true
Vue.prototype.$notify = Notification

Vue.prototype.bus = new Vue()
Vue.prototype.$EventBus = new Vue()
Vue.prototype.$MyEventBus = new Vue()
  /* eslint-disable no-new */
  ; (window).vm = new Vue({
    el: '#app',
    router,
    store,
    components: { App },
    template: '<App/>'
  })
