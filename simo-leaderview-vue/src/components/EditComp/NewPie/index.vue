<template>
  <div class="NewPie">
    <div
      ref="NewPie"
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
  name: 'NewPie',
  props: ['item'],
  data () {
    return {
      defaultcolor:[
      '#2d98f1',
      '#32c5e9',
      '#67e0e3',
      '#9fe6b8',
      '#ffdb5c',
      '#ffb092'],
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
      this.mychart = echarts.init(this.$refs.NewPie)
      let myData = this.item.chartData
      let optioncolor = []
      if(this.item.ifEidetColor)
      {
          if (this.item.ifGradual === 'true') {
          this.item.DLineColorArray.forEach((element, index) => {
            optioncolor.push(new echarts.graphic.LinearGradient(0, 0, 1, 1, [{ offset: 0, color: element[0] }, { offset: 1, color: element[1] }]))
          })
        } else {
          optioncolor = this.item.LineColorArray
        }
      }
      else
      {
        optioncolor=this.defaultcolor
      }
     
      var SericeData = []
      myData.rows.forEach(element => {
        SericeData.push({
          name: element[myData.columns[0]],
          value: element[myData.columns[1]] * 1
        })
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
        series: [
          {
            type: 'pie',
            roseType: this.item.roseType || false,
            labelLine: {
              normal: {
                show: this.item.showline
              }
            },
            // radius:,
            radius: this.item.isRing ? [(this.item.radius - this.item.detailwidth) > 0 ? (this.item.radius - this.item.detailwidth) + '%' : 0 + '%', this.item.radius + '%'] : ['0%', this.item.radius + '%'],
            data: SericeData,
            emphasis: {
              itemStyle: {

              }
            }
          }
        ]
      }
      if (this.oldOption !== JSON.stringify(myoption)) {
        this.oldOption = JSON.stringify(myoption)
        this.mychart.setOption(myoption,true)
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
