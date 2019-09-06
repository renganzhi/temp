<template>
  <div style="text-align: center; height: 100%;">
    <div>
      <span :style="numStyle">{{numStr}}</span>
    </div>
    <div v-show="showTitle"
         style="color: #828bac; font-size: 12px;">{{item.chartData.name}}{{ comUnit}}</div>
  </div>
</template>
<script>
export default {
  name: 'vnumber', // 指标展示
  props: ['item'],
  data () {
    return {
      showOver: true
    }
  },
  computed: {
    numStr: function () {
      var num = Math.round(this.item.chartData.value * 100) / 100
      num = num.toString()
      if (!num) {
        return null
      }
      var numArr = num.split('.')
      var formatStr = this.toThousands(numArr[0])
      if (numArr[1]) {
        return formatStr + '.' + numArr[1]
      }
      return formatStr
    },
    showTitle: function () {
      return this.item.ctLegendShow === 'true'
    },
    comUnit: function () {
      var unit = this.item.chartData.unit
      return unit === '' ? '' : ('(' + unit + ')')
    },
    numStyle: function () {
      return {
        // padding: '6px 8px',
        fontSize: this.item.fontSize + 'px',
        fontFamily: this.item.fontFamily + ' !important',
        color: this.item.clr
      }
    },
    numCardOver: function () {
      return {
        display: 'inline-block',
        verticalAlign: 'top',
        textAlign: 'center',
        width: '16px',
        height: '16px',
        lineHeight: '15px',
        color: this.item.clr,
        fontSize: '20px'
      }
    }
  },
  methods: {
    toThousands: function (num) {
      num = (num || 0).toString()
      var result = ''
      while (num.length > 3) {
        result = ',' + num.slice(-3) + result
        num = num.slice(0, num.length - 3)
      }
      if (num) { result = num + result }
      return result
    }
  },
  destoryed: function () {
  }
}
</script>
