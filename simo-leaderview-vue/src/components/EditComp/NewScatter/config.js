export default{
  'item': {
    'text': '地图实时图',
    'imgClass': 'icon-n-scatterMap',
    'chartType': 'NewScatter', // 散点图
    'width': 300,
    'height': 300,
    'mapLevel': 'country',
    'countryCode': 100000,
    'ctLegendShow': 'false', // 地名是否展示
    'provinceCode': '',
    'cityCode': '',
    'themeType': '1',
    'ctLegendColor': '#ffffff',
    'areaColor': '#121a33',
    'borderColor': '#38597b',
    'visualPosition': 'left',
    'scatterPoint': [{ name: '北京', value: [116.405285, 39.904989, 2] }], // 带有地理位置的散点数据
    'chartData': [{ name: '北京', value: 2 }]
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
          'name': '图例字颜色',
          'key': 'ctLegendColor',
          'tag': 'Color',
          'parentKey': {
            'ctLegendShow': 'true'
          }
        }]
      },
      {
        'name': '图表样式',
        'tag': 'Hint',
        'childoption': [{
          'name': '地图颜色',
          'key': 'areaColor',
          'tag': 'Color'
        }, {
          'name': '分界线颜色',
          'key': 'borderColor',
          'tag': 'Color'
        }]
      }
    ]
  }
}
