<template>
  <div class="Dashboard">
    <div
      v-show="showDashboard"
      ref="myDashboard"
      :style="boxStyle">
    </div>
    <div class="v-charts-data-empty"
        v-show="!showDashboard"
        style="width: 100%; height: 100%; text-align: center; font-size: 12px;">
        <div><i class="icon-n-nodata"
            style="font-size: 108px;"></i><br>
          <p>抱歉，没有数据可供展示...</p>
        </div>
    </div>
  </div>

</template>
<script>
import echarts from 'echarts'
export default {
  name: 'Dashboard',
  props: ['item'],
  data () {
    return {
      mychart: null,
      showDashboard: true,
      oldOption: '',
      timer: null
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
        if (this.item.chartData.value === null) {
          this.showDashboard = false
        } else {
          this.showDashboard = true
          this.drawFlow()
        }
        this.drawFlow()
      },
      deep: true
    }
  },
  methods: {
    opcityColor (hex, opacity) {
      if (hex.indexOf('#') < 0) {
        var rgb = hex.split('(')[1].split(')')[0].split(',')
        return 'rgba(' + rgb[0].trim() + ',' + rgb[1].trim() + ',' + rgb[2].trim() + ',' + opacity + ')'
      } else {
        return hex && hex.replace(/\s+/g, '').length === 7 ? 'rgba(' + parseInt('0x' + hex.slice(1, 3)) + ',' +
          parseInt('0x' + hex.slice(3, 5)) + ',' +
          parseInt('0x' + hex.slice(5, 7)) + ',' + opacity + ')' : ''
      }
    },
    drawFlow () {
      this.mychart = echarts.init(this.$refs.myDashboard)
      var dataArr = this.item.chartData.value || 0
      var colorSet = {
        color: new echarts.graphic.LinearGradient(0, 0, 1, 1, [{
          offset: 0,
          color: this.item.lineColor[0]
        }, {
          offset: 1,
          color: this.item.lineColor[1]
        }])
      }
      let myoption = {}
      if (this.item.DashboardType === 1) {
        myoption = {
          title: {
            text: this.item.chartData.name || '',
            x: 'center',
            y: '80%',
            textStyle: {
              color: this.item.ctLegendColor,
              fontSize: this.item.ctLegendSize
            }
          },
          series: [{
            type: 'gauge',
            radius: (70 - this.item.lineGaugeWidth) + '%', // '65%',
            startAngle: 200,
            endAngle: -20,
            center: ['50%', '50%'],
            min: 0,
            max: 100,
            axisLine: {
              lineStyle: { // 属性lineStyle控制线条样式
                width: 0,
                shadowBlur: 0,
                color: [
                  [dataArr / 100, colorSet.color],
                  [1, this.item.bgClr]
                ]
              }
            },
            splitLine: {
              show: false
            },
            axisTick: {
              show: true,
              length: (this.item.lineFontSize / 5).toFixed(0),
              splitNumber: 3,
              lineStyle: {
                color: 'auto',
                width: (this.item.lineFontSize / 5).toFixed(0)
              }
            },
            axisLabel: {
              show: false
            },
            pointer: {
              show: false
            },
            detail: {
              show: false
            }
          },
          {
            type: 'gauge',
            radius: '70%',
            startAngle: 200,
            endAngle: -20,
            center: ['50%', '50%'],
            axisLine: {
              lineStyle: { // 属性lineStyle控制线条样式
                width: this.item.lineWidth,
                color: [
                  [dataArr / 100, colorSet.color],
                  [1, this.item.bgClr]
                ]
              }
            },
            splitLine: {
              show: false
            },
            axisTick: {
              show: false
            },
            axisLabel: {
              show: false
            },
            pointer: {
              show: false
            },
            title: {
              show: false
            },
            detail: {
              offsetCenter: [0, '0%'],
              color: this.item.textColor,
              fontSize: this.item.textSize,
              formatter: dataArr + this.item.chartData.unit
            }
          }
          ]

        }
      } else if (this.item.DashboardType === 2) {
        myoption = {
          series: [{
            type: 'gauge',
            radius: (64 - this.item.lineGaugeWidth) + '%', // '60%',
            splitNumber: 10,
            axisLine: {
              lineStyle: {
                color: [
                  [dataArr / 100, colorSet.color],
                  [1, this.item.bgClr]
                ],
                width: this.item.lineWidth
              }
            },
            axisLabel: {
              show: false
            },
            axisTick: {
              show: false

            },
            splitLine: {
              show: false
            },
            itemStyle: {
              show: false
            },
            detail: {
              formatter: (value) => {
                if (value !== 0) {
                  return value + this.item.chartData.unit
                } else {
                  return 0
                }
              },
              offsetCenter: [0, this.item.textHeight],
              textStyle: {
                padding: [0, 0, 0, 0],
                fontSize: this.item.textSize,
                fontWeight: '700',
                color: this.item.textColor
              }
            },
            title: { // 标题
              show: true,
              offsetCenter: [0, 46], // x, y，单位px
              textStyle: {
                color: this.item.ctLegendColor,
                fontSize: this.item.ctLegendSize, // 表盘上的标题文字大小
                fontWeight: 400,
                fontFamily: 'PingFangSC'
              }
            },
            data: [{
              name: this.item.chartData.name || '',
              value: dataArr
            }],
            pointer: {
              show: true,
              length: '75%',
              radius: '20%',
              width: 10 // 指针粗细
            },
            animationDuration: 4000
          },
          {
            name: '外部刻度',
            type: 'gauge',
            //  center: ['20%', '50%'],
            radius: '80%',
            min: 0, // 最小刻度
            max: 100, // 最大刻度
            splitNumber: 10, // 刻度数量
            startAngle: 225,
            endAngle: -45,
            axisLine: {
              show: true,
              lineStyle: {
                width: 1,
                color: [
                  [1, 'rgba(0,0,0,0)']
                ]
              }
            }, // 仪表盘轴线
            axisLabel: {
              show: true,
              color: this.item.lineFontColor,
              fontSize: this.item.lineFontSize,
              distance: 25,
              formatter: function (v) {
                switch (v + '') {
                  case '0':
                    return '0'
                  case '10':
                    return '10'
                  case '20':
                    return '20'
                  case '30':
                    return '30'
                  case '40':
                    return '40'
                  case '50':
                    return '50'
                  case '60':
                    return '60'
                  case '70':
                    return '70'
                  case '80':
                    return '80'
                  case '90':
                    return '90'
                  case '100':
                    return '100'
                }
              }
            }, // 刻度标签。
            axisTick: {
              show: true,
              splitNumber: 7,
              lineStyle: {
                color: colorSet.color, // 用颜色渐变函数不起作用
                width: 1
              },
              length: -8
            }, // 刻度样式
            splitLine: {
              show: true,
              length: -20,
              lineStyle: {
                color: colorSet.color // 用颜色渐变函数不起作用
              }
            }, // 分隔线样式
            detail: {
              show: false
            },
            pointer: {
              show: false
            }
          }
          ]
        }
      } else if (this.item.DashboardType === 3) {
        myoption = {
          title: {
            text: dataArr + this.item.chartData.unit,
            textStyle: {
              color: this.item.textColor,
              fontSize: this.item.textSize
            },
            left: 'center',
            top: (this.item.height - this.item.textSize) / 2
          },
          graphic: [{
            type: 'text',
            z: 100,
            left: 'center',
            top: '90%',
            style: {
              fill: this.item.ctLegendColor,
              text: this.item.chartData.name || '',
              fontSize: this.item.ctLegendSize, // 表盘上的标题文字大小
              fontWeight: 400,
              fontFamily: 'PingFangSC'
            }
          }],
          angleAxis: {
            max: 100,
            clockwise: false, // 逆时针
            // 隐藏刻度线
            show: false
          },
          radiusAxis: {
            type: 'category',
            show: true,
            axisLabel: {
              show: false
            },
            axisLine: {
              show: false

            },
            axisTick: {
              show: false
            }
          },
          polar: [{
            center: ['50%', '50%'], // 中心点位置
            radius: '90%' // 图形大小
          }],
          series: [{
            name: '刻度',
            type: 'gauge',
            splitNumber: 10,
            radius: (70 + this.item.lineGaugeWidth * 1) + '%', // '75%',
            center: ['50%', '50%'],
            startAngle: 0,
            endAngle: 359.9999,
            axisLine: {
              show: false
            },
            axisTick: {
              show: true,
              lineStyle: {
                color: this.item.lineFontColor,
                width: 3.5,
                shadowBlur: 1,
                shadowColor: this.item.lineFontColor
              },
              length: this.item.lineFontSize,
              splitNumber: 3
            },
            splitLine: {
              show: false
            },
            axisLabel: {
              show: false
            },
            detail: {
              show: false
            }
          }, {
            type: 'bar',
            z: 10,
            data: [dataArr],
            showBackground: false,
            center: ['50%', '50%'],
            backgroundStyle: {
              color: this.item.lineColor[1],
              borderWidth: 10,
              width: 10
            },
            coordinateSystem: 'polar',
            roundCap: true,
            barWidth: this.item.lineWidth + '%', // 10%',
            itemStyle: {
              normal: {
                opacity: 1,
                color: colorSet.color,
                shadowBlur: 5,
                shadowColor: '#2A95F9'
              }
            }
          },
          {
            type: 'pie',
            name: '内层细圆环',
            radius: [(46 + this.item.lineWidth / 6) + '%', (45 - this.item.lineWidth / 6) + '%'],
            center: ['50%', '50%'],
            hoverAnimation: false,
            clockWise: true,
            itemStyle: {
              normal: {
                color: this.item.bgClr
              }
            },
            tooltip: {
              show: false
            },
            label: {
              show: false
            },
            data: [100]
          }
          ]
        }
      } else if (this.item.DashboardType === 4) {
        myoption = {
          title: {
            text: '{a|' + dataArr.toFixed(2).split('.')[0] + '}{a|' + this.item.chartData.unit + '}\n{c|' + this.item.chartData.name + '}',
            x: 'center',
            y: 'center',
            textStyle: {
              rich: {
                a: {
                  fontSize: this.item.textSize,
                  color: this.item.textColor,
                  fontWeight: '600'
                },
                c: {
                  fontSize: this.item.ctLegendSize,
                  color: this.item.ctLegendColor,
                  padding: [5, 0]
                }
              }
            }
          },
          series: [{
            type: 'gauge',
            radius: '60%',
            clockwise: false,
            startAngle: '90',
            endAngle: '-269.9999',
            splitNumber: 40,
            detail: {
              offsetCenter: [0, -20],
              formatter: ' '
            },
            pointer: {
              show: false
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: [
                  [0, '#2CFAFC'],
                  [(100 - dataArr) / 100, colorSet.color],
                  [1, this.item.bgClr]
                ],
                width: this.item.lineWidth // 20
              }
            },
            axisTick: {
              show: false
            },
            splitLine: {
              show: true,
              length: 100,
              lineStyle: {
                shadowBlur: 10,
                shadowColor: colorSet.color,
                shadowOffsetY: '0',
                color: '#020f18',
                width: 2
              }
            },
            axisLabel: {
              show: false
            }
          },
          {
            type: 'pie',
            radius: [(48 - this.item.lineGaugeWidth) + '%', (49 - this.item.lineGaugeWidth) + '%'],
            hoverAnimation: false,
            clockWise: false,
            itemStyle: {
              normal: {
                color: '#0C355E'
              }
            },
            label: {
              show: false
            },
            data: this._dashed()
          },
          {
            type: 'pie',
            radius: [0, '30%'],
            hoverAnimation: false,
            clockWise: false,
            itemStyle: {
              normal: {
                shadowBlur: 20,
                shadowColor: '#000',
                color: colorSet.color
              }
            },
            label: {
              show: false
            },
            data: [100]
          },
          {
            type: 'pie',
            radius: ['64%', '65.5%'],
            hoverAnimation: false,
            clockWise: false,
            itemStyle: {
              normal: {
                shadowBlur: 20,
                shadowColor: this.opcityColor(this.item.lineColor[0], 1),
                color: this.item.bgClr
              }
            },
            label: {
              show: false
            },
            data: [100]
          },
          {
            type: 'pie',
            radius: ['68%', '69.5%'],
            hoverAnimation: false,
            clockWise: false,
            itemStyle: {
              normal: {
                shadowBlur: 20,
                shadowColor: this.opcityColor(this.item.lineColor[0], 1),
                color: this.item.bgClr
              }
            },
            label: {
              show: false
            },
            data: [100]
          }
          ]
        }
      }
      if (this.oldOption !== JSON.stringify(myoption)) {
        this.oldOption = JSON.stringify(myoption)
        this.mychart.clear()
        this.mychart.setOption(myoption)
        setTimeout(this.startTimer, 0)
      }
    },
    _dashed () {
      let dataArr = []
      for (var i = 0; i < 100; i++) {
        if (i % 2 === 0) {
          dataArr.push({
            name: (i + 1).toString(),
            value: 20,
            itemStyle: {
              normal: {
                color: this.opcityColor(this.item.lineColor[0], 0.2)
              }
            }
          })
        } else {
          dataArr.push({
            name: (i + 1).toString(),
            value: 20,
            itemStyle: {
              normal: {
                color: 'rgb(0,0,0,0)',
                borderWidth: 1,
                borderColor: new echarts.graphic.LinearGradient(0, 0, 1, 1, [{
                  offset: 0,
                  color: this.item.lineColor[0]
                }, {
                  offset: 1,
                  color: this.item.lineColor[1]
                }])
              }
            }
          })
        }
      }
      return dataArr
    },
    doing () {
      if (this.item.DashboardType === 4) {
        let option = echarts.init(this.$refs.myDashboard).getOption()
        option.series[1].startAngle = option.series[1].startAngle - 2
        echarts.init(this.$refs.myDashboard).setOption(option)
      } else {
        clearInterval(this.timer)
      }
    },
    startTimer () {
      clearInterval(this.timer)
      this.timer = setInterval(this.doing, 500)
    }

  },
  mounted () {
    this.drawFlow()
  },
  beforeDestroy () {
    clearInterval(this.timer)
    this.mychart.dispose()
    this.mychart = null
  }

}
</script>
