<template>
  <div class="ELine">
    <div
      ref="ELine"
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
import { gbs } from '@/config/settings'
export default {
  name: 'ELine',
  props: ['item'],
  data () {
    return {
      mychart: null,
      defaultColor: [
        '#2d98f1',
        '#32c5e9',
        '#67e0e3',
        '#9fe6b8',
        '#ffdb5c'
      ],
      showLine: true,
      oldOption: '',
      subsectionType: '',
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
      return this.item.chartData.min || 1
    },
    maxIndex: function () {
      return this.item.chartData.maxIndex || 10000
    },
    minIndex: function () {
      return this.item.chartData.minIndex || 1
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
      // let errorArry = []
      // mySeriesData.forEach(data => {
      //   let oneArry = []
      //   data.forEach((d, index) => {
      //     if (d >= this.minData && d <= this.maxData) {
      //       oneArry.push(index)
      //     }
      //   })
      //   errorArry.push(oneArry)
      // })
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
              color: this.item.ifEidetColor2 ? this.item.ifAreaGradual === 'true' ? this.item.AreaDScatterColor[index - 1] ? {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [{
                  offset: 0, color: this.item.AreaDScatterColor[index - 1][1] || '' // 0% 处的颜色
                }, {
                  offset: 1, color: this.item.AreaDScatterColor[index - 1][0] || '' // 100% 处的颜色
                }],
                global: false // 缺省为 false
              } : '' : this.item.AreaScatterColor[index - 1] || '' : this.defaultColor[index - 1] || '',
              opacity: this.item.lineArea ? 1 : 0
            },
            itemStyle: {
              normal: {
                lineStyle: {
                  color: this.item.ifEidetColor ? this.item.ifGradual === 'true' ? this.item.areaLineType ? this.item.DScatterColor[index - 1] ? {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [{
                      offset: 0, color: this.item.DScatterColor[index - 1][1] || '' // 0% 处的颜色
                    }, {
                      offset: 1, color: this.item.DScatterColor[index - 1][0] || '' // 100% 处的颜色
                    }],
                    global: false // 缺省为 false
                  } : '' : this.item.DScatterColor[index - 1] ? this.item.DScatterColor[index - 1][1] : '' || '' : this.item.ScatterColor[index - 1] || '' : this.defaultColor[index - 1] || '',
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
      if (this.item.ifEidetColor) {
        if (this.item.ifGradual === 'true') {
          this.item.DScatterColor.forEach(element => {
            optioncolor.push(element[1])
          })
        } else {
          optioncolor = this.item.ScatterColor
        }
      } else {
        optioncolor = this.defaultColor
      }
      let myoption = {
        xAxis: {
          type: 'category',
          boundaryGap: this.item.boundaryGap,
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
            interval: 'auto' // auto 采用不重叠的方式展示，具体数字n则为间隔n展示
          }
        },
        color: optioncolor,
        grid: {
          left: this.item.gridLeft + '%',
          right: this.item.gridRight + '%',
          top: this.item.gridTop + '%',
          bottom: this.item.gridBotton + '%',
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
                if (typeof (value * 1) !== 'number') {
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
      if (this.item.Linesubsection && this.item.subsectionType) {
        // let myvisualMap = []
        // errorArry.forEach((d, index) => {
        //   let pices = []
        //   d.forEach(element => {
        //     pices.push({gte: element, lte: element * 1 + 1, color: this.item.ifGradual === 'true' ? this.item.DScatterColor[0][0] || 'red' : this.item.ScatterColor[0] || 'red'})
        //   })
        //   myvisualMap.push({
        //     show: false,
        //     dimension: 0,
        //     seriesIndex: index,
        //     pieces: pices,
        //     outOfRange: {
        //       color: this.item.ifGradual === 'true' ? this.item.DScatterColor[1][0] || '#009bff' : this.item.ScatterColor[1] || '#009bff'
        //     }
        //   })
        // })
        let myvisualMap = {
          show: false,
          dimension: 0,
          pieces: [
            { lte: this.minIndex, color: this.item.ifGradual === 'true' ? this.item.DScatterColor[0][0] || 'red' : this.item.ScatterColor[0] || 'red' },
            { gte: this.minIndex, lte: this.maxIndex, color: this.item.ifGradual === 'true' ? this.item.DScatterColor[1][0] || 'red' : this.item.ScatterColor[1] || 'red' },
            { gte: this.maxIndex, color: this.item.ifGradual === 'true' ? this.item.DScatterColor[2][0] || 'red' : this.item.ScatterColor[2] || 'red' }
          ]
        }
        myoption.visualMap = myvisualMap
        myoption.series.forEach((element, index) => {
          element.itemStyle.normal.lineStyle = {
            width: this.item.lineWidth, // 设置线条粗细
            type: this.item.LineType || 'solid'
          }
          if (this.item.ifGradual === 'true' && this.item.lineArea) {
            element.itemStyle.normal.lineStyle.color = this.item.ifGradual === 'true' ? this.item.areaLineType ? this.item.DScatterColor[index] ? {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [{
                offset: 0, color: this.item.DScatterColor[index][1] || '' // 0% 处的颜色
              }, {
                offset: 1, color: this.item.DScatterColor[index][0] || '' // 100% 处的颜色
              }],
              global: false // 缺省为 false
            } : '' : this.item.DScatterColor[index] ? this.item.DScatterColor[index][1] : '' || '' : this.item.ScatterColor[index] || ''
          }
          element.itemStyle.normal.areaStyle = {
            opacity: this.item.lineArea ? 1 : 0
          }
        })
      } else if (this.item.Linesubsection && !this.item.subsectionType) {
        let visualMap = {
          show: false,
          dimension: 1,
          pieces: [
            { lte: this.minData, color: this.item.ifGradual === 'true' ? this.item.DScatterColor[0][0] || 'red' : this.item.ScatterColor[0] || 'red' },
            { gte: this.minData, lte: this.maxData, color: this.item.ifGradual === 'true' ? this.item.DScatterColor[1][0] || 'red' : this.item.ScatterColor[1] || 'red' },
            { gte: this.maxData, color: this.item.ifGradual === 'true' ? this.item.DScatterColor[2][0] || 'red' : this.item.ScatterColor[2] || 'red' }
          ]
        }
        myoption.visualMap = visualMap
        myoption.series.forEach((element, index) => {
          element.itemStyle.normal.lineStyle = {
            width: this.item.lineWidth, // 设置线条粗细
            type: this.item.LineType || 'solid'
          }
          if (this.item.ifGradual === 'true' && this.item.lineArea) {
            element.itemStyle.normal.lineStyle.color = this.item.ifGradual === 'true' ? this.item.areaLineType ? this.item.DScatterColor[index] ? {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [{
                offset: 0, color: this.item.DScatterColor[index][1] || '' // 0% 处的颜色
              }, {
                offset: 1, color: this.item.DScatterColor[index][0] || '' // 100% 处的颜色
              }],
              global: false // 缺省为 false
            } : '' : this.item.DScatterColor[index] ? this.item.DScatterColor[index][1] : '' || '' : this.item.ScatterColor[index] || ''
          }
          element.itemStyle.normal.areaStyle = {
            opacity: this.item.lineArea ? 1 : 0
          }
        })
      } else {
        delete myoption.visualMap
      }
      var rows = this.item.chartData.rows
      let barW = Math.floor((this.item.width - 60) * 0.7 / rows.length)
      let strLen = Math.round(barW / (this.item.axisLabelSize * 2))
      if (this.item.formatterType === '0' && this.oldformatterType !== this.item.formatterType) {
        myoption.xAxis.axisLabel.formatter = function (params, index) {
          return params.length > strLen ? params.substr(0, strLen) + '...' : params
        }
        this.oldformatterType = this.item.formatterType
        this.mychart.clear()
        this.mychart.setOption(myoption)
      } else if (this.oldformatterType !== this.item.formatterType) {
        myoption.xAxis.axisLabel.formatter = function (params, index) {
          return params
        }
        this.oldformatterType = this.item.formatterType
        this.mychart.clear()
        this.mychart.setOption(myoption)
      }
      if (this.oldOption !== JSON.stringify(myoption)) {
        this.oldOption = JSON.stringify(myoption)
        if (this.subsectionType === this.item.subsectionType) {

        } else {
          this.subsectionType = this.item.subsectionType
          this.mychart.clear()
        }
        if (this.Linesubsection === this.item.Linesubsection) {

        } else {
          this.Linesubsection = this.item.Linesubsection
          this.mychart.clear()
        }
        if (this.oldchartData === JSON.stringify(this.item.chartData)) {

        } else {
          this.oldchartData = JSON.stringify(this.item.chartData)
          this.mychart.clear()
        }
        this.mychart.setOption(myoption)
      } else {

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
