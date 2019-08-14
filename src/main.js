// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
// import 'jquery'

import VueAxios from 'vue-axios'
import VCharts from 'v-charts'
import axios from './../config/axios-init'
import '#/icon/iconfont.css'

Vue.use(VueAxios, axios)
Vue.use(VCharts)
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
