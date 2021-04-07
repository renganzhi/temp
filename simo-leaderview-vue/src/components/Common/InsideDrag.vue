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
              :x.sync="item.x"
              :y.sync="item.y"
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
    <!-- <Liquidfill v-else-if="item.secondType=='liquidfill'"
                :item="item"></Liquidfill> -->
    <template v-if="dynamicList.includes(item.chartType)">
      <component :is="capitalize(item.chartType)" :item="item"></component>
    </template>
  </DragResize>
</template>
<script>
import dynamicList from './dynamicList'
import components from './chartComponents'
import { capitalize } from '@/utils'
export default {
  name: 'insideDrag',
  props: ['item', 'editable', 'index', 'parentIndex', 'sacleX', 'sacleY', 'parentW', 'parentH'],
  components,
  data () {
    return {
      dynamicList
    }
  },
  methods: {
    palyErr () {
      this.$emit('palyErr')
    },
    capitalize,
    resizing (item, attr) {
      item.width = attr.width
      item.height = attr.height
      attr.id = item.id
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
    sacleX: {
      handler: function (newV) {
        this.$set(this.item, 'width', Math.round(this.item.oldW * newV))
        this.$set(this.item, 'x', Math.round(this.item.oldX * newV))
        this.$forceUpdate()
      },
      deep: true
      // immediate: true
    },
    sacleY: {
      handler: function (newV) {
        this.$set(this.item, 'height', Math.round(this.item.oldH * newV))
        this.$set(this.item, 'y', Math.round(this.item.oldY * newV))
        this.$forceUpdate()
      },
      deep: true
    },
    'item.x': function (newV) {
      this.item.oldX = Math.round(newV / this.sacleX)
    },
    'item.y': function (newV) {
      this.item.oldY = Math.round(newV / this.sacleY)
    },
    'item.width': function (newV) {
      this.item.oldW = Math.round(newV / this.sacleX)
    },
    'item.height': function (newV) {
      this.item.oldH = Math.round(newV / this.sacleY)
    }
  },
  beforeDestroy () {
  },
  destroyed: function () {
  }
}
</script>
