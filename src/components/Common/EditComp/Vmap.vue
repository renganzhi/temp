<template>
  <!-- :init-options="initOption" -->
  <component :is="'ve-map'"
             :data="dealChartData"
             :width="comWidth"
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
  <!-- <ve-map :data="dealChartData"
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
  </ve-map> -->
</template>
<script>
export default {
  name: 'vmap',
  props: ['item'],
  data () {
    var code = 100000 // 中国
    if (this.item.mapLevel === 'province') {
      code = this.item.provinceCode
    } else if (this.item.mapLevel === 'city') {
      code = this.item.cityCode
    }
    this.settings = {
      positionJsonLink: './../../../../static/libs/map/' + code + '.json',
      position: code === 100000 ? 'china' : '四川' // 设置为非china才不显示南海群岛
    }
    return {
      empty: false,
      keyId: new Date().getTime() + Math.random() * 10000,
      initOption: { renderer: 'svg' },
      // settings: {
      //   // yAxisType: [0],
      //   // positionJsonLink: 'https://unpkg.com/v-charts-custom-maps@0.2.1/hk-geo.json',
      //   positionJsonLink: './../../../../static/libs/map/100000.json',
      //   // positionJsonLink: './../../../../leaderview-static/libs/map/100000.json', // 打包部署
      //   position: '四川' // 设置为非china才不显示南海群岛
      //   // dimension: '位置',
      //   // metrics: ['资源', '告警'],
      //   // dataType: {
      //   //   '资源': 'KMB'
      //   // }
      // },
      extend: {
        silent: true, // 不响应和触发鼠标事件
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
        textStyle: {
          color: '#666f8b'
        },
        visualMap: {
          type: 'piecewise', // 分段显示值
          realtime: false,
          calculable: true,
          left: this.item.visualPosition === 'left' ? 0 : 'auto',
          right: this.item.visualPosition === 'right' ? 0 : 'auto', // 图例靠右
          inRange: {
            // color: ['pink', 'yellow', '#dd7e6b'] // 按照值的范围给的不同颜色
            color: this.item.ctColors.slice(0, this.item.piecesData.length)
          },
          // piecewise分段设置 https://echarts.apache.org/zh/option.html#visualMap-piecewise.pieces
          // splitNumber: 3, // 几种颜色值及取值范围
          pieces: this.item.piecesData, // 默认取data中最后一个维度
          // pieces: [
          //   // { min: 1000, label: '自定义', color: 'orange' },
          //   { min: 100, max: 499 },
          //   { max: 99 }
          // ],
          color: ['#E0022B', '#E09107', '#A3E00B'],
          // itemSymbol: 'none',
          show: this.item.ctLegendShow === 'true', // 是否显示取值范围颜色段
          hoverLink: true,
          showLabel: true,
          textStyle: {
            color: '#fff'
          }
        },
        tooltip: {
          show: false
        },
        legend: {
          show: false
        },
        series: {
          type: 'map',
          showLegendSymbol: false, // 不展示，所以以下配置无用，暂且留着万一需求有变
          // roam: true, // 允许鼠标缩放地图
          // selectedMode: 'single',
          // 图形上的文本标签
          label: {
            normal: {
              show: false, // 省份文字最开始不显示，选中之后再显示 // 测试地图是否准确
              textStyle: {
                // color: '#231816' // 默认的字体颜色! auto
              }
            }
          },
          itemStyle: {
            normal: {
              // color: 'red', // 展示指标及圆点的颜色
              areaColor: '#294671', // 地图区域的颜色!
              borderColor: '#f0f0f0' // 区域分割线颜色!
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
      }
    }
  },
  computed: {
    mapCode: function () {
      var code = 100000 // 中国
      if (this.item.mapLevel === 'province') {
        code = this.item.provinceCode
      } else if (this.item.mapLevel === 'city') {
        code = this.item.cityCode
      }
      return code
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
      return this.item.chartData
    }
  },
  watch: {
    'item.ctLegendShow': function (newV, oldValue) {
      this.extend.visualMap.show = newV === 'true'
    },
    'item.visualPosition': function (newV, oldValue) {
      // this.extend.visualMap.show = newV === 'true'
      if (newV === 'left') {
        this.extend.visualMap.left = 0
      } else if (newV === 'right') {
        this.extend.visualMap.left = 'auto'
        this.extend.visualMap.right = 0
      }
    },
    'item.mapLevel': function (newV) {
      this.$nextTick(() => {
        if (newV === 'city') {
          this.settings.positionJsonLink = './../../../../static/libs/map/' + this.item.cityCode + '.json'
          this.settings.position = '其它'
        } else if (newV === 'province') {
          this.settings.positionJsonLink = './../../../../static/libs/map/' + this.item.provinceCode + '.json'
          this.settings.position = '其它'
        } else {
          this.settings.positionJsonLink = './../../../../static/libs/map/100000.json'
          this.settings.position = 'china'
        }
        this.keyId = new Date().getTime() + Math.random() * 10000
      })
    },
    'item.provinceCode': function (newV) {
      console.log(newV)
      if (this.item.mapLevel === 'province') {
        console.log('procode:' + newV)
        this.settings.positionJsonLink = './../../../../static/libs/map/' + newV + '.json'
        this.keyId = new Date().getTime() + Math.random() * 10000
      }
    },
    'item.cityCode': function (newV) {
      if (this.item.mapLevel === 'city') {
        console.log('citycode:' + newV)
        this.settings.positionJsonLink = './../../../../static/libs/map/' + newV + '.json'
        this.keyId = new Date().getTime() + Math.random() * 10000
      }
    },
    'item.ctName': function (newV, oldValue) {
      this.extend.title.text = newV
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
    'item.ctColors': function (newV) {
      var len = this.extend.visualMap.pieces.length
      this.extend.visualMap.inRange.color = newV.slice(0, len).reverse()
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
      }
    }

  },
  beforeMount: function () {
    if (this.item.chartData && this.item.chartData.rows && this.item.chartData.rows.length === 0) {
      this.empty = true
    }
  },
  mounted: function () {
    console.log(this.item.cityCode)
    // setTimeout(() => {
    //   this.settings.positionJsonLink = './../../../../static/libs/map/510000.json'
    //   this.keyId = new Date().getTime() + Math.random() * 10000
    //   console.log(1)
    // }, 4000)
  },
  methods: {
    initMapData (mapJson) {
      var mapData = []
      for (var i = 0; i < mapJson.features.length; i++) {
        mapData.push({
          name: mapJson.features[i].properties.name
          // id:mapJson.features[i].id
        })
      }
      console.log(mapData)
      return mapData
    },
    setGaugeColor: function (type, newV) {
      if (Array.isArray(newV[0])) {
        this.settings.seriesMap[type].axisLine.lineStyle.color[0][1] = new echarts.graphic.LinearGradient(1, 0, 0, 0, [{ offset: 0, color: newV[0][0] }, { offset: 1, color: newV[0][1] }])
        this.settings.seriesMap[type].detail.color = newV[0][0]
      } else {
        this.settings.seriesMap[type].axisLine.lineStyle.color[0][1] = newV[0]
        this.settings.seriesMap[type].detail.color = newV[0]
      }
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
  destoryed: function () {
  }
}
</script>
<style>
.v-charts-data-empty {
  position: absolute !important;
  top: 0px !important;
  background-color: rgba(28, 36, 60, 0.71) !important;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
}
</style>
