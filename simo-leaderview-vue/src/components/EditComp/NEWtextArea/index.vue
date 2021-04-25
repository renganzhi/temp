<template>
  <div :style="wrapStyle">
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
              placeholder="请输入文本框内容"
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
      textHeight: 50
    }
  },
  methods: {
    getMessage () {
      // this.$refs.NEWtextArea.$el.focus() // 双击穿透，使文本框获得焦点
      if (this.item.ctDataSource === 'static') {
        this.$refs.NEWtextArea.focus()
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
    }
  },
  mounted () {
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
      if (!this.item.ColorType) {
        return {
          width: this.item.width - 20 + 'px !important',
          height: this.textHeight - 20 + 'px !important',
          backgroundImage: !this.item.ColorType ? '-webkit-linear-gradient(bottom,' + this.item.Gradientclr[0] + ',' + this.item.Gradientclr[1] + ')!important' : '-webkit-linear-gradient(bottom,' + this.item.clr + ',' + this.item.clr + ')!important',
          fontSize: this.item.fontSize + 'px !important',
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
          color: this.item.clr + ' !important',
          fontSize: this.item.fontSize + 'px !important',
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
    'item.chartData': function () {
      if (this.item.ctDataSource !== 'static') {
        this.updateColor()
      }
    },
    'item.fontSize': function () {
      this.updateHeight()
    },
    'item.height': function () {
      this.updateHeight()
    }
  },
  destroyed: function () {
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
  z-index: 1;
}
.hiddeLeft::-webkit-scrollbar { width: 0 !important }
#jianBian{
  -webkit-background-clip: text; /*必需加前缀 -webkit- 才支持这个text值 */
  -webkit-text-fill-color: transparent; /*text-fill-color会覆盖color所定义的字体颜色： */
}
</style>
