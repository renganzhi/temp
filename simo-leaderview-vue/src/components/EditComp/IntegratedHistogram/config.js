var NewHistogram = {
  'text1': '柱状图',
  'showBackground1': false, // 是否显示背景柱图
  'backgroundColor1': 'rgba(180, 180, 180, 0.2)', // 背景柱图的颜色
  'barRadius1': 0, // 柱体圆角
  'barWidth1': 30, // 柱体大小
  'HistogramType': 1, // 柱状图类型
  'lineColor': '#fff', // 类型2中的分割线颜色
  'downColor': '#6d9eeb', // 类型3中下方颜色
  'upColor': '#4a86e8', // 类型3中上方颜色
  'topTextColor': '#fff', // 柱状图类型2、3顶部文字的颜色
  'imgClass1': 'icon-n-histogram',
  'chartType1': 'NewHistogram',
  'intervieData': 5000,
  'ctLegendShow1': true,
  'colorful1': false,
  'ifGradual1': 'false',
  'legendColor1': '',
  'splitShow1': false,
  'ctLegendSize1': '16',
  'colorType1': 'custom',
  'ScatterColor1': [
    '#6fcaf7',
    '#8feee5',
    '#fa8d76',
    '#af8af3',
    '#f5739c',
    '#ffdf91',
    '#5c84e7'],
  'DScatterColor1': [
    ['#6fcaf7', '#0c79c5'],
    ['#8feee5', '#1bbcae'],
    ['#fa8d76', '#db4222'],
    ['#af8af3', '#874edc'],
    ['#f5739c', '#f31d53'],
    ['#ffdf91', '#eeb01b'],
    ['#5c84e7', '#144fe5'],
    ['#85f8c0', '#62dc26']
  ],
  'ctLegendColor1': '#666f8b',
  'axisLabelSize1': '16',
  'DanweiColor1': '#828bac',
  'DanweiSize1': 16,
  'minInterval1': '',
  'legendY1': 90,
  'gridTop1': 10,
  'gridBotton1': 10,
  'gridLeft1': 10,
  'gridRight1': 10,
  'formatterType1': '0',
  'tooltipShow1': true,
  'tooltipBackColor1': '#57625d',
  'tooltipTextColor1': '#fff',
  'tooltipfontSize1': 14,
  'splitColor1': '#333849',
  'splitSize1': 1,
  'rotate1': 0,
  'chartData1': {
    'columns': ['告警级别', '数量'],
    'unit': '次',
    'rows': [{
      '告警级别': '致命',
      '数量': 233
    },
    {
      '告警级别': '严重',
      '数量': 123
    },
    {
      '告警级别': '警告',
      '数量': 23
    },
    {
      '告警级别': '一般',
      '数量': 155
    },
    {
      '告警级别': '次要',
      '数量': 103
    },
    {
      '告警级别': '通知',
      '数量': 123
    }
    ]
  }
} // 1
var NewGroupHistogram = {
  'text2': '分组柱图',
  'imgClass2': 'icon-n-grouphistogram',
  'chartType2': 'NewGroupHistogram',
  'ifGradual2': 'false',
  'splitShow2': false,
  'legendColor2': '',
  'splitColor2': 'rgba(108, 108, 108, 0.51)',
  'ctLegendShow2': true,
  'splitSize2': 1,
  'ctLegendSize2': '16',
  'barWidth2': 7,
  'barGap2': 0.1,
  'ctLegendColor2': '#666f8b',
  'axisLabelSize2': '14',
  'DanweiColor2': '#828bac',
  'DanweiSize2': 16,
  'minInterval2': '',
  'legendY2': 90,
  'gridTop2': 10,
  'gridBotton2': 10,
  'gridLeft2': 10,
  'gridRight2': 10,
  'tooltipShow2': true,
  'tooltipBackColor2': '#57625d',
  'tooltipTextColor2': '#fff',
  'tooltipfontSize2': 14,
  'subType2': 'groupHistogram',
  'rotate2': 0,
  'formatterType2': '0',
  'ScatterColor2': [
    '#37a2da',
    '#30a4f9',
    '#5cfbff',
    '#af8af3',
    '#f5739c',
    '#ffdf91',
    '#5c84e7'],
  'DScatterColor2': [
    ['#6fcaf7', '#0c79c5'],
    ['#8feee5', '#1bbcae'],
    ['#fa8d76', '#db4222'],
    ['#af8af3', '#874edc'],
    ['#f5739c', '#f31d53'],
    ['#ffdf91', '#eeb01b'],
    ['#5c84e7', '#144fe5'],
    ['#85f8c0', '#62dc26']
  ],
  'chartData2': {
    'columns': ['资源', 'CPU利用率', '内存利用率', '健康度'],
    'unit': '%',
    'rows': [{
      '资源': '192.168.2.32',
      'CPU利用率': 80,
      '内存利用率': 90,
      '健康度': 80
    },
    {
      '资源': '192.168.2.132',
      'CPU利用率': 60,
      '内存利用率': 20,
      '健康度': 70
    },
    {
      '资源': '192.168.2.62',
      'CPU利用率': 10,
      '内存利用率': 60,
      '健康度': 80
    },
    {
      '资源': '192.168.2.200',
      'CPU利用率': 90,
      '内存利用率': 30,
      '健康度': 40
    },
    {
      '资源': '192.168.2.72',
      'CPU利用率': 50,
      '内存利用率': 60,
      '健康度': 70
    },
    {
      '资源': '192.168.2.190',
      'CPU利用率': 45,
      '内存利用率': 76,
      '健康度': 67
    }
    ]
  }
} // 2
var NewGroupLeftHistogram = {
  'text3': '堆叠柱图',
  'showBackground3': false, // 是否显示背景柱图
  'backgroundColor3': 'rgba(180, 180, 180, 0.2)', // 背景柱图的颜色
  'barRadius3': 0, // 柱体圆角
  'barWidth3': 30, // 柱体大小
  'imgClass3': 'icon-n-duidiezhutu',
  'chartType3': 'NewGroupLeftHistogram',
  'subType3': 'groupHistogram',
  'ctLegendShow3': true,
  'legendColor3': '',
  'thirdType3': 'stackHistogram',
  'ifGradual3': 'false',
  'splitShow3': false,
  'ctLegendSize3': '16',
  'ctLegendColor3': '#666f8b',
  'axisLabelSize3': '16',
  'DanweiColor3': '#828bac',
  'DanweiSize3': 16,
  'minInterval3': '',
  'legendY3': 90,
  'gridTop3': 10,
  'gridBotton3': 10,
  'gridLeft3': 10,
  'gridRight3': 10,
  'tooltipShow3': true,
  'tooltipBackColor3': '#57625d',
  'tooltipTextColor3': '#fff',
  'tooltipfontSize3': 14,
  'splitColor3': '#333849',
  'splitSize3': 1,
  'rotate3': 0,
  'formatterType3': '0',
  'ScatterColor3': [
    '#6fcaf7',
    '#8feee5',
    '#fa8d76',
    '#af8af3',
    '#f5739c',
    'ffdf91',
    '5c84e7'],
  'DScatterColor3': [
    ['#6fcaf7', '#0c79c5'],
    ['#8feee5', '#1bbcae'],
    ['#fa8d76', '#db4222'],
    ['#af8af3', '#874edc'],
    ['#f5739c', '#f31d53'],
    ['#ffdf91', '#eeb01b'],
    ['#5c84e7', '#144fe5'],
    ['#85f8c0', '#62dc26']
  ],
  'chartData3': {
    'columns': ['资源', 'CPU利用率', '内存利用率', '健康度'],
    'unit': '%',
    'rows': [{
      '资源': '192.168.2.32',
      'CPU利用率': 80,
      '内存利用率': 90,
      '健康度': 80
    },
    {
      '资源': '192.168.2.132',
      'CPU利用率': 60,
      '内存利用率': 20,
      '健康度': 70
    },
    {
      '资源': '192.168.2.62',
      'CPU利用率': 10,
      '内存利用率': 60,
      '健康度': 80
    },
    {
      '资源': '192.168.2.200',
      'CPU利用率': 90,
      '内存利用率': 30,
      '健康度': 40
    },
    {
      '资源': '192.168.2.72',
      'CPU利用率': 50,
      '内存利用率': 60,
      '健康度': 70
    },
    {
      '资源': '192.168.2.190',
      'CPU利用率': 45,
      '内存利用率': 76,
      '健康度': 67
    }
    ]
  }
} // 3
var NewBar = {
  'text4': '条形图',
  'barWidth4': 30,
  'imgClass4': 'icon-n-bar',
  'chartType4': 'NewBar',
  'ctLegendShow4': true,
  'gradientDirection4': '1', // 条形图渐变方向
  'ifGradual4': 'false',
  'colorful4': false,
  'splitShow4': false,
  'legendColor4': '',
  'ctLegendSize4': '16',
  'ctLegendColor4': '#666f8b',
  'axisLabelSize4': '16',
  'legendY4': 90,
  'gridTop4': 10,
  'gridBotton4': 10,
  'gridLeft4': 10,
  'gridRight4': 10,
  'tooltipShow4': true,
  'DanweiColor4': '#828bac',
  'DanweiSize4': 16,
  'minInterval4': '',
  'tooltipBackColor4': '#57625d',
  'tooltipTextColor4': '#fff',
  'tooltipfontSize4': 14,
  'splitColor4': '#333849',
  'splitSize4': 1,
  'rotate4': 0,
  'formatterType4': '0',
  'ScatterColor4': [
    '#6fcaf7',
    '#8feee5',
    '#fa8d76',
    '#af8af3',
    '#f5739c',
    '#ffdf91',
    '#5c84e7'],
  'DScatterColor4': [
    ['#6fcaf7', '#0c79c5'],
    ['#8feee5', '#1bbcae'],
    ['#fa8d76', '#db4222'],
    ['#af8af3', '#874edc'],
    ['#f5739c', '#f31d53'],
    ['#ffdf91', '#eeb01b'],
    ['#5c84e7', '#144fe5'],
    ['#85f8c0', '#62dc26']
  ],
  'chartData4': {
    'columns': ['告警级别', '数量'],
    'unit': '次',
    'rows': [{
      '告警级别': '致命',
      '数量': 233
    },
    {
      '告警级别': '严重',
      '数量': 123
    },
    {
      '告警级别': '警告',
      '数量': 23
    },
    {
      '告警级别': '一般',
      '数量': 155
    },
    {
      '告警级别': '次要',
      '数量': 103
    },
    {
      '告警级别': '通知',
      '数量': 123
    }
    ]
  }
} // 4

