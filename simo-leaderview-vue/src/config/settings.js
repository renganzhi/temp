var env = process.env.NODE_ENV


export const gbs = {
  host: env === 'development' ? '/api' : window.location.protocol + '//' + window.location.host,
  inDev: env === 'development'
  // host: 'http://' + window.location.host // 打包的前缀
  // host: 'http://localhost:9999/leaderview'
};

let titleCase = [
  {
    mini: '/leaderview/border/titleMini1.png',
    imgSrc: '/leaderview/border/titleBg1.png'
  },
  {
    mini: '/leaderview/border/titleMini2.png',
    imgSrc: '/leaderview/border/titleBg2.png'
  },
  {
    mini: '/leaderview/border/titleBg3.png',
    imgSrc: '/leaderview/border/titleBg3.png'
  },
  {
    mini: '/leaderview/border/titleBg4.png',
    imgSrc: '/leaderview/border/titleBg4.png'
  },
  {
    mini: '/leaderview/border/titleBg5.png',
    imgSrc: '/leaderview/border/titleBg5.png'
  }
];

for (let i = 0; i < 8; i++){
  // console.log('i: ', typeof i);
  const src = `/leaderview/border/titleBg${6+Number(i)}.svg`
  titleCase.push({
    mini: src,
    imgSrc: src
  })
}

let cardCase = [
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
  },
  {
    mini: '/leaderview/border/cardBg4.png',
    imgSrc: '/leaderview/border/cardBg4.png'
  }
];

for (let i = 0; i < 11; i++){
  // console.log('i: ', typeof i);
  const src = `/leaderview/border/cardBg${5+Number(i)}.svg`
  cardCase.push({
    mini: src,
    imgSrc: src
  })
}

// 装饰器
let decoratorCase = [];

for (let i = 0; i < 21; i++){
  // console.log('i: ', typeof i);
  let curIndex = 1+Number(i);
  let type = 'svg';
  if (curIndex == 13) {
    type = 'png';
  } else if (curIndex == 21) {
    type = 'gif';
  }
  const src = `/leaderview/decorator/decoratorBg${curIndex}.${type}`
  decoratorCase.push({
    mini: src,
    imgSrc: src
  })
}


export const baseData = {
    allowOverflow: 20, // 允许超出画布的范围
    home: {
      // 主页大屏的默认配置
      w: 1920,
      h: 1080
    },
    // 指标器的可选字体
    fontFaces: [
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
      },
      {
        fontFace: 'asn',
        fontName: '0123456789'
      },
      {
        fontFace: 'SourceHanSansCN-Regular',
        fontName: '0123456789'
      }
    ],
    textFontFaces: [
      {
        fontFace: 'baidu',
        fontName: '百度综艺简体'
      },
      {
        fontFace: 'SourceHanSansCN-ExtraLight',
        fontName: '思源黑体ExtraLight'
      },
      {
        fontFace: 'SourceHanSansCN-Light',
        fontName: '思源黑体Light'
      },
      {
        fontFace: 'SourceHanSansCN-Normal',
        fontName: '思源黑体Normal'
      },
      {
        fontFace: 'SourceHanSansCN-Regular',
        fontName: '思源黑体Regular'
      },
      {
        fontFace: 'SourceHanSansCN-Medium',
        fontName: '思源黑体Medium'
      },
      {
        fontFace: 'SourceHanSansCN-Bold',
        fontName: '思源黑体Bold'
      },
      {
        fontFace: 'SourceHanSansCN-Heavy',
        fontName: '思源黑体Heavy'
      },
    ],
    cardCase,
    titleCase,
    decoratorCase,
  }
