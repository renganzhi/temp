export default {
  'item': {
    'text': '旭日图',
    'imgClass': 'icon-sunburst',
    'chartType': 'Sunrise',
    'width': 300,
    'height': 300,
    'ifGradual': 'false',
    'FontColor': 'white',
    'LineColor': 'black',
    'FontSize': 12,
    'LineWidth': 3,
    'isHollow': 0,
    'PieColorArray': [
      '#37a2da',
      '#32c5e9',
      '#67e0e3',
      '#9fe6b8',
      '#ffdb5c'],
    'chartData': {
      'dataArry': [{
        'name': 'Grandpa',
        'children': [{
          'name': 'Uncle Leo',
          'value': 11,
          'children': [{
            'name': 'Cousin Jack',
            'value': 2
          }, {
            'name': 'Cousin Mary',
            'value': 5,
            'children': [{
              'name': 'Jackson',
              'value': 2
            }]
          }, {
            'name': 'Cousin Ben',
            'value': 4
          }]
        }, {
          'name': 'Father',
          'value': 10,
          'children': [{
            'name': 'Me',
            'value': 5
          }, {
            'name': 'Brother Peter',
            'value': 1
          }]
        }]
      }, {
        'name': 'Nancy',
        'children': [{
          'name': 'Uncle Nike',
          'children': [{
            'name': 'Cousin Betty',
            'value': 1
          }, {
            'name': 'Cousin Jenny',
            'value': 2
          }]
        }]
      }, {
        'name': 'Nancy',
        'children': [{
          'name': 'Uncle Nike',
          'children': [{
            'name': 'Cousin Betty',
            'value': 1
          }, {
            'name': 'Cousin Jenny',
            'value': 2
          }]
        }]
      }]
    }
  },
  'styles': {
    'base': [
      {
        'name': '是否空心',
        'key': 'isHollow',
        'tag': 'select',
        'options': [
          {
            'name': '否',
            'value': 0
          },
          {
            'name': '是',
            'value': 10
          }
        ]
      }, {
        'name': '字体颜色',
        'key': 'FontColor',
        'tag': 'Color'
      }, {
        'name': '字体大小',
        'key': 'FontSize',
        'tag': 'input'
      }, {
        'name': '分界线颜色',
        'key': 'LineColor',
        'tag': 'Color'
      }, {
        'name': '分界线粗细',
        'key': 'LineWidth',
        'tag': 'input'
      }, {
        'name': '旭日图颜色',
        'key': 'PieColorArray',
        'tag': 'ColorArray'
      }
    ]
  }
}
