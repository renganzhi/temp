<template>
  <ve-map
          :width="comWidth"
          :height="comHeight"
          :init-options="initOption"
          :settings="settings"
          :extend="extend"
          :id="'map_' + keyId"
          :key="keyId"
          :judge-width="true">
    <!-- <div class="v-charts-data-empty"
         v-if="empty"
         style="width: 100%; height: 100%; text-align: center; font-size: 12px;">
      <div><i class="icon-n-nodata"
           style="font-size: 108px;"></i><br>
        <p>抱歉，没有数据可供展示...</p>
      </div>
    </div> -->
  </ve-map>
</template>
<script>
import { VeMap } from 'v-charts'
import { gbs } from '@/config/settings'
import echarts from 'echarts'
export default {
  name: 'vmap',
  components: { VeMap },
  props: ['item'],
  data () {
    var code = 100000 // 中国
    if (this.item.mapLevel === 'province') {
      code = this.item.provinceCode
    } else if (this.item.mapLevel === 'city') {
      code = this.item.cityCode
    }
    var _static = gbs.inDev ? 'static' : 'leaderview-static'
    this.settings = {
      positionJsonLink: './../../../../' + _static + '/libs/map/' + code + '.json', // 打包部署
      position: code === 100000 ? 'china' : 'map_' + code // 设置为非china才不显示南海群岛
    }
    var colordirectionArry = [
      [0, 0, this.item.width, 0],
      [0, 0, this.item.width, this.item.height],
      [0, 0, 0, this.item.height],
      [this.item.width, 0, 0, this.item.height]
    ]
    let dataArry = []
    this.item.chartData.rows.forEach(d => {
      dataArry.push({
        name: d[this.item.chartData.columns[0]],
        value: d[this.item.chartData.columns[2]]
      })
    })
    return {
      empty: false,
      keyId: new Date().getTime() + Math.random() * 10000,
      // initOption: { renderer: 'svg' },
      mapStatic: gbs.inDev ? 'static' : 'leaderview-static',
      // settings: {
      //   // yAxisType: [0],
      //   // positionJsonLink: 'https://unpkg.com/v-charts-custom-maps@0.2.1/hk-geo.json',
      //   positionJsonLink: './../../../../static/libs/map/100000.json',
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
            color: this.item.ColorArry.slice(0, this.item.piecesData.length).reverse()
          },
          // inverse: true,
          // piecewise分段设置 https://echarts.apache.org/zh/option.html#visualMap-piecewise.pieces
          // splitNumber: 3, // 几种颜色值及取值范围
          pieces: this.formatPieces(this.item.piecesData), // 默认取data中最后一个维度
          // pieces: [
          //   // { min: 1000, label: '自定义', color: 'orange' },
          //   { min: 100, max: 499 },
          //   { max: 99 }
          // ],
          color: ['#E0022B', '#E09107', '#A3E00B'],
          // itemSymbol: 'none',
          show: this.item.ctLegendShow === 'true', // 是否显示取值范围颜色段
          hoverLink: false,
          showLabel: true,
          textStyle: {
            color: this.item.themeType === '1' ? '#fff' : '#50607c'
          }
        },
        tooltip: {
          show: false
        },
        legend: {
          show: false
        },
        geo: { // 地图配置
          silent: true, // 不响应和触发鼠标事件
          show: true,
          roam: this.item.roam, // 是否允许缩放
          map: code === 100000 ? 'china' : 'map_' + code,
          label: {
            normal: {
              show: this.item.cityShow,
              fontSize: this.item.fontSize,
              color: this.item.cityColor
            },
            emphasis: {
              show: false
            }
          },
          itemStyle: {
            normal: {
              // areaColor: this.item.areaColor,
              color: {
                type: 'linear',
                x: colordirectionArry[this.item.colordirection][0],
                y: colordirectionArry[this.item.colordirection][1],
                x2: colordirectionArry[this.item.colordirection][2],
                y2: colordirectionArry[this.item.colordirection][3],
                colorStops: [{
                  offset: 0,
                  color: this.item.normalcolor[0] // 0% 处的颜色
                }, {
                  offset: 1,
                  color: this.item.normalcolor[1] // 50% 处的颜色
                }],
                global: true // 缺省为 false
              },
              borderColor: this.item.borderColor,
              borderWidth: 0.5,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
              // shadowBlur: 1
            }
            // emphasis: {
            //   areaColor: '#2B91B7'
            // }
          }
        },
        series: {
          type: 'map',
          geoIndex: 0,
          data: dataArry
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
    colordirectionArry: function () {
      return [
        [0, 0, this.item.width, 0],
        [0, 0, this.item.width, this.item.height],
        [0, 0, 0, this.item.height],
        [this.item.width, 0, 0, this.item.height]
      ]
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
    }
  },
  watch: {
    'item.cityColor': function (newV) {
      this.extend.geo.label.normal.color = newV
    },
    'item.cityShow': function (newV) {
      this.extend.geo.label.normal.show = newV
    },
    'item.fontSize': function (newV) {
      this.extend.geo.label.normal.fontSize = newV
    },
    // 'item.themeType': function (newV) {
    //   this.extend.geo.itemStyle.normal.areaColor = newV === '1' ? '#121a33' : '#cfd9e3'
    //   this.extend.geo.itemStyle.normal.borderColor = newV === '1' ? '#38597b' : '#a2b1c0'
    //   this.extend.visualMap.textStyle.color = newV === '1' ? '#fff' : '#50607c'
    // },
    'item.borderColor': function (newV) {
      this.extend.geo.itemStyle.normal.borderColor = newV
    },
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
    'item.piecesData': function (newV) {
      this.extend.visualMap.pieces = this.formatPieces(newV)
      var len = newV.length
      this.extend.visualMap.inRange.color = this.item.ColorArry.slice(0, len).reverse()
      // this.extend.visualMap.inRange.color = this.item.ctColors.slice(0, len)
    },
    'item.mapLevel': function (newV, oldV) {
      this.$nextTick(() => {
        if (newV === 'city') {
          if (this.item.cityCode) {
            this.settings.positionJsonLink = './../../../../' + this.mapStatic + '/libs/map/' + this.item.cityCode + '.json'
            this.settings.position = 'map_' + this.item.cityCode
            this.extend.geo.map = 'map_' + this.item.cityCode
          }
        } else if (newV === 'province') {
          if (this.item.provinceCode) {
            this.settings.positionJsonLink = './../../../../' + this.mapStatic + '/libs/map/' + this.item.provinceCode + '.json'
            this.settings.position = 'map_' + this.item.provinceCode
            this.extend.geo.map = 'map_' + this.item.provinceCode
          }
        } else {
          this.settings.positionJsonLink = './../../../../' + this.mapStatic + '/libs/map/100000.json'
          this.settings.position = 'china'
          this.extend.geo.map = 'china'
        }
        this.keyId = new Date().getTime() + Math.random() * 10000
      })
    },
    'item.provinceCode': function (newV) {
      if (this.item.mapLevel === 'province') {
        this.settings.positionJsonLink = './../../../../' + this.mapStatic + '/libs/map/' + newV + '.json'
        this.settings.position = 'map_' + newV
        this.extend.geo.map = 'map_' + newV
        this.keyId = new Date().getTime() + Math.random() * 10000
      }
    },
    'item.cityCode': function (newV, oldV) {
      if (this.item.mapLevel === 'city') {
        this.settings.positionJsonLink = './../../../../' + this.mapStatic + '/libs/map/' + newV + '.json'
        this.settings.position = 'map_' + newV
        this.extend.geo.map = 'map_' + newV
        this.keyId = new Date().getTime() + Math.random() * 10000
      }
    },
    'item.roam': function (newV, oldValue) {
      this.extend.geo.roam = newV
    },
    'item.ctName': function (newV, oldValue) {
      this.extend.title.text = newV
    },
    'item.normalcolor': function (newV, oldValue) {
      this.extend.geo.itemStyle.normal.color.colorStops = [{
        offset: 0,
        color: this.item.normalcolor[0] // 0% 处的颜色
      }, {
        offset: 1,
        color: this.item.normalcolor[1] // 50% 处的颜色
      }]
    },
    'item.width': function (newV, oldValue) {
      var colordirectionArry = [
        [0, 0, this.item.width, 0],
        [0, 0, this.item.width, this.item.height],
        [0, 0, 0, this.item.height],
        [this.item.width, 0, 0, this.item.height]
      ]
      this.extend.geo.itemStyle.normal.color.x = colordirectionArry[this.item.colordirection][0]
      this.extend.geo.itemStyle.normal.color.y = colordirectionArry[this.item.colordirection][1]
      this.extend.geo.itemStyle.normal.color.x2 = colordirectionArry[this.item.colordirection][2]
      this.extend.geo.itemStyle.normal.color.y2 = colordirectionArry[this.item.colordirection][3]
    },
    'item.height': function (newV, oldValue) {
      var colordirectionArry = [
        [0, 0, this.item.width, 0],
        [0, 0, this.item.width, this.item.height],
        [0, 0, 0, this.item.height],
        [this.item.width, 0, 0, this.item.height]
      ]
      this.extend.geo.itemStyle.normal.color.x = colordirectionArry[this.item.colordirection][0]
      this.extend.geo.itemStyle.normal.color.y = colordirectionArry[this.item.colordirection][1]
      this.extend.geo.itemStyle.normal.color.x2 = colordirectionArry[this.item.colordirection][2]
      this.extend.geo.itemStyle.normal.color.y2 = colordirectionArry[this.item.colordirection][3]
    },
    'item.colordirection': function (newV) {
      var colordirectionArry = [
        [0, 0, this.item.width, 0],
        [0, 0, this.item.width, this.item.height],
        [0, 0, 0, this.item.height],
        [this.item.width, 0, 0, this.item.height]
      ]
      this.extend.geo.itemStyle.normal.color.x = colordirectionArry[this.item.colordirection][0]
      this.extend.geo.itemStyle.normal.color.y = colordirectionArry[this.item.colordirection][1]
      this.extend.geo.itemStyle.normal.color.x2 = colordirectionArry[this.item.colordirection][2]
      this.extend.geo.itemStyle.normal.color.y2 = colordirectionArry[this.item.colordirection][3]
    },
    'item.ColorArry': function (newV) {
      var len = this.extend.visualMap.pieces.length
      this.extend.visualMap.inRange.color = newV.slice(0, len).reverse()
    },

    'item.chartData': {
      handler (newVal, oldVal) {
        let dataArry = []
        this.item.chartData.rows.forEach(d => {
          dataArry.push({
            name: d[this.item.chartData.columns[0]],
            value: d[this.item.chartData.columns[2]]
          })
        })
        this.extend.series.data = dataArry
      },
      deep: true
    }

  },
  beforeMount: function () {
    // if (this.item.chartData && this.item.chartData.rows && this.item.chartData.rows.length === 0) {
    //   this.empty = true
    // }
    var theme = $('html').attr('data-theme')
    if (theme === 'blackWhite' || theme === 'blueWhite') {
      this.extend.visualMap.textStyle = '#50607c'
    }
  },
  mounted: function () {
    if (this.item.mapLevel === 'country') {
      this.item.provinceCode = ''
      this.item.cityCode = ''
    }
    if (this.item.mapLevel === 'province') {
      this.item.cityCode = ''
    }
  },
  methods: {
    formatPieces (piecesData) {
      for (let i = 0, len = piecesData.length; i < len - 1; i++) {
        piecesData[i].max = Number(piecesData[i].max)
        if (piecesData[i].gte) {
          // piecesData[i].gte = null
          delete piecesData[i].gte
        }
      }
      piecesData[piecesData.length - 1].gte = piecesData[piecesData.length - 1].min
      return piecesData
    }
  },
  beforeDestroy: function () {
    // 销毁echarts
    var _echarts = $(this.$el).find('div')[0]
    var instance = $(_echarts).attr('_echarts_instance_')
    if (instance) {
      var chart = echarts.getInstanceById(instance) // 要在本页引入echarts才生效
      chart.dispose() // 销毁
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
</style>
