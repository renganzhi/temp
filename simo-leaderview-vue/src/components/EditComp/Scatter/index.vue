<template>
  <div class="Scatter">
    <div
      :id="echartId"
      :style="boxStyle">
    </div>
  </div>

</template>
<script>
import echarts from 'echarts'
export default {
  name: 'Scatter',
  props: ['item'],
  data () {
    return {
      mychart: null,
      echartId: ''
    }
  },
  created () {
    this.echartId = 'Scatter' + document.querySelectorAll('.Scatter').length
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
        this.mychart.resize()
      })
    },
    'item.height': function () {
      this.$nextTick(() => {
        this.mychart.resize()
      })
    },
    'item': {
      handler (newVal, oldVal) {
        this.drawFlow()
      },
      deep: true
    }
  },
  methods: {
    drawFlow () {
      this.mychart = echarts.init(document.getElementById(this.echartId))
      var data = this.item.chartData.dataArry
      let myoption = {
        legend: {
          show: this.item.openlegend,
          textStyle: {
            color: 'red',
            fontSize: 16
          },
          left: 'center',
          top: 'top'
        },
        tooltip: {
          trigger: 'item',
          axisPointer: {
            type: 'cross' // 'none'
          }
        },
        xAxis: {
          nameRotate: '10',
          axisLine: {
            lineStyle: {
              color: 'red'
            }
          },
          splitLine: {
            show: false,
            lineStyle: {
              type: 'dashed'
            }
          }
        },
        yAxis: {
          axisLine: {
            lineStyle: {
              color: 'red'
            }
          },
          axisLabel: {
            color: 'green',
            rotate: 0,
            fontSize: 24
          },
          splitLine: {
            show: false,
            lineStyle: {
              type: 'dashed'
            }
          }
        },
        series: [{
          name: 'scatter',
          type: 'scatter',
          data: data[0],
          symbolSize: function (data) {
            return (data[2] + 10)
          },
          emphasis: {
            focus: 'self'
          },
          itemStyle: {
            shadowBlur: 10,
            shadowColor: 'rgba(120, 36, 50, 0.5)',
            shadowOffsetY: 5,
            shadowOffsetX: 10,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
              offset: 0,
              color: 'rgb(129, 27, 28,.5)'
            }, {
              offset: 1,
              color: 'rgb(25, 13, 207,.8)'
            }])
          }
        }, {
          name: 'sssss',
          type: 'scatter',
          data: data[1],
          symbolSize: function (data) {
            return (data[2] + 10)
          },
          emphasis: {
            focus: 'self'
          },
          itemStyle: {
            shadowBlur: 10,
            shadowColor: 'rgba(120, 36, 50, 0.5)',
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
              offset: 0,
              color: 'rgb(129, 227, 238,.5)'
            }, {
              offset: 1,
              color: 'rgb(25, 183, 207,.8)'
            }])
          }
        }]
      }
      this.mychart.setOption(myoption)
    }
  },
  mounted () {
    this.drawFlow()
  },
  beforeDestroy () {
    this.mychart.dispose()
    this.mychart = null
  }

}
</script>
