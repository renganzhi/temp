<template>
  <div :class="isShanShuo ? 'WuhoYXHLPoint':'WuhoYXHL'" :style="BodyStyle" @click="PontSplitFun">

  </div>
</template>
<script>
export default {
  name: "WuhoYXHL",
  props: ["item"],
  data() {
    return {
      isShanShuo: false,
      nowPick:0,
      timeOut:null
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
  mounted() {
  },
  methods: {
    PontSplitFun(){
      if(this.timeOut){
        this.nowPick = 0
      }else{
        this.timeOut = setInterval(() => {
          if(this.nowPick < 10){
            this.nowPick = this.nowPick+1
            this.isShanShuo = !this.isShanShuo
          }else{
            clearInterval(this.timeOut)
            this.isShanShuo = false
            this.nowPick = 0
          }
        }, 1000);
      }
    }
  },
  beforeDestroy() {},
};
</script>
<style scoped lang="scss">
.WuhoYXHL{
    background-image: url(./yjcz.png);
    background-size: 100% 100%;
    cursor: pointer;
}
.WuhoYXHLPoint{
    display: none;
    cursor: pointer;
}
</style>
