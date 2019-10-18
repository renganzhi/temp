<template>
  <!-- :active="item.slted"
              :isActive="item.slted" -->
  <!-- @bodyUp="bodyUp" -->
  <DragResize class="newDrag"
              :isDraggable="item.slted"
              :insideFlag="true"
              :id="'p_drag'+index"
              :isActive="item.slted"
              :preventActiveBehavior="!item.slted"
              :key="item.id"
              :w="Number(item.width)"
              :h="Number(item.height)"
              :x="Number(item.x)"
              :y="Number(item.y)"
              :z="item.zIndex || 500"
              :item="item"
              :parentW="Number(parentW)"
              :parentH="Number(parentH)"
              @childResize="childResize"
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
    <Vnumber v-else-if="item.chartType=='number'"
             :item="item"></Vnumber>
    <Vscatter v-else-if="item.chartType=='v-scatter'"
              :item="item"></Vscatter>
    <Vmap v-else-if="item.chartType=='v-map'"
          :item="item"></Vmap>
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
import Vnumber from './EditComp/Vnumber' // 指标展示
import Vmap from './EditComp/Vmap' // 指标展示
import Vscatter from './EditComp/Vscatter' // 散点图

export default {
  name: 'insideDrag',
  props: ['item', 'editable', 'index', 'parentIndex', 'sacleX', 'sacleY', 'parentW', 'parentH'],
  components: { DragResize, Vtextarea, Vprogress, Vimg, Doubler, Border, Vchart, Vtable, Topo, Marquee, Vtime, Vnumber, Vmap, Vscatter },
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
    // 子组件改变位置或大小
    childResize (attr) {
      this.$emit('childResize', attr)
    },
    bodyDown (item, attr) { // 点击
      this.$emit('selected', item, 'down', 'item', this.index)
    },
    bodymove (item, attr) {
      item.x = attr.left
      item.y = attr.top
      this.$emit('selected', item, 'move', 'item', this.index)
    },
    bodyUp () {
      this.item.slted = false
    },
    vdbclick () { // 双击
      this.$emit('childChoose', this.index)
      this.item.slted = true
      // if (this.item.chartType === 'text' || this.item.chartType === 'marquee') {
      //   this.$refs.childtext.getMessage(this.$refs.childtext)
      // }
    },
    contextMenu (item, ev) {
      this.$emit('selected', item, 'context', 'item', this.index)
      this.$emit('context', this.index, ev)
    }
  },
  watch: {
    sacleX: function (newV) {
      this.$nextTick(() => {
        this.item.width = parseInt(this.item.oldW * newV)
        this.item.x = parseInt(this.item.oldX * newV)
      })
    },
    sacleY: function (newV) {
      this.$nextTick(() => {
        this.item.height = parseInt(this.item.oldH * newV)
        this.item.y = parseInt(this.item.oldY * newV)
      })
    },
    'item.x': function (newV) {
      this.item.oldX = (newV / this.sacleX)
    },
    'item.y': function (newV) {
      this.item.oldY = (newV / this.sacleY)
    },
    'item.width': function (newV) {
      this.item.oldW = (newV / this.sacleX)
    },
    'item.height': function (newV) {
      this.item.oldH = (newV / this.sacleY)
    }
  },
  beforeDestroy () {
  },
  destoryed: function () {
  }
}
</script>
