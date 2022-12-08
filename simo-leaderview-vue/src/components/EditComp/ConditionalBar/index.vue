<template>
  <div class="ConditionalBar">
    <div class="checkBox" :style="checkStyle" v-if="item.chartData.dataArray && item.chartData.dataArray.length">
            <div class="normalBtn" v-for="(v, i) in item.chartData.dataArray" :style="buttonStyle(i)" :key="i" @click="changeData(i)">
              {{v.title}}
            </div>
       </div>
    <div
      ref="ConditionalBar"
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
  name: 'ConditionalBar',
  props: ['item'],
  data () {
    return {
      mychart: null,
      showLine: true,
      oldOption: '',
      currentIndex: 0,
      tableData: {
        columns: [],
        rows: []
      },
      oldmyData: '',
      Oldcolorful: '',
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
    checkStyle: function () {
      return {
        left: this.item.paddingLeft + 'px',
        top: this.item.paddingTop + 'px',
        fontSize: this.item.boxFontSize + 'px',
        flexDirection: this.item.boxDirection ? 'column' : 'row'
      }
    },
    buttonStyle: function () {
      return (index) => {
        if (this.currentIndex === index) {
          return {
            backgroundImage: this.item.checkedButton
              ? 'url(' + gbs.host + this.item.checkedButton + ')'
              : 'url(' + require('./checked.png') + ')',
            padding: this.item.buttonPadding + 'px !important',
            margin: this.item.buttonMargin + 'px'
          }
        } else {
          return {
            backgroundImage: this.item.normalButton
              ? 'url(' + gbs.host + this.item.normalButton + ')'
              : 'url(' + require('./normal.png') + ')',
            padding: this.item.buttonPadding + 'px !important',
            margin: this.item.buttonMargin + 'px'
          }
        }
      }
    },
    maxData: function () {
      return this.tableData.max || 10000
    },
    minData: function () {
      return this.tableData.min || 0
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
    'item.chartData': {
      handler (newV, oldV) {
        if (this.item.chartData.dataArray && this.item.chartData.dataArray.length) {
          this.tableData = this.item.chartData.dataArray[0]
          this.currentIndex = 0
        }
      },
      deep: true
    },
    'currentIndex': function (newV, oldV) {
      if (newV !== oldV) {
        this.tableData = this.item.chartData.dataArray[this.currentIndex]
      }
    },
    'item': {
      handler (newV, oldV) {
        if (this.item.chartData.dataArray && this.item.chartData.dataArray.length) {
          this.drawFlow()
        }
      },
      deep: true
    },
    'tableData': {
      handler (newVal, oldVal) {
        if (this.tableData === null || this.tableData === '') {
          this.tableData = {
            'columns': [],
            'rows': []
          }
        }
        if (this.tableData.rows && this.tableData.columns) {
          if (this.tableData.rows.length === 0 || this.tableData.columns.length === 0) {
            this.showLine = false
          } else {
            this.showLine = true
            this.drawFlow()
          }
        }
      },
      deep: true
    }
  },
  methods: {
    changeData (index) {
      this.currentIndex = index
    },
    drawFlow () {
      this.mychart = echarts.init(this.$refs.ConditionalBar)
      let myData = this.tableData
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
          if (this.item.colorful) {
            var arry = []
            data.forEach((d, i) => {
              arry.push(0)
            })
            data.forEach((d, i) => {
              let newArry = JSON.parse(JSON.stringify(arry))
              newArry[i] = d
              myseries.push({
                name: myData.columns[index],
                data: newArry,
                stack: 'total',
                barWidth: '60%',
                type: 'bar',
                itemStyle: {
                  normal: {
                    color: this.item.ifGradual === 'true' ? {
                      type: 'linear',
                      x: 0,
                      y: 0,
                      x2: 0,
                      y2: 1,
                      colorStops: [{
                        offset: 0, color: this.item.DScatterColor[i % this.item.DScatterColor.length][0] // 0% 处的颜色
                      }, {
                        offset: 1, color: this.item.DScatterColor[i % this.item.DScatterColor.length][1] // 100% 处的颜色
                      }],
                      global: false // 缺省为 false
                    } : this.item.ScatterColor[i % this.item.DScatterColor.length]
                  }
                }
              })
            })
          } else {
            myseries.push({
              name: myData.columns[index],
              data: data,
              type: 'bar',
              barWidth: '60%',
              itemStyle: {
                normal: {
                  color: this.item.ifGradual === 'true' ? {
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
                  } : this.item.ScatterColor[index - 1]
                }
              }
            })
          }
        }
      })
      let myoption = {
        xAxis: {
          type: 'category',
          name: this.tableData.unitX,
          nameTextStyle: {
            color: this.item.DanweiColor || '#828bac',
            fontSize: this.item.DanweiSize || 16
          },
          data: myXAxisData,
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
              var rows = this.tableData.rows
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
        yAxis: {
          type: 'value',
          name: this.tableData.unit,
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
          // trigger: 'axis',
          show: this.item.tooltipShow,
          backgroundColor: this.item.tooltipBackColor,
          textStyle: {
            color: this.item.tooltipTextColor,
            fontSize: this.item.tooltipfontSize
          }
          // formatter: (params, index) => {
          //   var value = ''
          //   params.forEach(element => {
          //     if (element.value !== 0) {
          //       value = element.axisValue + '<br>' + element.seriesName + ':' + element.value
          //     }
          //   })
          //   return value
          // }
        },
        series: myseries
      }
      var rows = this.tableData.rows
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
      if (this.item.colorful !== this.Oldcolorful) {
        this.Oldcolorful = this.item.colorful
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
    if (this.item.chartData.dataArray && this.item.chartData.dataArray.length) {
      this.tableData = this.item.chartData.dataArray[this.currentIndex]
    }
    if (this.tableData.rows.length === 0 || this.tableData.columns.length === 0) {
      this.showLine = false
    } else {
      this.showLine = true
      this.drawFlow()
    }
  },
  beforeDestroy () {
    this.mychart.dispose()
    this.mychart = null
  }

}
</script>
<style lang="scss" scoped>
 .checkBox{
  display: flex;
  position:absolute;
  top: 0;
  left: 0;
  z-index: 10;
  .normalBtn{
    padding: 10px;
    cursor: pointer;
    background: url(./normal.png) no-repeat;
    background-repeat: no-repeat !important;
    background-size: 100% 100% !important;
    margin: 5px;
  }
}
</style>