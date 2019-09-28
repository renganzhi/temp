<template>
  <div class="timeFont"
       :style="timeStyle">{{showTime}}</div>
</template>
<script>
let moment = require('moment')
// import moment from 'moment'
export default {
  name: 'vtime',
  props: ['item'],
  data () {
    return {
      showTime: '',
      refreshTime: 1, // 设置为1误差最小
      timeoutId: 0,
      sizeObj: {
        'f12': { w: 44, h: 17 },
        'f13': { w: 48, h: 18 },
        'f14': { w: 51, h: 19 },
        'f16': { w: 60, h: 22 },
        'f18': { w: 66, h: 25 },
        'f20': { w: 73, h: 28 },
        'f28': { w: 101, h: 39 },
        'f36': { w: 131, h: 51 },
        'f48': { w: 175, h: 68 },
        'f72': { w: 262, h: 102 }
      }
    }
  },
  computed: {
    timeStyle: function () {
      return {
        width: this.item.width + 'px',
        height: this.item.height + 'px',
        overflow: 'hidden',
        color: this.item.clr + ' !important',
        fontSize: this.item.fontSize + 'px !important'
        // border: this.item.bdpx + 'px solid ' + this.item.bdClr + ' !important',
      }
    }
  },
  mounted () {
    this.initTime(this.item.timeType)
    var _this = this
    this.timeoutId = setTimeout(function test () {
      _this.initTime(_this.item.timeType)
      _this.timeoutId = setTimeout(test, _this.refreshTime)
    }, this.refreshTime * 1000)
  },
  methods: {
    initTime (type) {
      switch (type) {
        case '1': // 时分秒
          this.showTime = moment().format('HH:mm:ss')
          this.refreshTime = 1
          break
        case '2':
          this.showTime = moment().format('YYYY-MM-DD')
          this.refreshTime = 60 * 60 // 这种需要确定一下精确度
          break
        case '3':
          this.showTime = moment().format('YYYY-MM-DD HH:mm')
          this.refreshTime = 10 // 存在10s内的误差
          break
        case '4':
          this.showTime = moment().format('YYYY-MM-DD HH:mm:ss')
          this.refreshTime = 1
          break
      }
    }
  },
  watch: {
    'item.timeType': function (newV, oldV) {
      this.initTime(newV)
      if (newV > oldV) {
        let len = this.showTime.length
        let width = parseInt(this.sizeObj['f' + this.item.fontSize].w / 8 * len)
        if (this.item.width < width) {
          this.item.width = width + 20
        }
      }
    },
    'item.fontSize': function (newV) {
      let len = this.showTime.length
      let width = parseInt(this.sizeObj['f' + newV].w / 8 * len)
      if (this.item.width < width) {
        this.item.width = width + 20
      }
      if (this.item.height < this.sizeObj['f' + newV].h) {
        this.item.height = this.sizeObj['f' + newV].h + 20
      }
    }
  },
  beforeDestroy () {
    this.timeoutId && clearTimeout(this.timeoutId)
  },
  destoryed: function () {
  }
}
</script>
<style scoped>
.timeFont {
  font-family: "timeFont";
}
</style>
