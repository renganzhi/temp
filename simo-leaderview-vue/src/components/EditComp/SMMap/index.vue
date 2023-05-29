<template>
  <div>
    <div id="map" style="margin: 0 auto; width: 100%; height: 100%"></div>
  </div>
</template>

<script>
import L from "leaflet";
import { TiledMapLayer } from "@supermap/iclient-leaflet";
import border from "./武侯边界.json";
import mask from "./mask.json";
import * as turf from "@turf/turf";
import 'leaflet-draw/dist/leaflet.draw.css';
import 'leaflet-draw/dist/leaflet.draw.js';
export default {
  name: "SMMap",
  data: function () {
    return {
      potData: {},
      llzyList: [],
      boundPot: [],
      gridWorkersAll: [],
      toggleSelect: false,
      mapType: true, // 2d与3d切换
      map: null,
      markerGroups: {},
      boxGroups:{},
      keyValue: {
        name: "名称",
        sort: "类型",
        // type: '类型',
        street: "街道",
        address: "地址",
        category: "大类",
        community: "社区",
        person_liable: "联系人",
        contact_number: "联系电话",
        room_name: "网约房名称",
        owner_name: "业主（房东）姓名",
        owner_phone: "业主（房东）联系方式",
        manager_name: "经营者姓名",
        maager_phone: "经营者联系方式",
        room_number: "房间数",
        bed_number: "床位数",
        gridman_name: "网格员姓名",
        gridman_phone: "网格员联系方式",
        police_name: "社区民警姓名",
        police_phone: "社区民警联系方式",
        liaison_name: "区域微型消防站联络员姓名",
        liaison_phone: "区域微型消防站联络员联系方式",
        team_name: "队伍名称",
        team_type: "队伍类型",
        team_work: "队伍职能",
        team_address: "队伍驻地",
        manager: "负责人",
        manager_phone: "负责人联系方式",
        member_number: "队伍人数",
        team_specialty: "队伍特长",
        team_equipment: "随队装备",
        assemble: "集结要求",
        gsbh: "古树编号",
        zm: "中名",
        sm: "俗名",
        ldxm: "拉丁学名",
        k: "科",
        s: "属",
        szcs: "生长场所",
        fbtd: "分布特点",
        px: "坡向",
        trmc: "土壤名称",
        nllx: "年龄类型",
        gsdj: "古树等级",
        pw: "坡位",
        xzgsmyy: "新增古树名木原因",
        lb: "类别",
        qs: "权属",
        pd: "坡度",
        hb: "海拔",
        tchd: "土层厚度",
        sl: "树龄",
        sg: "树高",
        xw: "胸围",
        pjgf: "平均冠幅",
        dxgf: "东西冠幅",
        nbgf: "南北冠幅",
        smqtxms: "树木奇特性状描述",
        ygph: "原挂牌号",
        ghdw: "管护单位",
        ghr: "管护人",
        bhxz: "保护现状",
        yhfzxz: "养护复壮现状",
        szjdjz: "树种鉴定记载",
        dcz: "调查者",
        dcrq: "调查日期",
        scz: "审查者",
        scrq: "审查日期",
        szs: "生长势",
        szhj: "生长环境",
        tbsj: "填表时间",
        yxszhjys: "影响生长环境因素",
        company: "公司名称",
        asset_name: "资产名称",
        ssgnq: "所属功能区",
        area: "面积（㎡）",
        qzyt: "权证用途",
        sjsyyt: "实际使用业态",
      },
      mapView: "",
      imgOverlay: "",
      focusMarker: {
        icon: "",
        point: "",
        dataArr: {
          title: "",
          rows: [],
        },
      }, // 弹窗点位
      showPop: false,
      showContent: false,
      tableDetail: {},
      header: process.env.NODE_ENV === "development" ? "./static/" : "./",
    };
  },
  props: ["nowPageName"],
  watch: {
    potData: {
      handler() {
        let url = "";
        this.markerGroups.eachLayer((laer) => {
          this.markerGroups.removeLayer(laer);
        });
        for (let index1 in this.potData) {
          if (index1 === "医院") {
            url = "/leaderview/newDistrict/GetDTDD1?param=";
          } else if (index1 === "党政机关") {
            url = "/leaderview/newDistrict/GetDTDD3?param=";
          } else if (index1 === "高层建筑") {
            url = "/leaderview/newDistrict/GetDTDD4?param=";
          } else if (index1 === "文化景点") {
            url = "/leaderview/newDistrict/GetDTDD4?param=";
          } else if (index1 === "学校") {
            url = "/leaderview/newDistrict/GetDTDD2?param=";
          } else if (index1 === "重点场所") {
            url = "/leaderview/newDistrict/GetDTDD7?param=";
          } else if (index1 === "处置队伍") {
            url = "/leaderview/newDistrict/GetDTDD6?street=";
          } else if (index1 === "天网") {
            url = "";
          } else if (index1 === "力量资源") {
            url = "/leaderview/ChengYun4/GetDTDD1";
          }
          for (let index2 in this.potData[index1]) {
            for (let index3 in this.potData[index1][index2]) {
              if (this.potData[index1][index2][index3]) {
                let u = url;
                if (index2 === "处置队伍") {
                  u = url + index3;
                } else if (index2 === "视频监控") {
                  if (index3 === "浆洗街街道") {
                    u = "/leaderview/newDistrict/GetJXList";
                  } else if (index3 === "火车南站街道") {
                    u = "/leaderview/newDistrict/GetHCNZList";
                  } else if (index3 === "簇桥街道") {
                    u = "/leaderview/newDistrict/GetCQList";
                  } else if (index3 === "华兴街道") {
                    u = "/leaderview/newDistrict/GetHXList";
                  } else if (index3 === "晋阳街道") {
                    u = "/leaderview/newDistrict/GetJYList";
                  } else if (index3 === "玉林街道") {
                    u = "/leaderview/newDistrict/GetYLList";
                  } else if (index3 === "簇锦街道") {
                    u = "/leaderview/newDistrict/GetCJList";
                  } else if (index3 === "红牌楼街道") {
                    u = "/leaderview/newDistrict/GetHPLList";
                  } else if (index3 === "金花桥街道") {
                    u = "/leaderview/newDistrict/GetJHQList";
                  } else if (index3 === "机投桥街道") {
                    u = "/leaderview/newDistrict/GetJTQList";
                  } else if (index3 === "望江路街道") {
                    u = "/leaderview/newDistrict/GetWJLList";
                  }
                } else if (index2 === "力量资源") {
                  debugger
                } else {
                  u = url + index2 + "&street=" + index3;
                }
                if (index2 === "名木古树") {
                  u = "/leaderview/newDistrict/GetDTDD5?street=" + index3;
                } else if (index2 === "网约房") {
                  u = "/leaderview/newDistrict/GetDTDD8?street=" + index3;
                }
                this.axios.get(u).then((res) => {
                  if (index2 === "视频监控") {
                    res.obj.forEach((d, i) => {
                      let lng = d.location_longitude || d.longitude;
                      let lat = d.location_latitude || d.latitude;
                      this.dotMap(lng, lat, index2, {
                        deviceIndexCode: d.deviceIndexCode,
                        name: d.name,
                      });
                      // this.markerGroups.addTo(this.map);
                    });
                  } else if (index2 === "力量资源") {
                    res.obj.rows.forEach((element) => {
                      this.dotMap(
                        element["经度"],
                        element["纬度"],
                        index2,
                        element
                      );
                    });
                    // // 在循环结束后将图层组添加到地图上
                    // this.markerGroups.addTo(this.map);
                  } else {
                    res.obj.dataArray.forEach((d, i) => {
                      let lng = d.location_longitude || d.longitude;
                      let lat = d.location_latitude || d.latitude;
                      this.dotMap(lng, lat, index2, d.items);
                    });
                    // 在循环结束后将图层组添加到地图上
                    
                  }
                });
              }
            }
          }
        }
        this.markerGroups.addTo(this.map);
        // this.MarkLLZY();
        // this.findBoundPot();
        // this.toggleSelect()
      },
      deep: true,
    },
    // llzyList: function () {
    //   if (this.llzyList) {
    //     if (this.llzyList.length) {
    //       this.MarkLLZY();
    //     } else {
    //       this.map.eachLayer((layer) => {
    //         if (!(layer instanceof L.TileLayer)) {
    //           this.map.removeLayer(layer);
    //         }
    //       });
    //     }
    //   }
    // },
    boundPot: function () {
      if (this.boundPot) {
        if (this.boundPot.length) {
          this.findBoundPot();
        } else {
        this.markerGroups.eachLayer((laer) => {
          this.markerGroups.removeLayer(laer);
        });
        }
      }
    },
    toggleSelect: function () {
        this.boxSelect() 
      },
    // gridWorkersAllDot: function () {
    //     this.gridWorkersAll
    //   },
    nowPageName: function () {
      if (this.nowPageName && this.nowPageName.indexOf("32:9") >= 0) {
        let centerPoint = L.latLng(30.62094, 104.01764); // 30.62367, 104.01035 (内网) / 30.62094, 104.01764 (外网)
        this.map.setView(centerPoint, 14.5); // 13.5 (内网) / 15 (外网)
        this.map.setZoom(13.5);
      } else {
        let centerPoint = L.latLng(30.630842978583685, 104.02394863823523); // 30.62840, 104.01869 (内网) / 30.630842978583685, 104.02394863823523 (外网)
        this.map.setView(centerPoint, 13.4); // 13.5 (内网) / 15 (外网)
        this.map.setZoom(13.44378);
      }
    },
  },
  mounted() {
    // 监听事件
    this.bus.$on("Mark", (res) => {
      this.potData = res;
    });
    // this.bus.$on("LLZYDot", (res) => {
    //   this.llzyList = res;
    // });
    this.bus.$on("searchPot", (res) => {
      this.boundPot = res;
    });
    this.bus.$on("toggleBoxSelect", (isEnabled) => {
      this.toggleSelect = isEnabled;
    });
    this.bus.$on("gridWorkersAll", (res) => {
      this.gridWorkersAll = res;
    });
    // 接收消息
    window.onmessage = (e) => {
      if (e.data.style === "2d") {
        this.map.setView([e.data.lat, e.data.lng], e.data.zoom);
        console.log("2d", e);
      }
      if (e.data.style === "3d") {
        console.log("3d", e);
      }
    };

    this.bus.$on("SwitchMap", (res) => {
      this.mapType = res.mapType;
      let mapData = {
        lng: this.map.getCenter().lng,
        lat: this.map.getCenter().lat,
        zoom: this.map.getZoom(),
        tilt: this.map.getTilt(),
        head: this.map.getHeading(),
        animation: res.animation,
        style: res.mapType ? "2d" : "3d",
      };
      window.parent && window.parent.postMessage(mapData, "*");
    });

    this.initMap();
  },

  methods: {
    initMap() {
      const url =
        // "http://10.1.173.241:58443/portalproxy/v84l5hpt/iserver/services/map-agscachev2-lanseCD2/rest/maps/lanseCD?KEY=Jdzyz5BhURnfTxeEFrEgsisT";
        "https://api.cdmbc.cn:4432/gateway/gis/1/102afceb09984699b1bf58f801229f73?AppKey=962000783182659584";
        

      const map = (this.map = window.L.map("map", {
        center: [30.6257, 104.0285],
        maxZoom: 18,
        zoom: 12,
        crs: window.L.CRS.EPSG4326,
      }));

      this.markerGroups = window.L.layerGroup();
      this.markerGroups.addTo(map);
      this.boxGroups = window.L.layerGroup();
      this.boxGroups.addTo(map);
      new TiledMapLayer(url).addTo(map);
      // 将遮罩添加到地图上
      window.L.geoJSON(mask, {
        style: {
          color: "#000",
          opacity: 0.5,
          fillOpacity: 0.5,
          fillColor: "#1e8ec6", // 填充色
          // strokeColor: '#8feee5', // 边线颜色
          // strokeWeight: 1, // 边线宽度
          // strokeOpacity: 0.5, // 边线透明度
          // strokeStyle: '' // 边线类型，solid或dashed
        },
      }).addTo(map);



      fetch(this.header + "geojson/xzqh.json")
        .then((response) => response.json())
        .then((data) => {
          const header = this.header;
          L.geoJSON(data, {
            onEachFeature: function (feature, layer) {
              // alert(header)
              var pointer = turf.centerOfMass(feature.geometry).geometry
                .coordinates;
              var myIcon;
              if (feature.properties.Name === "金花桥街道") {
                pointer = [103.97374548683935, 30.591885280709842];
              }
              if (feature.properties.Name.length === 4) {
                myIcon = L.icon({
                  iconUrl:
                    header + `img/街道名称/${feature.properties.Name}.png`,
                  iconSize: [32, 13],
                });
              } else if (feature.properties.Name.length === 5) {
                myIcon = L.icon({
                  iconUrl:
                    header + `img/街道名称/${feature.properties.Name}.png`,
                  iconSize: [49, 13],
                });
              } else {
                myIcon = L.icon({
                  iconUrl:
                    header + `img/街道名称/${feature.properties.Name}.png`,
                  iconSize: [66, 13],
                });
              }
              var streetName = L.marker([pointer[1], pointer[0]], {
                icon: myIcon,
              }).addTo(map);

              // 添加 click 事件监听器
              streetName.on("click", function () {
                map.setView(streetName.getLatLng(), 13);
              });
            },
          }).addTo(map);
        });


      window.L.DrawToolbar.prototype.options.position = 'topright'; // 设置绘图工具栏位置
      window.L.drawLocal.draw.handlers.rectangle.tooltip.start = '按住鼠标拖动绘制框选区域'; // 设置绘制矩形的提示文本

      var drawnItems = new window.L.FeatureGroup().addTo(map); // 创建一个图层用于存放绘制的矩形框
      map.addLayer(drawnItems);
      var drawControl = new window.L.Control.Draw({
        draw: {
          rectangle: {
            shapeOptions: {
              color: '#db3504', // 设置绘制矩形框的颜色
              weight: 2 // 设置绘制矩形框的线宽
            }
          },
          // 可以根据需要添加其他绘制类型，如圆、多边形等
        },
        edit: false // 禁用编辑功能
      });
      map.addControl(drawControl);

      // 监听绘制完成事件
      map.on(window.L.Draw.Event.CREATED, function (event) {
        var layer = event.layer;
        drawnItems.clearLayers(); // 清空之前绘制的框选区域
        drawnItems.addLayer(layer); // 将新绘制的框选区域添加到图层中
        var bounds = layer.getBounds();
        // 在这里处理框选范围内的标注显示逻辑
        // 可以使用 QueryByBoundsParameters 查询范围内的标注数据并显示
      });



    },
    dotMap(lng, lat, type, dataArray) {
      const offsetX = 0.00888;
      const offsetY = 0.0032;
      // 计算偏移后的经纬度
      const fixLng = Number(lng) - offsetX;
      const fixLat = Number(lat) - offsetY;
      if (type === "视频监控") {
        let potIcon = window.L.icon({
          iconUrl: this.header + `img/打点图/${type}.png`,
          iconSize: [40, 40],
        });
        let marker = window.L.marker([fixLat, fixLng], { icon: potIcon });
        marker.bindTooltip(dataArray.name);
        marker.on("click", (e) => {
          if (this.$parent.ShowVideoBox) {
            this.$parent.ShowVideoBox(dataArray.deviceIndexCode);
          } else if (this.$parent.$parent.ShowVideoBox) {
            this.$parent.$parent.ShowVideoBox(dataArray.deviceIndexCode);
          }
        });
        // 添加新标记到图层组
        this.markerGroups.addLayer(marker);
      } else if (type === "力量资源") {
        let potIcon = L.icon({
          iconUrl: this.header + `img/打点图/${type}.png`,
          iconSize: [40, 40],
        });
        let marker = L.marker([fixLat, fixLng], { icon: potIcon });
        marker.on("click", (e) => {
          if (this.$parent.ShowTanKuangBox) {
            this.$parent.ShowTanKuangBox({
              title: "力量资源详情",
              data: dataArray,
            });
          } else if (this.$parent.$parent.ShowTanKuangBox) {
            this.$parent.$parent.ShowTanKuangBox({
              title: "力量资源详情",
              data: dataArray,
            });
          }
        });

        // marker.addTo(this.map);
        // 添加新标记到图层组
        this.markerGroups.addLayer(marker);
      } else if (type === "网格员") {
        let potIcon = L.icon({
              iconUrl: this.header + `img/打点图/力量资源.png`,
              iconSize: [40, 40],
            });
        var marker = L.marker([fixLat, fixLng], { icon: potIcon })
        // 设置其他标注属性、样式等
        marker.on("click", (e) => {
          debugger
          
          if (this.$parent.ShowTableBox) {
            this.$parent.ShowTableBox(tableData);
          } else if (this.$parent.$parent.ShowTableBox) {
            this.$parent.$parent.ShowTableBox(tableData);
          }
          e.originalEvent.stopPropagation();
        });


        this.boxGroups.addLayer(marker);


        } else {
        let tableData = {
          title: type + "列表",
          data: "arry",
        };
        let columns = ["名称", "类型", "街道", "社区", "地址"];
        if (type === "处置队伍") {
          columns = ["队伍名称", "队伍类型", "队伍职能", "队伍驻地", "街道"];
        } else if (type === "名木古树") {
          columns = ["古树编号", "中名", "地址", "街道", "社区"];
        } else if (type === "网约房") {
          columns = ["网约房名称", "街道", "地址"];
        }
        let rows = [];
        dataArray.forEach((element) => {
          let json = {};
          for (let i in element) {
            if (this.keyValue[i]) {
              json[this.keyValue[i]] = element[i];
            }
          }
          rows.push(json);
        });
        tableData.dataArray = {
          columns: columns,
          rows: rows,
        };
        let potIcon = L.icon({
          iconUrl: this.header + `img/打点图/${type}.png`,
          iconSize: [40, 40],
        });
        let marker = L.marker([fixLat, fixLng], { icon: potIcon });
        marker.on("click", (e) => {
          debugger
          if (this.$parent.ShowTableBox) {
            this.$parent.ShowTableBox(tableData);
          } else if (this.$parent.$parent.ShowTableBox) {
            this.$parent.$parent.ShowTableBox(tableData);
          }
        });
        // 添加新标记到图层组
        this.markerGroups.addLayer(marker);
      }
    },
    // MarkLLZY() {
    //   this.llzyList.forEach((element) => {
    //     this.dotMap(element["经度"], element["纬度"], "力量资源", element);
    //   });
    // },
    boxSelect() {
      if (this.toggleSelect) {

        this.map.dragging.disable();
  


let map = this.map
var isDragging = false;
var dragStartLatLng, dragEndLatLng, selectionBox;
var mousedownPos = null;
var mousedownTime = null;
var isClick = false;
// 监听地图鼠标按下事件
map.on("mousedown", (e) =>{
  if (e.originalEvent.button === 0) {
    isDragging = true;
    dragStartLatLng = e.latlng;
    mousedownPos = map.mouseEventToLatLng(e.originalEvent);
    mousedownTime = Date.now();
    isClick = false; 
  }
});

// 监听地图鼠标移动事件
map.on("mousemove", (e) => {
  if (isDragging) {
    dragEndLatLng = e.latlng;

    // 绘制选择框矩形
    if (selectionBox) {
      this.map.removeLayer(selectionBox);
    }
    selectionBox = window.L.rectangle([dragStartLatLng, dragEndLatLng], {
      color: "#ff0000",
      weight: 2,
      opacity: 0.5,
    }).addTo(map);
  }
});

// 监听地图鼠标松开事件
map.on("mouseup", (e) => {
  debugger
  if (e.originalEvent.button === 0) {
    isDragging = false;
    // var mouseupPos = map.mouseEventToLatLng(e.originalEvent);
    // var mouseupTime = Date.now();

    // var dx = mouseupPos.lng - mousedownPos.lng;
    // var dy = mouseupPos.lat - mousedownPos.lat;
    // var distance = Math.sqrt(dx*dx + dy*dy);
    // var time = mouseupTime - mousedownTime;

    // var clickMaxDistance = 0.01;  // 设置你认为的最大距离
    // var clickMaxTime = 200;  // 设置你认为的最长时间（毫秒）

    // if (distance < clickMaxDistance && time < clickMaxTime) {
    //   // 如果移动的距离小，并且按下的时间很短，就认为是点击
    //   isClick = true;
    // }

    // // 如果是点击行为，就不进行框选相关的操作
    // if (isClick) {
    //   return;
    // }
    // 移除选择框
    if (selectionBox) {
      map.removeLayer(selectionBox);
    }

    // 进行空间查询，获取框选范围内的标注
    var bounds = L.latLngBounds(dragStartLatLng, dragEndLatLng);
    var selectedMarkers = this.gridWorkersAll.filter(function(marker) {
      // var markerLatLng = L.latLng(marker['经度'], marker['纬度']);
      var markerLatLng = L.latLng(marker['纬度'], marker['经度']);
// console.log(selectedMarkers.length);
      return bounds.contains(markerLatLng);
    });


    // 显示选中的标注
    this.boundPot = [];
    // this.boxGroups = [];
    if(this.boxGroups.getLayers().length > 0) {
      debugger
this.boxGroups.clearLayers();

    }
      
// this.boxGroups = [];
    selectedMarkers.forEach((marker) => {
    this.dotMap( marker['经度'], marker['纬度'],'网格员',marker);
  });

    
  }
});



      } else {
        this.map.dragging.enable();
      }
    },
    findBoundPot() {
      this.boundPot.forEach((element) => {
        this.dotMap(element["经度"], element["纬度"], "力量资源", element);
      });
    },
    ClosePop() {
      if (this.showContent) {
        this.showContent = false;
        this.tableDetail = {};
      } else {
        this.showContent = false;
        this.tableDetail = {};
        this.showPop = false;
        this.focusMarker = {
          icon: "",
          point: "",
          dataArr: {
            title: "",
            rows: [],
          },
        };
      }
    },
    ShowContent(data) {
      this.showContent = true;
      this.tableDetail = data;
    },
   
  },
  beforeDestroy(){
    //移除监听事件"aMsg"
  	// this.Bus.$off("searchPot")
  	// this.Bus.$off("Mark")
    // this.bus.$off('toggleBoxSelect');
  }
};
</script>

<style scoped>
#map {
  position: absolute;
  top: 0;
  bottom: 0;
  right: 0;
  left: 0;
  background: #001934;
}
</style>
