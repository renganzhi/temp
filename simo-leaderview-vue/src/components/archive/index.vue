<template>
  <div class="archive">
      <el-menu class="el-menu-vertical-demo" collapse="true"
        text-color="rgb(202, 214, 221)"
        active-text-color="#00a0e9"
      >
        <el-submenu index="1"   :popper-append-to-body="true">
          <template slot="title">
            <i class="nav_icon icon-archive1"></i>
            <span slot="title">分类一</span>
          </template>
          <el-menu class="archive_sub"
          :default-openeds="['1-1']"
          :unique-opened="true"
           width="180"
          text-color="rgb(202, 214, 221)"
          active-text-color="#00a0e9"
          >
            <el-submenu index="1-1" class="cs-item-container"
            >
              <template slot="title">
                <span>坐标图</span>
              </template>
              <el-menu-item :index="`1-1-${index}`" :key="key"
                      v-for="(value,key) in compsArr.coordinate.child"
                      class="cs-item"
                      width="90"
                      @click="initChart(value)">
                <div slot="title" :class="value.imgClass">{{value.text}}</div>
              </el-menu-item>
            </el-submenu>
            <el-submenu index="1-2" class="cs-item-container">
              <template slot="title">
                <span>百分比图</span>
              </template>
              <el-menu-item :index="`1-2-${index}`" :key="key"
                      v-for="(value,key) in compsArr.percentage.child"
                      class="cs-item"
                      width="90"
                      @click="initChart(value)">
                <div slot="title" :class="value.imgClass">{{value.text}}</div>
              </el-menu-item>
            </el-submenu>
            <el-submenu index="1-3" class="cs-item-container">
              <template slot="title">
                <span>关系图</span>
              </template>
              <el-menu-item :index="`1-3-${index}`" :key="key"
                      v-for="(value,key) in compsArr.relation.child"
                      class="cs-item"
                      width="90"
                      @click="initChart(value)">
                <div slot="title" :class="value.imgClass">{{value.text}}</div>
              </el-menu-item>
            </el-submenu>
            <el-submenu index="1-4" class="cs-item-container">
              <template slot="title">
                <span>表格</span>
              </template>
              <el-menu-item :index="`1-4-${index}`" :key="key"
                      v-for="(value,key) in compsArr.table.child"
                      class="cs-item"
                      width="90"
                      @click="initChart(value)">
                <div slot="title" :class="value.imgClass">{{value.text}}</div>
              </el-menu-item>
            </el-submenu>
            <el-submenu index="1-5" class="cs-item-container">
              <template slot="title">
                <span>地图</span>
              </template>
              <el-menu-item :index="`1-5-${index}`" :key="key"
                      v-for="(value,key) in compsArr.map.child"
                      class="cs-item"
                      width="90"
                      @click="initChart(value)">
                <div slot="title" :class="value.imgClass">{{value.text}}</div>
              </el-menu-item>
            </el-submenu>
          </el-menu>
        </el-submenu>
        <el-submenu index="2" :popper-append-to-body="true">
          <template slot="title">
            <i class="nav_icon icon-archive2"></i>
            <span slot="title">分类二</span>
          </template>
          <el-menu class="archive_sub"
          :default-openeds="['2-1']"
          :unique-opened="true"
           width="180"
          text-color="rgb(202, 214, 221)"
          active-text-color="#00a0e9"
          >
          <el-submenu index="2-1" class="cs-item-container">
              <template slot="title">
                <span>地图</span>
              </template>
              <el-menu-item :index="`2-1-${index}`" :key="key"
                      v-for="(value,key) in compsArr.media.child"
                      class="cs-item"
                      width="90"
                      @click="initChart(value)">
                <div slot="title" :class="value.imgClass">{{value.text}}</div>
              </el-menu-item>
            </el-submenu>
          </el-menu>
        </el-submenu>
        <el-submenu index="3">
          <template slot="title">
            <i class="nav_icon icon-archive3"></i>
            <span slot="title">分类三</span>
          </template>
          <el-menu class="archive_sub"
          :default-openeds="['3-1']"
          :unique-opened="true"
           width="180"
          text-color="rgb(202, 214, 221)"
          active-text-color="#00a0e9"
          >
          <el-submenu index="3-1" class="cs-item-container">
              <template slot="title">
                <span>文本</span>
              </template>
              <el-menu-item :index="`3-1-${index}`" :key="key"
                      v-for="(value,key) in compsArr.text.child"
                      class="cs-item"
                      width="90"
                      @click="initChart(value)">
                <div slot="title" :class="value.imgClass">{{value.text}}</div>
              </el-menu-item>
            </el-submenu>
          </el-menu>
        </el-submenu>
        <el-submenu index="4">
          <template slot="title">
            <i class="nav_icon icon-archive4"></i>
            <span slot="title">其他元件</span>
          </template>
          <el-menu class="archive_sub"
          :default-openeds="['4-1']"
          :unique-opened="true"
           width="180"
          text-color="rgb(202, 214, 221)"
          active-text-color="#00a0e9"
          >
          <el-submenu index="4-1" class="cs-item-container">
              <template slot="title">
                <span>其他元件</span>
              </template>
              <el-menu-item :index="`4-1-${index}`" :key="key"
                      v-for="(value,key) in compsArr.others.child"
                      class="cs-item"
                      width="90"
                      @click="initChart(value)">
                <div slot="title" :class="value.imgClass">{{value.text}}</div>
              </el-menu-item>
            </el-submenu>
          </el-menu>
        </el-submenu>
        <!-- 参考 -->
        <!-- <el-menu-item index="4">
          <template >
            <i class="nav_icon icon-archive4"></i>
            <span slot="title">分类四</span>
          </template>
        </el-menu-item> -->
      </el-menu>
  </div>
