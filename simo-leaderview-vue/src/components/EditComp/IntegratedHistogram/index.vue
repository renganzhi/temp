<template>
  <div class="NewHistogram">
    <div
      ref="NewHistogram"
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
// import echarts from 'echarts'
export default {
  name: 'NewHistogram',
  props: ['item'],
  data () {
    return {
      mychart: null,
      // showLine: true,
      oldOption: '',
      oldmyData: '',
      Oldcolorful: '',
      //   Linesubsection: '',
      oldformatterType: '',
      oldOption2: '',
      oldmyData2: '',
      Oldcolorful2: '',
      //   Linesubsection: '',
      oldformatterType2: '',
      oldOption3: '',
      oldmyData3: '',
      Oldcolorful3: '',
      //   Linesubsection: '',
      oldformatterType3: '',
      oldOption4: '',
      oldmyData4: '',
      Oldcolorful4: '',
      myInterVale: '',
      nowShowIndex: 0,
      //   Linesubsection: '',
      oldformatterType4: ''
    //   oldchartData: ''
    }
  },
  computed: {
    showLine: function () {
      if (this.item.barType === 'NewHistogram') {
        if (this.nowShowDataObj.rows.length === 0 || this.nowShowDataObj.columns.length === 0) {
          return false
        }
      }
      if (this.item.barType === 'NewGroupHistogram') {
        if (this.item.chartData2.rows.length === 0 || this.item.chartData2.columns.length === 0) {
          return false
        }
      }
      if (this.item.barType === 'NewGroupLeftHistogram') {
        if (this.item.chartData3.rows.length === 0 || this.item.chartData3.columns.length === 0) {
          return false
        }
      }
      if (this.item.barType === 'NewBar') {
        if (this.item.chartData4.rows.length === 0 || this.item.chartData4.columns.length === 0) {
          return false
        }
      }
      return true
    },
    nowShowDataObj:function(){
      var reg = /^\{[\s\S]*\}$/
        // 先判断是{}类型的对象，而不是new Object
      if(this.item.chartData1 && this.item.barType === 'NewHistogram'){
        var textData = JSON.stringify(this.item.chartData1)
        if (reg.test(textData.trim())) {
          return this.item.chartData1
        }else{
          if(this.item.chartData1.length > 1){
          if (this.myInterVale){
            clearInterval(this.myInterVale)
          }
          this.myInterVale = setInterval(() => {
            this.nowShowIndex = ( this.nowShowIndex + 1)% this.item.chartData1.length
            this.drawFlow()
          }, this.item.intervieData);
          }
          return this.item.chartData1[this.nowShowIndex]
        }
      }
    },
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
    'item.intervieData':function() {
      if (this.myInterVale){
        clearInterval(this.myInterVale)
      }
      this.myInterVale = setInterval(() => {
        this.nowShowIndex = ( this.nowShowIndex + 1)% this.item.chartData.length
        this.drawFlow()
      }, this.item.intervieData);
    },
    'item.barType': function () {
      this.drawFlow()
    },
    'item.upColor': function () {
      this.drawFlow()
    },
    'item.downColor': function () {
      this.drawFlow()
    },
    'item': {
      handler (newVal, oldVal) {
        // if (this.item.chartData.rows.length === 0 || this.item.chartData.columns.length === 0) {
        //   this.showLine = false
        // } else {
        // this.showLine = true
        this.drawFlow()
        // }
      },
      deep: true
    }
  },
  methods: {
    drawFlow () {
      this.mychart = echarts.init(this.$refs.NewHistogram)
      if (this.item.barType === 'NewHistogram') {
        let myseries = []
        let myXAxisData = []
        let mySeriesData = []
        let myData = this.nowShowDataObj
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
            if (this.item.colorful1) {
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
                  barWidth: this.item.barWidth1,
                  showBackground: this.item.showBackground1,
                  backgroundStyle: {
                    color: this.item.backgroundColor1
                  },
                  type: 'bar',
                  label: {
                    show: this.item.HistogramType === 2,
                    position: 'top',
                    distance: 10,
                    fontSize: 16,
                    color: '#01fff4'
                  },
                  itemStyle: {
                    normal: {
                      color: this.item.ifGradual === 'true' ? {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{
                          offset: 0, color: this.item.DScatterColor1[i % this.item.DScatterColor1.length][0] // 0% 处的颜色
                        }, {
                          offset: 1, color: this.item.DScatterColor1[i % this.item.DScatterColor1.length][1] // 100% 处的颜色
                        }],
                        global: false // 缺省为 false
                      } : this.item.ScatterColor1[i % this.item.DScatterColor1.length],
                      borderRadius: Number(this.item.barRadius1)
                    }
                  }
                })
              })
            } else {
              myseries.push({
                name: myData.columns[index],
                data: data,
                type: 'bar',
                barWidth: this.item.barWidth1,
                showBackground: this.item.showBackground1,
                backgroundStyle: {
                  color: this.item.backgroundColor1
                },
                label: {
                  show: this.item.HistogramType === 2 || this.item.HistogramType === 3,
                  position: 'top',
                  distance: 10,
                  fontSize: 16,
                  color: this.item.topTextColor
                },
                itemStyle: {
                  normal: {
                    color: this.item.ifGradual === 'true' ? {
                      type: 'linear',
                      x: 0,
                      y: 0,
                      x2: 0,
                      y2: 1,
                      colorStops: [{
                        offset: 0, color: this.item.DScatterColor1[index - 1][0] // 0% 处的颜色
                      }, {
                        offset: 1, color: this.item.DScatterColor1[index - 1][1] // 100% 处的颜色
                      }],
                      global: false // 缺省为 false
                    } : this.item.ScatterColor1[index - 1],
                    borderRadius: Number(this.item.barRadius1)
                  }
                }
              })
            }
            if (this.item.HistogramType === 2) {
              myseries.push({
                name: myData.columns[index],
                data: data,
                type: 'pictorialBar',
                itemStyle: {
                  normal: {
                    color: this.item.lineColor
                  }
                },
                symbolRepeat: 'fixed',
                symbolMargin: 6,
                symbol: 'rect',
                symbolClip: true,
                symbolSize: [this.item.barWidth1, 2],
                // barWidth: '60%',
                symbolPosition: 'start',
                symbolOffset: [0, -1],
                tooltip: {
                  show: false
                },
                z: 0,
                zlevel: 1
              })
            }
            if (this.item.HistogramType === 3) {
              myseries.push({
                name: myData.columns[index],
                data: [1, 1, 1, 1, 1, 1],
                type: 'pictorialBar',
                barMaxWidth: '20',
                symbol: 'diamond',
                symbolOffset: [0, '50%'],
                symbolSize: [this.item.barWidth1, 10],
                itemStyle: {
                  color: this.item.downColor
                },
                tooltip: {
                  show: false
                }
              },
              {
                name: myData.columns[index],
                data: data,
                type: 'pictorialBar',
                barMaxWidth: '20',
                symbolPosition: 'end',
                symbol: 'diamond',
                symbolOffset: [0, '-50%'],
                symbolSize: [this.item.barWidth1, 12],
                zlevel: 2,
                itemStyle: {
                  color: this.item.upColor
                },
                tooltip: {
                  show: false
                }
              })
            }
          }
        })
        let myoption = {
          xAxis: {
            type: 'category',
            name: this.nowShowDataObj.unitX,
            nameTextStyle: {
              color: this.item.DanweiColor1 || '#828bac',
              fontSize: this.item.DanweiSize1 || 16
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
                color: this.item.splitColor1 || '#333849', // 坐标轴颜色
                width: this.item.splitSize1 || 1
              }
            },
            splitLine: {
              show: false
            },
            axisLabel: {
              rotate: this.item.rotate1 || 0,
              textStyle: {
                color: this.item.legendColor1 || '#828bac',
                fontSize: this.item.axisLabelSize1 || '14'
              },
              formatter: (params, index) => {
                var rows = this.nowShowDataObj.rows
                let barW = Math.floor((this.item.width - 60) * 0.7 / rows.length)
                let strLen = Math.round(barW / (this.item.axisLabelSize1 * 2))
                if (this.item.formatterType1 === '0') {
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
            name: this.nowShowDataObj.unit,
            nameTextStyle: {
              color: this.item.DanweiColor1 || '#828bac',
              fontSize: this.item.DanweiSize1 || 16
            },
            minInterval: this.item.minInterval1,
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
                color: this.item.splitColor1 || '#333849', // 坐标轴颜色
                width: this.item.splitSize1 || 1
              }
            },
            splitLine: {
              show: this.item.splitShow1,
              lineStyle: {
                color: this.item.splitColor1 || '#333849', // 修改网格线颜色
                width: this.item.splitSize1 || 1
              }
            },
            axisLabel: {
              interval: 'auto', // 采用不重叠的方式展示
              textStyle: {
                color: this.item.legendColor1 || '#828bac',
                fontSize: this.item.axisLabelSize1 || '14'
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
            y: this.item.legendY1 + '%',
            show: this.item.ctLegendShow1,
            textStyle: {
              fontSize: this.item.ctLegendSize1,
              color: this.item.ctLegendColor1
            }
          },
          grid: {
            left: this.item.gridLeft1 + '%',
            right: this.item.gridRight1 + '%',
            top: this.item.gridTop1 + '%',
            bottom: this.item.gridBotton1 + '%',
            containLabel: true
          },
          tooltip: {
          // trigger: 'axis',
            show: this.item.tooltipShow1,
            borderWidth: 0,
            backgroundColor: this.item.tooltipBackColor1,
            textStyle: {
              color: this.item.tooltipTextColor1,
              fontSize: this.item.tooltipfontSize1
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
        var rows = this.nowShowDataObj.rows
        let barW = Math.floor((this.item.width - 60) * 0.7 / rows.length)
        let strLen = Math.round(barW / (this.item.axisLabelSize1 * 2))
        if (this.item.formatterType1 === '0' && this.oldformatterType !== this.item.formatterType1) {
          myoption.xAxis.axisLabel.formatter = function (params, index) {
            return params.length > strLen ? params.substr(0, strLen) + '...' : params
          }
          this.oldformatterType = this.item.formatterType1
          this.mychart.clear()
          this.mychart.setOption(myoption, true)
        } else if (this.oldformatterType !== this.item.formatterType1) {
          myoption.xAxis.axisLabel.formatter = function (params, index) {
            return params
          }
          this.oldformatterType = this.item.formatterType1
          this.mychart.clear()
          this.mychart.setOption(myoption, true)
        }
        if (this.item.colorful1 !== this.Oldcolorful) {
          this.Oldcolorful = this.item.colorful1
          this.mychart.clear()
          this.mychart.setOption(myoption, true)
        }
        if (this.oldmyData !== JSON.stringify(myData.columns)) {
          this.oldmyData = JSON.stringify(myData.columns)
          this.mychart.clear()
        }
        if (this.oldOption !== JSON.stringify(myoption)) {
          this.oldOption = JSON.stringify(myoption)
          this.mychart.setOption(myoption, true)
        } else {

        }
      }
      if (this.item.barType === 'NewGroupHistogram') {
        let myseries = []
        let myXAxisData = []
        let mySeriesData = []
        let myData = this.item.chartData2
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
              name: myData.columns[index],
              data: data,
              type: 'bar',
              barWidth: this.item.barWidth2, // 柱图宽度
              barGap: this.item.barGap2,
              itemStyle: {
                normal: {
                  color: this.item.ifGradual === 'true' ? {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [{
                      offset: 0, color: this.item.DScatterColor2[index - 1][0] // 0% 处的颜色
                    }, {
                      offset: 1, color: this.item.DScatterColor2[index - 1][1] // 100% 处的颜色
                    }],
                    global: false // 缺省为 false
                  } : this.item.ScatterColor2[index - 1]
                }
              }
            })
          }
        })
        let myoption = {
          xAxis: {
            type: 'category',
            name: this.item.chartData2.unitX,
            nameTextStyle: {
              color: this.item.DanweiColor2 || '#828bac',
              fontSize: this.item.DanweiSize2 || 16
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
                color: this.item.splitColor2 || '#333849', // 坐标轴颜色
                width: this.item.splitSize2 || 1
              }
            },
            splitLine: {
              show: false
            },
            axisLabel: {
              rotate: this.item.rotate2 || 0,
              textStyle: {
                color: this.item.legendColor2 || '#828bac',
                fontSize: this.item.axisLabelSize2 || '14'
              },
              formatter: (params, index) => {
                var rows = this.item.chartData2.rows
                let barW = Math.floor((this.item.width - 60) * 0.7 / rows.length)
                let strLen = Math.round(barW / (this.item.axisLabelSize2 * 2))
                if (this.item.formatterType2 === '0') {
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
            name: this.item.chartData2.unit,
            nameTextStyle: {
              color: this.item.DanweiColor2 || '#828bac',
              fontSize: this.item.DanweiSize2 || 16
            },
            minInterval: this.item.minInterval2,
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
                color: this.item.splitColor2 || '#333849', // 坐标轴颜色
                width: this.item.splitSize2 || 1
              }
            },
            splitLine: {
              show: this.item.splitShow2,
              lineStyle: {
                color: this.item.splitColor2 || '#333849', // 修改网格线颜色
                width: this.item.splitSize2 || 1
              }
            },
            axisLabel: {
              interval: 'auto', // 采用不重叠的方式展示
              textStyle: {
                color: this.item.legendColor2 || '#828bac',
                fontSize: this.item.axisLabelSize2 || '14'
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
            y: this.item.legendY2 + '%',
            show: this.item.ctLegendShow2,
            textStyle: {
              fontSize: this.item.ctLegendSize2,
              color: this.item.ctLegendColor2
            }
          },
          grid: {
            left: this.item.gridLeft2 + '%',
            right: this.item.gridRight2 + '%',
            top: this.item.gridTop2 + '%',
            bottom: this.item.gridBotton2 + '%',
            containLabel: true
          },
          tooltip: {
            trigger: 'axis',
            show: this.item.tooltipShow2,
            borderWidth: 0,
            backgroundColor: this.item.tooltipBackColor2,
            textStyle: {
              color: this.item.tooltipTextColor2,
              fontSize: this.item.tooltipfontSize2
            }
          },
          series: myseries
        }
        let rows = this.item.chartData2.rows
        let barW = Math.floor((this.item.width - 60) * 0.7 / rows.length)
        let strLen = Math.round(barW / (this.item.axisLabelSize2 * 2))
        if (this.item.formatterType2 === '0' && this.oldformatterType !== this.item.formatterType2) {
          myoption.xAxis.axisLabel.formatter = function (params, index) {
            return params.length > strLen ? params.substr(0, strLen) + '...' : params
          }
          this.oldformatterType = this.item.formatterType2
          this.mychart.clear()
          this.mychart.setOption(myoption, true)
        } else if (this.oldformatterType !== this.item.formatterType2) {
          myoption.xAxis.axisLabel.formatter = function (params, index) {
            return params
          }
          this.oldformatterType = this.item.formatterType2
          this.mychart.clear()
          this.mychart.setOption(myoption, true)
        }
        if (this.oldmyData !== JSON.stringify(myData.columns)) {
          this.oldmyData = JSON.stringify(myData.columns)
          this.mychart.clear()
        }
        if (this.oldOption !== JSON.stringify(myoption)) {
          this.oldOption = JSON.stringify(myoption)
          this.mychart.setOption(myoption, true)
        } else {

        }
      }
      if (this.item.barType === 'NewGroupLeftHistogram') {
        let myseries = []
        let myXAxisData = []
        let mySeriesData = []
        let myData = this.item.chartData3
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
              name: myData.columns[index],
              data: data,
              type: 'bar',
              stack: 'total',
              barWidth: this.item.barWidth3,
              showBackground: this.item.showBackground3,
              backgroundStyle: {
                color: this.item.backgroundColor3
              },
              itemStyle: {
                normal: {
                  color: this.item.ifGradual === 'true' ? {
                    type: 'linear',
                    x: 0,
                    y: 0,
                    x2: 0,
                    y2: 1,
                    colorStops: [{
                      offset: 0, color: this.item.DScatterColor3[index - 1][0] // 0% 处的颜色
                    }, {
                      offset: 1, color: this.item.DScatterColor3[index - 1][1] // 100% 处的颜色
                    }],
                    global: false // 缺省为 false
                  } : this.item.ScatterColor3[index - 1],
                  borderRadius: Number(this.item.barRadius3)
                }
              }
            })
          }
        })
        let myoption = {
          xAxis: {
            type: 'category',
            name: this.item.chartData3.unitX,
            nameTextStyle: {
              color: this.item.DanweiColor3 || '#828bac',
              fontSize: this.item.DanweiSize3 || 16
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
                color: this.item.splitColor3 || '#333849', // 坐标轴颜色
                width: this.item.splitSize3 || 1
              }
            },
            splitLine: {
              show: false
            },
            axisLabel: {
              rotate: this.item.rotate3 || 0,
              textStyle: {
                color: this.item.legendColor3 || '#828bac',
                fontSize: this.item.axisLabelSize3 || '14'
              },
              formatter: (params, index) => {
                var rows = this.item.chartData3.rows
                let barW = Math.floor((this.item.width - 60) * 0.7 / rows.length)
                let strLen = Math.round(barW / (this.item.axisLabelSize3 * 2))
                if (this.item.formatterType3 === '0') {
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
            name: this.item.chartData3.unit,
            nameTextStyle: {
              color: this.item.DanweiColor3 || '#828bac',
              fontSize: this.item.DanweiSize3 || 16
            },
            minInterval: this.item.minInterval3,
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
                color: this.item.splitColor3 || '#333849', // 坐标轴颜色
                width: this.item.splitSize3 || 1
              }
            },
            splitLine: {
              show: this.item.splitShow3,
              lineStyle: {
                color: this.item.splitColor3 || '#333849', // 修改网格线颜色
                width: this.item.splitSize3 || 1
              }
            },
            axisLabel: {
              interval: 'auto', // 采用不重叠的方式展示
              textStyle: {
                color: this.item.legendColor3 || '#828bac',
                fontSize: this.item.axisLabelSize3 || '14'
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
            y: this.item.legendY3 + '%',
            show: this.item.ctLegendShow3,
            textStyle: {
              fontSize: this.item.ctLegendSize3,
              color: this.item.ctLegendColor3
            }
          },
          grid: {
            left: this.item.gridLeft3 + '%',
            right: this.item.gridRight3 + '%',
            top: this.item.gridTop3 + '%',
            bottom: this.item.gridBotton3 + '%',
            containLabel: true
          },
          tooltip: {
            trigger: 'axis',
            show: this.item.tooltipShow3,
            borderWidth: 0,
            backgroundColor: this.item.tooltipBackColor3,
            textStyle: {
              color: this.item.tooltipTextColor3,
              fontSize: this.item.tooltipfontSize3
            }
          },
          series: myseries
        }
        let rows = this.item.chartData3.rows
        let barW = Math.floor((this.item.width - 60) * 0.7 / rows.length)
        let strLen = Math.round(barW / (this.item.axisLabelSize3 * 2))
        if (this.item.formatterType3 === '0' && this.oldformatterType !== this.item.formatterType3) {
          myoption.xAxis.axisLabel.formatter = function (params, index) {
            return params.length > strLen ? params.substr(0, strLen) + '...' : params
          }
          this.oldformatterType = this.item.formatterType3
          this.mychart.clear()
          this.mychart.setOption(myoption, true)
        } else if (this.oldformatterType !== this.item.formatterType3) {
          myoption.xAxis.axisLabel.formatter = function (params, index) {
            return params
          }
          this.oldformatterType = this.item.formatterType3
          this.mychart.clear()
          this.mychart.setOption(myoption, true)
        }
        if (this.oldmyData !== JSON.stringify(myData.columns)) {
          this.oldmyData = JSON.stringify(myData.columns)
          this.mychart.clear()
        }
        if (this.oldOption !== JSON.stringify(myoption)) {
          this.oldOption = JSON.stringify(myoption)
          this.mychart.setOption(myoption, true)
        } else {

        }
      }
      if (this.item.barType === 'NewBar') {
        let myData = this.item.chartData4
        let myseries = []
        let myXAxisData = []
        let mySeriesData = []
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
            if (this.item.colorful4) {
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
                  barWidth: this.item.barWidth4,
                  type: 'bar',
                  itemStyle: {
                    normal: {
                      color: this.item.ifGradual === 'true' ? this.item.gradientDirection4 === '1' ? {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 0,
                        y2: 1,
                        colorStops: [{
                          offset: 0, color: this.item.DScatterColor4[i % this.item.DScatterColor4.length][0] // 0% 处的颜色
                        }, {
                          offset: 1, color: this.item.DScatterColor4[i % this.item.DScatterColor4.length][1] // 100% 处的颜色
                        }],
                        global: false // 缺省为 false
                      } : {
                        type: 'linear',
                        x: 0,
                        y: 0,
                        x2: 1,
                        y2: 0,
                        colorStops: [{
                          offset: 0, color: this.item.DScatterColor4[i % this.item.DScatterColor4.length][0] // 0% 处的颜色
                        }, {
                          offset: 1, color: this.item.DScatterColor4[i % this.item.DScatterColor4.length][1] // 100% 处的颜色
                        }],
                        global: false // 缺省为 false
                      } : this.item.ScatterColor4[i % this.item.DScatterColor4.length]
                    }
                  }
                })
              })
            } else {
              myseries.push({
                name: myData.columns[index],
                data: data,
                type: 'bar',
                barWidth: this.item.barWidth4,
                itemStyle: {
                  normal: {
                    color: this.item.ifGradual === 'true' ? this.item.gradientDirection4 === '1' ? {
                      type: 'linear',
                      x: 0,
                      y: 0,
                      x2: 0,
                      y2: 1,
                      colorStops: [{
                        offset: 0, color: this.item.DScatterColor4[index - 1][0] // 0% 处的颜色
                      }, {
                        offset: 1, color: this.item.DScatterColor4[index - 1][1] // 100% 处的颜色
                      }],
                      global: false // 缺省为 false
                    } : {
                      type: 'linear',
                      x: 0,
                      y: 0,
                      x2: 1,
                      y2: 0,
                      colorStops: [{
                        offset: 0, color: this.item.DScatterColor4[index - 1][0] // 0% 处的颜色
                      }, {
                        offset: 1, color: this.item.DScatterColor4[index - 1][1] // 100% 处的颜色
                      }],
                      global: false // 缺省为 false
                    } : this.item.ScatterColor4[index - 1]
                  }
                }
              })
            }
          }
        })
        let myoption = {
          yAxis: {
            type: 'category',
            name: this.item.chartData4.unitX,
            nameTextStyle: {
              color: this.item.DanweiColor4 || '#828bac',
              fontSize: this.item.DanweiSize4 || 16
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
                color: this.item.splitColor4 || '#333849', // 坐标轴颜色
                width: this.item.splitSize4 || 1
              }
            },
            splitLine: {
              show: false
            },
            axisLabel: {
              rotate: this.item.rotate4 || 0,
              textStyle: {
                color: this.item.legendColor4 || '#828bac',
                fontSize: this.item.axisLabelSize4 || '14'
              },
              formatter: (params, index) => {
                var rows = this.item.chartData4.rows
                let barW = Math.floor((this.item.width - 60) * 0.7 / rows.length)
                let strLen = Math.round(barW / (this.item.axisLabelSize4 * 2))
                if (this.item.formatterType4 === '0') {
                  return params.length > strLen ? params.substr(0, strLen) + '...' : params
                } else {
                  return params
                }
              },
              interval: 'auto' // auto 采用不重叠的方式展示，具体数字n则为间隔n展示
            }
          },
          xAxis: {
            type: 'value',
            name: this.item.chartData4.unit,
            nameTextStyle: {
              color: this.item.DanweiColor4 || '#828bac',
              fontSize: this.item.DanweiSize4 || 16
            },
            minInterval: this.item.minInterval4,
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
                color: this.item.splitColor4 || '#333849', // 坐标轴颜色
                width: this.item.splitSize4 || 1
              }
            },
            splitLine: {
              show: this.item.splitShow4,
              lineStyle: {
                color: this.item.splitColor4 || '#333849', // 修改网格线颜色
                width: this.item.splitSize4 || 1
              }
            },
            axisLabel: {
              interval: 'auto', // 采用不重叠的方式展示
              textStyle: {
                color: this.item.legendColor4 || '#828bac',
                fontSize: this.item.axisLabelSize4 || '14'
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
            y: this.item.legendY4 + '%',
            show: this.item.ctLegendShow4,
            textStyle: {
              fontSize: this.item.ctLegendSize4,
              color: this.item.ctLegendColor4
            }
          },
          grid: {
            left: this.item.gridLeft4 + '%',
            right: this.item.gridRight4 + '%',
            top: this.item.gridTop4 + '%',
            bottom: this.item.gridBotton4 + '%',
            containLabel: true
          },
          tooltip: {
            trigger: 'axis',
            show: this.item.tooltipShow4,
            borderWidth: 0,
            backgroundColor: this.item.tooltipBackColor4,
            textStyle: {
              color: this.item.tooltipTextColor4,
              fontSize: this.item.tooltipfontSize4
            },
            formatter: (params, index) => {
              var value = ''
              params.forEach(element => {
                if (element.value !== 0) {
                  value = element.axisValue + '<br>' + element.seriesName + ':' + element.value
                }
              })
              return value
            }
          },
          series: myseries
        }
        let rows = this.item.chartData4.rows
        let barW = Math.floor((this.item.width - 60) * 0.7 / rows.length)
        let strLen = Math.round(barW / (this.item.axisLabelSize4 * 2))
        if (this.item.formatterType4 === '0' && this.oldformatterType !== this.item.formatterType4) {
          myoption.xAxis.axisLabel.formatter = function (params, index) {
            return params.length > strLen ? params.substr(0, strLen) + '...' : params
          }
          this.oldformatterType = this.item.formatterType4
          this.mychart.clear()
          this.mychart.setOption(myoption, true)
        } else if (this.oldformatterType !== this.item.formatterType4) {
          myoption.xAxis.axisLabel.formatter = function (params, index) {
            return params
          }
          this.oldformatterType = this.item.formatterType4
          this.mychart.clear()
          this.mychart.setOption(myoption, true)
        }
        if (this.item.colorful4 !== this.Oldcolorful) {
          this.Oldcolorful = this.item.colorful4
          this.mychart.clear()
          this.mychart.setOption(myoption, true)
        }
        if (this.oldOption !== JSON.stringify(myoption)) {
          this.oldOption = JSON.stringify(myoption)
          this.mychart.setOption(myoption, true)
        } else {

        }
      }
    }

  },
  mounted () {
    this.drawFlow()
  },
  beforeDestroy () {
    this.mychart.dispose()
    this.mychart = null
    if (this.myInterVale){
      clearInterval(this.myInterVale)
    }
  }

}
</script>
