<template>
  <div id="home-html"
       class="flex flex-vertical full-height full-width">
    <div style="width: 100%; height: 100%;"
         v-if="loadAll && pageList.length < 1">
      <div class="homeEmpty">
        <img v-if="defTheme"
             src="../../assets/homeEmpty1.png" />
        <img v-else
             src="../../assets/homeEmpty.png" />
        <div>
          <p v-show="isNewUser"
             style="margin: 30px 0px;">还没有设置可展示的大屏页面！</p>
          <p v-show="!isNewUser"
             style="margin: 30px 0px;">请配置可展示的大屏页面！</p>
          <button type="button"
                  v-if="access==='w' && isNewUser"
                  @click="addPage = true">新增页面</button>
        </div>
      </div>
      <AddPage :showModal="addPage"
               @hideModal="hideModal"></AddPage>
    </div>

    <div class="portlet light bordered flex-1"
         id="paintWrap">
      <div id="mainbox"
           v-show="pageList.length >= 1"></div>
      <div class="home_wrapBox">
        <div class="full-height pagebox">
          <LookItem v-for="(item,index) in nowPage"
                    :index="index"
                    :item="item"
                    :key="(pageIndex+index)"></LookItem>
          <LookCompose v-for="(list, index1) in combinList"
                       :index="index1"
                       :key="list.id"
                       :list="list"></LookCompose>
        </div>
      </div>
    </div>
    <div v-if="loadAll">
      <div class="btm-tools"
           :class="isFullScreen?'full':''">
        <div class="fl btn-box"
             v-show="!isNewUser">
          <span @click="editPage"
                class="ring-icon"
                title="编辑"
                v-show="!isFullScreen"><i class="icon-n-set"></i></span>
          <span @click="refresh"
                class="ring-icon"
                :title="isFullScreen ? '刷新' : ' 刷新 '"><i class="icon-n-freshen"></i></span>
          <span @click="fullScreen"
                class="ring-icon"
                :title="isFullScreen ? '退出全屏' : '全屏'"><i :class="isFullScreen ? 'icon-n-exitFull' : 'icon-n-fullScreen'"></i></span>
        </div>
        <div class="fr btn-box"
             v-show="pageSize>1">
          <span @click="prev"
                class="ring-icon"
                :title="isFullScreen ? '上一页' : ' 上一页 '"><i class="icon-n-prev"></i></span>
          <span @click="togglePlay"
                class="ring-icon"
                :title="!timer ? '开启轮播' : '暂停轮播'"
                v-show="isFullScreen"><i :class="!timer ? 'icon-n-lunbo' : 'icon-n-suspend'"></i></span>
          <span @click="next"
                class="ring-icon"
                :title="isFullScreen ? '下一页' : ' 下一页 '"><i class="icon-n-next"></i></span>
        </div>
      </div>

      <div role="alert"
           v-if="showTip"
           class="el-notification toast toast-info right"
           style="bottom: 16px; z-index: 2001;">
        <div class="el-notification__group">
          <h2 class="el-notification__title"></h2>
          <div class="el-notification__content">
            <p>鼠标移动到左/右下角对大屏操作</p>
          </div>
          <div class="el-notification__closeBtn el-icon-close"></div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { baseData, gbs } from '@/config/settings'
