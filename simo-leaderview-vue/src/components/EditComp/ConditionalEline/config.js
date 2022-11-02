export default {
  'item': {
    'text': '条件曲线图',
    'imgClass': 'icon-n-line',
    'height': 400,
    'width': 700,
    'chartType': 'ConditionalEline',
    'ifEidetColor': false, // 曲线是否配色
    'ifEidetColor2': false,
    'ifGradual': 'false', // 曲线是否渐变
    'ifAreaGradual': 'false', // 区域是否渐变
    'splitShow': false,
    'ctLegendShow': true,
    'ctLegendColor': '#828bac',
    'ctLegendSize': '16',
    'axisLabelSize': '16',
    'legendY': 90,
    'gridTop': 10,
    'gridBotton': 10,
    'gridLeft': 10,
    'gridRight': 10,
    'tooltipShow': true,
    'subsectionType': true,
    'areaLineType': true,
    'tooltipBackColor': '#57625d',
    'tooltipTextColor': '#e9eaee',
    'tooltipfontSize': 14,
    'splitColor': '#333849',
    'splitSize': 1,
    'minInterval': '',
    'Linesubsection': false,
    'boundaryGap': true,
    'legendColor': '#828bac',
    'DanweiColor': '#828bac',
    'DanweiSize': 16,
    'lineArea': true, // 是否为区域图
    'lineColorType': false, // 是否为区域图
    'smooth': false,
    'LineType': 'solid',
    'formatterType': '0',
    'symbolType': 'circle',
    'symbolSrc': '',
    'symbolName': '',
    'symbolSize': 6,
    'lineWidth': 1,
    'showPoint': true, // 是否标点
    'PointSize': '14',
    'rotate': 0,
    'colorMatchType': 'line', // 配色类型
    'ScatterColor': [
      '#2d98f1',
      '#32c5e9',
      '#67e0e3',
      '#9fe6b8',
      '#ffdb5c'],
    paddingTop: 0,
    paddingLeft: 0,
    boxFontSize: 16,
    buttonPadding: 20,
    buttonMargin: 20,
    boxDirection: false,
    normalButton: '',
    normalButtonName: '',
    checkedButton: '',
    checkedButtonName: '',
    'DScatterColor': [
      ['rgba(213, 153, 17, 0.52)', '#be4d24'],
      ['rgba(2, 210, 255, 0.49)', '#1bbcae'],
      ['#fa8d76', '#db4222'],
      ['#af8af3', '#874edc'],
      ['#f5739c', '#f31d53'],
      ['#ffdf91', '#eeb01b'],
      ['#5c84e7', '#144fe5'],
      ['#85f8c0', '#62dc26']
    ],
    'AreaScatterColor': [
      '#2d98f1',
      '#32c5e9',
      '#67e0e3',
      '#9fe6b8',
      '#ffdb5c'
    ], // 区域单色
    'AreaDScatterColor': [
      ['rgba(213, 153, 17, 0.52)', '#be4d24'],
      ['rgba(2, 210, 255, 0.49)', '#1bbcae'],
      ['#fa8d76', '#db4222'],
      ['#af8af3', '#874edc'],
      ['#f5739c', '#f31d53'],
      ['#ffdf91', '#eeb01b'],
      ['#5c84e7', '#144fe5'],
      ['#85f8c0', '#62dc26']
    ], // 区域渐变
    'chartData': {
      dataArray: [
        {
          'title': '风险告警',
          'columns': ['日期', 'CPU核心利用率', 'CPU平均利用率'],
          'unit': '%',
          'min': 60,
          'max': 80,
          'minIndex': 2,
          'maxIndex': 3,
          'unitX': '时间',
          'rows': [{
            '日期': '2020-01-01',
            'CPU核心利用率': 15,
            'CPU平均利用率': 15
          },
          {
            '日期': '2020-01-02',
            'CPU核心利用率': 80,
            'CPU平均利用率': 50
          },
          {
            '日期': '2020-01-03',
            'CPU核心利用率': 40,
            'CPU平均利用率': 6
          },
          {
            '日期': '2020-01-05',
            'CPU核心利用率': 45,
            'CPU平均利用率': 70
          },
          {
            '日期': '2020-01-06',
            'CPU核心利用率': 10,
            'CPU平均利用率': 40
          },
          {
            '日期': '2020-01-07',
            'CPU核心利用率': 95,
            'CPU平均利用率': 50
          }
          ]
        },
        {
          'title': '终端告警',
          'columns': ['日期', 'CPU核心利用率', 'CPU平均利用率'],
          'unit': '%',
          'min': 60,
          'max': 80,
          'minIndex': 2,
          'maxIndex': 3,
          'unitX': '时间',
          'rows': [{
            '日期': '2020-01-01',
            'CPU核心利用率': 15,
            'CPU平均利用率': 15
          },
          {
            '日期': '2020-01-02',
            'CPU核心利用率': 80,
            'CPU平均利用率': 50
          },
          {
            '日期': '2020-01-03',
            'CPU核心利用率': 40,
            'CPU平均利用率': 6
          },
          {
            '日期': '2020-01-05',
            'CPU核心利用率': 45,
            'CPU平均利用率': 70
          },
          {
            '日期': '2020-01-06',
            'CPU核心利用率': 10,
            'CPU平均利用率': 40
          },
          {
            '日期': '2020-01-07',
            'CPU核心利用率': 95,
            'CPU平均利用率': 50
          }
          ]
        }
      ]
    }
  },
  'styles': {
    'base': [
      {
        'name': '图例配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '图例可见性',
            'key': 'ctLegendShow',
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
            'name': '选择框上边距',
            'key': 'paddingTop',
            'tag': 'input'
          }, {
            'name': '选择框左边距',
            'key': 'paddingLeft',
            'tag': 'input'
          },
          {
            'name': '选择框方向',
            'key': 'boxDirection',
            'tag': 'select',
            'options': [
              {
                'name': '纵向',
                'value': true
              },
              {
                'name': '横向',
                'value': false
              }
            ]
          }, {
            'name': '按钮文字大小',
            'key': 'boxFontSize',
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
            'key': 'normalButton',
            'tag': 'ImgFile',
            'keyName': 'normalButtonName'
          }, {
            'name': '高亮按钮背景',
            'key': 'checkedButton',
            'tag': 'ImgFile',
            'keyName': 'checkedButtonName'
          },
          {
            'name': '图例字体大小',
            'key': 'ctLegendSize',
            'parentKey': {
              'ctLegendShow': true
            },
            'tag': 'input'
          },
          {
            'name': '图例高度',
            'parentKey': {
              'ctLegendShow': true
            },
            'key': 'legendY',
            'tag': 'input'
          },
          {
            'name': '图例字颜色',
            'key': 'ctLegendColor',
            'tag': 'Color'
          },
          {
            'name': '图元上边距',
            'key': 'gridTop',
            'tag': 'input'
          },
          {
            'name': '图元下边距',
            'key': 'gridBotton',
            'tag': 'input'
          },
          {
            'name': '图元左边距',
            'key': 'gridLeft',
            'tag': 'input'
          },
          {
            'name': '图元右边距',
            'key': 'gridRight',
            'tag': 'input'
          }
        ]
      },
      {
        'name': 'tips配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': 'tips可见性',
            'key': 'tooltipShow',
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
              'tooltipShow': true
            },
            'key': 'tooltipBackColor',
            'tag': 'Color'
          },
          {
            'name': 'tips字体色',
            'key': 'tooltipTextColor',
            'parentKey': {
              'tooltipShow': true
            },
            'tag': 'Color'
          },
          {
            'name': 'tips字体大小',
            'key': 'tooltipfontSize',
            'parentKey': {
              'tooltipShow': true
            },
            'tag': 'input'
          }
        ]
      },
      {
        'name': '坐标线配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '坐标线可见性',
            'key': 'splitShow',
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
            'key': 'minInterval',
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
            'key': 'splitColor',
            'tag': 'Color'
          },
          {
            'name': '线条粗细',
            'key': 'splitSize',
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
            'key': 'legendColor',
            'tag': 'Color'
          },
          {
            'name': '坐标文字大小',
            'key': 'axisLabelSize',
            'tag': 'input'
          },
          {
            'name': '坐标单位颜色',
            'key': 'DanweiColor',
            'tag': 'Color'
          },
          {
            'name': '坐标单位大小',
            'key': 'DanweiSize',
            'tag': 'input'
          },
          {
            'name': '折线图类型',
            'key': 'lineArea',
            'tag': 'select',
            'options': [
              {
                'name': '折线图',
                'value': false
              },
              {
                'name': '区域图',
                'value': true
              }
            ]
          },
          {
            'name': '线距Y轴距离',
            'key': 'boundaryGap',
            'tag': 'select',
            'options': [
              {
                'name': '开启',
                'value': true
              },
              {
                'name': '关闭',
                'value': false
              }
            ]
          },
          {
            'name': '线条类型',
            'key': 'smooth',
            'tag': 'select',
            'options': [
              {
                'name': '曲线',
                'value': false
              },
              {
                'name': '折线',
                'value': true
              }
            ]
          },
          {
            'name': '数据线类型',
            'key': 'LineType',
            'tag': 'select',
            'options': [
              {
                'name': '实线',
                'value': 'solid'
              },
              {
                'name': '虚线',
                'value': 'dashed'
              },
              {
                'name': '点状线',
                'value': 'dotted'
              }
            ]
          },
          {
            'name': '数据线分段',
            'key': 'Linesubsection',
            'parentKey': {
              'lineArea': true
            },
            'tag': 'select',
            'options': [
              {
                'name': '分段',
                'value': true
              },
              {
                'name': '不分段',
                'value': false
              }
            ]
          },
          {
            'name': '分段类型',
            'key': 'subsectionType',
            'parentKey': {
              'Linesubsection': true,
              'lineArea': true
            },
            'tag': 'select',
            'options': [
              {
                'name': '纵向',
                'value': true
              },
              {
                'name': '横向',
                'value': false
              }
            ]
          },
          {
            'name': '线条宽度',
            'key': 'lineWidth',
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
              }
            ]
          },
          {
            'name': '数据点类型',
            'key': 'symbolType',
            'tag': 'select',
            'options': [
              {
                'name': '空白圆点',
                'value': 'emptyCircle'
              },
              {
                'name': '实心圆点',
                'value': 'circle'
              },
              {
                'name': '三角',
                'value': 'triangle'
              },
              {
                'name': '气泡',
                'value': 'pin'
              },
              {
                'name': '不显示',
                'value': 'none'
              },
              {
                'name': '自定义图片',
                'value': 'pic'
              }
            ]
          },
          {
            'name': '数据点图片',
            'parentKey': {
              'symbolType': 'pic'
            },
            'key': 'symbolSrc',
            'keyName': 'symbolName',
            'tag': 'ImgFile'
          },
          {
            'name': '数据点大小',
            'key': 'symbolSize',
            'tag': 'input'
          },
          {
            'name': '是否标点',
            'key': 'showPoint',
            'tag': 'select',
            'options': [
              {
                'name': '是',
                'value': true
              },
              {
                'name': '否',
                'value': false
              }
            ]
          },
          {
            'name': '标点字大小',
            'key': 'PointSize',
            'tag': 'input'
          }
        ]
      },
      {
        'name': '坐标轴样式',
        'tag': 'Hint',
        'childoption': [{
          'name': 'x轴标注倾斜',
          'key': 'rotate',
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
          'key': 'formatterType',
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
        ]
      },
      {
        'name': '颜色配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '配色类型',
            'key': 'colorMatchType',
            'tag': 'select',
            'options': [
              {
                'name': '线条',
                'value': 'line'
              },
              {
                'name': '区域',
                'value': 'area'
              }
            ]
          },
          {
            'name': '配色',
            'key': 'ifEidetColor',
            'tag': 'select',
            'options': [
              {
                'name': '默认',
                'value': false
              },
              {
                'name': '自定义',
                'value': true
              }
            ],
            'parentKey': {
              'colorMatchType': 'line'
            }
          },
          {
            'name': '配色',
            'key': 'ifEidetColor2',
            'tag': 'select',
            'options': [
              {
                'name': '默认',
                'value': false
              },
              {
                'name': '自定义',
                'value': true
              }
            ],
            'parentKey': {
              'colorMatchType': 'area'
            }
          },
          {
            'name': '颜色类型',
            'key': 'ifAreaGradual',
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
            ],
            'parentKey': {
              'colorMatchType': 'area',
              'ifEidetColor2': true
            }
          },
          {
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
            ],
            'parentKey': {
              'colorMatchType': 'line',
              'ifEidetColor': true
            }
          },
          {
            'name': '颜色配置',
            'key': 'ScatterColor',
            'parentKey': {
              'ifGradual': 'false',
              'colorMatchType': 'line',
              'ifEidetColor': true
            },
            'tag': 'ColorArray'
          },
          {
            'name': '颜色配置',
            'key': 'DScatterColor',
            'parentKey': {
              'ifGradual': 'true',
              'colorMatchType': 'line',
              'ifEidetColor': true
            },
            'tag': 'ColorArray'
          },
          {
            'name': '颜色配置',
            'key': 'AreaScatterColor',
            'parentKey': {
              'ifAreaGradual': 'false',
              'colorMatchType': 'area',
              'ifEidetColor2': true
            },
            'tag': 'monochromeArray'
          },
          {
            'name': '颜色配置',
            'key': 'AreaDScatterColor',
            'parentKey': {
              'ifAreaGradual': 'true',
              'colorMatchType': 'area',
              'ifEidetColor2': true
            },
            'tag': 'GradientArray'
          }
        ]
      }

    ]
  }
}
