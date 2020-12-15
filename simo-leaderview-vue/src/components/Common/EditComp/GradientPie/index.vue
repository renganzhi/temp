<template>
  <div class="GradientPie" >
    <div id="GradientPie2"
      :style="boxStyle">
    </div>
    <div id="GradientPie1"
      :style="boxStyle">
    </div>
  </div>
</template>
<script>
import echarts from 'echarts'
export default {
  name: 'GradientPie',
  props: ['item'],
  data () {
    return {
      mychart1: null,
      mychart2: null
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
        this.mychart1.resize()
        this.mychart2.resize()
      })
    },
    'item.height': function () {
      this.$nextTick(() => {
        this.mychart1.resize()
        this.mychart2.resize()
      })
    }
  },
  methods: {
    drawPre () {
      this.mychart1 = echarts.init(document.getElementById('GradientPie1'))
      let myoption1 = {
        // title: {
        //   text: 88 + '分',
        //   textStyle: {
        //     color: '#28BCFE',
        //     fontSize: 40
        //   },
        //   subtext: '综合得分',
        //   subtextStyle: {
        //     color: '#666666',
        //     fontSize: 30
        //   },
        //   itemGap: 20,
        //   left: 'center',
        //   top: '43%'
        // },
        tooltip: {
          formatter: function (params) {
            return '<span style="color: #fff;">综合得分：' + params.seriesName + '分</span>'
          }
        },
        angleAxis: {
          max: 100,
          clockwise: true, // 逆时针
          // 隐藏刻度线
          show: false
        },
        radiusAxis: {
          type: 'category',
          show: true,
          axisLabel: {
            show: false
          },
          axisLine: {
            show: false

          },
          axisTick: {
            show: false
          }
        },
        polar: {
          center: ['50%', '50%'],
          radius: '100%' // 图形大小
        },
        series: [{
          type: 'bar',
          data: [66 / 4 * 3],
          showBackground: true,
          backgroundStyle: {
            color: 'transparent'
          },
          name: '66',
          coordinateSystem: 'polar',
          roundCap: true,
          barWidth: 30,
          itemStyle: {
            normal: {
              opacity: 1,
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: 'rgba(250, 141, 118, 0.94)'
              }, {
                offset: 1,
                color: 'rgba(27, 188, 174, 0.97)'
              }]),
              shadowBlur: 5,
              shadowColor: '#2A95F9'
            }
          }
        }, {
          type: 'bar',
          data: [77 / 4 * 3],
          showBackground: true,
          backgroundStyle: {
            color: 'transparent'
          },
          name: '77',
          coordinateSystem: 'polar',
          roundCap: true,
          barWidth: 30,
          itemStyle: {
            normal: {
              opacity: 1,
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: 'rgb(24, 183, 142)'
              }, {
                offset: 1,
                color: 'rgb(1, 179, 238)'
              }]),
              shadowBlur: 5,
              shadowColor: '#2A95F9'
            }
          }
        }],
        legend: {
          show: true,
          data: ['Without Round Cap', 'With Round Cap']
        }
      }
      this.mychart1.setOption(myoption1)
      this.mychart2 = echarts.init(document.getElementById('GradientPie2'))
      let myoption2 = {
        angleAxis: {
          max: 100,
          clockwise: true, // 逆时针
          // 隐藏刻度线
          show: false
        },
        radiusAxis: {
          type: 'category',
          show: true,
          axisLabel: {
            show: false
          },
          axisLine: {
            show: false

          },
          axisTick: {
            show: false
          }
        },
        polar: {
          center: ['50%', '50%'],
          radius: '100%' // 图形大小
        },
        series: [{
          type: 'bar',
          data: [100 / 4 * 3],
          showBackground: true,
          backgroundStyle: {
            color: 'transparent'
          },
          coordinateSystem: 'polar',
          roundCap: true,
          barWidth: 30,
          itemStyle: {
            normal: {
              opacity: 1,
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: 'rgba(250, 141, 118, 0.1)'
              }, {
                offset: 1,
                color: 'rgba(27, 188, 174, 0.1)'
              }]),
              shadowBlur: 5,
              shadowColor: '#2A95F9'
            }
          }
        }, {
          type: 'bar',
          data: [100 / 4 * 3],
          showBackground: true,
          backgroundStyle: {
            color: 'transparent'
          },
          coordinateSystem: 'polar',
          roundCap: true,
          barWidth: 30,
          itemStyle: {
            normal: {
              opacity: 1,
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: 'rgb(24, 183, 142,.1)'
              }, {
                offset: 1,
                color: 'rgb(1, 179, 238,.1)'
              }]),
              shadowBlur: 5,
              shadowColor: '#2A95F9'
            }
          }
        }]
      }
      this.mychart2.setOption(myoption2)
    }
  },
  mounted () {
    this.drawPre()
  }
}
</script>
<style lang="scss" scoped>
.GradientPie{
  div{
    position: absolute;
  }
}
</style>
