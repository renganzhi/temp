export default{
  'item': {
    'text': '新城运值班体系',
    'imgClass': 'icon-n-text',
    'chartType': 'NewCYZBTX',
    'width': 780,
    'height': 871,
    pop1Left: 0,
    pop1Top: 0,
    pop2Right: -780,
    pop2Top: 0,
    pop3Right: -780,
    pop3Top: 0,
    'chartData': {}
  },
  'styles': {
    'base': [
      {
        'name': '一级弹窗配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '弹窗左边距',
          'key': 'pop1Left',
          'tag': 'input'
        },
        {
          'name': '弹窗上边距',
          'key': 'pop1Top',
          'tag': 'input'
        }]
      },
      {
        'name': '二级弹窗配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '弹窗右边距',
          'key': 'pop2Right',
          'tag': 'input'
        },
        {
          'name': '弹窗上边距',
          'key': 'pop2Top',
          'tag': 'input'
        }]
      },
      {
        'name': '三级弹窗配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '弹窗右边距',
          'key': 'pop3Right',
          'tag': 'input'
        },
        {
          'name': '弹窗上边距',
          'key': 'pop3Top',
          'tag': 'input'
        }]
      }
    ]
  }
}
