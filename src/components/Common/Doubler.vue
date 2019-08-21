<template>
  <div style="text-align: center; height: 100%; overflow: hidden">
    <div>
      <span v-for="(num,index) in numArr"
            :key="index"
            :style="numCardValue">{{num}}</span>
    </div>
    <div style="margin-top: 14px;color: #828bac;font-size: 12px;">{{item.chartData.name}}{{ comUnit}}</div>
  </div>
</template>
<script>
export default {
  name: 'doubler', // 数字翻牌器
  props: ['item'],
  data () {
    return {
      showOver: false
    }
  },
  computed: {
    comUnit: function () {
      var unit = this.item.chartData.unit
      return unit === '' ? '' : ('(' + unit + ')')
    },
    numArr: function () {
      if (!this.item.chartData.value && this.item.chartData.value !== 0) {
        return '--'
      }
      var str = this.item.chartData.value.toString().trim()
      return str.split('')
    },
    numCardValue: function () {
      return {
        padding: '6px 8px',
        fontSize: this.item.fontSize + 'px',
        fontFamily: 'asn !important',
        border: '1px solid ' + this.item.bdClr,
        backgroundColor: this.item.bgClr,
        marginRight: '2px',
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
        border: '1px solid ' + this.item.bdClr,
        lineHeight: '15px',
        color: this.item.clr,
        fontSize: '20px',
        backgroundColor: this.item.bgClr
      }
    }

  }
}
</script>
