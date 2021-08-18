export default {
  'item': {
    'text': '弹框轮播',
    'width': 100,
    'height': 100,
    'imgClass': 'icon-sliderdialog',
    'chartType': 'BulletFrame',
    'showType': '2',
    'stationPlace': '1',
    'imgSrc': '/leaderviewWeb/border/back.jpg',
    'imgName': '',
    'videoSrc': '',
    'videoName': '',
    'openBoxheight': 320,
    'openBoxwidth': 560,
    'openBoxRight': 110,
    'openBoxBtn': 0,
    'closeBoxheight': 45,
    'closeBoxwidth': 45,
    'closeBoxRight': 0,
    'closeBoxBtn': 0,
    'videoheight': 200,
    'videowidth': 400,
    'videoBoxRight': 0,
    'videoBoxBtn': 0,
    'autoplay': true,
    'loop': true,
    'srcList': [],
    'isPlay': false,
    'PlayByClick': true,
    'PlayTime': 2,
    'Playheight': 400,
    'PlayBoxwidth': 400,
    'PlayBoxRight': 40,
    'PlayBoxBtn': 40,
    'chartData': {}
  },
  'styles': {
    'base': [
      {
        'name': '背景配置',
        'tag': 'Hint',
        'childoption': [ {
          'name': '背景图片',
          'key': 'imgSrc',
          'keyName': 'imgName',
          'tag': 'ImgFile'
        }, {
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
        }, {
          'name': '背景图片高度',
          'key': 'openBoxheight',
          'tag': 'input'
        }, {
          'name': '背景图片宽度',
          'key': 'openBoxwidth',
          'tag': 'input'
        }, {
          'name': '图片距左距离',
          'key': 'openBoxRight',
          'tag': 'input'
        }, {
          'name': '图片距上距离',
          'key': 'openBoxBtn',
          'tag': 'input'
        } ]
      }, {
        'name': '按钮配置',
        'tag': 'Hint',
        'childoption': [ {
          'name': '关闭按钮高度',
          'key': 'closeBoxheight',
          'tag': 'input'
        }, {
          'name': '关闭按钮宽度',
          'key': 'closeBoxwidth',
          'tag': 'input'
        }, {
          'name': '按钮距右距离',
          'key': 'closeBoxRight',
          'tag': 'input'
        }, {
          'name': '按钮距上距离',
          'key': 'closeBoxBtn',
          'tag': 'input'
        }]
      }, {
        'name': '视频配置',
        'tag': 'Hint',
        'childoption': [ {
          'name': '选择视频',
          'key': 'videoSrc',
          'keyName': 'videoName',
          'tag': 'videoFile'
        }, {
          'name': '视频高度',
          'key': 'videoheight',
          'tag': 'input'
        }, {
          'name': '视频宽度',
          'key': 'videowidth',
          'tag': 'input'
        }, {
          'name': '视频距左距离',
          'key': 'videoBoxRight',
          'tag': 'input'
        }, {
          'name': '视频距上距离',
          'key': 'videoBoxBtn',
          'tag': 'input'
        }]
      }, {
        'name': '轮播图片配置',
        'tag': 'Hint',
        'childoption': [ {
          'name': '轮播图片',
          'key': 'isPlay',
          'tag': 'select',
          'options': [
            {
              'name': '隐藏',
              'value': false
            },
            {
              'name': '显示',
              'value': true
            }
          ]
        }, {
          'name': '图片轮播方式',
          'key': 'PlayByClick',
          'parentKey': {
            'isPlay': true
          },
          'tag': 'select',
          'options': [
            {
              'name': '点击轮播',
              'value': false
            },
            {
              'name': '自动轮播',
              'value': true
            }
          ]
        }, {
          'name': '轮播时间(s)',
          'key': 'PlayTime',
          'parentKey': {
            'isPlay': true,
            'PlayByClick': true
          },
          'tag': 'input'
        }, {
          'name': '轮播图片高度',
          'parentKey': {
            'isPlay': true
          },
          'key': 'Playheight',
          'tag': 'input'
        }, {
          'name': '轮播图片宽度',
          'parentKey': {
            'isPlay': true
          },
          'key': 'PlayBoxwidth',
          'tag': 'input'
        }, {
          'name': '图片距右距离',
          'parentKey': {
            'isPlay': true
          },
          'key': 'PlayBoxRight',
          'tag': 'input'
        }, {
          'name': '图片距下距离',
          'parentKey': {
            'isPlay': true
          },
          'key': 'PlayBoxBtn',
          'tag': 'input'
        }]
      }
    ]
  }
}
