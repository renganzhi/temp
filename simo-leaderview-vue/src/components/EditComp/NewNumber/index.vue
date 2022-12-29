<template>
  <div style="text-align: center; height: 100%;">
    <div v-if="numberType === 'number'">
      <span :style="numStyle">
        {{numStr}}
      </span>
    </div>
    <div v-if="numberType === 'percent'">
      <span :style="numStyle2">
          <div :style="triangleStyle">
            <img :src="imgUrl" :style="{width: item.upIcon + 'px'}" alt="">
          </div>{{increase}}
        </span>
    </div>
    <div
      v-show="showTitle"
      :style="titleStyle"
    >
      {{item.chartData.name}}{{ comUnit}}
    </div>
  </div>
</template>
<script>
export default {
  name: 'NewNumber', // 指标展示
  props: ['item'],
  data () {
    return {
      showOver: true
    }
  },
  watch: {
    'item.chartData': function () {
      if (!this.item.chartData) {
        this.item.chartData = {
          'name': '--',
          'unit': '',
          'value': '-',
          'type': 'number'
        }
      }
    }
  },
  computed: {
    numStr: function () {
      if (this.item.chartData.value !== 0 && !this.item.chartData.value) {
        return '--'
      }
      var num = Math.round(this.item.chartData.value * 100) / 100
      if (num != num) {
        return '--'
      }
      num = num.toString()
      if (!num) {
        return null
      }
      var numArr = num.split('.')
      // var formatStr = this.toThousands(numArr[0])
      // 英文逗号显示不完全
      var formatStr = numArr[0]
      if (numArr[1]) {
        return formatStr + '.' + numArr[1]
      }
      return formatStr
    },
    increase: function () {
      return this.item.chartData.value && (Math.abs(this.item.chartData.value).toFixed(2) + '%')
    },
    numberType: function () {
      return this.item.chartData.type || 'number'
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
    numStyle2: function () { // 悬浮字号
      return {
        position: 'absolute',
        lineHeight: '54px',
        left: this.item.upPos + 'px' || '200px',
        fontSize: this.item.upFontSize + 'px',
        color: this.item.chartData.value > 0 ? '#ff1f36' : '#2ed8be'
      }
    },
    titleStyle: function () { // {'font-size': '12px', 'color': item.legendColor || '#828bac'}
      return {
        fontSize: this.item.legendFontSize + 'px',
        color: this.item.legendColor || '#828bac'
      }
    },
    triangleStyle: function () {
      // let obj = this.item.chartData.value > 0
      //   ? {
      //     borderTop: (this.item.upIcon || 6) + 'px solid transparent',
      //     borderBottom: (this.item.upIcon || 6) + 'px solid red'
      //   } // 增加
      //   : {
      //     borderTop: (this.item.upIcon || 6) + 'px solid green',
      //     borderBottom: (this.item.upIcon || 6) + 'px solid transparent'
      //   } // 减少
      return {
        position: 'absolute',
        left: (this.item.upIconLeft || -20) + 'px' || '-20px',
        top: (this.item.upIconTop || 23) + 'px' || '23px'
        // borderLeft: (this.item.upIcon || 6) + 'px solid transparent',
        // borderRight: (this.item.upIcon || 6) + 'px solid transparent',
      }
    },
    imgUrl: function () {
      let img = ''
      if (this.item.chartData.value >= 0) {
        img = require('./上涨.png')
      } else if (this.item.chartData.value < 0) {
        img = require('./下跌.png')
      }
      return img
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
  destroyed: function () {
  }
}
</script>
