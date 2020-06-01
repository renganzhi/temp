// clearInterval(baseData.mapTopo.refreshTimer);
// baseData.mapTopo.refreshTimer = null; //定时刷新控制器
import mapTopology from './topology'
import { gbs } from '@/config/settings'
// var mapTp = ''
var mpTopo = {
  initTopo: function (mapTp, userId, mpId, el) {
    // this.initToolBar()
    this.getNedata(mapTp, userId, mpId)
    // this.setRefreshTimer(mapTp)
  },
  setRefreshTimer: function (mapTp) { // 设置全局刷新定时器
    // clearInterval(baseData.mapTopo.refreshTimer);
    // baseData.mapTopo.refreshTimer = setInterval(function(){
    //     mpTopo.getNedata();
    //  },3*60000)
  },
  getNedata: function (mapTp, userId, mpId) {
    var _this = this
    // var userId = $('#map_attention .portletTitle .active').text().trim() == '分享地图' ? $('#map-tree-wrap .userClick').attr('userId') : ''
    // var mpId = $('#map_attention .portletTitle .active').text().trim() == '分享地图' ? $('#map-tree-wrap .userClick .curSelectedNode').attr('mpId') : $('#topoMapTree .curSelectedNode').attr('mpId')
    newDomainTopo(mpId, userId, function (datas) {
      // $.each(datas.data.obj.nodes, function (i, d) {
      // if (baseData.mapTopo.iconChange == undefined) {
      //   baseData.mapTopo.iconChange = d.showIcon
      // }
      // })
      mapTp.addNodes(datas.data.obj.nodes)
      mapTp.addLinks(datas.data.obj.links)
      mapTp.update()
      // linkManage.bind()
    })
    _this.iconChange()
  },
  initToolBar: function () {
    $('#mapTopoTool').removeClass('.tl-closed').find('div[data-tlClick="returnParentTopo"]')
  },
  iconChange: function () {
    // var className = baseData.mapTopo.iconChange == true ? 'u-switch-on' : 'u-switch-off'
    // $('#iconChange').removeClass('u-switch-on u-switch-off').addClass(className)
  },
  reset: function (mapTp, userId, mpId) {
    // $('#networkMapTopo').off()
    // $(document).off('click.contextMenu')
    // $.contextMenu('destroy', {
    //   selector: '#networkMapTopo'
    // })
  }
}
// mpTopo.reset()
// var mapTp = new mapTopology({
//   selector: '#networkMapTopo',
//   isShow: true,
//   isMonit: true
// })
// mpTopo.initTopo()

// if (!$('body').hasClass('onlyRead')) {
//   judgeBindMapEvent(mapTp)
// }

function judgeBindMapEvent (mapTp) {
  // $('#networkMapTopo').off('click.hidetl').on('click.hidetl', function (e) {
  //   if (mapTp.isMarquee) {
  //     mapTp.isMarquee = false
  //   } else {
  //     mapTp.cancelSelected()
  //   }
  //   $('#mapTopoTool .sub-tl').hide()
  // })

  // $('#networkMapTopo').off('click.tp').on('click.tp', '.province,.node,.link', function (e) {
  //   var o_this = d3.select(this)
  //   var isSelected = o_this.classed('selected')
  //   if (isSelected) {
  //     return
  //   }
  //   if (!e.ctrlKey) {
  //     mapTp.svgContainer.selectAll('.selected').classed('selected', false)
  //   }

  //   o_this.classed('selected', !isSelected)
  //   // mapTools.showLineToolChoosed();
  //   if (e.toElement.__data__.geometry) {
  //     $.each($('#topoMapTree .node_name'), function (i, d) {
  //       if (o_this[0][0].__data__.properties.adcode == $(this).attr('acode')) {
  //         $(this).closest('a').click()
  //       }
  //     })
  //   }

  //   e.stopPropagation()
  // })

  // $(document).off('click.contextMenu').on('click.contextMenu', function () {
  //   $('.context-menu-list').trigger('contextmenu:hide')
  // })

  mapTp.svgContainer.selectAll('.province').on('mouseenter', function (e) {
    var dom = $(this)
    mapTp.svgContainer.selectAll('.mptText').filter(function (d) {
      if (d.properties.name == $(e)[0].properties.name) {
        $(this).css('display', 'block')
      }
    })
    $.each($('#topoMapTree .node_name'), function (i, d) {
      if ($(this).attr('acode') == $(e)[0].properties.adcode && !$(e)[0].properties.sole) {
        dom.attr('class', 'province hoverMp')
      }
    })
  }).on('mouseleave', function (e) {
    mapTp.svgContainer.selectAll('.mptText').filter(function (d) {
      if (d.properties.name == $(e)[0].properties.name) {
        $(this).css('display', 'none')
      }
    })
    $(this).attr('class', 'province')
  })
  // mapTp.svgContainer.on('contextmenu', null).on('contextmenu', function () {
  //   baseData.topo.menuXY = d3.mouse(this)
  // })
}

function newDomainTopo (mapId, userId, callback) { // 地图拓扑数据加载
  $.ajax({
    method: 'post',
    url: gbs.host + '/monitor/topo/mapTopoDomain',
    async: false,
    data: {
      mapLocationId: mapId,
      userId: userId
    },
    success: function (data) {
      if (data.success) {
        (typeof callback === 'function') && callback(data.obj)
      }
    }
  })
}

function initMapTopo (data) {
  // mpTopo.reset()
  if (!data.mapCode) {
    return
  }
  var mapTp = new mapTopology({
    selector: data.el,
    isShow: true,
    isMonit: true,
    mpCode: data.mapCode || '100000'
  })
  mpTopo.initTopo(mapTp, data.userId, data.mpId)
  judgeBindMapEvent(mapTp)
}
export default initMapTopo
