<template>
  <div class="bootstrap-table home-table"
       :style="boxStyle">
       <div class="checkBox" :style="checkStyle" v-if="item.chartData.dataArray && item.chartData.dataArray.length">
            <div class="normalBtn" v-for="(v, i) in item.chartData.dataArray" :style="buttonStyle(i)" :key="i" @click="changeData(i)">
              {{v.title}}
            </div>
       </div>
    <div class="fixed-table-header"
        :style="heightLinght">
      <table class="table"
             :style="theadTrStyle"
             style="table-layout: fixed;">
        <thead :style="theadTrStyle">
          <tr :style="[trStyle,theadTrStyle]">
            <th v-for="(title, index) in tableData.columns"
                :key="index"
                :style="[thStyle,heightLinght,widthLinght(index)]"
                v-tooltip.bottom="{ content: title, container: '#home-html', classes: 'bottom in'}"
              >{{title}}</th>
          </tr>
        </thead>
      </table>
    </div>
    <div class="fixed-table-body"
         style="padding-bottom: 26px; overflow: hidden; position: relative;height: 95%;">
      <transition :name="tableEmpty ? '' : item.direction === 'top' ? 'table-tpfadeout': 'table-fadeout'">
        <table class="table"
               v-if="tableMove"
               style="table-layout: fixed; position: absolute; top:0px; left: 0;">
          <tbody>
            <tr v-for="(tr, id) in page1Data"
                :style="[trStyle,tbodyTrStyle(id),warnStyle('page1Data',id)]"
                @click="showXQ(tr)"
                :key="id">
              <td v-for="(tdText, ind) in tableData.columns"
                  :key="ind"
                  :style="[thStyle,heightLinght,widthLinght(ind)]"
                  v-tooltip.bottom="{ content: tdText, container: '#home-html', classes: 'bottom in'}">{{tr[tdText]}}</td>
            </tr>
          </tbody>
        </table>
      </transition>
      <transition :name="tableEmpty ? '' : item.direction === 'top' ? 'table-tpfadein': 'table-fadein'">
        <table class="table"
               v-if="!tableMove"
               style="table-layout: fixed; position: absolute; top:0px; left: 0;">
          <tbody>
            <tr v-for="(tr, id) in page2Data"
                :style="[trStyle,tbodyTrStyle(id),warnStyle('page2Data',id)]"
                @click="showXQ(tr)"
                :key="id">
              <td v-for="(tdText, ind) in tableData.columns"
                  :key="ind"
                  :style="[thStyle,heightLinght,widthLinght(ind)]"
                  v-tooltip.bottom="{ content: tdText, container: '#home-html', classes: 'bottom in'}">{{tr[tdText]}}</td>
            </tr>
          </tbody>
        </table>
      </transition>
    </div>
    <div class="v-charts-data-empty"
         v-if="tableEmpty"
         style="width: 100%; position:absolute; top: 50%; text-align: center; font-size: 12px;">
      <div>
        <p>抱歉，没有数据可供展示...</p>
      </div>
    </div>
  </div>
