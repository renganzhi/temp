<template>
  <div :style="wrapStyle">
    <div v-if="item.ctDataSource === 'system' && item.chartData && item.chartData.state"
         ref="titleBox"
         :style="titleStyle">
      <span :style="stateSty"
            v-show="item.chartData.state">【{{item.chartData.state}}】</span>
      <span>{{item.chartData.title}}</span>
      <span style="float: right; padding-right: 10px;">{{item.chartData.time}}</span>
    </div>
    <textarea :style="textStyle"
              class="homeText"
              v-model="item.ctName"
              ref="vtextarea"
              :disabled="dis"></textarea>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import _ from 'lodash'
export default {
  name: 'vtextarea',
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
      // vtextarea.$el.focus() // 双击穿透，使文本框获得焦点
      if (this.item.ctDataSource === 'static') {
        this.$refs.vtextarea.focus()
      }
    },
    getBlur () {
      this.$refs.vtextarea.blur()
    },
    updateHeight () {
      if (this.item.ctDataSource === 'system') {
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
        color: this.item.clr + ' !important',
        fontSize: this.item.fontSize + 'px !important',
        fontWeight: this.item.fontWeight + ' !important',
        fontFamily: this.item.fontFamily ? this.item.fontFamily + ' !important' : '',
      }
    },
    textStyle: function () {
      return {
        width: this.item.width + 'px !important',
        height: this.textHeight + 'px !important',
        color: this.item.clr + ' !important',
        fontSize: this.item.fontSize + 'px !important',
        fontWeight: this.item.fontWeight + ' !important',
        fontFamily: this.item.fontFamily ? this.item.fontFamily + ' !important' : '',
      }
    }
  },
  watch: {
    'item.ctDataSource': function () {
      this.updateHeight()
      this.updateColor()
    },
    'item.chartData': function () {
      if (this.item.ctDataSource === 'system') {
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
    background: transparent !important;
    background-color: transparent !important;
  }
}
.homeText {
  border: none !important;
  position: relative;
  z-index: 1;
}
</style>
