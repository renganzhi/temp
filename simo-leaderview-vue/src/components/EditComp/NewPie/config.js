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
    'legendY': 85,
    'radius': 50,
    'detailwidth': 12,
    'isRing': false,
    'showline': true,
    'tooltipShow': true,
    'tooltipBackColor': '#57625d',
    'tooltipTextColor': '#fff',
    'tooltipfontSize': 14,
    'LineColorArray': [
      '#2d98f1',
      '#32c5e9',
      '#67e0e3',
      '#9fe6b8',
      '#ffdb5c',
      '#c23531'],
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
            'tag': 'Color'
          },
          {
            'name': '图元半径',
            'key': 'radius',
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
              'isRing': true
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
            'name': '区域图线条',
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
