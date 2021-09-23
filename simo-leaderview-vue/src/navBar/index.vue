<template>
  <div class="navbarWrap">
    <div class="navLeft">
      <div class="logo">
        <img :src="require('./images/logo_white.png')" />
      </div>
      <!-- theme === 'default'
              ? require('./images/logo.png')
              :  -->
      <!-- <div class="headIcon" @click.stop="iconDropDown('menu')">
        <i class="simo iconn-menuconf" />
      </div> -->
    </div>
    <h4 class="navTittle">
      <span @click.stop="iconDropDown('menu')">
        {{ typeTitle }}
        <!-- <i class="icon-n-unfold" /> -->
        <i
          :class="
            openName === 'menu' && isOpen ? 'icon-n-putAway' : 'icon-n-unfold'
          "
        />
      </span>
    </h4>
    <ul class="navRight">
      <li v-for="(v, k) in rightMenu" :key="k" @click="toggleMenu(v)">
        <img
          v-if="!Number.isNaN(Number(v.iclass))"
          class="header_img"
          :src="'mc/menu/icon/' + v.iclass"
        />
        <i v-else :class="['simo', v.iclass]" />
      </li>
      <li @click.stop="iconDropDown('msg')">
        <i class="simo icon-n-xiaoxi" />
        <span class="msgNum">
          {{ msgNum > 99 ? '99+' : msgNum }}
        </span>
        <MsgPop
          v-if="openName === 'msg' && isOpen"
          @close-pop="closePop"
          @show-all-msg="showAllMsg"
          :urlArry="MenuUrlArry"
          @opencontent="openContent"
        />
      </li>
      <!-- <li>
        <i class="simo iconn-skin" @click.stop="iconDropDown('skin')" />
        <ul v-show="openName === 'skin' && isOpen" class="skin">
          <li v-for="(v, k) in iconSkin" :key="k" @click="changeSkin(k)">
            {{ v }}主题
          </li>
        </ul>
      </li> -->
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
      <div class="drop-Menu-wrap flex1">
        <template v-for="item in dropPramMenu">
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
                <i v-else class="left_icon simo" :class="item.iclass" />
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
      </div>
      <div class="drop-menu-comps">
        <ul v-if="dropPramCompsMenu.length > 0">
          <li
            v-for="item in dropPramCompsMenu"
            :key="item.id"
            @click="toggleMenu(item)"
          >
            <div>
              <i :class="item.iclass" />
            </div>
            {{ item.name }}
          </li>
        </ul>
      </div>
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
    <div v-if="myUsMsgConf.show">
      <UsMsgConf :mdpram="myUsMsgConf" />
      <!-- <Component
        :is="curModal"
        :value.sync="showModal"
      /> -->
    </div>
    <div v-if="menuMoveMd.isShow">
      <!-- <UsMenuMove :mdpram="menuMoveMd" /> -->
    </div>
    <Modal
      v-model="showContent"
      class="msgdefultBox us-md"
      footer-hide
      title="消息内容"
      @on-cancel="confirm"
    >
      <div class="msgBox">
        {{ msgRow.content }}
      </div>
    </Modal>
  </div>
</template>

