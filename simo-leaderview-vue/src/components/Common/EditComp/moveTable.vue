<template>
  <div class="bootstrap-table home-table"
       :style="boxStyle">
    <div class="fixed-table-header"
         style="height: 36px;">
      <table class="table table-hover"
             :style="theadTrStyle"
             style="table-layout: fixed;">
        <thead :style="theadTrStyle">
          <tr :style="[trStyle,theadTrStyle]">
            <th v-for="(title, index) in item.chartData.columns"
                :key="index"
                :title="title">{{title}}</th>
          </tr>
        </thead>
      </table>
    </div>
    <div class="fixed-table-body"
         v-if="item.direction === 'top'"
         :style="{'max-height': pageNum * 36 + 'px'}"
         style="padding-bottom: 26px; overflow: hidden;">
      <transition>
        <table class="table table-hover"
               :style="scrollStyle"
               style="table-layout: fixed; transition: all 0.6s ease;">
          <tbody>
            <tr :style="[trStyle,tbodyTrStyle]"
                v-for="(tr, id) in item.chartData.rows"
                :key="id">
              <td v-for="(tdText, ind) in tr"
                  :key="ind"
                  :title="tdText">{{tdText}}</td>
            </tr>
          </tbody>
        </table>
      </transition>
      <!-- <transition name="table-fadein">
        <table class="table table-hover"
               v-show="!tableMove"
               style="table-layout: fixed; position: relative; float: left;">
          <tbody>
            <tr :style="[trStyle,tbodyTrStyle]"
                v-for="(tr, id) in page2Data"
                :key="id">
              <td v-for="(tdText, ind) in tr"
                  :key="ind"
                  :title="tdText">{{tdText}}</td>
            </tr>
          </tbody>
        </table>
      </transition> -->
    </div>
    <div class="fixed-table-body"
         v-else
         :style="{'max-height': pageNum * 36 + 'px'}"
         style="padding-bottom: 26px; overflow: hidden; position: relative;">
      <transition name="table-fadeout">
        <table class="table table-hover"
               v-show="tableMove"
               style="table-layout: fixed; position: absolute; top:0px;">
          <tbody>
            <tr :style="[trStyle,tbodyTrStyle]"
                v-for="(tr, id) in page1Data"
                :key="id">
              <td v-for="(tdText, ind) in tr"
                  :key="ind"
                  :title="tdText">{{tdText}}</td>
            </tr>
          </tbody>
        </table>
      </transition>
      <transition name="table-fadein">
        <table class="table table-hover"
               v-show="!tableMove"
               style="table-layout: fixed; position: absolute; top:0px;">
          <tbody>
            <tr :style="[trStyle,tbodyTrStyle]"
                v-for="(tr, id) in page2Data"
                :key="id">
              <td v-for="(tdText, ind) in tr"
                  :key="ind"
                  :title="tdText">{{tdText}}</td>
            </tr>
          </tbody>
        </table>
      </transition>
    </div>
    <div class="v-charts-data-empty"
         v-if="tableEmpty"
         style="width: 100%; text-align: center; font-size: 12px;">
      <div>
        <p>抱歉，没有数据可供展示...</p>
      </div>
    </div>
  </div>
