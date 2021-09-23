<template>
  <div class="bootstrap-table home-table"
       :style="boxStyle">
    <div class="fixed-table-header"
        :style="heightLinght">
      <table class="table"
             :style="theadTrStyle"
             style="table-layout: fixed;">
        <thead :style="theadTrStyle">
          <tr :style="[trStyle,theadTrStyle]">
            <th v-for="(title, index) in item.chartData.columns"
                :key="index"
                :style="[thStyle,heightLinght,widthLinght(index)]"
                v-tooltip.bottom="{ content: title, container: '#home-html', classes: 'bottom in'}"
              >{{title}}</th>
          </tr>
        </thead>
      </table>
    </div>
    <!-- <div class="fixed-table-body"
         v-if="item.direction === 'top'"
         :style="{'max-height': pageNum * 36 + 'px'}"
         style="padding-bottom: 26px; overflow: hidden;">
      <transition>
        <table class="table"
               :style="scrollStyle"
               style="table-layout: fixed; transition: all 0.6s ease;">
          <tbody>
            <tr :style="[trStyle,tbodyTrStyle]"
                v-for="(tr, id) in item.chartData.rows"
                :key="id">
              <td v-for="(tdText, ind) in tr"
                  :key="ind"
                  data-original-title="tdText">{{tdText}}</td>
            </tr>
          </tbody>
        </table>
      </transition>
    </div> -->
    <div class="fixed-table-body"
         style="padding-bottom: 26px; overflow: hidden; position: relative;height: 95%;">
      <transition :name="tableEmpty ? '' : item.direction === 'top' ? 'table-tpfadeout': 'table-fadeout'">
        <table class="table"
               v-if="tableMove"
               style="table-layout: fixed; position: absolute; top:0px; left: 0;">
          <tbody>
            <tr v-for="(tr, id) in page1Data"
                :style="[trStyle,tbodyTrStyle(id),warnStyle('page1Data',id)]"
                :key="id">
              <td v-for="(tdText, ind,i) in tr"
                  :key="ind"
                  :style="[thStyle,heightLinght,widthLinght(i)]"
                  v-tooltip.bottom="{ content: tdText, container: '#home-html', classes: 'bottom in'}">{{tdText}}</td>
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
                :key="id">
              <td v-for="(tdText, ind,i) in tr"
                  :key="ind"
                  :style="[thStyle,heightLinght,widthLinght(i)]"
                  v-tooltip.bottom="{ content: tdText, container: '#home-html', classes: 'bottom in'}">{{tdText}}</td>
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
  name: 'NewMoveTable',
  props: ['item', 'moving'],
  data () {
    return {
      tableMove: true,
      pageNum: 5,
      intervalTime: 4000,
      page1Data: [],
      page2Data: [],
      intervalId: 0,
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
    trStyle: function () {
      return {
        color: this.item.clr + ' !important',
        fontSize: this.item.fontSize + 'px !important'
      }
    },
    // theadTrStyle: function () {
    //   return {
    //     backgroundColor: this.item.hdBgClr + ' !important', // 表头背景色
    //     color: this.item.hdClr + ' !important',
    //     fontSize: this.item.hdfontSize + 'px !important'
    //   }
    // },
    thStyle: function () {
      return {
        textAlign: this.item.Alignment + '!important',
        padding: '8px'
      }
    },
    heightLinght: function () {
      return {
        height: this.item.heightLinght + 'px'
      }
    },
    widthArry: function () {
      let arr = this.item.LineSizeArry || []
      if(this.item.chartData.columns){
      this.item.chartData.columns.forEach((element, i) => {
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
    // tbodyTrStyle: function () {
    //   return {
    //     backgroundColor: this.item.bgClr + ' !important', // 表体背景色
    //     borderTop: this.item.bdpx + 'px solid ' + this.item.bdClr + ' !important'
    //   }
    // },
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
    'item.chartData': function (newV, oldV) {
      this.pageNum = Number(this.item.pageNum)
      if (JSON.stringify(oldV) === JSON.stringify(newV)) return
      if ((this.item.chartData.rows && this.item.chartData.rows.length < 1) || !this.item.chartData.rows) {
        this.tableEmpty = true
        this.page1Data = []
        this.page2Data = []
        this.intervalId && clearTimeout(this.intervalId)
        return
      } else {
        this.tableEmpty = false
      }
      // if (this.item.direction === 'left') {
      //   this.initLeftMove()
      // } else {
      //   this.initTopMove()
      // }
      this.initLeftMove()
    },
    'item.direction': function (val) {
      // if (val === 'left') {
      //   this.initLeftMove()
      // } else {
      //   this.initTopMove()
      // }
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
      this.item.LineSizeArry[this.item.chartData.columns.indexOf(this.item.AlarmField)] = newV
      document.querySelector('.DataChangeBtn').click()
    },
    'item.AlarmField': function (newV, oldV) {
      this.item.OneLineSize = this.item.LineSizeArry[this.item.chartData.columns.indexOf(newV)]
    }
  },
  methods: {
    // initTopMove () {
    //   if (this.intervalId) {
    //     clearTimeout(this.intervalId)
    //   }
    //   this.nowPage = 0
    //   if (this.item.chartData.rows.length > this.pageNum) {
    //     let totalPage = Math.floor(this.item.chartData.rows.length / this.pageNum)
    //     if (totalPage === this.item.chartData.rows.length / this.pageNum) {
    //       totalPage--
    //     }
    //     // let nowPage = 0
    //     if (!this.moving || this.moving === 'false') return
    //     this.intervalId = setInterval(() => {
    //       this.nowPage++
    //       if (this.nowPage > totalPage) {
    //         this.nowPage = 0
    //       }
    //     }, this.intervalTime)
    //   }
    // },
    warnStyle (ArryName, index) {
      if (this.item.AlarmField) {
        if (this.item.AlarmType === 'chart') {
          if (this.item.AlarmChart !== '' && JSON.stringify(this[ArryName][index][this.item.AlarmField]).indexOf(this.item.AlarmChart) >= 0) {
            console.log(11)
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
      if(this.item.chartData.rows){
      this.page1Data = this.item.chartData.rows.slice(0, this.pageNum)
      this.page2Data = this.item.chartData.rows.slice(this.pageNum, this.pageNum * (this.nowPage + 2))
      // 这里不用注释
      // if ($(this.$el).find('[title]').length > 0) {
      //   $(this.$el).find('[title]').tooltip('destroy')
      // }
      // if ($('#paintWrap').find('[title]').length > 0) {
      //   $('#paintWrap').find('[title]').tooltip('destroy')
      // }
      // this.$nextTick(() => {
      //   if ($('#home-html').length > 0) {
      //     titleShowFn('bottom', $('#paintWrap'), '#paintWrap')
      //   } else {
      //     titleShowFn('bottom', $(this.$el), this.$el)
      //   }
      // })
      // 这里不用注释
      if (this.item.chartData.rows.length > this.pageNum) {
        let totalPage = Math.floor(this.item.chartData.rows.length / this.pageNum)
        if (totalPage === this.item.chartData.rows.length / this.pageNum) {
          totalPage--
        }
        if (!this.moving || this.moving === 'false') return
        this.intervalId = setTimeout(function tableFn () {
          _this.tableMove = !_this.tableMove
          if (_this.tableMove) {
            _this.nowPage++
            if (_this.nowPage === totalPage) {
              _this.nowPage = -1
              _this.page1Data = _this.item.chartData.rows.slice(_this.pageNum * totalPage, _this.pageNum * (totalPage + 1))
            } else {
              _this.page1Data = _this.item.chartData.rows.slice(_this.pageNum * _this.nowPage, _this.pageNum * (_this.nowPage + 1))
            }
            _this.page2Data = _this.item.chartData.rows.slice(_this.pageNum * (_this.nowPage + 1), _this.pageNum * (_this.nowPage + 2))
          }
          // 这里不用注释
          // if ($(_this.$el).find('[title]').length > 0) {
          //   $(_this.$el).find('[title]').tooltip('destroy')
          // }
          // if ($('#paintWrap').find('[title]').length > 0) {
          //   $('#paintWrap').find('[title]').tooltip('destroy')
          // }
          // _this.$nextTick(() => {
          //   if ($('#home-html').length > 0) {
          //     titleShowFn('bottom', $('#paintWrap'), '#paintWrap')
          //   } else {
          //     titleShowFn('bottom', $(_this.$el), _this.$el)
          //   }
          // })
          // 这里不用注释
          clearTimeout(_this.intervalId)
          _this.intervalId = setTimeout(tableFn, _this.intervalTime)
        }, _this.intervalTime)
      }
      }
    }
    // initMove () {
    //   // 两个transition，vue动画实现的方式（可用于横向轮播,或者允许设置最后一页的数据不足时自动添加空数据）
    //   if (this.intervalId) {
    //     clearTimeout(this.intervalId)
    //   }
    //   if (this.item.chartData.rows.length > this.pageNum) {
    //     let totalPage = Math.floor(this.item.chartData.rows.length / this.pageNum)
    //     if (totalPage === this.item.chartData.rows.length / this.pageNum) {
    //       totalPage--
    //     }
    //     let nowPage = 0
    //     this.page1Data = this.item.chartData.rows.slice(0, this.pageNum)
    //     this.page2Data = this.item.chartData.rows.slice(this.pageNum, this.pageNum * (nowPage + 2))
    //     this.intervalId = setInterval(() => {
    //       this.tableMove = !this.tableMove
    //       if (this.tableMove) {
    //         nowPage++
    //         if (nowPage === totalPage) {
    //           nowPage = 0
    //         }
    //         this.page1Data = this.item.chartData.rows.slice(this.pageNum * nowPage, this.pageNum * (nowPage + 1))
    //         this.page2Data = this.item.chartData.rows.slice(this.pageNum * (nowPage + 1), this.pageNum * (nowPage + 2))
    //       }
    //     }, 3000)
    //   }
    // }
  },
  mounted: function () {
    if(this.item.chartData.columns){
    this.item.chartData.columns.forEach((element, i) => {
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
    // if (this.item.direction === 'left') {
    //   this.initLeftMove()
    // } else {
    //   this.initTopMove()
    // }
    this.initLeftMove()
    if (this.item.chartData.rows && this.item.chartData.rows.length < 1) {
      this.tableEmpty = true
    }
  },
  beforeDestroy: function () {
    if (this.intervalId) {
      clearTimeout(this.intervalId)
      this.intervalId = null
    }
    if ($(this.$el).find('[title]').length > 0) {
      $(this.$el).find('[title]').tooltip('destroy')
    }
    this.page1Data = null
    this.page2Data = null
  }
}
</script>
<style>
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
