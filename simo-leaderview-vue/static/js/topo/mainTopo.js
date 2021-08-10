import { gbs } from '@/config/settings'
import Topology from './topology'
import Region from './regionText'
import { newAjax } from '@/config/thirdLoginMix'
function MainTp (opt) {
  this.opt = opt
  $(opt.el)
    .empty()
    .css('opacity', 0)
  if (!this.opt || !this.opt.tpId) {
    return
  }
  this.init()
}

MainTp.prototype = {
  constructor: Topology,
  init: function () {
    this.getTpConfig()
    this.tp = new Topology(
      {
        selector: this.opt.el,
        config: this.opt.config
      },
      this.opt.tpId
    )

    this.rg = new Region({
      selector: this.tp.regionWrap
    })

    this.getTpInfo()
    this.getRegions()
  },
  getTpConfig: function () {
    var _this = this
    // $.api.topo.topoConfig(
    //   this.opt.tpId,
    //   function (res) {
    //     res.topoId = _this.opt.tpId
    //     res.default = false
    //     _this.opt.curConfig = res
    //     _this.changeTpData(res)
    //     _this = null
    //   },
    //   false
    // )
    // topoConfig:function(tpId,callback,isAsync){   //获取拓扑全局设置
    newAjax({
      url: gbs.host + '/monitor/topo/findTopoBackgroundSetByTopoId?topoId=' + this.opt.tpId,
      type: 'GET',
      async: false,
      success: function (data) {
        if (data.success) {
          var res = data.obj || {}
          res.topoId = _this.opt.tpId
          res.default = false
          _this.opt.curConfig = res
          _this.changeTpData(res)
          _this = null
        }
      }
    })
    // },
  },
  changeTpData: function (data) {
    this.opt.config = $.extend(
      true,
      { canvasSize: { width: 1920, height: 1080 } },
      data
    )
  },
  getTpInfo: function (flag) {
    var _this = this
    // $.api.topo.domainTopo(
    //   this.opt.tpId,
    //   function (data) {
    //     _this.nodes = data.nodes || []
    //     _this.links = data.links || []
    //     _this.initTopo(flag)
    //     _this = null
    //   },
    //   function (msg) {
    //     _this.nodata(msg)
    //   }
    // )
    // 获取拓扑图中的节点、连线、链路信息
    newAjax({
      url: gbs.host + '/monitor/topo/domainTopo/' + this.opt.tpId,
      type: 'GET',
      success: function (data) {
        if (data.success) {
          var res = data.obj || {}
          _this.nodes = res.nodes || []
          _this.links = res.links || []
          _this.initTopo(flag)
          _this = null
          //  typeof successCb === 'function' && successCb(data.obj || {})
        } else {
          _this.nodata(data.msg)
          //  typeof errorCb === 'function' && errorCb(data.msg)
        }
      }
    })
  },
  initTopo: function (isFresh) {
    var _this = this
    var tp = this.tp
    var rg = this.rg
    if (!tp) {
      return
    }
    tp.addNodes(this.nodes)
    tp.addLinks(this.links)
    tp.update()
    if (isFresh) {
      return
    }
    setTimeout(function () {
      /* if(_this.opt.viewBox) {
           tp.svgContainer.attr('viewBox',_this.opt.viewBox);
       }else{ */
      tp.setMaxCavWH(rg.minRgX, rg.maxRgX, rg.minRgY, rg.maxRgY)
      $(_this.opt.el).css('opacity', 1)
      /* } */
      _this = null
    }, 0)
  },
  getRegions: function () {
    var _this = this
    // $.api.topo.getRegion(this.opt.tpId, function (res) {
    //   _this.rg.init(res)
    //   _this = null
    // })
    // 区域
    newAjax({
      url: gbs.host + '/monitor/topo/getTopoRegionByTopoId/' + this.opt.tpId,
      type: 'GET',
      success: function (data) {
        if (data.success) {
          var res = data.obj || []
          _this.rg.init(res)
          _this = null
          //   typeof callback === 'function' && callback(data.obj || [])
        }
      }
    })
  },
  refreshTp: function () {
    this.tp.nodes = []
    this.tp.links = []
    this.getTpInfo('fresh')
    this.getRegions()
  },
  nodata: function (msg) {
    $(this.opt.el)
      .empty()
      .html('<div class="portlet_status_noDataText">' + msg + '</div>')
  }
}
export default MainTp
