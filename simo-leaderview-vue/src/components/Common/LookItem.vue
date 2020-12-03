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
    <moveTable v-else-if="item.chartType=='table' && item.thirdType=='moveTable'"
               :item="item"
               :moving="true"></moveTable>
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
    <Player v-else-if="item.chartType=='video'"
            :item="item"></Player>
    <Vchart v-else
            :item="item"></Vchart>
  </div>
</template>
<script>
import components from './chartComponents'

export default {
  name: 'lookItem',
  props: ['item', 'index'],
  components,
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
