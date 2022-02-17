<template>
  <div style="margin: 20px;">
    <input v-model="name" placeholder="请输入用户名" /><br />
    <input v-model="pwd" placeholder="请输入密码" /><br />
    <img :src="baseUrl + '/verification'" />
    <input v-model="code" @keyup.enter="login" /><button @click="login">
      登录
    </button>
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
      // pwd: 'iegJXNo9DJiNXndQJ+vgm3NQLLBrmPJCI88ZTHsuE+h8NMGYrzdxY2ki+LEcqYr5vD93mE+GwTYPxnuiari2He+PnKo5IFuSGikYwq+XDV2AEwaf2adH9RnTwtCG2ZXg+boBS8O+Mu1GQ33bnwXof823U2yIQqY4QD59o3ETRVA='
      pwd:
        'OetGyNLRbqQrGtMuvYxL613w+vqZBOz4b4KUfJjN8UgGvuBWqHiIwgDFdFjZtxH9yfxGwmAhE2NSmILVmgRcXc4qpRI4YTvCE/phh+UiSE/UhjmSJyZh3piPBlp42+ecWzrh+rkeHakM3NnqjLEu/nP8O6s0Um8NSgH84eVSPNE='
    }
  },
  mounted: function () {
    this.baseUrl = gbs.host
  },
  methods: {
    login () {
      var _data = {
        username: this.name,
        password: this.pwd,
        checkCode: this.code
      }
      this.axios({
        method: 'post',
        url: '/login',
        data: qs.stringify(_data),
        headers: {
          'content-type': 'application/x-www-form-urlencoded; charset=UTF-8'
        }
      }).then(res => {
        if (res.success) {
          this.$router.push('/')
        }
      })
    }
  }
}
</script>
