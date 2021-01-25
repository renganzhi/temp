import {morenData} from '@/views/Edit/chartJson'
import GradientPie from '@/components/EditComp/GradientPie/config.js'
import polarBar from '@/components/EditComp/polarBar/config.js'
import TDHistogram from '@/components/EditComp/TDHistogram/config.js'
import Scatter from '@/components/EditComp/Scatter/config.js'
import KLine from '@/components/EditComp/KLine/config.js'

const coordinate = {
  name: '坐标图',
  child: [
    {
      text: '柱状图',
      colorful: false,
      imgClass: 'icon-n-histogram',
      chartType: 've-histogram',
      ifGradual: 'false',
      splitShow: 'false',
      ctLegendSize: '16',
      splitColor: '#333849',
      splitSize: 1,
      rotate: 0,
      chartData: morenData.pie
    },
    {
      text: '分组柱图',
      imgClass: 'icon-n-grouphistogram',
      chartType: 've-histogram',
      ifGradual: 'false',
      splitShow: 'false',
      splitColor: '#333849',
      splitSize: 1,
      ctLegendSize: '14',
      subType: 'groupHistogram',
      rotate: 0,
      chartData: morenData.line
    },
    {
      text: '堆叠柱图',
      imgClass: 'icon-n-grouphistogram',
      chartType: 've-histogram',
      subType: 'groupHistogram',
      thirdType: 'stackHistogram',
      ifGradual: 'false',
      splitShow: 'false',
      ctLegendSize: '16',
      splitColor: '#333849',
      splitSize: 1,
      rotate: 0,
      chartData: morenData.line
    },
    {
      text: '条形图',
      imgClass: 'icon-n-bar',
      chartType: 've-bar',
      ifGradual: 'false',
      colorful: false,
      splitShow: 'false',
      ctLegendSize: '16',
      splitColor: '#333849',
      splitSize: 1,
      rotate: 0,
      chartData: morenData.pie
    },
    {
      text: '曲线图',
      imgClass: 'icon-n-line',
      chartType: 've-line',
      ifGradual: 'false',
      splitShow: 'false',
      ctLegendSize: '16',
      splitColor: '#333849',
      splitSize: 1,
      chartData: morenData.time,
      lineArea: false, // 是否为区域图
      smooth: 'true',
      showPoint: 'true', // 是否标点
      PointSize: '14',
      rotate: 0
    },
    {
      text: '双轴曲线图',
      imgClass: 'icon-biaxialline',
      chartType: 've-line',
      ifGradual: 'false',
      subType: 'doubleAxis',
      splitShow: 'false',
      ctLegendSize: '16',
      splitColor: '#333849',
      splitSize: 1,
      chartData: morenData.times,
      lineArea: false, // 是否为区域图
      smooth: 'true',
      showPoint: 'true', // 是否标点
      PointSize: '14',
      rotate: 0
    },
    GradientPie.item,
    polarBar.item,
    TDHistogram.item,
    Scatter.item,
    KLine.item
  ]
}

export default coordinate
