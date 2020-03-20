/*
 * opt={
 *  selector:'#tp',
 *  config:{}   //全局设置
 * }
 * */
import levelMapName from './../topo/enum'
import { gbs } from '@/config/settings'
function mapTopology (opt) {
  this.defaultConfig = {
    width: 24,
    height: 24,
    fontSize: 12,
    textColor: '#000',
    lineWidth: 1
  }
  this.tpId = opt.tpId || '01677d30-9c0b-42ae-a934-11fbae6dc908' // add
  this.isMonit = opt.isMonit
  this.config = $.extend({}, this.defaultConfig, opt.config)
  this.ele = d3.select(opt.selector)[0][0]
  this.canvasWidth = this.ele.clientWidth
  this.canvasHeight = this.ele.clientHeight
  this.force = d3.layout.force().distance(160).charge(-3000).size([this.canvasWidth, this.canvasHeight / 2])
  this.force.start()
  this.nodes = []
  this.links = []
  this.nodes = this.force.nodes()
  this.links = this.force.links()

  if (opt.isShow) {
    this.createMainSvg(opt.mpCode)
  }
  this.lineC = d3.svg.line().interpolate('linear')
  this.lineradial = d3.svg.line.radial()
    .radius(function (d) { return d.r })
    .angle(function (d) { return d.angle })
    .tension(0.1)
  this.showTip = true // 控制悬浮提示是否显示,默认显示
  this.isMarquee = false // 是否在框选
  this.iconChange = true // baseData.mapTopo.iconChange
}

