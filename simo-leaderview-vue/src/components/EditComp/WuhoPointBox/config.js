export default{
    'item': {
      'text': '武侯调度',
      'imgClass': 'icon-n-text',
      'chartType': 'WuhoPointBox',
      'width': 490,
      'height': 1136,
      'size': 'big',
      'chartData': {
        'hkwsid':''
      }
    },
    'styles': {
      'base': [
        // {
        //   'name': '监控挑战',
        //   'tag': 'Hint',
        //   'childoption': [
        //     {
        //       'name': '监控编号',
        //       'key': 'hkwsid',
        //       'tag': 'input'
        //     }
        //   ]
        // }
        {
          'name': '样式',
          'tag': 'Hint',
          'childoption': [
            {
              'name': '大小',
              'key': 'size',
              'tag': 'select',
              'options': [
                {
                  'name': '大',
                  'value': 'big'
                },
                {
                  'name': '小',
                  'value': 'small'
                }
              ]
            }
          ]
        }
      ]
    }
  }
  