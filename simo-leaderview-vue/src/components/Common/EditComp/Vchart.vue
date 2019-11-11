<template>
  <component :is="item.chartType"
             :data="dealChartData"
             :width="comWidth"
             v-if="initOption"
             :init-options="initOption"
             :height="comHeight"
             :settings="settings"
             :extend="extend"
             :key="keyId"
             :judge-width="true">
    <div class="v-charts-data-empty"
         v-if="empty"
         style="width: 100%; height: 100%; text-align: center; font-size: 12px;">
      <div><i class="icon-n-nodata"
           style="font-size: 108px;"></i><br>
        <p>抱歉，没有数据可供展示...</p>
      </div>
    </div>
  </component>
</template>
<script>
export default {
  name: 'vchart',
  props: ['item'],
  data: function () {
    // 这里没有直接定义，所以component里调用了data属性的都会先报错，但不会影响页面渲染，后续需要改进
    var obj = {}
    if (this.item.chartType.indexOf('ve-') !== -1) { // v-chart
      var setings = {
        grid: {
          left: '10%',
          right: 10,
          top: 10,
          bottom: 10
        },
        axis: {
          splitLine: {
            show: false
          },
          axisLine: {
            show: false,
            lineStyle: {
              color: '#B4B4B4'
            }
          },
          axisTick: {
            show: false
          }
        }
      }
      obj = {
        empty: false,
        keyId: new Date().getTime() + Math.random() * 10000,
        initOption: { renderer: this.item.chartType === 've-line' ? 'canvas' : 'svg' },
        settings: {
          yAxisType: [0]
        },
        extend: {
          // grid:setings.grid,
          title: {
            show: false,
            // text:this.item.ctName,
            textStyle: {
              color: '#c2c6d7',
              fontFamily: 'SourceHanSansCN-Regular',
              fontWeight: 'normal',
              fontSize: 14
            }
          },
          // label: {
          //   show: this.item.showPoint === 'true'
          // },
          legend: {
            // orient : 'vertical', //横向、纵向
            x: 'center',
            y: 'bottom',
            show: this.item.chartType === 've-gauge' ? false : this.item.ctLegendShow === 'true',
            textStyle: {
              color: '#666f8b'
            }
          },
          // color:this.item.ctColors,
          color: this.getColors(this.item.ctColors),
          textStyle: {
            color: '#666f8b'
          },
          xAxis: $.extend({}, setings.axis, {
            position: 'bottom'
          }),
          yAxis: $.extend({}, setings.axis, {
            position: 'left'
          })
        }
      }
      this.dealCompsData(obj)
    }
    return obj
  },
  computed: {
    comWidth: function () {
      return this.item.width + 'px'
    },
    comHeight: function () {
      return this.item.height + 'px'
    },
    comStyle: function () {
      return {
        width: this.item.width + 'px',
        height: this.item.height + 'px'
      }
    },
    dealChartData: function () {
      // if(this.item.chartType === 've-bar' && this.item.subType === 'category') {
      // 弧形柱图
      //     return []
      // }
      var d = this.item.chartData
      // if (!_.isObject(d) && !_.isArray(d)) {
      //   this.empty = true
      //   return {}
      // }
      // this.empty = false
      if (this.item.chartType === 've-gauge' && typeof d.value !== 'undefined') {
        var outer = { name: 'outerpro' }
        var rows = [
          { name: 'p' },
          { name: 'pro', value: d.value, unit: d.unit, dataName: d.name }
        ]
        if (this.item.subType !== 'progress') {
          rows.push(outer)
        }
        return {
          columns: ['name', 'value', 'unit'],
          rows: rows
        }
      }
      return this.item.chartData
    }
  },
  watch: {
    'item.ctName': function (newV, oldValue) {
      this.extend.title.text = newV
    },
    'item.fontSize': function (newV) {
      if (this.item.subType === 'progress') {
        this.settings.seriesMap.pro.detail.fontSize = newV
      }
    },
    'item.width': function (newV, oldValue) {
      if (this.item.chartType === 've-histogram') {
        var barW = Math.floor((newV - 60) * 0.7 / this.item.chartData.rows.length)
        var strLen = Math.round(barW / 10)
        this.extend.xAxis.axisLabel.formatter = function (params, index) {
          return params.length > strLen ? params.substr(0, strLen) + '...' : params
        }
      }
    },
    'item.ctLegendShow': function (newV) {
      if (this.item.subType === 'progress') {
        if (newV === 'true') {
          this.settings.dataName.p = this.item.chartData.name
        } else {
          this.settings.dataName.p = ''
        }
      } else {
        this.extend.legend.show = newV === 'true'
      }
    },
    'item.lineArea': function (newV, oldValue) {
      this.settings.area = newV === 'true'
    },
    'item.showPoint': function (newV, oldValue) {
      this.extend.label.show = newV === 'true'
      this.keyId = new Date().getTime() // 强制更新视图
    },
    'item.bgClr': {
      handler: function (newV) {
        if (this.item.chartType === 've-gauge') {
          if (!newV) {
            newV = '#657992'
          }
          if (this.item.subType === 'progress') {
            this.settings.seriesMap.p.axisLine.lineStyle.color[0].splice(1, 1, newV)
          } else {
            this.settings.seriesMap.p.axisLine.lineStyle.color[0].splice(1, 1, newV)
            this.settings.seriesMap.outerpro.axisLine.lineStyle.color[1].splice(1, 1, newV)
          }
        }
      },
      deep: true
    },
    'item.ctColors': function (newV) {
      if (this.item.chartType === 've-gauge') {
        this.setGaugeColor('pro', newV)
        if (this.item.subType !== 'progress') {
          this.setGaugeColor('outerpro', newV)
        }
      }
      // this.extend.color = newV
      this.extend.color = this.getColors(newV)
    },
    'item.colorful': function (newV) {
      var _this = this
      // 页面中判断了只有条形图和柱状图才会触发改变
      if (newV === 'true') {
        this.extend.series.itemStyle.normal.color = function (params) {
          var colorList = _this.extend.color
          var len = colorList.length
          return colorList[params.dataIndex % len]
        }
      } else {
        this.extend.series.itemStyle.normal.color = null
      }
    },
    'item.chartData': function (newV) {
      if (this.item.chartType === 've-gauge') {
        if (newV.hasOwnProperty('value') && (newV.value || newV.value === 0)) {
          this.empty = false
        } else {
          this.empty = true
          delete newV.value // value为null时
        }
      } else if (newV.rows && newV.rows.length > 0) {
        this.empty = false
      } else {
        this.empty = true
      }
      if (this.item.chartType === 've-histogram') {
        var barW = Math.floor((this.item.width - 60) * 0.7 / newV.rows.length)
        var strLen = Math.round(barW / 10)
        this.extend.xAxis.axisLabel.formatter = function (params, index) {
          return params.length > strLen ? params.substr(0, strLen) + '...' : params
        }
      }
      //  && _.isObject(newV) && !_.isArray(newV)
      if (this.item.chartType === 've-gauge') {
        this.settings.dataName.p = newV.name
        this.settings.seriesMap.pro.detail.formatter = '{value}' + newV.unit
        this.settings.seriesMap.pro.axisLine.lineStyle.color[0][0] = newV.value / 100
        if (this.item.subType !== 'progress') {
          this.settings.seriesMap.outerpro.axisLine.lineStyle.color[0][0] = newV.value / 100
        }
        return
      }
      if (this.item.chartType === 've-line') {
        this.extend.yAxis.name = newV.unit
        if (newV.unit === '%') {
          this.extend.yAxis.max = this.getYaxiosMax(newV)
        } else {
          this.extend.yAxis.max = null
        }
      }
      if (this.item.chartType === 've-pie' || this.item.chartType === 've-ring' || this.item.chartType === 've-radar') {
        this.extend.color = this.getColors(this.item.ctColors)
      }
    },
    'item.symbolImg': function (newV) {
      if (this.item.secondType === 'symbolBar') {
        this.extend.series.symbol = 'image://' + newV
      }
    }
  },
  beforeMount: function () {
    if (this.item.chartData && this.item.chartData.rows && this.item.chartData.rows.length === 0) {
      this.empty = true
    }
  },
  methods: {
    getYaxiosMax: function (obj) {
      var rowData = obj.rows
      var maxData = 0
      for (let i = 1, len = obj.columns.length; i < len; i++) {
        var key = obj.columns[i]
        var maxItem = _.maxBy(rowData, function (item) { return item[key] })
        if (maxItem[key] > maxData) {
          maxData = maxItem[key]
        }
      }
      return (parseInt(maxData / 100) * 100 + 100)
    },
    getColors: function (tempArr) {
      if (this.item.chartType === 've-pie' || this.item.chartType === 've-ring') {
        if (this.item.chartData.rows && this.item.chartData.rows.length === 1 && this.item.chartData.columns) {
          // 判断是不是只有一个为0的数据
          let key = this.item.chartData.columns[1]
          let firstVal = this.item.chartData.rows[0][key]
          if (!firstVal || firstVal === '0') {
            return 'rgba(51, 57, 75, 0.62)'
          }
        }
      }
      if (this.item.chartType === 've-radar') {
        if (this.item.chartData.rows && this.item.chartData.rows.length === 1 && this.item.chartData.columns) {
          let keys = this.item.chartData.columns.slice(1)
          let flag = 0
          keys.forEach((item) => {
            let _val = this.item.chartData.rows[0][item]
            if (!_val || _val === '0') {
              flag++
            }
          })
          if (flag === keys.length) {
            return 'rgba(51, 57, 75, 0.62)'
          }
        }
      }
      var type = this.item.chartType
      // LinearGradient: 右 下 左 上
      var direct = [0, 0, 1, 0]
      if (type === 've-histogram') {
        direct = [0, 1, 0, 0]
      } else if (type === 've-pie') {
        direct = [0, 0, 1, 1]
      } else if (type === 've-line') {
        direct = [0, 1, 0, 0]
      }
      if (Array.isArray(tempArr[0])) {
        var colorArr = []
        for (var i = 0, len = tempArr.length; i < len; i++) {
          var tempColor = new echarts.graphic.LinearGradient(direct[0], direct[1], direct[2], direct[3], [{ offset: 0, color: tempArr[i][0] }, { offset: 1, color: tempArr[i][1] }])
          // 径向： new echarts.graphic.RadialGradient(0.5, 0.5, 0.5, [{ offset: 0, color: tempArr[i][0] }, { offset: 1, color: tempArr[i][1]}])
          colorArr.push(tempColor)
        }
        return colorArr
      } else {
        return tempArr // 默认配色
      }
    },
    setGaugeColor: function (type, newV) {
      if (Array.isArray(newV[0])) {
        this.settings.seriesMap[type].axisLine.lineStyle.color[0][1] = new echarts.graphic.LinearGradient(1, 0, 0, 0, [{ offset: 0, color: newV[0][0] }, { offset: 1, color: newV[0][1] }])
        this.settings.seriesMap[type].detail.color = newV[0][0]
      } else {
        this.settings.seriesMap[type].axisLine.lineStyle.color[0][1] = newV[0]
        this.settings.seriesMap[type].detail.color = newV[0]
      }
    },
    dealCompsData: function (obj) {
      var _this = this
      var Fn = {
        've-bar': function () {
          obj.settings.xAxisType = [0]
          obj.extend = $.extend(obj.extend, {
            series: {
              type: 'bar',
              /* barWidth:'35%', */
              barGap: '20%',
              barCategoryGap: '50%',
              itemStyle: {
                normal: {
                  color: _this.item.colorful && _this.item.colorful === 'true' ? function (params) {
                    var colorList = _this.extend.color
                    var len = colorList.length
                    return colorList[params.dataIndex % len]
                  } : null
                }
              }
            }
          })
          if (_this.item.subType === 'category') {
            // 弧形柱图
            obj.extend = $.extend(obj.extend, {
              angleAxis: {
              },
              radiusAxis: {
                type: 'category',
                data: ['周一', '周二', '周三', '周四'],
                z: 10
              },
              polar: {
              },
              series: [{
                type: 'bar',
                data: [1, 2, 3, 4],
                coordinateSystem: 'polar',
                name: 'A',
                stack: 'a'
              }],
              legend: {
                show: true,
                data: ['A']
              }
            })
          }
        },
        've-histogram': function () {
          //  if(_this.item.subType === 'groupHistogram'){
          //      // 分组柱状图
          //  }
          var barW = Math.floor((_this.item.width - 60) * 0.7 / _this.item.chartData.rows.length)
          var strLen = Math.round(barW / 10)
          obj.extend = $.extend(obj.extend, {
            xAxis: {
              axisLabel: {
                interval: 0,
                showMinLabel: true,
                showMaxLabel: true,
                formatter: function (params, index) {
                  return params.length > strLen ? params.substr(0, strLen) + '...' : params
                }
              }
            }
          })
          if (_this.item.subType === 'pictorialBar') {
            // 象形柱状图
            if (_this.item.secondType === 'symbolBar') {
              obj.extend = $.extend(obj.extend, {
                series: {
                  type: 'pictorialBar',
                  symbol: _this.item.symbolImg ? 'image://' + _this.item.symbolImg : 'image://http://ico.58pic.com/iconset01/emotes-icons/gif/117937.gif',
                  symbolRepeat: true,
                  symbolMargin: '10%',
                  symbolClip: false,
                  symbolSize: 30,
                  barGap: '20%',
                  barCategoryGap: '50%'
                }
              })
            } else if (_this.item.secondType === 'peakBar') {
              // 山峰柱状图
              obj.extend = $.extend(obj.extend, {
                series: {
                  type: 'pictorialBar',
                  barCategoryGap: '-130%',
                  symbol: 'path://M0,10 L10,10 C5.5,10 5.5,5 5,0 C4.5,5 4.5,10 0,10 z',
                  itemStyle: {
                    normal: {
                      opacity: 0.5
                    },
                    emphasis: {
                      opacity: 1
                    }
                  },
                  z: 10
                },
                yAxis: {
                  axisLabel: { show: false },
                  splitLine: { show: false }
                }
              })
            }
          } else {
            obj.extend = $.extend(obj.extend, {
              series: {
                type: 'bar',
                barGap: '20%',
                barCategoryGap: '50%',
                itemStyle: {
                  normal: {
                    color: _this.item.colorful && _this.item.colorful === 'true' ? function (params) {
                      var colorList = _this.extend.color
                      var len = colorList.length
                      return colorList[params.dataIndex % len]
                    } : null
                  }
                }
                // barWidth:'35%'
              }
            })
          }
        },
        've-line': function () {
          obj.settings.area = _this.item.lineArea === 'true'
          // obj.settings.xAxisType = 'time'
          obj.extend = $.extend(obj.extend, {
            series: {
              type: 'line',
              showAllSymbol: false
            },
            label: {
              show: _this.item.showPoint === 'true'
            },
            yAxis: {
              type: 'value',
              position: 'left',
              name: _this.item.chartData.unit, // 单位
              max: _this.item.chartData.unit === '%' ? _this.getYaxiosMax(_this.item.chartData) : null,
              axisTick: {
                show: true,
                lineStyle: {
                  color: '#333849', // 坐标轴刻度
                  width: 1
                }
              },
              axisLine: {
                show: true,
                lineStyle: {
                  color: '#333849', // 坐标轴颜色
                  width: 1
                }
              },
              splitLine: {
                show: false,
                lineStyle: {
                  color: '#333849', // 修改网格线颜色
                  width: 1
                }
              },
              axisLabel: {
                interval: 'auto' // 采用不重叠的方式展示
              }
            },
            xAxis: {
              type: 'category',
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
                  color: '#333849', // 坐标轴颜色
                  width: 1
                }
              },
              splitLine: {
                show: false
              },
              axisLabel: {
                interval: 'auto' // auto 采用不重叠的方式展示，具体数字n则为间隔n展示
              }
            },
            tooltip: {
              trigger: 'axis',
              formatter: function (params, ticket, callback) {
                var nameArr = []
                var time = params[0].data[0]
                var showHtm = time + '<br>'
                params.forEach(element => {
                  var name = element.seriesName
                  if (nameArr.indexOf(name) === -1) {
                    nameArr.push(name)
                    var value = element.data[1]
                    if (typeof (value) !== 'number' || value !== value) {
                      value = '--'
                    }
                    showHtm += name + '：' + value + (_this.item.chartData.unit || '') + '<br>'
                  }
                })
                return showHtm
              }
            }
          })
        },
        've-ring': function () {
          obj.settings = $.extend(obj.settings, {
            radius: ['40%', '50%']
          })
          obj.extend = $.extend(obj.extend, {
            series: {
              center: ['50%', '45%']
            }
          })
        },
        've-pie': function () {
          if (_this.item.roseType) {
            obj.settings = {
              roseType: _this.item.roseType
            }
          }
          obj.extend = $.extend(obj.extend, {
            series: {
              center: ['50%', '45%'],
              radius: [0, '55%']
            }
          })
        },
        've-gauge': function () {
          if (Array.isArray(_this.item.ctColors[0])) {
            var fontColor = _this.item.ctColors[0][0]
            var color = new echarts.graphic.LinearGradient(1, 0, 0, 0, [{ offset: 0, color: _this.item.ctColors[0][0] }, { offset: 1, color: _this.item.ctColors[0][1] }])
          } else {
            color = _this.item.ctColors[0]
            fontColor = _this.item.ctColors[0]
          }
          var data = _this.item.chartData
          if (data.rows) {
            data.name = data.rows[1].dataName
            data.unit = data.rows[1].unit
            data.value = data.rows[1].value
          }
          if (_this.item.subType === 'progress') {
            // 目标占比图
            obj.settings = {
              dataName: {
                'p': _this.item.ctLegendShow === 'true' ? data.name : ''
              },
              seriesMap: {
                'p': {
                  startAngle: 90,
                  endAngle: -269.9999,
                  center: ['50%', '50%'],
                  // radius: '50%',
                  axisLine: {
                    lineStyle: { //  属性lineStyle控制线条样式
                      width: 12,
                      color: [
                        [1, _this.item.bgClr || '#657992']
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
                    offsetCenter: [0, '120%'], //  x,  y，单位px
                    textStyle: {
                      color: '#666f8b',
                      fontSize: 14
                    }
                  },
                  detail: {
                    show: false
                  }
                },
                'pro': {
                  startAngle: 90,
                  endAngle: -269.9999,
                  center: ['50%', '50%'],
                  // radius: '50%',
                  axisLine: {
                    lineStyle: {
                      width: 12,
                      color: [
                        [data.value / 100, color]
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
                    offsetCenter: [0, 8],
                    fontSize: 24,
                    color: fontColor,
                    formatter: '{value}' + data.unit
                  }
                }
              }
            }
          } else {
            obj.settings = {
              dataName: {
                'p': data.name
              },
              seriesMap: {
                'p': {
                  startAngle: 200,
                  endAngle: -20,
                  center: ['50%', '50%'],
                  axisLine: {
                    lineStyle: { //  属性lineStyle控制线条样式
                      width: 4,
                      color: [
                        [1, _this.item.bgClr || '#657992']
                      ]
                    }
                  },
                  axisTick: {
                    show: false
                  },
                  axisLabel: {
                    show: false
                  },
                  splitLine: {
                    show: false
                  },
                  pointer: {
                    show: false
                  },
                  title: {
                    offsetCenter: [0, '60%'], //  x,  y，单位px
                    textStyle: {
                      color: '#666f8b',
                      fontSize: 14
                    }
                  },
                  detail: {
                    show: false
                  }
                },
                'pro': {
                  startAngle: 200,
                  endAngle: -20,
                  center: ['50%', '50%'],
                  axisLine: {
                    lineStyle: { // 属性lineStyle控制线条样式
                      width: 4,
                      color: [
                        [data.value / 100, color]
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
                    color: fontColor,
                    formatter: '{value}' + data.unit
                  }
                },
                'outerpro': {
                  radius: '65%',
                  startAngle: 201,
                  endAngle: -20,
                  center: ['50%', '50%'],
                  min: 0,
                  max: 100,
                  axisLine: {
                    lineStyle: { // 属性lineStyle控制线条样式
                      width: 0,
                      shadowBlur: 0,
                      color: [
                        [data.value / 100, color],
                        [1, _this.item.bgClr]
                      ]
                    }
                  },
                  splitLine: {
                    show: false

                  },
                  axisTick: {
                    show: true,
                    length: 2.5,
                    splitNumber: 3,
                    lineStyle: {
                      color: 'auto',
                      width: 2.5
                    }
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
                    show: false
                  }
                }
              }
            }
          }
        },
        've-radar': function () {
          obj.extend = $.extend(obj.extend, {
            radar: {
              shape: 'polygon',
              splitNumber: 1,
              splitArea: {
                areaStyle: {
                  color: 'transparent'
                }
              },
              splitLine: {
                lineStyle: {
                  color: '#757c89',
                  opacity: 0.2
                }
              },
              axisLabel: {
                show: false
              },
              axisLine: {
                show: false
              }
            },
            series: {
              itemStyle: { normal: { areaStyle: { type: 'default' } } },
              symbolSize: 0
            }
          })
        },
        've-funnel': function () {
          obj.extend = $.extend(obj.extend, {
            series: {
              type: 'funnel',
              sort: 'descending',
              gap: 1
            }
          })
        },
        've-map': function () {
          if (_this.item.secondType === 'scatter') {
            obj.settings = $.extend(obj.settings, {
              // position: 'province/sichuan',
              position: 'china',
              dimension: '位置',
              metrics: ['资源', '告警'],
              dataType: {
                '资源': 'KMB'
              }
            })
            obj.extend = $.extend(obj.extend, {
              tooltip: {
                trigger: 'item'
              },
              visualMap: {
                type: 'piecewise', // 分段显示值
                realtime: false,
                calculable: true,
                // backgroundColor: 'gray' // 柱状框的背景色
                inRange: {
                  color: ['pink', 'yellow', '#dd7e6b'] // 按照值的范围给的不同颜色
                },
                outOfRange: {
                  // color: ['pink', 'yellow', 'orange']
                },
                // piecewise分段设置 https://echarts.apache.org/zh/option.html#visualMap-piecewise.pieces
                splitNumber: 3, // 几种颜色值及取值范围
                pieces: [
                  { min: 1000, label: '自定义', color: 'orange' },
                  { min: 100, max: 999 },
                  { max: 99 }
                ], // 默认取data中最后一个维度
                color: ['#E0022B', '#E09107', '#A3E00B'],
                // itemSymbol: 'none',
                show: true, // 是否显示取值范围颜色段
                hoverLink: true,
                // showMaxLabel: true,
                showLabel: true,
                textStyle: {
                  color: '#fff'
                },
                controller: {
                }
              },
              series: {
                type: 'scatter',
                // name: '人口',
                // roam: true, // 允许鼠标缩放地图
                selectedMode: 'single',
                // 图形上的文本标签
                itemStyle: {
                  normal: {
                    areaColor: '#1d324e', // 地图区域的颜色!
                    borderColor: '#f0f0f0' // 区域分割线颜色!
                  }
                },
                // 选中之后的状态
                emphasis: {
                  label: {
                    show: true,
                    textStyle: {
                      color: '#000' // 选中之后的字体颜色!
                    }
                  },
                  itemStyle: {
                    areaColor: '#0573bf', // 选中之后的颜色值
                    shadowColor: 'rgba(0, 0, 0, 0.5)',
                    shadowBlur: 2
                  }
                }
              }
            })
          } else {
            // 区域分布图
            // obj.settings = $.extend(obj.settings, {
            //   // position: 'province/sichuan',
            //   // position: 'china',
            //   dimension: '位置',
            //   metrics: ['资源', '告警'],
            //   dataType: {
            //     '资源': 'KMB'
            //   }
            // })

            obj.extend = $.extend(obj.extend, {
              tooltip: {
                trigger: 'item'
              },
              visualMap: {
                type: 'piecewise', // 分段显示值
                realtime: false,
                calculable: true,
                // backgroundColor: 'gray' // 柱状框的背景色
                inRange: {
                  color: ['pink', 'yellow', '#dd7e6b'] // 按照值的范围给的不同颜色
                },
                outOfRange: {
                  // color: ['pink', 'yellow', 'orange']
                },
                // piecewise分段设置 https://echarts.apache.org/zh/option.html#visualMap-piecewise.pieces
                splitNumber: 3, // 几种颜色值及取值范围
                pieces: [
                  { min: 1000, label: '自定义', color: 'orange' },
                  { min: 100, max: 999 },
                  { max: 99 }
                ], // 默认取data中最后一个维度
                color: ['#E0022B', '#E09107', '#A3E00B'],
                // itemSymbol: 'none',
                show: true, // 是否显示取值范围颜色段
                hoverLink: true,
                // showMaxLabel: true,
                // text: ['高', '低'], // 文本，默认为数值文本
                showLabel: true,
                textStyle: {
                  color: '#fff'
                },
                controller: {
                }
              },
              series: {
                type: 'map',
                name: '',
                data: [],
                // name: '人口',
                // roam: true, // 允许鼠标缩放地图
                selectedMode: 'single',
                // 图形上的文本标签
                label: {
                  normal: {
                    // show: true, // 省份文字最开始不显示，选中之后再显示
                    textStyle: {
                      // fontWeight:'bold',
                      // backgroundColor: 'pink', // 文字背景色
                      // color: '#231816' // 默认的字体颜色! auto
                    }
                  }
                },
                itemStyle: {
                  normal: {
                    // color: 'red', // 展示指标及圆点的颜色
                    areaColor: '#294671', // 地图区域的颜色!
                    borderColor: '#f0f0f0' // 区域分割线颜色!
                    // color: 'green', // 图例的颜色!
                    // borderColor: 'pink', // 各区域分界线!
                    // borderWidth: 2
                  }
                },
                // 选中之后的状态
                emphasis: {
                  label: {
                    show: false, // 选中区域的文字展示
                    textStyle: {
                      color: '#000' // 选中之后的字体颜色!
                    }
                  },
                  itemStyle: {
                    areaColor: '#0573bf', // 选中之后的颜色值
                    shadowColor: 'rgba(0, 0, 0, 0.5)',
                    shadowBlur: 0
                  }
                }
              }
            })
          }
        },
        've-scatter': function () {
          obj.settings = $.extend(obj.settings, {
            // position: 'province/sichuan',
            // position: 'china',
            // type: 'map', // 散点图
            geo: {
              type: 'scatter',
              map: 'china',
              label: {
                emphasis: {
                  show: false
                }
              },
              itemStyle: {
                normal: {
                  areaColor: '#323c48',
                  borderColor: '#111'
                },
                emphasis: {
                  areaColor: '#2a333d'
                }
              }
            }
          })
          obj.extend = {
            series: {
              type: 'scatter'
            }
          }
        }
      }
      typeof Fn[this.item.chartType] === 'function' && Fn[this.item.chartType]()
    }
  },
  beforeDestroy: function () {
    // 销毁echarts
    var _echarts = $(this.$el).find('div')[0]
    var instance = $(_echarts).attr('_echarts_instance_')
    if (instance) {
      // var chart = echarts.getInstanceById(instance) // 不知道什么原因，这里获取不到对象，后续需要解决
      // chart.dispose() // 销毁
    }
  },
  destroyed: function () {
  }
}
</script>
<style>
.v-charts-data-empty {
  position: absolute !important;
  top: 0px !important;
  background-color: transparent !important;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
}
.v-charts-data-empty i,
.v-charts-data-empty p {
  color: #666f8b;
}
</style>
