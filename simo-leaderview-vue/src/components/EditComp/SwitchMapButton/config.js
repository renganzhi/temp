export default{
  'item': {
    'text': '地图切换',
    'imgClass': 'icon-n-text',
    'chartType': 'SwitchMapButton',
    'width': 870,
    'height': 660,
    'size': 'big',
    animation: true,
    'chartData': {}
  },
  'styles': {
    'base': [
      {
        'name': '其他设置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '是否播放动画',
            'key': 'animation',
            'tag': 'select',
            'options': [
              {
                'name': '开启',
                'value': true
              },
              {
                'name': '关闭',
                'value': false
              }
            ]
          }
        ]
      }
    ]
  }
}
