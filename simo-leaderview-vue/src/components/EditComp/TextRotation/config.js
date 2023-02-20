export default {
  'item': {
    'text': '文本轮播',
    'imgClass': 'icon-sliderimg',
    'chartType': 'TextRotation',
    'width': 300,
    'height': 300,
    'pageSize': 2, // 每页数据条数
    'speed': 3000, // 循环速度
    'direction': true, // 轮播方向
    keySize: 14,
    keyColor: '#fff',
    keySpace: 0,
    keyFamily: '',
    keyWeight: 'normal',
    valueSize: 14,
    valueColor: '#fff',
    valueSpace: 0,
    valueFamily: '',
    valueWeight: 'normal',
    valueRight: 0,
    unitSize: 14,
    unitColor: '#fff',
    unitSpace: 0,
    autoPlay: true,
    unitFamily: '',
    unitWeight: 'normal',
    'chartData': {
      dataArray: [
        {
          'key': '今日出警',
          'value': '10',
          'unit': '件'
        },
        {
          'key': '今日出警',
          'value': '10',
          'unit': '件'
        },
        {
          'key': '今日出警',
          'value': '10',
          'unit': '件'
        }
      ]
    }
  },
  'styles': {
    'base': [
      {
        'name': '图表样式',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '播放速度',
            'key': 'speed',
            'tag': 'input'
          },
          {
            'name': '每页条数',
            'key': 'pageSize',
            'tag': 'input'
          },
          {
            'name': '轮播方向',
            'key': 'direction',
            'tag': 'select',
            'options': [
              {
                'name': '上下',
                'value': true
              },
              {
                'name': '左右',
                'value': false
              }
            ]
          },
          {
            'name': '是否自动轮播',
            'key': 'autoPlay',
            'tag': 'select',
            'options': [
              {
                'name': '是',
                'value': true
              },
              {
                'name': '否',
                'value': false
              }
            ]
          }
        ]
      },
      {
        'name': 'key值样式',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '字体大小',
            'key': 'keySize',
            'tag': 'input'
          },
          {
            'name': '字体颜色',
            'key': 'keyColor',
            'tag': 'Color'
          },
          {
            'name': '字间距',
            'key': 'keySpace',
            'tag': 'input'
          },
          {
            'name': '字体粗细',
            'key': 'keyWeight',
            'tag': 'select',
            'options': [
              {
                'name': '正常',
                'value': 'normal'
              },
              {
                'name': '加粗',
                'value': 'bold'
              }
            ]
          },
          {
            'name': '字体样式',
            'key': 'keyFamily',
            'tag': 'fontFamily'
          }
        ]
      },
      {
        'name': 'value值样式',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '字体大小',
            'key': 'valueSize',
            'tag': 'input'
          },
          {
            'name': '字体颜色',
            'key': 'valueColor',
            'tag': 'Color'
          },
          {
            'name': '字间距',
            'key': 'valueSpace',
            'tag': 'input'
          },
          {
            'name': '右边距',
            'key': 'valueRight',
            'tag': 'input'
          },
          {
            'name': '字体粗细',
            'key': 'valueWeight',
            'tag': 'select',
            'options': [
              {
                'name': '正常',
                'value': 'normal'
              },
              {
                'name': '加粗',
                'value': 'bold'
              }
            ]
          },
          {
            'name': '字体样式',
            'key': 'valueFamily',
            'tag': 'fontFamily'
          }
        ]
      },
      {
        'name': 'unit值样式',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '字体大小',
            'key': 'unitSize',
            'tag': 'input'
          },
          {
            'name': '字体颜色',
            'key': 'unitColor',
            'tag': 'Color'
          },
          {
            'name': '字间距',
            'key': 'unitSpace',
            'tag': 'input'
          },
          {
            'name': '字体粗细',
            'key': 'unitWeight',
            'tag': 'select',
            'options': [
              {
                'name': '正常',
                'value': 'normal'
              },
              {
                'name': '加粗',
                'value': 'bold'
              }
            ]
          },
          {
            'name': '字体样式',
            'key': 'unitFamily',
            'tag': 'fontFamily'
          }
        ]
      }
    ]
  }
}
