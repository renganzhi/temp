<template>
  <div class="bootstrap-table home-table"
       :style="boxStyle">
    <div class="fixed-table-header"
         style="height: 36px;">
      <table class="table table-hover"
             style="table-layout: fixed;">
        <thead>
          <tr :style="[trStyle,theadTrStyle]">
            <th v-for="(title, index) in item.chartData.columns"
                :key="index"
                :title="title">{{title}}</th>
          </tr>
        </thead>
      </table>
    </div>
    <div class="fixed-table-body"
         id="tableMove"
         :style="{'max-height': pageNum * 36 + 'px'}"
         style="padding-bottom: 26px; overflow: hidden;">
      <transition name="table-fadeout">
        <table class="table table-hover"
               v-show="tableMove"
               :style="scrollStyle"
               style="table-layout: fixed; position: relative; float: left; transition: all 0.6s ease;">
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
  </div>
</template>
<script>
export default {
  name: 'moveTable',
  props: ['item'],
  data () {
    return {
      tableMove: true,
      direction: 'top',
      pageNum: 5,
      intervalTime: 4000,
      page1Data: [],
      page2Data: [],
      intervalId: 0,
      nowPage: 0
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
    'item.chartData': function () {
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
    initMove () {
      // 两个transition，vue动画实现的方式（可用于横向轮播）
      if (this.intervalId) {
        clearInterval(this.intervalId)
      }
      if (this.item.chartData.rows.length > this.pageNum) {
        let totalPage = Math.floor(this.item.chartData.rows.length / this.pageNum)
        if (totalPage === this.item.chartData.rows.length / this.pageNum) {
          totalPage--
        }
        console.log(totalPage)
        let nowPage = 0
        this.page1Data = this.item.chartData.rows.slice(0, this.pageNum)
        this.page2Data = this.item.chartData.rows.slice(this.pageNum, this.pageNum * (nowPage + 2))
        this.intervalId = setInterval(() => {
          this.tableMove = !this.tableMove
          if (this.tableMove) {
            nowPage++
            console.log(nowPage)
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
    // this.initMove()
    this.initTopMove()
    // titleShow('bottom', $(this.$el))
  },
  destroyed: function () {
    clearInterval(this.intervalId)
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
/* 向上移动 */
.table-fadeout-leave-active {
  /* transition: all 0.6s ease;
  transform: translateY(-100%); */
  animation: table-leave 1s;
  animation-timing-function: linear;
}
.table-fadein-enter-active {
  /* transition: all 0.6s ease;
  transform: translateY(-100%); */
  animation: table-leave 1s;
  animation-timing-function: linear;
}
/* 向下移动 */
/* .table-fadeout-leave-active {
  animation: table-leave 1s reverse;
}
.table-fadein-enter-active {
  animation: table-leave 1s reverse;
} */
@keyframes table-leave {
  from {
    transform: translateY(0);
  }
  to {
    transform: translateY(-100%);
  }
}
</style>
