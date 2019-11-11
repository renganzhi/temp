<template>
  <ve-map :data="dealChartData"
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
import { gbs } from '@/config/settings'
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
    var _static = gbs.inDev ? 'static' : 'leaderview-static'
    this.settings = {
      positionJsonLink: './../../../../' + _static + '/libs/map/' + code + '.json', // 打包部署
      position: code === 100000 ? 'china' : 'map_' + code // 设置为非china才不显示南海群岛
    }
    return {
      empty: false,
      keyId: new Date().getTime() + Math.random() * 10000,
      initOption: { renderer: 'svg' },
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
            color: this.item.ctColors.slice(0, this.item.piecesData.length).reverse()
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
              // areaColor: '#294671', // 地图区域的颜色!
              areaColor: this.item.themeType === '1' ? '#121a33' : '#cfd9e3',
              borderColor: this.item.themeType === '1' ? '#38597b' : '#a2b1c0',
              borderWidth: 0.5
              // shadowColor: 'rgba(0, 0, 0, 1)',
              // shadowBlur: 6
              // areaColor: {
              //   type: 'linear',
              //   x: 0,
              //   y: 0,
              //   x2: 0,
              //   y2: 1,
              //   colorStops: [{
              //     offset: 0, color: '#3f15d6' // 0% 处的颜色
              //   }, {
              //     offset: 1, color: '#d243cd' // 100% 处的颜色
              //   }],
              //   globalCoord: false // 缺省为 false
              // },
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
    'item.themeType': function (newV) {
      this.extend.series.itemStyle.normal.areaColor = newV === '1' ? '#121a33' : '#cfd9e3'
      this.extend.series.itemStyle.normal.borderColor = newV === '1' ? '#38597b' : '#a2b1c0'
      this.extend.visualMap.textStyle.color = newV === '1' ? '#fff' : '#50607c'
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
      this.extend.visualMap.inRange.color = this.item.ctColors.slice(0, len).reverse()
      // this.extend.visualMap.inRange.color = this.item.ctColors.slice(0, len)
    },
    'item.mapLevel': function (newV, oldV) {
      console.log('v-map mapLevel:' + oldV + ' to ' + newV)
      this.$nextTick(() => {
        if (newV === 'city') {
          this.settings.positionJsonLink = './../../../../' + this.mapStatic + '/libs/map/' + this.item.cityCode + '.json'
          this.settings.position = 'map_' + this.item.cityCode
        } else if (newV === 'province') {
          this.settings.positionJsonLink = './../../../../' + this.mapStatic + '/libs/map/' + this.item.provinceCode + '.json'
          this.settings.position = 'map_' + this.item.provinceCode
        } else {
          this.settings.positionJsonLink = './../../../../' + this.mapStatic + '/libs/map/100000.json'
          this.settings.position = 'china'
        }
        this.keyId = new Date().getTime() + Math.random() * 10000
      })
    },
    'item.provinceCode': function (newV) {
      if (this.item.mapLevel === 'province') {
        console.log('v-map procode:' + newV)
        this.settings.positionJsonLink = './../../../../' + this.mapStatic + '/libs/map/' + newV + '.json'
        this.settings.position = 'map_' + newV
        this.keyId = new Date().getTime() + Math.random() * 10000
      }
    },
    'item.cityCode': function (newV, oldV) {
      if (this.item.mapLevel === 'city') {
        console.log('v-map cityCode:' + oldV + ' to ' + newV)
        this.settings.positionJsonLink = './../../../../' + this.mapStatic + '/libs/map/' + newV + '.json'
        this.settings.position = 'map_' + newV
        this.keyId = new Date().getTime() + Math.random() * 10000
      }
    },
    'item.ctName': function (newV, oldValue) {
      this.extend.title.text = newV
    },
    'item.width': function (newV, oldValue) {
    },
    'item.ctColors': function (newV) {
      var len = this.extend.visualMap.pieces.length
      this.extend.visualMap.inRange.color = newV.slice(0, len).reverse()
    },
    'item.chartData': function (newV) {
      // if (newV.rows && newV.rows.length > 0) {
      //   this.empty = false
      // } else {
      //   this.empty = true
      // }
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
  },
  methods: {
    formatPieces (piecesData) {
      piecesData[piecesData.length - 1].gte = piecesData[piecesData.length - 1].min
      return piecesData
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
</style>
