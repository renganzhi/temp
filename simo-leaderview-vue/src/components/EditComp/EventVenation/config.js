export default{
  'item': {
    'text': '事件脉络',
    'imgClass': 'icon-n-text',
    'chartType': 'EventVenation',
    'width': 400,
    'height': 600,
    'titleFontSize': 16,
    'titleBottm': 10,
    'iconColor': 'blue',
    'titleColor': 'blue',
    'contBorderColor': 'gray',
    'contPadding': 6,
    'contBorderRdius': 5,
    'contTitleSize': 16,
    'contTitleColor': 'white',
    'contColor': 'white',
    'contSize': 12,
    'chartData': {
      'data': [
        {
          'sj_time': '2022-08-22',
          'items': {
            'columns': [
              '处置单位',
              '处置人',
              '操作时间'
            ],
            'rows': [
              {
                '操作时间': '2022-08-22 18:47:29',
                '处置单位': 'xxxxxxxx',
                '处置人': 'xxxxx'
              },
              {
                '操作时间': '2022-08-22 18:47:29',
                '处置单位': 'xxxxxxxx',
                '处置人': 'xxxxx'
              }
            ]
          }
        }
      ]
    }
  },
  'styles': {
    'base': [
      {
        'name': '事件脉络设置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '图标颜色',
            'key': 'iconColor',
            'tag': 'Color'
          },
          {
            'name': '标题大小',
            'key': 'titleFontSize',
            'tag': 'input'
          },
          {
            'name': '标题颜色',
            'key': 'titleColor',
            'tag': 'Color'
          },
          {
            'name': '标题下边距',
            'key': 'titleBottm',
            'tag': 'input'
          },
          {
            'name': '内容边框颜色',
            'key': 'contBorderColor',
            'tag': 'Color'
          },
          {
            'name': '内容内边距',
            'key': 'contPadding',
            'tag': 'input'
          },
          {
            'name': '内容圆角',
            'key': 'contBorderRdius',
            'tag': 'input'
          },
          {
            'name': '内容标题大小',
            'key': 'contTitleSize',
            'tag': 'input'
          },
          {
            'name': '内容标题颜色',
            'key': 'contTitleColor',
            'tag': 'Color'
          },
          {
            'name': '内容大小',
            'key': 'contSize',
            'tag': 'input'
          },
          {
            'name': '内容颜色',
            'key': 'contColor',
            'tag': 'Color'
          }
        ]
      }
    ]
  }
}
