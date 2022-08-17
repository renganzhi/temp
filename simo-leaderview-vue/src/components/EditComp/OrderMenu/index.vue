<template>
    <div class="OrderMenu">
        <div class="mainMenu">
            <div class="mainBox" :style="ifCheck(index)" @click="showSubManu(index)" v-for="(element, index) in menuData" :key="index">
              <div>
                <img :src="getImg('./' + index)" style="width:30px" alt="">
                <span>{{index}}</span>
              </div>
              <div>
                <span style="color:#318ec7">{{selectLength(element)}}/{{Object.keys(element).length}}</span>
                <img src="./more.png" style="width:24px" alt="">
              </div>
            </div>
        </div>
        <div class="subMenu" v-show="selectMainMenu">
            <div class="subBox" v-for="(el, i) in menuData[selectMainMenu]" :key="i">
                <div>
                  <img :src="'../../../../static/img/打点图/' + i + '.png'" style="width:28px" alt="">
                  <span>{{i}}</span>
                </div>
                <el-switch
                  v-model="menuData[selectMainMenu][i]"
                  active-color="#1F7CDB"
                  inactive-color="#0A2047"
                ></el-switch>
            </div>
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
          '其他院校': false
        },
        '医院': {
          '医院': false
        },
        '重点场所': {
          '宾馆、酒店': false,
          '网约房': false,
          '商超、市场': false,
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
      selectMainMenu: '',
      submenuList: []
    }
  },
  props: ['item'],
  computed: {
    selectLength: function () {
      return (data) => {
        let len = 0
        for (let i in data) {
          if (data[i]) {
            len++
          }
        }
        return len
      }
    },
    ifCheck: function () {
      return (data) => {
        if (data === this.selectMainMenu) {
          return {
            background: 'url(' + require('./高亮背景.png') + ')'
          }
        } else {
          return {
            background: 'none'
          }
        }
      }
    },
    getImg: function () {
      return (url) => {
        let img = ''
        try {
          img = require(url + '.png')
        } catch (err) {

        }
        return img
      }
    }
  },
  watch: {
    menuData: {
      handler () {
        this.bus.$emit('Mark', this.menuData)
      },
      deep: true

    }
  },
  methods: {
    showSubManu: function (data) {
      if (this.selectMainMenu === data) {
        this.selectMainMenu = ''
      } else {
        this.selectMainMenu = data
      }
    }
  }
}
</script>
<style scoped lang="scss">
.OrderMenu{
  display: flex;
  .mainMenu{
    height: 580px;
    padding: 10px 0;
    background:#002b59;
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
        >div:first-child{
          margin-right: 30px;
        }
      }
  }
  .subMenu{
    background: #00244e;
    height: 580px;
    padding: 20px;
    overflow: scroll;
    .subBox{
      color: #ACCFFE;
      font-size: 20px;
      height: 60px;
      background: url(./矩形.png) no-repeat;
      background-size: 100% 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      text-align: center;
      vertical-align: middle;
      margin-bottom: 16px;
      padding: 16px;
      >div{
        img{
          margin-right: 10px;
        }
        margin-right: 20px;
      }
    }
    .subBox:last-child{
      margin-bottom: 0;
    }
  }
}
</style>
