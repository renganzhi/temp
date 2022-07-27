export default {
  'item': {
    'text': '饼图',
    'imgClass': 'icon-n-pie',
    'chartType': 'NewPie',
    'ifGradual': 'false',
    'ctLegendSize': '16',
    'ctLegendColor': '#666f8b',
    'axisLabelSize': '16',
    'ifEidetColor': false,
    'ctLegendShow': true,
    'legendY': 85,
    'radius': 50,
    'detailwidth': 12,
    'borderRadius': 0,
    'ringWidth': 50,
    'isRing': false,
    'legendwidth': 100,
    'legendX': 75,
    'gridTop': 50,
    'gridLeft': 50,
    'showline': true,
    'showword': true,
    'showwordSize': 12,
    'tooltipShow': true,
    'tooltipBackColor': '#57625d',
    'tooltipTextColor': '#fff',
    'tooltipfontSize': 14,
    'pieType': '饼图',
    'roseType': 'area',
    'LineColorArray': [
      '#2d98f1',
      '#32c5e9',
      '#67e0e3',
      '#9fe6b8',
      '#ffdb5c',
      '#ffb092'],
    'DLineColorArray': [
      ['rgba(213, 153, 17, 0.52)', '#be4d24'],
      ['rgba(2, 210, 255, 0.49)', '#1bbcae'],
      ['#fa8d76', '#db4222'],
      ['#af8af3', '#874edc'],
      ['#f5739c', '#f31d53'],
      ['#ffdf91', '#eeb01b'],
      ['#5c84e7', '#144fe5'],
      ['#85f8c0', '#62dc26']
    ],
    'chartData': {
      'columns': [
        '告警级别',
        '数量'
      ],
      'unit': '次',
      'rows': [
        {
          '告警级别': '致命',
          '数量': 233
        },
        {
          '告警级别': '严重',
          '数量': 123
        },
        {
          '告警级别': '警告',
          '数量': 23
        },
        {
          '告警级别': '一般',
          '数量': 155
        },
        {
          '告警级别': '次要',
          '数量': 103
        },
        {
          '告警级别': '通知',
          '数量': 123
        }
      ]
    }
  },
  'styles': {
    'base': [
      {
        'name': '图元样式',
        'tag': 'Hint',
        'childoption': [
          {
            'key': 'pieType',
            'tag': 'typeSelect',
            'options': [
              {
                'icon': 'icon-n-pie',
                'value': '饼图'
              },
              {
                'icon': 'icon-n-ndge',
                'value': '南丁格尔图'
              },
              {
                'icon': 'icon-n-ring',
                'value': '环形图'
              }
            ]
          },
          {
            'name': '图元上边距',
            'key': 'gridTop',
            'tag': 'input'
          },
          {
            'name': '图元左边距',
            'key': 'gridLeft',
            'tag': 'input'
          }
        ]
      },
      {
        'name': '图例配置',
        'tag': 'Hint',
        'childoption': [
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
            'tag': 'input'
          },
          {
            'name': '图例宽度',
            'parentKey': {
              'ctLegendShow': true
            },
            'key': 'legendwidth',
            'tag': 'input'
          },
          {
            'name': '图例左距离',
            'parentKey': {
              'ctLegendShow': true
            },
            'key': 'legendX',
            'tag': 'input'
          },
          {
            'name': '图例上距离',
            'parentKey': {
              'ctLegendShow': true
            },
            'key': 'legendY',
            'tag': 'input'
          },
          // {
          //   'name': '图例高度',
          //   'parentKey': {
          //     'ctLegendShow': true
          //   },
          //   'key': 'legendY',
          //   'tag': 'input'
          // },
          {
            'name': '图例字颜色',
            'key': 'ctLegendColor',
            'tag': 'Color',
            'parentKey': {
              'ctLegendShow': true
            }
          }
        ]
      },
      {
        'name': '图元配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '图元半径',
            'key': 'radius',
            'tag': 'input'
          },
          {
            'name': '图表文字显示',
            'key': 'showword',
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
            'name': '图表文字大小',
            'key': 'showwordSize',
            'tag': 'input'
          },
          {
            'name': '图表文字连线',
            'key': 'showline',
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
            'name': '环宽',
            'key': 'detailwidth',
            'parentKey': {
              'pieType': '环形图'
            },
            'tag': 'input'
          },
          {
            'name': '环宽',
            'key': 'ringWidth',
            'parentKey': {
              'pieType': '南丁格尔图'
            },
            'tag': 'input'
          },
          {
            'name': '圆角大小',
            'key': 'borderRadius',
            'parentKey': {
              'pieType': '饼图'
            },
            'tag': 'input'
          },
          {
            'name': '圆角大小',
            'key': 'borderRadius',
            'parentKey': {
              'pieType': '南丁格尔图'
            },
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
        'name': '颜色配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '配色',
            'key': 'ifEidetColor',
            'tag': 'select',
            'options': [
              {
                'name': '默认',
                'value': false
              },
              {
                'name': '自定义',
                'value': true
              }
            ]
          }, {
            'name': '颜色类型',
            'key': 'ifGradual',
            'parentKey': {
              'ifEidetColor': true
            },
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
            'name': '描边线条',
            'key': 'areaLineType',
            'parentKey': {
              'ifGradual': 'true',
              'lineArea': true
            },
            'tag': 'select',
            'options': [
              {
                'name': '渐变',
                'value': true
              },
              {
                'name': '单色',
                'value': false
              }
            ]
          }, {
            'name': '折线颜色',
            'key': 'LineColorArray',
            'parentKey': {
              'ifGradual': 'false',
              'ifEidetColor': true
            },
            'tag': 'ColorArray'
          }, {
            'name': '折线颜色',
            'key': 'DLineColorArray',
            'parentKey': {
              'ifGradual': 'true',
              'ifEidetColor': true
            },
            'tag': 'ColorArray'
          }
        ]
      }

    ]
  }
}
