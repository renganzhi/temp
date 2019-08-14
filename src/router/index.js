import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import HomePage from '@/components/HomePage/HomePage'
import EditPage from '@/components/EditPage/EditPage'
import Edit from '@/components/Edit/Edit.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'HomePage',
      component: HomePage
    },
    {
      path: '/home',
      name: 'Home',
      component: Home
    },
    {
      path: '/editPage',
      name: 'EditPage',
      component: EditPage
    },
    {
      path: '/edit',
      name: 'edit',
      component: Edit
    }
  ]
})
