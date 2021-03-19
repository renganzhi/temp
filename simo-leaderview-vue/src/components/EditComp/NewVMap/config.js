export default{
  'item': {
    'text': '区域分布图',
    'imgClass': 'icon-n-areaMap',
    'chartType': 'NewVMap',
    'width': 300,
    'height': 300,
    'mapLevel': 'country',
    'countryCode': 100000,
    'provinceCode': '',
    'cityCode': '',
    'visualPosition': 'left',
    'fontSize': 10,
    'cityShow': false,
    'ifGradual': 'false',
    'ctLegendShow': 'true',
    'ctLegendColor': '#fff',
    'areaColor': '#121a33',
    'ColorArry': ['#bb2a52', '#bd3d50', '#bf4e4e', '#c2634b'],
    'borderColor': '#38597b',
    'cityColor': '#828bac',
    'themeType': '1', // 1深色 2浅色
    'piecesData': [
      { 'min': 0, 'max': 50 },
      { 'min': 51, 'max': 100 },
      { 'min': 101 }
    ],
    'chartData': {
      'columns': ['位置', '资源', '告警'],
      'rows': [
        { '位置': '台湾', '告警': 25 },
        { '位置': '河北', '告警': 75 },
        { '位置': '山西', '告警': 125 }
      ]}
  },
  'styles': {
    'base': [
      {
        'name': '图例配置',
        'tag': 'Hint',
        'childoption': [{
          'name': '图例可见性',
          'key': 'ctLegendShow',
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
          'name': '图例位置',
          'key': 'visualPosition',
          'tag': 'select',
          'parentKey': {
            'ctLegendShow': 'true'
          },
          'options': [
            {
              'name': '底部靠左',
              'value': 'left'
            },
            {
              'name': '底部靠右',
              'value': 'right'
            }
          ]
        }]
      },
      {
        'name': '图表样式',
        'tag': 'Hint',
        'childoption': [{
          'name': '地名可见性',
          'key': 'cityShow',
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
          'name': '地名颜色',
          'key': 'cityColor',
          'parentKey': {
            'cityShow': 'true'
          },
          'tag': 'Color'
        }, {
          'name': '字号',
          'key': 'fontSize',
          'parentKey': {
            'cityShow': 'true'
          },
          'tag': 'select',
          'options': [
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
            },
            {
              'name': '12',
              'value': 12
            },
            {
              'name': '14',
              'value': 14
            }
          ]
        }, {
          'name': '地图颜色',
          'key': 'areaColor',
          'tag': 'Color'
        }, {
          'name': '分界线颜色',
          'key': 'borderColor',
          'tag': 'Color'
        }, {
          'name': '配色',
          'key': 'ColorArry',
          'tag': 'ColorArray'
        }]
      }
    ]
  }
}
