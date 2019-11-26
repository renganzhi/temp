<template>
  <div class="bootstrap-table home-table"
       :style="boxStyle">
    <div class="fixed-table-header"
         style="height: 36px;">
      <table class="table table-hover"
             style="table-layout: fixed;">
        <thead :style="theadTrStyle">
          <tr :style="[trStyle,theadTrStyle]">
            <th v-for="(title, index) in item.chartData.columns"
                :key="index"
                :title="title">{{title}}</th>
          </tr>
        </thead>
        <tbody>
          <tr :style="trStyle"
              v-for="(tr, id) in item.chartData.rows"
              :key="id">
            <td v-for="(tdText, ind) in tr"
                :key="ind">{{tdText}}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div class="fixed-table-body"
         style="padding-bottom: 26px;">
      <table class="table table-hover"
             style="table-layout: fixed;">
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
    </div>
  </div>
</template>
<script>
export default {
  name: 'vtable',
  props: ['item'],
  data () {
    return {

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
    }
  },
  mounted: function () {
    titleShow('bottom', $(this.$el))
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
  background: none;
}
</style>
