<template>
  <div class="Bigappmarket">
    <div class='market'>
      <div class="title">
        <div class="openTitle" @click="isShowModel = true"></div>
      </div>
      <div class="appmodel">
        <a :href="item.chartData.appArry[0].url"  target="_blank">{{item.chartData.appArry[0].xtname}}</a>
      </div>
      <div class="appmodel">
        <a :href="item.chartData.appArry[1].url"  target="_blank">{{item.chartData.appArry[1].xtname}}</a>
      </div>
      <div class="appmodel">
        <a :href="item.chartData.appArry[2].url"  target="_blank">{{item.chartData.appArry[2].xtname}}</a>
      </div>
      <div class="appmodel">
        <a :href="item.chartData.appArry[3].url"  target="_blank">{{item.chartData.appArry[3].xtname}}</a>
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
      SelectName: ''
    }
  },
  props: ['item'],
  computed: {
    appArry: function () {
      if (this.SelectName) {
        let arr = []
        this.item.chartData.appArry.forEach(element => {
          if (element.name.indexOf(this.SelectName) >= 0 || element.xtname.indexOf(this.SelectName) >= 0) {
            arr.push(element)
          }
        })
        return arr
      } else {
        return this.item.chartData.appArry
      }
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
.Bigappmarket{
  height: 100%;
  width: 100%;
  position: relative;
  .market{
    height: 100%;
    width: 100%;
    position: relative;
    .title{
      height: 72px;
      width: 100%;
      background-image: url('./img/124.png');
      background-size: 100% 100%;
      background-repeat: no-repeat;
      position: relative;
      .openTitle{
        width: 40px;
        height: 22px;
        position: absolute;
        right: 11px;
        top: 22px;
        cursor: pointer;
        background-image: url('./img/122.png');
        background-size: 100% 100%;
        background-repeat: no-repeat;
      }
    }
    .appmodel{
      width: 100%;
      height: 82px;
      margin-top: 14px;
      display: flex;
      align-items: center;
      background-image: url('./img/123.png');
      background-size: 100%;
      a{
        color: #C8E0FF;
        font-size: 28px;
      }
    }
    .appmodel::before{
      content: '';
      height: 16px;
      width: 16px;
      margin-right: 10px;
      margin-left: 10px;
      display: inline-block;
      border-radius: 50%;
      background-image: linear-gradient(45deg,#32C2D4,#D5EFFF );
    }
  }
  .showModelBox{
    width: 1170px;
    height: 793px;
    position: absolute;
    right: 0;
    bottom: 0;
    .title{
      height: 73px;
      width: 100%;
      background-image: url('./img/125.png');
      background-size: 100% 100%;
      background-repeat: no-repeat;
      position: relative;
      .closeTitle{
        width: 39px;
        height: 39px;
        position: absolute;
        right: 11px;
        top: 10px;
        cursor: pointer;
        background-image: url('./img/126.png');
        background-size: 100% 100%;
        background-repeat: no-repeat;
      }
    }
    .ModelBody{
      width: 100%;
      height: 720px;
      padding: 20px;
      background-color: #112C5C;
      .selectTitle{
        width: 100%;
        height: 50px;
        display: flex;
        align-items: center;
        padding-left: 4px;
        input{
          height: 50px !important;
        }
        .search{
          height: 50px;
          width: 50px;
          background-image: url('./img/127.png');
          background-size: 100%;
          display: inline-block;
          cursor: pointer;
          margin-left: 10px;
        }
      }
      .dataBody{
        width: 100%;
        height: 566px;
        display: flex;
        align-items: flex-start;
        flex-wrap: wrap;
        justify-content: flex-start;
        overflow: auto;
        .data{
          height: 131px;
          width: 260px;
          background-image: url('./img/128.png');
          background-size: 100% 100%;
          background-repeat: no-repeat;
          margin: 10px;
          padding: 20px 20px 20px 25px;
          .name{
            color: #C8E0FF;
            font-size: 24px;
            position: relative;
          }
          .name::before{
            content: '';
            height: 16px;
            position: absolute;
            left: -28px;
            top: 11px;
            width: 16px;
            margin-right: 10px;
            margin-left: 10px;
            display: inline-block;
            border-radius: 50%;
            background-image: linear-gradient(45deg,#32C2D4,#D5EFFF );
          }
          .name2{
            color: #52D6E7;
            font-size: 20px;
          }
        }
      }
    }
  }
}

.Bigappmarket{
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

<style lang="scss">
.Bigappmarket{
  .selectTitle{
    .ivu-input{
      height: 50px !important;
    }
  }
}
</style>
