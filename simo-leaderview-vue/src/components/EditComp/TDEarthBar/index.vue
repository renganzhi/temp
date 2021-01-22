<template>
  <div class="TDEarthBar" >
    <div class="TDEarthBar"
      ref="TDEarthBar"
      id="TDEarthBar"
      :style="boxStyle">
    </div>
  </div>
</template>
<script>
import echarts from 'echarts'
import 'echarts-gl'
export default {
  name: 'TDEarthBar',
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
    'item.metalness': function () {
      this.drawLine()
    },
    'item.roughness': function () {
      this.drawLine()
    },
    'item.shadingtype': function () {
      this.drawLine()
    },
    'item.symbolSize': function () {
      this.drawLine()
    },
    'item.barSize': function () {
      this.drawLine()
    },
    'item.itemStyleColor': function () {
      this.drawLine()
    },
    'item.labelColor': function () {
      this.drawLine()
    },
    'item.labelWeight': function () {
      this.drawLine()
    },
    'item.labelSize': function () {
      this.drawLine()
    },
    'item.labelBorderwidth': function () {
      this.drawLine()
    },
    'item.labelBorderColor': function () {
      this.drawLine()
    },
    'item.labelBorderRadius': function () {
      this.drawLine()
    },
    'item.labellineHeight': function () {
      this.drawLine()
    },
    'item.labelbackColor': function () {
      this.drawLine()
    }
  },
  methods: {
    drawLine () {
      let _this = this
      let barArry = JSON.parse(JSON.stringify(_this.item.chartData.barArry))
      barArry.push({
        value: [0, 0, 0]
      })
      var series = [{
        type: 'bar3D',
        coordinateSystem: 'globe',
        data: barArry,
        barSize: _this.item.barSize,
        minHeight: 0.2,
        silent: true,
        itemStyle: {
          color: _this.item.itemStyleColor
        },
        label: {
          show: false,
          distance: 2,
          formatter: function (a) {
            if (a.data.labelvalue) {
              let labelValue = []
              for (const key in a.data.labelvalue) {
                if (a.data.labelvalue.hasOwnProperty(key)) {
                  labelValue.push(key + ':' + a.data.labelvalue[key])
                }
              }
              return labelValue.join('\n')
            }
          },
          textStyle: {
            color: _this.item.labelColor, // 字体颜色
            fontSize: _this.item.labelSize, // 字体大小
            fontWeight: _this.item.labelWeight, // 字体宽度
            borderWidth: _this.item.labelBorderwidth, // 边框宽度
            borderColor: _this.item.labelBorderColor, // 边框颜色
            borderRadius: _this.item.labelBorderRadius, // 圆角大小
            padding: [10, 10], // pandding值
            lineHeight: _this.item.labellineHeight, // 行高
            backgroundColor: _this.item.labelbackColor // 背景颜色
          }
        }
      }]
      this.mychart = echarts.init(this.$refs.TDEarthBar)
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
            roughness: _this.item.roughness,
            metalness: _this.item.metalness
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

          environment: require('../../../../static/img/earthPic/Mapcolor.jpg'),

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
