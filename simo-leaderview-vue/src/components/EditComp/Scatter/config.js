export default {
  'item': {
    'text': '散点图',
    'imgClass': 'icon-scatter',
    'chartType': 'Scatter',
    'height': 300,
    'width': 400,
    'openlegend': true,
    'legendColor': '#828bac',
    'DanweiColor': '#828bac',
    'DanweiSize': 16,
    'legendSize': 12,
    'legendStation': 'top',
    'splitLine': true,
    'legendY': 15,
    'splitLinetype': 'dashed',
    'splitLinecolor': '#828bac',
    'axisLinecolor': '#828bac',
    'axisLabelcolor': '#828bac',
    'tooltipShow': true,
    'tooltipBackColor': '#57625d',
    'tooltipColor': '#ebedf6',
    'tooltipSize': 14,
    'axisLabelfontSize': 12,
    'XaxisLabelrotate': 0,
    'YaxisLabelrotate': 0,
    'ScatterColor': [
      '#089fef',
      '#ffdb5c',
      '#32c5e9',
      '#67e0e3',
      '#9fe6b8'],
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
    'ifGradual': 'false',
    'chartData': {
      'unit': '次',
      'unitX': '日期',
      'dataArray': [{
        'name': '正常',
        'points': [
          [1, 3, 5],
          [2, 4, 10],
          [3, 4, 5],
          [4, 4, 15],
          [5, 5, 16],
          [6, 7, 9],
          [7, 5, 5],
          [8, 9, 5],
          [9, 7, 5],
          [10, 8, 5],
          [11, 7, 5],
          [12, 10, 16],
          [13, 7, 12],
          [14, 7, 2]
        ]
      }, {
        'name': '告警',
        'points': [
          [1, 8, 5],
          [2, 6, 5],
          [3, 7, 12],
          [4, 9, 1],
          [5, 8, 5],
          [6, 9, 2],
          [7, 9, 8],
          [8, 6, 5],
          [9, 10, 5],
          [10, 6, 6],
          [11, 7, 5],
          [12, 7, 11],
          [13, 4, 5],
          [14, 7, 5],
          [15, 4, 10]
        ]
      }
      ]
    }
  },
  'styles': {
    'base': [
      {
        'name': '图例配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '是否显示图例',
          'key': 'openlegend',
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
        }, {
          'name': '图例颜色',
          'key': 'legendColor',
          'parentKey': {
            'openlegend': true
          },
          'tag': 'Color'
        }, {
          'name': '图例字体大小',
          'key': 'legendSize',
          'parentKey': {
            'openlegend': true
          },
          'tag': 'input'
        },
        {
          'name': '图例位置',
          'key': 'legendStation',
          'tag': 'select',
          'parentKey': {
            'openlegend': true
          },
          'options': [
            {
              'name': '顶部居中',
              'value': 'top'
            },
            {
              'name': '底部居中',
              'value': 'bottom'
            }
          ]
        },
        {
          'name': '图元间隙',
          'key': 'legendY',
          'tag': 'input'
        }
        ]
      },
      {
        'name': 'tips配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '是否显示tips',
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
          'key': 'tooltipBackColor',
          'parentKey': {
            'tooltipShow': true
          },
          'tag': 'Color'
        },
        {
          'name': 'tips字体色',
          'key': 'tooltipColor',
          'parentKey': {
            'tooltipShow': true
          },
          'tag': 'Color'
        },
        {
          'name': 'tips字体大小',
          'key': 'tooltipSize',
          'parentKey': {
            'tooltipShow': true
          },
          'tag': 'input'
        } ]
      },
      {
        'name': '背景线配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '显示背景线',
          'key': 'splitLine',
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
          'name': '背景线类型',
          'key': 'splitLinetype',
          'parentKey': {
            'splitLine': true
          },
          'tag': 'select',
          'options': [
            {
              'name': '实线',
              'value': 'solid'
            },
            {
              'name': '虚线',
              'value': 'dashed'
            },
            {
              'name': '点状线',
              'value': 'dotted'
            }
          ]
        },
        {
          'name': '背景线颜色',
          'key': 'splitLinecolor',
          'parentKey': {
            'splitLine': true
          },
          'tag': 'Color'
        },
        {
          'name': '坐标轴颜色',
          'key': 'axisLinecolor',
          'tag': 'Color'
        },
        {
          'name': '刻度字体颜色',
          'key': 'axisLabelcolor',
          'tag': 'Color'
        },
        {
          'name': '刻度字体大小',
          'key': 'axisLabelfontSize',
          'tag': 'input'
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
        },
        {
          'name': 'X轴刻度倾斜度',
          'key': 'XaxisLabelrotate',
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
          'name': 'Y轴刻度倾斜度',
          'key': 'YaxisLabelrotate',
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
        }, {
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
