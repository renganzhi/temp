<template>
  <div class="waterWave"
       v-if="!empty">
    <div class="liquidfill-chart"
         ref="liquidfill"
         :style="boxStyle"></div>
    <div class="name"
         v-show="item.ctLegendShow != 'false'">{{item.chartData.name}}</div>
  </div>
  <div class="v-charts-data-empty"
       v-else
       style="width: 100%; height: 100%; text-align: center; font-size: 12px;">
    <div><i class="icon-n-nodata"
         style="font-size: 108px;"></i><br>
      <p>抱歉，没有数据可供展示...</p>
    </div>
  </div>
</template>
<script>
import echarts from 'echarts'
export default {
  name: 'liquidfill',
  props: ['item'],
  data () {
    return {
      chart: null,
      empty: false,
      option: {}
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
        this.chart.resize()
      })
    },
    'item.height': function () {
      this.$nextTick(() => {
        this.chart.resize()
      })
    },
    'item.bgClr': function (newV) {
      this.chart.clear()
      this.option.series[0].itemStyle.color = newV
      this.option.series[0].emphasis.itemStyle.color = newV
      this.chart.setOption(this.option)
    },
    'item.clr': function (newV) {
      this.option.series[0].label.color = newV
      this.option.series[0].label.insideColor = newV
      this.chart.setOption(this.option)
    },
    'item.bdClr': function (newV) {
      this.option.series[0].outline.itemStyle.borderColor = newV
      this.chart.setOption(this.option)
    },
    'item.bdpx': function (newV) {
      this.option.series[0].outline.itemStyle.borderWidth = newV
      this.chart.setOption(this.option)
    },
    'item.fontSize': function (newV) {
      this.option.series[0].label.fontSize = newV
      this.chart.setOption(this.option)
    },
    'item.chartData': function () {
      if (this.item.chartData.rows && this.item.chartData.rows.length === 0) {
        this.empty = true
        return
      }
      if (!this.item.chartData.value && this.item.chartData.value!==0) {
        this.empty = true
        return
      }
      this.empty = false
      this.$nextTick(() => {
        this.initBall()
      })
    }
  },
  methods: {
    // API: https://github.com/ecomfe/echarts-liquidfill#api
    initBall () {
      // this.chart = echarts.init(document.getElementById('liquidfill-chart'))
      this.chart = echarts.init(this.$refs.liquidfill, null, { renderer: 'svg' })
      // var option = {
      //   series: [{
      //     type: 'liquidFill',
      // data: [0.5, {
      //   name: '数据名称',
      //   value: '0.5',
      //   direction: 'right',
      //   itemStyle: {
      //     normal: {
      //       color: 'red'
      //     }
      //   }
      // }],
      //   }]
      // }
      this.option = {
        series: [
          {
            type: 'liquidFill',
            name: '系列名称',
            // waveAnimation: false, // 禁止左右波动
            animationDuration: 0,
            animationDurationUpdate: 0, // 更改数值时候的动画时长
            data: [0.8, 0.75],
            itemStyle: {
              color: this.item.bgClr
            },
            emphasis: {
              itemStyle: {
                // opacity: 0.9,
                color: this.item.bgClr
              }
            },
            // color: ['#294D99', '#156ACF', '#1598ED', '#45BDFF'],
            // itemStyle: {
            //   opacity: 0.6
            // },
            // itemStyle: {
            //   shadowBlur: 30,
            //   shadowColor: 'rgba(0, 0, 0, 0.5)'
            // },
            outline: {
              // show: false
              borderDistance: 5,
              itemStyle: {
                borderWidth: this.item.bdpx,
                borderColor: this.item.bdClr
                // shadowBlur: 20,
                // shadowColor: 'rgba(255, 0, 0, 1)'
              }
            },
            backgroundStyle: {
              color: 'transparent',
              shadowColor: 'rgba(0, 0, 0, 0)',
              shadowBlur: 20
            },
            label: {
              fontWeight: 'normal',
              fontSize: this.item.fontSize,
              formatter: '80%', // 显示在水球图中间的文字，可以是字符串，可以是占位符，也可以是一个函数。
              // 如果使用{a}\n{b}\nValue: {c} ，a代表系列名称，b代表数据名称，c代表数据值。
              color: this.item.clr, // 默认背景下的文字颜色
              insideColor: this.item.clr // 水波背景下的文字颜色
            }
          }
        ]
      }
      // if (this.item.chartData.unit.trim() === '%') {
        this.option.series[0].label.formatter = this.item.chartData.value + this.item.chartData.unit
        let val = Number(this.item.chartData.value)
        if (val === val && val > 0) {
          // 非NaN
          let data = parseInt(val * 100) / 10000
          this.option.series[0].data = [data, data - 0.05]
        } else {
          this.option.series[0].data = [-1]
        }
      // }
      if (this.chart) {
        this.chart.setOption(this.option)
      }
    }
  },
  mounted () {
    if ((this.item.chartData.rows && this.item.chartData.rows.length === 0) || (!this.item.chartData.value && this.item.chartData.value!==0)) {
      this.empty = true
    } else {
      this.initBall()
    }
  },
  beforeDestroyed () {
    this.chart.dispose()
  },
  destroyed () {
  }
}
</script>
<style scoped>
.waterWave {
  text-align: center;
  position: relative;
}
.waterWave .name {
  position: absolute;
  text-align: center;
  width: 100%;
  top: 80%;
  font-size: 14px;
  color: #666f8b;
}
</style>
