import Mock from 'mockjs'

const rows = Mock.mock({
  'list|5': [
    {
      'name|+1': ['交换机', '路由器', '服务器', '主机', '其他设备'],
      'value|10-100': 100,
      'alarm|1-10': 10
    }
  ]
})['list']

export default {
  item: {
    text: '金字塔',
    imgClass: 'icon-pyramid',
    chartType: 'pyramid',
    colorful: false,
    width: 350,
    height: 250,
    ifGradual: 'false',
    chartData: {
      rows
    }
  }
}
