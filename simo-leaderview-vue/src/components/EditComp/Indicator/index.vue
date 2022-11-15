<template>
  <div style="text-align: center; height: 100%;">
    <div v-if="numberType === 'number'">
      <span :style="numStyle">
        {{numStr}}
      </span>
    </div>
    <div v-if="numberType === 'percent'">
        <span :style="numStyle2">
          <div :style="triangleStyle">
            <img :src="imgUrl" :style="{width: item.upIcon + 'px'}" alt="">
          </div>{{increase}}
        </span>
        <span :style="numStyle" @click="ShowXq">
          {{numStr2}}
        </span>
    </div>
  </div>
</template>
<script>
export default {
  name: 'NewNumber', // 指标展示
  props: ['item'],
  data () {
    return {
      showOver: true
    }
  },
  watch: {
    'item.chartData': function () {
      if (!this.item.chartData) {
        this.item.chartData = {
          'name': '--',
          'unit': '',
          'value': '-',
          'type': 'number'
        }
      }
    }
  },
  computed: {
    numStr: function () {
      if (this.item.chartData.value !== 0 && !this.item.chartData.value) {
        return '--'
      }
      var num = Math.round(this.item.chartData.value * 100) / 100
      num = num.toString()
      if (!num) {
        return null
      }
      var numArr = num.split('.')
      // var formatStr = this.toThousands(numArr[0])
      // 英文逗号显示不完全
      var formatStr = numArr[0]
      if (numArr[1]) {
        return formatStr + '.' + numArr[1]
      }
      return formatStr
    },
    numStr2: function () {
      if (this.item.chartData.number !== 0 && !this.item.chartData.number) {
        return '--'
      }
      return this.item.chartData.number
    },
    increase: function () {
      return this.item.chartData.value && (Math.abs(this.item.chartData.value).toFixed(2) + '%')
    },
    numberType: function () {
      return this.item.chartData.type || 'number'
    },
    comUnit: function () {
      var unit = this.item.chartData.unit
      return unit === '' ? '' : ('(' + unit + ')')
    },
    numStyle: function () {
      return {
        // padding: '6px 8px',
        fontSize: this.item.fontSize + 'px',
        fontFamily: this.item.fontFamily + ' !important',
        color: this.item.clr,
        width: this.item.width + 'px !important',
        height: this.textHeight + 'px !important',
        textAlign: this.item.textAlign,
        lineHeight: this.item.fontLineHeight + 'px',
        fontWeight: this.item.fontWeight + ' !important',
        margin: '10px !important',
        padding: '0px !important',
        display: 'inline-block',
        letterSpacing: this.item.fontSpaceing + 'px !important'
      }
    },
    numStyle2: function () { // 悬浮字号
      return {
        position: 'absolute',
        lineHeight: '54px',
        left: this.item.leftPos + 'px' || '200px',
        top: this.item.topPos + 'px',
        fontSize: this.item.upFontSize + 'px',
        color: this.item.chartData.value > 0 ? '#ff1f36' : '#2ed8be'
      }
    },
    triangleStyle: function () {
      // let obj = this.item.chartData.value > 0
      //   ? {
      //     borderTop: (this.item.upIcon || 6) + 'px solid transparent',
      //     borderBottom: (this.item.upIcon || 6) + 'px solid red'
      //   } // 增加
      //   : {
      //     borderTop: (this.item.upIcon || 6) + 'px solid green',
      //     borderBottom: (this.item.upIcon || 6) + 'px solid transparent'
      //   } // 减少
      return {
        position: 'absolute',
        left: (this.item.upIconLeft || -20) + 'px' || '-20px',
        top: (this.item.upIconTop || 23) + 'px' || '23px'
        // borderLeft: (this.item.upIcon || 6) + 'px solid transparent',
        // borderRight: (this.item.upIcon || 6) + 'px solid transparent',
      }
    },
    imgUrl: function () {
      let img = ''
      if (this.item.chartData.value >= 0) {
        img = require('./上涨.png')
      } else if (this.item.chartData.value < 0) {
        img = require('./下跌.png')
      }
      return img
    },
    numCardOver: function () {
      return {
        display: 'inline-block',
        verticalAlign: 'top',
        textAlign: 'center',
        width: '16px',
        height: '16px',
        lineHeight: '15px',
        color: this.item.clr,
        fontSize: '20px'
      }
    }
  },
  methods: {
    ShowXq () {
      if (this.$route.name === 'HomePage' || this.$route.name === 'lookPage' || this.$route.name === 'popPage') {
        if (this.item.chartData.list) {
          let boxData = {
            title: '数据详情',
            data: 'arry',
            dataArray: this.item.chartData.list
          }
          this.$parent.$parent.ShowTableBox(boxData)
        } else if (this.item.chartData.url) {
          if (this.item.chartData.type) {
            if (this.item.chartData.type === '藏区人口') {
              let boxData = {
                title: '藏区人口详情',
                data: {}
              }
              this.axios
                .get(this.item.chartData.url)
                .then((data) => {
                  if (data.success) {
                  // this.DataTkArry = data.obj
                    boxData.data = data.obj.rows[0]
                    this.$parent.$parent.ShowTanKuangBox(boxData)
                  }
                })
            } else if (this.item.chartData.type === '涉藏高校') {
              let boxData = {
                title: '涉藏高校详情',
                data: {}
              }
              this.axios
                .get(this.item.chartData.url)
                .then((data) => {
                  if (data.success) {
                  // this.DataTkArry = data.obj
                    boxData.data = data.obj
                    this.$parent.$parent.ShowElineBox(boxData)
                  }
                })
            }
          } else if (this.item.chartData.name === '指挥长') {
            let boxData = {
              title: '指挥长信息',
              dataArray: []
            }
            this.axios
              .get(this.item.chartData.url + this.item.chartData.name)
              .then((data) => {
                if (data.success) {
                  boxData.dataArray = data.obj.rows
                  this.$parent.$parent.ShowInformation(boxData)
                }
              })
          } else if (this.item.chartData.name === '副总指挥长') {
            let boxData = {
              title: '副总指挥长信息',
              dataArray: []
            }
            this.axios
              .get(this.item.chartData.url + this.item.chartData.name)
              .then((data) => {
                if (data.success) {
                  boxData.dataArray = data.obj.rows
                  this.$parent.$parent.ShowInformation(boxData)
                }
              })
          } else {
            let boxData = {
              title: '数据详情',
              data: {name: this.item.chartData.name},
              dataUrl: this.item.chartData.url
            }
            this.$parent.$parent.ShowTableBox(boxData)
          }
        }
      }
    }
  },
  destroyed: function () {
  }
}
</script>
