<template>
  <div>
    <div @click="toggleSearchBar" class="togglebar">
        <img class="ren" src="./wgyimg/ren.png" >
        <img class="qiu" src="./wgyimg/qiu.png" >
        <img class="dizuo" src="./wgyimg/dizuo.png" >
    </div>
    <transition name="slide">
      <div v-show="isSearchBarVisible" class="search-bar">
        <input v-model="street" placeholder="请输入街道" />
        <input v-model="community" placeholder="请输入社区" />
        <input v-model="microGrid" placeholder="请输入微网格" />
        <button @click="searchPot" class="search">查询</button>
        <button @click="reset" class="reset">重置</button>
        <img class="grep" src="./wgyimg/fenge.png" >
        <div @click="toggleSelection" class="kuangx">{{ boxSelectEnabled ? '结束' : '开始' }}</div>
        <div class="closeBtn" @click="onClose">◀</div>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isSearchBarVisible: false,
      street: '',
      community: '',
      microGrid: '',
      gridWorkers: [
  { street: '街道A', community: '社区A', microGrid: '微网格A', 纬度: 30.621543967342, 经度: 104.031782746196 },
  { street: '街道B', community: '社区A', microGrid: '微网格B', 纬度: 30.618343967652, 经度: 104.022102746195 },
  { street: '街道A', community: '社区B', microGrid: '微网格C', 纬度: 30.638343967962, 经度: 104.022902746203 },
  { street: '街道B', community: '社区B', microGrid: '微网格A', 纬度: 30.612343968072, 经度: 104.02490274621099 },
  // 其他网格员数据项...
],
    boxSelectEnabled: false,
    };
  },
  methods: {
    toggleSearchBar() {
      this.isSearchBarVisible = !this.isSearchBarVisible;
    },
    toggleSelection() {
      // 切换状态
      this.boxSelectEnabled = !this.boxSelectEnabled;
      this.bus.$emit('toggleBoxSelect', this.boxSelectEnabled);
      this.bus.$emit('gridWorkersAll', this.gridWorkers)
    },
    reset() {
      debugger
      this.street = '';
      this.community = '';
      this.microGrid = '';
      this.bus.$emit('searchPot', []);
    },
    searchPot () {
      const filteredData = this.filterData(); 
      this.bus.$emit('searchPot', filteredData);
    },
    filterData() {
        const filteredData = this.gridWorkers.filter(item => {
        const matchStreet = this.street === '' || item.street === this.street;
        const matchCommunity = this.community === '' || item.community === this.community;
        const matchMicroGrid = this.microGrid === '' || item.microGrid === this.microGrid;
        return matchStreet && matchCommunity && matchMicroGrid;
      });
      // console.log(filteredData)
      return filteredData;

      //  this.axios.get(u).then((res) => {
                  
      //               })
      //             ;
      // 根据输入框的值进行数据筛选逻辑，返回筛选结果
      // 这里假设 data 是所有的网格员坐标数据，根据输入框的值进行筛选

    },
     onClose() {
      // 停止框选功能
      this.boxSelectEnabled = false;
      this.bus.$emit('toggleBoxSelect', false);
      this.toggleSearchBar()
    }
  }
};
</script>

<style scoped>
.slide-enter-active,
.slide-leave-active {
  transition: width 0.5s;
}

.slide-enter,
.slide-leave-to {
  width: 0;
  opacity: 0;
}

.slide-enter-to,
.slide-leave {
  width: 1212px;
  opacity: 1;
}
.togglebar {
  position: relative;
  width: 102px;
  height: 92px;
  margin-right: 15px;
  float: left;
  margin-top: -38px;
}

/* .togglebar img {
  position: absolute;
} */
.ren{
  width: 42px;
  height: 46px;
  position: absolute;
  top: 67%;
  left: 51%;
  transform: translate(-50%, -50%);
}
.qiu{
  width: 76px;
  height: 76px;
  position: absolute;
  top: 28%;
  left: 14%;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  animation: spin 10s linear infinite;
}
@keyframes spin {
    from {
        transform: rotate(0deg);
    }
    to {
        transform: rotate(360deg);
    }
}
.dizuo{
  position: absolute;
  width: 92px;
  height: 71px;
  top: 100%;
  left: 50%;
  transform: translate(-50%, -50%);
}
.search-bar {
  background: url('./wgyimg/bar.png') no-repeat;
  display: flex;
  position: relative;
  overflow: hidden;
  height: 80px;
  padding:8px;
}
.search-bar input{
  background: url('./wgyimg/shuruk.png') no-repeat;
  width: 282px;
  height: 50px;
  font-size: 22px !important;
  border-radius: 3px;
  color:#8FABBF;
  line-height: 20px;
  margin:8px;
  }
.search-bar .search{
  background: url('./wgyimg/chaxun.png') no-repeat;
  width:80px;
  height:48px;
  font-size: 20px !important;
  border-radius: 2px;
  line-height: 20px;
  color:#ACD2F6;
  margin:8px;
  }
.search-bar .reset{
  background: url('./wgyimg/chongzhi.png') no-repeat;
  width: 76px;
  height: 48px;
  font-size: 20px !important;
  border-radius: 2px;
  line-height: 20px;
  color:#91C2F2;
  margin:8px;
  }
.search-bar .search:hover{
  background: url('./wgyimg/chaxunac.png') no-repeat;
  color:#DCEEFF;
  }
.search-bar .reset:hover{
  background: url('./wgyimg/resetac.png') no-repeat;
  color:#DCEEFF;
  }
  .grep{
  width:2px;
  height:50px;
  margin:8px 20px;
  }
.search-bar .kuangx{
  background: url('./wgyimg/kuangxuan.png') no-repeat;
  width: 34px;
  height: 34px;
  padding-left: 5px;
  border-radius: 2px;
  line-height: 30px;
  color: #DCEEFF;
  margin: 16px 8px 16px 1px;
  }
/* .search-bar .kuangx:hover{
  background: #324f6b93;
  } */
.closeBtn{
  color: #777;
  width: 31px;
  padding: 10px;
  height: 37px;
  margin-left: 3px;
  margin-top: 13px;
  background: #11293f;
}
.closeBtn:hover{
  background: #324f6b93;
  border-radius: 3px;
}
</style>
