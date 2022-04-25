<template>
  <div class="WuHolunboTab" :style="BodyStyle">
    <div class="FanPaiTab0" @click="getTableData(0)" v-if="NowData[0]">
      <NewDoubler :item='getliqiudfill(0)'></NewDoubler>
      {{NowData[0]['所属街道']}}
    </div>
    <div class="NoData" v-else>
        <div>
          暂无数据！
        </div>
    </div>
    <div class="leftBody">
      <div class="FanPaiTab FanPaiTab1" @click="getTableData(1)" v-if="NowData[1]">
        <NewDoubler :item='getliqiudfill(1)'></NewDoubler>
        {{NowData[1]['所属街道']}}
      </div>
      <div class="NoData" v-else>
        <div>
          暂无数据！
        </div>
      </div>
      <div class="FanPaiTab FanPaiTab2" @click="getTableData(2)" v-if="NowData[2]">
        <NewDoubler :item='getliqiudfill(2)'></NewDoubler>
        {{NowData[2]['所属街道']}}
      </div>
      <div class="NoData" v-else>
        <div>
          暂无数据！
        </div>
      </div>
      <div class="FanPaiTab FanPaiTab3" @click="getTableData(3)" v-if="NowData[3]">
        <NewDoubler :item='getliqiudfill(3)'></NewDoubler>
        {{NowData[3]['所属街道']}}
      </div>
      <div class="NoData" v-else>
        <div>
          暂无数据！
        </div>
      </div>
      <div class="FanPaiTab FanPaiTab4" @click="getTableData(4)" v-if="NowData[4]">
        <NewDoubler :item='getliqiudfill(4)'></NewDoubler>
        {{NowData[4]['所属街道']}}
      </div>
      <div class="NoData" v-else>
        <div>
          暂无数据！
        </div>
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
        'clr': '#15fbff',
        'width': this.item.width/3,
        'height': this.item.height/2,
        'ctLegendShow': 'false',
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
  display: flex;
  flex-wrap: wrap;
}
.FanPaiTab0{
  width: 33%;
  cursor: pointer;
  height: 100% !important;
  text-align: center;
  padding: 129px 0px;
  .v-doubler{
    height: 85%;
  }
}
.leftBody{
  height: 100%;
  display: flex;
  flex-wrap: wrap;
  width: 66%;
  .NoData{
    cursor: pointer;
    height: 50%;
    width: 50%;
    display: flex;
    justify-content: center;
    color: #8d8b8b;
    align-content: center;
    div{
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
  .FanPaiTab{
    cursor: pointer;
    height: 50%;
    width: 50%;
    text-align: center;
    padding: 30px 0px;
    .v-doubler{
      height: 85%;
    }
  }
}
.NoData{
  cursor: pointer;
  height: 50%;
  font-size: 28px !important;
  width: 33%;
}
</style>
