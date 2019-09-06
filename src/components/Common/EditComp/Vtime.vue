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
      timeoutId: 0
    }
  },
  computed: {
    timeStyle: function () {
      return {
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
    'item.timeType': function (newV) {
      this.initTime(newV)
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
