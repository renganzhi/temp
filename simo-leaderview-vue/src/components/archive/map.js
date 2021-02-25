import {morenData} from '@/views/Edit/chartJson'
import DataFlow from '@/components/EditComp/DataFlow/config.js'

const map = {
  name: '地图',
  child: [
    {
      text: '区域分布图',
      imgClass: 'icon-n-areaMap',
      chartType: 'v-map',
      width: 300,
      height: 300,
      mapLevel: 'country',
      countryCode: 100000,
      provinceCode: '',
      cityCode: '',
      visualPosition: 'left',
      fontSize: 10,
      cityShow: false,
      ctLegendColor: '#fff',
      areaColor: '#121a33',
      borderColor: '#38597b',
      cityColor: '#828bac',
      themeType: '1', // 1深色 2浅色
      piecesData: [
        { min: 0, max: 50 },
        { min: 51, max: 100 },
        { min: 101 }
      ],
      chartData: morenData.map
    },
    {
      text: '地图实时图',
      imgClass: 'icon-n-scatterMap',
      chartType: 'v-scatter', // 散点图
      width: 300,
      height: 300,
      mapLevel: 'country',
      countryCode: 100000,
      ctLegendShow: 'false', // 地名是否展示
      provinceCode: '',
      cityCode: '',
      themeType: '1',
      ctLegendColor: '#ffffff',
      areaColor: '#121a33',
      borderColor: '#38597b',
      visualPosition: 'left',
      scatterPoint: [{ name: '北京', value: [116.405285, 39.904989, 2] }], // 带有地理位置的散点数据
      chartData: morenData.mapData
    },
    {
      text: '3D路径图',
      imgClass: 'icon-3dlines',
      chartType: 'TDEarthLine',
      width: 300,
      height: 300,
      scanningspot: 'true',
      scanningspeed: 40,
      scanningradiu: 2,
      scanninglength: 0.2,
      scanningopcity: 1,
      scanningcolor: '#6fa8dc',
      backPicName: 'Mapcolor1-2',
      linewidth: 1,
      lineColor: '#1c4587',
      lineoption: 0.8,
      needrotate: 'true',
      rotatedirection: 'ccw',
      rotatespeed: 5,
      norotatetime: 4,
      alpha: 25,
      beta: 25,
      symbolSize: 5,
      symbolcolor: 'rgb(50, 250, 150)',
      symbolopacity: 0.5,
      displacementScale: 0.03,
      roughness: '0',
      metalness: '0',
      shadingtype: 'realistic',
      ambientcolor: '#ffffff',
      ambientintensity: 0.5,
      maincolor: '#ffffff',
      mainintensity: 0.5,
      mainbeta: 20,
      mainalpha: 10,
      chartData: morenData.TDEarthLineData
    },
    {
      text: '3D地球柱图',
      imgClass: 'icon-3dhistogrammap',
      chartType: 'TDEarthBar',
      width: 600,
      height: 600,
      scanningspot: 'true',
      scanningspeed: 40,
      scanningradiu: 2,
      scanninglength: 0.2,
      scanningopcity: 1,
      scanningcolor: '#6fa8dc',
      backPicName: 'Mapcolor1-2',
      linewidth: 1,
      lineColor: '#1c4587',
      lineoption: 0.8,
      needrotate: 'true',
      rotatedirection: 'ccw',
      rotatespeed: 5,
      norotatetime: 4,
      alpha: 25,
      beta: 25,
      symbolSize: 5,
      symbolcolor: 'rgb(50, 250, 150)',
      symbolopacity: 0.5,
      displacementScale: 0.03,
      roughness: '0',
      metalness: '0',
      shadingtype: 'realistic',
      ambientcolor: '#ffffff',
      ambientintensity: 0.5,
      maincolor: '#ffffff',
      mainintensity: 0.5,
      barSize: 0.6,
      itemStyleColor: '#d2ca47',
      labelColor: '#b4b851',
      labelSize: 10,
      labelWeight: 100,
      labelBorderwidth: 1,
      labelBorderColor: '#9c8057',
      labelBorderRadius: 20,
      labellineHeight: 14,
      labelbackColor: '#2b3c57',
      mainbeta: 20,
      mainalpha: 10,
      chartData: morenData.TDEarthbarData
    },
    DataFlow.item
    // {
    //   text: '地图-迁徙图',
    //   imgClass: 'icon-migratemap',
    //   chartType: 'DataFlow',
    //   width: 600,
    //   height: 600,
    //   labelemphasis: 'true',
    //   textStyleColor: '#fff',
    //   labelfontSize: 14,
    //   roam: 'true',
    //   normalcolor: 'rgba(51, 69, 89, .5)',
    //   normalborderColor: '#516a89',
    //   normalborderWidth: 1,
    //   emphasis: 'rgba(37, 43, 61, .5)',
    //   effectshow: 'true',
    //   imgSrc: '',
    //   effectperiod: 3,
    //   effecttrailLength: 0.6,
    //   effectsymbolSize: 15,
    //   normalwidth: 1,
    //   normalopacity: '1.0',
    //   normalcurveness: -0.3,
    //   showEffectOn: 'render',
    //   rippleEffectbrushType: 'stroke',
    //   rippleEffectperiod: 4,
    //   rippleEffectscale: 4,
    //   normalposition: 'right',
    //   normalfontSize: 16,
    //   symbolSize: 6,
    //   EffectbrushType: 'stroke',
    //   Effectscale: 1.6,
    //   labelposition: 'right',
    //   labelcolor: '#fff',
    //   labeltextSize: 16,
    //   geosymbolSize: 25,
    //   tooltipBackColor: 'rgba(166, 200, 76, 0.82)',
    //   tooltipTextColor: '#fff',
    //   tooltipTextfontSize: 12,
    //   visualMapShow: 'true',
    //   calculable: 'true',
    //   visualMapTextcolor: '#fff',
    //   chartData: morenData.DataFlowData
    // }
  ]
}

export default map
