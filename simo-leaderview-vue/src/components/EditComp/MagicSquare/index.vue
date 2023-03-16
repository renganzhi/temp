<template>
    <div class="MagicSquare">
        <div class="bigTitle">预警魔方</div>
        <div class="squareContent">
            <div class="title1">日期选择</div>
            <div class="datepicker">
                <DatePicker v-model="dateRange" type="daterange" split-panels placeholder="Select date"></DatePicker>
            </div>
            <div class="timeAxis" ref="timeAxis" @mousewheel="onMouseWheel($event, 'timeAxis')">
                <div class="timeBox" v-for="(item, index) in dateList" :key="index">
                    <div class="yAndm">
                        <span>{{item.split('-')[0]}}年</span>
                        <span>{{item.split('-')[1]}}月</span>
                    </div>
                    <div class="day"><span>{{item.split('-')[2]}}</span>日</div>
                </div>
            </div>
            <div class="title2">预警信息</div>
            <div class="warningBox">
                <div class="boxList">

                </div>
                <div v-show="showBoxDetail" class="boxDetail">

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
      showBoxDetail: false // 是否展示预警详情
    }
  },
  computed: {
    dateList () {
      let arr = []
      if (this.dateRange.length === 2 && this.dateRange[0]) {
        let startDate = this.DateToString(this.dateRange[0])
        let endDate = this.DateToString(this.dateRange[1])
        arr = this.getdiffdate(startDate, endDate)
      }
      return arr
    }
  },
  watch: {
    dateRange: function () {
      console.log('dateRange', this.dateRange)
    },
    dateList: function () {
      console.log('dateList', this.dateList)
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
    }
  },
  mounted () {
  }
}
</script>
<style scoped lang="scss">
.moveLeft-enter-active {
    animation: box-left-in 0.5s;
}
.moveLeft-leave-active {
    animation: box-right-leave 0.5s;
}
@keyframes box-left-in {
    from {
        transform: translateX(100%);
    }
    to {
        transform: translateX(0);
    }
}
@keyframes box-right-leave {
    from {
        transform: translateX(0);
    }
    to {
        transform: translateX(100%);
    }
}
.MagicSquare{
    width: 100%;
    height: 100%;
    .bigTitle{
        height: 84px;
        width: 100%;
        background: linear-gradient(90deg, rgba(4, 58, 121, 1) 75%, rgba(0, 0, 0, 1) 107%);;
    }
    .squareContent{
        height: calc(100% - 84px);
        width: 100%;
        padding: 30px 40px;
        background: #0a2047;
        .title1{
            width: 100%;
            margin-bottom: 20px;
        }
        .datepicker{
            width: 100%;
            margin-bottom: 30px;
        }
        .timeAxis{
            width: 100%;
            height: 100px;
            overflow-x: scroll;
            overflow-y: hidden;
            white-space: nowrap;
            margin-bottom: 20px;
            .timeBox{
                width: 140px;
                height: 95px;
                border: 1px solid rgba(190, 225, 255, 1);
                margin-right: 20px;
                border-radius: 5px;
                display: inline-block;
                padding: 10px;
                .yAndm{
                    width: 100%;
                    font-size: 16px;
                    display: flex;
                    justify-content: space-between;
                }
                .day{
                    width: 100%;
                    font-size: 16px;
                    text-align: center;
                    >span{
                        font-size: 28px;
                        font-weight: bold;
                    }
                }
            }
            .timeBox:last-child{
                margin-right: 0px !important;
            }
            .checkedBox{
                color: #0b3063;
            }
        }
    }
}
</style>
<style lang="scss">
.MagicSquare{
    .datepicker{
        .ivu-date-picker{
            .ivu-date-picker-rel{
                .ivu-icon{
                    font-size: 24px !important;
                    line-height: 50px !important;
                    color: #fff !important;
                }
                .ivu-input{
                    width: 600px !important;
                    height: 50px !important;
                    border: 1px solid #d5d9e8 !important;
                    font-size: 28px !important;
                    color: #fff !important;
                }
            }
            .ivu-select-dropdown{
                top: unset !important;
                left: unset !important;
                font-size: 28px !important;
                width: 600px !important;
                height: 350px !important;
                max-height: unset !important;
                .ivu-picker-panel-body{
                    .ivu-picker-panel-content{ // 设置下拉框中左右两个box
                        width: 300px !important;
                        height: 350px !important;
                        .ivu-date-picker-header{ // 设置下拉框中头部
                            height: 40px !important;
                            line-height: 40px !important;
                        }
                        .ivu-date-picker-cells{ // 设置下拉框中数字box
                            width: 300px !important; // 存在10px的margin，父级宽度减去
                            margin: 0px !important;
                            padding: 15px !important;
                            .ivu-date-picker-cells-header{
                                span{
                                    margin: 2px 7px !important;
                                }
                            }
                            .ivu-date-picker-cells-cell{
                                width: 30px !important;
                                margin: 4px 4px !important;
                                em{
                                    width: 30px !important;
                                    height: 30px !important;
                                    line-height: 30px !important;
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
