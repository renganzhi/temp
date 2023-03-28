<template>
  <div class="WuHouEvents" :style="BodyStyle">
    <Timeline style="position:absolute;top:0;left:0;">
      <TimelineItem
        v-for="(event, i) in eventsList"
        :key="i"
        :color="iconColor"
      >
        <p class="dateline" :style="titleStyle">{{ event.sj_time }}</p>
        <div
          v-for="(cont, n) in event.items.rows"
          :key="n"
          class="content"
          :style="contentBoxStyle"
        >
          <p :style="contentTitleStyle">{{event.items.columns[0]}}:{{ cont[event.items.columns[0]] }}</p>
          <div :style="contentStyle">
            <p v-for="(val, ind) in event.items.columns" v-show="ind > 0" :key="val + ind">
              {{val}}:{{cont[val]}}
            </p>
          </div>
        </div>
      </TimelineItem>
    </Timeline>
  </div>
</template>
<script>
export default {
  name: 'WuHouEvents',
  props: ['item'],
  data () {
    return {
      // eventsList: this.item.chartData.data || []
    }
  },
  computed: {
    BodyStyle () {
      return {
        height: this.item.height + 'px !important',
        width: this.item.width + 'px !important'
      }
    },
    iconColor () {
      return this.item.iconColor
    },
    titleStyle () {
      return {
        color: this.item.titleColor,
        fontSize: this.item.titleFontSize + 'px',
        marginBottom: this.item.titleBottm + 'px',
        left: this.item.dateLeft + 'px',
        top: this.item.dateTop + 'px'
      }
    },
    contentBoxStyle () {
      return {
        border: '1px solid ' + this.item.contBorderColor,
        padding: this.item.contPadding + 'px',
        borderRadius: this.item.contBorderRdius + 'px'
      }
    },
    contentTitleStyle () {
      return {
        color: this.item.contTitleColor,
        fontSize: this.item.contTitleSize + 'px',
        overflow: 'hidden',
        'white-space': 'nowrap',
        'text-overflow': 'ellipsis'
      }
    },
    contentStyle () {
      return {
        // display: 'flex',
        // 'justify-content': 'space-between',
        color: this.item.contColor,
        fontSize: this.item.contSize + 'px'
      }
    },
    eventsList () {
      let dataArr = []
      this.item.chartData.data.forEach(element => {
        let dateKey = ''
        element.items.columns.forEach(e => {
          if (e.indexOf('时间') >= 0) {
            dateKey = e
          }
        })
        element.items.rows.forEach(el => {
          let d = {
            sj_time: el[dateKey],
            items: {
              columns: element.items.columns,
              rows: [el]
            }
          }
          dataArr.push(d)
        })
      })
      return dataArr
    }
  },
  watch: {
    // 'item.chartData': {
    //   handler: function () {
    //     this.eventsList = this.item.chartData.data || []
    //   },
    //   deep: true
    // }
  },
  methods: {
    showDetails (data) {
      if (this.$parent.$parent.ShowTanKuangBox) {
        let dataArray = {
          title: '数据详情',
          data: data
        }
        this.$parent.$parent.ShowTanKuangBox(dataArray)
      } else if (this.$parent.ShowTanKuangBox) {
        let dataArray = {
          title: '数据详情',
          data: data
        }
        this.$parent.ShowTanKuangBox(dataArray)
      }
    }
  }
}
</script>
<style scoped lang="scss">
.WuHouEvents {
  position: relative;
  overflow: visible;
  .dateline{
    position: absolute;
    top: 0px;
    left: 0px;
  }
}

.content + .content {
    margin-top: 10px;
}

::v-deep {
  .ivu-timeline-item-head {
    left: 50px;
    width: 14px;
    height: 14px;
  }
  .ivu-timeline-item-tail {
    border-left: 2px solid #e8eaec;
    left: 56px;
  }
}
</style>
