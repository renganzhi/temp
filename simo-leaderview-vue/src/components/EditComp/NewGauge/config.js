export default{
  'item': {
    'text': '目标占比图',
    'width': 200,
    'height': 200,
    'imgClass': 'icon-n-percent',
    'chartType': 'NewGauge',
    'ifGradual': 'false',
    'subType': 'progress',
    'bgClr': '#657992',
    'legendY': 86,
    'fontSize': 24,
    'NameShow': true,
    'NameSize': 24,
    'NamelegendY': 43,
    'colorful': 'false',
    'NameColor': '#52a8c0',
    'ctLegendShow': true,
    'detailColor': '#52a8c0',
    'barClr': '#37a2da',
    'barClrs': ['#6fcaf7', '#0c79c5'],
    'detailwidth': 12,
    'ctLegendSize': '16',
    'ctLegendColor': '#adb1be',
    'axisLabelSize': '16',
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
              'value': true
            },
            {
              'name': '隐藏',
              'value': false
            }
          ]
        },
        {
          'name': '图例字体大小',
          'key': 'ctLegendSize',
          'parentKey': {
            'ctLegendShow': true
          },
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
          'name': '图例高度',
          'parentKey': {
            'ctLegendShow': true
          },
          'key': 'legendY',
          'tag': 'input'
        },
        {
          'name': '图例字颜色',
          'key': 'ctLegendColor',
          'parentKey': {
            'ctLegendShow': true
          },
          'tag': 'Color'
        }]
      },
      {
        'name': '文字配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '文字可见性',
            'key': 'NameShow',
            'tag': 'select',
            'options': [
              {
                'name': '显示',
                'value': true
              },
              {
                'name': '隐藏',
                'value': false
              }
            ]
          },
          {
            'name': '文字字体大小',
            'key': 'NameSize',
            'parentKey': {
              'NameShow': true
            },
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
            'name': '文字高度',
            'parentKey': {
              'NameShow': true
            },
            'key': 'NamelegendY',
            'tag': 'input'
          },
          {
            'name': '文字字颜色',
            'key': 'NameColor',
            'tag': 'Color'
          }
        ]
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
            'name': '环宽',
            'key': 'detailwidth',
            'tag': 'input'
          },
          {
            'name': '颜色配置',
            'key': 'colorful',
            'ColorKey': 'barClr',
            'DoubleColorKey': 'barClrs',
            'tag': 'GradualColor'
          }
        ]
      }
    ]
  }
}
