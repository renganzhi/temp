export default{
  'item': {
    'text': '武侯毛玻璃',
    'imgClass': 'icon-n-text',
    'chartType': 'WuhoMaoBL',
    'width': 200,
    'height': 200,
    'MapBLType': false,
    'MapBLFX': false,
    'MapBLTOP': false,
    'chartData': {
      'hkwsid': ''
    }
  },
  'styles': {
    'base': [
      {
        'name': '毛玻璃设置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '毛玻璃类型',
            'key': 'MapBLType',
            'tag': 'select',
            'options': [
              {
                'name': '常规正方形',
                'value': true
              },
              {
                'name': '武侯定制',
                'value': false
              }
            ]
          },
          {
            'name': '毛玻璃方向',
            'key': 'MapBLFX',
            'parentKey': {
              'MapBLType': false
            },
            'tag': 'select',
            'options': [
              {
                'name': '左侧',
                'value': false
              },
              {
                'name': '右侧',
                'value': true
              }
            ]
          }
          // {
          //   'name': '三角方向',
          //   'key': 'MapBLTOP',
          //   'parentKey': {
          //     'MapBLType': false
          //   },
          //   'tag': 'select',
          //   'options': [
          //     {
          //       'name': '上侧',
          //       'value': false
          //     },
          //     {
          //       'name': '下侧',
          //       'value': true
          //     }
          //   ]
          // }
        ]
      }
    ]
  }
}
