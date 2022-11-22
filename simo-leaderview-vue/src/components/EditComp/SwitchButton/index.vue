<template>
      <div  ref="Tabs">
        <div v-if="item.bindCom.length === 0">
          暂未配置数据
        </div>
        <Tabs v-else v-model="item.showCom" class="tabbody" :style="wrapStyle">
        <TabPane v-for="(value,index) in item.bindCom" :label="setTabPaneLabel(value)"  :key="index" :name="value"></TabPane>
      </Tabs>
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
      baseUrl: ''
    }
  },
  computed: {
    wrapStyle () {
      let style = {
        width: this.item.width - 10 + 'px !important',
        height: this.item.height - 10 + 'px !important',
        transform: 'rotate(' + this.item.rotate + 'deg)'
      }
      if (this.$route.name === 'edit' && !this.$parent.$parent.previewStatus) {
        style.opacity = '1'
      } else {
        style.opacity = this.item.showTabs ? '1' : '0'
      }
      return style
    },
    setTabPaneLabel () {
      return (value) => {
        return (h) => {
          if (this.item.showCom === value) {
            return h('div', {
              style: {
                background: this.item.checkedBack ? 'url(' + gbs.host + this.item.checkedBack + ') 0% 0% / 100% 100% no-repeat' : this.item.focusColor,
                width: '100%',
                height: '100%',
                marginRight: this.item.marginRight + 'px !important',
                borderRadius: this.item.borderRadius + 'px !important',
                border: '1px solid ' + this.item.focusBorderColor
              }
            })
          } else {
            return h('div', {
              style: {
                background: this.item.normalBack ? 'url(' + gbs.host + this.item.normalBack + ') 0% 0% / 100% 100% no-repeat' : this.item.normalColor,
                width: '100%',
                height: '100%',
                marginRight: this.item.marginRight + 'px !important',
                borderRadius: this.item.borderRadius + 'px !important',
                border: '1px solid ' + this.item.normalBorderColor
              }
            })
          }
        }
      }
    }
  },
  watch: {
    'item.showCom': function (newV) {
      if (newV) {
        this.control()
      }
    },
    'item.bindCom': function (newV, oldV) {
      if (newV.length) {
        this.item.showCom = newV[0]
      }
      if (newV.length < oldV.length) {
        oldV.forEach(v => {
          if (newV.indexOf(v) === -1) {
            document.getElementById('p_compose' + v).classList.remove('animate__fadeOutLeft', 'animate__fadeInRight')
            document.getElementById('p_compose' + v).style.display = 'block'
          }
        })
      }
    },
    'item.ifCarousel': function (newV, oldV) {
      if (newV) {
        this.loopTab()
      } else {
        clearInterval(this.clock)
      }
    },
    'item.speed': function () {
      clearInterval(this.clock)
      if (this.item.ifCarousel) {
        this.loopTab()
      }
    }
  },
  methods: {
    loopTab () {
      if (this.$route.name === 'HomePage' || this.$route.name === 'lookPage' || this.$route.name === 'popPage') {
        this.clock = setInterval(() => {
          if (this.item.bindCom.length) {
            let next = 0 // 下一个选项的index
            this.item.bindCom.forEach((element, index) => {
              if (element === this.item.showCom) {
                next = (index + 1) % this.item.bindCom.length
              }
            })
            this.item.showCom = this.item.bindCom[next]
          } else {
            clearInterval(this.clock)
          }
        }, this.item.speed)
      }
    },
    control () {
      let controlDOM = ''
      this.item.bindCom.forEach(element => {
        if (this.$route.name === 'edit' && !this.$parent.$parent.previewStatus) {
          controlDOM = document.getElementById('p_compose' + element)
        } else {
          controlDOM = document.getElementById('p_viewCompose' + element)
        }
        controlDOM.classList.add('animate__animated')
        if (element === this.item.showCom) {
          controlDOM.style.display = 'block'
          if (!controlDOM.classList.contains('animate__fadeInRight')) {
            controlDOM.classList.add('animate__fadeInRight')
          }
        } else {
          controlDOM.classList.remove('animate__fadeInRight')
          controlDOM.style.display = 'none'
        }
      })
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
      this.item.showCom = this.item.bindCom[0]
      this.control()
    }
    if (this.item.ifCarousel) {
      clearInterval(this.clock)
      this.loopTab()
    } else {
      clearInterval(this.clock)
    }
  },
  beforeDestroy () {
    clearInterval(this.clock)
    this.item.bindCom.forEach(element => {
      try {
        document.getElementById('p_compose' + element).classList.remove('animate__fadeOutLeft', 'animate__fadeInRight')
        document.getElementById('p_compose' + element).style.display = 'block'
      } catch (err) {

      }
    })
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
