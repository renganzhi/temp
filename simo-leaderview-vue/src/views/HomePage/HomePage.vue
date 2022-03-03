<template>
  <div
    style="width: 100%;height: calc(100% - 50px);top: 50px;position: absolute;"
  >
    <navBar></navBar>
    <div id="home-html" class="flex flex-vertical full-height full-width">
      <div
        style="width: 100%; height: 100%;"
        v-if="loadAll && pageList.length < 1"
      >
        <div class="homeEmpty">
          <img v-if="defTheme" src="../../navBar/images/no_data_back.png" />
          <img v-else src="../../navBar/images/no_data.png" />
          <div>
            <p v-show="isNewUser" style="margin: 30px 0px;">
              还没有设置可展示的大屏页面！
            </p>
            <p v-show="!isNewUser" style="margin: 30px 0px;">
              请配置可展示的大屏页面！
            </p>
            <button
              type="button"
              v-if="access === 'w' && isNewUser"
              @click="addPage = true"
            >
              新增
            </button>
            <button
              type="button"
              v-if="isSuperAdmin && isNewUser"
              @click="showImport = true"
            >
              导入
            </button>
          </div>
        </div>
        <AddPage :showModal="addPage" @hideModal="hideModal"></AddPage>

        <ImportPage
          :showModal="showImport"
          @hideModal="hideImportModal"
          :tems="pageList"
        ></ImportPage>
      </div>
      <transition v-else :name="moveBox1">
        <div
          class="portlet light bordered flex-1"
          v-show="moveFlag"
          id="paintWrap"
        >
          <div id="mainbox" v-show="pageList.length >= 1"></div>
          <div class="home_wrapBox">
              <div class="back" style="height: 2160px;width: 3840px;position: absolute;">
                <beijing :nowPageID="pageID"></beijing>
              </div>
            <div class="full-height pagebox">
              <div class="Tbaleban"  v-if="showTableBox">
                <div class="TableBox">
                  <div class="closeBtn" @click="closeTableTtn()"></div>
                    <div class="BoxTitle">{{TableData.title}}</div>
                    <div class="TableHead">
                        <tr>
                          <th v-for="(data, index) in tableData.columns" :key="index" :style="{width:`calc(${100 / tableData.columns.length}%)`}">
                            {{ data }}
                          </th>
                        </tr>
                    </div>
                    <div class="TableBody">
                      <tr  v-for="(rowsData, i) in tableData.rows" :key="i"  @click="showXQ(rowsData)">
                        <th v-for="(data, index) in tableData.columns" :key="index"  :style="{width:`calc(${100 / tableData.columns.length}%)`}">
                          {{  rowsData[data] }}
                        </th>
                      </tr>
                    </div>
                </div>
              </div>
              <div class="BoxMban"  v-if="showModelBox">
                <div class="ModelBox">
                  <div class="closeBtn" @click="closeBoxTtn()"></div>
                  <div class="BoxTitle">{{boxData.title}}</div>
                  <div class="BoxBody" v-if="showModelBoxtype === 0">
                    <div class="lineBox" v-for="(data,index) in boxData.data" :key="index">
                      <div class="Nmae" v-if="data.title !== '详情'">{{data.title}} : </div>
                      <div class="Data" v-if="data.title !== '详情' && data.title !== '失控状态'" :style="{
                          color: data.value && data.value.color? data.value.color:'#5983b6'
                        }">{{ data.value.value ? data.value.value : data.value}} </div>
                      <div class="selectData" style="position: relative;" v-if="data.title === '失控状态'">
                        <Select v-model="data.value">
                            <Option value="1">1级 </Option>
                            <Option value="2">2级 </Option>
                            <Option value="3">3级 </Option>
                        </Select>
                        <div class="suerBtn" style="display: inline-block;">
                          <Button style="background:#5c8bff;" @click="onSure">
                            确定
                          </Button>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div v-else-if="showModelBoxtype === 1">
                    <div class="DataValue" v-for="(data,index) in boxData.data" :key="index">
                      {{ data.value }}
                    </div>
                  </div>
                  <div v-else-if="showModelBoxtype === 2">
                  </div>
                </div>
              </div>
              <div class="ParentBox">
                <div class="BoxArry">
                  <div class="SmallBox" v-if="OpenBox" @mousemove="OpenBox = false"></div>
                  <div class="BigBox" v-else @mouseleave="OpenBox = true">
                    <div class="CloseBox" @click="OpenBox = true"></div>
                    <div class="AhrefBox" @click="exchangeisOpenTW()"><div :class="isOpenTW?'openBox':'closeStyle'"></div> <a href="">天网调度</a></div>
                    <div class="AhrefBox"><a href="">视频调度</a></div>
                    <div class="AhrefBox"><a href="">语音调度</a></div>
                    <div class="AhrefBox" @mousemove="OpenChileBox = true" @mouseleave="OpenChileBox = false"><a href="">事件调度</a></div>
                    <div class="ChildrenBox" v-if="OpenChileBox"  @mousemove="OpenChileBox = true" @mouseleave="OpenChileBox = false">
                      <a href="">社区</a>
                      <a href="">专版/指挥部</a>
                      <a href="">网格长</a>
                      <a href="">委办局/街道</a>
                    </div>
                  </div>
                </div>
              </div>
              <LookItem
                v-for="(item, index) in nowPage"
                :index="index"
                :item="item"
                :key="item.id"
              ></LookItem>
              <LookCompose
                v-for="(list, index1) in combinList"
                :index="index1"
                :key="list.id"
                :list="list"
              ></LookCompose>
            </div>
          </div>
        </div>
      </transition>
      <!-- 下一页 -->
      <transition :name="moveBox2">
        <div
          class="portlet light bordered flex-1"
          v-show="!moveFlag"
          id="paintWrap2"
        >
          <div id="mainbox2" v-show="pageList.length >= 1"></div>
          <div class="home_wrapBox">
            <div class="full-height pagebox">
              <LookItem
                v-for="(item, index) in nowPage2"
                :index="index"
                :item="item"
                :key="item.id"
              ></LookItem>
              <LookCompose
                v-for="(list, index1) in combinList2"
                :index="index1"
                :key="list.id"
                :list="list"
              ></LookCompose>
            </div>
          </div>
        </div>
      </transition>
      <!-- 下一页 -->
      <div v-show="loadAll" id="homeTips">
        <div class="btm-tools" :class="isFullScreen ? 'full' : ''">
          <div class="fl ringparent" v-show="!isNewUser">
            <span
              @click="editPage"
              class="ring-icon"
              title
              data-original-title="设置"
              v-show="!isFullScreen"
              ><i class="icon-n-set"></i
            ></span>
            <span
              @click="toEditPage()"
              class="ring-icon"
              data-toggle="tooltip"
              ref="editbutton"
              title
              data-original-title="编辑当前页"
              v-show="!isFullScreen"
              ><i class="icon-n-edit"></i
            ></span>
            <span
              @click="refresh"
              class="ring-icon"
              data-toggle="tooltip"
              title
              :data-original-title="isFullScreen ? '刷新' : ' 刷新 '"
              ><i class="icon-n-freshen"></i
            ></span>
            <span
              @click="fullScreen"
              class="ring-icon"
              data-toggle="tooltip"
              title
              :data-original-title="isFullScreen ? '退出全屏' : '全屏'"
              ><i
                :class="isFullScreen ? 'icon-n-exitFull' : 'icon-n-fullScreen'"
              ></i
            ></span>
          </div>
          <div class="fr ringparent">
            <span
              @click="prev"
              class="ring-icon"
              data-toggle="tooltip"
              title
              v-show="showPagination"
              :data-original-title="isFullScreen ? '上一页' : ' 上一页 '"
              ><i class="icon-n-prev"></i
            ></span>
            <span
              @click="togglePlay"
              class="ring-icon"
              data-toggle="tooltip"
              title
              :data-original-title="!timer ? '开启轮播' : '暂停轮播'"
              v-show="showPagination && isFullScreen"
              ><i :class="!timer ? 'icon-n-lunbo' : 'icon-n-suspend'"></i
            ></span>
            <span
              @click="next"
              class="ring-icon"
              data-toggle="tooltip"
              title
              v-show="showPagination"
              :data-original-title="isFullScreen ? '下一页' : ' 下一页 '"
              ><i class="icon-n-next"></i
            ></span>
          </div>
        </div>

        <div
          role="alert"
          v-if="showTip"
          class="el-notification toast toast-info right"
          style="bottom: 16px; z-index: 2001;"
        >
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
  </div>
