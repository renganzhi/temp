<template>
    <div @click="ShowPot" :style="wrapStyle"></div>
</template>
<script>
export default {
  data: function () {
    return {
      mapType: false // 2d,true为3d
    }
  },
  props: ['item'],
  computed: {
    wrapStyle () {
      let style = {
        width: this.item.width + 'px !important',
        height: this.item.height + 'px !important'
      }
      if (this.$route.name === 'edit') {
        style.backgroundColor = 'rgba(255, 255, 0, 0.3)'
      }
      return style
    }
  },
  methods: {
    ShowPot () {
      this.mapType = !this.mapType
      this.bus.$emit('SwitchMap', this.mapType)
    }
  }
}
</script>
