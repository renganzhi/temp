<template>
    <div class="cityEvent" :style="boxStyle">
      <div class="checkBox" :style="checkStyle" v-if="item.chartData.dataArray && item.chartData.dataArray.length > 1">
            <div class="normalBtn" v-for="(v, i) in item.chartData.dataArray" :style="buttonStyle(i)" :key="i" @click="changeData(i)">
              {{v.title}}
            </div>
       </div>
       <!-- <vue-seamless-scroll
          :data="eventData.rows"
          :class-option="classOption"
          class="warp"
          @click.native="ClickBox($event)"
        >
          <ul class="item">
            <li :style="liStyle" v-for="(val, ind) in eventData.rows" :data-obj="JSON.stringify(val)" :key="ind">
              <div class="eventBox" :data-obj="JSON.stringify(val)">
                  <div class="title" :data-obj="JSON.stringify(val)" :style="{backgroundImage: 'linear-gradient(' + item.titleColor[0] + ',' + item.titleColor[1] + ')',fontSize:item.titleSize + 'px'}">标题：{{val.title || val['问题标题']}}</div>
                  <div class="date" :data-obj="JSON.stringify(val)" :style="{color:item.dateColor,fontSize:item.dateSize + 'px'}">时间：{{val.date || val['发起时间'] || val['上报时间']}}</div>
                  <div class="content" :data-obj="JSON.stringify(val)" :style="{color:item.contentColor,fontSize:item.contentSize + 'px'}">内容：{{val.content || val['发生地址'] || val['问题描述']}}</div>
              </div>
            </li>
          </ul>
        </vue-seamless-scroll> -->
        <div class="outBox" ref="outBox">
          <div class="boxList" ref="innerBox" @mouseenter="mouseenterEvent()" @mouseleave="mouseleaveEvent()">
            <div class="li" @click="showDetails(val)" :style="liStyle2" v-for="(val, ind) in eventData.rows" :key="ind">
              <div class="eventBox">
                <div class="title" :style="{backgroundImage: 'linear-gradient(' + item.titleColor[0] + ',' + item.titleColor[1] + ')',fontSize:item.titleSize + 'px'}">标题：{{val.title || val['问题标题']}}</div>
                <div class="date"  :style="{color:item.dateColor,fontSize:item.dateSize + 'px'}">时间：{{val.date || val['发起时间'] || val['上报时间']}}</div>
                <div class="content" :style="{color:item.contentColor,fontSize:item.contentSize + 'px'}">内容：{{val.content || val['发生地址'] || val['问题描述']}}</div>
              </div>
            </div>
          </div>
        </div>
    </div>
