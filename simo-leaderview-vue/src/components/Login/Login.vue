<template>
  <div style="margin: 20px;">
    <input v-model="name"
           placeholder="请输入用户名" /><br>
    <input v-model="pwd"
           placeholder="请输入密码" /><br>
    <img :src="baseUrl + '/verification'" />
    <input v-model="code" /><button @click='login'>登录</button>
  </div>
</template>
<script>
import { gbs } from '@/config/settings'
import qs from 'qs'
export default {
  name: 'login',
  props: [],
  data () {
    return {
      baseUrl: '',
      code: 0,
      name: '',
      pwd: ''
    }
  },
  mounted: function () {
    this.baseUrl = gbs.host
  },
  methods: {
    login () {
      var _data = { 'username': this.name, 'password': this.pwd, 'checkCode': this.code }
      this.axios({
        method: 'post',
        url: '/login',
        data: qs.stringify(_data),
        headers: { 'content-type': 'application/x-www-form-urlencoded; charset=UTF-8' }
      }).then((res) => {
        if (res.success) {
          this.$router.push('/')
        }
      })
    }
  }
}
</script>
