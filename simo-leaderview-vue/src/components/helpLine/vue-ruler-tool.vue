<template>
  <!-- <div :style="wrapperStyle" class="vue-ruler-wrapper"> -->
    <section v-show="rulerToggle"  :id="tapsStation" class="vue-ruler-wrapper">
      <div ref="horizontalRuler" class="vue-ruler-h" :style="{width:Hstyle}" @mousedown.stop="horizontalDragRuler">
      </div>
      <div ref="verticalRuler" class="vue-ruler-v" :style="{height:Vstyle}" @mousedown.stop="verticalDragRuler">
      </div>
      <div :style="{top:verticalDottedTop + 'px',width:Hstyle}" class="vue-ruler-ref-dot-h" />
      <div :style="{left:horizontalDottedLeft + 'px',height:Vstyle}" class="vue-ruler-ref-dot-v" />
      <div
        v-for="item in lineList"
        data-toggle='tooltip'
        :title="item.title"
        :style="getLineStyle(item)"
        :key="item.id"
        :class="`vue-ruler-ref-line-${item.type} hoverTips` "
        @mousedown.stop="handleDragLine(item)">
        <div v-if="item.type==='h'" class="line" :style="{'width':Hstyle,'background-color':helpLineColor}"></div>
        <div v-else class="line" :style="{height:Vstyle,'background-color':helpLineColor}"></div>
      </div>
    </section>
    <!-- <div ref="content" class="vue-ruler-content" :style="contentStyle">
      <slot />
    </div>
    <div v-show="isDrag" class="vue-ruler-content-mask"></div> -->
  <!-- </div> -->
</template>

