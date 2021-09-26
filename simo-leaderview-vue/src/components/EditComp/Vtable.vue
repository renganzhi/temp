<template>
  <div class="bootstrap-table home-table"
       :style="boxStyle">
    <div class="fixed-table-header"
        :style="heightLinght">
      <table class="table"
             style="table-layout: fixed;"
             :style="theadTrStyle">
        <thead :style="theadTrStyle">
          <tr :style="[trStyle,theadTrStyle]">
            <th v-for="(title, index) in item.chartData.columns"
                :style="[thStyle,heightLinght,widthLinght(index)]"
                :key="index">
                <span class="hoverTips" data-toggle='tooltip'
                    :title="title"><div :class="noworder[title] ? noworder[title]==='up'?'sortable desc':'sortable asc' :'sortable' " v-if="tableData[0] && !isNaN(tableData[0][title]*1)" @click="sortArry(title)">{{title}}</div> <div v-else>{{title}}</div></span></th>
          </tr>
        </thead>
        <!-- <tbody>
          <tr :style="[trStyle,tbodyTrStyle(id), warnStyle(index)]"
              v-for="(tr, id) in item.chartData.rows"
              :key="id">
            <td v-for="(tdText, ind) in tr"
                :key="ind">{{tdText}}</td>
          </tr>
        </tbody> -->
      </table>
    </div>
    <div class="fixed-table-body"
         style="padding-bottom: 26px;">
      <table class="table"
             style="table-layout: fixed;">
        <tbody>
          <tr
              v-for="(tr, id) in tableData"
              :key="id" :style="[trStyle,tbodyTrStyle(id), warnStyle(id)]">
            <td v-for="(tdText, ind, i) in tr"
                :style="[thStyle,heightLinght,widthLinght(i)]"
                :key="ind">
                <!-- template: '<div class=\'tooltip\' role=\'tooltip\'><div class=\'tooltip-arrow\'></div><div class=\'tooltip-inner\'></div></div>'  -->
              <span v-if="i === 0"
                  v-tooltip.bottom="{ content: tdText, container: '#home-html', classes: 'bottom in'}"
                  :style="{ 'color': alertColor(tdText, ind) }">{{tdText}}</span>
              <span
                v-tooltip.bottom="{ content: tdText, container: '#home-html', classes: 'bottom in'}"
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
// import { titleShowFn } from '#/js/public'
import _ from 'lodash'
export default {
  name: 'vtable',
  props: ['item'],
  data () {
    return {
      tableEmpty: false,
      noworder: {}
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
        backgroundImage: this.item.tableBack
          ? 'url(' + gbs.host + this.item.tableBack + ')'
          : '',
        backgroundSize: '100% 100%',
        border: this.item.bdpx + 'px solid ' + this.item.bdClr + ' !important'
      }
    },
    tableData: function () {
      return this.item.chartData.rows
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
    }
  },
  watch: {
    'item.chartData': function (newV, oldV) {
      if (JSON.stringify(newV) === JSON.stringify(oldV)) {
        return
      }
      if ((this.item.chartData.rows && this.item.chartData.rows.length < 1) || !this.item.chartData.rows) {
        this.tableEmpty = true
      } else {
        this.tableEmpty = false
      }
      // 这里不用注释
      // if ($('#home-html').length > 0) {
      //   if ($('#paintWrap').find('[title]').length > 0) {
      //     $('#paintWrap').find('[title]').tooltip('destroy')
      //   }
      //   titleShowFn('bottom', $('#paintWrap'), '#paintWrap')
      // } else {
      //   if ($(this.$el).find('[title]').length > 0) {
      //     $(this.$el).find('[title]').tooltip('destroy')
      //   }
      //   titleShowFn('bottom', $(this.$el), this.$el)
      // }
      // 这里不用注释
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
    sortArry (key) {
      if (this.noworder[key] === 'down') {
        this.tableData.sort(function (a, b) {
          if (a[key] > b[key]) {
            return -1
          } else {
            return 1
          }
        })
        this.noworder[key] = 'up'
      } else {
        this.tableData.sort(function (a, b) {
          if (a[key] < b[key]) {
            return -1
          } else {
            return 1
          }
        })
        this.noworder[key] = 'down'
      }
    },
    warnStyle (index) {
      if (this.item.AlarmField) {
        if (this.item.AlarmType === 'chart') {
          if (this.item.AlarmChart !== '' && JSON.stringify(this.item.chartData.rows[index][this.item.AlarmField]).indexOf(this.item.AlarmChart) >= 0) {
            return {
              'color': this.item.AlarmColor + ' !important'
            }
          }
        } else {
          if (this.item.AlarmNum !== '') {
            let error = false
            if (this.item.AlarmNumType === 'greater') {
              if (this.item.chartData.rows[index][this.item.AlarmField] * 1 > this.item.AlarmNum * 1) {
                error = true
              }
            } else if (this.item.AlarmNumType === 'equal') {
              if (this.item.chartData.rows[index][this.item.AlarmField] * 1 === this.item.AlarmNum * 1) {
                error = true
              }
            } else {
              if (this.item.chartData.rows[index][this.item.AlarmField] * 1 < this.item.AlarmNum * 1) {
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
    if(this.item.chartData.columns){
    this.item.chartData.columns.forEach((element, i) => {
      if (this.widthArry[i]) {

      } else {
        this.widthArry.push(86)
      }
    })
    this.item.LineSizeArry = this.widthArry
    if (this.item.chartData.rows && this.item.chartData.rows.length < 1) {
      this.tableEmpty = true
    }
    }
    // 这里不用注释
    // if ($('#home-html').length > 0) {
    //   titleShowFn('bottom', $('#paintWrap'), '#paintWrap')
    // } else {
    //   titleShowFn('bottom', $(this.$el), this.$el)
    // }
    // 这里不用注释
  },
  beforeDestroy: function () {
    if ($(this.$el).find('[title]').length > 0) {
      // $(this.$el).find('[title]').tooltip('destroy')
    }
    // this.$destroy(true)
  }
}
</script>
<style>
.home-table .table {
  background: transparent;
}
.home-table .desc{
  background-image:url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABMAAAATCAYAAAByUDbMAAAAZUlEQVQ4y2NgGAWjYBSggaqGu5FA/BOIv2PBIPFEUgxjB+IdQPwfC94HxLykus4GiD+hGfQOiB3J8SojEE9EM2wuSJzcsFMG4ttQgx4DsRalkZENxL+AuJQaMcsGxBOAmGvopk8AVz1sLZgg0bsAAAAASUVORK5CYII= ') !important
}
.home-table .asc{
  background-image:url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABMAAAATCAYAAAByUDbMAAAAZ0lEQVQ4y2NgGLKgquEuFxBPAGI2ahhWCsS/gDibUoO0gPgxEP8H4ttArEyuQYxAPBdqEAxPBImTY5gjEL9DM+wTENuQahAvEO9DMwiGdwAxOymGJQLxTyD+jgWDxCMZRsEoGAVoAADeemwtPcZI2wAAAABJRU5ErkJggg==') !important
}
.home-table .sortable {
  background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABMAAAATCAQAAADYWf5HAAAAkElEQVQoz7X QMQ5AQBCF4dWQSJxC5wwax1Cq1e7BAdxD5SL+Tq/QCM1oNiJidwox0355mXnG/DrEtIQ6azioNZQxI0ykPhTQIwhCR+BmBYtlK7kLJYwWCcJA9M4qdrZrd8pPjZWPtOqdRQy320YSV17OatFC4euts6z39GYMKRPCTKY9UnPQ6P+GtMRfGtPnBCiqhAeJPmkqAAAAAElFTkSuQmCC');
  cursor: pointer;
  background-position: right;
  background-repeat: no-repeat;
  padding-right: 20px;
  vertical-align: top;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
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
</style>
