<template>
  <div class="bootstrap-table home-table"
       :style="boxStyle">
    <div class="fixed-table-header"
         style="height: 36px;">
      <table class="table table-hover"
             style="table-layout: fixed;"
             :style="theadTrStyle">
        <thead :style="theadTrStyle">
          <tr :style="[trStyle,theadTrStyle]">
            <th v-for="(title, index) in item.chartData.columns"
                :key="index"><span data-toggle='tooltip'
                    title
                    :data-original-title="title">{{title}}</span></th>
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
            <td v-for="(tdText, ind, i) in tr"
                :key="ind">
              <span v-if="i === 0"
                    data-toggle='tooltip'
                    title
                    :data-original-title="tdText"
                    :style="{ 'color': alertColor(tdText, ind) }">{{tdText}}</span>
              <span data-toggle='tooltip'
                    title
                    :data-original-title="tdText"
                    v-else>{{tdText}}</span>
            </td>
          </tr>
        </tbody>
      </table>
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
import { mapGetters } from 'vuex'
import { gbs } from '@/config/settings'
export default {
  name: 'vtable',
  props: ['item'],
  data () {
    return {
      tableEmpty: false
    }
  },
  computed: {
    ...mapGetters([
      'alertInfo'
    ]),
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
  watch: {
    'item.chartData': function (newV, oldV) {
      if ((this.item.chartData.rows && this.item.chartData.rows.length < 1) || !this.item.chartData.rows) {
        this.tableEmpty = true
      } else {
        this.tableEmpty = false
      }
    }
  },
  methods: {
    alertColor: function (type, ind) {
      if (ind !== '状态') {
        return ''
      }
      if (this.alertInfo && this.alertInfo.length > 0) {
        let index = _.findIndex(this.alertInfo, function (o) {
          return o.name === type
        })
        if (index !== -1) {
          return this.alertInfo[index].color
        }
      }
      return ''
    }
  },
  mounted: function () {
    if (this.item.chartData.rows && this.item.chartData.rows.length < 1) {
      this.tableEmpty = true
    }
    if (!gbs.inDev) {
      titleShow('bottom', $(this.$el))
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
  background: none;
}
</style>
