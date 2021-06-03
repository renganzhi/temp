<template>
  <DragResize class="newDrag"
              :isDraggable="editable"
              :id="'p_drag'+index"
              :active="editable && item.slted"
              :isActive="editable && item.slted"
              :preventActiveBehavior="!editable"
              :key="item.id"
              :parentW="parentW"
              :parentH="parentH"
              :w="Number(item.width)"
              :h="Number(item.height)"
              :x.sync="item.x"
              :y.sync="item.y"
              :z="item.zIndex || 500"
              :item="item"
              @dragging="dragging"
              @dragstop="dragstop"
              @resizing="resizing"
              @resizestop="resizestop"
              @bodyDown="bodyDown"
              @bodymove="bodymove"
              @dbclick="vdbclick"
              @deactivated="deactivated"
              @contextMenu="contextMenu">
    <Vtextarea v-if="item.chartType=='text'"
               :item="item"
               ref="childtext"
               :disabled="editable"></Vtextarea>
    <template v-else-if="dynamicList.includes(item.chartType)">
      <component :is="capitalize(item.chartType)" :item="item" :moving="false"></component>
    </template>
    <Marquee v-else-if="item.chartType=='marquee'"
             :item="item"
             ref="childtext"
             :disabled="editable"></Marquee>
    <Border v-else-if="item.chartType=='border'"
            :item="item"></Border>
    <moveTable v-else-if="item.chartType=='table' && item.thirdType=='moveTable'"
               :item="item"
               :moving="false"></moveTable>
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
    <TDEarthLine v-else-if="item.chartType=='TDEarthLine'"
          :item="item"></TDEarthLine>
    <TDEarthBar v-else-if="item.chartType=='TDEarthBar'"
          :item="item"></TDEarthBar>
    <GradientPie v-else-if="item.chartType=='GradientPie'"
          :item="item"></GradientPie>
    <Sunrise v-else-if="item.chartType=='Sunrise'"
          :item="item"></Sunrise>
    <Scatter v-else-if="item.chartType=='Scatter'"
          :item="item"></Scatter>
    <KLine v-else-if="item.chartType=='KLine'"
          :item="item"></KLine>
    <BulletFrame v-else-if="item.chartType=='BulletFrame'"
          :item="item"></BulletFrame>
    <TreeMap v-else-if="item.chartType=='TreeMap'"
          :item="item"></TreeMap>
    <NEWtextArea v-else-if="item.chartType=='NEWtextArea'"
          :item="item"
          ref="childtext"
          :disabled="editable"></NEWtextArea>
    <TDHistogram v-else-if="item.chartType=='TDHistogram'"
          :item="item"></TDHistogram>
    <DataFlow v-else-if="item.chartType=='DataFlow'"
          :item="item"></DataFlow>
    <!-- <Liquidfill v-else-if="item.secondType=='liquidfill'"
                :item="item"></Liquidfill> -->
    <Player v-else-if="item.chartType=='video'"
            @palyErr="palyErr"
            :item="item"></Player>
    <Vchart v-else
            :item="item"></Vchart>
  </DragResize>
</template>
<script>
import DragResize from '@/components/EditComp/DragResize' // drag拖拽组件
import dynamicList from './dynamicList'
import components from './chartComponents'
import { capitalize } from '@/utils'

export default {
  name: 'dragBox',
  props: ['item', 'editable', 'index', 'parentW', 'parentH'],
  components: {
    DragResize,
    ...components
  },
  data () {
    return {
      dynamicList,
      oldW: 0,
      oldH: 0,
      oldX: 0,
      oldY: 0
    }
  },
  methods: {
    capitalize,
    palyErr () {
      this.$emit('palyErr')
    },
    dragging (chgX, chgY, attr) {
      this.$emit('draged', chgX, chgY, attr)
    },
    dragstop (item, attr) {
      item.x = attr.left
      item.y = attr.top
      if (Number(attr.left) !== Number(this.oldX) || Number(attr.top) !== Number(this.oldY)) {
        this.$emit('changeStop', this.index)
      }
    },
    deactivated () {
      if (this.item.chartType === 'text' || this.item.chartType === 'NEWtextArea') {
        this.$refs.childtext.getBlur()
      }
    },
    resizestop (item, attr) {
      item.width = attr.width
      item.height = attr.height
      if (Number(attr.width) !== Number(this.oldW) || Number(attr.height) !== Number(this.oldH)) {
        this.$emit('changeStop', this.index)
      }
    },
    resizing (item, attr) {
      item.width = attr.width
      item.height = attr.height
      attr.id = item.id
      this.$emit('resized', attr)
    },
    bodyDown (item, attr) { // 点击
      this.oldW = item.width
      this.oldH = item.height
      this.oldX = item.x
      this.oldY = item.y
      this.$emit('selected', item, 'down', 'item', this.index)
      this.$emit('bodyDown')
      // if (this.item.chartType === 'NEWtextArea') {
      //   this.$refs.childtext.getMessage()
      // }
    },
    bodymove (item, attr) {
      item.x = attr.left
      item.y = attr.top
      this.$emit('selected', item, 'move', 'item', this.index)
    },
    vdbclick () { // 双击
      // this.item.chartType === 'text'
      if (this.item.chartType === 'marquee') {
        this.$refs.childtext.getMessage()
      }
    },
    contextMenu (item, ev) {
      this.$emit('selected', item, 'context', 'item', this.index)
      this.$emit('context', this.index, ev)
    }
  },
  beforeDestroy () {
  },
  destroyed: function () {
  }
}
</script>
