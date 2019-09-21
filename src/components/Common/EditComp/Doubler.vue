<template>
  <div class="v-doubler">
    <div>
      <span class="autoSize"
            ref="hideNum"
            :style="numCardValue">8</span>
      <TurnOver v-for="(num,index) in numArr"
                :key="index"
                :nextNum="num"
                :numWidth="numWidth"
                :numHeight="numHeight"
                :style="numCardValue">

      </TurnOver>
    </div>
    <div v-show="showTitle"
         style="margin-top: 10px;color: #828bac;font-size: 12px;">{{item.chartData.name}}{{ comUnit}}</div>
  </div>
</template>
<script>
import TurnOver from './TurnOver'
export default {
  name: 'doubler', // 数字翻牌器
  components: { TurnOver },
  props: ['item'],
  data () {
    return {
      showOver: true,
      numHeight: 100,
      numWidth: 80
    }
  },
  computed: {
    showTitle: function () {
      return this.item.ctLegendShow === 'true'
    },
    comUnit: function () {
      var unit = this.item.chartData.unit
      return unit === '' ? '' : ('(' + unit + ')')
    },
    numArr: function () {
      if (!this.item.chartData.value && this.item.chartData.value !== 0) {
        return '--'
      }
      var str = this.item.chartData.value.toString().trim()
      return str.split('')
    },
    numCardValue: function () {
      return {
        padding: '6px 16px',
        fontSize: this.item.fontSize + 'px',
        fontFamily: 'asn !important',
        border: '1px solid ' + this.item.bdClr,
        borderRadius: '8px',
        backgroundColor: this.item.bgClr,
        marginRight: '2px',
        color: this.item.clr
      }
    },
    numCardOver: function () {
      return {
        display: 'inline-block',
        verticalAlign: 'top',
        textAlign: 'center',
        width: '16px',
        height: '16px',
        border: '1px solid ' + this.item.bdClr,
        lineHeight: '15px',
        color: this.item.clr,
        fontSize: '20px',
        backgroundColor: this.item.bgClr
      }
    }
  },
  watch: {
    'item.fontSize': function () {
      this.$nextTick(() => {
        this.numHeight = this.$refs.hideNum.getBoundingClientRect().height
        this.numWidth = this.$refs.hideNum.getBoundingClientRect().width
      })
    }
  },
  methods: {
    getAutoSize () {

    }
  },
  mounted () {
    this.numHeight = this.$refs.hideNum.getBoundingClientRect().height
    this.numWidth = this.$refs.hideNum.getBoundingClientRect().width
  },
  destoryed: function () {
  }
}
</script>
<style scoped>
.v-doubler {
  text-align: center;
  height: 100%;
  overflow: hidden;
  white-space: nowrap;
  position: relative;
}
.autoSize {
  position: absolute;
  top: 0px;
  left: 0px;
  opacity: 0;
}
</style>
