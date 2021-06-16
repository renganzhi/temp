import {morenData} from '@/views/Edit/chartJson'
import DataFlow from '@/components/EditComp/DataFlow/config.js'
import TDEarthLine from '@/components/EditComp/TDEarthLine/config.js'
import BaiDuMap from '@/components/EditComp/BaiDuMap/config.js'
import TDEarthBar from '@/components/EditComp/TDEarthBar/config.js'
import NewVMap from '@/components/EditComp/NewVMap/config.js'
import NewScatter from '@/components/EditComp/NewScatter/config.js'

const map = {
  name: '地图',
  child: [
    // {
    //   text: '区域分布图',
    //   imgClass: 'icon-n-areaMap',
    //   chartType: 'v-map',
    //   width: 300,
    //   height: 300,
    //   mapLevel: 'country',
    //   countryCode: 100000,
    //   provinceCode: '',
    //   cityCode: '',
    //   visualPosition: 'left',
    //   fontSize: 10,
    //   cityShow: false,
    //   ctLegendColor: '#fff',
    //   areaColor: '#121a33',
    //   borderColor: '#38597b',
    //   cityColor: '#828bac',
    //   themeType: '1', // 1深色 2浅色
    //   piecesData: [
    //     { min: 0, max: 50 },
    //     { min: 51, max: 100 },
    //     { min: 101 }
    //   ],
    //   chartData: morenData.map
    // },
    // {
    //   text: '地图实时图',
    //   imgClass: 'icon-n-scatterMap',
    //   chartType: 'v-scatter', // 散点图
    //   width: 300,
    //   height: 300,
    //   mapLevel: 'country',
    //   countryCode: 100000,
    //   ctLegendShow: 'false', // 地名是否展示
    //   provinceCode: '',
    //   cityCode: '',
    //   themeType: '1',
    //   ctLegendColor: '#ffffff',
    //   areaColor: '#121a33',
    //   borderColor: '#38597b',
    //   visualPosition: 'left',
    //   scatterPoint: [{ name: '北京', value: [116.405285, 39.904989, 2] }], // 带有地理位置的散点数据
    //   chartData: morenData.mapData
    // },
    NewScatter.item,
    NewVMap.item,
    TDEarthLine.item,
    TDEarthBar.item,
    DataFlow.item,
    BaiDuMap.item
  ]
}

export default map
