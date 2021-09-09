<template>
  <div class="full-height full-width over-auto">
    <div class="full-height full-width">
      <TreeTip
        :tip="tip"
        :left="tipLeft"
        :top="tipTop"
      />
      <svg
        :width="maxXY.width"
        :height="maxXY.height"
        :style="svgStyle"
      >
        <g :transform="`translate(${maxXY.width/2},${maxXY.height/2})`">
          <path
            v-for="(item,index) in lLinks.concat(rLinks)"
            :key="index"
            class="link"
            :d="path(item)"
          />
          <g
            v-for="item in lNodes.concat(rNodes)"
            :key="item.id"
            class="node"
            :class="item.data.runStatus || item.data.status"
            :transform="`translate(${item.y * item.data.dir },${item.x})`"
            @mouseenter="showTip($event,item)"
            @mouseleave="hideTip"
          >
            <rect
              :width="rectW"
              height="40"
              fill="#354775"
              y="-25"
              x="-30"
            />
            <template>
              <svg
                viewBox="0 0 32 32"
                width="30"
                height="30"
                y="-20"
                x="-20"
                v-html="showImg(item.data,item)"
              />
              <text
                fill="#fff"
                x="14"
                y="0"
              >
                {{ item.data.name | ellipsisName(14) }}
              </text>
            </template>
          </g>
        </g>
      </svg>
    </div>
  </div>
</template>
<script>

import { logicTopoImg } from './logicTopoImg.js'
let d3 = Object.assign({}, require('d3-hierarchy'), require('d3-shape'))

export default {
  name: 'EsxiTree',
  components: {
    TreeTip: () => import('./treeTip.vue')
  },
  filters: {
    ellipsisName (v, length) {
      if (v.replace(/[\u4e00-\u9fa5]/g, 'aa').length <= length) {
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
          if (len > (length - 2)) {
            break
          } else {
            outText += v[i]
          }
        }
        return outText + '...'
      }
    }
  },
  props: {
    neId: {
      type: String,
      default: ''
    },
    esxiData: {
      type: Object,
      default: () => {}
    }
  },
  data () {
    return {
      tipLeft: -99999, // tip定位
      tipTop: 0,
      tip: { },
      mouseout: true, // 判断鼠标是否移出，解决异步问题，鼠标移出了请求还没完
      rectW: 120,
      boxWidth: '',
      boxHeight: '',
      lNodes: [],
      rNodes: [],
      lLinks: [],
      rLinks: []
    }
  },
  computed: {
    svgStyle () {
      let left = this.boxWidth > this.maxXY.width ? '50%' : '0'
      let top = this.boxHeight > this.maxXY.height ? '50%' : '0'
      return {
        left: left,
        top: top,
        transform: `translate(-${left},-${top})`
      }
    },
    maxXY () {
      let maxx = 0; let maxy = 0
      this.rNodes.concat(this.lNodes).forEach(d => {
        maxx = Math.max(d.x, maxx)
        maxy = Math.max(d.y, maxy)
      })

      return {
        width: maxy * 2 + this.rectW * 2,
        height: Math.max(maxy, maxx) * 2 + this.rectW
      }
    }
  },
  mounted () {
    this.$nextTick(() => {
      this.boxWidth = this.$el.getBoundingClientRect().width
      this.boxHeight = this.$el.getBoundingClientRect().height
      this.initTree()
    })
  },
  methods: {
    initTree () {
      let children = this.esxiData.children
      let lRoot = JSON.parse(JSON.stringify(this.esxiData))
      let rRoot = JSON.parse(JSON.stringify(this.esxiData))
      lRoot.children = children.slice(0, children.length / 2)
      rRoot.children = children.slice(children.length / 2, children.length)

      this.getTree(lRoot, 'l')
      this.getTree(rRoot, 'r')
    },
    getTree (data, dir) { // 数据转为树形数据
      let hiData = d3.hierarchy(data).sum(function (d) {
        d.dir = dir === 'l' ? -1 : 1
        return d.value
      })
      let tree = d3.tree().nodeSize([50, 210]).separation(function (a, b) {
        return (a.parent === b.parent ? 1 : 2) / a.depth
      })(hiData)
      this[`${dir}Nodes`] = tree.descendants() // 获取所有节点
      this[`${dir}Links`] = tree.links() // 获取所有节点
    },
    path (d) {
      let dir = d.target.data.dir
      let curveGenerator = d3.linkHorizontal().x(d => d.y).y(d => d.x)
      var start = { x: d.source.x, y: dir === 1 ? d.source.y + this.rectW / 2 : d.source.y }
      var end = { x: d.target.x, y: dir === 1 ? d.target.y : d.target.y * dir + this.rectW / 2 }
      return curveGenerator({ source: start, target: end })
    },
    showImg (d, item) {
      if (d.ind) {
        return logicTopoImg['root']
      } else if (d.datacenterName) {
        return logicTopoImg['dataCenter']
      } else if (d.type === 'datastores') {
        return logicTopoImg['datastore']
      } else {
        return logicTopoImg[d.type]
      }
    },
    async showTip (e, item) {
      this.mouseout = false
      let d = item.data || item
      if (d && !d.name) {
        return
      }

      if (['computeResource', 'hostSystems', 'virtualMachines'].includes(d.type)) {
        let res = await this.$api.virTip({
          neId: this.neId,
          type: d.type,
          name: d.name,
          identifier: d.identifier
        })
        res.obj.type = d.type
        res.obj.monitoring = res.obj.monitoring || d.monitoring
        this.tip = res.obj
      } else {
        this.tip = d
      }
      if (!this.mouseout) {
        this.tipLeft = e.x + 10
        this.tipTop = e.y - 40
      }
    },
    hideTip () {
      this.tipLeft = -99999
      this.mouseout = true
    }
  }
}
</script>
<style lang="scss" scoped>
@import "./vmTree.scss";
</style>
