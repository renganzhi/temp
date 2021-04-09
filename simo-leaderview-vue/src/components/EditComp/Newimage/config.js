export default{
  'item': {
    'text': '图片',
    'imgClass': 'icon-n-exportPicture',
    'chartType': 'Newimage',
    'width': 300,
    'height': 300,
    'imgSrc': '',
    'imgName': '',
    'showType': '1',
    'linkId': '',
    'chartData': {}
  },
  'styles': {
    'base': [
      {
        'name': '图表样式',
        'tag': 'Hint',
        'childoption': [{
          'name': '缩放方式',
          'key': 'showType',
          'tag': 'select',
          'options': [
            {
              'name': '按比例缩放',
              'value': '1'
            },
            {
              'name': '自由缩放',
              'value': '2'
            }
          ]
        }]
      }]
  }
}
