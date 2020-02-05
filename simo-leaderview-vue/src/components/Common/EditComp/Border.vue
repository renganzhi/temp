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
  name: 'border',
  props: ['item'],
  data () {
    return {
      baseUrl: gbs.host,
      typeCard: true // 卡片，标题栏
    }
  },
  computed: {
    imgStyle: function () {
      return {
        width: '100%',
        height: this.typeCard ? '100%' : 'auto',
        maxHeight: this.typeCard ? '' : '100%'
      }
    },
    boxStyle: function () {
      return {
        width: this.item.width + 'px',
        height: this.item.height + 'px',
        borderRadius: this.item.radius + 'px',
        background: this.item.barClrs ? 'linear-gradient(180deg, ' + this.item.barClrs[0] + ', ' + this.item.barClrs[1] + ')' : 'linear-gradient(0deg, ' + this.item.bgClr + ', ' + this.item.bgClr + ')',
        border: this.item.bdpx + 'px solid ' + this.item.bdClr
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
    'item.borderType': function (newV) {
      if (newV === 'simple') {
        this.item.imgSrc = ''
      }
    }
  },
  mounted: function () {
    var reg = /.?title.?/
    if (reg.test(this.item.imgSrc)) {
      this.typeCard = false
    }
  },
  destroyed: function () {
  }
}
</script>
