<template>
  <textarea :style="textStyle"
            v-model="item.ctName"
            :disabled="dis"></textarea>
</template>
<script>
export default {
  name: 'vtextarea',
  props: ['item', 'disabled'],
  data () {
    return {

    }
  },
  methods: {
    getMessage (vtextarea) {
      vtextarea.$el.focus() // 双击穿透，使文本框获得焦点
    }
  },
  computed: {
    dis () {
      return !this.disabled
    },
    textStyle: function () {
      return {
        width: this.item.width + 'px !important',
        height: this.item.height + 'px !important',
        color: this.item.clr + ' !important',
        backgroundColor: this.item.bgClr + ' !important',
        border: this.item.bdpx + 'px solid ' + this.item.bdClr + ' !important',
        fontSize: this.item.fontSize + 'px !important'
      }
    }
  },
  destroyed: function () {
  }
}
</script>
<style scoped lang="scss">
html[data-theme="blackWhite"],
html[data-theme="blueWhite"] {
  textarea {
    background: transparent !important;
    background-color: transparent !important;
  }
}
</style>
