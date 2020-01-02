const Public = {
  bigScreenfullScreen: function (el) {
    // F11 浏览器全屏模式
    var rfs =
      el.requestFullScreen ||
      el.webkitRequestFullScreen ||
      el.mozRequestFullScreen ||
      el.msRequestFullScreen
    var wscript = null
    if (typeof rfs !== 'undefined' && rfs) {
      rfs.call(el)
    }

    if (typeof window.ActiveXObject !== 'undefined') {
      wscript = new ActiveXObject('WScript.Shell')
      if (wscript) {
        wscript.SendKeys('{F11}')
      }
    }
  },
  exitFullScreen: function () {
    // 关闭浏览器全屏模式
    var el = document
    var cfs =
      el.cancelFullScreen ||
      el.webkitCancelFullScreen ||
      el.mozCancelFullScreen ||
      el.exitFullScreen
    var wscript
    if (typeof cfs !== 'undefined' && cfs) {
      cfs.call(el)
    }

    if (typeof window.ActiveXObject !== 'undefined') {
      wscript = new ActiveXObject('WScript.Shell')
      if (wscript != null) {
        wscript.SendKeys('{F11}')
      }
    }
  },
  checkFull: function () {
    return !!(
      document.fullscreen ||
      document.mozFullScreen ||
      document.webkitIsFullScreen ||
      document.webkitFullScreen ||
      document.msFullScreen
    )
    // // 监听是否是全屏状态
    // var isFull = document.fullscreenEnabled || window.fullScreen || document.webkitIsFullScreen
    //         || document.msFullscreenEnabled;
    // return !!isFull;
  }
}
// module.exports = Public
export default Public
