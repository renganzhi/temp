<template>
  <!-- :key="item.id + marqueeKey" -->
  <div class="text-wrap"
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
         :id='"hideText" + item.id'
         :style="hideTextStyle">{{item.ctName}}</div>
  </div>
</template>
<script>
export default {
  name: 'marquee',
  props: ['item', 'disabled'],
  data () {
    return {
      intervalId: 0,
      marqueeKey: new Date().getTime() + Math.random() * 1000,
      speed: 10,
      stop: false,
      distance: 0,
      offsetWidth: 0, // 内容改变之后与原始长度的偏移量
      textWidth: 0 // 字体长度
    }
  },
  methods: {
    getMessage (vtextarea) {
      // this.stopmove()
      this.stop = true
      this.$nextTick(() => {
        this.$refs.marqueeText.focus()
      })
    },
    initMove () {
      // this.stop = false
      this.textWidth = this.$refs.hideText.getBoundingClientRect().width + 20 // textarea 默认有padding10
      this.speed = parseInt((this.textWidth + this.item.width) / 60)
      this.stop = false
      this.marqueeKey = new Date().getTime() + Math.random() * 1000
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
    dis () {
      return !this.disabled
    },
    boxStyle: function () {
      return {
        width: this.textWidth + 'px',
        animation: this.stop ? '' : 'textMove linear ' + this.speed + 's infinite',
        position: 'relative'
        // left: '100%'
        // transform: 'translateX(' + this.distance + 'px)'
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
    textWidth: function () {
      if (this.stop) return
      this.$nextTick(() => {
        this.speed = parseInt((this.textWidth + this.item.width) / 60)
      })
    },
    'item.fontSize': function () {
      this.$nextTick(() => {
        this.initMove()
      })
    },
    'item.ctName': function (newV, oldV) {
      if (this.stop) return
      this.$nextTick(() => {
        this.initMove()
      })
    }
  },
  mounted: function () {
    // this.move()
    this.initMove()
  },
  beforeDestroy: function () {
    // this.stopmove()
  },
  destoryed: function () {
  }
}
</script>

<style>
@keyframes textMove {
  from {
    left: 100%;
    transform: translateX(0);
  }
  to {
    left: 0px;
    transform: translateX(-100%);
  }
}
</style>
