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
        height: (this.item.proHeight || 16) + 'px',
        backgroundColor: this.item.bgClr,
        position: 'relative',
        borderRadius: this.item.radius === 0 ? '0px' : (this.item.radius || 8) + 'px',
        overflow: 'hidden'
      }
    },
    barStyle: function () {
      return {
        width: this.persent + '%',
        height: (this.item.proHeight || 16) + 'px',
        backgroundColor: this.item.barClr,
        position: 'relative',
        borderRadius: this.item.radius === 0 ? '0px' : (this.item.radius || 8) + 'px',
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
  },
  created: function () {
    if (this.item.proHeight === undefined) {
      this.$set(this.item, 'proHeight', 16)
    }
    if (this.item.radius === undefined) {
      this.$set(this.item, 'radius', 8)
    }
  },
  watch: {
    // 'item.proHeight': function (newV) {
    //   if (newV < 8 && newV !== '') {
    //     this.item.proHeight = 8
    //   }
    //   if (newV > 24) {
    //     this.item.proHeight = 24
    //   }
    // },
    // 'item.radius': function (newV) {
    //   if (newV > parseInt(this.item.proHeight / 2)) {
    //     this.item.radius = parseInt(this.item.proHeight / 2)
    //   }
    // }
  },
  destoryed: function () {
  }
}
</script>
