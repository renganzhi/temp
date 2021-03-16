export default {
  'item': {
    'text': '矩形树图',
    'imgClass': 'icon-treemap',
    'chartType': 'TreeMap',
    'width': 600,
    'height': 400,
    'showTooltip': true,
    'TooltipBackColor': 'white',
    'TooltipColor': 'black',
    'TooltipSize': 16,
    'fontColor': 'white',
    'fontSize': 16,
    'breadcrumb': true,
    'roam': true,
    'gapWidth': 5,
    'gapWidthColor': 'rgba(252, 252, 252, 0.28)',
    'leafDepth': null,
    'leafDepthSelf': 1,
    'ifGradual': 'false',
    'breadcrumbStation': 'top',
    'breadcrumbBackColor': 'white',
    'breadcrumbColor': 'black',
    'breadcrumbsize': 12,
    'TreeMapColorArray': [
      '#37a2da',
      '#32c5e9',
      '#67e0e3',
      '#9fe6b8',
      '#ffdb5c'],
    'DTreeMapColorArray': [
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
      'name': '这是示例数据',
      'dataArry': [{
        'name': 'nodeA',
        'value': 10,
        'children': [{
          'name': 'nodeAa',
          'value': 4
        }, {
          'name': 'nodeAb',
          'value': 6
        }, {
          'name': '-------',
          'value': 1
        }]
      }, {
        'name': 'nodeB',
        'value': 20,
        'children': [{
          'name': 'nodeBa',
          'value': 20,
          'children': [{
            'name': 'nodeBa1',
            'value': 25
          }, {
            'name': 'nodeBa2',
            'value': 25
          }]
        }, {
          'name': '99999999',
          'value': 40,
          'children': [{
            'name': 'nodeBa1',
            'value': 25
          }, {
            'name': 'nodeBa2',
            'value': 25
          }, {
            'name': '2222222222222',
            'value': 1
          }]
        }]
      }]
    }
  },
  'styles': {
    'base': [
      {
        'name': 'tips配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '显示tips',
          'key': 'showTooltip',
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
          'key': 'TooltipBackColor',
          'parentKey': {
            'showTooltip': true
          },
          'tag': 'Color'
        },
        {
          'name': 'tips字体色',
          'key': 'TooltipColor',
          'parentKey': {
            'showTooltip': true
          },
          'tag': 'Color'
        },
        {
          'name': 'tips字大小',
          'key': 'TooltipSize',
          'parentKey': {
            'showTooltip': true
          },
          'tag': 'input'
        }
        ]
      },
      {
        'name': '字体配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '字体颜色',
          'key': 'fontColor',
          'tag': 'Color'
        },
        {
          'name': '字体大小',
          'key': 'fontSize',
          'tag': 'input'
        } ]
      },
      {
        'name': '面包屑配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '显示面包屑',
          'key': 'breadcrumb',
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
          'name': '面包屑位置',
          'key': 'breadcrumbStation',
          'parentKey': {
            'breadcrumb': true
          },
          'tag': 'select',
          'options': [
            {
              'name': '上方居中',
              'value': 'top'
            },
            {
              'name': '下方居中',
              'value': 'bottom'
            }
          ]
        },
        {
          'name': '面包屑背景色',
          'key': 'breadcrumbBackColor',
          'parentKey': {
            'breadcrumb': true
          },
          'tag': 'Color'
        },
        {
          'name': '面包屑字体色',
          'key': 'breadcrumbColor',
          'parentKey': {
            'breadcrumb': true
          },
          'tag': 'Color'
        },
        {
          'name': '面包屑字大小',
          'key': 'breadcrumbsize',
          'parentKey': {
            'breadcrumb': true
          },
          'tag': 'input'
        }
        ]
      },
      {
        'name': '放缩拖动配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '开启放缩拖动',
          'key': 'roam',
          'tag': 'select',
          'options': [
            {
              'name': '开启放缩和拖动',
              'value': true
            },
            {
              'name': '关闭',
              'value': false
            },
            {
              'name': '仅放缩',
              'value': 'zoom'
            },
            {
              'name': '仅拖动',
              'value': 'pan'
            }
          ]
        }]
      },
      {
        'name': '间隔配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '矩形间隔大小',
          'key': 'gapWidth',
          'tag': 'input'
        },
        {
          'name': '矩形间隔颜色',
          'key': 'gapWidthColor',
          'tag': 'Color'
        },
        {
          'name': '最多显示层级',
          'key': 'leafDepth',
          'tag': 'select',
          'options': [
            {
              'name': '全部显示',
              'value': null
            },
            {
              'name': '显示一层',
              'value': 1
            },
            {
              'name': '自定义',
              'value': 'false'
            }
          ]
        },
        {
          'name': '自定义层级',
          'key': 'leafDepthSelf',
          'tag': 'input'
        } ]
      },
      {
        'name': '颜色配置',
        'tag': 'Hint',
        'childoption': [{
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
          'name': '矩形颜色',
          'key': 'TreeMapColorArray',
          'parentKey': {
            'ifGradual': 'false'
          },
          'tag': 'ColorArray'
        }, {
          'name': '矩形颜色',
          'key': 'DTreeMapColorArray',
          'parentKey': {
            'ifGradual': 'true'
          },
          'tag': 'ColorArray'
        }]
      }
    ]
  }
}
