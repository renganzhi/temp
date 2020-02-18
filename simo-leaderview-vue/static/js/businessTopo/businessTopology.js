/*
 * opt={
 *  selector:'#tp',
 *  config:{}   //全局设置
 * }
 * */
import { gbs } from '@/config/settings'
import levelMapName from './../topo/enum'
function businessTopology(opt,businessId){
    this.defaultConfig = {
            width:64,
            height:64,
            fontSize:12,
            textColor:'#000',
            lineWidth:1
    };
    this.config = $.extend({},this.defaultConfig,opt.config);
    this.businessId = businessId;
    this.ele = d3.select(opt.selector)[0][0];
    this.width = this.ele.clientWidth;  //可能可以去掉，暂时不去掉
    this.height = this.ele.clientHeight;
 //  this.expWidth = $("#testHeight")[0].clientWidth;  
   // this.expHeight =$("#testHeight")[0].clientHeight;	//防止页面无高度
    this.canvasWidth = this.width;
    this.canvasHeight = this.height;
//    this.canvasHeight = this.height ==0 ? 753:this.height;
    this.resetCanvasPoint();
    this.force = d3.layout.force()/* .linkDistance(120).gravity(.05) */.distance(160)
            /* .linkStrength(1) */.charge(-3000).size([this.canvasWidth, this.canvasHeight/2]);
    this.force.start();
    this.nodes =[];
    this.links = [];
    this.nodes = this.force.nodes();
    this.links = this.force.links();
    this.createMainSvg().createZoom();
    this.lineC = d3.svg.line().interpolate('linear');
    this.doNodeDrag();
    this.dragFn = opt.dragFn;
    this.dragPoint();
    this.showTip = true;    //控制悬浮提示是否显示,默认显示
    this.isMarquee = false; //是否在框选
    this.falg;
    this.bind();
}

