<template>
  <div class="TreeMap">
    <div
      ref="myTreeMap"
      :style="boxStyle">
    </div>
  </div>

</template>
<script>
import echarts from 'echarts'
export default {
  name: 'TreeMap',
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
      this.mychart = echarts.init(this.$refs.myTreeMap)
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
        tooltip: {
          show: this.item.showTooltip,
          backgroundColor: this.item.TooltipBackColor,
          textStyle: {
            color: this.item.TooltipColor,
            fontSize: this.item.TooltipSize
          }
        },
        color: mycolorArry,
        series: [{
          name: this.item.chartData.name,
          type: 'treemap',
          leafDepth: this.item.leafDepth === 'false' ? this.item.leafDepthSelf : this.item.leafDepth,
          roam: this.item.roam,
          breadcrumb: {
            show: this.item.breadcrumb,
            left: 'center',
            top: this.item.breadcrumbStation,
            itemStyle: {
              color: this.item.breadcrumbBackColor,
              textStyle: {
                color: this.item.breadcrumbColor,
                fontSize: this.item.breadcrumbsize
              }
            }
          },
          label: {
            color: this.item.fontColor,
            fontSize: this.item.fontSize
          },
          itemStyle: {
            borderColor: this.item.gapWidthColor,
            gapWidth: this.item.gapWidth
          },
          data: this.item.chartData.dataArry
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
