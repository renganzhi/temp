$(function () {
    var linkManage = {
        bind: function () {
            var _this = this;
            _this.getCheckName();
            _this.getTopTenShow();
            _this.getUserfulShow();
            $('#map_linkmanagement').off('click', '[data-linkMan]').on('click', '[data-linkMan]', function (e) {
                var type = $(this).attr('data-linkMan');
                typeof _this[type] == 'function' && _this[type](this, e);
                e.stopPropagation();
            })
        },
        getTopTenShow: function () {
            var _this = this;
            function sortBy(props) {
                return function (a, b) {
                    if (a[props] == undefined) {
                        return 1;
                    } else if (b[props] == undefined) {
                        return -1;
                    } else {
                        return b[props] - a[props];
                    }
                }
            }
            var topTendata = mapTp.links.sort(sortBy("traffic"));
            if (topTendata.length > 0) {
                var num = 0;
                var flag = true;
                $('#map_linkmanagement .topTenlink').empty();
                $.each(topTendata, function (i, d) {
                    if (i<10&&d.networkLinkId) {
                        _this.linkDraw(d, (num += 1));
                        flag = false;
                    }
                })
                if (flag) {
                    _this.noDataShow($("#map_linkmanagement .topTenlink"));
                }
            } else {
                _this.noDataShow($("#map_linkmanagement .topTenlink"));
            }
        },
        getUserfulShow: function () {
            var _this = this;
            var monitoredata = {};
            monitoredata.monitoredNum = 0;
            monitoredata.nomonitoredNum = 0;
            monitoredata.availableNum = 0;
            monitoredata.unavailableNum = 0;
            var unuseable = $('#map_linkmanagement .resourcesListul ul').empty();
            var userId = $("#map_attention .portletTitle .active").text().trim()=="分享地图"?$("#map-tree-wrap .userClick").attr("userId"):"";
            $.apiMp.mapTopo.neAvailability($('#map_topo_wrap').attr("mapid"),userId, function (res) {
                var nodesData = res;
                var useableName;
                for (var i = 0; i < nodesData.length; i++) {
                    if (nodesData[i].isMonitoring) {
                        monitoredata.monitoredNum++;
                    } else {
                        monitoredata.nomonitoredNum++;
                    }
                    if (nodesData[i].useableState) {
                        monitoredata.availableNum++;
                    } else {
                        monitoredata.unavailableNum++;
                        var unuseableli = document.createElement('li');
                        var spaceText = nodesData[i].location.split(',');
                        var spaceTextNum = spaceText.length;
                        var MpValue = $('#topoMapTree').find('.curSelectedNode span').eq(1).text();
                        if(MpValue=="全国地图"){
                            if(spaceTextNum==1){
                                useableName=spaceText[0];
                            }else{
                                useableName=spaceText[1];
                            }
                        }else if(MpValue==spaceText[1]){
                            if(spaceTextNum==2){
                                useableName=spaceText[1];
                            }else{
                                useableName=spaceText[2];
                            }
                        }else if(MpValue==spaceText[2]){
                            if(spaceTextNum==3){
                                useableName=spaceText[2];
                            }else{
                                useableName=spaceText[3];
                            }
                        }
                        unuseable.append($('<li>').text(useableName).append($('<p class="unAvailable">').text(nodesData[i].ip)));
                    }
                }
                if (monitoredata.unavailableNum <= 0) {
                    _this.noDataShow($('#map_linkmanagement .resourcesListul ul'));
                }
    
                $.each($('#map_linkmanagement .resourcesNum').find('[name]'), function (i, d) {
                    d.innerHTML = monitoredata[d.getAttribute("name")];
                })
            })

        },
        getCheckName: function () {
            var name = $("#map_attention .portletTitle .active").text().trim()=="分享地图"?$('#map-tree-wrap .userClick').find('.curSelectedNode .node_name').html().split("<")[0]:$('#topoMapTree').find('.curSelectedNode .node_name').html().split("<")[0]; 
            $('#map_linkmanagement .mapName').html(name);
        },
        linkDraw: function (d, num) {//渲染数据样式
            var startName = d.source.location.split(',');
            var endplacearry = d.target.location.split(',');
            var totalFlow = bytesToSize(d.traffic) ? bytesToSize(d.traffic) + 'PS' : '--';
            var linkutilization = d.speedUsage || d.speedUsage == 0 ? d.speedUsage.toFixed(2) + '%' : '--';
            var $dom = $('#map_linkmanagement .topTenlink');
            var singerBox = $dom.append($('<div class="linkList">').append($('<div class="linkList-line flex">')
                .append($('<div class="tipsnum">').text(num + "."))
                .append($('<div class="tenTextBox">').attr("title", startName[startName.length - 1]).text(startName[startName.length - 1]))
                .append($('<div class="linkBox">')
                    .append($('<div class="totalFlow">').text("总流量:").append($('<span>').text(totalFlow)))
                    .append($('<div class="line-box isok">').append($('<div>').attr("class","link full-height full-width fl " + d.linkStatus).append($('<div class="bluecircular fl bgcolorwhite '+ "anim"+num+'">'))))
                    .append($('<div class="linkutilization">').text("宽带利用率:").append($('<span>').text(linkutilization))))
                .append($('<div class="tenTextBox">').attr("title", endplacearry[endplacearry.length - 1]).text(endplacearry[endplacearry.length - 1]))
            ));
            if(d.linkStatus!="Enable"&&d.linkStatus!="Alert"){
               $(".anim"+num).hide();
            }
            titleShow();
        },
        noDataShow: function (dom) { //无据展示
            var _this = this;
            dom.empty()
                .append($('<div style="text-align: center;">').attr("class", "neNodataIcon").append($('<i>').attr('class', 'icon-n-nodata nodata')).append($('<span>').attr('class', 'noprompt').html("抱歉，没有数据可供展示...")));

        },
        listSwitch: function () {//下部收缩放开按钮
            $('.manageTitle').toggleClass('hidden');
            $('.manageBody').toggleClass('hidden');
            $('.listmanage').toggleClass('closetree');
            $('.listmanage').toggleClass("icon-n-open").toggleClass("icon-n-retract");
            // $('#map_linkmanagement').toggleClass('content-side');
            $('#map_linkmanagement').toggleClass('linkmanagement');
        }
    }
    window.linkManage = linkManage;
})