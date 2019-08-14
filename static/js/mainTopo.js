function MainTp(opt){
    this.opt = opt;
    $(opt.el).empty().css('opacity',0);
    if(!this.opt || !this.opt.tpId){
        return;
    }
    this.init();
}

MainTp.prototype = {
    constructor : Topology,
    init:function () {
        this.getTpConfig();
        this.tp = new Topology({
            selector:this.opt.el,
            config:this.opt.config
        }, this.opt.tpId);

        this.rg = new Region({
            selector:this.tp.regionWrap,
        });

        this.getTpInfo();
        this.getRegions();
    },
    getTpConfig:function () {
        var _this = this;
        $.api.topo.topoConfig(this.opt.tpId,function(res){
            res.topoId = _this.opt.tpId;
            res.default = false;
            _this.opt.curConfig = res;
            _this.changeTpData(res)
            _this = null;
        },false);
    },
    changeTpData:function(data){
        this.opt.config = $.extend(true,{canvasSize:{width:1920,height:1080}},data);
    },
    getTpInfo:function(flag){
        var _this = this;
        $.api.topo.domainTopo(this.opt.tpId,function(data){
            _this.nodes = data.nodes || [];
            _this.links = data.links || [];
            _this.initTopo(flag);
            _this = null;
        },function(msg){
            _this.nodata(msg);
        })
    },
    initTopo:function(isFresh){
        var _this = this;
        var tp = this.tp;
        var rg = this.rg;
        tp.addNodes(this.nodes);
        tp.addLinks(this.links);
        tp.update();
        if(isFresh){
            return;
        }
        setTimeout(function(){
           /* if(_this.opt.viewBox) {
                tp.svgContainer.attr('viewBox',_this.opt.viewBox);
            }else{*/
                tp.setMaxCavWH(rg.minRgX, rg.maxRgX, rg.minRgY, rg.maxRgY);
               $(_this.opt.el).css('opacity',1);
            /*}*/
            _this = null;
        },0)

    },
    getRegions:function(){
        var _this = this;
        $.api.topo.getRegion(this.opt.tpId,function(res){
            _this.rg.init(res);
            _this = null;
        })
    },
    refreshTp:function(){
        this.tp.nodes = [];
        this.tp.links = [];
        this.getTpInfo('fresh');
        this.getRegions();
    },
    nodata:function(msg){
        $(this.opt.el).empty().html('<div class="portlet_status_noDataText">'+msg+'</div>');
    }
}
