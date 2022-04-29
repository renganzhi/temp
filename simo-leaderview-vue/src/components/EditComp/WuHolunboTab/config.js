export default{
    'item': {
      'text': '武侯翻牌器',
      'imgClass': 'icon-n-text',
      'chartType': 'WuHolunboTab',
      'width': 1060,
      'height': 150,
      'fontSize': 28,
      'textfontColor': 'white',
      'Zeroclr': 'white',
      'textfontSize': 28,
      'chartData': {
      }
    },
    'styles': {
      'base': [
        {
          'name': '翻牌器设置',
          'tag': 'Hint',
          'childoption': [
            {
              'name': '文字大小',
              'key': 'fontSize',
              'tag': 'input'
            },
          ]
        },
        {
          'name': '文本设置',
          'tag': 'Hint',
          'childoption': [
            {
              'name': '文字大小',
              'key': 'textfontSize',
              'tag': 'input'
            },
            {
              'name': '前缀颜色',
              'key': 'Zeroclr',
              'tag': 'Color'
            },
            {
              'name': '文字颜色',
              'key': 'textfontColor',
              'tag': 'Color'
            }
          ]
        }
      ]
    }
  }
  