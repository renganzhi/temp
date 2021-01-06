import Vue from 'vue'

export default Vue.component('hotspot', {
    template: `<div :style="wrapStyle"></div>`,
    props: ['item'],
    computed: {
        wrapStyle () {
            let style = {
                width: this.item.width + 'px !important',
                height: this.item.height + 'px !important',
            }
            // console.log(this.$parent.$parent._name == '<PreView>');
            if (this.$route.name == 'edit' && this.$parent.$parent._name != '<PreView>' ) {
                style.backgroundColor = this.item.linkId ? 'rgba(0, 255, 0, 0.3)' : 'rgba(255, 0, 0, 0.3)'
              }
              return style
        } 
    }
})