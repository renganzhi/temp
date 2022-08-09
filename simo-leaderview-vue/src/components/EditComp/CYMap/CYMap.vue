<template>
     <div id="backBox">
        <div  id="cyMap">
        </div>
     </div>
</template>

<script>
import * as turf from '@turf/turf'
import styleJson from './custom_map_config.json'
export default {
  data: function () {
    return {
      header: process.env.NODE_ENV === 'development' ? './static/' : './'
    }
  },
  props: ['nowPageName'],
  watch: {

  },
  methods: {
    initMap () {
      this.map = new window.BMapGL.Map('cyMap')
      const centerPoint = new window.BMapGL.Point(104.01550, 30.61490) // 定义一个中心点坐标
      this.map.centerAndZoom(centerPoint, 12) // 设定地图的中心点和坐标并将地图显示在地图容器中 104.02959, 30.60570
      this.map.enableScrollWheelZoom(true) // 开启鼠标滚轮缩放
      this.map.setMapStyleV2({styleJson: styleJson})
      this.getWHQ()
    },
    // 绘制武侯区
    getWHQ () {
      $.getJSON(this.header + 'geojson/xzqh.json', res => {
        console.log('res', res)
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
                //   this.$parent.ShowTablePop({
                //     name: data.properties.Name
                //   })
                // })
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
              //   this.$parent.ShowTablePop({
              //     name: data.properties.Name
              //   })
              // })
              this.map.addOverlay(polygon)
            }
            let pointer = turf.centerOfMass(data.geometry).geometry.coordinates
            if (data.properties.Name === '金花桥街道') {
              pointer = [103.97374548683935, 30.591885280709842]
            }
            console.log('name', data.properties.Name, data.properties.Name.length)
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
    }
  },
  mounted () {
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
