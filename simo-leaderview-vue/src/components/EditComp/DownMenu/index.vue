<template>
  <div class="dropMenu">
    <div class="" :style="titleStyle" v-if="item.chartData.children">
      <div class="dropMenuTitle" id="dropMenuTitle" ref="dropMenuTitle">
        {{ item.chartData.title }}
        <div :style="contenterStyle" class="contenter" id="contenter" ref="contenter">
          <div v-for="(data, index) in item.chartData.children" :key="index" :class="item.chartData.nowCheckId === data.id?'checked':'onchecked'">
            <a v-if="data.url" target="_blank" :href="data.url">{{
              data.title
            }}</a>
            <div v-else @click="clickKnowPage(data.id)">{{ data.title }}</div>
          </div>
        </div>
      </div>
    </div>
    <a
      :style="titleStyle"
      v-else-if="item.chartData.url"
      target="_blank"
      :href="item.chartData.url"
      >{{ item.chartData.title }}</a
    >
  </div>
</template>
<script>
import { gbs } from '@/config/settings'
import { mapMutations } from 'vuex'
export default {
  name: 'DownMenu',
  props: ['item'],
  data () {
    return {
      visible: false
    }
  },
  computed: {
    titleStyle: function () {
      return {
        left: '10px',
        top: '10px',
        position: 'relative',
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
        color: this.item.clr + ' !important',
        letterSpacing: this.item.fontSpaceing + 'px !important',
        fontSize: this.item.fontSize + 'px !important',
        backgroundSize: '100% 100%',
        overflow: 'hidden'
      }
    },
    contenterStyle: function () {
      let style = {}
      if (this.item.placement === 'top') {
        style = {
          position: 'relative',
          bottom: this.item.chartData.children.length * 50 + 50 + 'px'
        }
      }
      return style
    }
  },
  mounted () {
    var Moveout = this.$refs.dropMenuTitle
    var content = this.$refs.contenter
    // 鼠标移入显示
    if (Moveout) {
      Moveout.onmouseover = function () {
        content.style.display = 'block'
      }
      // 鼠标移出隐藏
      Moveout.onmouseout = function () {
        content.style.display = 'none'
      }
    }
  },
  methods: {
    ...mapMutations(['changeNowPage']),
    clickKnowPage (id) {
      this.changeNowPage(id)
    },
    handleOpen () {
      this.visible = true
    },
    handleClose () {
      this.visible = false
    }
  }
}
</script>
<style scoped lang="scss">
.dropMenu {
  width: 100%;
  height: 100%;
}
.dropMenuTitle {
  cursor: pointer;
  text-align: center;
  .ivu-icon {
    display: none;
  }
}
.checked{
  background: url(./checked.png);
  background-size: 100% 100%;
}
.onchecked{
  background: url(./onchecked.png);
  background-size: 100% 100%;
}
.onchecked:hover{
  background: url(./checked.png);
  background-size: 100% 100%;
}
.contenter {
  margin: 8px 0px;
  padding: 7px;
  text-align: center;
  display: none;
  background-color: #13288359;
}
// .dropMenuTitle:hover{
//   color: #5b8bff;
// }
</style>
