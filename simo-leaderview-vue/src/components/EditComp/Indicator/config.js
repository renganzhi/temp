export default{
  'item': {
    'text': '指标数据',
    'imgClass': 'icon-n-number',
    'chartType': 'Indicator',
    'fontSize': 36,
    'upFontSize': 26,
    'leftPos': 50,
    'topPos': 0,
    'upIcon': 6,
    'upIconLeft': -20,
    'upIconTop': 23,
    'upShow': 'false',
    'fontFamily': 'asn',
    'clr': '#30a1ff',
    'width': 300,
    'height': 90,
    'textAlign': 'left',
    'fontLineHeight': 16,
    'fontSpaceing': 0,
    'fontWeight': 'normal',
    'chartData': {
      'name': '繁忙度',
      'unit': '%',
      'value': 60,
      'type': 'percent',
      'number': 20
    }
  },
  'styles': {
    'base': [
      {
        'name': '数值样式',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '字号',
            'key': 'fontSize',
            'tag': 'input'
          },
          {
            'name': '文本对齐',
            'key': 'textAlign',
            'tag': 'select',
            'options': [{
              'name': '左对齐',
              'value': 'left'
            },
            {
              'name': '居中对齐',
              'value': 'center'
            },
            {
              'name': '右对齐',
              'value': 'right'
            }
            ]
          },
          {
            'name': '行间距',
            'key': 'fontLineHeight',
            'tag': 'input'
          },
          {
            'name': '字体粗细',
            'key': 'fontWeight',
            'tag': 'select',
            'options': [
              {
                'name': 'normal',
                'value': 'normal'
              },
              {
                'name': 'bold',
                'value': 'bold'
              }
            ]
          },
          {
            'name': '字间距',
            'key': 'fontSpaceing',
            'tag': 'input'
          },
          {
            'name': '字体颜色',
            'key': 'clr',
            'tag': 'Color'
          },
          {
            'name': '字体样式',
            'key': 'fontFamily',
            'tag': 'fontFamily'
          }
        ]
      },
      {
        'name': '悬浮配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '悬浮可见性',
            'key': 'upShow',
            'tag': 'select',
            'options': [
              {
                'name': '显示',
                'value': 'true'
              },
              {
                'name': '隐藏',
                'value': 'false'
              }
            ]
          },
          {
            'name': '悬浮字号',
            'key': 'upFontSize',
            'tag': 'input'
          },
          {
            'name': '悬浮左边距',
            'key': 'leftPos',
            'tag': 'input'
          },
          {
            'name': '悬浮上边距',
            'key': 'topPos',
            'tag': 'input'
          },
          {
            'name': '悬浮图标大小',
            'key': 'upIcon',
            'tag': 'input'
          },
          {
            'name': '图标左边距',
            'key': 'upIconLeft',
            'tag': 'input'
          },
          {
            'name': '图标上边距',
            'key': 'upIconTop',
            'tag': 'input'
          }
        ]
      }
    ]
  }
}
