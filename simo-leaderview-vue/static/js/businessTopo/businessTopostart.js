import businessTopology from './businessTopology'
import Region from './../topo/regionText'
import { gbs } from '@/config/settings'
var topoBaseData = '45f44be9-ebc8-4f6e-b3f8-fcc18f2b6db7' // sessionStorage.getItem('businessId')
var busData = {}
var baseData = {
  business: {
    curTp: '',
    curConfig: {}
  }
}
var tp = null
var rg = null
var viewTools = {
  dragFn: function () {
    console.log(111)
  }
}
// baseData.business.curTp
// baseData.business.curConfig
clearInterval(baseData.business.refreshTimer)
baseData.business.refreshTimer = null // 定时刷新控制器
window.draging = false // 全局变量，判断是否是拖动操作

var businessViewTopo = {
  changeTpData: function (data) {
    baseData.business.curTp = $.extend({}, baseData.business.curTp)
    baseData.business.curTp.parentIds = baseData.business.curTp.parentIds == undefined ? [] : baseData.business.curTp.parentIds
    baseData.business.config = $.extend(true, {}, baseData.business.curTp, data)
  },
  getTpConfig: function () {
    var _this = this
    $('.tooltip.in').remove()
    businessApi.topoConfig(topoBaseData, function (res) {
      res.businessId = topoBaseData
      res.default = false
      baseData.business.curConfig = res
      _this.changeTpData(res)
      // baseData.business.config = res;
    }, false)
    _this = null
  },
  getTpInfo: function (tp, rg, el) {
    var _this = this
    businessApi.businessMainTopo(topoBaseData, function (data) {
      businessViewTopo.nodes = data.nodes || []
      businessViewTopo.links = data.links || []
      _this.initTopo(tp, rg, el)
      _this = null
    }, function (data) {
      var menu = $('#menu_left ul.sub-menu').find('li.active').attr('name')
      $("#menu_left ul>li[name='" + menu + "']>a").click()
      _this = null
    })
  },
  initTopo: function (tp, rg, el) {
    this.initToolBar()
    tp.addNodes(businessViewTopo.nodes)
    tp.addLinks(businessViewTopo.links)
    tp.update()
    if (el != 'refresh') { // 当前操作是刷新页面时，不改变svg的viewBox
      setTimeout(function () {
        tp.setMaxCavWH(rg.minRgX, rg.maxRgX, rg.minRgY, rg.maxRgY)
      }, 0)
      // this.setRefreshTimer()
    }
    this.placeholderSet()
  },
  setRefreshTimer: function () { // 设置全局刷新定时器
    if (baseData.business.curConfig.refreshCycle) {
      clearInterval(baseData.business.refreshTimer)
      baseData.business.refreshTimer = setInterval(function () {
        if ($('#business_attention').length == 0) {
          clearInterval(baseData.business.refreshTimer)
        }
        tp.dragNodeId = null
        tp.dragLinkId = null
        businessViewTopo.getNedata()
      }, baseData.business.curConfig.refreshCycle * 60000)
    }
  },
  getNedata: function () {
    var _this = this
    businessApi.businessMainTopo(topoBaseData, function (data) {
      //            	var nodes = _.filter(data.nodes,function(d){
      //                    if(d.nodeType =='NE' || d.nodeType =='SubnetTopo'){
      //                        delete d.x;
      //                        delete d.y;
      //                        return true;
      //                    }
      //                });
      //                var netLinks = _.filter(data.links,function(d){
      //                        if(d.linkClass){
      //                            delete d.linkClass['lineType'];
      //                            delete d.linkClass.point;
      //                        }
      //                        return true;
      //                });
      var newNodes = _this.differNeVal(tp.nodes, data.nodes, 'node')
      var newLinks = _this.differNeVal(tp.links, data.links, 'link')
      tp.nodes = []
      tp.links = []
      tp.addNodes(newNodes)
      tp.addLinks(newLinks)
      tp.update()
      _this = null
    })
  },
  differNeVal: function (oldval, newval, type) {
    var result = []
    $.each(newval, function (i, d) { // new
      var v = 0
      $.each(oldval, function (j, k) {
        if (d.id == k.id) {
          if (type == 'node') {
            newval[i].x = k.x
            newval[i].y = k.y
          } else if (type == 'link') {
            if (d.linkClass && (d.linkClass['lineType'] == k.linkClass['lineType'])) {
              if (k.linkClass.point && (k.linkClass.point.toString() != d.linkClass.point.toString())) {
                d.linkClass.point = k.linkClass.point
              }
            }
          }
          result.push(d)
          return true
        } else {
          v++
        }
      })
      if (v == oldval.length) {
        result.push(d)
      }
    })
    return result
  },
  getRegions: function (tp, rg) {
    businessApi.getRegion(topoBaseData, function (res) {
      rg.init(res)
      // viewTools.dblClickRgText()
    })
  },
  initToolBar: function () {
    $('#businessTopo #viewTool').removeClass('.tl-closed').find('div[data-tlClick="returnParentTopo"]').toggleClass('notClick', JSON.stringify(baseData.business.curTp.parentIds) === '[]')
  },
  placeholderSet: function () {
    $('#businessMainTopo .confData').focus(function () {
      if ($(this).text() == '请输入内容') {
        $(this).text('')
      }
    })
    $('#businessMainTopo .confData').blur(function () {
      if ($(this).text() == '') {
        $(this).text('请输入内容')
      }
    })
  },
  reset: function () {
    // $('#businessMainTopo').off()
    $('body').find('.tp-tip').addClass('hide') // 否则每次都会新增
    $(busData.el).off()
    $(busData.el).empty()
    // $(document).off('click.contextMenu')
    // $.contextMenu('destroy', {
    //   selector: busData.el
    // })
  }
}

