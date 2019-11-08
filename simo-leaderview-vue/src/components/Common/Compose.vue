<template>
  <!-- :preventActiveBehavior="chooseChild && !editable" -->
  <!-- :isActive="editable && list.slted" -->
  <!-- :isResizable="!chooseChild" -->
  <DragResize class="newDrag2 newDrag3"
              :isDraggable="editable"
              :isResizable="!chooseChild"
              :active="editable && list.slted"
              :isActive="editable && list.slted"
              :preventActiveBehavior="!editable"
              :hasChild="true"
              :key="list.id"
              :parentW="parentW"
              :parentH="parentH"
              :w="Number(list.width)"
              :h="Number(list.height)"
              :x.sync="list.x"
              :y.sync="list.y"
              :z="list.zIndex || 500"
              :item="list"
              @vdbclick="vdbclick"
              @bodyDown="bodyDown"
              @bodymove="bodymove"
              @resizing="resizing"
              @dragging="dragging"
              @contextMenu="contextMenu">
    <!-- :style="wrapStyle" -->
    <!-- :editable="false" -->
    <InsideDrag v-for="(item,_index) in list.child"
                :index="_index"
                :item="item"
                :sacleX="list.sacleX"
                :sacleY="list.sacleY"
                :parentW="list.width"
                :parentH="list.height"
                @childChoose="childChoose"
                @childResize="childResize"
                @selected="selected"
                @resized="resized"
                @context="context"
                @contextmenu="contextItem(_index)"
                :key="'inside_' + item.id">
    </InsideDrag>
    <!-- </div> -->
  </DragResize>
</template>
<script>
import DragResize from './EditComp/DragResize' // drag拖拽组件
// 组内编辑
import InsideDrag from './InsideDrag'

export default {
  name: 'compose',
  props: ['list', 'index', 'editable', 'parentH', 'parentW'],
  components: { DragResize, InsideDrag },
  data () {
    return {
      chooseChild: false
      // sacleX: 1,
      // sacleY: 1
    }
  },
  computed: {
    wrapStyle: function () {
      return {
        width: this.list.oldWidth + 'px',
        height: this.list.oldHeight + 'px',
        position: 'absolute',
        top: 0,
        left: 0,
        float: 'left',
        transformOrigin: 'left top'
        // transform: 'scale(' + this.list.sacleX + ', ' + this.list.sacleY + ')'
      }
    }
  },
  watch: {
    'list.width': function (newV, oldV) {
      this.$set(this.list, 'sacleX', newV / this.list.oldWidth)
    },
    'list.height': function (newV, oldV) {
      this.$set(this.list, 'sacleY', newV / this.list.oldHeight)
    },
    'list.slted': function (newV) {
      if (newV === 'false' || !newV) {
        this.cancleChildSlted()
      }
    }
  },
  methods: {
    dragging (chgX, chgY, attr) {
      this.$emit('draged', chgX, chgY, attr)
    },
    resizing (list, attr) {
      list.width = attr.width
      list.height = attr.height
      attr.id = list.id
      this.$emit('resized', attr)

      this.$set(this.list, 'sacleX', attr.width / this.list.oldWidth)
      this.$set(this.list, 'sacleY', attr.height / this.list.oldHeight)
    },
    childResize (attr) {
      this.$emit('childResize', attr)
    },
    selected (item, ev, type, i) {
      // console.log(item)
      // console.log(i)
    },
    resized (item) {
    },
    bodyDown (item, attr) { // 点击
      this.cancleChildSlted()
      this.chooseChild = false
      this.$emit('selected', item, 'down', 'compose', this.index)
    },
    childChoose (id) {
      this.chooseChild = true
      this.cancleChildSlted()
      this.list.child[id].slted = true
      this.$emit('childSelect', this.list.child[id], id, this.index)
    },
    cancleChildSlted () {
      // 取消所有子元件的选中状态
      // if (id === -1) {
      //   this.chooseChild = false
      // }
      for (let i = 0, len = this.list.child.length; i < len; i++) {
        this.list.child[i].slted = false
      }
    },
    bodymove (item, attr) {
      item.x = attr.left
      item.y = attr.top
      this.$emit('selected', item, 'move', 'compose', this.index)
    },
    vdbclick (e) { // 双击
      // e.stopPropagation()
      // 内部元件双击，父级也获取焦点
      // this.list.child.slted = true
      // if (this.item.chartType === 'text' || this.item.chartType === 'marquee') {
      //   this.$refs.childtext.getMessage(this.$refs.childtext)
      // }
    },
    contextMenu (item, ev) {
      this.$emit('selected', item, 'context', 'compose', this.index)
      this.$emit('context', this.index, ev, 'compose')
    },
    context (index, ev) {
      console.log('右键内部元件：' + index)
    },
    contextItem (index) {
      console.log('右键内部元件：' + index)
    }
  },
  beforeDestroy () {
  },
  destoryed: function () {
  }
}
</script>
<style>
.comp-item {
  float: left;
  position: absolute;
}
.dragwrap {
  width: 100%;
  height: 100%;
}
</style>
