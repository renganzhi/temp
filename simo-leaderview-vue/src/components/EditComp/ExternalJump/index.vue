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
        style.backgroundColor = this.item.chartData.linkURL ? 'rgba(0, 255, 0, 0.3)' : 'rgba(255, 0, 0, 0.3)'
      }
      return style
    }
  },
  methods: {
    jump () {
      if (this.item.chartData.linkURL) {
        window.open(this.item.chartData.linkURL)
      }
    }
  }
}
</script>
