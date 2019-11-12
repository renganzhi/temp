<template>
  <!-- :z="item.zIndex || 500" -->
  <DragResize class="newDrag2 newDrag3"
              :isDraggable="editable"
              :active="editable && list.slted"
              :isActive="editable && list.slted"
              :preventActiveBehavior="!editable"
              :key="list.id"
              :w="Number(list.width)"
              :h="Number(list.height)"
              :x="Number(list.x)"
              :y="Number(list.y)"
              :z="list.zIndex || 500"
              :item="list"
              @bodyDown="bodyDown"
              @bodymove="bodymove"
              @resizing="resizing"
              @contextMenu="contextMenu">
    <div class="dragwrap"
         :style="wrapStyle">
      <div class="comp-item"
           :style="{'left': item.left + 'px', 'top': item.top + 'px', 'width': item.width + 'px'}"
           v-for="(item, index) in list.child"
           :key="index"
           @contextmenu="contextItem(index)">
        <!-- <DragBox :index="index"
                 :item="item"
                 :editable="editable"
                 :key="item.id">
        </DragBox> -->
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
        <Vchart v-else
                :item="item"></Vchart>
      </div>
    </div>
  </DragResize>
</template>
<script>
// import DragResize from './DragResize' // drag拖拽组件
// import Vtextarea from './Vtextarea' // 文本
// import Vprogress from './Vprogress' // 进度条
// import Vimg from './Vimg'
// import Doubler from './Doubler' // 数字翻牌器
// import Border from './Border' // 边框
// import Vchart from './Vchart'
// import Vtable from './Vtable' // 表格
// import Topo from './Topo' // 拓扑
// import Marquee from './Marquee' // 跑马灯
// import Vtime from './Vtime' // 时间器
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

// 测试组内编辑
import DragBox from './DragBox'

export default {
  name: 'compose',
  props: ['list', 'index', 'editable'],
  components: { DragResize, Vtextarea, Vprogress, Vimg, Doubler, Border, Vchart, Vtable, Topo, Marquee, Vtime, DragBox, Vnumber },
  data () {
    return {
      oldWidth: 0,
      oldHeight: 0
      // sacleX: 1,
      // sacleY: 1
    }
  },
  computed: {
    wrapStyle: function () {
      return {
        width: this.oldWidth + 'px',
        height: this.oldHeight + 'px',
        position: 'absolute',
        top: 0,
        left: 0,
        float: 'left',
        transformOrigin: 'left top',
        transform: 'scale(' + this.list.sacleX + ', ' + this.list.sacleY + ')'
      }
    }
  },
  watch: {
    'list.width': function (newV, oldV) {
      // this.oldWidth += newV - oldV
      var sacleX = newV / this.oldWidth
      this.list.sacleX = Number(sacleX.toFixed(5))
    },
    'list.height': function (newV, oldV) {
      var sacleY = newV / this.oldHeight
      this.list.sacleY = Number(sacleY.toFixed(5))
    }
  },
  methods: {
    resizing (list, attr) {
      list.width = attr.width
      list.height = attr.height
      var sacleX = attr.width / this.oldWidth
      this.list.sacleX = Number(sacleX.toFixed(5))
      var sacleY = attr.height / this.oldHeight
      this.list.sacleY = Number(sacleY.toFixed(5))
      this.$emit('resized', attr)
    },
    bodyDown (item, attr) { // 点击
      this.$emit('selected', item, 'down', 'compose', this.index)
    },
    bodymove (item, attr) {
      item.x = attr.left
      item.y = attr.top
      this.$emit('selected', item, 'move', 'compose', this.index)
    },
    vdbclick () { // 双击
      // if (this.item.chartType === 'text' || this.item.chartType === 'marquee') {
      //   this.$refs.childtext.getMessage(this.$refs.childtext)
      // }
    },
    contextMenu (item, ev) {
      this.$emit('selected', item, 'context', 'compose', this.index)
      this.$emit('context', this.index, ev, 'compose')
    },
    contextItem (index) {
      console.log('右键内部元件：' + index)
    }
  },
  beforeMount () {
    this.oldWidth = this.list.width
    this.oldHeight = this.list.height
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
</style>
