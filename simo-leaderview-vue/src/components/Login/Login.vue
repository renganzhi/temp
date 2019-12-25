<template>
  <div style="margin: 20px;">
    <input v-model="name"
           placeholder="请输入用户名" /><br>
    <input v-model="pwd"
           placeholder="请输入密码" /><br>
    <img :src="baseUrl + '/verification'" />
    <input v-model="code" @keyup.enter="login"/><button @click='login'>登录</button>
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
      code: '',
      name: 'admin',
      pwd: 'PvdD05Lzi7hl2I5Foizn16eIPpTdXfkx85bCrFZ+Ry3bBK0f5lqYI12T05VNR1IlbR7AiQ1X/6465hJk2p+Dz9a5h0451tUGhA/ZIEd7pyAmljstut+ymKBduMuoryw2fja/7/DV3b22L6lUJMloYi12mG122vDy4kFfgLAVgQ0='
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
