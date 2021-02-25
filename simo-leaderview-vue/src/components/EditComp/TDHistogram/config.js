export default {
  'item': {
    'text': '3D柱图',
    'imgClass': 'icon-3dhistogram',
    'chartType': 'TDHistogram',
    'width': 600,
    'height': 400,
    'showgrid3D': true,
    'barSize': 10,
    'grid3DLineColor': 'white',
    'grid3DHLolor': 'black',
    'tooltipShow': true,
    'tooltipBackColor': '#57625d',
    'tooltipColor': '#fff',
    'tooltipSize': 14,
    'grid3DFontSize': 16,
    'ifGradual': 'false',
    'grid3DlineSize': 16,
    'autoRotate': false,
    'autoRotateDirection': 'cw',
    'autoRotateSpeed': 20,
    'autoRotateAfterStill': 5,
    'distance': 250,
    'alpha': 20,
    'beta': 30,
    'grid3DColorArray': [
      '#37a2da',
      '#32c5e9',
      '#67e0e3',
      '#9fe6b8',
      '#ffdb5c'],
    'chartData': {
      'dataArry': [
        [0, 0, 2],
        [0, 1, 4],
        [0, 2, 5.6],
        [0, 3, 6],
        [0, 4, 4.3],
        [1, 0, 5],
        [1, 1, 2.1],
        [1, 2, 3],
        [1, 3, 3],
        [1, 4, 5.1],
        [2, 0, 4.2],
        [2, 1, 6],
        [2, 2, 2.1],
        [2, 3, 3],
        [2, 4, 4],
        [3, 0, 2],
        [3, 1, 3.2],
        [3, 2, 4],
        [3, 3, 5],
        [3, 4, 2],
        [4, 0, 3.5],
        [4, 1, 6],
        [4, 2, 4.5],
        [4, 3, 5],
        [4, 4, 6]
      ]
    }
  },
  'styles': {
    'base': [
      {
        'name': '柱体大小',
        'key': 'barSize',
        'tag': 'input'
      },
      {
        'name': 'tips配置',
        'tag': 'Hint'
      },
      {
        'name': '是否显示tips',
        'key': 'tooltipShow',
        'tag': 'select',
        'options': [
          {
            'name': '显示',
            'value': true
          },
          {
            'name': '隐藏',
            'value': false
          }
        ]
      },
      {
        'name': 'tips背景色',
        'key': 'tooltipBackColor',
        'parentKey': {
          'tooltipShow': true
        },
        'tag': 'Color'
      },
      {
        'name': 'tips字体色',
        'key': 'tooltipColor',
        'parentKey': {
          'tooltipShow': true
        },
        'tag': 'Color'
      },
      {
        'name': 'tips字体大小',
        'key': 'tooltipSize',
        'parentKey': {
          'tooltipShow': true
        },
        'tag': 'input'
      },
      {
        'name': '旋转配置',
        'tag': 'Hint'
      },
      {
        'name': '是否旋转',
        'key': 'autoRotate',
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
      },
      {
        'name': '旋转方向',
        'key': 'autoRotateDirection',
        'tag': 'select',
        'parentKey': {
          'autoRotate': true
        },
        'options': [
          {
            'name': '顺时针',
            'value': 'cw'
          },
          {
            'name': '逆时针',
            'value': 'ccw'
          }
        ]
      },
      {
        'name': '旋转速度',
        'key': 'autoRotateSpeed',
        'parentKey': {
          'autoRotate': true
        },
        'tag': 'input'
      },
      {
        'name': '操作静置时间',
        'key': 'autoRotateAfterStill',
        'parentKey': {
          'autoRotate': true
        },
        'tag': 'input'
      },
      // {
      //   'name': '视角高度',
      //   'key': 'distance',
      //   // 'parentKey': {
      //   //   'autoRotate': true
      //   // },
      //   'tag': 'input'
      // },
      {
        'name': '俯仰角',
        'key': 'alpha',
        'tag': 'input'
      },
      {
        'name': '旋转角',
        'key': 'beta',
        'tag': 'input'
      },
      {
        'name': '坐标轴配置',
        'tag': 'Hint'
      },
      {
        'name': '显示坐标轴',
        'key': 'showgrid3D',
        'tag': 'select',
        'options': [
          {
            'name': '显示',
            'value': true
          },
          {
            'name': '隐藏',
            'value': false
          }
        ]
      },
      {
        'name': '坐标轴颜色',
        'key': 'grid3DLineColor',
        'parentKey': {
          'showgrid3D': true
        },
        'tag': 'Color'
      },
      {
        'name': '坐标文字大小',
        'key': 'grid3DFontSize',
        'parentKey': {
          'showgrid3D': true
        },
        'tag': 'input'
      },
      {
        'name': '指示线颜色',
        'key': 'grid3DHLolor',
        'parentKey': {
          'showgrid3D': true
        },
        'tag': 'Color'
      },
      {
        'name': '指示线字体',
        'key': 'grid3DlineSize',
        'parentKey': {
          'showgrid3D': true
        },
        'tag': 'input'
      }, {
        'name': '柱体颜色',
        'key': 'grid3DColorArray',
        'tag': 'ColorArray'
      }
    ]
  }
}
