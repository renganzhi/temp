export default {
  'item': {
    'text': '环形柱图',
    'imgClass': 'icon-roundcap',
    'chartType': 'GradientPie',
    'width': 300,
    'height': 300,
    'direction': true,
    'PieType': 0.75,
    'PieRadius': 30,
    'PieColor': 'red',
    'PieSpacing': 40,
    'tipsShow': true,
    'tipsSize': 24,
    'tipsColor': '#fff',
    'ifGradual': 'false',
    'tipsNotes': true,
    'NotesType': 1,
    'NotesSize': 24,
    'NotesToTop': 20,
    'NotesToLeft': 20,
    'NotesColor': 'rgba(60, 214, 151, 0.96)',
    'PieColorArray': [
      '#37a2da',
      '#32c5e9',
      '#67e0e3',
      '#9fe6b8',
      '#ffdb5c'],
    'DPieColorArray': [
      ['#6fcaf7', '#0c79c5'],
      ['#8feee5', '#1bbcae'],
      ['#fa8d76', '#db4222'],
      ['#af8af3', '#874edc'],
      ['#f5739c', '#f31d53'],
      ['#ffdf91', '#eeb01b'],
      ['#5c84e7', '#144fe5'],
      ['#85f8c0', '#62dc26']
    ],
    'chartData': {
      'dataArry': [
        {
          'name': 'xxxxxx',
          'value': 30
        }, {
          'name': 'yyyyy',
          'value': 40,
          'unit': '个'
        }, {
          'name': 'yyyyy',
          'value': 60,
          'unit': '个'
        }
      ]
    }
  },
  'styles': {
    'base': [
      {
        'name': '开始方向',
        'key': 'direction',
        'tag': 'select',
        'options': [
          {
            'name': '顺时针',
            'value': true
          },
          {
            'name': '逆时针',
            'value': false
          }
        ]
      }, {
        'name': '饼图类型',
        'key': 'PieType',
        'tag': 'select',
        'options': [
          {
            'name': '3/4圆',
            'value': 0.75
          },
          {
            'name': '整圆',
            'value': 1
          }
        ]
      }, {
        'name': '饼图粗细',
        'key': 'PieRadius',
        'tag': 'input'
      }, {
        'name': '饼图间距',
        'key': 'PieSpacing',
        'tag': 'select',
        'options': [
          {
            'name': '小间距',
            'value': 40
          },
          {
            'name': '中间距',
            'value': 60
          },
          {
            'name': '大间距',
            'value': 80
          },
          {
            'name': '超大中间距',
            'value': 100
          }
        ]
      }, {
        'name': '是否显示tips',
        'key': 'tipsShow',
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
      }, {
        'name': 'tips字体颜色',
        'key': 'tipsColor',
        'parentKey': {
          'tipsShow': true
        },
        'tag': 'Color'
      }, {
        'name': 'tips字体大小',
        'key': 'tipsSize',
        'parentKey': {
          'tipsShow': true
        },
        'tag': 'input'
      }, {
        'name': '是否显示注释',
        'key': 'tipsNotes',
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
      }, {
        'name': '注释类型',
        'key': 'NotesType',
        'tag': 'select',
        'parentKey': {
          'tipsNotes': true
        },
        'options': [
          {
            'name': '类型1',
            'value': 1
          },
          {
            'name': '类型2',
            'value': 2
          },
          {
            'name': '类型3',
            'value': 3
          }
        ]
      }, {
        'name': '注释字体颜色',
        'key': 'NotesColor',
        'parentKey': {
          'tipsNotes': true
        },
        'tag': 'Color'
      }, {
        'name': '注释字体大小',
        'key': 'NotesSize',
        'parentKey': {
          'tipsNotes': true
        },
        'tag': 'input'
      }, {
        'name': '注释字体距上距离',
        'key': 'NotesToTop',
        'parentKey': {
          'tipsNotes': true
        },
        'tag': 'input'
      }, {
        'name': '注释字体距左距离',
        'key': 'NotesToLeft',
        'parentKey': {
          'tipsNotes': true
        },
        'tag': 'input'
      }, {
        'name': '颜色类型',
        'key': 'ifGradual',
        'tag': 'select',
        'options': [
          {
            'name': '渐变',
            'value': 'true'
          },
          {
            'name': '单色',
            'value': 'false'
          }
        ]
      }, {
        'name': '饼图颜色',
        'key': 'PieColorArray',
        'parentKey': {
          'ifGradual': 'false'
        },
        'tag': 'ColorArray'
      }, {
        'name': '饼图颜色',
        'key': 'DPieColorArray',
        'parentKey': {
          'ifGradual': 'true'
        },
        'tag': 'ColorArray'
      }
    ]
  }
}