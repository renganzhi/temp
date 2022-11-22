
export default {
  'item': {
    text: '收缩框',
    imgClass: 'icon-hotspot',
    chartType: 'ShrinkBox',
    width: 200,
    height: 50,
    bdpx: 0,
    bgClr: '',
    clr: '#666f8b',
    bdClr: '#3d445a',
    fontWeight: 'normal',
    fontFamily: '',
    chartData: {},
    rotate: 0,
    showCom: '', // tabs绑定元素
    bindCom: [], // 绑定的组合
    ifCarousel: false,
    speed: 5000,
    showTabs: false,
    normalBack: '',
    normalBackName: '',
    checkedBack: '',
    checkedBackName: '',
    normalColor: 'rgba(255,0,0,0.4)',
    focusColor: 'rgba(0,255,0,0.4)',
    focusBorderColor: '#00ff00',
    normalBorderColor: '#ff0000',
    borderRadius: 4,
    marginRight: 3
  },
  'styles': {
    'base': [
      {
        'name': '其他设置',
        'tag': 'Hint',
        'childoption': [
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
          }, {
            'name': '正常按钮背景',
            'key': 'normalBack',
            'tag': 'ImgFile',
            'keyName': 'normalBackName'
          }
        ]
      }
    ]
  }
}
