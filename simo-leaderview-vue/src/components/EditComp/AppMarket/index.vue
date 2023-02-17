<template>
  <div class="appmarket">
    <div class='market'>
      <div class="title">
        <div class="openTitle" @click="isShowModel = true"></div>
      </div>
      <div class="appmodel">
        <a :href="appArry[0].url"  target="_blank">{{appArry[0].xtname}}</a>
      </div>
      <div class="appmodel">
        <a :href="appArry[1].url"  target="_blank">{{appArry[1].xtname}}</a>
      </div>
      <div class="appmodel">
        <a :href="appArry[2].url"  target="_blank">{{appArry[2].xtname}}</a>
      </div>
      <div class="appmodel">
        <a :href="appArry[3].url"  target="_blank">{{appArry[3].xtname}}</a>
      </div>
    </div>
    <transition name="moveLeft">
      <div class="showModelBox" v-show="isShowModel">
        <div class="title">
          <div class="closeTitle" @click="isShowModel = false"></div>
        </div>
        <div class="ModelBody">
          <div class="selectTitle">
            <Input
              v-model="SelectName"
              style="width:270px"
              type="password"
              placeholder="请输入搜索内容"
            />
            <div class="search" @click="searchData"></div>
          </div>
          <div class="dataBody">
            <div class="data" v-for="(item,index) in appArry" :key="index">
              <a :href="item.url"  target="_blank">
                <div class="name">{{item.xtname}}</div>
                <div class="name2">{{item.name}}</div>
              </a>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>
