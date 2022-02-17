export default {
  'item': {
    'text': '环形图',
    'imgClass': 'icon-n-ring',
    'chartType': 'NewPie',
    'ifGradual': 'false',
    'ctLegendSize': '16',
    'ctLegendColor': '#666f8b',
    'axisLabelSize': '16',
    'ifEidetColor': false,
    'legendY': 85,
    'radius': 50,
    'showline': true,
    'showword': true,
    'isRing': true,
    'detailwidth': 12,
    'borderRadius': 0,
    'ringWidth': 50,
    'tooltipShow': true,
    'tooltipBackColor': '#57625d',
    'tooltipTextColor': '#fff',
    'tooltipfontSize': 14,
    'LineColorArray': [
      '#2d98f1',
      '#32c5e9',
      '#67e0e3',
      '#9fe6b8',
      '#ffdb5c',
      '#ffb092'],
    'DLineColorArray': [
      ['rgba(213, 153, 17, 0.52)', '#be4d24'],
      ['rgba(2, 210, 255, 0.49)', '#1bbcae'],
      ['#fa8d76', '#db4222'],
      ['#af8af3', '#874edc'],
      ['#f5739c', '#f31d53'],
      ['#ffdf91', '#eeb01b'],
      ['#5c84e7', '#144fe5'],
      ['#85f8c0', '#62dc26']
    ],
    'chartData': {
      'columns': [
        '告警级别',
        '数量'
      ],
      'unit': '次',
      'rows': [
        {
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
  },
  'styles': {
    'base': []
  }
}
