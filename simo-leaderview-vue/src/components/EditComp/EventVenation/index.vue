<template>
  <div class="WuHouEvents" :style="BodyStyle">
    <Timeline style="position:absolute;top:0;left:0;">
      <TimelineItem
        v-for="(event, i) in eventsList"
        :key="i"
        :color="iconColor"
      >
        <p :style="titleStyle">{{ event.sj_time }}</p>
        <div
          v-for="(cont, n) in event.items.rows"
          :key="n"
          class="content"
          :style="contentBoxStyle"
          @click="showDetails(cont)"
        >
          <p :style="contentTitleStyle">{{ cont['处置单位'] || cont.info_title }}</p>
          <div :style="contentStyle">
            <span>处置人：{{ cont['处置人'] || cont.info_source_name }}</span>
            <span>{{ cont['操作时间'] || cont.info_time }}</span>
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
      eventsList: this.item.chartData.data || []
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
        marginBottom: this.item.titleBottm + 'px'
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
        display: 'flex',
        'justify-content': 'space-between',
        color: this.item.contColor,
        fontSize: this.item.contSize + 'px'
      }
    }
  },
  watch: {
    'item.chartData': {
      handler: function () {
        this.eventsList = this.item.chartData.data || []
      },
      deep: true
    }
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
