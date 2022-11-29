
export default {
  'item': {
    text: '切换组合',
    imgClass: 'icon-hotspot',
    chartType: 'SwitchButton',
    width: 200,
    height: 50,
    bdpx: 0,
    bgClr: '',
    clr: '#666f8b',
    bdClr: '#3d445a',
    fontWeight: 'normal',
    fontFamily: '',
    srcList: [], // 上传图片的信息
    rotate: 0,
    showCom: '', // tabs绑定元素
    bindCom: [], // 绑定的组合
    ifCarousel: false,
    speed: 5000,
    showTabs: false,
    normalBack: '',
    normalBackName: '',
    checkedBack: '',
    textSize: 14,
    normalTextColor: '#fff',
    checkedTextColor: '#fff',
    checkedBackName: '',
    normalColor: 'rgba(255,0,0,0.4)',
    focusColor: 'rgba(0,255,0,0.4)',
    focusBorderColor: '#00ff00',
    normalBorderColor: '#ff0000',
    borderRadius: 4,
    marginRight: 3,
    chartData: {
      data: []
    }
  },
  'styles': {
    'base': [
      {
        'name': '其他设置',
        'tag': 'Hint',
        'childoption': [
          {
            'name': '旋转角度',
            'tag': 'input',
            'key': 'rotate'
          },
          {
            'name': '自动轮播',
            'key': 'ifCarousel',
            'tag': 'select',
            'options': [
              {
                'name': '开启',
                'value': true
              },
              {
                'name': '关闭',
                'value': false
              }
            ]
          },
          {
            'name': '轮播速度',
            'key': 'speed',
            'tag': 'select',
            'parentKey': {
              'ifCarousel': true
            },
            'options': [
              {
                'name': '2000ms',
                'value': 2000
              },
              {
                'name': '3000ms',
                'value': 3000
              },
              {
                'name': '4000ms',
                'value': 4000
              },
              {
                'name': '5000ms',
                'value': 5000
              }
            ]
          },
          {
            'name': '是否显示Tabs',
            'key': 'showTabs',
            'tag': 'select',
            'options': [
              {
                'name': '开启',
                'value': true
              },
              {
                'name': '关闭',
                'value': false
              }
            ]
          },
          {
            'name': '正常按钮颜色',
            'key': 'normalColor',
            'tag': 'Color'
          },
          {
            'name': '焦点按钮颜色',
            'key': 'focusColor',
            'tag': 'Color'
          },
          {
            'name': '正常边框颜色',
            'key': 'normalBorderColor',
            'tag': 'Color'
          },
          {
            'name': '焦点边框颜色',
            'key': 'focusBorderColor',
            'tag': 'Color'
          },
          {
            'name': '按钮圆角',
            'key': 'borderRadius',
            'tag': 'input'
          },
          {
            'name': '按钮间隔',
            'key': 'marginRight',
            'tag': 'input'
          }, {
            'name': '正常按钮背景',
            'key': 'normalBack',
            'tag': 'ImgFile',
            'keyName': 'normalBackName'
          }, {
            'name': '高亮按钮背景',
            'key': 'checkedBack',
            'tag': 'ImgFile',
            'keyName': 'checkedBackName'
          },
          {
            'name': '文字大小',
            'key': 'textSize',
            'tag': 'input'
          },
          {
            'name': '正常文字颜色',
            'key': 'normalTextColor',
            'tag': 'Color'
          },
          {
            'name': '焦点文字颜色',
            'key': 'checkedTextColor',
            'tag': 'Color'
          }
        ]
      }
    ]
  }
}
