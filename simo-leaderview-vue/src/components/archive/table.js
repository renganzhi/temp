import {morenData} from '@/views/Edit/chartJson'

const table = {
  name: '表格',
  child: [
    {
      text: '表格',
      imgClass: 'icon-n-table',
      chartType: 'table',
      hdBgClr: '#1c2132',
      hdClr: '#cad6dd',
      hdfontSize: 12,
      bgClr: '#22283a',
      clr: '#919cc1',
      bdClr: '#c2c6d7',
      bdpx: 0,
      chartData: morenData.table
    },
    {
      text: '轮播表格',
      imgClass: 'icon-slidertable',
      chartType: 'table',
      thirdType: 'moveTable',
      direction: 'top',
      speed: 2,
      pageNum: 5,
      hdBgClr: '#1c2132',
      hdClr: '#cad6dd',
      hdfontSize: 12,
      bgClr: '#22283a',
      clr: '#919cc1',
      bdClr: '#c2c6d7',
      bdpx: 0,
      chartData: morenData.table
    }
  ]
}

export default table