mapTopology.prototype = {
  constructor: mapTopology,
  createMainSvg: function (mpCode) {
    var _this = this
    var toporoot, selectMapNum
    if ($('#home-html').length > 0) {
      this.tip = d3.select('#home-html').append('div').classed('tp-tip', true).classed('hide', true) // 提示信息
    } else {
      this.tip = d3.select('body').append('div').classed('tp-tip', true).classed('hide', true) // 提示信息
    }
    this.svgContainer = d3.select(this.ele).append('svg')
      .attr('width', '100%')
      .attr('height', '100%')
      .attr('viewBox', '0 0 ' + this.canvasWidth + ' ' + this.canvasHeight)
    // var mpCode = $('#map_topo_wrap').attr('mapId').substring($('#map_topo_wrap').attr('mapId').length - 6)
    mpCode = mpCode.substring(mpCode.length - 6)
    if (mpCode.substring(2, 6) === '0000') {
      selectMapNum = mpCode
    } else {
      var num = mpCode.substring(0, 2)
      selectMapNum = num + '0000/' + mpCode
    }
    $.ajax({
      url: gbs.host + '/resources/data/mapJson/' + selectMapNum + '.geoJson',
      async: false,
      success: function (data) {
        toporoot = JSON.parse(data)
        toporoot.type = 'FeatureCollection'
      }
    })
    this.center = d3.geo.centroid(toporoot)
    this.bounds = d3.geo.bounds(toporoot)
    var extra = mpCode == '100000' ? _this.canvasHeight * 0.4 : this.flagGoe(mpCode) ? 0 : 110 // 这里又是啥？
    var geoDis = d3.geo.distance(this.bounds[0], this.bounds[1])
    this.scale = Math.floor((_this.canvasHeight + extra) / (geoDis > 3 ? (geoDis / 15) : geoDis) / Math.sqrt(2))
    this.projection = d3.geo.mercator()
      .center(this.center)
      .scale(this.scale)
      .translate([_this.canvasWidth / 2, _this.canvasHeight / 2])

    this.path = d3.geo.path()
      .projection(this.projection)
    var path = this.path

    this.defs = this.svgContainer.append('defs')

    this.arrowMarker = this.defs.append('marker')
      .attr('id', 'arrow')
      .attr('markerUnits', 'strokeWidth')
      .attr('markerWidth', '12')
      .attr('markerHeight', '12')
      .attr('viewBox', '0 0 12 12')
      .attr('refX', '6')
      .attr('refY', '6')
      .attr('orient', 'auto')

    this.arrow_path = 'M2,2 L10,6 L2,10 L6,6 L2,2'

    this.arrowMarker.append('path')
      .attr('d', this.arrow_path)
      .attr('fill', '#000')

    this.startMarker = this.defs.append('marker')
      .attr('id', 'startPoint')
      .attr('markerUnits', 'strokeWidth')
      .attr('markerWidth', '12')
      .attr('markerHeight', '12')
      .attr('viewBox', '0 0 12 12')
      .attr('refX', '6')
      .attr('refY', '6')
      .attr('orient', 'auto')

    this.startMarker.append('circle')
      .attr('cx', 6)
      .attr('cy', 6)
      .attr('r', 2)
      .attr('fill', '#000')

    this.copy = this.svgContainer.append('g').attr('class', 'containerDataCopy')
    this.china = this.svgContainer.append('g').attr('class', 'containerData')

    this.provinces = this.china.selectAll('path')
      .data(toporoot.features)
      .enter()
    this.provincesCopy = this.copy.selectAll('path')
      .data(toporoot.features)
      .enter()

    this.provinces.append('path')
      .attr('class', 'province')
      .style('fill', function (d) {
        return d.backColor
      })
      .attr('d', this.path)
    this.provincesCopy.append('path')
      .attr('class', 'provincesCopy')
      .style('fill', function (d) {
        return d.backColor
      })
      .attr('d', this.path)

    this.provinces.append('text').attr('class', 'mptText').text(function (d) {
      return d.properties.name
    })
      .each(function (d) {
        // var p = projection(d.properties.cp);
        var p = path.centroid(d.geometry)
        d.isBind = false
        d3.select(this).attr({
          x: p[0],
          y: p[1]
        })
      })
    // });
    this.nodeLine = this.china.append('g').attr('id', 'nodeLine')
    this.marqueeRect = this.svgContainer.append('rect').classed('marqueeRect', true)
    this.dragline = this.svgContainer.append('svg:path').attr({
      'class': 'link dragline hidden'
    })
    this.createLiquid()
    _this = null
    return this
  },
  flagGoe: function (code) { // 判断地图上下是否过长
    if (code == '210100' || code == '230000' || code == '654300' || code == '650100' || code == '230600' || code == '152200' || code == '150700' || code == '120000' || code == '659004' || code == '659001' || code == '150300') {
      return true
    } else {
      return false
    }
  },
  update: function (neSwitch) {
    this.enterLinks(this.nodeLine)
    this.enterNodes(neSwitch)
    this.updateNode().updateNodeImg().updateNodeRect()
    this.updateLink()
    this.nodeTick()
  },
  updateNodeImg: function (selection) {
    var _this = this
    var colors = {
      'Warning': '#fe943e',
      'Good': '#0088cc',
      'Unconnection': '#ef2446',
      'Unknow': 'grey',
      'Loading': 'grey'
    }
    var nodes = selection || this.china.selectAll('.node')
    nodes.each(function (d) {
      if (_this.iconChange) {
        d3.select(this).select('.nodeImg').attr({
          'width': d.width,
          'height': d.height,
          'id': d.id,
          'href': _this.setNodeImg(d)
        })
      } else {
        d3.select(this).select('.sport').attr({
          'fill': colors[d.runStatus]
        })
        d3.select(this).select('.sportOne').attr({
          'stroke': colors[d.runStatus]
        })
        d3.select(this).select('.sportTwo').attr({
          'stroke': colors[d.runStatus]
        })
      }
    })
    _this = nodes = null
    return this
  },
  updateNode: function () { // 隐藏网元
    var nodes = this.china.selectAll('.node')
    nodes.classed('hide', function (d) {
      return !d.show
    })
    return this
  },
  updateNodeRect: function (isSelected) {
    var nodes = isSelected ? this.china.selectAll('.node.selected') : this.china.selectAll('.node')
    nodes.each(function (d) {
      var node = d3.select(this)
      var rect = node.select('.nodeRect').attr('width', 0).attr('height', 0)
      var isHide = node.classed('hide')
      isHide && node.classed('hide', false) // getBBox方法在firefox不能用于隐藏的元素
      var box = this.getBBox()
      rect.attr({
        'width': box.width,
        'height': box.height
      })
      d.outerWidth = box.width
      d.outerHeight = box.height
      isHide && node.classed('hide', true)
      // node = rect = $img = $text = box = null
      node = rect = box = null
    })
    nodes = null
    return this
  },
  setNodeImg: function (d) {
    if (d.iconId) {
      return gbs.host + '/monitor/topo/getIcon/' + d.iconId + '/' + (d.runStatus || 'Loading')
    }
  },
  enterEnableLink: function () {
    var _this = this
    this.subLinks = []
    this.netLinks = this.links.filter(function (d, i) { // 子网的networkLinkId
      // == ''
      if (d.networkLinkId === '') {
        d.linkStatus = 'Enable'
        _this.subLinks.push(d)
      }
      if ((d.linkStatus == 'Enable' || d.linkStatus == 'Alert') && d.show) {
        return d.networkLinkId
      }
    })
    var use = this.nodeLine.selectAll('use').data(this.netLinks.concat(this.subLinks), function (d) {
      return d.id
    })

    use.enter().insert('svg:use', 'g.node').attr({
      'width': 2,
      'height': 2,
      'x': 0,
      'y': -10,
      'href': '#liquid'
    }).attr('id', function (d) {
      return d.id
    }).append('animateMotion').attr({
      dur: '3s',
      rotate: 'auto',
      repeatCount: 'indefinite'
    })
    use.exit().remove()
    this.enterEnableText().updateEnableText()
    _this = null
    return this
  },
  enterEnableText: function () {
    var _this = this
    var text = this.nodeLine.selectAll('text').data(this.netLinks, function (d) {
      return d.id
    })

    text.enter().insert('svg:text', 'g.node').classed('pathText', true).attr('dy', function (d) {
      return (d.linkIndex == -1) ? '-6' : '.71em'
    }).append('textPath').attr('xlink:href', function (d) {
      return '#' + d.id
    })
    text.exit().remove()
    _this = text = null
    return this
  },
  updateEnableText: function (selection) {
    var _this = this
    selection = selection || this.nodeLine.selectAll('textPath')
    selection.text(
      function (d) {
        var label = d.label || _this.config.label
        if (label == 'BandwidthUsage') {
          return addUnit(d.speedUsage, '%') || '--'
        }
        if (label == 'Traffic') {
          var str = bytesToSize(d.upBps === null ? undefined : d.upBps) ? '↑ ' + bytesToSize(d.upBps) + 'ps ↓' +
                        bytesToSize(d.downBps) + 'ps' : '↑-- ↓--'
          return str
        }
        return ''
      }).attr('startOffset', '55%')
    _this = null
    return this
  },
  nodeTick: function () { // 拖动时设置
    var _this = this
    this.china.selectAll('.node').each(function (d) {
      d.px = d.x
      d.py = d.y
      d3.select(this).attr({
        x: d.x,
        y: d.y
      })
    })
    this.lineTick()
    _this = null
  },
  enterNodes: function (neSwitch) {
    var _this = this
    var nodes = this.nodes
    if (neSwitch) {
      this.china.selectAll('.node').remove()
    }
    var node = this.china.selectAll('.node').data(nodes, function (d) {
      return d.id
    })
    this.updateNodeData()
    var svg = node.enter().append('svg:svg').classed('node', true)
    if (_this.iconChange) {
      svg.append('svg:rect').classed('nodeRect', true)
      svg.append('svg:image').classed('nodeImg', true).on('mouseenter', function (d) {
        if (!_this.showTip || (d.nodeType !== 'NE' && d.nodeType !== 'SubnetTopo')) { // 子网
          return
        }
        _this.tip.text(_this.nodeTip(d)).classed('hide', false)
        _this.tipRange(d3.event.pageX + 20, d3.event.pageY + 20)
      }).on('mouseleave', function (d) {
        _this.tip.classed('hide', true)
      })
    } else {
      svg.append('svg:circle').classed('sportFour', true)
        .attr('r', '10')
        .attr('cx', '10')
        .attr('cy', '10')
      svg.append('svg:circle').classed('sport', true)
        .attr('r', '4')
        .attr('stroke-width', '1')
        .attr('cx', '10')
        .attr('cy', '10').on('mouseenter', function (d) {
          if (!_this.showTip || (d.nodeType !== 'NE' && d.nodeType !== 'SubnetTopo')) { // 子网
            return
          }
          _this.tip.text(_this.nodeTip(d)).classed('hide', false)
          _this.tipRange(d3.event.pageX + 20, d3.event.pageY + 20)
        }).on('mouseleave', function (d) {
          _this.tip.classed('hide', true)
        })
      svg.append('svg:circle').classed('sportOne', true)
        .attr('r', '4')
        .attr('cx', '10')
        .attr('cy', '10')
      svg.append('svg:circle').classed('sportTwo', true)
        .attr('r', '4')
        .attr('cx', '10')
        .attr('cy', '10')
    }
    node.exit().remove()
    return this
  },
  updateNodeData: function (selection) {
    selection = selection || this.china.selectAll('.node')
    selection.each(function (d) { // 刷新其子元素绑定的数据，暂未有更好的方法
      d3.select(this).selectAll('.nodeImg').each(function () {
        this.__data__ = d
      })
    })
    // seletion = null
    selection = null
    return this
  },
  nodeTip: function (d) { // TODO 添加异步请求数据显示
    d.alertLevel = null
    var label = [{
      name: '名称',
      key: 'name'
    }, {
      name: 'IP地址',
      key: 'ip'
    }, {
      name: '设备类型',
      key: 'neClassText'
    }, {
      name: '厂商',
      key: 'vendor'
    }, {
      name: '设备状态',
      key: 'runStatusText'
    }, {
      name: 'CPU利用率',
      key: 'cpuAvg',
      unit: '%'
    }, {
      name: '内存利用率',
      key: 'memoryAvg',
      unit: '%'
    }, {
      name: '告警等级',
      key: 'alertLevel'
    }]
    if (d.nodeType == 'NE') {
      var indicatorNames = []
      if (d.baseNeClass == 'host') {
        indicatorNames = ['cpu_usage_avg', 'physical_memory']
      } else if (d.baseNeClass == 'network') {
        indicatorNames = ['network_cpu', 'network_memory']
      }
      $
        .ajax({
          url: gbs.host + '/monitor/ne/view/' + d.neId,
          dataType: 'json',
          data: {
            indicatorNames: indicatorNames.length > 0
              ? indicatorNames.join(',')
              : ''
          },
          type: 'post',
          async: false,
          success: function (res) {
            if (res.success == true) {
              var data = res.obj
              d.alertLevel = data.alertLevel || d.alertLevel
              if (data.indicators) {
                $
                  .each(
                    data.indicators,
                    function (idx, idc) {
                      if (!idc.indicatorValue && !idc.value) {
                        return
                      }
                      if (idc.indicatorName == 'physical_memory') {
                        if (Array.isArray(idc.indicatorValue)) {
                          idc.indicatorValue.length > 0 &&
                                                        (d.memoryAvg = idc.indicatorValue[0].memory_usage)
                        } else {
                          d.memoryAvg = idc.indicatorValue.memory_usage
                        }
                      } else if (idc.indicatorName == 'cpu_usage_avg') {
                        if (Array.isArray(idc.indicatorValue)) {
                          idc.indicatorValue.length > 0 &&
                                                        (d.cpuAvg = idc.indicatorValue[0].result)
                        } else {
                          d.cpuAvg = idc.indicatorValue.result
                        }
                      } else if (idc.indicatorName == 'network_memory') {
                        if (Array.isArray(idc.indicatorValue)) {
                          idc.indicatorValue.length > 0 &&
                                                        (d.memoryAvg = idc.indicatorValue[0].memory_usage)
                        } else {
                          d.memoryAvg = idc.indicatorValue.memory_usage
                        }
                      } else if (idc.indicatorName == 'network_cpu') {
                        if (Array.isArray(idc.indicatorValue)) {
                          idc.indicatorValue.length > 0 &&
                                                        (d.cpuAvg = idc.indicatorValue[0].used_cpu_usage)
                        } else {
                          d.cpuAvg = idc.indicatorValue.used_cpu_usage
                        }
                      } else if (idc.text == '内存配额使用率') {
                        d.memoryAvg = idc.value
                      } else if (idc.text == 'CPU配额使用率') {
                        d.cpuAvg = idc.value
                      }
                    })
              }
            }
          }
        })
    } else if (d.nodeType == 'SubnetTopo' && d.runStatus == 'Warning') {
      $.ajax({
        url: gbs.host + '/monitor/topo/view/' + d.neId,
        dataType: 'json',
        type: 'post',
        async: false,
        success: function (res) {
          if (res.success == true) {
            var data = res.obj
            d.alertLevel = data.alertLevel || d.alertLevel
          }
        }
      })
    }
    var str = ''

    $.each(label, function (i, o) {
      if (d.baseNeClass == 'storage' && (o.key == 'cpuAvg' || o.key == 'memoryAvg')) {
        return true
      }
      var unit = [o.unit] || ''
      var v = addUnit(d[o.key], unit)
      if (o.key == 'alertLevel') {
        v = levelMapName[parseInt(v)]
      }
      v = ((v || v === 0) ? v : '--')
      if (d.runStatus == 'Unknow' &&
                (o.key == 'cpuAvg' || o.key == 'memoryAvg' || o.key == 'alertLevel')) { // 资源状态为未知，不展示cpu、内存、告警
        v = '--'
      }
      str += (o.name + '：' + v + '\n')
    })
    return str
  },
  linkTip: function (d) {
    d.alertLevel = null
    if (d.linkStatus == 'Alert' || d.linkStatus == 'Unconnection') {
      $.ajax({
        url: gbs.host + '/monitor/topo/linkView/' + d.networkLinkId,
        dataType: 'json',
        type: 'post',
        async: false,
        success: function (res) {
          if (res.success == true) {
            var data = res.obj
            d.alertLevel = data.alertLevel || d.alertLevel
          }
        }
      })
    }
    var label = [{
      name: '源端',
      key: 'source'
    }, {
      name: '源端接口',
      key: 'sourceIfName'
    }, {
      name: '目的端',
      key: 'target'
    }, {
      name: '目的端接口',
      key: 'targetIfName'
    }, {
      name: '状态',
      key: 'linkStatusText'
    }, {
      name: '上行流量',
      key: 'upBps'
    }, {
      name: '下行流量',
      key: 'downBps'
    }, {
      name: '带宽利用率',
      key: 'speedUsage',
      unit: '%'
    }, {
      name: '告警等级',
      key: 'alertLevel'
    }]
    var str = ''
    $.each(label, function (i, o) {
      var unit = [o.unit] || ''
      if (o.key == 'upBps' || o.key == 'downBps') {
        var v = bytesToSize(d[o.key] === null ? undefined : d[o.key]) ? bytesToSize(d[o.key]) + 'PS' : '--'
      } else {
        var v = addUnit(d[o.key], unit)
      }

      if (o.key == 'alertLevel') {
        v = levelMapName[parseInt(v)]
      }
      if (o.key == 'source' || o.key == 'target') {
        if (d[o.key].nodeType == 'SubnetTopo') {
          v = d[o.key + 'NeName'] + '[' + d[o.key + 'Ip'] + ']'
        } else {
          v = d[o.key].name + '[' + d[o.key].ip + ']'
        }
      }

      v = ((v || v === 0) ? v : '--')
      str += (o.name + '：' + v + '\n')
    })
    return str
  },
  setForce: function (flag) {
    flag ? this.force.stop() : this.force.start()
    this.force.nodes().forEach(function (d, i) {
      d.fixed = flag
    })
  },
  enterLinks: function (parent) {
    var _this = this
    var linktree = this.links
    var linkCopy = this.nodeLine.selectAll('path.linkId').data(linktree, function (d) {
      return d.id
    })
    var link = this.nodeLine.selectAll('path.linkR').data(linktree, function (d) {
      return d.id
    })
    link.attr('status', function (d) {
      var oldStatus = d3.select(this).attr('status')
      if (oldStatus) {
        d3.select(this).classed(d.linkStatus, false)
      }
      d3.select(this).classed(d.linkStatus, true)
      return d.linkStatus
    })
    linkCopy.enter().insert('svg:path', 'g.node').attr('class', 'link linkId').attr('linkId', function (d) {
      return d.id
    }).on('mouseenter', function (d) {
      // $(mapTp.ele).find('#' + d.id).attr('stroke-width', '8px')
      $(_this.ele).find('#' + d.id).attr('stroke-width', '8px')
    }).on('mouseleave', function (d) {
      // $(mapTp.ele).find('#' + d.id).attr('stroke-width', d.linkClass['stroke-width'])
      $(_this.ele).find('#' + d.id).attr('stroke-width', d.linkClass['stroke-width'])
    })
    link.enter().insert('svg:path', 'g.node').attr('class', 'link linkR').attr('id', function (d) {
      return d.id
    }).on('mouseenter', function (d) {
      if (_this.showTip && d.networkLinkId) {
        _this.tip.html(_this.linkTip(d)).classed('hide', false)
        _this.tipRange(d3.event.pageX + 20, d3.event.pageY + 20)
      }
    }).on('mouseleave', function (d) {
      _this.tip.classed('hide', true)
    })
    link.exit().remove()
    linkCopy.exit().remove()
    this.enterEnableLink()
    return this
  },
  tipRange: function (x, y) { // tip范围确定
    var tip = this.tip.node().getBoundingClientRect()
    var maxX = window.innerWidth, maxY = window.innerHeight

    x = (x + tip.width) > maxX ? (maxX - tip.width - 100) : x
    y = (y + tip.height) > maxY ? (maxY - tip.height) : y
    this.tip.style({
      left: x + 'px',
      top: y + 'px'
    })
  },
  cancelSelected: function () {
    this.china.selectAll('.selected').classed('selected', false)
  },
  hideTip: function () { // 隐藏提示
    this.tip.classed('hide', true)
  },
  addNode: function (node, x, y) { // 增加节点
    var index = this.findNodeIndex(node.id)
    node = $.extend({}, this.config, node)
    if (node.status == 'DELETED' && index != -1) {
      this.deleteNodes(node)
      return
    }
    if (index == -1 && node.status !== 'DELETED') {
      node.fixed = true
      node.x = x - 7
      node.y = y - 10
      this.nodes.push(node)
    } else {
      this.nodes[index] = $.extend({}, this.nodes[index], node)
    }
  },
  addNodes: function (nodes) { // 增加多个节点
    var _this = this
    var p
    if (Object.prototype.toString.call(nodes) == '[object Array]') {
      if (nodes == false) {
        $.each(_this.provinces[0], function (i, d) {
          d3.select(this).each(function (d) {
            d.isBind = false
            d.neId = ''
          })
        })
      } else {
        $.each(nodes, function (i, j) {
          delete j.x
          delete j.y
          $.each(_this.provinces[0], function (k, l) {
            if (j.status == 'DELETED') {
              d3.select(this).each(function (d) {
                d.isBind = false
                d.neId = ''
              })
              _this.addNode(j, j.x, j.y)
            } else {
              if (l.__data__.properties.adcode == parseInt(j.locationCode.substring(j.locationCode.length - 6))) {
                d3.select(this)
                  .each(function (d) {
                    p = _this.path.centroid(d.geometry)
                    d.isBind = true
                    d.neId = j.neId
                  })
                if (l.__data__.properties.adcode == 620000) {
                  _this.addNode(j, p[0], p[1] - 10)
                } else if (l.__data__.properties.adcode == 130000) {
                  _this.addNode(j, p[0] - 15, p[1])
                } else {
                  _this.addNode(j, p[0], p[1])
                }
              }
            }
          })
        })
      }
    }
    _this = null
    return this
  },
  deleteNodes: function (nodes) { // 删除节点及其相关连线
    var _this = this
    var nodesArr = this.nodes
    var linksArr = this.links
    if (_.isPlainObject(nodes)) {
      nodes = [nodes]
    }
    if (_.isArray(nodes)) {
      $.each(nodes, function (i, d) {
        _.remove(nodesArr, function (n) {
          return d.id == n.id
        })
        _.remove(linksArr, function (n) {
          return d.id == n.sourceNodeId || d.id == n.targetNodeId
        })
      })
    }
    return this
  },
  findNodeIndex: function (id) { // 查找节点所在序号
    var nodes = this.nodes
    for (var i in nodes) {
      if (nodes[i]['id'] == id) { return i }
    }
    return -1
  },
  addLink: function (source, target, link) { // 增加连线
    var oldLink = _.find(this.links, ['id', link.id])
    if (oldLink) {
      oldLink.handSpeed = null
      delete oldLink.downBps
      delete oldLink.upBps
      delete oldLink.speedUsage
      if (oldLink.sourceId == link.targetId) {
        var oldSourseValue = oldLink.sourceIp
        oldLink.sourceIp = oldLink.targetIp
        oldLink.targetIp = oldSourseValue
      }
    }
    var o = this.changeLinkData(source, target, link, this.tpId)
    if (oldLink) {
      o = $.extend(true, _.cloneDeep(oldLink), o)
      _.remove(this.links, ['id', link.id])
    }
    return o
  },
  addLinks: function (links) {
    this.enterLinks()
    if (Object.prototype.toString.call(links) == '[object Array]') {
      var _this = this, newLinks = []
      links.forEach(function (link) {
        var s = link['sourceNodeId'], t = link['targetNodeId']
        if (s && t) {
          newLinks.push(_this.addLink(s, t, link))
        }
      })
      this.enterLinks() // 销毁删掉数据的dom
      this.links = this.links.concat(newLinks)
      this.enterLinks() // 对新的数据创建dom
      this.updateLink().lineTick()
    }
    _this = null
    return this
  },
  changeLinkData: function (source, target, link, tpId) {
    if (source && target) {
      var s = this.findNode(source) || source
      var t = this.findNode(target) || target
      return $.extend({}, link, {
        source: s,
        target: t,
        // topoId: baseData.mapTopo.tpId,
        topoId: tpId,
        sourceIp: s.ip,
        sourceNeId: s.neId,
        targetIp: t.ip,
        targetNeId: t.neId,
        sourceNodeId: s.id,
        targetNodeId: t.id,
        show: s.show && t.show
      })
    }
  },
  findNode: function (id) { // 查找节点
    var nodes = this.nodes
    for (var i in nodes) {
      if (nodes[i]['id'] == id) { return nodes[i] }
    }
    return null
  },
  updateLink: function (isSelected) {
    var _this = this
    var links = isSelected ? this.china.selectAll('.link.selected') : this.china.selectAll('.link')
    links.classed('hide', function (d) {
      var obj = {
        'stroke-width': d.lineWidth || _this.config.lineWidth,
        'stroke-dasharray': (d.linkClass && d.linkClass['stroke-dasharray']) || '0'
      }
      d.linkClass = $.extend(d.linkClass, obj)
      if ($(this).attr('id')) {
        d3.select(this).attr(d.linkClass).attr('class', 'link linkR ' + d.linkStatus)
      } else {
        d3.select(this).attr(d.linkClass).attr('class', 'link linkId')
      }
      return !d.show
    })
    _this = links = null
    return this
  },
  lineTick: function () {
    var _this = this
    _this.nodeLine
      .selectAll('.link')
      .each(
        function (d) {
          var s = _.filter(_this.nodes, {
            id: d.sourceNodeId
          })[0]
          var t = _.filter(_this.nodes, {
            id: d.targetNodeId
          })[0]
          if (s &&
                        t &&
                        ((!_this.dragNodeId && !_this.dragLinkId) ||
                            _this.dragNodeId.indexOf(s.id) !== -1 ||
                            _this.dragNodeId.indexOf(t.id) !== -1 || _this.dragLinkId == d.id)) {
            d = $.extend(d, _this.changeLinkData(s, t, d, _this.tpId))
            this.__data__ = d
            var path = _this.polygonalLine(s, t, d)
            d3.select(this).attr('d', path)
          }
        })

    _this.nodeLine.selectAll('use').each(
      function (d) {
        if ((!_this.dragNodeId && !_this.dragLinkId) ||
                    _this.dragNodeId.indexOf(d.sourceNodeId) !== -1 ||
                    _this.dragNodeId.indexOf(d.targetNodeId) !== -1 ||
                    _this.dragLinkId == d.id) {
          var id = d3.select(this).attr('id')
          var path = _this.nodeLine.selectAll('.link[id="' + id + '"]').attr('d')
          d3.select(this).select('animateMotion').attr('path', path)
        }
      })

    _this.nodeLine.selectAll('.lineWrap').each(
      function (d) {
        if ((!_this.dragNodeId && !_this.dragLinkId) ||
                    _this.dragNodeId.indexOf(d.sourceNodeId) !== -1 ||
                    _this.dragNodeId.indexOf(d.targetNodeId) !== -1 ||
                    _this.dragLinkId == d.id) {
          d3.select(this).attr('class', 'lineWrap ' + d.linkClass.lineType)
        }
      })
    _this.nodeLine.selectAll('textPath').each(
      function (d) {
        d3.select(this.parentNode).attr('transform', '')
        if (d != undefined) {
          if (!_this.dragNodeId ||
                        (_this.dragNodeId.indexOf(d.sourceNodeId) !== -1 || _this.dragNodeId
                          .indexOf(d.targetNodeId) !== -1)) {
            if ((d.target.x - d.source.x) < 0) {
              var b_box = d3.select(this.parentNode).node().getBBox()
              var x = b_box.x + b_box.width / 2
              var y = b_box.y + b_box.height / 2
              d3.select(this.parentNode).attr('transform',
                'rotate(180, ' + x + ',' + y + ')')
            }
          }
        }
      })
    _this.updateEnableText()
    _this = null
  },
  polygonalLine: function (s, t, d) { // 折线
    var sx = s.x + s.width / 2
    var sy = s.y + s.height / 2
    var tx = t.x + t.width / 2
    var ty = t.y + t.height / 2
    var linkClass = d.linkClass
    var linkSum = this.findLinkSum(s, t)
    var l = Math.abs(Math.sqrt((s.x - t.x) * (s.x - t.x) + (s.y - t.y) * (s.y - t.y))) || 1
    sx = sx - ((s.x - t.x) / l * s.width * 0.5)
    sy = sy - ((s.y - t.y) / l * s.height * 0.5)
    tx = tx + ((s.x - t.x) / l * t.width * 0.5)
    ty = ty + ((s.y - t.y) / l * t.height * 0.5)
    if (linkSum > 1) {
      sx = sx + ((s.y - t.y) / l * 5 * d.linkIndex)
      sy = sy - ((s.x - t.x) / l * 5 * d.linkIndex)
      tx = tx + ((s.y - t.y) / l * 5 * d.linkIndex)
      ty = ty - ((s.x - t.x) / l * 5 * d.linkIndex)
    }
    if (linkClass && linkClass.lineType == 'arc') {
      var route = this.lineC([[sx, sy], [tx, ty]])
      var center = (sx + tx / 2, sy + ty / 2)
      var x = (sx + tx) / 2 - 20
      var y = (sy + ty) / 2 - 30
      if (sx < tx && sy < ty || sx > tx && sy > ty) {
        var x = (sx + tx) / 2 + 10
        var y = (sy + ty) / 2 - 20
      }
      var pathArr = route.split('L')
      var qPath = 'Q' + x + ',' + y + ' ' + pathArr[1].substr(0)
      return pathArr[0] + ' ' + qPath
    } else {
      return this.lineC([[sx, sy], [tx, ty]])
    }
  },
  findLinkByLinkId: function (id) {
    var index = _.findIndex(this.links, function (o) {
      return id == o.id
    })
    return index
  },
  findLinkIndex: function (sourceNode, targetNode) {
    var s0 = sourceNode.id, t0 = targetNode.id
    var index = _.findIndex(this.links, function (o) {
      var s = o.sourceNodeId, t = o.targetNodeId
      return (s0 == s && t0 == t) || (s0 == t && t0 == s)
    })
    return index
  },
  findLinkSum: function (sourceNode, targetNode) {
    var sum = 0
    var _links = this.links
    var s0 = sourceNode.id, t0 = targetNode.id
    $.each(_links, function (i, d) {
      var s = d.sourceNodeId, t = d.targetNodeId
      if ((s0 == s && t0 == t) || (s0 == t && t0 == s)) {
        sum++
      }
    })
    return sum
  },
  addNodeDrag: function () {
    this.china.selectAll('.node').on('mousedown.drag', null)
    return this
  },
  _addLink: function (callback) {
    var _this = this
    this.cancelNodeDrag()
    this.china.selectAll('.node').on('mousedown', null).on('mousedown', function (d) {
      // console.log('新增连线：node-mousedown')
      var downXY = d3.mouse(_this.china[0][0])
      _this.mousedownNode = d
      _this.dragline.lineData = [[downXY[0], downXY[1]]]
      _this.dragline.classed('hidden', false).attr('d', function (d) {
        return _this.lineC(_this.dragline.lineData)
      })
      d3.select(document).on('mousemove.tp', function () {
        // console.log('新增连线：node-mousemove')
        _this.cancelZoom()
        if (_this.mousedownNode == null) {
          return
        }
        _this.dragline.attr('d', function (d) {
          var xy = d3.mouse(_this.svgContainer.node())
          _this.dragline.lineData[1] = [xy[0], xy[1]]
          return _this.lineC(_this.dragline.lineData)
        })
      }).on('mouseup.tp', function () {
        var source = _this.mousedownNode
        var target = d3.event.target.__data__

        _this.dragline.classed('hidden', true)
        _this.dragline.lineData = []
        _this.mousedownNode = null
        d3.select(document).on('mousemove.tp', null).on('mouseup.tp', null)

        if (_this.canAddLinks(source, target)) {
          typeof callback === 'function' && callback([{
            // topoId: baseData.mapTopo.tpId,
            topoId: _this.tpId,
            sourceNodeId: source.id,
            targetNodeId: target.id
          }])
        }
      })
    })
  },
  createLiquid: function () {
    this.liquid = this.svgContainer.append('defs').append('g').attr('id', 'liquid').attr(
      'transform', 'translate(0,10)').html(
      '<path d="M-1,0 Q2,2 4,2 Q6,2 6,0 T4,-2 Q2,-2 -1,0" fill="#fff" ></path>')
  },
  cancelZoom: function () {
    this.svgContainer.classed('cursorMove', false).classed('cursorAuto', true)
    return this
  },
  canAddLinks: function (sor, tar) {
    // sor与tar不能为同一节点;节点间不能重复添加
    if (!sor || !tar) {
      return false
    }
    if (sor.nodeType && tar.nodeType) {
      if (sor.id == tar.id) {
        return false
      }
      return true
    }
    return false
  },
  cancelNodeDrag: function () {
    this.china.selectAll('.node').on('mousedown.drag', null)
    return this
  },
  destoryed: function () {
    this.cancelNodeDrag()
  }
}

