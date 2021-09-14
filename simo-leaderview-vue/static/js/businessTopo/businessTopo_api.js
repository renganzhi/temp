(function ($) {
    $.extend($.api || ($.api = {}), {
        businessTopoApi: {
            querySelNeIds: function () {
                var selectedNeIds = [];
                $.ajax({
                    url: "/business/topology/findAllNodeBySysId/" + topoBaseData,
                    type: "get",
                    dataType: "json",
                    async: false,
                    success: function (data) {
                        selectedNeIds = data.obj;
                    },
                });
                return selectedNeIds;
            },
            deviceType: function (callback) {
                $.ajax({
                    url: '/business/topology/findTopoNeClasses',
                    type: 'GET',
                    async: false,
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || []);
                        }
                    }
                });
            },
            getAuthority: function (callback) {		//查询业务系统的权重设置
                $.ajax({
                    url: "/business/weight/list",
                    data: {
                        businessId: topoBaseData
                    },
                    type: "GET",
                    dataType: 'json',
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || []);
                        }
                    }
                });
            },
            saveAuthority: function (data, callback) {
                $.ajax({
                    url: '/business/weight/save',
                    type: 'POST',
                    data: {
                        businessId: topoBaseData,
                        weightType: data.weightType,
                        nodeWeight: JSON.stringify(data.nodeWeight)
                    },
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || {});
                        }
                    }
                });
            },
            bnsTipInfo: function (businessId, callback, isAsync) {
                $.ajax({
                    url: '/business/topology/bnsTipInfo/' + businessId,
                    type: 'GET',
                    async: isAsync && true,
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || {});
                        }
                    }
                });
            },
            saveCustomTopo: function (backgroundIconId, callback) {
                $.ajax({
                    url: '/business/topology/setBackgroundIcon/' + topoBaseData + '/' + backgroundIconId,
                    type: 'GET',
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || []);
                        }
                    }
                });
            },
            getSelection: function (searchQuery, callback) {
                $.api.restful({
                    url: "/monitor/ne/search",
                    type: "POST",
                    async: false,
                    data: searchQuery,
                    dataType: "json",
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || {});
                        } else {
                            Notification({
                                message: '查询失败！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        }
                    }
                });
            },
            queryCapacity: function (callback, isAsync) {  //查询业务系统的容量配置
                $.ajax({
                    url: "/business/capacity/list?businessId=" + topoBaseData,
                    type: 'get',
                    async: isAsync && true,
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || {});
                        } else {
                            Notification({
                                message: '操作失败！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        }
                    }
                });
            },
            saveCapacity: function (data, callback, isAsync) {  //保存业务系统的容量配置
                $.ajax({
                    url: '/business/capacity/save',
                    type: 'post',
                    data: {
                        businessId: topoBaseData,
                        confStr: JSON.stringify(data)
                    },
                    async: isAsync && true,
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || {});
                            Notification({
                                message: '操作成功！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        } else {
                            Notification({
                                message: '操作失败！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        }
                    }
                });
            },
            topoConfig: function (tpId, callback, isAsync) {   //获取拓扑全局设置
                $.ajax({
                    url: '/business/topology/backgroundSet/' + tpId,
                    type: 'GET',
                    async: isAsync && true,
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || {});
                        }
                    }
                });
            },
            saveTopoConfig: function (data, callback) {
                $.ajax({
                    url: '/business/topology/saveBackgroundSet',
                    type: 'POST',
                    data: data,
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data || {});
                            Notification({
                                message: '操作成功！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        } else {
                            Notification({
                                message: '操作成功！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        }
                    }
                });
            },
            businessMainTopo: function (businessId, successCb, errorCb) {    //根据业务ID 获取拓扑图中的节点、连线、链路信息
                var nodesInfo = [];
                $.ajax({
                    url: '/business/topology/' + businessId,
                    type: 'GET',
                    success: function (data) {
                        if (data.success) {
                            nodesInfo = data.obj;
                            (typeof successCb == 'function') && successCb(data.obj || {});
                        } else {
                            (typeof errorCb == 'function') && errorCb(data.msg);
                        }
                    }
                });
                return nodesInfo;
            },
            deleteNe: function (neId, callback) {
                $.ajax({
                    url: '/monitor/ne/delete/' + neId + '/false',
                    type: 'DELETE',
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data || {});
                        }
                    }
                });
            },
            saveNodes: function (data, callback, isAsync) {  //节点增删改，param={node：[].toString()}
                $.ajax({
                    url: '/business/topology/saveNodes',
                    type: 'post',
                    data: {
                        businessId: topoBaseData,
                        nodeStr: JSON.stringify(data.nodes),
                        paramStr: JSON.stringify(data.params)
                    },
                    async: isAsync && true,
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || {});
                            Notification({
                                message: '操作成功！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        } else {
                            Notification({
                                message: '操作失败！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        }
                    }
                });
            },
            clusterInfo: function (id, callback) {    //根据集群节点ID 获取集群下的所有资源信息
                var nodesInfo = [];
                $.ajax({
                    url: '/business/topology/clusterInfo/' + id,
                    type: 'GET',
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || []);
                        }
                    }
                });
            },
            saveCluster: function (data, callback, isAsync) {  //集群节点增删改
                $.ajax({
                    url: '/business/topology/saveCluster',
                    type: 'post',
                    data: {
                        businessId: topoBaseData,
                        clusterStr: JSON.stringify(data.clusterStr),
                        nodeStr: JSON.stringify(data.nodeStr),
                        paramStr: JSON.stringify(data.params)
                    },
                    async: isAsync && true,
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || {});
                            Notification({
                                message: '操作成功！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        } else {
                            Notification({
                                message: '操作失败！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        }
                    }
                });
            },
            saveLinks: function (data, isDel, callback) {    //保存连线
                var param;
                if (isDel) {
                    param = {
                        linkStr: JSON.stringify(data.linkStr),
                        paramStr: JSON.stringify(data.params)
                    };
                } else {
                    param = { linkStr: JSON.stringify(data.links) };
                }
                $.ajax({
                    url: '/business/topology/saveLinks',
                    type: 'POST',
                    data: param,
                    success: function (res) {
                        if (res.success) {
                            (typeof callback == 'function') && callback(res.obj.links || []);
                            if (res.obj.nodes && res.obj.nodes.length > 0) {
                                var addNodeArr = [], delNodeArr = [];
                                $.each(res.obj.nodes, function (i, m) {
                                    if (m.status == "OK") {
                                        addNodeArr.push(m);
                                    } else {
                                        delNodeArr.push(m);
                                    }
                                });
                                tp.deleteNodes(delNodeArr);
                                tp.addNodes(addNodeArr);
                                tp.updateTp();
                            }
                            Notification({
                                message: '操作成功！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        } else {
                            Notification({
                                message: res.msg,
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        }
                    }
                });
            },
            /*saveLinks:function(data,callback){    //保存连线
                $.ajax({
                    url : '/business/topology/saveLinks',
                    type : 'POST',
                    data : {
                        linkStr:JSON.stringify(data.links)
  //                      paramStr:JSON.stringify(data.params)
                    },
                    success : function(data) {
                        if(data.success) {
                            (typeof callback== 'function') && callback(data.obj || []);
                            tooltip('', '操作成功！', 'success');
                        }else{
                            tooltip('', data.msg, 'info');
                        } 
                    }
                });
            },*/
            saveNetworkLinks: function (data, callback) {  //保存链路
                $.ajax({
                    url: '/monitor/topo/saveNetworkLinks',
                    type: 'POST',
                    data: {
                        linkStr: JSON.stringify(data.links),
                        paramStr: JSON.stringify(data.params),
                        businessId: data.businessId
                    },
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || []);
                            Notification({
                                message: '操作成功！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        }
                    }
                });
            },
            /*saveTopoChoosed:function(data,callback){  //保存复制的元素
                $.ajax({
                    url : '/monitor/topo/saveTopoChoosed',
                    type : 'POST',
                    data : {
                        topoIdFrom:data.topoIdFrom,
                        topoIdTo:data.topoIdTo,
                        ifSkip:data.ifSkip,
                        dataStr:JSON.stringify(data.dataStr)
                    },
                    success : function(data) {
                        if(data.success) {
                            data.obj && (typeof callback== 'function') && callback(JSON.parse(data.obj));
                            tooltip('', '操作成功！', 'success');
                        }
                    }
                });
            },*/
            saveAllTopo: function (data, callback) {  //保存元素(节点、区域)
                $.ajax({
                    url: '/business/topology/saveTopo',
                    type: 'POST',
                    data: {
                        businessId: topoBaseData,
                        dataStr: JSON.stringify(data.dataStr)
                    },
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || []);
                            Notification({
                                message: '操作成功！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        }
                    }
                });
            },
            refreshNode: function (nodeId, callback) {  //刷新节点信息
                $.ajax({
                    url: '/monitor/topo/refreshNode',
                    type: 'GET',
                    data: {
                        nodeId: nodeId
                    },
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || []);
                            Notification({
                                message: '操作成功！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        }
                    }
                });
            },
            //**区域**//*
            getRegion: function (businessId, callback) {
                $.ajax({
                    url: '/business/topology/region/' + businessId,
                    type: 'GET',
                    async: false,
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || []);
                        }
                    }
                });
            },
            saveRegion: function (rgData, businessId, callback) {
                $.ajax({
                    url: '/business/topology/saveRegion',
                    type: 'post',
                    data: {
                        regionStr: JSON.stringify(rgData),
                        businessId: businessId
                    },
                    success: function (res) {
                        if (res.success) {
                            (typeof callback == 'function') && callback(res.obj || []);
                            Notification({
                                message: '操作成功！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        }
                    }
                });
            },
            deleteIcons: function (ids, callback) {
                $.ajax({
                    url: '/business/topology/icon/delete',
                    type: 'post',
                    data: {
                        ids: ids.toString()
                    },
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || {});
                            Notification({
                                message: '删除成功！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        }
                    }
                });
            },
            findIconByType: function (data, callback) {
                $.ajax({
                    url: '/business/topology/icon/findIconByTypes',
                    type: 'GET',
                    data: {
                        iconTypeArray: JSON.stringify(data.iconType),
                        neClassArray: JSON.stringify(data.neClass)
                    },
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || []);
                        }
                    }
                });
            },
            getResPanel: function (callback) {
                $.ajax({
                    url: '/business/topology/icon/all',
                    type: 'GET',
                    async: false,
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || []);
                        }
                    }
                });
            },
            iconExist: function (name, id) {
                var msg = true;
                $.ajax({
                    url: '/business/topology/icon/nameCheck?name=' + name + '&id=' + id,
                    type: 'GET',
                    async: false,
                    success: function (data) {
                        msg = data.success;
                    }
                });
                return msg;
            },
            saveIcon: function (flag, callback) {  //上传、编辑图片
                return {
                    type: 'post',
                    url: flag == 'add' ? '/zuul/business/topology/icon/upload' : '/business/topology/icon/update',
                    dataType: 'json',
                    success: function (res) {
                        if (res.success) {
                            (typeof callback == 'function') && callback(res.obj || []);
                            Notification({
                                message: '删除成功！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        } else {
                            Notification({
                                message: res.msg,
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        }
                    }
                }
            },
            bindResList: function (param, businessId, callback) {
                //资源面板-拖拽-绑定资源类型列表
                $.api.restful({
                    url: '/monitor/topo/searchNE?businessId=' + businessId,
                    data: param,
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data || []);
                        }
                    }
                });
            },
            getNeWebManage: function (neId, callback) {   //获取资源webUrl配置
                $.ajax({
                    url: '/monitor/topo/getNeWebManage/' + neId,
                    type: 'GET',
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || {});
                        }
                    }
                });
            },
            saveNeWebManage: function (data, callback) {
                $.ajax({
                    url: '/monitor/topo/saveNeWebManage',
                    data: data,
                    type: 'POST',
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || {});
                        }
                    }
                });
            },
            ifCancelMonitoring: function (data, callback) {   //开启/取消链路监控
                $.ajax({
                    url: '/monitor/topo/ifCancelMonitoring',
                    data: {
                        linkStr: JSON.stringify(data.linkStr),
                        ifCancel: data.ifCancel
                    },
                    type: 'POST',
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || {});
                        }
                    }
                });
            },
            getResourceType: function (params) {
                $.ajax({
                    url: "/monitor/model/findActiveNeClassTree",
                    data: {
                        checkedNeClass: params.type
                    },
                    success: function (data) {
                        if (data.success && data.obj) {
                            (typeof params.callback == 'function') && params.callback(data.obj, params);
                        }
                    }
                })
            },
            getVendor: function (params) {
                $.ajax({
                    url: "/monitor/model/getVendorId/" + params.hostType,
                    success: function (data) {
                        if (data.success && data.obj) {
                            (typeof params.callback == 'function') && params.callback(data.obj.sort(), params);
                        }
                    }
                });
            },
            getVersion: function (params) {
                $.ajax({
                    url: "/monitor/model/getVersion/" + params.type + "/" + params.vendor,
                    success: function (data) {
                        if (data.success && data.obj) {
                            (typeof params.callback == 'function') && params.callback(data.obj.sort(), params);
                        }
                    }
                });
            },
            getDomain: function (params) {
                $.ajax({
                    url: "/mc/department/tree/false",
                    type: "GET",
                    success: function (data) {
                        if (data.success) {
                            (typeof params.callback == 'function') && params.callback(data.obj || [], params);
                        }
                    }
                });
            },
            isSoftware: function (objectType) {
                var isSoft;
                var defer = $.Deferred();
                $.ajax({
                    url: "monitor/ne/isSoftware/" + objectType,
                    type: "get",
                    async: false,
                    success: function (data) {
                        if (data && data.success) {
                            isSoft = true;
                        }
                    }
                });
                return isSoft;
            },
            ableRelateAsset: function (params) {
                $.ajax({
                    url: "/cmdb/asset/findByNeId/" + params.id,
                    type: "get",
                    error: function (err) {
                        (typeof params.errorCallback == 'function') && params.errorCallback(err, params);
                    },
                    success: function (data) {
                        (typeof params.successCallback == 'function') && params.successCallback(data, params);
                    }
                });
            },
            createAndRelateAsset: function (params) {
                $.ajax({
                    url: "/cmdb/asset/createAssetFromNe",
                    data: params.data,
                    traditional: true,
                    dataType: "json",
                    type: "post",
                    success: function (data) {
                        (typeof params.callback == 'function') && params.callback(data, params);
                    }
                });
            },
            getAbleRelatedAsset: function (params) {
                $.ajax({
                    url: "/cmdb/asset/getNoLinkedNe?hardOrSoft=" + !params.isSoft,
                    data: JSON.stringify($.extend({}, params.pgInfo.param(), params.keyword)),
                    traditional: true,
                    contentType: "application/json",
                    dataType: "json",
                    type: "post",
                    error: function (error) {
                        (typeof params.errorCallback == 'function') && params.errorCallback(error, params);
                    },
                    success: function (data) {
                        (typeof params.successCallback == 'function') && params.successCallback(data, params);
                    }
                });
            },
            relatedAsset: function (params) {
                $.ajax({
                    type: "get",
                    url: "/cmdb/asset/neLinkAsset",
                    data: {
                        neId: params.neId,
                        assetId: params.assetVal
                    },
                    success: function (data) {
                        (typeof params.callback == 'function') && params.callback(data, params);
                    }
                });
            },
            getResourceConfigInfo: function (params) {
                $.ajax({
                    url: "/monitor/ne/indicators/" + params.id,
                    type: "get",
                    success: function (data) {
                        (typeof params.callback == 'function') && params.callback(data, params);
                    }
                })
            },
            saveResourceConfigInfo: function (params) {
                $.ajax({
                    url: "/monitor/ne/indicators/save/" + params.id,
                    data: {
                        indicators: params.data
                    },
                    type: "post",
                    success: function (data) {
                        (typeof params.callback == 'function') && params.callback(data, params);
                    }
                });
            },
            changeIcon: function (data, callback) {    //拓扑图标修改
                $.ajax({
                    url: '/monitor/topo/changeIcon',
                    data: {
                        nodeStr: JSON.stringify(data.nodeStr),
                        newIconId: data.newIconId
                    },
                    type: 'GET',
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || {});
                            Notification({
                                message: '操作成功！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        }
                    }
                });
            },
            saveSubTopo: function (data, callback) {   //选择元素创建子网
                $.ajax({
                    method: 'post',
                    url: 'monitor/topo/saveSubTopo',
                    data: {
                        'parentId': data.parentId,
                        'nodeStr': JSON.stringify(data.nodeStr),
                        'regionStr': JSON.stringify(data.regionStr),
                        'iconId': data.iconId,
                        'name': data.name
                    },
                    error: function () {
                        Notification({
                            message: '操作失败！',
                            position: 'bottom-right',
                            customClass: 'toast toast-error'
                        })
                    },
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data);
                            Notification({
                                message: '操作失败！',
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        }
                    }
                });
            }
        },
        alert: {
            level: function (callback) {
                $.ajax({
                    url: '/alert/currencyAlertmanager/findActivatedLevels',
                    type: 'GET',
                    async: false,
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || {});
                        }
                    }
                });
            },

        },
        rule: {
            eventTemplate: function (callback) {
                $.ajax({
                    url: '/alert/template/event/enable/all',
                    type: 'GET',
                    async: false,
                    success: function (data) {
                        //                      if(data.success) {
                        (typeof callback == 'function') && callback(data.obj || {});
                        //                      }
                    }
                });
            },
        },
        domain: {
            userDomains: function (callback) {//获取所有域
                $.ajax({
                    url: "/mc/department/tree/true",
                    type: "GET",
                    async: false,
                    success: function (data) {
                        if (!data.success) {
                            return;
                        }
                        var domains = data.obj || [];
                        var tmp_domains = [];
                        $.each(domains, function (idx, dm) {
                            if (!dm.chkDisabled) {
                                tmp_domains.push(dm);
                            }
                        });
                        (typeof callback == 'function') && callback(tmp_domains);
                    }
                });
            }
        },
        ne: {
            collector: function (callback) {//获取所有采集器
                $.ajax({
                    url: "/monitor/ne/collector/all",
                    type: "GET",
                    success: function (data) {
                        (typeof callback == 'function') && callback(data || {});
                    }
                })
            },
            deleteMore: function (neIds, callback) { //删除资源
                $.ajax({
                    url: "/monitor/ne/deleteMore/" + neIds,
                    type: "DELETE",
                    success: function (data) {
                        (typeof callback == 'function') && callback(data || {});
                    }
                })
            }
        },
        customGroup: { //资源分组
            getCustomGroupById: function (groupId, callback, async) { //根据分组id获取分组资源
                $.ajax({
                    url: "/monitor/customGroup/getCustomGroupById/" + groupId,
                    async: !!async && true,
                    type: "GET",
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || {});
                        }
                    }
                })
            },
        },
        viewapi: {    //相关指标接口
            ifEntryAll: function (neId, callback, async) {    //获取资源所有接口
                $.ajax({
                    url: "/monitor/view/network/ifEntryAll/" + neId + '?fetchDate=',
                    type: "GET",
                    async: !!async && true,
                    success: function (data) {
                        (typeof callback == 'function') && callback(data.obj || {});
                    }
                })
            }
        },
        protocol: {
            getConf: function (neId, proto, callback) {  //获取资源原有的协议配置
                $.ajax({
                    url: '/monitor/protocol/protocolConf/' + neId + "/" + proto,
                    type: 'GET',
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj);
                        }
                    }
                });
            },
        },
        model: {
            hwNeClasses: function (callback) {
                $.ajax({
                    url: "monitor/model/hwNeClasses",
                    type: "get",
                    async: false,
                    success: function (data) {
                        if (data.success) {
                            (typeof callback == 'function') && callback(data.obj || {});
                        } else {
                            Notification({
                                message: data.msg,
                                position: 'bottom-right',
                                customClass: 'toast toast-error'
                            })
                        }
                    }
                })
            }
        }

    })
})($);