</template>


<script>
import coordinate from './coordinate'
import percentage from './percentage'
import relation from './relation'
import table from './table'
import map from './map'
import media from './media'
import text from './text'
import others from './others'

export default {
    name: 'archive',
    data() {
      return {
        tabPosition: 'left',
        compsArr: {
          coordinate,
          percentage,
          relation,
          table,
          map,
          media,
          text,
          others
        }
      };
    },
    methods: {
      initChart (data) {
        // console.log('type: ', data);
        this.$emit('click', data);
        
      }
    }
}
</script>

<style lang="scss" scoped>

.archive {
  box-sizing: border-box;
  border: solid 3px transparent;
}
.nav_icon {
  font-size: 30px;
  // line-height: 64px;
}

.nav_icon.active {
  background-image: linear-gradient(to right, #12174e, #0f3858);
  border-left: solid 3px #00a0e9;
}
</style>

<style lang="scss">
.archive {
  background: #1b2031;
  .el-menu-vertical-demo {
    background: transparent;
    border: none;
  }
  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 200px;
    min-height: 400px;
  }
  svg{
    width: 30px;
    height: 30px;
    fill:#324156;
  }
  // .el-menu-item:hover {
  //   background-image: linear-gradient(to right, #12174e, #0f3858);
  //   border-left: solid 3px #00a0e9;
  //   svg {
  //     fill: #00a0e9;
  //   }
  // }
  .el-submenu__title {
    border: solid 3px transparent;
  }
.el-submenu__title:focus, .el-submenu__title:hover{  
    outline: 0 !important;  
    // color: #409EFF !important;  
    background: none !important;
    background-image: linear-gradient(to right, #12174e, #0f3858) !important;
    box-sizing: border-box !important;
    border-left: solid 3px #00a0e9;
    .nav_icon {
      color: #00a0e9;
    }
    }  
}

.el-menu--vertical  {
  .el-menu {
    border: none;
    background: #1b2031;
    color: rgb(202, 214, 221);
  }

  .archive_sub {
    .cs-item-container .el-submenu__title {
       background: #141a2d;
      &:hover {
        background: #2e3444;
        color: #ffffff;
      }
    
    }
  }
  .cs-item-container ul{
    width: 180px;
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    margin: 10px;
  }

  .cs-item {
    min-width: 90px;
    width: 88px;
    height: 88px;
    margin: 0;
    padding: 0;
    border: solid 1px #3d445a !important;
    // margin: 0 0 -1px -1px;
    flex: 1;
        
    &:hover {
      background: none !important;
       border: 1px solid #0088cc !important;
    }
    &:before {
      font-size: 30px;
      margin-left: -12px;
    }
    div {
          width: 88px;
          height: 88px;
          margin-left: -40px;
          font-size: 12px;
          display: flex;
        flex-direction: column;
      justify-content: center;
      align-items: center;
      text-align: center;
      position: relative;
      line-height: 36px;
    text-indent: 15px;
      &:before {
        font-size: 30px;
        margin-left: -12px;

      }
    }
    }
    .cs-item:nth-child(2n+1) {
      &:last-child {
        margin-right: 90px;
      }
    }
    .is-active {
      background: none !important;
      color: rgb(202, 214, 221) !important;
    }
  
}
</style>