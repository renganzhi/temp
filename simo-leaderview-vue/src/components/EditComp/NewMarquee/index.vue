<template>
  <!-- :key="item.id + marqueeKey" -->
  <div v-if="item.direction === 'top'"
       class="text-wrap"
       :key="item.id + marqueeKey"
       :style="wrapStyle">
    <div class="text"
         :style="boxStyle1">
      <textarea ref="marqueeText"
                :style="textStyle1"
                v-model="item.ctName"
                :disabled="dis"
                @blur="initMove"></textarea>
    </div>
    <div ref="hideText"
         :id='"hideText" + item.id'
         :style="hideTextStyle1">{{item.ctName}}</div>
  </div>
  <div v-else
       class="text-wrap"
       :key="item.id + marqueeKey"
       :style="wrapStyle">
    <div class="text"
         :style="boxStyle">
      <textarea ref="marqueeText"
                :style="textStyle"
                v-model="item.ctName"
                :disabled="dis"
                @blur="initMove"
                @keydown="checkEnter"></textarea>
    </div>
    <div @keydown="checkEnter"
         ref="hideText"
         class="noWrapText"
         contenteditable="true"
         :id='"hideText" + item.id'
         :style="hideTextStyle">{{item.ctName}}</div>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
export default {
  name: 'marquee',
  props: ['item', 'disabled'],
  data () {
    return {
      intervalId: 0,
      marqueeKey: new Date().getTime() + Math.random() * 10000,
      speed: 10,
      speed2: 3,
      textHeight: 30,
      stop: false,
      distance: 0,
      offsetWidth: 0, // 内容改变之后与原始长度的偏移量
      textWidth: 0 // 字体长度
    }
  },
  methods: {
    getMessage (vtextarea) {
      // this.stopmove()
      if (this.item.ctDataSource === 'static') {
        this.stop = true
        this.$nextTick(() => {
          this.$refs.marqueeText.focus()
        })
      }
    },
    topSpeed (val) {
      if (!val) {
        val = this.item.speed
      }
      if (val === 1) {
        this.speed2 = parseFloat((Number(this.textHeight) + Number(this.item.height)) / 70)
      } else if (val === 3) {
        this.speed2 = parseFloat((Number(this.textHeight) + Number(this.item.height)) / 30)
      } else {
        this.speed2 = parseFloat((Number(this.textHeight) + Number(this.item.height)) / 50)
      }
    },
    leftSpeed (val) {
      if (!val) {
        val = this.item.speed
      }
      if (val === 1) {
        this.speed = parseFloat((Number(this.textWidth) + Number(this.item.width)) / 70)
      } else if (val === 3) {
        this.speed = parseFloat((Number(this.textWidth) + Number(this.item.width)) / 30)
      } else {
        this.speed = parseFloat((Number(this.textWidth) + Number(this.item.width)) / 50)
      }
    },
    initMove () {
      // this.stop = false
      this.textWidth = this.$refs.hideText.getBoundingClientRect().width + 20 // textarea 默认有padding10
      this.textHeight = this.$refs.hideText.getBoundingClientRect().height + 20
      var _transform = $(this.$el).parents('.full-height').css('transform')
      if (!_transform) {
        _transform = $('.paint-bg').css('transform')
      }
      if (_transform && _transform !== 'none') {
        var reg = /\((.*)\)$/
        var _temp = reg.exec(_transform)
        _temp = _temp[0]
        _temp = _temp.slice(1, _temp.length)
        _temp = _temp.split(',')
        var _scaleX = Number(_temp[0])
        var _scaleY = Number(_temp[3])
        if (_scaleX) {
          this.textWidth = this.textWidth / _scaleX
        }
        if (_scaleY) {
          this.textHeight = this.textHeight / _scaleY
        }
      }
      // if (_transform) {
      //   var _temp = _transform.split(',')[0]
      //   var _scaleX = _temp.split('.')[1]
      //   if (_scaleX) {
      //     _scaleX = Number('0.' + _scaleX)
      //     this.textWidth = this.textWidth / _scaleX
      //   }
      // }
      if (this.item.direction === 'top') {
        this.topSpeed()
      } else {
        this.leftSpeed()
      }
      // this.speed = parseFloat((this.textWidth + this.item.width) / 60)
      // this.speed2 = parseFloat((this.textHeight + this.item.height) / 40) // 100 60 40
      this.stop = false
      this.marqueeKey = new Date().getTime() + Math.random() * 10000
    },
    // 该方法在大屏展示时，因为缩放的原因所以会有问题
    move () {
      // 获取文字text的计算后宽度 由于overflow的存在，直接获取不到，需要独立的div计算
      this.textWidth = this.$refs.hideText.getBoundingClientRect().width + 20
      this.distance = 0 // 位移距离
      this.stopmove()
      // 设置位移
      var _this = this
      this.intervalId = setTimeout(function test () {
        clearTimeout(_this.intervalId)
        _this.distance = _this.distance - 1
        // 如果位移超过文字宽度，则从末尾开始移动
        if (-_this.distance >= _this.textWidth) {
          _this.distance = _this.item.width
        }
        _this.intervalId = setTimeout(test, 25)
      }, 25)
      // this.intervalId = setInterval(() => {
      //   this.distance = this.distance - 1
      //   // 如果位移超过文字宽度，则从末尾开始移动
      //   if (-this.distance >= this.textWidth) {
      //     this.distance = this.item.width
      //   }
      // }, 25)
    },
    remove () {
      this.marqueeKey = new Date().getTime()
      this.$nextTick(() => {
        this.move()
      })
    },
    stopmove () {
      this.intervalId && clearTimeout(this.intervalId)
      this.intervalId = null
    },
    checkEnter (e) {
      // 禁止换行
      var et = e || window.event
      var keycode = et.charCode || et.keyCode
      if (keycode === 13) {
        if (window.event) {
          window.event.returnValue = false
        } else {
          e.preventDefault() // for firefox
        }
      }
    }
  },
  computed: {
    ...mapGetters([
      'pageVisiable'
    ]),
    dis () {
      return !this.disabled
    },
    boxStyle1: function () {
      return {
        // width: '100%',
        width: this.textWidth + 'px',
        height: this.textHeight + 'px',
        overflow: 'hidden',
        animation: this.stop ? '' : 'textMoveTop linear ' + this.speed2 + 's infinite',
        position: 'relative',
        left: this.stop ? '0px' : '',
        transform: this.stop ? 'translateX(0px)' : ''
      }
    },
    textStyle1: function () {
      return {
        width: '100% !important',
        // height: 'auto !important',
        height: this.textHeight + 'px !important',
        color: this.item.clr + ' !important',
        border: 'none !important',
        fontSize: this.item.fontSize + 'px !important',
        float: 'left',
        backgroundColor: 'transparent !important',
        overflow: 'hidden'
      }
    },
    hideTextStyle1: function () {
      return {
        fontSize: this.item.fontSize + 'px !important',
        float: 'left',
        padding: '10px',
        opacity: 0
      }
    },
    boxStyle: function () {
      return {
        width: this.textWidth + 'px',
        animation: this.stop ? '' : 'textMoveLeft linear ' + this.speed + 's infinite',
        position: 'relative',
        left: this.stop ? '0px' : '',
        transform: this.stop ? 'translateX(0px)' : ''
      }
    },
    wrapStyle: function () {
      return {
        width: this.item.width + 'px !important',
        height: this.item.height + 'px !important',
        overflow: 'hidden',
        display: 'block',
        backgroundColor: this.item.bgClr + ' !important',
        border: this.item.bdpx + 'px solid ' + this.item.bdClr + ' !important',
        boxSizing: 'border-box'
      }
    },
    textStyle: function () {
      return {
        width: this.textWidth + 20 + 'px !important',
        //    height:this.item.height+'px !important',
        color: this.item.clr + ' !important',
        border: 'none !important',
        fontSize: this.item.fontSize + 'px !important',
        whiteSpace: 'nowrap',
        float: 'left',
        backgroundColor: 'transparent !important',
        overflow: 'hidden'
      }
    },
    hideTextStyle: function () {
      return {
        fontSize: this.item.fontSize + 'px !important',
        whiteSpace: 'nowrap',
        float: 'left',
        opacity: 0
        // opacity: 1,
        // zIndex: 1000,
        // position: 'absolute'
      }
    }
  },
  watch: {
    pageVisiable: function (newV) {
      if (newV) {
        this.initMove()
      } else {
        this.stopmove()
      }
    },
    textWidth: function () {
      if (this.stop) return
      this.$nextTick(() => {
        this.leftSpeed()
      })
    },
    textHeight: function () {
      if (this.stop) return
      this.$nextTick(() => {
        this.topSpeed()
      })
    },
    'item.speed': function (val) {
      if (this.item.direction === 'top') {
        this.topSpeed(val)
      } else {
        this.leftSpeed(val)
      }
      this.marqueeKey = new Date().getTime() + Math.random() * 10000
    },
    'item.direction': function (val) {
      if (val === 'left') {
        let text = this.$refs.hideText.innerText
        this.item.ctName = text
      }
      if (this.stop) return
      this.$nextTick(() => {
        this.initMove()
      })
    },
    'item.fontSize': function () {
      this.textHeight = this.$refs.hideText.getBoundingClientRect().height
      this.$nextTick(() => {
        this.initMove()
      })
    },
    'item.ctName': function (newV, oldV) {
      if (this.stop || newV === oldV) return
      this.$nextTick(() => {
        this.initMove()
      })
    },
    'item.width': function (newV) {
      // this.textHeight = this.$refs.hideText.getBoundingClientRect().height
      if (this.stop) return
      this.initMove()
    }
  },
  mounted: function () {
    // this.move()
    this.textHeight = this.$refs.hideText.getBoundingClientRect().height
    this.initMove()
  },
  beforeDestroy: function () {
    this.stopmove()
  },
  destroyed: function () {
  }
}
</script>

<style>
@keyframes textMoveLeft {
  from {
    left: 100%;
    transform: translateX(0);
  }
  to {
    left: 0px;
    transform: translateX(-100%);
  }
}
@keyframes textMoveRight {
  from {
    left: 0px;
    transform: translateX(-100%);
  }
  to {
    left: 100%;
    transform: translateX(0);
  }
}
@keyframes textMoveTop {
  from {
    top: 100%;
    transform: translateY(0);
  }
  to {
    /* 有padding值 */
    top: 12px;
    transform: translateY(-100%);
  }
}
</style>
