var env = process.env.NODE_ENV
var settings = {
  // 这里配置一些全局变量
  gbs: {
    host: env === 'development' ? '/api' : window.location.protocol + '//' + window.location.host,
    inDev: env === 'development'
    // host: 'http://' + window.location.host // 打包的前缀
    // host: 'http://localhost:9999/leaderview'
  },
  baseData: {
    allowOverflow: 20, // 允许超出画布的范围
    home: {
      // 主页大屏的默认配置
      w: 1920,
      h: 1080
    },
    // 指标器的可选字体
    fontFaces: [
      // {
      //   fontFace: 'SourceHanSansCN-Regular',
      //   fontName: '0123456789'
      // },
      {
        fontFace: 'number1',
        fontName: '0123456789'
      },
      {
        fontFace: 'number2',
        fontName: '0123456789'
      },
      {
        fontFace: 'number3',
        fontName: '0123456789'
      },
      {
        fontFace: 'number4',
        fontName: '0123456789'
      }
    ],
    cardCase: [
      {
        mini: '/leaderview/border/cardMini1.png',
        imgSrc: '/leaderview/border/cardBg1.png'
      },
      {
        mini: '/leaderview/border/cardMini2.png',
        imgSrc: '/leaderview/border/cardBg2.png'
      },
      {
        mini: '/leaderview/border/cardMini3.png',
        imgSrc: '/leaderview/border/cardBg3.png'
      }
    ],
    titleCase: [
      {
        mini: '/leaderview/border/titleMini1.png',
        imgSrc: '/leaderview/border/titleBg1.png'
      },
      {
        mini: '/leaderview/border/titleMini2.png',
        imgSrc: '/leaderview/border/titleBg2.png'
      }
    ]
  }
}
module.exports = settings
