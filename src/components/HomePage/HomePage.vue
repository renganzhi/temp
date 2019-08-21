<template>
  <div id="home-html"
       class="flex flex-vertical full-height full-width">
    <div class="portlet light bordered flex-1"
         id="mainbox">
      <div class="full-height pagebox">
        <DragBox v-for="(item,index) in nowPage"
                 :index="index"
                 :item="item"
                 :editable="editable"
                 :key="(pageIndex+index)"></DragBox>
      </div>
    </div>
    <div class="btm-tools"
         :class="isFullScreen?'full':''">
      <div class="fl btn-box">
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
        <!-- <el-tooltip class="item"
                    effect="dark"
                    content="Right Top 提示文字"
                    placement="right-start">
          2123123
        </el-tooltip> -->
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
  </div>
</template>

<script>
import { baseData } from '@/config/settings'
import DragBox from './../Common/DragBox'
import Public from '#/js/public'
import { Notification } from 'element-ui'
export default {
  name: 'HomePage',
  components: { DragBox, Notification },
  data () {
    return {
      isFullScreen: false,
      editable: false,
      pageList: [],
      nowPage: [],
      pageSize: 0,
      pageIndex: 0,
      refreshType: 'Automatic',
      refreshTimer: null, // 每页的刷新定时器
      refreshTime: 3, // 刷新时间
      intervalTime: 5, // 定时器时间默认5s
      timer: null, // 轮播定时器
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
  },
  methods: {
    getPageData: function () {
      // 获取大屏配置内容
      this.axios.get('home/homePage').then((data) => {
        if (data.success) {
          this.initPage(data.obj)
        }
      })
    },
    initPage: function (res) {
      this.pageSize = res.pages.length
      this.pageIndex = 0
      this.pageList = res.pages
      this.intervalTime = res.intervalTime || 5
      this.refreshTime = res.refreshTime || 3
      // this.refreshType = res.refreshType;
      if (this.pageSize) {
        this.timeFn()
        this.setScale()
      }
    },
    editPage: function () { // 编辑主页
      this.stopTimer()
      this.stopRefreshTimer()
      this.pageList = []
      this.nowPage = []
      this.pageIndex = 0
      this.$router.push('/editPage')
      // $.comps.editHome.open({
      //   exitCb: function () {
      //     _this.getPage();
      //   }
      // });
    },
    fullScreen: function () { // 切换全屏
      var ct = this
      Public.checkFull() ? this.exitFull() : this.full()
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
      // tooltip('', '鼠标移动到左/右下角对大屏操作', 'info', {
      //   target: '#home-html'
      // })
      $('#home-html').css('background', $('body').css('background'))
      Public.bigScreenfullScreen($('#home-html').get(0))
      this.isFullScreen = true
      this.interTimer()
    },
    prev: function () { // 上一页
      this.pageIndex--
      if (this.pageIndex === 0) {
        this.pageIndex = this.pageSize
      }
      this.nowPage = JSON.parse(this.pageList[(this.pageIndex - 1) % this.pageSize].viewConf)
      this.isFullScreen && this.interTimer()
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
      this.nowPage = JSON.parse(this.pageList[(this.pageIndex - 1) % this.pageSize].viewConf)
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

    /* 刷新页面相关 */
    refresh: function () {
      this.refreshFn()
    },
    stopRefreshTimer: function () {
      this.refreshTimer && clearTimeout(this.refreshTimer)
      this.refreshTimer = null
    },
    initRefreshTimer: function () {
      var ct = this
      this.stopRefreshTimer()
      /* if(this.refreshType === 'Manual'){
          return;
      } */
      // 定时器
      this.refreshTimer = setTimeout(function () {
        if (!$('#home-html').length) {
          ct.clearPage()
          return
        }
        ct.refreshFn()
        // ct.refreshTimer = setTimeout(arguments.callee, ct.refreshTime * 1000)
      }, this.refreshTime * 1000)
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

        if (d.url) {
          $.each(d.params, function (i, o) {
            d.params[i] = $.isArray(o) ? o.join(',') : o
          })
          ct.axios({
            method: d.method || 'get',
            url: d.url,
            data: d.params
          }).then((res) => {
            res.obj = res.obj || []
            if (res.obj.colors) {
              d.ctColors = res.obj.colors
            }
            d.chartData = res.obj
          })
          // $.ajax({
          //   url: d.url,
          //   data: d.params,
          //   type: d.method || 'get',
          //   ascyn: false,
          //   success: function (res) {
          //     res.obj = res.obj || [];
          //     if (res.obj.colors) {
          //       d.ctColors = res.obj.colors;
          //     }
          //     d.chartData = res.obj;
          //   }
          // })
        }
      })
      ct.setScale()
    },

    /* 缩放 */
    setScale: function () {
      // var $el = $(this.$el);
      // var $el = document.getElementById('home-html')
      var $el = $('#home-html')
      var w = $el.width()
      var h = $el.height()
      var scaleX = w / baseData.home.w
      var scaleY = h / baseData.home.h
      var scale = Math.min(scaleX, scaleY)
      var mrg = 0
      if (scaleX <= 1) {
        mrg = [0, (w - baseData.home.w * scale) / 2 + 'px'].join(' ')
      }
      $el.find('.pagebox').css({
        transform: 'scale(' + scale + ',' + scale + ')',
        margin: mrg
      })
    },
    clearPage: function () { // 离开主页清定时器
      this.stopTimer()
      this.stopRefreshTimer()
      this.$destroy()
    }
  },
  destroyed () {
    // mainPage = _this = _this.vue = null;
  },
  mounted: function () {
    this.getPageData()
    Notification({
      message: '鼠标移动到左/右下角对大屏操作',
      position: 'bottom-right',
      customClass: 'toast toast-info'
    })
    // titleShow('top', $('#home-html'));
    // $('#home-html').tooltip()
    $(window).off('resize.homescale').on('resize.homescale', () => {
      this.setScale()
    })
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
      this.initRefreshTimer()
    }
  }
}
</script>
<style scoped>
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
  z-index: 100 !important;
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
  border-radius: 50%;
  text-align: center;
  line-height: 30px;
  opacity: 0.3;
  margin: 0 4px;
}

#home-html .pagebox {
  transform-origin: 0 0;
  -webkit-transform-origin: 0 0;
  -moz-transform-origin: 0 0;
  -ms-transform-origin: 0 0;
}

#home-html .ring-icon:hover {
  opacity: 1;
}

#home-html .ring-icon [class*="icon-n-"] {
  font-size: 13px;
  color: #8fbae5;
}

#home-html .btm-tools {
  margin-bottom: -8px;
  position: fixed;
  bottom: 12px;
  width: 100%;
  padding-right: 30px;
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

.v-charts-data-empty {
  background-color: rgba(28, 36, 60, 0.71) !important;
  color: inherit !important;
}
</style>
