const mutations = {
  changeHomeData (state, data) {
    state.homeData = data
  },
  changeAreaData (state, data) {
    state.areaData = data
  },
  changeAlertInfo (state, data) {
    state.alertInfo = data
  },
  changeVideoTims (state, data) {
    // 添加，改变某一个视频的播放进度
    state.videoTims[data.id] = data.time
  },
  initVideoTims (state, data) {
    state.videoTims = data
  }
}
export default mutations
