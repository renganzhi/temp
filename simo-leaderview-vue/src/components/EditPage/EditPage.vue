<template>
  <!-- class="wrap moniwrap nofooter" -->
  <!-- padding: 10px; padding-bottom: 0px; -->
  <div id="editHome-wrap"
       style="height: 100%; padding: 15px;">
    <AddPage :showModal="addPage"
             @hideModal="hideModal"></AddPage>
    <PageSetting :showModal="pageSetting"
                 @hideModal="hideSetting"></PageSetting>
    <!-- <SettingPage></SettingPage> -->
    <PreView :showModal="viewPage"
             :viewId="viewId"
             :pageData="pageData"
             :key="viewKey"
             @hidePreview="hidePreview"></PreView>
    <Confirm :showModal="showDelModal"
             :message="'删除操作不可恢复，是否继续？'"
             :okText="'是'"
             @hideModal="sureDel"></Confirm>
    <div class="wrap-dialog">
      <div class="wrap-content">
        <div class="wrap-body flex flex-vertical">
          <div class="searchForm">
            <select name="pageType"
                    v-model="pageType"
                    @change="changePage"
                    style="margin-right: 10px;">
              <option value="1">全部页面</option>
              <option value="2">我的页面</option>
              <option value="3">分享的页面</option>
            </select>
            <button type="button"
                    v-if="access === 'w'"
                    @click="add">新增页面</button>
            <button type="button"
                    @click="openSetting">设置</button>
            <button type="button"
                    class="homeBack"
                    @click="backHome"><i class="icon-n-back"></i> 返回</button>
          </div>
          <div id="pagesBox"
               class="auto flex flex-wrap flex-1">
            <div v-for="(item,index) in pageList"
                 :key="index"
                 class="page-item flex flex-vertical"
                 @mouseenter="showHover(index)"
                 @mouseleave="cancleHover">
              <div :class="{'canSee': true, 'notSee': !item.visible}"></div>
              <img class="page-img"
                   v-if="item.viewImage"
                   :src="baseUrl + item.viewImage" />
              <img class="page-img"
                   v-else />
              <div class="operates"
                   v-show="hoverIndex === index">

                <a class="opera-item noUse"
                   v-if="item.belongCurrentUser === 'false' || access !== 'w'">复制</a>
                <a class="opera-item"
                   v-else
                   @click.prevent="copy(item)">复制</a>

                <a class="opera-item"
                   @click.prevent="pev(item)">预览</a>

                <a class="opera-item noUse"
                   v-if="item.belongCurrentUser === 'false' || access !== 'w'">编辑</a>
                <a class="opera-item"
                   v-else
                   @click.prevent="edit(item)">编辑</a>

                <a class="opera-item noUse"
                   v-if="item.belongCurrentUser === 'false' || access !== 'w'">删除</a>
                <a class="opera-item"
                   v-else
                   @click.prevent="del(item)">删除</a>

              </div>
              <div v-if="editIndex === index"
                   class="page-title titleShow">
                <form autocomplete="off">
                  <input name="name"
                         v-model="editName" />
                </form>
                <span class="operate-title">
                  <a class="simoLink"
                     @click="changeName(index,item)">确定</a>
                  <a class="cancle"
                     @click="cancleChange(index)">取消</a>
                </span>
              </div>
              <div v-else
                   class="page-title flex-1 flex">
                <span class="shareIcon"
                      v-show="item.belongCurrentUser === 'false'"><i class="icon-n-assetys"
                     :title="'负责人：' + item.shareName"></i></span>
                <span class="title-name flex-1">{{item.name}}</span>

                <a class="icon-n-edit2 edit-icon noClk"
                   v-if="(item.belongCurrentUser === 'false' || access !== 'w') && !isSuperAdmin"></a>
                <a class="icon-n-edit2 edit-icon"
                   v-else
                   @click="changeEdit(index)"></a>

                <a class="icon-n-share edit-icon noClk"
                   v-if="item.belongCurrentUser === 'false' || access !== 'w'"></a>
                <a class="icon-n-share edit-icon"
                   v-else
                   @click="toShare(item)"></a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div id="homeShareModal"
         class="modal"
         style="z-index: 10086">
      <div class="modal-dialog"
           role="document"
           style="margin: 206px auto;">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button"
                    class="close"
                    data-dismiss="modal"
                    aria-hidden="true">
              <span aria-hidden="true">×</span>
            </button>
            <h4 class="modal-title">
              <span class="pre-page"
                    data-dismiss="modal"></span>
              <span class="now-page">分享设置</span>
            </h4>
          </div>
          <div class="modal-body">
            <form autocomplete="off"
                  id="shareFm1">
              <div class="form-group">
                <label class="page-lable">分享给用户</label>
                <div class="page-lable-content">
                  <!-- <Select2 v-if="v.type=='drop-down' || v.type=='multi-select'" :name="v.key"
                                                      v-model="syst.curConf.params[v.key]" :obj="v" @input="chgSelects(v)">
                                            </Select2> -->
                  <select id="shareUsers"
                          v-model="shareUsers">
                    <option v-for="(user, index) in userList"
                            :value="user.id"
                            :key="index">{{user.userName}}({{user.loginName}})</option>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label class="page-lable">分享给角色</label>
                <div class="page-lable-content">
                  <select v-model="shareRoles"
                          id="shareRoles">
                    <option v-for="(role, index) in roleList"
                            :value="role.id"
                            :key="index">{{role.name}}</option>
                  </select>
                </div>
              </div>
              <div class="form-group"
                   id="hasChild"
                   style="display: none;">
                <label class="page-lable">
                  <input type="checkbox"
                         name="ifShareSub">
                </label>
                <div class="page-lable-content">
                  <span class="share-checkcontent">同时分享其子结构</span>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button"
                    @click="sureShare">确认</button>
            <button type="button"
                    data-dismiss="modal">取消</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import qs from 'qs'
