<template>
  <div :style="boxStyle"></div>
</template>
<script>
export default {
  name: 'border',
  props: ['item'],
  data () {
    return {

    }
  },
  computed: {
    boxStyle: function () {
      return {
        width: this.item.width + 'px',
        height: this.item.height + 'px',
        backgroundColor: this.item.bgClr,
        border: this.item.bdpx + 'px solid ' + this.item.bdClr
      }
    }
  }
}
</script>
