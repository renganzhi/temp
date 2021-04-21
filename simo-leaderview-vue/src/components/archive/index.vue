<template>
  <div class="archive" :id="tapsStation">
      <el-menu class="el-menu-vertical-demo" collapse
        text-color="rgb(202, 214, 221)"
        active-text-color="#00a0e9"
      >
        <el-submenu index="1" :popper-append-to-body="true">
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
                <span>多媒体</span>
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
  props: ['tapsStation'],
  data () {
    return {
      index:'',
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
    }
  },
  methods: {
    initChart (data) {
      // console.log('type: ', data);
      this.$emit('click', data)
    }
  }
}
</script>

<style lang="scss" scoped>

.archive {
  box-sizing: border-box;
  border: solid 3px transparent;
  width: 60px;
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

html[data-theme="blackWhite"],
html[data-theme="blueWhite"] {
    .el-menu--vertical .archive_sub .cs-item-container .el-submenu__title{
      background-color: #fff;
      color: #50607c !important;
    }
    .el-menu--vertical .archive_sub .cs-item-container .el-submenu__title:hover{
      background: #f6f6f6!important;
      color: #08c !important;
    }
    .el-menu--vertical .cs-item div{
      color: #50607c !important
    }
    .is-opened .el-submenu__title{
      background-image: linear-gradient(to right, #cedcdd, #ebeef1);
      background-color:#e4e4e4 !important;
      box-sizing: border-box !important;
      color: rgb(91,123,166)!important;
      .nav_icon {
        color: #00a0e9;
      }
    }
    .archive {
      background: #fff !important;
    }
    .el-menu--vertical .el-menu{
      background: #fff !important;
      color: #50607c !important;
    }
    .el-menu--vertical .cs-item{
      border: 1px solid #c4c4c4 !important;
    }
    #mainEdit-edit .ring-icon{
      background: #edf3fe !important;
      color: #0089ff !important;
    }
}

html[data-theme="default"] {
 .archive{
    .el-submenu__title:focus, .el-submenu__title:hover {
      outline: 0 !important;
      // color: #409EFF !important;
      background: none !important;
      background-image: linear-gradient(to right, #12174e, #0f3858) !important;
      background-color:transparent;
      box-sizing: border-box !important;
      .nav_icon {
        color: #00a0e9;
      }
    }
  }
}
.archive {
  background: #1b2031;
  width: 60px;
  .el-menu-vertical-demo {
    background: transparent;
    border: none;
    width: 100%;
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
  .el-submenu {
    width: calc(100% - 3px);
    height: 35px;
        margin-top: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .el-submenu__title {
    border: solid 2.5px transparent;
    width: 54px;
    // margin-left: 3px;
    height: 35px;
    display: flex;
    justify-content: center;
    align-items: center;
    line-height: 28px;
    .nav_icon {
      font-size: 26px;
    }
  }
}
.el-menu--vertical{
  height: calc(100% - 210px);
  overflow: auto;
}
.is-opened {
  .el-submenu__title {
    background-image: linear-gradient(to right, #12174e, #0f3858) ;
      background-color:transparent;
    box-sizing: border-box !important;
      .nav_icon {
        color: #00a0e9;
      }
  }
}
#left{
  .el-submenu {
    margin-left: 3px;
  }
  .is-opened {
    .el-submenu__title {
      border-left: solid 2.5px #00a0e9;
    }
  }
}
#center{
  .el-submenu {
    margin-left: 3px;
  }
  .is-opened {
    .el-submenu__title {
      border-left: solid 2.5px #00a0e9;
    }
  }
}
#right{
  .el-submenu {
    margin-right: 3px;
  }
  .is-opened {
    .el-submenu__title {
      border-right: solid 2.5px #00a0e9;
    }
  }
}

.el-menu--vertical  {

  .el-submenu__title {
    height: 36px !important;
    line-height: 36px !important;
  }
  .el-submenu .el-menu-item {
    height: auto;
    line-height: 50px;
    padding: 0;
    min-width: auto;
}
  .el-menu {
    border: none;
    background: #262e44;
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
  .is-opened .el-submenu__title{
        background: #2e3444 !important;
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
    border: solid 1px #3d445a;
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
  .el-menu--popup {
    margin: 0;
    padding: 0;
    box-shadow: #000000e6 0px 0px 15px;
  }
}
</style>
