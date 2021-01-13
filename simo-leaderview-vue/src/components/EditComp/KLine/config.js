export default {
  'item': {
    'text': 'K线图',
    'imgClass': 'icon-candlestick',
    'chartType': 'KLine',
    'width': 300,
    'height': 300,
    'openlegend': true,
    'legendColor': '#FFF',
    'legendSize': 12,
    'legendStation': 'top',
    'axisLinecolor': 'white',
    'axisLabelcolor': 'white',
    'axisLabelfontSize': 12,
    'XaxisLabelrotate': 0,
    'YaxisLabelrotate': 0,
    'dataZoom': true,
    'dataZoomFontColor': 'white',
    'dataZoomAreaStyle': '#8392A5',
    'KlineColorYang': ['rgba(255, 38, 38, 0.44)', '#dc4908'],
    'KlineColorYing': ['rgba(99, 174, 99, 0.52)', '#bcbce9'],
    'borderColorYang': 'rgba(59, 56, 56, 0.75)',
    'borderColorYing': 'rgba(58, 102, 58, 0.65)',
    'ifGradual': false,
    'LinerColor': [
      '#37a2da',
      '#32c5e9',
      '#67e0e3',
      '#9fe6b8',
      '#ffdb5c'],
    'chartData': {
      'dataArry': {
        'name': ['K线数据1', '直线数据1'],
        'data': [
          ['2017-10-24', [20, 34, 10, 38], 20],
          ['2017-10-25', [40, 35, 30, 50], 35],
          ['2017-10-26', [31, 38, 33, 44], 32],
          ['2017-10-27', [38, 15, 5, 12], 22],
          ['2017-10-28', [28, 6, 15, 22], 21],
          ['2017-10-29', [40, 35, 30, 50], 35],
          ['2017-10-30', [31, 38, 33, 44], 32],
          ['2017-10-31', [38, 15, 5, 12], 22],
          ['2017-11-01', [28, 6, 15, 22], 21],
          ['2017-11-02', [40, 35, 30, 50], 35],
          ['2017-11-03', [31, 38, 33, 44], 32],
          ['2017-11-04', [38, 15, 5, 12], 22],
          ['2017-11-05', [28, 6, 15, 22], 21],
          ['2017-11-06', [40, 35, 30, 50], 35],
          ['2017-11-07', [31, 38, 33, 44], 32],
          ['2017-11-08', [38, 15, 5, 12], 22],
          ['2017-11-09', [28, 6, 15, 22], 21],
          ['2017-11-10', [40, 35, 30, 50], 35],
          ['2017-11-11', [31, 38, 33, 44], 32],
          ['2017-11-12', [38, 15, 5, 12], 22],
          ['2017-11-13', [28, 6, 15, 22], 21],
          ['2017-11-14', [40, 35, 30, 50], 35],
          ['2017-11-15', [31, 38, 33, 44], 32],
          ['2017-11-16', [38, 15, 5, 12], 22],
          ['2017-11-17', [28, 6, 15, 22], 21],
          ['2017-11-18', [40, 35, 30, 50], 35],
          ['2017-11-19', [31, 38, 33, 44], 32],
          ['2017-11-20', [38, 15, 5, 12], 22],
          ['2017-11-21', [28, 6, 15, 22], 21],
          ['2017-11-22', [40, 35, 30, 50], 35],
          ['2017-11-23', [31, 38, 33, 44], 32],
          ['2017-11-24', [38, 15, 5, 12], 22]
        ]
      }
    }
  },
  'styles': {
    'base': [
      {
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
            'name': '上方居中',
            'value': 'top'
          },
          {
            'name': '下放居中',
            'value': 'bottom'
          }
        ]
      },
      {
        'name': '显示时间轴',
        'key': 'dataZoom',
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
        'name': '时间轴字颜色',
        'key': 'dataZoomFontColor',
        'tag': 'Color'
      },
      {
        'name': '时间轴阴影色',
        'key': 'dataZoomAreaStyle',
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
        'name': 'X轴刻度倾斜度',
        'key': 'XaxisLabelrotate',
        'tag': 'input'
      },
      {
        'name': 'Y轴刻度倾斜度',
        'key': 'YaxisLabelrotate',
        'tag': 'input'
      },
      {
        'name': 'K线图阳线颜色',
        'key': 'KlineColorYang',
        'tag': 'GradientColor'
      },
      {
        'name': '阳线边框颜色',
        'key': 'borderColorYang',
        'tag': 'Color'
      },
      {
        'name': 'K线图阴线颜色',
        'key': 'KlineColorYing',
        'tag': 'GradientColor'
      },
      {
        'name': '阴线边框颜色',
        'key': 'borderColorYing',
        'tag': 'Color'
      }, {
        'name': '直线颜色',
        'key': 'LinerColor',
        'tag': 'ColorArray'
      }
    ]
  }
}