</template>
<script>
import { gbs } from '@/config/settings'
export default {
  name: 'moveTable',
  props: ['item'],
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
    boxStyle: function () {
      return {
        width: this.item.width + 'px !important',
        height: this.item.height + 'px !important',
        tableLayout: 'fixed',
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
    theadTrStyle: function () {
      return {
        backgroundColor: this.item.hdBgClr + ' !important' // 表头背景色
      }
    },
    tbodyTrStyle: function () {
      return {
        backgroundColor: this.item.bgClr + ' !important' // 表体背景色
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
    'item.chartData': function (newV, oldV) {
      this.pageNum = Number(this.item.pageNum)
      if (JSON.stringify(oldV) === JSON.stringify(newV)) return
      if ((this.item.chartData.rows && this.item.chartData.rows.length < 1) || !this.item.chartData.rows) {
        this.tableEmpty = true
        this.intervalId && clearInterval(this.intervalId)
        return
      } else {
        this.tableEmpty = false
      }
      if (this.item.direction === 'left') {
        this.initLeftMove()
      } else {
        this.initTopMove()
      }
    },
    'item.direction': function (val) {
      if (val === 'left') {
        this.initLeftMove()
      } else {
        this.initTopMove()
      }
      // this.$nextTick(() => {
      //   this.initMove()
      // })
    },
    'item.speed': function (newV) {
      if (newV === '1') {
        this.intervalTime = 2000
      } else if (newV === '2') {
        this.intervalTime = 4000
      } else {
        this.intervalTime = 6000
      }
      this.initTopMove()
    }
  },
  methods: {
    initTopMove () {
      if (this.intervalId) {
        clearInterval(this.intervalId)
      }
      this.nowPage = 0
      if (this.item.chartData.rows.length > this.pageNum) {
        let totalPage = Math.floor(this.item.chartData.rows.length / this.pageNum)
        if (totalPage === this.item.chartData.rows.length / this.pageNum) {
          totalPage--
        }
        // let nowPage = 0
        this.intervalId = setInterval(() => {
          this.nowPage++
          if (this.nowPage > totalPage) {
            this.nowPage = 0
          }
        }, this.intervalTime)
      }
    },
    initLeftMove () {
      // 两个transition，vue动画实现的方式（可用于横向轮播,或者允许设置最后一页的数据不足时自动添加空数据）
      if (this.intervalId) {
        clearInterval(this.intervalId)
      }
      this.nowPage = 0
      this.page1Data = this.item.chartData.rows.slice(0, this.pageNum)
      this.page2Data = this.item.chartData.rows.slice(this.pageNum, this.pageNum * (this.nowPage + 2))
      if (this.item.chartData.rows.length > this.pageNum) {
        let totalPage = Math.floor(this.item.chartData.rows.length / this.pageNum)
        if (totalPage === this.item.chartData.rows.length / this.pageNum) {
          totalPage--
        }
        this.intervalId = setInterval(() => {
          this.tableMove = !this.tableMove
          if (this.tableMove) {
            this.nowPage++
            if (this.nowPage === totalPage) {
              this.nowPage = 0
            }
            this.page1Data = this.item.chartData.rows.slice(this.pageNum * this.nowPage, this.pageNum * (this.nowPage + 1))
            this.page2Data = this.item.chartData.rows.slice(this.pageNum * (this.nowPage + 1), this.pageNum * (this.nowPage + 2))
          }
        }, 3000)
      }
    },
    initMove () {
      // 两个transition，vue动画实现的方式（可用于横向轮播,或者允许设置最后一页的数据不足时自动添加空数据）
      if (this.intervalId) {
        clearInterval(this.intervalId)
      }
      if (this.item.chartData.rows.length > this.pageNum) {
        let totalPage = Math.floor(this.item.chartData.rows.length / this.pageNum)
        if (totalPage === this.item.chartData.rows.length / this.pageNum) {
          totalPage--
        }
        let nowPage = 0
        this.page1Data = this.item.chartData.rows.slice(0, this.pageNum)
        this.page2Data = this.item.chartData.rows.slice(this.pageNum, this.pageNum * (nowPage + 2))
        this.intervalId = setInterval(() => {
          this.tableMove = !this.tableMove
          if (this.tableMove) {
            nowPage++
            if (nowPage === totalPage) {
              nowPage = 0
            }
            this.page1Data = this.item.chartData.rows.slice(this.pageNum * nowPage, this.pageNum * (nowPage + 1))
            this.page2Data = this.item.chartData.rows.slice(this.pageNum * (nowPage + 1), this.pageNum * (nowPage + 2))
          }
        }, 3000)
      }
    }
  },
  mounted: function () {
    this.pageNum = Number(this.item.pageNum)
    if (this.item.speed === '3') {
      this.intervalTime = 6000
    } else if (this.item.speed === '1') {
      this.intervalTime = 2000
    } else {
      this.intervalTime = 4000
    }
    if (this.item.direction === 'left') {
      this.initLeftMove()
    } else {
      this.initTopMove()
    }
    if (this.item.chartData.rows && this.item.chartData.rows.length < 1) {
      this.tableEmpty = true
    }
    if (!gbs.inDev) {
      titleShow('bottom', $(this.$el))
    }
  },
  beforeDestroy: function () {
    if (this.intervalId) {
      clearInterval(this.intervalId)
    }
  },
  destroyed: function () {
    if ($(this.$el).find('.tooltip').length > 0) {
      $(this.$el).find('[title]').tooltip('destroy')
    }
    // this.$destroy(true)
  }
}
</script>
<style>
.home-table .table tr,
.home-table .table td,
.home-table .table th {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
/* 向右移动 */
/* .table-fadeout-leave {
  transform: translateX(0);
}
.table-fadeout-leave-active {
  transition: all 0.6s ease;
}
.table-fadeout-leave-to {
  transform: translateX(100%);
}
.table-fadein-enter {
  transform: translateX(-100%);
}
.table-fadein-enter-active {
  transition: all 0.6s ease;
}
.table-fadein-enter-to {
  transform: translateX(0);
} */
/* 向上移动 */
/* .table-fadeout-leave-active {
  transition: all 0.6s ease;
  transform: translateY(-100%);
  animation: table-leave 1s;
  animation-timing-function: linear;
}
.table-fadein-enter-active {
  transition: all 0.6s ease;
  transform: translateY(-100%);
  animation: table-leave 1s;
  animation-timing-function: linear;
} */
/* 向下移动 */
/* .table-fadeout-leave-active {
  animation: table-leave 1s reverse;
}
.table-fadein-enter-active {
  animation: table-leave 1s reverse;
} */
.table-fadeout-leave-active {
  animation: table-left-leave 1s;
}
.table-fadein-enter-active {
  animation: table-left-in 1s;
}
@keyframes table-leave {
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(-100%);
  }
}
@keyframes table-left-leave {
  from {
    transform: translateX(0);
  }
  to {
    transform: translateX(100%);
  }
}
@keyframes table-left-in {
  from {
    transform: translateX(-100%);
  }
  to {
    transform: translateX(0);
  }
}
</style>