import AddPage from './AddPage'
import PageSetting from './PageSetting'
import PreView from './../PreView/PreView'
import { gbs } from '@/config/settings'
import Confirm from './../Common/Confirm'
import Select2 from './../Common/Select2'
import { Notification } from 'element-ui'
export default {
  name: 'editPage',
  components: { AddPage, PreView, PageSetting, Confirm, Select2, Notification },
  data () {
    return {
      baseUrl: gbs.host,
      pageList: [],
      allPage: [], // 全部页面
      access: 'r',
      isSuperAdmin: false,
      editIndex: -1,
      hoverIndex: -1,
      pageType: '1',
      shareItem: {},
      shareId: 0,
      shareUsers: [],
      shareRoles: [],
      userList: [],
      roleList: [],
      showDelModal: false, // 确认删除
      delId: -1,
      addPage: false, // 新增页面
      pageSetting: false, // 设置
      viewPage: false, // 预览
      pageData: '', // 预览的page内容
      viewId: -1, // 预览的id
      editName: '',
      userIds: [],
      viewKey: new Date().getTime() + parseInt(Math.random() * 70)
    }
  },

  methods: {
    backHome () {
      this.$router.push('/')
    },
    getAdminUsers () {
      // 获取超级管理员角色下的所有用户
      return new Promise((resolve, reject) => {
        this.axios.get('/mc/role/findAllUserByRoleId?roleIds=1').then((res) => {
          if (res.success) {
            this.userIds = res.obj
            return resolve()
          }
          if (gbs.inDev) {
            Notification({
              message: res.msg,
              position: 'bottom-right',
              customClass: 'toast toast-error'
            })
          } else {
            tooltip('', res.msg, 'error')
          }
        })
      })
    },
    changePage () {
      this.search()
    },
    changePageType () {
      let type = this.pageType
      if (type === '3') {
        this.pageList = _.filter(this.allPage, function (o) { return o.belongCurrentUser === 'false' })
      } else if (type === '2') {
        this.pageList = _.filter(this.allPage, function (o) { return o.belongCurrentUser === 'true' })
      } else {
        this.pageList = this.allPage
      }
    },
    toShare (item) {
      this.shareId = item.id
      if (item.shareConf) {
        this.shareItem = JSON.parse(item.shareConf)
        this.shareUsers = this.shareItem.uids || []
        this.shareRoles = this.shareItem.roles || []
      } else {
        this.shareItem = {}
        this.shareRoles = []
        this.shareUsers = []
      }
      this.initSelect2('shareUsers', this.shareUsers)
      this.initSelect2('shareRoles', this.shareRoles)
      $('#homeShareModal').modal('show')
    },
    sureShare () {
      var data = {}
      data.roles = (this.shareRoles && this.shareRoles.length > 0) ? this.shareRoles.join(',') : ''
      data.uids = (this.shareUsers && this.shareUsers.length > 0) ? this.shareUsers.join(',') : ''

      this.axios.get('/mc/role/findAllUserByRoleId?roleIds=' + data.roles).then((object) => {
        if (object.success) {
          data.uidsByRoles = object.obj.join(',')
          this.axios({
            method: 'post',
            url: '/leaderview/home/share/' + this.shareId,
            data: qs.stringify(data),
            headers: { 'content-type': 'application/x-www-form-urlencoded' }
          }).then((res) => {
            if (res.success) {
              this.search()
              $('#homeShareModal').modal('hide')
              if (gbs.inDev) {
                Notification({
                  message: '操作成功！',
                  position: 'bottom-right',
                  customClass: 'toast toast-success'
                })
              } else {
                tooltip('', '操作成功！', 'success')
              }
            } else {
              if (gbs.inDev) {
                Notification({
                  message: res.msg,
                  position: 'bottom-right',
                  customClass: 'toast toast-error'
                })
              } else {
                tooltip('', res.msg, 'error')
              }
            }
          })
        }
      })
    },
    saerchShareUser () {
      this.axios.get('/user/findAvailableUsers?isNotMe=true').then((res) => {
        if (res.success) {
          this.userList = res.obj
          this.initSelect2('shareUsers')
          this.initSelect2User()
        }
      })
      this.axios.get('/role/findAllEnableRoles').then((res) => {
        if (res.success) {
          this.roleList = res.obj
          this.initSelect2('shareRoles')
          this.initSelect2Role()
        }
      })
    },
    initSelect2 (id, v) {
      var value = typeof v === 'undefined' ? [] : v
      $('#' + id).select2({
        multiple: true,
        closeOnSelect: false
      }).val(value).trigger('change')
    },
    initSelect2User () {
      var _this = this
      $('#shareUsers').on('change', function () {
        // vm.$emit('input', $(this).val())
        _this.shareUsers = $(this).val() ? $(this).val() : []
      }).on('select2:selecting', function (e) {
        if (e.params && e.params.args && e.params.args.data) {
          var v = $(this).val()
          if (e.params.args.data.id === '') { // 选择不限
            $(this).val([])
          } else { // 选中其他
            if (v && v.indexOf('') !== -1) {
              $(this).val([e.params.args.data.id])
            }
          }
        }
      }).on('select2:unselecting', function (e) {
        if (e.params && e.params.args && e.params.args.data) {
          var v = $(this).val()
          if (v && v.length === 1) {
            if (e.params.args.data.id === '') {
              e.preventDefault()
            }
            $(this).val([])
          }
        }
      })
    },
    initSelect2Role () {
      var _this = this
      $('#shareRoles').on('change', function () {
        _this.shareRoles = $(this).val() ? $(this).val() : []
      }).on('select2:selecting', function (e) {
        if (e.params && e.params.args && e.params.args.data) {
          var v = $(this).val()
          if (e.params.args.data.id === '') { // 选择不限
            $(this).val([])
          } else { // 选中其他
            if (v && v.indexOf('') !== -1) {
              $(this).val([e.params.args.data.id])
            }
          }
        }
      }).on('select2:unselecting', function (e) {
        if (e.params && e.params.args && e.params.args.data) {
          var v = $(this).val()
          if (v && v.length === 1) {
            if (e.params.args.data.id === '') {
              e.preventDefault()
            }
            $(this).val([])
          }
        }
      })
    },
    search () {
      this.axios.get('/leaderview/home/homePage/noConf').then((res) => {
        if (res.success) {
          // this.pageList = res.obj
          this.allPage = res.obj
          this.changePageType()
          if (!gbs.inDev) {
            this.$nextTick(() => {
              titleShow('bottom', $('.shareIcon'))
            })
          }
        } else {
          if (gbs.inDev) {
            Notification({
              message: res.msg,
              position: 'bottom-right',
              customClass: 'toast toast-error'
            })
          } else {
            tooltip('', res.msg, 'error')
          }
        }
      })
    },
    getAccess () {
      this.access = sessionStorage.getItem('leaderAccess') || 'r'
      this.axios.get('/leaderview/home/validSuperAdmin').then((res) => {
        if (res.success) {
          this.isSuperAdmin = res.obj.isSuperAdmin
        }
      })
    },
    hideModal (data) {
      this.addPage = false
      if (data.ifAdd) {
        // this.search()
        this.$router.push('/edit/' + data.addId)
      }
    },
    hideSetting (data) {
      this.pageSetting = false
      if (data.ifSort) {
        this.search()
      }
    },
    openSetting () {
      this.pageSetting = true
      /* $.comps.homeCarouselConf.open({
        callback: function () {
          // cTthis.search()
        }
      }) */
    },
    add () {
      this.addPage = true
      /* var _this = this
      $.comps.addHome.open({
        callback: function (res) {
          _this.edit(res)
        }
      }) */
    },
    copy (item) {
      this.getAdminUsers().then(() => {
        this.axios.get('/leaderview/home/homePage/copy', { params: { 'pageId': item.id, adminId: this.userIds.join(',') } }).then((res) => {
          if (res.success) {
            this.search()
          } else {
            if (gbs.inDev) {
              Notification({
                message: res.msg,
                position: 'bottom-right',
                customClass: 'toast toast-error'
              })
            } else {
              tooltip('', res.msg, 'error')
            }
          }
        })
      })
    },
    pev (item) {
      this.viewId = item.id
      this.viewKey = new Date().getTime() + parseInt(Math.random() * 70)
      this.$nextTick(() => {
        this.viewPage = true
      })
    },
    // 关闭预览弹窗
    hidePreview () {
      this.viewPage = false
    },
    edit (item) {
      this.$router.push('/edit/' + item.id)
      // this.$router.push({
      //   name: 'edit',
      //   params: {
      //     id: item.id
      //   }
      // })
    },
    del (item) {
      this.showDelModal = true
      this.delId = item.id
      // $.api.view.confirm({
      //   showCancel: false,
      //   showNo: true,
      //   msg: '删除操作不可恢复，是否继续？',
      //   callBack: function (state) {
      //     if (state == 1) {
      //       $.api.home.delPage({
      //         id: item.id,
      //         callback: function () {
      //           cTthis.search();
      //           tooltip("", "操作成功！", "success");
      //         }
      //       })
      //     }
      //   }
      // });
    },
    sureDel (data) {
      this.showDelModal = false
      if (data && data.sure === '1') {
        this.axios.delete('/leaderview/home/homePage/deleteById/' + this.delId).then((res) => {
          if (res.success) {
            this.search()
          } else {
            if (gbs.inDev) {
              Notification({
                message: res.msg,
                position: 'bottom-right',
                customClass: 'toast toast-error'
              })
            } else {
              tooltip('', res.msg, 'error')
            }
          }
        })
      }
    },
    changeEdit (index) {
      this.editName = this.pageList[index].name
      this.editIndex = index
    },
    changeName (index, item) {
      // var _this = this
      // if (!cTthis.validator.form()) {
      //   return; // 这里校验输入是否合法
      // }
      // if (this.editName !== '') {
      //   $.api.home.savePageName({
      //     data: {
      //       name: this.editName,
      //       id: item.id
      //     },
      //     callback: function () {
      //       item.name = _this.editName;
      //       tooltip("", "操作成功！", "success");
      //     }
      //   })
      // }
      if (this.editName !== '') {
        var data = { 'name': this.editName, 'id': item.id }
        this.axios.post('/leaderview/home/homePage/edit', qs.stringify(data), { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } })
          .then((res) => {
            item.name = this.editName
            if (gbs.inDev) {
              Notification({
                message: '操作成功！',
                position: 'bottom-right',
                customClass: 'toast toast-success'
              })
            } else {
              tooltip('', '操作成功！', 'success')
            }
          })
      }
      this.editIndex = -1
    },
    cancleChange (index) {
      this.editIndex = -1
    },
    showHover (index) {
      this.hoverIndex = index
    },
    cancleHover () {
      this.hoverIndex = -1
    },
    exitwrap () {
      // typeof cTthis.pram.exitCb === 'function' && cTthis.pram.exitCb()
    }
  },
  watch: {
    editName: function (newV, oldV) {
      // 缩略图编辑校验，不能输入特殊字符
      if (newV.length > 15) {
        this.editName = newV.slice(0, 15)
      } else {
        var str = new RegExp("[`~!@#$^*|{}';',<>》《~！@#￥……*——|{}【】‘；”“'。，、？]")
        var flag = (!str.test(newV)) && !/\s/.test(newV)
        if (!flag) {
          this.editName = oldV
        }
      }
    }
  },
  mounted: function () {
    this.search()
    var _url = window.location.protocol + '//' + window.location.host + '/index'
    window.history.pushState({}, '', _url)
    this.getAccess()
    this.saerchShareUser()
  },
  beforeDestroy: function () {
    $('.modal-backdrop').remove()
  },
  destroyed: function () {
    if ($('.tooltip').length > 0) {
      $(this.$el).find('[title]').tooltip('destroy')
    }
  }
}
</script>

