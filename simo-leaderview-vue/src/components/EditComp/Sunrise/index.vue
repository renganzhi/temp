<template>
  <div class="Sunrise"  ref='Gradient'>
    <div id="Sunrise" ref='Sunrise'
      :style="boxStyle">
    </div>
  </div>
</template>
<script>
import echarts from 'echarts'
export default {
  name: 'Sunrise',
  props: ['item'],
  data () {
    return {
      mychart1: null,
      oldOption: ''
    }
  },
  computed: {
    boxStyle: function () {
      return {
        width: this.item.width + 'px',
        height: this.item.height + 'px'
      }
    }
  },
  watch: {
    'item.width': function () {
      this.$nextTick(() => {
        this.mychart1.resize()
      })
    },
    'item.height': function () {
      this.$nextTick(() => {
        this.mychart1.resize()
      })
    },
    'item': {
      handler (newVal, oldVal) {
        this.drawFlow()
      },
      deep: true
    }
  },
  mounted () {
    this.drawFlow()
  },
  methods: {
    drawFlow () {
      this.mychart1 = echarts.init(this.$refs.Sunrise)
      let myoption1 = {
        backgroundColor: 'transparent',
        color: this.item.PieColorArray,
        series: {
          type: 'sunburst',
          // highlightPolicy: 'ancestor',
          data: this.item.chartData.dataArry,
          radius: [this.item.isHollow + '%', '90%'],
          itemStyle: {
            borderColor: this.item.LineColor,
            borderWidth: this.item.LineWidth
          },
          label: {
            rotate: 'radial',
            color: this.item.FontColor,
            fontSize: this.item.FontSize
          }
          // levels: [{}, {
          //   r0: 10,
          //   r: 40,
          //   label: {
          //     rotate: 0
          //   }
          // }, {
          //   r0: 40,
          //   r: 95
          // }, {
          //   r0: 105,
          //   r: 140,
          //   itemStyle: {
          //     shadowBlur: 2,
          //     shadowColor: '#FFAE57',
          //     color: 'transparent'
          //   },
          //   label: {
          //     rotate: 'tangential',
          //     fontSize: 10,
          //     color: '#FFAE57'
          //   }
          // }, {
          //   r0: 140,
          //   r: 195,
          //   itemStyle: {
          //     shadowBlur: 80,
          //     shadowColor: '#FFAE57'
          //   },
          //   label: {
          //     position: 'outside',
          //     textShadowBlur: 5,
          //     textShadowColor: '#333'
          //   },
          //   downplay: {
          //     label: {
          //       opacity: 0.1
          //     }
          //   }
          // }]
        }
      }
      if (this.oldOption !== JSON.stringify(myoption1)) {
        this.oldOption = JSON.stringify(myoption1)
        this.mychart1.clear()
        this.mychart1.setOption(myoption1)
      }
    }
  }
}
</script>
