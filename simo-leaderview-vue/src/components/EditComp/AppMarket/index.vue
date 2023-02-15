<template>
  <div class="appmarket">
    <div class='market'>
      <div class="title">
        <div class="openTitle" @click="isShowModel = true"></div>
      </div>
      <div class="appmodel">大数据资源平台</div>
      <div class="appmodel">成都市防汛减灾保障系统</div>
      <div class="appmodel">大数据资源平台</div>
      <div class="appmodel">成都市防汛减灾保障系统</div>
    </div>
    <div class="showModelBox" v-if="isShowModel">
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
          <div class="data" v-for="(item,index) in 40" :key="index">
            <div class="name">成都市防汛减灾保障系统</div>
            <div class="name2">市城运中心</div>
          </div>
        </div>
      </div>
    </div>
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
      color: #C8E0FF;
      font-size: 16px;
      display: flex;
      align-items: center;
      background-image: url('./img/123.png');
      background-size: 100%;
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
</style>