// $('#businessMainTopo').off('dblclick', '.node').on('dblclick', '.node', function () { // 子网拓扑双击，进入子拓扑
//   var data = d3.select(this).data()[0]
//   if (data.nodeType == 'Business' && data.neId != topoBaseData) {
//     baseData.business.curTp.parentIds.push(topoBaseData)
//     sessionStorage.setItem('businessId', data.neId)
//     $.api.navBusiness.toPage()
//   }
// })
// 新增----------
function initBusTp (bussData, refresh) {
  busData = bussData
  topoBaseData = busData.businessId
  businessViewTopo.reset()
  businessViewTopo.getTpConfig()

  tp = new businessTopology({
    // selector: '#businessMainTopo',
    selector: busData.el,
    config: baseData.business.config,
    dragFn: viewTools.dragFn
  }, topoBaseData)

  rg = new Region({
    selector: tp.regionWrap,
    dragFn: viewTools.dragFn
  })

  businessViewTopo.getTpInfo(tp, rg, refresh)
  businessViewTopo.getRegions(tp, rg)
  // return businessViewTopo
}
// 新增----------
// businessViewTopo.reset()
// businessViewTopo.getTpConfig()

// var tp = new businessTopology({
//   selector: '#businessMainTopo',
//   config: baseData.business.config,
//   dragFn: viewTools.dragFn
// }, topoBaseData)

// var rg = new Region({
//   selector: tp.regionWrap,
//   dragFn: viewTools.dragFn
// })
// businessViewTopo.getTpInfo()
// businessViewTopo.getRegions()

// if (!$('body').hasClass('onlyRead')) {
//   judgeBindBusEvent()
// }

function judgeBindBusEvent () {
  $('#businessMainTopo').off('click.hidetl').on('click.hidetl', function (e) {
    if (tp.isMarquee) {
      tp.isMarquee = false
    } else {
      tp.cancelSelected()
    }
    $('#viewTool .sub-tl').hide()
  })

  $('#businessMainTopo').off('click.tp').on('click.tp', '.node,.link,.rgText,.circleG,.rectG', function (e) {
    $('#viewTool #tl-color').spectrum('hide')
    var o_this = d3.select(this)
    var isSelected = o_this.classed('selected')
    if (tp.draging && isSelected) {
      return
    }
    if (!e.ctrlKey) {
      tp.svgContainer.selectAll('.selected').classed('selected', false)
    }

    o_this.classed('selected', !isSelected)

    if (o_this.classed('link') && isSelected) { // 选中连线显示拖动的点
      tp.hideLinePoint()
    }
    tp.draging = false
    e.stopPropagation()
  })

  $(document).off('click.contextMenu').on('click.contextMenu', function () {
    $('.context-menu-list').trigger('contextmenu:hide')
  })

  $.contextMenu({
    selector: '#businessMainTopo',
    appendTo: '#business_attention',
    build: function ($trigger, e) {
      if ($('body').attr('isShare') != 'share') {
        tp.hideTip()
        viewTools.selectedEle(e.target)
        //   console.log(e.target)
        var $target = $(e.target)
        var type = topoContextMenu.buildItemsByTarget($target)
        return {
          callback: function (key, options) {
            if (key.indexOf('setNetSize') == -1 && key.indexOf('netDef') == -1) {
              topoContextMenu.cb[type][key]($target)
            } else if (key.indexOf('netDef') != -1) {
              topoContextMenu.cb[type]['netDef']($target)
            } else {
              topoContextMenu.cb[type]['setNetSize']($target, key.replace('setNetSize', ''))
            }
          },
          items: topoContextMenu[type]
        }
      }
    }
  })

  tp.svgContainer.on('contextmenu', null).on('contextmenu', function () {
    baseData.business.menuXY = d3.mouse(this)
  })
}

