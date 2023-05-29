<template>
  <div>
    <button @click="toggleSearchBar">按钮</button>
    <transition name="slide">
      <div v-show="isSearchBarVisible" class="search-bar">
        <input v-model="street" placeholder="街道" />
        <input v-model="community" placeholder="社区" />
        <input v-model="microGrid" placeholder="微网格" />
        <button @click="searchPot">查询</button>
        <button @click="reset">重置</button>
        <button @click="toggleSelection">{{ boxSelectEnabled ? '结束框选' : '开始框选' }}</button>
        <div class="closeBtn" @click="onClose">'◀'</div>
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
  width: 1000px;
  opacity: 1;
}
.search-bar {
  display: flex;
  background: #082546;
  position: relative;
  overflow: hidden;
}
.closeBtn{
  color:#777;
  width: 30px;
  height: 30px;
  background: #082546;
}
</style>
