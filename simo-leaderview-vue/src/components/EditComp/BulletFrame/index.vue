<template>
  <div>
      <div v-if="!(value)" @click="value = true" type="primary" class="openBtn"
      :style="openBoxStyle"></div>
      <div class="hiddenbox" v-if="value" :style="hiddenboxStyle">
        <div class="v-charts-data-empty"
            v-if="!item.imgSrc">请上传图片</div>
        <img :src="baseUrl + item.imgSrc"
            v-else
            :style="imgSctyle"/>

        <img :src="baseUrl + PlayimgSrc"
            v-if="item.isPlay"
            class="playImg"
            @click="toLinkPage"
            :style="imgPlaySctyle" />

        <video v-if="item.videoSrc"
              ref="videoItem"
              :loop="item.loop"
              :autoplay="item.autoplay"
              @error="playError"
              controls
              controlsList='nodownload'
              style="outline: none !important;"
              :style="videoSctyle"
              :src="item.videoSrc">
          您的浏览器不支持 video 标签。
        </video>

        <div  @click="value = false" type="primary" class="openBtn"
        :style="closeBoxStyle"></div>
        </div>
  </div>
</template>
<script>
import { gbs } from '@/config/settings'
export default {
  name: 'BulletFrame',
  props: ['item'],
  data () {
    return {
      value: false,
      baseUrl: '',
      Interval: '',
      timer: '',
      PlayimgSrc: '',
      activeIndex: 0,
      openBoxStyle: {}
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
    this.activeIndex = 0
    this.updateImg()
    // 多图片轮播
    this.timer && clearTimeout(this.timer)
    if (this.item.isPlay && this.item.PlayByClick) {
      this.timer = setInterval(() => {
        let activeIndex = this.activeIndex - 1
        if (activeIndex < 0) {
          activeIndex = this.item.srcList.length - 1
        }
        this.activeIndex = activeIndex
        this.updateImg()
        if (this.activeIndex == 0 && !this.item.loop) {
          this.timer = clearInterval(this.timer)
        }
      }, this.item.PlayTime * 1000 || 2000)
    }
  },
  watch: {
    'item.stationPlace' (val) {
      this.computed(val)
    },
    'item.srcList' (val) {
      this.updateImg()
      console.log(val)
    },
    'item.PlayByClick' (val) {
      this.timer && clearTimeout(this.timer)
      if (val) {
        console.log(this.item.PlayTime * 1000)
        this.timer = setInterval(() => {
          let activeIndex = this.activeIndex - 1
          if (activeIndex < 0) {
            activeIndex = this.item.srcList.length - 1
          }
          this.activeIndex = activeIndex
          this.updateImg()
          if (this.activeIndex == 0 && !this.item.loop) {
            this.timer = clearInterval(this.timer)
          }
        }, this.item.PlayTime * 1000 || 2000)
      }
    },
    'item.PlayTime' (val) {
      this.timer && clearTimeout(this.timer)
      if (this.item.PlayByClick) {
        this.timer = setInterval(() => {
          let activeIndex = this.activeIndex - 1
          if (activeIndex < 0) {
            activeIndex = this.item.srcList.length - 1
          }
          this.activeIndex = activeIndex
          this.updateImg()
          if (this.activeIndex == 0 && !this.item.loop) {
            this.timer = clearInterval(this.timer)
          }
        }, val * 1000 || 2000)
      }
    }
  },
  created () {
    this.computed(this.item.stationPlace)
  },
  computed: {
    hiddenboxStyle: function () {
      return {
        width: this.item.openBoxwidth + 'px',
        height: this.item.openBoxheight + 'px',
        left: this.item.openBoxRight + 'px',
        top: this.item.openBoxBtn + 'px'
      }
    },
    closeBoxStyle: function () {
      return {
        backgroundColor: this.item.bgClr + '',
        width: this.item.closeBoxwidth + 'px',
        height: this.item.closeBoxheight + 'px',
        right: this.item.closeBoxRight + 'px',
        top: this.item.closeBoxBtn + 'px'
      }
    },
    imgSctyle: function () {
      if (this.item.showType && this.item.showType === '1') {
        return {
          maxWidth: '100%',
          maxHeight: '100%',
          cursor: this.item.linkId ? 'pointer' : ''
        }
      }
      return {
        width: '100%',
        height: '100%',
        cursor: this.item.linkId ? 'pointer' : ''
      }
    },
    imgPlaySctyle: function () {
      return {
        width: this.item.PlayBoxwidth + 'px',
        height: this.item.Playheight + 'px',
        right: this.item.PlayBoxRight + 'px',
        bottom: this.item.PlayBoxBtn + 'px'
      }
    },
    videoSctyle: function () {
      return {
        width: this.item.videowidth + 'px',
        height: this.item.videoheight + 'px',
        left: this.item.videoBoxRight + 'px',
        top: this.item.videoBoxBtn + 'px'
      }
    }
  },
  methods: {
    computed (val) {
      if (val === '2') {
        this.stopcomputed()
        this.Interval = setInterval((val) => {
          if (document.querySelector('.moveBoximg')) {
            let option = window.bacPostion.toFixed(2)
            let lefthchajusize = this.item.x - document.querySelector('.moveBoximg').parentNode.parentNode.style.left.split('px')[0] * 1 + document.querySelector('.moveBoximg').style.left.split('px')[0] * 1
            let tophchajusize = this.item.y - document.querySelector('.moveBoximg').parentNode.parentNode.style.top.split('px')[0] * 1 + document.querySelector('.moveBoximg').style.top.split('px')[0] * 1
            let leftsize = lefthchajusize * option - lefthchajusize + document.querySelector('.moveBoximg').style.left.split('px')[0] * 1 / option
            let topsize = tophchajusize * option - tophchajusize + document.querySelector('.moveBoximg').style.top.split('px')[0] * 1 / option
            this.openBoxStyle = {
              backgroundColor: this.item.bgClr + '',
              width: (this.item.width * option).toFixed(2) + 'px',
              height: (this.item.height * option).toFixed(2) + 'px',
              top: topsize.toFixed(2) + 'px',
              left: leftsize.toFixed(2) + 'px'
            }
          } else {
            this.openBoxStyle = {
              backgroundColor: this.item.bgClr + '',
              width: this.item.width + 'px',
              height: this.item.height + 'px'
            }
          }
        }, 100)
      } else {
        this.stopcomputed()
        this.Interval = setInterval((val) => {
          this.openBoxStyle = {
            backgroundColor: this.item.bgClr + '',
            width: this.item.width + 'px',
            height: this.item.height + 'px'
          }
        }, 100)
      }
    },
    toLinkPage () {
      if (this.item.isPlay && !this.item.PlayByClick) {
        let activeIndex = this.activeIndex - 1
        if (activeIndex < 0) {
          activeIndex = this.item.srcList.length - 1
        }
        this.activeIndex = activeIndex
        this.updateImg()
      }
    },
    updateImg () {
      this.PlayimgSrc = this.item.srcList[this.activeIndex || 0] ? this.item.srcList[this.activeIndex || 0].src : ''
    },
    stopcomputed () {
      this.Interval && clearTimeout(this.Interval)
    },
    playError (e) {
      if (this.item.videoSrc) {
        this.canNotPlay = true
        this.$emit('palyErr')
      }
    }
  },
  beforeDestroy () {
    this.timer && clearTimeout(this.timer)
    this.Interval && clearTimeout(this.Interval)
  }
}
</script>
<style lang="scss" scoped>
.openBtn{
  position: absolute;
  cursor: pointer;
}
.playImg{
  position: absolute;
}
video{
  position: absolute;
}
.hiddenbox{
  position: absolute;
  background-color: transparent;
}
</style>
