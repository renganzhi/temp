import {morenData} from '@/views/Edit/chartJson'

const table = {
  name: '表格',
  child: [
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
      OneLineType: 'default',
      OneLineSize: 86,
      LineSizeArry: [],
      AlarmChart: '',
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
      LineSizeArry: [],
      AlarmChart: '',
      AlarmNumType: 'greater',
      AlarmNum: '',
      AlarmColor: 'red',
      bdpx: 0,
      chartData: morenData.table
    }
  ]
}

export default table
