<template>
  <div class="BiaxialBarChart">
    <div
      ref="BiaxialBarChart"
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
export default {
  name: 'BiaxialBarChart',
  props: ['item'],
  data () {
    return {
      mychart: null,
      showLine: true,
      oldOption: '',
      oldmyData: '',
      //   Linesubsection: '',
      oldformatterType: ''
    //   oldchartData: ''
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
      this.mychart = echarts.init(this.$refs.BiaxialBarChart)
      let myData = this.item.chartData
      var myseries = []
      var myYAxisData = []
      var mySeriesData = []
      myData.rows.forEach(element => {
        myData.columns.forEach((e, d) => {
          if (d === 0) {
            myYAxisData.push(element[e])
          } else if (d === 2) {
            if (mySeriesData[d]) {
              mySeriesData[d].push(-element[e])
            } else {
              mySeriesData[d] = [-element[e]]
            }
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
            name: myData.columns[index],
            data: data,
            type: 'bar',
            stack: 'total',
            barWidth: this.item.barWidth,
            showBackground: this.item.showBackground,
            backgroundStyle: {
              color: this.item.backgroundColor
            },
            itemStyle: {
              normal: {
                color: this.item.ifGradual === 'true' ? this.item.gradientDirection === '1'? {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [{
                    offset: 0, color: this.item.DScatterColor[index - 1][0] // 0% 处的颜色
                  }, {
                    offset: 1, color: this.item.DScatterColor[index - 1][1] // 100% 处的颜色
                  }],
                  global: false // 缺省为 false
                } : {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 1,
                  y2: 0,
                  colorStops: [{
                    offset: 0, color: this.item.DScatterColor[index - 1][0] // 0% 处的颜色
                  }, {
                    offset: 1, color: this.item.DScatterColor[index - 1][1] // 100% 处的颜色
                  }],
                  global: false // 缺省为 false
                } : this.item.ScatterColor[index - 1],
                borderRadius: Number(this.item.barRadius)
              }
            }
          })
        }
      })
      let myoption = {
        yAxis: {
          type: 'category',
          name: this.item.chartData.unitX,
          nameTextStyle: {
            color: this.item.DanweiColor || '#828bac',
            fontSize: this.item.DanweiSize || 16
          },
          data: myYAxisData,
          animation: true,
          animationDuration: 500,
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
            formatter: (params, index) => {
              var rows = this.item.chartData.rows
              let barW = Math.floor((this.item.width - 60) * 0.7 / rows.length)
              let strLen = Math.round(barW / (this.item.axisLabelSize * 2))
              if (this.item.formatterType === '0') {
                return params.length > strLen ? params.substr(0, strLen) + '...' : params
              } else {
                return params
              }
            },
            interval: 0 // auto 采用不重叠的方式展示，具体数字n则为间隔n展示
          }
        },
        xAxis: {
          type: 'value',
          name: this.item.chartData.unit || '',
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
            interval: 0, // 采用不重叠的方式展示
            textStyle: {
              color: this.item.legendColor || '#828bac',
              fontSize: this.item.axisLabelSize || '14'
            },
            formatter: function (value) {
              if (value >= 1000) {
                return (value / 1000 + 'k')
              } else {
                return Math.abs(value)
              }
            }
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
        grid: {
          left: this.item.gridLeft + '%',
          right: this.item.gridRight + '%',
          top: this.item.gridTop + '%',
          bottom: this.item.gridBotton + '%',
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          show: this.item.tooltipShow,
          backgroundColor: this.item.tooltipBackColor,
          borderWidth: 0,
          textStyle: {
            color: this.item.tooltipTextColor,
            fontSize: this.item.tooltipfontSize
          },
          formatter: function (params) {
            // 因为为了展示双向，数据使用的是正负数，但展示时为正数，需要重写tips将负数转换为正数
            let html = ''
            // let html = `
            //   <div style="margin: 0px 0 0;line-height:1;">
            //     <div style="margin: 0px 0 0;line-height:1;">
            //       <div style="font-size:14px;color:#fff;font-weight:400;line-height:1;">
            //         ${params[0].name}
            //       </div>
            //       <div style="margin: 10px 0 0;line-height:1;">
            //         <div style="margin: 0px 0 0;line-height:1;">
            //           <div style="margin: 0px 0 0;line-height:1;">
            //             <span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:#6fcaf7;"></span>
            //             <span style="font-size:14px;color:#fff;font-weight:400;margin-left:2px">CPU利用率</span>
            //             <span style="float:right;margin-left:20px;font-size:14px;color:#fff;font-weight:900">70</span>
            //             <div style="clear:both"></div>
            //           </div>
            //           <div style="clear:both"></div>
            //         </div>
            //         <div style="margin: 10px 0 0;line-height:1;">
            //           <div style="margin: 0px 0 0;line-height:1;">
            //             <span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:#6fcaf7;"></span>
            //             <span style="font-size:14px;color:#fff;font-weight:400;margin-left:2px">CPU利用率</span>
            //             <span style="float:right;margin-left:20px;font-size:14px;color:#fff;font-weight:900">70</span>
            //             <div style="clear:both"></div>
            //           </div>
            //           <div style="clear:both"></div>
            //         </div>
            //         <div style="clear:both"></div>
            //       </div>
            //       <div style="clear:both"></div>
            //     </div>
            //     <div style="clear:both"></div>
            //   </div>`
            var div = ''
            params.forEach((v, i) => {
              if (i === 0) {
                div = `<div style="margin: 0px 0 0;line-height:1;">
                      <div style="margin: 0px 0 0;line-height:1;">
                        <span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:${typeof v.color == 'string' ? v.color : v.color.colorStops[0].color};"></span>
                        <span style="font-weight:400;margin-left:2px">${v.seriesName}</span>
                        <span style="float:right;margin-left:20px;font-weight:900">${v.value < 0 ? -v.value : v.value}</span>
                        <div style="clear:both"></div>
                      </div>
                      <div style="clear:both"></div>
                    </div>`
              } else {
                div = div + `
                <div style="margin: 10px 0 0;line-height:1;">
                      <div style="margin: 0px 0 0;line-height:1;">
                        <span style="display:inline-block;margin-right:4px;border-radius:10px;width:10px;height:10px;background-color:${typeof v.color == 'string' ? v.color : v.color.colorStops[0].color};"></span>
                        <span style="font-weight:400;margin-left:2px">${v.seriesName}</span>
                        <span style="float:right;margin-left:20px;font-weight:900">${v.value < 0 ? -v.value : v.value}</span>
                        <div style="clear:both"></div>
                      </div>
                      <div style="clear:both"></div>
                    </div>
                `
              }
            })
            html = `
            <div style="margin: 0px 0 0;line-height:1;">
                <div style="margin: 0px 0 0;line-height:1;">
                  <div style="font-weight:400;line-height:1;">
                    ${params[0].name}
                  </div>
                  <div style="margin: 10px 0 0;line-height:1;">
                    ` + div + `
                    <div style="clear:both"></div>
                  </div>
                  <div style="clear:both"></div>
                </div>
                <div style="clear:both"></div>
              </div>
            `
            return html
          }
        },
        series: myseries
      }
      var rows = this.item.chartData.rows
      let barW = Math.floor((this.item.width - 60) * 0.7 / rows.length)
      let strLen = Math.round(barW / (this.item.axisLabelSize * 2))
      if (this.item.formatterType === '0' && this.oldformatterType !== this.item.formatterType) {
        myoption.yAxis.axisLabel.formatter = function (params, index) {
          return params.length > strLen ? params.substr(0, strLen) + '...' : params
        }
        this.oldformatterType = this.item.formatterType
        this.mychart.clear()
        this.mychart.setOption(myoption)
      } else if (this.oldformatterType !== this.item.formatterType) {
        myoption.yAxis.axisLabel.formatter = function (params, index) {
          return params
        }
        this.oldformatterType = this.item.formatterType
        this.mychart.clear()
        this.mychart.setOption(myoption)
      }
      if (this.oldmyData !== JSON.stringify(myData.columns)) {
        this.oldmyData = JSON.stringify(myData.columns)
        this.mychart.clear()
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