import LookItem from './../Common/LookItem'
import LookCompose from './../Common/LookCompose'
import Public from '#/js/public'
import AddPage from './../EditPage/AddPage'
import { Notification } from 'element-ui'
import { mapActions, mapGetters } from 'vuex'
export default {
  name: 'HomePage',
  components: { Notification, LookItem, LookCompose, AddPage },
  data () {
    return {
      defTheme: true, // 默认主题
      isFullScreen: false,
      editable: false,
      isNewUser: false,
      showTip: false, // 全屏的提示信息
      addPage: false,
      access: 'r',
      loadAll: false, // 请求完成之后再展示页面
      pageList: [],
      combinList: [],
      paintConf: {},
      nowPage: [],
      pageSize: 0,
      pageIndex: 0,
      refreshType: 'Automatic',
      refreshTimer: null, // 每页的刷新定时器
      refreshTime: 3, // 刷新时间
      intervalTime: 5, // 定时器时间默认5s
      timer: null, // 轮播定时器
      freshInterval: null, // 定时器
      nowTime: 0, // 当前页面已停留多少秒
      // autoPlay:true,
      item: {
        chartType: 've-gauge',
        colorType: 'defalut',
        bdpx: 1,
        ctDataSource: 'static',
        ctLegendShow: false,
        ctName: '目标占比图',
        fontSize: 12,
        height: 200,
        id: 1564540601883,
        imgClass: 'icon-n-percent',
        lineArea: false,
        params: {},
        slted: false,
        subType: 'progress',
        url: '',
        width: 200,
        x: 66,
        y: 43,
        ctColors: ['#37a2da', '#32c5e9', '#67e0e3', '#9fe6b8', '#ffdb5c', '#ff9f7f', '#fb7293', '#e062ae', '#e690d1', '#e7bcf3', '#9d96f5', '#8378ea', '#96bfff'],
        chartData: { name: '繁忙度', unit: '%', value: 60 }
      }
    }
  },
  computed: {
    ...mapGetters([
      'videoTims'
    ])
  },
  methods: {
    ...mapActions([
      'changeAlertInfo',
      'initVideoTims'
    ]),
    hideModal (data) {
      this.addPage = false
      if (data.ifAdd) {
        this.$router.push('/edit/' + data.addId)
      }
    },
    getPageData: function () {
      // 获取大屏配置内容
      this.axios.get('/leaderview/home/homePage').then((data) => {
        if (data.success) {
          this.initPage(data.obj)
        } else {
          this.loadAll = true
        }
      })
    },
    initPage: function (res) {
      this.pageSize = res.pages.length
      this.pageIndex = 0
      this.pageList = res.pages
      this.isNewUser = res.isNewUser
      this.loadAll = true
      this.intervalTime = res.intervalTime || 5
      this.refreshTime = res.refreshTime || 3
      // this.refreshType = res.refreshType;
      if (this.pageSize) {
        this.timeFn()
        this.$nextTick(() => {
          this.setScale()
        })
      }
    },
    editPage: function () { // 编辑主页
      this.stopTimer()
      this.stopRefreshTimer()
      this.pageList = []
      this.nowPage = []
      this.combinList = []
      this.pageIndex = 0
      this.$router.push('/editPage')
    },
    fullScreen: function () { // 切换全屏
      if (this.pageList.length === 0) {
        if (gbs.inDev) {
          Notification({
            message: '请配置可展示的大屏页面',
            position: 'bottom-right',
            customClass: 'toast toast-info'
          })
        } else {
          tooltip('', '请配置可展示的大屏页面', 'info')
        }
        return
      }
      var ct = this
      Public.checkFull() ? this.exitFull() : this.full()
      $('.tp-tip').remove()
      $('.tooltip.in').remove()
      // this.isFullScreen ? this.exitFull() : this.full()
      $(window).on('resize.home', function () {
        !Public.checkFull() && ct.exitFull()
      })
    },
    exitFull: function () { // 退出全屏
      $('#home-html').css('background', 'transparent')
      this.stopTimer()
      this.isFullScreen = false
      Public.exitFullScreen()
      $(window).off('resize.home')
    },
    full: function () { // 全屏
      if (gbs.inDev) {
        this.showTip = true
        setTimeout(() => {
          this.showTip = false
        }, 3500)
      } else {
        tooltip('', '鼠标移动到左/右下角对大屏操作', 'info', {
          target: '#home-html'
        })
      }
      $('#home-html').css('background', $('body').css('background'))
      Public.bigScreenfullScreen($('#home-html').get(0))
      // Public.bigScreenfullScreen($('.home_wrapBox').get(0))
      this.isFullScreen = true
      this.interTimer()
    },
    prev: function () { // 上一页
      this.pageIndex--
      if (this.pageIndex === 0) {
        this.pageIndex = this.pageSize
      }
      var nowPageObj = this.pageList[(this.pageIndex - 1) % this.pageSize]
      if (nowPageObj.composeObj) {
        this.combinList = JSON.parse(nowPageObj.composeObj)
      } else {
        this.combinList = []
      }
      if (nowPageObj.paintObj) {
        this.paintConf = JSON.parse(nowPageObj.paintObj)
      } else {
        this.paintConf = {}
      }
      this.setPaint()
      this.nowPage = JSON.parse(nowPageObj.viewConf)
      this.isFullScreen && this.interTimer()
    },
    setPaint: function () {
      if (this.paintConf) {
        if (this.paintConf.bgImg) {
          $('#mainbox').css('background', 'url(' + gbs.host + '/leaderview' + this.paintConf.bgImg + ')')
        } else {
          $('#mainbox').css('background', '')
        }
        $('#mainbox').css('opacity', this.paintConf.opacity / 100)
        if (this.paintConf.bgColor) {
          $('#paintWrap').css('background', this.paintConf.bgColor)
        } else {
          $('#paintWrap').css('background', '')
        }
        if (this.paintConf.bgStyle) {
          var type = this.paintConf.bgStyle
          if (type === '1') {
            var backgroundSize = '100% auto'
          } else if (type === '2') {
            backgroundSize = 'auto 100%'
          } else {
            backgroundSize = '100% 100%'
          }
          $('#mainbox').css('background-size', backgroundSize)
        } else {
          $('#mainbox').css('background-size', '')
        }
      } else {
        $('#mainbox').css('background', '')
        $('#mainbox').css('background-size', '')
      }
    },
    togglePlay: function () { // 开启/暂停
      this.timer ? this.stopTimer() : this.interTimer()
    },
    next: function () { // 下一页
      this.timeFn()
      this.isFullScreen && this.interTimer()
    },
    /* 轮播切换相关 */
    timeFn: function () { // 轮播
      this.pageIndex++
      var nowPageObj = this.pageList[(this.pageIndex - 1) % this.pageSize]
      if (nowPageObj.composeObj) {
        this.combinList = JSON.parse(nowPageObj.composeObj)
      } else {
        this.combinList = []
      }
      if (nowPageObj.paintObj) {
        this.paintConf = JSON.parse(nowPageObj.paintObj)
      } else {
        this.paintConf = {}
      }
      this.setPaint()
      this.nowPage = JSON.parse(nowPageObj.viewConf)
      $('.tp-tip').remove()
      $('.tooltip.in').remove()
    },
    stopTimer: function () {
      this.timer && clearTimeout(this.timer)
      this.timer = null
    },
    interTimer: function () { // 使用setTimeout模拟setInterval，初始化轮播定时器
      var ct = this
      this.stopTimer()
      if (this.pageSize <= 1) {
        return
      }
      this.timer = setTimeout(function test () {
        ct.timeFn()
        ct.timer = setTimeout(test, ct.intervalTime * 1000)
      }, ct.intervalTime * 1000)
    },
    autoFresh: function () {
      var ct = this
      this.stopRefreshTimer()
      this.nowTime = 0
      this.freshInterval = setTimeout(function fresh () {
        if (!$('#home-html').length) {
          ct.clearPage()
          return
        }
        ct.nowTime += 1
        ct.refreshTargetFn()
        ct.freshInterval = setTimeout(fresh, 1000)
      }, 1000)
    },

    /* 刷新页面相关 */
    refresh: function () {
      this.refreshFn()
      this.refreshCompose()
    },
    stopRefreshTimer: function () {
      this.freshInterval && clearTimeout(this.freshInterval)
      this.freshInterval = null // 每秒刷新——触发单个元件刷新
      this.refreshTimer && clearTimeout(this.refreshTimer)
      this.refreshTimer = null // 整页刷新
    },
    initRefreshTimer: function () {
      var ct = this
      this.stopRefreshTimer()
      /* if(this.refreshType === 'Manual'){
          return;
      } */
      // 定时器
      this.refreshTimer = setTimeout(function freshFn () {
        if (!$('#home-html').length) {
          ct.clearPage()
          return
        }
        ct.refreshFn()
        ct.refreshCompose()
        ct.refreshTimer = setTimeout(freshFn, ct.refreshTime * 1000)
        // ct.refreshTimer = setTimeout(arguments.callee, ct.refreshTime * 1000)
      }, this.refreshTime * 1000)
    },
    refreshTargetFn: function (newV) { // 刷新本页数据
      newV = newV || this.nowPage
      var ct = this
      if (!newV) return
      $.each(newV, function (i, d) {
        let freshTime = d.refreshTm ? d.refreshTm : 5 // 这里是刷新周期
        if (ct.nowTime % freshTime === 0 && d.chartType === 'topo') {
          ct.$set(d, 'time', new Date().getTime())
        }

        if (d.ctDataSource == 'system' && d.url && ct.nowTime % freshTime === 0) {
          $.each(d.params, function (i, o) {
            d.params[i] = $.isArray(o) ? o.join(',') : o
          })
          $.ajax({
            url: gbs.host + d.url,
            data: d.params,
            type: d.method || 'get',
            ascyn: false,
            success: function (res) {
              res.obj = res.obj || []
              if (res.obj.colors) {
                d.ctColors = res.obj.colors
              }
              if (d.chartType === 'marquee' || d.chartType === 'text') {
                d.ctName = res.obj.info
              }
              if (d.chartType !== 'marquee') {
                d.chartData = res.obj
              }
            },
            error: function (xhr) {
              if (xhr.status != 776) {
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
            }
          })
        }
      })
      // this.$nextTick(() => {
      //   ct.setScale()
      // })
    },
    refreshFn: function (newV) { // 刷新本页数据
      newV = newV || this.nowPage
      var ct = this
      $.each(newV, function (i, d) {
        if (d.chartType === 'topo') {
          ct.$set(d, 'time', new Date().getTime())
          /* var tpid = d.tpId
          d.tpId = '';
          ct.$nextTick(function(){
              d.tpId = tpid;
          }) */
        }

        if (d.ctDataSource == 'system' && d.url) {
          $.each(d.params, function (i, o) {
            d.params[i] = $.isArray(o) ? o.join(',') : o
          })
          $.ajax({
            url: gbs.host + d.url,
            data: d.params,
            type: d.method || 'get',
            ascyn: false,
            success: function (res) {
              res.obj = res.obj || []
              if (res.obj.colors) {
                d.ctColors = res.obj.colors
              }
              if (d.chartType === 'marquee' || d.chartType === 'text') {
                d.ctName = res.obj.info
              }
              if (d.chartType !== 'marquee') {
                d.chartData = res.obj
              }
            },
            error: function (xhr) {
              if (xhr.status != 776) {
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
            }
          })
        }
      })
      this.$nextTick(() => {
        ct.setScale()
      })
    },
    refreshCompose: function () {
      if (this.combinList && this.combinList.length > 0) {
        this.combinList.forEach((list) => {
          this.refreshFn(list.child)
        })
      }
    },

    /* 缩放setScale */
    setScale: function () {
      // var $el = document.getElementById('home-html')
      var $el = $('#home-html')
      var w = $el.width()
      // var h = $el.height()
      var pageContainer = $('#page_container')
      if (this.isFullScreen) {
        var h = $el.height()
      } else {
        if (pageContainer.length) {
          h = pageContainer.height() // 打包的时候获取不到高度使用这个
        } else {
          h = $el.height()
        }
      }
      // console.log('app width:' + _app.width() + '  app height: ' + _app.height())
      var paintW = (this.paintConf && this.paintConf.width) || 1920
      var paintH = (this.paintConf && this.paintConf.height) || 1080
      var scaleX = w / paintW // 这里需要改成设置的画布的大小
      var scaleY = h / paintH
      var scale = Math.min(scaleX, scaleY)
      var mrg = 0
      // if (scaleX <= 1) {
      // var _width = this.paintConf.width || baseData.home.w
      // mrg = [0, (w - _width * scale) / 2 + 'px'].join(' ')
      mrg = [0, Math.abs(w - paintW * scale) / 2 + 'px'].join(' ')
      // }
      if (this.isFullScreen) {
        var boxMrg = [0, Math.abs(w - paintW * scale) / 2 + 'px'].join(' ')
        $el.find('.pagebox').css({
          transform: 'scale(' + scale + ',' + scale + ')',
          width: paintW + 'px',
          height: paintH + 'px',
          overflow: 'hidden',
          margin: boxMrg
        })
        $el.find('.home_wrapBox').css({
          margin: 0
        })
      } else {
        $el.find('.pagebox').css({
          transform: 'scale(' + scale + ',' + scale + ')',
          width: 'auto',
          height: 'auto',
          overflow: 'visible',
          margin: 0
        })
        $el.find('.home_wrapBox').css({
          margin: mrg
        })
      }
    },
    clearPage: function () { // 离开主页清定时器
      this.stopTimer()
      this.stopRefreshTimer()
      this.$destroy()
    },
    getAccess () {
      let permission = 'r'
      // 先获取simo存在本地的
      if (baseData && baseData.menuParam && baseData.menuParam.permission) {
        permission = baseData.menuParam.permission.toLowerCase().split(',')
        if (permission.indexOf('w') !== -1) {
          this.access = 'w'
        } else {
          this.access = 'r'
        }
        sessionStorage.setItem('leaderAccess', this.access)
      } else {
        this.axios.get('/mc/getMenu').then((res) => {
          if (res.success) {
            let obj = res.obj
            obj.forEach(item => {
              if (item.id === 'VIEW01' || item.name === '大屏展示') {
                permission = item.permission.toLowerCase().split(',')
                if (permission.indexOf('w') !== -1) {
                  this.access = 'w'
                } else {
                  this.access = 'r'
                }
                sessionStorage.setItem('leaderAccess', this.access)
                return false
              }
            })
          }
        })
      }
    }
  },
  watch: {
    nowPage: function (newV) {
      $(this.$el).find('.pagebox').css({
        transform: 'scale(1)'
      })
      this.stopRefreshTimer()
      if (!newV) {
        return []
      }
      this.refreshFn(newV)
      this.autoFresh()
      // this.initRefreshTimer() 取消整页刷新
    },
    combinList: function () {
      this.refreshCompose()
    }
  },
  beforeMount: function () {
    this.axios.get('/alert/currencyAlertmanager/findAlertLevelList').then((res) => {
      this.changeAlertInfo(res.obj)
    })
  },
  mounted: function () {
    var _url = window.location.protocol + '//' + window.location.host + '/index'
    window.history.pushState({}, '', _url)
    this.getAccess()
    this.$nextTick(() => {
      this.getPageData()
      $(window).off('resize.homescale').on('resize.homescale', () => {
        this.$nextTick(() => {
          this.setScale()
        })
      })
    })
    var theme = $('html').attr('data-theme')
    if (theme !== 'default') {
      this.defTheme = false
    }
    var videoTims = this.videoTims
    for (let i in videoTims) {
      videoTims[i] = 0
    }
    this.initVideoTims(videoTims) // 进入大屏展示页时都初始化一次视频播放的时间
    if (!gbs.inDev) {
      titleShow('top', $('#home-html'))
    }
    this.autoFresh()
    $('#screen').addClass('disShow')
  },
  beforeDestroy: function () {
    $('#screen').removeClass('disShow')
    this.stopRefreshTimer()
  },
  destroyed: function () {
    if ($('.tooltip').length > 0) {
      $(this.$el).find('[title]').tooltip('destroy')
    }
  }
}
</script>
<style scoped lang="scss">
/*   drag-class */
.vdr {
  position: absolute;
  box-sizing: border-box;
}
.vdr-stick-tl,
.vdr-stick-br {
  cursor: nwse-resize;
}
.vdr-stick-tm,
.vdr-stick-bm {
  left: 50%;
  cursor: ns-resize;
}
.vdr-stick-tr,
.vdr-stick-bl {
  cursor: nesw-resize;
}
.vdr-stick-ml,
.vdr-stick-mr {
  top: 50%;
  cursor: ew-resize;
}
.vdr-stick.not-resizable {
  display: none;
}
.newDrag {
  opacity: 1;
  background: transparent;
}
.child-drag {
  z-index: -299 !important;
}

#home-html .ring-icon {
  display: inline-block;
  width: 30px;
  height: 30px;
  background: #15192a;
  // background: rgba(21, 25, 42, 0.3);
  border-radius: 50%;
  text-align: center;
  line-height: 30px;
  opacity: 0.3;
  margin: 0 4px;
  cursor: pointer;
}

#home-html .pagebox {
  transform-origin: 0 0;
  -webkit-transform-origin: 0 0;
  -moz-transform-origin: 0 0;
  -ms-transform-origin: 0 0;
}
.home_wrapBox {
  height: 100%;
  overflow: hidden;
}
#home-html .ring-icon:hover {
  opacity: 1;
}

#home-html .ring-icon [class*="icon-n-"] {
  font-size: 13px;
  color: #8fbae5;
}

#home-html .btm-tools {
  margin-bottom: -3px;
  position: fixed;
  bottom: 12px;
  width: 100%;
  padding-right: 30px;
  padding-left: 15px;
}

#home-html .homeEmpty {
  text-align: center;
  position: relative;
  top: 50%;
  margin-top: -153px;
}
#home-html .btm-tools.full {
  bottom: 22px;
  padding-right: 0px;
}

#home-html .btm-tools.full .btn-box {
  opacity: 0;
}

#home-html .btm-tools.full .btn-box:hover {
  opacity: 1;
}
#home-html #mainbox {
  width: 100%;
  height: 100% !important;
  position: absolute;
  top: 0px;
  height: 0px;
}

html[data-theme="blackWhite"],
html[data-theme="blueWhite"] {
  textarea {
    background: transparent !important;
    background-color: transparent !important;
  }
  #home-html .ring-icon {
    background: #edf3fe;
    opacity: 1;
  }
  #home-html .ring-icon [class*="icon-n-"] {
    color: #0089ff;
  }
}
</style>
