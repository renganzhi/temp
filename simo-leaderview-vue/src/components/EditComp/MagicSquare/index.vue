<template>
    <div class="MagicSquare">
            <div class="title1">
                <span>日期选择</span>
                <div class="datepicker">
                    <div style="overflow: hidden;">
                        <transition name="moveLeft">
                            <DatePicker v-show="showDateCheckbox" v-model="dateRange" type="daterange" split-panels placeholder="选择日期范围"></DatePicker>
                        </transition>
                    </div>
                    <img @click="showDateCheckbox = !showDateCheckbox" style="margin-left:25px;" src="./切图/日历.png" alt="">
                </div>
            </div>
            <div class="timeAxis" ref="timeAxis" @mousewheel="onMouseWheel($event, 'timeAxis')">
                <div :class="{timeBox: true, checkedBox:checkDate === item?true:false}" v-for="(item, index) in dateList" :key="index" @click="ChangeDate(item)">
                    <img class="warning" src="./切图/提前告警.png" alt="">
                    <div class="yAndm">
                        {{item.split('-')[0]}}.{{item.split('-')[1]}}
                    </div>
                    <div class="day"><span>{{item.split('-')[2]}}</span>日</div>
                </div>
            </div>
            <div class="title2">预警信息</div>
            <div class="warningBox">
                <div v-show="!showBoxDetail" class="boxList">
                    <div class="smallBox" @click="ShowDetail(item)" v-for="(item, index) in 10" :key="index">
                        <img class="todayHappen" src="./切图/本日发生.png" alt="">
                        <div class="title">
                            <img src="./切图/图标.png" alt="">
                            <span>预警类型{{item}}</span>
                        </div>
                        <div class="content">
                            <div class="rows" v-for="(val, ind) in 10" :key="val +',' + ind">
                                {{ind === 0? '潜在风险行为':'风险' + val}} : xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                            </div>
                        </div>
                    </div>
                </div>
                <div v-show="showBoxDetail" class="boxDetail">
                    <div class="return" @click="CloseDtail"><Icon type="ios-return-left" />返回</div>
                    <div class="leftPart">
                        <div class="showButton" @click="showRightPart = !showRightPart">{{showRightPart?'收起<<':'展开>>'}}</div>
                        <div class="title">
                            <img src="./切图/图标.png" alt="">
                            <span>预警类型一</span>
                        </div>
                        <div class="content">
                            <div class="rows" v-for="(val, ind) in 10" :key="val +',' + ind">
                                {{ind === 0? '潜在风险行为':'风险' + val}} : xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                            </div>
                        </div>
                    </div>
                    <div style="overflow:hidden;width: calc(100% - 993px);height: 100%;">
                        <transition name="moveRight">
                            <div v-show="showRightPart" class="rightPart">
                                <img class="line" src="./切图/连接线.png" alt="">
                                <div>
                                    <div class="top">
                                        <div class="head">风险处置</div>
                                        <div class="body">
                                            <div class="lhead">
                                                <img src="./切图/小图标.png" alt="">
                                                <div>处置形式</div>
                                                <div>情况</div>
                                            </div>
                                            <div class="lbody">
                                                <div class="li" v-for="(item, index) in 10" :key="index">
                                                    <div>1.xx区教育局</div>
                                                    <div>完成<input type="checkbox"></div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="bottom">
                                        <div class="head">启动应急预案</div>
                                        <div class="body">
                                            <div class="lhead">
                                                <img src="./切图/小图标.png" alt="">
                                                紧急预案
                                            </div>
                                            <div class="lbody">
                                                1.xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                                2.xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                                3.xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </transition>
                    </div>
                </div>
            </div>
    </div>