<script>
// import { gbs } from "@/config/settings.js";
import { navImg } from './menuIcon'

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
      dropPramCompsMenu: [],
      dropPramMenu: [],
      comps: ['SYS01', 'USERAUTH01', 'MSG01', 'DS_00', 'KBMenu'],
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
      MenuUrlArry: {},
      theme: 'default',
      typeId: '',
      typeTitle: '数据可视化',
      msgNum: ''
    }
  },
  created () {
    this.axios.get('/mc/getCustomMenu?' + new Date().getTime()).then(res => {
      if (res.success) {
        this.menu = res.obj
        this.dropPramCompsMenu = []
        this.dropPramMenu = []
        this.menu.forEach(element => {
          if (element.modelName) {
            this.MenuUrlArry[element.modelName] = element.url
          }
          element.items.forEach(data => {
            if (data.modelName) {
              this.MenuUrlArry[data.modelName] = data.url
            }
            if (this.comps.includes(data.menuId)) {
              this.dropPramCompsMenu.push(data)
            } else {
              this.dropPramMenu.push(data)
            }
          })
        })
      }
    })
    // this.axios.get("/mc/getMenu?" + new Date().getTime()).then((res) => {
    //   if (res.success) {
    //     this.menu = res.obj;
    //   }
    // });
  },
  computed: {
    iclassMap () {
      const map = {}
      const arr = this.menu[0] ? this.menu[0].items || [] : []
      arr.length &&
        arr.forEach(d => {
          if (Number.isNaN(Number(d.iclass))) {
            // 非图片
            if (navImg.includes(d.iclass)) {
              // 系统图片
              var imgName = d.iclass.split('icon-n-')[1]
              map[d.iclass] = require(`./images/head/${imgName +
                (this.theme === 'default' ? '.png' : '_light.png')}`)
              // map[d.iclass] = require("./images/head/" + imgName + (this.theme === "default" ? "_dark.png" : "_light.png"));
            }
          }
        })
      map['imgBg'] = require('./images/head/nav_bottom_bg' +
        (this.theme === 'default' ? '.png' : '_light.png'))
      map['nameBg'] = require('./images/head/nav_name_bg' +
        (this.theme === 'default' ? '.png' : '_light.png'))
      map['system'] = require('./images/head/system' +
        (this.theme === 'default' ? '.png' : '_light.png'))
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
      // this.$notify({
      //   message: "更新皮肤成功！",
      //   position: "bottom-right",
      //   customClass: "toast toast-info",
      // });
      this.isOpen = ''
    },
    msgConf () {
      this.myUsMsgConf.show = true
      // this.curModal = 'UsMsgConf'
    },

    logout () {
      this.$ensureModal.confirm('确认退出系统？', () => {
        window.location.href = window.location.origin + '/mc/logout'
      })
    },

    getThirdPlat () {
      this.axios.post('/mc/bus/search', { pagination: false }).then(res => {
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
      const formData = new FormData()
      formData.append('ids', this.msgRow.id)
      this.axios.post('/msg/mailbox/tagRead', formData).then(res => {
        this.showContent = false
      })
    },
    toggleMenu (item) {
      if (item.url) {
        window.location = window.location.origin + item.url
      }
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
    text-align: center;
    background: url(./images/head-bg.png);
    // background-color: #141d2d;
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
    font-size: 16px;
    margin-left: 15px;
    line-height: 50px;
    cursor: pointer;
    font-weight: 400;
    color: #436bf6;
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
    width: 100%;
    position: absolute;
    top: 50px;
    z-index: 9999;
    height: 670px;
    background-color: #fff;
    background-position: 50%;
    background-repeat: no-repeat;
    background-size: cover;
    padding: 10px;
    display: flex;
    flex-wrap: nowrap;
    background: url(./images/head/bg.png);
    background-size: 100% 100%;
    flex-direction: column;
    box-shadow: 0 1px 4px 0 #0015291f;

    .drop-Menu-wrap {
      width: 100%;
      overflow: auto;
      padding: 10px;
      flex: 1;
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
          // background: rgba(0, 71, 179, 0.08);
          border-radius: 9px;
          box-shadow: 0 0 1px #3c96ff;
          box-shadow: -1px 0px 1px #4650fb, 0px -1px 1px #3c96ff,
            1px 0px 1px #4650fb, 0px 1px 1px #4650fb;
        }
        .head-nav-img {
          position: relative;
          width: 65px;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
          margin: 0 10px 0 20px;
          .head-nav-curimg {
            height: 40px;
            width: 40px;
            margin-top: -14px;
          }
          .head-nav-img-bg {
            width: 65px;
            height: 23px;
            position: absolute;
            top: 52px;
            z-index: -1;
          }
          .left_icon {
            display: block;
            line-height: 38px;
            font-size: 38px;
            position: relative;
            margin-bottom: 14px;
            color: #436bf6;
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
    .drop-menu-comps {
      text-align: center;
      ul {
        background: #fff;
        display: inline-block;
        padding: 10px 8px;
        border-radius: 10px;
        margin-bottom: 20px;
      }
      li {
        color: #436bf6;
        float: left;
        text-align: center;
        font-size: 12px;
        margin: 0 40px;
        cursor: pointer;
        list-style: none;
        i {
          font-size: 24px;
        }
      }
    }
  }
}
[class*=' icon-'],
[class*=' icon-n-'],
[class*=' icon-ux-'],
[class^='icon-'],
[class^='icon-n-'],
[class^='icon-ux-'] {
  line-height: 1;
}
</style>
