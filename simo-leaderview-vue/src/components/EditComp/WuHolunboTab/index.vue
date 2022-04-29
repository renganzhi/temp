<template>
  <div class="WuHolunboTab" :style="BodyStyle">
    <img src="./top.png" alt="">
    <div class="FanPaiTab" @click="getTableData(index)" v-for="(item,index) in NowData" :key="index">
      <div class="FanPaiName">
         {{NowData[index]['所属街道']}}
      </div>
      <div class="FanPaiValue">
        <NewDoubler :item='getliqiudfill(index)'></NewDoubler>
      </div>
    </div>
  </div>
</template>
<script>
import NewDoubler from "../NewDoubler/index.vue";
export default {
  name: "WuHolunboTab",
  props: ["item"],
  components: {NewDoubler},
  data() {
    return {
      value1: 0,
      NowData:[],
      url:''
    };
  },
  computed: {
    BodyStyle() {
      return {
        height: this.item.height + "px !important",
        width: this.item.width + "px !important",
        fontSize:this.item.textfontSize + "px !important",
      };
    },
  },
  watch: {},
  mounted() {
    this.axios.get('leaderview/QZF/getWBZ3').then((res) => {
      this.NowData = res.obj.rows
      this.url = res.obj.url
    })
  },
  methods: {
    getliqiudfill(i) {
      return{
        'fontSize': this.item.fontSize,
        'bgClr': '#152b5f',
        'bdClr': '', // #0c527c
        'clr': this.item.textfontColor,
        'Zeroclr': this.item.Zeroclr,
        'width': this.item.width/5,
        'height': 52,
        'ctLegendShow': 'false',
        'minLength': 16,
        'chartData': {
          'name': '繁忙度',
          'unit': '%',
          'value': this.NowData[i]['数量']
        }
      }
    },
    getTableData(i){
      var _this = this
      let boxData = {
        title:'数据详情',
        data:this.NowData[i],
        dataUrl:this.url
      }
      this.$parent.$parent.ShowTableBox(boxData)
    }
  },
  beforeDestroy() {},
};
</script>
<style scoped lang="scss">
.WuHolunboTab{
  img{
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    z-index: -1;
    width: 100%;
  }
  padding: 10px 0;
}
.FanPaiTab{
  width: 100%;
  display: inline-block;
  cursor: pointer;
  display: flex;
  height: 20% !important;
  .FanPaiName{
    height: 100%;
    width: 30%;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 0 0 0 60px;
  }
  .FanPaiValue{
    height: 100%;
    width: 70%;
  }
  .v-doubler{
    padding: 14px 10px 10px 0;
  }
}
</style>
