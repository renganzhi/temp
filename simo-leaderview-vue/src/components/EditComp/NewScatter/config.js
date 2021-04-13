export default{
  'item': {
    'text': '地图实时图',
    'imgClass': 'icon-n-scatterMap',
    'chartType': 'NewScatter', // 散点图
    'width': 600,
    'height': 600,
    'mapLevel': 'country',
    'countryCode': 100000,
    'colordirection': 1,
    'roam': true,
    'normalcolor': ['#38f9ff', '#3c1095'],
    'ctLegendShow': true, // 地名是否展示
    'provinceCode': '',
    'cityCode': '',
    'themeType': '1',
    'ctLegendColor': '#d4ff7a',
    'areaColor': '#121a33',
    'borderColor': '#45fcf3',
    'visualPosition': 'left',
    'scatterPoint': [{ name: '北京', value: [116.405285, 39.904989, 4] }], // 带有地理位置的散点数据
    'chartData': [{ name: '北京', value: 4 }]
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
              'value': true
            },
            {
              'name': '隐藏',
              'value': false
            }
          ]
        },
        {
          'name': '图例字颜色',
          'key': 'ctLegendColor',
          'tag': 'Color',
          'parentKey': {
            'ctLegendShow': true
          }
        },
        {
          'name': '允许缩放拖拽',
          'key': 'roam',
          'tag': 'select',
          'options': [
            {
              'name': '允许',
              'value': true
            },
            {
              'name': '不允许',
              'value': false
            }
          ]
        }]
      }
      // {
      //   'name': '图表样式',
      //   'tag': 'Hint',
      //   'childoption': [
      //     {
      //       'name': '地图颜色',
      //       'key': 'normalcolor',
      //       'tag': 'GradientColor'
      //     },
      //     {
      //       'name': '颜色渐变角度',
      //       'key': 'colordirection',
      //       'tag': 'select',
      //       'options': [
      //         {
      //           'name': '0°',
      //           'value': 0
      //         },
      //         {
      //           'name': '45°',
      //           'value': 1
      //         },
      //         {
      //           'name': '90°',
      //           'value': 2
      //         },
      //         {
      //           'name': '135°',
      //           'value': 3
      //         }
      //       ]
      //     }, {
      //       'name': '分界线颜色',
      //       'key': 'borderColor',
      //       'tag': 'Color'
      //     }]
      // }
    ]
  }
}