</template>
<script>
// import { titleShowFn } from '#/js/public'
import { mapGetters } from 'vuex'
import { gbs } from '@/config/settings'
export default {
  name: 'ConditionalTable',
  props: ['item', 'moving'],
  data () {
    return {
      tableMove: true,
      pageNum: 5,
      intervalTime: 4000,
      checkedIndex: 0,
      page1Data: [],
      page2Data: [],
      tableData: {
        columns: [],
        rows: []
      },
      currentIndex: 0,
      intervalId: 0,
      nowShowIndex: -1,
      myNewInterVal: '',
      nowPage: 0,
      tableEmpty: false
    }
  },
  computed: {
    ...mapGetters([
      'pageVisiable'
    ]),
    boxStyle: function () {
      return {
        width: this.item.width + 'px !important',
        height: this.item.height + 'px !important',
        tableLayout: 'fixed',
        backgroundImage: this.item.tableBack
          ? 'url(' + gbs.host + this.item.tableBack + ')'
          : '',
        backgroundSize: '100% 100%',
        overflow: 'hidden',
        border: this.item.bdpx + 'px solid ' + this.item.bdClr + ' !important'
      }
    },
    checkStyle: function () {
      return {
        paddingLeft: this.item.paddingLeft + 'px',
        paddingTop: this.item.paddingTop + 'px',
        fontSize: this.item.boxFontSize + 'px'
      }
    },
    buttonStyle: function () {
      return (index) => {
        if (this.currentIndex === index) {
          return {
            background: 'url(' + require('./checked.png') + ') no-repeat',
            padding: this.item.buttonPadding + 'px !important',
            marginRight: this.item.buttonMargin + 'px'
          }
        } else {
          return {
            background: 'url(' + require('./normal.png') + ') no-repeat',
            padding: this.item.buttonPadding + 'px !important',
            marginRight: this.item.buttonMargin + 'px'
          }
        }
      }
    },
    trStyle: function () {
      return {
        color: this.item.clr + ' !important',
        fontSize: this.item.fontSize + 'px !important'
      }
    },
    thStyle: function () {
      return {
        textAlign: this.item.Alignment + '!important',
        padding: '8px'
      }
    },
    heightLinght: function () {
      return {
        height: this.item.heightLinght + 'px',
        'line-height': this.item.heightLinght - 16 + 'px'
      }
    },
    widthArry: function () {
      let arr = this.item.LineSizeArry || []
      if (this.tableData && this.tableData.columns) {
        this.tableData.columns.forEach((element, i) => {
          if (arr[i]) {

          } else {
            arr.push(86)
          }
        })
      }
      return arr
    },
    theadTrStyle: function () {
      if (this.item.Internal === 'true') {
        return {
          backgroundColor: 'transparent !important',
          boxShadow: '0 0 15px ' + this.item.hdBgClr + ' inset',
          color: this.item.hdClr + ' !important',
          fontSize: this.item.hdfontSize + 'px !important'
        }
      } else {
        return {
          backgroundColor: this.item.hdBgClr + ' !important', // 表头背景色
          color: this.item.hdClr + ' !important',
          fontSize: this.item.hdfontSize + 'px !important'
        }
      }
    },
    scrollStyle: function () {
      return {
        // marginTop: -36 * this.pageNum * this.nowPage + 'px' // 回流
        transform: 'translateY(' + -36 * this.pageNum * this.nowPage + 'px)'
      }
    }
  },
  watch: {
    pageVisiable: function (newV) {
      if (newV) {
        this.initLeftMove()
      } else {
        this.intervalId && clearTimeout(this.intervalId)
        this.intervalId = null
      }
    },
    'item.chartData': {
      handler (newV, oldV) {
        if (this.item.chartData.dataArray && this.item.chartData.dataArray.length) {
          this.tableData = this.item.chartData.dataArray[0]
          this.currentIndex = 0
        }
      },
      deep: true
    },
    'currentIndex': function (newV, oldV) {
      if (newV !== oldV) {
        this.tableData = this.item.chartData.dataArray[this.currentIndex]
      }
    },
    'tableData': function (newV, oldV) {
      if (this.tableData === null || this.tableData === '') {
        this.tableData = {
          'columns': [],
          'rows': []
        }
      }
      this.pageNum = Number(this.item.pageNum)
      if (JSON.stringify(oldV) === JSON.stringify(newV)) return
      if ((this.tableData.rows && this.tableData.rows.length < 1) || !this.tableData.rows) {
        this.tableEmpty = true
        this.page1Data = []
        this.page2Data = []
        this.intervalId && clearTimeout(this.intervalId)
        return
      } else {
        this.tableEmpty = false
      }
      this.initLeftMove()
    },
    'item.direction': function (val) {
      this.initLeftMove()
    },
    'item.pageNum': function (val) {
      this.pageNum = Number(val)
      this.initLeftMove()
    },
    'item.speed': function (newV) {
      if (newV === '1') {
        this.intervalTime = 2000
      } else if (newV === '2') {
        this.intervalTime = 4000
      } else {
        this.intervalTime = 6000
      }
      this.initLeftMove()
    },
    'item.OneLineSize': function (newV, oldV) {
      this.item.LineSizeArry[this.tableData.columns.indexOf(this.item.AlarmField)] = newV
      document.querySelector('.DataChangeBtn').click()
    },
    'item.AlarmField': function (newV, oldV) {
      this.item.OneLineSize = this.item.LineSizeArry[this.tableData.columns.indexOf(newV)]
    }
  },
  methods: {
    getNewChartData () {
      var _this = this
      if (_this.item.moreUrlArry[_this.nowShowIndex]) {
        let myUrl = _this.item.moreUrlArry[_this.nowShowIndex].url
        $.each(_this.item.moreUrlArry[_this.nowShowIndex].params, function (i, d) {
          _this.item.moreUrlArry[_this.nowShowIndex].params[i] = $.isArray(d) ? d.join(',') : d
        })
        if (_this.item.ctDataSource === 'system') {
          $.ajax({
            url: gbs.host + myUrl, // 第三方的ur已经拼接好host
            data: _this.item.moreUrlArry[_this.nowShowIndex].params,
            type: _this.item.moreUrlArry[_this.nowShowIndex].method || 'post',
            cache: false,
            ascyn: false,
            success: function (res) {
              _this.tableData = res.obj
            }
          })
        }
      }
    },
    showXQ (data) {
      if (this.$parent.$parent.ShowTanKuangBox) {
        let dataArray = {
          title: '数据详情',
          data: data
        }
        this.$parent.$parent.ShowTanKuangBox(dataArray)
      }
    },
    changeData (index) {
      this.currentIndex = index
    },
    warnStyle (ArryName, index) {
      if (this.item.AlarmField) {
        if (this.item.AlarmType === 'chart') {
          if (this.item.AlarmChart !== '' && JSON.stringify(this[ArryName][index][this.item.AlarmField]).indexOf(this.item.AlarmChart) >= 0) {
            return {
              'color': this.item.AlarmColor + ' !important'
            }
          }
        } else {
          if (this.item.AlarmNum !== '') {
            let error = false
            if (this.item.AlarmNumType === 'greater') {
              if (this[ArryName][index][this.item.AlarmField] * 1 > this.item.AlarmNum * 1) {
                error = true
              }
            } else if (this.item.AlarmNumType === 'equal') {
              if (this[ArryName][index][this.item.AlarmField] * 1 === this.item.AlarmNum * 1) {
                error = true
              }
            } else {
              if (this[ArryName][index][this.item.AlarmField] * 1 < this.item.AlarmNum * 1) {
                error = true
              }
            }
            if (error) {
              return {
                'color': this.item.AlarmColor + ' !important'
              }
            }
          }
        }
      }
      return {}
    },
    widthLinght (index) {
      // let arr = JSON.parse(this.widthArry)
      if (this.item.OneLineType === 'default') {
        return ''
      } else {
        return {
          display: 'inline-block',
          width: this.item.LineSizeArry[index] + 'px'
        }
      }
    },
    tbodyTrStyle: function (index) {
      let Color = ''
      if (this.item.Zebra === 'true' && index % 2 === 1) {
        Color = this.item.ZebraColor
      } else {
        Color = this.item.bgClr
      }
      if (this.item.Internal === 'true') {
        return {
          backgroundColor: 'transparent !important',
          boxShadow: '0 0 15px ' + Color + ' inset'
        }
      } else {
        return {
          backgroundColor: Color + ' !important', // 表体背景色
          borderTop: this.item.bdpx + 'px solid ' + this.item.bdClr + ' !important'
        }
      }
    },
    initLeftMove () {
      // 两个transition，vue动画实现的方式（可用于横向轮播,或者允许设置最后一页的数据不足时自动添加空数据）
      if (this.intervalId) {
        clearTimeout(this.intervalId)
      }
      var _this = this
      this.nowPage = 0
      if (this.tableData && this.tableData.rows) {
        this.page1Data = this.tableData.rows.slice(0, this.pageNum)
        this.page2Data = this.tableData.rows.slice(this.pageNum, this.pageNum * (this.nowPage + 2))
        if (this.tableData.rows.length > this.pageNum) {
          let totalPage = Math.floor(this.tableData.rows.length / this.pageNum)
          if (totalPage === this.tableData.rows.length / this.pageNum) {
            totalPage--
          }
          if (!this.moving || this.moving === 'false') return
          if (this.intervalId) {
            clearInterval(this.intervalId)
            this.intervalId = null
          }
          this.intervalId = setInterval(() => {
            _this.tableMove = !_this.tableMove
            if (_this.tableMove) {
              _this.nowPage++
              if (_this.nowPage === totalPage) {
                _this.nowPage = -1
                _this.page1Data = _this.tableData.rows.slice(_this.pageNum * totalPage, _this.pageNum * (totalPage + 1))
              } else {
                _this.page1Data = _this.tableData.rows.slice(_this.pageNum * _this.nowPage, _this.pageNum * (_this.nowPage + 1))
              }
              _this.page2Data = _this.tableData.rows.slice(_this.pageNum * (_this.nowPage + 1), _this.pageNum * (_this.nowPage + 2))
            }
          }, _this.intervalTime)
        }
      }
    }
  },
  mounted: function () {
    if (this.item.chartData.dataArray && this.item.chartData.dataArray.length) {
      this.tableData = this.item.chartData.dataArray[this.currentIndex]
    }
    if (this.tableData && this.tableData.columns) {
      this.tableData.columns.forEach((element, i) => {
        if (this.widthArry[i]) {

        } else {
          this.widthArry.push(86)
        }
      })
    }
    this.pageNum = Number(this.item.pageNum)
    if (this.item.speed === '3') {
      this.intervalTime = 6000
    } else if (this.item.speed === '1') {
      this.intervalTime = 2000
    } else {
      this.intervalTime = 4000
    }
    this.initLeftMove()
    if (this.tableData && this.tableData.rows && this.tableData.rows.length < 1) {
      this.tableEmpty = true
    }
    if (this.item.moreUrlArry && this.item.moreUrlArry.length > 1 && this.item.intervieData > 0) {
      if (this.myNewInterVal) {
        clearInterval(this.myNewInterVal)
      }
      this.myNewInterVal = setInterval(() => {
        this.nowShowIndex = (this.nowShowIndex + 1) % this.item.moreUrlArry.length
        this.getNewChartData()
      }, this.item.intervieData * 1000)
    }
  },
  beforeDestroy: function () {
    if (this.intervalId) {
      clearInterval(this.intervalId)
      this.intervalId = null
    }
    if ($(this.$el).find('[title]').length > 0) {
      $(this.$el).find('[title]').tooltip('destroy')
    }
    if (this.myNewInterVal) {
      clearInterval(this.myNewInterVal)
    }
    this.page1Data = null
    this.page2Data = null
  }
}
</script>
<style scoped lang="scss">
.checkBox{
  display: flex;
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
.home-table .table {
  background: transparent;
  position: relative;
}
.home-table .table tbody tr,
.home-table .table tbody td,
.home-table .table th {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  background: transparent !important;
  border-bottom: none !important;
}

/* 左右移动 */
.table-fadeout-leave-active {
  animation: table-left-leave 1s;
}
.table-fadein-enter-active {
  animation: table-left-in 1s;
}
/* 上下轮播 */
.table-tpfadeout-leave-active {
  animation: table-top-leave 1s;
}
.table-tpfadein-enter-active {
  animation: table-top-in 1s;
}
/* @keyframes table-leave {
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(-100%);
  }
} */
@keyframes table-left-leave {
  from {
    transform: translateX(0);
  }
  to {
    transform: translateX(-100%);
  }
}
@keyframes table-left-in {
  from {
    transform: translateX(100%);
  }
  to {
    transform: translateX(0);
  }
}
@keyframes table-top-leave {
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(-100%);
  }
}
@keyframes table-top-in {
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
}
</style>
