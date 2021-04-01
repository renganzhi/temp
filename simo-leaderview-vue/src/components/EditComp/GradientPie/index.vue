<template>
  <div class="GradientPie" ref="Gradient">
    <div id="GradientPie2" ref="Gradient2" :style="boxStyle"></div>
    <div id="GradientPie1" ref="Gradient1" :style="boxStyle"></div>
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
      mychart2: null,
      oldOption1: '',
      oldOption2: ''
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
    },
    item: {
      handler (newVal, oldVal) {
        console.log(newVal)
        this.drawPre()
      },
      deep: true
    }
  },
  methods: {
    drawPre () {
      this.mychart1 = echarts.init(this.$refs.Gradient1)
      let array = this.item.chartData.rows
      let allData = 0
      array.forEach((element) => {
        allData = allData + element.value
      })
      let myseries = []
      let titleText = ''
      let AllNum = 100
      array.forEach((d, index) => {
        let oneserise = {
          type: 'bar',
          data: [((d.value * 100) / allData) * this.item.PieType],
          showBackground: true,
          backgroundStyle: {
            color: 'transparent'
          },
          name: `${d.name}:${d.value}${d.unit || ''}`,
          coordinateSystem: 'polar',
          roundCap: true,
          barGap: this.item.PieSpacing + '%',
          barWidth: this.item.PieRadius,
          itemStyle: {
            normal: {
              opacity: 1,
              color:
                this.item.ifGradual === 'false'
                  ? this.item.ScatterColor[index]
                  : new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                      offset: 0,
                      color: this.item.DScatterColor[index][0]
                    },
                    {
                      offset: 1,
                      color: this.item.DScatterColor[index][1]
                    }
                  ])
            }
          }
        }
        let myVale = Math.floor((d.value * 100) / allData)
        if (index === array.length - 1) {
          myVale = AllNum
        } else {
          AllNum = AllNum - myVale
        }
        if (this.item.NotesType === 1) {
          titleText =
             d.name + ':' + d.value + (d.unit || '') + '\n' + titleText
        } else if (this.item.NotesType === 2) {
          titleText =
            d.name +
            ':' +
            d.value +
            (d.unit || '') +
            '   ' +
            myVale +
            '%' +
            '\n' + titleText
        } else if (this.item.NotesType === 3) {
          titleText =
            d.name +
            ':' +
            d.value +
            (d.unit || '') +
            '----' +
            myVale +
            '%' +
            '\n' + titleText
        }
        myseries.push(oneserise)
      })
      let myoption1 = {
        title: {
          show: this.item.tipsNotes,
          text: titleText,
          textStyle: {
            color: this.item.NotesColor,
            fontSize: this.item.NotesSize,
            lineHeight: 30
          },
          left: this.item.NotesToLeft,
          top: this.item.NotesToTop
        },
        tooltip: {
          show: this.item.tipsShow,
          formatter: (params) => {
            return `<span style="color: ${this.item.tipsColor}; font-size: ${this.item.tipsSize}px;">${params.seriesName}</span>`
          }
        },
        angleAxis: {
          max: 100,
          clockwise: this.item.direction, // 逆时针
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
        series: myseries
      }
      if (this.oldOption1 !== JSON.stringify(myoption1)) {
        this.oldOption1 = JSON.stringify(myoption1)
        this.mychart1.clear()
        this.mychart1.setOption(myoption1)
      }
      this.mychart2 = echarts.init(this.$refs.Gradient2)
      let myseries2 = []
      array.forEach((d, index) => {
        let oneserise = {
          type: 'bar',
          data: [100 * this.item.PieType],
          showBackground: true,
          backgroundStyle: {
            color: 'transparent'
          },
          coordinateSystem: 'polar',
          roundCap: true,
          barGap: this.item.PieSpacing + '%',
          barWidth: this.item.PieRadius,
          itemStyle: {
            normal: {
              opacity: 0.1,
              color:
                this.item.ifGradual === 'false'
                  ? this.item.ScatterColor[index]
                  : new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                      offset: 0,
                      color: this.item.DScatterColor[index][0]
                    },
                    {
                      offset: 1,
                      color: this.item.DScatterColor[index][1]
                    }
                  ])
            }
          }
        }
        myseries2.push(oneserise)
      })
      let myoption2 = {
        angleAxis: {
          max: 100,
          clockwise: this.item.direction, // 逆时针
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
        series: myseries2
      }
      if (this.oldOption2 !== JSON.stringify(myoption2)) {
        this.oldOption2 = JSON.stringify(myoption2)
        this.mychart2.clear()
        this.mychart2.setOption(myoption2)
      }
    }
  },
  mounted () {
    this.drawPre()
  }
}
</script>
<style lang="scss" scoped>
.GradientPie {
  div {
    position: absolute;
  }
}
</style>
