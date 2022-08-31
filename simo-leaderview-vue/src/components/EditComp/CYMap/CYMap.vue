<template>
     <div id="backBox">
        <div  id="cyMap">
        </div>
     </div>
</template>

<script>
import * as turf from '@turf/turf'
import styleJson from './custom_map_config.json'
import element from '@/element'
import { ColorPicker } from 'element-ui'
export default {
  data: function () {
    return {
      potData: {},
      keyValue: {
        name: '名称',
        sort: '类型',
        // type: '类型',
        street: '街道',
        address: '地址',
        category: '大类',
        community: '社区',
        person_liable: '联系人',
        contact_number: '联系电话',
        room_name: '网约房名称',
        owner_name: '业主（房东）姓名',
        owner_phone: '业主（房东）联系方式',
        manager_name: '经营者姓名',
        maager_phone: '经营者联系方式',
        room_number: '房间数',
        bed_number: '床位数',
        gridman_name: '网格员姓名',
        gridman_phone: '网格员联系方式',
        police_name: '社区民警姓名',
        police_phone: '社区民警联系方式',
        liaison_name: '区域微型消防站联络员姓名',
        liaison_phone: '区域微型消防站联络员联系方式',
        team_name: '队伍名称',
        team_type: '队伍类型',
        team_work: '队伍职能',
        team_address: '队伍驻地',
        manager: '负责人',
        manager_phone: '负责人联系方式',
        member_number: '队伍人数',
        team_specialty: '队伍特长',
        team_equipment: '随队装备',
        assemble: '集结要求',
        gsbh: '古树编号',
        zm: '中名',
        sm: '俗名',
        ldxm: '拉丁学名',
        k: '科',
        s: '属',
        szcs: '生长场所',
        fbtd: '分布特点',
        px: '坡向',
        trmc: '土壤名称',
        nllx: '年龄类型',
        gsdj: '古树等级',
        pw: '坡位',
        xzgsmyy: '新增古树名木原因',
        lb: '类别',
        qs: '权属',
        pd: '坡度',
        hb: '海拔',
        tchd: '土层厚度',
        sl: '树龄',
        sg: '树高',
        xw: '胸围',
        pjgf: '平均冠幅',
        dxgf: '东西冠幅',
        nbgf: '南北冠幅',
        smqtxms: '树木奇特性状描述',
        ygph: '原挂牌号',
        ghdw: '管护单位',
        ghr: '管护人',
        bhxz: '保护现状',
        yhfzxz: '养护复壮现状',
        szjdjz: '树种鉴定记载',
        dcz: '调查者',
        dcrq: '调查日期',
        scz: '审查者',
        scrq: '审查日期',
        szs: '生长势',
        szhj: '生长环境',
        tbsj: '填表时间',
        yxszhjys: '影响生长环境因素',
        company: '公司名称',
        asset_name: '资产名称',
        ssgnq: '所属功能区',
        area: '面积（㎡）',
        qzyt: '权证用途',
        sjsyyt: '实际使用业态'
      },
      header: process.env.NODE_ENV === 'development' ? './static/' : './'
    }
  },
  props: ['nowPageName'],
  watch: {
    potData: {
      handler () {
        let url = ''
        this.map.clearOverlays()
        for (let index1 in this.potData) {
          if (index1 === '医院') {
            url = '/leaderview/newDistrict/GetDTDD1?param='
          } else if (index1 === '党政机关') {
            url = '/leaderview/newDistrict/GetDTDD3?param='
          } else if (index1 === '高层建筑') {
            url = '/leaderview/newDistrict/GetDTDD4?param='
          } else if (index1 === '文化景点') {
            url = '/leaderview/newDistrict/GetDTDD4?param='
          } else if (index1 === '学校') {
            url = '/leaderview/newDistrict/GetDTDD2?param='
          } else if (index1 === '重点场所') {
            url = '/leaderview/newDistrict/GetDTDD7?param='
          } else if (index1 === '处置队伍') {
            url = '/leaderview/newDistrict/GetDTDD6?street='
          } else if (index1 === '天网') {
            url = ''
          }
          for (let index2 in this.potData[index1]) { // index2表示2级菜单点位
            for (let index3 in this.potData[index1][index2]) { // index3表示各个街道
              if (this.potData[index1][index2][index3]) {
                let u = url
                if (index2 === '处置队伍') {
                  u = url + index3
                } else if (index2 === '视频监控') {
                  if (index3 === '浆洗街街道') {
                    u = '/leaderview/newDistrict/GetJXList'
                  } else if (index3 === '火车南站街道') {
                    u = '/leaderview/newDistrict/GetHCNZList'
                  } else if (index3 === '簇桥街道') {
                    u = '/leaderview/newDistrict/GetCQList'
                  } else if (index3 === '华兴街道') {
                    u = '/leaderview/newDistrict/GetHXList'
                  } else if (index3 === '晋阳街道') {
                    u = '/leaderview/newDistrict/GetJYList'
                  } else if (index3 === '玉林街道') {
                    u = '/leaderview/newDistrict/GetYLList'
                  } else if (index3 === '簇锦街道') {
                    u = '/leaderview/newDistrict/GetCJList'
                  } else if (index3 === '红牌楼街道') {
                    u = '/leaderview/newDistrict/GetHPLList'
                  } else if (index3 === '金花桥街道') {
                    u = '/leaderview/newDistrict/GetJHQList'
                  } else if (index3 === '机投桥街道') {
                    u = '/leaderview/newDistrict/GetJTQList'
                  } else if (index3 === '望江路街道') {
                    u = '/leaderview/newDistrict/GetWJLList'
                  }
                } else {
                  u = url + index2 + '&street=' + index3
                }
                if (index2 === '名木古树') {
                  u = '/leaderview/newDistrict/GetDTDD5?street=' + index3
                } else if (index2 === '网约房') {
                  u = '/leaderview/newDistrict/GetDTDD8?street=' + index3
                }
                this.axios.get(u).then(res => {
                  if (index2 === '视频监控') {
                    res.obj.forEach((d, i) => {
                      let lng = d.location_longitude || d.longitude
                      let lat = d.location_latitude || d.latitude
                      this.dotMap(lng, lat, index2, {
                        deviceIndexCode: d.deviceIndexCode,
                        name: d.name
                      }
                      )
                    })
                  } else {
                    res.obj.dataArray.forEach((d, i) => {
                      let lng = d.location_longitude || d.longitude
                      let lat = d.location_latitude || d.latitude
                      this.dotMap(lng, lat, index2, d.items)
                    })
                  }
                })
              }
            }
          }
        }
      },
      deep: true
    }
  },
  methods: {
    initMap () {
      this.map = new window.BMapGL.Map('cyMap')
      const centerPoint = new window.BMapGL.Point(104.22513, 30.600862475501987) // 定义一个中心点坐标
      this.map.centerAndZoom(centerPoint, 14.25472) // 设定地图的中心点和坐标并将地图显示在地图容器中 104.02959, 30.60570
      this.map.enableScrollWheelZoom(true) // 开启鼠标滚轮缩放
      this.map.setMapStyleV2({styleJson: styleJson})
      this.getWHQ()
    },
    // 绘制武侯区
    getWHQ () {
      $.getJSON(this.header + 'geojson/xzqh.json', res => {
        res.features.forEach(data => {
          let boundaries = []
          let points = []
          let myIcon = ''
          let marker = ''
          if (data.geometry.coordinates) {
            if (data.geometry.coordinates.length > 1) {
              data.geometry.coordinates.forEach((el, index) => {
                boundaries[index] = []
                el.forEach(e => {
                  e.forEach(v => {
                    boundaries[index].push(v)
                  })
                })
              })
            } else {
              data.geometry.coordinates.forEach(el => {
                el.forEach(e => {
                  boundaries.push(e)
                })
              })
            }
            if (Array.isArray(boundaries[0][0])) {
              boundaries.forEach(el => {
                points = []
                el.forEach(e => {
                  points.push(new window.BMapGL.Point(e[0], e[1]))
                })
                let polygon = new window.BMapGL.Polygon(points, {
                  fillColor: '#132041', // 填充色
                  fillOpacity: 0.8, // 填充色透明度
                  strokeColor: '#25396A', // 边线颜色
                  strokeWeight: 2, // 边线宽度
                  strokeOpacity: 1, // 边线透明度
                  strokeStyle: '' // 边线类型，solid或dashed
                })
                // polygon.addEventListener('click', e => {
                //   this.getMapCenter()
                // })
                polygon.disableMassClear()
                this.map.addOverlay(polygon)
              })
            } else {
              boundaries.forEach(element => {
                points.push(new window.BMapGL.Point(element[0], element[1]))
              })
              let polygon = new window.BMapGL.Polygon(points, {
                fillColor: '#132041', // 填充色
                fillOpacity: 0.8, // 填充色透明度
                strokeColor: '#25396A', // 边线颜色
                strokeWeight: 2, // 边线宽度
                strokeOpacity: 1, // 边线透明度
                strokeStyle: '' // 边线类型，solid或dashed
              })
              // polygon.addEventListener('click', e => {
              //   this.getMapCenter()
              // })
              polygon.disableMassClear()
              this.map.addOverlay(polygon)
            }
            let pointer = turf.centerOfMass(data.geometry).geometry.coordinates
            if (data.properties.Name === '金花桥街道') {
              pointer = [103.97374548683935, 30.591885280709842]
            }
            if (data.properties.Name.length === 4) {
              myIcon = new window.BMapGL.Icon(this.header + `img/街道名称/${data.properties.Name}.png`, new window.BMapGL.Size(16, 6.5))
            } else if (data.properties.Name.length === 5) {
              myIcon = new window.BMapGL.Icon(this.header + `img/街道名称/${data.properties.Name}.png`, new window.BMapGL.Size(24.5, 6.5))
            } else {
              myIcon = new window.BMapGL.Icon(this.header + `img/街道名称/${data.properties.Name}.png`, new window.BMapGL.Size(33, 6.5))
            }
            // if (data.properties.Name === '火车南站街道') {
            //   myIcon = new window.BMapGL.Icon(this.header + `img/街道名称/${data.properties.Name}.png`, new window.BMapGL.Size(50.5, 14.85))
            // } else {
            //   myIcon = new window.BMapGL.Icon(this.header + `img/街道名称/${data.properties.Name}.png`, new window.BMapGL.Size(42, 14.85))
            // }
            // 创建标注对象并添加到地图
            marker = new window.BMapGL.Marker(new window.BMapGL.Point(pointer[0], pointer[1]), {icon: myIcon})
            marker.disableMassClear()
            this.map.addOverlay(marker)
          }
        })
      }) // 绘制武侯区
    },
    getMapCenter () {
      var cen = this.map.getCenter() // 获取地图中心点
      alert('地图中心点: (' + cen.lng.toFixed(5) + ', ' + cen.lat.toFixed(5) + ')')
    },
    // 清除覆盖物
    removeOverlay () {
      this.map.clearOverlays()
    },
    dotMap (lng, lat, type, dataArray) {
      if (type === '视频监控') {
        let potIcon = new window.BMapGL.Icon(this.header + `img/打点图/${type}.png`, new window.BMapGL.Size(40, 40))
        let marker = new window.BMapGL.Marker(new window.BMapGL.Point(lng, lat), {icon: potIcon})
        // let label = new window.BMapGL.Label(dataArray.name, {
        //   position: new window.BMapGL.Point(lng, lat),
        //   offset: new window.BMapGL.Size(30, -30)
        // })
        // marker.setLabel(label)
        marker.setTitle(dataArray.name)
        marker.addEventListener('click', e => {
          this.$parent.ShowVideoBox(dataArray.deviceIndexCode)
        })
        this.map.addOverlay(marker)
      } else {
        let tableData = {
          title: type + '列表',
          data: 'arry'
        }
        let columns = ['名称', '类型', '街道', '社区', '地址']
        if (type === '处置队伍') {
          columns = ['队伍名称', '队伍类型', '队伍职能', '队伍驻地', '街道']
        } else if (type === '名木古树') {
          columns = ['古树编号', '中名', '地址', '街道', '社区']
        } else if (type === '网约房') {
          columns = ['网约房名称', '街道', '地址']
        }
        let rows = []
        dataArray.forEach(element => {
          let json = {}
          for (let i in element) {
            if (this.keyValue[i]) {
              json[this.keyValue[i]] = element[i]
            }
          }
          rows.push(json)
        })
        tableData.dataArray = {
          columns: columns,
          rows: rows
        }
        let potIcon = new window.BMapGL.Icon(this.header + `img/打点图/${type}.png`, new window.BMapGL.Size(40, 40))
        let marker = new window.BMapGL.Marker(new window.BMapGL.Point(lng, lat), {icon: potIcon})
        marker.addEventListener('click', e => {
          this.$parent.ShowTableBox(tableData)
        })
        this.map.addOverlay(marker)
      }
    }
  },
  mounted () {
    this.bus.$on('Mark', res => {
      this.potData = res
    })
    this.initMap()
  }
}
</script>
<style lang="scss">
#backBox{
  width: 100%;
  height: 100%;
  position: relative;
    #cyMap{
        width: 100%;
        height: 100%;
    }
}

</style>
