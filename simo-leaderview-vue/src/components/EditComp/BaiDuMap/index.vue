<template>
    <div
      id="Boxmap"
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
    return {}
  },
  watch: {
    'item.chartData': function (newV, oldV) {
      this.initMap()
    }
  },
  mounted () {
    this.$nextTick(() => {
      this.initMap()
    })
  },
  created () {
    // console.log(this.item)
  },
  methods: {
    initMap () {
      this.createMap()
      this.setMapEvent()
      this.addMapControl(this.item.chartData)
    },
    createMap () {
      this.map = new window.BMap.Map('Boxmap', { minZoom: 6, maxZoom: 31 }) // 在百度地图容器中创建一个地图
      const point = new window.BMap.Point(105.46372, 34.624337) // 定义一个中心点坐标
      this.map.centerAndZoom(point, 6) // 设定地图的中心点和坐标并将地图显示在地图容器中
    },
    setMapEvent () {
      this.map.enableDragging() // 启用地图拖拽事件，默认启用(可不写)
      this.map.enableScrollWheelZoom() // 启用地图滚轮放大缩小
      this.map.enableDoubleClickZoom() // 启用鼠标双击放大，默认启用(可不写)
      this.map.enableKeyboard() // 启用键盘上下左右键移动地图
      this.map.setMapStyleV2({
        styleId: 'b36533adfda5fe951af60644c0457f70'
      })
    },
    addMapControl (list) {
      this.map.setMapStyleV2({
        styleId: 'b36533adfda5fe951af60644c0457f70'
      })
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
              closeIconUrl: '/leaderviewWeb/home/getImg/false/52', // apiUrl.getImg +
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
    }
  }
}
</script>

<style>
</style>
