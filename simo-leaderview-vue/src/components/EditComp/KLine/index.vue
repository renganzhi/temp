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
      var rawData = JSON.parse(JSON.stringify(this.item.chartData.dataArry.data))
      var data = this.splitData(rawData)
      let myoption = {
        animation: false,
        legend: {
          show: this.item.openlegend,
          textStyle: {
            color: this.item.legendColor,
            fontSize: this.item.legendSize
          },
          left: 'center',
          data: [this.item.chartData.name, 'MA5', 'MA10', 'MA20', 'MA30'],
          top: this.item.legendStation
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          show: this.item.tooltipShow,
          backgroundColor: this.item.tooltipBackColor,
          textStyle: {
            color: this.item.tooltipColor,
            fontSize: this.item.tooltipSize
          },
          borderWidth: 1,
          borderColor: '#ccc',
          padding: 10
        },
        axisPointer: {
          link: {xAxisIndex: 'all'},
          label: {
            color: this.item.tooltipColor,
            backgroundColor: this.item.tooltipBackColor
          }
        },
        toolbox: {
          feature: {
            dataZoom: {
              yAxisIndex: false
            },
            brush: {
              type: ['lineX', 'clear']
            }
          }
        },
        brush: {
          xAxisIndex: 'all',
          brushLink: 'all',
          outOfBrush: {
            colorAlpha: 0.1
          }
        },
        visualMap: {
          show: false,
          seriesIndex: 5,
          dimension: 2,
          pieces: [{
            value: 1,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
              offset: 0,
              color: this.item.KlineColorYang[0]
            }, {
              offset: 1,
              color: this.item.KlineColorYang[1]
            }])
          }, {
            value: -1,
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
              offset: 0,
              color: this.item.KlineColorYing[0]
            }, {
              offset: 1,
              color: this.item.KlineColorYing[1]
            }])
          }]
        },
        grid: [
          {
            left: '10%',
            right: '8%',
            bottom: this.item.legendY + '%'
          },
          {
            left: '10%',
            right: '8%',
            top: '63%',
            height: '16%'
          }
        ],
        xAxis: [
          {
            type: 'category',
            data: data.categoryData,
            scale: true,
            boundaryGap: false,
            splitNumber: 20,
            min: 'dataMin',
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
            },
            max: 'dataMax',
            axisPointer: {
              z: 100
            }
          },
          {
            type: 'category',
            gridIndex: 1,
            data: data.categoryData,
            scale: true,
            boundaryGap: false,
            axisLine: {onZero: false},
            axisTick: {show: false},
            splitLine: {show: false},
            axisLabel: {show: false},
            splitNumber: 20,
            min: 'dataMin',
            max: 'dataMax'
          }
        ],
        yAxis: [
          {
            scale: true,
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
          {
            scale: true,
            gridIndex: 1,
            splitNumber: 2,
            axisLabel: {show: false},
            axisLine: {show: false},
            axisTick: {show: false},
            splitLine: {show: false}
          }
        ],
        dataZoom: [
          {
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
          }
        ],
        series: [
          {
            name: this.item.chartData.name,
            type: 'candlestick',
            data: data.values,
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
            tooltip: {
              formatter: function (param) {
                param = param[0]
                return [
                  'Date: ' + param.name + '<hr size=1 style="margin: 3px 0">',
                  'Open: ' + param.data[0] + '<br/>',
                  'Close: ' + param.data[1] + '<br/>',
                  'Lowest: ' + param.data[2] + '<br/>',
                  'Highest: ' + param.data[3] + '<br/>'
                ].join('')
              }
            }
          },
          {
            name: 'MA5',
            type: 'line',
            symbol: 'none',
            data: this.calculateMA(5, data),
            smooth: true,
            color: this.item.LinerColor ? this.item.LinerColor[0] || '' : '',
            lineStyle: {
              opacity: 0.5
            }
          },
          {
            name: 'MA10',
            type: 'line',
            symbol: 'none',
            data: this.calculateMA(10, data),
            smooth: true,
            color: this.item.LinerColor ? this.item.LinerColor[1] || '' : '',
            lineStyle: {
              opacity: 0.5
            }
          },
          {
            name: 'MA20',
            type: 'line',
            data: this.calculateMA(20, data),
            smooth: true,
            symbol: 'none',
            color: this.item.LinerColor ?this.item.LinerColor[2] || '':'',
            lineStyle: {
              opacity: 0.5
            }
          },
          {
            name: 'MA30',
            type: 'line',
            data: this.calculateMA(30, data),
            smooth: true,
            symbol: 'none',
            color: this.item.LinerColor ?this.item.LinerColor[3] || '':'',
            lineStyle: {
              opacity: 0.5
            }
          },
          {
            name: 'Volume',
            type: 'bar',
            xAxisIndex: 1,
            yAxisIndex: 1,
            data: data.volumes
          }
        ]
      }
      if (this.oldOption !== JSON.stringify(myoption)) {
        this.oldOption = JSON.stringify(myoption)
        this.mychart.clear()
        this.mychart.setOption(myoption, true)
      }
    },
    splitData (rawData) {
      var categoryData = []
      var values = []
      var volumes = []
      for (var i = 0; i < rawData.length; i++) {
        categoryData.push(rawData[i].splice(0, 1)[0])
        values.push(rawData[i])
        volumes.push([i, rawData[i][4], rawData[i][0] > rawData[i][1] ? 1 : -1])
      }

      return {
        categoryData: categoryData,
        values: values,
        volumes: volumes
      }
    },
    calculateMA (dayCount, data) {
      var result = []
      for (var i = 0, len = data.values.length; i < len; i++) {
        if (i < dayCount) {
          result.push('-')
          continue
        }
        var sum = 0
        for (var j = 0; j < dayCount; j++) {
          sum += data.values[i - j][1] * 1
        }
        result.push(+(sum / dayCount).toFixed(3))
      }
      return result
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
