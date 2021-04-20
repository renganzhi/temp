export default {
  'item': {
    'text': '分组柱图',
    'imgClass': 'icon-n-grouphistogram',
    'chartType': 'NewGroupHistogram',
    'ifGradual': 'false',
    'splitShow': false,
    'splitColor': 'rgba(108, 108, 108, 0.51)',
    'ctLegendShow': true,
    'splitSize': 1,
    'ctLegendSize': '16',
    'barWidth': 7,
    'barGap': 0.1,
    'ctLegendColor': '#666f8b',
    'axisLabelSize': '14',
    'DanweiColor': '#828bac',
    'DanweiSize': 16,
    'minInterval': '',
    'legendY': 90,
    'gridTop': 10,
    'gridBotton': 10,
    'gridLeft': 10,
    'gridRight': 10,
    'tooltipShow': 'true',
    'tooltipBackColor': '#57625d',
    'tooltipTextColor': '#fff',
    'tooltipfontSize': 14,
    'subType': 'groupHistogram',
    'rotate': 0,
    'formatterType': '0',
    'ScatterColor': [
      '#37a2da',
      '#30a4f9',
      '#5cfbff',
      '#af8af3',
      '#f5739c',
      'ffdf91',
      '5c84e7'],
    'DScatterColor': [
      ['#6fcaf7', '#0c79c5'],
      ['#8feee5', '#1bbcae'],
      ['#fa8d76', '#db4222'],
      ['#af8af3', '#874edc'],
      ['#f5739c', '#f31d53'],
      ['#ffdf91', '#eeb01b'],
      ['#5c84e7', '#144fe5'],
      ['#85f8c0', '#62dc26']
    ],
    'chartData': {
      'columns': ['资源', 'CPU利用率', '内存利用率', '健康度'],
      'unit': '%',
      'rows': [{
        '资源': '192.168.2.32',
        'CPU利用率': 80,
        '内存利用率': 90,
        '健康度': 80
      },
      {
        '资源': '192.168.2.132',
        'CPU利用率': 60,
        '内存利用率': 20,
        '健康度': 70
      },
      {
        '资源': '192.168.2.62',
        'CPU利用率': 10,
        '内存利用率': 60,
        '健康度': 80
      },
      {
        '资源': '192.168.2.200',
        'CPU利用率': 90,
        '内存利用率': 30,
        '健康度': 40
      },
      {
        '资源': '192.168.2.72',
        'CPU利用率': 50,
        '内存利用率': 60,
        '健康度': 70
      },
      {
        '资源': '192.168.2.190',
        'CPU利用率': 45,
        '内存利用率': 76,
        '健康度': 67
      }
      ]
    }
  },
  'styles': {
    'base': [
      {
        'name': '图例配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '柱体宽度',
            'key': 'barWidth',
            'tag': 'input',
            'min': 0
          },
          {
            'name': '柱体间距',
            'key': 'barGap',
            'tag': 'select',
            'options': [
              {
                'name': '0',
                'value': 0
              },
              {
                'name': '0.1',
                'value': 0.1
              },
              {
                'name': '0.2',
                'value': 0.2
              },
              {
                'name': '0.4',
                'value': 0.4
              },
              {
                'name': '0.6',
                'value': 0.6
              },
              {
                'name': '1',
                'value': 1
              }
            ]
          },
          {
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
                'name': '10',
                'value': 10
              },
              {
                'name': '12',
                'value': 12
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
            'name': '图例字颜色',
            'key': 'ctLegendColor',
            'parentKey': {
              'ctLegendShow': true
            },
            'tag': 'Color'
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
            'name': '图元上边距',
            'key': 'gridTop',
            'tag': 'input'
          },
          {
            'name': '图元下边距',
            'key': 'gridBotton',
            'tag': 'input'
          },
          {
            'name': '图元左边距',
            'key': 'gridLeft',
            'tag': 'input'
          },
          {
            'name': '图元右边距',
            'key': 'gridRight',
            'tag': 'input'
          }
        ]
      },
      {
        'name': 'tips配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': 'tips可见性',
            'key': 'tooltipShow',
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
            'name': 'tips背景色',
            'parentKey': {
              'tooltipShow': true
            },
            'key': 'tooltipBackColor',
            'tag': 'Color'
          },
          {
            'name': 'tips字体色',
            'key': 'tooltipTextColor',
            'parentKey': {
              'tooltipShow': true
            },
            'tag': 'Color'
          },
          {
            'name': 'tips字体大小',
            'key': 'tooltipfontSize',
            'parentKey': {
              'tooltipShow': true
            },
            'tag': 'input'
          }
        ]
      },
      {
        'name': '坐标线配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '坐标线可见性',
            'key': 'splitShow',
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
            'name': '坐标刻度类型',
            'key': 'minInterval',
            'tag': 'select',
            'options': [
              {
                'name': '整数',
                'value': 1
              },
              {
                'name': '自适应',
                'value': ''
              }
            ]
          },
          {
            'name': '坐标线颜色',
            'key': 'splitColor',
            'tag': 'Color'
          },
          {
            'name': '线条粗细',
            'key': 'splitSize',
            'tag': 'select',
            'options': [
              {
                'name': '1',
                'value': 1
              },
              {
                'name': '2',
                'value': 2
              },
              {
                'name': '3',
                'value': 3
              }
            ]
          },
          {
            'name': '坐标文字颜色',
            'key': 'legendColor',
            'tag': 'Color'
          },
          {
            'name': '坐标文字大小',
            'key': 'axisLabelSize',
            'tag': 'select',
            'options': [
              {
                'name': '8',
                'value': 8
              },
              {
                'name': '10',
                'value': 10
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
            'name': '坐标单位颜色',
            'key': 'DanweiColor',
            'tag': 'Color'
          },
          {
            'name': '坐标单位大小',
            'key': 'DanweiSize',
            'tag': 'select',
            'options': [
              {
                'name': '8',
                'value': 8
              },
              {
                'name': '10',
                'value': 10
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
      },
      {
        'name': '坐标轴样式',
        'tag': 'Hint',
        'childoption': [{
          'name': 'x轴标注倾斜',
          'key': 'rotate',
          'tag': 'select',
          'options': [
            {
              'name': '0',
              'value': 0
            },
            {
              'name': '10',
              'value': 10
            },
            {
              'name': '20',
              'value': 20
            },
            {
              'name': '30',
              'value': 30
            },
            {
              'name': '40',
              'value': 40
            },
            {
              'name': '-10',
              'value': -10
            },
            {
              'name': '-20',
              'value': -20
            },
            {
              'name': '-30',
              'value': -30
            },
            {
              'name': '-40',
              'value': -40
            }
          ]
        },
        {
          'name': 'x轴标注剪裁',
          'key': 'formatterType',
          'tag': 'select',
          'options': [
            {
              'name': '自适应',
              'value': '0'
            },
            {
              'name': '不裁剪',
              'value': '1'
            }
          ]
        }
        ]
      },
      {
        'name': '颜色配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '颜色类型',
          'key': 'ifGradual',
          'tag': 'select',
          'options': [
            {
              'name': '渐变',
              'value': 'true'
            },
            {
              'name': '单色',
              'value': 'false'
            }
          ]
        }, {
          'name': '颜色配置',
          'key': 'ScatterColor',
          'parentKey': {
            'ifGradual': 'false'
          },
          'tag': 'ColorArray'
        }, {
          'name': '颜色配置',
          'key': 'DScatterColor',
          'parentKey': {
            'ifGradual': 'true'
          },
          'tag': 'ColorArray'
        }]
      }
    ]
  }
}
