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
      mychart: null,
      oldItem: ''
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
        if (this.oldItem === JSON.stringify(newVal)) {

        } else {
          this.oldItem = JSON.stringify(newVal)
          this.drawFlow()
        }
        // this.drawFlow()
      },
      deep: true
    }
  },
  methods: {
    drawFlow () {
      this.mychart = echarts.init(this.$refs.myTDHistogram)
      let mydata = this.item.chartData.dataArray
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
      this.item.grid3DColorArray.forEach(element => {
        mycolorArry.push(element)
      })
      let myoption = {
        backgroundColor: 'transparent',
        bevelSmoothness: 20,
        // emphasis:{
        //     itemStyle:{
        //         color:'black'
        //     }
        // },
        tooltip: {
          show: this.item.tooltipShow,
          backgroundColor: this.item.tooltipBackColor,
          textStyle: {
            color: this.item.tooltipColor,
            fontSize: this.item.tooltipSize
          }
        },
        visualMap: {
          show: false,
          min: valMin,
          max: valMax,
          inRange: {
            color: mycolorArry
          }
        },
        xAxis3D: {
          name: this.item.chartData.xAxis.name,
          nameTextStyle: {
            fontSize: this.item.grid3DDWFontSize
          },
          data: this.item.chartData.xAxis.value,
          type: 'category'
        },
        yAxis3D: {
          name: this.item.chartData.yAxis.name,
          nameTextStyle: {
            fontSize: this.item.grid3DDWFontSize
          },
          data: this.item.chartData.yAxis.value,
          type: 'category'
        },
        zAxis3D: {
          name: this.item.chartData.zAxis.name,
          nameTextStyle: {
            fontSize: this.item.grid3DDWFontSize
          },
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
            projection: 'perspective',
            autoRotate: this.item.autoRotate, // 自动旋转
            autoRotateDirection: this.item.autoRotateDirection, // 旋转方向 cw 或者 ccw
            autoRotateSpeed: this.item.autoRotateSpeed, // 旋转速度  单位 角度/秒
            autoRotateAfterStill: this.item.autoRotateAfterStill, // 鼠标操作后静止多久后开始旋转
            distance: this.item.distance, // 视角的高度
            alpha: this.item.alpha, // 俯仰角
            beta: this.item.beta // 俯仰角
          }
        },
        series: [{
          name: this.item.chartData.name,
          type: 'bar3D',
          data: this.item.chartData.dataArray,
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