</template>
<script>
import { gbs } from '@/config/settings'
import vueSeamlessScroll from 'vue-seamless-scroll'
export default {
  props: ['item'],
  components: {
    vueSeamlessScroll
  },
  data: function () {
    return {
      pageIndex: 0,
      pageSize: 3,
      timer: null, // 时间器
      currentIndex: 0,
      eventData: {
        rows: [],
        columns: []
      }
    }
  },
  computed: {
    boxStyle: function () {
      return {
        width: this.item.width + 'px !important',
        height: this.item.height + 'px !important'
      }
    },
    classOption: function () {
      return {
        singleHeight: (this.item.height - 20) / 3 + 10,
        waitTime: this.item.loopSpeed
      }
    },
    liStyle: function () {
      return {
        height: (this.item.height - 20) / 3 + 'px'
      }
    },
    liStyle2: function () {
      return {
        height: this.item.pageHeight + 'px',
        marginBottom: this.item.pageMarginBottom + 'px'
      }
    },
    buttonStyle: function () {
      return (index) => {
        if (this.currentIndex === index) {
          return {
            backgroundImage: this.item.checkedButton
              ? 'url(' + gbs.host + this.item.checkedButton + ')'
              : 'url(' + require('./checked.png') + ')',
            padding: this.item.buttonPadding + 'px !important',
            margin: this.item.buttonMargin + 'px'
          }
        } else {
          return {
            backgroundImage: this.item.normalButton
              ? 'url(' + gbs.host + this.item.normalButton + ')'
              : 'url(' + require('./normal.png') + ')',
            padding: this.item.buttonPadding + 'px !important',
            margin: this.item.buttonMargin + 'px'
          }
        }
      }
    },
    checkStyle: function () {
      return {
        left: this.item.paddingLeft + 'px',
        top: this.item.paddingTop + 'px',
        fontSize: this.item.boxFontSize + 'px',
        flexDirection: this.item.boxDirection ? 'column' : 'row'
      }
    },
    pageLength: function () {
      return Math.ceil(Number(this.eventData.rows.length) / Number(this.pageSize))
    },
    dataList: function () {
      return (value) => {
        return this.eventData.rows.slice((value - 1) * this.pageSize, value * this.pageSize)
      }
    }
  },
  watch: {
    'item.chartData': {
      handler (newV, oldV) {
        if (this.item.chartData.dataArray && this.item.chartData.dataArray.length) {
          if (JSON.stringify(newV) !== JSON.stringify(oldV)) {
            this.currentIndex = 0
            this.eventData = this.item.chartData.dataArray[0]
          }
        }
      },
      deep: true
    },
    'currentIndex': function (newV, oldV) {
      if (newV !== oldV) {
        this.eventData = this.item.chartData.dataArray[this.currentIndex]
      }
    },
    'eventData': {
      handler: function () {
        if (this.$refs.outBox) {
          this.$refs.outBox.scrollTop = 0
          this.initTimerInterval()
        }
      },
      deep: true
    }
  },
  methods: {
    ClickBox ($event) {
      console.log('event', $event, $event.target.dataset.obj)
      let data = JSON.parse($event.target.dataset.obj)
      this.showDetails(data)
    },
    showDetails (data) {
      let d = {}
      for (let i in data) {
        if (i === 'title') {
          d['标题'] = data[i]
        } else if (i === 'content') {
          d['内容'] = data[i]
        } else if (i === 'date') {
          d['时间'] = data[i]
        } else {
          d[i] = data[i]
        }
      }
      if (this.$route.name === 'HomePage' || this.$route.name === 'lookPage' || this.$route.name === 'popPage') {
        this.$parent.$parent.ShowVenationBox({
          title: this.eventData.title,
          data: d,
          url: this.eventData.url
        })
      }
    },
    changeData (index) {
      this.currentIndex = index
    },
    initTimerInterval () {
      if (this.$route.name === 'HomePage' || this.$route.name === 'lookPage' || this.$route.name === 'popPage') {
        this.clearEvent()
        this.timer = setInterval(() => {
          window.requestAnimationFrame(this.scroll)
        }, this.item.dvtime || 20)
      }
    },
    scroll: function () {
      const that = this
      const DOM = this.$refs.outBox
      // 如果滚动到头则重新滚动
      if (DOM) {
        if (DOM.scrollHeight - DOM.scrollTop - 1 <= DOM.clientHeight) {
          DOM.scrollTop = 0
          setTimeout(() => {
            window.requestAnimationFrame(that.scroll)
          }, that.item.dvtime || 20)
          return
        }
        DOM.scrollTop++
      }
    },
    clearEvent () {
      if (this.timer) {
        clearInterval(this.timer)
        this.timer = null
      }
    },
    // 鼠标移入关闭定时器
    mouseenterEvent () {
      this.clearEvent()
    },
    // 鼠标移出重新调用定时器
    mouseleaveEvent () {
      this.initTimerInterval()
    }
  },
  mounted () {
    if (this.item.chartData.dataArray && this.item.chartData.dataArray.length) {
      this.eventData = this.item.chartData.dataArray[this.currentIndex]
      this.initTimerInterval()
    }
  },
  beforeDestroy () {
    this.clearEvent()
  }
}
</script>
<style lang="scss" scoped>
.cityEvent{
    display: flex;
    // justify-content: space-between;
    align-items: center;
    flex-direction: column;
    .warp{
      overflow: hidden;
      width: 100%;
      height: 100%;
      ul{
        list-style: none;
        padding: 0;
        margin: 0 auto;
        li{
          margin-bottom: 10px;
          background: #122f61;
          padding: 10px;
        }
      }
    }
    .checkBox{
      display: flex;
      position: absolute;
      .normalBtn{
        padding: 20px;
        background: url(./normal.png) no-repeat;
        background-size: 100% 100% !important;
        margin-right: 20px;
      }
      .normalBtn:last-child{
        margin-right: 0px !important;
      }
    }
    .outBox{
      width: 100%;
      height: 100%;
      overflow: hidden;
      &:hover {
        overflow-y: scroll;
      }
    }
    .boxList{
      width: calc(100% - 2px);
      height: 100%;
      overflow: visible;
      // transition:all 0.5s linear;
      .li{
        margin-bottom: 10px;
        background: #122f61;
        padding: 10px;
      }
    }
    .eventBox{
        width: 100%;
        height: 100%;
        padding: 10px;
        overflow: hidden;
        // margin-bottom: 10px;
        background: #122f61;
        font-weight: bold;
        overflow-y: scroll;
        .title{
            // 渐变色字体
            background-image: linear-gradient(#f04d4d, rgba(211, 179, 179, 0.67));
            background-clip: text;
            -webkit-background-clip: text;
            color: transparent;
            font-size: 34px;
        }
        .date{
            font-size: 22px;
            color: #3667ff;
        }
        .content{
            // height: 80%;
            // overflow: scroll;
            // text-overflow: ellipsis;
            font-size: 27px;
            color: #6689f8;
        }
    }
}
</style>
