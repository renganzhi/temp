<template>
  <div class="WuhoPointBox" :style="maoBlstyle" >
    <div class="OpenPointBtn">
      <div class="SmallName">社治力量</div>
      <div v-for="(d,i) in dataArray" :key="i" :class="CheckedArry.indexOf(d) >= 0? 'CheckedBox':'NoChecked'" @click="ChangeState(d)">
        {{d}}
      </div>
      <div class="AddPonit" v-if="CheckedArry.indexOf('重点点位') >= 0" @click="AddZDDW()"></div>
      <div class="SmallName">工作重点</div>
      <div v-for="(d,i) in dataArray2" :key="i+'ss'" :class="CheckedArry.indexOf(d) >= 0? 'CheckedBox':'NoChecked'" @click="ChangeState(d)">
        {{d}} <div class="SanJiaoTab" v-if="d === '娱乐场所' || d === '锅庄舞场'" @click="ShowXQByUrl(d)"></div>
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
      dataArray:['天网','管控区','公安日常勤务','公安网格','网格区','重点点位','社区区划'],
      dataArray2:['涉藏商店','民宿旅馆','藏餐茶吧','娱乐场所','涉藏机构','小区院落','锅庄舞场'],
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
    },
    'item.size': function () {
      if (this.item.size === 'small') {
        document.getElementsByClassName('OpenPointBtn')[0].classList.add('small')
      } else {
        document.getElementsByClassName('OpenPointBtn')[0].classList.remove('small')
      }
    }
  },
  mounted () {
    window.changeCheckedArry = this.changeCheckedArry
    if (this.item.size === 'small') {
      document.getElementsByClassName('OpenPointBtn')[0].classList.add('small')
    } else {
      document.getElementsByClassName('OpenPointBtn')[0].classList.remove('small')
    }
  },
  methods: {
    AddZDDW(){
      if(window.addPointTrue){
          window.addPointTrue()
      }
    },
    changeCheckedArry(data){
      console.log(data)
      this.CheckedArry = data
    },
    ShowXQByUrl(data){
      this.$parent.$parent.ShowIfreamBoxFun(data,this.item.size)
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
  height: 1540px;
  width: 490px;
  padding: 20px 0 0 180px;
  background: url(./backbox.png);
  background-size: 100% 100%;
  position: relative;
}
.SmallName{
  font-size: 30px;
  color: #9b9b9b;
  left: -20px;
  position: relative;
}
.CheckedBox{
  width: 240px;
  margin: 0 0 28px 0px;
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
  height: 74px;
  background: url(./checkedbox.png);
}
.NoChecked{
  width: 240px;
  margin: 0 0 28px 0px;
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
  height: 74px;
  background: url(./nocheckedbox.png);
}
.AddPonit{
  position: absolute;
  height: 50px;
  cursor: pointer;
  width: 50px;
  background: url(./AddPonit.png);
  background-size: 100% 100%;
  right: 12px;
  top: 590px;
}

.small {
  height: 834px;
  width: 289px;
  padding: 20px 0 0 100px;
  font-size: 24px;
  background: url(./左侧.png);
  .NoChecked{
    width:150px;
    margin: 0 0 12px 0px;
    height: 40px;
    background-size: 100% 100%;
    font-size: 20px;
  }
  .SmallName{
  font-size: 25px;
  color: #9b9b9b;
  left: -5px;
  position: relative;
  }
  .CheckedBox{
    width:150px;
    margin: 0 0 12px 0px;
    height: 40px;
    background-size: 100% 100%;
    font-size: 20px;
  }
  .AddPonit{
    top: 325px;
    height: 27px;
    width: 27px;
    right: 6px;

  }
}
.SanJiaoTab{
  height: 30px;
  width: 30px;
  background: url(./icon.png);
  background-size: 100% 100%;
  position: absolute;
  left: 440px;
}
.small .SanJiaoTab{
  height: 30px;
  width: 30px;
  background: url(./icon.png);
  background-size: 100% 100%;
  position: absolute;
  left: 250px;
}
</style>