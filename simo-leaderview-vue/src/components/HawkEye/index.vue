<template>
<div class="back" id="back" :style='backStyle'>
    <div class="closeHead">
        鹰眼 {{ Math.floor(scale) }}%
        <button class="close" @click="closeBox">x</button>
    </div>
    <div class="centerBox">
        <div class="mycanvas">

        </div>
        <div id="HawkEye">
        </div>
    </div>
</div>
</template>
<script>
import { gbs } from '@/config/settings'
export default {
  name: 'HawkEye',
  props: ['scale', 'bgTop', 'bgLeft', 'boxTop', 'boxLeft'],
  data () {
    return {
      newHeight: 0,
      params: {
        zoomVal: 1,
        left: 0,
        top: 0,
        currentX: 0,
        oldLeft: 0,
        oldTop: 0,
        currentY: 0,
        flag: false,
        flagHawkEye: false
      }
    }
  },
  computed: {
    backStyle: function () {
      return {
        top: this.boxTop + 'px',
        left: this.boxLeft + 'px'
      }
    }
  },
  watch: {
    scale: function (newV) {
      this.getNewStyle(newV)
    }
  },
  methods: {
    closeBox () {
      this.$parent.closeHawkEye()
    },
    getNewStyle (newV) {
      this.params.zoomVal = 100 / newV
      let newWidth = 180 * this.params.zoomVal * document.querySelector('#centerMapBox').clientWidth / document.querySelector('.paint-bg').clientWidth
      let newHeight = this.newHeight * this.params.zoomVal * document.querySelector('#centerMapBox').clientHeight / document.querySelector('.paint-bg').clientHeight
      var o = document.getElementById('HawkEye')
      let MaxWidth = document.querySelector('.mycanvas').clientWidth - document.querySelector('#HawkEye').offsetLeft + 10
      if (newWidth > MaxWidth && MaxWidth !== 0) {
        newWidth = MaxWidth
      }
      let MaxHeight = document.querySelector('.mycanvas').clientHeight - document.querySelector('#HawkEye').offsetTop + 10
      if (newHeight > MaxHeight && MaxHeight !== 0) {
        newHeight = MaxHeight
      }
      o.style.width = (newWidth) + 'px'
      o.style.height = (newHeight) + 'px'
    },
    getCss (o, key) {
      return o.currentStyle
        ? o.currentStyle[key]
        : document.defaultView.getComputedStyle(o, false)[key]
    },
    bbimg (d) {
      this.params.zoomVal += (d.wheelDelta / 1200).toFixed(2) * -1
      if (this.params.zoomVal >= 0.5) {
        if (this.params.zoomVal > 5) {
          this.params.zoomVal = 5
        }
        this.$parent.changePaintStyle(this.params.zoomVal, document.getElementById('HawkEye').offsetTop, document.getElementById('HawkEye').offsetLeft)
      } else {
        this.params.zoomVal = 0.5
        this.$parent.changePaintStyle(this.params.zoomVal, document.getElementById('HawkEye').offsetTop, document.getElementById('HawkEye').offsetLeft)
        return false
      }
    },
    startDrag (bar, target, callback) {
      if (this.getCss(target, 'left') !== 'auto') {
        this.params.left = this.getCss(target, 'left')
      }
      if (this.getCss(target, 'top') !== 'auto') {
        this.params.top = this.getCss(target, 'top')
      }
      // o是移动对象
      bar.onmousedown = (event) => {
        this.params.flag = true
        if (!event) {
          event = window.event
          // 防止IE文字选中
          bar.onselectstart = function () {
            return false
          }
        }
        if (event.srcElement.id === 'HawkEye') {
          this.params.flagHawkEye = true
          let e = event
          this.params.currentX = e.clientX
          this.params.currentY = e.clientY
          this.oldTop = document.getElementById('HawkEye').offsetTop
          this.oldLeft = document.getElementById('HawkEye').offsetLeft
        } else {
          this.params.flagHawkEye = false
          let e = event
          this.params.currentX = e.clientX
          this.params.currentY = e.clientY
        }
      }
      document.onmouseup = () => {
        this.params.flag = false
        this.params.flagHawkEye = false
        if (this.getCss(target, 'left') !== 'auto') {
          this.params.left = this.getCss(target, 'left')
        }
        if (this.getCss(target, 'top') !== 'auto') {
          this.params.top = this.getCss(target, 'top')
        }
      }
    },
    changeHawkEye () {
      this.newHeight = Math.floor(180 * document.querySelector('.paint-bg').clientHeight / document.querySelector('.paint-bg').clientWidth)
      // if (this.newHeight > 150) {
      //   this.newHeight = 150
      // }
      document.getElementById('HawkEye').style.height = this.newHeight + 'px'
      this.startDrag(document.getElementById('HawkEye'), document.getElementById('HawkEye'))
      this.startDrag(document.getElementById('back'), document.getElementById('back'))
      document
        .getElementById('HawkEye')
        .addEventListener('mousewheel', this.bbimg)
      document.getElementById('back').onmousemove = (event) => {
        var e = event || window.event
        if (this.params.flag && !this.params.flagHawkEye) {
          var nowX = e.clientX
          var nowY = e.clientY
          var disX = nowX - this.params.currentX
          var disY = nowY - this.params.currentY
          document.getElementById('back').style.left = parseInt(this.params.left) + disX + 'px'
          document.getElementById('back').style.top = parseInt(this.params.top) + disY + 'px'
          if (typeof callback === 'function') {
            // callback((parseInt(this.params.left) || 0) + disX, (parseInt(this.params.top) || 0) + disY)
          }
          if (event.preventDefault) {
            event.preventDefault()
          }
          return false
        }
      }
      document.getElementById('HawkEye').onmousemove = (event) => {
        var e = event || window.event
        if (this.params.flag && this.params.flagHawkEye) {
          var nowX = e.clientX
          var nowY = e.clientY
          var disX = nowX - this.params.currentX
          var disY = nowY - this.params.currentY
          if (disX + this.oldLeft < 10) {
            document.getElementById('HawkEye').style.left = '10px'
          } else if (disX + this.oldLeft > 190 - document.getElementById('HawkEye').style.width.split('px')[0] * 1) {
            document.getElementById('HawkEye').style.left = 190 - document.getElementById('HawkEye').style.width.split('px')[0] * 1 + 'px'
          } else {
            document.getElementById('HawkEye').style.left = disX + this.oldLeft + 'px'
          }
          if (disY + this.oldTop < 10) {
            document.getElementById('HawkEye').style.top = '10px'
          } else if (disY + this.oldTop > document.querySelector('.mycanvas canvas').clientHeight - document.querySelector('#HawkEye').clientHeight + 10) {
            document.getElementById('HawkEye').style.top = document.querySelector('.mycanvas canvas').clientHeight - document.querySelector('#HawkEye').clientHeight + 10 + 'px'
          } else {
            document.getElementById('HawkEye').style.top = disY + this.oldTop + 'px'
          }
          if (typeof callback === 'function') {
            // callback((parseInt(this.params.left) || 0) + disX, (parseInt(this.params.top) || 0) + disY)
          }
          this.$parent.changePaintStyle(this.params.zoomVal, document.getElementById('HawkEye').offsetTop, document.getElementById('HawkEye').offsetLeft)
          if (event.preventDefault) {
            event.preventDefault()
          }
          return false
        }
      }
      if ($('#mainEdit-edit .main_video').length > 0) {
        $('#mainEdit-edit .main_video')
          .find('video')
          .css('opacity', 0)
        $('#mainEdit-edit .main_video').append(
          $('<img>')
            .addClass('monitp')
          // .attr('src', gbs.host + '/leaderview/border/videoBg.png')
            .attr('src', gbs.host + '/leaderviewWeb/border/videoBg2.png')
            .css({
              width: '100%',
              height: '100%',
              position: 'absolute',
              top: 0,
              left: 0
            })
        )
      }
      $('#mainEdit-edit .main_topo')
        .find('svg')
        .css('opacity', 0)
      $('#mainEdit-edit .main_topo').append(
        $('<img>')
          .addClass('monitp')
          // .attr('src', gbs.host + '/leaderview/border/tpstander.png')
          .attr('src', gbs.host + '/leaderviewWeb/border/topoBg.png')
          .css({
            width: '100%',
            height: '100%',
            position: 'absolute',
            top: 0,
            left: 0
          })
      )
      if ($('#mainEdit-edit .JSMpeg').length > 0) {
        $('#mainEdit-edit .JSMpeg')
          .find('canvas')
          .css('opacity', 0)
        $('#mainEdit-edit .JSMpeg').append(
          $('<img>')
            .addClass('monitp')
          // .attr('src', gbs.host + '/leaderview/border/videoBg.png')
            .attr('src', gbs.host + '/leaderviewWeb/border/videoBg2.png')
            .css({
              width: '100%',
              height: '100%',
              position: 'absolute',
              top: 0,
              left: 0
            })
        )
      }
      $('.getPicSpan').show()
      html2canvas(document.querySelector('.paint-bg'), {
        with: document.querySelector('.paint-bg').clientWidth,
        height: document.querySelector('.paint-bg').clientHeight,
        backgroundColor: 'transparent', // 设置背景透明
        allowTaint: true, // 允许加载跨域资源
        logging: false,
        scale: 1,
        onclone: function (doc) {
          $('.getPicSpan').hide()
          $('#mainEdit-edit .JSMpeg')
            .find('canvas')
            .css('opacity', 1)
          // 提前还原拓扑
          $('#mainEdit-edit .main_topo')
            .find('svg')
            .css('opacity', 1)
          $('#mainEdit-edit .main_video')
            .find('video')
            .css('opacity', 1)
          $('#mainEdit-edit .monitp').remove()
        }
      }).then(function (canvas) {
        document.querySelector('.mycanvas').appendChild(canvas)
      })
      this.getNewStyle(this.scale)
    },
    handleScroll () {
      let left = document.querySelector('#centerMapBox').scrollLeft
      let top = document.querySelector('#centerMapBox').scrollTop
      let myscalc = 100 / this.params.zoomVal
      let height = document.querySelector('.paint-bg').clientHeight
      let width = document.querySelector('.paint-bg').clientWidth
      let canvasheight = document.querySelector('.centerBox .mycanvas canvas').clientHeight
      let canvaswidth = document.querySelector('.centerBox .mycanvas canvas').clientWidth
      document.getElementById('HawkEye').style.top = top * 100 / myscalc * canvasheight / height + 10 + 'px'
      document.getElementById('HawkEye').style.left = left * 100 / myscalc * canvaswidth / width + 10 + 'px'
    }
  },
  mounted () {
    this.changeHawkEye()
    document.querySelector('#centerMapBox').addEventListener('scroll', this.handleScroll)
  },
  beforeDestroy () {}
}
</script>
<style lang="scss">
.back{
  min-height: 100px;
  width: 200px;
  position: absolute;
  z-index: 5000;
  background-color: #30364d;
  top: 0;
  left: 0;
}
.centerBox{
  width: 100%;
  padding: 10px;
  position: relative;
  overflow: hidden;
  background-color: #30364d;
}
.closeHead{
    width: 100%;
    height: 30px;
    padding: 0 0 0 10px;
    line-height: 30px;
    background-color: #222739;
    .close{
        border: none !important;
        line-height: 30px;
        min-width: 37px;
        height: 30px;
        padding: 0;
        cursor: pointer;
        background: 0 0;
    }
    .close:hover {
        background-color: transparent !important;
        color: #cad6dd;
    }
}
#HawkEye {
  height: 180px;
  width: 180px;
  position: absolute;
  box-shadow: #ff0100 0px 0px 5px inset;
  left: 10px;
  top: 10px;
}
.mycanvas{
    width: 100%;
    canvas {
        width: 100% !important;
        height: 100% !important;
    }
}
</style>
