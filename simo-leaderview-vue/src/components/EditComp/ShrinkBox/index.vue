<template>
      <div :style="wrapStyle" @click="ifShow = !ifShow">
        <p v-show="!item.bdCom.length">暂未配置</p>
      </div>
</template>

<script>
import { gbs } from '@/config/settings'
import element from '@/element'

export default {
  props: ['item'],
  data: function () {
    return {
      clock: '',
      baseUrl: '',
      ifShow: false
    }
  },
  computed: {
    wrapStyle () {
      let style = {
        width: this.item.width + 'px !important',
        height: this.item.height + 'px !important',
        opacity: this.$route.name === 'edit' && !this.$parent.$parent.previewStatus ? '1' : this.item.showTabs ? '1' : '0'
      }
      if (this.item.bdCom || this.item.bdCom === 0) {
        if (this.item.normalBack) {
          style.background = 'url(' + gbs.host + this.item.normalBack + ') 0% 0% / 100% 100% no-repeat'
        } else {
          style.background = 'rgba(255,255,0,0.4)'
        }
      } else {
        style.background = 'rgba(255,0,0,0.4)'
      }
      return style
    }
  },
  watch: {
    'ifShow': function (newV) {
      if (this.item.bdCom) {
        this.control()
      }
    },
    'item.bdCom': function (newV, oldV) {
      console.log('bdCom', newV, oldV)
      this.control()
    }
  },
  methods: {
    control () {
      let controlDOM = ''
      if (this.item.bdCom && document.getElementById(this.item.bdCom)) {
        controlDOM = document.getElementById(this.item.bdCom)
        controlDOM.classList.add('animate__animated')
        if (this.ifShow) {
          controlDOM.style.display = 'block'
          if (!controlDOM.classList.contains('animate__fadeInRight')) {
            controlDOM.classList.add('animate__fadeInRight')
          }
        } else {
          controlDOM.classList.remove('animate__fadeInRight')
          controlDOM.style.display = 'none'
        }
      }
    }
  },
  beforeMount () {
    var reg = /^\/api/
    if (!reg.test(this.item.imgSrc)) {
      this.baseUrl = gbs.host
      // this.baseUrl = gbs.host + '/leaderview'
    }
  },
  mounted () {
    if (this.item.bdCom || this.item.bdCom === 0) {
      this.control()
    }
  },
  beforeDestroy () {
    if (this.$route.name === 'edit') {
      if (document.getElementById(this.item.bdCom)) {
        document.getElementById(this.item.bdCom).classList.remove('animate__fadeOutLeft', 'animate__fadeInRight')
        document.getElementById(this.item.bdCom).style.display = 'block'
      }
    }
  }
}
</script>
<style lang="scss">
.tabbody>.ivu-tabs-bar{
  margin: 0 !important;
  height: 100%;
  // background: rgba(255, 0, 0, 0.3);
  border: none !important;
  .ivu-tabs-nav-container{
    height: 100%;
    margin: 0 !important;
    .ivu-tabs-nav-wrap{
      height: 100%;
      .ivu-tabs-nav-scroll{
        height: 100%;
        .ivu-tabs-nav{
          height: 100%;
          width: 100%;
          display: flex;
          .ivu-tabs-tab{
            flex: 1;
            display: flex;
            margin: 0 !important;
            padding: 0;
            // background-repeat: no-repeat;
            // background-size: 100% 100% !important;
            // background-position: center 0;
            div{
                flex: 1;
            }
          }
          .ivu-tabs-tab-focused{
          }
        }
      }
    }
  }
}
.ivu-tabs-ink-bar{
  display: none;
}
@keyframes right {
  from {
    opacity: 0.5;
    -webkit-transform: translate3d(60px, 0, 0);
    transform: translate3d(60px, 0, 0);
  }

  to {
    opacity: 1;
    -webkit-transform: translate3d(0, 0, 0);
    transform: translate3d(0, 0, 0);
  }
}
.animate__fadeInRight{
   -webkit-animation-name: right;
    animation-name: right;
}

</style>
