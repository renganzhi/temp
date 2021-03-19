<template>
  <img v-if="item.borderType==='stable'"
       :src="baseUrl + item.imgSrc"
       :style="imgStyle" />
  <div v-else
       :style="boxStyle"></div>
</template>
<script>
import { gbs } from '@/config/settings'
export default {
  name: 'NewBorder',
  props: ['item'],
  data () {
    return {
      baseUrl: gbs.host,
      typeCard: true, // 卡片，标题栏
      boxStyle: ''
    }
  },
  computed: {
    imgStyle: function () {
      if (this.item.showType && this.item.showType === '1') {
        return {
          maxWidth: '100%',
          maxHeight: '100%'
        }
      }
      return {
        width: '100%',
        height: '100%'
      }
    }
  },
  watch: {
    'item.imgSrc': function (newV) {
      var reg = /.?title.?/
      if (reg.test(this.item.imgSrc)) {
        this.typeCard = false
      } else {
        this.typeCard = true
      }
    },
    'item': {
      handler (newVal, oldVal) {
        this.getStyle()
      },
      deep: true
    }
  },
  mounted: function () {
    var reg = /.?title.?/
    if (reg.test(this.item.imgSrc)) {
      this.typeCard = false
    }
    this.getStyle()
  },
  methods: {
    getStyle () {
      if (this.item.borderType === 'simple') {
        this.item.imgSrc = ''
      }
      this.boxStyle = {
        width: this.item.width + 'px',
        height: this.item.height + 'px',
        borderRadius: this.item.radius + 'px',
        // background: this.item.barClrs ? 'linear-gradient(180deg, ' + this.item.barClrs[0] + ', ' + this.item.barClrs[1] + ')' : 'linear-gradient(0deg, ' + this.item.bgClr + ', ' + this.item.bgClr + ')',
        background: this.item.colorful === 'true' ? `linear-gradient(${this.item.directionLinear}deg,${this.item.barClrs[0]},${this.item.barClrs[1]})` : this.item.bgClr,
        border: this.item.bdpx + 'px solid ' + this.item.bdClr
      }
    }
  },
  destroyed: function () {
  }
}
</script>
