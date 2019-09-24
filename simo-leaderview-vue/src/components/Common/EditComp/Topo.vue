<template>
  <div :style="barBoxStyle"
       class="main_topo">
    <div class="v-charts-data-empty"
         v-if="!item.tpId">请选择拓扑</div>
    <div width="100%"
         height="100%">
    </div>
  </div>
</template>
<script>
import MainTp from '#/js/topo/mainTopo'
export default {
  name: 'topo',
  props: ['item'],
  data () {
    return {

    }
  },
  computed: {
    barBoxStyle: function () {
      return {
        width: this.item.width + 'px',
        height: this.item.height + 'px',
        position: 'relative'
      }
    }
  },
  methods: {
    initTp: function () {
      if (this.item.tpId === '') {
        return
      }
      if (this.topo) {
        /* var viewbox = this.topo.tp.svgContainer.attr('viewBox');
         this.topo.tp.destoryed();
         this.topo = this.topo.tp = null; */
        this.topo && this.topo.refreshTp()
        return
      }
      this.topo = new MainTp({
        el: this.$el,
        tpId: this.item.tpId,
        width: Number(this.item.width),
        height: Number(this.item.height)
        // viewBox:viewbox
      })
      // 拓扑内部图片跨域
      this.$nextTick(() => {
        // console.log($(this.$el).find('img'))
      })
    },
    clearTp: function () {
      if (this.topo) {
        this.topo.tp.destoryed()
        this.topo = this.topo.tp = null
      }
    }
  },
  mounted: function () {
    this.initTp()
  },
  watch: {
    'item.tpId': function () {
      this.clearTp()
      this.initTp()
    },
    'item.time': function () { // 为了刷新能及时更新
      this.initTp()
    }
  },
  beforeDestroy () {
    this.clearTp()
  },
  destoryed: function () {
  }
}
</script>
