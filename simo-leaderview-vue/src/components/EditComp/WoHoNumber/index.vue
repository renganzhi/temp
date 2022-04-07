<template>
  <div class="WoHoNumber" :style="BodyStyle">
    <!-- <Carousel
      autoplay
      v-model="value1"
      :style="BodyStyle"
      :autoplay-speed="3000"
      :arrow="'never'"
      loop
      :dots="'none'"
      :radius-dot="false"
    > -->
      <!-- <CarouselItem
        v-for="(d, i) in Math.ceil(item.chartData.dataArray.length / 3)"
        :style="BodyStyle"
        :key="i"
      > -->
        <div class="ValueBody">
            <div class="valueBox" v-for="(data, index) in 3" :key="index">
                <div
                    v-if="item.chartData.dataArray[index]"
                    class="OneDataBox"
                >
                    <div class="LeftImg">
                        {{ item.chartData.dataArray[ index].name.split('')[0] }}
                    </div>
                    <div class="RightValue">
                        <div class="TopValue">                       
                            {{ item.chartData.dataArray[index].val }}
                            <div class="UnitData">
                                {{ item.chartData.dataArray[index].unit }}
                            </div>
                        </div>
                        <div class="BottonValue">
                            {{ item.chartData.dataArray[index].name }}
                        </div>
                    </div>
                </div>
            </div>
        </div>
      <!-- </CarouselItem> -->
    <!-- </Carousel> -->
  </div>
</template>
<script>
export default {
  name: "WoHoNumber",
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
.ValueBody{
    display: flex;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
    width: 100%;
    height: 100%;
    .valueBox{
        width: 33%;
        height: 100%;
        padding: 20px;
        .OneDataBox{
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            .LeftImg{
                width: 116px;
                height: 120px;
                background: url(./backImg.png);
                font-size: 30px;
                color: #11cae4;
                font-weight: 900;
                display: flex;
                justify-content: center;
                align-items: center;
            }
            .TopValue{
                display: flex;
                font-size: 52px;
                color: #15abff;
                .UnitData{
                    color: #6c8aa5;
                    font-size: 26px;
                    display: flex;
                    align-items: flex-end;
                    padding-bottom: 10px;
                }
            }
            .BottonValue{
                font-size: 32px;
                color: #cce7ff;
            }
        }
    }
}
</style>
