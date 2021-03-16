
const bubbleData = Array.from({ length: 30 }, (v, i) => ({
  name: String(i + 1),
  value: Math.floor(Math.random() * 60) + 20
}))

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
      rows: bubbleData
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
