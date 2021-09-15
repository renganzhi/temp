// 拖拽排序
// import { SlickList, SlickItem } from 'vue-slicksort'

import compsArr from './chartJson'
import ChartStyle from './ChartStyle'
import DragBox from '@/components/Common/DragBox'
import Compose from '@/components/Common/Compose'
import Select2 from '@/components/Common/Select2'
// import UEidetBox from '@/components/Common/uEidetBox'
import Vcolor from '@/components/Common/Vcolor'
import PreView from '@/components/PreView/PreView'
import Confirm from '@/components/Common/Confirm'
import { baseData, gbs } from '@/config/settings'
import { Slider, Notification } from 'element-ui'
import draggable from 'vuedraggable'
import { mapActions, mapGetters } from 'vuex'
// import html2canvas from 'html2canvas' // 图片的层级总是要高一些
import { checkLogin, newAjax } from '@/config/thirdLoginMix'
import qs from 'qs'
import _ from 'lodash'
// import oldConfig from './config.json'

import ChildTag from '@/components/ChildTag/styleTag'
import VueRulerTool from '@/components/helpLine/vue-ruler-tool'
import VueRuler from '@/components/helpLine/vue-ruler'
import Archive from '@/components/archive'
import HawkEye from '@/components/HawkEye'

import UE from '@/components/Common/ue'
// 改造， 过渡， 主要用于编辑页面右侧的样式和数据
let config = {
  // ...oldConfig,
  video: require('@/components/EditComp/player/config.js'),
  BiaxialBarChart: require('@/components/EditComp/BiaxialBarChart/config.js'),
  // ppt: require('@/components/EditComp/ppt/config.json'),
  GradientPie: require('@/components/EditComp/GradientPie/config.js'),
  Sunrise: require('@/components/EditComp/Sunrise/config.js'),
  BulletFrame: require('@/components/EditComp/BulletFrame/config.js'),
  Scatter: require('@/components/EditComp/Scatter/config.js'),
  polarBar: require('@/components/EditComp/polarBar/config.js'),
  KLine: require('@/components/EditComp/KLine/config.js'),
  Dashboard: require('@/components/EditComp/Dashboard/config.js'),
  ELine: require('@/components/EditComp/ELine/config.js'),
  DataFlow: require('@/components/EditComp/DataFlow/config.js'),
  TreeMap: require('@/components/EditComp/TreeMap/config.js'),
  Ueditor: require('@/components/EditComp/Ueditor/config.js'),
  TDHistogram: require('@/components/EditComp/TDHistogram/config.js'),
  TDModel: require('@/components/EditComp/TDModel/config.js'),
  VmVareTopo: require('@/components/EditComp/VmVareTopo/config.js'),
  TDEarthLine: require('@/components/EditComp/TDEarthLine/config.js'),
  BaiDuMap: require('@/components/EditComp/BaiDuMap/config.js'),
  IntegratedHistogram: require('@/components/EditComp/IntegratedHistogram/config.js'),
  TDEarthBar: require('@/components/EditComp/TDEarthBar/config.js'),
  NEWtextArea: require('@/components/EditComp/NEWtextArea/config.js'),
  NewMarquee: require('@/components/EditComp/NewMarquee/config.js'),
  NewDoubler: require('@/components/EditComp/NewDoubler/config.js'),
  DoubleLinde: require('@/components/EditComp/DoubleLinde/config.js'),
  NewPie: require('@/components/EditComp/NewPie/config.js'),
  NewRadar: require('@/components/EditComp/NewRadar/config.js'),
  NewGroupHistogram: require('@/components/EditComp/NewGroupHistogram/config.js'),
  NewGroupLeftHistogram: require('@/components/EditComp/NewGroupLeftHistogram/config.js'),
  NewBar: require('@/components/EditComp/NewBar/config.js'),
  NewTime: require('@/components/EditComp/NewTime/config.js'),
  NewGauge: require('@/components/EditComp/NewGauge/config.js'),
  NewNumber: require('@/components/EditComp/NewNumber/config.js'),
  NewBorder: require('@/components/EditComp/NewBorder/config.js'),
  JSMpeg: require('@/components/EditComp/JSMpeg/config.js'),
  Newimage: require('@/components/EditComp/Newimage/config.js'),
  NewMoveTable: require('@/components/EditComp/NewMoveTable/config.js'),
  NewTable: require('@/components/EditComp/NewTable/config.js'),
  NewProgress: require('@/components/EditComp/NewProgress/config.js'),
  NewHistogram: require('@/components/EditComp/NewHistogram/config.js'),
  NewVMap: require('@/components/EditComp/NewVMap/config.js'),
  NewScatter: require('@/components/EditComp/NewScatter/config.js'),
  liquidfill: require('@/components/EditComp/liquidfill/config.js'),
  ppt: require('@/components/EditComp/ppt/config.js'),
  bubble: require('@/components/EditComp/bubble/config.js')
}

