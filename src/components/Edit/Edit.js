import compsArr from '#/js/chartJson'
import DragBox from './../Common/DragBox'
import Compose from './../Common/Compose'
import Select2 from './../Common/Select2'
import Vcolor from './../Common/Vcolor'
import PreView from './../PreView/PreView'
import { baseData, gbs } from '@/config/settings'
import { Slider, Notification } from 'element-ui'
// import html2canvas from 'html2canvas' // 图片的层级总是要高一些
import qs from 'qs'
import lodash from 'lodash'

export default {
  name: 'edit',
  components: { DragBox, Compose, Select2, Vcolor, PreView, Slider },
  props: [],
  data: function () {
    return {
      pageId: 0,
      value2: 0.5,
      oldCheckId: '', // 优化每次选中都会触发请求接口，仅在切换元件时才请求
      viewPage: false,
      pageData: '',
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
      miniW: 10,
      minIndex: 500, // 当前最低层级
      maxIndex: 500, // 当前最高层级
      paintObj: {
        width: 1920,
        height: 1080,
        bgColor: '',
        bgImg: '',
        scale: 100,
        bgStyle: '3', // 背景图铺满方式
        opacity: 1,
        showGrid: true // 显示网格
      },
      ceditable: true, // 组合
      combinList: [], // 组合的数组
      chooseItems: [], // ctrl选中的元件
      chooseIndexs: [], // ctrl选中的单个元件在chartNum中的序号
      chooseCompIndexs: [], // ctrl选中的组合元件的序号
      onCtrl: false, // 是否按住ctrl
      colorType: 'defalut',
      defaultFontSize: [12, 13, 14, 16, 18, 20, 28, 36, 48, 72],
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
      syst: {
        // 系统数据
        urlSel: [], // 接口下拉列表，根据不同的图形类型有不同的接口
        curUrl: [], // 缓存当前选中的接口params中需要配置的数据
        curConf: {
          // 缓存当前配置的系统数据，用于请求数据
          url: '',
          params: {} // 请求发送给后端的数据
        },
        chainParams: {} // 用于缓存需要多级下拉联动请求，以便不用每次遍历当前选中接口数据
      },
      dataApi: [], // 选择接口数据
      currApi: {}, // 当前选中api
      dataInps: [],
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
      proHeightErr: false,
      radiusErr: false
    }
  },
  computed: {
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
          ? 'url(' + gbs.host + this.paintObj.bgImg + ')'
          : '',
        backgroundColor: this.paintObj.bgColor,
        backgroundSize: backgroundSize,
        opacity: this.paintObj.opacity
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
    changeProHeight () {
      // 改变进度条的高度
      var proHeight = Math.floor(this.selectedItem.proHeight)
      if (isNaN(proHeight) || proHeight < 0) {
        this.selectedItem.proHeight = ''
        this.proHeightErr = true
      }
      if (proHeight >= 8 && proHeight <= 24) {
        this.proHeightErr = false
      } else {
        this.proHeightErr = true
      }
      this.changeRadius()
    },
    changeRadius () {
      var radius = Math.floor(this.selectedItem.radius)
      if (isNaN(radius) || radius < 0) {
        this.radiusErr = true
      }
      if (radius >= 0 && radius <= Math.ceil(this.selectedItem.proHeight / 2)) {
        this.radiusErr = false
      } else {
        this.radiusErr = true
      }
    },
    gerPageConf (id) {
      // 获取当前页的配置
      // home/homePage/getById
      this.axios.get(`home/homePage/getById/${id}`).then(res => {
        if (res.obj.viewConf) {
          this.chartNum = JSON.parse(res.obj.viewConf)
          this.pageName = res.obj.name
          let tempNum = this.chartNum
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
        true,
        {},
        {
          id: new Date().getTime(),
          ctName: value.text,
          ctLegendShow: 'true',
          x: 400,
          y: 100,
          width: 350,
          height: 350,
          colorType: 'defalut',
          ctColors: this.defalutColors.concat(),
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
      this.testObj = this.selectedItem
      this.$nextTick(function () {
        // titleShow('bottom', $('.m-right'));
      })
    },
    chgColorType: function () {
      if (this.selectedItem.colorType === 'defalut') {
        this.selectedItem.ctColors.splice(0, this.selectedItem.ctColors.length)
        this.$set(this.selectedItem, 'ctColors', this.defalutColors.concat())
      } else {
        this.selectedItem.ctColors.splice(0, this.selectedItem.ctColors.length)
        var newColors = this.setDefColor()
        this.$set(this.selectedItem, 'ctColors', newColors)
      }
    },
    setDefColor: function () {
      var colorArr = []
      for (var i = 0; i < this.defalutColors.length; i++) {
        var temp = [this.defalutColors[i], this.defGradColors[i]]
        colorArr.push(temp)
      }
      return colorArr
    },
    editPage: function () {
      // $.comps.editHome.open();
    },
    fullScreen: function () {
      // Public.bigScreenfullScreen($('#home-html').get(0));
    },
    clickPaint (event) {
      if (!window.event.ctrlKey) {
        this.cancelSelected(event)
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
      // this.onCtrl = false
    },
    selected: function (item, ev, type, i) {
      // console.log(window.event.ctrlKey)
      // console.log(ev)
      if (ev !== 'context' && !window.event.ctrlKey) {
        this.cancelSelected()
      }
      item.slted = this.editable && true
      this.selectedItem = item
      this.testObj = JSON.parse(JSON.stringify(item))
      if (type === 'compose') {
        this.combinList[i].slted = this.editable && true
        if (this.chooseCompIndexs.indexOf(i) === -1) {
          this.chooseCompIndexs.push(i)
          // this.chooseItems.push(this.chartNum[i])
        }
      } else {
        if (this.chooseIndexs.indexOf(i) === -1) {
          this.chooseIndexs.push(i)
          this.chooseItems.push(this.chartNum[i])
        }
      }
      if (this.oldCheckId !== item.id) {
        // 切换选中的元件
        this.oldCheckId = item.id
        if (ev === 'down' && item.ctDataSource === 'system') {
          this.getUrlByType(true)
        }
      }
      this.$nextTick(function () {
        // titleShow('bottom', $('.m-right'));
      })
    },
    bindCtrl: function () {
      console.log('按住ctrl键')
      // this.onCtrl = true // 按住ctrl键
    },
    addToCompose: function () {
      // console.log(this.chooseItems)
      this.compose(this.chooseItems)
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
      }
      // 为每一个内部组件重新计算相对位置
      for (var i = 0, len = arr.length; i < len; i++) {
        arr[i].left = arr[i].x - _left
        arr[i].top = arr[i].y - _top
      }
      var newObj = $.extend(
        true,
        {},
        {
          id: new Date().getTime(),
          ctLegendShow: 'true',
          x: _left,
          y: _top,
          width: _right - _left,
          height: _bottom - _top,
          // slted: false,
          child: []
        },
        {}
      )
      newObj.slted = false
      this.combinList.push(newObj)
      this.combinList[this.combinList.length - 1].child = arr.concat()
      this.removeItem()
    },
    removeItem: function () {
      // 组合的元件从chartNum中删除
      var i = 0
      var tempArr = this.chooseIndexs.sort()
      tempArr.forEach(item => {
        // this.chartNum.splice(item, 1, {});
        this.chartNum.splice(item - i, 1)
        i++
      })
      this.chooseIndexs = []
      this.chooseItems = []
    },
    // 拆分
    itemSplit: function () {
      var index = this.selectedIndex
      var tempArr = this.combinList[index].child
      tempArr.forEach(item => {
        this.chartNum.push(item)
      })
      this.combinList.splice(index, 1)
    },
    // 下移
    downward: function () {
      // this.selectedItem.zIndex -= 1
      var z = this.selectedItem.zIndex || 500
      this.$set(this.selectedItem, 'zIndex', z - 1)
      if (z - 1 < this.minIndex) {
        this.minIndex = z - 1
      }
    },
    // 上移
    upward: function () {
      // this.selectedItem.zIndex += 1
      var z = this.selectedItem.zIndex || 500
      this.$set(this.selectedItem, 'zIndex', z + 1)
      if (z + 1 > this.maxIndex) {
        this.maxIndex = z + 1
      }
    },
    toBottom: function () {
      this.minIndex--
      this.$set(this.selectedItem, 'zIndex', this.minIndex)
    },
    toTop: function () {
      this.maxIndex++
      this.$set(this.selectedItem, 'zIndex', this.maxIndex)
    },
    resized: function (item) {
      this.testObj.width = item.width
      this.testObj.height = item.height
      this.testObj.x = item.left
      this.testObj.y = item.top
    },
    resetPaint () {
      this.paintObj.bgColor = ''
      this.paintObj.bgImg = ''
      this.paintObj.bgStyle = '3'
      this.paintObj.opacity = 1
      this.paintObj.showGrid = true
    },
    addColor () {
      // 添加颜色
      this.selectedItem.ctColors.push(['#c23531', '#c23531'])
    },
    delColor (index) {
      // 删除自定义颜色
      if (this.selectedItem.ctColors.length === 1) {
        // // tooltip('', '至少配置一种颜色', 'info');
        Notification({
          message: '至少配置一种颜色',
          position: 'bottom-right',
          customClass: 'toast toast-info'
        })
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
        url: gbs.host + 'home/getUrl',
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
          Notification({
            message: '连接错误！',
            position: 'bottom-right',
            customClass: 'toast toast-error'
          })
        }
      })
      // 这个接口不想出现弹出层所以用ajax
      // var _type = this.dealTypeStr()
      // await this.axios.get('home/getUrl?typeStr=' + _type).then((data) => {
      //   this.$set(this.syst, 'urlSel', data.obj || [])
      //   this.$nextTick(function () {
      //     _this.syst.urlSel.length && _this.chgUrl(flag)
      //   })
      // })
    },
    // 改变接口下拉框，需要根据index更新当前选中接口数据,及下面的参数
    chgUrl (flag) {
      var _this = this
      var chainP = (this.syst.chainParams = {}) // 将需要联动的字段缓存
      var index = -1

      if (typeof flag === 'boolean') {
        var selectedP = (this.syst.curConf.params = $.extend(
          true,
          {},
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
        $.each(api, function (i, d) {
          if (d.dataType === 'remote') {
            // 需要通过请求拿数据
            var postData = {}
            if (d.params) {
              $.each(d.params, function (j, o) {
                postData[o] = selectedP[o] || ''
              })
              chainP[d.key] = d
            }
            $.ajax({
              url: gbs.host + d.dataUrl,
              async: false,
              data: postData,
              type: d.method || 'get',
              success: function (data) {
                d.data = data.obj || []
                $.isEmptyObject(selectedP) && _this.setFirstV(d)
              },
              error: function () {
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
      $.each(chaip, async function (i, d) {
        var index = d.params.indexOf(v.key)
        if (index !== -1) {
          var flag = false
          var grp = $('#mainSystemConf')
            .find('[name="' + i + '"]')
            .closest('.form-group')
          $.each(d.params, function (j, o) {
            postData[o] = cur[o]
            if (chaip[o] && chaip[o].params && v.key !== chaip[o].key) {
              // 避免重复请求
              flag = chaip[o].params.indexOf(v.key) !== -1
            }
          })
          if (flag) {
            return false
          }
          /* $.ajax({
            url: d.dataUrl,
            async: false,
            data: postData,
            success: function (data) {
              data.obj = data.obj || [];
              d.data.splice(0, d.data.length);
              d.data = data.obj;
              //  console.log(v.key,d.dataUrl,postData);
            }
          }) */
          await _this.axios.get(d.dataUrl, { params: postData }).then(data => {
            data.obj = data.obj || []
            d.data.splice(0, d.data.length)
            d.data = data.obj
          })
          _this.$set(_this.syst.curUrl, grp.index(), $.extend(true, {}, d))
        }
      })
    },
    getUrlData () {
      // 根据接口获取数据更新图表
      var _this = this
      var curConf = this.syst.curConf
      var param = curConf.params
      if (this.selectedItem.chartType === 'topo') {
        this.saveTopoConf(param)
        return
      }
      var datas = {}
      $.each(param, function (i, d) {
        datas[i] = $.isArray(d) ? d.join(',') : d
      })
      $.ajax({
        url: gbs.host + curConf.url,
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
          Notification({
            message: '连接错误！',
            position: 'bottom-right',
            customClass: 'toast toast-error'
          })
        }
      })
      /* this.axios({
        method: curConf.method,
        url: curConf.url,
        data: qs.stringify(datas),
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
      }).then((data) => {
        data.obj = data.obj || {}
        if (data.obj.colors) {
          _this.selectedItem.ctColors = data.obj.colors
          _this.selectedItem.colorType = 'defalut'
        }
        _this.selectedItem.chartData = data.obj
        _this.selectedItem.url = curConf.url
        _this.selectedItem.method = curConf.method
        _this.selectedItem.params = param
      }) */
    },
    saveTopoConf: function (param) {
      // 拓扑与其他组件不同，需要特殊处理
      this.selectedItem.tpId = param.topoId
    },
    dataChange () {
      if (this.selectedItem.ctDataSource === 'system') {
        this.getUrlData()
      } else {
        var textData = this.$refs.textarea.innerText
        var reg = /^\{[\s\S]*\}$/
        // 先判断是{}类型的对象，而不是new Object
        if (reg.test(textData.trim())) {
          try {
            this.selectedItem.chartData = $.api.utils.json.format(textData)
          } catch (err) {
            // tooltip('', '请输入正确的JSON格式的数据', 'info')
            Notification({
              message: '请输入正确的JSON格式的数据',
              position: 'bottom-right',
              customClass: 'toast toast-info'
            })
          }
        } else {
          // tooltip('', '请输入正确的JSON格式的数据', 'info')
          Notification({
            message: '请输入正确的JSON格式的数据',
            position: 'bottom-right',
            customClass: 'toast toast-info'
          })
        }
      }
    },
    saveConf: function (event, cb) {
      // 保存
      if (
        !(
          !this.widthVali.isShowError &&
          !this.heightVali.isShowError &&
          !this.xVali.isShowError &&
          !this.yVali.isShowError && !this.proHeightErr && !this.radiusErr
        )
      ) {
        // tooltip('', '请填写正确的配置信息', 'info')
        Notification({
          message: '请填写正确的配置信息',
          position: 'bottom-right',
          customClass: 'toast toast-info'
        })
        return
      }
      $('#screen').show()
      var cThis = this
      cThis.selectedItem.slted = false
      var canvas2 = document.createElement('canvas')
      // var _canvas = document.querySelector('#mainEdit-edit .m-main>div')
      // var _canvas = document.querySelector('.m-main #chooseWrap')
      var _canvas = document.querySelector('.m-main .paint-bg')
      // _canvas.style.background = _this.$edit.find('.edit-content').css('background');
      // _canvas.style.background = $('#mainEdit-edit').find('.edit-content').css('background');
      _canvas.style.background = $('body').css('background')
      // document.querySelector('.m-main #chooseWrap').style.background = $('body').css('background')

      // 将canvas画布放大若干倍，然后盛放在较小的容器内，就显得不模糊了
      // canvas2.width = baseData.home.w
      // canvas2.height = baseData.home.h
      canvas2.width = baseData.home.w * (this.paintObj.scale / 100)
      canvas2.height = baseData.home.h * (this.paintObj.scale / 100)
      canvas2.getContext('2d')
      $('#mainEdit-edit .main_topo')
        .find('svg')
        .css('opacity', 0)
      $('#mainEdit-edit .main_topo').append(
        $('<img>')
          .addClass('monitp')
          .attr('src', gbs.host + '/resources/img/topo/tpstander.png')
          .css({
            width: '100%',
            height: '100%',
            position: 'absolute',
            left: 0
          })
      )
      html2canvas(_canvas, {
        width: baseData.home.w * (cThis.paintObj.scale / 100),
        height: baseData.home.h * (cThis.paintObj.scale / 100),
        logging: false,
        scale: 0.4,
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
            paintConf: 'test',
            viewImage:
              '/mc/home/getImg/' + data.obj.isCustom + '/' + data.obj.id
          }
          cThis
            .axios({
              method: 'post',
              url: 'home/homePage',
              data: qs.stringify(_data),
              headers: { 'content-type': 'application/x-www-form-urlencoded' }
            })
            .then(res => {
              canvas.remove() // 这里是作为回调函数，canvas没有传过去
              _canvas.style.background = 'transparent'
              // $('#screen').hide()
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
        /* var _data = {
          id: cThis.pageId,
          viewConf: JSON.stringify(cThis.chartNum),
          viewImage: canvas.toDataURL('png')
        }
        cThis.axios({
          method: 'post',
          url: 'home/homePage',
          data: qs.stringify(_data),
          headers: { 'content-type': 'application/x-www-form-urlencoded' }
        }).then((res) => {
          canvas.remove()
          _canvas.style.background = 'transparent'
          $('#screen').hide()
        }) */
      })
    },
    uploadFile: function (formData, cb) {
      $.ajax({
        url: gbs.host + '/mc/home/file/upload',
        type: 'post',
        data: formData,
        async: false,
        cache: false,
        processData: false,
        contentType: false,
        success: function (data) {
          if (data.success) {
            // var _url = '/mc/home/getImg/' + data.obj.isCustom + '/' + data.obj.id
            typeof cb === 'function' && cb(data)
          } else {
            // $("#screen").hide()
            // tooltip('', data.msg, 'error')
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
      if (
        !(
          !this.widthVali.isShowError &&
          !this.heightVali.isShowError &&
          !this.xVali.isShowError &&
          !this.yVali.isShowError
        )
      ) {
        // tooltip('', '请填写正确的配置信息', 'info')
        Notification({
          message: '请填写正确的配置信息',
          position: 'bottom-right',
          customClass: 'toast toast-info'
        })
        return
      }
      this.pageData = this.chartNum
      this.viewPage = true
      // $.comps.mainPreview.open({
      //   conf: this.chartNum
      // })
    },
    hidePreview: function () {
      this.viewPage = false
    },
    back: function () {
      this.$router.push('editPage')
      // $.api.view.confirm({
      //   okText: '确定',
      //   msg: '确认离开当前页吗？未保存数据将会丢失！',
      //   callBack: function (state) {
      //     if (state === 1) { // 确定
      //       _this.$edit.edit('hide');
      //       typeof _this.params.callback === 'function' && _this.params.callback();
      //     }
      //   }
      // })
    },
    /* 右键 */
    context: function (index, ev, type) {
      // if (type !=='compose') {

      // }
      $(this.$refs.contextMenu)
        .css({
          left: ev.pageX,
          top: ev.pageY
        })
        .toggle(true)
      this.selectedIndex = index
    },
    // 组合右键 可以和上面公用一个方法
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
      console.log('注册框选事件')
      var stateBar = document.getElementById('chooseWrap')
      //                        var posLeft = stateBar.clientLeft;
      //                        var posTop = stateBar.clientTop;
      var posLeft = 210
      var posTop = 50
      document.onmousedown = function (e) {
        var posx = e.clientX - posLeft
        var posy = e.clientY - posTop
        var div = document.createElement('div')
        div.className = 'tempDiv'
        div.style.left = e.clientX - posLeft + 'px'
        div.style.top = e.clientY - posTop + 'px'
        // console.log(div);
        stateBar.appendChild(div)
        document.onmousemove = function (ev) {
          div.style.left = Math.min(ev.clientX - posLeft, posx) + 'px'
          div.style.top = Math.min(ev.clientY - posTop, posy) + 'px'
          div.style.width = Math.abs(posx - (ev.clientX - posLeft)) + 'px'
          div.style.height = Math.abs(posy - (ev.clientY - posTop)) + 'px'
          // console.log('MouseX: ' + ev.clientX - posLeft + '<br/>MouseY: ' + ev.clientY - posTop);
        }
        document.onmouseup = function () {
          div.parentNode.removeChild(div)
          document.onmousemove = null
          document.onmouseup = null
        }
      }
    },
    del: function () {
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
    },
    deleteOne: function (type, tempArr) {
      var i = 0
      tempArr = tempArr.sort()
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
        this.chooseIndexs = []
      }
      if (this.chooseCompIndexs.length > 0) {
        this.copyOne('compose', this.chooseCompIndexs)
        this.chooseCompIndexs = []
      }
    },
    copyOne: function (type, arr) {
      // 单个元件的复制操作
      if (type === 'item') {
        var _type = 'chartNum'
      } else {
        _type = 'combinList'
      }
      for (let i = 0, len = arr.length; i < len; i++) {
        this[_type][arr[i]].slted = false
        let tempItem = JSON.parse(JSON.stringify(this[_type][arr[i]]))
        tempItem.x += 20
        tempItem.y += 20
        tempItem.id = new Date().getTime() + parseInt(Math.random() * 100)
        // console.log(tempItem.id)
        this[_type].push(tempItem)
      }
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
        // return // tooltip('', '上传的文件不能大于15MB', 'info')
        Notification({
          message: '上传的文件不能大于15MB',
          position: 'bottom-right',
          customClass: 'toast toast-info'
        })
        return
      }
      var _this = this
      var formData = new FormData()
      formData.append('uploaded_file', e.target.files[0])
      this.uploadFile(formData, function (data) {
        if (!_this.selectedItem.chartType) {
          // 上传画布图片
          _this.paintObj.bgImg =
            '/mc/home/getImg/' + data.obj.isCustom + '/' + data.obj.id
          return
        }
        if (_this.selectedItem.chartType === 'image') {
          _this.selectedItem.imgSrc =
            '/mc/home/getImg/' + data.obj.isCustom + '/' + data.obj.id
        } else if (_this.selectedItem.subType === 'pictorialBar') {
          _this.selectedItem.symbolImg =
            '/mc/home/getImg/' + data.obj.isCustom + '/' + data.obj.id
        }
      })
      e.target.value = ''
    },
    getPaintCl (data) {
      // console.log(data.color)
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
      var defData = 0,
        isWidth = direct == 'width',
        valiType = direct + 'Vali'
      defData = isWidth ? baseData.home.w : baseData.home.h
      if (newValue < this.miniW || newValue > defData) {
        this[valiType].isShowError = true
        this[valiType].errorMsg = isWidth
          ? '宽度范围为' + this.miniW + '-' + defData
          : '高度范围为' + this.miniW + '-' + defData
      } else {
        this[valiType].isShowError = false
        this.selectedItem[direct] = newValue
      }
      // not Number
      if (Number(newValue) < this.miniW) {
        this.selectedItem[direct] = this.miniW
      } else {
        this.selectedItem[direct] = Number(this.selectedItem[direct])
      }
    },
    testObjPosChange (position, newValue) {
      var defData = 0,
        selectData = 0,
        isX = position === 'x',
        valiType = position + 'Vali'
      defData = isX ? baseData.home.w : baseData.home.h
      selectData = isX ? this.selectedItem.width : this.selectedItem.height
      if (selectData > defData) {
        this[valiType].isShowError = true
        this[valiType].errorMsg = isX
          ? '横轴位置范围为0-' + defData
          : '纵轴位置范围为0-' + defData
        if (newValue > defData) {
          this.testObj[position] = defData
        }
      } else {
        if (newValue < 0 || newValue > defData - selectData) {
          this[valiType].isShowError = true
          this[valiType].errorMsg = isX
            ? '横轴位置范围为0-' + (defData - selectData)
            : '纵轴位置范围为0-' + (defData - selectData)
          if (newValue > defData - selectData) {
            this.testObj[position] = defData - selectData
          }
        } else {
          this[valiType].isShowError = false
          this.selectedItem[position] = newValue
        }
      }
      // not Number
      this.selectedItem[position] = Number(this.selectedItem[position])
    }
  },
  watch: {
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
    // this.gerPageConf();
    // this.$nextTick(() => {
    //   this.chooseMap()
    // })
  },
  destoryed: function () {
    // $.comps.editHome = null;
  }
}
