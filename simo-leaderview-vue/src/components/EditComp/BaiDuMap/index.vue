<template>
  <div
    :id="id"
    class="Boxmap"
    style="
        width: calc(100% - 20px);
        height: calc(100% - 20px);
        top: 10px;
        left: 10px;
      "
  />
</template>
<script>
// import { apiUrl } from '@/config/settings'
export default {
  name: 'baidumap',
  props: ['item'],
  data () {
    return {
      id: 'Boxmap', // 地图绑定dom的id
      inputId: 'Boxinput',
      delLabel: ''
    }
  },
  watch: {
    'item.chartData': function (newV, oldV) {
      this.initMap()
    },
    'item.selectChange': function () {
      let point = new window.BMap.Point(
        this.item.selectMark.lng,
        this.item.selectMark.lat
      )
      let marker = new window.BMap.Marker(point, {
        icon:
          this.item.selectMark.icon.indexOf('类型3') !== -1 ||
          this.item.selectMark.icon.indexOf('类型4') !== -1
            ? new window.BMap.Icon(
              this.item.selectMark.icon,
              new window.BMap.Size(80, 80)
            )
            : this.item.selectMark.icon.indexOf('marker_red_sprite') !== -1
              ? new window.BMap.Icon(
                this.item.selectMark.icon,
                new window.BMap.Size(39, 25)
              )
              : this.item.selectMark.icon.indexOf('类型1') !== -1
                ? new window.BMap.Icon(
                  this.item.selectMark.icon,
                  new window.BMap.Size(30, 30)
                )
                : new window.BMap.Icon(
                  this.item.selectMark.icon,
                  new window.BMap.Size(40, 40)
                )
      })
      this.map.addOverlay(marker)
      let _this = this
      if (
        _this.$route.name === 'edit' &&
        !_this.$parent.$parent.previewStatus
      ) {
        marker.addEventListener('rightclick', function (e) {
          // 点标记右键事件删除
          let html = `<p id="del" style="z-index:1000;cursor:pointer;text-align:center;line-height:30px">删除</p>`
          let opt = {
            position: point
          }
          let label = new window.BMap.Label(html, opt)
          label.setStyle({
            color: '#cad6dd',
            width: '60px',
            background: '#2d3c4c',
            borderColor: 'transparent',
            height: '30px',
            // fontSize: '16px',
            padding: '0 0 5px 0',
            lineHeight: '30px',
            fontFamily: '微软雅黑'
          })
          if (_this.delLabel) {
            _this.map.removeOverlay(_this.delLabel)
          }
          _this.delLabel = label
          _this.map.addOverlay(label)
          document.getElementById('del').onclick = function () {
            _this.map.removeOverlay(e.currentTarget)
            _this.item.pointArray.forEach((element, index) => {
              if (
                e.currentTarget.point.lng === element.lng &&
                e.currentTarget.point.lat === element.lat
              ) {
                _this.item.pointArray.splice(index, 1)
              }
            })
            _this.map.removeOverlay(label)
            _this.delLabel = ''
          }
        })
      }
    },
    // 'item.pointArray': {
    //   handler (newV) {
    //     console.log('point', newV)
    //   },
    //   deep: true
    // },
    'item.showPOI': function (newV) {
      this.item.styleJson.forEach(element => {
        if (element.featureType === 'poilabel') {
          element.stylers.visibility = newV
        }
      })
      this.map.setMapStyleV2({
        styleJson: this.item.styleJson
      })
    },
    'item.landColor': function (newV) {
      this.item.styleJson.forEach(element => {
        if (
          element.featureType === 'land' &&
          element.elementType === 'geometry'
        ) {
          element.stylers.color = this.getHexColor(newV) + 'ff'
        }
      })
      this.map.setMapStyleV2({
        styleJson: this.item.styleJson
      })
    },
    'item.roadColor': function (newV) {
      this.item.styleJson.forEach(element => {
        if (
          (element.featureType === 'highway' &&
            element.elementType === 'geometry.fill') ||
          (element.featureType === 'cityhighway' &&
            element.elementType === 'geometry.fill') ||
          (element.featureType === 'fourlevelway' &&
            element.elementType === 'geometry.fill') ||
          (element.featureType === 'nationalway' &&
            element.elementType === 'geometry.fill') ||
          (element.featureType === 'provincialway' &&
            element.elementType === 'geometry.fill') ||
          (element.featureType === 'tertiaryway' &&
            element.elementType === 'geometry.fill')
        ) {
          element.stylers.color = this.getHexColor(newV) + 'ff'
        }
      })
      this.map.setMapStyleV2({
        styleJson: this.item.styleJson
      })
    },
    'item.oceanColor': function (newV) {
      this.item.styleJson.forEach(element => {
        if (
          element.featureType === 'water' &&
          element.elementType === 'geometry'
        ) {
          element.stylers.color = this.getHexColor(newV) + 'ff'
        }
      })
      console.log(this.item.styleJson)
      this.map.setMapStyleV2({
        styleJson: this.item.styleJson
      })
    },
    'item.mapTextColor': function (newV) {
      this.item.styleJson.forEach(element => {
        if (element.elementType === 'labels.text.fill') {
          element.stylers.color = this.getHexColor(newV) + 'ff'
        }
      })
      this.map.setMapStyleV2({
        styleJson: this.item.styleJson
      })
    }
  },
  mounted () {
    this.id = this.id + Math.round(Math.random() * 100000) // 百度地图通过id绑定DOM，设置每次产生的id不同
    this.inputId = this.inputId + Math.round(Math.random() * 100000)
    this.$nextTick(() => {
      this.initMap()
    })
  },
  created () {
    // console.log(this.item)
  },
  methods: {
    // 将rgba转化为十六进制颜色
    getHexColor (color) {
      var values = color
        .replace(/rgba?\(/, '')
        .replace(/\)/, '')
        .replace(/[\s+]/g, '')
        .split(',')
      let a = parseFloat(values[3] || 1)
      let r = Math.floor(a * parseInt(values[0]) + (1 - a) * 255)
      let g = Math.floor(a * parseInt(values[1]) + (1 - a) * 255)
      let b = Math.floor(a * parseInt(values[2]) + (1 - a) * 255)
      return (
        '#' +
        ('0' + r.toString(16)).slice(-2) +
        ('0' + g.toString(16)).slice(-2) +
        ('0' + b.toString(16)).slice(-2)
      )
    },
    initMap () {
      this.createMap()
      this.setMapEvent()
      // this.addMapControl(this.item.chartData)
      this.addPoint()
      let _this = this
      window.onclick = function (e) {
        if (_this.delLabel && e.target !== document.getElementById('del')) {
          _this.map.removeOverlay(_this.delLabel)
          _this.delLabel = ''
        }
      }
    },
    createMap () {
      let _this = this
      this.map = new window.BMap.Map(this.id) // 在百度地图容器中创建一个地图
      const point = new window.BMap.Point(116.404, 39.915) // 定义一个中心点坐标
      this.map.centerAndZoom(point, 7) // 设定地图的中心点和坐标并将地图显示在地图容器中
      if (this.item.pointArray.length > 0) {
        for (let i = 0; i < this.item.pointArray.length; i++) {
          let p = new window.BMap.Point(
            this.item.pointArray[i].lng,
            this.item.pointArray[i].lat
          )
          let m = new window.BMap.Marker(p, {
            icon:
              this.item.pointArray[i].icon.indexOf('类型3') !== -1 ||
              this.item.pointArray[i].icon.indexOf('类型4') !== -1
                ? new window.BMap.Icon(
                  this.item.pointArray[i].icon,
                  new window.BMap.Size(80, 80)
                )
                : this.item.pointArray[i].icon.indexOf('marker_red_sprite') !==
                  -1
                  ? new window.BMap.Icon(
                    this.item.pointArray[i].icon,
                    new window.BMap.Size(39, 25)
                  )
                  : this.item.pointArray[i].icon.indexOf('类型1') !== -1
                    ? new window.BMap.Icon(
                      this.item.pointArray[i].icon,
                      new window.BMap.Size(30, 30)
                    )
                    : new window.BMap.Icon(
                      this.item.pointArray[i].icon,
                      new window.BMap.Size(40, 40)
                    )
          })
          this.map.addOverlay(m)
          if (
            _this.$route.name === 'edit' &&
            !_this.$parent.$parent.previewStatus
          ) {
            m.addEventListener('rightclick', function (e) {
              // 点标记右键事件删除
              let html = `<p id="del" style="z-index:1000;cursor:pointer;text-align:center;line-height:30px">删除</p>`
              let opt = {
                position: p
              }
              let label = new window.BMap.Label(html, opt)
              label.setStyle({
                color: '#cad6dd',
                width: '60px',
                background: '#2d3c4c',
                borderColor: 'transparent',
                height: '30px',
                // fontSize: '16px',
                padding: '0 0 5px 0',
                lineHeight: '30px',
                fontFamily: '微软雅黑'
              })
              if (_this.delLabel) {
                _this.map.removeOverlay(_this.delLabel)
              }
              _this.delLabel = label
              _this.map.addOverlay(label)
              document.getElementById('del').onclick = function () {
                _this.map.removeOverlay(e.currentTarget)
                _this.item.pointArray.forEach((element, index) => {
                  if (
                    e.currentTarget.point.lng === element.lng &&
                    e.currentTarget.point.lat === element.lat
                  ) {
                    _this.item.pointArray.splice(index, 1)
                  }
                })
                _this.map.removeOverlay(label)
                _this.delLabel = ''
              }
            })
          }
        }
      }
      if (this.item.textArray.length > 0) {
        for (let i = 0; i < this.item.textArray.length; i++) {
          let opts = {
            position: new window.BMap.Point(
              this.item.textArray[i].lng,
              this.item.textArray[i].lat
            ) // 指定文本标注所在的地理位置
          }
          let label = new window.BMap.Label(this.item.textArray[i].value, opts)
          label.setStyle({
            color: 'blue',
            borderRadius: '5px',
            borderColor: '#ccc',
            fontSize: '16px',
            padding: '0 5px',
            height: '30px',
            lineHeight: '30px',
            fontFamily: '微软雅黑'
          })
          this.map.addOverlay(label)
          if (
            _this.$route.name === 'edit' &&
            !_this.$parent.$parent.previewStatus
          ) {
            label.addEventListener('rightclick', function (e) {
              // 文本标注右键事件删除
              let html = `<p id="del" style="z-index:1000;cursor:pointer;text-align:center;line-height:30px">删除</p>`
              let opt = {
                position: new window.BMap.Point(
                  this.item.textArray[i].lng,
                  this.item.textArray[i].lat
                ),
                offset: new window.BMap.Size(30, 30)
              }
              let delLabel = new window.BMap.Label(html, opt)
              delLabel.setStyle({
                color: '#cad6dd',
                width: '60px',
                background: '#2d3c4c',
                borderColor: 'transparent',
                height: '30px',
                // fontSize: '16px',
                padding: '0 0 5px 0',
                lineHeight: '30px',
                fontFamily: '微软雅黑'
              })
              if (_this.delLabel) {
                _this.map.removeOverlay(_this.delLabel)
              }
              _this.delLabel = delLabel
              _this.map.addOverlay(delLabel)
              document.getElementById('del').onclick = function () {
                _this.map.removeOverlay(e.currentTarget)
                _this.item.textArray.forEach((element, index) => {
                  if (
                    e.currentTarget.point.lng === element.lng &&
                    e.currentTarget.point.lat === element.lat
                  ) {
                    _this.item.textArray.splice(index, 1)
                  }
                })
                _this.map.removeOverlay(delLabel)
                _this.delLabel = ''
              }
            })
          }
        }
      }

      function MessageControl () {
        // 默认停靠位置和偏移量
        this.defaultAnchor = BMAP_ANCHOR_TOP_RIGHT
        this.defaultOffset = new window.BMap.Size(10, 10)
      }

      // 通过JavaScript的prototype属性继承于BMap.Control
      MessageControl.prototype = new window.BMap.Control()

      // 自定义控件必须实现自己的initialize方法,并且将控件的DOM元素返回
      // 在本方法中创建个div元素作为控件的容器,并将其添加到地图容器中
      MessageControl.prototype.initialize = function (map) {
        // 创建一个DOM元素
        let div = document.createElement('div')
        if (_this.item.mapType === '卫星') {
          div.innerHTML = `<div class="twoD" style="border-radius:2px 0 0 2px;width:50%;text-align:center;line-height:30px">2D</div><div class="satellite buttonActive" style="border-radius:0 2px 2px 0;width:50%;text-align:center;line-height:30px">卫星</div>`
        } else {
          div.innerHTML = `<div class="twoD buttonActive" style="border-radius:2px 0 0 2px;width:50%;text-align:center;line-height:30px">2D</div><div class="satellite" style="border-radius:0 2px 2px 0;width:50%;text-align:center;line-height:30px">卫星</div>`
        }
        // 设置样式
        div.style.height = '30px'
        div.style.width = '80px'
        div.style.background = 'rgba(23, 44, 102, 0.9)'
        div.style.color = '#fff'
        div.style.display = 'flex'
        div.style.cursor = 'pointer'
        // 绑定事件,点击一次放大两级
        div.onclick = function (e) {
          document
            .querySelector('#' + _this.id + ' .twoD')
            .classList.remove('buttonActive')
          document
            .querySelector('#' + _this.id + ' .satellite')
            .classList.remove('buttonActive')
          console.log('e', e)
          e.target.classList.add('buttonActive')
          if (e.target.innerText === '2D') {
            _this.item.mapType = '2D'
            _this.map.setMapType(BMAP_NORMAL_MAP)
          } else {
            _this.item.mapType = '卫星'
            _this.map.setMapType(BMAP_HYBRID_MAP)
          }
        }
        // 添加DOM元素到地图中
        map.getContainer().appendChild(div)
        // 将DOM元素返回
        return div
      }

      // 创建控件
      let messageCtrl = new MessageControl()

      // 添加到地图当中
      this.map.addControl(messageCtrl)

      // bmap.addControl(new BMap.MapTypeControl());  默认创建地图的方式是三种都选
    },
    setMapEvent () {
      this.map.enableDragging() // 启用地图拖拽事件，默认启用(可不写)
      this.map.enableScrollWheelZoom() // 启用地图滚轮放大缩小
      this.map.enableDoubleClickZoom() // 启用鼠标双击放大，默认启用(可不写)
      this.map.enableKeyboard() // 启用键盘上下左右键移动地图
      this.map.setMapStyleV2({
        styleId: 'a8a122804c7d2f4c15bf04aba0956cbf'
      })
      if (this.item.mapType === '2D') {
        this.map.setMapType(BMAP_NORMAL_MAP)
      } else if (this.item.mapType === '卫星') {
        this.map.setMapType(BMAP_HYBRID_MAP)
      }
      // var controler = new window.BMap.MapTypeControl({
      //   mapTypes: [
      //     BMAP_NORMAL_MAP,
      //     BMAP_HYBRID_MAP
      //   ]
      // })
      // this.map.addControl(controler)
      // this.map.addEventListener('maptypechange', () => {
      //   if (this.item.mapType === '2D') {
      //     this.item.mapType = '卫星'
      //   } else {
      //     this.item.mapType = '2D'
      //   }
      // })
    },
    addMapControl (list) {
      console.log(this.item.mapTypeID)
      if (list) {
        let gatewayList = []
        let getindexList = []
        for (let i = 0; i < list.length; i++) {
          for (let j = 1; j < list.length; j++) {
            if (list[i].station.id === list[j].station.id && i !== j) {
              getindexList.push(j)
              gatewayList.push({
                iot: list[i].iot,
                name: list[i].name,
                num: list[i].num
              })
            }
          }
        }
        // for (let i = 0; i < getindexList.length; i++) {
        //   list.splice(getindexList[i], 1)
        // }
        // <div style="line-height: 2.0em">
        //           <i class="el-icon-reading"></i>
        //             网关：<span style="font-weight:600;">` +
        //       item.num +
        //       `</span>
        //             &nbsp;
        //             <i class="el-icon-reading"></i>
        //             绑定设备数：<span style="font-weight:600;" onclick="visit('` +
        //       item.station.id +
        //       `')">` +
        //       item.devices.length +
        //       `</span>
        //           </div>
        let stringDivlist = []
        list.forEach((element, index) => {
          let stringDiv = ''
          if (getindexList.indexOf(index) === -1) {
            stringDiv =
              `
            <div style="line-height: 2.0em">
                  <i class="el-icon-reading"></i>
                    网关：<span style="font-weight:600;">` +
              element.num +
              `</span>
                    &nbsp;
                    <i class="el-icon-reading"></i>
                    绑定设备数：<span style="font-weight:600;" onclick="visit('` +
              element.station.id +
              `')">` +
              element.devices.length +
              `</span>
            </div>`
          } else {
            console.log(gatewayList)
            for (let i = 0; i < gatewayList.length; i++) {
              stringDiv +=
                `
            <div style="line-height: 2.0em">
                  <i class="el-icon-reading"></i>
                    网关：<span style="font-weight:600;">` +
                gatewayList[i].num +
                `</span>
                    &nbsp;
                    <i class="el-icon-reading"></i>
                    绑定设备数：<span style="font-weight:600;" onclick="visit('` +
                element.station.id +
                `')">` +
                element.devices.length +
                `</span>
            </div>`
            }
            stringDiv +=
              `<div style="line-height: 2.0em">
                  <i class="el-icon-reading"></i>
                    网关：<span style="font-weight:600;">` +
              element.num +
              `</span>
                    &nbsp;
                    <i class="el-icon-reading"></i>
                    绑定设备数：<span style="font-weight:600;" onclick="visit('` +
              element.station.id +
              `')">` +
              element.devices.length +
              `</span>
            </div>`
          }
          stringDivlist.push(stringDiv)
        })
        // for (let i = 0; i < getindexList.length; i++) {
        //     list.splice(getindexList[i], 1)
        // }
        for (let index = 0; index < list.length; index++) {
          const item = list[index]
          const str = stringDivlist[index]
          const coordinate = item.station.coordinate.split(',')
          // const untreated = parseInt(item.untreatedWarnCount) || 0
          // const processed = parseInt(item.warnCount) - untreated
          window.lastInfoBox = null
          const point = new window.BMap.Point(coordinate[1], coordinate[0])
          const marker = new window.BMap.Marker(point) // 创建标注
          this.map.addOverlay(marker) // 将标注添加到地图中
          this.map.centerAndZoom(point, 6)
          marker.addEventListener('mouseover', function () {
            const html =
              `<div class='infoBoxContent'>
                <a target="_blank" href="/#/site-statistics/index?id=` +
              item.station.id +
              `"><h3 style="font-weight:600; line-height: 2.0em">站点：` +
              item.station.name +
              `</h3></a>
              <div>` +
              //  <!--<div class="baojingText" style="font-size:14px;color:white;margin-top:22px;">
              //      <span style="font-weight:600;">告警累计</span>
              //      <span style="font-weight:600;">未处理</span>
              //      <span style="font-weight:600;">已处理</span>
              //    </div>
              //    '<div class="baojingnum">
              //        <p>
              //          <span style="width: auto; padding: 0 10px; border-radius:5px;margin-top:2px;font-size:14px;">` +

              // item.warnCount +
              // `</span>
              //       </p>
              //       <p>
              //         <span style="width: auto; padding: 0 10px; border-radius:5px;margin-top:2px;font-size:14px;">` +
              // untreated +
              // `</span>
              //       </p>
              //       <p>
              //         <span style="width: auto; padding: 0 10px; border-radius:5px;margin-top:2px;font-size:14px;">` +
              // processed +
              // `</span>
              //       </p>
              //   </div>
              `<div class="card-info" style="height: auto;font-size:14px;">
                  <div style="line-height: 2.0em">
                  <i class="el-icon-guide"></i>
                    负责人：<span style="font-weight:600;">` +
              item.station.leader +
              `</span>
                  </div>
                  <div style="line-height: 2.0em">
                  <i class="el-icon-reading"></i>
                    类型：<span style="font-weight:600;">` +
              (item.station.typeName == null ? '' : item.station.typeName) +
              `</span>
                  </div>
                  <div style="line-height: 2.0em">
                  <i class="el-icon-position"></i>
                    地址：<span style="font-weight:600;" title="` +
              item.station.location +
              `">` +
              (item.station.location.length > 15
                ? item.station.location.substring(0, 15) + '...'
                : item.station.location) +
              `</span>
              </div>` +
              str +
              `</div>`
            const infoBox = new window.BMapLib.InfoBox(this.map, html, {
              boxStyle: {
                opacity: '0.8',
                background:
                  'url(' +
                  //   apiUrl.getImg +
                  '/leaderview/home/getImg/false/51) no-repeat 100% 100%',
                backgroundSize: 'cover',
                width: '357px',
                height: '230px'
              },
              closeIconUrl: '/leaderview/home/getImg/false/52', // apiUrl.getImg +
              closeIconMargin: '10px 10px 10px 0',
              enableAutoPan: false
            })
            infoBox.open(marker)
            // eslint-disable-next-line no-undef
            if (lastInfoBox) {
              // eslint-disable-next-line no-undef
              lastInfoBox.close()
            }
            // eslint-disable-next-line no-undef
            lastInfoBox = infoBox
            const appDiv = document.getElementById('app')
            // infoBox.uid = indexedDB
            appDiv.addEventListener('click', function () {
              infoBox.close()
            })
          })
        }
      }
    },
    addPoint () {
      var menu = new window.BMap.ContextMenu()
      var _this = this
      var txtMenuItem = [
        {
          text: '添加点标记',
          callback: function (val) {
            //   var myIcon = new window.BMap.Icon("../../../../static/img/图标.gif", new window.BMap.Size(52, 26));
            let point1 = new window.BMap.Point(val.lng, val.lat)
            let marker1 = new window.BMap.Marker(point1, {
              icon:
                _this.item.markerType.indexOf('类型3') !== -1 ||
                _this.item.markerType.indexOf('类型4') !== -1
                  ? new window.BMap.Icon(
                    _this.item.markerType,
                    new window.BMap.Size(80, 80)
                  )
                  : _this.item.markerType.indexOf('marker_red_sprite') !== -1
                    ? new window.BMap.Icon(
                      _this.item.markerType,
                      new window.BMap.Size(39, 25)
                    )
                    : _this.item.markerType.indexOf('类型1') !== -1
                      ? new window.BMap.Icon(
                        _this.item.markerType,
                        new window.BMap.Size(30, 30)
                      )
                      : new window.BMap.Icon(
                        _this.item.markerType,
                        new window.BMap.Size(40, 40)
                      )
            })
            _this.item.pointArray.push({
              lng: val.lng,
              lat: val.lat,
              icon: _this.item.markerType
            })
            _this.map.addOverlay(marker1)
            if (
              _this.$route.name === 'edit' &&
              !_this.$parent.$parent.previewStatus
            ) {
              marker1.addEventListener('rightclick', function (e) {
                // 点标记右键事件删除
                let html = `<p id="del" style="z-index:1000;cursor:pointer;text-align:center;line-height:30px">删除</p>`
                let opt = {
                  position: point1
                }
                let label = new window.BMap.Label(html, opt)
                label.setStyle({
                  color: '#cad6dd',
                  width: '60px',
                  background: '#2d3c4c',
                  borderColor: 'transparent',
                  height: '30px',
                  // fontSize: '16px',
                  padding: '0 0 5px 0',
                  lineHeight: '30px',
                  fontFamily: '微软雅黑'
                })
                if (_this.delLabel) {
                  _this.map.removeOverlay(_this.delLabel)
                }
                _this.delLabel = label
                _this.map.addOverlay(label)
                document.getElementById('del').onclick = function () {
                  _this.map.removeOverlay(e.currentTarget)
                  _this.item.pointArray.forEach((element, index) => {
                    if (
                      e.currentTarget.point.lng === element.lng &&
                      e.currentTarget.point.lat === element.lat
                    ) {
                      _this.item.pointArray.splice(index, 1)
                    }
                  })
                  _this.map.removeOverlay(label)
                  _this.delLabel = label
                }
                // let pointWindow = new window.BMap.InfoBox(sContent, {
                //   width: 0,
                //   height: 0
                // }) // 创建信息窗口对象
                // let poi = new window.BMap.Point(e.currentTarget.point.lng, e.currentTarget.point.lat)
                // _this.map.openInfoWindow(pointWindow, poi)
                // if (!pointWindow.isOpen()) {
                //   pointWindow.addEventListener('open', () => {
                //     document.getElementById('delete').onclick = function () {
                //       _this.map.removeOverlay(e.currentTarget)
                //       _this.item.pointArray.forEach((element, index) => {
                //         if (e.currentTarget.point.lng === element.lng && e.currentTarget.point.lat === element.lat) {
                //           _this.item.pointArray.splice(index, 1)
                //         }
                //       })
                //       _this.map.closeInfoWindow(pointWindow)
                //     }
                //     document.getElementById('cancel').onclick = function () {
                //       _this.map.closeInfoWindow(pointWindow)
                //     }
                //   })
                // } else {
                //   document.getElementById('delete').onclick = function () {
                //     _this.map.removeOverlay(e.currentTarget)
                //     _this.item.pointArray.forEach((element, index) => {
                //       if (e.currentTarget.point.lng === element.lng && e.currentTarget.point.lat === element.lat) {
                //         _this.item.pointArray.splice(index, 1)
                //       }
                //     })
                //     _this.map.closeInfoWindow(pointWindow)
                //   }
                //   document.getElementById('cancel').onclick = function () {
                //     _this.map.closeInfoWindow(pointWindow)
                //   }
                // }
                // _this.map.removeOverlay(e.currentTarget)
                // _this.item.pointArray.forEach((element, index) => {
                //   if (e.currentTarget.point.lng === element.lng && e.currentTarget.point.lat === element.lat) {
                //     _this.item.pointArray.splice(index, 1)
                //   }
                // })
              })
            }
          }
        },
        {
          text: '自定义文本标注',
          callback: function (val) {
            let opts = {
              position: new window.BMap.Point(val.lng, val.lat) // 指定文本标注所在的地理位置
              // offset: new window.BMap.Size(30, -30) // 设置文本偏移量
            }
            let po = new window.BMap.Point(val.lng, val.lat)
            var sContent = `<input id="${_this.inputId}" type="text" autocomplete="off" style="margin-top:15px"/><p style="color:#666666;text-align:right">回车保存,右键删除</p>`
            var infoWindow = new window.BMap.InfoWindow(sContent) // 创建信息窗口对象
            _this.map.openInfoWindow(infoWindow, po)
            var enterEvent = function (event) {
              event.preventDefault()
              event.stopPropagation()
              if (event.keyCode === 13) {
                let label = new window.BMap.Label(
                  document.getElementById(_this.inputId).value,
                  opts
                )
                label.setStyle({
                  color: 'blue',
                  borderRadius: '5px',
                  borderColor: '#ccc',
                  fontSize: '16px',
                  padding: '0 5px',
                  height: '30px',
                  lineHeight: '30px',
                  fontFamily: '微软雅黑'
                })
                _this.map.addOverlay(label)
                _this.map.closeInfoWindow(infoWindow, po)
                _this.item.textArray.push({
                  value: document.getElementById(_this.inputId).value,
                  lng: val.lng,
                  lat: val.lat
                })
                if (
                  _this.$route.name === 'edit' &&
                  !_this.$parent.$parent.previewStatus
                ) {
                  label.addEventListener('rightclick', function (e) {
                    // 文本标注右键事件删除
                    let html = `<p id="del" style="z-index:1000;cursor:pointer;text-align:center;line-height:30px">删除</p>`
                    let opt = {
                      position: new window.BMap.Point(val.lng, val.lat),
                      offset: new window.BMap.Size(30, 30)
                    }
                    let delLabel = new window.BMap.Label(html, opt)
                    delLabel.setStyle({
                      color: '#cad6dd',
                      width: '60px',
                      background: '#2d3c4c',
                      borderColor: 'transparent',
                      height: '30px',
                      // fontSize: '16px',
                      padding: '0 0 5px 0',
                      lineHeight: '30px',
                      fontFamily: '微软雅黑'
                    })
                    if (_this.delLabel) {
                      _this.map.removeOverlay(_this.delLabel)
                    }
                    _this.delLabel = delLabel
                    _this.map.addOverlay(delLabel)
                    document.getElementById('del').onclick = function () {
                      _this.map.removeOverlay(e.currentTarget)
                      _this.item.textArray.forEach((element, index) => {
                        if (
                          e.currentTarget.point.lng === element.lng &&
                          e.currentTarget.point.lat === element.lat
                        ) {
                          _this.item.textArray.splice(index, 1)
                        }
                      })
                      _this.map.removeOverlay(delLabel)
                      _this.delLabel = ''
                    }
                  })
                }
              }
            }
            if (!document.getElementById(_this.inputId)) {
              // 当是第一个信息窗口时获取不到这个dom,需要把监听事件加载父级上
              document
                .getElementById(_this.id)
                .addEventListener('keyup', enterEvent)
            } else {
              document
                .getElementById(_this.id)
                .removeEventListener('keyup', enterEvent)
              document
                .getElementById(_this.inputId)
                .addEventListener('keyup', enterEvent)
            }
          }
        }
      ]
      for (var i = 0; i < txtMenuItem.length; i++) {
        menu.addItem(
          new window.BMap.MenuItem(
            txtMenuItem[i].text,
            txtMenuItem[i].callback,
            100
          )
        )
      }
      if (this.$route.name === 'edit' && !this.$parent.$parent.previewStatus) {
        this.map.addContextMenu(menu)
      }
    }
  }
}
</script>

<style lang="scss">
/*这两段是去掉百度地图水印的css*/
.BMap_cpyCtrl {
  display: none !important;
}
.anchorBL {
  display: none !important;
}
.BMap_contextMenu {
  width: 150px !important;
  height: 60px !important;
  padding: 5px 5px 0 5px !important;
  border: 1px solid transparent !important;
  background: #2d3c4c !important;
  color: #fff !important;
}
.BMap_contextMenu div {
  width: 100% !important;
  height: 50%;
  color: #fff !important;
}
// .BMap_contextMenu div:first-child{
//   margin-bottom: 10px;
// }
// 处理删除按钮会被点标记覆盖的问题
.Boxmap > div:first-child {
  > div:nth-child(2) {
    > div:nth-child(4) {
      z-index: 1000 !important;
    }
  }
}
.BMap_Marker div img {
  width: 100%;
  height: 100%;
  color: #fff !important;
}
#del:hover {
  color: #15aaf5;
}
.buttonActive {
  background-color: rgba(45, 131, 249, 0.9);
}
// .BMap_noprint .anchorTR{
//   >div:first-child{
//     width: 50px !important
//   }
// }
</style>
