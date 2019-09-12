<template>
  <div>
    <div :id="'map_'+item.id"
         :key="item.id"
         style="width: 500px; height: 500px;"
         class="chart v-map"></div>
  </div>

</template>
<script>
// 中国地图（第一级地图）的ID、Name、Json数据
import cityMap from '#/libs/map/china-main-city-map'
// import echarts from 'echarts'
// import a from './../../../../static/libs/map'
var chinaId = 100000 // 100000
var chinaName = '四川' // 'china'
var chinaJson = null
// 记录父级ID、Name
var mapStack = []
var parentId = null
var parentName = null
var myChart = null
export default {
  name: 'vmap',
  props: ['item'],
  data () {
    return {
      geoCoordMap: []
    }
  },
  methods: {
    mapChart () {
      var divid = 'map_' + this.item.id
      this.axios.get('./../../../../static/libs/map/' + chinaId + '.json', {}).then(response => {
        const mapJson = response
        // console.log(response)
        chinaJson = mapJson
        myChart = echarts.init(document.getElementById(divid))
        this.registerAndsetOption(myChart, chinaId, chinaName, mapJson, false)
        parentId = chinaId
        parentName = 'china'
        var _this = this
        myChart.on('click', function (param) {
          console.log(divid)
          var cityId = cityMap[param.name]
          if (cityId) {
            // 代表有下级地图
            _this.axios
              .get('./../../../../static/libs/map/' + cityId + '.json', {})
              .then(response => {
                const mapJson = response
                _this.registerAndsetOption(
                  myChart,
                  cityId,
                  param.name,
                  mapJson,
                  true
                )
              })
          } else {
            // 没有下级地图，回到一级中国地图，并将mapStack清空
            _this.registerAndsetOption(myChart, chinaId, chinaName, chinaJson, false)
            mapStack = []
            parentId = chinaId
            parentName = chinaName
          }
          // $.get('./asset/json/map/'+param.data.id+'.json', function (mapJson,status) {
          //     this.registerAndsetOption(myChart,param.data.id,param.name,mapJson,true)
          // }).fail(function () {
          //     this.registerAndsetOption(myChart,chinaId,'china',chinaJson,false)
          // });
        })
      })
    },
    /**
   *
   * @param {*} myChart
   * @param {*} id        省市县Id
   * @param {*} name      省市县名称
   * @param {*} mapJson   地图Json数据
   * @param {*} flag      是否往mapStack里添加parentId，parentName
   */
    registerAndsetOption (myChart, id, name, mapJson, flag) {
      console.log(myChart)
      console.log(name)
      console.log(id)
      console.log('-------mapJson-----')
      console.log(mapJson)
      echarts.registerMap(name, mapJson)
      myChart.setOption({
        visualMap: {
          type: 'piecewise', // 分段显示值
          realtime: false,
          calculable: true,
          // backgroundColor: 'gray' // 柱状框的背景色
          inRange: {
            color: ['pink', 'yellow', '#dd7e6b'] // 按照值的范围给的不同颜色
          },
          outOfRange: {
            // color: ['pink', 'yellow', 'orange']
          },
          // piecewise分段设置 https://echarts.apache.org/zh/option.html#visualMap-piecewise.pieces
          splitNumber: 3, // 几种颜色值及取值范围
          pieces: [
            { min: 1000, label: '自定义', color: 'orange' },
            { min: 100, max: 999 },
            { max: 99 }
          ], // 默认取data中最后一个维度
          color: ['#E0022B', '#E09107', '#A3E00B'],
          // itemSymbol: 'none',
          show: true, // 是否显示取值范围颜色段
          hoverLink: true,
          // showMaxLabel: true,
          // text: ['高', '低'], // 文本，默认为数值文本
          showLabel: true,
          textStyle: {
            color: '#fff'
          },
          controller: {
          }
        },
        series: [
          {
            type: 'map',
            map: name,
            // itemStyle: {
            //   normal: {
            //     areaColor: 'rgba(23, 27, 57,0)',
            //     borderColor: '#1dc199',
            //     borderWidth: 1
            //   }
            // },
            itemStyle: {
              normal: {
                // color: 'red', // 展示指标及圆点的颜色
                areaColor: '#294671', // 地图区域的颜色!
                borderColor: '#f0f0f0' // 区域分割线颜色!
                // color: 'green', // 图例的颜色!
                // borderColor: 'pink', // 各区域分界线!
                // borderWidth: 2
              }
            },
            data: this.initMapData(mapJson)
          },
          {
            type: 'map',
            map: name,
            name: 'test',
            // label: {
            //   normal: {
            //     show: false
            //   },
            //   emphasis: {
            //     // show: true
            //     show: false
            //   }
            // },
            data: [
              { name: '北京', value: Math.round(Math.random() * 1000) },
              { name: '天津', value: Math.round(Math.random() * 1000) },
              { name: '上海', value: Math.round(Math.random() * 1000) },
              { name: '重庆', value: Math.round(Math.random() * 1000) },
              { name: '河北', value: Math.round(Math.random() * 1000) },
              { name: '安徽', value: Math.round(Math.random() * 1000) },
              { name: '新疆', value: Math.round(Math.random() * 1000) },
              { name: '浙江', value: Math.round(Math.random() * 1000) },
              { name: '江西', value: Math.round(Math.random() * 1000) },
              { name: '山西', value: Math.round(Math.random() * 1000) },
              { name: '内蒙古', value: Math.round(Math.random() * 1000) },
              { name: '吉林', value: Math.round(Math.random() * 1000) },
              { name: '福建', value: Math.round(Math.random() * 1000) },
              { name: '广东', value: Math.round(Math.random() * 1000) },
              { name: '西藏', value: Math.round(Math.random() * 1000) },
              { name: '四川', value: Math.round(Math.random() * 1000) },
              { name: '宁夏', value: Math.round(Math.random() * 1000) },
              { name: '香港', value: Math.round(Math.random() * 1000) },
              { name: '澳门', value: Math.round(Math.random() * 1000) }
            ]
          }
        ]
      })
      if (flag) {
        // 往mapStack里添加parentId，parentName,返回上一级使用
        mapStack.push({
          mapId: parentId,
          mapName: parentName
        })
        parentId = id
        parentName = name
      }
    },
    initMapData (mapJson) {
      var mapData = []
      for (var i = 0; i < mapJson.features.length; i++) {
        mapData.push({
          name: mapJson.features[i].properties.name
          // id:mapJson.features[i].id
        })
      }
      console.log('-------mapData-----')
      console.log(mapData)
      return mapData
    },
    convertData (data) {
      var res = []
      for (var i = 0; i < data.length; i++) {
        var geoCoord = this.geoCoordMap[data[i].name]
        if (geoCoord) {
          res.push({
            name: data[i].name,
            value: geoCoord.concat(data[i].value)
          })
        }
      }
      return res
    }
  },
  mounted () {
    // this.init()
    this.mapChart()
  },
  computed: {

  },
  watch: {

  },
  destoryed: function () {
  }
}
</script>