<script>
import { on, off } from './event.js'
export default {
  name: 'VRuler',
  components: {},
  props: {
    tapsStation: {
      type: String,
      default: ''
    },
    isHotKey: {
      type: Boolean, default: true
    }, // 热键开关
    isScaleRevise: {
      type: Boolean, default: false
    }, // 刻度修正(根据content进行刻度重置)
    value: {
      type: Array,
      default: () => {
        return [] // { type: 'h', site: 50 }, { type: 'v', site: 180 }
      }
    }, // 预置参考线
    contentLayout: {
      type: Object,
      default: () => {
        return { top: 0, left: 0 }
      }
    }, // 内容部分布局
    parent: {
      type: Boolean,
      default: false
    },
    visible: {
      type: Boolean,
      default: true
    },
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
    },
    helpLineColor: {
      type: String,
      default: '#348cea'
    },
    stepLength: {
      type: Number,
      default: 50,
      validator: (val) => val % 10 === 0
    } // 步长
  },
  data () {
    return {
      size: 17,
      left_top: 18, // 内容左上填充
      windowWidth: 0, // 窗口宽度
      windowHeight: 0, // 窗口高度
      xScale: [], // 水平刻度
      yScale: [], // 垂直刻度
      topSpacing: 0, // 标尺与窗口上间距
      leftSpacing: 0, //  标尺与窗口左间距
      isDrag: false,
      Hstyle: '',
      Vstyle: '',
      Vheight: '',
      dragFlag: '', // 拖动开始标记，可能值x(从水平标尺开始拖动),y(从垂直标尺开始拖动)
      horizontalDottedLeft: -999, // 水平虚线位置
      verticalDottedTop: -999, // 垂直虚线位置
      rulerWidth: 0, // 垂直标尺的宽度
      rulerHeight: 0, // 水平标尺的高度
      dragLineId: '', // 被移动线的ID
      keyCode: {
        r: 82
      }, // 快捷键参数
      rulerToggle: true // 标尺辅助线显示开关
    }
  },
  computed: {
    wrapperStyle () {
      return {
        width: '100%', // this.windowWidth + 'px',
        height: '100%' //  this.windowHeight + 'px'
      }
    },
    contentStyle () {
      return {
        left: this.contentLayout.left + 'px',
        top: this.contentLayout.top + 'px',
        padding: this.left_top + 'px 0px 0px ' + this.left_top + 'px'
      }
    },
    lineList () {
      let hCount = 0
      let vCount = 0
      return this.value.map((item) => {
        const isH = item.type === 'h'
        return {
          id: `${item.type}_${isH ? hCount++ : vCount++}`,
          type: item.type,
          title: item.site.toFixed(2) + 'px',
          [isH ? 'top' : 'left']: item.site / (this.stepLength / 50) + this.size
        }
      })
    }
  },
  watch: {
    visible: {
      handler (visible) {
        this.rulerToggle = visible
      },
      immediate: true
    },
    scale: {
      handler () {
        this.init()
      }
    }
  },
  mounted () {
    on(document, 'mousemove', this.dottedLineMove)
    on(document, 'mouseup', this.dottedLineUp)
    on(document, 'keyup', this.keyboard)
    on(window, 'resize', this.windowResize)
    this.init()
  },
  beforeDestroy () {
    off(document, 'mousemove', this.dottedLineMove)
    off(document, 'mouseup', this.dottedLineUp)
    off(document, 'keyup', this.keyboard)
    off(window, 'resize', this.windowResize)
  },
  created () {
    this.init()
  },
  methods: {
    init () {
      this.box()
      this.scaleCalc()
      var ele = document.querySelector('.paint-bg')
      if (ele) {
        if (this.scale > 100) {
          this.Hstyle = (document.querySelector('.paint-bg').clientWidth).toFixed(0) + 'px'
          this.Vstyle = (document.querySelector('.paint-bg').clientHeight).toFixed(0) + 500 + 'px'
        } else {
          this.Hstyle = (document.querySelector('.paint-bg').clientWidth / this.scale * 100).toFixed(0) + 'px'
          this.Vstyle = (document.querySelector('.paint-bg').clientHeight / this.scale * 100).toFixed(0) + 500 + 'px'
        }
      } else {
        this.Hstyle = (this.parentW / this.scale * 100).toFixed(0) + 'px'
        this.Vstyle = (this.parentH / this.scale * 100).toFixed(0) + 'px'
      }
      this.Vheight = 18 / this.scale * 100 + 'px'
    },
    windowResize () {
      this.xScale = []
      this.yScale = []
      this.init()
    },
    getLineStyle ({type, top, left}) {
      return type === 'h' ? {top: top + 'px', width: this.Hstyle} : {left: left + 'px', height: this.Vstyle}
    },
    handleDragLine ({type, id}) {
      return type === 'h' ? this.dragHorizontalLine(id) : this.dragVerticalLine(id)
    },
    box () {
      if (this.$el) {
        // const style = window.getComputedStyle($('.m-main'), null)
        if (this.isScaleRevise) { // 根据内容部分进行刻度修正
          const style = window.getComputedStyle(this.$el.parentNode, null)
          // const content = this.$refs.content
          const contentLeft = parseInt(style.getPropertyValue('width'), 10)
          const contentTop = parseInt(style.getPropertyValue('height'), 10)
          this.getCalcRevise(this.xScale, contentLeft)
          this.getCalcRevise(this.yScale, contentTop)
        }
        if (this.parent) {
          const style = window.getComputedStyle(this.$el.parentNode, null)
          this.windowWidth = parseInt(style.getPropertyValue('width'), 10)
          this.windowHeight = parseInt(style.getPropertyValue('height'), 10)
        } else {
          this.windowWidth = document.documentElement.clientWidth - this.leftSpacing
          this.windowHeight = document.documentElement.clientHeight - this.topSpacing
        }
        this.rulerWidth = this.$refs.verticalRuler.clientWidth
        this.rulerHeight = this.$refs.horizontalRuler.clientHeight
        this.setSpacing()
      }
    }, // 获取窗口宽与高
    setSpacing () {
      this.topSpacing = this.$refs.horizontalRuler.getBoundingClientRect().y // .offsetParent.offsetTop
      this.leftSpacing = this.$refs.verticalRuler.getBoundingClientRect().x // .offsetParent.offsetLeft
    },
    scaleCalc () {
      this.getCalc(this.xScale, this.windowWidth)
      this.getCalc(this.yScale, this.windowHeight)
    }, // 计算刻度
    getCalc (array, length) {
      for (let i = 0; i < length * this.stepLength / 50; i += this.stepLength) {
        if (i % this.stepLength === 0) {
          array.push({ id: i })
        }
      }
    }, // 获取刻度方法
    getCalcRevise (array, length) {
      for (let i = 0; i < length; i += 1) {
        if (i % this.stepLength === 0 && i + this.stepLength <= length) {
          array.push({ id: i })
        }
      }
    }, // 获取矫正刻度方法
    newLine (val) {
      this.isDrag = true
      this.dragFlag = val
    }, // 生成一个参考线
    dottedLineMove ($event) {
      this.setSpacing()
      switch (this.dragFlag) {
        case 'x':
          if (this.isDrag) {
            this.verticalDottedTop = ($event.pageY - this.topSpacing) / this.scale * 100 - 18
          }
          break
        case 'y':
          if (this.isDrag) {
            this.horizontalDottedLeft = ($event.pageX - this.leftSpacing) / this.scale * 100 - 18
          }
          break
        case 'h':
          if (this.isDrag) {
            this.verticalDottedTop = ($event.pageY - this.topSpacing) / this.scale * 100 - 18
          }
          break
        case 'v':
          if (this.isDrag) {
            this.horizontalDottedLeft = ($event.pageX - this.leftSpacing) / this.scale * 100 - 18
          }
          break
        default:
          break
      }
    }, // 虚线移动
    dottedLineUp ($event) {
      this.setSpacing()
      if (this.isDrag) {
        this.isDrag = false
        const cloneList = JSON.parse(JSON.stringify(this.value))
        switch (this.dragFlag) {
          case 'x':
            cloneList.push({
              type: 'h',
              site: ($event.pageY / this.scale * 100 - this.topSpacing / this.scale * 100 - this.size - 18) * (this.stepLength / 50)
            })
            this.$emit('input', cloneList)
            break
          case 'y':
            cloneList.push({
              type: 'v',
              site: ($event.pageX / this.scale * 100 - this.leftSpacing / this.scale * 100 - this.size - 18) * (this.stepLength / 50)
            })
            this.$emit('input', cloneList)
            break
          case 'h':
            this.dragCalc(cloneList, $event.pageY / this.scale * 100, this.topSpacing / this.scale * 100 + 18, this.rulerHeight, 'h')
            this.$emit('input', cloneList)
            break
          case 'v':
            this.dragCalc(cloneList, $event.pageX / this.scale * 100, this.leftSpacing / this.scale * 100 + 18, this.rulerWidth, 'v')
            this.$emit('input', cloneList)
            break
          default:
            break
        }
        this.verticalDottedTop = this.horizontalDottedLeft = -10 - 18
      }
    }, // 虚线松开
    dragCalc (list, page, spacing, ruler, type) {
      if (page - spacing < ruler) {
        let Index, id
        this.lineList.forEach((item, index) => {
          if (item.id === this.dragLineId) {
            Index = index
            id = item.id
          }
        })
        list.splice(Index, 1, {
          type: type,
          site: -600
        })
      } else {
        let Index, id
        this.lineList.forEach((item, index) => {
          if (item.id === this.dragLineId) {
            Index = index
            id = item.id
          }
        })
        list.splice(Index, 1, {
          type: type,
          site: (page - spacing - this.size) * (this.stepLength / 50)
        })
      }
    },
    horizontalDragRuler (e) {
      this.newLine('x')
    }, // 水平标尺处按下鼠标
    verticalDragRuler () {
      this.newLine('y')
    }, // 垂直标尺处按下鼠标
    dragHorizontalLine (id) {
      this.isDrag = true
      this.dragFlag = 'h'
      this.dragLineId = id
    }, // 水平线处按下鼠标
    dragVerticalLine (id) {
      this.isDrag = true
      this.dragFlag = 'v'
      this.dragLineId = id
    }, // 垂直线处按下鼠标
    keyboard ($event) {
      if (this.isHotKey) {
        switch ($event.keyCode) {
          case this.keyCode.r:
            this.rulerToggle = !this.rulerToggle
            this.$emit('update:visible', this.rulerToggle)
            if (this.rulerToggle) {
              this.left_top = 18
            } else {
              this.left_top = 0
            }
            break
        }
      }
    } // 键盘事件
  }
}
</script>

