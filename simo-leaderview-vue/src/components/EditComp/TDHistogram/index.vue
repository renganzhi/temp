<template>
  <div class="TDHistogram">
    <div
      ref="myTDHistogram"
      :style="boxStyle">
    </div>
  </div>

</template>
<script>
import echarts from 'echarts'
export default {
  name: 'TDHistogram',
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
    'item': {
      handler (newVal, oldVal) {
        this.drawFlow()
      },
      deep: true
    }
  },
  methods: {
    drawFlow () {
      this.mychart = echarts.init(this.$refs.myTDHistogram)
      let mydata = this.item.chartData.dataArry
      var valMin = Infinity
      var valMax = -Infinity
      mydata.forEach(d => {
        if (d[2] > valMax) {
          valMax = d[2]
        }
        if (d[2] < valMin) {
          valMin = d[2]
        }
      })
      let mycolorArry = []
      this.item.ctColors.forEach(element => {
        mycolorArry.push(this.item.ifGradual === 'false' ? element : new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
          offset: 0,
          color: element[0]
        }, {
          offset: 1,
          color: element[1]
        }]))
      })
      let myoption = {
        backgroundColor: 'transparent',
        bevelSmoothness: 20,
        // emphasis:{
        //     itemStyle:{
        //         color:'black'
        //     }
        // },
        visualMap: {
          show: false,
          min: valMin,
          max: valMax,
          inRange: {
            color: mycolorArry
          }
        },
        xAxis3D: {
          type: 'value'
        },
        yAxis3D: {
          type: 'value'
        },
        zAxis3D: {
          type: 'value',
          max: Math.floor(valMax + valMax * 0.2),
          min: 0
        },
        grid3D: {
          show: this.item.showgrid3D,
          axisLine: {
            lineStyle: { color: this.item.grid3DLineColor }
          },
          axisLabel: {
            textStyle: {
              fontSize: this.item.grid3DFontSize
            }
          },
          splitLine: {
            lineStyle: { color: this.item.grid3DLineColor }
          },
          axisPointer: {
            lineStyle: { color: this.item.grid3DHLolor },
            label: {
              textStyle: {
                fontSize: this.item.grid3DlineSize
              }
            }
          },
          viewControl: {
            projection: 'orthographic',
            autoRotate: false
          },
          light: {
            main: {
              shadow: true,
              quality: 'ultra',
              intensity: 1.5
            }
          }
        },
        series: [{
          type: 'bar3D',
          data: this.item.chartData.dataArry,
          shading: 'lambert',
          barSize: this.item.barSize,
          label: {
            formatter: function (param) {
              return param.value[2].toFixed(1)
            }
          }
        }]
      }
      this.mychart.clear()
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
