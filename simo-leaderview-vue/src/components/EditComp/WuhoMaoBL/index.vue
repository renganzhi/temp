<template>
  <div class="WuhoMaoBL" :style="maoBlstyle" ></div>
</template>
<script>
export default {
  name: "WuhoMaoBL",
  props: ["item"],
  data() {
    return {
    };
  },
  computed: {
    maoBlstyle () {
      return {
        height: this.item.height + 'px !important',
        width:this.item.width + 'px !important',
      }
    }
  },
};
</script>
<style lang="scss" scoped>
.WuhoMaoBL{
    backdrop-filter: blur(10px);
    background-color: rgba(255, 255, 255, 0);
}
</style>