<template>
  <div :style="wrapStyle"
    @click="ShowXq()">
    <div v-if="item.ctDataSource !== 'static' && item.chartData && item.chartData.state"
         ref="titleBox"
         :style="titleStyle">
      <span :style="stateSty"
            v-show="item.chartData.state">【{{item.chartData.state}}】</span>
      <span>{{item.chartData.title}}</span>
      <span style="float: right; padding-right: 10px;">{{item.chartData.time}}</span>
    </div>
    <span class="getPicSpan" :style="[spanStyle,{'display': 'none'}]">{{item.ctName}}</span>
    <textarea :style="textStyle"
              :id="!item.ColorType ? 'jianBian':''"
              :class="item.overflow ? 'getTextArea homeText':'getTextArea homeText hiddeLeft'"
              v-model="item.ctName"
              v-show="ifShow"
              placeholder="无"
              ref="NEWtextArea"
              :disabled="dis"></textarea>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import _ from 'lodash'
export default {
  name: 'NEWtextArea',
  props: ['item', 'disabled'],
  data () {
    return {
      stateCol: '',
      titleHeight: 0,
      barParam: '',
      clock: '',
      textHeight: 50,
      reClock: '',
      ifShow: true
    }
  },
  methods: {
    getMessage () {
      // this.$refs.NEWtextArea.$el.focus() // 双击穿透，使文本框获得焦点
      if (this.item.ctDataSource === 'static') {
        this.$refs.NEWtextArea.focus()
      }
    },
    requestInterface () {
      if (this.item.conditionType) {
        let url = ''
        if (this.item.conditionType === 1) {
          url = '/leaderview/newDistrict/GetGGFW14' + '?param=' + (this.barParam || '房屋中介')
        }
        if (url) {
          this.axios.get(url).then(res => {
            this.item.chartData = res.obj
            this.item.ctName = res.obj.info
            if ($.isEmptyObject(this.item.chartData)) {
              this.item.chartData = {}
            }
          })
        }
      }
    },
    getBlur () {
      this.$refs.NEWtextArea.blur()
    },
    updateHeight () {
      if (this.item.ctDataSource !== 'static') {
        this.$nextTick(() => {
          this.titleHeight = this.$refs.titleBox ? this.$refs.titleBox.getBoundingClientRect().height : 0
          this.textHeight = this.item.height - this.titleHeight
        })
      } else {
        this.textHeight = this.item.height
      }
    },
    updateColor () {
      if (this.item.chartData && this.item.chartData.state) {
        var _this = this
        let index = _.findIndex(this.alertInfo, function (i) {
          return i.name === _this.item.chartData.state
        })
        if (index !== -1) {
          this.stateCol = this.alertInfo[index].color
        }
      }
    },
    startClock () {
      this.ifShow = true
      if (Number(this.item.ctName) && this.item.ifwave) {
        let num = 1
        if (!this.reClock) {
          this.reClock = setInterval(() => {
            if (num % 2 === 0) {
              this.ifShow = false
            } else {
              this.ifShow = true
            }
            num++
          }, 900)
        }
      } else {
        this.ifShow = true
        clearInterval(this.reClock)
        this.reClock = ''
      }
    },
    ShowXq () {
      this.ifShow = true
      clearInterval(this.reClock)
      this.reClock = ''
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
        } else if (this.item.chartData.name.indexOf('燃气') >= 0) {
          let boxData = {
            title: '数据详情',
            data: {name: this.item.chartData.name},
            dataUrl: this.item.chartData.url,
            czType: this.item.chartData.name
          }
          if (this.$parent.$parent.ShowTableBox) {
            this.$parent.$parent.ShowTableBox(boxData)
          } else if (this.$parent.$parent.$parent.ShowTableBox) {
            this.$parent.$parent.$parent.ShowTableBox(boxData)
          }
        } else {
          let boxData = {
            title: '数据详情',
            data: {name: this.item.chartData.name},
            dataUrl: this.item.chartData.url
          }
          if (this.$parent.$parent.ShowTableBox) {
            this.$parent.$parent.ShowTableBox(boxData)
          } else if (this.$parent.$parent.$parent.ShowTableBox) {
            this.$parent.$parent.$parent.ShowTableBox(boxData)
          }
        }
      }
    }
  },
  mounted () {
    if (this.item.placeHolder) {
      this.item.ctName = this.item.placeHolder
    }
    if (this.item.ifwave) {
      this.startClock()
    }
    if (this.$route.name === 'HomePage' || this.$route.name === 'lookPage' || this.$route.name === 'popPage') {
      this.bus.$on('selectType', res => {
        this.barParam = res
      })
      if (this.item.conditionType) {
        this.clock = window.setInterval(() => {
          if (this.$route.name !== 'HomePage' && this.$route.name !== 'lookPage' && this.$route.name !== 'popPage') {
            clearInterval(this.clock)
          }
          this.requestInterface()
        }, this.item.refrashTime || 30000)
      }
    }
    this.requestInterface()
    this.updateHeight()
    this.updateColor()
  },
  computed: {
    ...mapGetters([
      'alertInfo'
    ]),
    dis () {
      return !this.disabled
    },
    wrapStyle () {
      return {
        width: this.item.width + 'px !important',
        height: this.item.height + 'px !important',
        overflow: 'hidden',
        backgroundColor: this.item.bgClr + ' !important',
        border: this.item.bdpx + 'px solid ' + this.item.bdClr + ' !important'
      }
    },
    stateSty () {
      return {
        color: this.stateCol
      }
    },
    titleStyle: function () {
      return {
        paddingLeft: '10px',
        paddingTop: '10px',
        color: this.item.ColorType ? this.item.clr + ' !important' : '',
        fontSize: this.item.fontSize + 'px !important',
        fontWeight: this.item.fontWeight + ' !important',
        fontFamily: this.item.fontFamily ? this.item.fontFamily + ' !important' : ''
      }
    },
    spanStyle: function () {
      return {
        width: this.item.width - 20 + 'px !important',
        height: this.textHeight - 20 + 'px !important',
        color: this.item.ColorType ? this.item.clr : this.item.Gradientclr[1] + ' !important',
        fontSize: this.item.fontSize + 'px !important',
        textAlign: this.item.textAlign,
        lineHeight: this.item.fontLineHeight + 'px',
        fontWeight: this.item.fontWeight + ' !important',
        margin: '10px !important',
        padding: '0px !important',
        letterSpacing: this.item.fontSpaceing + 'px !important',
        fontFamily: this.item.fontFamily ? this.item.fontFamily + ' !important' : ''
      }
    },
    textStyle: function () {
      let fontsize = this.item.fontSize
      let fontColor = this.item.clr
      let Gradientclr = this.item.Gradientclr
      if (this.item.ifthreshold && Number(this.item.ctName) > this.item.thresholdNum) {
        fontsize = this.item.thresholdSize
        fontColor = this.item.thresholdColor
        Gradientclr = this.item.thresholdGclr
      } else {
        fontsize = this.item.fontSize
        fontColor = this.item.clr
        Gradientclr = this.item.Gradientclr
      }
      if (!this.item.ColorType) {
        return {
          width: this.item.width - 20 + 'px !important',
          height: this.textHeight - 20 + 'px !important',
          backgroundImage: !this.item.ColorType ? '-webkit-linear-gradient(bottom,' + Gradientclr[0] + ',' + Gradientclr[1] + ')!important' : '-webkit-linear-gradient(bottom,' + fontColor + ',' + fontColor + ')!important',
          fontSize: fontsize + 'px !important',
          textAlign: this.item.textAlign,
          lineHeight: this.item.fontLineHeight + 'px',
          fontWeight: this.item.fontWeight + ' !important',
          backgroundSize: '100% ' + (this.item.fontSize * 1 + 8) + 'px!important',
          margin: '10px !important',
          padding: '0px !important',
          letterSpacing: this.item.fontSpaceing + 'px !important',
          resize: 'none',
          fontFamily: this.item.fontFamily ? this.item.fontFamily + ' !important' : ''
        }
      } else {
        return {
          width: this.item.width - 20 + 'px !important',
          height: this.textHeight - 20 + 'px !important',
          color: fontColor + ' !important',
          fontSize: fontsize + 'px !important',
          textAlign: this.item.textAlign,
          lineHeight: this.item.fontLineHeight + 'px',
          fontWeight: this.item.fontWeight + ' !important',
          margin: '10px !important',
          padding: '0px !important',
          letterSpacing: this.item.fontSpaceing + 'px !important',
          fontFamily: this.item.fontFamily ? this.item.fontFamily + ' !important' : ''
        }
      }
    }
  },
  watch: {
    'item.ctDataSource': function () {
      this.updateHeight()
      this.updateColor()
    },
    'item.conditionType': function () {
      if (this.item.conditionType) {
        this.requestInterface()
      }
    },
    'barParam': function () {
      this.requestInterface()
    },
    'item.chartData': function () {
      if (this.item.ctDataSource !== 'static') {
        this.updateColor()
      }
    },
    'item.ctName': function () {
      if (this.item.ctName === null) {
        this.item.ctName = '暂无数据'
      }
      if (this.item.ifwave) {
        this.startClock()
      }
    },
    'item.ifwave': function () {
      this.startClock()
    },
    'item.fontSize': function () {
      this.updateHeight()
    },
    'item.height': function () {
      this.updateHeight()
    }
  },
  destroyed: function () {
    clearInterval(this.reClock)
    this.reClock = ''
  }
}
</script>
<style scoped lang="scss">
html[data-theme="blackWhite"],
html[data-theme="blueWhite"] {
  textarea {
    background-color: transparent !important;
  }
}
.getPicSpan{
  display: inline-block;
  overflow: auto;
}
.homeText {
  border: none !important;
  position: relative;
  cursor: pointer;
  z-index: 1;
}
.hiddeLeft::-webkit-scrollbar { width: 0 !important }
#jianBian{
  -webkit-background-clip: text; /*必需加前缀 -webkit- 才支持这个text值 */
  -webkit-text-fill-color: transparent; /*text-fill-color会覆盖color所定义的字体颜色： */
}
</style>
