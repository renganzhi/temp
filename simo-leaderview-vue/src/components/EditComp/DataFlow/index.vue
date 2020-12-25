<template>
  <div class="DataFlow">
    <div
      :id="echartId"
      :style="boxStyle">
    </div>
  </div>

</template>
<script>
import echarts from 'echarts'
import china from '../../../../static/libs/map/100000.json'
export default {
  name: 'TDEarth',
  props: ['item'],
  data () {
    return {
      mychart: null,
      echartId: ''
    }
  },
  created () {
    this.echartId = 'DataFlow' + document.querySelectorAll('.DataFlow').length
  },
  computed: {
    boxStyle: function () {
      return {
        width: this.item.width + 'px',
        height: this.item.height + 'px'
      }
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
    'item.labelemphasis': function () {
      this.drawFlow()
    },
    'item.roam': function () {
      this.drawFlow()
    },
    'item.normalcolor': function () {
      this.drawFlow()
    },
    'item.normalborderColor': function () {
      this.drawFlow()
    },
    'item.normalborderWidth': function () {
      this.drawFlow()
    },
    'item.emphasis': function () {
      this.drawFlow()
    },
    'item.textStyleColor': function () {
      this.drawFlow()
    },
    'item.visualMapShow': function () {
      this.drawFlow()
    },
    'item.visualMapTextcolor': function () {
      this.drawFlow()
    },
    'item.calculable': function () {
      this.drawFlow()
    },
    'item.labelfontSize': function () {
      this.drawFlow()
    },
    'item.ctColors': function () {
      this.drawFlow()
    },
    'item.effectshow': function () {
      this.drawFlow()
    },
    'item.effectperiod': function () {
      this.drawFlow()
    },
    'item.effecttrailLength': function () {
      this.drawFlow()
    },
    'item.effectsymbolSize': function () {
      this.drawFlow()
    },
    'item.normalwidth': function () {
      this.drawFlow()
    },
    'item.normalopacity': function () {
      this.drawFlow()
    },
    'item.normalcurveness': function () {
      this.drawFlow()
    },
    'item.showEffectOn': function () {
      this.drawFlow()
    },
    'item.rippleEffectbrushType': function () {
      this.drawFlow()
    },
    'item.rippleEffectperiod': function () {
      this.drawFlow()
    },
    'item.rippleEffectscale': function () {
      this.drawFlow()
    },
    'item.normalposition': function () {
      this.drawFlow()
    },
    'item.symbolSize': function () {
      this.drawFlow()
    },
    'item.EffectbrushType': function () {
      this.drawFlow()
    },
    'item.Effectscale': function () {
      this.drawFlow()
    },
    'item.normalfontSize': function () {
      this.drawFlow()
    },
    'item.labelposition': function () {
      this.drawFlow()
    },
    'item.labelcolor': function () {
      this.drawFlow()
    },
    'item.labeltextSize': function () {
      this.drawFlow()
    },
    'item.geosymbolSize': function () {
      this.drawFlow()
    },
    'item.tooltipBackColor': function () {
      this.drawFlow()
    },
    'item.tooltipTextColor': function () {
      this.drawFlow()
    },
    'item.tooltipTextfontSize': function () {
      this.drawFlow()
    },
    'item.chartData': function () {
      this.drawFlow()
    }
  },
  methods: {
    drawFlow () {
      let _this = this
      var series = []
      var planePath = 'path://M995.679767 658.818049c-8.943231-1.490539-23.848617-2.981077-44.716157-4.471615-73.03639-8.943231-207.184862-26.829694-295.126638-38.754003l-19.377001-5.962154h-2.981077l-44.716157-13.414848c-4.471616 90.922853-10.43377 166.94032-17.886463 225.071325v10.43377l22.358078 10.43377 70.055313 32.791849c2.981077 1.490539 4.471616 4.471616 4.471616 7.452693l1.490539 55.149927v4.471615c0 2.981077-1.490539 5.962154-4.471616 4.471616l-114.77147-19.377001c-8.943231 22.358079-17.886463 34.282387-31.30131 35.772925h-1.490539c-13.414847 0-23.848617-11.924309-31.30131-35.772925l-114.77147 17.886463c-2.981077 0-4.471616-1.490539-4.471616-4.471616v-4.471616l1.490539-55.149927c0-2.981077 1.490539-5.962154 4.471615-7.452693l70.055313-32.791849 22.358079-10.43377V819.796215c-5.962154-58.131004-10.43377-134.148472-14.905386-225.071324l-46.206696 13.414847h-4.471615l-19.377002 4.471616c-87.941776 10.43377-222.090247 26.829694-295.126637 35.772925-20.86754 2.981077-35.772926 4.471616-44.716158 4.471616-23.848617 2.981077-25.339156-26.829694-7.452692-37.263464 2.981077-1.490539 56.640466-29.810771 123.714701-67.074236v-38.754003c0-14.905386 11.924309-26.829694 26.829695-26.829694s26.829694 11.924309 26.829694 26.829694v8.943232c40.244541-20.86754 81.979622-43.225619 119.243086-64.093159v-44.716157c0-14.905386 11.924309-26.829694 26.829694-26.829695s26.829694 11.924309 26.829694 26.829695v14.905385c23.848617-11.924309 43.225619-23.848617 56.640466-31.30131-1.490539-174.393013 11.924309-308.541485 61.112082-332.390101 2.981077-1.490539 4.471616-1.490539 7.452693-2.981078 1.490539 0 4.471616 0 5.962154-1.490538h5.962154c1.490539 0 4.471616 0 5.962154 1.490538 2.981077 0 4.471616 1.490539 7.452693 2.981078 49.187773 23.848617 62.60262 157.997089 58.131005 332.390101 13.414847 7.452693 32.791849 17.886463 55.149927 29.810772v-14.905386c0-14.905386 11.924309-26.829694 26.829694-26.829694s26.829694 11.924309 26.829695 26.829694v44.716157c37.263464 20.86754 78.998544 43.225619 119.243085 65.583698V506.783115c0-14.905386 11.924309-26.829694 26.829695-26.829694s26.829694 11.924309 26.829694 26.829694v44.716157c68.564774 37.263464 122.224163 67.074236 123.714702 68.564775 25.339156 10.43377 23.848617 41.73508-1.490539 38.754002z'
      series.push({
        type: 'lines',
        zlevel: 2,
        effect: {
          show: _this.item.effectshow === 'true',
          period: _this.item.effectperiod, // 箭头指向速度，值越小速度越快
          trailLength: _this.item.effecttrailLength * 1, // 特效尾迹长度[0,1]值越大，尾迹越长重
          // symbol: _this.item.imgSrc === '' ? planePath : 'image://' + 'http://localhost:8080/api/leaderview/home/getImg/true/1199', // 箭头图标
          symbol: planePath, // 箭头图标   'image://' + require('./FlowPic/111111.png')
          symbolSize: _this.item.effectsymbolSize // 图标大小
        },
        lineStyle: {
          normal: {
            width: _this.item.normalwidth * 1, // 尾迹线条宽度
            opacity: _this.item.normalopacity * 1, // 尾迹线条透明度
            curveness: _this.item.normalcurveness * 1 // 尾迹线条曲直度
          }
        },
        data: _this.item.chartData.lineData || []
      }, {
        type: 'effectScatter',
        coordinateSystem: 'geo',
        legendHoverLink: true,
        showEffectOn: _this.item.showEffectOn, // render
        zlevel: 2,
        rippleEffect: { // 涟漪特效
          period: _this.item.rippleEffectperiod, // 动画时间，值越小速度越快
          brushType: _this.item.rippleEffectbrushType, // 波纹绘制方式 stroke, fill
          scale: _this.item.rippleEffectscale // 波纹圆环最大限制，值越大波纹越大
        },
        label: {
          normal: {
            show: true,
            position: _this.item.normalposition, // 显示位置
            offset: [5, 10, 5, 5], // 偏移设置
            formatter: function (params) { // 圆环显示文字
              return params.data.name
            },
            fontSize: _this.item.normalfontSize
          },
          emphasis: {
            show: true
          }
        },
        symbol: 'circle',
        symbolSize: function (val) {
          return 5 + val[2] / (_this.item.chartData.maxValue ? _this.item.chartData.maxValue : 20) * _this.item.symbolSize // 圆环大小
        },
        itemStyle: {
          normal: {
            show: false,
            color: '#f00'
          }
        },
        data: _this.item.chartData.stationData || []
      },
        // 被攻击点
      {
        type: 'effectScatter',
        coordinateSystem: 'geo',
        zlevel: 2,
        rippleEffect: {
          period: 4,
          brushType: _this.item.EffectbrushType,
          scale: _this.item.Effectscale
        },
        label: {
          normal: {
            show: true,
            position: _this.item.labelposition,
            formatter: '{b}',
            textStyle: {
              color: _this.item.labelcolor,
              fontSize: _this.item.labeltextSize
            }
          }
        },
        symbol: 'pin',
        symbolSize: _this.item.geosymbolSize,
        data: _this.item.chartData.Statistical || []
      }
      )
      this.mychart = echarts.init(document.getElementById(this.echartId))
      echarts.registerMap('china', china)
      let myoption = {
        tooltip: {
          trigger: 'item',
          backgroundColor: _this.item.tooltipBackColor,
          borderColor: '#FFFFCC',
          textStyle: {
            color: _this.item.tooltipTextColor,
            fontSize: _this.item.tooltipTextfontSize
          },
          showDelay: 0,
          hideDelay: 0,
          enterable: true,
          transitionDuration: 0,
          extraCssText: 'z-index:100',
          formatter: function (params, ticket, callback) {
            // 根据业务自己拓展要显示的内容
            var res = ''
            var name = params.name
            var value = params.value[2] === undefined ? params.value : params.value[2]
            res = '<span>' + name + '</span><br/>数据：' + value
            return res
          }
        },
        backgroundColor: '',
        visualMap: { // 图例值控制
          min: _this.item.chartData.minValue || 0,
          max: _this.item.chartData.maxValue || 10,
          calculable: _this.item.calculable === 'true',
          show: _this.item.visualMapShow === 'true',
          color: _this.item.ctColors,
          textStyle: {
            color: _this.item.visualMapTextcolor
          }
        },
        geo: {
          map: 'china',
          zoom: 1.2,
          label: {
            emphasis: {
              show: _this.item.labelemphasis === 'true',
              color: _this.item.textStyleColor,
              fontSize: _this.item.labelfontSize
            }
          },
          roam: _this.item.roam === 'true', // 是否允许缩放
          itemStyle: {
            normal: {
              color: _this.item.normalcolor, // 地图背景色
              borderColor: _this.item.normalborderColor, // 省市边界线00fcff 516a89
              borderWidth: _this.item.normalborderWidth
            },
            emphasis: {
              color: _this.item.emphasis // 悬浮背景
            }
          }
        },
        series: series
      }
      this.mychart.setOption(myoption)
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
