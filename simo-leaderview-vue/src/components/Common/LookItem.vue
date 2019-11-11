<template>
  <div class="itemWrapBox newDrag"
       :id="'p_view'+index"
       :key="item.id"
       :style="boxStyle">
    <Vtextarea v-if="item.chartType==='text'"
               :item="item"
               :disabled="editable"></Vtextarea>
    <Marquee v-else-if="item.chartType==='marquee'"
             :item="item"
             :disabled="editable"></Marquee>
    <Border v-else-if="item.chartType==='border'"
            :item="item"></Border>
    <Vtable v-else-if="item.chartType==='table'"
            :item="item"></Vtable>
    <Vprogress v-else-if="item.chartType==='progress'"
               :item="item"></Vprogress>
    <Doubler v-else-if="item.chartType==='doubler'"
             :item="item"></Doubler>
    <Topo v-else-if="item.chartType==='topo'"
          :item="item"></Topo>
    <Vimg v-else-if="item.chartType==='image'"
          :item="item"></Vimg>
    <Vtime v-else-if="item.chartType==='time'"
           :item="item"></Vtime>
    <Vnumber v-else-if="item.chartType==='number'"
             :item="item"></Vnumber>
    <Vscatter v-else-if="item.chartType=='v-scatter'"
              :item="item"></Vscatter>
    <Vmap v-else-if="item.chartType=='v-map'"
          :item="item"></Vmap>
    <Liquidfill v-else-if="item.secondType=='liquidfill'"
                :item="item"></Liquidfill>
    <Vchart v-else
            :item="item"></Vchart>
  </div>
</template>
<script>
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
import Vmap from './EditComp/Vmap' // 地图
import Vscatter from './EditComp/Vscatter' // 散点图
import Liquidfill from './EditComp/Liquidfill' // 水波图

export default {
  name: 'lookItem',
  props: ['item', 'index'],
  components: { Vtextarea, Vprogress, Vimg, Doubler, Border, Vchart, Vtable, Topo, Marquee, Vtime, Vnumber, Vmap, Vscatter, Liquidfill },
  data () {
    return {
      editable: false
    }
  },
  computed: {
    boxStyle: function () {
      return {
        width: Number(this.item.width) + 'px',
        height: Number(this.item.height) + 'px',
        left: Number(this.item.x) + 'px',
        top: Number(this.item.y) + 'px',
        zIndex: this.item.zIndex || 500
      }
    }
  }
}
</script>
<style>
.itemWrapBox {
  position: absolute;
  box-sizing: border-box;
}
</style>
