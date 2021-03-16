export default{
  'item': {
    'text': '跑马灯',
    'imgClass': 'icon-n-marquee',
    'chartType': 'NewMarquee',
    'width': 200,
    'height': 50,
    'bdpx': 0,
    'bgClr': '',
    'direction': 'left',
    'speed': 2,
    'clr': '#666f8b',
    'bdClr': '',
    'ctName': '这是一个跑马灯的演示demo，双击可对文案进行编辑'
  },
  'styles': {
    'base': [
      {
        'name': '图表样式',
        'tag': 'Hint',
        'childoption': [
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
          }
        ]
      },
      {
        'name': '字体样式',
        'tag': 'Hint',
        'childoption': [{
          'name': '字体颜色',
          'key': 'clr',
          'tag': 'Color'
        },
        {
          'name': '字号',
          'key': 'fontSize',
          'tag': 'input'
        }]
      },
      {
        'name': '轮播样式',
        'tag': 'Hint',
        'childoption': [{
          'name': '轮播方向',
          'key': 'direction',
          'tag': 'select',
          'options': [
            {
              'name': '横向',
              'value': 'left'
            },
            {
              'name': '纵向',
              'value': 'top'
            }
          ]
        },
        {
          'name': '轮播速度',
          'key': 'speed',
          'tag': 'select',
          'options': [
            {
              'name': '高速',
              'value': 1
            },
            {
              'name': '中速',
              'value': 2
            },
            {
              'name': '低速',
              'value': 3
            }
          ]
        }]
      }

    ]
  }
}
