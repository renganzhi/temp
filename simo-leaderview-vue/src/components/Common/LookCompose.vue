<template>
  <div class="newDrag compWrapBox"
       :id="'p_viewCompose'+index"
       :key="list.id"
       :style="boxStyle">
    <LookItem v-for="(item,_index) in list.child"
              :index="_index"
              :item="item"
              :key="'inside_' + item.id">
    </LookItem>
  </div>
</template>
<script>
import DragResize from '@/components/EditComp/DragResize' // drag拖拽组件
// 组内编辑
import InsideDrag from './InsideDrag'
import LookItem from './../Common/LookItem'

export default {
  name: 'compose',
  props: ['list', 'index', 'editable', 'parentH', 'parentW'],
  components: { DragResize, InsideDrag, LookItem },
  data () {
    return {
      chooseChild: false
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
      }
    },
    boxStyle: function () {
      return {
        width: Number(this.list.width) + 'px',
        height: Number(this.list.height) + 'px',
        left: Number(this.list.x) + 'px',
        top: Number(this.list.y) + 'px',
        zIndex: this.list.zIndex || 500
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
    resizing (list, attr) {
      list.width = attr.width
      list.height = attr.height
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
  beforeMount () {
    // this.list.oldWidth = this.list.width
    // this.list.oldHeight = this.list.height
    // console.log(this.list.oldWidth, this.list.oldHeight)
  },
  beforeDestroy () {
  },
  destroyed: function () {
  }
}
</script>
<style>
.comp-item {
  float: left;
  position: absolute;
}
.compWrapBox {
  position: absolute;
  box-sizing: border-box;
}
.dragwrap {
  width: 100%;
  height: 100%;
}
</style>