</template>
<script>
export default {
  name: 'MagicSquare',
  props: ['item'],
  data () {
    return {
      dateRange: [], // 日期范围列表
      showBoxDetail: false, // 是否展示预警详情
      checkDate: '', // 选中日期
      showRightPart: false, // 是否展示右侧部分
      showDateCheckbox: false // 是否展示日期选择框
    }
  },
  computed: {
    dateList () {
      let arr = []
      if (this.dateRange.length === 2 && this.dateRange[0]) {
        let startDate = this.DateToString(this.dateRange[0])
        let endDate = this.DateToString(this.dateRange[1])
        arr = this.getdiffdate(startDate, endDate)
      } else {
        let startDate = new Date()
        let endDate = new Date(startDate.getTime() + 1000 * 60 * 60 * 24 * 14)
        arr = this.getdiffdate(this.DateToString(startDate), this.DateToString(endDate))
      }
      return arr
    }
  },
  watch: {
    dateRange: function () {
      console.log('dateRange', this.dateRange)
    },
    dateList: function () {
      if (this.dateList.length) {
        this.checkDate = this.dateList[0]
        this.$refs.timeAxis.scrollLeft = 0
      }
    },
    checkDate: function () {
      this.showRightPart = false
      this.showBoxDetail = false
    }
  },
  methods: {
    // 获取两日期之间日期列表函数
    getdiffdate (stime, etime) {
    // 初始化日期列表，数组
      var diffdate = []
      var i = 0
      // 开始日期小于等于结束日期,并循环
      while (stime <= etime) {
        diffdate[i] = stime

        // 获取开始日期时间戳
        var stime_ts = new Date(stime).getTime()

        // 增加一天时间戳后的日期
        var next_date = stime_ts + (24 * 60 * 60 * 1000)

        // 拼接年月日，这里的月份会返回（0-11），所以要+1
        var next_dates_y = new Date(next_date).getFullYear() + '-'
        var next_dates_m = (new Date(next_date).getMonth() + 1 < 10) ? '0' + (new Date(next_date).getMonth() + 1) + '-' : (new Date(next_date).getMonth() + 1) + '-'
        var next_dates_d = (new Date(next_date).getDate() < 10) ? '0' + new Date(next_date).getDate() : new Date(next_date).getDate()

        stime = next_dates_y + next_dates_m + next_dates_d

        // 增加数组key
        i++
      }
      return diffdate
    },
    // 设置横向滚动
    onMouseWheel (e, refName) {
      let eventDelta = -e.wheelDelta || -e.deltaY * 40
      let box = this.$refs[refName]
      box.scrollLeft = box.scrollLeft + eventDelta / 2
    },
    // 日期类型转换字符串
    DateToString (date) {
      if (typeof date === 'object') {
        let y = date.getFullYear()
        let M = ''
        let d = ''
        if (date.getMonth() < 9) {
          M = '0' + (date.getMonth() + 1)
        } else {
          M = date.getMonth() + 1
        }
        if (date.getDate() < 10) {
          d = '0' + date.getDate()
        } else {
          d = date.getDate()
        }
        return y + '-' + M + '-' + d
      } else {
        return date
      }
    },
    ChangeDate (date) {
      this.checkDate = date
    },
    ShowDetail (data) {
      this.showBoxDetail = true
    },
    CloseDtail () {
      this.showRightPart = false
      this.showBoxDetail = false
    }
  },
  mounted () {
  }
}
</script>
<style scoped lang="scss">
::-webkit-scrollbar-thumb{
    color: #8C9EA0 !important;
}
.MagicSquare{
    width: 100%;
    height: 100%;
    background: url('./切图/背景.png') no-repeat;
    background-size: 100% 100%;
    padding: 166px 78px 0px 80px;
        .title1{
            width: 100%;
            height: 80px;
            margin-bottom: 53px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding-left: 92px;
            background: url('./切图/标题2.png') no-repeat;
            background-size: 100% 100%;
            >span:first-child{
                font-size: 36px;
                font-family: Alibaba PuHuiTi, Alibaba PuHuiTi-Medium;
                font-weight: 500;
                text-align: left;
                color: #ffffff;
                letter-spacing: 2.7px;
            }
        }
        .timeAxis{
            width: 100%;
            height: 220px;
            overflow-x: scroll;
            overflow-y: hidden;
            white-space: nowrap;
            margin-bottom: 47px;
            .timeBox{
                width: 260px;
                height: 190px;
                cursor: pointer;
                border-radius: 5px;
                background: #24374c;
                border: 1px solid #5b6d9f;
                margin-right: 14px;
                border-radius: 5px;
                display: inline-block;
                padding: 32px 21px;
                position: relative;
                .warning{
                    width: 82px;
                    height: 74px;
                    position: absolute;
                    top: 0;
                    right: -5px;
                }
                .yAndm{
                    font-size: 33px;
                    font-family: PingFang SC, PingFang SC-Light;
                    font-weight: 300;
                    text-align: left;
                    color: #cee9eb;
                }
                .day{
                    width: 100%;
                    font-size: 33px;
                    font-family: PingFang SC, PingFang SC-Regular;
                    font-weight: 400;
                    color: #bcd6d9;
                    text-align: center;
                    >span{
                        font-size: 66px;
                        font-family: PingFang SC, PingFang SC-Regular;
                        font-weight: 400;
                        text-align: left;
                        color: #00fff6;
                        margin-right: 5px;
                    }
                }
            }
            .timeBox:last-child{
                margin-right: 0px !important;
            }
            .checkedBox{
                background: #20446b;
                border: 3px solid #00fff6;
            }
        }
        .title2{
            width: 100%;
            height: 80px;
            line-height: 80px;
            margin-bottom: 29px;
            padding-left: 92px;
            background: url('./切图/标题2.png') no-repeat;
            background-size: 100% 100%;
            font-size: 36px;
            font-family: Alibaba PuHuiTi, Alibaba PuHuiTi-Medium;
            font-weight: 500;
            text-align: left;
            color: #ffffff;
            letter-spacing: 2.7px;
        }
        .warningBox{
            width: 100%;
            height: 730px;
            padding: 30px 27px;
            background: url('./切图/框.png') no-repeat;
            background-size: 100% 100%;
            position: relative;
            .boxList{
                width: 100%;
                height: 100%;
                display: flex;
                align-items: center;
                overflow: scroll;
                flex-wrap: wrap;
                .smallBox{
                    width: 1310px;
                    height: 317px;
                    cursor: pointer;
                    flex-shrink: 0;
                    margin-right: 22px;
                    margin-bottom: 34px;
                    border-radius: 5px;
                    background: url('./切图/矩形1.png') no-repeat;
                    background-size: 100% 100%;
                    position: relative;
                    padding: 30px 0 0 40px;
                    .todayHappen{
                        width: 135px;
                        height: 54px;
                        position: absolute;
                        top: 14px;
                        right: -13px;
                    }
                    .title{
                        width: 100%;
                        margin-bottom: 27px;
                        display: flex;
                        align-items: center;
                        >img{
                            width: 49px;
                            height: 38px;
                            margin-right: 5px;
                        }
                        >span{
                            font-size: 36px;
                            font-family: Alibaba PuHuiTi, Alibaba PuHuiTi-Regular;
                            font-weight: 400;
                            text-align: left;
                            color: #F8CE7C;
                        }
                    }
                    .content{
                        width: 100%;
                        height: 190px;
                        padding-left: 60px;
                        overflow: scroll;
                        .rows{
                            width: 100%;
                            font-size: 32px;
                            font-family: Alibaba PuHuiTi, Alibaba PuHuiTi-Regular;
                            font-weight: 400;
                            text-align: left;
                            color: #d0f7f8;
                            margin-bottom: 25px;
                        }
                    }
                }
            }
            .boxDetail{
                width: 100%;
                height: 100%;
                position: absolute;
                top: 0;
                left: 0;
                display: flex;
                align-items: center;
                .return{
                    position: absolute;
                    top: 0px;
                    right: 0px;
                    font-size: 26px;
                    color: #00fff6;
                    cursor: pointer;
                }
                .leftPart{
                    width: 993px;
                    height: 686px;
                    border-radius: 5px;
                    background: url('./切图/预警类型框.png') no-repeat;
                    background-size: 100% 100%;
                    padding: 26px 30px;
                    position: relative;
                    .showButton{
                        width: 140px;
                        height: 50px;
                        text-align: center;
                        line-height: 50px;
                        font-size: 26px;
                        cursor: pointer;
                        color: #00fff6;
                        position: absolute;
                        top: 33px;
                        right: 26px;
                        background: #152a43;
                        border: 1px solid #00fff7;
                    }
                    .title{
                        width: 100%;
                        margin-bottom: 27px;
                        display: flex;
                        align-items: center;
                        >img{
                            width: 49px;
                            height: 38px;
                            margin-right: 5px;
                        }
                        >span{
                            font-size: 36px;
                            font-family: Alibaba PuHuiTi, Alibaba PuHuiTi-Regular;
                            font-weight: 400;
                            text-align: left;
                            color: #F8CE7C;
                        }
                    }
                    .content{
                        width: 100%;
                        height: 580px;
                        overflow: scroll;
                        .rows{
                            width: 100%;
                            font-size: 32px;
                            font-family: Alibaba PuHuiTi, Alibaba PuHuiTi-Regular;
                            font-weight: 400;
                            text-align: left;
                            color: #d0f7f8;
                            margin-bottom: 25px;
                        }
                    }
                }
                .rightPart{
                    width: 100%;
                    height: 100%;
                    display: flex;
                    align-items: center;
                    .line{
                        width: 299px;
                        height: 363px;
                    }
                    >div:last-child{
                        .top{
                            display: flex;
                            align-items: center;
                            margin-bottom: 27px;
                            .head{
                                width: 77px;
                                height: 300px;
                                border: 1px solid #a7f6f3;
                                background: url('./切图/矩形18.png') no-repeat;
                                background-size: 100% 100%;
                                font-size: 32px;
                                font-weight: 400;
                                text-align: left;
                                color: #d0f7f8;
                                letter-spacing: 3.2px;
                                writing-mode:vertical-rl;
                                text-align: center;
                                line-height: 77px;
                                margin-right: 12px;
                            }
                            .body{
                                padding: 26px;
                                height: 300px;
                                width: 1291px;
                                background: url('./切图/矩形15.png') no-repeat;
                                background-size: 100% 100%;
                                .lhead{
                                    width: 100%;
                                    display: flex;
                                    align-items: center;
                                    font-size: 32px;
                                    font-weight: 400;
                                    text-align: left;
                                    color: #ffffff;
                                    >img{
                                        width: 11px;
                                        height: 19px;
                                        margin-right: 7px;
                                    }
                                    >div:nth-child(2){
                                        width: 80%
                                    }
                                    >div:nth-child(3){

                                    }
                                }
                                .lbody{
                                    width: 100%;
                                    height: 180px;
                                    overflow: scroll;
                                    font-size: 32px;
                                    font-weight: 400;
                                    text-align: left;
                                    margin-left: 18px;
                                    color: #d0f7f8;
                                    .li{
                                        width: 100%;
                                        padding: 5px 0px;
                                        display: flex;
                                        align-items: center;
                                        >div:nth-child(1){
                                            width: 80%
                                        }
                                        >div:nth-child(2){
                                            >input{
                                                width: 40px;
                                                height: 40px;
                                                vertical-align: middle;
                                                margin-left: 10px;
                                            }
                                        }
                                    }
                                    .li:nth-child(odd){
                                        background:url('./切图/矩形21.png') no-repeat;
                                        background-size: 100% 100%;
                                    }
                                }
                            }
                        }
                        .bottom{
                            display: flex;
                            align-items: center;
                            .head{
                                width: 77px;
                                height: 300px;
                                border: 1px solid #a7f6f3;
                                background: url('./切图/矩形18.png') no-repeat;
                                background-size: 100% 100%;
                                font-size: 32px;
                                font-weight: 400;
                                text-align: left;
                                color: #d0f7f8;
                                letter-spacing: 3.2px;
                                writing-mode:vertical-rl;
                                text-align: center;
                                line-height: 77px;
                                margin-right: 12px;
                            }
                            .body{
                                padding: 26px;
                                height: 300px;
                                width: 1291px;
                                background: url('./切图/矩形15.png') no-repeat;
                                background-size: 100% 100%;
                                .lhead{
                                    width: 100%;
                                    display: flex;
                                    align-items: center;
                                    font-size: 32px;
                                    font-weight: 400;
                                    text-align: left;
                                    color: #ffffff;
                                    >img{
                                        width: 11px;
                                        height: 19px;
                                        margin-right: 7px;
                                    }
                                    >div:nth-child(2){
                                        width: 80%
                                    }
                                    >div:nth-child(3){

                                    }
                                }
                                .lbody{
                                    width: 100%;
                                    height: 200px;
                                    overflow: scroll;
                                    font-size: 32px;
                                    font-weight: 400;
                                    text-align: left;
                                    padding-left: 24px;
                                    color: #ffffff;
                                }
                            }
                        }
                    }
                }
            }
        }

}
    .moveRight-enter-active {
    animation: box-right-in 0.6s;
    }
    .moveRight-leave-active {
        animation: box-left-leave 0.6s;
    }
    .moveLeft-enter-active {
    animation: box-left-in 0.5s;
    }
    .moveLeft-leave-active {
        animation: box-right-leave 0.5s;
    }
    .moveTop-enter-active {
    animation: box-top-in 0.5s;
    }
    .moveTop-leave-active {
        animation: box-bottom-leave 0.5s;
    }
    .moveBottom-enter-active {
    animation: box-bottom-in 0.5s;
    }
    .moveBottom-leave-active {
        animation: box-top-leave 0.5s;
    }
    // 向左移动
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
</style>
<style lang="scss">
.MagicSquare{
    .datepicker{
        display: flex;
        align-items: center;
        .ivu-date-picker{
            .ivu-date-picker-rel{
                .ivu-input-wrapper{
                    background: url('./切图/日期.png') no-repeat;
                    background-size: 100% 100%;
                } // 输入框背景
                .ivu-input-suffix{
                    right: 15px;
                } // 日历图标位置
                .ivu-icon{
                    font-size: 36px !important;
                    line-height: 80px !important;
                    color: #d0f7f8 !important;
                }
                .ivu-input{
                    width: 594px !important;
                    height: 80px !important;
                    font-size: 36px !important;
                    color: #d0f7f8 !important;
                }
            }
            .ivu-select-dropdown{
                background: rgb(53, 91, 133) !important;
                top: unset !important;
                left: 1650px !important;
                font-size: 32px !important;
                width: 1070px !important;
                height: 555px !important;
                max-height: unset !important;
                .ivu-picker-panel-body{
                    .ivu-picker-panel-content{ // 设置下拉框中左右两个box
                        width: 535px !important;
                        height: 555px !important;
                        .ivu-date-picker-header{ // 设置下拉框中头部
                            height: 90px !important;
                            line-height: 90px !important;
                            .ivu-picker-panel-icon-btn{
                                height: 80px !important;
                                width: auto !important;
                                line-height: 80px !important;
                                .ivu-icon{
                                    font-size: 34px !important;
                                }
                            }
                        }
                        .ivu-date-picker-cells{ // 设置下拉框中数字box
                            width: 535px !important; // 存在10px的margin，父级宽度减去
                            margin: 0px !important;
                            padding: 15px !important;
                            .ivu-date-picker-cells-header{
                                span{
                                    margin: 2px 24px !important;
                                }
                            }
                            .ivu-date-picker-cells-cell{
                                width: 38px !important;
                                margin: 15px 17px !important;
                                height: 35px !important;
                                em{
                                    width: 40px !important;
                                    height: 35px !important;
                                    line-height: 35px !important;
                                    margin: 0 !important;
                                }
                            }
                        }
                        .ivu-date-picker-cells-year{
                            .ivu-date-picker-cells-cell{
                                width: 80px !important;
                                em{
                                    width: 80px !important;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
</style>
