<template>
    <div class="StreetMenu">
        <div class="openBtn" @click="showMenu = true" ><img src="./街道按钮.png" alt=""></div>
        <div class="mainMenu" v-show="showMenu">
            <div style="font-size:30px;padding:10px 5px;text-align:center">街镇城运平台</div>
            <div class="mainBox"  v-for="(element, index) in item.chartData" :key="index">
                <a :href="element" target="_blank">{{index}}</a>
            </div>
            <div class="closeBtn" @click="showMenu = false"><img style="width:140px;height:32px" src="./收起.png" alt=""></div>
        </div>
    </div>
</template>
<script>
export default {
  data () {
    return {
      menuData: {
        '党政机关': {
          '党政机关': false,
          '公安机关': false
        },
        '高层建筑': {
          '地标建筑': false
        },
        '文化景点': {
          '景点': false,
          '公园': false,
          '名木古树': false
        },
        '学校': {
          '幼儿园': false,
          '中小学': false,
          '高校': false,
          '其它院校': false
        },
        '医院': {
          '医院': false
        },
        '重点场所': {
          '宾馆、酒店': false,
          '网约房': false,
          '商场、超市、市场': false,
          '固废处理场所': false,
          '加油、加气站': false,
          '酒吧、KTV、会所': false,
          '网吧、影城': false,
          '危化存储处理站': false,
          '微型消防站': false,
          '洗浴、美容': false,
          '应急避难场所': false,
          '应急疏散场所': false,
          '中央驻川科研单位': false,
          '重点治安场所': false
        },
        '处置队伍': {
          '处置队伍': false
        }
      },
      showMenu: false,
      selectMainMenu: '',
      submenuList: []
    }
  },
  props: ['item'],
  computed: {
  },
  watch: {
  },
  methods: {
  }
}
</script>
<style scoped lang="scss">
.StreetMenu{
  display: flex;
  .openBtn{
  }
  .mainMenu{
    padding: 10px 0;
    background:#002b59;
    position: relative;
    .mainBox{
        height: 80px;
        color: #ACCFFE;
        font-size: 28px;
        padding: 20px;
        display: flex;
        background-repeat: no-repeat;
        background-size: 100% 100%;
        justify-content: space-between;
        align-items: center;
        text-align: center;
        vertical-align: middle;
      }
    .closeBtn{
      position:absolute;
      bottom: 0;
      left: 50%;
      transform: translate(-50%, 0);
    }
  }
}
</style>
