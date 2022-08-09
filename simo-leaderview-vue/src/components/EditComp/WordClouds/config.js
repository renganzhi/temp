export default {
  'item': {
    'text': '字符云',
    'imgClass': 'icon-n-ndge',
    'chartType': 'WordClouds',
    'chartData': {
      'columns': [
        'name',
        'value'
      ],
      'rows': [
        {
          'name': '游戏机',
          'value': 188
        },
        {
          'name': '模拟辅助',
          'value': 166
        },
        {
          'name': '个护美容',
          'value': 942
        },
        {
          'name': '护肤品',
          'value': 177
        },
        {
          'name': '彩妆',
          'value': 133
        },
        {
          'name': '美发',
          'value': 80
        },
        {
          'name': '香水',
          'value': 50
        },
        {
          'name': '个人护理',
          'value': 46
        },
        {
          'name': '美甲',
          'value': 26
        },
        {
          'name': 'SPA美体',
          'value': 21
        },
        {
          'name': '花鸟萌宠',
          'value': 914
        },
        {
          'name': '绿植花卉',
          'value': 311
        }
      ]
    },
    'textColor': [
      '#2d98f1',
      '#32c5e9',
      '#67e0e3',
      '#9fe6b8',
      '#ffdb5c',
      '#ffb092'],
    'gridSize': 5,
    'maxSize': 55
    // 'minSize': 25
  },
  'styles': {
    'base': [
      {
        'name': '图元样式',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '文字间隔',
            'key': 'gridSize',
            'tag': 'input'
          },
          {
            'name': '字体最大值',
            'key': 'maxSize',
            'tag': 'input'
          },
          //   {
          //     'name': '字体最小值',
          //     'key': 'minSize',
          //     'tag': 'input'
          //   },
          {
            'name': '文字颜色',
            'key': 'textColor',
            'tag': 'ColorArray'
          }
        ]
      }
    ]
  }
}
