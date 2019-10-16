import compsArr from '#/js/chartJson'
import DragBox from './../Common/DragBox'
import Compose from './../Common/Compose'
import Select2 from './../Common/Select2'
import Vcolor from './../Common/Vcolor'
import PreView from './../PreView/PreView'
import Confirm from './../Common/Confirm'
import { baseData, gbs } from '@/config/settings'
import { Slider, Notification } from 'element-ui'
import { mapActions, mapGetters } from 'vuex'
// import html2canvas from 'html2canvas' // 图片的层级总是要高一些
import qs from 'qs'
import lodash from 'lodash'

export default {
  name: 'edit',
  components: { DragBox, Compose, Select2, Vcolor, Confirm, PreView, Slider },
  props: [],
  data: function () {
    return {
      refreshData: true,
      viewKey: new Date().getTime() + parseInt(Math.random() * 10),
      showKeybd: false,
      levelTipsShow: false, // 数据量级提示信息
      levelChangeIndex: -1, // 量级改变的输入框
      selfMapLevel: false, // 当前元件的展示范围发生改变，并非切换元件导致的改变
      alertMapData: [], // 实时地图的数据
      selectedPositn: [],
      geoCoordMap: {}, // 各地理位置对应的坐标
      allowOverflow: 20, // 允许溢出的宽高
      childResize: false, // 当前选中的是不是组合内部的元件
      parentId: 0, // 当前选中组合内部的元件的父级元件的序号
      provinceArr: [], // 34个省的数据
      cityArr: [], // 选中省的所有市数据
      areaArr: [], // 选中市的所有区域数据
      editPieces: [], // 区域分布图量级
      editPiecesCopy: [],
      selectMapData: {}, // 匹配区域分布图输入框的数据
      settingData: baseData,
      pageId: 0,
      value2: 0.5,
      oldCheckId: '', // 优化每次选中都会触发请求接口，仅在切换元件时才请求
      viewPage: false,
      pageData: '',
      composeData: '',
      compsArr: compsArr,
      isFlase: false,
      editable: true, // 操作flag,编辑为true,查看为false
      showStyleTab: true,
      pageName: '', // 页面名称
      showDataConf: false, // 展示系统数据配置
      isFull: false,
      selectedItem: {},
      selectedIndex: null,
      chartNum: [],
      miniW: 20,
      minIndex: 499, // 当前最低层级
      maxIndex: 500, // 当前最高层级
      paintInput: {
        width: 1920, // 输入的画布大小
        height: 1080
      },
      paintObj: {
        width: 1920,
        height: 1080,
        bgColor: '',
        bgImg: '',
        scale: 80,
        bgStyle: '3', // 背景图铺满方式
        opacity: 100,
        showGrid: true // 显示网格
      },
      // 箭头的展示与隐藏
      arrowObj: {
        witdhS: false,
        heightS: false,
        xShow: false,
        yShow: false
      },
      ceditable: true, // 组合
      combinList: [], // 组合的数组
      chooseItems: [], // ctrl选中的元件
      chooseIndexs: [], // ctrl选中的单个元件在chartNum中的序号
      chooseCompIndexs: [], // ctrl选中的组合元件的序号
      onCtrl: false, // 是否按住ctrl
      showBackModal: false, // 离开页面弹窗
      colorType: 'defalut',
      defaultFontSize: [12, 13, 14, 16, 18, 20, 28, 36, 48, 72],
      defMapColors: ['#bb2a52', '#bd3d50', '#bf4e4e', '#c2634b', '#c47346', '#c7833f', '#ca9137', '#cd9d2c'],
      defalutColors: [
        '#37a2da',
        '#32c5e9',
        '#67e0e3',
        '#9fe6b8',
        '#ffdb5c',
        '#ff9f7f',
        '#fb7293',
        '#e062ae',
        '#e690d1',
        '#e7bcf3',
        '#9d96f5',
        '#8378ea',
        '#96bfff'
      ],
      defGradColors: [
        ['#6fcaf7', '#0c79c5'],
        ['#8feee5', '#1bbcae'],
        ['#fa8d76', '#db4222'],
        ['#af8af3', '#874edc'],
        ['#f5739c', '#f31d53'],
        ['#ffdf91', '#eeb01b'],
        ['#5c84e7', '#144fe5'],
        ['#85f8c0', '#62dc26']
      ],
      showWindowBtn: false, // 多资源多指标弹窗按钮
      syst: {
        // 系统数据
        urlSel: [], // 接口下拉列表，根据不同的图形类型有不同的接口
        curUrl: [], // 缓存当前选中的接口params中需要配置的数据
        curConf: {
          // 缓存当前配置的系统数据，用于请求数据
          url: '',
          params: {} // 请求发送给后端的数据
        },
        windowObj: [], // 保存弹窗数据
        windowData: [], // 发送给后端的弹窗数据
        chainParams: {} // 用于缓存需要多级下拉联动请求，以便不用每次遍历当前选中接口数据
      },
      dataApi: [], // 选择接口数据
      currApi: {}, // 当前选中api
      dataInps: [],
      minXItem: {
        x: 1920,
        y: 1080,
        maxX: 0,
        maxY: 0
      }, // 多选情况下X值最小的元素
      dataApiParams: {}, // 请求数据传给后端的数据结构
      chainParams: {}, // 存放需要联动的指标
      testObj: {}, // 测试验证
      widthVali: {
        isShowError: false,
        errorMsg: ''
      },
      heightVali: {
        isShowError: false,
        errorMsg: ''
      },
      xVali: {
        isShowError: false,
        errorMsg: ''
      },
      yVali: {
        isShowError: false,
        errorMsg: ''
      },
      progressObj: {
        height: 16,
        radius: 8
      },
      proHeightErr: false,
      radiusErr: false,
      selectArea: {
        choose: false, // 是否处于框选状态
        left: 0, // 多选情况下输入框改变前的x位移
        top: 0, // 多选情况下输入框改变前的y位移
        width: 0,
        height: 0
      },
      // 框选的鼠标起点
      chooseStart: {
        posX: 0,
        posY: 0
      }
    }
  },
  computed: {
    ...mapGetters([
      'alertInfo'
    ]),
    alertLevels: function () {
      if (this.alertInfo && this.alertInfo.length > 0) {
        return _.forEach(this.alertInfo, function (item) {
          item.value = item.level
        })
      } else {
        return [
          { name: '通知', value: 1 },
          { name: '警告', value: 2 },
          { name: '一般', value: 3 },
          { name: '严重', value: 4 },
          { name: '致命', value: 5 },
          { name: '提示', value: 6 }
        ]
      }
    },
    paintStyle: function () {
      var type = this.paintObj.bgStyle
      if (type === '1') {
        var backgroundSize = '100% auto'
      } else if (type === '2') {
        backgroundSize = 'auto 100%'
      } else {
        backgroundSize = '100% 100%'
      }
      return {
        backgroundImage: this.paintObj.bgImg
          ? 'url(' + gbs.host + '/leaderview' + this.paintObj.bgImg + ')' : '',
        backgroundColor: this.paintObj.bgColor,
        backgroundSize: backgroundSize,
        opacity: this.paintObj.opacity / 100
      }
    },
    isEcharts: function () {
      return (
        this.selectedItem.chartType &&
        this.selectedItem.chartType.indexOf('ve-') !== -1
      )
    },
    showLengendConf: function () {
      // 对部分
      var type = this.selectedItem.chartType
      return type !== 've-gauge'
    },
    alertLevel: function () {
      // 告警的接口需要展示系统配置的告警色，当数据为系统数据时，颜色不可以修改
      if (
        this.selectedItem &&
        this.selectedItem.chartData &&
        this.selectedItem.chartData.colors &&
        this.selectedItem.ctDataSource === 'system'
      ) {
        return false
      }
      return true
    }
  },
  methods: {
    ...mapActions([
      'changeHomeData',
      'changeAreaData'
    ]),
    scrollLeft (x) {
      if (this.chooseIndexs.length + this.chooseCompIndexs.length > 1) {
        this.minXItem.x += x
        this.changeTarget('x')
      } else {
        this.testObj.x += x
      }
      /* x = x || 10
      var _scrollLeft = $('.m-main').scrollLeft()
      _scrollLeft += x
      if (_scrollLeft < 0) {
        _scrollLeft = 0
      }
      $('.m-main').scrollLeft(_scrollLeft) */
    },
    scrollTop (y) {
      if (this.chooseIndexs.length + this.chooseCompIndexs.length > 1) {
        this.minXItem.y += y
        this.changeTarget('y')
      } else {
        this.testObj.y += y
      }
      /* // 画布移动
      y = y || 10
      var _scrollTop = $('.m-main').scrollTop()
      _scrollTop += y
      if (_scrollTop < 0) {
        _scrollTop = 0
      }
      $('.m-main').scrollTop(_scrollTop) */
    },
    getMapData (chinaId) {
      var mapPth = gbs.inDev ? 'static' : 'leaderview-static'
      if (chinaId) {
        return new Promise((resolve, reject) => {
          this.axios.get('./../../../../' + mapPth + '/libs/map/' + chinaId + '.json', {}).then(response => {
            var data = this.initMapData(response)
            resolve(data)
          }).catch((error) => {
            if (gbs.inDev) {
              Notification({
                message: '该地区暂无详细地图',
                position: 'bottom-right',
                customClass: 'toast toast-info'
              })
            } else {
              tooltip('', '该地区暂无详细地图', 'info')
            }
            reject(error)
          })
        })
      } else {
        return null
      }
    },
    mapDataToChart () {
      // 用户输入的数据转化为区域分布图所需数据
      var tempData = []
      for (var k in this.selectMapData) {
        let obj = { '位置': k, '告警': this.selectMapData[k] }
        tempData.push(obj)
      }
      this.selectedItem.chartData.rows = tempData
    },
    chartDataToMap () {
      if (this.selectedItem.chartType === 'v-map') {
        // chartData转化为input输入数据
        this.selectMapData = {}
        var tempData = this.selectedItem.chartData.rows
        tempData.forEach((item) => {
          this.selectMapData[item['位置']] = item['告警']
        })
      }
    },
    selectToPoint () {
      // 数据点转化为散点数据
      this.geoCoordMap = {}
      this.areaArr.forEach((item) => {
        this.geoCoordMap[item.name] = item.geoCoord
      })
    },
    clearAlertMap () {
      console.log('清空初始化数据点')
      this.alertMapData = []
      this.selectedItem.chartData = [{ name: this.areaArr[0].name, value: 2 }]
      this.$nextTick(() => {
        this.alertMapData = [{ name: this.areaArr[0].name, value: 2 }]
        this.selectedPositn = [this.areaArr[0].name]
      })
      // this.$set('selectedItem', 'chartData', [])
    },
    initLevelData (areaData) {
      // 区域分布图给前三项赋默认值
      var arr = areaData || this.areaArr
      console.log('初始化前三条默认数据')
      this.selectMapData = {}
      var tempData = []
      let len = arr.length < 3 ? arr.length : 3
      for (let i = 0; i < len; i++) {
        this.selectMapData[arr[i].name] = i * 50 + 25
        let chartObj = { '位置': arr[i].name, '告警': i * 50 + 25 }
        tempData.push(chartObj)
      }
      this.selectedItem.chartData.rows = tempData
    },
    changeMapData (chinaId, target) {
      var mapPth = gbs.inDev ? 'static' : 'leaderview-static'
      this.axios.get('./../../../../' + mapPth + '/libs/map/' + chinaId + '.json', {}).then(response => {
        this[target] = this.initMapData(response)
        if (target === 'provinceArr' && !this.selectedItem.provinceCode) {
          this.selectedItem.provinceCode = this[target][0].value
        }
        if (target === 'cityArr') {
          this.selectedItem.cityCode = this[target][0].value
        }
      }).catch((err) => {
        console.log(err)
      })
    },
    // 改变展示范围
    chgMapLevel () {
      // 这里会先于watch mapLevel触发
      console.log('-------selfMapLevel: true------')
      this.selfMapLevel = true
    },
    chgMapGrad (index) {
      if (this.editPieces[index]['max'] < this.editPieces[index]['min']) {
        if (gbs.inDev) {
          Notification({
            message: '量级的最大值不可小于最小值',
            position: 'bottom-right',
            customClass: 'toast toast-info'
          })
        } else {
          tooltip('', '量级的最大值不可小于最小值', 'info')
        }
        this.editPieces = JSON.parse(JSON.stringify(this.editPiecesCopy))
      } else if (!this.editPieces[index + 1]['max'] || (this.editPieces[index]['max'] < this.editPieces[index + 1]['max'] - 1)) {
        this.editPieces[index + 1]['min'] = Number(this.editPieces[index]['max']) + 1
        this.editPiecesCopy = JSON.parse(JSON.stringify(this.editPieces))
      } else {
        var newValue = this.editPieces[index]['max']
        if (index === 0 && newValue + 2 > this.editPieces[this.editPieces.length - 2]['max']) {
          // 合并之后梯度不足3个
          if (gbs.inDev) {
            Notification({
              message: '区间跨度太大，请至少保证三个量级',
              position: 'bottom-right',
              customClass: 'toast toast-info'
            })
          } else {
            tooltip('', '区间跨度太大，请至少保证三个量级', 'info')
          }
          this.editPieces = JSON.parse(JSON.stringify(this.editPiecesCopy))
          return
        }
        // var delLevel = 0
        // for (let i = index + 1, len = this.editPieces.length - 1; i < len; i++) {
        //   if (newValue >= this.editPieces[i]['max']) {
        //     delLevel++
        //   }
        // }
        // var r = confirm('与其余量级区间重合，是否合并为一个量级')
        this.levelChangeIndex = index
        this.levelTipsShow = true
        // if (r) {
        //   this.editPieces.splice(index + 1, delLevel)
        //   this.editPieces[index + 1].min = Number(newValue) + 1
        // } else {
        //   this.editPieces = JSON.parse(JSON.stringify(this.editPiecesCopy))
        // }
      }
      // this.editPiecesCopy = JSON.parse(JSON.stringify(this.editPieces))
    },
    sureLevelTips () {
      var index = this.levelChangeIndex
      var newValue = this.editPieces[index]['max']
      var delLevel = 0
      for (let i = index + 1, len = this.editPieces.length - 1; i < len; i++) {
        if (newValue >= this.editPieces[i]['max']) {
          delLevel++
        }
      }
      this.editPieces.splice(index + 1, delLevel)
      this.editPieces[index + 1].min = Number(newValue) + 1
      this.levelTipsShow = false
      this.editPiecesCopy = JSON.parse(JSON.stringify(this.editPieces))
    },
    cancelLevelTips () {
      this.editPieces = JSON.parse(JSON.stringify(this.editPiecesCopy))
      this.levelTipsShow = false
    },
    addMapLevel () {
      if (this.editPieces.length >= 8) {
        return
      }
      var lastMin = this.editPieces[this.editPieces.length - 1].min
      this.editPieces[this.editPieces.length - 1].max = lastMin + 49
      this.editPieces.push({ min: lastMin + 50 })
      this.editPiecesCopy = JSON.parse(JSON.stringify(this.editPieces))
      this.selectedItem.piecesData = JSON.parse(JSON.stringify(this.editPieces))
    },
    delMapLevel () {
      if (this.editPieces.length <= 3) {
        return
      }
      this.editPieces.pop()
      delete this.editPieces[this.editPieces.length - 1].max
      this.editPiecesCopy = JSON.parse(JSON.stringify(this.editPieces))
      this.selectedItem.piecesData = JSON.parse(JSON.stringify(this.editPieces))
    },
    initMapData (mapJson) {
      var mapData = []
      for (var i = 0; i < mapJson.features.length; i++) {
        mapData.push({
          value: mapJson.features[i].id,
          name: mapJson.features[i].properties.name,
          geoCoord: mapJson.features[i].properties.cp || mapJson.features[i].geometry.coordinates[0][0][0]
        })
      }
      return mapData
    },
    // 删除实时图数据点
    delAlertLevel (index) {
      this.alertMapData.splice(index, 1)
      this.selectedPositn.splice(index, 1)
    },
    // 地图实时图添加数据点
    addAlertLevel () {
      for (let i = 0, len = this.areaArr.length; i < len; i++) {
        if (this.selectedPositn.indexOf(this.areaArr[i].name) === -1) {
          this.selectedPositn.push(this.areaArr[i].name)
          this.alertMapData.push({ name: this.areaArr[i].name, value: 1 })
          break
        }
      }
    },
    // 切换地图的省
    chgProvince (id) {
      if (this.selectedItem.mapLevel === 'city') {
        // 北京， ... ， 香港， 澳门
        var noMapArr = ['110000', '310000', '500000', '120000', '710000', '810000', '820000']
        if (noMapArr.indexOf(id) !== -1) {
          this.selectedItem.mapLevel = 'province'
          if (gbs.inDev) {
            Notification({
              message: '该地区不支持第三级地图',
              position: 'bottom-right',
              customClass: 'toast toast-info'
            })
          } else {
            tooltip('', '该地区不支持第三级地图', 'info')
          }
        }
      }
      if (id) {
        this.getMapData(id).then((data) => {
          this.cityArr = data
          console.log('===========' + this.selectedItem.mapLevel + '=========')
          if (this.selectedItem.mapLevel === 'province') {
            this.areaArr = data
            if (this.selectedItem.chartType === 'v-map') {
              this.initLevelData()
            } else if (this.selectedItem.chartType === 'v-scatter') {
              this.clearAlertMap()
            }
          } else if (this.selectedItem.mapLevel === 'city') {
            this.selectedItem.cityCode = data[0].value
            if (this.selectedItem.chartType === 'v-scatter') {
              this.clearAlertMap()
            }
          }
        })
      }
    },
    chgCity (id) {
      // console.log('+++++++chgCity+++++++-' + this.selfMapLevel)
      id = id || this.selectedItem.cityCode
      if (id && this.selfMapLevel) {
        this.getMapData(id).then((data) => {
          this.areaArr = data
          this.selectedItem.cityCode = id
          if (this.selectedItem.chartType === 'v-map') {
            this.initLevelData()
          }
          if (this.selectedItem.chartType === 'v-scatter') {
            if (this.selfMapLevel && id) {
              this.clearAlertMap()
            }
          }
        })
      }
    },
    chgAreaName (name, index) {
      console.log('chgAreaName:' + name)
      if (name) {
        this.selectedPositn[index] = name
      }
      // 数组需要更换
    },
    changeProHeight () {
      // 改变进度条元件的高度
      var proHeight = Math.floor(this.progressObj.height)
      if (isNaN(proHeight) || proHeight < 0) {
        this.progressObj.height = this.selectedItem.proHeight
      }
      if (proHeight >= 8 && proHeight <= 24) {
        this.selectedItem.proHeight = proHeight
      } else if (proHeight > 24) {
        this.progressObj.height = 24
        this.selectedItem.proHeight = 24
      } else if (proHeight < 8) {
        this.progressObj.height = 8
        this.selectedItem.proHeight = 8
      } else {
        this.progressObj.height = this.selectedItem.proHeight
      }
      this.selectedItem.radius = Math.ceil(this.selectedItem.proHeight / 2)
      this.progressObj.radius = this.selectedItem.radius
    },
    // 改变进度条元件的圆角
    changeRadius () {
      var radius = Math.floor(this.progressObj.radius)
      if (isNaN(radius) || radius < 0) {
        this.progressObj.radius = this.selectedItem.radius
      }
      if (radius >= 0 && radius <= Math.ceil(this.selectedItem.proHeight / 2)) {
        this.selectedItem.radius = radius
      } else {
        this.progressObj.radius = Math.ceil(this.selectedItem.proHeight / 2)
        this.selectedItem.radius = this.progressObj.radius
      }
    },
    colorToAll (color) {
      var _colors = this.selectedItem.ctColors
      this.chartNum.forEach((item) => {
        if (item.chartType.indexOf('ve-') !== -1) {
          if (
            item.chartData &&
            item.chartData.colors &&
            item.ctDataSource === 'system'
          ) {
            // 接口返回的系统默认颜色不做修改
          } else {
            item.ctColors = JSON.parse(JSON.stringify(_colors))
            item.colorType = 'custom'
          }
        }
      })
    },
    // 获取当前页的配置
    gerPageConf (id) {
      // home/homePage/getById
      this.axios.get(`/leaderview/home/homePage/getById/${id}`).then(res => {
        this.pageName = res.obj.name
        if (res.obj.viewConf) {
          this.chartNum = JSON.parse(res.obj.viewConf)
          this.paintObj = JSON.parse(res.obj.paintObj)
          this.paintInput.width = this.paintObj.width
          this.paintInput.height = this.paintObj.height
          this.changeHomeData(this.paintObj) // vuex保存画布大小
          this.combinList = JSON.parse(res.obj.composeObj)
          let tempNum = this.chartNum.concat(this.combinList)
          for (let i = 0, len = tempNum.length; i < len; i++) {
            tempNum[i].zIndex > this.maxIndex
              ? (this.maxIndex = tempNum[i].zIndex)
              : ''
            tempNum[i].zIndex < this.minIndex
              ? (this.minIndex = tempNum[i].zIndex)
              : ''
          }
        } else {
          this.chartNum = []
        }
      })
    },
    initChart (value) {
      this.showStyleTab = true
      var obj = $.extend(
        true, {}, {
        id: new Date().getTime(),
        ctName: value.text,
        ctLegendShow: 'true',
        x: 400,
        y: 100,
        width: 350,
        height: 350,
        zIndex: ++this.maxIndex,
        colorType: 'defalut',
        ctColors: value.chartType === 'v-map' ? this.defMapColors.concat() : this.defalutColors.concat(),
        ctDataSource: 'static', // 数据来源system\static，默认static
        url: '', // 请求接口
        params: {}, // 请求接口参数
        bdpx: 1, // 边框线宽
        fontSize: 12, // 字号
        lineArea: false // 折线是否为区域
      },
        value
      )

      delete obj.className
      delete obj.text
      this.cancelSelected()
      obj.slted = true
      this.chartNum.push(obj)
      this.selectedItem = obj
      // this.testObj = this.selectedItem // 修改宽高等会直接修改元件
      this.testObj = JSON.parse(JSON.stringify(this.selectedItem))
      this.chooseIndexs = [this.chartNum.length - 1]
      if (value.chartType === 'v-map') {
        this.selectMapData = { '台湾': 25, '河北': 75, '山西': 125 }
      }
      if (value.chartType === 'progress') {
        this.progressObj.height = 16
        this.progressObj.radius = 8
      }
      if (!gbs.inDev) {
        this.$nextTick(function () {
          titleShow('bottom', $('.m-right'))
        })
      }
    },
    chgColorType: function () {
      if (this.selectedItem.colorType === 'defalut') {
        this.selectedItem.ctColors.splice(0, this.selectedItem.ctColors.length)
        if (this.selectedItem.chartType === 'v-map') {
          this.$set(this.selectedItem, 'ctColors', this.defMapColors.concat())
        } else {
          this.$set(this.selectedItem, 'ctColors', this.defalutColors.concat())
        }
      } else {
        if (this.selectedItem.chartType !== 'v-map') {
          this.selectedItem.ctColors.splice(0, this.selectedItem.ctColors.length)
          // var newColors = this.setDefColor()
          let newColors = JSON.parse(JSON.stringify(this.defGradColors))
          this.$set(this.selectedItem, 'ctColors', newColors)
        }
      }
    },
    // 旧的系列色赋值
    setDefColor: function () {
      var colorArr = []
      for (var i = 0; i < this.defalutColors.length; i++) {
        var temp = [this.defalutColors[i], this.defGradColors[i]]
        colorArr.push(temp)
      }
      return colorArr
    },
    clickPaint (event) {
      if (!window.event.ctrlKey) {
        // console.log(this.selectArea.choose)
        this.cancelSelected(event)
      }
      if ($('.tempDiv').length > 0) {
        if (this.selectArea.choose) {
          this.selectArea.choose = false
          $('.tempDiv').remove()
        } else {
          this.selectArea.choose = true
        }
      }
    },
    cancelSelected (event) {
      // 取消其他selected
      $.each(this.chartNum, function (i, d) {
        d.slted = false
      })
      $.each(this.combinList, function (i, d) {
        d.slted = false
      })
      if (event) {
        this.selectedItem = {}
      }
      this.chooseItems = []
      this.chooseIndexs = []
      this.chooseCompIndexs = []
      // console.log('取消所有选中')
      // 初始化多选时的x，y值
      this.minXItem.x = 10000
      this.minXItem.y = 10000
      this.minXItem.maxX = 0
      this.minXItem.maxY = 0
      this.childResize = false // 暂且保留
      // this.onCtrl = false
    },
    selected: function (item, ev, type, i) {
      this.selfMapLevel = false
      if (this.childResize && ev === 'context') {
        // 内部元件的右键
        return
      }
      this.childResize = false
      if (ev !== 'context' && !window.event.ctrlKey) {
        this.cancelSelected()
      }
      if (window.event.ctrlKey && ev !== 'move' && item.slted) {
        // 取消选中
        item.slted = this.editable && false
        if (type === 'compose') {
          this.combinList[i].slted = this.editable && false
          var _id = this.chooseCompIndexs.indexOf(i)
          if (_id !== -1) {
            this.chooseCompIndexs.splice(_id, 1)
          }
        } else {
          _id = this.chooseIndexs.indexOf(i)
          if (_id !== -1) {
            this.chooseIndexs.splice(_id, 1)
            // this.chooseItems.push(this.chartNum[i])
          }
        }
        // 更新多选情况下的x，y
        this.updateMinXitem()
        return
      } else {
        // 增加选中
        item.slted = this.editable && true
        if (item.chartType === 'v-map') {
          this.selectedItem = {} // 避免触发三级下拉的监听
        }
        this.selectedItem = item
        if (item.chartType === 'progress') {
          this.progressObj.height = item.proHeight || 16
          this.progressObj.radius = item.radius || 8
        }
        this.testObj = JSON.parse(JSON.stringify(item))
        if (type === 'compose') {
          this.combinList[i].slted = this.editable && true
          if (this.chooseCompIndexs.indexOf(i) === -1) {
            this.chooseCompIndexs.push(i)
          }
        } else {
          if (this.chooseIndexs.indexOf(i) === -1) {
            this.chooseIndexs.push(i)
            // this.chooseItems.push(this.chartNum[i])
          }
        }
        // 设置多选情况下的x，y
        if (item.x < this.minXItem.x) {
          // this.minXItem = item
          this.minXItem.x = item.x
          this.minXItem.y = item.y
          this.selectArea.left = item.x // 不变的最小值
          this.selectArea.top = item.y
        }
        if (item.x + item.width > this.minXItem.maxX) {
          this.minXItem.maxX = item.x // 最远元素的x
        }
        if (item.y + item.height > this.minXItem.maxY) {
          this.minXItem.maxY = item.y // 最远元素的y
        }
      }
      if (this.selectedItem.chartType === 'v-scatter') {
        if (ev !== 'move' && this.oldCheckId !== item.id) {
          this.alertMapData = []
          if (this.selectedItem.mapLevel === 'country') {
            this.alertMapData = _.cloneDeep(this.selectedItem.chartData)
            this.selectedPositn = _.map(this.alertMapData, 'name')
          }
        }
        if (!window.event.ctrlKey && this.oldCheckId !== item.id) {
          this.oldCheckId = item.id
          // console.log('切换元件，重新计算地图~')
          if (this.selectedItem.mapLevel === 'country') {
            this.areaArr = this.provinceArr
          } else if (this.selectedItem.mapLevel === 'province') {
            this.getMapData(this.selectedItem.provinceCode).then((data) => {
              this.areaArr = data
              this.alertMapData = _.cloneDeep(this.selectedItem.chartData)
              this.selectedPositn = _.map(this.alertMapData, 'name')
            })
          } else {
            this.getMapData(this.selectedItem.provinceCode).then((data) => {
              this.cityArr = data
            })
            this.getMapData(this.selectedItem.cityCode).then((data) => {
              this.areaArr = data
              this.alertMapData = _.cloneDeep(this.selectedItem.chartData)
              this.selectedPositn = _.map(this.alertMapData, 'name')
            })
          }
        }
        // this.selectToPoint() // 这里应该不需要
      }
      if (this.selectedItem.chartType === 'v-map') {
        // 这里是不是少了点什么
        if (!window.event.ctrlKey && this.oldCheckId !== item.id) {
          this.editPieces = JSON.parse(JSON.stringify(this.selectedItem.piecesData))
          this.editPiecesCopy = JSON.parse(JSON.stringify(this.selectedItem.piecesData)) // 副本
          // 地图元件重新加载右边的区域数据
          this.oldCheckId = item.id
          console.log('选中地图: ' + this.selectedItem.mapLevel)
          if (this.selectedItem.mapLevel === 'country') {
            this.areaArr = this.provinceArr
            this.$nextTick(() => {
              this.chartDataToMap()
            })
          } else if (this.selectedItem.mapLevel === 'province') {
            this.getMapData(this.selectedItem.provinceCode).then((data) => {
              this.areaArr = data
              this.chartDataToMap()
            })
          } else {
            this.getMapData(this.selectedItem.provinceCode).then((data) => {
              this.cityArr = data
            })
            this.getMapData(this.selectedItem.cityCode).then((data) => {
              this.areaArr = data
              this.chartDataToMap()
            })
          }
        } else {
          if (ev !== 'move') {
            this.chartDataToMap()
          }
        }
      }

      if (!window.event.ctrlKey && this.oldCheckId !== item.id) {
        // 切换选中的元件
        this.showWindowBtn = false // 隐藏部件弹窗按钮
        this.oldCheckId = item.id
        if (ev === 'down' && item.ctDataSource === 'system') {
          this.getUrlByType(true)
        }
      }
      if (!gbs.inDev) {
        this.$nextTick(function () {
          titleShow('bottom', $('.m-right'))
        })
      }
      if (ev !== 'move') {
        // 重新给静态数据框赋值
        this.refreshData = false
        this.$nextTick(() => {
          this.refreshData = true
        })
      }
    },
    // 组合内的元件选中
    childSelect (item, childId, index) {
      console.log('双击选中内部元件: ' + index)
      this.selectedItem = item
      if (item.chartType === 'progress') {
        this.progressObj.height = item.proHeight
        this.progressObj.radius = item.radius
      }
      this.testObj = JSON.parse(JSON.stringify(item))
      this.chooseIndexs = [childId]
      // this.parentId = this.chooseCompIndexs[0] // 父级元件的序号
      this.parentId = index // 父级元件的序号
      this.chooseCompIndexs = []
      this.childResize = true
      if (this.selectedItem.chartType === 'v-scatter') {
        this.alertMapData = []
        if (this.selectedItem.mapLevel === 'country') {
          this.alertMapData = _.cloneDeep(this.selectedItem.chartData)
          this.selectedPositn = _.map(this.alertMapData, 'name')
        }
        // this.oldCheckId = item.id
        if (this.selectedItem.mapLevel === 'country') {
          this.areaArr = this.provinceArr
        } else if (this.selectedItem.mapLevel === 'province') {
          this.getMapData(this.selectedItem.provinceCode).then((data) => {
            this.areaArr = data
            this.alertMapData = _.cloneDeep(this.selectedItem.chartData)
            this.selectedPositn = _.map(this.alertMapData, 'name')
          })
        } else {
          this.getMapData(this.selectedItem.provinceCode).then((data) => {
            this.cityArr = data
          })
          this.getMapData(this.selectedItem.cityCode).then((data) => {
            this.areaArr = data
            this.alertMapData = _.cloneDeep(this.selectedItem.chartData)
            this.selectedPositn = _.map(this.alertMapData, 'name')
          })
        }
      }
      if (this.selectedItem.chartType === 'v-map') {
        this.editPieces = JSON.parse(JSON.stringify(this.selectedItem.piecesData))
        this.editPiecesCopy = JSON.parse(JSON.stringify(this.selectedItem.piecesData)) // 副本
        // 地图元件重新加载右边的区域数据
        // this.oldCheckId = item.id
        if (this.selectedItem.mapLevel === 'country') {
          this.areaArr = this.provinceArr
          this.$nextTick(() => {
            this.chartDataToMap()
          })
        } else if (this.selectedItem.mapLevel === 'province') {
          this.getMapData(this.selectedItem.provinceCode).then((data) => {
            this.areaArr = data
            this.chartDataToMap()
          })
        } else {
          this.getMapData(this.selectedItem.provinceCode).then((data) => {
            this.cityArr = data
          })
          this.getMapData(this.selectedItem.cityCode).then((data) => {
            this.areaArr = data
            this.chartDataToMap()
          })
        }
      }
    },
    updateMinXitem: function () {
      var _this = this
      var minIndex = _.minBy(this.chooseIndexs, function (i) { return _this.chartNum[i].x })
      var minCompIndex = _.minBy(this.chooseCompIndexs, function (i) { return _this.combinList[i].x })
      if (minIndex === undefined && minCompIndex === undefined) {
        return
      }
      if (minCompIndex === undefined) {
        // this.minXItem = this.chartNum[minIndex]
        this.changeMinXitem(this.chartNum[minIndex].x, this.chartNum[minIndex].y)
        return
      }
      if (minIndex === undefined) {
        // this.minXItem = this.combinList[minCompIndex]
        this.changeMinXitem(this.combinList[minCompIndex].x, this.combinList[minCompIndex].y)
        return
      }
      if (this.chartNum[minIndex].x < this.combinList[minCompIndex].x) {
        // this.minXItem = this.chartNum[minIndex]
        this.changeMinXitem(this.chartNum[minIndex].x, this.chartNum[minIndex].y)
      } else {
        // this.minXItem = this.combinList[minCompIndex]
        this.changeMinXitem(this.combinList[minCompIndex].x, this.combinList[minCompIndex].y)
      }
    },
    changeMinXitem: function (x, y) {
      this.minXItem.x = x
      this.minXItem.y = y
      this.selectArea.left = x
      this.selectArea.top = y
    },
    // 多选时改变x，y位移
    changeTarget: function (xy) {
      var left = 'left'
      var width = 'width'
      var maxX = 'maxX'
      if (xy === 'y') {
        left = 'top'
        width = 'height'
        maxX = 'maxY'
      }
      var allowOverflow = baseData.allowOverflow // 可提取为可配置变量
      if (this.minXItem[xy] < -allowOverflow) {
        // this.minXItem[xy] = this.selectArea[left] // 恢复原值
        this.minXItem[xy] = -allowOverflow
      }
      if (this.minXItem[xy] > this.paintObj[width] - this.minXItem[maxX] + allowOverflow) {
        this.minXItem[xy] = this.paintObj[width] - this.minXItem[maxX] + allowOverflow
      }
      var changes = parseInt(this.minXItem[xy] - this.selectArea[left])
      this.chooseIndexs.forEach((i) => {
        this.chartNum[i][xy] += changes
      })
      this.chooseCompIndexs.forEach((i) => {
        this.combinList[i][xy] += changes
      })
      this.selectArea[left] = this.minXItem[xy]
    },
    bindCtrl: function () {
      // this.onCtrl = true // 按住ctrl键
    },
    addToCompose: function () {
      // this.compose(this.chooseItems)
      var itemArr = this.indexToItem(1) // 只要单个元件，不要元件组
      this.compose(itemArr)
    },
    // 单层元件组合 有待调整，也许会用于支持组合之后的再组合
    compose: function (arr) {
      var leftIndex = 0,
        _left = arr[0].x
      var rightIndex = 0,
        _right = arr[0].x + arr[0].width
      var topIndex = 0,
        _top = arr[0].y
      var bottomIndex = 0,
        _bottom = arr[0].y + arr[0].height
      // var _index = 500
      var _index = this.maxIndex // 没有图层属性的旧数据
      var minIndexItem = _.minBy(arr, function (item) { return item.zIndex }) // 旧数据会返回undefined
      // var maxIndexItem = _.maxBy(arr, function (item) { return item.zIndex })
      // console.log(minIndexItem.zIndex, maxIndexItem.zIndex)
      if (minIndexItem) {
        _index = minIndexItem.zIndex
      }
      // 获取合并之后的组件的范围
      for (var i = 1, len = arr.length; i < len; i++) {
        if (arr[i].x < _left) {
          _left = arr[i].x
          leftIndex = i
        }
        if (arr[i].y < _top) {
          _top = arr[i].y
          topIndex = i
        }
        if (arr[i].x + arr[i].width > _right) {
          _right = arr[i].x + arr[i].width
          rightIndex = i
        }
        if (arr[i].y + arr[i].height > _bottom) {
          _bottom = arr[i].y + arr[i].height
          bottomIndex = i
        }
        // if (arr[i].zIndex && arr[i].zIndex < _index) {
        //   _index = arr[i].zIndex
        // }
      }
      // 为每一个内部组件重新计算相对位置
      for (var i = 0, len = arr.length; i < len; i++) {
        // arr[i].left = arr[i].x - _left
        // arr[i].top = arr[i].y - _top

        arr[i].x = arr[i].x - _left
        arr[i].y = arr[i].y - _top
        arr[i].slted = false
      }
      var newObj = $.extend(
        true, {}, {
        id: new Date().getTime() + parseInt(Math.random() * 10000),
        ctLegendShow: 'true',
        x: _left,
        y: _top,
        width: _right - _left,
        height: _bottom - _top,
        zIndex: _index,
        sacleX: 1,
        sacleY: 1,
        // slted: false,
        child: []
      }, {}
      )
      newObj.slted = true
      this.combinList.push(newObj)
      this.combinList[this.combinList.length - 1].child = arr.concat()
      this.removeItem()

      this.selectedItem = this.combinList[this.combinList.length - 1]
      this.chooseCompIndexs = [this.combinList.length - 1] // 设置右边的值
      if (this.selectArea.choose) {
        this.selectArea.choose = false
        $('.tempDiv').remove()
      }
    },
    removeItem: function () {
      // 组合的元件从chartNum中删除
      var i = 0
      var tempArr = this.chooseIndexs.sort(function (a, b) {
        return a - b
      })
      tempArr.forEach(item => {
        // this.chartNum.splice(item, 1, {});
        this.chartNum.splice(item - i, 1)
        i++
      })
      this.chooseIndexs = []
      this.chooseItems = []
    },
    itemIndexAdd: function (index, zIndex) {
      // 组合元素的index， 该组合元素的zIndex
      this.chartNum.forEach((item) => {
        if (item.zIndex > zIndex) {
          item.zIndex++
        }
      })
      this.combinList.forEach((item, i) => {
        if (index !== i && item.zIndex > zIndex) {
          item.zIndex++
        }
      })
    },
    // 取消组合
    itemSplit: function () {
      var index = this.selectedIndex
      var tempArr = _.sortBy(this.combinList[index].child, function (o) { return o.zIndex })
      var zIndex = this.combinList[index].zIndex
      this.itemIndexAdd(index, zIndex)
      // 图层比他大的全部加1
      this.chooseIndexs = []
      tempArr.forEach((item, i) => {
        // item.x = parseInt((item.left + this.combinList[index].x) * this.combinList[index].sacleX)
        // item.y = parseInt((item.top + this.combinList[index].y) * this.combinList[index].sacleY)
        item.zIndex = zIndex + i
        item.x = item.x + this.combinList[index].x
        item.y = item.y + this.combinList[index].y
        item.id = item.id + parseInt(Math.random() * 1000) // 复制的元素id一样
        // item.x = item.left + this.combinList[index].x
        // item.y = item.top + this.combinList[index].y
        item.width = parseInt(item.width * this.combinList[index].sacleX)
        item.height = parseInt(item.height * this.combinList[index].sacleY)
        item.slted = true
        this.chartNum.push(item)
        this.chooseIndexs.push(this.chartNum.length - 1)
      })
      this.combinList.splice(index, 1)
      this.chooseCompIndexs = []
    },
    // 下移
    downward: function () {
      var z = this.selectedItem.zIndex || 500
      if (z > this.minIndex) {
        if (this.selectedItem.zIndex && this.selectedItem.zIndex === this.maxIndex) {
          this.maxIndex -= 1
        }
        this.$set(this.selectedItem, 'zIndex', z - 1)
        if (z - 1 < this.minIndex) {
          this.minIndex = z - 1
        }
      }
    },
    // 上移
    upward: function () {
      var z = this.selectedItem.zIndex || 500
      if (z < this.maxIndex) {
        if (this.selectedItem.zIndex && this.selectedItem.zIndex === this.minIndex) {
          this.minIndex += 1
        }
        this.$set(this.selectedItem, 'zIndex', z + 1)
        if (z + 1 > this.maxIndex) {
          this.maxIndex = z + 1
        }
      }
    },
    toBottom: function () {
      var z = this.selectedItem.zIndex || 500
      if (z > this.minIndex) {
        this.minIndex--
        this.$set(this.selectedItem, 'zIndex', this.minIndex)
      }
    },
    toTop: function () {
      var z = this.selectedItem.zIndex || 500
      if (z < this.maxIndex) {
        this.maxIndex++
        this.$set(this.selectedItem, 'zIndex', this.maxIndex)
      }
    },
    // 通过chooseIndex 获取 选中的元件集合
    indexToItem: function (type) {
      var arr = this.chooseIndexs
      var itemArr = []
      for (var i = 0, len = arr.length; i < len; i++) {
        itemArr.push(this.chartNum[arr[i]])
      }
      if (type !== 1) {
        var cArr = this.chooseCompIndexs
        for (var j = 0, _len = cArr.length; j < _len; j++) {
          itemArr.push(this.combinList[cArr[j]])
        }
      }
      return itemArr
    },
    // 左对齐
    alignLeft: function () {
      var itemArr = this.indexToItem()
      var minLeftItem = _.minBy(itemArr, function (item) { return item.x })
      var minLeft = minLeftItem.x
      for (let i = 0, len = itemArr.length; i < len; i++) {
        itemArr[i].x = minLeft
      }
    },
    // 右对齐
    alignRight: function () {
      var itemArr = this.indexToItem()
      var maxRightItem = _.maxBy(itemArr, function (item) { return item.x + item.width })
      var maxRight = maxRightItem.x + maxRightItem.width
      for (let i = 0, len = itemArr.length; i < len; i++) {
        itemArr[i].x = maxRight - itemArr[i].width
      }
    },
    // 上对齐
    alignTop: function () {
      var itemArr = this.indexToItem()
      var minTopItem = _.minBy(itemArr, function (item) { return item.y })
      var minTop = minTopItem.y
      for (let i = 0, len = itemArr.length; i < len; i++) {
        itemArr[i].y = minTop
      }
    },
    // 下对齐
    alignBottom: function () {
      var itemArr = this.indexToItem()
      var maxBottomItem = _.maxBy(itemArr, function (item) { return item.y + item.height })
      var maxBottom = maxBottomItem.y + maxBottomItem.height
      for (let i = 0, len = itemArr.length; i < len; i++) {
        itemArr[i].y = maxBottom - itemArr[i].height
      }
    },
    // 左右居中对齐
    textCenter: function () {
      // 同一中心纵轴
      var itemArr = this.indexToItem()
      var minLeftItem = _.minBy(itemArr, function (item) { return item.x }) // 左边界
      var maxRightItem = _.maxBy(itemArr, function (item) { return item.x + item.width })
      var maxRight = maxRightItem.x + maxRightItem.width // 右边界
      var mindItem = (maxRight - minLeftItem.x) / 2 + minLeftItem.x
      for (let i = 0, len = itemArr.length; i < len; i++) {
        itemArr[i].x = parseInt(mindItem - itemArr[i].width / 2)
      }
    },
    // 垂直居中对齐
    alignCenter: function () {
      // 同一中心横轴
      var itemArr = this.indexToItem()
      var minTopItem = _.minBy(itemArr, function (item) { return item.y }) // 上边界
      var maxBottomItem = _.maxBy(itemArr, function (item) { return item.y + item.height })
      var maxBottom = maxBottomItem.y + maxBottomItem.height // 下边界
      var mindItem = (maxBottom - minTopItem.y) / 2 + minTopItem.y
      for (let i = 0, len = itemArr.length; i < len; i++) {
        itemArr[i].y = parseInt(mindItem - itemArr[i].height / 2)
      }
    },
    // 水平分布
    levelDist: function () {
      var itemArr = this.indexToItem()
      if (itemArr.length <= 2) return // 至少三个元件才执行函数
      var minLeftItem = _.minBy(itemArr, function (item) { return item.x }) // 左边界
      var minLeft = minLeftItem.x
      var maxRightItem = _.maxBy(itemArr, function (item) { return item.x + item.width })
      var maxRight = maxRightItem.x + maxRightItem.width // 右边界
      var gapWidth = maxRight - minLeft // 水平分布区域的间距
      var allItemWid = _.sumBy(itemArr, function (item) { return item.width })
      var gap = parseInt((gapWidth - allItemWid) / (itemArr.length - 1))
      itemArr.sort(function (a, b) {
        var pre = a.x + a.width
        var next = b.x + b.width
        if (a.x === minLeft) {
          pre = 0 // 设置最小，则x最小的在第一个位置
        }
        return pre - next
      })
      for (let i = 1, len = itemArr.length; i < len - 1; i++) {
        itemArr[i].x = parseInt(itemArr[i - 1].x + itemArr[i - 1].width + gap)
      }
    },
    // 垂直分布
    verticalDist: function () {
      var itemArr = this.indexToItem()
      if (itemArr.length <= 2) return // 至少三个元件才执行函数
      var minTopItem = _.minBy(itemArr, function (item) { return item.y }) // 上边界
      var minTop = minTopItem.y
      var maxBottomItem = _.maxBy(itemArr, function (item) { return item.y + item.height })
      var maxBottom = maxBottomItem.y + maxBottomItem.height // 右边界
      var gapWidth = maxBottom - minTop // 垂直分布区域的间距
      var allItemWid = _.sumBy(itemArr, function (item) { return item.height })
      var gap = parseInt((gapWidth - allItemWid) / (itemArr.length - 1))
      itemArr.sort(function (a, b) {
        var pre = a.y + a.height
        var next = b.y + b.height
        if (a.y === minTop) {
          pre = 0 // 设置最小，则x最小的在第一个位置
        }
        return pre - next
      })
      for (let i = 1, len = itemArr.length; i < len - 1; i++) {
        itemArr[i].y = parseInt(itemArr[i - 1].y + itemArr[i - 1].height + gap)
      }
    },
    resized: function (item) {
      this.testObj.width = item.width
      this.testObj.height = item.height
      this.testObj.x = item.left
      this.testObj.y = item.top
      // 组合内部的元件的移动传递一个flag，区别在设置时位移量不能为-20
    },
    resetPaint () {
      this.paintInput.width = 1920
      this.paintInput.height = 1080
      this.paintObj.width = 1920
      this.paintObj.height = 1080
      this.paintObj.bgColor = ''
      this.paintObj.bgImg = ''
      this.paintObj.bgStyle = '3'
      this.paintObj.opacity = 100
      this.paintObj.showGrid = true
      this.paintObj.scale = 80
    },
    addColor (index) {
      // 添加颜色
      this.selectedItem.ctColors.splice(index, 0, ['#c23531', '#c23531'])
      // this.selectedItem.ctColors.push(['#c23531', '#c23531'])
    },
    delColor (index) {
      // 删除自定义颜色
      if (this.selectedItem.ctColors.length === 1) {
        if (gbs.inDev) {
          Notification({
            message: '至少配置一种颜色',
            position: 'bottom-right',
            customClass: 'toast toast-info'
          })
        } else {
          tooltip('', '至少配置一种颜色', 'info')
        }
        return
      }
      this.selectedItem.ctColors.splice(index, 1)
    },
    moveUp (index) {
      if (index !== 0) {
        var tempColor = this.selectedItem.ctColors.splice(index, 1)[0]
        this.selectedItem.ctColors.splice(index - 1, 0, tempColor)
      }
    },
    moveDown (index) {
      if (index !== this.selectedItem.ctColors.length - 1) {
        var tempColor = this.selectedItem.ctColors.splice(index, 1)[0]
        this.selectedItem.ctColors.splice(index + 1, 0, tempColor)
      }
    },
    chgDataSource: function ($event, flag) {
      // 改变数据来源
      $event &&
        this.selectedItem.ctDataSource === 'system' &&
        this.getUrlByType()
    },
    dealTypeStr: function () {
      // 根据需求传值给后端对应的接口
      var type = this.selectedItem.chartType
      if (type.indexOf('ve-') !== -1) {
        if (type === 've-gauge') {
          return 'progress'
        }
        if (this.selectedItem.subType) {
          return this.selectedItem.subType
        }
      }
      return type
    },
    getUrlByType (flag) {
      // 根据选中图标类型获取可以配置的接口
      var _this = this
      $.ajax({
        url: gbs.host + '/leaderview/home/getUrl',
        data: {
          typeStr: this.dealTypeStr()
        },
        async: false,
        success: function (data) {
          _this.$set(_this.syst, 'urlSel', data.obj || [])
          _this.$nextTick(function () {
            _this.syst.urlSel.length && _this.chgUrl(flag)
          })
        },
        error: function () {
          if (gbs.inDev) {
            Notification({
              message: '连接错误！',
              position: 'bottom-right',
              customClass: 'toast toast-error'
            })
          } else {
            tooltip('', '连接错误！', 'error')
          }
        }
      })
    },
    // 改变接口下拉框，需要根据index更新当前选中接口数据,及下面的参数
    chgUrl (flag) {
      var _this = this
      var chainP = (this.syst.chainParams = {}) // 将需要联动的字段缓存
      var index = -1
      this.showWindowBtn = false
      if (typeof flag === 'boolean') {
        var selectedP = (this.syst.curConf.params = $.extend(
          true, {},
          this.selectedItem.params
        ))
        index = _.findIndex(this.syst.urlSel, function (o) {
          return o.url === _this.selectedItem.url
        })
      } else {
        selectedP = this.syst.curConf.params = {}
      }

      var first = $.isEmptyObject(selectedP)

      if (index === -1) {
        if (this.selectedItem.url || typeof flag !== 'boolean') {
          index =
            this.$refs.urlSel.selectedIndex === -1
              ? 0
              : this.$refs.urlSel.selectedIndex
        } else {
          index = 0
        }
      }
      var urlsel = this.syst.urlSel[index]
      this.syst.curConf.url = urlsel.url
      this.syst.curConf.method = urlsel.method

      this.syst.curUrl = []
      this.$nextTick(function () {
        var api = (_this.syst.curUrl = urlsel.params)
        var reg = /^\//
        $.each(api, function (i, d) {
          if (d.dataType === 'remote') {
            // 需要通过请求拿数据
            var postData = {}
            if (d.params) {
              $.each(d.params, function (j, o) {
                postData[o] = selectedP[o] || ''
                if (_this.isArray(postData[o])) {
                  postData[o] = postData[o].join(',') // 数组形式转化为，分割的字符串传给后端
                }
              })
              chainP[d.key] = d
            }
            if (selectedP.windows) {
              _this.showWindowBtn = true
              _this.syst.windowData = JSON.parse(selectedP.windows)
            }
            $.ajax({
              url: reg.test(d.dataUrl) ? gbs.host + d.dataUrl : gbs.host + '/' + d.dataUrl,
              async: false,
              data: postData,
              type: d.method || 'get',
              success: function (data) {
                d.data = data.obj || []
                if (_this.isArray(data.obj) && data.obj.length > 0) {
                  if (data.obj[0].hasOwnProperty('ne') && data.obj[0].hasOwnProperty('fields')) {
                    _this.syst.windowObj = data.obj
                    if (!_this.isArray(_this.syst.windowData) || _this.syst.windowData.length < 1) {
                      _this.syst.windowData = _this.initWindowData(data.obj)
                      // 初始化弹窗赋值并展示
                    }
                    _this.showWindowBtn = true
                  }
                }
                $.isEmptyObject(selectedP) && _this.setFirstV(d)
              },
              error: function () {
                if (gbs.inDev) {
                  Notification({
                    message: '连接错误！',
                    position: 'bottom-right',
                    customClass: 'toast toast-error'
                  })
                } else {
                  tooltip('', '连接错误！', 'error')
                }
              }
            })
          }
          first && _this.setFirstV(d)
        })
        _this.syst.curUrl = api
      })
    },
    setFirstV: function (d) {
      this.syst.curConf.params[d.key] = !d.notNull
        ? null
        : (d.data.length && d.data[0].value) || null
    },
    chgSelects (v) {
      // 需要判断是否有改变联动下拉框的值，需要重新请求
      var _this = this
      var cur = this.syst.curConf.params
      var chaip = this.syst.chainParams
      var postData = {}
      var reg = /^\//
      $.each(chaip, function (i, d) {
        var index = d.params.indexOf(v.key)
        if (index !== -1) {
          var flag = false
          var grp = $('#mainSystemConf')
            .find('[name="' + i + '"]')
            .closest('.form-group')
          $.each(d.params, function (j, o) {
            if (_this.isArray(cur[o])) {
              postData[o] = cur[o].join(',')
            } else {
              postData[o] = cur[o]
            }
            if (chaip[o] && chaip[o].params && v.key !== chaip[o].key) {
              // 避免重复请求
              flag = chaip[o].params.indexOf(v.key) !== -1
            }
          })
          if (flag) {
            return false
          }
          $.ajax({
            url: reg.test(d.dataUrl) ? gbs.host + d.dataUrl : gbs.host + '/' + d.dataUrl,
            async: false,
            data: postData,
            success: function (data) {
              data.obj = data.obj || []
              d.data.splice(0, d.data.length)
              d.data = data.obj
              // 判断是否是获取的多资源的部件和属性
              _this.getWindowObj(data)
              //  console.log(v.key,d.dataUrl,postData);
            }
          })
          _this.$set(_this.syst.curUrl, grp.index(), $.extend(true, {}, d))
        }
      })
    },
    getWindowObj (data) {
      this.showWindowBtn = false
      if (this.isArray(data.obj) && data.obj.length > 0) {
        if (data.obj[0].hasOwnProperty('ne') && data.obj[0].hasOwnProperty('fields')) {
          this.syst.windowObj = data.obj
          this.syst.windowData = []
          this.syst.windowData = this.initWindowData(data.obj)
          this.syst.curConf.params.windows = JSON.stringify(this.syst.windowData)
          this.showWindowBtn = true
        }
      }
    },
    initWindowData (data) {
      var obj = []
      data.forEach((item, index) => {
        obj.push({
          'indicator': item.indicator.value,
          'fields': item.fields[0].value,
          'ne': []
        })
        item.ne.forEach((list) => {
          obj[index].ne.push({
            'id': list.id,
            'component': list.component.length > 0 ? list.component[0].value : null
          })
        })
      })
      return obj
    },
    getWindowData () {
      $('#partsEdit-modal').modal('show')
    },
    getUrlData () {
      // 根据接口获取数据更新图表
      var _this = this
      var curConf = this.syst.curConf
      var param = curConf.params
      if (this.showWindowBtn && param.hasOwnProperty('windows')) {
        param.windows = JSON.stringify(this.syst.windowData) // 保存弹窗填写的数据
      }
      if (this.selectedItem.chartType === 'topo') {
        this.saveTopoConf(param)
        return
      }
      var datas = {}
      var reg = /^\//
      $.each(param, function (i, d) {
        datas[i] = $.isArray(d) ? d.join(',') : d
      })
      // if (_this.syst.windowData.length > 0) {
      //   datas.windows = JSON.stringify(_this.syst.windowData)
      // }
      $.ajax({
        url: reg.test(curConf.url) ? gbs.host + curConf.url : gbs.host + '/' + curConf.url,
        data: datas,
        type: curConf.method,
        success: function (data) {
          data.obj = data.obj || {}
          if (data.obj.colors) {
            _this.selectedItem.ctColors = data.obj.colors
            _this.selectedItem.colorType = 'defalut'
          }
          _this.selectedItem.chartData = data.obj
          _this.selectedItem.url = curConf.url
          _this.selectedItem.method = curConf.method
          _this.selectedItem.params = param
        },
        error: function () {
          if (gbs.inDev) {
            Notification({
              message: '连接错误！',
              position: 'bottom-right',
              customClass: 'toast toast-error'
            })
          } else {
            tooltip('', '连接错误！', 'error')
          }
        }
      })
    },
    saveTopoConf: function (param) {
      // 拓扑与其他组件不同，需要特殊处理
      this.selectedItem.tpId = param.topoId
    },
    // 以下为静态数据的输入校验
    formatJson (a) {
      if (a === null) return null
      if (typeof a === 'string' && (a = eval('(' + a + ')'))) {
        return this._format(a, a, null, null, null)
      }
    },
    _format: function (a, c, d, b, e) {
      d || (d = '')
      var isObj = typeof c === 'object' && Object.prototype.toString.call(c).toLowerCase() === '[object object]'
      if (isObj) {
        if (c.$ref) {
          var g = c.$ref
          g.indexOf('$.') === 0 && (b[e] = this._getJsonValue(a, g.substring(2)))
          return
        }
        for (var f in c) {
          b = d
          b !== '' && (b += '.')
          g = c[f]
          b += f
          this._format(a, g, b, c, f)
        }
      } else if (this.isArray(c)) {
        for (f in c) {
          b = d, g = c[f], b = b + '[' + f + ']', this._format(a, g, b, c, f)
        }
      }
      return a
    },
    _getJsonValue: function (a, c) {
      var d = 'randomId_' + parseInt(1E9 * Math.random()),
        b
      b = '' + ('function ' + d + '(root){') + ('return root.' + c + '')
      b += '}'
      b += ''
      var e = document.createElement('script')
      e.id = d
      e.text = b
      document.body.appendChild(e)
      d = window[d](a)
      e.parentNode.removeChild(e)
      return d
    },
    isArray: function (a) {
      return typeof a === 'object' && Object.prototype.toString.call(a).toLowerCase() === '[object array]'
    },
    // isObject: function(a) {
    //   return 'object' === typeof a && '[object object]' === Object.prototype.toString.call(a).toLowerCase()
    // },
    dataChange () {
      if (this.selectedItem.ctDataSource === 'system') {
        this.getUrlData()
      } else if (this.selectedItem.chartType === 'v-map') {
        this.mapDataToChart()
        this.selectedItem.piecesData = JSON.parse(JSON.stringify(this.editPieces))
      } else if (this.selectedItem.chartType === 'v-scatter') {
        this.selectedItem.chartData = JSON.parse(JSON.stringify(this.alertMapData))
      } else {
        var textData = this.$refs.textarea.innerText
        var reg = /^\{[\s\S]*\}$/
        // 先判断是{}类型的对象，而不是new Object
        if (reg.test(textData.trim())) {
          try {
            this.selectedItem.chartData = this.formatJson(textData)
          } catch (err) {
            if (gbs.inDev) {
              Notification({
                message: '请输入正确的JSON格式的数据',
                position: 'bottom-right',
                customClass: 'toast toast-info'
              })
            } else {
              tooltip('', '请输入正确的JSON格式的数据', 'info')
            }
          }
        } else {
          if (gbs.inDev) {
            Notification({
              message: '请输入正确的JSON格式的数据',
              position: 'bottom-right',
              customClass: 'toast toast-info'
            })
          } else {
            tooltip('', '请输入正确的JSON格式的数据', 'info')
          }
        }
      }
    },
    saveConf: function (event, cb) {
      // 保存
      if (!(!this.widthVali.isShowError &&
        !this.heightVali.isShowError &&
        !this.xVali.isShowError &&
        !this.yVali.isShowError && !this.proHeightErr && !this.radiusErr
      )) {
        if (gbs.inDev) {
          Notification({
            message: '请填写正确的配置信息',
            position: 'bottom-right',
            customClass: 'toast toast-info'
          })
        } else {
          tooltip('', '请填写正确的配置信息', 'info')
        }
        return
      }
      $('#screen').show()
      var cThis = this
      // cThis.selectedItem.slted = false
      this.clickPaint() // 取消所有的选中
      this.selectedItem = {}
      var canvas2 = document.createElement('canvas')
      var _canvas = document.querySelector('.m-main .paint-bg')
      $('#chooseWrap').removeClass('gridBg')
      // _canvas.style.background = $('body').css('background')
      // document.querySelector('.m-main #chooseWrap').style.background = $('body').css('background')

      // 将canvas画布放大若干倍，然后盛放在较小的容器内，就显得不模糊了
      // canvas2.width = baseData.home.w
      // canvas2.height = baseData.home.h
      canvas2.width = this.paintObj.width * (this.paintObj.scale / 100)
      canvas2.height = this.paintObj.height * (this.paintObj.scale / 100)
      canvas2.getContext('2d')
      $('#mainEdit-edit .main_topo')
        .find('svg')
        .css('opacity', 0)
      $('#mainEdit-edit .main_topo').append(
        $('<img>')
          .addClass('monitp')
          .attr('src', window.location.host + '/resources/img/topo/tpstander.png')
          .css({
            width: '100%',
            height: '100%',
            position: 'absolute',
            left: 0
          })
      )
      html2canvas(_canvas, {
        width: this.paintObj.width * (cThis.paintObj.scale / 100),
        height: this.paintObj.height * (cThis.paintObj.scale / 100),
        logging: false,
        scale: 0.4,
        backgroundColor: 'transparent', // 设置背景透明
        canvas: canvas2,
        onclone: function (doc) {
          // 提前还原拓扑
          $('#mainEdit-edit .main_topo')
            .find('svg')
            .css('opacity', 1)
          $('#mainEdit-edit .monitp').remove()
        }
      }).then(function (canvas) {
        // $('#mainEdit-edit .monitp').remove()
        document.body.appendChild(canvas)
        var dataUrl = canvas.toDataURL('image/png')
        var arr = dataUrl.split(',')
        var mime = arr[0].match(/:(.*?);/)[1]
        var suffix = mime.split('/')[1]
        var bstr = atob(arr[1])
        var n = bstr.length
        var u8arr = new Uint8Array(n)
        var filename = 'page' + cThis.pageId
        while (n--) {
          u8arr[n] = bstr.charCodeAt(n)
        }
        var file = new File([u8arr], `${filename}.${suffix}`, { type: mime })
        var formdata = new FormData()
        formdata.append('uploaded_file', file)
        canvas.remove()
        cThis.uploadFile(formdata, function (data) {
          var _data = {
            id: cThis.pageId,
            viewConf: JSON.stringify(cThis.chartNum),
            paintObj: JSON.stringify(cThis.paintObj),
            composeObj: JSON.stringify(cThis.combinList),
            viewImage: '/leaderview/home/getImg/' + data.obj.isCustom + '/' + data.obj.id
          }
          cThis
            .axios({
              method: 'post',
              url: '/leaderview/home/homePage',
              data: qs.stringify(_data),
              headers: { 'content-type': 'application/x-www-form-urlencoded' }
            })
            .then(res => {
              canvas.remove() // 这里是作为回调函数，canvas没有传过去
              _canvas.style.background = 'transparent'
              if (cThis.paintObj.showGrid) {
                $('#chooseWrap').addClass('gridBg')
              }
              // $('#screen').hide()
              typeof cb === 'function' && cb()
              if (res.success) {
                if (gbs.inDev) {
                  Notification({
                    message: '操作成功！',
                    position: 'bottom-right',
                    customClass: 'toast toast-success'
                  })
                } else {
                  tooltip('', '操作成功！', 'success')
                }
              }
            })
        })
      })
    },
    uploadFile: function (formData, cb) {
      $.ajax({
        url: gbs.host + '/leaderview/home/file/upload',
        type: 'post',
        data: formData,
        async: false,
        cache: false,
        processData: false,
        contentType: false,
        success: function (data) {
          if (data.success) {
            // var _url = '/home/getImg/' + data.obj.isCustom + '/' + data.obj.id
            typeof cb === 'function' && cb(data)
          } else {
            // $("#screen").hide()
            if (gbs.inDev) {
              Notification({
                message: data.msg,
                position: 'bottom-right',
                customClass: 'toast toast-error'
              })
            } else {
              tooltip('', data.msg, 'error')
            }
          }
        }
      })
    },
    preview: function () {
      // 预览
      if (!(!this.widthVali.isShowError &&
        !this.heightVali.isShowError &&
        !this.xVali.isShowError &&
        !this.yVali.isShowError
      )) {
        if (gbs.inDev) {
          Notification({
            message: '请填写正确的配置信息',
            position: 'bottom-right',
            customClass: 'toast toast-info'
          })
        } else {
          tooltip('', '请填写正确的配置信息', 'info')
        }
        return
      }
      this.pageData = JSON.stringify(this.chartNum)
      this.composeData = JSON.stringify(this.combinList)
      this.viewKey = new Date().getTime() + parseInt(Math.random() * 10)
      this.$nextTick(() => {
        this.viewPage = true
      })
    },
    hidePreview: function () {
      this.viewPage = false
    },
    preBack: function () {
      this.showBackModal = true
    },
    back: function (data) {
      this.showBackModal = false
      if (data && data.sure === '1') {
        this.$router.push('/editPage')
      }
    },
    /* 统一右键 */
    context: function (index, ev, type) {
      // if (type !=='compose') {

      // }
      console.log(this.chooseIndexs)
      console.log(this.chooseCompIndexs)
      console.log('childResize:' + this.childResize)
      $(this.$refs.contextMenu)
        .css({
          left: ev.pageX,
          top: ev.pageY
        })
        .toggle(true)
      this.selectedIndex = index
    },
    // 组合右键 未使用该方法
    composeMenu: function (index, ev) {
      // console.log(ev);
      $(this.$refs.contextMenu)
        .css({
          left: ev.pageX,
          top: ev.pageY
        })
        .toggle(true)
      this.selectedIndex = -1
      this.cancelSelected()
    },
    // 框选
    chooseMap: function () {
      var _this = this
      console.log('注册框选事件')
      var stateBar = document.getElementById('chooseWrap')
      // var posLeft = stateBar.clientLeft
      // var posTop = stateBar.clientTop
      var posLeft = 210 // 左侧宽度
      var posTop = 38 // 顶部banner的高度
      stateBar.addEventListener('mousedown', _this.userChoose)
      /*     var _this = this
        console.log('注册框选事件')
        var stateBar = document.getElementById('chooseWrap')
        var wrap = document.getElementsByClassName('m-main')[0]
        var posLeft = stateBar.clientLeft
        var posTop = stateBar.clientTop
        // var posLeft = 210 // 左侧宽度
        // var posTop = 38 // 顶部banner的高度
        stateBar.addEventListener('mousedown', function (e) {
          _this.cancelSelected()
          var posx = e.clientX - posLeft
          var posy = e.clientY - posTop
          var div = document.createElement('div')
          div.className = 'tempDiv'
          div.style.left = e.clientX - posLeft + 'px'
          div.style.top = e.clientY - posTop + 'px'
          stateBar.onmousemove = function (ev) {
            if ($('.tempDiv').length > 0) {
              $('.tempDiv').remove()
            }
            stateBar.appendChild(div)
            _this.selectArea.choose = false
            div.style.left = Math.min(ev.clientX - posLeft, posx) + 'px'
            div.style.top = Math.min(ev.clientY - posTop, posy) + 'px'
            div.style.width = Math.abs(posx - (ev.clientX - posLeft)) + 'px'
            div.style.height = Math.abs(posy - (ev.clientY - posTop)) + 'px'
            // console.log('MouseX: ' + (ev.clientX - posLeft) + '<br/>MouseY: ' + (ev.clientY - posTop))
          }
          stateBar.onmouseup = function () {
            // div.parentNode.removeChild(div)
            div.addEventListener('contextmenu', function (ee) {
              _this.getChooseItems(div.style.left, div.style.top, div.style.width, div.style.height)
              $(_this.$refs.contextMenu)
                .css({
                  left: ee.pageX,
                  top: ee.pageY
                })
                .toggle(true)
              ee.preventDefault()
            })
            stateBar.onmousemove = null
            stateBar.onmouseup = null
          }
        }, false) */
    },
    getChooseItems: function (left, top, width, height) {
      var bottom = top + height
      var right = left + width
      this.chooseIndexs = []
      this.chooseItems = [] // 选中的单个元件集合
      this.chooseCompIndexs = [] // 选中的组合元件集合
      this.chartNum.forEach((item, index) => {
        if (this.itemInChoose(left, right, top, bottom, item)) {
          this.chooseIndexs.push(index)
          this.chooseItems.push(item)
          this.chartNum[index].slted = true
        }
      })
      this.combinList.forEach((item, index) => {
        if (this.itemInChoose(left, right, top, bottom, item)) {
          this.chooseCompIndexs.push(index)
          this.combinList[index].slted = true
        }
      })
      this.updateMinXitem()
    },
    itemInChoose: function (left, right, top, bottom, item) {
      // 判断是否在框选区域内
      if (item.x < left || item.y < top || item.x + item.width > right || item.y + item.height > bottom) {
        return false
      }
      return true
    },
    userChoose: function (e) {
      var _this = this
      var stateBar = document.getElementById('chooseWrap')
      // _this.cancelSelected()
      e = e || window.event
      // 获取鼠标在整个页面的位置
      var posx = e.offsetX
      var posy = e.offsetY
      var div = document.createElement('div')
      // div.className = 'tempDiv'
      div.style.left = posx + 'px'
      div.style.top = posy + 'px'

      stateBar.onmousemove = function (ev) {
        ev = ev || window.event
        // 获取盒子在整个页面的位置
        var clientX = ev.offsetX
        var clientY = ev.offsetY

        div.style.left = Math.min(clientX, posx) + 'px'
        div.style.top = Math.min(clientY, posy) + 'px'
        div.style.width = Math.abs(posx - clientX) + 'px'
        div.style.height = Math.abs(posy - clientY) + 'px'
        if (parseInt(div.style.width) > 10 && parseInt(div.style.height) > 10) {
          $('#inWrap').show()
          var inWrap = document.getElementById('inWrap')
          _this.chooseStart.posX = posx
          _this.chooseStart.posY = posy
          inWrap.addEventListener('mousemove', _this.wrapChoose)
          stateBar.onmousemove = null
          stateBar.onmouseup = null
          // return
        }
        // console.log('MouseX: ' + (ev.clientX - posLeft) + '<br/>MouseY: ' + (ev.clientY - posTop))
      }
      stateBar.onmouseup = function () {
        var tempDiv = document.getElementsByClassName('tempDiv')[0]
        if (tempDiv) {
          setTimeout(() => {
            _this.getChooseItems(parseInt(tempDiv.style.left), parseInt(tempDiv.style.top), parseInt(tempDiv.style.width), parseInt(tempDiv.style.height))
          }, 0)
        }
        div.addEventListener('contextmenu', function (ee) {
          // _this.getChooseItems(parseInt(div.style.left), parseInt(div.style.top), parseInt(div.style.width), parseInt(div.style.height))
          if (_this.chooseCompIndexs.length + _this.chooseIndexs.length > 0) {
            $(_this.$refs.contextMenu)
              .css({
                left: ee.pageX,
                top: ee.pageY
              })
              .toggle(true)
          }
          ee.preventDefault()
        })
        stateBar.onmousemove = null
        stateBar.onmouseup = null
      }
    },
    wrapChoose: function (ev) {
      var _this = this
      var stateBar = document.getElementById('inWrap')
      // 获取鼠标起点位置，并绘制div起点
      var posx = this.chooseStart.posX
      var posy = this.chooseStart.posY
      var div = document.createElement('div')
      div.className = 'tempDiv'
      div.style.left = posx + 'px'
      div.style.top = posy + 'px'

      ev = ev || window.event
      // 获取盒子在整个页面的位置
      var clientX = ev.offsetX
      var clientY = ev.offsetY
      div.style.left = Math.min(clientX, posx) + 'px'
      div.style.top = Math.min(clientY, posy) + 'px'
      div.style.width = Math.abs(posx - clientX) + 'px'
      div.style.height = Math.abs(posy - clientY) + 'px'
      stateBar.appendChild(div)
      if ($('.tempDiv').length > 1) {
        $('.tempDiv').eq(0).remove()
      }
      _this.selectArea.choose = false

      stateBar.onmouseup = function () {
        var tempDiv = document.getElementsByClassName('tempDiv')[0]
        if (tempDiv) {
          setTimeout(() => {
            _this.getChooseItems(parseInt(tempDiv.style.left), parseInt(tempDiv.style.top), parseInt(tempDiv.style.width), parseInt(tempDiv.style.height))
            $('.tempDiv').remove()
            $('#inWrap').empty()
            $('#inWrap').hide()
            stateBar.removeEventListener('mousemove', _this.wrapChoose)
          }, 0)
        }
        // div.addEventListener('contextmenu', function (ee) {
        //   if (_this.chooseCompIndexs.length + _this.chooseIndexs.length > 0) {
        //     $(_this.$refs.contextMenu)
        //       .css({
        //         left: ee.pageX,
        //         top: ee.pageY
        //       })
        //       .toggle(true)
        //   }
        //   ee.preventDefault()
        // })
        stateBar.onmousemove = null
        stateBar.onmouseup = null
      }
    },
    del: function () {
      if (this.chooseIndexs.length === 0 && this.chooseCompIndexs.length === 0) {
        return
      }
      if (this.chooseIndexs.length > 0) {
        this.deleteOne('item', this.chooseIndexs)
        this.chooseIndexs = []
      }
      if (this.chooseCompIndexs.length > 0) {
        this.deleteOne('compose', this.chooseCompIndexs)
        this.chooseCompIndexs = []
      }
      this.selectedItem = {}
      this.selectedIndex = null

      if (this.selectArea.choose) {
        this.selectArea.choose = false
        $('.tempDiv').remove()
      }
    },
    deleteOne: function (type, tempArr) {
      var i = 0
      tempArr = tempArr.sort(function (a, b) {
        return a - b
      })
      if (type === 'item') {
        var _type = 'chartNum'
      } else {
        _type = 'combinList'
      }
      tempArr.forEach(item => {
        this[_type].splice(item - i, 1)
        i++
      })
    },
    /* delOne: function () {
      if (this.selectedItem.hasOwnProperty('chartType')) {
        // 非组合元件
        this.chartNum.splice(this.selectedIndex, 1)
      } else {
        this.combinList.splice(this.selectedIndex, 1)
      }
      this.selectedItem = {}
      this.selectedIndex = null
    }, */
    copy: function () {
      if (this.chooseIndexs.length > 0) {
        this.copyOne('item', this.chooseIndexs)
      }
      if (this.chooseCompIndexs.length > 0) {
        this.copyOne('compose', this.chooseCompIndexs)
      }
      if (this.selectArea.choose) {
        this.selectArea.choose = false
        $('.tempDiv').remove()
      }
    },
    copyOne: function (type, arr) {
      // 单个元件的复制操作
      if (type === 'item') {
        var _type = 'chartNum'
        this.chooseIndexs = []
      } else {
        _type = 'combinList'
        this.chooseCompIndexs = []
      }
      for (let i = 0, len = arr.length; i < len; i++) {
        this[_type][arr[i]].slted = false
        let tempItem = JSON.parse(JSON.stringify(this[_type][arr[i]]))
        tempItem.x += 20
        tempItem.y += 20
        tempItem.slted = true // 复制的元件默认选中
        tempItem.id = new Date().getTime() + parseInt(Math.random() * 10000)
        this[_type].push(tempItem)
        if (type === 'item') {
          this.chooseIndexs.push(this[_type].length - 1)
        } else {
          this.chooseCompIndexs.push(this[_type].length - 1)
        }
      }
      this.updateMinXitem()
    },
    hideContext: function () {
      $(this.$refs.contextMenu).toggle(false)
    },
    /* 图片 */
    changeImg: function (e) {
      if (e.value === '') {
        return
      }
      if (e.target.files[0].size > 15 * 1024 * 1024) {
        if (gbs.inDev) {
          Notification({
            message: '上传的文件不能大于15MB',
            position: 'bottom-right',
            customClass: 'toast toast-info'
          })
        } else {
          tooltip('', '上传的文件不能大于15MB', 'info')
        }
        return
      }
      var _this = this
      var formData = new FormData()
      formData.append('uploaded_file', e.target.files[0])
      this.uploadFile(formData, function (data) {
        if (!_this.selectedItem.chartType) {
          // 上传画布图片
          _this.paintObj.bgImg =
            '/home/getImg/' + data.obj.isCustom + '/' + data.obj.id
          return
        }
        if (_this.selectedItem.chartType === 'image') {
          _this.selectedItem.imgSrc =
            '/leaderview/home/getImg/' + data.obj.isCustom + '/' + data.obj.id
        } else if (_this.selectedItem.subType === 'pictorialBar') {
          _this.selectedItem.symbolImg =
            '/leaderview/home/getImg/' + data.obj.isCustom + '/' + data.obj.id
        }
      })
      e.target.value = ''
    },
    getPaintCl (data) {
      this.paintObj.bgColor = data.color
    },
    getColor (data) {
      if (data.type !== undefined) {
        this.selectedItem[data.type] = data.color
      } else {
        // 用来解决不能监听直接赋值的数组变化
        this.selectedItem.ctColors.splice(data.index, 1)
        this.selectedItem.ctColors.splice(data.index, 0, data.color)
      }
    },
    getMapColor (data) {
      if (data.type !== undefined) {
        this.selectedItem[data.type] = data.color
      } else {
        // var oldColor = this.selectedItem.ctColors[data.index]
        // oldColor = data.color
        this.selectedItem.ctColors.splice(data.index, 1, data.color)
      }
    },
    getColorStart (data) {
      if (data.type !== undefined) {
        this.selectedItem[data.type][0] = data.color
      } else {
        var oldColor = this.selectedItem.ctColors[data.index]
        oldColor[0] = data.color
        this.selectedItem.ctColors.splice(data.index, 1, oldColor)
      }
    },
    // 渐变色颜色改变
    getGradColor (data) {
      if (data.type !== undefined) {
        this.selectedItem[data.type][1] = data.color
      } else {
        var oldColor = this.selectedItem.ctColors[data.index]
        oldColor[1] = data.color
        this.selectedItem.ctColors.splice(data.index, 1, oldColor)
      }
    },
    testObjChange (direct, newValue) {
      var defData = 0
      var isWidth = direct === 'width'
      var valiType = direct + 'Vali'
      var allowOverflow = this.childResize ? 0 : baseData.allowOverflow
      defData = isWidth ? this.paintObj.width : this.paintObj.height
      var selectData = isWidth ? this.selectedItem.x : this.selectedItem.y // 选中元素的x,y
      var limitValue = defData - selectData + allowOverflow // 可设置的最大值
      // not Number
      // if (Number(newValue) < this.miniW) {
      //   this.selectedItem[direct] = this.miniW
      // } else {
      //   this.selectedItem[direct] = Number(this.selectedItem[direct])
      // }
      if (newValue < this.miniW || newValue < -allowOverflow || newValue > limitValue) {
        this[valiType].isShowError = true
        this[valiType].errorMsg = isWidth
          ? '宽度范围为' + this.miniW + '~' + (limitValue)
          : '高度范围为' + this.miniW + '~' + (limitValue)
        if (newValue > limitValue) {
          this.testObj[direct] = limitValue
        }
      } else {
        this[valiType].isShowError = false
        this.selectedItem[direct] = newValue
      }
    },
    testObjPosChange (position, newValue) {
      var allowOverflow = this.childResize ? 0 : baseData.allowOverflow
      var defData = 0
      var selectData = 0
      var isX = position === 'x'
      var valiType = position + 'Vali'
      if (this.childResize) {
        var compId = this.parentId
        defData = isX ? this.combinList[compId].width : this.combinList[compId].height // 父元素
      } else {
        // defData = isX ? baseData.home.w : baseData.home.h // 画布的宽高
        defData = isX ? this.paintObj.width : this.paintObj.height // 画布的宽高
      }
      selectData = isX ? this.selectedItem.width : this.selectedItem.height // 选中元素的宽高
      if (selectData > defData) {
        // 这里暂不需要判断宽高的限制
        // this[valiType].isShowError = true
        // // this[valiType].errorMsg = isX
        // //   ? '横轴位置范围为0-' + defData
        // //   : '纵轴位置范围为0-' + defData
        // // if (newValue > defData) {
        // //   this.testObj[position] = defData
        // // }
        // var limitValue = defData + allowOverflow // 可设置的最大值
        // this[valiType].errorMsg = isX
        //   ? '宽度范围为' + this.miniW + '~' + (limitValue)
        //   : '高度范围为' + this.miniW + '~' + (limitValue)
      } else {
        var limitValue = defData - selectData + allowOverflow // 可设置的最大值
        if (newValue < -allowOverflow || newValue > limitValue) {
          this[valiType].isShowError = true
          this[valiType].errorMsg = isX
            ? '横轴位置范围为' + -allowOverflow + '~' + (limitValue)
            : '纵轴位置范围为' + -allowOverflow + '~' + (limitValue)
          if (newValue > limitValue) {
            this.testObj[position] = limitValue
          }
        } else {
          this[valiType].isShowError = false
          this.selectedItem[position] = newValue
        }
      }
      // not Number
      this.selectedItem[position] = Number(this.selectedItem[position])
    },
    // 改变画布大小
    changePaintSize (type) {
      var key = 'width'
      if (type === 'h') {
        key = 'height'
      }
      if (this.paintInput[key] < 500 || this.paintInput[key] > 10000) {
        this.paintInput[key] = this.paintObj[key]
      } else {
        this.paintObj[key] = Number(this.paintInput[key])
      }
      this.changeHomeData(this.paintObj)
    },
    // 这里来处理快捷键
    handleKeyDown (e) {
      var key = window.event.keyCode ? window.event.keyCode : window.event.which
      /***
        delete：46
        上键：38
        下键：40
        左键：37
        右键：39
        crtl: 17
      */
      if (key === 46) {
        this.del()
        e.preventDefault() // 取消浏览器原有的操作
      }
      if (key === 38) {
        if ($(':focus').length === 0) {
          this.scrollTop(-1) // 上移
          e.preventDefault()
        }
      }
      if (key === 40) {
        if ($(':focus').length === 0) {
          this.scrollTop(1) // 下移
          e.preventDefault()
        }
      }
      if (key === 37) {
        if ($(':focus').length === 0) {
          this.scrollLeft(-1) // 左移
          e.preventDefault()
        }
      }
      if (key === 39) {
        if ($(':focus').length === 0) {
          this.scrollLeft(1) // 右移
          e.preventDefault()
        }
      }
      if (key === 113) {
        // F2
        if (window.event.ctrlKey && this.paintObj.scale >= 25) {
          this.paintObj.scale -= 5
        }
      }
      if (key === 114) {
        // F3
        if (window.event.ctrlKey && this.paintObj.scale <= 195) {
          this.paintObj.scale += 5
        }
      }
    },
    handleKeyUp (e) {
      var key = window.event.keyCode ? window.event.keyCode : window.event.which
      if (key === 13) {
        // flag = true
        e.preventDefault()
      }
    },
    onMouseScroll (e) {
      if (window.event.ctrlKey) {
        e.preventDefault()
        var wheel = e.originalEvent.wheelDelta || -e.originalEvent.detail
        var delta = Math.max(-1, Math.min(1, wheel))
        if (delta < 0) { // 向下滚动
          console.log('向下滚动')
        } else { // 向上滚动
          console.log('向上滚动')
        }
      }
    },
    scrollFunc (e) {
      e = e || window.event
      e.preventDefault()
      if (e.wheelDelta && event.ctrlKey) { // IE/Opera/Chrome
        event.returnValue = false
      } else if (e.detail) { // Firefox
        event.returnValue = false
      }
    }
  },
  watch: {
    'selectedItem.mapLevel': function (newValue, oldV) {
      if (!this.selfMapLevel) {
        console.log('切换元件导致的mapLevel更改') // 不同地图元件的改变不触发watch
        return
      }
      if (oldV) {
        this.selectMapData = {}
      }
      var _this = this
      if (newValue === 'country') {
        this.areaArr = this.provinceArr
        if (this.selectedItem.chartType === 'v-scatter') {
          this.clearAlertMap()
        }
        if (this.selectedItem.chartType === 'v-map') {
          this.initLevelData()
        }
      } else if (newValue === 'city') {
        var noMapArr = ['110000', '310000', '500000', '120000', '710000', '810000', '820000'] // 直辖市自治区没有三级地图
        if (oldV === 'country') {
          console.log('从国家级到市级') // 从国家级到市级
          if (!this.selectedItem.provinceCode || noMapArr.indexOf(this.selectedItem.provinceCode) !== -1) {
            this.selectedItem.provinceCode = 510000 // 默认选中一个位置
          }
        }
        if (noMapArr.indexOf(this.selectedItem.provinceCode) !== -1) {
          this.selectedItem.mapLevel = oldV
          if (gbs.inDev) {
            Notification({
              message: '该地区不支持第三级地图',
              position: 'bottom-right',
              customClass: 'toast toast-info'
            })
          } else {
            tooltip('', '该地区不支持第三级地图', 'info')
          }
        } else {
          this.getMapData(this.selectedItem.provinceCode).then((data) => {
            _this.cityArr = data
            // if (!_this.selectedItem.cityCode) {
            _this.selectedItem.cityCode = data[0].value
            // }
          })
        }
      } else {
        if (newValue === 'province') {
          if (!_this.selectedItem.provinceCode) {
            _this.selectedItem.provinceCode = _this.provinceArr[0].value
            _this.getMapData(_this.selectedItem.provinceCode).then((data) => {
              _this.cityArr = data
              _this.areaArr = data
              if (this.selectedItem.chartType === 'v-map') {
                this.initLevelData()
              }
              if (this.selectedItem.chartType === 'v-scatter') {
                this.clearAlertMap()
              }
            })
          } else {
            _this.areaArr = _this.cityArr
            if (this.selectedItem.chartType === 'v-map') {
              this.initLevelData()
            }
            if (this.selectedItem.chartType === 'v-scatter') {
              this.clearAlertMap()
            }
          }
          // if (this.selectedItem.chartType === 'v-scatter') {
          //   this.clearAlertMap()
          // }
        }
      }
    },
    areaArr: function (newObj) {
      this.changeAreaData(newObj)
    },
    'testObj.width': function (newValue, oldValue) {
      this.testObjChange('width', newValue)
    },
    'testObj.height': function (newValue, oldValue) {
      this.testObjChange('height', newValue)
    },
    'testObj.x': function (newValue, oldValue) {
      this.testObjPosChange('x', newValue)
    },
    'testObj.y': function (newValue, oldValue) {
      this.testObjPosChange('y', newValue)
    }
  },
  beforeMount: function () {
    var id = this.$route.params.id || sessionStorage.getItem('pageId')
    this.pageId = id
    this.gerPageConf(id)
    sessionStorage.setItem('pageId', id)
  },
  mounted: function () {
    var _url = window.location.protocol + '//' + window.location.host + '/index'
    window.history.pushState({}, '', _url)
    // $('#header').hide()
    $('.navbar-fixed-top').css('display', 'none')
    $('.page-container').css('top', '0px')
    this.chooseMap()
    if (!gbs.inDev) {
      titleShow()
    }
    // 初始化paintInput
    document.addEventListener('keydown', this.handleKeyDown)
    document.addEventListener('keyup', this.handleKeyUp)
    // this.changeMapData(100000, 'provinceArr')
    this.getMapData(100000).then((data) => {
      this.provinceArr = data
    })
    // 添加事件监听
    // if (document.addEventListener) {
    //   document.addEventListener('DOMMouseScroll', this.scrollFunc, false)
    // } // W3C
    // window.onmousewheel = document.onmousewheel = this.scrollFunc // IE/Opera/Chrome/Safari
    // $(document).on('mousewheel DOMMouseScroll', this.onMouseScroll)
  },
  beforeDestroy: function () {
    $('#header').show()
    $('.tempDiv').remove() // 绑定的事件也会移除
    document.removeEventListener('keydown', this.handleKeyDown)
    document.removeEventListener('keyup', this.handleKeyUp)
    // var stateBar = document.getElementById('chooseWrap')
    // var stateBar = $('#chooseWrap')
    // stateBar.removeEventListener('mousedown', this.userChoose)
    $('.navbar-fixed-top').css('display', 'block')
    $('.page-container').css('top', '50px')
  },
  destoryed: function () {
    // $.comps.editHome = null;
  }
}