<style lang="scss">
.vue-ruler-wrapper{
  position: static;
}
.vue-ruler{
  &-wrapper {
    left: 0;
    top: 0;
    z-index: 999;
    user-select: none;
  }
  &-h,
  &-v,
  &-ref-line-v,
  &-ref-line-h,
  &-ref-dot-h,
  &-ref-dot-v {
    position: fixed;
    left: 0;
    top: 0;
    overflow: hidden;
    z-index: 999;
  }
  &-h,
  &-v,
  &-ref-line-v,
  &-ref-line-h,
  &-ref-dot-h,
  &-ref-dot-v {
    position: fixed;
    left: 0;
    top: 0;
    overflow: hidden;
    z-index: 999;
  }

  &-h {
    width: 100%;
    height: 18px;
    top: -18px;
    opacity: 1;
  }

  &-v {
    width: 18px;
    height: 100%;
    left: -18px;
    opacity: 1;
  }

  &-v .n,
  &-h .n {
    position: absolute;
    font: 10px/1 Arial, sans-serif;
    color: #333;
    cursor: default;
  }

  &-v .n {
    width: 8px;
    left: 3px;
    word-wrap: break-word;
  }

  &-h .n {
    top: 1px;
  }

  &-ref-line-v,
  &-ref-line-h,
  &-ref-dot-h,
  &-ref-dot-v {
    z-index: 998;
  }

  &-ref-line-h {
    height: 5px;
    cursor: n-resize; /*url(./image/cur_move_h.cur), move*/
    display: flex;
    left: -10px;
    justify-content: center;
    align-items: center;
    .line{
      width: 100%;
      height: 2px;
    }
  }

  &-ref-line-v {
    width: 5px;
    top: -10px;
    cursor: w-resize; /*url(./image/cur_move_v.cur), move*/
    display: flex;
    justify-content: center;
    align-items: center;
    .line{
      height: 100%;
      width: 2px;
    }
  }

  &-ref-dot-h {
    width: 100%;
    height: 3px;
    left: -10px;
    background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAIAAAACCAMAAABFaP0WAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAAZQTFRFf39/////F3PnHQAAAAJ0Uk5T/wDltzBKAAAAEElEQVR42mJgYGRgZAQIMAAADQAExkizYQAAAABJRU5ErkJggg==)
      repeat-x left 1px; /*./image/line_dot.png*/
    cursor: n-resize; /*url(./image/cur_move_h.cur), move*/
    top: -10px;
  }

  &-ref-dot-v {
    width: 3px;
    height: 100%;
    top: -10px;
    _height: 9999px;
    background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAIAAAACCAMAAABFaP0WAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAAZQTFRFf39/////F3PnHQAAAAJ0Uk5T/wDltzBKAAAAEElEQVR42mJgYGRgZAQIMAAADQAExkizYQAAAABJRU5ErkJggg==)
      repeat-y 1px top; /*./image/line_dot.png*/
    cursor: w-resize; /*url(./image/cur_move_v.cur), move*/
    left: -10px;
  }
  &-content {
    position: absolute;
    z-index: 997;
  }
  &-content-mask{
    position: absolute;
    width: 100%;
    height: 100%;
    background: transparent;
    z-index: 998;
  }
}

</style>
