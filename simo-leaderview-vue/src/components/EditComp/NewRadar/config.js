export default {
  'item': {
    'text': '雷达图',
    'imgClass': 'icon-n-radar',
    'chartType': 'NewRadar',
    'ifGradual': 'false',
    'splitShow': true,
    'ctLegendSize': '16',
    'ctLegendColor': '#666f8b',
    'axisLabelSize': '16',
    'ctLegendShow': true,
    'ifEidetColor': true,
    'legendY': 85,
    'gridTop': 50,
    'gridBotton': 50,
    'gridLeft': 50,
    'gridRight': 50,
    'tooltipShow': true,
    'tooltipBackColor': '#57625d',
    'tooltipTextColor': '#fff',
    'tooltipfontSize': 14,
    'splitColor': 'rgba(117, 124, 137, 0.2)',
    'splitSize': 1,
    'width': 500,
    'height': 380,
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
