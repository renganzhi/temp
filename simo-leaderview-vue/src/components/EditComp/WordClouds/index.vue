<template>
  <div class="WordClouds">

    <div ref="WordClouds" :style="boxStyle" @click="showList"></div>
    <div
      class="v-charts-data-empty"
      v-show="!showLine"
      style="width: 100%; height: 100%; text-align: center; font-size: 12px"
    >
      <div>
        <i class="icon-n-nodata" style="font-size: 108px"></i><br />
        <p>抱歉，没有数据可供展示...</p>
      </div>
    </div>
  </div>
</template>
<script>
import echarts from 'echarts'
export default {
  name: 'WordClouds',
  props: ['item'],
  data () {
    return {
      defaultcolor: [
        '#2d98f1',
        '#32c5e9',
        '#67e0e3',
        '#9fe6b8',
        '#ffdb5c',
        '#ffb092'
      ],
      mychart: null,
      showLine: true,
      oldOption: ''
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
    item: {
      handler (newVal, oldVal) {
        if (this.item.chartData.rows) {
          if (
            this.item.chartData.rows.length === 0
          ) {
            this.showLine = false
          } else {
            this.showLine = true
          }
        }
        this.drawFlow()
      },
      deep: true
    }
  },
  methods: {
    drawFlow () {
      this.mychart = echarts.init(this.$refs.WordClouds)
      let myData = this.item.chartData
      let _this = this
      let mySeries = [{
        type: 'wordCloud',
        gridSize: this.item.gridSize,
        shape: 'rect',
        sizeRange: [25, this.item.maxSize],
        rotationRange: [0, 0, 0, 0],
        //   maskImage: maskImage,
        textStyle: {
          normal: {
            color: function (v) {
              let i = v.dataIndex % _this.item.textColor.length
              return _this.item.textColor[i]
            }
          }
        },
        left: 'center',
        top: 'center',
        // width: '96%',
        // height: '100%',
        right: null,
        bottom: null,
        // width: 300,
        // height: 200,
        // top: 20,
        data: myData.rows.slice(0, 11) // 限制词云数量为10个
      }]
      let option = {
        tooltip: {
          show: false
        },
        series: mySeries
      }
      if (this.oldOption !== JSON.stringify(option)) {
        this.oldOption = JSON.stringify(option)
        this.mychart.setOption(option, true)
      }
    },
    showList () {
      this.$parent.$parent.ShowTablePop(this.item.chartData)
    }
  },
  mounted () {
    if (
      !this.item.chartData.rows ||
       this.item.chartData.rows.length === 0
    ) {
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
