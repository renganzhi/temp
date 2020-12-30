export default{
  'item': {
    'text': '文本框2',
    'imgClass': 'icon-n-text',
    'chartType': 'VtextArea',
    'width': 200,
    'height': 50,
    'bdpx': 0,
    'bgClr': '',
    'clr': '#666f8b',
    'bdClr': '#3d445a',
    'ctName': '请输入文本框内容',
    'fontWeight': 'normal',
    'fontFamily': '',
    'fontSize': 12,
    'linkId': '',
    'chartData': {}
  },
  'styles': {
    'base': [
      {
        'name': '图表样式',
        'tag': 'Hint'
      },
      {
        'name': '填充色',
        'key': 'bgClr',
        'tag': 'Color'
      },
      {
        'name': '边框色',
        'key': 'bdClr',
        'tag': 'Color'
      },
      {
        'name': '线宽',
        'key': 'bdpx',
        'tag': 'select',
        'options': [
          {
            'name': '0',
            'value': 0
          },
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
          },
          {
            'name': '4',
            'value': 4
          },
          {
            'name': '5',
            'value': 5
          },
          {
            'name': '6',
            'value': 6
          },
          {
            'name': '7',
            'value': 7
          },
          {
            'name': '8',
            'value': 8
          },
          {
            'name': '9',
            'value': 9
          },
          {
            'name': '10',
            'value': 10
          }
        ]
      },
      {
        'name': '字体颜色',
        'key': 'fontColor',
        'tag': 'Color'
      },
      {
        'name': '字号',
        'key': 'fontSize',
        'tag': 'input'
      },
      {
        'name': '字体粗细',
        'key': 'fontWeight',
        'tag': 'select',
        'options': [
          {
            'name': 'lighter',
            'value': 'lighter'
          },
          {
            'name': 'normal',
            'value': 'normal'
          },
          {
            'name': 'bold',
            'value': 'bold'
          },
          {
            'name': 'bolder',
            'value': 'bolder'
          }

        ]
      },
      {
        'name': '字体样式',
        'key': 'fontFamily',
        'tag': 'fontFamily'
      }
    ]
  }
}