// 给对应的数据加单位
function addUnit (value, unit, index) {
  if (typeof value === 'undefined' || value === 'undefined' || value === 'NaN' || value === 'null') {
    return '--'
  }
  if ((typeof value) === 'number' || !!Number(value)) {
    if (value % 1 != 0) {
      value = parseFloat(value).toFixed(2)
      value = value == '0.00' ? 0 : value
    }
  }
  if (!unit) {
    return value
  }
  switch (unit) {
    case 'timeStampToDate': // 时间戳转时间2017-10-19 15:27:32
      return value && Number(value * 1000) ? timestampformat(Number(value * 1000)) : '--'
    case 'bytesToSize':
      return bytesToSize(value, index)
    case 'duringTime': // 13天2时18分27秒
      return duringTime(value, index)
    default:
      if (value || value === 0) {
        unit = unit || ''
        return value + ' ' + unit
      } else {
        return '--'
      }
  }
}

// 保留两位小数，byte 转换 成其他
function bytesToSize (bytes, index) {
  if (bytes === 'undefined' || bytes === 'NaN' || bytes === 'null' || bytes === false) {
    return '--'
  }

  if (Number(bytes) !== 0 && !Number(bytes)) {
    return bytes
  }
  bytes = Number(bytes)
  var sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']
  if (bytes > 0) {
    var k = 1024
    var i = Math.floor(Math.log(bytes) / Math.log(k))
    if (i < 0) {
      return bytes.toFixed(2) + ' ' + sizes[index || 0]
    }
    return (bytes / Math.pow(k, i)).toFixed(2) + ' ' + sizes[index ? i + index : i]
  } else if (bytes === 0) {
    return '0 ' + sizes[index || 0]
  } else {
    return bytes
  }
}

function duringTime (t, unit) {
  var arr = ['天', '时', '分', '秒', '厘秒', '毫秒']
  var gap = [24, 60, 60, 100, 1000]
  var time = [null, null, null, null, null]
  unit = unit || '秒'
  var index = arr.indexOf(unit)
  if (t === 0) {
    return t + unit
  }
  for (var i = index; i >= 0; i--) {
    if (t <= 0) {
      break
    }
    if (i == 0) {
      time[0] = t + arr[i]
      break
    }
    time[i] = t % gap[i - 1] + arr[i]
    t = Math.floor(t / gap[i - 1])
  }

  return time.join('')
}
export default mapTopology
