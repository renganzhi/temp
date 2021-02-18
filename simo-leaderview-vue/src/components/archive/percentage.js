import {morenData} from '@/views/Edit/chartJson'
import liquidfill from '@/components/EditComp/liquidfill/config.js'
import Dashboard from '@/components/EditComp/Dashboard/config.js'

const percentage = {
  name: '百分比图',
  child: [
    {
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
      detailwidth: 12,
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
      detailColor: '#52a8c0',
      detailwidth: 12,
      ctLegendSize: '16',
      ctLegendColor: '#666f8b',
      axisLabelSize: '16',
      chartData: morenData.single
    },
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
    //   ctLegendShow: false,
    //   bgClr: '#657992',
    //   chartData: morenData.single
    // },
    {
      text: '进度条',
      imgClass: 'icon-n-progress',
      chartType: 'progress',
      width: 300,
      height: 50,
      bgClr: '#062e4d',
      barClr: '#1fc3ce',
      barClrs: ['#1068ed', '#69b1f3'],
      clr: '#47b8f5',
      colorful: 'true',
      chartData: morenData.single,
      proHeight: 16,
      radius: 8
    },
    liquidfill.item
  ]
}

export default percentage