<style scoped lang="scss">
.homeBack {
  float: right;
  background: transparent;
  color: #0088cc;
  font-size: 13px;
  border: none;
  i {
    font-size: 15px;
    margin-right: 2px;
  }
}

.page-item {
  position: relative;
  height: 206px;
  margin: 12px;
  width: 310px;
  box-shadow: 2px 3px 10px rgba(0, 0, 0, 0.25);
  .canSee {
    position: absolute;
    top: 0px;
    right: 0px;
    width: 30px;
    height: 30px;
    background: url("../../assets/canSee.png");
  }
  .notSee {
    background: url("../../assets/noSee.png");
  }
}

.page-item .page-title {
  line-height: 40px;
  padding-left: 15px;
  overflow: hidden;
}

.page-item .page-img {
  width: 100%;
  height: 165px;
  visibility: hidden;
}

.page-item .page-img[src] {
  visibility: visible;
}

.page-item .operates {
  position: absolute;
  width: 100%;
  left: 0;
  bottom: 40px;
  height: 40px;
  line-height: 40px;
  background-color: rgba(13, 17, 31, 0.8);
}

.page-item .operates a {
  display: inline-block;
  width: 24%;
  text-align: center;
  color: #666f8b;
  margin: 0px;
  padding: 0px;
}

