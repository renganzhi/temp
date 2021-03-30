export default {
  'item': {
    'text': '3D路径图',
    'imgClass': 'icon-3dlines',
    'chartType': 'TDEarthLine',
    'width': 300,
    'height': 300,
    'scanningspot': 'true',
    'scanningspeed': 40,
    'scanningradiu': 2,
    'scanninglength': 0.2,
    'scanningopcity': 1,
    'scanningcolor': '#6fa8dc',
    'backPicName': 'Mapcolor2-2',
    'linewidth': 2,
    'lineColor': '#22e9e0',
    'lineoption': 0.8,
    'needrotate': 'true',
    'rotatedirection': 'ccw',
    'rotatespeed': 5,
    'norotatetime': 4,
    'alpha': 25,
    'beta': 25,
    'symbolSize': 5,
    'symbolcolor': 'rgb(50, 250, 150)',
    'symbolopacity': 0.5,
    'displacementScale': 0.03,
    'roughness': '0',
    'metalness': '0',
    'shadingtype': 'realistic',
    'ambientcolor': '#ffffff',
    'ambientintensity': 0.5,
    'maincolor': '#ffffff',
    'mainintensity': 0.5,
    'mainbeta': 20,
    'mainalpha': 10,
    'chartData': {
      'lineArry': [
        // station 为飞线的起点和重点经纬度  linecolor为线条颜色，scanningcolor为飞线点颜色，不填则使用配置页面配置的颜色
        {'station': [[[145.391881, -6.081689], [-125.7725, 49.082222]]], 'linecolor': '#ff0000', 'scanningcolor': '#00ff00'},
        {'station': [[[125.391881, -16.081689], [-125.7725, 54.082222]]]}
      ]
    }
  },
  'styles': {
    'base': [
      {
        'name': '地球配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '地球类型',
          'key': 'backPicName',
          'tag': 'select',
          'options': [
            {
              'name': '地球影像',
              'value': 'Mapcolor1-2'
            },
            {
              'name': '地球影像(网格)',
              'value': 'Mapcolor1-1'
            },
            {
              'name': '地球剪影-浅蓝蓝色',
              'value': 'Mapcolor2-2'
            },
            {
              'name': '地球剪影-浅蓝蓝色(网格)',
              'value': 'Mapcolor2-1'
            },
            {
              'name': '地球-深绿',
              'value': 'Mapcolor3-2'
            },
            {
              'name': '地球-浅绿深绿(网格)',
              'value': 'Mapcolor3-1'
            },
            {
              'name': '地球影像-黄绿色(纬度环)',
              'value': 'Mapcolor4'
            },
            {
              'name': '地球剪影-浅灰',
              'value': 'Mapcolor5-2'
            },
            {
              'name': '地球剪影-浅灰(网格)',
              'value': 'Mapcolor5-1'
            },
            {
              'name': '地球剪影-深绿青色',
              'value': 'Mapcolor6-2'
            },
            {
              'name': '地球剪影-深绿青色(网格)',
              'value': 'Mapcolor6-1'
            }
          ]
        }, {
          'name': '地球凹凸感',
          'key': 'displacementScale',
          'tag': 'input'
        }, {
          'name': '地球粗糙度',
          'key': 'roughness',
          'tag': 'select',
          'options': [{
            'name': '完全光滑',
            'value': 0
          }, {
            'name': '光滑',
            'value': 0.2
          }, {
            'name': '中等',
            'value': 0.5
          }, {
            'name': '粗糙',
            'value': 0.8
          }, {
            'name': '完全粗糙',
            'value': 1
          }
          ]
        }, {
          'name': '地球材质',
          'key': 'metalness',
          'tag': 'select',
          'options': [{
            'name': '非金属',
            'value': 0
          }, {
            'name': '金属',
            'value': 1
          }
          ]
        }
        ]
      },
      {
        'name': '线条配置',
        'tag': 'Hint',
        'childoption': [ {
          'name': '线条宽度',
          'key': 'linewidth',
          'tag': 'select',
          'options': [{'name': '1', 'value': 1},
            {'name': '2', 'value': 2},
            {'name': '3', 'value': 3},
            {'name': '4', 'value': 4},
            {'name': '5', 'value': 5},
            {'name': '6', 'value': 6},
            {'name': '7', 'value': 7},
            {'name': '8', 'value': 8},
            {'name': '9', 'value': 9},
            {'name': '10', 'value': 10}
          ]
        }, {
          'name': '线条颜色',
          'key': 'lineColor',
          'tag': 'Color'
        }, {
          'name': '线条透明度',
          'key': 'lineoption',
          'tag': 'select',
          'options': [{'name': '0.1', 'value': 0.1},
            {'name': '0.2', 'value': 0.2},
            {'name': '0.3', 'value': 0.3},
            {'name': '0.4', 'value': 0.4},
            {'name': '0.5', 'value': 0.5},
            {'name': '0.6', 'value': 0.6},
            {'name': '0.7', 'value': 0.7},
            {'name': '0.8', 'value': 0.8},
            {'name': '0.9', 'value': 0.9},
            {'name': '1', 'value': 1}
          ]
        }
        ]
      },
      {
        'name': '落地点配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '落地点大小',
          'key': 'symbolSize',
          'tag': 'input'
        }, {
          'name': '落地点颜色',
          'key': 'symbolcolor',
          'tag': 'Color'
        }, {
          'name': '落地点透明度',
          'key': 'symbolopacity',
          'tag': 'input'
        }
        ]
      },
      {
        'name': '扫描点配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '显示扫描点',
          'key': 'scanningspot',
          'tag': 'select',
          'options': [{'name': '显示', 'value': 'true'},
            {'name': '显示', 'value': 'false'} ]
        }, {
          'name': '扫描点速度',
          'key': 'scanningspeed',
          'parentKey': {
            'scanningspot': 'true'
          },
          'tag': 'input'
        }, {
          'name': '扫描点半径',
          'key': 'scanningradiu',
          'parentKey': {
            'scanningspot': 'true'
          },
          'tag': 'input'
        }, {
          'name': '扫描点长度',
          'key': 'scanninglength',
          'parentKey': {
            'scanningspot': 'true'
          },
          'tag': 'select',
          'options': [{'name': '0.1', 'value': 0.1},
            {'name': '0.2', 'value': 0.2},
            {'name': '0.3', 'value': 0.3},
            {'name': '0.4', 'value': 0.4}
          ]
        }, {
          'name': '扫描点透明度',
          'key': 'symbolopacity',
          'parentKey': {
            'scanningspot': 'true'
          },
          'tag': 'select',
          'options': [{'name': '0.4', 'value': 0.4},
            {'name': '0.6', 'value': 0.6},
            {'name': '0.8', 'value': 0.8},
            {'name': '1', 'value': 1}
          ]
        }, {
          'name': '扫描点颜色',
          'key': 'scanningcolor',
          'parentKey': {
            'scanningspot': 'true'
          },
          'tag': 'Color'
        }
        ]
      },
      {
        'name': '旋转配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '是否旋转',
          'key': 'needrotate',
          'tag': 'select',
          'options': [{'name': '旋转', 'value': 'true'},
            {'name': '不旋转', 'value': 'false'} ]
        }, {
          'name': '旋转方向',
          'key': 'rotatedirection',
          'tag': 'select',
          'parentKey': {
            'needrotate': 'true'
          },
          'options': [
            {'name': '由左往右', 'value': 'ccw'},
            {'name': '由右往左', 'value': 'cw'} ]
        }, {
          'name': '旋转速度',
          'key': 'rotatespeed',
          'parentKey': {
            'needrotate': 'true'
          },
          'tag': 'input'
        }, {
          'name': '操作后静止时长',
          'parentKey': {
            'needrotate': 'true'
          },
          'key': 'rotatespeed',
          'tag': 'input'
        }, {
          'name': '地图俯仰角',
          'parentKey': {
            'needrotate': 'true'
          },
          'key': 'alpha',
          'tag': 'input'
        }, {
          'name': '地图旋转角',
          'parentKey': {
            'needrotate': 'true'
          },
          'key': 'beta',
          'tag': 'input'
        }
        ]
      },
      {
        'name': '光照配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '光源类型',
          'key': 'shadingtype',
          'tag': 'select',
          'options': [{'name': '平行光', 'value': 'color'},
            {'name': '自然光', 'value': 'realistic'} ]
        }, {
          'name': '环境光颜色',
          'key': 'ambientcolor',
          'tag': 'Color'
        }, {
          'name': '环境光亮度',
          'key': 'ambientintensity',
          'tag': 'input'
        }, {
          'name': '主光源颜色',
          'key': 'maincolor',
          'tag': 'Color'
        }, {
          'name': '主光源亮度',
          'key': 'mainintensity',
          'tag': 'input'
        }, {
          'name': '主光源y方向角度',
          'key': 'mainbeta',
          'tag': 'input'
        }, {
          'name': '主光源x方向角度',
          'key': 'mainalpha',
          'tag': 'input'
        }
        ]
      }
    ]
  }
}
