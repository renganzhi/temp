import player from '@/components/EditComp/player/config.js'
import ppt from '@/components/EditComp/ppt/config.js'
import TextRotation from '@/components/EditComp/TextRotation/config.js'
import liquidfill from '@/components/EditComp/liquidfill/config.js'
import polarBar from '@/components/EditComp/polarBar/config.js'
import pyramid from '@/components/EditComp/pyramid/config.js'
import GradientPie from '@/components/EditComp/GradientPie/config.js'
import Sunrise from '@/components/EditComp/Sunrise/config.js'
import Scatter from '@/components/EditComp/Scatter/config.js'
import ELine from '@/components/EditComp/ELine/config.js'
import ConditionalEline from '@/components/EditComp/ConditionalEline/config.js'
import ConditionalBar from '@/components/EditComp/ConditionalBar/config.js'
import TimePickerEline from '@/components/EditComp/TimePickerEline/config.js'
import Dashboard from '@/components/EditComp/Dashboard/config.js'
import KLine from '@/components/EditComp/KLine/config.js'
import DownMenu from '@/components/EditComp/DownMenu/config.js'
import NewDropMenu from '@/components/EditComp/NewDropMenu/config.js'
import TreeMap from '@/components/EditComp/TreeMap/config.js'
import BulletFrame from '@/components/EditComp/BulletFrame/config.js'
import TDHistogram from '@/components/EditComp/TDHistogram/config.js'
import NEWtextArea from '@/components/EditComp/NEWtextArea/config.js'
import bubble from '@/components/EditComp/bubble/config.js'
import hotspot from '@/components/EditComp/hotspot/config.js'
import SwitchButton from '@/components/EditComp/SwitchButton/config.js'
import ShrinkBox from '@/components/EditComp/ShrinkBox/config.js'
import ExternalJump from '@/components/EditComp/ExternalJump/config.js'
import LLZYPotButton from '@/components/EditComp/LLZYPotButton/config.js'
import SwitchMapButton from '@/components/EditComp/SwitchMapButton/config.js'
import ToggleButton from '@/components/EditComp/ToggleButton/config.js'
import IframePop from '@/components/EditComp/IframePop/config.js'
import OrderMenu from '@/components/EditComp/OrderMenu/config.js'
import SmallOrderMenu from '@/components/EditComp/SmallOrderMenu/config.js'
import CityEvent from '@/components/EditComp/CityEvent/config.js'
import WuhouSinglePage from '@/components/EditComp/WuhouSinglePage/config.js'
import AppMarket from '@/components/EditComp/AppMarket/config.js'
import BigAppMarket from '@/components/EditComp/BigAppMarket/config.js'
import KeyProjects from '@/components/EditComp/KeyProjects/config.js'
import EventVenation from '@/components/EditComp/EventVenation/config.js'
import StreetMenu from '@/components/EditComp/StreetMenu/config.js'
import WordClouds from '@/components/EditComp/WordClouds/config.js'
import decorator from '@/components/EditComp/decorator/config.js'

