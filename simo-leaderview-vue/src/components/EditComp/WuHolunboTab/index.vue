<template>
  <div class="WuHolunboTab" :style="BodyStyle">

  </div>
</template>
<script>
export default {
  name: "WuHolunboTab",
  props: ["item"],
  data() {
    return {
      value1: 0,
    };
  },
  computed: {
    BodyStyle() {
      return {
        height: this.item.height + "px !important",
        width: this.item.width + "px !important",
      };
    },
  },
  watch: {},
  mounted() {},
  methods: {},
  beforeDestroy() {},
};
</script>
<style scoped lang="scss">

</style>
