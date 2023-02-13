<template>
  <div class="Pro" ref="progress"></div>
</template>
<script>
export default {
  name: 'progress',
  props: ['successdata', 'progressType', 'color'],
  data () {
    return {
      mychart: null,
      options: {}
    }
  },
  mounted () {
    this.mychart = echarts.init(this.$refs.progress)
    setTimeout(() => {
      if (this.progressType === 2) {
        this.setOption2()
      } else {
        this.setOption()
      }
    }, 100)
  },
  methods: {
    setOption () {
      this.options = {
        xAxis: [
          {
            type: 'value',
            show: false,
            max: 100
          }
        ],
        grid: {
          left: '0',
          right: '0'
        },
        yAxis: [
          {
            show: false,
            type: 'category',
            axisTick: {
              show: false
            },
            axisLabel: {
              show: false
            },
            axisLine: {
              lineStyle: {
                width: 0,
                color: '#1dfffd'
              }
            },
            z: 3
          }
        ],
        series: [
          {
            name: 'Income',
            type: 'bar',
            barGap: '-100%',
            barWidth: 20,
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 1,
              y2: 1,
              colorStops: [
                {
                  offset: 0,
                  color: '#3df8c266' // 0% 处的颜色
                },
                {
                  offset: 1,
                  color: '#3DF8C2' // 100% 处的颜色
                }
              ],
              global: false // 缺省为 false
            },
            itemStyle: {
              barBorderRadius: [10, 10, 10, 10],
              borderWidth: 0,
              borderColor: 'rgba(0,0,0,0)'
            },
            data: [100]
          },
          {
            name: 'Income',
            type: 'bar',
            barGap: '-100%',
            color: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 1,
              y2: 1,
              colorStops: [
                {
                  offset: 0,
                  color: '#fbd7b3' // 100% 处的颜色
                },
                {
                  offset: 1,
                  color: '#F59B42' // 0% 处的颜色
                }
              ],
              global: false // 缺省为 false
            },
            barWidth: 20,
            itemStyle: {
              barBorderRadius: [10, 10, 50, 10],
              borderWidth: 0,
              borderColor: 'rgba(0,0,0,0)'
            },
            data: [this.successdata]
          }
        ]
      }
      this.mychart.setOption(this.options, true)
    },
    setOption2 () {
      this.options = {
        grid: {
          bottom: '0',
          left: '0',
          right: '0',
          top: '0'
        },
        xAxis: {
          show: false,
          type: 'value',
          max: 100
        },
        yAxis: [
          {
            type: 'category',
            inverse: true,
            axisTick: 'none',
            axisLine: 'none',
            show: false
          }
        ],
        series: [
          {
            type: 'bar',
            // showBackground: true,
            // backgroundStyle: {
            //   color: 'rgba(180, 180, 180, 0.2)',
            //   borderRadius: 0
            // },
            label: {
              show: true,
              position: 'right',
              formatter: '{@score}%',
              textStyle: {
                color: '#C5EEF3',
                fontSize: '24'
              }
            },
            itemStyle: {
              normal: {
                barBorderRadius: 0,
                color: new echarts.graphic.LinearGradient(1, 0, 0, 0, [
                  {
                    offset: 0,
                    color: this.color[0] || '#61BEF5'
                  },
                  {
                    offset: 1,
                    color: this.color[1] || '#61bef533'
                  }
                ])
              }
            },
            barWidth: 20,
            data: [this.successdata]
          }
        ]
      }
      this.mychart.setOption(this.options, true)
    }
  }
}
</script>
<style lang="scss" scoped>
.Pro{
  width: 100%;
  // margin-left: -38px;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
