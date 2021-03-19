export default{
  'item': {
    'text': '进度条',
    'imgClass': 'icon-n-progress',
    'chartType': 'NewProgress',
    'width': 300,
    'height': 50,
    'bgClr': '#062e4d',
    'barClr': '#1fc3ce',
    'barClrs': ['#1068ed', '#69b1f3'],
    'clr': '#47b8f5',
    'colorful': 'true',
    'ctLegendShow': 'true',
    'chartData': {
      'name': '繁忙度',
      'unit': '%',
      'value': 60
    },
    'proHeight': 16,
    'radius': 8
  },
  'styles': {
    'base': [
      {
        'name': '图例配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '图例可见性',
          'key': 'ctLegendShow',
          'tag': 'select',
          'options': [
            {
              'name': '显示',
              'value': 'true'
            },
            {
              'name': '隐藏',
              'value': 'false'
            }
          ]
        }]
      },
      {
        'name': '图表样式',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '底色',
            'key': 'bgClr',
            'tag': 'Color'
          },
          {
            'name': '字号',
            'key': 'fontSize',
            'tag': 'select',
            'options': [
              {
                'name': '12',
                'value': 12
              },
              {
                'name': '13',
                'value': 13
              },
              {
                'name': '14',
                'value': 14
              },
              {
                'name': '16',
                'value': 16
              },
              {
                'name': '18',
                'value': 18
              },
              {
                'name': '20',
                'value': 20
              },
              {
                'name': '24',
                'value': 24
              },
              {
                'name': '26',
                'value': 26
              },
              {
                'name': '28',
                'value': 28
              },
              {
                'name': '30',
                'value': 30
              },
              {
                'name': '36',
                'value': 36
              },
              {
                'name': '40',
                'value': 40
              },
              {
                'name': '48',
                'value': 48
              },
              {
                'name': '54',
                'value': 54
              },
              {
                'name': '60',
                'value': 60
              },
              {
                'name': '72',
                'value': 72
              },
              {
                'name': '84',
                'value': 84
              },
              {
                'name': '88',
                'value': 88
              }
            ]
          },
          {
            'name': '底色',
            'key': 'colorful',
            'ColorKey': 'barClr',
            'DoubleColorKey': 'barClrs',
            'tag': 'GradualColor'
          },
          {
            'name': '高度',
            'key': 'proHeight',
            'tag': 'input'
          },
          {
            'name': '圆角',
            'key': 'radius',
            'tag': 'input'
          }
        ]
      }
    ]
  }
}
