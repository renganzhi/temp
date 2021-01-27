export default {
  'item': {
    'text': '仪表盘',
    'imgClass': 'icon-n-gauge',
    'chartType': 'Dashboard',
    'ifGradual': 'false',
    'DashboardType': 1,
    'ctLegendSize': 16,
    'textSize': 30,
    'textColor': '#46e2fd',
    'textHeight': '80%',
    'ctLegendColor': '#46e2fd',
    'lineColor': ['rgba(0,44,254,1)', '#56f2f5'],
    'lineFontColor': '#468EFD',
    'lineFontSize': 14,
    'bgClr': 'rgba(107, 121, 139, 0.29)',
    'chartData': {
      'name': '繁忙度',
      'unit': '%',
      'value': 60
    }
  },
  'styles': {
    'base': [
      {
        'name': '仪表盘类型',
        'key': 'DashboardType',
        'tag': 'select',
        'options': [
          {
            'name': '类型一',
            'value': 1
          },
          {
            'name': '类型二',
            'value': 2
          },
          {
            'name': '类型三',
            'value': 3
          },
          {
            'name': '类型四',
            'value': 4
          }
        ]
      },
      {
        'name': '图例配置',
        'tag': 'Hint'
      },
      {
        'name': '图例文字颜色',
        'key': 'ctLegendColor',
        'tag': 'Color'
      },
      {
        'name': '图例字体大小',
        'key': 'ctLegendSize',
        'tag': 'select',
        'options': [
          {
            'name': '14',
            'value': 14
          },
          {
            'name': '16',
            'value': 16
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
            'name': '28',
            'value': 28
          },
          {
            'name': '30',
            'value': 30
          }
        ]
      },
      {
        'name': '底色',
        'key': 'bgClr',
        'tag': 'Color'
      },
      {
        'name': '字体颜色',
        'key': 'textColor',
        'tag': 'Color'
      },
      {
        'name': '字体大小',
        'key': 'textSize',
        'tag': 'select',
        'options': [
          {
            'name': '14',
            'value': 14
          },
          {
            'name': '20',
            'value': 20
          },
          {
            'name': '26',
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
            'name': '42',
            'value': 42
          }
        ]
      },
      {
        'name': '字体高度',
        'key': 'textHeight',
        'parentKey': {
          'DashboardType': 2
        },
        'tag': 'select',
        'options': [
          {
            'name': '居上',
            'value': '80%'
          },
          {
            'name': '居中',
            'value': '90%'
          },
          {
            'name': '居下',
            'value': '100%'
          }
        ]
      },
      {
        'name': '刻度轴颜色',
        'key': 'lineColor',
        'tag': 'GradientColor'
      },
      {
        'name': '刻度颜色',
        'key': 'lineFontColor',
        'tag': 'Color'
      },
      {
        'name': '刻度大小',
        'key': 'lineFontSize',
        'tag': 'select',
        'options': [
          {
            'name': '14',
            'value': 14
          },
          {
            'name': '16',
            'value': 16
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
            'name': '28',
            'value': 28
          },
          {
            'name': '30',
            'value': 30
          }
        ]
      }
    ]
  }
}
