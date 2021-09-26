export default {
  'item': {
    'text': '在线地图',
    'imgClass': 'icon-dituonline',
    'chartType': 'BaiDuMap',
    'width': 600,
    'height': 300,
    'showPOI': 'on',
    'pointArray': [], // 点标记数组
    'textArray': [], // 文本标注数组
    'mapType': '2D',
    'markerType': 'http://api.map.baidu.com/images/marker_red_sprite.png', // 点标记图标类型
    'selectMark': {
      'lng': 0,
      'lat': 0
    }, // 查询添加的点
    'selectChange': true, // 用于表示点击查询操作的
    'landColor': '#091220',
    'boundaryColor': '#cbb77a',
    'oceanColor': '#113549',
    'mapTextColor': '#2dc4bb',
    'roadColor': '#12223d',
    'styleJson': [{
      'featureType': 'boundary', // 设置边界
      'elementType': 'geometry',
      'stylers': {
        'color': '#cbb77a'
      }
    }, {
      'featureType': 'boundary',
      'elementType': 'geometry.fill', // 设置边界
      'stylers': {
        'color': '#cbb77a'
      }
    }, {
      'featureType': 'poilabel', // 设置poi
      'elementType': 'labels',
      'stylers': {
        'visibility': 'on'
      }
    }, {
      'featureType': 'poilabel', // 设置poi
      'elementType': 'labels.icon',
      'stylers': {
        'visibility': 'on'
      }
    }, {
      'featureType': 'land', // 设置陆地
      'elementType': 'all',
      'stylers': {
        'color': '#091220'
      }
    }, {
      'featureType': 'water',
      'elementType': 'all',
      'stylers': {
        'color': '#113549'
      }
    }, {
      'featureType': 'label',
      'elementType': 'labels.text.fill',
      'stylers': {
        'color': '#2dc4bb'
      }
    }, {
      'featureType': 'label',
      'elementType': 'labels.text.stroke',
      'stylers': {
        'color': '#2dc4bb00'
      }
    }, {
      'featureType': 'road',
      'elementType': 'geometry',
      'stylers': {
        'color': '#12223dff',
        'visibility': 'on'
      }
    }, {
      'featureType': 'road',
      'elementType': 'labels.text.fill',
      'stylers': {
        'color': '#ffffff',
        'visibility': 'on'
      }
    }, {
      'featureType': 'road',
      'elementType': 'labels.text.stroke',
      'stylers': {
        'color': '#ffffff00',
        'visibility': 'on'
      }
    }, {
      'featureType': 'manmade',
      'elementType': 'all',
      'stylers': {
        'color': '#164066ff',
        'visibility': 'on'
      }
    }, {
      'featureType': 'building',
      'elementType': 'geometry',
      'stylers': {
        'color': '#164066ff',
        'visibility': 'on'
      }
    }, {
      'featureType': 'green',
      'elementType': 'geometry',
      'stylers': {
        'color': '#164066ff',
        'visibility': 'on'
      }
    }]
  },
  'styles': {
    'base': [
      {
        'name': '地图配置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '地图区域颜色',
            'tag': 'Color',
            'key': 'landColor'
          },
          {
            'name': '分界线颜色',
            'tag': 'Color',
            'key': 'boundaryColor'
          },
          {
            'name': '海洋颜色',
            'tag': 'Color',
            'key': 'oceanColor'
          },
          {
            'name': '地图文字颜色',
            'tag': 'Color',
            'key': 'mapTextColor'
          },
          {
            'name': '道路颜色',
            'tag': 'Color',
            'key': 'roadColor'
          },
          {
            'name': '地图POI',
            'tag': 'select',
            'key': 'showPOI',
            'options': [
              {
                'name': '显示',
                'value': 'on'
              },
              {
                'name': '隐藏',
                'value': 'off'
              }
            ]
          },
          {
            'name': '点标记类型',
            'tag': 'select',
            'key': 'markerType',
            'options': [
              {
                'name': '默认',
                'value': 'http://api.map.baidu.com/images/marker_red_sprite.png'
              },
              {
                'name': '类型一',
                'value': '../../../../static/img/类型1.png'
              },
              {
                'name': '类型二',
                'value': '../../../../static/img/类型2.png'
              },
              {
                'name': '类型三',
                'value': '../../../../static/img/类型3.png'
              },
              {
                'name': '类型四',
                'value': '../../../../static/img/类型4.png'
              }
            ]
          },
          {
            'name': '点标记定位',
            'tag': 'MarkLocation'
          }
        ]
      }
    ]
  }
}
