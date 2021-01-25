export default {
  item: {
    text: '水波图',
    width: 300,
    height: 300,
    imgClass: 'icon-n-waveball',
    subType: 'progress',
    secondType: 'liquidfill',
    chartType: 'liquidfill',
    bdpx: 4,
    isLinear: false,
    directionLinear: '0,0,0,1',
    bgClr: '#156acf',
    bgClrRange: ['rgba(21,106,207, 1)', 'rgba(255, 255, 255, 0)'],
    clr: 'rgba(221, 221, 221, 0.9)',
    bdClr: '#666f8b',
    fontSize: 28,
    ctLegendShow: true,
    legendColor: 'rgba(255, 255, 255, 1)',
    chartData: {
      name: '繁忙度',
      unit: '%',
      value: 60
    }
  },
  styles: {
    lengend: [
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
      },
      {
        'name': '图例文字颜色',
        'key': 'legendColor',
        'tag': 'singleColor'
      }
    ],
    base: [
      {
        'name': '填充色方式',
        'key': 'isLinear',
        'tag': 'select',
        'options': [
          {
            'name': '渐变',
            'value': true
          },
          {
            'name': '单色',
            'value': false
          }
        ]
      },
      {
        'name': '填充色',
        'key': 'bgClr',
        'tag': 'singleColor',
        dep: {
          targetKey: 'isLinear',
          targetVal: false
        }
      },
      {
        'name': '渐变方向',
        'key': 'directionLinear',
        'tag': 'select',
        'options': [
          {
            'name': '上下',
            'value': [0, 0, 0, 1]
          },
          {
            'name': '左右',
            'value': [0, 0, 1, 0]
          }
        ],
        dep: {
          targetKey: 'isLinear',
          targetVal: true
        }
      },
      {
        'name': '渐变色',
        'key': 'bgClrRange',
        'tag': 'rangeColor',
        dep: {
          targetKey: 'isLinear',
          targetVal: true
        }
      },
      {
        'name': '边框色',
        'key': 'bdClr',
        'tag': 'singleColor'
      },
      {
        'name': '线宽',
        'key': 'bdpx',
        'tag': 'select',
        'options': Array.from({ length: 11 }, (v, i) => ({
          'name': i,
          'value': i
        }))
      },
      {
        'name': '字体颜色',
        'key': 'clr',
        'tag': 'singleColor'
      },
      {
        'name': '字号',
        'key': 'fontSize',
        'tag': 'select',
        'options': [12, 13, 14, 16, 18, 20, 24, 26, 28, 30, 36, 40, 48, 54, 60, 72, 84, 88].map(d => ({
          'name': d,
          'value': d
        }))
      }
    ],
    axis: []
  }
}
