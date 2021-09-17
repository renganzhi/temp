<template>
  <div class="full-height full-width over-auto"  style="overflow: hidden;">
    <div class="full-height full-width" v-if="showTopData" style="position: relative;">
      <TreeTip
        style="position: absolute;"
        :tip="tip"
        :left="tipLeft"
        :top="tipTop"
      />
      <svg
        v-show="showVm"
        width="100%"
        height="100%"
        :style="svgStyle"
      >
        <g :transform="`scale(${scaleNum}) translate(40,${maxXY.height/2 - 80})`" ref="mycanvasD">
          <path
            v-for="(item,index) in links"
            :key="index"
            class="link"
            :d="path(item)"
          />
          <g
            v-for="item in nodes"
            :key="item.id"
            class="node"
            :class="item.data.runStatus || item.data.status"
            :transform="`translate(${item.y},${item.x})`"
            @dblclick="esxiRelation(item.data)"
            @mouseenter="showTip($event,item)"
            @mouseleave="hideTip"
          >
            <rect
              width="120"
              height="40"
              fill="#354775"
              y="-25"
              x="-30"
            />
            <template v-if="item.data.name">
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
            <template v-else>
              <rect
                v-for="(it,idx) in item.data"
                :key="idx"
                class="datastores"
                :class="it.status"
                width="10"
                height="25"
                fill="#299bd7"
                y="-18"
                :x="-20+idx*15"
                @mouseenter="showTip($event,it)"
                @mouseleave="hideTip"
              />
            </template>
          </g>
        </g>
      </svg>
      <EsxiTree
        v-if="!showVm"
        :ne-id="neId"
        :esxi-data="esxiData"
      />
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

import { logicTopoImg } from './logicTopoImg.js'
import { Notification } from 'element-ui'
let d3 = Object.assign({}, require('d3-hierarchy'), require('d3-shape'))

export default {
  name: 'VmTree',
  components: {
    EsxiTree: () => import('./esxi.vue'),
    TreeTip: () => import('./treeTip.vue')
  },
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
      showVm: true,
      logicTopoImg,
      tip: { },
      mouseout: true, // 判断鼠标是否移出，解决异步问题，鼠标移出了请求还没完
      boxWidth: '',
      boxHeight: '',
      nodes: [],
      links: [],
      showTopData: false,
      topoData: {},
      esxiData: {},
      tipLeft: -99999, // tip定位
      tipTop: 0
    }
  },
  computed: {
    svgStyle () {
      let left = this.boxWidth > this.maxXY.width * 2 ? '50%' : '0'
      let top = this.boxHeight > this.maxXY.height * 2 ? '50%' : '0'
      return {
        left: left,
        top: top,
        transform: `translate(-${left},-${top})`
      }
    },
    scaleNum(){
      let scalewidth = (this.item.width / this.maxXY.width).toFixed(2)
      let scaleheight = (this.item.height / this.maxXY.height).toFixed(2)
      return scalewidth>scaleheight?scaleheight:scalewidth
    },
    neId(){
      return this.item.iputneId.split('&')[0] || ''
    },
    maxXY () {
      let maxx = 0; let maxy = 0; let minx = 0
      this.nodes.forEach(d => {
        maxx = Math.max(d.x, maxx)
        maxy = Math.max(d.y, maxy)
        minx = Math.min(d.x, minx)
      })

      return {
        width: Math.max(maxx, Math.abs(minx)) + 120, // 120矩形宽
        height: maxy + 60
      }
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
  created(){
    this.getTopoData()
  },
  methods: {
    getTopoData () { // 获取拓扑数据
    // /virtualization/vmware/topo/base/
    this.axios.get(`/monitor/virtualization/vmware/topo/base/${this.neId}?neId=${this.neId}`).then(res => {
      // this.$api.vmTopoVmware({ neId: this.neId }).then(res => {
        if(res.success){
          if (res.obj && res.obj.indicatorValue) {
            let treeData = res.obj
            if (treeData.indicatorValue[0].hostSystems && treeData.indicatorValue[0].hostSystems.length) {
              treeData.indicatorValue[0].hostSystems.forEach(d => {
                treeData.indicatorValue[0].computeResource.push(d)
              })
            }
            treeData.indicatorValue[0].hostSystems = null
            this.convert(treeData, ['ind', 'datacenterName', 'computeResourceName', 'name'], ['indicatorValue', 'computeResource', 'hostSystems', 'datastores'])
            this.showTopData = true
            this.topoData = treeData
          } else {
            this.showTopData = false
            this.topoData = {}
          }
          this.getTree()
        }else{
          this.showTopData = false
          this.topoData = {}
          Notification({
            message: res.msg,
            position: 'bottom-right',
            customClass: 'toast toast-error'
          })
        }
      })
    },
    convert: function (res, nameFields, childrenFields) { // 转换数据满足业务需求
      if (res == null || typeof res === 'string') {
        return
      }
      if (Array.isArray(res)) {
        res.forEach(d => {
          this.convert(d, nameFields, childrenFields)
        })
      } else if (typeof res === 'object') {
        for (let k in res) {
          let v = res[k]
          if (Array.isArray(v) && v.length) {
            if (childrenFields.indexOf(k) !== -1) {
              res.children = k === 'datastores' ? [v] : v
            }
          }
          if (nameFields.includes(k)) {
            res.name = v
          }
          this.convert(v, nameFields, childrenFields)
        }
      } else {

      }
    },
    getTree () { // 数据转为树形数据
      let hiData = d3.hierarchy(this.topoData).sum(function (d) {
        return d.value
      })
      let tree = d3.tree().nodeSize([90, 190]).separation(function (a, b) {
        return (a.parent === b.parent ? 1 : 2) / a.depth
      })(hiData)
      this.nodes = tree.descendants() // 获取所有节点
      this.links = tree.links() // 获取所有联系
    },
    path (d) {
      let curveGenerator = d3.linkHorizontal().x(d => d.y).y(d => d.x)
      var start = { x: d.source.x, y: d.source.y }
      var end = { x: d.target.x, y: d.target.y }
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
      this.axios.get(`/monitor/virtualization/vmware/tooltip/${this.neId}/${d.type}?neId=${this.neId}&type=${d.type}&name=${d.name}&identifier=${d.identifier}`).then(res => {
          res.obj.type = d.type
          res.obj.monitoring = res.obj.monitoring || d.monitoring
          this.tip = res.obj
      })
      } else {
        this.tip = d
      }
      if (!this.mouseout) {
        this.tipLeft = e.offsetX + 10
        this.tipTop = e.offsetY - 40
      }
    },
    hideTip () {
      this.tipLeft = -99999
      this.mouseout = true
    },
    esxiRelation (d) { // 双击查看esxi与虚拟主机的关系
      if (d.virtualMachines) {
        let copyD = JSON.parse(JSON.stringify(d))
        copyD.children = d.virtualMachines.filter(d => {
          return d.state
        })

        this.esxiData = copyD
        this.hideTip()
        this.showVm = false
      }
    }
  }
}
</script>
<style lang="scss" scoped>
@import "./vmTree.scss";
</style>
