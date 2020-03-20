const state = {
  homeData: {
    width: 1920,
    height: 1080
  },
  limitItem: {
    minX: 11000, // 多选时记录边界
    minY: 11000,
    maxX: 0,
    maxY: 0
  },
  onlyOneItem: true, // 编辑页是否只选中一个元件
  alertInfo: [],
  areaData: [], // 地图实时图的可选数据点的区域信息
  videoTims: {} // 视频的播放位置
}

export default state