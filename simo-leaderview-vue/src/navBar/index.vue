<template>
  <div class="navbarWrap">
    <div class="navLeft">
      <div class="logo">
        <img
          :src="
            theme === 'default'
              ? require('./images/logo.png')
              : require('./images/logo_white.png')
          "
        />
      </div>
      <div class="headIcon" @click.stop="iconDropDown('menu')">
        <i class="simo icon-n-menuconf" />
      </div>
    </div>
    <div class="navTittle">
      {{ typeTitle }}
    </div>
    <ul class="navRight">
      <li v-for="(v, k) in rightMenu" :key="k" @click="toggleMenu(v)">
        <img
          v-if="!isNaN(Number(v.iclass))"
          class="header_img"
          :src="'mc/menu/icon/' + v.iclass"
        />
        <i v-else :class="['simo', v.iclass]" />
      </li>
      <li @click.stop="iconDropDown('msg')">
        <i class="simo icon-n-xiaoxi" />
        <span class="msgNum">
          {{ msgNum > 99 ? "99+" : msgNum }}
        </span>
        <MsgPop
          v-if="openName === 'msg' && isOpen"
          @close-pop="closePop"
          @show-all-msg="showAllMsg"
          @open-content="openContent"
        />
      </li>
      <li>
        <i class="simo icon-n-skin" @click.stop="iconDropDown('skin')" />
        <ul v-show="openName === 'skin' && isOpen" class="skin">
          <li v-for="(v, k) in iconSkin" :key="k" @click="changeSkin(k)">
            {{ v }}主题
          </li>
        </ul>
      </li>
      <li>
        <i class="simo icon-n-yonghu" @click.stop="iconDropDown('user')" />
        <ul v-show="openName === 'user' && isOpen" class="user">
          <li v-for="(v, k) in iconUser" :key="k" @click="eventBus(k)">
            {{ v }}
          </li>
        </ul>
      </li>
    </ul>

    <ul v-show="openName === 'menu' && isOpen" class="dropdownMenuWrap">
      <template v-for="item in menu[0]?menu[0].items:''">
        <li
          :key="item.id"
          class="head-nav-item"
          :class="{ bordColor: typeId === item.id }"
          @click="toggleMenu(item)"
        >
          <div class="head-nav-img">
            <img
              v-if="!isNaN(Number(item.iclass))"
              class="head-nav-curimg"
              :src="'mc/menu/icon/' + item.iclass"
            />
            <template v-else>
              <img
                v-if="navImg.includes(item.iclass)"
                class="head-nav-curimg nav-system-img"
                :src="iclassMap[item.iclass]"
              />
              <i v-else class="left_icon" :class="item.iclass" />
            </template>
            <img class="head-nav-img-bg" :src="iclassMap.imgBg" />
          </div>
          <div class="head-nav-name">
            <p>
              {{ item.name }}
            </p>
            <img class="head-nav-name-bg" :src="iclassMap.nameBg" />
          </div>
        </li>
      </template>
      <!-- 第三方平台 -->
      <li
        v-for="item in thirdData"
        v-show="item.enable"
        :key="item.id"
        class="head-nav-item"
        @click="openThird(item.appId)"
      >
        <div class="head-nav-img">
          <img
            class="head-nav-curimg"
            :src="
              item.isCustomIcon
                ? PROXY_URL + '/mc/bus/getIcon?id=' + item.id
                : iclassMap.system
            "
          />
          <img class="head-nav-img-bg" :src="iclassMap.imgBg" />
        </div>
        <div class="head-nav-name">
          <p>
            <Tooltip placement="top" :content="item.name">
              {{ item.name }}
            </Tooltip>
          </p>
          <img class="head-nav-name-bg" :src="iclassMap.nameBg" />
        </div>
      </li>
    </ul>
    <div v-if="userInfoMd.isShow">
      <NavUserInfo :mdpram="userInfoMd" />
    </div>
    <div v-if="passWordMd.isShow">
      <UsPassWord :mdpram="passWordMd" />
    </div>
    <div v-if="authorizeMd.isShow">
      <!-- <Authorize :mdpram="authorizeMd" /> -->
    </div>
    <div v-if="transJobMd.isShow">
      <!-- <TransJob :mdpram="transJobMd" /> -->
    </div>
    <div v-if='myUsMsgConf.show'>
      <UsMsgConf :mdpram="myUsMsgConf"/>
      <!-- <Component
        :is="curModal"
        :value.sync="showModal"
      /> -->
    </div>
    <div v-if="menuMoveMd.isShow">
      <!-- <UsMenuMove :mdpram="menuMoveMd" /> -->
    </div>
    <!-- <Modal
      v-model="showContent"
      class="us-sm"
      title="消息内容"
      @on-cancel="confirm"
    >
      {{ msgRow.content }}
      <div slot="footer">
        <Button @click="confirm"> 确定 </Button>
      </div>
    </Modal> -->
  </div>
