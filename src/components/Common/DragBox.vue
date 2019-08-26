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
              :z="item.zIndex || 500"
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
    <Marquee v-else-if="item.chartType=='marquee'"
             :item="item"
             ref="childtext"
             :disabled="editable"></Marquee>
    <Border v-else-if="item.chartType=='border'"
            :item="item"></Border>
    <Vtable v-else-if="item.chartType=='table'"
            :item="item"></Vtable>
    <Vprogress v-else-if="item.chartType=='progress'"
               :item="item"></Vprogress>
    <Doubler v-else-if="item.chartType=='doubler'"
             :item="item"></Doubler>
    <Topo v-else-if="item.chartType=='topo'"
          :item="item"></Topo>
    <Vimg v-else-if="item.chartType=='image'"
          :item="item"></Vimg>
    <Vtime v-else-if="item.chartType=='time'"
           :item="item"></Vtime>
    <Vchart v-else
            :item="item"></Vchart>
  </DragResize>
</template>
<script>
import DragResize from './EditComp/DragResize' // drag拖拽组件
import Vtextarea from './EditComp/Vtextarea' // 文本
import Vprogress from './EditComp/Vprogress' // 进度条
import Vimg from './EditComp/Vimg'
import Doubler from './EditComp/Doubler' // 数字翻牌器
import Border from './EditComp/Border' // 边框
import Vchart from './EditComp/Vchart'
import Vtable from './EditComp/Vtable' // 表格
import Topo from './EditComp/Topo' // 拓扑
import Marquee from './EditComp/Marquee' // 跑马灯
import Vtime from './EditComp/Vtime' // 时间器

export default {
  name: 'dragBox',
  props: ['item', 'editable', 'index'],
  components: { DragResize, Vtextarea, Vprogress, Vimg, Doubler, Border, Vchart, Vtable, Topo, Marquee, Vtime },
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
      this.$emit('selected', item, 'down', 'item', this.index)
    },
    bodymove (item, attr) {
      item.x = attr.left
      item.y = attr.top
      // this.$emit('selected', item, 'move', 'item', this.index)
    },
    vdbclick () { // 双击
      if (this.item.chartType === 'text' || this.item.chartType === 'marquee') {
        this.$refs.childtext.getMessage(this.$refs.childtext)
      }
    },
    contextMenu (item, ev) {
      this.$emit('selected', item, 'context', 'item', this.index)
      this.$emit('context', this.index, ev)
    }
  },
  beforeDestroy () {
  },
  destoryed: function () {
  }
}
</script>
