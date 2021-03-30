export default {
  'item': {
    'text': '3D地球柱图',
    'imgClass': 'icon-3dhistogrammap',
    'chartType': 'TDEarthBar',
    'width': 600,
    'height': 600,
    'scanningspot': 'true',
    'scanningspeed': 40,
    'scanningradiu': 2,
    'scanninglength': 0.2,
    'scanningopcity': 1,
    'scanningcolor': '#6fa8dc',
    'backPicName': 'Mapcolor2-2',
    'linewidth': 1,
    'lineColor': '#1c4587',
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
    'barSize': 0.6,
    'itemStyleColor': '#d2ca47',
    'labelColor': '#b4b851',
    'labelSize': 10,
    'labelWeight': 100,
    'labelBorderwidth': 1,
    'labelBorderColor': '#9c8057',
    'labelBorderRadius': 20,
    'labellineHeight': 14,
    'labelbackColor': '#2b3c57',
    'mainbeta': 20,
    'mainalpha': 10,
    'chartData': {
      'barArry': [
        {
          'value': [34, 50, 1],
          'itemStyle': {
            'color': 'green',
            'opacity': '1'
          },
          'label': {
            'show': true
          },
          'labelvalue': {
            '单位': '北京XX有限公司',
            '类型': '主机',
            '数量': 40
          }
        },
        {
          'value': [34, 40, 2],
          'itemStyle': {
            'color': 'red',
            'opacity': '1'
          }
        },
        {
          'value': [116.397128, 39.916527, 1]
        }
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
        'name': '柱体配置',
        'tag': 'Hint',
        'childoption': [ {
          'name': '柱体大小',
          'key': 'barSize',
          'tag': 'input'
        }, {
          'name': '柱体颜色',
          'key': 'itemStyleColor',
          'tag': 'Color'
        }
        ]
      },
      {
        'name': '标签配置',
        'tag': 'Hint',
        'childoption': [ {
          'name': '字体颜色',
          'key': 'labelColor',
          'tag': 'Color'
        }, {
          'name': '字体大小',
          'key': 'labelSize',
          'tag': 'input'
        }, {
          'name': '字体宽度',
          'key': 'labelWeight',
          'tag': 'input'
        }, {
          'name': '边框宽度',
          'key': 'labelBorderwidth',
          'tag': 'input'
        }, {
          'name': '边框颜色',
          'key': 'labelBorderColor',
          'tag': 'Color'
        }, {
          'name': '圆角大小',
          'key': 'labelBorderRadius',
          'tag': 'input'
        }, {
          'name': '行高',
          'key': 'labellineHeight',
          'tag': 'input'
        }, {
          'name': '背景颜色',
          'key': 'labelbackColor',
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
