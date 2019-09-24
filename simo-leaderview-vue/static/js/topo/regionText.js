function Region(opt){
    this.regionWrap = opt.selector;
    this.dragFn = opt.dragFn;
    this.rgRect = this.regionWrap.append('rect').attr({
        'class':'regionDragRect'
    }).classed('hide',true);
    this.rgs = [];
    this.resetRgXY();
    this.doDrag();
}

Region.prototype = {
        constructor:Region,
        resetRgXY:function(){
            this.minRgX = 0;
            this.minRgY = 0;
            this.maxRgX = 0;
            this.maxRgY = 0;
        },
        findRgIndex:function(id){
            var rgs = this.rgs;
            for( var i in rgs) {
                if(rgs[i]['id'] == id)
                    return i;
            }
            return -1;
        },
        addRg:function(rg){ //增加节点
            var index = this.findRgIndex(rg.id);
            if(rg.status == 'DELETED' && index != -1){
                this.rgs.splice(index,1);
                return;
            }
            if(index==-1 && rg.status !== 'DELETED'){
                this.rgs.push(rg);
            }else{
                this.rgs[index] = rg;
            }
        },
        addRgs:function(rgs){   
            if(Object.prototype.toString.call(rgs) == '[object Array]') {
                var _this = this;
                rgs.forEach(function(d,i) {
                    _this.addRg(d);
                });
            }
            return this;
        },
        removeRgs:function(rgs){
            var rgsArr = this.rgs;
            if(_.isPlainObject(rgs)){
                rgs = [rgs];
            }
            if(_.isArray){
                $.each(rgs,function(i,d){
                    _.remove(rgsArr, function(n) {
                        return d.id==n.id;
                      });
                })
            }
            return this;
        },
        updateRgData:function(){    //绑定的元素部分属性在其子元素上并未更新，暂时用这种方式
            this.regionWrap.selectAll('.reText,.rectG,.circleG').each(function(d){
                this.__data__ = d;
                d3.select(this).selectAll('*').each(function(o){
                    this.__data__ = d;
                })
            })
            return this;
        },
        init:function(data){
            this.rgs = [];
            this._addRgs(data);
        },
        _addRgs:function(data){
            this.addRgs(data).enterRegions();
            this.updateRgData().updateRegions();
        },
        enterText:function(){
            this.txts = this.rgs.filter(function(d){
                return d.contextJson;
            })
            var text = this.regionWrap.selectAll('.rgText').data(this.txts,function(d){
                return d.id
            });
            text.enter().append('text').classed('rgText',true).call(this.dragRg);
            text.exit().remove();
            return this;
        },
        enterRects:function(){
            var _this = this;
            this.rects = this.rgs.filter(function(d){
                if(d.regionJson){
                    return d.regionJson.regionType == 'rect';  
                }
            })
            var rects = this.regionWrap.selectAll('.rectG').data(this.rects,function(d){
                return d.id
            });
            var g = rects.enter().insert('g','.regionDragRect').classed('rectG',true).each(function(d){
                _this.enterPoint(d3.select(this),d);
            });
            g.append('rect').classed('rgRect',true).call(this.dragRg);
            rects.exit().remove();
            rects = g = null;
            return this;
        },
        enterCircle:function(){
            var _this = this;
            this.circles = this.rgs.filter(function(d){
                if(d.regionJson){
                    return d.regionJson.regionType == 'ellipse';
                }
            })
            var circles = this.regionWrap.selectAll('.circleG').data(this.circles,function(d){
                return d.id
            });
            var g = circles.enter().insert('g','.regionDragRect').classed('circleG',true).each(function(d){
                _this.enterPoint(d3.select(this),d);
            });
            g.append('ellipse').classed('rgCircle',true).call(this.dragRg);
            circles.exit().remove();
            circles = g = null;
            return this;
        },
        enterPoint:function(selection,d){
            var rgP = selection.selectAll('.rgPoint').data(d.regionJson.point);
            var pt = rgP.enter().append('rect').attr('class',function(d,i){
                d3.select(this).attr({
                    width:8,
                    height:8,
                    x:d[0],
                    y:d[1]
                })
                return 'rgPoint rgP'+i;
            });
            if(d.regionJson.regionType == 'rect'){
                pt.call(this.dragRectPoint);
            }else{
                pt.call(this.dragEllipPoint);
            }
            rgP.exit().remove();
            return this;
        },
        updatePoint:function(selection){
            var points = selection.__data__.regionJson.point;
            var g = d3.select($(selection).closest('g').get(0));
            g.selectAll('.rgPoint').each(function(d,i){
                d3.select(this).attr({
                    x:points[i][0],
                    y:points[i][1]
                })
            })
        },
        enterRegions:function(opt){
            opt = opt || {text:true,rect:true,circle:true};
            opt.rect && this.enterRects();
            opt.circle && this.enterCircle();
            opt.text && this.enterText();
            return this;
        },
        updateRgText:function(selection){
            var _this = this;
            selection = selection || this.regionWrap.selectAll('.rgText');
            selection.text(function(d){
                d3.select(this).attr(d.contextJson).attr('dominant-baseline','hanging').attr('text',null);
                return d.contextJson.text;
            });
            selection.attr('text',function(d){
                var text = d3.select(this).attr(d.contextJson).text('');
                var text_datas = d.contextJson.text.split("\n");
                text_datas.forEach(function(value) {
                    text.append('tspan').text(value).attr({
                        'x' : d.contextJson.x,
                        'dy' : parseInt(d.contextJson['font-size'])+6
                    });
                });
                var bbox = this.getBBox();
                d.contextJson.width = bbox.width;
                d.contextJson.height = bbox.height;
                _this.maxRgX = Math.max(d.contextJson.x + d.contextJson.width, _this.maxRgX);
                _this.minRgX = Math.min(d.contextJson.x, _this.minRgX);
                _this.maxRgY = Math.max(d.contextJson.y+ d.contextJson.height, _this.maxRgY);
                _this.minRgY = Math.min(d.contextJson.y, _this.minRgY);
                return null;
            })
            _this = selection = null;
            return this;
        },
        setTextAttr:function(opt){ //修改文本框属性
            opt.selection = opt.selection || this.regionWrap.selectAll('.rgText');
            var obj = $.extend(true,{},opt);
            delete obj.selection;
            opt.selection.each(function(d){
                d.contextJson = $.extend({},d.contextJson,obj);
            })
            this.updateRgText(opt.selection);
        },
        updateShaps:function(selection,className){
            var _this = this;
            className = className || '.rgRect,.rgCircle';
            selection = selection || this.regionWrap.selectAll(className);
            selection.attr('regionType',function(d){
                d.regionJson.point = _this.pointXY(d.regionJson);
                _this.updatePoint(this);
                d3.select(this).attr(d.regionJson).attr('point',null);
                _this.maxRgX = Math.max(d.regionJson.point[2][0], _this.maxRgX);
                _this.minRgX = Math.min(d.regionJson.point[0][0], _this.minRgX);
                _this.maxRgY = Math.max(d.regionJson.point[2][1], _this.maxRgY);
                _this.minRgY = Math.min(d.regionJson.point[0][1], _this.minRgY);
                return null;
            });
            _this = selection = null;
            return this;
        },
        updateRegions:function(opt){
            this.resetRgXY();
            opt = opt || {text:true,shap:true};
            opt.text && this.updateRgText();
            opt.shap && this.updateShaps();
           // tp.setMaxCavWH(this.minRgX,this.maxRgX,this.minRgY,this.maxRgY);
            return this;
        },
        moveText:function(selection,x,y){
            selection.each(function(d){
                var _this = d3.select(this);
                d.contextJson.x=x;
                d.contextJson.y=y;
                _this.attr({
                    x:x,
                    y:y
                })
                _this.selectAll('tspan').attr({
                    x:x
                });
                _this = null;
            })
            /*selection.attr('x',function(d){
                d3.select(this).selectAll('tspan').attr({
                    x:x
                })
                return  d.contextJson.x=x
            }).attr('y',function(d){
                return  d.contextJson.y=y;;
            })*/
        },
        moveShaps:function(selection,x,y,isCircle){
            var _this = this;
            var xKey = isCircle ? 'cx' : 'x';
            var yKey = isCircle ? 'cy' : 'y';
            /*selection.attr(xKey,function(d){
                return  d.regionJson[xKey]=x;
            }).attr(yKey,function(d){
                d.regionJson[yKey]=y;
                d.regionJson.point = _this.pointXY(d.regionJson);
                _this.updatePoint(this);
                return  y;
            })*/
            selection.each(function(d){
                d.regionJson[xKey]=x;
                d.regionJson[yKey]=y;
                d.regionJson.point = _this.pointXY(d.regionJson);
                _this.updatePoint(this);
                var obj = {};
                obj[xKey]=x;
                obj[yKey]=y;
                d3.select(this).attr(obj);
                obj  = null;
            })
            _this = null;
        },
        doDrag:function(){
            var _this = this;
            this.dragRg = d3.behavior.drag().origin(function(d){   //拖动区域
                var json = d.contextJson || d.regionJson;
                return {
                    x: json.x || json.cx,
                    y: json.y || json.cy
                }
            }).on('drag',function(){
              //  d3.select(this).classed('selected',true);
                if(d3.event.dx==0 && d3.event.dy==0){
                    return;
                }
                typeof _this.dragFn == 'function' && _this.dragFn(d3.event,this);
            }).on('dragstart',function(){
                d3.event.sourceEvent.stopPropagation();
            }).on('dragend',function(){
                d3.event.sourceEvent.stopPropagation();
            });
            
            this.dragEllipPoint = d3.behavior.drag().on('drag',function(){
                var e = d3.event, x = e.x, y = e.y, dx = e.dx, dy = e.dy, gap =50;
                var target = d3.select(this);
                var shap = d3.select($(this).closest('g').find('.rgCircle').get(0));
                var json = shap.data()[0].regionJson;
                if(target.classed('rgP0')){ //左上
                    (x<json.point[2][0] -gap) && leftX();
                    (y<json.point[2][1] -gap) && rightY();
                }
                if(target.classed('rgP1')){
                    (x-json.point[0][0]>gap) && rightX();
                    (json.point[3][1] - y>gap) && rightY();
                }
                if(target.classed('rgP2')){
                    (x-json.point[0][0]>gap) && rightX();
                    (y -json.point[0][1]>gap) && leftY();
                }
                if(target.classed('rgP3')){
                    (x<json.point[2][0] -gap) && leftX();
                    (y -json.point[0][1]>gap) && leftY();
                }
                function leftX(){
                    json.width = json.width -dx;
                    json.rx = json.width / 2;
                    json.cx = x + json.rx
                }
                
                function leftY(){
                    json.height = y -json.point[0][1];
                    json.ry = json.height /2;
                    json.cy = y-json.ry;
                }
                
                function rightX(){
                    json.width = x-json.point[0][0];
                    json.rx = json.width / 2;
                    json.cx = x -json.rx;
                }
                
                function rightY(){
                    json.height = json.height -dy;
                    json.ry = json.height /2;
                    json.cy = y+json.ry;
                }
                _this.updateShaps(shap,'.rgCircle');
            }).on('dragstart',function(){
                d3.event.sourceEvent.stopPropagation();
            }).on('dragend',function(){
                d3.event.sourceEvent.stopPropagation();
            });
            
            this.dragRectPoint = d3.behavior.drag().on('drag',function(){
                var e = d3.event, x = e.x, y = e.y, dx = e.dx, dy = e.dy, gap =50;
                var target = d3.select(this);
                var shap = d3.select($(this).closest('g').find('.rgRect').get(0));
                var json = shap.data()[0].regionJson;
                if(target.classed('rgP0')){
                    (x<json.point[2][0] -gap) && leftX();
                    (y<json.point[2][1] -gap) && leftY();
                }
                if(target.classed('rgP1')){
                    (x-json.x>gap) && rightX();
                    (json.point[3][1] - y>gap) && leftY();
                }
                if(target.classed('rgP2')){
                    (x-json.x>gap) && rightX();
                    (y -json.y>gap) && rightY();
                }
                if(target.classed('rgP3')){
                    (x<json.point[2][0] -gap) && leftX();
                    (y -json.y>gap) && rightY();
                }
                function leftX(){
                    json.x = x;
                    json.width = json.width -dx;
                }
                
                function leftY(){
                    json.y = y;
                    json.height = json.height -dy;
                }
                
                function rightX(){
                    json.width = x - json.x;
                }
                
                function rightY(){
                    json.height = y - json.y;
                }
                _this.updateShaps(shap,'.rgRect');
            }).on('dragstart',function(){
                d3.event.sourceEvent.stopPropagation();
            }).on('dragend',function(){
                d3.event.sourceEvent.stopPropagation();
            });
        },
        pointXY:function(d){
            if(d.regionType){
                var x = (d.regionType == 'rect' ? d.x-4 : d.cx-d.rx);
                var y = (d.regionType == 'rect' ? d.y-4 : d.cy-d.ry);
            }else{
                var x = d.x, y = d.y;
            }
            var w=d.width,h=d.height;
            return [[x,y],[x+w,y],[x+w,y+h],[x,y+h]];//左上、右上、右下、左下
        },
        addTextClk:function(){
            this.rgRect.classed('hide',false);
        }
}
export default Region
