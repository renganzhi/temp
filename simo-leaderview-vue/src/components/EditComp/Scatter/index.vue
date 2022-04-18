<template>
  <div class="Scatter">
    <div
      ref="Scatter"
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
      this.mychart = echarts.init(this.$refs.Scatter)
      var data = this.item.chartData.dataArray
      let myseries = []
      data.forEach((d, index) => {
        myseries.push({
          name: d.name,
          type: 'scatter',
          data: d.points,
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
            color: this.item.ifGradual === 'false' ? this.item.ScatterColor[index] : new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
              offset: 0,
              color: this.item.DScatterColor[index] ? this.item.DScatterColor[index][0] : '#6fcaf7'
            }, {
              offset: 1,
              color: this.item.DScatterColor[index] ? this.item.DScatterColor[index][1] : '#0c79c5'
            }])
          }
        })
      })
      let myoption = {
        legend: {
          show: this.item.openlegend,
          textStyle: {
            color: this.item.legendColor,
            fontSize: this.item.legendSize
          },
          left: 'center',
          top: this.item.legendStation
        },
        grid: {
          left: this.item.legendY + '%',
          right: this.item.legendY + '%',
          top: this.item.legendY + '%',
          bottom: this.item.legendY + '%'
        },
        tooltip: {
          trigger: 'item',
          show: this.item.tooltipShow,
          backgroundColor: this.item.tooltipBackColor,
          // formatter: function (obj) {
          //   console.log()
          //   return obj.seriesName + '  ' +
          // },
          textStyle: {
            color: this.item.tooltipColor,
            fontSize: this.item.tooltipSize
          }
        },
        xAxis: {
          name: this.item.chartData.unitX,
          nameTextStyle: {
            color: this.item.DanweiColor || '#828bac',
            fontSize: this.item.DanweiSize || 16
          },
          axisLine: {
            lineStyle: {
              color: this.item.axisLinecolor
            }
          },
          axisLabel: {
            color: this.item.axisLabelcolor,
            rotate: this.item.XaxisLabelrotate,
            fontSize: this.item.axisLabelfontSize
          },
          splitLine: {
            show: this.item.splitLine,
            lineStyle: {
              color: this.item.splitLinecolor,
              type: this.item.splitLinetype
            }
          }
        },
        yAxis: {
          name: this.item.chartData.unit,
          nameTextStyle: {
            color: this.item.DanweiColor || '#828bac',
            fontSize: this.item.DanweiSize || 16
          },
          axisLine: {
            lineStyle: {
              color: this.item.axisLinecolor
            }
          },
          axisLabel: {
            color: this.item.axisLabelcolor,
            rotate: this.item.YaxisLabelrotate,
            fontSize: this.item.axisLabelfontSize
          },
          splitLine: {
            show: this.item.splitLine,
            lineStyle: {
              color: this.item.splitLinecolor,
              type: this.item.splitLinetype
            }
          }
        },
        series: myseries
      }
      if (this.oldOption !== JSON.stringify(myoption)) {
        this.oldOption = JSON.stringify(myoption)
        this.mychart.clear()
        this.mychart.setOption(myoption)
      }
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
