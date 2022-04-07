import Vue from 'vue'
import Router from 'vue-router'
// import HomePage from '@/views/HomePage/HomePage'
// import EditPage from '@/views/EditPage/EditPage'
// import Edit from '@/views/Edit/Edit.vue'
// import Login from '@/views/Login/Login.vue'

Vue.use(Router)

export default new Router({
  // mode: 'history',
  routes: [
    {
      path: '/',
      name: 'HomePage',
      component: (resolve) => require(['@/views/HomePage/HomePage'], resolve)
    },
    // {
    //   path: '/',
    //   name: 'pageShow',
    //   component: (resolve) => require(['@/views/pageShow/pageShow2'], resolve)
    // },
    {
      path: '/lookPage/:id',
      name: 'lookPage',
      component: (resolve) => require(['@/views/lookPage/lookPage'], resolve)
    },
    {
      path: '/editPage',
      name: 'EditPage',
      component: (resolve) => require(['@/views/EditPage/EditPage'], resolve)
    },
    {
      path: '/edit/:id',
      name: 'edit',
      component: (resolve) => require(['@/views/Edit/Edit.vue'], resolve)
    },
    {
      path: '/login',
      name: 'Login',
      component: (resolve) => require(['@/views/Login/Login.vue'], resolve)
    }
  ]
})
