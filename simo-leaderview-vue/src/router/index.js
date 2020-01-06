import Vue from 'vue'
import Router from 'vue-router'
// import HomePage from '@/components/HomePage/HomePage'
// import EditPage from '@/components/EditPage/EditPage'
// import Edit from '@/components/Edit/Edit.vue'
// import Login from '@/components/Login/Login.vue'

Vue.use(Router)

export default new Router({
  // mode: 'history',
  routes: [
    {
      path: '/',
      name: 'HomePage',
      component: (resolve) => require(['@/components/HomePage/HomePage'], resolve)
    },
    {
      path: '/editPage',
      name: 'EditPage',
      component: (resolve) => require(['@/components/EditPage/EditPage'], resolve)
    },
    {
      path: '/edit/:id',
      name: 'edit',
      component: (resolve) => require(['@/components/Edit/Edit.vue'], resolve)
    },
    {
      path: '/login',
      name: 'Login',
      component: (resolve) => require(['@/components/Login/Login.vue'], resolve)
    }
  ]
})