export var morenData = {
  pie: {
    columns: ['告警级别', '数量'],
    unit: '次',
    rows: [{
      告警级别: '致命',
      数量: 233
    },
    {
      告警级别: '严重',
      数量: 123
    },
    {
      告警级别: '警告',
      数量: 23
    },
    {
      告警级别: '一般',
      数量: 155
    },
    {
      告警级别: '次要',
      数量: 103
    },
    {
      告警级别: '通知',
      数量: 123
    }
    ]
  },
  line: {
    columns: ['资源', 'CPU利用率', '内存利用率', '健康度'],
    unit: '%',
    rows: [{
      资源: '192.168.2.32',
      CPU利用率: 80,
      内存利用率: 90,
      健康度: 80
    },
    {
      资源: '192.168.2.132',
      CPU利用率: 60,
      内存利用率: 20,
      健康度: 70
    },
    {
      资源: '192.168.2.62',
      CPU利用率: 10,
      内存利用率: 60,
      健康度: 80
    },
    {
      资源: '192.168.2.200',
      CPU利用率: 90,
      内存利用率: 30,
      健康度: 40
    },
    {
      资源: '192.168.2.72',
      CPU利用率: 50,
      内存利用率: 60,
      健康度: 70
    },
    {
      资源: '192.168.2.190',
      CPU利用率: 45,
      内存利用率: 76,
      健康度: 67
    }
    ]
  },
  time: {
    columns: ['日期', 'CPU核心利用率', 'CPU平均利用率'],
    unit: '%',
    rows: [{
      日期: '2020-01-01',
      CPU核心利用率: 15,
      CPU平均利用率: 15
    },
    {
      日期: '2020-01-02',
      CPU核心利用率: 80,
      CPU平均利用率: 50
    },
    {
      日期: '2020-01-03',
      CPU核心利用率: 40,
      CPU平均利用率: 6
    },
    {
      日期: '2020-01-05',
      CPU核心利用率: 45,
      CPU平均利用率: 70
    },
    {
      日期: '2020-01-06',
      CPU核心利用率: 10,
      CPU平均利用率: 40
    },
    {
      日期: '2020-01-07',
      CPU核心利用率: 95,
      CPU平均利用率: 50
    }
    ]
  },
  times: {
    columns: ['日期', 'CPU核心利用率', 'CPU平均利用率'],
    unit: ['%', '%'],
    rows: [{
      日期: '2020-01-01',
      CPU核心利用率: 15,
      CPU平均利用率: 15
    },
    {
      日期: '2020-01-02',
      CPU核心利用率: 80,
      CPU平均利用率: 50
    },
    {
      日期: '2020-01-03',
      CPU核心利用率: 40,
      CPU平均利用率: 6
    },
    {
      日期: '2020-01-05',
      CPU核心利用率: 45,
      CPU平均利用率: 70
    },
    {
      日期: '2020-01-06',
      CPU核心利用率: 10,
      CPU平均利用率: 40
    },
    {
      日期: '2020-01-07',
      CPU核心利用率: 95,
      CPU平均利用率: 50
    }
    ]
  },
  guage: {
    columns: ['type', 'value'],
    rows: [{
      type: 'CPU利用率',
      value: 60
    }]
  },
  table: {
    columns: ['IP', '日期', 'CPU利用率', '内存利用率'],
    rows: [{
      IP: '192.168.1.1',
      日期: '1/1',
      CPU利用率: 1193,
      内存利用率: 1013
    },
    {
      IP: '192.168.1.2',
      日期: '1/2',
      CPU利用率: 1293,
      内存利用率: 1023
    },
    {
      IP: '192.168.1.3',
      日期: '1/3',
      CPU利用率: 1393,
      内存利用率: 1033
    },
    {
      IP: '192.168.1.4',
      日期: '1/4',
      CPU利用率: 1493,
      内存利用率: 1043
    },
    {
      IP: '192.168.1.5',
      日期: '1/5',
      CPU利用率: 1593,
      内存利用率: 1053
    },
    {
      IP: '192.168.1.6',
      日期: '1/6',
      CPU利用率: 1693,
      内存利用率: 1063
    },
    {
      IP: '192.168.1.7',
      日期: '1/7',
      CPU利用率: 1793,
      内存利用率: 1073
    },
    {
      IP: '192.168.1.8',
      日期: '1/8',
      CPU利用率: 1893,
      内存利用率: 1083
    },
    {
      IP: '192.168.1.9',
      日期: '1/9',
      CPU利用率: 1993,
      内存利用率: 1093
    },
    {
      IP: '192.168.1.10',
      日期: '1/10',
      CPU利用率: 1093,
      内存利用率: 1003
    }
    ]
  },
  single: {
    name: '繁忙度',
    unit: '%',
    value: 60
  },
  map: {
    columns: ['位置', '资源', '告警'],
    rows: [
      { '位置': '台湾', '告警': 25 },
      { '位置': '河北', '告警': 75 },
      { '位置': '山西', '告警': 125 }
    ]
  },
  mapData: [
    { name: '北京', value: 2 }
  ],
  TDEarthLineData: {
    lineArry: [
      // station 为飞线的起点和重点经纬度  linecolor为线条颜色，scanningcolor为飞线点颜色，不填则使用配置页面配置的颜色
      {station: [[[145.391881, -6.081689], [-125.7725, 49.082222]]], linecolor: '#ff0000', scanningcolor: '#00ff00'},
      {station: [[[125.391881, -16.081689], [-125.7725, 54.082222]]]}
    ]
  },
  TDEarthbarData: {
    barArry: [
      {
        value: [34, 50, 1],
        itemStyle: {
          color: 'green',
          opacity: '1'
        },
        label: {
          show: true
        },
        labelvalue: {
          '项目名称': '项目001',
          '项目地址': '西班牙XXXXXXXXXX',
          '数据量': 2
        }
      },
      {
        value: [34, 40, 2],
        itemStyle: {
          color: 'red',
          opacity: '1'
        }
      },
      {
        value: [116.397128, 39.916527, 1]
      }
    ]
  },
  DataFlowData: {
    stationData: [
      {
        name: '黑龙江',
        value: [127.9688, 45.368, 1]
      }, {
        name: '内蒙古',
        value: [110.3467, 41.4899, 2]
      }, {
        name: '吉林',
        value: [125.8154, 44.2584, 4]
      }, {
        name: '辽宁',
        value: [123.1238, 42.1216, 5]
      }, {
        name: '河北',
        value: [114.4995, 38.1006, 7]
      }, {
        name: '天津',
        value: [117.4219, 39.4189, 43]
      }, {
        name: '山西',
        value: [112.3352, 37.9413, 0]
      }, {
        name: '陕西',
        value: [109.1162, 34.2004, 11]
      }, {
        name: '甘肃',
        value: [103.5901, 36.3043, 12]
      }
    ],
    lineData: [
      [
        {
          coord: [127.9688, 45.368],
          name: '黑龙江→北京',
          value: 1
        }, {
          coord: [116.4551, 40.2539]
        }
      ]
    ],
    Statistical: [
      {
        name: '北京市',
        value: [116.4551, 40.2539, 20]
      }, {
        name: '福建',
        value: [119.4543, 25.9222, 10]
      }
    ],
    maxValue: 30,
    minValue: 0
  }
}
var compsArr = [{
  text: '饼图',
  imgClass: 'icon-n-pie',
  chartType: 've-pie',
  ifGradual: 'false',
  ctLegendSize: '16',
  ctLegendColor: '#666f8b',
  axisLabelSize: '16',
  legendY: 85,
  gridTop: 50,
  tooltipShow: 'true',
  tooltipBackColor: '#57625d',
  tooltipTextColor: '#fff',
  tooltipfontSize: 14,
  chartData: morenData.pie
},
{
  text: '南丁格尔图',
  imgClass: 'icon-n-ndge',
  chartType: 've-pie',
  ifGradual: 'false',
  ctLegendSize: '16',
  ctLegendColor: '#666f8b',
  axisLabelSize: '16',
  legendY: 85,
  gridTop: 50,
  tooltipShow: 'true',
  tooltipBackColor: '#57625d',
  tooltipTextColor: '#fff',
  tooltipfontSize: 14,
  chartData: morenData.pie,
  roseType: 'area' // 与饼图的区别
},
{
  text: '环形图',
  imgClass: 'icon-n-ring',
  chartType: 've-ring',
  ifGradual: 'false',
  ctLegendSize: '16',
  ctLegendColor: '#666f8b',
  axisLabelSize: '16',
  legendY: 85,
  gridTop: 50,
  tooltipShow: 'true',
  tooltipBackColor: '#57625d',
  tooltipTextColor: '#fff',
  tooltipfontSize: 14,
  chartData: morenData.pie
},
{
  text: '目标占比图',
  width: 200,
  height: 200,
  imgClass: 'icon-n-percent',
  chartType: 've-gauge',
  ifGradual: 'false',
  subType: 'progress',
  bgClr: '#657992',
  fontSize: 24,
  ctLegendShow: 'true',
  detailColor: '#ff0000',
  ctLegendSize: '16',
  ctLegendColor: '#666f8b',
  axisLabelSize: '16',
  chartData: morenData.single
},
{
  text: '柱状图',
  colorful: false,
  imgClass: 'icon-n-histogram',
  chartType: 've-histogram',
  ifGradual: 'false',
  splitShow: 'false',
  ctLegendSize: '16',
  ctLegendColor: '#666f8b',
  axisLabelSize: '16',
  legendY: 85,
  gridTop: 50,
  tooltipShow: 'true',
  tooltipBackColor: '#57625d',
  tooltipTextColor: '#fff',
  tooltipfontSize: 14,
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
  ctLegendSize: '16',
  ctLegendColor: '#666f8b',
  axisLabelSize: '14',
  legendY: 85,
  gridTop: 50,
  tooltipShow: 'true',
  tooltipBackColor: '#57625d',
  tooltipTextColor: '#fff',
  tooltipfontSize: 14,
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
  ctLegendColor: '#666f8b',
  axisLabelSize: '16',
  legendY: 85,
  gridTop: 50,
  tooltipShow: 'true',
  tooltipBackColor: '#57625d',
  tooltipTextColor: '#fff',
  tooltipfontSize: 14,
  splitColor: '#333849',
  splitSize: 1,
  rotate: 0,
  chartData: morenData.line
},
/* {
  text:'山峰柱状图',
  imgClass:'icon-n-histogram',
  chartType:'ve-histogram',
  subType:'pictorialBar',
  secondType: 'peakBar',
  width:300,
  height:300,
  chartData:morenData.pie
},
{
  text:'象形柱状图',
  imgClass:'icon-n-histogram',
  chartType:'ve-histogram',
  subType:'pictorialBar',
  secondType: 'symbolBar',
  width:300,
  height:300,
  chartData:morenData.pie,
  symbolImg: ''
}, */
/* {
  text:'漏斗图',
  imgClass:'icon-n-histogram',
  chartType:'ve-funnel',
  chartData:morenData.pie,
}, */
/* {
  text:'弧形柱图',
  imgClass:'icon-n-bar',
  chartType:'ve-bar',
  subType:'category',
  chartData:morenData.pie
}, */
{
  text: '条形图',
  imgClass: 'icon-n-bar',
  chartType: 've-bar',
  ifGradual: 'false',
  colorful: false,
  splitShow: 'false',
  ctLegendSize: '16',
  ctLegendColor: '#666f8b',
  axisLabelSize: '16',
  legendY: 85,
  gridTop: 50,
  tooltipShow: 'true',
  tooltipBackColor: '#57625d',
  tooltipTextColor: '#fff',
  tooltipfontSize: 14,
  splitColor: '#333849',
  splitSize: 1,
  rotate: 0,
  chartData: morenData.pie
},
ELine.item,
ConditionalEline.item,
ConditionalBar.item,
TimePickerEline.item,
// {
//   text: '曲线图',
//   imgClass: 'icon-n-line',
//   chartType: 've-line',
//   ifGradual: 'false',
//   splitShow: 'false',
//   ctLegendSize: '16',
//   axisLabelSize: '16',
//   legendY: 85,
//   gridTop: 50,
//   tooltipShow: 'true',
//   tooltipBackColor: '#57625d',
//   tooltipTextColor: '#fff',
//   tooltipfontSize: 14,
//   splitColor: '#333849',
//   splitSize: 1,
//   chartData: morenData.time,
//   lineArea: false, // 是否为区域图
//   lineColorType: false, // 是否为区域图
//   smooth: 'true',
//   LineType: 'solid',
//   symbolType: 'circle',
//   symbolSrc: '',
//   symbolSize: 6,
//   showPoint: 'true', // 是否标点
//   PointSize: '14',
//   rotate: 0
// },
{
  text: '双轴曲线图',
  imgClass: 'icon-n-line',
  chartType: 've-line',
  ifGradual: 'false',
  subType: 'doubleAxis',
  splitShow: 'false',
  ctLegendSize: '16',
  ctLegendColor: '#666f8b',
  axisLabelSize: '16',
  legendY: 85,
  gridTop: 50,
  tooltipShow: 'true',
  tooltipBackColor: '#57625d',
  tooltipTextColor: '#fff',
  tooltipfontSize: 14,
  splitColor: '#333849',
  splitSize: 1,
  chartData: morenData.times,
  lineArea: false, // 是否为区域图
  lineColorType: false, // 是否为区域图
  smooth: 'true',
  LineType: 'solid',
  symbolType: 'circle',
  symbolSrc: '',
  symbolSize: 6,
  showPoint: 'true', // 是否标点
  PointSize: '14',
  rotate: 0
},
polarBar.item,
{
  text: '雷达图',
  imgClass: 'icon-n-radar',
  chartType: 've-radar',
  ifGradual: 'false',
  splitShow: 'true',
  ctLegendSize: '16',
  ctLegendColor: '#666f8b',
  axisLabelSize: '16',
  legendY: 85,
  gridTop: 50,
  tooltipShow: 'true',
  tooltipBackColor: '#57625d',
  tooltipTextColor: '#fff',
  tooltipfontSize: 14,
  splitColor: 'rgba(117, 124, 137, 0.2)',
  splitSize: 1,
  width: 500,
  height: 380,
  chartData: morenData.line
},
Dashboard.item,
// {
//   text: '仪表盘',
//   imgClass: 'icon-n-gauge',
//   chartType: 've-gauge',
//   ifGradual: 'false',
//   ctLegendSize: '16',
//   legendY: 85,
//   gridTop: 50,
//   ctLegendShow: false,
//   bgClr: '#657992',
//   chartData: morenData.single
// },
{
  text: '表格',
  imgClass: 'icon-n-table',
  chartType: 'table',
  hdBgClr: 'rgba(28, 33, 50, 0.54)',
  hdClr: '#cad6dd',
  hdfontSize: 12,
  heightLinght: 36,
  bgClr: 'rgba(34, 40, 58, 0.5)',
  clr: '#919cc1',
  bdClr: '#c2c6d7',
  Internal: 'false',
  Zebra: 'false',
  Alignment: 'left',
  ZebraColor: '#343c58',
  tableBack: '',
  AlarmField: '',
  AlarmType: 'num',
  AlarmChart: '',
  OneLineType: 'default',
  OneLineSize: 86,
  AlarmNumType: 'greater',
  AlarmNum: '',
  AlarmColor: 'red',
  bdpx: 0,
  chartData: morenData.table
},
{
  text: '轮播表格',
  imgClass: 'icon-n-table',
  chartType: 'table',
  thirdType: 'moveTable',
  direction: 'top',
  speed: 2,
  pageNum: 5,
  heightLinght: 36,
  hdBgClr: '#1c2132',
  hdClr: '#cad6dd',
  hdfontSize: 12,
  bgClr: 'rgba(34, 40, 58, 0.5)',
  clr: '#919cc1',
  bdClr: '#c2c6d7',
  Internal: 'false',
  Zebra: 'false',
  Alignment: 'left',
  ZebraColor: '#343c58',
  tableBack: '',
  AlarmField: '',
  AlarmType: 'num',
  OneLineType: 'default',
  OneLineSize: 86,
  AlarmChart: '',
  AlarmNumType: 'greater',
  AlarmNum: '',
  AlarmColor: 'red',
  bdpx: 0,
  chartData: morenData.table
},
// {
//   text: '文本框',
//   imgClass: 'icon-n-text',
//   chartType: 'text',
//   width: 200,
//   height: 50,
//   bdpx: 0,
//   bgClr: '',
//   clr: '#666f8b',
//   bdClr: '#3d445a',
//   ctName: '请输入文本框内容',
//   fontWeight: 'normal',
//   fontFamily: '',
//   linkId: '',
//   chartData: {}
// },
NEWtextArea.item,
hotspot.item,
SwitchButton.item,
ShrinkBox.item,
ExternalJump.item,
LLZYPotButton.item,
SwitchMapButton.item,
ToggleButton.item,
IframePop.item,
OrderMenu.item,
SmallOrderMenu.item,
CityEvent.item,
WuhouSinglePage.item,
AppMarket.item,
BigAppMarket.item,
KeyProjects.item,
EventVenation.item,
StreetMenu.item,
WordClouds.item,
{
  text: '跑马灯',
  imgClass: 'icon-n-marquee',
  chartType: 'marquee',
  width: 200,
  height: 50,
  bdpx: 0,
  bgClr: '',
  direction: 'left',
  speed: 2,
  clr: '#666f8b',
  bdClr: '',
  ctName: '这是一个跑马灯的演示demo，双击可对文案进行编辑'
},
{
  text: '进度条',
  imgClass: 'icon-n-progress',
  chartType: 'progress',
  width: 300,
  height: 50,
  bgClr: '#33394b',
  barClr: '#1fc3ce',
  barClrs: ['#8feee5', '#1bbcae'],
  clr: '#1fc3ce',
  colorful: false,
  chartData: morenData.single,
  proHeight: 16,
  radius: 8
},
{
  text: '数字翻牌器',
  imgClass: 'icon-n-doubler',
  chartType: 'doubler',
  fontSize: 36,
  bgClr: '#222739',
  bdClr: '', // #0c527c
  clr: '#0088cc',
  width: 300,
  height: 90,
  ctLegendShow: 'true',
  chartData: morenData.single
},
pyramid.item,
{
  text: '拓扑',
  imgClass: 'icon-n-topo',
  chartType: 'topo',
  width: 300,
  height: 300,
  tpId: '',
  cityColor: '',
  chartData: {}
},
{
  text: '图片',
  imgClass: 'icon-n-exportPicture',
  chartType: 'image',
  width: 300,
  height: 300,
  imgSrc: '',
  showType: '1',
  linkId: '',
  chartData: {}
},
ppt.item,
TextRotation.item,
decorator.item,
{
  text: '边框',
  imgClass: 'icon-n-rect',
  chartType: 'border',
  borderType: 'simple', // 内置stable, 简单simple
  imgSrc: '',
  radius: 0,
  width: 300,
  height: 300,
  bdpx: 1,
  showType: '1',
  colorful: false,
  bgClr: 'rgba(255, 255, 255, 0.02)',
  barClrs: ['rgba(255, 255, 255, 0.02)', 'rgba(255, 255, 255, 0.02)'],
  bdClr: '#175278'
},
{
  text: '时间器',
  imgClass: 'icon-n-time',
  chartType: 'time',
  timeSource: 'local',
  width: 200,
  height: 50,
  fontSize: 18,
  clr: '#666f8b',
  timeFrom: 'local',
  timeType: '1'
},
{
  text: '指标展示',
  imgClass: 'icon-n-number',
  chartType: 'number',
  fontSize: 36,
  fontFamily: 'number1',
  clr: '#25aff8',
  width: 300,
  height: 90,
  ctLegendShow: 'true',
  chartData: morenData.single
},
liquidfill.item,
bubble.item,
// {
//   text: '水波图',
//   width: 300,
//   height: 300,
//   imgClass: 'icon-n-waveball',
//   chartType: 've-gauge',
//   subType: 'progress',
//   secondType: 'liquidfill',
//   // chartType: 'liquidfill',
//   bdpx: 4,
//   bgClr: '#156acf',
//   clr: 'rgba(221, 221, 221, 0.9)',
//   bdClr: '#666f8b',
//   fontSize: 28,
//   ctLegendShow: 'true',
//   chartData: morenData.single
// },
{
  text: '区域分布图',
  imgClass: 'icon-n-areaMap',
  chartType: 'v-map',
  width: 300,
  height: 300,
  mapLevel: 'country',
  countryCode: 100000,
  provinceCode: '',
  countyCode: '',
  cityCode: '',
  visualPosition: 'left',
  fontSize: 10,
  cityShow: false,
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
  countyCode: '',
  countryCode: 100000,
  ctLegendShow: 'false', // 地名是否展示
  provinceCode: '',
  cityCode: '',
  themeType: '1',
  visualPosition: 'left',
  scatterPoint: [{ name: '北京', value: [116.405285, 39.904989, 2] }], // 带有地理位置的散点数据
  chartData: morenData.mapData
},
// {
//   text: '视频流',
//   imgClass: 'icon-n-video',
//   chartType: 'video',
//   width: 300,
//   height: 300,
//   videoType: 'local',
//   videoSrc: ''
// },
{
  text: '3d地图-飞线图',
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
  text: '3d地图-柱状图',
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
{
  text: '地图-迁徙图',
  imgClass: 'icon-migratemap',
  chartType: 'DataFlow',
  width: 600,
  height: 600,
  labelemphasis: 'true',
  textStyleColor: '#fff',
  labelfontSize: 20,
  roam: 'true',
  normalcolor: 'rgba(51, 69, 89, .5)',
  normalborderColor: '#516a89',
  normalborderWidth: 1,
  emphasis: 'rgba(37, 43, 61, .5)',
  effectshow: 'true',
  imgSrc: '',
  effectperiod: 3,
  effecttrailLength: 0.6,
  effectsymbolSize: 15,
  normalwidth: 1,
  normalopacity: '1.0',
  normalcurveness: -0.3,
  showEffectOn: 'render',
  rippleEffectbrushType: 'stroke',
  rippleEffectperiod: 4,
  rippleEffectscale: 4,
  normalposition: 'right',
  normalfontSize: 20,
  symbolSize: 6,
  EffectbrushType: 'stroke',
  Effectscale: 1.6,
  labelposition: 'right',
  labelcolor: '#fff',
  labeltextSize: 20,
  geosymbolSize: 40,
  tooltipBackColor: 'rgba(166, 200, 76, 0.82)',
  tooltipTextColor: '#fff',
  tooltipTextfontSize: 12,
  visualMapShow: 'true',
  calculable: 'true',
  visualMapTextcolor: '#fff',
  chartData: morenData.DataFlowData
},
GradientPie.item,
Sunrise.item,
Scatter.item,
KLine.item,
DownMenu.item,
NewDropMenu.item,
BulletFrame.item,
TreeMap.item,
TDHistogram.item,
// {
//   text: '饼图',
//   imgClass: 'icon-n-video',
//   chartType: 'GradientPie',
//   width: 300,
//   height: 300
// },
player.item
// {
//   text: '视频流',
//   imgClass: 'icon-n-video',
//   chartType: 'video',
//   width: 300,
//   height: 300,
//   videoType: 'local',
//   videoSrc: ''
// }
  // {
  //   text: '地图',
  //   imgClass: 'icon-n-radar',
  //   chartType: 'map',
  //   width: 500,
  //   height: 300,
  //   chartData: morenData.map
  // },
  // {
  //   text: '区域分布图',
  //   imgClass: 'icon-n-radar',
  //   chartType: 've-map',
  //   width: 500,
  //   height: 300,
  //   chartData: morenData.map
  // },
  // {
  //   text: '地图实时图',
  //   imgClass: 'icon-n-radar',
  //   chartType: 've-map',
  //   secondType: 'scatter', // 散点图
  //   width: 500,
  //   height: 300,
  //   chartData: morenData.map
  // }
]

// export default morenData;
export default compsArr
