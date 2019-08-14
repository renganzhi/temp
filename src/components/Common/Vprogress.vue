<template>
  <div>
    <div :style="barValueStyle">
      <span class="fl">{{item.chartData.name}}</span>{{persent}}{{item.chartData.unit}}</div>
    <div :style="barBoxStyle">
      <div :style="barStyle"></div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'vprogress', // 进度条
  props: ['item'],
  data () {
    return {

    }
  },
  computed: {
    barValueStyle: function () {
      return {
        color: this.item.clr,
        fontSize: this.item.fontSize + 'px',
        textAlign: 'right',
        'margin-bottom': '8px'
      }
    },
    barBoxStyle: function () {
      return {
        width: '100%',
        height: '16px',
        backgroundColor: this.item.bgClr,
        position: 'relative',
        borderRadius: '10px',
        overflow: 'hidden'
      }
    },
    barStyle: function () {
      return {
        width: this.persent + '%',
        height: '100%',
        backgroundColor: this.item.barClr,
        position: 'relative',
        borderRadius: '10px',
        left: 0,
        top: 0
      }
    },
    persent: function () {
      var value = Number(this.item.chartData.value)
      if (value > 100) {
        value = 100
      } else if (value < 0 || !value) {
        value = 0
      }
      return value
    }
  }
}
</script>
