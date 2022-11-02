export default{
  'item': {
    'text': '切换按钮',
    'imgClass': 'icon-n-text',
    'chartType': 'ToggleButton',
    'width': 200,
    'height': 50,
    'clr': '#666f8b',
    'fontSize': 12,
    'fontSpaceing': 1,
    'fontWeight': 'normal',
    'trigger': 'hover',
    'placement': 'top',
    checkedBack: '',
    checkedBackName: '',
    menuLeft: 0,
    menuTop: 0,
    buttonMargin: 0,
    buttonPadding: 5,
    menuFontSize: 12,
    'fontFamily': '',
    'normalBackName': '',
    'normalBack': '',
    'chartData': {
      array: [
        {
          name: '民生服务',
          options: [
            {
              name: '房屋中介'
            },
            {
              name: '工资发放'
            },
            {
              name: '水电气'
            }
          ]
        },
        {
          name: '居民保障',
          options: [
            {
              name: '物业服务'
            },
            {
              name: '城市服务医疗'
            },
            {
              name: '小区管理'
            }
          ]
        },
        {
          name: '城市保障',
          options: [
            {
              name: '占道停车'
            },
            {
              name: '占道经营'
            },
            {
              name: '消防安全'
            }
          ]
        },
        {
          name: '市场监管',
          options: [
            {
              name: '匹配消费纠纷'
            },
            {
              name: '食品安全'
            },
            {
              name: '商品质量'
            },
            {
              name: '营业执照'
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
          'name': '子菜单左边距',
          'key': 'menuLeft',
          'tag': 'input'
        }, {
          'name': '子菜单上边距',
          'key': 'menuTop',
          'tag': 'input'
        }, {
          'name': '按钮间距',
          'key': 'buttonMargin',
          'tag': 'input'
        }, {
          'name': '按钮内边距',
          'key': 'buttonPadding',
          'tag': 'input'
        }, {
          'name': '正常按钮背景',
          'key': 'normalBack',
          'tag': 'ImgFile',
          'keyName': 'normalBackName'
        }, {
          'name': '高亮按钮背景',
          'key': 'checkedBack',
          'tag': 'ImgFile',
          'keyName': 'checkedBackName'
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
        }, {
          'name': '子菜单字号',
          'key': 'menuFontSize',
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
