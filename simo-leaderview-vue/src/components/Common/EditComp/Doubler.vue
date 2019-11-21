<template>
  <div class="v-doubler">
    <div>
      <!-- <span class="autoSize"
            ref="hideNum"
            :style="numCardValue">8</span> -->
      <TurnOver v-for="(num,index) in numArr"
                :key="index"
                :nextNum="num"
                :numWidth="numWidth"
                :numHeight="numHeight"
                :bgClr="bgdColor"
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
      scaleX: 1,
      scaleY: 1,
      numHeight: 100,
      numWidth: 80,
      defConf: {
        'f12': [22, 23],
        'f13': [25, 24],
        'f14': [25, 25],
        'f16': [27, 28],
        'f18': [28.5, 31],
        'f20': [30, 34],
        'f28': [35, 45],
        'f36': [41, 57],
        'f48': [49, 74],
        'f72': [66, 108]
      }
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
        padding: '3px 8px',
        fontSize: this.item.fontSize + 'px',
        fontFamily: 'asn !important',
        border: this.item.bdClr ? '1px solid ' + this.item.bdClr : '',
        borderRadius: '8px',
        // backgroundColor: this.item.bgClr,
        marginRight: '6px',
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
    },
    bgdColor: function () {
      return this.item.bgClr
    }
  },
  watch: {
    'item.fontSize': function () {
      let key = 'f' + this.item.fontSize
      this.numWidth =this.defConf[key][0]
      this.numHeight = this.defConf[key][1]
      // this.getPageScale()
      // this.$nextTick(() => {
      //   this.numHeight = this.$refs.hideNum.getBoundingClientRect().height / this.scaleY
      //   this.numWidth = this.$refs.hideNum.getBoundingClientRect().width / this.scaleX
      // })
    }
  },
  methods: {
    getPageScale () {
      var _transform = $(this.$el).parents('.paint-bg').css('transform')
      if (_transform && _transform !== 'none') {
        var reg = /\((.*)\)$/
        var _temp = reg.exec(_transform)
        _temp = _temp[0]
        _temp = _temp.slice(1, _temp.length)
        _temp = _temp.split(',')
        this.scaleX = Number(_temp[0])
        this.scaleY = Number(_temp[3])
      }
    }
  },
  mounted () {
    let key = 'f' + this.item.fontSize
    this.numWidth =this.defConf[key][0]
    this.numHeight = this.defConf[key][1]
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