export default {
  name: 'edit',
  components: { DragBox, VueRulerTool, VueRuler, UE, Compose, Select2, Vcolor, Confirm, PreView, Slider, draggable, ChartStyle, ChildTag, Archive, HawkEye },
  // mixins:[thirdLoginMix],
  props: [],
  data: function () {
    return {
      imgHeightLight: 0,
      MapChange: false,
      ShowHawkEye: false,
      HawkEyeStyle: {
        top: 0,
        left: 0
      },
      advanced: false,
      helpLineColor: '#348cea',
      presetLine: [{ type: 'h', site: 200 }, { type: 'v', site: 100 }],
      allPageList: [],
      canChangeId: [],
      gltfNameArry: [],
      iputneIdArry: [],
      activeNames: [0],
      TDmodelFormData: new FormData(),
      config,
      chooseSameFlag: false, // 是否选中同样的元件
      selectChange: false, // 是否改变的选中的元件
      animationType: ['ve-pie', 've-ring', 've-histogram', 've-bar', 've-line', 've-radar'],
      baseUrl: gbs.host,
      revokeStep: 20, // 撤销步数
      refreshData: true,
      viewKey: new Date().getTime() + parseInt(Math.random() * 10),
      showKeybd: false,
      CanChangeServes: false,
      guideStation: 'static',
      AllPageId: [],
      pageIdIndex: '',
      // AllHcnetData: [], // 设备
      // AllVideoData: [], // 视频
      showPlayErr: false,
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
      countyArr: [], // 选中区县所有的乡镇数据
      editPieces: [], // 区域分布图量级
      editPiecesCopy: [],
      selectMapData: {}, // 匹配区域分布图输入框的数据
      settingData: baseData,
      pageId: 0,
      value2: 0.5,
      oldCheckId: '', // 优化每次选中都会触发请求接口，仅在切换元件时才请求
      lastKeyId: '',
      viewPage: false,
      pageData: '',
      composeData: '',
      compsArr: compsArr,
      editable: true, // 操作flag,编辑为true,查看为false
      showStyleTab: true,
      pageName: '', // 页面名称
      showDataConf: false, // 展示系统数据配置
      selectedItem: {},
      selectedIndex: null,
      chartNum: [],
      oldChartNum: '', // chartNum更改之前的历史
      miniW: 20,
      minIndex: 501, // 当前最低层级
      maxIndex: 500, // 当前最高层级
      paintInput: {
        width: 1920, // 输入的画布大小
        height: 1080
      },
      paintObj: {
        width: 1920,
        height: 1080,
        bgColor: '#141929',
        bgImg: '',
        scale: 100,
        top: 0,
        left: 0,
        bgStyle: '3', // 背景图铺满方式
        opacity: 100,
        showGrid: false // 默认不显示网格
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
      tempItemArry: [], // ctrl选中的单个元件在chartNum中的序号
      copyType: '', // ctrl选中的单个元件在chartNum中的序号
      copyonlyOne: false, // ctrl选中的单个元件在chartNum中的序号
      // copyIndexs: [], // ctrl选中的单个元件在chartNum中的序号
      // copyCompIndexs: [], // ctrl选中的单个元件在chartNum中的序号
      onCtrl: false, // 是否按住ctrl
      showBackModal: false, // 离开页面弹窗
      importModelForm: {
        name: '',
        fileName: ''
      },
      showUpload: false, // 离开页面弹窗
      showNextType: -1, // 页面切换类型
      colorType: 'defalut',
      fontWeights: ['lighter', 'normal', 'bold', 'bolder'],
      resourcesValueIds: [],
      resourcesIds: [],
      defaultFontSize: [12, 13, 14, 16, 18, 20, 24, 26, 28, 30, 36, 40, 48, 54, 60, 72, 84, 88],
      proFontSize: [12, 13, 14, 16, 18, 20, 24, 26, 28, 30, 36, 40, 48, 54, 60, 72, 84, 88],
      defMapColors: ['#bb2a52', '#bd3d50', '#bf4e4e', '#c2634b', '#c47346', '#c7833f', '#ca9137', '#cd9d2c'],
      defalutColors: [
        '#37a2da',
        '#32c5e9',
        '#67e0e3',
        '#9fe6b8',
        '#ffdb5c',
        '#ff9f7f',
        '#fb7293',
        '#e062ae'
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
      dataApiParams: {}, // 请求数据传给后端的数据结构
      chainParams: {}, // 存放需要联动的指标
      testObj: {}, // 测试验证
      freshVali: false,
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
      borderRadius: 0,
      proHeightErr: false,
      radiusErr: false,
      aroundItem: {
        minX: 11000, // 多选时记录边界
        minY: 11000,
        maxX: 0,
        maxY: 0
      },
      aroundItemCopy: {
        minX: 11000,
        minY: 11000,
        maxX: 0,
        maxY: 0
      },
      minY: 10000, // 最小的y
      minXItem: {
        x: 10000, // 最小的x
        y: 10000, // 最小x的y
        maxW: 0, // 最远元素的width
        maxH: 0, // 最远高度元素的height
        gapX: 0, // maxW元素与x的间距
        gapY: 0 // maxH元素与y的间距
      }, // 多选情况下X值最小的元素
      minXItemCopy: {
        x: 10000, // 最小的x
        y: 10000, // 最小x的y
        id: 0,
        maxW: 0, // 最远元素的width
        maxH: 0, // 最远高度元素的height
        gapX: 0, // maxW元素与x的间距
        gapY: 0 // maxH元素与y的间距
      },
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
      },
      itemHistoryObj: [],
      historyArr: [],
      tapsStation: 'center',
      tempHisObj: {},
      tempVideoUrl: '', // 用户输入的视频URL
      isThird: false, // 当前数据来源是否为第三方数据
      dataSource: {}, // 数据来源对象
      curDataHost: gbs.host, // 当前数据来源的host,默认为gbs.host
      thirdIpPort: '' // 第三方数据的ip和port
    }
  },
  computed: {
    ...mapGetters([
      'alertInfo',
      'onlyOneItem',
      'thirdUser',
      'editId'
    ]),
    itemShow() { // 右侧折叠面板根据条件显示隐藏
      return function (val) {
        var show = true
        if (val.parentKey) {
          for (const key in val.parentKey) {
            if (this.selectedItem[key] !== val.parentKey[key]) {
              show = false
            }
          }
        }
        return show
      }
    },
    staticData() {
      if (this.selectedItem.chartType === 'IntegratedHistogram') {
        if (this.selectedItem.barType === 'NewHistogram') {
          return this.selectedItem.chartData1
        }
        if (this.selectedItem.barType === 'NewGroupHistogram') {
          return this.selectedItem.chartData2
        }
        if (this.selectedItem.barType === 'NewGroupLeftHistogram') {
          return this.selectedItem.chartData3
        }
        if (this.selectedItem.barType === 'NewBar') {
          return this.selectedItem.chartData4
        }
      } else {
        return this.selectedItem.chartData
      }
    },
    curChartType() { return this.selectedItem.chartType },
    curChartName() {
      if (['text', 'NEWtextArea'].includes(this.curChartType)) {
        return '文本框'
      } else if (this.curChartType === 'NewMarquee' || this.curChartType === 'marquee') {
        return '跑马灯'
      }
      if (this.selectedItem.chartType === 'IntegratedHistogram') {
        if (this.selectedItem.barType === 'NewHistogram') {
          return '柱状图'
        }
        if (this.selectedItem.barType === 'NewGroupHistogram') {
          return '分组柱图'
        }
        if (this.selectedItem.barType === 'NewGroupLeftHistogram') {
          return '堆叠柱图'
        }
        if (this.selectedItem.barType === 'NewBar') {
          return '条形图'
        }
      }
      if (this.selectedItem.chartType === 'NewPie') {
        return this.selectedItem.pieType
      }
      return this.selectedItem.ctName || ''
    },
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
          ? 'url(' + gbs.host + '/leaderview/leaderviewWeb' + this.paintObj.bgImg + ')' : '',
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
        this.selectedItem.ctDataSource !== 'static'
      ) {
        return false
      }
      return true
    },
    fontFaces() {
      if (this.selectedItem.chartType === 'number') {
        return this.settingData.fontFaces
      }
      return this.settingData.textFontFaces
    }
  },
  provide: {
    editing: true
  },
  updated: function () {
    if ($('.typeSelect').parents('div').length) {
      $('.typeSelect').parents('div')[0].style.height = '50px'
    }
  },
  created() {
    $('.getPicSpan').hide()
    this.axios.get('/leaderview/home/getDatasource').then(res => {
      this.dataSource = { '静态数据': '', '系统数据': '', ...(res.obj || {}) }
    })
    if (window.CrossScreenCope) {
      this.tempItemArry = window.CrossScreenCope.ItemArry || []
      this.copyType = window.CrossScreenCope.copyType || ''
      this.copyonlyOne = window.CrossScreenCope.copyonlyOne || false
    }
    if (this.paintObj.templateType === 'single') {
      this.CanChangeServes = true
      // this.paintObj.templateConf.baseneclss  neclass
      this.axios.get(`/leaderview/monitor/params/nes?notUnknown=true&domainId=&baseNeClass=${this.paintObj.templateConf.baseneclss}&neClass=${this.paintObj.templateConf.neclass}`).then(res => {
        this.resourcesValueIds = res.obj || []
      })
    } else {
      this.CanChangeServes = false
    }
    this.axios.get('/leaderview/home/getCarouselTimeConf').then((data) => {
      // var res = data.obj
      this.AllPageId = []
      data.obj.pages.forEach((d, index) => {
        if (d.pageId * 1 === this.pageId * 1) {
          this.pageIdIndex = index
        }
        if (d.visible === true) {
          this.AllPageId.push(d.pageId)
        }
      })
    })
    this.getModelFun()
    this.TDmodelFormData.append('file', '')
    this.TDmodelFormData.append('name', '')

    this.axios.get('/monitor/virtualization/vmware/topo/menu').then((res) => {
      res.obj.forEach(element => {
        if (element.neId && element.neId !== '') {
          this.iputneIdArry.push(element)
        }
      })
    })
  },
  methods: {
    ...mapActions([
      'changeHomeData',
      'changeAreaData',
      'changeItemChoose',
      'changeLimitItem',
      'changeThirdConf'
    ]),
    upOnePage() {
      let id = this.pageId
      if (this.pageIdIndex * 1 === 0) {
        id = this.canChangeId[this.canChangeId.length - 1]
      } else {
        id = this.canChangeId[this.pageIdIndex - 1]
      }
      this.pageId = id
      this.getPageConf(id)
      sessionStorage.setItem('pageId', id)
      this.canChangeId.forEach((d, index) => {
        if (d * 1 === this.pageId * 1) {
          this.pageIdIndex = index
        }
      })
    },
    downOnePage() {
      let id = this.pageId
      if (this.pageIdIndex * 1 === this.canChangeId.length - 1) {
        id = this.canChangeId[0]
      } else {
        id = this.canChangeId[this.pageIdIndex + 1]
      }
      this.pageId = id
      this.getPageConf(id)
      sessionStorage.setItem('pageId', id)
      this.canChangeId.forEach((d, index) => {
        if (d * 1 === this.pageId * 1) {
          this.pageIdIndex = index
        }
      })
    },
    fatherhorizontalDragRuler() {
      this.$refs.rulertool.horizontalDragRuler()
    },
    fatherverticalDragRuler() {
      this.$refs.rulertool.verticalDragRuler()
    },
    changeChartStyle(key, val) {
      this.selectedItem[key] = val
      console.info('change-config', key, val)
      // this.$set('selectedItem', key, val )
    },
    getAllPage() {
      this.axios.get('/leaderview/home/homePage/noConf').then((res) => {
        this.allPageList = res.obj
        res.obj.forEach(d => {
          if (d.belongCurrentUser === 'true') {
            this.canChangeId.push(d.id)
          }
        })
      })
    },
    ifSameItems() {
      if (this.chooseCompIndexs.length > 0 || this.chooseIndexs.length < 2) {
        this.chooseSameFlag = false
        return false
      }
      let itemType = this.chartNum[this.chooseIndexs[0]].chartType
      for (let i = 0, len = this.chooseIndexs.length; i < len; i++) {
        if (this.chartNum[this.chooseIndexs[i]].chartType !== itemType) {
          this.chooseSameFlag = false
          return false
        }
      }
      this.showStyleTab = true
      this.chooseSameFlag = true
      return true
    },
    saveHistory(type) {
      /** 全部保存 */
      if (this.historyArr && this.historyArr.length >= this.revokeStep) {
        this.historyArr.shift()
      }
      if (type) {
        // 保存画布
        this.historyArr.push({
          type: 'paint',
          paintObj: JSON.stringify(this.paintObj)
        })
      } else {
        this.historyArr.push({
          type: 'item',
          chartNum: JSON.stringify(this.chartNum),
          compose: JSON.stringify(this.combinList)
        })
      }
    },
    bodyDown() {
      // 选中元件
      // console.log('这里暂存一次选中元件')
      this.tempHisObj = {
        type: 'item',
        chartNum: JSON.stringify(this.chartNum),
        compose: JSON.stringify(this.combinList)
      }
    },
    // 撤销
    Revoke() {
      // this.itemHistoryObj.pop() // 最后一条是当前的
      // var tempObj = this.itemHistoryObj.pop() // 倒数第二条才是历史记录
      // if (!tempObj) {
      //   return
      // }
      // this.selectedItem = {}
      // this.chooseCompIndexs = []
      // this.chooseIndexs = []
      // // this.$set(this.chartNum, tempObj.index, JSON.parse(JSON.stringify(tempObj.oldObj)))
      // tempObj.oldObj.slted = false
      // this.chartNum.splice(tempObj.index, 1, JSON.parse(JSON.stringify(tempObj.oldObj)))
      /** 全部保存 */
      var oldObj = this.historyArr.pop()
      if (!oldObj) {
        Notification({
          message: '已撤销至最大限制！',
          position: 'bottom-right',
          customClass: 'toast toast-info'
        })
        return
      }
      console.log('撤销一步')
      if (oldObj.type !== 'paint') {
        this.selectedItem = {}
        this.chooseCompIndexs = []
        this.chooseIndexs = []
        this.chartNum = JSON.parse(oldObj.chartNum)
        this.combinList = JSON.parse(oldObj.compose)
      } else {
        // console.log('type 不是item')
        this.paintObj = JSON.parse(oldObj.paintObj)
        this.paintInput = JSON.parse(oldObj.paintObj)
      }
    },
    // 单元件change
    changeStop(index) {
      // this.itemHistoryObj.push({
      //   type: 'item',
      //   index: index, // 改成id
      //   oldObj: JSON.parse(JSON.stringify(this.chartNum[index]))
      // })
      // this.saveHistory() // 应该保存之前的数据
      if (this.historyArr && this.historyArr.length >= this.revokeStep) {
        this.historyArr.shift()
      }
      this.historyArr.push(this.tempHisObj)
      this.bodyDown()
    },
    scrollLeft(x) {
      if (this.chooseIndexs.length + this.chooseCompIndexs.length > 1) {
        this.saveHistory()
        this.minXItem.x += x
        this.changeTarget('x')
      } else {
        this.saveHistory()
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
    scrollTop(y) {
      if (this.chooseIndexs.length + this.chooseCompIndexs.length > 1) {
        this.saveHistory()
        this.minXItem.y += y
        this.changeTarget('y')
      } else {
        this.saveHistory()
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
    changeBdType() {
      if (this.selectedItem.borderType === 'stable') {
        this.$set(this.selectedItem, 'imgSrc', this.settingData.cardCase[0].imgSrc)
      }
    },
    getMapData(chinaId) {
      var mapPth = gbs.inDev ? 'static' : 'leaderview/leaderviewWeb'
      if (chinaId) {
        return new Promise((resolve, reject) => {
          this.axios.get('./../../../../' + mapPth + '/libs/map/' + chinaId + '.json', {}).then(response => {
            var data = this.initMapData(response)
            resolve(data)
          }).catch((error) => {
            Notification({
              message: '该地区暂无详细地图',
              position: 'bottom-right',
              customClass: 'toast toast-info'
            })
            reject(error)
          })
        })
      } else {
        return null
      }
    },
    mapDataToChart() {
      // 用户输入的数据转化为区域分布图所需数据
      var tempData = []
      for (var k in this.selectMapData) {
        let obj = { '位置': k, '告警': this.selectMapData[k] }
        tempData.push(obj)
      }
      this.selectedItem.chartData.rows = tempData
    },
    chartDataToMap() {
      if (this.selectedItem.chartType === 'v-map' || this.selectedItem.chartType === 'NewVMap') {
        // chartData转化为input输入数据
        this.selectMapData = {}
        var tempData = this.selectedItem.chartData.rows
        tempData.forEach((item) => {
          this.selectMapData[item['位置']] = item['告警']
        })
      }
    },
    selectToPoint() {
      // 数据点转化为散点数据
      this.geoCoordMap = {}
      this.areaArr.forEach((item) => {
        this.geoCoordMap[item.name] = item.geoCoord
      })
    },
    clearAlertMap() {
      // console.log('清空初始化数据点')
      this.alertMapData = []
      this.selectedItem.chartData = [{ name: this.areaArr[0].name, value: 2 }]
      this.$nextTick(() => {
        this.alertMapData = [{ name: this.areaArr[0].name, value: 2 }]
        this.selectedPositn = [this.areaArr[0].name]
      })
      // this.$set('selectedItem', 'chartData', [])
    },
    initLevelData(areaData) {
      // 区域分布图给前三项赋默认值
      var arr = []
      if (areaData) {
        arr = areaData
      } else {
        arr = this.areaArr
      }
      // console.log('初始化前三条默认数据')
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
    changeMapData(chinaId, target) {
      var mapPth = gbs.inDev ? 'static' : 'leaderview/leaderviewWeb'
      this.axios.get('./../../../../' + mapPth + '/libs/map/' + chinaId + '.json', {}).then(response => {
        this[target] = this.initMapData(response)
        if (target === 'provinceArr' && !this.selectedItem.provinceCode) {
          this.selectedItem.provinceCode = this[target][0].value
        }
        if (target === 'cityArr') {
          this.selectedItem.cityCode = this[target][0].value
        }
      }).catch((err) => {
        // console.log(err)
      })
    },
    // 改变展示范围
    chgMapLevel() {
      // 这里会先于watch mapLevel触发
      this.selfMapLevel = true
    },
    chgMapGrad(index) {
      if (this.editPieces[index]['max'] < this.editPieces[index]['min']) {
        Notification({
          message: '量级的最大值不可小于最小值',
          position: 'bottom-right',
          customClass: 'toast toast-info'
        })
        this.editPieces = JSON.parse(JSON.stringify(this.editPiecesCopy))
      } else if (!this.editPieces[index + 1]['max'] || (this.editPieces[index]['max'] < this.editPieces[index + 1]['max'] - 1)) {
        this.editPieces[index + 1]['min'] = Number(this.editPieces[index]['max']) + 1
        this.editPiecesCopy = JSON.parse(JSON.stringify(this.editPieces))
      } else {
        var newValue = this.editPieces[index]['max']
        if (index === 0 && newValue + 2 > this.editPieces[this.editPieces.length - 2]['max']) {
          // 合并之后梯度不足3个
          Notification({
            message: '区间跨度太大，请至少保证三个量级',
            position: 'bottom-right',
            customClass: 'toast toast-info'
          })
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
    sureLevelTips() {
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
    cancelLevelTips() {
      this.editPieces = JSON.parse(JSON.stringify(this.editPiecesCopy))
      this.levelTipsShow = false
    },
    addMapLevel() {
      if (this.editPieces.length >= 8) {
        return
      }
      var lastMin = this.editPieces[this.editPieces.length - 1].min
      this.editPieces[this.editPieces.length - 1].max = lastMin + 49
      this.editPieces.push({ min: lastMin + 50 })
      this.editPiecesCopy = JSON.parse(JSON.stringify(this.editPieces))
      this.selectedItem.piecesData = JSON.parse(JSON.stringify(this.editPieces))
    },
    delMapLevel() {
      if (this.editPieces.length <= 3) {
        return
      }
      this.editPieces.pop()
      delete this.editPieces[this.editPieces.length - 1].max
      this.editPiecesCopy = JSON.parse(JSON.stringify(this.editPieces))
      this.selectedItem.piecesData = JSON.parse(JSON.stringify(this.editPieces))
    },
    initMapData(mapJson) {
      var mapData = []
      for (var i = 0; i < mapJson.features.length; i++) {
        mapData.push({
          value: mapJson.features[i].id || mapJson.features[i].properties.adcode,
          name: mapJson.features[i].properties.name,
          geoCoord: mapJson.features[i].properties.cp || this.getCenterPoint(mapJson.features[i].geometry.coordinates)
          // geoCoord: mapJson.features[i].properties.cp || mapJson.features[i].geometry.coordinates[0][0][0]
        })
      }
      return mapData
    },
    // 计算地市级地图的中心点
    getCenterPoint(data) {
      let index = data.length - 1
      var tempObj = data[index][0]
      if (tempObj) {
        let totalX = 0, totalY = 0
        tempObj.forEach((item) => {
          totalX += item[0]
          totalY += item[1]
        })
        let _length = tempObj.length
        return [totalX / _length, totalY / _length]
      } else {
        return data[0][0][0]
      }
    },
    // 删除实时图数据点
    delAlertLevel(index) {
      this.alertMapData.splice(index, 1)
      this.selectedPositn.splice(index, 1)
    },
    // 地图实时图添加数据点
    addAlertLevel() {
      for (let i = 0, len = this.areaArr.length; i < len; i++) {
        if (this.selectedPositn.indexOf(this.areaArr[i].name) === -1) {
          this.selectedPositn.push(this.areaArr[i].name)
          this.alertMapData.push({ name: this.areaArr[i].name, value: 1 })
          break
        }
      }
    },
    // 切换地图的省
    chgProvince(id) {
      if (this.selectedItem.mapLevel === 'city' || this.selectedItem.mapLevel === 'county') {
        // 北京， ... ， 香港， 澳门
        var noMapArr = ['110000', '310000', '500000', '120000', '710000', '810000', '820000']
        if (noMapArr.indexOf(id) !== -1) {
          this.selectedItem.mapLevel = 'province'
          Notification({
            message: '该地区不支持该级别地图',
            position: 'bottom-right',
            customClass: 'toast toast-info'
          })
        }
      }
      if (id) {
        this.getMapData(id).then((data) => {
          this.cityArr = data
          // console.log('===========' + this.selectedItem.mapLevel + '=========')
          if (this.selectedItem.mapLevel === 'province') {
            this.areaArr = data
            if (this.selectedItem.chartType === 'v-map' || this.selectedItem.chartType === 'NewVMap') {
              this.initLevelData()
            } else if (this.selectedItem.chartType === 'v-scatter' || this.selectedItem.chartType === 'NewScatter') {
              this.clearAlertMap()
            }
          } else if (this.selectedItem.mapLevel === 'city') {
            this.selectedItem.cityCode = data[0].value
            if (this.selectedItem.chartType === 'v-scatter' || this.selectedItem.chartType === 'NewScatter') {
              this.clearAlertMap()
            }
          } else if (this.selectedItem.mapLevel === 'county') {
            this.selectedItem.cityCode = data[0].value
            if (this.selectedItem.chartType === 'v-scatter' || this.selectedItem.chartType === 'NewScatter') {
              this.clearAlertMap()
            }
          }
        })
      }
    },
    chgCity(id) {
      // console.log('+++++++chgCity+++++++-' + this.selfMapLevel)
      id = id || this.selectedItem.cityCode
      if (id && this.selfMapLevel) {
        this.getMapData(id).then((data) => {
          this.selectedItem.cityCode = id
          if (this.selectedItem.mapLevel === 'city') {
            this.areaArr = data
          } else if (this.selectedItem.mapLevel === 'county') {
            this.countyArr = data
            this.selectedItem.countyCode = this.countyArr[0].name
          }
          if (this.selectedItem.chartType === 'v-map' || this.selectedItem.chartType === 'NewVMap') {
            this.initLevelData()
          }
          if (this.selectedItem.chartType === 'v-scatter' || this.selectedItem.chartType === 'NewScatter') {
            if (this.selfMapLevel && id) {
              this.clearAlertMap()
            }
          }
        })
      }
    },
    // 上传json
    uploadJson() {
      var filesList = document.querySelector('#uploadJson').files
      var file = filesList[0]
      var formdata = new FormData()
      formdata.append('file', file)
      formdata.append('name', this.selectedItem.countyCode)
      var name = this.selectedItem.countyCode
      this.selectedItem.countyCode = 'test'
      this.axios.post('/leaderview/home/uploadJson', formdata).then((data) => {
        var url = data.obj.url
        this.axios.get('/' + url).then((data) => {
          this.areaArr = this.initMapData(data)
          this.selectedItem.countyCode = name
          if (this.selectedItem.chartType === 'v-map' || this.selectedItem.chartType === 'NewVMap') {
            this.initLevelData()
          }
          if (this.selectedItem.chartType === 'v-scatter' || this.selectedItem.chartType === 'NewScatter') {
            if (this.selfMapLevel && this.selectedItem.countyCode) {
              this.clearAlertMap()
            }
          }
        }).catch((err) => {
          if (err) {
            this.areaArr = []
          }
        })
        document.getElementById('uploadJson').value = ''
      })
    },
    openJsonClick() {
      var upload = document.getElementById('uploadJson')
      upload.click()
    },
    chgCounty(id) {
      id = id || this.selectedItem.countyCode
      if (id && this.selfMapLevel) {
        this.axios.get('/leaderview/home/getJson', {
          params: {
            name: id
          }
        }).then((data) => {
          let url = data.obj['文件路径']
          this.axios.get('/' + url).then((data) => {
            this.selectedItem.countyCode = id
            this.areaArr = this.initMapData(data)
            if (this.selectedItem.chartType === 'v-map' || this.selectedItem.chartType === 'NewVMap') {
              this.initLevelData()
            }
            if (this.selectedItem.chartType === 'v-scatter' || this.selectedItem.chartType === 'NewScatter') {
              if (this.selfMapLevel && id) {
                this.clearAlertMap()
              }
            }
          }).catch((err) => {
            if (err) {
              this.areaArr = []
            }
          })
        })
      }
    },
    chgAreaName(name, index) {
      if (name) {
        this.$set(this.selectedPositn, index, name)
        let temp = this.alertMapData.pop()
        this.alertMapData.push(temp)
      }
      // 数组需要更换
    },
    // 边框圆角
    radiusChange() {
      var radius = Math.floor(this.borderRadius)
      if (radius === 0) {
        this.borderRadius = 0 // -e
      }
      if (isNaN(radius) || radius < 0) {
        this.borderRadius = this.selectedItem.radius
      }
      if (this.borderRadius <= this.selectedItem.height / 2) {
        this.selectedItem.radius = this.borderRadius
      } else {
        this.selectedItem.radius = this.selectedItem.height / 2
        this.borderRadius = this.selectedItem.radius
      }
    },
    changeProHeight() {
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
      if (this.selectedItem.radius > Math.ceil(this.selectedItem.proHeight / 2)) {
        this.selectedItem.radius = Math.ceil(this.selectedItem.proHeight / 2)
        this.progressObj.radius = this.selectedItem.radius
      }
    },
    // 改变进度条元件的圆角
    changeRadius() {
      var radius = Math.floor(this.progressObj.radius)
      if (radius === 0) {
        this.progressObj.radius = 0
      }
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
    colorToAll(ScatterColor, DScatterColor, ifGradual) {
      var _colors = this.selectedItem.ctColors
      if (this.chartNum.length) {
        this.saveHistory()
      }
      this.chartNum.forEach((item) => {
        if ((['NewHistogram', 'NewGroupHistogram', 'NewGroupLeftHistogram', 'NewBar', 'ELine', 'DoubleLinde', 'GradientPie', 'polarBar', 'TDHistogram', 'TDModel', 'Scatter', 'KLine'].includes(item.chartType))) {
          if (
            item.chartData &&
            item.chartData.colors &&
            item.ctDataSource !== 'static'
          ) {
            // 接口返回的系统默认颜色不做修改
          } else {
            if (item.DScatterColor && DScatterColor) {
              item.DScatterColor = JSON.parse(DScatterColor)
            }
            item.ScatterColor = JSON.parse(ScatterColor)
          }
        }
      })
      this.chartNum.forEach((item) => {
        if (item.chartType.indexOf('ve-') !== -1) {
          if (
            item.chartData &&
            item.chartData.colors &&
            item.ctDataSource !== 'static'
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
    getPageConf(id) {
      // home/homePage/getById
      this.axios.get(`/leaderview/home/homePage/getById/${id}`).then(res => {
        console.log(res)
        this.pageName = res.obj.name
        if (!res.obj.viewConf) {
          res.obj.viewConf = '[]'
          res.obj.composeObj = '[]'
        }
        if (res.obj.viewConf) {
          this.chartNum = JSON.parse(res.obj.viewConf)
          if (res.obj.paintObj) {
            this.paintObj = JSON.parse(res.obj.paintObj)
            this.paintInput.width = this.paintObj.width
            this.paintInput.height = this.paintObj.height
            this.changeHomeData(this.paintObj) // vuex保存画布大小
          }
          let tempNum = this.chartNum
          if (res.obj.composeObj) {
            this.combinList = JSON.parse(res.obj.composeObj)
            tempNum = this.chartNum.concat(this.combinList)
          }
          for (let i = 0, len = tempNum.length; i < len; i++) {
            tempNum[i].zIndex > this.maxIndex
              ? (this.maxIndex = tempNum[i].zIndex)
              : ''
            tempNum[i].zIndex < this.minIndex
              ? (this.minIndex = tempNum[i].zIndex)
              : ''
          }

          this.formatVersion()
        } else {
          this.chartNum = []
        }
        // this.saveHistory()
      })
    },
    formatVersion() {
      // 新增字段的监听需要对以前版本进行兼容
      this.chartNum.forEach((item) => {
        if (!item.legendColor) {
          this.$set(item, 'legendColor', '#828bac')
        }
        if (!item.refreshTm) {
          this.$set(item, 'refreshTm', 5)
        }
        if (item.chartType.includes('ve-')) {
          if (!item.ifGradual) {
            if (item.colorType === 'custom') {
              this.$set(item, 'ifGradual', 'true')
            } else {
              this.$set(item, 'ifGradual', 'false')
            }
          }
          if (item.chartType === 've-bar' || item.chartType === 've-histogram') {
            if (!item.splitColor) {
              this.$set(item, 'splitColor', '#333849')
            }
          }
          if (item.chartType === 've-line') {
            if (!item.splitColor) {
              this.$set(item, 'splitColor', '#333849')
            }
            if (!item.smooth) {
              this.$set(item, 'smooth', 'true')
            }
          }
          if (item.chartType === 've-radar') {
            if (!item.splitColor) {
              this.$set(item, 'splitColor', 'rgba(117, 124, 137, 0.2)')
            }
            if (!item.splitShow) {
              this.$set(item, 'splitShow', 'true')
            }
            if (!item.splitSize) {
              this.$set(item, 'splitSize', 1)
            }
          }
        }
        if (item.chartType === 'v-map' || item.chartType === 'NewVMap') {
          if (!item.cityShow) {
            this.$set(item, 'cityShow', 'false')
          }
          if (!item.cityColor) {
            this.$set(item, 'cityColor', '#828bac')
          }
        }
        if (item.chartType === 'topo' && !item.cityColor) {
          this.$set(item, 'cityColor', '')
        }
        // 以上为四期新增
        if (item.chartType === 've-gauge' && !item.bgClr) {
          this.$set(item, 'bgClr', '#657992')
        }
        if (item.chartType === 'progress') {
          if (!item.colorful) {
            this.$set(item, 'colorful', 'true')
          }
          if (!item.barClrs) {
            this.$set(item, 'barClrs', [item.barClr, item.barClr])
          }
        }
        if (item.chartType === 'border' && !item.borderType) {
          this.$set(item, 'borderType', 'simple')
          this.$set(item, 'imgSrc', '')
        }
        if (item.chartType === 'border' && item.borderType === 'simple') {
          if (!item.colorful) {
            this.$set(item, 'colorful', 'true')
          }
          if (!item.barClrs) {
            this.$set(item, 'barClrs', [item.bgClr, item.bgClr])
          }
        }
        if (item.chartType === 'table' || item.chartType === 'moveTable') {
          if (!item.hdClr) {
            this.$set(item, 'hdClr', item.clr || '#cad6dd')
          }
          if (!item.hdfontSize) {
            this.$set(item, 'hdfontSize', item.fontSize || 12)
          }
        }
      })
      this.combinList.forEach((item) => {
        item.child.forEach((list) => {
          if (list.chartType === 'table' || list.chartType === 'moveTable') {
            if (!list.hdClr) {
              this.$set(list, 'hdClr', list.clr || '#cad6dd')
            }
            if (!list.hdfontSize) {
              this.$set(list, 'hdfontSize', list.fontSize || 12)
            }
          }
          if (list.chartType === 'progress') {
            this.$set(list, 'colorful', 'true')
          }
          if (!list.legendColor) {
            this.$set(list, 'legendColor', '#828bac')
          }
          if (!list.refreshTm) {
            this.$set(list, 'refreshTm', 5)
          }
          if (list.chartType.includes('ve-')) {
            if (!list.ifGradual) {
              if (list.colorType === 'custom') {
                this.$set(list, 'ifGradual', 'true')
              } else {
                this.$set(list, 'ifGradual', 'false')
              }
            }
            if (list.chartType === 've-bar' || list.chartType === 've-histogram') {
              if (!list.splitColor) {
                this.$set(list, 'splitColor', '#333849')
              }
            }
            if (list.chartType === 've-line') {
              if (!list.splitColor) {
                this.$set(list, 'splitColor', '#333849')
              }
              if (!list.smooth) {
                this.$set(list, 'smooth', 'true')
              }
            }
            if (list.chartType === 'v-map' || list.chartType === 'NewVMap') {
              if (!list.cityShow) {
                this.$set(list, 'cityShow', 'false')
              }
              if (!list.cityColor) {
                this.$set(list, 'cityColor', '#828bac')
              }
            }
            if (list.chartType === 'topo' && !list.cityColor) {
              this.$set(list, 'cityColor', '')
            }
            if (list.chartType === 've-radar') {
              if (!list.splitColor) {
                this.$set(list, 'splitColor', 'rgba(117, 124, 137, 0.2)')
              }
              if (!list.splitShow) {
                this.$set(list, 'splitShow', 'true')
              }
              if (!list.splitSize) {
                this.$set(list, 'splitSize', 1)
              }
            }
          }
        })
      })
    },
    initChart(value) {
      // 画布中心位置
      const editCanvas = this.$refs.editCanvas
      const { scrollTop, scrollLeft, clientHeight, clientWidth } = editCanvas
      // console.log('width: ', clientHeight, clientWidth);
      const scale = this.paintObj.scale / 100
      const transformX = (clientWidth / 2 + scrollLeft - (value.width || 350) / 2) / scale
      const transformY = (clientHeight / 2 + scrollTop - (value.height || 350) / 2) / scale
      // console.log(editCanvas.scrollLeft, editCanvas.scrollTop, this.paintObj.scale);
      this.showStyleTab = true
      this.showWindowBtn = false // 隐藏部件弹窗按钮
      this.saveHistory()
      var obj = $.extend(
        true, {}, {
        id: new Date().getTime(),
        ctName: value.text,
        ctLegendShow: 'true',
        legendColor: '#828bac',
        x: Math.floor(transformX),
        y: Math.floor(transformY),
        width: 350,
        height: 350,
        refreshTm: 5, // 刷新周期
        zIndex: ++this.maxIndex,
        colorType: 'defalut',
        ctColors: value.chartType === 'v-map' || value.chartType === 'NewVMap' ? this.defMapColors.concat() : this.defalutColors.concat(),
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
      if (value.chartType === 'v-map' || value.chartType === 'v-scatter' || value.chartType === 'NewScatter' || value.chartType === 'NewVMap') {
        this.areaArr = this.provinceArr
        if (value.chartType === 'v-map' || value.chartType === 'NewVMap') {
          this.selectMapData = { '台湾': 25, '河北': 75, '山西': 125 }
          this.editPieces = JSON.parse(JSON.stringify(obj.piecesData))
        }
        if (value.chartType === 'v-scatter' || value.chartType === 'NewScatter') {
          this.alertMapData = _.cloneDeep(obj.chartData)
        }
      }
      if (value.chartType === 'progress') {
        this.progressObj.height = 16
        this.progressObj.radius = 8
      }
      if (!gbs.inDev) {
        this.$nextTick(function () {
          titleShow('bottom', $('.e-legend'))
        })
      }
    },
    chgColorType: function () {
      if (!this.selectChange && this.chooseSameFlag) {
        if (this.selectedItem.colorType === 'defalut') {
          this.chooseIndexs.forEach((i) => {
            this.chartNum[i]['ctColors'].splice(0, 8)
          })
          if (this.selectedItem.chartType === 'v-map' || this.selectedItem.chartType === 'NewVMap') {
            this.chooseIndexs.forEach((i) => {
              this.$set(this.chartNum[i], 'ctColors', this.defMapColors.concat())
            })
          } else {
            this.chooseIndexs.forEach((i) => {
              this.$set(this.chartNum[i], 'ctColors', this.defalutColors.concat())
            })
          }
        } else {
          if (this.selectedItem.chartType !== 'v-map' && this.selectedItem.chartType !== 'NewVMap') {
            this.chooseIndexs.forEach((i) => {
              this.$set(this.chartNum[i], 'ctColors', JSON.parse(JSON.stringify(this.defGradColors)))
            })
            this.changeTogether('ifGradual', 'true')
          }
        }
      } else {
        if (this.selectedItem.colorType === 'defalut') {
          this.selectedItem.ctColors.splice(0, this.selectedItem.ctColors.length)
          if (this.selectedItem.chartType === 'v-map' || this.selectedItem.chartType === 'NewVMap') {
            this.$set(this.selectedItem, 'ctColors', this.defMapColors.concat())
          } else {
            this.$set(this.selectedItem, 'ctColors', this.defalutColors.concat())
          }
        } else {
          if (this.selectedItem.chartType !== 'v-map' && this.selectedItem.chartType !== 'NewVMap') {
            this.selectedItem.ctColors.splice(0, this.selectedItem.ctColors.length)
            let newColors = JSON.parse(JSON.stringify(this.defGradColors))
            this.$set(this.selectedItem, 'ctColors', newColors)
            this.selectedItem.ifGradual = 'true'
          }
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
    clickPaint(event) {
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
    cancelSelected(event) {
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
      this.minXItem = JSON.parse(JSON.stringify(this.minXItemCopy))
      this.aroundItem = JSON.parse(JSON.stringify(this.aroundItemCopy))
      this.changeLimitItem(this.aroundItem)
      this.childResize = false // 暂且保留
      // this.onCtrl = false
    },
    mycontextmenu: function (ev) {
      if (ev.srcElement.className === 'vue-ruler-wrapper' || ev.srcElement.id === 'chooseWrap') {
        $(this.$refs.contextMenu).toggle(false)
        this.clickPaint() // 取消所有的选中
        if (!this.ShowHawkEye) {
          this.HawkEyeStyle.top = ev.pageY
          this.HawkEyeStyle.left = ev.pageX
        }
        $(this.$refs.copyMenu)
          .css({
            left: ev.pageX,
            top: ev.pageY
          })
          .toggle(true)
      }
    },
    selected: function (item, ev, type, i) {
      if (ev === 'down') {
        this.activeNames = [0]
      }
      $('--open').remove()
      this.selfMapLevel = false
      if (this.childResize && ev === 'context') {
        // 内部元件的右键
        return
      }
      this.childResize = false
      if (ev !== 'context' && ev !== 'move' && !window.event.ctrlKey) {
        // 判断是否拖拽，若已选中多个元件则不会取消
        if (type === 'compose') {
          var _id = this.chooseCompIndexs.indexOf(i)
          if (_id !== -1) {
            if (this.chooseIndexs.length + this.chooseCompIndexs.length > 1) {
              this.selectedItem = item
              return
            }
          }
        } else {
          _id = this.chooseIndexs.indexOf(i)
          if (_id !== -1) {
            if (this.chooseIndexs.length + this.chooseCompIndexs.length > 1) {
              this.selectedItem = item
              return
            }
          }
        }
        this.cancelSelected()
      }
      if (ev === 'move') {
        this.hideContext()
      }
      if (window.event.ctrlKey && ev !== 'move' && item.slted) {
        this.showWindowBtn = false
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
        if (this.chooseCompIndexs.length === 0) {
          if (this.chooseIndexs.length > 0) {
            let index = this.chooseIndexs[this.chooseIndexs.length - 1]
            this.selectedItem = this.chartNum[index]
          }
        }
        this.ifSameItems()
        return
      } else {
        if (ev !== 'move') {
          this.lastKeyId = this.testObj.id
          // 增加选中
          item.slted = this.editable && true
          if (item.chartType === 'video') {
            this.tempVideoUrl = item.videoSrc
          }
          if (item.chartType === 'border') {
            if (!item.radius) {
              this.$set(item, 'radius', 0)
            }
            this.borderRadius = item.radius || 0
          }
          if (item.chartType === 'v-map' || item.chartType === 'NewVMap') {
            this.selectedItem = {} // 避免触发三级下拉的监听
          }
          // this.s
          this.selectedItem = item
          if (item.chartType === 'progress') {
            this.progressObj.height = item.proHeight || 16
            this.progressObj.radius = (item.radius === 0) ? 0 : (item.radius || 8)
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
            this.minXItem.x = item.x
            this.aroundItem.minX = item.x
            this.minXItem.y = item.y
            this.minXItem.id = item.id
            this.selectArea.left = item.x // 不变的最小值
            this.selectArea.top = item.y
          }
          if (item.y < this.aroundItem.minY) {
            this.aroundItem.minY = item.y
          }
          if (item.x + item.width > this.aroundItem.maxX) {
            this.aroundItem.maxX = item.x + item.width
          }
          if (item.y + item.height > this.aroundItem.maxY) {
            this.aroundItem.maxY = item.y + item.height
          }
          this.changeLimitItem(this.aroundItem)
          this.setMaxItemInfo()
          this.ifSameItems()
        }
      }
      if (this.selectedItem.chartType === 'v-scatter' || this.selectedItem.chartType === 'NewScatter') {
        this.showWindowBtn = false
        if (ev !== 'move' && this.oldCheckId !== item.id) {
          this.alertMapData = []
          if (this.selectedItem.mapLevel === 'country') {
            this.alertMapData = _.cloneDeep(this.selectedItem.chartData)
            this.selectedPositn = _.map(this.alertMapData, 'name')
          }
        }
        if (!window.event.ctrlKey && this.oldCheckId !== item.id) {
          // this.oldCheckId = item.id
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
      if (this.selectedItem.chartType === 'v-map' || this.selectedItem.chartType === 'NewVMap') {
        this.showWindowBtn = false
        // 这里是不是少了点什么
        if (!window.event.ctrlKey && this.oldCheckId !== item.id) {
          this.editPieces = JSON.parse(JSON.stringify(this.selectedItem.piecesData))
          this.editPiecesCopy = JSON.parse(JSON.stringify(this.selectedItem.piecesData)) // 副本
          // 地图元件重新加载右边的区域数据
          // this.oldCheckId = item.id
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
          } else if (this.selectedItem.mapLevel === 'city') {
            this.getMapData(this.selectedItem.provinceCode).then((data) => {
              this.cityArr = data
            })
            this.getMapData(this.selectedItem.cityCode).then((data) => {
              this.areaArr = data
              this.chartDataToMap()
            })
          } else if (this.selectedItem.mapLevel === 'county') {
            this.getMapData(this.selectedItem.provinceCode).then((data) => {
              this.cityArr = data
            })
            this.getMapData(this.selectedItem.cityCode).then((data) => {
              this.countyArr = data
            })
            this.axios.get('/leaderview/home/getJson', {
              params: {
                name: this.selectedItem.countyCode
              }
            }).then((data) => {
              let url = data.obj['文件路径']
              this.axios.get('/' + url).then((data) => {
                this.areaArr = this.initMapData(data)
                if (this.selectedItem.chartType === 'v-map' || this.selectedItem.chartType === 'NewVMap') {
                  this.initLevelData()
                }
                if (this.selectedItem.chartType === 'v-scatter' || this.selectedItem.chartType === 'NewScatter') {
                  if (this.selfMapLevel && id) {
                    this.clearAlertMap()
                  }
                }
                this.chartDataToMap()
              }).catch((err) => {
                if (err) {
                  this.areaArr = []
                  this.chartDataToMap()
                }
              })
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
        if (ev === 'down' && item.ctDataSource !== 'static' && !this.showStyleTab) {
          this.handleHost()
          this.getUrlByType(true) // 优化单击元件时的选中延迟
        }
      }
      if (!gbs.inDev) {
        this.$nextTick(function () {
          titleShow('bottom', $('.e-legend'))
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
    childSelect(item, childId, index) {
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
      if (this.selectedItem.chartType === 'v-scatter' || this.selectedItem.chartType === 'NewScatter') {
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
      if (this.selectedItem.chartType === 'v-map' || this.selectedItem.chartType === 'NewVMap') {
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
      this.showStyleTab = true // 这样会快一些，不会被延迟
      // this.$nextTick(() => {
      //   if (!this.showStyleTab && this.selectedItem.ctDataSource === 'system') {
      //     this.getUrlByType(true)
      //   }
      // })
    },
    setMaxItemInfo() {
      if (this.chooseIndexs.concat(this.chooseCompIndexs).length <= 1) {
        return
      }
      var _this = this
      this.chooseIndexs.forEach((i) => {
        if (Number(_this.chartNum[i].x) + Number(_this.chartNum[i].width) >= Number(_this.minXItem.maxW) + Number(_this.minXItem.x) + Number(_this.minXItem.gapX)) {
          _this.minXItem.maxW = _this.chartNum[i].width
          _this.minXItem.gapX = _this.chartNum[i].x - _this.minXItem.x
          _this.aroundItem.maxX = Number(_this.chartNum[i].x) + Number(_this.chartNum[i].width)
        }
        if (Number(_this.chartNum[i].y) + Number(_this.chartNum[i].height) >= Number(_this.minXItem.maxH) + Number(_this.minXItem.y) + Number(_this.minXItem.gapY)) {
          _this.minXItem.maxH = _this.chartNum[i].height // 最远元素的y
          _this.minXItem.gapY = _this.chartNum[i].y - _this.minXItem.y
          _this.aroundItem.maxY = Number(_this.chartNum[i].y) + Number(_this.chartNum[i].height)
        }
      })
      this.chooseCompIndexs.forEach((i) => {
        if (Number(_this.combinList[i].x) + Number(_this.combinList[i].width) >= Number(_this.minXItem.maxW) + Number(_this.minXItem.x) + Number(_this.minXItem.gapX)) {
          _this.minXItem.maxW = _this.combinList[i].width
          _this.minXItem.gapX = _this.combinList[i].x - _this.minXItem.x
          _this.aroundItem.maxX = Number(_this.combinList[i].x) + Number(_this.combinList[i].width)
        }
        if (Number(_this.combinList[i].y) + Number(_this.combinList[i].height) >= Number(_this.minXItem.maxH) + Number(_this.minXItem.y) + Number(_this.minXItem.gapY)) {
          _this.minXItem.maxH = _this.combinList[i].height // 最远元素的y
          _this.minXItem.gapY = _this.combinList[i].y - _this.minXItem.y
          _this.aroundItem.maxY = Number(_this.combinList[i].y) + Number(_this.combinList[i].height)
        }
      })
      this.changeLimitItem(this.aroundItem)
    },
    updateMinXitem: function () {
      var _this = this
      var minIndex = _.minBy(this.chooseIndexs, function (i) { return _this.chartNum[i].x })
      var minCompIndex = _.minBy(this.chooseCompIndexs, function (i) { return _this.combinList[i].x })
      if (minIndex === undefined && minCompIndex === undefined) {
        return
      }
      var minYIndex = _.minBy(this.chooseIndexs, function (i) { return _this.chartNum[i].y })
      var minYCompIndex = _.minBy(this.chooseCompIndexs, function (i) { return _this.combinList[i].y })
      if (minYCompIndex === undefined) {
        var minY = this.chartNum[minYIndex].y
      } else if (minYIndex === undefined) {
        minY = this.combinList[minYCompIndex].y
      } else {
        minY = this.chartNum[minYIndex].y > this.combinList[minYCompIndex].y ? this.combinList[minYCompIndex].y : this.chartNum[minYIndex].y
      }
      if (minCompIndex === undefined) {
        this.changeMinXitem(this.chartNum[minIndex].x, this.chartNum[minIndex].y, this.chartNum[minIndex].id, minY)
        this.setMaxItemInfo() // 更新最远位置
        return
      }
      if (minIndex === undefined) {
        this.changeMinXitem(this.combinList[minCompIndex].x, this.combinList[minCompIndex].y, this.combinList[minCompIndex].id, minY)
        this.setMaxItemInfo() // 更新最远位置
        return
      }
      if (this.chartNum[minIndex].x < this.combinList[minCompIndex].x) {
        this.changeMinXitem(this.chartNum[minIndex].x, this.chartNum[minIndex].y, this.chartNum[minIndex].id, minY)
      } else {
        this.changeMinXitem(this.combinList[minCompIndex].x, this.combinList[minCompIndex].y, this.combinList[minCompIndex].id, minY)
      }
      this.setMaxItemInfo() // 更新最远位置
    },
    changeMinXitem: function (x, y, id, minY) {
      this.aroundItem.minX = x
      this.aroundItem.minY = minY
      this.changeLimitItem(this.aroundItem)
      this.minXItem.x = x
      this.minXItem.y = y
      if (id) {
        this.minXItem.id = id
      }
      this.selectArea.left = x
      this.selectArea.top = y
    },

    // 多选时改变x，y位移
    changeTarget: function (xy) {
      var left = 'left'
      var width = 'width'
      var maxW = 'maxW'
      var gapX = 'gapX'
      var maxX = 'maxX'
      var minX = 'minX'
      if (xy === 'y') {
        left = 'top'
        width = 'height'
        maxW = 'maxH'
        gapX = 'gapY'
        maxX = 'maxY'
        minX = 'minY'
      }
      var allowOverflow = baseData.allowOverflow // 可提取为可配置变量
      if (this.minXItem[xy] < -allowOverflow) {
        this.minXItem[xy] = -allowOverflow
      }
      // 右下边界的处理
      // console.log(this.minXItem[maxW], this.minXItem[gapX], this.minXItem[xy])
      if (Number(this.minXItem[maxW]) + Number(this.minXItem[gapX]) + Number(this.minXItem[xy]) > Number(this.paintObj[width]) + allowOverflow) {
        this.minXItem[xy] = Number(this.paintObj[width]) + allowOverflow - (Number(this.minXItem[maxW]) + Number(this.minXItem[gapX]))
        this.aroundItem[maxX] = Number(this.paintObj[width]) + allowOverflow // 最大值
      }
      // 上边界的处理
      var changes = parseInt(this.minXItem[xy] - this.selectArea[left])
      this.aroundItem[maxX] = Number(this.aroundItem[maxX]) + changes
      this.aroundItem[minX] = Number(this.aroundItem[minX]) + changes
      if (this.aroundItem[minX] < -allowOverflow) {
        this.aroundItem[minX] = -allowOverflow
      }
      this.changeLimitItem(this.aroundItem)
      if (false && window.event.ctrlKey) {
      } else {
        this.chooseIndexs.forEach((i) => {
          if (Number(this.chartNum[i][xy]) + changes <= -this.allowOverflow) {
            this.chartNum[i][xy] = -this.allowOverflow
          } else {
            this.chartNum[i][xy] = Number(this.chartNum[i][xy]) + changes
          }
        })
        this.chooseCompIndexs.forEach((i) => {
          if (Number(this.combinList[i][xy]) + changes <= -this.allowOverflow) {
            this.combinList[i][xy] = -this.allowOverflow
          } else {
            this.combinList[i][xy] = Number(this.combinList[i][xy]) + changes
          }
        })
      }
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
        _right = Number(arr[0].x) + Number(arr[0].width)
      var topIndex = 0,
        _top = arr[0].y
      var bottomIndex = 0,
        _bottom = Number(arr[0].y) + Number(arr[0].height)
      // var _index = 500
      var _index = this.maxIndex // 没有图层属性的旧数据
      var comZindex = [arr[0].zIndex] // 选中元素的图层
      var minIndexItem = _.minBy(arr, function (item) { return item.zIndex }) // 旧数据会返回undefined
      // var maxIndexItem = _.maxBy(arr, function (item) { return item.zIndex })
      // console.log(minIndexItem.zIndex, maxIndexItem.zIndex)
      if (minIndexItem) {
        _index = minIndexItem.zIndex
      }
      // 获取合并之后的组件的范围
      for (var i = 1, len = arr.length; i < len; i++) {
        comZindex.push(arr[i].zIndex)
        if (arr[i].x < _left) {
          _left = arr[i].x
          leftIndex = i
        }
        if (arr[i].y < _top) {
          _top = arr[i].y
          topIndex = i
        }
        if (Number(arr[i].x) + Number(arr[i].width) > _right) {
          _right = Number(arr[i].x) + Number(arr[i].width)
          rightIndex = i
        }
        if (Number(arr[i].y) + Number(arr[i].height) > _bottom) {
          _bottom = Number(arr[i].y) + Number(arr[i].height)
          bottomIndex = i
        }
        // if (arr[i].zIndex && arr[i].zIndex < _index) {
        //   _index = arr[i].zIndex
        // }
      }
      comZindex.sort() // 对图层进行从小到大排序
      // 为每一个内部组件重新计算相对位置
      for (var i = 0, len = arr.length; i < len; i++) {
        // arr[i].left = arr[i].x - _left
        // arr[i].top = arr[i].y - _top

        arr[i].x = arr[i].x - _left
        arr[i].y = arr[i].y - _top
        arr[i].zIndex = comZindex.indexOf(arr[i].zIndex) + 1
        arr[i].slted = false
        arr[i].oldX = arr[i].x
        arr[i].oldY = arr[i].y
        arr[i].oldW = arr[i].width
        arr[i].oldH = arr[i].height
      }
      var newObj = $.extend(
        true, {}, {
        id: new Date().getTime() + parseInt(Math.random() * 10000),
        ctLegendShow: 'true',
        x: _left,
        y: _top,
        width: _right - _left,
        oldWidth: _right - _left,
        height: _bottom - _top,
        oldHeight: _bottom - _top,
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
      comZindex.shift() // 删除第一个
      this.composeChgIndex(comZindex) // 改变其它元件的图层

      this.selectedItem = this.combinList[this.combinList.length - 1]
      this.testObj = JSON.parse(JSON.stringify(this.selectedItem))
      this.chooseCompIndexs = [this.combinList.length - 1] // 设置右边的值
      if (this.selectArea.choose) {
        this.selectArea.choose = false
        $('.tempDiv').remove()
      }
    },
    composeChgIndex: function (indexArr) {
      indexArr = indexArr.map((item, i) => {
        return item - i
      })
      for (let y = 0, len = indexArr.length; y < len; y++) {
        let z = indexArr[y]
        if (z) {
          for (let x = 0, xLen = this.chartNum.length; x < xLen; x++) {
            if (this.chartNum[x].zIndex > z) {
              this.chartNum[x].zIndex -= 1
            }
          }
          for (let x = 0, xLen = this.combinList.length; x < xLen; x++) {
            if (this.combinList[x].zIndex > z) {
              this.combinList[x].zIndex -= 1
            }
          }
        }
        this.maxIndex--
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
    itemIndexAdd: function (index, zIndex, len) {
      // 组合元素的index， 该组合元素的zIndex
      this.chartNum.forEach((item) => {
        if (item.zIndex > zIndex) {
          item.zIndex += len
        }
      })
      this.combinList.forEach((item, i) => {
        if (index !== i && item.zIndex > zIndex) {
          item.zIndex += len
        }
      })
      this.maxIndex += len
    },
    // 取消组合
    itemSplit: function () {
      this.saveHistory()
      var index = this.selectedIndex
      // var tempArr = _.sortBy(this.combinList[index].child, function (o) { return o.zIndex })
      var tempArr = this.combinList[index].child
      var length = tempArr.length
      var zIndex = this.combinList[index].zIndex
      this.itemIndexAdd(index, zIndex, length - 1)
      // 图层比他大的全部加1
      this.chooseIndexs = []
      tempArr.forEach((item, i) => {
        // item.x = parseInt((item.left + this.combinList[index].x) * this.combinList[index].sacleX)
        // item.y = parseInt((item.top + this.combinList[index].y) * this.combinList[index].sacleY)
        item.zIndex = item.zIndex + zIndex - 1
        item.x = item.x + this.combinList[index].x
        item.y = item.y + this.combinList[index].y
        item.id = item.id + parseInt(Math.random() * 1000) // 复制的元素id一样
        item.slted = true
        delete item.oldH
        delete item.oldW
        delete item.oldX
        delete item.oldY
        this.chartNum.push(item)
        this.chooseIndexs.push(this.chartNum.length - 1)
      })
      this.combinList.splice(index, 1)
      this.chooseCompIndexs = []
    },
    // 下移
    downward: function () {
      this.saveHistory()
      if (this.childResize) {
        var z = this.selectedItem.zIndex
        if (z > 1) {
          let chgIndex = _.findIndex(this.combinList[this.parentId].child, function (i) { return i.zIndex === z - 1 })
          let chgItem = this.combinList[this.parentId].child[chgIndex]
          this.$set(chgItem, 'zIndex', z)
          this.$set(this.selectedItem, 'zIndex', z - 1)
        }
      } else {
        var z = this.selectedItem.zIndex || 500
        if (z > this.minIndex) {
          let chgIndex = _.findIndex(this.chartNum, function (i) { return i.zIndex === z - 1 })
          let chgItem = {}
          if (chgIndex === -1) {
            chgIndex = _.findIndex(this.combinList, function (i) { return i.zIndex === z - 1 })
            chgItem = this.combinList[chgIndex]
          } else {
            chgItem = this.chartNum[chgIndex]
          }
          this.$set(chgItem, 'zIndex', z)
          this.$set(this.selectedItem, 'zIndex', z - 1)
        }
      }
    },
    // 上移
    upward: function () {
      this.saveHistory()
      if (this.childResize) {
        var maxIndex = this.combinList[this.parentId].child.length
        var z = this.selectedItem.zIndex
        if (z < maxIndex) {
          let chgIndex = _.findIndex(this.combinList[this.parentId].child, function (i) { return i.zIndex === z + 1 })
          let chgItem = this.combinList[this.parentId].child[chgIndex]
          this.$set(chgItem, 'zIndex', z)
          this.$set(this.selectedItem, 'zIndex', z + 1)
        }
      } else {
        var z = this.selectedItem.zIndex || 500
        if (z < this.maxIndex) {
          let chgIndex = _.findIndex(this.chartNum, function (i) { return i.zIndex === z + 1 })
          let chgItem = {}
          if (chgIndex === -1) {
            chgIndex = _.findIndex(this.combinList, function (i) { return i.zIndex === z + 1 })
            chgItem = this.combinList[chgIndex]
          } else {
            chgItem = this.chartNum[chgIndex]
          }
          this.$set(chgItem, 'zIndex', z)
          this.$set(this.selectedItem, 'zIndex', z + 1)
        }
      }
    },
    toBottom: function () {
      this.saveHistory()
      if (this.childResize) {
        var z = this.selectedItem.zIndex
        if (z && z > 1) {
          let tempArr = this.combinList[this.parentId].child
          tempArr.forEach((item) => {
            if (item.zIndex < z) {
              item.zIndex++
            }
          })
          this.$set(this.selectedItem, 'zIndex', 1)
        }
      } else {
        var z = this.selectedItem.zIndex
        if (z) {
          this.chartNum.forEach((item) => {
            if (item.zIndex < z) {
              item.zIndex++
            }
          })
          this.combinList.forEach((item) => {
            if (item.zIndex < z) {
              item.zIndex++
            }
          })
          this.$set(this.selectedItem, 'zIndex', this.minIndex)
        }
      }
    },
    toTop: function () {
      this.saveHistory()
      if (this.childResize) {
        var z = this.selectedItem.zIndex
        var maxIndex = this.combinList[this.parentId].child.length
        if (z && z < maxIndex) {
          let tempArr = this.combinList[this.parentId].child
          tempArr.forEach((item) => {
            if (item.zIndex > z) {
              item.zIndex--
            }
          })
          this.$set(this.selectedItem, 'zIndex', maxIndex)
        }
      } else {
        var z = this.selectedItem.zIndex
        if (z) {
          this.chartNum.forEach((item) => {
            if (item.zIndex > z) {
              item.zIndex--
            }
          })
          this.combinList.forEach((item) => {
            if (item.zIndex > z) {
              item.zIndex--
            }
          })
          this.$set(this.selectedItem, 'zIndex', this.maxIndex)
        }
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
      this.saveHistory()
      var itemArr = this.indexToItem()
      var minLeftItem = _.minBy(itemArr, function (item) { return Number(item.x) })
      var minLeft = minLeftItem.x
      for (let i = 0, len = itemArr.length; i < len; i++) {
        itemArr[i].x = minLeft
      }
    },
    // 右对齐
    alignRight: function () {
      this.saveHistory()
      var itemArr = this.indexToItem()
      var maxRightItem = _.maxBy(itemArr, function (item) { return Number(item.x) + Number(item.width) })
      var maxRight = Number(maxRightItem.x) + Number(maxRightItem.width)
      for (let i = 0, len = itemArr.length; i < len; i++) {
        itemArr[i].x = maxRight - itemArr[i].width
      }
    },
    // 上对齐
    alignTop: function () {
      this.saveHistory()
      var itemArr = this.indexToItem()
      var minTopItem = _.minBy(itemArr, function (item) { return Number(item.y) })
      var minTop = minTopItem.y
      for (let i = 0, len = itemArr.length; i < len; i++) {
        itemArr[i].y = minTop
      }
    },
    // 下对齐
    alignBottom: function () {
      var itemArr = this.indexToItem()
      var maxBottomItem = _.maxBy(itemArr, function (item) { return Number(item.y) + Number(item.height) })
      var maxBottom = Number(maxBottomItem.y) + Number(maxBottomItem.height)
      for (let i = 0, len = itemArr.length; i < len; i++) {
        itemArr[i].y = maxBottom - itemArr[i].height
      }
    },
    // 左右居中对齐
    textCenter: function () {
      // 同一中心纵轴
      var itemArr = this.indexToItem()
      var minLeftItem = _.minBy(itemArr, function (item) { return Number(item.x) }) // 左边界
      var maxRightItem = _.maxBy(itemArr, function (item) { return Number(item.x) + Number(item.width) })
      var maxRight = Number(maxRightItem.x) + Number(maxRightItem.width) // 右边界
      var mindItem = (maxRight - minLeftItem.x) / 2 + Number(minLeftItem.x)
      for (let i = 0, len = itemArr.length; i < len; i++) {
        itemArr[i].x = parseInt(mindItem - itemArr[i].width / 2)
      }
    },
    // 垂直居中对齐
    alignCenter: function () {
      // 同一中心横轴
      var itemArr = this.indexToItem()
      var minTopItem = _.minBy(itemArr, function (item) { return Number(item.y) }) // 上边界
      var maxBottomItem = _.maxBy(itemArr, function (item) { return Number(item.y) + Number(item.height) })
      var maxBottom = Number(maxBottomItem.y) + Number(maxBottomItem.height) // 下边界
      var mindItem = (maxBottom - minTopItem.y) / 2 + Number(minTopItem.y)
      for (let i = 0, len = itemArr.length; i < len; i++) {
        itemArr[i].y = parseInt(mindItem - itemArr[i].height / 2)
      }
    },
    // 水平分布
    levelDist: function () {
      var itemArr = this.indexToItem()
      if (itemArr.length <= 2) return // 至少三个元件才执行函数
      var minLeftItem = _.minBy(itemArr, function (item) { return Number(item.x) }) // 左边界
      var minLeft = minLeftItem.x
      var maxRightItem = _.maxBy(itemArr, function (item) { return Number(item.x) + Number(item.width) })
      var maxRight = Number(maxRightItem.x) + Number(maxRightItem.width) // 右边界
      var gapWidth = maxRight - minLeft // 水平分布区域的间距
      var allItemWid = _.sumBy(itemArr, function (item) { return Number(item.width) })
      var gap = parseInt((gapWidth - allItemWid) / (itemArr.length - 1))
      itemArr.sort(function (a, b) {
        var pre = Number(a.x) + Number(a.width)
        var next = Number(b.x) + Number(b.width)
        if (a.x === minLeft) {
          pre = 0 // 设置最小，则x最小的在第一个位置
        }
        return pre - next
      })
      for (let i = 1, len = itemArr.length; i < len - 1; i++) {
        itemArr[i].x = parseInt(Number(itemArr[i - 1].x) + Number(itemArr[i - 1].width) + Number(gap))
      }
    },
    // 垂直分布
    verticalDist: function () {
      var itemArr = this.indexToItem()
      if (itemArr.length <= 2) return // 至少三个元件才执行函数
      var minTopItem = _.minBy(itemArr, function (item) { return Number(item.y) }) // 上边界
      var minTop = minTopItem.y
      var maxBottomItem = _.maxBy(itemArr, function (item) { return Number(item.y) + Number(item.height) })
      var maxBottom = Number(maxBottomItem.y) + Number(maxBottomItem.height) // 右边界
      var gapWidth = maxBottom - minTop // 垂直分布区域的间距
      var allItemWid = _.sumBy(itemArr, function (item) { return Number(item.height) })
      var gap = parseInt((gapWidth - allItemWid) / (itemArr.length - 1))
      itemArr.sort(function (a, b) {
        var pre = Number(a.y) + Number(a.height)
        var next = Number(b.y) + Number(b.height)
        if (a.y === minTop) {
          pre = 0 // 设置最小，则x最小的在第一个位置
        }
        return pre - next
      })
      for (let i = 1, len = itemArr.length; i < len - 1; i++) {
        itemArr[i].y = parseInt(Number(itemArr[i - 1].y) + Number(itemArr[i - 1].height) + Number(gap))
      }
    },
    draged(chgX, chgY, attr) {
      chgX = Number(chgX)
      chgY = Number(chgY)
      // if (this.selectedItem.id === this.minXItem.id) return
      // if (true || window.event.ctrlKey) {
      if (chgX !== 0) {
        if (this.selectedItem.id === this.minXItem.id) {
          this.minXItem.x = Number(attr.left)
        } else {
          this.minXItem.x = Number(this.minXItem.x) + chgX
        }
        this.changeTarget('x')
      }
      if (chgY !== 0) {
        if (this.selectedItem.id === this.minXItem.id) {
          this.minXItem.y = Number(attr.top)
        } else {
          this.minXItem.y = Number(this.minXItem.y) + chgY
        }
        this.changeTarget('y')
      }
      // }
    },
    resized: function (item) {
      // this.moving = true
      if (item.id === this.testObj.id) {
        this.testObj.width = Number(item.width)
        this.testObj.height = Number(item.height)
        this.testObj.x = Number(item.left)
        this.testObj.y = Number(item.top)
      }
      // 组合内部的元件的移动传递一个flag，区别在设置时位移量不能为-20
    },
    resetLine() {
      this.presetLine = []
    },
    resetPaint() {
      this.paintInput.width = 1920
      this.paintInput.height = 1080
      this.paintObj.width = 1920
      this.paintObj.height = 1080
      this.paintObj.bgColor = '#141929'
      this.paintObj.bgImg = ''
      this.paintObj.bgStyle = '3'
      this.paintObj.opacity = 100
      // this.paintObj.showGrid = true
      this.paintObj.scale = 100
      this.paintObj.top = 100
      this.paintObj.left = 100
      this.paintObj.showGrid = false
      this.helpLineColor = '#348cea'
    },
    addColor(index) {
      // 添加颜色
      if (this.selectedItem.ifGradual === 'false') {
        if (!this.selectChange && this.chooseSameFlag) {
          this.chooseIndexs.forEach((i) => {
            this.chartNum[i]['ctColors'].splice(index, 0, '#c23531')
          })
        } else {
          this.selectedItem.ctColors.splice(index, 0, '#c23531')
        }
      } else {
        if (!this.selectChange && this.chooseSameFlag) {
          this.chooseIndexs.forEach((i) => {
            this.chartNum[i]['ctColors'].splice(index, 0, ['#c23531', '#c23531'])
          })
        } else {
          this.selectedItem.ctColors.splice(index, 0, ['#c23531', '#c23531'])
        }
      }
    },
    delColor(index) {
      // 删除自定义颜色
      if (this.selectedItem.ctColors.length === 1) {
        Notification({
          message: '至少配置一种颜色',
          position: 'bottom-right',
          customClass: 'toast toast-info'
        })
        return
      }
      if (!this.selectChange && this.chooseSameFlag) {
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i]['ctColors'].splice(index, 1)
        })
      } else {
        this.selectedItem.ctColors.splice(index, 1)
      }
    },
    moveUp(index) {
      if (index !== 0) {
        if (!this.selectChange && this.chooseSameFlag) {
          this.chooseIndexs.forEach((i) => {
            let tempColor = this.chartNum[i]['ctColors'].splice(index, 1)[0]
            this.chartNum[i]['ctColors'].splice(index - 1, 0, tempColor)
          })
        } else {
          var tempColor = this.selectedItem.ctColors.splice(index, 1)[0]
          this.selectedItem.ctColors.splice(index - 1, 0, tempColor)
        }
      }
    },
    moveDown(index) {
      if (index !== this.selectedItem.ctColors.length - 1) {
        if (!this.selectChange && this.chooseSameFlag) {
          this.chooseIndexs.forEach((i) => {
            let tempColor = this.chartNum[i]['ctColors'].splice(index, 1)[0]
            this.chartNum[i]['ctColors'].splice(index + 1, 0, tempColor)
          })
        } else {
          var tempColor = this.selectedItem.ctColors.splice(index, 1)[0]
          this.selectedItem.ctColors.splice(index + 1, 0, tempColor)
        }
      }
    },
    reverseClr() {
      if (!this.selectChange && this.chooseSameFlag) {
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i]['barClrs'].reverse()
        })
      } else {
        this.selectedItem.barClrs.reverse()
      }
    },
    reverseColor(index) {
      if (!this.selectChange && this.chooseSameFlag) {
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i]['ctColors'][index].reverse()
        })
      } else {
        this.selectedItem.ctColors[index].reverse()
      }
    },
    // 改变数据来源
    chgDataSource: async function ($event, flag) {
      this.handleHost()
      let curKey = this.selectedItem.ctDataSource
      if (['static', 'system'].indexOf(curKey) !== -1) {
        curKey === 'system' ? this.getUrlByType() : this.showWindowBtn = false
      } else { // 第三方系统
        if (!(await checkLogin(this.thirdIpPort))) return false // 登录失败：跳出循环
        this.getUrlByType()
      }
    },
    handleHost() {
      let curKey = this.selectedItem.ctDataSource
      if (['static', 'system'].indexOf(curKey) !== -1) {
        this.curDataHost = gbs.host
        this.isThird = false
      } else { // 第三方系统
        this.isThird = true
        this.thirdIpPort = this.dataSource[curKey] || ''
        this.curDataHost = 'http://' + this.thirdIpPort
      }
      this.changeThirdConf({ isThird: this.isThird, curDataHost: this.curDataHost, thirdIpPort: this.thirdIpPort })
    },
    dealTypeStr: function () {
      // 根据需求传值给后端对应的接口
      var type = this.selectedItem.chartType
      if (this.selectedItem.chartType === 'IntegratedHistogram') {
        return this.selectedItem.barType
      }
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
    // getHcnetData () {
    //   var _this = this
    //   var url = gbs.host + '/monitor/ne/hcnet/tree?hasPermission=true'
    //   $.ajax({
    //     url: url,
    //     type: 'get',
    //     success: function (data) {
    //       _this.AllHcnetData = []
    //       if (data.success) {
    //         data.obj.result.forEach(element => {
    //           if (element.pId === null && element.monitoring) {
    //             _this.AllHcnetData.push(element)
    //           } else {

    //           }
    //         })
    //       } else {
    //         // AllVideoData
    //       }
    //     }
    //   })
    // },
    // getVideoDate () {
    //   var _this = this
    //   var url = gbs.host + `/monitor/hcnet/channelList/${this.selectedItem.HcnetData}?keyWord=&enable=&channelType=&domainId=&id=${this.selectedItem.HcnetData}&ip=`
    //   //
    //   $.ajax({
    //     url: url,
    //     type: 'get',
    //     success: function (data) {
    //       _this.AllVideoData = []
    //       if (data.success && data.obj !== null) {
    //         data.obj.indicatorValue.object.forEach(element => {
    //           if (element.enable === '在线') {
    //             _this.AllVideoData.push(element)
    //           } else {
    //           }
    //         })
    //       } else {
    //         // AllVideoData
    //       }
    //     }
    //   })
    // },
    // 根据选中图标类型获取可以配置的接口
    getUrlByType(flag) {
      var _this = this
      newAjax({
        url: gbs.host + '/leaderview/home/getUrl',
        // url: _this.curDataHost + '/leaderview/home/getUrl',
        data: { typeStr: this.dealTypeStr() },
        async: false,
        success: function (data) {
          let arr = data.obj || []
          if (_this.isThird) { // 第三方数据：把host和_token_u_拼接到url上去
            arr = arr.filter(d => {
              d.url = _this.curDataHost + (/^\//.test(d.url) ? d.url : '/' + d.url) + (d.url.indexOf('?') !== -1 ? '&' : '?') + '_token_u_=token'
              return d
            })
          }
          _this.$set(_this.syst, 'urlSel', arr)
          _this.$nextTick(function () {
            _this.syst.urlSel.length && _this.chgUrl(flag)
          })
        },
        errorCallback: async function (xhr) {
          // if ( _this.isThird && xhr.status === 776 ) { //第三方登录过期->重新登录->重新请求当前接口
          //   await checkLogin(_this.thirdIpPort) && _this.getUrlByType (flag)
          //   return false
          // }
          Notification({
            message: '连接错误！',
            position: 'bottom-right',
            customClass: 'toast toast-error'
          })
        }
      })
    },
    // 改变接口下拉框，需要根据index更新当前选中接口数据,及下面的参数
    chgUrl(flag) {
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
        if (!flag) {
          index = 0
        }
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
        if (!api) return
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
            // _this.sentReq(d, postData, selectedP)  //发送请求
            newAjax({
              url: reg.test(d.dataUrl) ? gbs.host + d.dataUrl : gbs.host + '/' + d.dataUrl,
              async: false,
              data: postData,
              type: d.method || 'get',
              success: function (data) {
                d.data = data.obj || []
                if (data.msg === 'windows') {
                  if (_this.isArray(data.obj) && data.obj.length > 0) {
                    _this.syst.windowObj = data.obj
                    // if (!_this.isArray(_this.syst.windowData) || _this.syst.windowData.length < 1) {
                    _this.syst.windowData = _this.initWindowData(data.obj)
                    // 初始化弹窗赋值并展示
                    // }
                    _this.showWindowBtn = true
                  } else {
                    _this.showWindowBtn = false
                  }
                }
                $.isEmptyObject(selectedP) && _this.setFirstV(d)
              },
              errorCallback: function (xhr) {
                Notification({
                  message: '连接错误！',
                  position: 'bottom-right',
                  customClass: 'toast toast-error'
                })
              }
            })
          }
          first && _this.setFirstV(d)
        })
        _this.syst.curUrl = api
        if (!gbs.inDev) {
          this.$nextTick(() => {
            titleShow('bottom', $('.e-legend'))
          })
        }
      })
    },
    // 发送接口下拉框改变时请求
    openMapChange() {
      this.MapChange = !this.MapChange
      if (!this.MapChange) {
        this.tapsStation = 'center'
        document.querySelector('.archive').style.order = 0
        document.querySelector('#centerMapBox').style.order = 0
      }
    },
    // 发送接口下拉框改变时请求
    openHawkEye() {
      if (!this.ShowHawkEye) {
        this.paintObj.scale = 100
      }
      this.ShowHawkEye = !this.ShowHawkEye
    },
    closeHawkEye() {
      this.ShowHawkEye = false
    },
    upload() {
      document.getElementById('uploadZip').click()
    },
    cancel() {
      this.showUpload = false
    },
    openUpload() {
      this.showUpload = true
    },
    getZip(e) {
      if (!e.target.files[0]) {
        return
      }
      const file = e.target.files[0]
      this.importModelForm.fileName = file.name

      this.TDmodelFormData.set('file', file)
    },
    getModelFun() {
      this.axios.get('/leaderview/home/findAllModles').then((data) => {
        this.gltfNameArry = data.obj || []
      })
    },
    sureUpload: function () {
      var _this = this
      this.TDmodelFormData.set('name', this.importModelForm.name)
      this.axios.post('/leaderview/home/upload3dModel', this.TDmodelFormData).then((res) => {
        if (res.success) {
          Notification({
            message: res.msg,
            position: 'bottom-right',
            customClass: 'toast toast-success'
          })
          _this.showUpload = false
          _this.getModelFun()
        } else {
          Notification({
            message: res.msg,
            position: 'bottom-right',
            customClass: 'toast toast-error'
          })
        }
      })
    },
    sentReq(d, postData, selectedP) {
      let _this = this
      $.ajax({
        url: /^\//.test(d.dataUrl) ? _this.curDataHost + d.dataUrl : _this.curDataHost + '/' + d.dataUrl,
        async: false,
        data: postData,
        type: d.method || 'get',
        success: function (data) {
          d.data = data.obj || []
          if (data.msg === 'windows') {
            if (_this.isArray(data.obj) && data.obj.length > 0) {
              _this.syst.windowObj = data.obj
              // if (!_this.isArray(_this.syst.windowData) || _this.syst.windowData.length < 1) {
              _this.syst.windowData = _this.initWindowData(data.obj)
              // 初始化弹窗赋值并展示
              // }
              _this.showWindowBtn = true
            } else {
              _this.showWindowBtn = false
            }
          }
          $.isEmptyObject(selectedP) && _this.setFirstV(d)
        },
        error: async function (xhr) {
          if (_this.isThird && xhr.status === 776) { // 第三方登录过期->重新登录->重新请求当前接口
            await checkLogin(_this.thirdIpPort) && _this.sentReq(d, postData, selectedP)
            return false
          }
          Notification({
            message: '连接错误！',
            position: 'bottom-right',
            customClass: 'toast toast-error'
          })
        }
      })
    },
    setFirstV: function (d) {
      this.syst.curConf.params[d.key] = !d.notNull
        ? null
        : (d.data.length && d.data[0].value) || null
    },
    chgSelects(v) {
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
          // _this.sentSelectsReq(d, postData)  //发送请求
          newAjax({
            url: reg.test(d.dataUrl) ? gbs.host + d.dataUrl : gbs.host + '/' + d.dataUrl,
            async: false,
            data: postData,
            success: function (data) {
              data.obj = data.obj || []
              if (_this.isArray(d.data)) {
                d.data.splice(0, d.data.length)
              }
              d.data = data.obj
              // 判断是否是获取的多资源的部件和属性
              if (data.msg === 'windows') {
                _this.getWindowObj(data)
              }
              //  console.log(v.key,d.dataUrl,postData);
            }
          })
          _this.$set(_this.syst.curUrl, grp.index(), $.extend(true, {}, d))
        }
      })
    },
    // 发送下拉选择改变时请求
    sentSelectsReq(d, postData) {
      let _this = this
      $.ajax({
        // url: reg.test(d.dataUrl) ? gbs.host + d.dataUrl : gbs.host + '/' + d.dataUrl,
        url: /^\//.test(d.dataUrl) ? _this.curDataHost + d.dataUrl : _this.curDataHost + '/' + d.dataUrl,
        async: false,
        data: postData,
        success: function (data) {
          data.obj = data.obj || []
          if (_this.isArray(d.data)) {
            d.data.splice(0, d.data.length)
          }
          d.data = data.obj
          // 判断是否是获取的多资源的部件和属性
          if (data.msg === 'windows') {
            _this.getWindowObj(data)
          }
          //  console.log(v.key,d.dataUrl,postData);
        },
        error: async function (xhr) {
          if (_this.isThird && xhr.status === 776) { // 第三方登录过期->重新登录->重新请求当前接口
            await checkLogin(_this.thirdIpPort) && _this.sentSelectsReq(d, postData)
          }
        }
      })
    },
    getWindowObj(data) {
      this.showWindowBtn = false
      if (this.isArray(data.obj) && data.obj.length > 0) {
        // data.obj[0].hasOwnProperty('ne') && data.obj[0].hasOwnProperty('fields')
        // if (data.msg === 'windows') {
        this.syst.windowObj = data.obj
        this.syst.windowData = []
        this.syst.windowData = this.initWindowData(data.obj)
        this.syst.curConf.params.windows = JSON.stringify(this.syst.windowData)
        this.showWindowBtn = true
        // }
      }
    },
    initWindowData(data) {
      var obj = []
      data.forEach((item, index) => {
        obj.push({
          'indicator': item.indicator.value,
          'fields': (this.isArray(item.fields) && item.fields.length > 0) ? item.fields[0].value : null,
          'ne': []
        })
        item.ne.forEach((list) => {
          obj[index].ne.push({
            'id': list.id,
            'multipleComponent': list.multipleComponent,
            'component': (this.isArray(list.component) && list.component.length > 0) ? list.component[0].value : null
          })
        })
      })
      return obj
    },
    getWindowData() {
      $('#partsEdit-modal').modal('show')
    },
    getUrlData() {
      // 根据接口获取数据更新图表
      var _this = this
      var curConf = this.syst.curConf
      var param = curConf.params
      if (this.showWindowBtn && param.hasOwnProperty('windows')) {
        param.windows = JSON.stringify(this.syst.windowData) // 保存弹窗填写的数据
      } else if (param.windows) {
        param.windows = null
      }
      if (this.selectedItem.chartType === 'topo') {
        this.saveTopoConf(param, curConf)
        if (this.selectedItem.tptype !== 'maptp') {
          _this.selectedItem.url = curConf.url
          _this.selectedItem.method = curConf.method
          _this.selectedItem.params = param
          return
        }
      }
      if (this.selectedItem.chartType === 'v-map' || this.selectedItem.chartType === 'v-scatter' || this.selectedItem.chartType === 'NewScatter' || this.selectedItem.chartType === 'NewVMap') {
        let names = _.map(this.areaArr, 'name')
        let areaName = '中国'
        // console.log(names.join(','))
        var _id = -1
        if (this.selectedItem.mapLevel === 'province') {
          _id = _.findIndex(_this.provinceArr, function (o) { return o.value == _this.selectedItem.provinceCode })
          areaName = _this.provinceArr[_id].name
        } else if (this.selectedItem.mapLevel === 'city') {
          _id = _.findIndex(_this.cityArr, function (o) { return o.value == _this.selectedItem.cityCode })
          areaName = _this.cityArr[_id].name
        }
        param.names = names.join(',')
        param.areaName = areaName
      }
      var datas = {}
      var reg = /^\//
      $.each(param, function (i, d) {
        datas[i] = $.isArray(d) ? d.join(',') : d
      })
      // if (_this.syst.windowData.length > 0) {
      //   datas.windows = JSON.stringify(_this.syst.windowData)
      // }
      this.sentViewReq(curConf, datas, param) // 发送请求
      /*
      $.ajax({
        url: reg.test(curConf.url) ? gbs.host + curConf.url : gbs.host + '/' + curConf.url,
        data: datas,
        type: curConf.method,
        success: function (data) {
          if (data.success) {
            data.obj = data.obj || {}
            if (data.obj.colors) {
              _this.selectedItem.ctColors = data.obj.colors
              _this.selectedItem.colorType = 'defalut'
            } else {
              if (_this.selectedItem.colorType === 'defalut') {
                _this.selectedItem.ctColors = _this.defalutColors.concat()
              }
            }
            if (_this.selectedItem.chartType === 'v-map') {
              _this.selectMapData = data.obj
              _this.mapDataToChart()
              _this.selectedItem.piecesData = JSON.parse(JSON.stringify(_this.editPieces))
            } else {
              _this.selectedItem.chartData = data.obj
            }
            _this.selectedItem.url = curConf.url
            _this.selectedItem.method = curConf.method
            _this.selectedItem.params = param
            if (_this.selectedItem.chartType === 'text' || _this.selectedItem.chartType === 'marquee') {
              _this.selectedItem.ctName = data.obj.info
              if (_this.selectedItem.chartType === 'text') {
                _this.selectedItem.chartData = data.obj
              }
            }
          } else {
              Notification({
                message: data.msg,
                position: 'bottom-right',
                customClass: 'toast toast-info'
              })
          }
        },
        error: function () {
            Notification({
              message: '连接错误！',
              position: 'bottom-right',
              customClass: 'toast toast-error'
            })
        }
      })
      */
    },
    // 发送更新视图的请求
    sentViewReq(curConf, datas, param) {
      let _this = this
      if (_this.selectedItem.chartType === 'JSMpeg') {
        _this.selectedItem.chartData = JSON.stringify(param)
        _this.selectedItem.urlData = _this.syst.curConf.url
      } else {
        $.ajax({
          url: this.isThird ? curConf.url : (/^\//.test(curConf.url) ? gbs.host + curConf.url : gbs.host + '/' + curConf.url),
          data: datas,
          type: curConf.method,
          success: function (data) {
            if (data.success) {
              data.obj = data.obj || {}
              if (data.obj.colors) {
                _this.selectedItem.ctColors = data.obj.colors
                _this.selectedItem.colorType = 'defalut'
              } else {
                if (_this.selectedItem.colorType === 'defalut') {
                  _this.selectedItem.ctColors = _this.defalutColors.concat()
                }
              }
              if (_this.selectedItem.chartType === 'v-map' || _this.selectedItem.chartType === 'NewVMap') {
                _this.selectMapData = data.obj
                _this.mapDataToChart()
                _this.selectedItem.piecesData = JSON.parse(JSON.stringify(_this.editPieces))
              } else if (_this.selectedItem.chartType === 'IntegratedHistogram') {
                if (_this.selectedItem.barType === 'NewHistogram') {
                  _this.selectedItem.chartData1 = data.obj
                }
                if (_this.selectedItem.barType === 'NewGroupHistogram') {
                  _this.selectedItem.chartData2 = data.obj
                }
                if (_this.selectedItem.barType === 'NewGroupLeftHistogram') {
                  _this.selectedItem.chartData3 = data.obj
                }
                if (_this.selectedItem.barType === 'NewBar') {
                  _this.selectedItem.chartData4 = data.obj
                }
              } else {
                _this.selectedItem.chartData = data.obj
              }
              _this.selectedItem.url = curConf.url
              _this.selectedItem.method = curConf.method
              _this.selectedItem.params = param
              if (_this.selectedItem.chartType === 'text' || _this.selectedItem.chartType === 'NewMarquee' || _this.selectedItem.chartType === 'marquee' || _this.selectedItem.chartType === 'NEWtextArea') {
                _this.selectedItem.ctName = data.obj.info
                if (_this.selectedItem.chartType === 'text' || _this.selectedItem.chartType === 'NEWtextArea') {
                  _this.selectedItem.chartData = data.obj
                }
              }
            } else {
              Notification({
                message: data.msg,
                position: 'bottom-right',
                customClass: 'toast toast-info'
              })
            }
          },
          error: async function (xhr) {
            if (_this.isThird && xhr.status === 776) { // 第三方登录过期->重新登录->重新请求当前接口
              await checkLogin(_this.thirdIpPort) && _this.sentViewReq(curConf, datas, param)
              return false
            }
            Notification({
              message: '连接错误！',
              position: 'bottom-right',
              customClass: 'toast toast-error'
            })
          }
        })
      }
    },
    saveTopoConf: function (param, curConf) {
      // 拓扑与其他组件不同，需要特殊处理
      let oldTpid = this.selectedItem.tpId
      if (this.selectedItem.tpId === param.topoId) {
        if (oldTpid) {
          this.$set(this.selectedItem, 'refresh', true)
          this.$nextTick(() => {
            this.selectedItem.refresh = false
          })
        }
      } else {
        if (curConf.url.includes('domainTopo')) {
          this.$set(this.selectedItem, 'tptype', 'domain')
        } else if (curConf.url.includes('business')) {
          this.$set(this.selectedItem, 'tptype', 'business')
        } else {
          this.$set(this.selectedItem, 'tptype', 'maptp')
        }
        this.$set(this.selectedItem, 'tpId', param.topoId)
      }
    },
    // 以下为静态数据的输入校验
    formatJson(a) {
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
      var d = 'randomId_' + parseInt(1E9 * Math.random())
      var b = '' + ('function ' + d + '(root){') + ('return root.' + c + '')
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
    saveDataChange() {
      if (this.historyArr.length === 0) {
        console.log('data change 了0')
        this.saveHistory()
      } else {
        let tempObj = {
          type: 'item',
          chartNum: JSON.stringify(this.chartNum),
          compose: JSON.stringify(this.combinList)
        }
        if (!_.isEqual(tempObj, this.historyArr[this.historyArr.length - 1])) {
          console.log('data change 了')
          this.saveHistory()
        }
      }
    },
    dataChange() {
      this.saveDataChange() // 系统数据存历史
      if (this.selectedItem.ctDataSource !== 'static') {
        this.getUrlData()
      } else if (this.selectedItem.chartType === 'Ueditor') {
        let content = this.$refs.ue.getUEContent()
        this.selectedItem.chartData = content
      } else if (this.selectedItem.chartType === 'v-map' || this.selectedItem.chartType === 'NewVMap') {
        this.mapDataToChart()
        this.selectedItem.piecesData = JSON.parse(JSON.stringify(this.editPieces))
        // this.selectedItem.chartData = JSON.parse(JSON.stringify(this.selectMapData))
      } else if (this.selectedItem.chartType === 'v-scatter' || this.selectedItem.chartType === 'NewScatter') {
        this.selectedItem.chartData = JSON.parse(JSON.stringify(this.alertMapData))
      } else if (this.selectedItem.chartType === 'text' || this.selectedItem.chartType === 'marquee' || this.selectedItem.chartType === 'NewMarquee' || this.selectedItem.chartType === 'NEWtextArea') {
        console.log(this.$refs.textarea.innerText)
        this.selectedItem.ctName = this.$refs.textarea.innerText
        // this.$refs.textarea.innerText = this.selectedItem.ctName
      } else {
        var textData = this.$refs.textareaData.innerText
        var reg = /^\{[\s\S]*\}$/
        // 先判断是{}类型的对象，而不是new Object
        if (reg.test(textData.trim())) {
          try {
            if (this.selectedItem.chartType === 'IntegratedHistogram') {
              if (this.selectedItem.barType === 'NewHistogram') {
                this.selectedItem.chartData1 = this.formatJson(textData)
              }
              if (this.selectedItem.barType === 'NewGroupHistogram') {
                this.selectedItem.chartData2 = this.formatJson(textData)
              }
              if (this.selectedItem.barType === 'NewGroupLeftHistogram') {
                this.selectedItem.chartData3 = this.formatJson(textData)
              }
              if (this.selectedItem.barType === 'NewBar') {
                this.selectedItem.chartData4 = this.formatJson(textData)
              }
            } else {
              this.selectedItem.chartData = this.formatJson(textData)
            }
          } catch (err) {
            Notification({
              message: '请输入正确的JSON格式的数据',
              position: 'bottom-right',
              customClass: 'toast toast-info'
            })
          }
        } else {
          if (this.selectedItem.barType === 'NewHistogram' && Array.isArray(JSON.parse(textData))) {
            this.selectedItem.chartData1 = this.formatJson(textData)
          } else if (this.selectedItem.chartType === 'NewTable' && Array.isArray(JSON.parse(textData))) {
            this.selectedItem.chartData = this.formatJson(textData)
          } else {
            Notification({
              message: '请输入正确的JSON格式的数据',
              position: 'bottom-right',
              customClass: 'toast toast-info'
            })
          }
        }
      }
    },
    videoChange: function () {
      if (this.selectedItem.videoType === 'url') {
        this.selectedItem.videoSrc = this.tempVideoUrl
      }
    },
    saveConf: function (event, cb) {
      // 保存
      if (!(!this.widthVali.isShowError &&
        !this.heightVali.isShowError && !this.xVali.isShowError &&
        !this.yVali.isShowError && !this.proHeightErr && !this.radiusErr &&
        !this.freshVali
      )) {
        Notification({
          message: '请填写正确的配置信息',
          position: 'bottom-right',
          customClass: 'toast toast-info'
        })
        return
      }
      $('#lead-screen').show()
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
      this.paintObj.showGrid = false
      $('#mainEdit-edit .main_topo')
        .find('svg')
        .css('opacity', 0)
      $('#mainEdit-edit .main_topo').append(
        $('<img>')
          .addClass('monitp')
          // .attr('src', gbs.host + '/leaderview/border/tpstander.png')
          .attr('src', gbs.host + '/leaderview/border/topoBg.png')
          .css({
            width: '100%',
            height: '100%',
            position: 'absolute',
            top: 0,
            left: 0
          })
      )
      $('#mainEdit-edit .main_video')
        .find('video')
        .css('opacity', 0)
      $('#mainEdit-edit .main_video').append(
        $('<img>')
          .addClass('monitp')
          // .attr('src', gbs.host + '/leaderview/border/videoBg.png')
          .attr('src', gbs.host + '/leaderview/border/videoBg2.png')
          .css({
            width: '100%',
            height: '100%',
            position: 'absolute',
            top: 0,
            left: 0
          })
      )
      $('#mainEdit-edit .JSMpeg')
        .find('canvas')
        .css('opacity', 0)
      $('#mainEdit-edit .JSMpeg').append(
        $('<img>')
          .addClass('monitp')
          // .attr('src', gbs.host + '/leaderview/border/videoBg.png')
          .attr('src', gbs.host + '/leaderview/border/videoBg2.png')
          .css({
            width: '100%',
            height: '100%',
            position: 'absolute',
            top: 0,
            left: 0
          })
      )
      $('.getPicSpan').show()
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
          $('#mainEdit-edit .main_video')
            .find('video')
            .css('opacity', 1)
          $('#mainEdit-edit .JSMpeg')
            .find('canvas')
            .css('opacity', 1)
          $('.getPicSpan').hide()
          $('#mainEdit-edit .monitp').remove()
        }
      }).then(function (canvas) {
        // $('#mainEdit-edit .monitp').remove()
        document.body.appendChild(canvas)
        var dataUrl = canvas.toDataURL('image/png')
        console.log($(canvas))
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
        cThis.uploadFile('img', formdata, function (data) {
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
              // _canvas.style.background = 'transparent'
              $('#lead-screen').hide()
              typeof cb === 'function' && cb()
              if (res.success) {
                Notification({
                  message: '操作成功！',
                  position: 'bottom-right',
                  customClass: 'toast toast-success'
                })
              }
            })
        })
      })
    },
    uploadFile: function (type, formData, cb) {
      var _url = gbs.host + '/leaderview/home/file/upload'
      if (type === 'video') {
        _url = gbs.host + '/leaderview/home/file/uploadVideoFile'
      }
      $.ajax({
        url: _url,
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
            // $("#lead-screen").hide()
            Notification({
              message: data.msg,
              position: 'bottom-right',
              customClass: 'toast toast-error'
            })
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
        Notification({
          message: '请填写正确的配置信息',
          position: 'bottom-right',
          customClass: 'toast toast-info'
        })
        return
      }
      this.pageData = JSON.stringify(this.chartNum)
      this.composeData = JSON.stringify(this.combinList)
      this.viewKey = new Date().getTime() + parseInt(Math.random() * 10)
      this.$nextTick(() => {
        // this.viewPage = true
        this.$refs.PreView.reNewOne()
      })
    },
    preBack: function () {
      this.showNextType = -1
      this.showBackModal = true
    },
    preOther: function (data) {
      this.showNextType = data
      this.showBackModal = true
    },
    back: function (data) {
      this.showBackModal = false
      if (data && data.sure === '1') {
        this.resetPaint()
        this.ShowHawkEye = false
        if (this.showNextType === 0) {
          this.upOnePage()
        } else if (this.showNextType === 1) {
          this.downOnePage()
        } else {
          if (this.editId) {
            this.$router.push('/')
          } else {
            this.$router.push('/editPage')
          }
        }
      }
    },
    /* 统一右键 */
    context: function (index, ev, type) {
      // if (type !=='compose') {

      // }
      let _top = ev.pageY
      if (_top > document.body.clientHeight - 188) {
        _top = document.body.clientHeight - 188
      }
      $(this.$refs.copyMenu).toggle(false)
      if (!this.ShowHawkEye) {
        this.HawkEyeStyle.top = _top
        this.HawkEyeStyle.left = ev.pageX
      }
      $(this.$refs.contextMenu)
        .css({
          left: ev.pageX,
          top: _top
        })
        .toggle(true)
      this.selectedIndex = index
    },
    // 组合右键 未使用该方法
    composeMenu: function (index, ev) {
      // console.log(ev);
      $(this.$refs.copyMenu).toggle(false)
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
      // 注册框选事件
      var stateBar = document.getElementById('chooseWrap')
      stateBar.addEventListener('mousedown', _this.userChoose)
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
      if (this.ifSameItems()) {
        this.selectedItem = this.chartNum[this.chooseIndexs[this.chooseIndexs.length - 1]]
      }
    },
    itemInChoose: function (left, right, top, bottom, item) {
      // 判断是否在框选区域内
      if (item.x < left || item.y < top || item.x + item.width > right || item.y + item.height > bottom) {
        return false
      }
      return true
    },
    clearAll: function () {
      $.each(this.chartNum, function (i, d) {
        d.slted = false
      })
      $.each(this.combinList, function (i, d) {
        d.slted = false
      })
      this.chooseIndexs = []
      this.selectedItem = {}
      this.selectedIndex = null
    },
    userChoose: function (e) {
      if (e.button === 0) {
        this.clearAll() // 取消所有的选中
      }
      var _this = this
      var stateBar = document.getElementById('chooseWrap')
      e = e || window.event
      // 获取鼠标在整个页面的位置
      var posx = e.offsetX
      var posy = e.offsetY
      var div = document.createElement('div')
      // div.className = 'tempDiv'
      div.style.left = posx + 'px'
      div.style.top = posy + 'px'

      stateBar.onmousemove = function (ev) {
        if (ev.which !== 1) {
          return
        }
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
            $(_this.$refs.copyMenu).toggle(false)
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
      if (ev.which !== 1) {
        return
      }
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
        stateBar.onmousemove = null
        stateBar.onmouseup = null
        stateBar.onmouseleave = null
      }
      // 移出编辑区也取消拖拽
      stateBar.onmouseleave = function () {
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
        stateBar.onmousemove = null
        stateBar.onmouseup = null
        stateBar.onmouseleave = null
      }
    },
    del: function () {
      this.saveHistory()
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
      this.cancelSelected()
      if (this.selectArea.choose) {
        this.selectArea.choose = false
        $('.tempDiv').remove()
      }
    },
    deleteOne: function (type, tempArr) {
      var i = 0
      var indexArr = []
      tempArr = tempArr.sort(function (a, b) {
        return a - b
      })
      if (type === 'item') {
        var _type = 'chartNum'
      } else {
        _type = 'combinList'
      }
      for (let y = 0, len = tempArr.length; y < len; y++) {
        let item = tempArr[y]
        let z = this[_type][item].zIndex
        indexArr.push(z)
      }
      indexArr.sort()
      indexArr = indexArr.map((item, i) => {
        return item - i
      })
      for (let y = 0, len = tempArr.length; y < len; y++) {
        let item = tempArr[y]
        let z = indexArr[y]
        if (z) {
          for (let x = 0, xLen = this.chartNum.length; x < xLen; x++) {
            if (this.chartNum[x].zIndex > z) {
              this.chartNum[x].zIndex -= 1
            }
          }
          for (let x = 0, xLen = this.combinList.length; x < xLen; x++) {
            if (this.combinList[x].zIndex > z) {
              this.combinList[x].zIndex -= 1
            }
          }
        }
        this[_type].splice(item - i, 1)
        this.maxIndex--
        i++
      }
    },
    deleteOneOld: function (type, tempArr) {
      var i = 0
      tempArr = tempArr.sort(function (a, b) {
        return a - b
      })
      if (type === 'item') {
        var _type = 'chartNum'
      } else {
        _type = 'combinList'
      }

      for (let y = 0, len = tempArr.length; y < len; y++) {
        let item = tempArr[y]
        let z = this[_type][item].zIndex
        if (z) {
          for (let x = 0, xLen = this.chartNum.length; x < xLen; x++) {
            if (this.chartNum[x].zIndex > z) {
              this.chartNum[x].zIndex -= 1
            }
          }
          for (let x = 0, xLen = this.combinList.length; x < xLen; x++) {
            if (this.combinList[x].zIndex > z) {
              this.combinList[x].zIndex -= 1
            }
          }
        }
        this[_type].splice(item - i, 1)
        this.maxIndex--
        i++
      }
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
      this.saveHistory()
      if (this.chooseIndexs.length > 0) {
        if (this.chooseIndexs.length === 1 && this.chooseCompIndexs.length === 0) {
          this.copyOne('item', this.chooseIndexs, true)
        } else {
          this.copyOne('item', this.chooseIndexs)
        }
      }
      if (this.chooseCompIndexs.length > 0) {
        if (this.chooseIndexs.length === 0 && this.chooseCompIndexs.length === 1) {
          this.copyOne('compose', this.chooseCompIndexs, true)
        } else {
          this.copyOne('compose', this.chooseCompIndexs)
        }
      }
      if (this.selectArea.choose) {
        this.selectArea.choose = false
        $('.tempDiv').remove()
      }
      // this.copyIndexs = this.chooseIndexs
      // this.copyCompIndexs = this.chooseCompIndexs
    },
    compare: function (property) {
      return function (a, b) {
        return JSON.parse(a).x - JSON.parse(b).x
      }
    },
    paste: function (ev) {
      // console.log(1111111111)
      // this.saveHistory()
      // if (this.copyIndexs.length > 0) {
      //   if (this.copyIndexs.length === 1 && this.copyCompIndexs.length === 0) {
      //     this.copyOne('item', this.copyIndexs, true)
      //   } else {
      //     this.copyOne('item', this.copyIndexs)
      //   }
      // }
      // if (this.copyCompIndexs.length > 0) {
      //   if (this.copyIndexs.length === 0 && this.copyCompIndexs.length === 1) {
      //     this.copyOne('compose', this.copyCompIndexs, true)
      //   } else {
      //     this.copyOne('compose', this.copyCompIndexs)
      //   }
      // }
      // if (this.selectArea.choose) {
      //   this.selectArea.choose = false
      //   $('.tempDiv').remove()
      // }
      var cThis = this
      this.clickPaint()
      if (this.copyType === 'item') {
        var _type = 'chartNum'
        this.chooseIndexs = []
      } else {
        _type = 'combinList'
        this.chooseCompIndexs = []
      }
      var chaHeight = 0
      var chaWidth = 0
      this.tempItemArry.sort(this.compare())
      this.tempItemArry.forEach((element, index) => {
        let tempItem = JSON.parse(element)
        if (index === 0) {
          chaWidth = tempItem.x - Math.floor((ev.pageX - 80) / cThis.paintObj.scale * 100)
          chaHeight = tempItem.y - Math.floor((ev.pageY - 70) / cThis.paintObj.scale * 100)
        }
        tempItem.x = tempItem.x - chaWidth
        tempItem.y = tempItem.y - chaHeight
        tempItem.id = new Date().getTime() + parseInt(Math.random() * 10000)
        this[_type].push(tempItem)
        if (this.copyType === 'item') {
          this.chooseIndexs.push(this[_type].length - 1)
        } else {
          this.chooseCompIndexs.push(this[_type].length - 1)
        }
      })
      if (this.copyonlyOne) {
        this.selectedItem = this[_type][this[_type].length - 1]
      }
      this.updateMinXitem()
    },
    copyOne: function (type, arr, onlyOne) {
      // 单个元件的复制操作
      if (type === 'item') {
        var _type = 'chartNum'
        this.chooseIndexs = []
      } else {
        _type = 'combinList'
        this.chooseCompIndexs = []
      }
      this.tempItemArry = []
      this.copyType = type
      this.copyonlyOne = onlyOne
      for (let i = 0, len = arr.length; i < len; i++) {
        this[_type][arr[i]].slted = false
        let tempItem = JSON.parse(JSON.stringify(this[_type][arr[i]]))
        tempItem.x += 20
        tempItem.y += 20
        tempItem.slted = true // 复制的元件默认选中
        tempItem.zIndex = ++this.maxIndex
        this.tempItemArry.push(JSON.stringify(tempItem))
        // this[_type].push(tempItem)
        // if (type === 'item') {
        //   this.chooseIndexs.push(this[_type].length - 1)
        // } else {
        //   this.chooseCompIndexs.push(this[_type].length - 1)
        // }
      }
      // 只有一个元件时修改当前选中
      // if (onlyOne) {
      //   this.selectedItem = this[_type][this[_type].length - 1]
      // }
      // this.updateMinXitem()
      window.CrossScreenCope = {
        ItemArry: this.tempItemArry,
        copyType: type,
        copyonlyOne: onlyOne
      }
    },
    hideContext: function () {
      $(this.$refs.contextMenu).toggle(false)
      $(this.$refs.copyMenu).toggle(false)
    },
    gridChg: function (data) {
      this.saveHistory('paint')
      this.paintObj.showGrid = !this.paintObj.showGrid
    },
    opacityChg: function (data) {
      // console.log('opachg:' + data)
    },
    delPaintImg: function () {
      this.saveHistory('paint')
      this.paintObj.bgImg = ''
    },
    activeSrcList(index) {
      // console.log('index: ', index);
      this.$EventBus.$emit('activeSrcList', index)
      this.imgHeightLight = index
    },
    deleteSrcList(index) {
      this.selectedItem.srcList.splice(index, 1)
    },
    /* 图片 */
    changeImg: function (e) {
      if (e.value === '') {
        return
      }
      if (e.target.files[0].size > 15 * 1024 * 1024) {
        Notification({
          message: '上传的文件不能大于15MB',
          position: 'bottom-right',
          customClass: 'toast toast-info'
        })
        e.target.value = ''
        return
      }
      const name = e.target.files[0].name
      var _this = this
      var formData = new FormData()
      formData.append('uploaded_file', e.target.files[0])
      this.uploadFile('img', formData, function (data) {
        if (!_this.selectedItem.chartType) {
          // 上传画布图片
          _this.saveHistory('paint')
          _this.paintObj.bgImg = '/home/getImg/' + data.obj.isCustom + '/' + data.obj.id
          return
        }
        const chartType = _this.selectedItem.chartType
        const curSrc = '/leaderview/home/getImg/' + data.obj.isCustom + '/' + data.obj.id
        _this.saveHistory()
        if (_this.selectedItem.chartType === 'image' || _this.selectedItem.chartType === 'Newimage' || chartType === 'DataFlow') {
          _this.selectedItem.imgSrc = curSrc
          _this.selectedItem.imgName = e.target.files[0].name
        } if (_this.selectedItem.chartType === 'table') {
          _this.selectedItem.tableBack = curSrc
          _this.selectedItem.tableBackName = e.target.files[0].name
        } else if (_this.selectedItem.subType === 'pictorialBar') {
          _this.selectedItem.symbolImg = curSrc
        } if (_this.selectedItem.chartType === 've-line') {
          _this.selectedItem.symbolSrc = curSrc
        } else if (chartType === 'ppt' || chartType === 'BulletFrame') {
          // 列表顶部添加
          _this.selectedItem.srcList.unshift({
            name,
            src: curSrc
          })
          _this.$EventBus.$emit('activeSrcList', 0)
          _this.imgHeightLight = 0
        }
      })
      e.target.value = ''
    },
    removeImg(e) {
      let { chartType } = this.selectedItem
      if (chartType === 'image' || chartType === 'Newimage') {
        this.selectedItem.imgSrc = ''
        this.selectedItem.imgName = ''
      } else if (chartType === 'video') {
        this.selectedItem.videoSrc = ''
        this.selectedItem.VideoName = ''
      } else if (chartType === 'table') {
        this.selectedItem.tableBack = ''
        this.selectedItem.tableBackName = ''
      }
    },
    // 视频
    uploadVideo(e) {
      // field, that
      if (e.value === '') {
        return
      }
      if (e.target.files[0].size > 100 * 1024 * 1024) {
        e.target.value = ''
        Notification({
          message: '上传的视频不能大于100MB',
          position: 'bottom-right',
          customClass: 'toast toast-info'
        })
        return
      }
      var file = e.target.files[0]
      // 视频截图
      /**
  var windowURL = window.URL || window.webkitURL
  var videoURL = windowURL.createObjectURL(file)
  this.selectedItem.videoSrc = videoURL
  */
      var formdata = new FormData()
      formdata.append('uploaded_file', file)
      var _this = this
      this.uploadFile('video', formdata, function (data) {
        if (_this.selectedItem.chartType === 'video') {
          _this.selectedItem.videoSrc = gbs.host + data.obj
          _this.selectedItem.VideoName = e.target.files[0].name
          console.log(e.target.files[0].name)
          _this.tempVideoUrl = _this.selectedItem.videoSrc
          _this.selectedItem.linkSrc = data.obj // 保存一份纯接口的
        }
      })
      e.target.value = ''
    },
    palyErr() {
      this.showPlayErr = true
    },
    getPaintCl(data) {
      this.saveHistory('paint')
      this.paintObj.bgColor = data.color
    },
    getLineCl(data) {
      this.helpLineColor = data.color
    },
    changeStation() {
      document.querySelector('.vue-ruler-wrapper').style.position = this.guideStation
    },
    getColor(data) {
      this.saveHistory()
      if (data.type !== undefined) {
        if (data.ColorNum) {
          if (data.ColorNum === 1) {
            this.selectedItem[data.type] = [data.color, this.selectedItem[data.type][1]]
          } else {
            this.selectedItem[data.type] = [this.selectedItem[data.type][0], data.color]
          }
        } else {
          this.selectedItem[data.type] = data.color
        }
      } else {
        // 用来解决不能监听直接赋值的数组变化
        this.selectedItem.ctColors.splice(data.index, 1)
        this.selectedItem.ctColors.splice(data.index, 0, data.color)
      }
    },
    getGaugeCl(data) {
      this.saveHistory()
      this.$set(this.selectedItem, 'bgClr', data.color)
    },
    getMapColor(data) {
      this.saveHistory()
      if (!this.selectChange && this.chooseSameFlag) {
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i]['ctColors'].splice(data.index, 1, data.color)
        })
      } else {
        if (data.type !== undefined) {
          this.selectedItem[data.type] = data.color
        } else {
          this.selectedItem.ctColors.splice(data.index, 1, data.color)
        }
      }
    },
    getColorStart(data) {
      this.saveHistory()
      if (!this.selectChange && this.chooseSameFlag) {
        var oldColor = this.selectedItem.ctColors[data.index]
        oldColor[0] = data.color
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i]['ctColors'].splice(data.index, 1, oldColor)
        })
      } else {
        if (data.type !== undefined) {
          this.selectedItem[data.type][0] = data.color
        } else {
          var oldColor = this.selectedItem.ctColors[data.index]
          oldColor[0] = data.color
          this.selectedItem.ctColors.splice(data.index, 1, oldColor)
        }
      }
    },
    // 渐变色颜色改变
    getGradColor(data) {
      this.saveHistory()
      if (!this.selectChange && this.chooseSameFlag) {
        var oldColor = this.selectedItem.ctColors[data.index]
        oldColor[1] = data.color
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i]['ctColors'].splice(data.index, 1, oldColor)
        })
      } else {
        if (data.type !== undefined) {
          this.selectedItem[data.type][1] = data.color
        } else {
          var oldColor = this.selectedItem.ctColors[data.index]
          oldColor[1] = data.color
          this.selectedItem.ctColors.splice(data.index, 1, oldColor)
        }
      }
    },
    getSingleColor(data) {
      this.saveHistory()
      if (!this.selectChange && this.chooseSameFlag) {
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i]['ctColors'].splice(data.index, 1, data.color)
        })
      } else {
        this.selectedItem.ctColors.splice(data.index, 1, data.color)
      }
    },
    getBarClr(data) {
      this.saveHistory()
      this.selectedItem.barClrs.splice(data.index, 1, data.color)
      if (!this.selectChange && this.chooseSameFlag) {
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i].barClrs.splice(data.index, 1, data.color)
        })
      }
    },
    testObjChange(direct, newValue) {
      var defData = 0
      var isWidth = direct === 'width'
      var valiType = direct + 'Vali'
      var allowOverflow = this.childResize ? 0 : baseData.allowOverflow
      if (this.childResize) {
        defData = isWidth ? this.combinList[this.parentId].width : this.combinList[this.parentId].height
      } else {
        defData = isWidth ? this.paintObj.width : this.paintObj.height
      }
      let oldV = Number(this.selectedItem[direct])
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
        // if (newValue < this.miniW) {
        //   this.testObj[direct] = this.miniW
        // }
      } else {
        if (oldV !== newValue) {
          this.saveOldData(this.selectedItem.id, direct, oldV)
        }
        this[valiType].isShowError = false
        this.$set(this.selectedItem, direct, newValue)
      }
      if (!isWidth && this.selectedItem.chartType === 'border') {
        if (parseInt(this.selectedItem.height / 2) < this.selectedItem.radius) {
          this.selectedItem.radius = Number(this.selectedItem.height / 2)
          this.borderRadius = parseInt(this.selectedItem.height / 2)
        }
      }
    },
    testObjPosChange(position, newValue) {
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
      let oldV = Number(this.selectedItem[position])
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
        if ((newValue !== 0 && !newValue) || newValue < -allowOverflow || newValue > limitValue) {
          this[valiType].isShowError = true
          this[valiType].errorMsg = isX
            ? '横轴位置范围为' + -allowOverflow + '~' + (limitValue)
            : '纵轴位置范围为' + -allowOverflow + '~' + (limitValue)
          if (newValue > limitValue) {
            this.testObj[position] = limitValue
          }
          if (newValue < allowOverflow * -1) {
            this.testObj[position] = allowOverflow * -1
          }
        } else {
          this[valiType].isShowError = false
          this.selectedItem[position] = newValue
        }
      }
      // not Number
      if (oldV !== Number(this.selectedItem[position])) {
        this.saveOldData(this.selectedItem.id, position, oldV)
      }
      this.selectedItem[position] = Number(this.selectedItem[position])
    },
    changePaintStyle(scale, top, left) {
      this.paintObj.scale = Math.floor(100 / scale)
      let height = document.querySelector('.paint-bg').clientHeight
      let width = document.querySelector('.paint-bg').clientWidth
      let canvasheight = document.querySelector('.centerBox .mycanvas canvas').clientHeight
      let canvaswidth = document.querySelector('.centerBox .mycanvas canvas').clientWidth
      document.querySelector('#centerMapBox').scrollTop = height * (top - 10) / canvasheight * this.paintObj.scale / 100
      document.querySelector('#centerMapBox').scrollLeft = width * (left - 10) / canvaswidth * this.paintObj.scale / 100
    },
    // 改变画布大小
    changePaintSize(type) {
      this.$refs.rulertool.box()
      var key = 'width'
      if (type === 'h') {
        key = 'height'
      }
      if (this.paintInput[key] < 500 || this.paintInput[key] > 10000) {
        this.paintInput[key] = this.paintObj[key]
      } else {
        this.saveHistory('paint')
        this.paintObj[key] = Number(this.paintInput[key])
      }
      this.changeHomeData(this.paintObj)
    },
    // 这里来处理快捷键
    handleKeyDown(e) {
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
      if (window.event.ctrlKey && key === 90) {
        // ctrl + z
        e.preventDefault()
        e.returnValue = false
        this.Revoke()
      }
    },
    handleKeyUp(e) {
      var key = window.event.keyCode ? window.event.keyCode : window.event.which
      if (key === 13) {
        // flag = true
        e.preventDefault()
      }
    },
    onMouseScroll(e) {
      if (window.event.ctrlKey) {
        e.preventDefault()
        var wheel = e.originalEvent.wheelDelta || -e.originalEvent.detail
        var delta = Math.max(-1, Math.min(1, wheel))
        if (delta < 0) { // 向下滚动
          // console.log('向下滚动')
        } else { // 向上滚动
          // console.log('向上滚动')
        }
      }
    },
    scrollFunc(e) {
      e = e || window.event
      e.preventDefault()
      if (e.wheelDelta && event.ctrlKey) { // IE/Opera/Chrome
        event.returnValue = false
      } else if (e.detail) { // Firefox
        event.returnValue = false
      }
    },
    changeTogether(key, newV) {
      if (!this.selectChange && this.chooseSameFlag) {
        this.chooseIndexs.forEach((item) => {
          this.chartNum[item][key] = newV
        })
      }
    },
    saveOldData(tempId, key, oldV) {
      // let tempId = this.selectedItem.id
      let chgIndex = _.findIndex(this.chartNum, function (i) { return i.id === tempId })
      if (chgIndex !== -1) {
        var tempChartNum = JSON.parse(JSON.stringify(this.chartNum))
        tempChartNum[chgIndex][key] = oldV
        let tempHisObj = {
          type: 'item',
          chartNum: JSON.stringify(tempChartNum),
          compose: JSON.stringify(this.combinList)
        }
        if (this.historyArr && this.historyArr.length >= this.revokeStep) {
          this.historyArr.shift()
        }
        this.historyArr.push(tempHisObj)
        console.log('保存一次：' + chgIndex)
      } else {
        var tempCombin = JSON.parse(JSON.stringify(this.combinList))
        var _index = -1
        for (let n = 0, len = tempCombin.length; n < len; n++) {
          _index = _.findIndex(tempCombin[n].child, function (i) { return i.id === tempId })
          if (_index !== -1) {
            tempCombin[n].child[_index][key] = oldV
            break
          }
        }
        if (_index !== -1) {
          console.log('保存一次组合：' + _index)
          let tempHisObj = {
            type: 'item',
            chartNum: JSON.stringify(this.chartNum),
            compose: JSON.stringify(tempCombin)
          }
          if (this.historyArr && this.historyArr.length >= this.revokeStep) {
            this.historyArr.shift()
          }
          this.historyArr.push(tempHisObj)
        }
      }
    },
    chgImgSrc(imgSrc) {
      this.saveHistory()
      this.selectedItem.imgSrc = imgSrc
      this.changeTogether('imgSrc', imgSrc)
    }
  },
  watch: {
    /* chartNum: {
      handler (newV, oldV) {
          // this.oldChartNum = JSON.stringify(oldV)
          console.log('chartNum change')
          this.historyArr.push({
            type: 'item',
            chartNum: JSON.stringify(oldV),
            compose: JSON.stringify(this.combinList)
          })
      },
      deep: true
    }, */
    chooseCompIndexs: function (newV) {
      if (newV.length + this.chooseIndexs.length > 1) {
        this.changeItemChoose(false)
      } else {
        this.changeItemChoose(true)
      }
    },
    resourcesIds: function (newV) {
      if (newV !== '' && newV) {
        this.chartNum.forEach(data => {
          console.log(data)
          if (data.params.neIds && data.url) {
            data.params.neIds = newV
            data.params.baseneclss = this.paintObj.templateConf.baseneclss || ''
            data.params.neclass = this.paintObj.templateConf.neclass || ''
            // this.paintObj.templateConf.baseneclss  neclass
            if (data.params.windows) {
              let newData = JSON.parse(data.params.windows)[0]
              let mydata = newData.ne[0]
              mydata.id = newV
              newData.ne = [mydata]
              data.params.windows = JSON.stringify([newData])
            }
            $.ajax({
              url: data.ctDataSource === 'system' ? (gbs.host + data.url) : data.url, // 第三方的ur已经拼接好host
              data: data.params,
              type: data.method || 'get',
              cache: false,
              ascyn: false,
              success: function (res) {
                if (data.barType === 'NewHistogram') {
                  data.chartData1 = res.success ? res.obj : { columns: [], rows: [] }
                }
                if (data.barType === 'NewGroupHistogram') {
                  data.chartData2 = res.success ? res.obj : { columns: [], rows: [] }
                }
                if (data.barType === 'NewGroupLeftHistogram') {
                  data.chartData3 = res.success ? res.obj : { columns: [], rows: [] }
                }
                if (data.barType === 'NewBar') {
                  data.chartData4 = res.success ? res.obj : { columns: [], rows: [] }
                } else {
                  data.chartData = res.success ? res.obj : []
                }
              }
            })
          }
        })
      }
    },
    chooseIndexs: function (newV) {
      if (newV.length + this.chooseCompIndexs.length > 1) {
        this.changeItemChoose(false)
      } else {
        this.changeItemChoose(true)
      }
    },
    selectedItem: function (newV, oldV) {
      this.selectChange = true
      this.$nextTick(() => {
        this.selectChange = false
      })
      // if (newV.chartType === 'JSMpeg') {
      //   this.getHcnetData()
      // }
    },
    showStyleTab: function (newV) {
      if (!newV && this.selectedItem.ctDataSource !== 'static') {
        this.handleHost()
        if (this.oldCheckId !== this.selectedItem.id) {
          this.getUrlByType(true)
        } else {
          if (this.syst.curUrl.length < 1) {
            this.getUrlByType(true)
          }
        }
      }
    },
    'paintObj.opacity': function (newV, oldV) {
      // console.log(newV, oldV)
    },
    'selectedItem.ifGradual': function (newV) {
      // 对象类型不能统一赋值
      if (newV && !this.selectChange) {
        if (this.chooseSameFlag) {
          if (newV === 'true') {
            this.chooseIndexs.forEach((item) => {
              this.chartNum[item]['ctColors'] = JSON.parse(JSON.stringify(this.defGradColors))
            })
          } else {
            this.chooseIndexs.forEach((item) => {
              this.chartNum[item]['ctColors'] = this.defalutColors.slice(0, 8)
            })
          }
        } else {
          if (newV === 'true') {
            this.selectedItem.ctColors = JSON.parse(JSON.stringify(this.defGradColors))
          } else {
            this.selectedItem.ctColors = this.defalutColors.slice(0, 8)
          }
        }
      }
    },
    'selectedItem.refreshTm': function (newV) {
      if (newV < 3) {
        this.freshVali = true
      } else {
        this.freshVali = false
      }
    },
    'selectedItem.hdClr': function (newV) {
      this.changeTogether('hdClr', newV)
    },
    'selectedItem.hdfontSize': function (newV) {
      this.changeTogether('hdfontSize', newV)
    },
    'selectedItem.legendColor': function (newV, oldV) {
      this.changeTogether('legendColor', newV)
    },
    'selectedItem.splitShow': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'splitShow', oldV)
      }
      this.changeTogether('splitShow', newV)
    },
    'selectedItem.splitColor': function (newV) {
      this.changeTogether('splitColor', newV)
    },
    'selectedItem.splitSize': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'splitSize', oldV)
      }
      this.changeTogether('splitSize', newV)
    },
    'selectedItem.lineArea': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'lineArea', oldV)
      }
      this.changeTogether('lineArea', newV)
    },
    'selectedItem.smooth': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'smooth', oldV)
      }
      this.changeTogether('smooth', newV)
    },
    'selectedItem.showPoint': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'showPoint', oldV)
      }
      this.changeTogether('showPoint', newV)
    },
    'selectedItem.bgClr': function (newV) {
      this.changeTogether('bgClr', newV)
    },
    'selectedItem.barClr': function (newV) {
      this.changeTogether('barClr', newV)
    },
    'selectedItem.clr': function (newV) {
      this.changeTogether('clr', newV)
    },
    'selectedItem.fontSize': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'fontSize', oldV)
      }
      this.changeTogether('fontSize', newV)
    },
    'selectedItem.proHeight': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'proHeight', oldV)
      }
      this.changeTogether('proHeight', newV)
    },
    // 'selectedItem.HcnetData': function (newV, oldV) {
    //   if (newV !== '') {
    //     this.getVideoDate()
    //   }
    // },
    // 'selectedItem.VideoData': function (newV, oldV) {
    //   console.log(this.selectedItem.VideoData)
    // },
    'selectedItem.radius': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'radius', oldV)
      }
      this.changeTogether('radius', newV)
    },
    'selectedItem.ctLegendShow': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'ctLegendShow', oldV)
      }
      this.changeTogether('ctLegendShow', newV)
      // this.tempHisObj = {
      //   type: 'item',
      //   chartNum: this.oldChartNum,
      //   compose: JSON.stringify(this.combinList)
      // }
    },
    'selectedItem.visualPosition': function (newV) {
      this.changeTogether('visualPosition', newV)
    },
    'selectedItem.colorType': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'colorType', oldV)
      }
      this.changeTogether('colorType', newV)
    },
    'selectedItem.colorful': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'colorful', oldV)
        if (this.selectedItem.chartType === 'progress') {
          this.selectedItem.barClrs = ['#8feee5', '#1bbcae']
          if (!this.selectChange && this.chooseSameFlag) {
            this.chooseIndexs.forEach((i) => {
              this.chartNum[i].barClrs = ['#8feee5', '#1bbcae']
            })
          }
        }
      }
      this.changeTogether('colorful', newV)
    },
    'selectedItem.hdBgClr': function (newV) {
      this.changeTogether('hdBgClr', newV)
    },
    'selectedItem.bdClr': function (newV) {
      this.changeTogether('bdClr', newV)
    },
    'selectedItem.bdpx': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'bdpx', oldV)
      }
      this.changeTogether('bdpx', newV)
    },
    'selectedItem.direction': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'direction', oldV)
      }
      this.changeTogether('direction', newV)
    },
    'selectedItem.speed': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'speed', oldV)
      }
      this.changeTogether('speed', newV)
    },
    'selectedItem.showType': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'showType', oldV)
      }
      this.changeTogether('showType', newV)
    },
    'selectedItem.timeType': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'timeType', oldV)
      }
      this.changeTogether('timeType', newV)
    },
    'selectedItem.fontFamily': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'fontFamily', oldV)
      }
      this.changeTogether('fontFamily', newV)
    },
    'selectedItem.fontWeight': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'fontWeight', oldV)
      }
      this.changeTogether('fontWeight', newV)
    },
    'selectedItem.themeType': function (newV, oldV) {
      if (!this.selectChange && newV !== oldV) {
        let tempId = this.selectedItem.id
        this.saveOldData(tempId, 'themeType', oldV)
      }
      this.changeTogether('themeType', newV)
    },
    'selectedItem.mapLevel': function (newValue, oldV) {
      if (!this.selfMapLevel) {
        // console.log('切换元件导致的mapLevel更改') // 不同地图元件的改变不触发watch
        return
      }
      if (oldV) {
        this.selectMapData = {}
      }
      var _this = this
      if (newValue === 'country') {
        this.areaArr = this.provinceArr
        if (this.selectedItem.chartType === 'v-scatter' || this.selectedItem.chartType === 'NewScatter') {
          this.clearAlertMap()
        }
        if (this.selectedItem.chartType === 'v-map' || this.selectedItem.chartType === 'NewVMap') {
          this.initLevelData()
        }
      } else if (newValue === 'city') {
        var noMapArr = ['110000', '310000', '500000', '120000', '710000', '810000', '820000'] // 直辖市自治区没有三级地图
        if (oldV === 'country') {
          // console.log('从国家级到市级') // 从国家级到市级
          if (!this.selectedItem.provinceCode || noMapArr.indexOf(this.selectedItem.provinceCode) !== -1) {
            this.selectedItem.provinceCode = 510000 // 默认选中一个位置
          }
        }
        if (noMapArr.indexOf(this.selectedItem.provinceCode) !== -1) {
          this.selectedItem.mapLevel = oldV
          Notification({
            message: '该地区不支持第三级地图',
            position: 'bottom-right',
            customClass: 'toast toast-info'
          })
        } else {
          this.getMapData(this.selectedItem.provinceCode).then((data) => {
            _this.cityArr = data
            _this.selectedItem.cityCode = data[0].value
          })
        }
      } else if (newValue === 'county') {
        var noMapArr = ['110000', '310000', '500000', '120000', '710000', '810000', '820000'] // 直辖市自治区没有三级地图
        if (oldV === 'country') {
          if (!this.selectedItem.provinceCode || noMapArr.indexOf(this.selectedItem.provinceCode) !== -1) {
            this.selectedItem.provinceCode = 510000 // 默认选中一个位置
          }
        }
        if (noMapArr.indexOf(this.selectedItem.provinceCode) !== -1) {
          this.selectedItem.mapLevel = oldV
          Notification({
            message: '该地区不支持该级别地图',
            position: 'bottom-right',
            customClass: 'toast toast-info'
          })
        } else {
          this.getMapData(this.selectedItem.provinceCode).then((data) => {
            this.getMapData(data[0].value).then((dataCounty) => {
              _this.countyArr = dataCounty
              this.selectedItem.countyCode = dataCounty[0].name
            })
          })
        }
      } else {
        if (newValue === 'province') {
          if (!this.selectedItem.provinceCode) {
            this.selectedItem.provinceCode = this.provinceArr[0].value
          }
          this.getMapData(this.selectedItem.provinceCode).then((data) => {
            this.cityArr = data
            this.areaArr = data
            if (this.selectedItem.chartType === 'v-map' || this.selectedItem.chartType === 'NewVMap') {
              this.initLevelData()
            }
            if (this.selectedItem.chartType === 'v-scatter' || this.selectedItem.chartType === 'NewScatter') {
              this.clearAlertMap()
            }
          })
        }
      }
    },
    areaArr: function (newObj) {
      this.changeAreaData(newObj)
    },
    'testObj.width': function (newValue, oldValue) {
      // console.log(oldValue)
      /* console.log(this.testObj.id, this.lastKeyId)
      if (this.testObj.id === this.lastKeyId && oldValue !== newValue) {
        // 这里才保存历史
        var _this = this
        var oldChart = JSON.parse(JSON.stringify(this.chartNum))
        let itemIndex = _.findLastIndex(oldChart, function (item) {
          return item.id === _this.testObj.id
        })
        console.log(oldChart[itemIndex].chartType)
        oldChart[itemIndex].width = oldValue
        let tempObj = {
          type: 'item',
          chartNum: JSON.stringify(oldChart),
          compose: JSON.stringify(this.combinList)
        }
        if (this.historyArr && this.historyArr.length >= this.revokeStep) {
          this.historyArr.shift()
        }
        this.historyArr.push(tempObj)
        console.log('保存一次历史')
      } */
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
    this.getPageConf(id)
    sessionStorage.setItem('pageId', id)
  },
  mounted: function () {
    // var _url = window.location.protocol + '//' + window.location.host + '/index'
    // window.history.pushState({}, '', _url)
    // $('#header').hide()
    $('.navbar-fixed-top').css('display', 'none')
    $('.page-container').css('top', '0px')
    this.chooseMap()
    if (!gbs.inDev) {
      titleShow('bottom', $('.e-legend'))
    }
    // 初始化paintInput
    document.addEventListener('keydown', this.handleKeyDown)
    document.addEventListener('keyup', this.handleKeyUp)
    // this.changeMapData(100000, 'provinceArr')
    this.getMapData(100000).then((data) => {
      this.provinceArr = data
    })
    document.onmousemove = (e) => {
      if (this.MapChange) {
        var winWidth = window.screen.width
        var ShuWidth = e.clientX
        if (ShuWidth > Math.floor(winWidth * 3 / 4)) {
          this.tapsStation = 'right'
          document.querySelector('.archive').style.order = 1
          document.querySelector('#centerMapBox').style.order = -1
        } else if (ShuWidth < Math.floor(winWidth / 4)) {
          this.tapsStation = 'left'
          document.querySelector('.archive').style.order = -1
          document.querySelector('#centerMapBox').style.order = 1
        }
      }
    }
    $('#lead-screen').addClass('disShow')
    $('#screen').addClass('disShow')
    // 添加事件监听
    // if (document.addEventListener) {
    //   document.addEventListener('DOMMouseScroll', this.scrollFunc, false)
    // } // W3C
    // window.onmousewheel = document.onmousewheel = this.scrollFunc // IE/Opera/Chrome/Safari
    // $(document).on('mousewheel DOMMouseScroll', this.onMouseScroll)
    this.getAllPage()
  },
  beforeDestroy: function () {
    $('#header').show()
    $('.tempDiv').remove() // 绑定的事件也会移除
    document.removeEventListener('keydown', this.handleKeyDown)
    document.removeEventListener('keyup', this.handleKeyUp)
    // var stateBar = document.getElementById('chooseWrap')
    // var stateBar = $('#chooseWrap')
    // stateBar.removeEventListener('mousedown ', this.userChoose)
    $('.navbar-fixed-top').css('display', 'block')
    $('.page-container').css('top', '50px')
    $('#lead-screen').removeClass('disShow')
  },
  destroyed: function () {
    // $.comps.editHome = null;
  }
}
