<template>
    <div class="OrderMenu">
        <div class="openBtn" @click="showMenu = true">
          <img style="width:68px;height:68px" src="./打点.png" alt="">
          <div style="font-size:30px;padding:10px 5px;height:68px;text-align:center;">武侯区基础设施 </div>
        </div>
        <div class="mainMenu" v-show="showMenu">
            <div class="mainBox" :style="ifCheck(index, 1)" @click="showSubManu(index)" v-for="(element, index) in menuData" :key="index">
              <div>
                <img :src="getImg('./' + index)" style="width:30px" alt="">
                <span>{{index}}</span>
              </div>
              <div>
                <span style="color:#318ec7">{{selectLength(element)}}/{{Object.keys(element).length}}</span>
                <img src="./more.png" style="width:24px" alt="">
              </div>
            </div>
            <div class="closeBtn" @click="showMenu = false"><img style="width:140px;height:32px" src="./收起.png" alt=""></div>
        </div>
        <div class="mainMenu" style="overflow: scroll;" v-show="selectMainMenu && showMenu">
            <div class="mainBox" :style="ifCheck(i, 2)" @click="showThreeMenu(i)" v-for="(el, i) in menuData[selectMainMenu]" :key="i">
                <div>
                  <img :src="require('../../../../static/img/打点图/' + i + '.png')" style="width:28px" alt="">
                  <span>{{i}}</span>
                </div>
                <div>
                  <img src="./more.png" style="width:24px" alt="">
                </div>
            </div>
        </div>
        <div class="subMenu" v-if="selectMainMenu && selectSubMenu && showMenu && menuData[selectMainMenu] && menuData[selectMainMenu][selectSubMenu]">
          <div class="subBox" v-for="(val, ind) in menuData[selectMainMenu][selectSubMenu]" :key="ind">
            <div>
              {{ind}}
            </div>
            <el-switch
                  v-model="menuData[selectMainMenu][selectSubMenu][ind]"
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
      menuData: {},
      showMenu: false,
      selectMainMenu: '',
      selectSubMenu: ''
    }
  },
  props: ['item'],
  computed: {
    selectLength: function () {
      return (data) => {
        let len = 0
        for (let index in data) {
          for (let i in data[index]) {
            if (data[index][i]) {
              len++
              break
            }
          }
        }
        return len
      }
    },
    ifCheck: function () {
      return (data, i) => {
        if (i === 1) {
          if (data === this.selectMainMenu) {
            return {
              background: 'url(' + require('./高亮背景.png') + ')'
            }
          } else {
            return {
              background: 'none'
            }
          }
        } else if (i === 2) {
          if (data === this.selectSubMenu) {
            return {
              background: 'url(' + require('./高亮背景.png') + ')'
            }
          } else {
            return {
              background: 'none'
            }
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
  beforeCreate () {
    let menuData = {
      '党政机关': {
        '党政机关': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '公安机关': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        }
      },
      '高层建筑': {
        '地标建筑': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        }
      },
      '文化景点': {
        '景点': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '公园': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '名木古树': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        }
      },
      '学校': {
        '幼儿园': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '中小学': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '高校': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '其它院校': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        }
      },
      '医院': {
        '医院': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '诊所': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '医美机构': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        }
      },
      '重点场所': {
        '宾馆、酒店': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '网约房': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '商场、超市、市场': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '固废处理场所': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '加油、加气站': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '酒吧、KTV、会所': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '网吧、影城': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '危化存储处理站': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '微型消防站': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '洗浴、美容': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '应急避难场所': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '应急疏散场所': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '中央驻川科研单位': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        },
        '重点治安场所': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        }
      },
      '处置队伍': {
        '处置队伍': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        }
      },
      '天网': {
        '视频监控': {
          '浆洗街街道': false,
          '火车南站街道': false,
          '簇桥街道': false,
          '华兴街道': false,
          '晋阳街道': false,
          '玉林街道': false,
          '簇锦街道': false,
          '红牌楼街道': false,
          '金花桥街道': false,
          '机投桥街道': false,
          '望江路街道': false
        }
      }
    }
    this.axios.get('/leaderview/newDistrict/GetDTDD9').then(res => {
      res.obj.forEach(element => {
        for (let index1 in menuData) {
          for (let index2 in menuData[index1]) {
            if (index2 === element.type) {
              menuData[index1][index2] = {}
              element.items.forEach(e => {
                menuData[index1][index2][e] = false
              })
            }
          }
        }
      })
      this.menuData = JSON.parse(JSON.stringify(menuData))
    })
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
        this.selectSubMenu = ''
      } else {
        this.selectMainMenu = data
        this.selectSubMenu = ''
      }
    },
    showThreeMenu: function (data) {
      if (this.selectSubMenu === data) {
        this.selectSubMenu = ''
      } else {
        this.selectSubMenu = data
      }
    }
  }
}
</script>
<style scoped lang="scss">
.OrderMenu{
  display: flex;
  .openBtn{
    display: flex;
    background: #022a56;
    height: 68px;
    padding-right: 10px;
    position:absolute;
    bottom: 0;
    left:0;
  }
  .mainMenu{
    height: 660px;
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
        >div:first-child{
          margin-right: 30px;
        }
      }
    .closeBtn{
      position:absolute;
      bottom: 0;
      left: 50%;
      transform: translate(-50%, 0);
    }
  }
  .subMenu{
    background: #00244e;
    height: 660px;
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
