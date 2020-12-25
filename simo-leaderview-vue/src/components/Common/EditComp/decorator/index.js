import Vue from 'vue'
import { gbs } from '@/config/settings'

export default Vue.component('decorator', {
    template: `<img :style="wrapStyle" 
        :src="baseUrl + item.imgSrc"
    ></img>`,
    props: ['item'],
    data () {
        return {
            baseUrl: gbs.host,
        };
    },
    computed: {
        wrapStyle () {
            let style = {
                width: this.item.width + 'px !important',
                height: this.item.height + 'px !important',
            }
            if (this.item.showType && this.item.showType === '1') {
                style = {
                    maxWidth: '100%',
                    maxHeight: '100%'
                  }
            }
            return style
        } 
    }
})