businessTopology.prototype = {
    constructor:businessTopology,
    resetCanvasPoint:function(){    //重置画布最大最小值
        this.maxCanvasWidth = this.canvasWidth; 
        this.maxCanvasHeight = this.canvasHeight;
        this.minCanvasWidth = 0;    
        this.minCanvasHeight = 0;
    },
    setViewBox:function(x0,y0,x1,y1,ele){
        var str =  x0 + ' ' + y0 + ' ' + x1 + ' ' + y1;
        if(ele){
            return ele.attr('viewBox',str);
        }
        return str;
    },
    numberViewBox:function(str){
        return str.split(' ').map(function(d){
            return parseFloat(d)
        })
    },
    getViewPoint:function(view){
        if(Object.prototype.toString.call(view) == '[object String]'){
             view = view.split(' ').map(function(d){
                    return parseFloat(d)
                });
        }
        return [[view[0], view[1]],[(view[0] + view[2]), (view[1] + view[3])]];
    },
    scaleCoor:function(point, center, scale){   // 坐标缩放计算(仅针对viewBox),point
                                                // 要缩放的点,center 缩放的根据点,scale
                                                // 缩放的系数
        var dx = +point[0] - center[0];
        var dy = +point[1] - center[1];
        return [center[0] + (dx / scale), center[1] + (dy / scale)];
    },
    getShip:function(){ // 主图数据被拖动时的viewBox
        var xList = [];
        var yList = [];
        this.nodes.each(function(d) {
            xList.push(d.x);
            yList.push(d.y);
        });
        var xMin = d3.min(xList);
        var xMax = d3.max(xList);
        var yMin = d3.min(yList);
        var yMax = d3.max(yList);
        return [xMin, yMin, xMax - xMin, yMax - yMin];
    }, 
    createMainSvg:function(){
        var _this = this;
        this.inp = d3.select(this.ele).append('div').classed('rg-inp input-sm confData hide', true)
        .attr({'contenteditable':'true'});
        // this.tip = d3.select(this.ele).append('div').classed('tp-tip',true).classed('hide',true);   // 提示信息
        this.tip = d3.select('body').append('div').classed('tp-tip',true).classed('hide',true);   // 提示信息

        this.svgImage = d3.select(this.ele).append('img').attr({
            'src':gbs.host + '/business/topology/getIcon/'+(this.config.backgroundIconId || this.config.backgroundIconIdDefault)+'/Good',
            'width':'100%',
            'height':'100%',
            'class':'backgroundImage'
        }).style({
            'position' : 'absolute',
            'left' : '0',
            'top' : '0'
        });

        this.svgContainer = d3.select(this.ele).append('svg:svg').attr({
            'width':'100%',
            'height':'100% ',
            'id':'businessTest',
            'pointer-events':'all',
            'text-rendering' : 'geometricPrecision'
           // 'preserveAspectRatio':'xMidYMid slice'
        }).style({
            'position':'relative',
            'left':'0',
            'top':'0'
        }).attr('class','container_rect cursorMove');
        this.svgContainer.attr('viewBox',function(){
          // return _this.setViewBox(_this.canvasWidth / 2,_this.canvasHeight / 2,_this.canvasWidth,_this.canvasHeight);
          return _this.setViewBox(0,0,_this.canvasWidth,_this.canvasHeight);
        });

        this.container = this.svgContainer.append('g').attr('id', 'thumbnail_bus');
        this.createLiquid();
        this.dragline = this.container.append('svg:path').attr({
            'class':'link dragline hidden',
        });
        this.dragRect = this.container.append('rect').attr({
            'class' : 'dragRect hidden',
        }); //添加区域rect
        this.dragEllipse = this.container.append('ellipse').attr({
            'class' : 'dragRect hidden',
        });
        this.vis = this.container.append('g').attr('class','container_data');  
        this.marqueeRect = this.container.append('rect').classed('marqueeRect',true);
        this.regionWrap = this.vis.append('g').attr('id', 'region');
        this.nodeLine = this.vis.append('g').attr('id', 'nodeLine');
        this.linkPng = this.vis.append('g').attr('id', 'linkPng');
 //       this.isShowUse();
        _this = null;
        return this;
    },
    setBackground:function(iconId){
        this.config.backgroundIconId = iconId || this.config.backgroundIconId;
        this.svgImage.attr( 'src',gbs.host + '/business/topology/getIcon/'+(this.config.backgroundIconId)+'/Good');
    },
    createZoom:function(){
        var _this = this;
        this.IEoldX = 0;    // 记录ie下鼠标平移点击的位置
        this.IEoldY = 0;
        this.svgContainer.classed('cursorMove',true).classed('cursorAuto',false);
        this.zoomListener = d3.behavior.zoom().scaleExtent([0.1,10]).on('zoom',function(){
            var e = d3.event.sourceEvent, scale = d3.event.scale;
            if(!e || !e.type){
                return;
            }
            var $svg = _this.svgContainer;
            var dscale = scale / ($svg.property("zoomScale") || 1);
            var _viewBox = $svg.attr('viewBox');
            var viewBox = _this.numberViewBox(_viewBox);
            var viewBoxPoint = _this.getViewPoint(_viewBox);
            var maxW = _this.canvasWidth, maxH = _this.canvasHeight;
            var minX = 0, minY = 0, maxX = maxW, maxY = maxH;
            if(e.type == 'mousemove'){    // 平移，调整xy
                if(typeof e.movementX == 'number' || typeof e.mozMovementX == 'number'){
                    var dx = e.movementX || e.mozMovementX || 0;
                    var dy = e.movementY || e.mozMovementY || 0;
                }else{// ie
                    var dx = Math.min(_this.IEoldX ? (e.clientX - _this.IEoldX) : 0,50);
                    var dy = Math.min(_this.IEoldY ? (e.clientY - _this.IEoldY) : 0,50);
                    _this.IEoldX = e.clientX;
                    _this.IEoldY = e.clientY;
                }
                if(dx || dy){
                    dx = dx / ($svg.property("zoomScale") || 1);
                    dy = dy / ($svg.property("zoomScale") || 1);
                    _viewBox = [viewBox[0] - dx, viewBox[1] - dy,viewBox[2],viewBox[3]].join(' ');
                }
            }else if(e.type == "wheel" || typeof e.wheelDelta == "number"){// 缩放计算,调整wh
                var xy = d3.mouse(this);
                var _viewBoxPoint = [_this.scaleCoor(viewBoxPoint[0], xy, dscale),
                                     _this.scaleCoor(viewBoxPoint[1], xy, dscale)];
                var _x = _viewBoxPoint[0][0];
                var _y = _viewBoxPoint[0][1];
                var _width = _viewBoxPoint[1][0] - _viewBoxPoint[0][0];
                var _height = _viewBoxPoint[1][1] - _viewBoxPoint[0][1];
                _viewBox = [_x, _y,_width, _height].join(' ');
            }
            $svg.property("zoomScale", scale).attr("viewBox", _viewBox);
            $svg = null;
        });
        this.svgContainer.call(this.zoomListener);
        return this;
    },
    cancelZoom:function(){
        this.svgContainer.classed('cursorMove',false).classed('cursorAuto',true);
        this.zoomListener.on('zoom',null);
        return this;
    },
    createLiquid:function(){
    	var defs = this.container.append('defs');
    	defs.append('marker').attr({'id':'markerArrow','viewBox':'0 0 12 12','markerWidth':'12','markerHeight':'12','refX':'6','refY':'6','orient':'auto'})
    		.append('path').attr({'d':'M2,2 L10,6 L2,10 L6,6 L2,2','fill':'#1b85c3'});
    	defs.append('g').attr('id', 'liquid').attr(
                'transform', 'translate(0,10)').html('<path d="M-1,0 Q2,2 4,2 Q6,2 6,0 T4,-2 Q2,-2 -1,0" fill="#fff" ></path>');
    },
    setMaxCavWH:function(minx,maxx,miny,maxy){
        var _this=this;
        if(typeof minx != 'undefined'){
            _this.maxCanvasWidth = Math.max(maxx, _this.maxCanvasWidth);
            _this.minCanvasWidth = Math.min(minx, _this.minCanvasWidth);
            _this.maxCanvasHeight = Math.max(maxy, _this.maxCanvasHeight);
            _this.minCanvasHeight = Math.min(miny, _this.minCanvasHeight);
        }
        this.maxCanvasWidth -= this.minCanvasWidth;
        this.maxCanvasHeight -= this.minCanvasHeight;
        var flagW = this.canvasWidth<this.maxCanvasWidth, flagH = this.canvasHeight<this.maxCanvasHeight;
        var currWidth = 0,currHeight = 0;
        if(flagW || flagH){
            currWidth = flagW ? Math.round(this.maxCanvasWidth) : this.canvasWidth;
            currHeight = flagH ? Math.round(this.maxCanvasHeight) : this.canvasHeight;
            if(this.canvasWidth / this.canvasHeight > currWidth / currHeight){
                currWidth = currHeight * this.canvasWidth / this.canvasHeight;
            }else{
                currHeight = currWidth * this.canvasHeight / this.canvasWidth;
            }
            this.canvasWidth =currWidth;
            this.canvasHeight = currHeight;
            var visBox = _this.vis.node().getBBox();
            this.svgContainer.attr('viewBox',function(){
                return _this.setViewBox((visBox.width - currWidth) /2 + visBox.x, (visBox.height - currHeight) / 2 + visBox.y, currWidth, currHeight);
            });
        }
    },
    addNode:function(node,x,y){ // 增加节点
        var index = this.findNodeIndex(node.id);
        node = $.extend({},this.config,node);
        if(node.status == 'DELETED' && index != -1){
            this.deleteNodes(node);
            return;
        }
        if(index==-1 && node.status !== 'DELETED'){
            node.fixed = true;
            node.x = (typeof node.x == 'undefined' ? x : node.x);
            node.y = (typeof node.y == 'undefined' ? y : node.y);
            this.nodes.push(node);
        }else{
            this.nodes[index] = $.extend({},this.nodes[index],node);
        }
    },
    addNodes:function(nodes){   // 增加多个节点
        var _this = this;
    	var length = nodes.length;
        var gap = 150;
        var minh = 6;
        var minw = 20;
        var hw = Math.round(Math.sqrt(nodes.length));
        if(hw<minh){
            var h = hw;
            var l = h;
        }else{
            var h = minh;
            var l = Math.round(length / h);
        }
        var cw = this.canvasWidth / 2;
        var ch = this.canvasHeight /2;
        
        var cx = cw - (l / 2) * gap, cy = ch - (h / 2) * gap;   // 为没有x,y的节点加x,y
        if(Object.prototype.toString.call(nodes) == '[object Array]') {
            nodes.forEach(function(d,i) {
                var x = (i % l) * gap + cx;
                var y = Math.floor(i / l) * gap + cy;  
                _this.addNode(d,x,y);
            });
        }
        _this = null;
        return this;
    },
    findNode:function(id){  // 查找节点
        var nodes = this.nodes;
        for( var i in nodes) {
            if(nodes[i]['id'] == id)
                return nodes[i];
        }
        return null;
    },
    findNodeIndex:function(id){ // 查找节点所在序号
        var nodes = this.nodes;
        for( var i in nodes) {
            if(nodes[i]['id'] == id)
                return i;
        }
        return -1;
    },
    deleteNodes:function(nodes){    // 删除节点及其相关连线
        var _this = this;
        var nodesArr = this.nodes;
        var linksArr = this.links;
        if(_.isPlainObject(nodes)){
            nodes = [nodes];
        }
        if(_.isArray(nodes)){
            $.each(nodes,function(i,d){
                _.remove(nodesArr, function(n) {
                    return d.id==n.id;
                  });
                _.remove(linksArr, function(n) {
                    return d.id==n.sourceNodeId || d.id==n.targetNodeId;
                  });
            })
        }
        return this;
    },
    changeLinkData:function(source,target,link){
        if(source && target){
            var s = this.findNode(source) || source;
            var t = this.findNode(target) || target;
            return $.extend({},link,{
                source : s,
                target : t,
                businessId : this.businessId,
                sourceIp:s.ip,
                sourceNeId:s.neId,
                targetIp:t.ip,
                targetNeId:t.neId,
                sourceNodeId : s.id,
                targetNodeId : t.id,
                show:s.show && t.show 
            });
        }
    },
    addLink:function(source,target,link){    // 增加连线
        var oldLink = _.find(this.links,['id',link.id]);
        var o = this.changeLinkData(source,target,link);
        if(oldLink){
            o = $.extend(true, _.cloneDeep(oldLink), o);
            _.remove(this.links, ['id', link.id]);
         }
        return o;
     },
    addLinks:function(links){
        this.enterLinks();
        if(Object.prototype.toString.call(links) == '[object Array]') {
            var _this = this, newLinks = [];
            links.forEach(function(link) {        
                var s = link['sourceNodeId'],t = link['targetNodeId'];
                if(s && t){
                    newLinks.push( _this.addLink(s, t,link)); 
                }
            });
            this.enterLinks();  // 销毁删掉数据的dom
            this.links = this.links.concat(this.links, newLinks);
            this.enterLinks();  // 对新的数据创建dom
            this.updateLink().lineTick();
        }
        this.appendLinkImg();
        _this = null;
        return this;
    },
    findLinkByLinkId:function(id){
        var index =  _.findIndex(this.links, function(o) {
            return id==o.id;
        });
        return index;
    },
    findLinkIndex:function(sourceNode,targetNode){
        var s0 = sourceNode.id, t0 = targetNode.id;
        var index =  _.findIndex(this.links, function(o) {
            var s = o.sourceNodeId,t = o.targetNodeId;
            return (s0==s&&t0==t) || (s0==t&&t0==s);
        });
        return index;
    },
    findLinkSum:function(sourceNode,targetNode){
        var sum = 0;
        var _links = this.links;
        var s0 = sourceNode.id, t0 = targetNode.id;
        $.each(_links,function(i,d){
            var s = d.sourceNodeId,t = d.targetNodeId;
            if((s0==s&&t0==t) || (s0==t&&t0==s)){
                sum++;
            }
        })
        return sum;
    },
    doNodeDrag:function(){
        var _this = this;
         _this.nodeDrag = this.force.drag().on('drag',null).on('drag',function(d){
             if(d3.event.dx==0 && d3.event.dy==0){
                 return;
             }
             typeof _this.dragFn == 'function' && _this.dragFn(d3.event,this);
         });
         _this.nodeDrag.on('dragstart',function(d){
             d3.event.sourceEvent.stopPropagation();
             _this.dragNodeId = [];
         });
         _this.nodeDrag.on('dragend',function(d){
 //       	 _this.appendLinkImg();
             d3.event.sourceEvent.stopPropagation();
             _this.dragNodeId = null;
             _this.showTip = true;
         });
    },
    addNodeDrag:function(){
        this.vis.selectAll('.node').on('mousedown.drag', null).call(this.nodeDrag);
        return this;
    },
    cancelNodeDrag:function(){
        this.vis.selectAll('.node').on('mousedown.drag', null);
        return this;
    },
    _addLink:function(callback){
        var _this = this;
        this.cancelNodeDrag();
        this.vis.selectAll('.node').on('mousedown',null).on('mousedown', function(d){
            var downXY = d3.mouse(_this.vis[0][0]);
            _this.mousedownNode = d;
            _this.dragline.lineData = [[downXY[0],downXY[1]]]
            _this.dragline.classed('hidden', false).attr('d',function(d){
                return _this.lineC(_this.dragline.lineData )
            });
            d3.select(document).on('mousemove.bp',function(){
                _this.cancelZoom();
                if(_this.mousedownNode == null){
                    return;
                }
                _this.dragline.attr('d',function(d){
                    var xy = d3.mouse(_this.svgContainer.node());
                    _this.dragline.lineData[1] = [xy[0],xy[1]];
                    return _this.lineC(_this.dragline.lineData )
                });
            }).on('mouseup.bp',function(){
                _this.createZoom();
                var source = _this.mousedownNode;
                var target = d3.event.target.__data__;
                
                _this.dragline.classed('hidden',true);
                _this.dragline.lineData = [];
                _this.mousedownNode = null;
                d3.select(document).on('mousemove.bp',null).on('mouseup.bp',null);
                
                if(_this.canAddLinks(source,target)){
                    typeof callback== 'function' && callback([{
                    	businessId:_this.businessId,
                        sourceNodeId:source.id,
                        targetNodeId:target.id,
                    }]);
                } 
            });
        })
    },
    canAddLinks:function(sor,tar){
        // sor（父）与tar（子）不能为同一节点;节点间不能重复添加;集群只能指向资源，根业务系统只能作为父节点
        if(!sor || !tar){   
            return false;
        }
        if(sor.nodeType && tar.nodeType){
            if(sor.id == tar.id){
                return false;
            }
            if(tar.id == topoBaseData){
            	tooltip("","根节点只能作为父节点","info");
            	return false;
            }
            if(sor.nodeType == "Cluster" && tar.nodeType != "NE"){
            	tooltip("","集群下只能包含资源节点","info");
            	return false;
            }
            return true;
        }
        return false;  
    },
    enterLinks:function(parent){
        var _this = this;
        var linktree = this.links;
        var link = this.nodeLine.selectAll('path.link').data(linktree,
        		function(d) {
		        	if(d!=undefined){
		        		 return d.id;
		        	}
                });
        link.attr('status',function(d){
            var oldStatus = d3.select(this).attr('status');
            if(oldStatus){
                d3.select(this).classed(d.linkStatus,false);
            }
            d3.select(this).classed(d.linkStatus,true);
            return d.linkStatus;
        });
       link.enter().insert('svg:path', 'g.node').attr('class', 'link').attr('marker-end','url(#markerArrow)').attr('id',function(d) {
                    return d.id;
                }).on('mouseenter',function(d){
                	 if(_this.showTip && d.networkLinkId){
                        _this.tip.html(_this.linkTip(d)).classed('hide',false);
                        _this.tipRange(d3.event.pageX,d3.event.pageY);
                    }
                    _this.showlinePoint(d,true);
                }).on('mouseleave',function(d){
                    _this.hideLinePoint();
                    _this.tip.classed('hide',true);
                });
        link.exit().remove();
        this.enterEnableLink();
        this.enterLinePoint();
        return this;
    },
    showlinePoint:function(o,isShow){
        this.nodeLine.selectAll('.lineWrap').filter(function(d){
            return d.id == o.id
        });
    },
    hideLinePoint:function(){   // 选中的连线不隐藏
        var _this = this;
        this.nodeLine.selectAll('.lineWrap').style('display','none');
        this.nodeLine.selectAll('.link.selected').each(function(d){
            _this.showlinePoint(d,true)
        })
        _this = null;
    },
    enterEnableLink:function(){
        var _this = this;
        this.subLinks = [];
        this.netLinks = this.links.filter(function(d,i){    //子网的networkLinkId == ''
            if(d.networkLinkId === ''){
                d.linkStatus = "Enable";
                _this.subLinks.push(d);
            }
            if((d.linkStatus == 'Enable' || d.linkStatus == 'Alert') && d.show){
                return d.networkLinkId;
            }
        });
        var use = this.nodeLine.selectAll('use').data(this.netLinks.concat(this.subLinks),
                function(d) {
                        return d.id;
                })
            
        use.enter().insert('svg:use', 'g.node').attr({
                'width':2,
                'height':2,
                'x':0,
                'y':-10,
                'href':'#liquid',
            }).attr('id',function(d){
                return d.id
            }).append("animateMotion").attr({
                dur : "3s",
                rotate : "auto",
                repeatCount : "indefinite"
            });
        use.exit().remove();
        this.enterEnableText().updateEnableText();
        _this = null;
        return this;
    },   
    enterEnableText:function(){
        var _this = this;  
        var text = this.nodeLine.selectAll('text').data(this.links,
                function(d) {
                      if (d!=undefined){ 
                    	  return d.id;
                      }
                });
        text.enter().insert('svg:text', 'g.node').classed('pathText',true).attr('dy',function(d){
           return  (d.linkIndex == -1) ? '-6' : '1em';
        }).append('textPath').attr('xlink:href',function(d){
            return '#'+d.id;
        });
        text.exit().remove();
        _this = text = null;
        return this;
    },
    updateEnableText:function(selection){
        var _this = this;
        var labelText='';
        selection = selection || this.nodeLine.selectAll('textPath');
        selection.text(function(d){
        	if(d!=undefined){
        		var label = d.nodeRelation || _this.config.nodeRelation;
            	if(_this.config.default==false && _this.config.label=="NodeRelation"){
            		switch (label) {
    			    case "Cluster": labelText ="集群关系"; break
    			    case "RunningOn": labelText ="运行在关系"; break
    			    case "Call": labelText ="调用访问关系"; break
    			    case "Contain": labelText ="包含关系"; break 
    			    default: return ''
            		}
            	}
                return labelText
        	}
        }).attr('startOffset', '55%');
        _this = null;
        return this;
    },
    enterNodes:function(){
        var _this = this;
        var nodes = this.nodes;
        var node = this.vis.selectAll('.node').data(nodes, function(d) {
                    return d.id;
                });
        this.updateNodeData();
        var svg = node.enter().append('svg:svg').classed('node',true).call(this.nodeDrag);
        svg.append('svg:rect').classed('nodeRect',true);
        svg.append('svg:image').classed('nodeImg',true).on('mouseenter',function(d){
            if(!_this.showTip ||(d.nodeType !== 'NE' && d.nodeType !== 'Business' && d.nodeType !== 'SubnetTopo' && d.nodeType !== 'Cluster')){   // 子网
                return;
            }
            _this.tip.text(_this.nodeTip(d)).classed('hide',false);
            _this.tipRange(d3.event.pageX,d3.event.pageY);
        }).on('mouseleave',function(d){
            _this.tip.classed('hide',true);
        });
        svg.append('text').classed('nodetext',true);
        node.exit().remove();
        return this;
    },
    updateNodeData:function(selection){
        selection = selection || this.vis.selectAll('.node');
        selection.each(function(d){    // 刷新其子元素绑定的数据，暂未有更好的方法
            d3.select(this).selectAll('.nodetext,.nodeRect,.nodeImg').each(function(){
                this.__data__ = d;
            });
        })
        // seletion = null;
        selection = null;
        return this;
    },
    nodeTip:function(d){// TODO 添加异步请求数据显示
    	var str = '';
        if(d.nodeType == 'NE'){
        	var label = [{name:'名称',key:'name'},{name:'IP地址',key:'ip'},{name:'资源状态',key:'runStatusText'},/*{name:'设备类型',key:'neClass'},{name:'厂商',key:'vendor'},
                         {name:'设备状态',key:'runStatusText'},{name:'CPU利用率',key:'cpuAvg',unit:'%'},{name:'内存利用率',key:'memoryAvg',unit:'%'},*/{name:'告警等级',key:'alertLevel'}];
            var indicatorNames = [];
            if(d.baseNeClass == 'host'){
                indicatorNames = ['cpu_usage_avg', 'physical_memory'];
            }else if(d.baseNeClass == 'network'){
                indicatorNames = ['network_cpu' ,'network_memory'];
            }
            $.ajax({
                url: gbs.host + "/monitor/ne/view/"+d.neId,
                dataType:"json",
                data:{indicatorNames: indicatorNames.length>0?indicatorNames.join(","):""},
                type:"post",
                async: false,
                success: function (res) {
                    if(res.success == true){
                        var data = res.obj;
                        d.alertLevel = data.alertLevel||d.alertLevel;
                        if(data.indicators){
                            $.each(data.indicators, function(idx, idc){
                                if(!idc.indicatorValue){
                                    return;
                                }
                                if(idc.indicatorName == 'physical_memory'){
                                    if(Array.isArray(idc.indicatorValue)){
                                        idc.indicatorValue.length>0&&(d.memoryAvg = idc.indicatorValue[0].memory_usage);
                                    }else{
                                        d.memoryAvg =  idc.indicatorValue.memory_usage
                                    }
                                }else if(idc.indicatorName == 'cpu_usage_avg'){
                                    if(Array.isArray(idc.indicatorValue)){
                                        idc.indicatorValue.length>0&&(d.cpuAvg =  idc.indicatorValue[0].result);
                                    }else{
                                        d.cpuAvg =  idc.indicatorValue.result
                                    }
                                }else if(idc.indicatorName == 'network_memory'){
                                    if(Array.isArray(idc.indicatorValue)){
                                        idc.indicatorValue.length>0&&(d.memoryAvg =  idc.indicatorValue[0].memory_usage);
                                    }else{
                                        d.memoryAvg =  idc.indicatorValue.memory_usage
                                    }
                                }else if(idc.indicatorName == 'network_cpu'){
                                    if(Array.isArray(idc.indicatorValue)){
                                        idc.indicatorValue.length>0&&(d.cpuAvg =  idc.indicatorValue[0].used_cpu_usage);
                                    }else{
                                        d.cpuAvg =  idc.indicatorValue.used_cpu_usage
                                    }
                                }
                            });
                        }
                    }
                }
            });
            $.each(label,function(i,o){
                var unit = [o.unit] || '';
                var v = addUnit(d[o.key],unit);
                if(o.key == 'alertLevel'){
                    v = levelMapName[parseInt(v)];
                }
                v = ((v || v===0) ? v : '--');
                if(d.runStatus == 'Unknow' && (o.key == 'cpuAvg' || o.key == 'memoryAvg' || o.key == 'alertLevel')){    // 资源状态为未知，不展示cpu、内存、告警
                    v = '--';
                }
                str += (o.name + '：' + v +'\n');
            })
        }else if(d.nodeType == 'Business'){
        	var label = [{name:'健康度',key:'health'},{name:'可用率',key:'availableRate',unit:'%'},{name:'不可用次数',key:'downTimes'},/*{name:'不可用时长',key:'unavailableTime'},*/
                       {name:'繁忙度',key:'busy_rate',unit:'%'},{name:'MTTR',key:'MTTR'},{name:'MTBF',key:'MTBF'},{name:'告警等级',key:'alertLevel'},{name:'责任人',key:'liableUser'}];
            busTopoApi.bnsTipInfo(d.neId,function (datas) {
        		$.each(label,function(i,o){
        			var v = datas[o.key];
                    if(o.key == 'alertLevel'){
                        v = v && v.maxLevel && v.maxLevel != 0 ? levelMapName[v.maxLevel] : '--';
                    }else if(o.key == 'liableUser'){
                        v = v ? (v.userName+"（"+v.employeeCode+"）") : '--';
                    }else{
                        v = v && v != '' ? v : '--';
                    }
                    str += (o.name + '：' + v +'\n');
                })
            },false);
        }else if(d.nodeType == 'Cluster'){
            var runStatus;
            if(d.runStatus=="Good"){
                runStatus="正常";
            }else if(d.runStatus=="Warning"){
                runStatus="告警";
            }else{
                runStatus="不可用";
            }
            str = "集群名称" + '：' + d.customName +'\n'+ "集群状态" + '：' + runStatus;
        }
        return str;
    },
    tipRange:function(x,y){    // tip范围确定
        var tip = this.tip.node().getBoundingClientRect();
        var maxX = window.innerWidth, maxY = window.innerHeight;
        
        x = (x + tip.width) > maxX ? (maxX - tip.width - 100) : x;
        y = (y + tip.height) > maxY ? (maxY - tip.height) : y;
        this.tip.style({
            left:(x-130)+'px',
            top:(y-65)+'px'
        });
    },
    nodeTick:function(){  // 拖动时设置
        var _this = this;
        this.resetCanvasPoint();
        this.vis.selectAll('.node').each(function(d){
            _this.maxCanvasWidth = Math.max(d.x + d.outerWidth+20, _this.maxCanvasWidth);
            _this.minCanvasWidth = Math.min(d.x - 20, _this.minCanvasWidth);
            _this.maxCanvasHeight = Math.max(d.y + d.outerHeight+50, _this.maxCanvasHeight);
            _this.minCanvasHeight = Math.min(d.y - 50, _this.minCanvasHeight);
            d.px = d.x;
            d.py = d.y;
            d3.select(this).attr({
                x:d.x-d.imgDx,
                y:d.y
            })
        })
        this.lineTick();
        _this = null;
    },
    inRange:function(xy,flag,min,max){  // 拖动节点范围
        return xy;
    },
    lineTick:function(){
        var _this = this;
        _this.nodeLine.selectAll('.link').each(function(d){
        	if(d!=undefined){
        		 var s = _.filter(_this.nodes,{id:d.sourceNodeId})[0];
                 var t = _.filter(_this.nodes,{id:d.targetNodeId})[0];
                 if(s && t && ((!_this.dragNodeId && !_this.dragLinkId) || _this.dragNodeId.indexOf(s.id) !== -1 || _this.dragNodeId.indexOf(t.id) !== -1 || _this.dragLinkId == d.id)){
                	  d = $.extend(d, _this.changeLinkData(s, t, d));
                      this.__data__ = d;
                	 var path =  _this.polygonalLine(s,t,d);
                     _this.enterLinePoint();
                     d3.select(this).attr('d',path);
                 }
        	}
        });
       
        _this.nodeLine.selectAll('use').each(function(d){
            if((!_this.dragNodeId && !_this.dragLinkId) || _this.dragNodeId.indexOf(d.sourceNodeId) !== -1 || _this.dragNodeId.indexOf(d.targetNodeId) !== -1 || _this.dragLinkId == d.id){
                var id = d3.select(this).attr('id');
                var path = _this.nodeLine.selectAll('.link[id="'+id+'"]').attr('d');
                d3.select(this).select('animateMotion').attr('path',path); 
            }
        });
        
        _this.nodeLine.selectAll('.lineWrap').each(function(d){
        	if(d!=undefined && _this.dragNodeId!=undefined){
        		if((!_this.dragNodeId && !_this.dragLinkId) || _this.dragNodeId.indexOf(d.sourceNodeId) !== -1 || _this.dragNodeId.indexOf(d.targetNodeId) !== -1 || _this.dragLinkId == d.id){
                    d3.select(this).attr('class','lineWrap ' + d.linkClass.lineType);
                    _this.updatePoint(this); 
                }
        	}
        });
        _this.nodeLine.selectAll('textPath').each(function(d){
        	
            $("#businessMainTopo .setLink[name='"+d.id+"']").attr('x',(d.source.x+d.target.x)/2+16); 
            $("#businessMainTopo .setLink[name='"+d.id+"']").attr('y',(d.source.y+d.target.y)/2+10);
            if(d!=undefined && d.linkClass && d.linkClass.lineType && d.linkClass.point){
            	var pointArr = $("#businessMainTopo #"+d.id).attr("d").split("L");
                if(d.linkClass.lineType == 'ployN'){
                	d3.select(this).attr('href','#'+d.id);
                	 if(d.source.y>d.target.y&&d.source.x>d.target.x){
                 		d3.select(this.parentNode).attr('dy','0');
                 	}else if(d.source.y>d.target.y&&d.source.x<d.target.x){
                 		d3.select(this.parentNode).attr('dy','1em');
                 	}else if(d.source.y<d.target.y&&d.source.x<d.target.x){
                 		d3.select(this.parentNode).attr('dy','0');
                 	}else{
                 		d3.select(this.parentNode).attr('dy','1em');
                 	}
                	 
                	 if((d.source.x-d.target.x)<0){
                		 if((d.target.x-d.source.x)<=60){
                    		 d3.select(this.parentNode).style('text-anchor','start').attr('dx','0');
                    	}else{
                    		 d3.select(this.parentNode).style('text-anchor','middle');
                    	}
                	}
                	 if((d.source.x-d.target.x)>0){
                		 if((d.source.x-d.target.x)<=10){
                    		 d3.select(this.parentNode).style('text-anchor','start').attr('dx','0');
                    	}else if((d.source.x-d.target.x)<=70){
                    		d3.select(this.parentNode).style('text-anchor','start').attr('dx','25');
                    	}else if(Math.abs(d.source.x-d.target.x)<=130){
                    		d3.select(this.parentNode).style('text-anchor','start').attr('dx','55');
                    	}else{
                    		 d3.select(this.parentNode).style('text-anchor','middle').attr('dx','0');
                      	}
                	}
                    $("#businessMainTopo .setLink[name='"+d.id+"']").attr('x',pointArr[2].split(",")[0]-15); 
                    $("#businessMainTopo .setLink[name='"+d.id+"']").attr('y',pointArr[2].split(",")[1]-15); 
//                    return;
                }else if(d.linkClass.lineType == 'ployZ'){
                	d3.select(this).attr('href','#'+d.id);
                   if(d.source.y>d.target.y&&d.source.x>d.target.x){
               		d3.select(this.parentNode).attr('dy','1em');
               	}else if(d.source.y>d.target.y&&d.source.x<d.target.x){
               		d3.select(this.parentNode).attr('dy','0');
               	}else if(d.source.y<d.target.y&&d.source.x<d.target.x){
               		d3.select(this.parentNode).attr('dy','1em');
               	}else{
               		d3.select(this.parentNode).attr('dy','0');
               	}
                   if((d.source.x-d.target.x)<0){
                   	    if(Math.abs(d.source.y-d.target.y)<=40){
                  		   d3.select(this.parentNode).style('text-anchor','start').attr('dx','0');
                    	}else if(Math.abs(d.source.y-d.target.y)<=103){
                    	   d3.select(this.parentNode).style('text-anchor','middle').attr('dx','20');
                    	}
                   }
                   if((d.source.x-d.target.x)>0){
                     	if(Math.abs(d.source.y-d.target.y)<=40){
                  		   d3.select(this.parentNode).style('text-anchor','start').attr('dx','0');
                    	}else if(Math.abs(d.source.y-d.target.y)>40&&Math.abs(d.source.y-d.target.y)<=90){
                    		d3.select(this.parentNode).style('text-anchor','start').attr('dx','22');
                    	}else if(Math.abs(d.source.y-d.target.y)>90&&Math.abs(d.source.y-d.target.y)<=120){
                    		d3.select(this.parentNode).style('text-anchor','middle').attr('dx','-20');
                    	}else if(Math.abs(d.source.y-d.target.y)>120&&Math.abs(d.source.y-d.target.y)<=140){
                    		d3.select(this.parentNode).style('text-anchor','middle').attr('dx','-35');
                    	}else{
                    		d3.select(this.parentNode).style('text-anchor','middle').attr('dx','0');
                    	}
                   }
                	/*if(d.source.y>d.target.y){
                        d3.select(this).attr('href','#'+d.id); 
                    }else{
                        var link = _this.nodeLine.select('.link[id="'+d.id+'"]');
                        var point = link.data()[0].linkClass.point;
                        var d_link = _this.nodeLine.select('[id="d_link_'+d.id+'"]');
                        if(d_link.size()==0){
                            _this.nodeLine.append('path').attr('id','d_link_'+d.id).attr('d',function(){
                                return _this.lineC(point.reverse());
                            }).style('fill','none');
                        }else{
                            d_link.attr('d',function(){
                                return _this.lineC(point.reverse());
                            });
                        }
                        d3.select(this).attr('href','#d_link_'+d.id); 
                    }*/
                    $("#businessMainTopo .setLink[name='"+d.id+"']").attr('x',pointArr[2].split(",")[0]-15); 
                    $("#businessMainTopo .setLink[name='"+d.id+"']").attr('y',pointArr[2].split(",")[1]-15); 
         //           return;
                }
            } 
            if(d!=undefined){
                if(!_this.dragNodeId || (_this.dragNodeId.indexOf(d.sourceNodeId) !== -1 || _this.dragNodeId.indexOf(d.targetNodeId) !== -1)){
                     d3.select(this.parentNode).attr('transform','');           		
                     if((d.target.x-d.source.x)<0){
                         var b_box=d3.select(this.parentNode).node().getBBox();
                         var x=b_box.x+b_box.width/2;
                         var y=b_box.y+b_box.height/2;             
                         d3.select(this.parentNode).attr('transform','rotate(180, '+x+','+y+')');
                    }
                }
             }
           
            return;
        });
        _this.updateEnableText();
        _this = null;
    },
    enterLinePoint:function(){
        var polyLine = this.links.filter(function(d){
            if(d.linkClass && (d.linkClass.lineType == 'ployZ' || d.linkClass.lineType == 'ployN')&& d.linkClass.point){
            	if (d!=undefined) return d.id;
            }
        });
      
        var lineW = this.nodeLine.selectAll('.lineWrap').data(polyLine,function(d){
        	if (d!=undefined) return d.id;
        });
        var enterline = lineW.enter().append('g').each(function(d){
            d3.select(this).attr('class','lineWrap ' + d.linkClass.lineType);
        });
        var pt = lineW.selectAll('.linePoint').data(function(d){
            var p = d.linkClass.point;
            return [p[2]];
        });
        pt.enter().append('rect').attr('class',function(d,i){
            d3.select(this).attr({
                width:16,
                height:16,
                x:d[0]-8,
                y:d[1]-8
            })
            return 'linePoint lineP'+i;
        }).call(this.dragPoint);
        pt.exit().remove();
        lineW.exit().remove();
    },
    updatePoint:function(selection){
        var points = selection.__data__.linkClass.point;
        d3.select(selection).selectAll('.linePoint').each(function(d,i){
            d3.select(this).attr({
                x:points[i+2][0]-8,
                y:points[i+2][1]-8
            })
        })
    },
  /*
     * straightLine:function(s,t,d){ //直线 var x1 = s.x + s.width/2; var y1 = s.y
     * +s.height/2; var x2 = t.x +t.width/2; var y2 = t.y +t.height/2; return
     * this.lineC([[x1,y1],[x2,y2]]); },
     */
    polygonalLine:function(s,t,d){    // 折线
        var sx = s.x + s.width/2;
        var sy = s.y + s.height/2;
        var tx = t.x + t.width/2;
        var ty = t.y + t.height/2;
        var linkClass = d.linkClass;
        var linkSum = this.findLinkSum(s,t);
        var temp = (tx-sx) * (ty-sy)/Math.abs((tx-sx) * (ty-sy)) || 1;
        if(linkClass && linkClass.lineType == 'ployZ'){
            var cx = sx + (tx - sx)/2;
            var cy = sy + (ty - sy)/2;
            sy = sy - 5 * d.linkIndex * temp;
            ty = ty - 5 * d.linkIndex * temp;
            if(linkClass.point && (this.dragLinkId || !this.dragNodeId)){
                cx = linkClass.point[2][0];
            }else{
                cx = cx + 5 * d.linkIndex;
                if(Math.abs(s.x - t.x) < (s.width + t.width)){
                    if(s.x < t.x){
                        cx = tx - s.width - t.width + 5 * d.linkIndex; 
                    }else{
                        cx = sx + s.width + t.width + 5 * d.linkIndex; 
                    }
                    sy = sy + 5 * d.linkIndex * 2 * temp;
                }
            }
            sx=sx+(cx-s.x)/Math.abs(cx-s.x)*s.width*0.5;
            tx=tx+(cx-t.x)/Math.abs(cx-t.x)*s.width*0.5;
            var topx = cx, topy = sy, btmx = cx, btmy = ty;
            d.linkClass.point = [[sx,sy],[topx,topy],[cx,cy],[btmx,btmy],[tx,ty]];
            return this.lineC(d.linkClass.point )
        }else if(linkClass && linkClass.lineType == 'ployN'){
            var cx = sx + (tx - sx)/2;
            var cy = sy + (ty - sy)/2;
            // sx = sx - 5 * d.linkIndex * temp;
            // tx = tx - 5 * d.linkIndex * temp;
            if(linkClass.point && (this.dragLinkId || !this.dragNodeId)){
                cy = linkClass.point[2][1];
            }else{
                cy = cy + 5 * d.linkIndex;
                if(Math.abs(s.y - t.y) < (s.height + t.height)){
                    if(s.y < t.y){
                        cy = ty - s.height - t.height + 5 * d.linkIndex; 
                    }else{
                        cy = sy + s.height + t.height + 5 * d.linkIndex; 
                    }
                    sx = sx + 5 * d.linkIndex * 2 * temp;
                }
            }
            sy=sy+(cy-s.y)/Math.abs(s.y-cy)*s.width*0.5;
            ty=ty+(cy-t.y)/Math.abs(t.y-cy)*s.width*0.5;
            
            var  topx = sx, topy = cy, btmx = tx, btmy = cy;
            d.linkClass.point = [[sx,sy],[topx,topy],[cx,cy],[btmx,btmy],[tx,ty]];
            return this.lineC(d.linkClass.point )
        }else{
            var l = Math.abs(Math.sqrt((s.x-t.x)*(s.x-t.x)+(s.y-t.y)*(s.y-t.y))) || 1;
            sx = sx-((s.x-t.x)/l*s.width*0.5);
            sy = sy-((s.y-t.y)/l*s.height*0.5);
            tx = tx+((s.x-t.x)/l*t.width*0.5);
            ty = ty+((s.y-t.y)/l*t.height*0.5);
            if(linkSum > 1){
                sx=sx+((s.y-t.y)/l*5*d.linkIndex);
                sy=sy-((s.x-t.x)/l*5*d.linkIndex);
                tx=tx+((s.y-t.y)/l*5*d.linkIndex);
                ty=ty-((s.x-t.x)/l*5*d.linkIndex);
            }
            return this.lineC([[sx,sy],[tx,ty]]);
        }
    },
    dragPoint:function(){ // 拖拽折线的中间调节点
        var _this = this;
        this.dragPoint = d3.behavior.drag().on('drag',function(){
            _this.showTip = false; 
            var e = d3.event, x = e.x, y = e.y, dx = e.dx, dy = e.dy;
            var target = d3.select(this);
            var shap = d3.select($(this).closest('g').get(0));
            var d = shap.node().__data__.linkClass;
            var pit = d.point;
            var index = _this.findLinkByLinkId(shap.data()[0].id)
            var line = _this.links[index].linkClass.point;
            if( d.lineType == 'ployZ' ){ // 中间拖动点z
                pit[1][0] += dx;
                pit[2][0] = pit[1][0];
                pit[3][0] = pit[1][0];
            }else if(d.lineType == 'ployN'){
                pit[1][1] += dy;
                pit[2][1] = pit[1][1];
                pit[3][1] = pit[1][1];
            }
            _this.lineTick();
        }).on('dragstart',function(){
            d3.event.sourceEvent.stopPropagation();
        }).on('dragend',function(){
            d3.event.sourceEvent.stopPropagation();
        });
        this.dragPoint.on('dragstart',function(d){
            _this.dragLinkId = $(this).closest('g').get(0).__data__.id;
            _this.dragNodeId = [_this.dragLinkId];
        });
        this.dragPoint.on('dragend',function(){
            _this.showTip = true; 
            _this.dragLinkId = null;
            _this.dragNodeId = null;
        });
    },
    updateNode:function(){    // 隐藏网元
        var nodes = this.vis.selectAll('.node');
        return this;
    },
    moveNode:function(neId,x,y){
        if(!neId||isNaN(x)||isNaN(y)){
            return;
        }
        var nodes = this.nodes;
        for( var i in nodes) {
            var node = nodes[i];
            if(nodes[i]['neId'] == neId) {
                node.x = x;
                node.y = y;
                this.nodes.splice(i, 1, node);
                return;
            }
        }
    },
    updateNodeRect:function(isSelected){
        var nodes = isSelected ? this.vis.selectAll('.node.selected') : this.vis.selectAll('.node');
        nodes.each(function(d) {
            var node = d3.select(this);
            var $img = node.select('image').attr('x',0);
            var $text = node.select('text').attr('x',0);
            var rect = node.select('.nodeRect').attr('width',0).attr('height',0);
            var box = this.getBBox();
            d.imgDx = 0;
            rect.attr({
                'width':box.width,
                'height':box.height
            });
            d.outerWidth = box.width;
            d.outerHeight = box.height;
            
            // 调整使文字图片居中对齐
           /* if(box.width > $img.attr('width')){ // 文字长
                d.imgDx = box.width/2-$img.attr('width')/2;
                $img.attr('x',d.imgDx);
            }else{  // 图大
                var textW = $text.node().getBBox().width;
                $text.attr('x',box.width/2-textW/2);
            }*/
            var imgw = $img.attr('width') ;
            var textw = $text.node().getBBox().width;
            if(imgw >textw){
                $text.attr('x', box.width / 2 - textw / 2);
            }else{
                d.imgDx = (textw - imgw )/2;
                $img.attr('x', d.imgDx);
            }
            node = rect = $img = $text = box = null;
        });
        nodes = null;
        return this;
    },
    updateNodeImg:function(selection){
        var _this = this;
        var nodes = selection || this.vis.selectAll('.node');
        nodes.each(function(d) {
            d3.select(this).select('.nodeImg').attr({
                'width':d.width,
                'height':d.height,
                'id':d.id,
                'href':_this.setNodeImg(d)
            })
        });
        _this = nodes = null;
        return this;
    },
    setNodeImg:function(d){
        // 将monitor的RunStatus与business的统一
    	var tmpStatus = d.runStatus;
        if (d.runStatus === "Unconnection") {
        	tmpStatus = "Unavailable";
        }else if (d.runStatus === "Loading") {
        	tmpStatus = "Unknow";
        }
        if(d.iconId){
            return gbs.host + '/business/topology/getIcon/'+d.iconId+'/'+(tmpStatus || 'Good');
       }
    },
    updateNodeText:function(selection){
        var _this = this;
        var texts = selection || this.vis.selectAll('.node .nodetext');
        texts.text(function(d){
            d3.select(this).attr({
                'fill':d.textColor,
                'font-size':d.fontSize,
                'dy':parseFloat(d.fontSize)+parseFloat(d.height)
            })
            return _this.getNodeTextContent(d);
        })
        _this = texts = null;
        return this;
    },
    getNodeTextContent:function(d){
    	if(d.nodeType == 'NE') { // 资源
            var text = {
                IPAddr : d.ip,
                NodeName : d.customName,
                NodeType : d.neClass,
                Omit : ''
            }
            return text[this.config.textType];
        } else if((d.nodeType == 'Business'|| d.nodeType == 'Cluster') && this.config.textType != "Omit") { // 子网
        	 return d.customName
        }
    },
    updateLink:function(isSelected){
        var _this = this;
        var links = isSelected ? this.vis.selectAll('.link.selected') : this.vis.selectAll('.link');
        links.classed('',function(d){
            if(d!=undefined){
                var obj = {
                        'stroke-width':d.lineWidth || _this.config.lineWidth,
                        'stroke-dasharray':(d.linkClass && d.linkClass['stroke-dasharray']) ||'0',
                    }
                d.linkClass = $.extend(d.linkClass, obj);
                d3.select(this).attr(d.linkClass).attr('class','link '+d.linkStatus);
               return !d.show;
        	}
        });
        _this = links = null;
        return this;
    },
    updateTp:function(){
        this.enterLinks(this.nodeLine);
        this.enterNodes(this.vis);
        this.updateNode().updateNodeImg().updateNodeText().updateNodeRect();
        this.updateLink();
        this.nodeTick();
    },
    update:function(){
        this.enterLinks(this.nodeLine);
        this.enterNodes(this.vis);
        this.updateNode().updateNodeImg().updateNodeText().updateNodeRect();
        this.updateLink();
        this.nodeTick();
    },
    doTick:function(){
        var _this = this;
        this.force.on('tick',null).on('tick',function(){
           _this.nodeTick();
           if(_this.force.alpha()<=0.05){
               _this.setForce(true);
               _this.force.on('tick',null);
               _this.nodeTick();
               _this.setMaxCavWH();
           }
        })
    },
    setForce:function(flag){
        flag ? this.force.stop():this.force.start();
        this.force.nodes().forEach(function(d, i) {
            d.fixed = flag;
        });
    },
   showAll:function(){
       var nodeIds = [];
       this.nodes.forEach(function(d,i){
           d.show == false && nodeIds.push(d.id);
       });
       return nodeIds;
   },
   cancelSelected:function(){
       this.vis.selectAll('.selected').classed('selected',false);
   },
   layoutStar:function(){
       this.setForce(false);
       this.doTick();
   },
   lineTypeToStrai:function(){  // 将折线修改为直线
       $.each(this.links,function(i,d){
           d.linkClass.lineType = 'straight';
           delete d.linkClass.point;
       })
       return this;
   },
   hideTip:function(){  //隐藏提示
       this.tip.classed('hide',true);
   },
   delLink:function(type){  
	   var linkDate = [];
	   linkDate.push(JSON.parse($(type).attr("linkDate")));
	   var data = {
               linkStr:linkDate,
               params:{"status":"DELETED"}
       };
	   viewTools.saveLinks(data,true);
   },
   setLink:function(type){ 
	   var linkDate = $(type).attr("linkDate");
	   $.comps.nodeRelation.open(JSON.parse(linkDate));
	  
   },
   countPngXY:function(d){ //设置和删除图标的位置
	   var pointArr =[];
	   var px = 0, py=0;
	   if(d.linkClass && d.linkClass.lineType && (d.linkClass.lineType=="ployN"||d.linkClass.lineType=="ployZ")){
		   pointArr = $("#"+d.id).attr("d").split("L");
		   px=pointArr[2].split(",")[0]-30;
		   py=pointArr[2].split(",")[1]-15;
	   }else{
		   px=(d.source.x+d.target.x)/2-25;
		   py=(d.source.y+d.target.y)/2+10;
	   } 
	  return {
		  x:px,
		  y:py
	  }
   },
   appendLinkImg:function(){    //svg拼接连线的设置和删除图标
	   var _this = this;
	   $("#businessMainTopo svg.setLink").remove();
	   _this.vis.selectAll('.link').each(function(d){
		   if(d!=undefined){
			   var xy = _this.countPngXY(d);
			   var svgSet =$("<svg>").attr({"name":d.id,"class":"setLink cursorPoniter","x":xy.x,"y":xy.y})
					.append($("<image>").attr({"class":"nodeImg","linkDate":JSON.stringify(d),"link-click":"delLink","x":"24","y":"0","width":"24","height":"24","href":"../resources/img/business/linkDel.svg"}))
					.append($("<image>").attr({"class":"nodeImg","linkDate":JSON.stringify(d),"link-click":"setLink","x":"0","y":"0","width":"24","height":"24","href":"../resources/img/business/setLink.svg"}));
			   $("#businessMainTopo #linkPng").append(svgSet);
		   }
		});
	   $("#businessMainTopo svg.setLink").css("display","none");
	   //解决svg拼接元素后，页面不渲染问题
	   $("#businessMainTopo #linkPng").html($("#businessMainTopo #linkPng").html());
   },
   bind:function(){
	   var _this = this;
       $("#businessMainTopo").off('click','[link-click]').on('click','[link-click]',function(){
       		var type = $(this).attr('link-click');
            typeof _this[type]=='function' && _this[type](this);
       });
       $("#businessMainTopo").off("mouseover").on("mouseover","path.link,.setLink",function(e){
           if(d3.select(this).classed('link')){
               $(".setLink[name='"+$(this).attr("id")+"']").css("display","block");
           }
           if(d3.select(this).classed('setLink')){
               d3.select(this).style("display","block").classed('setLinkHover',true);
           }

       });
       // 路径背景和删除路径按钮隐藏
       $("#businessMainTopo").off("mouseout").on("mouseout","path.link,.setLink",function(){
           if(d3.select(this).classed('link') && !$("svg.setLink.setLinkHover").length){
               d3.selectAll("svg.setLink").style("display","none").classed('setLinkHover',false);
           }
           if(d3.select(this).classed('setLink')){
               d3.selectAll("svg.setLink").style("display","none").classed('setLinkHover',false);
           }
       });
   }
}

var busTopoApi = {
  bnsTipInfo:function(businessId,callback,isAsync){
    $.ajax({
        url : gbs.host + '/business/topology/bnsTipInfo/'+businessId,
        type : 'GET',
        async : isAsync && true,
        success : function(data) {
            if(data.success) {
                (typeof callback== 'function') && callback(data.obj || {});
            }
        }
    });
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
          break;
      }
      time[i] = t % gap[i - 1] + arr[i]
      t = Math.floor(t / gap[i - 1])
  }

  return time.join('')
}

export default businessTopology