</template>

<script>
import navBar from '../../../src/navBar/index.vue'
import { baseData, gbs } from '@/config/settings'
import LookItem from '@/components/Common/LookItem'
import LookCompose from '@/components/Common/LookCompose'
import beijing from '@/components/EditComp/beijing'
import ImportPage from './../EditPage/ImportPage'
import { Public, titleShowFn } from '#/js/public'
import AddPage from './../EditPage/AddPage'
import { Notification } from 'element-ui'
import { mapActions, mapMutations, mapGetters } from 'vuex'
import { checkLogin } from '@/config/thirdLoginMix'
export default {
  name: 'lookPage',
  components: {
    Notification,
    LookItem,
    LookCompose,
    AddPage,
    ImportPage,
    beijing,
    navBar
  },
  // mixins:[thirdLoginMix],
  data () {
    return {
      moveBox1: 'moveLeft1',
      moveBox2: 'moveLeft2',
      showModelBoxtype: 0,
      tableData:{
        columns: [
          '姓名',
          '入住时间',
          '登记入住旅馆',
          '预警提示'
        ],
        rows: [
          {
            '姓名': '高阳',
            '入住时间': '1月28日',
            '登记入住旅馆': '武侯区一环路西一段11号3栋',
            '身份证号码': '54215454545465654684645',
            '登记时间': '1月28日',
            '手机号码': '14562878554568',
            '预警提示': '[公安] 无异常 [本地] 矫正人员',
          }
        ]
      },
      showImport: false,
      showModelBox: false,
      showTableBox: false,
      boxData: {},
      TableData: {},
      isSuperAdmin: false,
      OpenBox: true,
      isOpenTW: false,
      OpenChileBox: false,
      moveFlag: true,
      defTheme: true, // 默认主题
      isFullScreen: false,
      editable: false,
      isNewUser: false,
      showTip: false, // 全屏的提示信息
      addPage: false,
      access: 'r',
      loadAll: false, // 请求完成之后再展示页面
      xhrArr: [], // 刷新数据的ajax请求，离开页面时取消
      pageList: [],
      combinList: [],
      combinList2: [],
      paintConf: {},
      paintConf2: {},
      nowPage: [],
      nowPage2: [],
      pageSize: 0,
      pageIndex: 0,
      refreshType: '1',
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
        ctColors: [
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
        chartData: { name: '繁忙度', unit: '%', value: 60 }
      },
      animationType: [
        've-pie',
        've-ring',
        've-histogram',
        've-bar',
        've-line',
        've-radar'
      ]
    }
  },
  computed: {
    ...mapGetters(['pageVisiable', 'videoTims', 'editId', 'nowPageId']),
    showPagination () {
      return this.pageSize > 1
    },
    pageID(){
      return this.pageList[(this.pageIndex - 1) % this.pageSize].id
    }
  },
  methods: {
    ...mapActions(['changeAlertInfo', 'initVideoTims', 'changePageVisiable']),
    ...mapMutations(['changeEditId', 'changeNowPage']),
    toEditPage () {
      const id = this.pageList[(this.pageIndex - 1) % this.pageSize].id
      this.changeEditId(id)
      this.$router.push(`/edit/${id}`)
    },
    exchangeisOpenTW(){
      this.isOpenTW = !this.isOpenTW
      console.log(1111)
    },
    consoleOUT(){
      console.log(1111)
    },
    showXQ(data){
      let boxData = {
        title:'数据详情',
        data:data
      }
      this.ShowTanKuangBox(boxData)
    },
    onSure(){
      console.log(1111)
    },
    hideModal (data) {
      this.addPage = false
      if (data.ifAdd) {
        this.$router.push('/edit/' + data.addId)
      }
    },
    ShowTableBox(dataArry){
      this.showTableBox = true
      console.log(dataArry.data)
      // this.TableData = dataArry
    },
    closeTableTtn(){
      this.showTableBox = false
    },
    ShowTanKuangBox(dataArry){
      this.showModelBox = true
      this.showModelBoxtype = dataArry.type || 0
      let newData = []
      for (const key in dataArry.data) {
        if (Object.hasOwnProperty.call(dataArry.data, key)) {
          let data = {
            title: key,
            value: dataArry.data[key]
          }
          newData.push(data)
        }
      }
      dataArry.data = newData
      this.boxData = dataArry
    },
    closeBoxTtn () {
      this.showModelBox = false
    },
    hideImportModal () {
      this.showImport = false
      this.getPageData()
    },
    getPageData: function () {
      // 获取大屏配置内容
      this.axios.get('/leaderview/home/homePage').then(data => {
        if (data.success) {
          this.initPage(data.obj)
          this.autoFresh()
        } else {
          this.loadAll = true
        }
      })
    },
    cancleRequest: function () {
      this.xhrArr.forEach(xhr => {
        if (xhr.status && xhr.status !== 200) {
          xhr.abort() // 取消当前所有请求
        }
      })
      this.xhrArr = []
    },
    initPage: function (res) {
      this.pageSize = res.pages.length
      this.pageList = res.pages
      if (this.editId) {
        // 遍历list
        for (var i = 0; i < this.pageList.length; i++) {
          if (this.pageList[i].id === this.editId) {
            this.pageIndex = i
            break
          }
        }
      } else {
        this.pageIndex = 0
      }
      // this.pageIndex = 0
      this.isNewUser = res.isNewUser
      this.loadAll = true
      this.intervalTime = res.intervalTime || 5
      this.refreshTime = res.refreshTime || 3
      this.refreshType = res.specialEffects
      // this.refreshType = res.refreshType;
      if (this.pageSize) {
        // this.timeFn()
        this.loadFirstPage()
        this.$nextTick(() => {
          this.setScale()
        })
      }
    },
    editPage: function () {
      // 编辑主页
      this.stopTimer()
      this.stopRefreshTimer()
      this.pageList = []
      this.nowPage = []
      this.nowPage2 = []
      this.combinList2 = []
      this.combinList = []
      this.pageIndex = 0
      this.changeEditId(null)
      this.$router.push('/editPage')
    },
    fullScreen: function () {
      // 切换全屏
      if (this.pageList.length === 0) {
        Notification({
          message: '请配置可展示的大屏页面',
          position: 'bottom-right',
          customClass: 'toast toast-info'
        })
        return
      }
      var ct = this
      Public.checkFull() ? this.exitFull() : this.full()
      $('.tp-tip').addClass('hide')
      $('.tooltip.in').remove()
      // this.isFullScreen ? this.exitFull() : this.full()
      $(window).on('resize.home', function () {
        !Public.checkFull() && ct.exitFull()
      })
    },
    exitFull: function () {
      // 退出全屏
      $('#home-html').css('background', 'transparent')
      this.stopTimer()
      this.isFullScreen = false
      Public.exitFullScreen()
      $(window).off('resize.home')
    },
    full: function () {
      // 全屏
      this.showTip = true
      setTimeout(() => {
        this.showTip = false
      }, 3500)
      $('#home-html').css('background', $('body').css('background'))
      Public.bigScreenfullScreen($('#home-html').get(0))
      // Public.bigScreenfullScreen($('.home_wrapBox').get(0))
      this.isFullScreen = true
      this.interTimer()
    },
    prevMove: function () {
      // 上一页
      this.pageIndex--
      if (this.pageIndex === 0) {
        this.pageIndex = this.pageSize
      }
      if (this.refreshType === 'left') {
        this.moveBox1 = 'moveRight1'
        this.moveBox2 = 'moveRight2'
      } else if (this.refreshType === 'top') {
        this.moveBox1 = 'moveBottom1'
        this.moveBox2 = 'moveBottom2'
      }
      var nowPageObj = this.pageList[(this.pageIndex - 1) % this.pageSize]
      if (nowPageObj.composeObj) {
        this.combinList2 = JSON.parse(nowPageObj.composeObj)
      } else {
        this.combinList2 = []
      }
      if (nowPageObj.paintObj) {
        this.paintConf2 = JSON.parse(nowPageObj.paintObj)
      } else {
        this.paintConf2 = {}
      }
      this.setPaint()
      this.nowPage2 = JSON.parse(nowPageObj.viewConf)
      nowPageObj = null
      // 下面是move
      this.moveFlag = !this.moveFlag // false
      setTimeout(() => {
        this.nowPage = JSON.parse(JSON.stringify(this.nowPage2))
        this.combinList = JSON.parse(JSON.stringify(this.combinList2))
        this.paintConf = JSON.parse(JSON.stringify(this.paintConf2))
        this.setPaint()
        this.$nextTick(() => {
          this.moveFlag = !this.moveFlag // true
        })
        this.nowPage2 = []
        this.combinList2 = []
        this.paintConf2 = {}
      }, 1010)
      this.isFullScreen && this.interTimer()
    },
    prev: function () {
      // 上一页
      this.changeNowPage(-1)
      this.cancleRequest()
      // window.$.cache = {}
      if (this.refreshType !== '1') {
        this.prevMove()
        return
      }
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
      nowPageObj = null
      this.isFullScreen && this.interTimer()
    },
    setPaint: function () {
      if (this.paintConf) {
        if (this.paintConf.bgImg) {
          $('#mainbox').css(
            'background',
            'url(' + gbs.host + '/leaderview' + this.paintConf.bgImg + ')'
          )
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
      // add
      if (this.paintConf2) {
        if (this.paintConf2.bgImg) {
          $('#mainbox2').css(
            'background',
            'url(' + gbs.host + '/leaderview' + this.paintConf2.bgImg + ')'
          )
        } else {
          $('#mainbox2').css('background', '')
        }
        $('#mainbox2').css('opacity', this.paintConf2.opacity / 100)
        if (this.paintConf2.bgColor) {
          $('#paintWrap2').css('background', this.paintConf2.bgColor)
        } else {
          $('#paintWrap2').css('background', '')
        }
        if (this.paintConf2.bgStyle) {
          var type2 = this.paintConf2.bgStyle
          if (type2 === '1') {
            var backgroundSize2 = '100% auto'
          } else if (type === '2') {
            backgroundSize2 = 'auto 100%'
          } else {
            backgroundSize2 = '100% 100%'
          }
          $('#mainbox2').css('background-size', backgroundSize2)
        } else {
          $('#mainbox2').css('background-size', '')
        }
      } else {
        $('#mainbox2').css('background', '')
        $('#mainbox2').css('background-size', '')
      }
    },
    togglePlay: function () {
      // 开启/暂停
      this.timer ? this.stopTimer() : this.interTimer()
    },
    next: function () {
      // 下一页
      this.timeFn()
      this.isFullScreen && this.interTimer()
      this.changeNowPage(-1)
    },
    // 加载第一页大屏
    loadFirstPage: function () {
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
      this.nowPage = JSON.parse(nowPageObj.viewConf)
      if (this.refreshType !== '1') {
        var nowPageObj2 = this.pageList[this.pageIndex % this.pageSize]
        if (nowPageObj2.composeObj) {
          this.combinList2 = JSON.parse(nowPageObj2.composeObj)
        } else {
          this.combinList2 = []
        }
        if (nowPageObj2.paintObj) {
          this.paintConf2 = JSON.parse(nowPageObj2.paintObj)
        } else {
          this.paintConf2 = {}
        }
        this.nowPage2 = JSON.parse(nowPageObj2.viewConf)
      }
      this.setPaint()
      nowPageObj = null
      nowPageObj2 = null
    },
    /* 轮播切换相关 */
    timeFn: function () {
      // 轮播
      this.cancleRequest()
      // window.$.cache = {}
      if (this.refreshType !== '1') {
        this.timeFnMove()
        return
      }
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
      nowPageObj = null
      $('.tp-tip').remove()
      $('.tooltip.in').remove()
    },
    timeFnMove: function () {
      // 轮播
      if (!this.moveFlag) return // 正在轮播中
      if (this.refreshType === 'left') {
        this.moveBox1 = 'moveLeft1'
        this.moveBox2 = 'moveLeft2'
      } else if (this.refreshType === 'top') {
        this.moveBox1 = 'moveTop1'
        this.moveBox2 = 'moveTop2'
      } else if (this.refreshType === 'scale') {
        this.moveBox1 = 'moveScale1'
        this.moveBox2 = 'moveScale2'
      }
      var nowPageObj2 = this.pageList[this.pageIndex % this.pageSize]
      if (nowPageObj2.composeObj) {
        this.combinList2 = JSON.parse(nowPageObj2.composeObj)
      } else {
        this.combinList2 = []
      }
      if (nowPageObj2.paintObj) {
        this.paintConf2 = JSON.parse(nowPageObj2.paintObj)
      } else {
        this.paintConf2 = {}
      }
      this.setPaint()
      this.nowPage2 = JSON.parse(nowPageObj2.viewConf)
      nowPageObj2 = null
      $('.tp-tip').remove()
      $('.tooltip.in').remove()
      this.moveFlag = !this.moveFlag // false
      setTimeout(() => {
        this.nowPage = JSON.parse(JSON.stringify(this.nowPage2))
        this.combinList = JSON.parse(JSON.stringify(this.combinList2))
        this.paintConf = JSON.parse(JSON.stringify(this.paintConf2))
        this.setPaint()
        this.moveFlag = !this.moveFlag // true
        this.nowPage2 = []
        this.combinList2 = []
        this.paintConf2 = {}
      }, 1010)
      this.pageIndex++
    },
    stopTimer: function () {
      this.timer && clearTimeout(this.timer)
      this.timer = null
    },
    interTimer: function () {
      // 使用setTimeout模拟setInterval，初始化轮播定时器
      var ct = this
      this.stopTimer()
      if (this.pageSize <= 1) {
        return
      }
      this.timer = setTimeout(function test () {
        clearTimeout(ct.timer) // 这里清除一下定时器
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
        if (ct.xhrArr.length > 50) {
          ct.xhrArr.splice(0, 10) // 防止数组中保存的xhr对象过多而导致内存泄漏
        }
        ct.refreshTargetFn()
        ct.refreshCompose()
        clearTimeout(ct.freshInterval)
        ct.freshInterval = setTimeout(fresh, 1000)
      }, 1000)
    },

    /* 刷新页面相关 */
    refresh: function () {
      this.cancleRequest() // 取消还未结束的请求
      this.refreshFn()
      this.refreshCompose(true)
      this.autoFresh()
    },
    stopRefreshTimer: function () {
      this.freshInterval && clearTimeout(this.freshInterval)
      this.freshInterval = null // 每秒刷新——触发单个元件刷新
      this.refreshTimer && clearTimeout(this.refreshTimer)
      this.refreshTimer = null // 整页刷新
    },
    // 整页刷新，未调用
    initRefreshTimer: function () {
      var ct = this
      this.stopRefreshTimer()
      /* if(this.refreshType === 'Manual'){
          return;
      } */
      // 定时器
      this.refreshTimer = setTimeout(function freshFn () {
        clearTimeout(ct.refreshTimer)
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
    mapDataToChart (datas, oldData) {
      for (let k in datas) {
        for (let i in oldData) {
          if (oldData[i]['位置'] === k) {
            oldData[i]['告警'] = datas[k]
          }
        }
      }
      return oldData
    },
    refreshTargetFn: function (newV) {
      // 刷新本页数据
      newV = newV || this.nowPage
      var ct = this
      if (!newV) return
      $.each(newV, async function (i, d) {
        let freshTime = d.refreshTm ? d.refreshTm : 5 // 这里是刷新周期
        if (
          ct.nowTime % freshTime === 0 &&
          d.chartType === 'topo' &&
          d.tptype !== 'maptp'
        ) {
          ct.$set(d, 'time', new Date().getTime())
        } else if (
          d.ctDataSource !== 'static' &&
          d.url &&
          ct.nowTime % freshTime === 0
        ) {
          $.each(d.params, function (i, o) {
            d.params[i] = $.isArray(o) ? o.join(',') : o
          })
          // if(!ct.thirdUser && d.ctDataSource !== 'system') {  //第三方系统：检查是否需要登录
          //   let curUrl = d.url.split('://')[1].split('/')[0]
          //   if(!(await checkLogin(curUrl))) return false  //登录失败：跳出循环
          // }
          if (d.moreUrlArry && d.moreUrlArry.length > 1) {
          } else {
            ct.sentReq(d)
          }
          /*
          let xhrobj = $.ajax({
            url: gbs.host + d.url,
            data: d.params,
            type: d.method || 'get',
            cache: false,
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
                if (d.chartType === 'v-map') {
                  d.chartData.rows = ct.mapDataToChart(res.obj, d.chartData.rows)
                } else {
                  d.chartData = res.obj // 会触发刷新
                }
              }
            },
            error: function (xhr) {
              if (xhr.status === 776) {
                // 776 取消页面刷新
                ct.freshInterval && clearTimeout(ct.freshInterval)
                ct.freshInterval = null
              }
              if (xhr.status != 776 && xhr.statusText !== 'abort') {
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
            },
            complete: function (XHR, textStatus) {
              XHR = null
            }
          })
          ct.xhrArr.push(xhrobj)
          */
        } else if (
          d.ctDataSource === 'static' &&
          ct.animationType.indexOf(d.chartType) !== -1
        ) {
          let freshTime = d.refreshTm ? d.refreshTm : 5 // 这里是刷新周期
          if (ct.nowTime % freshTime === 0) {
            ct.$set(
              d,
              'id',
              new Date().getTime() + parseInt(Math.random() * 10000)
            )
          }
        }
      })
      // this.$nextTick(() => {
      //   ct.setScale()
      // })
    },
    // 发送请求
    sentReq (d) {
      let ct = this
      let xhrobj = ''
      if (d.url === '/monitor/topo/domainTopo') {
        // $('#lead-screen').addClass('disShow')
        xhrobj = $.ajax({
          url:
            (d.ctDataSource === 'system' ? gbs.host + d.url : d.url) +
            '/' +
            d.params.topoId, // 第三方的ur已经拼接好host
          // data: d.params,
          type: d.method || 'get',
          cache: false,
          ascyn: false,
          success: function (res) {
            // $('#lead-screen').removeClass('disShow')
            res.obj = res.obj || []
            if (res.obj.colors) {
              d.ctColors = res.obj.colors
            }
            if (d.chartType === 'marquee' || d.chartType === 'text') {
              d.ctName = res.obj.info
            }
            if (d.chartType !== 'marquee') {
              if (d.chartType === 'v-map') {
                d.chartData.rows = ct.mapDataToChart(res.obj, d.chartData.rows)
              } else {
                d.chartData = res.obj // 会触发刷新
              }
            }
          },
          error: async function (xhr) {
            if (xhr.status === 776) {
              if (d.ctDataSource !== 'static' && d.ctDataSource !== 'system') {
                // 第三方登录过期:重新登录后再请求
                ct.xhrArr.pop()
                let curUrl = d.url.split('://')[1].split('/')[0]
                ;(await checkLogin(curUrl)) && ct.sentReq(d)
                return false
              }
              // 776 取消页面刷新
              ct.freshInterval && clearTimeout(ct.freshInterval)
              ct.freshInterval = null
            }
            if (xhr.status !== 776 && xhr.statusText !== 'abort') {
              Notification({
                message: '连接错误！',
                position: 'bottom-right',
                customClass: 'toast toast-error'
              })
            }
          },
          complete: function (XHR, textStatus) {
            XHR = null
          }
        })
      } else {
        if (
          d.params.neIds &&
          d.params.neIds !== null &&
          d.params.windows &&
          JSON.parse(d.params.windows)[0].fields === null
        ) {
          this.axios
            .get(
              `/leaderview/monitor/params/valid/singleFieldInd?indicatorId=${
                JSON.parse(d.params.windows)[0].indicator
              }`
            )
            .then(res => {
              if (res.success) {
                if (res.obj) {
                  let data = JSON.parse(d.params.windows)[0]
                  data.fields = [data.indicator]
                  d.params.windows = JSON.stringify([data])
                }
                // $('#lead-screen').addClass('disShow')
                xhrobj = $.ajax({
                  url: d.ctDataSource === 'system' ? gbs.host + d.url : d.url, // 第三方的ur已经拼接好host
                  data: d.params,
                  type: d.method || 'get',
                  cache: false,
                  ascyn: false,
                  success: function (res) {
                    // $('#lead-screen').removeClass('disShow')
                    res.obj = res.obj || []
                    if (res.obj.colors) {
                      d.ctColors = res.obj.colors
                    }
                    if (d.chartType === 'marquee' || d.chartType === 'text') {
                      d.ctName = res.obj.info
                    }
                    if (d.chartType !== 'marquee') {
                      if (d.chartType === 'v-map') {
                        d.chartData.rows = ct.mapDataToChart(
                          res.obj,
                          d.chartData.rows
                        )
                      } else {
                        d.chartData = res.obj // 会触发刷新
                      }
                    }
                  },
                  error: async function (xhr) {
                    // $('#lead-screen').removeClass('disShow')
                    if (xhr.status === 776) {
                      if (
                        d.ctDataSource !== 'static' &&
                        d.ctDataSource !== 'system'
                      ) {
                        // 第三方登录过期:重新登录后再请求
                        ct.xhrArr.pop()
                        let curUrl = d.url.split('://')[1].split('/')[0]
                        ;(await checkLogin(curUrl)) && ct.sentReq(d)
                        return false
                      }
                      // 776 取消页面刷新
                      ct.freshInterval && clearTimeout(ct.freshInterval)
                      ct.freshInterval = null
                    }
                    if (xhr.status !== 776 && xhr.statusText !== 'abort') {
                      Notification({
                        message: '连接错误！',
                        position: 'bottom-right',
                        customClass: 'toast toast-error'
                      })
                    }
                  },
                  complete: function (XHR, textStatus) {
                    XHR = null
                  }
                })
              }
            })
        } else {
          // $('#lead-screen').addClass('disShow')
          xhrobj = $.ajax({
            url: d.ctDataSource === 'system' ? gbs.host + d.url : d.url, // 第三方的ur已经拼接好host
            data: d.params,
            type: d.method || 'get',
            cache: false,
            ascyn: false,
            success: function (res) {
              // $('#lead-screen').removeClass('disShow')
              res.obj = res.obj || []
              if (res.obj.colors) {
                d.ctColors = res.obj.colors
              }
              if (d.chartType === 'marquee' || d.chartType === 'text') {
                d.ctName = res.obj.info
              }
              if (d.chartType !== 'marquee') {
                if (d.chartType === 'v-map') {
                  d.chartData.rows = ct.mapDataToChart(
                    res.obj,
                    d.chartData.rows
                  )
                } else {
                  d.chartData = res.obj // 会触发刷新
                }
              }
            },
            error: async function (xhr) {
              // $('#lead-screen').removeClass('disShow')
              if (xhr.status === 776) {
                if (
                  d.ctDataSource !== 'static' &&
                  d.ctDataSource !== 'system'
                ) {
                  // 第三方登录过期:重新登录后再请求
                  ct.xhrArr.pop()
                  let curUrl = d.url.split('://')[1].split('/')[0]
                  ;(await checkLogin(curUrl)) && ct.sentReq(d)
                  return false
                }
                // 776 取消页面刷新
                ct.freshInterval && clearTimeout(ct.freshInterval)
                ct.freshInterval = null
              }
              if (xhr.status !== 776 && xhr.statusText !== 'abort') {
                Notification({
                  message: '连接错误！',
                  position: 'bottom-right',
                  customClass: 'toast toast-error'
                })
              }
            },
            complete: function (XHR, textStatus) {
              XHR = null
            }
          })
        }
      }
      ct.xhrArr.push(xhrobj)
    },
    refreshFn: function (newV) {
      // 刷新本页数据
      newV = newV || this.nowPage
      var ct = this
      $.each(newV, function (i, d) {
        if (d.chartType === 'topo' && d.tptype !== 'maptp') {
          ct.$set(d, 'time', new Date().getTime())
          /* var tpid = d.tpId
          d.tpId = '';
          ct.$nextTick(function(){
              d.tpId = tpid;
          }) */
        } else if (d.ctDataSource !== 'static' && d.url) {
          $.each(d.params, function (i, o) {
            d.params[i] = $.isArray(o) ? o.join(',') : o
          })
          ct.sentReq(d)
          /*
          let xhrObj = $.ajax({
            url: gbs.host + d.url,
            data: d.params,
            type: d.method || 'get',
            cache: false,
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
                if (d.chartType === 'v-map') {
                  d.chartData.rows = ct.mapDataToChart(res.obj, d.chartData.rows)
                } else {
                  d.chartData = res.obj
                }
              }
            },
            error: function (xhr) {
              if (xhr.status === 776) {
                // 776 取消页面刷新
                ct.freshInterval && clearTimeout(ct.freshInterval)
                ct.freshInterval = null
              }
              if (xhr.status != 776 && xhr.statusText !== 'abort') {
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
            },
            complete: function (XHR, textStatus) {
              XHR = null
            }
          })
          ct.xhrArr.push(xhrObj)
          */
        }
        if (
          d.ctDataSource === 'static' &&
          ct.animationType.indexOf(d.chartType) !== -1
        ) {
          ct.$set(
            d,
            'id',
            new Date().getTime() + parseInt(Math.random() * 10000)
          )
        }
      })
      this.$nextTick(() => {
        ct.setScale()
      })
    },
    refreshCompose: function (flag) {
      if (this.combinList && this.combinList.length > 0) {
        this.combinList.forEach(list => {
          if (flag) {
            this.refreshFn(list.child)
          } else {
            this.refreshTargetFn(list.child)
          }
        })
      }
    },

    /* 缩放setScale */
    setScale: function () {
      // var $el = document.getElementById('home-html')
      let $el = $('#home-html')
      let w = $el.width()
      // var h = $el.height()
      let pageContainer = $('#page_container')
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
      let paintW = (this.paintConf && this.paintConf.width) || 1920
      let paintH = (this.paintConf && this.paintConf.height) || 1080
      let scaleX = w / paintW // 这里需要改成设置的画布的大小
      let scaleY = h / paintH
      let scale = Math.min(scaleX, scaleY)
      let mrg = 0
      // if (scaleX <= 1) {
      // var _width = this.paintConf.width || baseData.home.w
      // mrg = [0, (w - _width * scale) / 2 + 'px'].join(' ')
      mrg = [0, Math.abs(w - paintW * scale) / 2 + 'px'].join(' ')
      // }
      if (this.isFullScreen) {
        let boxMrg = [0, Math.abs(w - paintW * scale) / 2 + 'px'].join(' ')
        $el.find('.pagebox').css({
          transform: 'scale(' + scale + ',' + scale + ')',
          // width: paintW + 'px',
          // height: paintH + 'px',
          overflow: 'visible',
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
    clearPage: function () {
      // 离开主页清定时器
      this.stopTimer()
      this.stopRefreshTimer()
      this.$destroy()
    },
    getAccess () {
      let permission = 'r'
      // 先获取simo存在本地的
      this.axios.get('/leaderview/home/validSuperAdmin').then(res => {
        if (res.success) {
          this.isSuperAdmin = res.obj.isSuperAdmin
        }
      })
      if (baseData && baseData.menuParam && baseData.menuParam.permission) {
        permission = baseData.menuParam.permission.toLowerCase().split(',')
        if (permission.indexOf('w') !== -1) {
          this.access = 'w'
        } else {
          this.access = 'r'
        }
        sessionStorage.setItem('leaderAccess', this.access)
      } else {
        this.axios.get('/mc/getMenu').then(res => {
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
    },
    browerKernel () {
      let result
      ;['webkit', 'moz', 'o', 'ms'].forEach(function (prefix) {
        if (typeof document[prefix + 'Hidden'] !== 'undefined') {
          result = prefix
        }
      })
      return result
    },
    onVisibilityChange (e) {
      let prefix = this.browerKernel()
      if (document[prefix + 'VisibilityState'] === 'hidden') {
        this.changePageVisiable(false)
      } else if (document[prefix + 'VisibilityState'] === 'visible') {
        this.changePageVisiable(true)
      }
    },
    pageVisibInit () {
      let prefix = this.browerKernel()
      document.addEventListener(
        prefix + 'visibilitychange',
        this.onVisibilityChange
      )
    },
    getPageConf (id) {
      return this.axios.get(`/leaderview/home/homePage/getById/${id}`)
    },
    // 跳转大屏之后，修改pageIndex
    updatePageIndex (id) {
      for (let i = 0; i < this.pageSize; i++) {
        if (this.pageList[i].id === id) {
          this.pageIndex = i + 1
          break
        }
      }
      if (this.pageIndex === 0) {
        this.pageIndex = this.pageSize
      }
    }
  },
  watch: {
    nowPageId: function (newV, oldV) {
      if (newV !== -1) {
        this.getPageConf(newV).then(res => {
          var nowPageObj = res.obj
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
          if (nowPageObj.viewConf) {
            this.nowPage = JSON.parse(nowPageObj.viewConf)
          } else {
            this.nowPage = []
          }
          nowPageObj = null
          this.updatePageIndex(newV)
        })
        $('.tp-tip').remove()
        $('.tooltip.in').remove()
      }
    },
    pageVisiable: function (newV) {
      if (newV) {
        this.autoFresh()
        if (Public.checkFull()) {
          this.interTimer() // 全屏自动轮播
        }
      } else {
        this.stopRefreshTimer()
        this.stopTimer()
        this.cancleRequest()
      }
    },
    nowPage: function (newV) {
      // $(this.$el).find('.pagebox').css({
      //   transform: 'scale(1)'
      // })
      if ($('#paintWrap').find('[title]').length > 0) {
        // $('#paintWrap').find('[title]').tooltip('destroy') // 取消本页的表格tips
      }
      this.stopRefreshTimer()
      if (!newV) {
        return []
      }
      // this.refreshFn(newV)
      this.setScale()
      this.refresh() // 整页刷新
      // this.initRefreshTimer() 取消整页刷新
    }
    // combinList: function () {
    //   // this.refreshCompose()
    // }
  },
  beforeMount: function () {
    this.axios
      .get('/alert/currencyAlertmanager/findAlertLevelList')
      .then(res => {
        this.changeAlertInfo(res.obj)
      })
  },
  mounted: function () {
    $('#screen').addClass('disShow')
    // var _url = window.location.protocol + '//' + window.location.host + '/index'
    // window.history.pushState({}, '', _url)
    this.getAccess()
    this.$nextTick(() => {
      this.getPageData()
      $(window)
        .off('resize.homescale')
        .on('resize.homescale', () => {
          this.$nextTick(() => {
            this.setScale()
          })
        })
    })
    let theme = $('html').attr('data-theme')
    if (theme !== 'default') {
      this.defTheme = false
    }
    let videoTims = this.videoTims
    for (let i in videoTims) {
      videoTims[i] = 0
    }
    this.pageVisibInit()
    this.initVideoTims(videoTims) // 进入大屏展示页时都初始化一次视频播放的时间
    titleShowFn('top', $('#homeTips'), '#homeTips')
    // // $('#lead-screen').addClass('disShow')
  },
  updated () {
    if (
      this.pageList[(this.pageIndex - 1) % this.pageSize] &&
      this.pageList[(this.pageIndex - 1) % this.pageSize].belongCurrentUser !==
        'true'
    ) {
      this.$refs.editbutton.style.display = 'none'
    }
  },
  beforeDestroy: function () {
    // $('#lead-screen').removeClass('disShow')
    this.stopRefreshTimer()
    this.xhrArr.forEach(xhr => {
      if (xhr.status && xhr.status !== 200) {
        xhr.abort() // 取消当前所有请求
      }
    })
    this.xhrArr = null
    let prefix = this.browerKernel()
    document.removeEventListener(
      prefix + 'visibilitychange',
      this.onVisibilityChange
    )
  },
  destroyed: function () {
    if ($('#paintWrap').find('[title]').length > 0) {
      $('#paintWrap')
        .find('[title]')
        .tooltip('destroy')
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

#home-html .ringparent {
  display: flex;
}
#home-html .ring-icon {
  width: 30px;
  height: 30px;
  background: #15192a;
  // background: rgba(21, 25, 42, 0.3);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
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

#home-html .ring-icon [class*='icon-n-'] {
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
#home-html #mainbox,
#home-html #mainbox2 {
  width: 100%;
  height: 100% !important;
  position: absolute;
  top: 0px;
  height: 0px;
}

html[data-theme='blackWhite'],
html[data-theme='blueWhite'] {
  textarea {
    background: transparent !important;
    background-color: transparent !important;
  }
  #home-html .ring-icon {
    background: #edf3fe;
    opacity: 1;
  }
  #home-html .ring-icon [class*='icon-n-'] {
    color: #0089ff;
  }
}
// add 轮播相关
.portlet {
  position: absolute !important;
  top: 0px;
  left: 0px;
  width: 100%;
  height: 100%;
}
// 向左移动
.moveLeft1-leave-active {
  animation: box-left-leave 1s;
}
.moveLeft2-enter-active {
  animation: box-left-in 1s;
}
@keyframes box-left-leave {
  from {
    transform: translateX(0);
  }
  to {
    transform: translateX(-100%);
  }
}
@keyframes box-left-in {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}
// 向右移动
.moveRight1-leave-active {
  animation: box-right-leave 1s;
}
.moveRight2-enter-active {
  animation: box-right-in 1s;
}
@keyframes box-right-leave {
  from {
    transform: translateX(0);
  }
  to {
    transform: translateX(100%);
  }
}
@keyframes box-right-in {
  from {
    transform: translateX(-100%);
  }
  to {
    transform: translateX(0);
  }
}
// 向上移动
.moveTop1-leave-active {
  animation: box-top-leave 1s;
}
.moveTop2-enter-active {
  animation: box-top-in 1s;
}
@keyframes box-top-leave {
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(-100%);
  }
}
@keyframes box-top-in {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
}
// 向下移动
.moveBottom1-leave-active {
  animation: box-bottom-leave 1s;
}
.moveBottom2-enter-active {
  animation: box-bottom-in 1s;
}
@keyframes box-bottom-leave {
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(100%);
  }
}
@keyframes box-bottom-in {
  from {
    transform: translateY(-100%);
  }
  to {
    transform: translateY(0);
  }
}
// 缩放效果
.moveScale1-leave-active {
  animation: box-scale-leave 1s;
}
.moveScale2-enter-active {
  animation: box-scale-in 1s;
}
@keyframes box-scale-leave {
  from {
    transform: scale(1);
  }
  to {
    transform: scale(0);
  }
}
@keyframes box-scale-in {
  from {
    transform: scale(0);
  }
  to {
    transform: scale(1);
  }
}
.BoxMban{
  position: absolute;
  top: 0;
  left: 0;
  width: 3840px;
  height: 2160px;
  z-index: 5000;
  background-color: #15192a65;
}
.Tbaleban{
  position: absolute;
  top: 0;
  left: 0;
  width: 3840px;
  height: 2160px;
  z-index: 5000;
  background-color: #15192a65;
  .TableHead {
    width: 100%;
    tr {
      width: 100%;
      height: 60px;
      font-size: 30px !important;
      display: flex;
      color: #94cffa;
      th {
        height: 100%;
        text-align: center;
      }
    }
  }
  .TableBody {
    width: 100%;
    tr {
      width: 100%;
      height: 90px;
      margin: 10px 0;
      font-size: 30px !important;
      display: flex;
      color: #5983b6;
      th {
        height: 100%;
        text-align: center;
        overflow: hidden;
      }
    }
  }
}
.ParentBox{
  position: relative;
}
.BoxArry{
  .SmallBox{
    height: 1016px;
    width: 45px;
    position: fixed;
    top: 600px;
    left: 3790px;
    // left: 0px;
    position: absolute;
    z-index: 10000;
    background: url(./boxClose-r.png);
    background-size: 100%  100%;
  }
  .BigBox{
    height: 1016px;
    width: 253px;
    position: fixed;
    top: 600px;
    left: 3580px;
//    left: 0px;
    background-color: rgb(12, 236, 206);
    position: absolute;
    z-index: 10000;
    background: url(./boxTan-r.png);
    background-size: 100%  100%;
    .CloseBox{
      height: 220px;
      width: 50px;
      // right: 0px;
      cursor: pointer;
      position: absolute;
      top: 400px;
      z-index: 10000;
    }
    .AhrefBox{
      height: 260px;
      padding: 30px;
      position: relative;
      width: 260px;
      cursor: pointer;
      .openBox{
        top: 50px;
        left: 80px;
        position: absolute;
        height: 110px;
        width: 110px;
        background: url(./open.png);
        background-size: 100% 100%;
      }
      .closeStyle{
        top: 50px;
        left: 80px;
        position: absolute;
        height: 110px;
        width: 110px;
        background: url(./close.png);
        background-size: 100% 100%;
      }
      a{
        font-size: 34px;
        display: block;
        top: 176px;
        color: #CCE7FF;
        width: 200px;
        position: absolute;
        text-align: center;
      }
    }
    .AhrefBox:hover a{
      color: #15ABFF;
    }
    .ChildrenBox{
      height: 365px;
      width: 260px;
      left: -260px;
      // left: 260px;
      top: 750px;
      background: url(./btBack.png);
      background-size: 100%  100%;
      position: absolute;
      a{
        display: block;
        width: 100%;
        height: 25%;
        text-align: center;
        font-size: 34px;
        line-height: 100px;
        color: #CCE7FF;
      }
      a:hover{
        color: #15ABFF;
      }
    }
  }
}
.TableBox {
  height: 886px;
  width: 1747px;
  padding: 100px;
  top: 600px;
  left: 1050px;
  position: relative;
  z-index: 5000;
  background: url(./modelBox.png);
  .closeBtn{
    height: 100px;
    width: 100px;
    cursor: pointer;
    position: absolute;
    top: 20px;
    right: 20px;
  }
  .BoxTitle {
    font-size: 46px !important;
    color: #bbeefe;
    font-family: PangmenMainRoadTitleBody !important;
  }
  .BoxBody {
    padding: 80px 40px;
    display: flex;
    font-size: 30px !important;
    flex-wrap: wrap;
    width: 100%;
    height: 90%;
    overflow: auto;
  }
  .lineBox {
    display: flex;
    width: 50%;
    padding: 30px 0px;
  }
  .Nmae {
    padding: 0px 10px;
    width: 30%;
    color: #415468;
  }
  .Data {
    width: 70%;
    color: #789fb0;
  }
}
.ModelBox {
  height: 886px;
  width: 1747px;
  padding: 100px;
  top: 600px;
  left: 1050px;
  position: relative;
  z-index: 5000;
  background: url(./modelBox.png);
  .closeBtn{
    height: 100px;
    width: 100px;
    cursor: pointer;
    position: absolute;
    top: 20px;
    right: 20px;
  }
  .BoxTitle {
    font-size: 46px !important;
    color: #bbeefe;
    font-family: PangmenMainRoadTitleBody !important;
  }
  .BoxBody {
    padding: 80px 40px;
    display: flex;
    font-size: 30px !important;
    flex-wrap: wrap;
    width: 100%;
    height: 90%;
    overflow: auto;
  }
  .lineBox {
    display: flex;
    width: 50%;
    padding: 30px 0px;
  }
  .Nmae {
    padding: 0px 10px;
    width: 30%;
    color: #415468;
  }
  .Data {
    width: 70%;
    color: #789fb0;
  }
}
.DataValue{
  height: 600px;
  overflow: auto;
  width: 100%;
  font-size: 28px;
  text-indent:2em
}
</style>
