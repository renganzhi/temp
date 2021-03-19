export default{
  'item': {
    'text': '边框',
    'imgClass': 'icon-n-rect',
    'chartType': 'NewBorder',
    'borderType': 'simple', // 内置stable, 简单simple
    'imgSrc': '',
    'radius': 0,
    'width': 300,
    'height': 300,
    'bdpx': 1,
    'showType': '1',
    'colorful': 'false',
    'directionLinear': 180,
    'bgClr': 'rgba(255, 255, 255, 0.02)',
    'barClrs': ['rgba(255, 255, 255, 0.02)', 'rgba(255, 255, 255, 0.02)'],
    'bdClr': '#175278'
  },
  'styles': {
    'base': [
      {
        'name': '图表样式',
        'tag': 'Hint',
        'childoption': [{
          'name': '边框类型',
          'key': 'borderType',
          'tag': 'select',
          'options': [
            {
              'name': '简单边框',
              'value': 'simple'
            },
            {
              'name': '内置边框',
              'value': 'stable'
            }
          ]
        },
        {
          'name': '填充色',
          'key': 'colorful',
          'ColorKey': 'bgClr',
          'DoubleColorKey': 'barClrs',
          'parentKey': {
            'borderType': 'simple'
          },
          'tag': 'GradualColor'
        },
        {
          'name': '边框色',
          'key': 'bdClr',
          'tag': 'Color',
          'parentKey': {
            'borderType': 'simple'
          }
        },
        {
          'name': '线宽',
          'key': 'bdpx',
          'parentKey': {
            'borderType': 'simple'
          },
          'tag': 'select',
          'options': [
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
        },
        {
          'name': '圆角',
          'key': 'radius',
          'parentKey': {
            'borderType': 'simple'
          },
          'tag': 'input'
        },
        {
          'name': '缩放方式',
          'key': 'showType',
          'parentKey': {
            'borderType': 'stable'
          },
          'tag': 'select',
          'options': [
            {
              'name': '按比例缩放',
              'value': '1'
            },
            {
              'name': '自由缩放',
              'value': '2'
            }]
        },
        {
          'name': '卡片背景',
          'key': 'imgSrc',
          'parentKey': {
            'borderType': 'stable'
          },
          'tag': 'NewBorder'
        }]
      }
    ]
  }
}
