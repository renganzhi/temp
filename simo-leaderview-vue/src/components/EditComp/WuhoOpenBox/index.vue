<template>
  <div class="WuhoOpenBox" :style="maoBlstyle" >
    <div class="BaiDu" >
      <div :class="BaiDuState? 'BaiDuOpenButton':'BaiDuCloseButton'" @click="BaiDuState = !BaiDuState"></div>
      <div class="BaiDuBody" v-show="BaiDuState">
       <div class="btnBox ronghe"><div class="titleName"><a target="_blank" href="https://10.29.118.15/smc/Login.aspx?ReturnUrl=%2F">视频调度</a></div></div>
       <div class="btnBox yuyin"><div class="titleName"><a target="_blank" href=""> 语音调度</a></div></div>
       <div class="btnBox duiwu" ><div class="titleName">队伍调度</div></div>
       <div class="btnBox baige" ><div class="titleName">白鸽</div></div>
       <div class="btnBox weiwen" ><div class="titleName">维稳</div></div>
      </div>
    </div>
   </div>
</template>
<script>
export default {
  name: "WuhoOpenBox",
  props: ["item"],
  data() {
    return {
      BaiDuState:false
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
.titleName{
  font-size: 29px;
  display: inline-block;
  position: absolute;
  bottom: -50px;
  width: 100%;
  text-align: center;
}
.BaiDuOpenButton{
  width: 200px;
  height: 200px;
  background: url('./imgs/baiduChecked.png');
  background-size: 100% 100%;
  cursor: pointer;
  position: absolute;
  top: 0px;
  z-index: 10;
}
.BaiDuCloseButton{
  width: 200px;
  height: 200px;
  background: url('./imgs/baidu.png');
  background-size: 100% 100%;
  cursor: pointer;
  position: absolute;
  top: 0px;
  z-index: 10;
}
.BaiDuBody{
  width: 200px;
  height: 1260px;
  backdrop-filter: blur(10px);
  border-radius: 100px;
  background-color: #476e9073;
  position: absolute;
  top: 0px;
  z-index: 5;
  padding: 160px 0 0 0;
}
.btnBox{
  height: 120px;
  width: 120px;
  margin: 80px 40px;
  position: relative;
  cursor: pointer;
  a{
    height: 200px;
    width: 130px;
    color: #cad6dd;
    display: flex;
    align-items: flex-end;
  }
}
.ronghe{
  background: url('./imgs/ronghe.png');
  background-size: 100% 100%;
}
.yuyin{
  background: url('./imgs/yuyin.png');
  background-size: 100% 100%;
}
.duiwu{
  background: url('./imgs/duiwu.png');
  background-size: 100% 100%;
}
.baige{
  background: url('./imgs/baige.png');
  background-size: 100% 100%;
}
.weiwen{
  background: url('./imgs/weiwen.png');
  background-size: 100% 100%;
}
</style>