export default {
  'item': {
    'text': '极坐标柱状图',
    'imgClass': 'icon-ploarbar',
    'chartType': 'polarBar',
    'colorful': false,
    'openlegend': true,
    'legendColor': '#FFF',
    'legendSize': 12,
    'tooltipShow': true,
    'angleAxisColor': '#8a8383',
    'axisLabelSize': 12,
    'tooltipBackColor': '#57625d',
    'tooltipColor': '#fff',
    'tooltipSize': 14,
    'newbarHeight': 50,
    'legendStation': 'top',
    'ifGradual': 'false',
    'ScatterColor': [
      '#37a2da',
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
    'chartData': {
      'columns': [
        '城市',
        '价格范围',
        '均值'
      ],
      'rows': [
        {
          '城市': '北京',
          '价格范围': [4000, 10000],
          '均值': [6785.71]
        },
        {
          '城市': '上海',
          '价格范围': [3000, 6500],
          '均值': [4793.83]
        },
        {
          '城市': '深圳',
          '价格范围': [2000, 4000],
          '均值': [3222.33]
        },
        {
          '城市': '广州',
          '价格范围': [1800, 4000],
          '均值': [2057.14]
        },
        {
          '城市': '苏州',
          '价格范围': [2000, 2700],
          '均值': [2037.5]
        },
        {
          '城市': '杭州',
          '价格范围': [1500, 2000],
          '均值': [1650]
        }
      ]
    }
  },
  'styles': {
    'base': [
      {
        'name': '图例配置',
        'tag': 'Hint'
      },
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
      }, {
        'name': 'tips配置',
        'tag': 'Hint'
      },
      {
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
      }, {
        'name': '最小柱体高度',
        'key': 'newbarHeight',
        'tag': 'input'
      },
      {
        'name': '刻度颜色',
        'key': 'angleAxisColor',
        'tag': 'Color'
      },
      {
        'name': '刻度字大小',
        'key': 'axisLabelSize',
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
        'parentKey': {
          'ifGradual': 'false'
        },
        'tag': 'ColorArray'
      }, {
        'name': '饼图颜色',
        'key': 'DScatterColor',
        'parentKey': {
          'ifGradual': 'true'
        },
        'tag': 'ColorArray'
      }
    ]
  }
}
