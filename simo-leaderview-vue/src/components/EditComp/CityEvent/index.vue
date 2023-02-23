<template>
    <div class="cityEvent" :style="boxStyle">
      <div class="checkBox" :style="checkStyle" v-if="item.chartData.dataArray && item.chartData.dataArray.length > 1">
            <div class="normalBtn" v-for="(v, i) in item.chartData.dataArray" :style="buttonStyle(i)" :key="i" @click="changeData(i)">
              {{v.title}}
            </div>
       </div>
       <vue-seamless-scroll
          :data="eventData.rows"
          :class-option="classOption"
          class="warp"
        >
          <ul class="item">
            <li :style="liStyle" v-for="(val, ind) in eventData.rows" @click="showDetails(val)" :key="ind">
              <div class="eventBox" >
                  <div class="title" :style="{backgroundImage: 'linear-gradient(' + item.titleColor[0] + ',' + item.titleColor[1] + ')',fontSize:item.titleSize + 'px'}">标题：{{val.title}}</div>
                  <div class="date" :style="{color:item.dateColor,fontSize:item.dateSize + 'px'}">时间：{{val.date}}</div>
                  <div class="content" :style="{color:item.contentColor,fontSize:item.contentSize + 'px'}">内容：{{val.content}}</div>
              </div>
            </li>
          </ul>
        </vue-seamless-scroll>
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
    }
  },
  methods: {
    showDetails (data) {
      console.log('data', data)
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
          title: '事件详情',
          data: d,
          url: this.eventData.url
        })
      }
    },
    changeData (index) {
      this.currentIndex = index
    }
  },
  mounted () {
    if (this.item.chartData.dataArray && this.item.chartData.dataArray.length) {
      this.eventData = this.item.chartData.dataArray[this.currentIndex]
    }
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
