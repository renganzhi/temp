var morenData = {
  pie: {
    columns: ['告警级别', '数量'],
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
      日期: '2018-01-01',
      CPU核心利用率: 15,
      CPU平均利用率: 15
    },
    {
      日期: '2018-01-02',
      CPU核心利用率: 80,
      CPU平均利用率: 50
    },
    {
      日期: '2018-01-03',
      CPU核心利用率: 40,
      CPU平均利用率: 6
    },
    {
      日期: '2018-01-05',
      CPU核心利用率: 45,
      CPU平均利用率: 70
    },
    {
      日期: '2018-01-06',
      CPU核心利用率: 10,
      CPU平均利用率: 40
    },
    {
      日期: '2018-01-07',
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
  ]
}
var compsArr = [{
  text: '饼图',
  imgClass: 'icon-n-pie',
  chartType: 've-pie',
  chartData: morenData.pie
},
{
  text: '南丁格尔图',
  imgClass: 'icon-n-ndge',
  chartType: 've-pie',
  chartData: morenData.pie,
  roseType: 'area' // 与饼图的区别
},
{
  text: '环形图',
  imgClass: 'icon-n-ring',
  chartType: 've-ring',
  chartData: morenData.pie
},
{
  text: '目标占比图',
  width: 200,
  height: 200,
  imgClass: 'icon-n-percent',
  chartType: 've-gauge',
  subType: 'progress',
  bgClr: '#657992',
  fontSize: 24,
  ctLegendShow: 'true',
  chartData: morenData.single
},
{
  text: '柱状图',
  colorful: false,
  imgClass: 'icon-n-histogram',
  chartType: 've-histogram',
  chartData: morenData.pie
},
{
  text: '分组柱图',
  imgClass: 'icon-n-grouphistogram',
  chartType: 've-histogram',
  subType: 'groupHistogram',
  chartData: morenData.line
},
{
  text: '堆叠柱图',
  imgClass: 'icon-n-grouphistogram',
  chartType: 've-histogram',
  subType: 'groupHistogram',
  thirdType: 'stackHistogram',
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
  colorful: false,
  chartData: morenData.pie
},
{
  text: '曲线图',
  imgClass: 'icon-n-line',
  chartType: 've-line',
  chartData: morenData.time,
  lineArea: false, // 是否为区域图
  showPoint: 'true' // 是否标点
},
{
  text: '雷达图',
  imgClass: 'icon-n-radar',
  chartType: 've-radar',
  width: 500,
  height: 380,
  chartData: morenData.line
},
{
  text: '仪表盘',
  imgClass: 'icon-n-gauge',
  chartType: 've-gauge',
  ctLegendShow: false,
  bgClr: '#657992',
  chartData: morenData.single
},
{
  text: '表格',
  imgClass: 'icon-n-table',
  chartType: 'table',
  hdBgClr: '#1c2132',
  bgClr: '',
  clr: '#919cc1',
  bdClr: '#c2c6d7',
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
  hdBgClr: '#1c2132',
  bgClr: '',
  clr: '#919cc1',
  bdClr: '#c2c6d7',
  bdpx: 0,
  chartData: morenData.table
},
{
  text: '文本框',
  imgClass: 'icon-n-text',
  chartType: 'text',
  width: 200,
  height: 50,
  bdpx: 0,
  bgClr: '',
  clr: '#666f8b',
  bdClr: '#3d445a',
  ctName: '请输入文本框内容',
  chartData: {}
},
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
  clr: '#c2c6d7',
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
  barClrs: ['#1fc3ce', '#1fc3ce'],
  clr: '#1fc3ce',
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
{
  text: '拓扑',
  imgClass: 'icon-n-topo',
  chartType: 'topo',
  width: 300,
  height: 300,
  tpId: '',
  chartData: {}
},
{
  text: '图片',
  imgClass: 'icon-n-exportPicture',
  chartType: 'image',
  width: 300,
  height: 300,
  imgSrc: '',
  chartData: {}
},
{
  text: '边框',
  imgClass: 'icon-n-rect',
  chartType: 'border',
  borderType: 'simple', // 内置stable, 简单simple
  imgSrc: '',
  width: 300,
  height: 300,
  bdpx: 1,
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
{
  text: '水波图',
  width: 300,
  height: 300,
  imgClass: 'icon-n-waveball',
  chartType: 've-gauge',
  subType: 'progress',
  secondType: 'liquidfill',
  // chartType: 'liquidfill',
  bdpx: 4,
  bgClr: '#156acf',
  clr: 'rgba(221, 221, 221, 0.9)',
  bdClr: '#666f8b',
  fontSize: 28,
  ctLegendShow: 'true',
  chartData: morenData.single
},
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
  visualPosition: 'left',
  scatterPoint: [{ name: '北京', value: [116.405285, 39.904989, 2] }], // 带有地理位置的散点数据
  chartData: morenData.mapData
},
{
  text: '视频流',
  imgClass: 'icon-n-video',
  chartType: 'video',
  width: 300,
  height: 300,
  videoType: 'local',
  videoSrc: ''
}
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
