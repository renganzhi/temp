<template>
    <div>
        <div :style="wrapStyle" @click="showPop"></div>
        <!-- <div class="iframePop" v-if="showBox">
          <div :style="boxStyle">
            <span @click="showBox = false">x</span>
            <iframe :src="item.popUrl" frameborder="0"></iframe>
          </div>
        </div> -->
    </div>
</template>
<script>
export default {
  props: ['item'],
  data () {
    return {
      showBox: false
    }
  },
  computed: {
    wrapStyle () {
      let style = {
        width: this.item.width + 'px !important',
        height: this.item.height + 'px !important'
      }
      if (this.$route.name === 'edit' && !this.$parent.$parent.previewStatus) {
        style.backgroundColor = this.item.popUrl ? 'rgba(0, 255, 0, 0.3)' : 'rgba(255, 0, 0, 0.3)'
      }
      return style
    },
    boxStyle () {
      return {
        height: this.item.popHeight + 'px',
        width: this.item.popWidth + 'px',
        left: (8640 - this.item.popWidth) / 2 + 'px',
        top: (1620 - this.item.popHeight) / 2 + 'px'
      }
    }
  },
  methods: {
    showPop () {
      if (this.item.popUrl && (this.$route.name === 'HomePage' || this.$route.name === 'lookPage' || this.$route.name === 'popPage')) {
        if (this.item.popUrl.indexOf('am.glzt.com.cn') >= 0) {
          const formData = new FormData()
          formData.append('username', 'wh_admin')
          formData.append('password', 'd2gxMjM0NTZA')
          formData.append('mark', '2')
          formData.append('grant_type', 'password')
          formData.append('client_id', '024')
          formData.append('client_secret', 'secret')
          formData.append('redirect_uri', 'https://t-am.glzt.com.cn/login/redirect')
          formData.append('inside', '1')
          this.axios.post('https://pl-soul-h.glzt.com.cn/uaa-service/oauth/token', formData).then(res => {
            console.log('res', this.item.popUrl + res.access_token)
            let popData = {
              url: this.item.popUrl + res.access_token,
              width: this.item.popWidth,
              height: this.item.popHeight
            }
            // this.showBox = true
            this.$parent.$parent.ShowIframePop(popData)
          })
        } else {
          let popData = {
            url: this.item.popUrl,
            width: this.item.popWidth,
            height: this.item.popHeight
          }
          // this.showBox = true
          this.$parent.$parent.ShowIframePop(popData)
        }
      }
    }
  }
}
</script>
<style scoped lang="scss">
.iframePop{
  width: 8640px;
  height: 1620px;
  float: left;;
  position: fixed;
  top: 0;
  left: 0;
  background-color: #15192a65;
  z-index: 10000;
  >div{
    position: relative;
    span{
      position: absolute;
      top: 0;
      right: 0;
      color: white;
      font-size: 30px;
    }
    iframe{
      width: 100%;
      height: 100%;
    }
  }
}
</style>
