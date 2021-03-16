export default{
  'item': {
    'text': '指标展示',
    'imgClass': 'icon-n-number',
    'chartType': 'NewNumber',
    'fontSize': 36,
    'fontFamily': 'asn',
    'clr': '#30a1ff',
    'width': 300,
    'height': 90,
    'ctLegendShow': 'true',
    'chartData': {
      'name': '繁忙度',
      'unit': '%',
      'value': 60
    }
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
            'name': '字体颜色',
            'key': 'clr',
            'tag': 'Color'
          }
        ]
      },
      {
        'name': '数字配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '数字样式',
          'key': 'fontFamily',
          'tag': 'fontFamily'
        }]
      }
    ]
  }
}
