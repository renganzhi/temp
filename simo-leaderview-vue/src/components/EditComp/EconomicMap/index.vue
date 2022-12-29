<template>
     <div id="backBox">
        <div  id="cyMap">
        </div>
     </div>
</template>

<script>
import * as turf from '@turf/turf'
import styleJson from './custom_map_config.json'
import Region from './分区.json'
import element from '@/element'
export default {
  data: function () {
    return {
      header: process.env.NODE_ENV === 'development' ? './static/' : './',
      firstLayer: '', // 第一层覆盖物，七大分区
      firstLabel: '',
      secondLayer: '', // 第二层覆盖物，分区下各省份
      centerPoint: '', // 中国地图中心点
      mapView: '',
      dataArray: [],
      RegionInfo: {
        '东北地区': ['黑龙江', '吉林', '辽宁'],
        '华北地区': ['北京', '天津', '河北', '山西', '内蒙古'],
        '华中地区': ['河南', '湖北', '湖南'],
        '华东地区': ['山东', '江苏', '安徽', '上海', '浙江', '江西', '福建', '台湾'],
        '华南地区': ['广东', '广西', '海南', '香港', '澳门'],
        '西北地区': ['陕西', '甘肃', '宁夏', '青海', '新疆'],
        '西南地区': ['四川', '贵州', '云南', '重庆', '西藏']
      },
      colorArr: ['#ff4747', '#00bcb4', '#c4e86b', '#ffb547', '#e1ee32', '#8474a1', '#9b461f']
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
      this.centerPoint = turf.centerOfMass(Region).geometry.coordinates // 定义一个中心点坐标
      this.map.centerAndZoom(new window.BMapGL.Point(this.centerPoint[0], this.centerPoint[1]), 5) // 设定地图的中心点和坐标并将地图显示在地图容器中 104.02959, 30.60570
      this.map.enableScrollWheelZoom(true) // 开启鼠标滚轮缩放
      this.mapView = new window.mapvgl.View({
        map: this.map
      })
      // this.map.setMapStyleV2({styleJson: styleJson})
      // this.map.setOptions({
      //   style: {
      //     styleJson: styleJson
      //   },
      //   styleUrl: 'http://172.16.152.196:8219/baidumap/bmapgl/mapstyle/new_mapStyle.json'
      // })
      // this.map.setDisplayOptions({
      //   poiText: false
      // })
      // this.getFQ()
    },
    getFQ () {
      console.log('re', Region)
      Region.features.forEach(element => {
        console.log('center', turf.centroid(element))
        let lng = turf.centroid(element).geometry.coordinates[0]
        let lat = turf.centroid(element).geometry.coordinates[1]
        this.dataArray.push({
          geometry: {
            coordinates: [lng, lat],
            type: 'Point'
          },
          properties: {
            text: element.properties.name
          }
        })
      })
      this.firstLayer = new window.mapvgl.PolygonLayer({
        data: Region.features,
        lineJoin: 'round',
        fillColor: (param) => {
          return this.colorArr[param.properties.index]
        },
        // selectedColor: 'rgba(255,255,0,0.7)',
        enablePicked: true,
        onClick: (e) => {
          if (e.dataItem) {
            $.getJSON(this.header + 'geojson/经济分区/' + e.dataItem.properties.name + '.geojson', res => {
              console.log('res', res)
              this.secondLayer = new window.mapvgl.PolygonLayer({
                data: res.features,
                fillColor: this.colorArr[e.dataIndex],
                enablePicked: true,
                onClick: (e2) => {
                  console.log('event2', e2)
                  if (!e2.dataItem) {
                    this.mapView.removeLayer(this.secondLayer)
                    this.mapView.addLayer(this.firstLayer)
                    this.mapView.addLayer(this.firstLabel)
                  }
                }
              })
              this.mapView.removeLayer(this.firstLayer)
              this.mapView.removeLayer(this.firstLabel)
              this.mapView.addLayer(this.secondLayer)
            })
          }
          console.log('event', e, event)
        }
      })
      this.firstLabel = new window.mapvgl.TextLayer({
        color: '#4e4b4b',
        fontSize: 22
      })
      this.mapView.addLayer(this.firstLayer)
      this.mapView.addLayer(this.firstLabel)
      this.firstLabel.setData(this.dataArray)
    },
    // 绘制分区
    getDQ () {
      this.firstLayer = new window.BMapGL.GeoJSONLayer('china', {
        dataSource: Region,
        polygonStyle: (properties) => {
          let index = properties.index || 0
          return {
            fillColor: this.colorArr[index]
          }
        }
      })
      this.firstLayer.addEventListener('click', (event1) => {
        if (event1.features) {
          this.firstLayer.setVisible(false)
          $.getJSON(this.header + 'geojson/经济分区/' + event1.features[0].properties.name + '.geojson', res => {
            this.secondLayer && this.map.removeGeoJSONLayer(this.secondLayer)
            this.secondLayer = new window.BMapGL.GeoJSONLayer(event1.features[0].properties.name, {
              dataSource: res,
              polygonStyle: {
                fillColor: this.colorArr[event1.features[0].properties.index]
              }
            })
            this.map.addGeoJSONLayer(this.secondLayer)
            this.secondLayer.addEventListener('click', event2 => {
              if (!event2.features) {
                this.firstLayer.setVisible(true)
                this.map.removeGeoJSONLayer(this.secondLayer)
                this.map.centerAndZoom(new window.BMapGL.Point(this.centerPoint[0], this.centerPoint[1]), 5)
              }
            })
          })
          this.map.centerAndZoom(new window.BMapGL.Point(event1.latLng.lng, event1.latLng.lat), 6)
        }
      })
      this.map.addGeoJSONLayer(this.firstLayer)
    },
    getLabel () {

    },
    getMapCenter () {
      var cen = this.map.getCenter() // 获取地图中心点
      alert('地图中心点: (' + cen.lng.toFixed(5) + ', ' + cen.lat.toFixed(5) + ')' + '层级：' + this.map.getZoom())
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
