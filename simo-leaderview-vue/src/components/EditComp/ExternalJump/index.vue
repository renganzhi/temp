<template>
    <div @click="jump" :style="wrapStyle"></div>
</template>
<script>
export default {
  props: ['item'],
  computed: {
    wrapStyle () {
      let style = {
        width: this.item.width + 'px !important',
        height: this.item.height + 'px !important'
      }
      if (this.$route.name === 'edit' && !this.$parent.$parent.previewStatus) {
        style.backgroundColor = this.item.chartData.linkURL ? 'rgba(255, 255, 0, 0.3)' : 'rgba(255, 0, 0, 0.3)'
      }
      return style
    }
  },
  methods: {
    jump () {
      if (this.item.chartData.linkURL) {
        if (this.$route.name === 'HomePage' || this.$route.name === 'lookPage' || this.$route.name === 'popPage') {
          window.open(this.item.chartData.linkURL)
        }
      }
      if (this.item.chartData.streetName) {
        if (this.$route.name === 'HomePage' || this.$route.name === 'lookPage' || this.$route.name === 'popPage') {
          this.axios.get('/leaderview/ChengYun4/GetStreetUrl?param=' + this.item.chartData.streetName).then(res => {
            if (res.obj.linkURL) {
              window.open(res.obj.linkURL)
            }
          })
        }
      }
    }
  }
}
</script>
