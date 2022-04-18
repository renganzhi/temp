<template>
  <div class="WuhoPointBox" :style="maoBlstyle" >
    <div class="OpenPointBtn">
      <div v-for="(d,i) in dataArray" :key="i" :class="CheckedArry.indexOf(d) >= 0? 'CheckedBox':'NoChecked'" @click="ChangeState(d)">
        {{d}}
      </div>
    </div>
   </div>
</template>
<script>
export default {
  name: "WuhoPointBox",
  props: ["item"],
  data() {
    return {
      BaiDuState:false,
      dataArray:['天网','管控区','常规地点','封控','应急','公安网格','网格员','重点区域','社区区划'],
      CheckedArry:[]
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
  watch: {
    CheckedArry:function(){
      var _this = this
      window.changeSZChecked(_this.CheckedArry)
    }
  },
  mounted () {
    window.changeCheckedArry = this.changeCheckedArry
  },
  methods: {
    changeCheckedArry(data){
      console.log(data)
      this.CheckedArry = data
    },
    ChangeState(d){
      if(this.CheckedArry.indexOf(d)>=0){
        this.CheckedArry.splice(this.CheckedArry.indexOf(d),1)
      }else{
        this.CheckedArry.push(d)
      }
    }
  }
};
</script>
<style lang="scss" scoped>

.OpenPointBtn{
  height: 1136px;
  width: 490px;
  padding: 30px 0 0 180px;
  background: url(./backbox.png);
}
.CheckedBox{
  width: 240px;
  margin: 0 0 45px 0px;
  cursor: pointer;
  font-family: 'PangMenZhengDao';
  font-style: normal;
  font-weight: 400;
  font-size: 36px;
  line-height: 41px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #DCFBFF;
  text-shadow: 0px 4px 10px rgba(31, 174, 255, 0.72);
  height: 80px;
  background: url(./checkedbox.png);
}
.NoChecked{
  width: 240px;
  margin: 0 0 45px 0px;
  cursor: pointer;
  font-family: 'PangMenZhengDao';
  font-style: normal;
  font-weight: 400;
  font-size: 36px;
  line-height: 41px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #97C4D8;
  height: 80px;
  background: url(./nocheckedbox.png);
}
</style>