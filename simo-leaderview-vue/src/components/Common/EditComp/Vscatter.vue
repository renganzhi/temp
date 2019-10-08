<template>
  <component :is="'ve-map'"
             :width="comWidth"
             :height="comHeight"
             :settings="settings"
             :data="dealChartData"
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
  </component>
</template>
<script>
import { gbs } from '@/config/settings'
import { mapGetters } from 'vuex'
export default {
  name: 'vscatter',
  props: ['item'],
  data () {
    var code = 100000 // 中国
    if (this.item.mapLevel === 'province') {
      code = this.item.provinceCode
    } else if (this.item.mapLevel === 'city') {
      code = this.item.cityCode
    }
    // var code = 510000
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
          // type: 'continuous', // 连续显示值
          // min: 0, // 值域最小值，必须参数
          // max: 500, // 值域最大值，必须参数
          realtime: false,
          calculable: true, // 是否启用值域漫游
          left: this.item.visualPosition === 'left' ? 0 : 'auto',
          right: this.item.visualPosition === 'right' ? 0 : 'auto', // 图例靠右
          inRange: {
            color: ['#ff9900', '#00ffff', '#ffffff'] // 按照值的范围给的不同颜色
            // color: this.item.ctColors.slice(0, this.item.piecesData.length)
          },
          // piecewise分段设置 https://echarts.apache.org/zh/option.html#visualMap-piecewise.pieces
          pieces: [
            { value: 1, color: '#02D4A0' },
            { value: 2, color: '#FFFF00' },
            { value: 3, color: '#FFB445' },
            { value: 4, color: '#FF6E00' },
            { value: 5, color: '#FF0000' },
            { value: 6, color: '#87CEFA' }
          ],
          color: ['#E0022B', '#E09107', '#A3E00B'],
          show: false,
          // show: this.item.ctLegendShow === 'true', // 是否显示取值范围颜色段
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
        geo: { // 地图配置
          silent: true, // 不响应和触发鼠标事件
          show: true,
          map: code === 100000 ? 'china' : 'map_' + code,
          label: {
            normal: {
              show: false
            },
            emphasis: {
              show: false
            }
          },
          roam: false,
          itemStyle: {
            normal: {
              // areaColor: 'rgba(172, 191, 220, 0.5)',
              areaColor: 'rgba(104, 150, 197, 0.5)',
              borderColor: '#222739',
              borderWidth: 0.3,
              // borderWidth: 0.5,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
              // shadowBlur: 1
            }
            // emphasis: {
            //   areaColor: '#2B91B7'
            // }
          }
        },
        series: {
          type: 'effectScatter',
          coordinateSystem: 'geo',
          geoIndex: 0,
          data: this.item.scatterPoint,
          // data: [
          //   { name: '成都', value: [104.06, 30.67, 10] },
          // ],
          rippleEffect: { // 涟漪特效
            period: 4, // 动画时间，值越小速度越快
            brushType: 'stroke', // 波纹绘制方式 stroke, fill
            scale: 4 // 波纹圆环最大限制，值越大波纹越大
          },
          // showEffectOn: 'emphasis',
          // rippleEffect: {
          //   brushType: 'stroke'
          // },
          // hoverAnimation: true,
          symbolSize: 6,
          // 鼠标移上显示城市名
          label: {
            normal: {
              formatter: '{b}',
              position: 'right',
              color: '#fff',
              show: true // false
            },
            emphasis: {
              show: true
            }
          }
        }
        // series: {
        //   type: 'map',
        //   showLegendSymbol: false, // 不展示，所以以下配置无用，暂且留着万一需求有变
        //   // roam: true, // 允许鼠标缩放地图
        //   // selectedMode: 'single',
        //   // 图形上的文本标签
        //   label: {
        //     normal: {
        //       show: false, // 省份文字最开始不显示，选中之后再显示 // 测试地图是否准确
        //       textStyle: {
        //         // color: '#231816' // 默认的字体颜色! auto
        //       }
        //     }
        //   },
        //   itemStyle: {
        //     normal: {
        //       // color: 'red', // 展示指标及圆点的颜色
        //       areaColor: '#294671', // 地图区域的颜色!
        //       borderColor: '#f0f0f0' // 区域分割线颜色!
        //     }
        //   },
        //   // 选中之后的状态
        //   emphasis: {
        //     label: {
        //       show: false, // 选中区域的文字展示
        //       textStyle: {
        //         color: '#000' // 选中之后的字体颜色!
        //       }
        //     },
        //     itemStyle: {
        //       areaColor: '#0573bf', // 选中之后的颜色值
        //       shadowColor: 'rgba(0, 0, 0, 0.5)',
        //       shadowBlur: 0
        //     }
        //   }
        // }
      }
    }
  },
  computed: {
    ...mapGetters([
      'areaData',
      'alertInfo'
    ]),
    geoCoordMap: function () {
      var geoData = {}
      this.areaData.forEach((item) => {
        geoData[item.name] = item.geoCoord
      })
      return geoData
    },
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
    'item.piecesData': function (newV) {
      this.extend.visualMap.pieces = this.formatPieces(newV)
      var len = newV.length
      this.extend.visualMap.inRange.color = this.item.ctColors.slice(0, len).reverse()
    },
    'item.mapLevel': function (newV, oldV) {
      console.log('v-scatter mapLevel:' + oldV + ' to ' + newV)
      this.$nextTick(() => {
        if (newV === 'city') {
          this.settings.positionJsonLink = './../../../../' + this.mapStatic + '/libs/map/' + this.item.cityCode + '.json'
          this.settings.position = 'map_' + this.item.cityCode
          this.extend.geo.map = 'map_' + this.item.cityCode
        } else if (newV === 'province') {
          this.settings.positionJsonLink = './../../../../' + this.mapStatic + '/libs/map/' + this.item.provinceCode + '.json'
          this.settings.position = 'map_' + this.item.provinceCode
          this.extend.geo.map = 'map_' + this.item.provinceCode
        } else {
          this.settings.positionJsonLink = './../../../../' + this.mapStatic + '/libs/map/100000.json'
          this.settings.position = 'china'
          this.extend.geo.map = 'china'
        }
        this.keyId = new Date().getTime() + Math.random() * 10000
      })
    },
    'item.provinceCode': function (newV) {
      console.log(newV)
      if (this.item.mapLevel === 'province') {
        console.log('v-scatter procode:' + newV)
        this.settings.positionJsonLink = './../../../../' + this.mapStatic + '/libs/map/' + newV + '.json'
        this.settings.position = 'map_' + newV
        this.extend.geo.map = 'map_' + newV
        this.keyId = new Date().getTime() + Math.random() * 10000
      }
    },
    'item.cityCode': function (newV, oldV) {
      if (this.item.mapLevel === 'city') {
        console.log('v-scatter cityCode:' + oldV + ' to ' + newV)
        console.log('citycode:' + newV)
        this.settings.positionJsonLink = './../../../../' + this.mapStatic + '/libs/map/' + newV + '.json'
        this.settings.position = 'map_' + newV
        this.extend.geo.map = 'map_' + newV
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
    'item.chartData': function (newV, oldV) {
      if (!_.isEqual(newV, oldV)) {
        console.log('不等')
        this.extend.series.data = this.formatData(newV)
        this.item.scatterPoint = this.extend.series.data
      }
    }
  },
  beforeMount: function () {

  },
  mounted: function () {
    var piecesArr = []
    if (this.alertInfo && this.alertInfo.length > 0) {
      _.forEach(this.alertInfo, function (item) {
        let obj = { 'value': item.level, color: item.color }
        piecesArr.push(obj)
      })
      this.extend.visualMap.pieces = piecesArr
    }
    // setTimeout(() => {
    //   var code = 100000
    //   var _static = gbs.inDev ? 'static' : 'leaderview-static'
    //   this.settings = {
    //     positionJsonLink: './../../../../' + _static + '/libs/map/' + code + '.json', // 打包部署
    //     position: code === 100000 ? 'china' : 'map_' + code // 设置为非china才不显示南海群岛
    //   }
    //   this.extend.geo.map = 'china'
    // }, 5000)
  },
  methods: {
    formatPieces (piecesData) {
      piecesData[piecesData.length - 1].gte = piecesData[piecesData.length - 1].min
      return piecesData
    },
    formatData (newV) {
      var mapData = []
      newV.forEach((item) => {
        let _value = this.geoCoordMap[item.name].concat(item.value)
        let obj = { name: item.name, value: _value }
        mapData.push(obj)
      })
      return mapData
      // if (this.geoCoordMap && this.geoCoordMap.length > 0) {

      // } else {
      //   return false
      // }
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
