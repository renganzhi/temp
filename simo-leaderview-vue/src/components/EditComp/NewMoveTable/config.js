export default {
  'item': {
    'text': '轮播表格',
    'imgClass': 'icon-slidertable',
    'chartType': 'NewMoveTable',
    'thirdType': 'moveTable',
    'direction': 'top',
    'speed': 2,
    'pageNum': 5,
    'heightLinght': 36,
    'hdBgClr': '#1c2132',
    'hdClr': '#cad6dd',
    'hdfontSize': 12,
    'bgClr': 'rgba(34, 40, 58, 0.5)',
    'clr': '#919cc1',
    'bdClr': '#c2c6d7',
    'intervieData': 10,
    'moreUrlArry': [],
    'Internal': 'false',
    'Zebra': 'false',
    'Alignment': 'left',
    'ZebraColor': '#343c58',
    'tableBack': '',
    'tableBackName': '',
    'AlarmField': '',
    'AlarmType': 'num',
    'OneLineType': 'default',
    'OneLineSize': 86,
    'LineSizeArry': [],
    'AlarmChart': '',
    'AlarmNumType': 'greater',
    'AlarmNum': '',
    'AlarmColor': 'red',
    'bdpx': 0,
    'chartData': {
      'columns': ['IP', '日期', 'CPU利用率', '内存利用率'],
      'rows': [{
        'IP': '192.168.1.1',
        '日期': '1/1',
        'CPU利用率': 1193,
        '内存利用率': 1013
      },
      {
        'IP': '192.168.1.2',
        '日期': '1/2',
        'CPU利用率': 1293,
        '内存利用率': 1023
      },
      {
        'IP': '192.168.1.3',
        '日期': '1/3',
        'CPU利用率': 1393,
        '内存利用率': 1033
      },
      {
        'IP': '192.168.1.4',
        '日期': '1/4',
        'CPU利用率': 1493,
        '内存利用率': 1043
      },
      {
        'IP': '192.168.1.5',
        '日期': '1/5',
        'CPU利用率': 1593,
        '内存利用率': 1053
      },
      {
        'IP': '192.168.1.6',
        '日期': '1/6',
        'CPU利用率': 1693,
        '内存利用率': 1063
      },
      {
        'IP': '192.168.1.7',
        '日期': '1/7',
        'CPU利用率': 1793,
        '内存利用率': 1073
      },
      {
        'IP': '192.168.1.8',
        '日期': '1/8',
        'CPU利用率': 1893,
        '内存利用率': 1083
      },
      {
        'IP': '192.168.1.9',
        '日期': '1/9',
        'CPU利用率': 1993,
        '内存利用率': 1093
      },
      {
        'IP': '192.168.1.10',
        '日期': '1/10',
        'CPU利用率': 1093,
        '内存利用率': 1003
      }
      ]
    }
  },
  'styles': {
    'base': [
      {
        'name': '图表样式',
        'tag': 'Hint',
        'childoption': [{
          'name': '添加内发光',
          'key': 'Internal',
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
        }, {
          'name': '添加斑马纹',
          'key': 'Zebra',
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
        }, {
          'name': '斑马纹颜色',
          'key': 'ZebraColor',
          'parentKey': {
            'Zebra': 'true'
          },
          'tag': 'Color'
        }, {
          'name': '对齐方式',
          'key': 'Alignment',
          'tag': 'select',
          'options': [
            {
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
        }, {
          'name': '表格背景图',
          'key': 'tableBack',
          'tag': 'ImgFile',
          'keyName': 'tableBackName'
        }, {
          'name': '表头背景色',
          'key': 'hdBgClr',
          'tag': 'Color'
        }]
      },
      {
        'name': '文字样式',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '表头文字颜色',
            'key': 'hdClr',
            'tag': 'Color'
          },
          {
            'name': '表头字号',
            'key': 'hdfontSize',
            'tag': 'select',
            'options': [
              {
                'name': '12',
                'value': 12
              },
              {
                'name': '13',
                'value': 13
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
                'name': '26',
                'value': 26
              },
              {
                'name': '28',
                'value': 28
              },
              {
                'name': '30',
                'value': 30
              },
              {
                'name': '36',
                'value': 36
              },
              {
                'name': '40',
                'value': 40
              },
              {
                'name': '48',
                'value': 48
              },
              {
                'name': '54',
                'value': 54
              },
              {
                'name': '60',
                'value': 60
              },
              {
                'name': '72',
                'value': 72
              },
              {
                'name': '84',
                'value': 84
              },
              {
                'name': '88',
                'value': 88
              }
            ]
          },
          {
            'name': '行高',
            'key': 'heightLinght',
            'tag': 'input'
          },
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
            'name': '字体颜色',
            'key': 'clr',
            'tag': 'Color'
          },
          {
            'name': '字号',
            'key': 'fontSize',
            'tag': 'select',
            'options': [
              {
                'name': '12',
                'value': 12
              },
              {
                'name': '13',
                'value': 13
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
                'name': '26',
                'value': 26
              },
              {
                'name': '28',
                'value': 28
              },
              {
                'name': '30',
                'value': 30
              },
              {
                'name': '36',
                'value': 36
              },
              {
                'name': '40',
                'value': 40
              },
              {
                'name': '48',
                'value': 48
              },
              {
                'name': '54',
                'value': 54
              },
              {
                'name': '60',
                'value': 60
              },
              {
                'name': '72',
                'value': 72
              },
              {
                'name': '84',
                'value': 84
              },
              {
                'name': '88',
                'value': 88
              }
            ]
          },
          {
            'name': '轮播方向',
            'key': 'direction',
            'tag': 'select',
            'options': [
              {
                'name': '横向',
                'value': 'left'
              },
              {
                'name': '纵向',
                'value': 'top'
              }
            ]
          },
          {
            'name': '轮播速度',
            'key': 'speed',
            'tag': 'select',
            'options': [
              {
                'name': '高速',
                'value': 1
              },
              {
                'name': '中速',
                'value': 2
              },
              {
                'name': '低速',
                'value': 3
              }

            ]
          }
        ]
      }
    ]
  }
}
