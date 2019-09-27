<template>
  <div :style="boxStyle">
    <div class="v-charts-data-empty"
         v-if="!item.imgSrc">请上传图片</div>
    <img :src="baseUrl + item.imgSrc"
         v-else
         style="width: 100%;height:100%;" />
  </div>
</template>
<script>
import { gbs } from '@/config/settings'
export default {
  name: 'vimg',
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
        textAlign: 'center',
        maxWidth: this.item.width + 'px',
        height: this.item.height + 'px',
        overflow: 'hidden'
      }
    }
  },
  destoryed: function () {
  }
}
</script>
