<template>
  <div>
    <div :style="barValueStyle">
      <span class="fl"
            v-show="item.ctLegendShow !== 'false'">{{item.chartData.name}}</span>{{persent}}<span v-show="persent !== '暂无数据'">{{item.chartData.unit}}</span></div>
    <div :style="barBoxStyle">
      <div :style="barStyle"></div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'NewProgress', // 进度条
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
        width: (this.persent === '暂无数据' ? 0 : this.persent) + '%',
        height: (this.item.proHeight || 16) + 'px',
        // backgroundColor: this.item.barClr,
        background: this.item.colorful !== 'true' ? this.item.barClr : 'linear-gradient(45deg, ' + this.item.barClrs[0] + ', ' + this.item.barClrs[1] + ')',
        position: 'relative',
        borderRadius: this.item.radius === 0 ? '0px' : (this.item.radius || 8) + 'px',
        left: 0,
        top: 0
      }
    },
    persent: function () {
      var value = this.item.chartData.value
      if (!value && value !== 0) {
        return '暂无数据'
      }
      value = Number(value)
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
    // 'item.colorful': function (newV) {
    //   console.log(newV)
    // }
    // 'item.radius': function (newV) {
    //   if (newV > parseInt(this.item.proHeight / 2)) {
    //     this.item.radius = parseInt(this.item.proHeight / 2)
    //   }
    // }
  },
  destroyed: function () {
  }
}
</script>
