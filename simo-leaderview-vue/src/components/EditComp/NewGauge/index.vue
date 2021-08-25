<template>
  <div class="NewGauge">
    <div
      ref="NewGauge"
      v-show="showValue"
      :style="boxStyle">
    </div>
    <div class="v-charts-data-empty"
        v-show="!showValue"
        style="width: 100%; height: 100%; text-align: center; font-size: 12px;">
        <div><i class="icon-n-nodata"
            style="font-size: 108px;"></i><br>
          <p>抱歉，没有数据可供展示...</p>
        </div>
    </div>
  </div>

</template>
<script>
import echarts from 'echarts'
export default {
  name: 'NewGauge',
  props: ['item'],
  data () {
    return {
      mychart: null,
      showValue: true,
      oldOption: '',
      Linesubsection: '',
      oldformatterType: '',
      oldchartData: ''
    }
  },
  computed: {
    boxStyle: function () {
      return {
        width: this.item.width + 'px',
        height: this.item.height + 'px'
      }
    },
    maxData: function () {
      return this.item.chartData.max || 10000
    },
    minData: function () {
      return this.item.chartData.min || 0
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
        if (this.item.chartData) {
          this.showValue = true
          this.drawFlow()
        } else {
          this.showValue = false
        }
      },
      deep: true
    }
  },
  methods: {
    drawFlow () {
      this.mychart = echarts.init(this.$refs.NewGauge)
      let myData = this.item.chartData
      let myoption = {
        title: [{
          text: myData.name,
          x: 'center',
          top: this.item.legendY + '%',
          show: this.item.ctLegendShow,
          textStyle: {
            color: this.item.ctLegendColor,
            fontSize: this.item.ctLegendSize,
            fontWeight: '100'
          }
        }, {
          text: myData.value + myData.unit,
          x: 'center',
          show: this.item.legendY,
          // top: this.item.NamelegendY + '%',
          top: 'middle',
          textStyle: {
            fontSize: this.item.NameSize,
            color: this.item.NameColor,
            fontFamily: 'Lato',
            foontWeight: '600'
          }
        }],
        polar: {
          radius: [70 - this.item.detailwidth + '%', '70%'],
          center: ['50%', '50%']
        },
        angleAxis: {
          max: 100,
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
        series: [
          {
            name: '',
            type: 'bar',
            roundCap: true,
            barWidth: 60,
            showBackground: true,
            backgroundStyle: {
              color: this.item.bgClr
            },
            data: [myData.value],
            coordinateSystem: 'polar',

            itemStyle: {
              normal: {
                color: this.item.colorful === 'true' ? new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
                  offset: 0,
                  color: this.item.barClrs[0]
                }, {
                  offset: 1,
                  color: this.item.barClrs[1]
                }]) : this.item.barClr
              }
            }

          }
        ]
      }
      if (this.oldOption !== JSON.stringify(myoption)) {
        this.oldOption = JSON.stringify(myoption)
        this.mychart.setOption(myoption)
      }
    }
  },
  mounted () {
    if (this.item.chartData) {
      this.showValue = true
      this.drawFlow()
    } else {
      this.showValue = false
    }
  },
  beforeDestroy () {
    if (this.mychart) {
      this.mychart.dispose()
    }
    this.mychart = null
  }

}
</script>
