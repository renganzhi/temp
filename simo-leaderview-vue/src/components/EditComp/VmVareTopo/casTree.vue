<template>
  <div class="full-height full-width over-auto">
    <!-- <UsNodata v-if="!topoData.length" /> -->
    <div class="full-height full-width"  v-if="showTopData">
      <div
        ref="tip"
        class="tp-tip"
      >
        <p
          v-for="(value,key) in tip"
          :key="key"
        >
          {{ key }} : {{ value || '--' }}
        </p>
      </div>
      <svg
        id="casSvg"
        width="100%"
        height="100%"
      >
        <g
          id="svgWrap"
          :transform="`translate(${centerX},40)scale(1)`"
        >
          <path
            v-for="(item,index) in links"
            :key="index"
            class="link"
            :d="path(item)"
          />
          <g
            v-for="item in nodesObj"
            :key="item.uniq"
            class="node"
            :transform="`translate(${item.x-20},${item.y-20})`"
          >
            <image
              width="40"
              height="40"
              dy="20"
              dx="20"
              :href="showImg(item.data)"
              @mouseenter="showTip($event,item.data)"
              @mouseleave="hideTip"
            />
            <text
              v-if="item.data.name !== 'root'"
              fill="rgb(121, 130, 162)"
              dx="15"
              dy="50"
            >
              {{ item.data.name | ellipsisName(14) }}
            </text>
          </g>
        </g>
      </svg>
    </div>
    <div class="v-charts-data-empty"
        v-show="!showTopData"
        style="width: 100%; height: 100%; text-align: center; font-size: 12px;">
        <div><i class="icon-n-nodata"
            style="font-size: 108px;"></i><br>
          <p>抱歉，没有数据可供展示...</p>
        </div>
    </div>
  </div>
</template>
<script>
let d3 = Object.assign({}, require('d3-hierarchy'), require('d3-selection'), require('d3-shape'), require('d3-zoom'))

export default {
  name: 'CasTree',
  props: ['item'],
  filters: {
    ellipsisName (v) {
      if (v.replace(/[\u4e00-\u9fa5]/g, 'aa').length <= 14) {
        return v
      } else {
        let len = 0; let outText = ''
        for (let i = 0; i < v.length; i++) {
          if (/[\u4e00-\u9fa5]/.test(v[i])) {
            len += 2
          } else if (v[i] >= 'A' && v[i] <= 'Z') {
            len += 1.5
          } else {
            len += 1
          }
          if (len > 10) {
            break
          } else {
            outText += v[i]
          }
        }
        return outText + '...'
      }
    }
  },
  data () {
    return {
      tip: { },
      boxWidth: 800,
      boxHeight: 500,
      showTopData: false,
      nodes: [],
      nodesObj: {}, // 不重复节点
      links: [],
      topoData: {}
    }
  },
  computed: {
    neId(){
      return this.item.iputneId.split('&')[0] || ''
    },
    centerX () {
      let maxx = 0; let maxy = 0; let minx = 0
      this.nodes.forEach(d => {
        maxx = Math.max(d.x, maxx)
        maxy = Math.max(d.y, maxy)
        minx = Math.min(d.x, minx)
      })
      let o = {
        width: Math.max(maxx, Math.abs(minx)) + 40, // 120矩形宽
        height: maxy + 150
      }
      if (this.boxWidth > o.width) {
        return (this.boxWidth - o.width / 2) / 2
      }
      return 0
    }
  },
  watch: {
    neId: {
      handler (v) {
        v && this.getTopoData()
      },
      immediate: true
    }
  },
  mounted () {
    this.boxWidth = this.$el.getBoundingClientRect().width
    this.boxHeight = this.$el.getBoundingClientRect().height
  },
  methods: {
    getTopoData () { // 获取拓扑数据
      this.showTopData = false
      this.nodes = []
      this.nodesObj = {}
      this.links = []
      this.axios.get(`/monitor/casTopo/networkTopo/getTopoData/${this.neId}?neId=${this.neId}`).then(res => {
      // this.$api.vmCasTopo({ neId: this.neId }).then(res => {
        res.obj.name = 'root'
        this.topoData = res.obj
        this.showTopData = true
        this.getTree()
      })
    },
    getTree () { // 数据转为树形数据
      let hiData = d3.hierarchy(this.topoData).sum(function (d) {
        return d.value
      })
      let tree = d3.tree().nodeSize([140, 100]).separation(function (a, b) {
        return (a.parent === b.parent ? 2 : 1) / a.depth
      })(hiData)
      this.dealNodes(tree)
    },
    dealNodes (tree) { // 处理重复节点
      let nodes = tree.descendants()
      nodes.forEach((o, i) => {
        let d = o.data
        let id = d.id
        if (id === null && typeof id === 'undefined') {
          id = i
        }
        d.uniq = id + d.name + d.nodeType

        if (this.nodesObj[d.uniq]) {
          if (this.nodesObj[d.uniq].depth < d.depth) {
            this.nodesObj[d.uniq] = open
          }
        } else {
          this.nodesObj[d.uniq] = o
        }
      })
      this.nodes = Object.values(this.nodesObj)// 获取所有不重复节点
      this.links = tree.links() // 获取所有连线
      this.addEvent()
    },
    addEvent () {
      let zoom = d3.zoom().scaleExtent([0.1, 10]).on('zoom', function (event) {
        let transform = d3.zoomTransform(this)
        d3.select('#svgWrap').node().setAttribute('transform', transform)
      })
      d3.select('#casSvg').call(zoom).on('dblclick.zoom', null)
      this.$once('hook:beforeDestroy', () => {
        d3.select('#casSvg').on('.zoom', null)
      })
    },
    path (d) {
      let s = this.nodesObj[d.source.data.uniq] || d.source
      let t = this.nodesObj[d.target.data.uniq] || d.target
      return d3.line()([[s.x, s.y], [t.x, t.y]])
    },
    showImg (item) {
      let src = 'cloud'
      if (item.nodeType) {
        if (item.nodeType === 'cas_host_info') {
          src = `${item.nodeType}_${item.satus ? 'on' : 'off'}`
        } else if (item.nodeType === 'vm_host') {
          src = `${item.nodeType}_${item.vm_status === 'shutdown' || item.vm_status === 'shutOff' ? 'off' : 'on'}`
        } else {
          src = item.nodeType
        }
      }
      return require('../../../vm/' + src + '.svg')
    },
    async showTip (e, item) {
      if (item.name === 'root') {
        return
      }
      this.axios.get(`/monitor/casTopo/getResInfo/${this.neId}/${item.nodeType}?neId=${this.neId}&nodeType=${ item.nodeType}&nodeId=${ item.id}`).then(res => {
        if (Object.keys(res.obj).length) {
          this.tip = res.obj
          this.$refs.tip.style.left = e.x + 10 + 'px'
          this.$refs.tip.style.top = e.y - 40 + 'px'
        }
      })
    },
    hideTip () { // TODO:tip封装
      this.$refs.tip.style.left = '-999999px'
    },
  }
}
</script>
<style lang="scss" scoped>
svg{
  position: relative;
  path{
     fill:none;
     stroke:#5b9bd5;
  }
  .node{
    &:hover{
      cursor: pointer;
    }
    text{
      text-anchor: middle;
      font-size:10px ;
    }
  }
}

</style>
