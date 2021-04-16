
// const bubbleData = Array.from({ length: 30 }, (v, i) => ({
//   name: String(i + 1),
//   value: Math.floor(Math.random() * 60) + 20
// }))

const speedOptions = Array.from({ length: 20 }, (v, i) => ({
  name: i + 1,
  value: i + 1
}))

export default {
  item: {
    text: '气泡图',
    imgClass: 'icon-bubble',
    chartType: 'bubble',
    width: 660,
    height: 300,
    speed: 2,
    chartData: {
      'rows': [
        {
          'name': '主机',
          'value': 59
        },
        {
          'name': '服务器',
          'value': 56
        },
        {
          'name': '网络设备',
          'value': 69
        },
        {
          'name': '存储',
          'value': 28
        },
        {
          'name': '备份系统',
          'value': 30
        },
        {
          'name': '数据库',
          'value': 56
        },
        {
          'name': '中间件',
          'value': 54
        },
        {
          'name': '数据库集群',
          'value': 52
        },
        {
          'name': '应用',
          'value': 43
        }
      ]
    }
  },
  styles: {
    base: [
      {
        'name': '图表样式',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '动画速度',
            'key': 'speed',
            'tag': 'select',
            'options': speedOptions
          }
        ]
      }
    ]
  }

}
