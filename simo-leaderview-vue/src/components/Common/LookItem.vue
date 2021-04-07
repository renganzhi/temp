<template>
  <div class="itemWrapBox newDrag"
       :id="'p_view'+index"
       :key="item.id"
       :style="boxStyle"
       @click="handleClick"
       >
    <template v-if="dynamicList.includes(item.chartType)">
      <component :is="capitalize(item.chartType)" :item="item" :moving="true" :disabled="editable"></component>
    </template>
  </div>
</template>
<script>
import dynamicList from './dynamicList'
import components from './chartComponents'
import { mapMutations } from 'vuex'
import { capitalize } from '@/utils'

export default {
  name: 'lookItem',
  props: ['item', 'index'],
  components,
  data () {
    return {
      dynamicList,
      editable: false
    }
  },
  computed: {
    linkId () {
      return this.item.linkId === '' ? -1 : this.item.linkId
    },
    boxStyle () {
      let style = {
        width: Number(this.item.width) + 'px',
        height: Number(this.item.height) + 'px',
        left: Number(this.item.x) + 'px',
        top: Number(this.item.y) + 'px',
        zIndex: this.item.zIndex || 500
      }
      style.cursor = this.linkId > -1 ? 'pointer' : 'default'
      return style
    }
  },
  methods: {
    ...mapMutations([
      'changeNowPage'
    ]),
    capitalize,
    handleClick () {
      if (this.linkId > -1) {
        this.changeNowPage(this.linkId)
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.itemWrapBox {
  position: absolute;
  box-sizing: border-box;
}
</style>
