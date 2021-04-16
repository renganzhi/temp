export default {
  'item': {
    'text': '旭日图',
    'imgClass': 'icon-sunburst',
    'chartType': 'Sunrise',
    'width': 300,
    'height': 300,
    'ifGradual': 'false',
    'FontColor': 'white',
    'LineColor': '#11ecec',
    'FontSize': 12,
    'LineWidth': 2,
    'isHollow': 0,
    'PieColorArray': [
      '#37a2da',
      '#32c5e9',
      '#67e0e3',
      '#9fe6b8',
      '#ffdb5c'],
    'chartData': {
      'dataArry': [{
        'name': '主机',
        'children': [{
          'name': 'Linux',
          'value': 11,
          'children': [{
            'name': 'Linux1',
            'value': 2
          }, {
            'name': 'Linux2',
            'value': 5
          }, {
            'name': 'Linux3',
            'value': 4
          }]
        }, {
          'name': 'Windows',
          'value': 10,
          'children': [{
            'name': 'Windows1',
            'value': 5
          }, {
            'name': 'Windows2',
            'value': 1
          }]
        }]
      }, {
        'name': '数据库',
        'value': 7,
        'children': [{
          'name': 'Oracle',
          'value': 3,
          'children': [{
            'name': 'Oracle1',
            'value': 1
          }, {
            'name': 'Oracle2',
            'value': 2
          }]
        }, {
          'name': 'uxsinoDB',
          'value': 4
        }]
      }, {
        'name': '网络设备',
        'value': 7,
        'children': [{
          'name': '路由器',
          'value': 3,
          'children': [{
            'name': '路由器1',
            'value': 1
          }, {
            'name': '路由器2',
            'value': 2
          }]
        }, {
          'name': '交换机',
          'value': 4,
          'children': [{
            'name': '交换机1',
            'value': 3
          }, {
            'name': '交换机2',
            'value': 1
          }]
        }]
      }]
    }
  },
  'styles': {
    'base': [
      {
        'name': '基础配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '是否空心',
          'key': 'isHollow',
          'tag': 'select',
          'options': [
            {
              'name': '否',
              'value': 0
            },
            {
              'name': '是',
              'value': 10
            }
          ]
        }, {
          'name': '字体颜色',
          'key': 'FontColor',
          'tag': 'Color'
        }, {
          'name': '字体大小',
          'key': 'FontSize',
          'tag': 'input'
        } ]
      }, {
        'name': '分界线配置',
        'tag': 'Hint',
        'childoption': [ {
          'name': '分界线颜色',
          'key': 'LineColor',
          'tag': 'Color'
        }, {
          'name': '分界线粗细',
          'key': 'LineWidth',
          'tag': 'input'
        }, {
          'name': '旭日图颜色',
          'key': 'PieColorArray',
          'tag': 'ColorArray'
        }]
      }
    ]
  }
}