<script>
export default {
  data () {
    return {
      isShowModel: false,
      SelectName: '',
      appArry: [
        {
          name: '区应急管理局',
          xtname: '安全风险管控',
          url: 'https://cd.wh.safety114.com/login?id=eyJ1c2VybmFtZSI6InpmNjEwMDAwIiwicHdkIjoiNjEwMDAwIn0='
        },
        {
          name: '区社治委',
          xtname: '小武生活',
          url: 'https://xwlife.sciirc.com:6351/#/tempLogin'
        },
        {
          name: '区民政局',
          xtname: '颐居通智能物联',
          url: 'https://gov.91xxt.com/admin/report?wuhou&token=1X77hYVrwkLrRrLKprW2ndi4vjk'
        },
        {
          name: '区生态环保局',
          xtname: '智能大气监管',
          url: 'https://am.glzt.com.cn/login/redirect?noLogin=1'
        },
        {
          name: '区水务局',
          xtname: '智慧河长',
          url: 'http://10.29.119.30:8080/#/river/index'
        },
        {
          name: '区水务局',
          xtname: '智慧管网',
          url: 'http://10.29.119.30:8080/#/pipe/index'
        },
        {
          name: '区水务局',
          xtname: '智慧防汛',
          url: 'http://10.29.119.30:8080/#/floodControl/index'
        },
        {
          name: '区水务局',
          xtname: '智慧运管',
          url: 'http://10.29.119.30:8080/#/control/index'
        },
        {
          name: '区司法局',
          xtname: '智慧公共法律服务',
          url: 'https://cj.lvwa18.com/manager/login?adminLogin=cdwhsfj&adminPassword=MVrGyqgmQz5UxHbwiEMNw'
        },
        {
          name: '区综合执法局',
          xtname: '景观照明',
          url: 'http://110.185.171.39:81/#/bigScreen/index?user=DJF&pd=lAGIOwlxZqOMoH81DpCVPlG7edmWL1upB1zW5tySJ%2F4tvlXfO8bUSJNul6d7NYrsvXDRs7xBSU5CPHg9PE05eSDOA821SMfSDSFK9sjpH5DCLXiR4bPz2%2B3kW9iIXZyQfEmpWPPu5dVz5ohlNzYz2OE%2B0lFMRltbnM5wdCxh1VM%3D&path=%2FbigScreen%2Findex'
        },
        {
          name: '区发改局',
          xtname: '重点企业走访',
          url: 'https://whcig.cdwh.gov.cn:8060/#/4Hwl6bwK3r5kLEU'
        },
        {
          name: '行政审批局',
          xtname: '政务服务',
          url: 'http://172.16.251.99/whspj/cdwhzwfw/newdataanalysis/matter-analysis2'
        },
        {
          name: '行政审批局',
          xtname: '政务窗口全景',
          url: 'http://172.16.251.99/whspj/cdwhzwfw/windowdynamic/windowdynamic2'
        },
        {
          name: '行政审批局',
          xtname: '政务云',
          url: 'http://172.16.251.128:8080/bmc/vm/generalView/index?currentIndex=pg-13-06&amp;parentId=pg-05-group-04&sso=ywpn2m7UAaOiawGF2XNVUIq2BpqmNGfJ0gak1IgdKbP0EsSeDVgioQ=='
        }
      ]
    }
  },
  methods: {
    searchData () {
      console.log(this.SelectName)
    }
  }
}
</script>
<style lang="scss" scoped>
.appmarket{
  height: 100%;
  width: 100%;
  position: relative;
  .market{
    height: 100%;
    width: 100%;
    position: relative;
    .title{
      height: 45px;
      width: 100%;
      background-image: url('./img/124.png');
      background-size: 100%;
      position: relative;
      .openTitle{
        width: 40px;
        height: 22px;
        position: absolute;
        right: 11px;
        top: 10px;
        cursor: pointer;
        background-image: url('./img/122.png');
        background-size: 100%;
      }
    }
    .appmodel{
      width: 100%;
      height: 53px;
      margin-top: 7px;
      display: flex;
      align-items: center;
      background-image: url('./img/123.png');
      background-size: 100%;
      a{
        color: #C8E0FF;
        font-size: 16px;
      }
    }
    .appmodel::before{
      content: '';
      height: 8px;
      width: 8px;
      margin-right: 10px;
      margin-left: 10px;
      display: inline-block;
      border-radius: 50%;
      background-image: linear-gradient(45deg,#32C2D4,#D5EFFF );
    }
  }
  .showModelBox{
    width: 780px;
    height: 545px;
    position: absolute;
    right: 0;
    bottom: 0;
    .title{
      height: 45px;
      width: 100%;
      background-image: url('./img/125.png');
      background-size: 100%;
      position: relative;
      .closeTitle{
        width: 26px;
        height: 26px;
        position: absolute;
        right: 11px;
        top: 10px;
        cursor: pointer;
        background-image: url('./img/126.png');
        background-size: 100%;
      }
    }
    .ModelBody{
      width: 100%;
      height: 500px;
      padding: 10px 20px;
      background-color: #112C5C;
      .selectTitle{
        width: 100%;
        height: 50px;
        display: flex;
        align-items: center;
        padding-left: 4px;
        input{
          height: 32px !important;
        }
        .search{
          height: 32px;
          width: 32px;
          background-image: url('./img/127.png');
          background-size: 100%;
          display: inline-block;
          cursor: pointer;
          margin-left: 8px;
        }
      }
      .dataBody{
        width: 100%;
        height: 430px;
        display: flex;
        align-items: flex-start;
        flex-wrap: wrap;
        justify-content: flex-start;
        overflow: auto;
        .data{
          height: 85px;
          width: 140px;
          background-image: url('./img/128.png');
          background-size: 100%;
          margin: 3px;
          padding: 10px 10px 10px 23px;
          .name{
            color: #C8E0FF;
            font-size: 14px;
            position: relative;
          }
          .name::before{
            content: '';
            height: 8px;
            position: absolute;
            left: -24px;
            top: 6px;
            width: 8px;
            margin-right: 10px;
            margin-left: 10px;
            display: inline-block;
            border-radius: 50%;
            background-image: linear-gradient(45deg,#32C2D4,#D5EFFF );
          }
          .name2{
            color: #52D6E7;
            font-size: 12px;
          }
        }
      }
    }
  }
}

.appmarket{
    display: flex;
    flex-wrap: wrap;
    font-family: monospace !important;
    // overflow: hidden;
    .leftPart,.rightPart{
        display: flex;
    }
    .moveRight-enter-active {
    animation: box-right-in 0.5s;
    }
    .moveRight-leave-active {
        animation: box-left-leave 0.5s;
    }
    .moveLeft-enter-active {
    animation: box-left-in 0.5s;
    }
    .moveLeft-leave-active {
        animation: box-right-leave 0.5s;
    }
    .moveTop-enter-active {
    animation: box-top-in 0.5s;
    }
    .moveTop-leave-active {
        animation: box-bottom-leave 0.5s;
    }
    .moveBottom-enter-active {
    animation: box-bottom-in 0.5s;
    }
    .moveBottom-leave-active {
        animation: box-top-leave 0.5s;
    }
    // 向左移动
    @keyframes box-left-leave {
    from {
        transform: translateX(0);
    }
    to {
        transform: translateX(-100%);
    }
    }
    @keyframes box-left-in {
    from {
        transform: translateX(100%);
    }
    to {
        transform: translateX(0);
    }
    }
    // 向右移动
    @keyframes box-right-leave {
    from {
        transform: translateX(0);
    }
    to {
        transform: translateX(100%);
    }
    }
    @keyframes box-right-in {
    from {
        transform: translateX(-100%);
    }
    to {
        transform: translateX(0);
    }
    }
    // 向上移动
    @keyframes box-top-leave {
    from {
        transform: translateY(0);
    }
    to {
        transform: translateY(-100%);
    }
    }
    @keyframes box-top-in {
    from {
        transform: translateY(100%);
    }
    to {
        transform: translateY(0);
    }
    }
    // 向下移动
    @keyframes box-bottom-leave {
    from {
        transform: translateY(0);
    }
    to {
        transform: translateY(100%);
    }
    }
    @keyframes box-bottom-in {
    from {
        transform: translateY(-100%);
    }
    to {
        transform: translateY(0);
    }
    }
}
</style>