.page-item .operates a:hover {
  color: #0088cc;
}

.page-item .operates .noUse {
  color: #3d3d3e !important;
}

.page-item .title-name {
  overflow: hidden;
}

.page-item .shareIcon {
  width: 20px;
  height: 20px;
  text-align: center;
  border-radius: 50%;
  background: #0088cc;
  float: left;
  margin-top: 10px;
  margin-right: 10px;
  i {
    width: 20px;
    height: 20px;
    font-size: 12px;
    font-weight: bold;
    position: relative;
    top: -10px;
    color: rgba(13, 17, 31, 1);
  }
}

.page-item .edit-icon {
  width: 28px;
  line-height: 40px;
  text-align: center;
  display: inline-block;
  font-size: 12px;
}
.page-item .edit-icon.noClk {
  // color: #5b5f6d;
  color: #494d5a;
}
.page-item .icon-n-share {
  font-size: 13px;
  margin-right: 10px;
}
#pagesBox {
  margin-left: -6px;
  height: 100%;
  overflow: auto;
}

#pagesBox form {
  width: 74%;
  display: inline-block;
}

#pagesBox .titleShow input {
  width: 100%;
  box-sizing: border-box;
}

#pagesBox .titleShow .operate-title {
  position: absolute;
  right: 0px;
}

#pagesBox .titleShow .operate-title a {
  margin-right: 6px;
}

#pagesBox .titleShow .cancle {
  color: #666f8b;
}
.wrap-dialog,
.wrap-content,
.wrap-body {
  height: 100%;
}
html[data-theme="blackWhite"],
html[data-theme="blueWhite"] {
  .page-item .operates .noUse {
    color: #a9a9a9 !important;
  }
  .page-item .operates {
    background-color: #fff;
  }
  .page-item {
    background: #f6f6f6;
    box-shadow: 0px 5px 10px rgba(0, 0, 0, 0.07);
    .title-name {
      color: #50607c;
    }
    .edit-icon {
      color: #828bac;
    }
    .page-title {
      background-color: #fff;
    }
  }
  .page-item:hover {
    box-shadow: 0px 5px 10px rgba(0, 0, 0, 0.17);
    .edit-icon {
      color: #323746;
    }
    .noClk {
      color: #828bac;
    }
    .operates {
      background-color: #334153;
      opacity: 0.6;
    }
    .opera-item {
      color: #9ca6b5;
    }
    .opera-item:hover {
      color: #fefeff;
    }
  }
  .page-item .shareIcon i {
    color: #fff;
  }
}
</style>
