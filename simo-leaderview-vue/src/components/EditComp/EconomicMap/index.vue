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
      header: process.env.NODE_ENV === 'development' ? './static/' : './'
    }
  },
  props: ['nowPageName'],
  watch: {
    nowPageName: function () {
      if (this.nowPageName.indexOf('32:9') >= 0) {
        let centerPoint = new window.BMapGL.Point(104.01273, 30.62180)
        this.map.centerAndZoom(centerPoint, 13.5)
      } else {
        let centerPoint = new window.BMapGL.Point(104.20081, 30.59718) // 定义一个中心点坐标
        this.map.centerAndZoom(centerPoint, 13.39) // 设定地图的中心点和坐标并将地图显示在地图容器中 104.02959, 30.60570
      }
    }
  },
  methods: {
    initMap () {
      this.map = new window.BMapGL.Map('cyMap')
      if (this.nowPageName.indexOf('32:9') >= 0) {
        let centerPoint = new window.BMapGL.Point(104.01273, 30.62180)
        this.map.centerAndZoom(centerPoint, 13.5)
      } else {
        let centerPoint = new window.BMapGL.Point(104.20081, 30.59718) // 定义一个中心点坐标
        this.map.centerAndZoom(centerPoint, 13.39) // 设定地图的中心点和坐标并将地图显示在地图容器中 104.02959, 30.60570
      }
      this.map.enableScrollWheelZoom(true) // 开启鼠标滚轮缩放
      this.map.setMapStyleV2({styleJson: styleJson})
      // this.map.setOptions({
      //   style: {
      //     styleJson: styleJson
      //   },
      //   styleUrl: 'http://172.16.152.196:8219/baidumap/bmapgl/mapstyle/new_mapStyle.json'
      // })
      this.map.setDisplayOptions({
        poiText: false
      })
    },
    // 绘制武侯区
    getMapCenter () {
      var cen = this.map.getCenter() // 获取地图中心点
      alert('地图中心点: (' + cen.lng.toFixed(5) + ', ' + cen.lat.toFixed(5) + ')' + '层级：' + this.map.getZoom())
    },
    // 清除覆盖物
    removeOverlay () {
      this.map.clearOverlays()
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
