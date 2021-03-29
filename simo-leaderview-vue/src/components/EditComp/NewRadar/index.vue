<template>
  <div class="NewRadar">
    <div
      ref="NewRadar"
      v-show="showLine"
      :style="boxStyle">
    </div>
    <div class="v-charts-data-empty"
        v-show="!showLine"
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
  name: 'NewRadar',
  props: ['item'],
  data () {
    return {
      mychart: null,
      showLine: true,
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
        if (this.item.chartData.rows.length === 0 || this.item.chartData.columns.length === 0) {
          this.showLine = false
        } else {
          this.showLine = true
          this.drawFlow()
        }
      },
      deep: true
    }
  },
  methods: {
    drawFlow () {
      this.mychart = echarts.init(this.$refs.NewRadar)
      let myData = this.item.chartData
      let optioncolor = []
      if (this.item.ifGradual === 'true') {
        this.item.DLineColorArray.forEach((element, index) => {
          optioncolor.push(new echarts.graphic.LinearGradient(0, 0, 1, 1, [{ offset: 0, color: element[0] }, { offset: 1, color: element[1] }]))
        })
      } else {
        optioncolor = this.item.LineColorArray
      }
      let indicatorArr = []
      let names = myData.columns.slice(1, myData.columns.length)
      if (myData && myData.columns) {
        if (myData.rows) {
          names.forEach((key) => {
            let maxItem = _.maxBy(myData.rows, function (item) { return item[key] })
            let maxVal = Number(maxItem[key]) > 100 ? Number(maxItem[key]) : 100
            indicatorArr.push({
              name: key,
              max: maxVal
            })
          })
        }
      }
      let serirseData = []
      myData.rows.forEach(data => {
        let oneData = {
          value: [],
          name: data[myData.columns[0]]
        }
        names.forEach(d => {
          oneData.value.push(data[d])
        })
        serirseData.push(oneData)
      })
      let myoption = {
        tooltip: {
          trigger: 'item',
          show: this.item.tooltipShow,
          backgroundColor: this.item.tooltipBackColor,
          textStyle: {
            color: this.item.tooltipTextColor,
            fontSize: this.item.tooltipfontSize
          }
        },
        radar: {
          shape: 'polygon', // circle
          splitNumber: 1,
          max: 100,
          indicator: indicatorArr.length > 0 ? indicatorArr : '',
          splitArea: {
            areaStyle: {
              color: 'transparent'
            }
          },
          splitLine: {
            show: this.item.splitShow,
            lineStyle: {
              color: this.item.splitColor || 'rgba(117, 124, 137, 0.2)',
              width: this.item.splitSize || 1
            }
          },
          axisLabel: {
            show: false
          },
          axisLine: {
            show: false
          }
        },
        legend: {
          x: 'center',
          y: this.item.legendY + '%',
          show: this.item.ctLegendShow,
          textStyle: {
            fontSize: this.item.ctLegendSize,
            color: this.item.ctLegendColor
          }
        },
        color: optioncolor,
        series: [{
          type: 'radar',
          symbol: 'none',
          areaStyle: {},
          data: serirseData
        }]
      }
      if (this.oldOption !== JSON.stringify(myoption)) {
        this.oldOption = JSON.stringify(myoption)
        this.mychart.setOption(myoption)
      }
    }
  },
  mounted () {
    if (this.item.chartData.rows.length === 0 || this.item.chartData.columns.length === 0) {
      this.showLine = false
    } else {
      this.showLine = true
      this.drawFlow()
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
