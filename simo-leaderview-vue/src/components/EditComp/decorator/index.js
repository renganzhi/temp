import Vue from 'vue'
import { gbs } from '@/config/settings'
import GreenEarth from '@/../src/components/EditComp/CoolEarth/GreenEarth'
import VioletEarth from '@/../src/components/EditComp/CoolEarth/VioletEarth'
import BlueEarth from '@/../src/components/EditComp/CoolEarth/BlueEarth'

export default Vue.component('decorator', {
  template: `
  <GreenEarth v-if="item.imgSrc === 'GreenEarth'"  :item=earthStyle></GreenEarth>
  <VioletEarth v-else-if="item.imgSrc === 'VioletEarth'" :item=earthStyle></VioletEarth>
  <BlueEarth v-else-if="item.imgSrc === 'BlueEarth'" :item=earthStyle></BlueEarth>
  <img v-else :style="wrapStyle" 
        :src="baseUrl + item.imgSrc"     
    ></img>

  
    `,
  props: ['item'],
  components: {GreenEarth, VioletEarth, BlueEarth},
  data () {
    return {
      baseUrl: gbs.host
    }
  },
  computed: {
    wrapStyle () {
      let style = {
        width: this.item.width + 'px !important',
        height: this.item.height + 'px !important'
      }
      if (this.item.showType && this.item.showType === '1') {
        style = {
          maxWidth: '100%',
          maxHeight: '100%'
        }
      }
      return style
    },
    earthStyle () {
      let style = {
        width: this.item.width,
        height: this.item.height,
        showType: this.item.showType
      }
      if (this.item.showType && this.item.showType === '1') {
        style = {
          width: this.item.width > this.item.height ? this.item.height : this.item.width,
          height: this.item.width > this.item.height ? this.item.height : this.item.width,
          showType: this.item.showType
        }
      }
      return style
    }
  }
})
