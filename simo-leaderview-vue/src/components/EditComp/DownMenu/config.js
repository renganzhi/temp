export default{
  'item': {
    'text': '下拉列表',
    'imgClass': 'icon-n-text',
    'chartType': 'DownMenu',
    'width': 200,
    'height': 50,
    'clr': '#666f8b',
    'fontSize': 12,
    'fontSpaceing': 1,
    'fontWeight': 'normal',
    'trigger': 'hover',
    'placement': 'top',
    'fontFamily': '',
    'DropdownMenuBackName': '',
    'DropdownMenuBack': '',
    'chartData': {
      'title': '测试名称',
      'nowCheckId': '72',
      'children': [
        {
          'title': '链接1',
          'url': 'https://fanyi.baidu.com/?aldtype=16047'
        },
        {
          'title': '分组2',
          'children': [
            {
              'title': '链接1',
              'url': 'https://fanyi.baidu.com/?aldtype=16047'
            },
            {
              'title': '链接1',
              'url': 'https://fanyi.baidu.com/?aldtype=16047'
            }
          ]
        }
      ]
    }
  },
  'styles': {
    'base': [
      {
        'name': '菜单设置',
        'tag': 'Hint',
        'childoption': [{
          'name': '触发方式',
          'key': 'trigger',
          'tag': 'select',
          'options': [
            {
              'name': '悬停',
              'value': 'hover'
            },
            {
              'name': '点击',
              'value': 'click'
            }
          ]
        }, {
          'name': '展开方向',
          'key': 'placement',
          'tag': 'select',
          'options': [
            {
              'name': '上侧',
              'value': 'top'
            },
            {
              'name': '下侧',
              'value': 'bottom'
            },
            {
              'name': '右侧',
              'value': 'right'
            },
            {
              'name': '左侧',
              'value': 'left'
            }
          ]
        }, {
          'name': '菜单背景',
          'key': 'DropdownMenuBack',
          'tag': 'ImgFile',
          'keyName': 'DropdownMenuBackName'
        }]
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
          'name': '字间距',
          'key': 'fontSpaceing',
          'tag': 'input'
        },
        {
          'name': '字号',
          'key': 'fontSize',
          'tag': 'input'
        },
        {
          'name': '字体粗细',
          'key': 'fontWeight',
          'tag': 'select',
          'options': [
            {
              'name': 'normal',
              'value': 'normal'
            },
            {
              'name': 'bold',
              'value': 'bold'
            }

          ]
        },
        {
          'name': '字体样式',
          'key': 'fontFamily',
          'tag': 'fontFamily'
        }]
      }
    ]
  }
}
