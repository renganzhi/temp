export default {
  'item': {
    'text': '轮播图片',
    'imgClass': 'icon-sliderimg',
    'chartType': 'ppt',
    'width': 300,
    'height': 300,
    'showType': '1',
    'autoplay': true,
    'interval': 2,
    'loop': true,
    'srcList': [],
    'linkId': '',
    'chartData': {}
  },
  'styles': {
    'base': [
      {
        'name': '图表样式',
        'tag': 'Hint'
      },
      {
        'name': '是否自动播放',
        'key': 'autoplay',
        'tag': 'select',
        'options': [
          {
            'name': '自动播放',
            'value': true
          },
          {
            'name': '禁止自动播放',
            'value': false
          }
        ]
      },
      {
        'name': '播放速度',
        'key': 'interval',
        'tag': 'input',
        'type': 'number',
        'unit': '秒',
        'options': [
          {
            'name': '自动播放',
            'value': true
          },
          {
            'name': '禁止自动播放',
            'value': false
          }
        ],
        'parentKey': {
          'autoplay': true
        }
      },
      {
        'name': '是否循环',
        'key': 'loop',
        'tag': 'select',
        'options': [
          {
            'name': '循环',
            'value': true
          },
          {
            'name': '不循环',
            'value': false
          }
        ]
      }
    ]
  }
}
