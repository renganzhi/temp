import Vue from 'vue'
import Router from 'vue-router'
import HomePage from '@/components/HomePage/HomePage'
import EditPage from '@/components/EditPage/EditPage'
import Edit from '@/components/Edit/Edit.vue'
import Login from '@/components/Login/Login.vue'

Vue.use(Router)

export default new Router({
  // mode: 'history',
  routes: [
    {
      path: '/',
      name: 'HomePage',
      component: HomePage
    },
    {
      path: '/editPage',
      name: 'EditPage',
      component: EditPage
    },
    {
      path: '/edit/:id',
      name: 'edit',
      component: Edit
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    }
  ]
})
