<template>
    <div @click="ShowPot" :style="wrapStyle"></div>
</template>
<script>
export default {
  data: function () {
    return {
      ifShow: false
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
      if (this.$route.name !== 'edit') {
        this.ifShow = !this.ifShow
        if (this.ifShow) {
          this.axios.get('/leaderview/ChengYun4/GetDTDD1').then(res => {
            if (res.success && res.obj.rows) {
              this.bus.$emit('LLZYDot', res.obj.rows)
            }
          })
        } else {
          this.bus.$emit('LLZYDot', [])
        }
      }
    }
  }
}
</script>
