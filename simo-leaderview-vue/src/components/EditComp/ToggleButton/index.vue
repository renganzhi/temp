<template>
  <div class="ToggleButton" :style="buttonStyle">
    <div class="mainMenu">
        <div class="button" v-for="(value, index) in item.chartData.array" :style="mainStyle(value)" @click="changeSelect(value, 1)" :key="index">
            {{value.name}}
        </div>
    </div>
    <div class="mainMenu" v-show="subArray.length">
        <div class="button" v-for="(val, ind) in subArray" @click="changeSelect(val, 2)" :style="subStyle(val)" :key="ind">
            {{val.name}}
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
      selectMainMenu: '',
      selectSubMenu: '',
      subArray: []
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
        if (this.selectMainMenu === value.name) {
          backgroundImg = this.item.checkedBack
            ? 'url(' + gbs.host + this.item.checkedBack + ')'
            : 'url(' + require('./checked.png') + ')'
        }
        return {
          padding: this.item.buttonPadding + 'px',
          marginRight: this.item.buttonMargin + 'px',
          backgroundImage: backgroundImg
        }
      }
    },
    subStyle () {
      return (value) => {
        let backgroundImg = this.item.normalBack
          ? 'url(' + gbs.host + this.item.normalBack + ')'
          : 'url(' + require('./normal.png') + ')'
        if (this.selectSubMenu === value.name) {
          backgroundImg = this.item.checkedBack
            ? 'url(' + gbs.host + this.item.checkedBack + ')'
            : 'url(' + require('./checked.png') + ')'
        }
        return {
          padding: this.item.buttonPadding + 'px',
          marginRight: this.item.buttonMargin + 'px',
          backgroundImage: backgroundImg,
          position: 'relative',
          fontSize: this.item.menuFontSize + 'px !important',
          top: this.item.menuTop + 'px',
          left: this.item.menuLeft + 'px'
        }
      }
    }
  },
  watch: {
    'item.chartData': {
      handler () {
        this.subArray = []
        this.selectMainMenu = ''
        this.selectSubMenu = ''
      },
      deep: true
    },
    'selectSubMenu': function () {
      this.bus.$emit('selectType', this.selectSubMenu)
    }
  },
  mounted () {
    if (this.item.chartData.array) {
      this.selectMainMenu = this.item.chartData.array[0].name
      this.subArray = this.item.chartData.array[0].options
      this.selectSubMenu = this.item.chartData.array[0].options[0].name
      this.bus.$emit('selectType', this.selectSubMenu)
    }
  },
  methods: {
    changeSelect (value, type) {
      if (type === 1) {
        if (this.selectMainMenu !== value.name) {
          this.selectMainMenu = value.name
          if (value.options) {
            this.subArray = value.options
          } else {
            this.subArray = []
          }
          this.selectSubMenu = ''
        } else {
          this.subArray = []
          this.selectMainMenu = ''
          this.selectSubMenu = ''
        }
      } else {
        if (this.selectSubMenu !== value.name) {
          this.selectSubMenu = value.name
        } else {
          this.selectSubMenu = ''
        }
        // this.bus.$emit('selectType', this.selectSubMenu)
      }
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
        .button{
            background-repeat: no-repeat;
            background-size: 100% 100%;
        }
        .button:last-child{
            margin-right: 0px !important;
        }
    }
}
</style>
