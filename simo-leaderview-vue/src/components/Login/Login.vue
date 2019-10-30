<template>
  <div style="margin: 20px;">
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
      code: 0
    }
  },
  mounted: function () {
    this.baseUrl = gbs.host
  },
  methods: {
    login () {
      var _data = { 'username': 'admin', 'password': 'Admin123', 'checkCode': this.code }
      this.axios({
        method: 'post',
        url: '/login',
        data: qs.stringify(_data),
        headers: { 'content-type': 'application/x-www-form-urlencoded' }
      }).then((res) => {
        this.$router.push('/')
      })
    }
  }
}
</script>
