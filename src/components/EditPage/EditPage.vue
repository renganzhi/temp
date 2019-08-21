<template>
  <!-- class="wrap moniwrap nofooter" -->
  <div id="editHome-wrap"
       style="padding: 10px">
    <AddPage :showModal="addPage"
             @hideModal="hideModal"></AddPage>
    <PageSetting :showModal="pageSetting"
                 @hideModal="hideSetting"></PageSetting>
    <!-- <SettingPage></SettingPage> -->
    <PreView :showModal="viewPage"
             :viewId="viewId"
             :pageData="pageData"
             @hidePreview="hidePreview"></PreView>
    <Confirm :showModal="showDelModal"
             :message="'删除操作不可恢复，是否继续？'"
             :okText="'是'"
             @hideModal="sureDel"></Confirm>
    <div class="wrap-dialog">
      <div class="wrap-content">
        <div class="wrap-body flex flex-vertical">
          <div class="searchForm">
            <button type="button"
                    @click="add">新增页面</button>
            <button type="button"
                    @click="openSetting">设置</button>
            <button type="button"
                    @click="backHome">返回</button>
          </div>
          <div id="pagesBox"
               class="auto flex flex-wrap flex-1">
            <div v-for="(item,index) in pageList"
                 :key="index"
                 class="page-item flex flex-vertical"
                 @mouseenter="showHover(index)"
                 @mouseleave="cancleHover">
              <img class="page-img"
                   v-if="item.viewImage"
                   :src="baseUrl + item.viewImage" />
              <img class="page-img"
                   v-else />
              <div class="operates"
                   v-show="hoverIndex === index">
                <a class="opera-item"
                   @click.prevent="copy(item)">复制</a>
                <a class="opera-item"
                   @click.prevent="pev(item)">预览</a>
                <a class="opera-item"
                   @click.prevent="edit(item)">编辑</a>
                <a class="opera-item"
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
                <span class="title-name flex-1">{{item.name}}</span>
                <a class="icon-n-edit2 edit-icon"
                   @click="changeEdit(index)"></a>
              </div>
            </div>
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
import { Notification } from 'element-ui'
export default {
  name: 'editPage',
  components: { AddPage, PreView, PageSetting, Confirm, Notification },
  data () {
    return {
      baseUrl: gbs.host,
      pageList: [],
      editIndex: -1,
      hoverIndex: -1,
      showDelModal: false, // 确认删除
      delId: -1,
      addPage: false, // 新增页面
      pageSetting: false, // 设置
      viewPage: false, // 预览
      pageData: '', // 预览的page内容
      viewId: -1, // 预览的id
      editName: ''
    }
  },

  methods: {
    backHome () {
      this.$router.push('/')
    },
    search () {
      this.axios.get('home/homePage/noConf').then((res) => {
        if (res.success) {
          this.pageList = res.obj
        } else {
          // tooltip('', res.msg, 'error')
          Notification({
            message: res.msg,
            position: 'bottom-right',
            customClass: 'toast toast-error'
          })
        }
      })
    },
    hideModal (data) {
      this.addPage = false
      if (data.ifAdd) {
        this.search()
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
      this.axios.get('home/homePage/copy/' + item.id).then((res) => {
        if (res.success) {
          this.search()
        } else {
          // tooltip("", res.msg, "error");
          Notification({
            message: res.msg,
            position: 'bottom-right',
            customClass: 'toast toast-error'
          })
        }
      })
    },
    pev (item) {
      this.viewId = item.id
      this.viewPage = true
    },
    // 关闭预览弹窗
    hidePreview () {
      this.viewPage = false
    },
    edit (item) {
      this.$router.push({
        name: 'edit',
        params: {
          id: item.id
        }
      })
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
        this.axios.delete('home/homePage/deleteById/' + this.delId).then((res) => {
          if (res.success) {
            this.search()
          } else {
            // tooltip("", res.msg, "error");
            Notification({
              message: res.msg,
              position: 'bottom-right',
              customClass: 'toast toast-error'
            })
          }
        })
      }
    },
    changeEdit (index) {
      this.editName = this.pageList[index].name
      this.editIndex = index
      this.$nextTick(function () {
        // cTthis.addValidator()
      })
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
        this.axios.post('home/homePage/edit', qs.stringify(data), { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } })
          .then((res) => {
            item.name = this.editName
            Notification({
              message: '操作成功！',
              position: 'bottom-right',
              customClass: 'toast toast-success'
            })
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
  mounted: function () {
    this.search()
  },
  destoryed: function () {

  }
}
</script>

<style scoped>
.page-item {
  position: relative;
  height: 206px;
  margin: 12px;
  width: 312px;
  box-shadow: 1px 1px 4px 4px #191d2b;
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

.page-item .title-name {
  overflow: hidden;
}

.page-item .edit-icon {
  width: 40px;
  line-height: 40px;
  text-align: center;
  display: inline-block;
  font-size: 12px;
}

#pagesBox {
  margin-left: -6px;
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
</style>
