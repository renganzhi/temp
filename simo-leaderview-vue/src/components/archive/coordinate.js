import {morenData} from '@/views/Edit/chartJson'
import GradientPie from '@/components/EditComp/GradientPie/config.js'
import polarBar from '@/components/EditComp/polarBar/config.js'
import TDHistogram from '@/components/EditComp/TDHistogram/config.js'
import Scatter from '@/components/EditComp/Scatter/config.js'
import KLine from '@/components/EditComp/KLine/config.js'
import ELine from '@/components/EditComp/ELine/config.js'
import DoubleLinde from '@/components/EditComp/DoubleLinde/config.js'
import NewHistogram from '@/components/EditComp/NewHistogram/config.js'
import NewGroupHistogram from '@/components/EditComp/NewGroupHistogram/config.js'
import NewGroupLeftHistogram from '@/components/EditComp/NewGroupLeftHistogram/config.js'
import NewBar from '@/components/EditComp/NewBar/config.js'
import IntegratedHistogram from '@/components/EditComp/IntegratedHistogram/config.js'
import BiaxialBarChart from '@/components/EditComp/BiaxialBarChart/config.js'

const coordinate = {
  name: '坐标图',
  child: [
    // {
    //   text: '柱状图',
    //   colorful: false,
    //   imgClass: 'icon-n-histogram',
    //   chartType: 've-histogram',
    //   ifGradual: 'true',
    //   splitShow: 'false',
    //   ctLegendSize: '16',
    //   colorType: 'custom',
    //   ctColors: [
    //     ['#6fcaf7', '#0c79c5'],
    //     ['#8feee5', '#1bbcae'],
    //     ['#fa8d76', '#db4222'],
    //     ['#af8af3', '#874edc'],
    //     ['#f5739c', '#f31d53'],
    //     ['#ffdf91', '#eeb01b'],
    //     ['#5c84e7', '#144fe5'],
    //     ['#85f8c0', '#62dc26']
    //   ],
    //   ctLegendColor: '#666f8b',
    //   axisLabelSize: '16',
    //   DanweiColor: '#828bac',
    //   DanweiSize: 16,
    //   minInterval: '',
    //   legendY: 90,
    //   gridTop: 10,
    //   gridBotton: 10,
    //   gridLeft: 10,
    //   gridRight: 10,
    //   formatterType: '0',
    //   tooltipShow: 'true',
    //   tooltipBackColor: '#57625d',
    //   tooltipTextColor: '#fff',
    //   tooltipfontSize: 14,
    //   splitColor: '#333849',
    //   splitSize: 1,
    //   rotate: 0,
    //   chartData: morenData.pie
    // },
    // NewHistogram.item,
    // NewGroupHistogram.item,
    // NewGroupLeftHistogram.item,
    IntegratedHistogram.item,
    BiaxialBarChart.item,
    // NewBar.item,
    ELine.item,
    DoubleLinde.item,
    GradientPie.item,
    polarBar.item,
    TDHistogram.item,
    Scatter.item,
    KLine.item
    // {
    //   text: '分组柱图',
    //   imgClass: 'icon-n-grouphistogram',
    //   chartType: 've-histogram',
    //   ifGradual: 'false',
    //   splitShow: 'false',
    //   splitColor: '#333849',
    //   splitSize: 1,
    //   ctLegendSize: '16',
    //   ctLegendColor: '#666f8b',
    //   axisLabelSize: '14',
    //   DanweiColor: '#828bac',
    //   DanweiSize: 16,
    //   minInterval: '',
    //   legendY: 90,
    //   gridTop: 10,
    //   gridBotton: 10,
    //   gridLeft: 10,
    //   gridRight: 10,
    //   tooltipShow: 'true',
    //   tooltipBackColor: '#57625d',
    //   tooltipTextColor: '#fff',
    //   tooltipfontSize: 14,
    //   subType: 'groupHistogram',
    //   rotate: 0,
    //   formatterType: '0',
    //   chartData: morenData.line
    // },
    // {
    //   text: '堆叠柱图',
    //   imgClass: 'icon-n-grouphistogram',
    //   chartType: 've-histogram',
    //   subType: 'groupHistogram',
    //   thirdType: 'stackHistogram',
    //   ifGradual: 'false',
    //   splitShow: 'false',
    //   ctLegendSize: '16',
    //   ctLegendColor: '#666f8b',
    //   axisLabelSize: '16',
    //   DanweiColor: '#828bac',
    //   DanweiSize: 16,
    //   minInterval: '',
    //   legendY: 90,
    //   gridTop: 10,
    //   gridBotton: 10,
    //   gridLeft: 10,
    //   gridRight: 10,
    //   tooltipShow: 'true',
    //   tooltipBackColor: '#57625d',
    //   tooltipTextColor: '#fff',
    //   tooltipfontSize: 14,
    //   splitColor: '#333849',
    //   splitSize: 1,
    //   rotate: 0,
    //   formatterType: '0',
    //   chartData: morenData.line
    // },
    // {
    //   text: '条形图',
    //   imgClass: 'icon-n-bar',
    //   chartType: 've-bar',
    //   ifGradual: 'false',
    //   colorful: false,
    //   splitShow: 'false',
    //   ctLegendSize: '16',
    //   ctLegendColor: '#666f8b',
    //   axisLabelSize: '16',
    //   legendY: 90,
    //   gridTop: 10,
    //   gridBotton: 10,
    //   gridLeft: 10,
    //   gridRight: 10,
    //   tooltipShow: 'true',
    //   DanweiColor: '#828bac',
    //   DanweiSize: 16,
    //   minInterval: '',
    //   tooltipBackColor: '#57625d',
    //   tooltipTextColor: '#fff',
    //   tooltipfontSize: 14,
    //   splitColor: '#333849',
    //   splitSize: 1,
    //   rotate: 0,
    //   formatterType: '0',
    //   chartData: morenData.pie
    // },
    // {
    //   text: '曲线图',
    //   imgClass: 'icon-n-line',
    //   chartType: 've-line',
    //   ifGradual: 'false',
    //   splitShow: 'false',
    //   ctLegendSize: '16',
    //   splitColor: '#333849',
    //   splitSize: 1,
    //   chartData: morenData.time,
    //   lineArea: false, // 是否为区域图
    //   smooth: 'true',
    //   showPoint: 'true', // 是否标点
    //   PointSize: '14',
    //   rotate: 0
    // },
    // {
    //   text: '双轴曲线图',
    //   height: 400,
    //   width: 700,
    //   imgClass: 'icon-n-line',
    //   chartType: 've-line',
    //   ifGradual: 'false',
    //   subType: 'doubleAxis',
    //   splitShow: 'false',
    //   ctLegendSize: '16',
    //   ctLegendColor: '#666f8b',
    //   axisLabelSize: '16',
    //   legendY: 90,
    //   gridTop: 10,
    //   gridBotton: 10,
    //   gridLeft: 10,
    //   gridRight: 10,
    //   tooltipShow: 'true',
    //   tooltipBackColor: '#57625d',
    //   tooltipTextColor: '#fff',
    //   tooltipfontSize: 14,
    //   boundaryGap: 'true',
    //   lineWidth: 1,
    //   DanweiColor: '#828bac',
    //   DanweiSize: 16,
    //   splitColor: '#333849',
    //   splitSize: 1,
    //   chartData: morenData.times,
    //   lineArea: 'true', // 是否为区域图
    //   lineColorType: false, // 是否为区域图
    //   smooth: 'true',
    //   LineType: 'solid',
    //   symbolType: 'circle',
    //   formatterType: '0',
    //   symbolSrc: '',
    //   symbolSize: 6,
    //   showPoint: 'true', // 是否标点
    //   PointSize: '14',
    //   rotate: 0
    // },
  ]
}

export default coordinate
