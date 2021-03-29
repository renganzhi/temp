<template>
  <div class="NewPie">
    <div
      ref="NewPie"
      v-show="showLine"
      :style="boxStyle">
    </div>
    <div class="v-charts-data-empty"
        v-show="!showLine"
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
  name: 'NewPie',
  props: ['item'],
  data () {
    return {
      mychart: null,
      showLine: true,
      oldOption: '',
      Linesubsection: '',
      oldformatterType: '',
      oldchartData: ''
    }
  },
  computed: {
    boxStyle: function () {
      return {
        width: this.item.width + 'px',
        height: this.item.height + 'px'
      }
    },
    maxData: function () {
      return this.item.chartData.max || 10000
    },
    minData: function () {
      return this.item.chartData.min || 0
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
        if (this.item.chartData.rows.length === 0 || this.item.chartData.columns.length === 0) {
          this.showLine = false
        } else {
          this.showLine = true
          this.drawFlow()
        }
      },
      deep: true
    }
  },
  methods: {
    drawFlow () {
      this.mychart = echarts.init(this.$refs.NewPie)
      let myData = this.item.chartData
      let optioncolor = []
      if (this.item.ifGradual === 'true') {
        this.item.DLineColorArray.forEach((element, index) => {
          optioncolor.push(new echarts.graphic.LinearGradient(0, 0, 1, 1, [{ offset: 0, color: element[0] }, { offset: 1, color: element[1] }]))
        })
      } else {
        optioncolor = this.item.LineColorArray
      }
      var SericeData = []
      myData.rows.forEach(element => {
        SericeData.push({
          name: element[myData.columns[0]],
          value: element[myData.columns[1]] * 1
        })
      })
      // let myoption = {
      //   xAxis: {
      //     type: 'category',
      //     boundaryGap: this.item.boundaryGap,
      //     name: this.item.chartData.unitX,
      //     nameTextStyle: {
      //       color: this.item.DanweiColor || '#828bac',
      //       fontSize: this.item.DanweiSize || 16
      //     },
      //     animation: true,
      //     position: 'bottom',
      //     axisTick: {
      //       show: true,
      //       alignWithLabel: true, // 刻度线与标签对齐
      //       lineStyle: {
      //         color: '#333849', // 坐标轴刻度
      //         width: 1
      //       }
      //     },
      //     axisLine: {
      //       show: true,
      //       lineStyle: {
      //         color: this.item.splitColor || '#333849', // 坐标轴颜色
      //         width: this.item.splitSize || 1
      //       }
      //     },
      //     splitLine: {
      //       show: false
      //     },
      //     axisLabel: {
      //       rotate: this.item.rotate || 0,
      //       textStyle: {
      //         color: this.item.legendColor || '#828bac',
      //         fontSize: this.item.axisLabelSize || '14'
      //       },
      //       formatter: (params, index) => {
      //         var rows = this.item.chartData.rows
      //         let barW = Math.floor((this.item.width - 60) * 0.7 / rows.length)
      //         let strLen = Math.round(barW / (this.item.axisLabelSize * 2))
      //         if (this.item.formatterType === '0') {
      //           return params.length > strLen ? params.substr(0, strLen) + '...' : params
      //         } else {
      //           return params
      //         }
      //       },
      //       interval: 'auto' // auto 采用不重叠的方式展示，具体数字n则为间隔n展示
      //     }
      //   },
      //   color: optioncolor,
      //   grid: {
      //     left: this.item.gridLeft + '%',
      //     right: this.item.gridRight + '%',
      //     top: this.item.gridTop + '%',
      //     bottom: this.item.gridBotton + '%',
      //     containLabel: true
      //   },
      //   label: {
      //     show: this.item.showPoint,
      //     fontSize: this.item.PointSize || '14',
      //     color: '#828bac' // 标点的文字颜色
      //   },
      //   legend: {
      //     x: 'center',
      //     y: this.item.legendY + '%',
      //     show: this.item.ctLegendShow,
      //     textStyle: {
      //       fontSize: this.item.ctLegendSize,
      //       color: this.item.ctLegendColor
      //     }
      //   },
      //   tooltip: {
      //     trigger: 'axis',
      //     show: this.item.tooltipShow,
      //     backgroundColor: this.item.tooltipBackColor,
      //     textStyle: {
      //       color: this.item.tooltipTextColor,
      //       fontSize: this.item.tooltipfontSize
      //     },
      //     formatter: (params, ticket, callback) => {
      //       let nameArr = []
      //       let time = params[0].axisValue
      //       let showHtm = time + '<br>'
      //       params.forEach((element, i) => {
      //         let name = element.seriesName
      //         if (nameArr.indexOf(name) === -1) {
      //           nameArr.push(name)
      //           let value = element.value
      //           if (typeof (value * 1) !== 'number') {
      //             value = '--'
      //           }
      //           showHtm += name + '：' + value + (this.item.chartData.unit || '') + '<br>'
      //         }
      //       })
      //       return showHtm
      //     }
      //   },
      //   yAxis: {
      //     type: 'value',
      //     name: this.item.chartData.unit,
      //     nameTextStyle: {
      //       color: this.item.DanweiColor || '#828bac',
      //       fontSize: this.item.DanweiSize || 16
      //     },
      //     minInterval: this.item.minInterval,
      //     axisTick: {
      //       show: false,
      //       lineStyle: {
      //         color: '#333849', // 坐标轴刻度
      //         width: 1
      //       }
      //     },
      //     axisLine: {
      //       show: true,
      //       lineStyle: {
      //         color: this.item.splitColor || '#333849', // 坐标轴颜色
      //         width: this.item.splitSize || 1
      //       }
      //     },
      //     splitLine: {
      //       show: this.item.splitShow,
      //       lineStyle: {
      //         color: this.item.splitColor || '#333849', // 修改网格线颜色
      //         width: this.item.splitSize || 1
      //       }
      //     },
      //     axisLabel: {
      //       interval: 'auto', // 采用不重叠的方式展示
      //       textStyle: {
      //         color: this.item.legendColor || '#828bac',
      //         fontSize: this.item.axisLabelSize || '14'
      //       },
      //       formatter: function (value) {
      //         if (value >= 1000) {
      //           return (value / 1000 + 'k')
      //         } else {
      //           return value
      //         }
      //       }
      //     }
      //   },
      //   series: newseries
      // }
      let myoption = {
        tooltip: {
          trigger: 'item',
          show: this.item.tooltipShow,
          backgroundColor: this.item.tooltipBackColor,
          textStyle: {
            color: this.item.tooltipTextColor,
            fontSize: this.item.tooltipfontSize
          }
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
        color: optioncolor,
        series: [
          {
            type: 'pie',
            roseType: this.item.roseType || false,
            labelLine: {
              normal: {
                show: this.item.showline
              }
            },
            // radius:,
            radius: this.item.isRing ? [(this.item.radius - this.item.detailwidth) > 0 ? (this.item.radius - this.item.detailwidth) + '%' : 0 + '%', this.item.radius + '%'] : ['0%', this.item.radius + '%'],
            data: SericeData,
            emphasis: {
              itemStyle: {

              }
            }
          }
        ]
      }
      if (this.oldOption !== JSON.stringify(myoption)) {
        this.oldOption = JSON.stringify(myoption)
        this.mychart.clear()
        this.mychart.setOption(myoption)
      }
    }
  },
  mounted () {
    if (this.item.chartData.rows.length === 0 || this.item.chartData.columns.length === 0) {
      this.showLine = false
    } else {
      this.showLine = true
      this.drawFlow()
    }
  },
  beforeDestroy () {
    if (this.mychart) {
      this.mychart.dispose()
    }
    this.mychart = null
  }

}
</script>
