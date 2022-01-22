<template>
  <div class="dropMenu">
    <Dropdown :style="titleStyle" :trigger="item.trigger ||'hover'" :placement="item.placement ||'top'">
        <div class="dropMenuTitle">
            {{item.chartData.title}}
            <Icon type="ios-arrow-down"></Icon>
        </div>
        <DropdownMenu :style="menuStyle" slot="list">
          <div v-for="(data,index) in item.chartData.children" :key = index>
            <DropdownItem v-if="data.url"><a  target="_blank" :href="data.url">{{data.title}}</a></DropdownItem>

            <Dropdown v-else-if="data.children" :placement="item.placement === 'top'?'right-end':'right-start'">
                <DropdownItem>
                    {{data.title}}
                    <Icon type="ios-arrow-forward"></Icon>
                </DropdownItem>
                <DropdownMenu :style="menuStyle" slot="list">
                  <div v-for="(a,i) in data.children" :key = i>
                    <DropdownItem v-if="a.url"><a  target="_blank" :href="a.url">{{a.title}}</a></DropdownItem>
                  </div>
                </DropdownMenu>
            </Dropdown>
          </div>
        </DropdownMenu>
    </Dropdown>
  </div>
</template>
<script>
import { gbs } from '@/config/settings'
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
        fontWeight: this.item.fontWeight + ' !important',
        fontFamily: this.item.fontFamily ? this.item.fontFamily + ' !important' : ''
      }
    },
    menuStyle: function () {
      return {
        backgroundImage: this.item.DropdownMenuBack
          ? 'url(' + gbs.host + this.item.DropdownMenuBack + ')'
          : '',
        color: this.item.clr + ' !important',
        fontSize: this.item.fontSize + 'px !important',
        backgroundSize: '100% 100%',
        overflow: 'hidden'
      }
    }
  },
  methods: {
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
  .dropMenu{
      width: 100%;
      height: 100%;
  }
  .dropMenuTitle{
    cursor: pointer;
  }
  .dropMenuTitle:hover{
    color: #5b8bff;
  }
</style>
