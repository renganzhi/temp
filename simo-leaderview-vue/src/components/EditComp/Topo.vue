<template>
  <div>
    <div
      :style="barBoxStyle"
      class="main_topo"
      :class="'map' + item.id"
      ref="MainTop"
    >
      <div class="v-charts-data-empty" v-if="!item.tpId">请选择拓扑</div>
      <div width="100%" height="100%"></div>
    </div>
    <div :class="'BackButton BackBtn' + item.id">返回</div>
  </div>
</template>
<script>
import MainTp from '#/js/topo/mainTopo'
import initBusTp from '#/js/businessTopo/businessTopostart'
import initMapTopo from '#/js/newMapTopo/topostart'
import { gbs } from '@/config/settings'
// import mapTopology from '#/js/newMapTopo/topology'

export default {
  name: 'topo',
  props: ['item'],
  data () {
    return {
      busTp: null
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
      if (!this.item.tptype) {
        return
      }
      $(this.$refs.MainTop).css('opacity', '1')
      if (this.item.tptype === 'business') {
        // 业务拓扑 'cd520898-231c-4fae-959a-2287643ad6ec'
        if (this.busTp) {
          this.busTp.getNedata() // 一个页面中有多个业务拓扑时刷新可能会有问题
        } else {
          this.busTp = initBusTp({
            businessId: this.item.tpId,
            el: this.$refs.MainTop
          })
        }
      } else if (this.item.tptype === 'maptp') {
        $(this.$refs.MainTop).empty() // 刷新有问题，直接清空绘制
        // initMapTopo({el: this.$refs.MainTop, mapCode: '510000', mpId: '34f820b1-3fd2-4fa3-9ddb-1c87fd7654fc', userId: ''})
        initMapTopo({
          el: this.$refs.MainTop,
          mapCode: this.item.chartData.mapCode,
          mpId: this.item.chartData.mpId,
          userId: this.item.chartData.userId
        })
      } else {
        if (this.topo) {
          this.topo && this.topo.refreshTp() // 刷新网络拓扑
          return
        }
        // 网络拓扑 domain
        this.topo = new MainTp({
          el: this.$refs.MainTop,
          tpId: this.item.tpId,
          width: Number(this.item.width),
          height: Number(this.item.height)
          // viewBox:viewbox
        })
      }
    },
    clearTp: function () {
      if (this.topo) {
        if (this.topo.tp) {
          this.topo.tp.destoryed()
          this.topo.tp = null
        }
        this.topo = null
      }
      if (this.busTp) {
        this.busTp = null
      }
      $(this.$refs.MainTop).empty()
    }
  },
  mounted: function () {
    var _this = this
    if (_this.item.tptype !== 'maptp' && !this.item.tpId) {
    } else {
      this.initTp()
      if (this.item.cityColor) {
        $('.map' + this.item.id)
          .find('.province')
          .css('fill', this.item.cityColor)
      }
      if (
        _this.item.chartData &&
        this.item.tptype === 'maptp' &&
        _this.item.chartData.userId
      ) {
        let dataArray = {
          userId: _this.item.chartData.userId,
          pLocationCode: `${_this.item.chartData.mapCode};${_this.item.chartData.mpId}`,
          mapLocationId: ''
        }
        $.ajax({
          url: gbs.host + '/monitor/mapTopo/getMapTopoParams', // 第三方的ur已经拼接好host
          data: dataArray,
          type: 'get',
          cache: false,
          ascyn: false,
          success: function (res) {
            _this.item.chartData = res.obj
          }
        })
      }
    }
  },
  watch: {
    'item.cityColor': function (newV) {
      $('.map' + this.item.id)
        .find('.province')
        .css('fill', newV)
    },
    'item.chartData': function () {
      this.clearTp()
      this.initTp()
      if (this.item.tptype === 'maptp' && this.item.cityColor) {
        $('.map' + this.item.id)
          .find('.province')
          .css('fill', this.item.cityColor)
      }
    },
    'item.tptype': function (newV) {
      this.clearTp()
      this.initTp()
      if (this.item.tptype !== 'maptp' && !this.item.tpId) {
        $(this.$refs.MainTop).css('opacity', '1')
        $(this.$refs.MainTop).append(
          '<div class="v-charts-data-empty">请选择拓扑</div>'
        )
      }
    },
    'item.tpId': function (newV) {
      this.clearTp()
      this.initTp()
      if (this.item.tptype !== 'maptp' && !newV) {
        $(this.$refs.MainTop).css('opacity', '1')
        $(this.$refs.MainTop).append(
          '<div class="v-charts-data-empty">请选择拓扑</div>'
        )
      }
    },
    'item.refresh': function (newV) {
      if (newV) {
        this.clearTp()
        this.initTp()
      }
    },
    'item.time': function () {
      // 为了刷新能及时更新
      this.initTp()
    }
  },
  beforeDestroy () {
    this.clearTp()
  },
  destroyed: function () {}
}
</script>
<style>
.v-charts-data-empty {
  position: absolute !important;
  top: 0px !important;
  background-color: transparent !important;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
}
.main_topo svg:first-of-type {
  z-index: 10;
}
.v-charts-data-empty i,
.v-charts-data-empty p {
  color: #666f8b;
}
</style>
