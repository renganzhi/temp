<template>
    <section class="vue-ruler">
      <div ref="horizontalRuler" class="vue-rulerh" :style="{width:Hstyle}" @mousedown.stop="horizontalDragRuler">
      </div>
      <div ref="verticalRuler" class="vue-rulerv" :style="{height:Vstyle}" @mousedown.stop="verticalDragRuler">
      </div>
    </section>
</template>

<script>
import { on, off } from './event.js'
export default {
  name: 'VRuler',
  components: {},
  props: {
    scale: {
      type: Number,
      default: 100
    },
    parentW: {
      type: Number,
      default: 1920
    },
    parentH: {
      type: Number,
      default: 1080
    }
  },
  data () {
    return {
      Hstyle: '',
      Vstyle: ''
    }
  },
  mounted () {
    on(window, 'resize', this.windowResize)
    this.init()
  },
  beforeDestroy () {
    off(window, 'resize', this.windowResize)
  },
  computed: {
    // Hstyle () {
    //   var ele = document.querySelector('.m-main')
    //   console.log(ele)
    //   if (ele) {
    //     return window.getComputedStyle(ele).width.split('px')[0] * 1 / this.scale * 100 + 'px'
    //   } else {
    //     return this.parentW / 100 * 100 + 'px'
    //   }
    // },
    // Vstyle () {
    //   // var ele = document.querySelector('.m-main')
    //   // if (ele) {
    //   return this.parentH / 100 * 100 + 'px'
    //   // }
    // }
  },
  created () {
    this.init()
  },
  methods: {
    horizontalDragRuler (e) {
      e.stopPropagation()
      e.preventDefault()
      this.$parent.fatherhorizontalDragRuler()
    },
    init () {
      var ele = document.querySelector('.m-main')
      if (ele) {
        this.Hstyle = window.getComputedStyle(ele).width.split('px')[0] * 1 / 100 * 100 + 'px'
        this.Vstyle = window.getComputedStyle(ele).height.split('px')[0] * 1 / 100 * 100 + 'px'
      } else {
        this.Hstyle = this.parentW / 100 * 100 + 'px'
        this.Vstyle = this.parentH / 100 * 100 + 'px'
      }
    },
    windowResize () {
      this.init()
    },
    verticalDragRuler (e) {
      e.stopPropagation()
      e.preventDefault()
      this.$parent.fatherverticalDragRuler()
    }
  }
}
</script>

<style lang="scss">
.vue-ruler{
  position: absolute;
  z-index: 999;
}

  .vue-rulerh {
    height: 12px;
    position: fixed;
    left: 65px;
    top: 50px;
    overflow: hidden;
    z-index: 999;
    opacity: 1;
    background: url(./image/ruler_h.jpg) repeat-x; /*./image/ruler_h.png*/
  }

  .vue-rulerv {
    width: 12px;
    position: fixed;
    left: 65px;
    top: 50px;
    overflow: hidden;
    z-index: 999;
    opacity: 1;
    background:url(./image/ruler_v.jpg) repeat-y; /*./image/ruler_v.png*/
  }
</style>