export default {
  'item': {
    'text': '柱状图',
    'imgClass': 'icon-n-histogram',
    'chartType': 'IntegratedHistogram',
    'barType': 'NewHistogram',
    'chartData': {},
    'ifGradual': 'false',
    ...NewHistogram,
    ...NewGroupHistogram,
    ...NewGroupLeftHistogram,
    ...NewBar
  },
  'styles': {
    'base': [
      {
        'name': '图元样式',
        'tag': 'Hint',
        'childoption': [
          {
            'key': 'barType',
            'tag': 'typeSelect',
            'options': [
              {
                'icon': 'icon-n-histogram',
                'value': 'NewHistogram'
              },
              {
                'icon': 'icon-n-grouphistogram',
                'value': 'NewGroupHistogram'
              },
              {
                'icon': 'icon-n-duidiezhutu',
                'value': 'NewGroupLeftHistogram'
              },
              {
                'icon': 'icon-n-bar',
                'value': 'NewBar'
              }
            ]
          }
        ]
      },
      {
        'name': '类型选择',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '柱状图类型',
            'key': 'HistogramType',
            'tag': 'select',
            'options': [
              {
                'name': '类型一',
                'value': 1
              },
              {
                'name': '类型二',
                'value': 2
              },
              {
                'name': '类型三',
                'value': 3
              }
            ]
          },
          {
            'name': '分割线颜色',
            'tag': 'Color',
            'key': 'lineColor',
            'parentKey': {
              'HistogramType': 2
            }
          },
          {
            'name': '顶部文字颜色',
            'tag': 'Color',
            'key': 'topTextColor',
            'parentKey': {
              'HistogramType': 2
            }
          },
          {
            'name': '顶部文字颜色',
            'tag': 'Color',
            'key': 'topTextColor',
            'parentKey': {
              'HistogramType': 3
            }
          },
          {
            'name': '上方颜色',
            'tag': 'Color',
            'key': 'upColor',
            'parentKey': {
              'HistogramType': 3
            }
          },
          {
            'name': '下方颜色',
            'tag': 'Color',
            'key': 'downColor',
            'parentKey': {
              'HistogramType': 3
            }
          }
        ],
        'parentKey': {
          'barType': 'NewHistogram'
        }
      },
      {
        'name': '图例配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '图例可见性',
            'key': 'ctLegendShow1',
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
            'name': '图例字体大小',
            'key': 'ctLegendSize1',
            'parentKey': {
              'ctLegendShow1': true
            },
            'tag': 'select',
            'options': [
              {
                'name': '8',
                'value': 8
              },
              {
                'name': '10',
                'value': 10
              },
              {
                'name': '12',
                'value': 12
              },
              {
                'name': '14',
                'value': 14
              },
              {
                'name': '16',
                'value': 16
              },
              {
                'name': '18',
                'value': 18
              },
              {
                'name': '20',
                'value': 20
              },
              {
                'name': '24',
                'value': 24
              },
              {
                'name': '28',
                'value': 28
              },
              {
                'name': '30',
                'value': 30
              }
            ]
          },
          {
            'name': '图例字颜色',
            'key': 'ctLegendColor1',
            'tag': 'Color'
          },
          {
            'name': '图例高度',
            'parentKey': {
              'ctLegendShow1': true
            },
            'key': 'legendY1',
            'tag': 'input'
          },
          {
            'name': '图元上边距',
            'key': 'gridTop1',
            'tag': 'input'
          },
          {
            'name': '图元下边距',
            'key': 'gridBotton1',
            'tag': 'input'
          },
          {
            'name': '图元左边距',
            'key': 'gridLeft1',
            'tag': 'input'
          },
          {
            'name': '图元右边距',
            'key': 'gridRight1',
            'tag': 'input'
          }
        ],
        'parentKey': {
          'barType': 'NewHistogram'
        }
      },
      {
        'name': 'tips配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': 'tips可见性',
            'key': 'tooltipShow1',
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
            'parentKey': {
              'tooltipShow1': true
            },
            'key': 'tooltipBackColor1',
            'tag': 'Color'
          },
          {
            'name': 'tips字体色',
            'key': 'tooltipTextColor1',
            'parentKey': {
              'tooltipShow1': true
            },
            'tag': 'Color'
          },
          {
            'name': 'tips字体大小',
            'key': 'tooltipfontSize1',
            'parentKey': {
              'tooltipShow1': true
            },
            'tag': 'input'
          }
        ],
        'parentKey': {
          'barType': 'NewHistogram'
        }
      },
      {
        'name': '图元配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '背景色',
            'key': 'showBackground1',
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
            'name': '背景色颜色',
            'key': 'backgroundColor1',
            'tag': 'Color',
            'parentKey': {
              'showBackground1': true
            }
          },
          {
            'name': '圆角大小',
            'key': 'barRadius1',
            'tag': 'input'
          },
          {
            'name': '柱体大小',
            'key': 'barWidth1',
            'tag': 'input'
          }
        ],
        'parentKey': {
          'barType': 'NewHistogram'
        }
      },
      {
        'name': '坐标线配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '坐标线可见性',
            'key': 'splitShow1',
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
            'name': '坐标刻度类型',
            'key': 'minInterval1',
            'tag': 'select',
            'options': [
              {
                'name': '整数',
                'value': 1
              },
              {
                'name': '自适应',
                'value': ''
              }
            ]
          },
          {
            'name': '坐标线颜色',
            'key': 'splitColor1',
            'tag': 'Color'
          },
          {
            'name': '线条粗细',
            'key': 'splitSize1',
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
              }
            ]
          },
          {
            'name': '坐标文字颜色',
            'key': 'legendColor1',
            'tag': 'Color'
          },
          {
            'name': '坐标文字大小',
            'key': 'axisLabelSize1',
            'tag': 'select',
            'options': [
              {
                'name': '8',
                'value': 8
              },
              {
                'name': '10',
                'value': 10
              },
              {
                'name': '14',
                'value': 14
              },
              {
                'name': '16',
                'value': 16
              },
              {
                'name': '20',
                'value': 20
              },
              {
                'name': '24',
                'value': 24
              },
              {
                'name': '28',
                'value': 28
              },
              {
                'name': '30',
                'value': 30
              }
            ]
          },
          {
            'name': '坐标单位颜色',
            'key': 'DanweiColor1',
            'tag': 'Color'
          },
          {
            'name': '坐标单位大小',
            'key': 'DanweiSize1',
            'tag': 'select',
            'options': [
              {
                'name': '8',
                'value': 8
              },
              {
                'name': '10',
                'value': 10
              },
              {
                'name': '14',
                'value': 14
              },
              {
                'name': '16',
                'value': 16
              },
              {
                'name': '20',
                'value': 20
              },
              {
                'name': '24',
                'value': 24
              },
              {
                'name': '28',
                'value': 28
              },
              {
                'name': '30',
                'value': 30
              }
            ]
          }
        ],
        'parentKey': {
          'barType': 'NewHistogram'
        }
      },
      {
        'name': '坐标轴样式',
        'tag': 'Hint',
        'childoption': [{
          'name': 'x轴标注倾斜',
          'key': 'rotate1',
          'tag': 'select',
          'options': [
            {
              'name': '0',
              'value': 0
            },
            {
              'name': '10',
              'value': 10
            },
            {
              'name': '20',
              'value': 20
            },
            {
              'name': '30',
              'value': 30
            },
            {
              'name': '40',
              'value': 40
            },
            {
              'name': '-10',
              'value': -10
            },
            {
              'name': '-20',
              'value': -20
            },
            {
              'name': '-30',
              'value': -30
            },
            {
              'name': '-40',
              'value': -40
            }
          ]
        },
        {
          'name': 'x轴标注剪裁',
          'key': 'formatterType1',
          'tag': 'select',
          'options': [
            {
              'name': '自适应',
              'value': '0'
            },
            {
              'name': '不裁剪',
              'value': '1'
            }
          ]
        }
        ],
        'parentKey': {
          'barType': 'NewHistogram'
        }
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
          'name': '柱体类型',
          'key': 'colorful1',
          'tag': 'select',
          'options': [
            {
              'name': '异色柱',
              'value': true
            },
            {
              'name': '同色柱',
              'value': false
            }
          ]
        }, {
          'name': '颜色配置',
          'key': 'ScatterColor1',
          'parentKey': {
            'ifGradual': 'false'
          },
          'tag': 'ColorArray'
        }, {
          'name': '颜色配置',
          'key': 'DScatterColor1',
          'parentKey': {
            'ifGradual': 'true'
          },
          'tag': 'ColorArray'
        }],
        'parentKey': {
          'barType': 'NewHistogram'
        }
      }, // NewHistogram
      {
        'name': '图例配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '柱体宽度',
            'key': 'barWidth2',
            'tag': 'input',
            'min': 0
          },
          {
            'name': '柱体间距',
            'key': 'barGap2',
            'tag': 'select',
            'options': [
              {
                'name': '0',
                'value': 0
              },
              {
                'name': '0.1',
                'value': 0.1
              },
              {
                'name': '0.2',
                'value': 0.2
              },
              {
                'name': '0.4',
                'value': 0.4
              },
              {
                'name': '0.6',
                'value': 0.6
              },
              {
                'name': '1',
                'value': 1
              }
            ]
          },
          {
            'name': '图例可见性',
            'key': 'ctLegendShow2',
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
            'name': '图例字体大小',
            'key': 'ctLegendSize2',
            'parentKey': {
              'ctLegendShow2': true
            },
            'tag': 'select',
            'options': [
              {
                'name': '8',
                'value': 8
              },
              {
                'name': '10',
                'value': 10
              },
              {
                'name': '12',
                'value': 12
              },
              {
                'name': '14',
                'value': 14
              },
              {
                'name': '16',
                'value': 16
              },
              {
                'name': '18',
                'value': 18
              },
              {
                'name': '20',
                'value': 20
              },
              {
                'name': '24',
                'value': 24
              },
              {
                'name': '28',
                'value': 28
              },
              {
                'name': '30',
                'value': 30
              }
            ]
          },
          {
            'name': '图例字颜色',
            'key': 'ctLegendColor2',
            'parentKey': {
              'ctLegendShow2': true
            },
            'tag': 'Color'
          },
          {
            'name': '图例高度',
            'parentKey': {
              'ctLegendShow2': true
            },
            'key': 'legendY2',
            'tag': 'input'
          },
          {
            'name': '图元上边距',
            'key': 'gridTop2',
            'tag': 'input'
          },
          {
            'name': '图元下边距',
            'key': 'gridBotton2',
            'tag': 'input'
          },
          {
            'name': '图元左边距',
            'key': 'gridLeft2',
            'tag': 'input'
          },
          {
            'name': '图元右边距',
            'key': 'gridRight2',
            'tag': 'input'
          }
        ],
        'parentKey': {
          'barType': 'NewGroupHistogram'
        }
      },
      {
        'name': 'tips配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': 'tips可见性',
            'key': 'tooltipShow2',
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
            'parentKey': {
              'tooltipShow2': true
            },
            'key': 'tooltipBackColor2',
            'tag': 'Color'
          },
          {
            'name': 'tips字体色',
            'key': 'tooltipTextColor2',
            'parentKey': {
              'tooltipShow2': true
            },
            'tag': 'Color'
          },
          {
            'name': 'tips字体大小',
            'key': 'tooltipfontSize2',
            'parentKey': {
              'tooltipShow2': true
            },
            'tag': 'input'
          }
        ],
        'parentKey': {
          'barType': 'NewGroupHistogram'
        }
      },
      {
        'name': '坐标线配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '坐标线可见性',
            'key': 'splitShow2',
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
            'name': '坐标刻度类型',
            'key': 'minInterval2',
            'tag': 'select',
            'options': [
              {
                'name': '整数',
                'value': 1
              },
              {
                'name': '自适应',
                'value': ''
              }
            ]
          },
          {
            'name': '坐标线颜色',
            'key': 'splitColor2',
            'tag': 'Color'
          },
          {
            'name': '线条粗细',
            'key': 'splitSize2',
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
              }
            ]
          },
          {
            'name': '坐标文字颜色',
            'key': 'legendColor2',
            'tag': 'Color'
          },
          {
            'name': '坐标文字大小',
            'key': 'axisLabelSize2',
            'tag': 'select',
            'options': [
              {
                'name': '8',
                'value': 8
              },
              {
                'name': '10',
                'value': 10
              },
              {
                'name': '14',
                'value': 14
              },
              {
                'name': '16',
                'value': 16
              },
              {
                'name': '20',
                'value': 20
              },
              {
                'name': '24',
                'value': 24
              },
              {
                'name': '28',
                'value': 28
              },
              {
                'name': '30',
                'value': 30
              }
            ]
          },
          {
            'name': '坐标单位颜色',
            'key': 'DanweiColor2',
            'tag': 'Color'
          },
          {
            'name': '坐标单位大小',
            'key': 'DanweiSize2',
            'tag': 'select',
            'options': [
              {
                'name': '8',
                'value': 8
              },
              {
                'name': '10',
                'value': 10
              },
              {
                'name': '14',
                'value': 14
              },
              {
                'name': '16',
                'value': 16
              },
              {
                'name': '20',
                'value': 20
              },
              {
                'name': '24',
                'value': 24
              },
              {
                'name': '28',
                'value': 28
              },
              {
                'name': '30',
                'value': 30
              }
            ]
          }
        ],
        'parentKey': {
          'barType': 'NewGroupHistogram'
        }
      },
      {
        'name': '坐标轴样式',
        'tag': 'Hint',
        'childoption': [{
          'name': 'x轴标注倾斜',
          'key': 'rotate2',
          'tag': 'select',
          'options': [
            {
              'name': '0',
              'value': 0
            },
            {
              'name': '10',
              'value': 10
            },
            {
              'name': '20',
              'value': 20
            },
            {
              'name': '30',
              'value': 30
            },
            {
              'name': '40',
              'value': 40
            },
            {
              'name': '-10',
              'value': -10
            },
            {
              'name': '-20',
              'value': -20
            },
            {
              'name': '-30',
              'value': -30
            },
            {
              'name': '-40',
              'value': -40
            }
          ]
        },
        {
          'name': 'x轴标注剪裁',
          'key': 'formatterType2',
          'tag': 'select',
          'options': [
            {
              'name': '自适应',
              'value': '0'
            },
            {
              'name': '不裁剪',
              'value': '1'
            }
          ]
        }
        ],
        'parentKey': {
          'barType': 'NewGroupHistogram'
        }
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
          'name': '颜色配置',
          'key': 'ScatterColor2',
          'parentKey': {
            'ifGradual': 'false'
          },
          'tag': 'ColorArray'
        }, {
          'name': '颜色配置',
          'key': 'DScatterColor2',
          'parentKey': {
            'ifGradual': 'true'
          },
          'tag': 'ColorArray'
        }],
        'parentKey': {
          'barType': 'NewGroupHistogram'
        }
      }, // NewGroupHistogram
      {
        'name': '图例配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '图例可见性',
            'key': 'ctLegendShow3',
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
            'name': '图例字体大小',
            'key': 'ctLegendSize3',
            'parentKey': {
              'ctLegendShow3': true
            },
            'tag': 'select',
            'options': [
              {
                'name': '8',
                'value': 8
              },
              {
                'name': '10',
                'value': 10
              },
              {
                'name': '12',
                'value': 12
              },
              {
                'name': '14',
                'value': 14
              },
              {
                'name': '16',
                'value': 16
              },
              {
                'name': '18',
                'value': 18
              },
              {
                'name': '20',
                'value': 20
              },
              {
                'name': '24',
                'value': 24
              },
              {
                'name': '28',
                'value': 28
              },
              {
                'name': '30',
                'value': 30
              }
            ]
          },
          {
            'name': '图例字颜色',
            'key': 'ctLegendColor3',
            'tag': 'Color'
          },
          {
            'name': '图例高度',
            'parentKey': {
              'ctLegendShow3': true
            },
            'key': 'legendY3',
            'tag': 'input'
          },
          {
            'name': '图元上边距',
            'key': 'gridTop3',
            'tag': 'input'
          },
          {
            'name': '图元下边距',
            'key': 'gridBotton3',
            'tag': 'input'
          },
          {
            'name': '图元左边距',
            'key': 'gridLeft3',
            'tag': 'input'
          },
          {
            'name': '图元右边距',
            'key': 'gridRight3',
            'tag': 'input'
          }
        ],
        'parentKey': {
          'barType': 'NewGroupLeftHistogram'
        }
      },
      {
        'name': 'tips配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': 'tips可见性',
            'key': 'tooltipShow3',
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
            'parentKey': {
              'tooltipShow3': true
            },
            'key': 'tooltipBackColor3',
            'tag': 'Color'
          },
          {
            'name': 'tips字体色',
            'key': 'tooltipTextColor3',
            'parentKey': {
              'tooltipShow3': true
            },
            'tag': 'Color'
          },
          {
            'name': 'tips字体大小',
            'key': 'tooltipfontSize3',
            'parentKey': {
              'tooltipShow3': true
            },
            'tag': 'input'
          }
        ],
        'parentKey': {
          'barType': 'NewGroupLeftHistogram'
        }
      },
      {
        'name': '图元配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '背景色',
            'key': 'showBackground3',
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
            'name': '背景色颜色',
            'key': 'backgroundColor3',
            'tag': 'Color',
            'parentKey': {
              'showBackground3': true
            }
          },
          {
            'name': '圆角大小',
            'key': 'barRadius3',
            'tag': 'input'
          },
          {
            'name': '柱体大小',
            'key': 'barWidth3',
            'tag': 'input'
          }
        ],
        'parentKey': {
          'barType': 'NewGroupLeftHistogram'
        }
      },
      {
        'name': '坐标线配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '坐标线可见性',
            'key': 'splitShow3',
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
            'name': '坐标刻度类型',
            'key': 'minInterval3',
            'tag': 'select',
            'options': [
              {
                'name': '整数',
                'value': 1
              },
              {
                'name': '自适应',
                'value': ''
              }
            ]
          },
          {
            'name': '坐标线颜色',
            'key': 'splitColor3',
            'tag': 'Color'
          },
          {
            'name': '线条粗细',
            'key': 'splitSize3',
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
              }
            ]
          },
          {
            'name': '坐标文字颜色',
            'key': 'legendColor3',
            'tag': 'Color'
          },
          {
            'name': '坐标文字大小',
            'key': 'axisLabelSize3',
            'tag': 'select',
            'options': [
              {
                'name': '8',
                'value': 8
              },
              {
                'name': '10',
                'value': 10
              },
              {
                'name': '14',
                'value': 14
              },
              {
                'name': '16',
                'value': 16
              },
              {
                'name': '20',
                'value': 20
              },
              {
                'name': '24',
                'value': 24
              },
              {
                'name': '28',
                'value': 28
              },
              {
                'name': '30',
                'value': 30
              }
            ]
          },
          {
            'name': '坐标单位颜色',
            'key': 'DanweiColor3',
            'tag': 'Color'
          },
          {
            'name': '坐标单位大小',
            'key': 'DanweiSize3',
            'tag': 'select',
            'options': [
              {
                'name': '8',
                'value': 8
              },
              {
                'name': '10',
                'value': 10
              },
              {
                'name': '14',
                'value': 14
              },
              {
                'name': '16',
                'value': 16
              },
              {
                'name': '20',
                'value': 20
              },
              {
                'name': '24',
                'value': 24
              },
              {
                'name': '28',
                'value': 28
              },
              {
                'name': '30',
                'value': 30
              }
            ]
          }
        ],
        'parentKey': {
          'barType': 'NewGroupLeftHistogram'
        }
      },
      {
        'name': '坐标轴样式',
        'tag': 'Hint',
        'childoption': [{
          'name': 'x轴标注倾斜',
          'key': 'rotate3',
          'tag': 'select',
          'options': [
            {
              'name': '0',
              'value': 0
            },
            {
              'name': '10',
              'value': 10
            },
            {
              'name': '20',
              'value': 20
            },
            {
              'name': '30',
              'value': 30
            },
            {
              'name': '40',
              'value': 40
            },
            {
              'name': '-10',
              'value': -10
            },
            {
              'name': '-20',
              'value': -20
            },
            {
              'name': '-30',
              'value': -30
            },
            {
              'name': '-40',
              'value': -40
            }
          ]
        },
        {
          'name': 'x轴标注剪裁',
          'key': 'formatterType3',
          'tag': 'select',
          'options': [
            {
              'name': '自适应',
              'value': '0'
            },
            {
              'name': '不裁剪',
              'value': '1'
            }
          ]
        }
        ],
        'parentKey': {
          'barType': 'NewGroupLeftHistogram'
        }
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
          'name': '颜色配置',
          'key': 'ScatterColor3',
          'parentKey': {
            'ifGradual': 'false'
          },
          'tag': 'ColorArray'
        }, {
          'name': '颜色配置',
          'key': 'DScatterColor3',
          'parentKey': {
            'ifGradual': 'true'
          },
          'tag': 'ColorArray'
        }],
        'parentKey': {
          'barType': 'NewGroupLeftHistogram'
        }
      }, // NewGroupLeftHistogram
      {
        'name': '图例配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '图例可见性',
            'key': 'ctLegendShow4',
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
            'name': '图例字体大小',
            'key': 'ctLegendSize4',
            'parentKey': {
              'ctLegendShow4': true
            },
            'tag': 'select',
            'options': [
              {
                'name': '8',
                'value': 8
              },
              {
                'name': '10',
                'value': 10
              },
              {
                'name': '12',
                'value': 12
              },
              {
                'name': '14',
                'value': 14
              },
              {
                'name': '16',
                'value': 16
              },
              {
                'name': '18',
                'value': 18
              },
              {
                'name': '20',
                'value': 20
              },
              {
                'name': '24',
                'value': 24
              },
              {
                'name': '28',
                'value': 28
              },
              {
                'name': '30',
                'value': 30
              }
            ]
          },
          {
            'name': '图例字颜色',
            'key': 'ctLegendColor4',
            'tag': 'Color'
          },
          {
            'name': '图例高度',
            'parentKey': {
              'ctLegendShow4': true
            },
            'key': 'legendY4',
            'tag': 'input'
          },
          {
            'name': '图元上边距',
            'key': 'gridTop4',
            'tag': 'input'
          },
          {
            'name': '图元下边距',
            'key': 'gridBotton4',
            'tag': 'input'
          },
          {
            'name': '图元左边距',
            'key': 'gridLeft4',
            'tag': 'input'
          },
          {
            'name': '图元右边距',
            'key': 'gridRight4',
            'tag': 'input'
          }
        ],
        'parentKey': {
          'barType': 'NewBar'
        }
      },
      {
        'name': 'tips配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': 'tips可见性',
            'key': 'tooltipShow4',
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
            'parentKey': {
              'tooltipShow4': true
            },
            'key': 'tooltipBackColor4',
            'tag': 'Color'
          },
          {
            'name': 'tips字体色',
            'key': 'tooltipTextColor4',
            'parentKey': {
              'tooltipShow4': true
            },
            'tag': 'Color'
          },
          {
            'name': 'tips字体大小',
            'key': 'tooltipfontSize4',
            'parentKey': {
              'tooltipShow4': true
            },
            'tag': 'input'
          }
        ],
        'parentKey': {
          'barType': 'NewBar'
        }
      },
      {
        'name': '图元配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '柱体大小',
            'key': 'barWidth4',
            'tag': 'input'
          }
        ],
        'parentKey': {
          'barType': 'NewBar'
        }
      },
      {
        'name': '坐标线配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '坐标线可见性',
            'key': 'splitShow4',
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
            'name': '坐标刻度类型',
            'key': 'minInterval4',
            'tag': 'select',
            'options': [
              {
                'name': '整数',
                'value': 1
              },
              {
                'name': '自适应',
                'value': ''
              }
            ]
          },
          {
            'name': '坐标线颜色',
            'key': 'splitColor4',
            'tag': 'Color'
          },
          {
            'name': '线条粗细',
            'key': 'splitSize4',
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
              }
            ]
          },
          {
            'name': '坐标文字颜色',
            'key': 'legendColor4',
            'tag': 'Color'
          },
          {
            'name': '坐标文字大小',
            'key': 'axisLabelSize4',
            'tag': 'select',
            'options': [
              {
                'name': '8',
                'value': 8
              },
              {
                'name': '10',
                'value': 10
              },
              {
                'name': '14',
                'value': 14
              },
              {
                'name': '16',
                'value': 16
              },
              {
                'name': '20',
                'value': 20
              },
              {
                'name': '24',
                'value': 24
              },
              {
                'name': '28',
                'value': 28
              },
              {
                'name': '30',
                'value': 30
              }
            ]
          },
          {
            'name': '坐标单位颜色',
            'key': 'DanweiColor4',
            'tag': 'Color'
          },
          {
            'name': '坐标单位大小',
            'key': 'DanweiSize4',
            'tag': 'select',
            'options': [
              {
                'name': '8',
                'value': 8
              },
              {
                'name': '10',
                'value': 10
              },
              {
                'name': '14',
                'value': 14
              },
              {
                'name': '16',
                'value': 16
              },
              {
                'name': '20',
                'value': 20
              },
              {
                'name': '24',
                'value': 24
              },
              {
                'name': '28',
                'value': 28
              },
              {
                'name': '30',
                'value': 30
              }
            ]
          }
        ],
        'parentKey': {
          'barType': 'NewBar'
        }
      },
      {
        'name': '坐标轴样式',
        'tag': 'Hint',
        'childoption': [{
          'name': 'y轴标注倾斜',
          'key': 'rotate4',
          'tag': 'select',
          'options': [
            {
              'name': '0',
              'value': 0
            },
            {
              'name': '10',
              'value': 10
            },
            {
              'name': '20',
              'value': 20
            },
            {
              'name': '30',
              'value': 30
            },
            {
              'name': '40',
              'value': 40
            },
            {
              'name': '-10',
              'value': -10
            },
            {
              'name': '-20',
              'value': -20
            },
            {
              'name': '-30',
              'value': -30
            },
            {
              'name': '-40',
              'value': -40
            }
          ]
        },
        {
          'name': 'y轴标注剪裁',
          'key': 'formatterType4',
          'tag': 'select',
          'options': [
            {
              'name': '自适应',
              'value': '0'
            },
            {
              'name': '不裁剪',
              'value': '1'
            }
          ]
        }
        ],
        'parentKey': {
          'barType': 'NewBar'
        }
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
          'name': '渐变方向',
          'key': 'gradientDirection4',
          'tag': 'select',
          'options': [
            {
              'name': '上下渐变',
              'value': '1'
            },
            {
              'name': '左右渐变',
              'value': '2'
            }
          ],
          'parentKey': {
            'ifGradual': 'true'
          }
        }, {
          'name': '柱体类型',
          'key': 'colorful4',
          'tag': 'select',
          'options': [
            {
              'name': '异色柱',
              'value': true
            },
            {
              'name': '同色柱',
              'value': false
            }
          ]
        }, {
          'name': '颜色配置',
          'key': 'ScatterColor4',
          'parentKey': {
            'ifGradual': 'false'
          },
          'tag': 'ColorArray'
        }, {
          'name': '颜色配置',
          'key': 'DScatterColor4',
          'parentKey': {
            'ifGradual': 'true'
          },
          'tag': 'ColorArray'
        }],
        'parentKey': {
          'barType': 'NewBar'
        }
      }
    ]
  }
}