</template>

<script>
import { navImg } from './menuIcon.js'
import { gbs } from '@/config/settings'
export default {
  components: {
    MsgPop: () => import('./msgPop'),
    NavUserInfo: () => import('./userInfo'),
    UsPassWord: () => import('./passWord'),
    UsMsgConf: () => import('./msgConf')
    // Authorize: () => import('./authorize'),
    // TransJob: () => import('./transJob'),
    // UsMenuManage: () => import('./menuManage'),
    // UsMenuMove: () => import('./menuMove'),
  },
  data () {
    return {
      menu: [],
      navImg,
      curText: '',
      thirdData: '',
      iconSkin: {
        default: '深蓝',
        blackWhite: '黑白',
        blueWhite: '蓝白'
      },
      iconUser: {
        userInfo: '用户信息',
        passWord: '修改密码',
        // authorize: '临时授权',
        // transJob: '工作移交',
        msgConf: '消息配置',
        // editMenu: '编辑菜单',
        logout: '退出系统'
      },
      userInfoMd: {
        isShow: false
      },
      passWordMd: {
        isShow: false
      },
      authorizeMd: {
        isShow: false
      },
      transJobMd: {
        isShow: false
      },
      myUsMsgConf: {
        isShow: false
      },
      showIcon: {
        VIEW01: false,
        MSG01: false,
        SYS01: false
      },
      curModal: '',
      showModal: false,
      openName: '',
      isOpen: false,
      showContent: false,
      msgRow: {},
      menuMoveMd: { isShow: false },
      rightMenu: [],

      theme: 'default',
      typeId: '',
      typeTitle: '',
      msgNum: ''
    }
  },
  created () {
    this.axios.get('/mc/getCustomMenu?1627953454159').then((res) => {
      if (res.success) {
        this.menu = res.obj
      }
    })
  },
  computed: {
    iclassMap () {
      let map = {}
      let arr = this.menu[0] ? this.menu[0].items || [] : []
      arr.length &&
        arr.forEach((d) => {
          if (isNaN(Number(d.iclass))) {
            // 非图片
            if (navImg.includes(d.iclass)) {
              // 系统图片
              var imgName = d.iclass.split('icon-n-')[1]
              map[d.iclass] = require('./images/head/' +
                imgName +
                (this.theme === 'default' ? '_dark.png' : '_light.png'))
            }
          }
        })
      map['imgBg'] = require('./images/head/nav_bottom_bg' +
        (this.theme === 'default' ? '_dark.png' : '_light.png'))
      map['nameBg'] = require('./images/head/nav_name_bg' +
        (this.theme === 'default' ? '_dark.png' : '_light.png'))
      map['system'] = require('./images/head/system' +
        (this.theme === 'default' ? '_dark.png' : '_light.png'))
      return map
    }
  },
  methods: {
    eventBus (eveName) {
      this.isOpen = ''
      this[eveName]()
    },
    userInfo () {
      this.userInfoMd.isShow = true
    },
    // 密码重置
    passWord () {
      this.passWordMd.isShow = true
    },
    // 临时授权
    authorize () {
      this.authorizeMd.isShow = true
    },
    // 工作移交
    transJob () {
      this.transJobMd.isShow = true
    },
    changeSkin (name) {
      $('html').attr('data-theme', name)
      this.theme = name
      this.$notify({
        message: '更新皮肤成功！',
        position: 'bottom-right',
        customClass: 'toast toast-info'
      })
      this.isOpen = ''
    },
    msgConf () {
      this.myUsMsgConf.show = true
      // this.curModal = 'UsMsgConf'
    },

    logout () {
      this.$ensureModal.confirm('确认要退出系统？', () => {
        this.axios.get('/logout', { pagination: false }).then((res) => {
          if (res.success) {
          }
        })
        if (gbs.inDev) {
          window.location.href = window.location.origin + '/#/login'
        } else {
          this.$router.push('/login')
        }
      })
    },

    getThirdPlat () {
      this.axios.get('/mc/bus/search', { pagination: false }).then((res) => {
        if (res.success) {
          this.thirdData = res.obj.object || []
        }
      })
    },
    iconDropDown (str) {
      if (this.openName === str) {
        this.isOpen = !this.isOpen
      } else {
        if (str === 'menu') {
          this.getThirdPlat()
        }
        this.openName = str
        this.isOpen = true
      }
    },
    // 消息-查看全部消息
    showAllMsg () {
      this.setCurrentMenu({ id: 'MSG04', parentId: 'MSG02', typeId: 'MSG01' })
      this.$router.push('/msg/allMsg')
    },
    // 消息-确定
    confirm () {
      this.$api.msgTagRead({ ids: this.msgRow.id }).then((res) => {
        this.showContent = false
      })
    },
    closePop () {
      this.openName = ''
      this.isOpen = false
    },
    // 消息-打开消息内容
    openContent (msgRow) {
      this.showContent = true
      this.msgRow = msgRow
    }
  }
}
</script>
<style lang="scss" scoped>
.navbarWrap {
  position: fixed;
  top: 0;
  width: 100%;
  height: 50px;
  // @include getTheme($themes){
  //   background-color: themed('navbarWrap-background-color');
  //   box-shadow: themed('navbarWrap-box-shadow');
  // }
  z-index: 9;
  .headIcon {
    width: 44px;
    height: 50px;
    display: inline-block;
    line-height: 50px;
    cursor: pointer;
    text-align: center;
    position: absolute;
    // @include getTheme($themes){
    //   color: themed('navbarWrap-navLeft-headIcon-color');
    // }
    vertical-align: top;
    i {
      font-size: 20px;
    }
  }
  .logo {
    width: 116px;
    height: 50px;
    display: inline-block;
  }
  .navLeft {
    display: inline-block;
    width: 160px;
    height: 50px;
    background-color: #141d2d;
    // @include getTheme($themes){
    //   background-color: themed('navbarWrap-navLeft-background-color');
    // }
    float: none;
  }
  .close {
    width: 40px;
    .logo {
      display: none;
    }
    .headIcon {
      width: 40px;
    }
  }
  .navTittle {
    display: inline-block;
    line-height: 50px;
    font-size: 16px;
    padding: 0 15px;
    vertical-align: top;
  }
  .navRight {
    float: right;
    & > li {
      position: relative;
      float: left;
      & > i,
      & > a i {
        display: inline-block;
        width: 44px;
        height: 50px;
        text-align: center;
        line-height: 50px;
        font-size: 20px;
      }
      .header_img {
        width: 22px;
        height: 22px;
        margin: 14px 11px;
      }
    }
    .user,
    .skin {
      position: absolute;
      right: 0;
      text-align: left;
      min-width: 160px;
      padding: 5px 0;
      // @include getTheme($themes){
      //   background: themed('navbarWrap-navRight-user-background-color');
      // }
      li {
        line-height: 30px;
        text-indent: 15px;
        cursor: pointer;
        // &:hover{
        //   @include getTheme($themes){
        //     background-color: themed('navbarWrap-navRight-user-li-hover-background-color');
        //   }
        // }
      }
    }
    .skin {
      left: 0;
    }
    .msgNum {
      background: #ff0000;
      position: absolute;
      border-radius: 7px;
      font-size: 12px;
      min-width: 18px;
      line-height: 15px;
      text-align: center;
      top: 10px;
      right: 4px;
      color: white;
    }
  }
  .dropdownMenuWrap {
    background: #0d111f;
    width: 100%;
    position: absolute;
    top: 50px;
    height: 450px;
    overflow: auto;
    // @include getTheme($themes){
    //   background-color: themed('navbarWrap-dropdownMenuWrap-background-color');
    // }
    padding: 10px;
    li {
      float: left;
      display: flex;
      width: 220px;
      height: 100px;
      margin: 20px 60px 25px 20px;
      text-align: center;
      cursor: pointer;
      align-items: center;
      border: 1px solid rgba(0, 0, 0, 0);
      position: relative;
      &.bordColor,
      &:hover {
        background: rgba(0, 71, 179, 0.08);
        border-radius: 9px;
        box-shadow: 0 0 1px #3c96ff;
        box-shadow: -1px 0px 1px #4650fb, 0px -1px 1px #3c96ff,
          1px 0px 1px #4650fb, 0px 1px 1px #4650fb;
      }
      .head-nav-img {
        position: relative;
        width: 65px;
        margin: 0 10px 0 20px;
        .head-nav-curimg {
          height: 40px;
          width: 40px;
          margin-top: -8px;
        }
        .head-nav-img-bg {
          width: 65px;
          height: 30px;
          position: absolute;
          left: 0;
          top: 16px;
        }
        .left_icon {
          display: block;
          line-height: 30px;
          font-size: 30px;
          position: relative;
          margin-bottom: 6px;
          // @include getTheme($themes){
          //   color: themed('navbarWrap-dropdownMenuWrap-p-color');
          // }
        }
      }
      .head-nav-name {
        width: 112px;
        font-size: 0;
        position: absolute;
        left: 95px;
        bottom: 35px;
        p {
          text-align: center;
          font-size: 14px;
          // @include getTheme($themes){
          //   color: themed('navbarWrap-dropdownMenuWrap-p-color');
          // }
          // ::v-deep .ivu-tooltip-rel{
          //   @include text-ellipsis('',2);
          // }
        }
        .head-nav-name-bg {
          width: 80px;
          height: 4px;
          margin-top: 6px;
        }
      }
    }
  }
}
</style>
