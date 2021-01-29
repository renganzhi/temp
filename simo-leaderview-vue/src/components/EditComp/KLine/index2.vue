<template>
  <div class="KLine">
    <div
      ref="myKline"
      :style="boxStyle">
    </div>
  </div>

</template>
<script>
import echarts from 'echarts'
export default {
  name: 'KLine',
  props: ['item'],
  data () {
    return {
      mychart: null,
      oldOption: '',
      colorIndex: 0,
      MAarry: {
        MA5: [],
        MA10: [],
        MA20: [],
        MA120: []
      }
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
      this.mychart = echarts.init(this.$refs.myKline)
      var data = this.item.chartData.dataArry
      let myseries = []
      let AllxAxis = []
      data.data.forEach(element => {
        AllxAxis.push(element[0])
      })
      data.data.forEach((element, index) => {
        if (index < 5) {
          this.MAarry.MA5.push('')
        } else {
          let pinjun = ((data.data[index][1][1] * 1 + data.data[index - 1][1][1] * 1 + data.data[index - 2][1][1] * 1 + data.data[index - 3][1][1] * 1 + data.data[index - 4][1][1] * 1) / 5).toFixed(2)
          this.MAarry.MA5.push(pinjun)
        }
      })
      data.name.forEach((d, index) => {
        let myData = []
        data.data.forEach(element => {
          myData.push(element[index + 1])
        })
        if (index === 0) {
          myseries.push({
            type: 'candlestick',
            name: d,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: this.item.KlineColorYang[0]
              }, {
                offset: 1,
                color: this.item.KlineColorYang[1]
              }]),
              color0: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: this.item.KlineColorYing[0]
              }, {
                offset: 1,
                color: this.item.KlineColorYing[1]
              }]),
              borderColor: this.item.borderColorYang,
              borderColor0: this.item.borderColorYing
            },
            data: myData
          })
        }
      })
      this.colorIndex = 0
      for (const key in this.MAarry) {
        if (Object.hasOwnProperty.call(this.MAarry, key)) {
          const element = this.MAarry[key]
          myseries.push({
            name: key,
            type: 'line',
            data: element,
            smooth: true,
            showSymbol: false,
            color: this.item.LinerColor[this.colorIndex],
            lineStyle: {
              width: 1
            }
          })
          this.colorIndex = this.colorIndex + 1
        }
      }
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
        tooltip: {
          trigger: 'axis',
          // trigger: 'item',
          show: this.item.tooltipShow,
          backgroundColor: this.item.tooltipBackColor,
          textStyle: {
            color: this.item.tooltipColor,
            fontSize: this.item.tooltipSize
          }
          // axisPointer: {
          // animation: false,
          // type: 'cross',
          // lineStyle: {
          //   color: '#376df4',
          //   width: 2,
          //   opacity: 1
          // }
          // }
        },
        // xAxis: {
        //   data: AllxAxis
        // },
        // yAxis: {},
        xAxis: {
          data: AllxAxis,
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
        grid: {
          left: '15%',
          right: '10%',
          top: '15%',
          bottom: this.item.legendY
        },
        series: myseries,
        dataZoom: [{
          show: this.item.dataZoom,
          textStyle: {
            color: this.item.dataZoomFontColor
          },
          handleIcon: 'path://M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
          dataBackground: {
            areaStyle: {
              color: this.item.dataZoomAreaStyle
            },
            lineStyle: {
              opacity: 0.8,
              color: '#8392A5'
            }
          },
          bottom: 20,
          brushSelect: true
        }, {
          type: 'inside'
        }]
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