// $(function () {
//     var busTopoSearch = null;
//     busTopoSearch = {
//         init: function () {    //初始化搜索
//             this.$wrap = $("#business_attention").find('.topoSearch');
//             this.$wrap.find('#bustp_resSearch').val('');
//             this.$wrap.find('.bustp_searchList').hide();
//             this.bind();
//         },
//         updateOriginData: function () {    //更新搜索数据源
//             var _this = this;
//             var $list = _this.$wrap.find('.bustp_searchList');
//             this.allData = [];
//             $.each(tp.nodes, function (i, d) {
//                 var str;
//                 if (d.nodeType == 'NE') {
//                     str = '【' + d.ip + '】' + d.name;
//                 } else if (d.nodeType == 'Cluster') {
//                     str = '【集群】' + d.customName;
//                 } else {
//                     str = '【业务系统】' + d.name;
//                 }
//                 _this.allData.push(str);
//             })
//             this.allData.sort();
//             this.buildList();
//             $list.mCustomScrollbar();
//             _this = $list = null;
//         },
//         buildList: function () {
//             var $list = d3.select(this.$wrap.find('.bustp_searchList').get(0));
//             var $li = $list.selectAll('.tp_allRes').data(this.allData, function (d) {
//                 return d;
//             });
//             $li.enter().append('div').classed('tp_allRes', true).attr('title', function (d) {
//                 return d;
//             }).text(function (d) {
//                 return d;
//             })
//             $li.exit().remove();
//             $list = $li = null;
//         },
//         showList: function (val) { //显示符后结果的列表
//             if (val == '') {
//                 this.$wrap.find('.bustp_searchList').hide();
//             } else {
//                 this.showResultList(val);
//                 this.$wrap.find('.bustp_searchList').show();
//             }
//         },
//         showResultList: function (value) {
//             var _this = this;
//             var $selection = this.$wrap.find('.tp_allRes');
//             $selection.each(function () {
//                 var val = $(this).attr('title').trim();
//                 $(this).toggle(_this.find(val, value));
//             })
//             _this = $selection = null;
//         },
//         find: function (str, val) {
//             return str.indexOf(val) != -1 ? true : false;
//         },
//         toSearch: function () {    //在拓扑中高亮搜索结果
//             var _this = this;
//             var $nodes = tp.vis.selectAll('.node').classed('selected', false);
//             this.$wrap.find('.bustp_searchList').hide();
//             var val = this.$wrap.find('#bustp_resSearch').val().replace(/\s+/g, "");
//             if (val == '') {
//                 return;
//             }
//             $nodes.each(function (d) {
//                 var str;
//                 if (d.nodeType == 'NE') {
//                     str = '【' + d.ip + '】' + d.name;
//                 } else if (d.nodeType == 'Cluster') {
//                     str = '【集群】' + d.customName;
//                 } else {
//                     str = '【业务系统】' + d.name;
//                 }
//                 if (_this.find(str, val)) {
//                     d3.select(this).classed('selected', true);
//                 }
//             })
//             _this = $nodes = null;
//         },
//         listClick: function (ele) {
//             this.$wrap.find('input').val($(ele).attr('title'));
//         },
//         bind: function () {    //绑定事件
//             var _this = this;
//             this.$wrap.find('#bustp_resSearch').off('focus').on('focus', function () {
//                 _this.updateOriginData();
//             })
//             this.$wrap.find('#bustp_resSearch').off('input propertychange').on('input propertychange', function () {
//                 var value = $(this).val().trim();
//                 _this.showList(value);
//             })
//             this.$wrap.off('click', '.rg-search,.tp_allRes').on('click', '.rg-search,.tp_allRes', function () {
//                 if ($(this).hasClass('tp_allRes')) {
//                     _this.listClick(this);
//                 }
//                 _this.toSearch();
//             })
//         }
//     }
//     busTopoSearch.init();
// })

// 新增
var businessApi = {
  topoConfig: function (tpId, callback, isAsync) { // 获取拓扑全局设置
    $.ajax({
      url: gbs.host + '/business/topology/backgroundSet/' + tpId,
      type: 'GET',
      async: isAsync && true,
      success: function (data) {
        if (data.success) {
          (typeof callback === 'function') && callback(data.obj || {})
        }
      }
    })
  },
  businessMainTopo: function (businessId, successCb, errorCb) { // 根据业务ID 获取拓扑图中的节点、连线、链路信息
    var nodesInfo = []
    $.ajax({
      url: gbs.host + '/business/topology/' + businessId,
      type: 'GET',
      success: function (data) {
        if (data.success) {
          nodesInfo = data.obj;
          (typeof successCb === 'function') && successCb(data.obj || {})
        } else {
          (typeof errorCb === 'function') && errorCb(data.msg)
        }
      }
    })
    return nodesInfo
  },
  //* *区域**//*
  getRegion: function (businessId, callback) {
    $.ajax({
      url: gbs.host + '/business/topology/region/' + businessId,
      type: 'GET',
      async: false,
      success: function (data) {
        if (data.success) {
          (typeof callback === 'function') && callback(data.obj || [])
        }
      }
    })
  }
}
export default initBusTp
