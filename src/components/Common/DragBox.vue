<template>
  <DragResize class="newDrag"
              :isDraggable="editable"
              :id="'p_drag'+index"
              :active="editable && item.slted"
              :isActive="editable && item.slted"
              :preventActiveBehavior="!editable"
              :key="item.id"
              :w="Number(item.width)"
              :h="Number(item.height)"
              :x="Number(item.x)"
              :y="Number(item.y)"
              :item="item"
              @resizing="resizing"
              @bodyDown="bodyDown"
              @bodymove="bodymove"
              @dbclick="vdbclick"
              @contextMenu="contextMenu">
    <Vtextarea v-if="item.chartType=='text'"
               :item="item"
               ref="childtext"
               :disabled="editable"></Vtextarea>
    <!-- <v-marquee v-else-if="item.chartType=='marquee'" :item="item" ref="childtext" :disabled="editable"></v-marquee> -->
    <Border v-else-if="item.chartType=='border'"
            :item="item"></Border>
    <!-- <v-table v-else-if="item.chartType=='table'" :item="item"></v-table> -->
    <Vprogress v-else-if="item.chartType=='progress'"
               :item="item"></Vprogress>
    <Doubler v-else-if="item.chartType=='doubler'"
             :item="item"></Doubler>
    <!-- <v-topo v-else-if="item.chartType=='topo'" :item="item"></v-topo> -->
    <Vimg v-else-if="item.chartType=='image'"
          :item="item"></Vimg>
    <Vchart v-else
            :item="item"></Vchart>
  </DragResize>
</template>
<script>
import DragResize from './DragResize'
import Vtextarea from './Vtextarea'
import Vprogress from './Vprogress'
import Vimg from './Vimg'
import Doubler from './Doubler'
import Border from './Border'
import Vchart from './Vchart'

export default {
  name: 'dragBox',
  props: ['item', 'editable', 'index'],
  components: { DragResize, Vtextarea, Vprogress, Vimg, Doubler, Border, Vchart },
  data () {
    return {

    }
  },
  methods: {
    resizing (item, attr) {
      item.width = attr.width
      item.height = attr.height
      this.$emit('resized', attr)
    },
    bodyDown (item, attr) { // 点击
      this.$emit('selected', item, 'down')
    },
    bodymove (item, attr) {
      item.x = attr.left
      item.y = attr.top
      this.$emit('selected', item, 'move')
    },
    vdbclick () { // 双击
      if (this.item.chartType === 'text' || this.item.chartType === 'marquee') {
        this.$refs.childtext.getMessage(this.$refs.childtext)
      }
    },
    contextMenu (item, ev) {
      this.$emit('selected', item, 'context')
      this.$emit('context', this.index, ev)
    }
  },
  beforeDestroy () {
  }
}
</script>
