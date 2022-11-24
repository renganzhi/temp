<template>
      <div :style="wrapStyle" @click="ifShow = !ifShow">
        <p v-show="!item.bindCom.length">暂未配置</p>
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
      if (this.item.bindCom && this.item.bindCom.length) {
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
      if (this.item.bindCom.length) {
        this.control()
      }
    },
    'item.bindCom': function (newV, oldV) {
      this.control()
    }
  },
  methods: {
    control () {
      let controlDOM = ''
      if (this.item.bindCom.length) {
        if (this.$route.name === 'edit' && !this.$parent.$parent.previewStatus) {
          controlDOM = document.getElementById('p_compose' + this.item.bindCom[0])
        } else {
          controlDOM = document.getElementById('p_viewCompose' + this.item.bindCom[0])
        }
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
    if (this.item.bindCom.length) {
      this.control()
    }
  },
  beforeDestroy () {
    if (document.getElementById('p_compose' + this.item.bindCom[0])) {
      document.getElementById('p_compose' + this.item.bindCom[0]).classList.remove('animate__fadeOutLeft', 'animate__fadeInRight')
      document.getElementById('p_compose' + this.item.bindCom[0]).style.display = 'block'
    }
    if (document.getElementById('p_viewCompose' + this.item.bindCom[0])) {
      document.getElementById('p_viewCompose' + this.item.bindCom[0]).classList.remove('animate__fadeOutLeft', 'animate__fadeInRight')
      document.getElementById('p_viewCompose' + this.item.bindCom[0]).style.display = 'block'
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
