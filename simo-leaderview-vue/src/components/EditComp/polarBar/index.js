import Vue from 'vue'
import echarts from 'echarts'

export default Vue.component('polarBar', {
  template: `<div :style="style">热力图</div>`,
  // render (createElement) {
  //     return createElement('div', {
  //        class: [],
  //        style: {}
  //     }, this.$slots.default)
  // },
  props: ['item'],
  data () {
    return {
      type: 'heatmap',
      count: 0,
      oldOption: ''
    }
  },
  computed: {
    style () {
      return {
        width: `${this.item.width}px`,
        height: `${this.item.height}px`
      }
    }
  },
  mounted () {
    this.rerender()
  },
  watch: {
    'item.width' (newVal, oldVal) {
      // console.log('chang width');
      this.resize()
    },
    'item.height' (newVal, oldVal) {
      // console.log('chang height');
      this.resize()
    },
    'item': {
      handler (newVal, oldVal) {
        this.rerender()
      },
      deep: true
    }
  },
  methods: {
    resize () {
      this.chart.resize()
    },
    rerender () {
      let chartData = this.item.chartData
      var newbarHeight = this.item.newbarHeight
      let dataArray = []
      chartData.columns.forEach(name => {
        let Onedata = {
          name: name,
          value: []
        }
        chartData.rows.forEach(d => {
          Onedata.value.push(d[name])
        })
        dataArray.push(Onedata)
      })
      var myseries = []
      dataArray.forEach((element, index) => {
        if (index > 0) {
          myseries.push({
            type: 'bar',
            itemStyle: {
              color: 'transparent'
            },
            data: element.value.map(function (d) {
              if (d[1]) {
                return d[0]
              } else {
                return d[0] - newbarHeight
              }
            }),
            coordinateSystem: 'polar',
            stack: element.name,
            silent: true,
            z: index
          })
          myseries.push({
            type: 'bar',
            itemStyle: {
              color: this.item.ifGradual === 'false' ? this.item.ScatterColor[index - 1] : new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: this.item.DScatterColor[index - 1][0]
              }, {
                offset: 1,
                color: this.item.DScatterColor[index - 1][1]
              }])
            },
            data: element.value.map(function (d) {
              if (d[1]) {
                return d[1] - d[0]
              } else {
                return 2 * newbarHeight
              }
            }),
            coordinateSystem: 'polar',
            name: element.name,
            barGap: '-100%',
            stack: element.name,
            z: index
          })
        }
      })
      this.option = {
        legend: {
          show: this.item.openlegend,
          textStyle: {
            color: this.item.legendColor,
            fontSize: this.item.legendSize
          },
          left: 'center',
          top: this.item.legendStation
        },
        grid: {
          top: 100
        },
        angleAxis: {
          type: 'category',
          data: dataArray[0].value,
          axisLine: {
            show: true,
            lineStyle: {
              color: this.item.angleAxisColor
            }
          },
          axisLabel: {
            fontSize: this.item.axisLabelSize
          }
        },
        tooltip: {
          trigger: 'item',
          show: this.item.tooltipShow,
          backgroundColor: this.item.tooltipBackColor,
          textStyle: {
            color: this.item.tooltipColor,
            fontSize: this.item.tooltipSize
          },
          formatter: function (params) {
            var id = params.dataIndex
            let stringData = ''
            dataArray.forEach(data => {
              let mydata = data.value[id]
              let value = typeof (mydata) === 'string' ? mydata : data.value[id].join('-')
              stringData = stringData + data.name + ':' + value + '<br>'
            })
            return stringData
          }
        },
        radiusAxis: {
          axisLine: {
            show: true,
            lineStyle: {
              color: this.item.angleAxisColor
            }
          },
          axisLabel: {
            fontSize: this.item.axisLabelSize
          }
        },
        polar: {
        },
        series: myseries
      }
      this.chart = echarts.init(this.$el)
      if (this.oldOption !== JSON.stringify(this.option)) {
        this.oldOption = JSON.stringify(this.option)
        this.chart.setOption(this.option)
      }
    }
  }
})
