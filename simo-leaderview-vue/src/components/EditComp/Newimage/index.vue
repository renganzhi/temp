<template>
  <div :style="boxStyle">
    <div class="v-charts-data-empty"
         v-if="!getImg">请上传图片</div>
    <img :src="getImg"
         v-else
         :style="imgSctyle" />
  </div>
</template>
<script>
import { gbs } from '@/config/settings'
export default {
  name: 'Newimage',
  props: ['item'],
  data () {
    return {
      baseUrl: ''
    }
  },
  beforeMount () {
    var reg = /^\/api/
    if (!reg.test(this.item.imgSrc)) {
      this.baseUrl = gbs.host
      // this.baseUrl = gbs.host + '/leaderview'
    }
  },
  computed: {
    boxStyle: function () {
      return {
        maxWidth: this.item.width + 'px',
        height: this.item.height + 'px',
        overflow: 'hidden'
      }
    },
    getImg: function () {
      let url = ''
      if (this.item.imgSrc) {
        url = this.baseUrl + this.item.imgSrc
      } else if (this.item.chartData.value) {
        url = this.item.chartData.value
      }
      return url
    },
    imgSctyle: function () {
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
  destroyed: function () {
  }
}
</script>
