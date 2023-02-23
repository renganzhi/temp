<template>
  <div class="ToggleButton" :style="buttonStyle">
    <div class="mainMenu">
        <div class="button" v-for="(value, index) in item.chartData.array" :style="mainStyle(value)" @click="changeSelect(value, 1)" :key="index">
            {{value}}
        </div>
    </div>
  </div>
</template>
<script>
import { gbs } from '@/config/settings'
export default {
  name: 'ToggleButton',
  props: ['item'],
  data () {
    return {
      selectMainMenu: ''
    }
  },
  computed: {
    buttonStyle () {
      return {
        fontSize: this.item.fontSize + 'px',
        lineHeight: this.item.fontLineHeight + 'px',
        fontWeight: this.item.fontWeight + ' !important',
        color: this.item.clr + ' !important',
        letterSpacing: this.item.fontSpaceing + 'px !important',
        fontFamily: this.item.fontFamily ? this.item.fontFamily + ' !important' : ''
      }
    },
    mainStyle () {
      return (value) => {
        let backgroundImg = this.item.normalBack
          ? 'url(' + gbs.host + this.item.normalBack + ')'
          : 'url(' + require('./normal.png') + ')'
        if (this.selectMainMenu === value) {
          backgroundImg = this.item.checkedBack
            ? 'url(' + gbs.host + this.item.checkedBack + ')'
            : 'url(' + require('./checked.png') + ')'
        }
        return {
          padding: this.item.buttonPadding + 'px',
          marginRight: this.item.buttonMargin + 'px',
          marginBottom: this.item.buttonMarginTop + 'px',
          backgroundImage: backgroundImg
        }
      }
    }
  },
  watch: {
    'item.chartData': {
      handler () {
        if (this.item.chartData.array) {
          this.selectMainMenu = this.item.chartData.array[0]
        }
      },
      deep: true
    },
    'selectMainMenu': function () {
      this.bus.$emit('selectType', this.selectMainMenu)
    }
  },
  mounted () {
    if (this.item.chartData.array) {
      this.selectMainMenu = this.item.chartData.array[0]
      this.bus.$emit('selectType', this.selectMainMenu)
    }
  },
  methods: {
    changeSelect (value) {
      this.selectMainMenu = value
    }
  }
}
</script>
<style scoped lang="scss">
.ToggleButton{
    width: 100%;
    height: 100%;
    .mainMenu{
        display: flex;
        width: 100%;
        flex-wrap: wrap;
        .button{
            background-repeat: no-repeat;
            background-size: 100% 100%;
            cursor: pointer;
        }
        .button:last-child{
            margin-right: 0px !important;
            // margin-bottom: 0px !important;
        }
    }
}
</style>
