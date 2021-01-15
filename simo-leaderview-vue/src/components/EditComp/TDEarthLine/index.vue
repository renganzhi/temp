<template>
  <div class="TDEarth" >
    <div class="TDEarth"
      ref="TDEarth"
      id="TDEarth"
      :style="boxStyle">
    </div>
  </div>
</template>
<script>
import echarts from 'echarts'
import 'echarts-gl'
export default {
  name: 'TDEarth',
  props: ['item'],
  data () {
    return {
      mychart: null
    }
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
    'item.scanningspot': function () {
      this.drawLine()
    },
    'item.scanningradiu': function () {
      this.drawLine()
    },
    'item.scanninglength': function () {
      this.drawLine()
    },
    'item.scanningopcity': function () {
      this.drawLine()
    },
    'item.scanningcolor': function () {
      this.drawLine()
    },
    'item.scanningspeed': function () {
      this.drawLine()
    },
    'item.linewidth': function () {
      this.drawLine()
    },
    'item.lineColor': function () {
      this.drawLine()
    },
    'item.lineoption': function () {
      this.drawLine()
    },
    'item.needrotate': function () {
      this.drawLine()
    },
    'item.rotatedirection': function () {
      this.drawLine()
    },
    'item.rotatespeed': function () {
      this.drawLine()
    },
    'item.norotatetime': function () {
      this.drawLine()
    },
    'item.distance': function () {
      this.drawLine()
    },
    'item.alpha': function () {
      this.drawLine()
    },
    'item.chartData': function () {
      this.drawLine()
    },
    'item.ambientcolor': function () {
      this.drawLine()
    },
    'item.ambientintensity': function () {
      this.drawLine()
    },
    'item.maincolor': function () {
      this.drawLine()
    },
    'item.mainintensity': function () {
      this.drawLine()
    },
    'item.mainbeta': function () {
      this.drawLine()
    },
    'item.mainalpha': function () {
      this.drawLine()
    },
    'item.displacementScale': function () {
      this.drawLine()
    },
    'item.shadingtype': function () {
      this.drawLine()
    },
    'item.symbolSize': function () {
      this.drawLine()
    },
    'item.symbolcolor': function () {
      this.drawLine()
    },
    'item.symbolopacity': function () {
      this.drawLine()
    }
  },
  methods: {
    drawLine () {
      let _this = this
      var pointsData = []
      _this.item.chartData.lineArry.forEach(function (airline) {
        pointsData.push(airline.station)
      })
      var series = _this.item.chartData.lineArry.map(function (airline) {
        return {
          type: 'lines3D',
          name: airline.name || '',
          effect: {
            show: _this.item.scanningspot === 'true', // 是否开启扫描点
            trailWidth: _this.item.scanningradiu, // 扫描的线条高度
            trailLength: _this.item.scanninglength, // 扫描点的长度
            constantSpeed: _this.item.scanningspeed, // 扫描点的长度
            trailOpacity: _this.item.scanningopcity, // 扫描点的透明度
            trailColor: airline.scanningcolor || _this.item.scanningcolor // 扫描点的颜色
          },
          lineStyle: {
            width: _this.item.linewidth, // 线条宽度
            color: airline.linecolor || _this.item.lineColor, // 线条颜色
            opacity: _this.item.lineoption // 线条透明度
          },
          blendMode: 'lighter',
          data: airline.station
        }
      }).filter(function (series) {
        return !!series
      })
      let pointArry = []
      pointsData.forEach(element => {
        element.forEach(d => {
          d.forEach(data => {
            pointArry.push(data)
          })
        })
      })
      series.push({
        type: 'scatter3D',
        coordinateSystem: 'globe',
        blendMode: 'lighter',
        symbolSize: _this.item.symbolSize, // 落地圆的大小
        itemStyle: {
          color: _this.item.symbolcolor, // 落地圆颜色
          opacity: _this.item.symbolopacity // 落地圆透明度
        },
        data: pointArry
      })
      this.mychart = echarts.init(this.$refs.TDEarth)
      var myoption = {
        backgroundColor: '', // 背景颜色  不填为透明
        globe: {
          baseTexture: require('../../../../static/img/earthPic/Mapcolor.jpg'),

          heightTexture: require('../../../../static/img/earthPic/Mapcolor.jpg'),
          displacementScale: _this.item.displacementScale, // 凹凸度
          shading: _this.item.shadingtype, // color   realistic
          baseColor: '#fff',
          displacementQuality: 'high',
          realisticMaterial: {
            roughness: 0.2,
            metalness: 0
          },

          postEffect: {
            enable: true,
            depthOfField: {
              // enable: true
            }
          },
          light: {
            ambient: {
              color: _this.item.ambientcolor, // 环境光的颜色
              intensity: _this.item.ambientintensity // 环境光亮度
            },
            main: {
              color: _this.item.maincolor,
              intensity: _this.item.mainintensity, // 主光源亮度
              beta: _this.item.mainbeta,
              alpha: _this.item.mainalpha
            }
          },
          viewControl: {
            autoRotate: _this.item.needrotate === 'true', // 自动旋转
            autoRotateDirection: _this.item.rotatedirection, // 旋转方向 cw 或者 ccw
            autoRotateSpeed: _this.item.rotatespeed, // 旋转速度  单位 角度/秒
            autoRotateAfterStill: _this.item.norotatetime, // 鼠标操作后静止多久后开始旋转
            distance: 200, // 视角距离地图的高度
            alpha: _this.item.alpha // 俯仰角
          }
        },
        series: series
      }
      let myoption2 = {
        visualMap: {
          show: false,
          min: 0,
          max: 60,
          inRange: {
            symbolSize: [1.0, 10.0]
          }
        },
        backgroundColor: '', // 背景颜色  不填为透明
        globe: {

          baseTexture: require('../../../../static/img/earthPic/Mapcolor.jpg'),

          heightTexture: require('../../../../static/img/earthPic/Mapcolor.jpg'),

          displacementScale: 0.05,
          displacementQuality: 'high',

          globeOuterRadius: 100,

          baseColor: '#000',

          shading: 'realistic',
          realisticMaterial: {
            roughness: 0.2,
            metalness: 0
          },

          postEffect: {
            enable: true,
            depthOfField: {
              focalRange: 15
            }
          },
          temporalSuperSampling: {
            enable: true
          },
          light: {
            ambient: {
              intensity: 0
            },
            main: {
              intensity: 0.1,
              shadow: false
            },
            ambientCubemap: {
              texture: 'lake.hdr', // 请求不是路径
              exposure: 1,
              diffuseIntensity: 0.5,
              specularIntensity: 2
            }
          },
          viewControl: {
            autoRotate: false,
            beta: 180,
            alpha: 20,
            distance: 100
          }
        }
      }
      // this.mychart.dispose()
      // this.mychart.setOption(myoption2)
      this.mychart.setOption(myoption)
    }
  },
  mounted () {
    this.drawLine()
  },
  beforeDestroy () {
    this.mychart.dispose()
    this.mychart = null
  }
}
</script>
