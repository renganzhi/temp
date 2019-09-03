var env = process.env.NODE_ENV
var settings = {
  // 这里配置一些全局变量
  gbs: {
    host: env === 'development' ? '/api' : 'http://' + window.location.host
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
    fontFaces: [
      {
        fontFace: 'SourceHanSansCN-Regular',
        fontName: '标准字体'
      },
      {
        fontFace: 'asn',
        fontName: '样式1'
      }
    ]
  }
}
module.exports = settings
