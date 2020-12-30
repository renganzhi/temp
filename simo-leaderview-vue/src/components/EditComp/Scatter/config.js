export default {
  'item': {
    'text': '散点图',
    'imgClass': 'icon-n-video',
    'chartType': 'Scatter',
    'width': 300,
    'height': 300,
    'openlegend': true,
    'legendColor': '#FFF',
    'legendSize': 12,
    'legendStation': 'top',
    'splitLine': true,
    'splitLinetype': 'dashed',
    'splitLinecolor': 'white',
    'axisLinecolor': 'white',
    'axisLabelcolor': 'white',
    'axisLabelfontSize': 12,
    'XaxisLabelrotate': 0,
    'YaxisLabelrotate': 0,
    'ifGradual': 'false',
    'chartData': {
      'dataArry': [{
        'name': '测试数据1',
        'points': [
          [10.0, 8.04, 5],
          [8.07, 6.95, 5],
          [13.0, 7.58, 12],
          [9.05, 8.81, 1],
          [11.0, 8.33, 5],
          [14.0, 7.66, 2],
          [13.4, 6.81, 5],
          [10.0, 6.33, 5],
          [14.0, 8.96, 5],
          [12.5, 6.82, 6],
          [9.15, 7.20, 5],
          [11.5, 7.20, 11],
          [3.03, 4.23, 5],
          [12.2, 7.83, 5],
          [2.02, 4.47, 10],
          [1.05, 3.33, 5],
          [4.05, 4.96, 15],
          [6.03, 7.24, 9],
          [12.0, 6.26, 5],
          [12.0, 8.84, 16],
          [7.08, 5.82, 5],
          [5.02, 5.68, 16]
        ]
      }, {
        'name': '测试数据2',
        'points': [
          [11.0, 8.04, 5],
          [9.07, 6.95, 5],
          [15.0, 7.58, 12],
          [13.05, 8.81, 1],
          [16.0, 8.33, 5],
          [14.0, 7.66, 2],
          [15.4, 6.81, 5],
          [17.0, 6.33, 5],
          [16.0, 8.96, 5],
          [16.5, 6.82, 6],
          [4.15, 7.20, 5],
          [15.5, 7.20, 11],
          [9.03, 4.23, 5],
          [15.2, 7.83, 5],
          [9.02, 4.47, 10],
          [6.05, 3.33, 5],
          [2.05, 4.96, 15],
          [9.03, 7.24, 9],
          [16.0, 6.26, 5],
          [16.0, 8.84, 16],
          [8.08, 5.82, 5],
          [6.02, 5.68, 16]
        ]
      }
      ]
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
            'name': '圆点',
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
        'name': 'X轴刻度倾斜度',
        'key': 'XaxisLabelrotate',
        'tag': 'input'
      },
      {
        'name': 'Y轴刻度倾斜度',
        'key': 'YaxisLabelrotate',
        'tag': 'input'
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
        'name': '饼图颜色',
        'key': 'ScatterColor',
        'tag': 'ColorArray'
      }
    ]
  }
}
