export default{
  'item': {
    'text': '新下拉列表',
    'imgClass': 'icon-n-text',
    'chartType': 'NewDropMenu',
    'width': 200,
    'height': 50,
    'clr': '#666f8b',
    'menuClr': '#fff',
    'fontSize': 12,
    'fontSpaceing': 1,
    'fontWeight': 'normal',
    'trigger': 'hover',
    'placement': 'top',
    ButtonImage: '',
    ButtonImageName: '',
    menuLeft: 0,
    menuTop: 0,
    menuHeight: 50,
    menuWidth: 50,
    'fontFamily': '',
    'DropdownMenuBackName': '',
    'DropdownMenuBack': '',
    'chartData': {
      'title': '测试名称',
      'children': [
        {
          'title': '链接1',
          'url': 'https://fanyi.baidu.com/?aldtype=16047'
        },
        {
          'title': '分组2',
          'url': 'https://fanyi.baidu.com/?aldtype=16047'
        },
        {
          'title': '分组3',
          'url': 'https://fanyi.baidu.com/?aldtype=16047'
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
          'name': '菜单左边距',
          'key': 'menuLeft',
          'tag': 'input'
        }, {
          'name': '菜单上边距',
          'key': 'menuTop',
          'tag': 'input'
        }, {
          'name': '菜单高度',
          'key': 'menuHeight',
          'tag': 'input'
        }, {
          'name': '菜单宽度',
          'key': 'menuWidth',
          'tag': 'input'
        }, {
          'name': '菜单背景',
          'key': 'DropdownMenuBack',
          'tag': 'ImgFile',
          'keyName': 'DropdownMenuBackName'
        }, {
          'name': '按钮背景',
          'key': 'ButtonImage',
          'tag': 'ImgFile',
          'keyName': 'ButtonImageName'
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
          'name': '菜单字体颜色',
          'key': 'menuClr',
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
