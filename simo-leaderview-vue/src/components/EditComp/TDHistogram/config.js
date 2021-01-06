export default {
  'item': {
    'text': '3D 柱状图',
    'imgClass': 'icon-n-video',
    'chartType': 'TDHistogram',
    'width': 600,
    'height': 400,
    'showgrid3D': true,
    'barSize': 10,
    'grid3DLineColor': 'white',
    'grid3DHLolor': 'black',
    'grid3DFontSize': 16,
    'ifGradual': 'false',
    'grid3DlineSize': 16,
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
        'name': '坐标轴字体',
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
