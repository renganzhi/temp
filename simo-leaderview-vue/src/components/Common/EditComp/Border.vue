<template>
  <div v-if="item.borderType==='simple'"
       :style="boxStyle"></div>
  <img v-else
       :src="item.imgSrc"
       style="width: 100%; max-height: 100%" />
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
        background: this.item.barClrs ? 'linear-gradient(180deg, ' + this.item.barClrs[0] + ', ' + this.item.barClrs[1] + ')' : 'linear-gradient(0deg, ' + this.item.bgClr + ', ' + this.item.bgClr + ')',
        border: this.item.bdpx + 'px solid ' + this.item.bdClr
      }
    }
  },
  watch: {
    'item.imgSrc': function (newV) {
      // console.log(newV)
    },
    'item.borderType': function (newV) {
      if (newV === 'simple') {
        this.item.imgSrc = ''
      }
    }
  },
  destoryed: function () {
  }
}
</script>
