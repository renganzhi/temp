<template>
  <div class="newDropMenu">
    <div class="title" @click="showMenu" :style="titleStyle">{{item.chartData.title}}</div>
    <div class="menu" :style="menuStyle" v-show="visible">
      <div class="button" @click="jump(data.url)" :style="buttonStyle" v-for="(data, index) in item.chartData.children" :key="'a' + index">
        {{data.title}}
      </div>
    </div>
  </div>
</template>
<script>
import { gbs } from '@/config/settings'
export default {
  name: 'NewDropMenu',
  props: ['item'],
  data () {
    return {
      visible: false
    }
  },
  computed: {
    titleStyle: function () {
      return {
        color: this.item.clr + ' !important',
        fontSize: this.item.fontSize + 'px !important',
        letterSpacing: this.item.fontSpaceing + 'px !important',
        fontWeight: this.item.fontWeight + ' !important',
        fontFamily: this.item.fontFamily
          ? this.item.fontFamily + ' !important'
          : ''
      }
    },
    menuStyle: function () {
      return {
        backgroundImage: this.item.DropdownMenuBack
          ? 'url(' + gbs.host + this.item.DropdownMenuBack + ')'
          : '',
        height: this.item.menuHeight + 'px',
        width: this.item.menuWidth + 'px',
        // color: this.item.clr + ' !important',
        letterSpacing: this.item.fontSpaceing + 'px !important',
        fontSize: this.item.fontSize + 'px !important',
        backgroundSize: '100% 100%',
        left: this.item.menuLeft + 'px',
        top: this.item.menuTop + 'px'
      }
    },
    buttonStyle: function () {
      return {
        backgroundImage: this.item.ButtonImage
          ? 'url(' + gbs.host + this.item.ButtonImage + ')'
          : '',
        backgroundSize: '100% 100%',
        color: this.item.menuClr + ' !important'
      }
    }
  },
  mounted () {
  },
  methods: {
    showMenu () {
      this.visible = !this.visible
    },
    jump (url) {
      if (url) {
        window.open(url, '_blank')
      }
    }
  }
}
</script>
<style scoped lang="scss">
.newDropMenu{
  .title{
    cursor: pointer;
  }
  .menu{
    position: absolute;
    padding: 5px;
    text-align: center;
    background: url(./bg.png) no-repeat;
    background-repeat: no-repeat;
    background-size: 100% 100%;
    overflow: scroll;
    .button{
      background-repeat: no-repeat;
      background-size: 100% 100%;
      margin-bottom: 5px;
      cursor: pointer;
    }
    .button:last-child{
      margin-bottom: 0;
    }
  }
}
</style>
