export default {
  'item': {
    'text': '地图迁徙图',
    'imgClass': 'icon-migratemap',
    'chartType': 'DataFlow',
    'width': 600,
    'height': 600,
    'labelemphasis': 'true',
    'textStyleColor': '#1b86b6',
    'labelfontSize': 16,
    'roam': 'true',
    'normalcolor': ['#32f1d5', '#1b1cb7'],
    'normalborderColor': '#64e6d5',
    'normalborderWidth': 1,
    'OpenMap2': 'true',
    'backmapstation': 2,
    'colordirection': 1,
    'normalcolor2': ['#15abd8', '#1559b0'],
    'normalborderColor2': 'rgba(0, 255, 255, 0.3)',
    'normalborderWidth2': 3,
    'shadowColor': '#2cefef',
    'shadowSize': 25,
    'emphasis': 'rgba(84, 213, 219, 0.5)',
    'effectshow': 'true',
    'imgSrc': '',
    'effectperiod': 3,
    'effecttrailLength': 0,
    'effectsymbolSize': 26,
    'normalwidth': 1,
    'normalopacity': 1,
    'normalcurveness': -0.3,
    'showEffectOn': 'render',
    'rippleEffectbrushType': 'stroke',
    'rippleEffectperiod': 4,
    'rippleEffectscale': 4,
    'normalposition': 'right',
    'normalfontSize': 16,
    'symbolSize': 6,
    'EffectbrushType': 'stroke',
    'Effectscale': 1.6,
    'labelposition': 'right',
    'labelcolor': '#fff',
    'labeltextSize': 16,
    'geosymbolSize': 25,
    'tooltipBackColor': 'rgba(166, 200, 76, 0.82)',
    'tooltipTextColor': '#fff',
    'tooltipTextfontSize': 12,
    'visualMapShow': 'true',
    'calculable': 'true',
    'visualMapTextcolor': '#fff',
    'myctColors': [
      '#fe5aac',
      '#ffc92f',
      '#a9ff53'],
    'chartData': {
      'stationData': [
        {
          'name': '黑龙江',
          'value': [127.9688, 45.368, 1]
        }, {
          'name': '内蒙古',
          'value': [110.3467, 41.4899, 2]
        }, {
          'name': '吉林',
          'value': [125.8154, 44.2584, 4]
        }, {
          'name': '辽宁',
          'value': [123.1238, 42.1216, 5]
        }, {
          'name': '河北',
          'value': [114.4995, 38.1006, 7]
        }, {
          'name': '天津',
          'value': [117.4219, 39.4189, 43]
        }, {
          'name': '山西',
          'value': [112.3352, 37.9413, 0]
        }, {
          'name': '陕西',
          'value': [109.1162, 34.2004, 11]
        }, {
          'name': '甘肃',
          'value': [103.5901, 36.3043, 12]
        }
      ],
      'lineData': [
        [
          {
            'coord': [127.9688, 45.368],
            'name': '黑龙江→北京',
            'value': 1
          }, {
            'coord': [116.4551, 40.2539]
          }
        ]
      ],
      'Statistical': [
        {
          'name': '北京市',
          'value': [116.4551, 40.2539, 20]
        }, {
          'name': '福建',
          'value': [119.4543, 25.9222, 10]
        }
      ],
      'maxValue': 30,
      'minValue': 0
    }

  },
  'styles': {
    'base': [
      {
        'name': '地图配置',
        'tag': 'Hint'
      },
      {
        'name': '移入显示地名',
        'key': 'labelemphasis',
        'tag': 'select',
        'options': [
          {
            'name': '显示',
            'value': 'true'
          },
          {
            'name': '隐藏',
            'value': 'false'
          }
        ]
      },
      {
        'name': '地名字体颜色',
        'key': 'textStyleColor',
        'parentKey': {
          'labelemphasis': 'true'
        },
        'tag': 'Color'
      },
      {
        'name': '地名字体大小',
        'key': 'labelfontSize',
        'parentKey': {
          'labelemphasis': 'true'
        },
        'tag': 'input'
      },
      {
        'name': '允许缩放拖拽',
        'key': 'roam',
        'tag': 'select',
        'options': [
          {
            'name': '允许',
            'value': 'true'
          },
          {
            'name': '不允许',
            'value': 'false'
          }
        ]
      },
      {
        'name': '主地图配置',
        'tag': 'Hint'
      },
      {
        'name': '主地图颜色',
        'key': 'normalcolor',
        'tag': 'GradientColor'
      },
      {
        'name': '颜色渐变角度',
        'key': 'colordirection',
        'tag': 'select',
        'options': [
          {
            'name': '0°',
            'value': 0
          },
          {
            'name': '45°',
            'value': 1
          },
          {
            'name': '90°',
            'value': 2
          },
          {
            'name': '135°',
            'value': 3
          }
        ]
      },
      {
        'name': '边界线颜色',
        'key': 'normalborderColor',
        'tag': 'Color'
      },
      {
        'name': '边界线大小',
        'key': 'normalborderWidth',
        'tag': 'input'
      },
      {
        'name': '悬浮背景颜色',
        'key': 'emphasis',
        'tag': 'Color'
      },
      {
        'name': '背景地图配置',
        'tag': 'Hint'
      },
      {
        'name': '背景投影',
        'key': 'OpenMap2',
        'tag': 'select',
        'options': [
          {
            'name': '开启',
            'value': 'true'
          },
          {
            'name': '关闭',
            'value': 'false'
          }
        ]
      },
      {
        'name': '背景地图位置',
        'key': 'backmapstation',
        'parentKey': {
          'OpenMap2': 'true'
        },
        'tag': 'select',
        'options': [
          {
            'name': '正后方',
            'value': 0
          },
          {
            'name': '左下角',
            'value': 1
          },
          {
            'name': '右下角',
            'value': 2
          }
        ]
      },
      {
        'name': '背景地图颜色',
        'key': 'normalcolor2',
        'parentKey': {
          'OpenMap2': 'true'
        },
        'tag': 'GradientColor'
      },
      {
        'name': '边界线颜色',
        'key': 'normalborderColor2',
        'parentKey': {
          'OpenMap2': 'true'
        },
        'tag': 'Color'
      },
      {
        'name': '边界线大小',
        'key': 'normalborderWidth2',
        'parentKey': {
          'OpenMap2': 'true'
        },
        'tag': 'input'
      },
      {
        'name': '阴影颜色',
        'key': 'shadowColor',
        'parentKey': {
          'OpenMap2': 'true'
        },
        'tag': 'Color'
      },
      {
        'name': '阴影大小',
        'key': 'shadowSize',
        'parentKey': {
          'OpenMap2': 'true'
        },
        'tag': 'input'
      },
      {
        'name': '迁徙线配置',
        'tag': 'Hint'
      },
      {
        'name': '线条宽度',
        'key': 'normalwidth',
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
          }
        ]
      },
      {
        'name': '线条透明度',
        'key': 'normalopacity',
        'tag': 'select',
        'options': [
          {
            'name': '1.0',
            'value': 1
          },
          {
            'name': '0.8',
            'value': 0.8
          },
          {
            'name': '0.6',
            'value': 0.6
          },
          {
            'name': '0.4',
            'value': 0.4
          },
          {
            'name': '0.2',
            'value': 0.2
          },
          {
            'name': '0.01',
            'value': 0.01
          },
          {
            'name': '0.001',
            'value': 0.001
          }
        ]
      },
      {
        'name': '线条弯曲度',
        'key': 'normalcurveness',
        'tag': 'select',
        'options': [
          {
            'name': '-0.7',
            'value': -0.7
          },
          {
            'name': '-0.5',
            'value': -0.5
          },
          {
            'name': '-0.4',
            'value': -0.4
          },
          {
            'name': '-0.3',
            'value': -0.3
          },
          {
            'name': '-0.1',
            'value': -0.1
          },
          {
            'name': '0',
            'value': 0
          },
          {
            'name': '0.1',
            'value': 0.1
          },
          {
            'name': '0.3',
            'value': 0.3
          },
          {
            'name': '0.4',
            'value': 0.4
          },
          {
            'name': '0.5',
            'value': 0.5
          },
          {
            'name': '0.7',
            'value': 0.7
          }
        ]
      },
      {
        'name': '迁徙图标配置',
        'tag': 'Hint'
      },
      {
        'name': '是否显示图标',
        'key': 'effectshow',
        'tag': 'select',
        'options': [
          {
            'name': '允许',
            'value': 'true'
          },
          {
            'name': '不允许',
            'value': 'false'
          }
        ]
      },
      {
        'name': '图标运动速度',
        'key': 'effectperiod',
        'tag': 'select',
        'parentKey': {
          'effectshow': 'true'
        },
        'options': [
          {
            'name': '特快',
            'value': 1
          },
          {
            'name': '快',
            'value': 3
          },
          {
            'name': '中',
            'value': 6
          },
          {
            'name': '慢',
            'value': 9
          }
        ]
      },
      {
        'name': '图标大小',
        'key': 'effectsymbolSize',
        'parentKey': {
          'effectshow': 'true'
        },
        'tag': 'input'
      },
      {
        'name': '尾迹长度',
        'key': 'effecttrailLength',
        'tag': 'select',
        'parentKey': {
          'effectshow': 'true'
        },
        'options': [
          {
            'name': '无',
            'value': 0
          },
          {
            'name': '短',
            'value': 0.1
          },
          {
            'name': '长',
            'value': 0.3
          },
          {
            'name': '特长',
            'value': 0.5
          }
        ]
      },
      {
        'name': '涟漪点配置',
        'tag': 'Hint'
      },
      {
        'name': '涟漪点显示时机',
        'key': 'showEffectOn',
        'tag': 'select',
        'options': [
          {
            'name': '移入显示',
            'value': 'emphasis'
          },
          {
            'name': '一直显示',
            'value': 'render'
          }
        ]
      },
      {
        'name': '涟漪类型',
        'key': 'rippleEffectperiod',
        'tag': 'select',
        'options': [
          {
            'name': '快',
            'value': 2
          },
          {
            'name': '中',
            'value': 4
          },
          {
            'name': '慢',
            'value': 6
          }
        ]
      },
      {
        'name': '涟漪环数',
        'key': 'symbolSize',
        'tag': 'select',
        'options': [
          {
            'name': '小',
            'value': 3
          },
          {
            'name': '中',
            'value': 6
          },
          {
            'name': '大',
            'value': 9
          },
          {
            'name': '特大',
            'value': 20
          }
        ]
      },
      {
        'name': '文字位置',
        'key': 'normalposition',
        'tag': 'select',
        'options': [
          {
            'name': '上方',
            'value': 'top'
          },
          {
            'name': '下方',
            'value': 'bottom'
          },
          {
            'name': '左方',
            'value': 'left'
          },
          {
            'name': '右方',
            'value': 'right'
          }
        ]
      },
      {
        'name': '文字大小',
        'key': 'normalfontSize',
        'tag': 'input'
      },
      {
        'name': '气泡配置',
        'tag': 'Hint'
      },
      {
        'name': '气泡类型',
        'key': 'EffectbrushType',
        'tag': 'select',
        'options': [
          {
            'name': '类型一',
            'value': 'stroke'
          },
          {
            'name': '类型二',
            'value': 'fill'
          }
        ]
      },
      {
        'name': '气泡缩放比例',
        'key': 'Effectscale',
        'tag': 'select',
        'options': [
          {
            'name': '无',
            'value': 1
          },
          {
            'name': '小',
            'value': 1.4
          },
          {
            'name': '中',
            'value': 1.6
          },
          {
            'name': '大',
            'value': 1.8
          }
        ]
      },
      {
        'name': '气泡大小',
        'key': 'geosymbolSize',
        'tag': 'input'
      },
      {
        'name': '文字位置',
        'key': 'labelposition',
        'tag': 'select',
        'options': [
          {
            'name': '上方',
            'value': 'top'
          },
          {
            'name': '下方',
            'value': 'bottom'
          },
          {
            'name': '左方',
            'value': 'left'
          },
          {
            'name': '右方',
            'value': 'right'
          }
        ]
      },
      {
        'name': '字体颜色',
        'key': 'labelcolor',
        'tag': 'Color'
      },
      {
        'name': '文字大小',
        'key': 'labeltextSize',
        'tag': 'input'
      },
      {
        'name': 'tips配置',
        'tag': 'Hint'
      },
      {
        'name': 'tips背景颜色',
        'key': 'tooltipBackColor',
        'tag': 'Color'
      },
      {
        'name': 'tips字体颜色',
        'key': 'tooltipTextColor',
        'tag': 'Color'
      },
      {
        'name': 'tips字体大小',
        'key': 'tooltipTextfontSize',
        'tag': 'input'
      },
      {
        'name': '图例配置',
        'tag': 'Hint'
      },
      {
        'name': '显示图例',
        'key': 'visualMapShow',
        'tag': 'select',
        'options': [
          {
            'name': '显示',
            'value': 'true'
          },
          {
            'name': '隐藏',
            'value': 'false'
          }
        ]
      },
      {
        'name': '显示图例文字',
        'key': 'calculable',
        'parentKey': {
          'visualMapShow': 'true'
        },
        'tag': 'select',
        'options': [
          {
            'name': '显示',
            'value': 'true'
          },
          {
            'name': '隐藏',
            'value': 'false'
          }
        ]
      },
      {
        'name': '图例字体颜色',
        'key': 'visualMapTextcolor',
        'parentKey': {
          'visualMapShow': 'true'
        },
        'tag': 'Color'
      },
      {
        'name': '直线颜色',
        'key': 'myctColors',
        'tag': 'ColorArray'
      }
    ]
  }
}
