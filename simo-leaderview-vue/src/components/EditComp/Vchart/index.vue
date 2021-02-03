<template>
  <div>
    <div v-if="item.chartData.tabs" class="tab_btn_box">
      <button v-for="tab in item.chartData.tabs" :key="tab"
      :class="{'tab_btn': true, 'tab_active_btn': activeTab == tab}"
      :style="btnStyle"
      @click="changeActiveTab(tab)">{{ tab }}</button></div>
      <component :is="item.chartType"
             :data="dealChartData"
             :width="comWidth"
             v-if="initOption"
             :init-options="initOption"
             :height="comHeight"
             :settings="settings"
             :extend="extend"
             :key="keyId">
      <div class="v-charts-data-empty"
          v-if="empty"
          style="width: 100%; height: 100%; text-align: center; font-size: 12px;">
        <div><i class="icon-n-nodata"
            style="font-size: 108px;"></i><br>
          <p>抱歉，没有数据可供展示...</p>
        </div>
      </div>
    </component>
  </div>
</template>
<script>
import echarts from 'echarts'
import { gbs } from '@/config/settings'
import _ from 'lodash'
export default {
  name: 'vchart',
  props: ['item'],
  data: function () {
    // 这里没有直接定义，所以component里调用了data属性的都会先报错，但不会影响页面渲染，后续需要改进
    let obj = {}
    if (this.item.chartType.indexOf('ve-') !== -1) { // v-chart
      let setings = {
        grid: {
          left: this.item.gridTop + '%',
          right: this.item.gridTop + '%',
          top: this.item.gridTop + '%',
          bottom: this.item.gridTop + '%'
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
        activeTab: null,
        empty: false,
        keyId: new Date().getTime() + Math.random() * 10000,
        initOption: { renderer: this.item.chartType === 've-line' ? 'canvas' : 'svg' },
        settings: {
          yAxisType: [0]
        },
        extend: {
          grid: setings.grid,
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
            y: this.item.legendY + '%',
            show: this.item.chartType === 've-gauge' ? false : this.item.ctLegendShow === 'true',
            textStyle: {
              fontSize: this.item.ctLegendSize,
              color: this.item.ctLegendColor
            }
          },
          // color:this.item.ctColors,
          color: this.getColors(this.item.ctColors),
          textStyle: {
            color: this.item.legendColor || '#666f8b'
          },
          xAxis: $.extend({}, setings.axis, {
            position: 'bottom'
          }),
          yAxis: $.extend({}, setings.axis, {
            position: 'left'
          }),
          tooltip: {
            show: this.item.tooltipShow === 'true',
            backgroundColor: this.item.tooltipBackColor,
            textStyle: {
              color: this.item.tooltipTextColor,
              fontSize: this.item.tooltipfontSize
            }
          },
          animation: true,
          animationDuration: 500
        }
      }
      this.dealCompsData(obj)
    }
    return obj
  },
  computed: {
    btnStyle () {
      let style = {}
      if (this.item.labelFontSize) {
        const size = this.item.labelFontSize
        // console.log(size)
        style = {
          // color: 'red',
          'font-size': `${size}px`,
          height: `${size * 2}px !important`
        }
      }
      return style
    },
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
      let d = this.item.chartData
      if (['ve-line', 've-bar', 've-histogram'].includes(this.item.chartType) && this.item.chartData.allData) {
        // console.log(this.activeTab)
        return this.item.chartData.allData[this.activeTab]
      }
      if (this.item.chartType === 've-gauge' && typeof d.value !== 'undefined') {
        let outer = { name: 'outerpro' }
        let rows = [
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
      d = null
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
    'item.detailColor': function (newV) {
      this.settings.seriesMap.pro.detail.color = newV
    },
    'item.gridTop': function (newV) {
      this.extend.grid.top = newV + '%'
      this.extend.grid.bottom = newV + '%'
      this.extend.grid.left = newV + '%'
      this.extend.grid.right = newV + '%'
    },
    'item.width': function (newV, oldValue) {
      if (this.item.chartType === 've-histogram') {
        let barW = Math.floor((newV - 60) * 0.7 / this.dealChartData.rows.length)
        let strLen = Math.ceil(barW / 10)
        this.extend.xAxis.axisLabel.formatter = function (params, index) {
          return params.length > strLen ? params.substr(0, strLen) + '...' : params
        }
      }
      if (this.item.chartType === 've-bar') {
        this.extend.xAxis.axisLabel.formatter = function (params, index) {
          if (newV === '0') {
            return params.length > 5 ? params.substr(0, 5) + '...' : params
          } else {
            return params
          }
        }
      } else {
        var rows = this.item.chartData.rows
        let barW = Math.floor((this.item.width - this.item.width * this.item.gridTop / 50) * 0.7 / rows.length)
        let strLen = Math.ceil(barW / (this.item.axisLabelSize * 2))
        this.extend.xAxis.axisLabel.formatter = (params, index) => {
          if (this.item.formatterType === '0') {
            return params.length > strLen ? params.substr(0, strLen) + '...' : params
          } else {
            return params
          }
        }
      }
    },
    'item.splitShow': function (newV) {
      if (this.item.chartType === 've-bar') {
        this.extend.xAxis.splitLine.show = newV === 'true'
      } else if (this.item.chartType === 've-line' || this.item.chartType === 've-histogram') {
        this.extend.yAxis.splitLine.show = newV === 'true'
      } else if (this.item.chartType === 've-radar') {
        this.extend.radar.splitLine.show = newV === 'true'
      }
    },
    'item.DanweiColor': function (newV) {
      if (this.item.chartType === 've-bar') {
        this.extend.xAxis.nameTextStyle.color = newV
      } else {
        this.extend.yAxis.nameTextStyle.color = newV
      }
    },
    'item.minInterval': function (newV) {
      if (this.item.chartType === 've-bar') {
        this.extend.xAxis.minInterval = newV
      } else {
        this.extend.yAxis.minInterval = newV
      }
    },
    'item.DanweiSize': function (newV) {
      if (this.item.chartType === 've-bar') {
        this.extend.xAxis.nameTextStyle.fontSize = newV
      } else {
        this.extend.yAxis.nameTextStyle.fontSize = newV
      }
    },
    'item.splitColor': function (newV) {
      if (this.item.chartType === 've-bar') {
        this.extend.xAxis.splitLine.lineStyle.color = newV
      } else if (this.item.chartType === 've-line' || this.item.chartType === 've-histogram') {
        this.extend.yAxis.splitLine.lineStyle.color = newV
        if (this.item.chartType === 've-line') {
          this.extend.xAxis.axisLine.lineStyle.color = newV
          this.extend.yAxis.axisLine.lineStyle.color = newV
        }
      } else if (this.item.chartType === 've-radar') {
        this.extend.radar.splitLine.lineStyle.color = newV
      }
    },
    'item.splitSize': function (newV) {
      if (this.item.chartType === 've-bar') {
        this.extend.xAxis.splitLine.lineStyle.width = Number(newV)
      } else if (this.item.chartType === 've-line' || this.item.chartType === 've-histogram') {
        this.extend.yAxis.splitLine.lineStyle.width = Number(newV)
        this.extend.yAxis.axisLine.lineStyle.width = Number(newV)
        this.extend.xAxis.axisLine.lineStyle.width = Number(newV)
      } else if (this.item.chartType === 've-radar') {
        this.extend.radar.splitLine.lineStyle.width = Number(newV)
      }
    },
    'item.legendColor': function (newV) {
      if (this.item.chartType === 've-pie' || this.item.chartType === 've-ring' || this.item.chartType === 've-radar') {
        this.extend.textStyle.color = newV
      } else if (this.item.chartType === 've-gauge') {
        this.settings.seriesMap.p.title.textStyle.color = newV
      } else {
        this.extend.xAxis.axisLabel.textStyle.color = newV
        this.extend.yAxis.axisLabel.textStyle.color = newV
        this.extend.yAxis.nameTextStyle.color = newV
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
    'item.legendY': function (newV) {
      this.extend.legend.y = newV + '%'
    },
    'item.ctLegendSize': function (newV) {
      if (this.item.chartType === 've-gauge') {
        this.settings.seriesMap.p.title.textStyle.fontSize = newV
      } else {
        this.extend.legend.textStyle.fontSize = newV
      }
    },
    'item.ctLegendColor': function (newV) {
      if (this.item.chartType === 've-gauge') {

      } else {
        this.extend.legend.textStyle.color = newV
      }
    },
    'item.axisLabelSize': function (newV) {
      this.extend.xAxis.axisLabel.textStyle.fontSize = newV
      this.extend.yAxis.axisLabel.textStyle.fontSize = newV
      if (this.item.chartType === 've-bar') {
        this.extend.xAxis.axisLabel.formatter = function (params, index) {
          if (newV === '0') {
            return params.length > 5 ? params.substr(0, 5) + '...' : params
          } else {
            return params
          }
        }
      } else {
        var rows = this.item.chartData.rows
        let barW = Math.floor((this.item.width - this.item.width * this.item.gridTop / 50) * 0.7 / rows.length)
        let strLen = Math.ceil(barW / (this.item.axisLabelSize * 2))
        this.extend.xAxis.axisLabel.formatter = (params, index) => {
          if (this.item.formatterType === '0') {
            return params.length > strLen ? params.substr(0, strLen) + '...' : params
          } else {
            return params
          }
        }
      }
    },
    'item.smooth': function (newV) {
      this.extend.series.smooth = newV === 'true'
    },
    'item.symbolSrc': function (newV) {
      var reg = /^\/api/
      var baseUrl = ''
      if (!reg.test(this.item.symbolSrc)) {
        baseUrl = gbs.host
      }
      this.extend.series.symbol = `image://${baseUrl}${newV}`
    },
    'item.symbolType': function (newV) {
      this.extend.series.symbol = newV
    },
    'item.symbolSize': function (newV) {
      this.extend.series.symbolSize = newV
    },
    'item.LineType': function (newV) {
      this.extend.series.itemStyle.normal.lineStyle.type = newV
    },
    'item.lineWidth': function (newV) {
      this.extend.series.itemStyle.normal.lineStyle.width = newV
    },
    'item.tooltipShow': function (newV) {
      this.extend.tooltip.show = newV === 'true'
    },
    'item.tooltipBackColor': function (newV) {
      this.extend.tooltip.backgroundColor = newV
    },
    'item.tooltipTextColor': function (newV) {
      this.extend.tooltip.textStyle.color = newV
    },
    'item.tooltipfontSize': function (newV) {
      this.extend.tooltip.textStyle.fontSize = newV
    },
    'item.lineArea': function (newV, oldValue) {
      this.settings.area = newV === 'true'
    },
    'item.showPoint': function (newV, oldValue) {
      this.extend.label.show = newV === 'true'
      this.keyId = new Date().getTime() // 强制更新视图
    },
    'item.PointSize': function (newV) {
      this.extend.label.fontSize = newV * 1
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
    'item.detailwidth': {
      handler: function (newV) {
        if (this.item.chartType === 've-gauge') {
          this.settings.seriesMap.p.axisLine.lineStyle.width = newV
          this.settings.seriesMap.pro.axisLine.lineStyle.width = newV
        } else if (this.item.chartType === 've-ring') {
          let index = 50 - newV + '%'
          this.settings.radius = [index, '50%']
        }
      }
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
      let _this = this
      // 页面中判断了只有条形图和柱状图才会触发改变
      if (newV === 'true') {
        this.extend.series.itemStyle.normal.color = function (params) {
          let colorList = _this.extend.color
          let len = colorList.length
          return colorList[params.dataIndex % len]
        }
      } else {
        this.extend.series.itemStyle.normal.color = null
      }
    },
    'item.formatterType' (newV, oldV) {
      if (this.item.chartType === 've-bar') {
        this.extend.xAxis.axisLabel.formatter = function (params, index) {
          if (newV === '0') {
            return params.length > 5 ? params.substr(0, 5) + '...' : params
          } else {
            return params
          }
        }
      } else {
        var rows = this.item.chartData.rows
        let barW = Math.floor((this.item.width - this.item.width * this.item.gridTop / 50) * 0.7 / rows.length)
        let strLen = Math.ceil(barW / (this.item.axisLabelSize * 2))
        this.extend.xAxis.axisLabel.formatter = (params, index) => {
          if (this.item.formatterType === '0') {
            return params.length > strLen ? params.substr(0, strLen) + '...' : params
          } else {
            return params
          }
        }
      }
    },
    'item.rotate' (newV, oldV) {
      this.extend.xAxis.axisLabel.rotate = newV
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
      } else if (newV.tabs) {
        this.empty = false
        this.activeTab = newV.tabs[0]
        this.updateUnit()
      } else {
        if (this.item.chartData.columns) {
          this.item.chartData.columns = []
        }
        if (this.item.chartData.unit) {
          this.item.chartData.unit = ''
        }
        this.empty = true
      }
      if (this.item.chartType === 've-histogram' || this.item.chartType === 've-line') {
        if (this.item.subType !== 'doubleAxis') {
          this.extend.yAxis.name = newV.unit
        }
      } else if (this.item.chartType === 've-bar') {
        this.extend.xAxis.name = newV.unit
      }
      if (this.item.chartType === 've-histogram') {
        let rows
        if (newV.rows) {
          rows = newV.rows
        } else {
          this.activeTab = newV.tabs[0]
          rows = newV.allData[this.activeTab].rows
        }
        let barW = Math.floor((this.item.width - 60) * 0.7 / rows.length)
        let strLen = Math.ceil(barW / this.item.ctLegendSize)
        this.extend.xAxis.axisLabel.formatter = function (params, index) {
          return params.length > strLen ? params.substr(0, strLen) + '...' : params
        }
      }
      //  && _.isObject(newV) && !_.isArray(newV)
      if (this.item.chartType === 've-gauge') {
        if (this.item.ctLegendShow !== 'false') {
          this.settings.dataName.p = newV.name
        }
        this.settings.seriesMap.pro.detail.formatter = '{value}' + newV.unit
        this.settings.seriesMap.pro.axisLine.lineStyle.color[0][0] = newV.value / 100
        if (this.item.subType !== 'progress') {
          this.settings.seriesMap.outerpro.axisLine.lineStyle.color[0][0] = newV.value / 100
        }
        return
      }
      if (this.item.chartType === 've-line') {
        if (this.item.subType && this.item.subType === 'doubleAxis') {
          // 双轴曲线的坐标轴最大值
          this.settings.max = this.getYaxiosMaxs(newV)
          this.settings.axisSite.left = [newV.columns[1]]
          if (newV.columns[1] !== newV.columns[2]) {
            this.settings.axisSite.right = [newV.columns[2]]
          } else {
            if (this.settings.axisSite.right) delete this.settings.axisSite.right
          }
          this.settings.yAxisName = newV.columns[1] === newV.columns[2] ? [newV.columns[1], ''] : [newV.columns[1], newV.columns[2]]
        } else {
          this.extend.yAxis.name = newV.unit
          if (newV.unit === '%') {
            this.extend.yAxis.max = this.getYaxiosMax(newV)
          } else {
            this.extend.yAxis.max = null
          }
        }
      }
      if (this.item.chartType === 've-pie' || this.item.chartType === 've-ring' || this.item.chartType === 've-radar') {
        this.extend.color = this.getColors(this.item.ctColors)
      }
      if (this.item.chartType === 've-radar') {
        if (newV && newV.columns) {
          if (newV.columns.length < 2) {
            this.item.chartData.rows = []
            this.item.chartData.columns = []
          }
          let indicatorArr = []
          let names = newV.columns.slice(1, newV.columns.length)
          if (newV.rows) {
            names.forEach((key) => {
              let maxItem = _.maxBy(newV.rows, function (item) { return item[key] })
              let maxVal = Number(maxItem[key]) > 100 ? Number(maxItem[key]) : 100
              indicatorArr.push({
                name: key,
                max: maxVal
              })
            })
          }
          this.extend.radar.indicator = indicatorArr
        }
      }
      if (this.item.thirdType === 'stackHistogram') {
        let _key = this.item.chartData.columns[0]
        let _value = this.item.chartData.columns.slice(1, this.item.chartData.columns.length)
        this.settings.stack = { _key: _value }
      }
      this.keyId = new Date().getTime() + parseInt(Math.random() * 10000)
      if (this.item.chartType === 've-line') {
        this.extend.xAxis.axisLine.show = !this.empty
        this.extend.yAxis.axisLine.show = !this.empty
      }
    },
    'item.symbolImg': function (newV) {
      if (this.item.secondType === 'symbolBar') {
        this.extend.series.symbol = 'image://' + newV
      }
    }
  },
  beforeMount: function () {
    if (this.item.chartData.tabs) {
      this.activeTab = this.item.chartData.tabs[0]
    }
    if (this.item.chartType === 've-gauge') {
      if (!this.item.chartData.value && this.item.chartData.value != 0) {
        this.empty = true
      }
    } else if (this.item.chartData && this.item.chartData.rows && this.item.chartData.rows.length === 0) {
      this.empty = true
      if (this.item.chartData.columns) {
        this.item.chartData.columns = []
      }
      if (this.item.chartData.unit) {
        this.item.chartData.unit = ''
      }
      if (this.item.chartType === 've-line') {
        this.extend.xAxis.axisLine.show = !this.empty
        this.extend.yAxis.axisLine.show = !this.empty
      }
    }
  },
  methods: {
    changeActiveTab (tab) {
      this.activeTab = tab
      this.updateUnit()
    },
    updateUnit () {
      const item = this.item
      // 折线图, dealChartData
      if (item.chartType == 've-line' && item.subType == null) {
        this.extend.yAxis.name = this.dealChartData.unit || ''
      }
    },
    getYaxiosMax: function (obj) {
      let rowData = obj.rows
      let maxData = 0
      for (let i = 1, len = obj.columns.length; i < len; i++) {
        let key = obj.columns[i]
        let maxItem = _.maxBy(rowData, function (item) { return item[key] })
        if (maxItem && maxItem[key] > maxData) {
          maxData = maxItem[key]
        }
      }
      rowData = null
      if (parseInt(maxData / 100) === (maxData / 100)) {
        // 整百
        return maxData
      } else {
        return (parseInt(maxData / 100) * 100 + 100)
      }
    },
    // 如果有不止两条曲线的情况需要和后端确认并修改次函数
    getYaxiosMaxs: function (obj) {
      let rowData = obj.rows
      let maxData = []
      for (let i = 1, len = obj.columns.length; i < len; i++) {
        let key = obj.columns[i]
        let maxItem = _.maxBy(rowData, function (item) { return item[key] })
        if (maxItem && maxItem[key]) {
          let tempData = maxItem[key]
          if (parseInt(tempData / 100) === (tempData / 100)) {
            // 整百
            maxData.push(tempData)
          } else {
            maxData.push(parseInt(tempData / 100) * 100 + 100)
          }
        }
      }
      rowData = null
      return maxData
    },
    getColors: function (tempArr) {
      if (this.item.chartType === 've-pie' || this.item.chartType === 've-ring') {
        if (this.item.chartData.rows && this.item.chartData.columns) {
          // 判断是不是只有一个为0的数据
          let key = this.item.chartData.columns[1]
          for (let i = 0, len = this.item.chartData.rows.length; i < len; i++) {
            if (this.item.chartData.rows[i][key] && this.item.chartData.rows[i][key] !== '0') {
              break
            }
            if (i === len - 1) {
              return 'rgba(51, 57, 75, 0.62)'
            }
          }
        }
      }

      if (this.item.chartType === 've-radar') {
        if (this.item.chartData.rows && this.item.chartData.columns) {
          let unKey = this.item.chartData.columns[0]
          let flag = 0
          for (let i = 0, len = this.item.chartData.rows.length; i < len; i++) {
            let obj = this.item.chartData.rows[i]
            let objFlag = true
            for (let key in obj) {
              if (key !== unKey && obj[key] && obj[key] !== '0') {
                objFlag = false
                break
              }
            }
            if (objFlag) {
              flag++
            }
          }
          if (flag === this.item.chartData.rows.length) {
            return 'rgba(51, 57, 75, 0.62)'
          }
        }
      }
      // 以上为校验是否是全为0的值
      let type = this.item.chartType
      // LinearGradient: 右 下 左 上
      let direct = [0, 0, 1, 0]
      if (type === 've-histogram') {
        direct = [0, 1, 0, 0]
      } else if (type === 've-pie') {
        direct = [0, 0, 1, 1]
      } else if (type === 've-line') {
        direct = [0, 1, 0, 0]
      }
      if (Array.isArray(tempArr[0])) {
        let colorArr = []
        for (let i = 0, len = tempArr.length; i < len; i++) {
          let tempColor = new echarts.graphic.LinearGradient(direct[0], direct[1], direct[2], direct[3], [{ offset: 0, color: tempArr[i][0] }, { offset: 1, color: tempArr[i][1] }])
          // 径向： new echarts.graphic.RadialGradient(0.5, 0.5, 0.5, [{ offset: 0, color: tempArr[i][0] }, { offset: 1, color: tempArr[i][1]}])
          colorArr.push(tempColor)
          tempColor = null
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
      let _this = this
      let Fn = {
        've-bar': function () {
          obj.settings.xAxisType = [0]
          obj.extend = $.extend(obj.extend, {
            grid: {
              right: 65
            },
            series: {
              type: 'bar',
              barGap: '20%',
              barCategoryGap: '50%',
              itemStyle: {
                normal: {
                  color: _this.item.colorful && _this.item.colorful === 'true' ? function (params) {
                    let colorList = _this.extend.color
                    let len = colorList.length
                    return colorList[params.dataIndex % len]
                  } : null
                }
              }
            },
            xAxis: {
              minInterval: _this.item.minInterval,
              name: _this.item.chartData.unit,
              position: 'bottom',
              nameTextStyle: {
                color: _this.item.DanweiColor || '#828bac',
                fontSize: _this.item.DanweiSize || 16
              },
              splitLine: {
                show: _this.item.splitShow === 'true',
                lineStyle: {
                  color: _this.item.splitColor || '#333849',
                  width: _this.item.splitSize || 1
                }
              },
              axisLabel: {
                interval: 'auto', // 采用不重叠的方式展示
                rotate: _this.item.rotate || 0,
                textStyle: {
                  color: _this.item.legendColor || '#828bac',
                  fontSize: _this.item.axisLabelSize || '14'
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
            yAxis: {
              axisLabel: {
                interval: 0,
                showMinLabel: true,
                showMaxLabel: true,
                textStyle: {
                  color: _this.item.legendColor || '#828bac',
                  fontSize: _this.item.axisLabelSize || '14'
                },
                formatter: function (params, index) {
                  if (_this.item.formatterType === '0') {
                    return params.length > 5 ? params.substr(0, 5) + '...' : params
                  } else {
                    return params
                  }
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
          let rows
          if (_this.item.chartData.rows) {
            rows = _this.item.chartData.rows
          } else {
            if (!_this.activeTab) {
              _this.activeTab = _this.item.chartData.tabs[0]
            }
            rows = _this.item.chartData.allData[_this.activeTab].rows
          }
          let barW = Math.floor((_this.item.width - _this.item.width * _this.item.gridTop / 50) * 0.7 / rows.length)
          let strLen = Math.ceil(barW / (_this.item.axisLabelSize * 2))
          obj.extend = $.extend(obj.extend, {
            xAxis: {
              axisLabel: {
                interval: 0,
                showMinLabel: true,
                showMaxLabel: true,
                rotate: _this.item.rotate || 0,
                textStyle: {
                  color: _this.item.legendColor || '#828bac',
                  fontSize: _this.item.axisLabelSize || '14'
                },
                formatter: function (params, index) {
                  if (_this.item.formatterType === '0') {
                    return params.length > strLen ? params.substr(0, strLen) + '...' : params
                  } else {
                    return params
                  }
                }
              }
            },
            yAxis: {
              minInterval: _this.item.minInterval,
              name: _this.item.chartData.unit,
              position: 'left',
              nameTextStyle: {
                color: _this.item.DanweiColor || '#828bac',
                fontSize: _this.item.DanweiSize || 16
              },
              splitLine: {
                show: _this.item.splitShow === 'true',
                lineStyle: {
                  color: _this.item.splitColor || '#333849',
                  width: _this.item.splitSize || 1
                }
              },
              axisLabel: {
                interval: 'auto', // 采用不重叠的方式展示
                textStyle: {
                  color: _this.item.legendColor || '#828bac',
                  fontSize: _this.item.axisLabelSize || '14'
                },
                formatter: function (value) {
                  if (value >= 1000) {
                    return (value / 1000 + 'k')
                  } else {
                    return value
                  }
                }
              }
            }
          })
          if (_this.item.thirdType === 'stackHistogram') {
            let columns
            if (_this.item.chartData.columns) {
              columns = _this.item.chartData.columns
            } else {
              if (!_this.activeTab) {
                _this.activeTab = _this.item.chartData.tabs[0]
              }
              columns = _this.item.chartData.allData[_this.activeTab].columns
            }
            let _key = columns[0]
            let _value = columns.slice(1, columns.length)
            obj.settings.stack = { _key: _value }
          }
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
                      let colorList = _this.extend.color
                      let len = colorList.length
                      return colorList[params.dataIndex % len]
                    } : null
                  }
                }
              }
            })
          }
        },
        've-line': function () {
          obj.settings.area = _this.item.lineArea === 'true'
          // obj.settings.xAxisType = 'time'
          var reg = /^\/api/
          var baseUrl = ''
          if (!reg.test(_this.item.symbolSrc)) {
            baseUrl = gbs.host
          }
          obj.extend = $.extend(obj.extend, {
            series: {
              type: 'line',
              smooth: _this.item.smooth ? _this.item.smooth === 'true' : true, // 折线/曲线
              showAllSymbol: false,
              symbol: _this.item.symbolType !== 'pic' ? _this.item.symbolType : _this.item.symbolSrc ? `image://${baseUrl}${_this.item.symbolSrc}` : 'circle',
              symbolSize: _this.item.symbolSize,
              itemStyle: {
                normal: {
                  lineStyle: {
                    width: _this.item.lineWidth, // 设置线条粗细
                    type: _this.item.LineType || 'solid'
                  }
                }
              }
            },
            label: {
              show: _this.item.showPoint === 'true',
              fontSize: _this.item.PointSize || '14',
              color: '#828bac' // 标点的文字颜色
            },
            yAxis: {
              type: 'value',
              nameTextStyle: {
                color: _this.item.legendColor || '#828bac'
              },
              // position: 'left',
              // name: _this.item.chartData.unit, // 单位
              // max: (_this.item.chartData.unit === '%' && _this.item.subType !== 'doubleAxis') ? _this.getYaxiosMax(_this.item.chartData) : null,
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
                  color: _this.item.splitColor || '#333849', // 坐标轴颜色
                  width: _this.item.splitSize || 1
                }
              },
              splitLine: {
                show: _this.item.splitShow === 'true',
                lineStyle: {
                  color: _this.item.splitColor || '#333849', // 修改网格线颜色
                  width: _this.item.splitSize || 1
                }
              },
              axisLabel: {
                interval: 'auto', // 采用不重叠的方式展示
                textStyle: {
                  color: _this.item.legendColor || '#828bac',
                  fontSize: _this.item.axisLabelSize || '14'
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
                  color: _this.item.splitColor || '#333849', // 坐标轴颜色
                  width: _this.item.splitSize || 1
                }
              },
              splitLine: {
                show: false
              },
              axisLabel: {
                rotate: _this.item.rotate || 0,
                textStyle: {
                  color: _this.item.legendColor || '#828bac',
                  fontSize: _this.item.axisLabelSize || '14'
                },
                formatter: function (params, index) {
                  var rows = _this.item.chartData.rows
                  let barW = Math.floor((_this.item.width - 60) * 0.7 / rows.length)
                  let strLen = Math.ceil(barW / (_this.item.axisLabelSize * 2))
                  _this.extend.xAxis.axisLabel.formatter = function (params, index) {
                    if (_this.item.formatterType === '0') {
                      return params.length > strLen ? params.substr(0, strLen) + '...' : params
                    } else {
                      return params
                    }
                  }
                },
                interval: 'auto' // auto 采用不重叠的方式展示，具体数字n则为间隔n展示
              }
            },
            tooltip: {
              trigger: 'axis',
              show: _this.item.tooltipShow === 'true',
              backgroundColor: _this.item.tooltipBackColor,
              textStyle: {
                color: _this.item.tooltipTextColor,
                fontSize: _this.item.tooltipfontSize
              },
              formatter: function (params, ticket, callback) {
                let nameArr = []
                let time = params[0].data[0]
                let showHtm = time + '<br>'
                params.forEach((element, i) => {
                  let name = element.seriesName
                  if (nameArr.indexOf(name) === -1) {
                    nameArr.push(name)
                    let value = element.data[1]
                    if (typeof (value) !== 'number' || value !== value) {
                      value = '--'
                    }
                    if (_this.item.subType === 'doubleAxis') {
                      showHtm += name + '：' + value + (_this.item.chartData.unit[i] || '') + '<br>'
                    } else {
                      showHtm += name + '：' + value + (_this.item.chartData.unit || '') + '<br>'
                    }
                  }
                })
                return showHtm
              }
            }
          })
          if (_this.item.subType === 'doubleAxis') {
            // CPU平均利用率
            obj.settings = $.extend(obj.settings, {
              axisSite: {
                left: [_this.item.chartData.columns[1]]
              },
              // yAxisType: ['KMB', 'percent'],
              max: _this.getYaxiosMaxs(_this.item.chartData), // 设置双轴的最大值 [100,200]
              yAxisName: _this.item.chartData.columns[1] === _this.item.chartData.columns[2] ? [_this.item.chartData.columns[1], ''] : [_this.item.chartData.columns[1], _this.item.chartData.columns[2]]
            })
            if (_this.item.chartData.columns[1] !== _this.item.chartData.columns[2]) {
              obj.settings.axisSite.right = [_this.item.chartData.columns[2]]
            }
          } else {
            obj.extend.yAxis.position = 'left'
            obj.extend.yAxis.name = _this.item.chartData.unit // 单位
            if (_this.item.chartData.unit === '%') {
              obj.extend.yAxis.max = _this.getYaxiosMax(_this.item.chartData)
            }
          }
        },
        've-ring': function () {
          let index = 50 - _this.item.detailwidth + '%'
          obj.settings = $.extend(obj.settings, {
            radius: [index, '50%']
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
              type: 'pie',
              center: ['50%', '45%'],
              radius: [0, '55%']
            }
          })
        },
        've-gauge': function () {
          if (Array.isArray(_this.item.ctColors[0])) {
            var color = new echarts.graphic.LinearGradient(1, 0, 0, 0, [{ offset: 0, color: _this.item.ctColors[0][0] }, { offset: 1, color: _this.item.ctColors[0][1] }])
          } else {
            color = _this.item.ctColors[0]
          }
          let data = _this.item.chartData
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
                      width: _this.item.detailwidth, // 12,
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
                      color: _this.item.legendColor || '#666f8b',
                      fontSize: _this.item.ctLegendSize || 14
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
                      width: _this.item.detailwidth, // 12,
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
                    fontSize: _this.item.fontSize,
                    color: _this.item.detailColor,
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
                      color: _this.item.legendColor || '#666f8b',
                      fontSize: _this.item.ctLegendSize || 14
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
                    color: _this.item.detailColor,
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
          color = null // 手动置空一下
        },
        've-radar': function () {
          let indicatorArr = []
          if (_this.item.chartData && _this.item.chartData.columns) {
            let names = _this.item.chartData.columns.slice(1, _this.item.chartData.columns.length)
            if (_this.item.chartData.rows) {
              names.forEach((key) => {
                let maxItem = _.maxBy(_this.item.chartData.rows, function (item) { return item[key] })
                let maxVal = Number(maxItem[key]) > 100 ? Number(maxItem[key]) : 100
                indicatorArr.push({
                  name: key,
                  max: maxVal
                })
              })
            }
          }
          obj.extend = $.extend(obj.extend, {
            radar: {
              shape: 'polygon', // circle
              splitNumber: 1,
              max: 100,
              indicator: indicatorArr.length > 0 ? indicatorArr : '',
              splitArea: {
                areaStyle: {
                  color: 'transparent'
                }
              },
              splitLine: {
                show: _this.item.splitShow === 'true',
                lineStyle: {
                  color: _this.item.splitColor || 'rgba(117, 124, 137, 0.2)',
                  width: _this.item.splitSize || 1
                }
              },
              axisLabel: {
                show: false
              },
              axisLine: {
                show: false
              }
            },
            series (v) { // 解决：ve-radar为svg时，渐变在直线上失效问题
              let colorArr = _this.extend.color
              let columns = _this.dealChartData.columns
              v.forEach(d => {
                d.itemStyle = { normal: { areaStyle: { type: 'default' } } }
                if (columns.length > 2) { // 解决：雷达图只有一个指标时，无信息展示问题，此情况露出圆点
                  d.symbolSize = 0
                }
                if (columns.length === 3 && _this.item.ifGradual === 'true' && d.data.length) {
                  d.data.forEach((o, i) => {
                    o.lineStyle = {
                      color: colorArr[i].colorStops[0].color // 默认取渐变的0%的颜色
                    }
                  })
                }
              })
              return v
            }
            /*
            series: {
              itemStyle: { normal: { areaStyle: { type: 'default' } } },
              symbolSize: 0
            }
            */
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
        }
      }
      typeof Fn[this.item.chartType] === 'function' && Fn[this.item.chartType]()
    }
  },
  beforeDestroy: function () {
    let _echarts = $(this.$el).find('div')[0]
    let instance = $(_echarts).attr('_echarts_instance_')
    if (instance) {
      let chart = echarts.getInstanceById(instance) // 要在本页引入echarts才生效
      chart.dispose() // 销毁
      chart = null
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

<style lang="scss" scoped>
.tab_btn_box {
    position: absolute;
    right: 5px;
    top: 5px;
    z-index: 9999;
}
.tab_btn {
  border-width: 0.667px;
  border-color: rgb(0, 255, 255);
  border-style: solid;
  border-radius: 5px;
  background-color: transparent;
}
.tab_active_btn {
   border-width: 0.667px;
  border-color: rgb(0, 255, 255);
  border-style: solid;
  border-radius: 5px;
  background-color: rgba(10, 63, 130, 0.502);
}
</style>
