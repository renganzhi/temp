export default{
  'item': {
    'text': '文本框',
    'imgClass': 'icon-n-text',
    'chartType': 'NEWtextArea',
    'width': 200,
    'height': 50,
    'bdpx': 0,
    'fontLineHeight': 16,
    'bgClr': '',
    'clr': '#666f8b',
    'overflow': true,
    'bdClr': '#3d445a',
    'ColorType': true,
    'textAlign': 'left',
    ifwave: false,
    'ctName': '请输入文本框内容',
    'fontWeight': 'normal',
    'Gradientclr': ['rgba(255, 38, 38, 0.44)', '#dc4908'],
    'fontFamily': '',
    'fontSize': 12,
    'fontSpaceing': 5,
    conditionType: '', // 接口选择
    refrashTime: 30000,
    placeHolder: '',
    ifthreshold: false,
    thresholdNum: 0,
    thresholdSize: 16,
    thresholdClr: '#f00',
    thresholdGclr: ['rgba(255, 38, 38, 0.44)', '#dc4908'],
    'linkId': '',
    'chartData': {}
  },
  'styles': {
    'base': [
      {
        'name': '接口配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '选择接口',
            'key': 'conditionType',
            'tag': 'select',
            'options': [
              {
                'name': '空',
                'value': ''
              },
              {
                'name': '近七日各元素数据量占总量比例',
                'value': 1
              }
            ]
          },
          {
            'name': '接口刷新间隔',
            'key': 'refrashTime',
            'tag': 'input'
          },
          {
            'name': '占位内容',
            'key': 'placeHolder',
            'tag': 'input',
            'type': 'text'
          },
          {
            'name': '是否闪烁',
            'key': 'ifwave',
            'tag': 'select',
            'options': [
              {
                'name': '是',
                'value': true
              }, {
                'name': '否',
                'value': false
              }
            ]
          },
          {
            'name': '设置阈值',
            'key': 'ifthreshold',
            'tag': 'select',
            'options': [
              {
                'name': '是',
                'value': true
              }, {
                'name': '否',
                'value': false
              }
            ]
          },
          {
            'name': '阈值',
            'parentKey': {
              'ifthreshold': true
            },
            'key': 'thresholdNum',
            'tag': 'input'
          },
          {
            'name': '阈值颜色(单)',
            'parentKey': {
              'ifthreshold': true,
              'ColorType': true
            },
            'key': 'thresholdClr',
            'tag': 'Color'
          },
          {
            'name': '字体颜色(多)',
            'key': 'thresholdGclr',
            'parentKey': {
              'ColorType': false
            },
            'tag': 'GradientColor'
          },
          {
            'name': '阈值字体大小',
            'parentKey': {
              'ifthreshold': true
            },
            'key': 'thresholdSize',
            'tag': 'input'
          }
        ]
      },
      {
        'name': '图表样式',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '填充色',
            'key': 'bgClr',
            'tag': 'Color'
          },
          {
            'name': '边框色',
            'key': 'bdClr',
            'tag': 'Color'
          },
          {
            'name': '线宽',
            'key': 'bdpx',
            'tag': 'select',
            'options': [
              {
                'name': '0',
                'value': 0
              },
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
            'name': '滚动条',
            'key': 'overflow',
            'tag': 'select',
            'options': [
              {
                'name': '显示',
                'value': true
              }, {
                'name': '隐藏',
                'value': false
              }
            ]
          },
          {
            'name': '文本对齐',
            'key': 'textAlign',
            'tag': 'select',
            'options': [{
              'name': '左对齐',
              'value': 'left'
            },
            {
              'name': '居中对齐',
              'value': 'center'
            },
            {
              'name': '右对齐',
              'value': 'right'
            }
            ]
          },
          {
            'name': '颜色类型',
            'key': 'ColorType',
            'tag': 'select',
            'options': [{
              'name': '单色',
              'value': true
            },
            {
              'name': '渐变',
              'value': false
            }
            ]
          }
        ]
      },
      {
        'name': '字体样式',
        'tag': 'Hint',
        'childoption': [{
          'name': '字体颜色',
          'key': 'clr',
          'parentKey': {
            'ColorType': true
          },
          'tag': 'Color'
        },
        {
          'name': '字体颜色',
          'key': 'Gradientclr',
          'parentKey': {
            'ColorType': false
          },
          'tag': 'GradientColor'
        },
        {
          'name': '字号',
          'key': 'fontSize',
          'tag': 'input'
        },
        {
          'name': '字间距',
          'key': 'fontSpaceing',
          'tag': 'input'
        },
        {
          'name': '行间距',
          'key': 'fontLineHeight',
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
