<template>
  <div class="ELine">
    <div
      ref="ELine"
      :style="boxStyle">
    </div>
  </div>

</template>
<script>
import echarts from 'echarts'
import { gbs } from '@/config/settings'
export default {
  name: 'ELine',
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
      var reg = /^\/api/
      var baseUrl = ''
      if (!reg.test(this.item.symbolSrc)) {
        baseUrl = gbs.host
      }
      this.mychart = echarts.init(this.$refs.ELine)
      let myData = this.item.chartData
      var myseries = []
      var myXAxisData = []
      var mySeriesData = []
      myData.rows.forEach(element => {
        myData.columns.forEach((e, d) => {
          if (d === 0) {
            myXAxisData.push(element[e])
          } else {
            if (mySeriesData[d]) {
              mySeriesData[d].push(element[e])
            } else {
              mySeriesData[d] = [element[e]]
            }
          }
        })
      })
      mySeriesData.forEach((data, index) => {
        if (data) {
          myseries.push({
            type: 'line',
            name: myData.columns[index],
            smooth: this.item.smooth ? this.item.smooth === 'true' : true, // 折线/曲线
            data: data,
            symbol: this.item.symbolType !== 'pic' ? this.item.symbolType : this.item.symbolSrc ? `image://${baseUrl}${this.item.symbolSrc}` : 'circle',
            showAllSymbol: false,
            symbolSize: this.item.symbolSize,
            areaStyle: {
              color: this.item.ifGradual === 'true' ? this.item.DLineColorArray[index - 1] ? {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: this.item.DLineColorArray[index - 1][1] || '' // 0% 处的颜色
                }, {
                  offset: 1, color: this.item.DLineColorArray[index - 1][0] || '' // 100% 处的颜色
                }],
                global: false // 缺省为 false
              } : '' : this.item.LineColorArray[index - 1] || '',
              opacity: this.item.lineArea ? 1 : 0
            },
            itemStyle: {
              normal: {
                lineStyle: {
                  color: this.item.ifGradual === 'true' ? this.item.areaLineType ? this.item.DLineColorArray[index - 1] ? {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [{
                      offset: 0, color: this.item.DLineColorArray[index - 1][1] || '' // 0% 处的颜色
                    }, {
                      offset: 1, color: this.item.DLineColorArray[index - 1][0] || '' // 100% 处的颜色
                    }],
                    global: false // 缺省为 false
                  } : '' : this.item.DLineColorArray[index - 1] ? this.item.DLineColorArray[index - 1][1] : '' || '' : this.item.LineColorArray[index - 1] || '',
                  width: this.item.lineWidth, // 设置线条粗细
                  type: this.item.LineType || 'solid'
                }
              }
            }
          })
        }
      })
      let newseries = []
      myseries.forEach(element => {
        newseries.push(element)
      })
      let optioncolor = []
      if (this.item.ifGradual === 'true') {
        this.item.DLineColorArray.forEach(element => {
          optioncolor.push(element[1])
        })
      } else {
        optioncolor = this.item.LineColorArray
      }
      let myoption = {
        xAxis: {
          type: 'category',
          name: this.item.chartData.unitX,
          nameTextStyle: {
            color: this.item.DanweiColor || '#828bac',
            fontSize: this.item.DanweiSize || 16
          },
          animation: true,
          animationDuration: 500,
          data: myXAxisData,
          position: 'bottom',
          axisTick: {
            show: true,
            alignWithLabel: true, // 刻度线与标签对齐
            lineStyle: {
              color: '#333849', // 坐标轴刻度
              width: 1
            }
          },
          axisLine: {
            show: true,
            lineStyle: {
              color: this.item.splitColor || '#333849', // 坐标轴颜色
              width: this.item.splitSize || 1
            }
          },
          splitLine: {
            show: false
          },
          axisLabel: {
            rotate: this.item.rotate || 0,
            textStyle: {
              color: this.item.legendColor || '#828bac',
              fontSize: this.item.axisLabelSize || '14'
            },
            interval: 'auto' // auto 采用不重叠的方式展示，具体数字n则为间隔n展示
          }
        },
        color: optioncolor,
        grid: {
          // left: this.item.gridTop,
          // right: this.item.gridTop,
          top: this.item.gridTop + '%',
          bottom: this.item.gridTop + '%',
          containLabel: true
        },
        label: {
          show: this.item.showPoint,
          fontSize: this.item.PointSize || '14',
          color: '#828bac' // 标点的文字颜色
        },
        legend: {
          x: 'center',
          y: this.item.legendY + '%',
          show: this.item.ctLegendShow,
          textStyle: {
            fontSize: this.item.ctLegendSize,
            color: this.item.ctLegendColor
          }
        },
        tooltip: {
          trigger: 'axis',
          show: this.item.tooltipShow,
          backgroundColor: this.item.tooltipBackColor,
          textStyle: {
            color: this.item.tooltipTextColor,
            fontSize: this.item.tooltipfontSize
          },
          formatter: (params, ticket, callback) => {
            let nameArr = []
            let time = params[0].axisValue
            let showHtm = time + '<br>'
            params.forEach((element, i) => {
              let name = element.seriesName
              if (nameArr.indexOf(name) === -1) {
                nameArr.push(name)
                let value = element.value
                if (typeof (value) !== 'number') {
                  value = '--'
                }
                showHtm += name + '：' + value + (this.item.chartData.unit || '') + '<br>'
              }
            })
            return showHtm
          }
        },
        yAxis: {
          type: 'value',
          name: this.item.chartData.unit,
          nameTextStyle: {
            color: this.item.DanweiColor || '#828bac',
            fontSize: this.item.DanweiSize || 16
          },
          minInterval: this.item.minInterval,
          axisTick: {
            show: false,
            lineStyle: {
              color: '#333849', // 坐标轴刻度
              width: 1
            }
          },
          axisLine: {
            show: true,
            lineStyle: {
              color: this.item.splitColor || '#333849', // 坐标轴颜色
              width: this.item.splitSize || 1
            }
          },
          splitLine: {
            show: this.item.splitShow,
            lineStyle: {
              color: this.item.splitColor || '#333849', // 修改网格线颜色
              width: this.item.splitSize || 1
            }
          },
          axisLabel: {
            interval: 'auto', // 采用不重叠的方式展示
            textStyle: {
              color: this.item.legendColor || '#828bac',
              fontSize: this.item.axisLabelSize || '14'
            },
            formatter: function (value) {
              if (value >= 1000) {
                return (value / 1000 + 'k')
              } else {
                return value
              }
            }
          }
        },
        series: newseries
      }
      if (this.oldOption !== JSON.stringify(myoption)) {
        this.oldOption = JSON.stringify(myoption)
        this.mychart.setOption(myoption)
      } else {

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
