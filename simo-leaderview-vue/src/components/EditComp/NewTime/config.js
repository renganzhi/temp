export default{
  'item': {
    'text': '时间器',
    'imgClass': 'icon-n-time',
    'chartType': 'NewTime',
    'timeSource': 'local',
    'width': 200,
    'height': 50,
    'fontSize': 18,
    'clr': '#666f8b',
    'timeFrom': 'local',
    'timeType': '1'
  },
  'styles': {
    'base': [
      {
        'name': '图表样式',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '字体颜色',
            'key': 'clr',
            'tag': 'Color'
          },
          {
            'name': '字号',
            'key': 'fontSize',
            'tag': 'select',
            'options': [
              {
                'name': '12',
                'value': 12
              },
              {
                'name': '13',
                'value': 13
              },
              {
                'name': '14',
                'value': 14
              },
              {
                'name': '16',
                'value': 16
              },
              {
                'name': '18',
                'value': 18
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
                'name': '26',
                'value': 26
              },
              {
                'name': '28',
                'value': 28
              },
              {
                'name': '30',
                'value': 30
              },
              {
                'name': '36',
                'value': 36
              },
              {
                'name': '40',
                'value': 40
              },
              {
                'name': '48',
                'value': 48
              },
              {
                'name': '54',
                'value': 54
              },
              {
                'name': '60',
                'value': 60
              },
              {
                'name': '72',
                'value': 72
              },
              {
                'name': '84',
                'value': 84
              },
              {
                'name': '88',
                'value': 88
              }
            ]
          },
          {
            'name': '时间格式',
            'key': 'timeType',
            'tag': 'select',
            'options': [
              {
                'name': '时分秒',
                'value': '1'
              },
              {
                'name': '年月日',
                'value': '2'
              },
              {
                'name': '年月日+时分',
                'value': '3'
              },
              {
                'name': '年月日+时分秒',
                'value': '4'
              }
            ]
          }
        ]
      }
    ]
  }
}
