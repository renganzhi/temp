<template>
  <div>
    <div id="addHomePage-modal"
         style="z-index: 20100"
         class="modal in"
         role="dialog"
         aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button"
                    class="close"
                    data-dismiss="modal"
                    aria-hidden="true">&times;</button>
            <h4 class="modal-title">新建页面</h4>
          </div>
          <div class="modal-body">
            <form autocomplete="off">
              <div class="form-group">
                <label class="page-lable required-label">页面名称</label>
                <div class="page-lable-content">
                  <input type="text"
                         :style="{'border': showErr ? '1px solid red !important' : ''}"
                         @input="changeName"
                         v-model="name"
                         name="name" />
                  <label class="error"
                         v-show="showErr"
                         style="margin-left: 10px; margin-top: 8px;">{{errMsg}}</label>
                </div>
              </div>
              <div class="form-group"
                   style="line-height: 28px;">
                <label class="page-lable"
                       style="margin-right: 15px;">是否可见</label>
                <input type="radio"
                       name="visible"
                       v-model="visible"
                       value="true" /> 是
                <input type="radio"
                       name="visible"
                       v-model="visible"
                       style="margin-left: 15px;"
                       value="false" /> 否
              </div>
              <div class="form-group"
                   style="margin-bottom: 0">
                <label class="page-lable">选择模板</label>
                <div class="page-lable-content">
                  <div class="defPages flex">
                    <div class="flex-item first-item"
                         :class="{active:temId===''}"
                         @click="choosePage('')">
                      空白页面
                    </div>
                    <div class="flex-item first-item"
                         :class="{ active:temId===item.id }"
                         v-for="(item,index) in tems"
                         :key=index
                         @click="choosePage(item.id)">
                      <img :src="baseUrl + item.viewImage"
                           alt=""
                           style="width:100%;height:100%;" />
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button"
                    @click="save">确定</button>
            <button type="button"
                    data-dismiss="modal">取消</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { gbs } from '@/config/settings'
import { Notification } from 'element-ui'
import qs from 'qs'
export default {
  name: 'addPage',
  props: ['showModal'],
  components: { Notification },
  data () {
    return {
      name: '',
      visible: true,
      baseUrl: gbs.host,
      addId: 0,
      addOne: false,
      errMsg: '必填项',
      showErr: false,
      userIds: [],
      tems: [],
      temId: ''
    }
  },
  mounted: function () {
    var _this = this
    this.getTemps()
    if (this.showModal) {
      $('#addHomePage-modal').modal('show')
    }
    $('#addHomePage-modal').on('hide.bs.modal', function () {
      // 关闭模态框时触发
      _this.$emit('hideModal', { ifAdd: _this.addOne, addId: _this.addId })
      _this.addOne = false
    })
  },
  watch: {
    showModal: function (newV) {
      if (newV) {
        $('#addHomePage-modal').modal('show')
        this.temId = ''
        this.name = ''
      }
    }
  },
  methods: {
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
    changeName () {
      if (!this.name) {
        this.errMsg = '必填项'
        this.showErr = true
        return
      } else if (this.name.length > 15) {
        this.errMsg = '最大长度为15个字符'
        this.showErr = true
        return
      } else {
        var str = new RegExp("[`~!@#$^*|{}';',<>》《~！@#￥……*——|{}【】‘；”“'。，、？]")
        var flag = (!str.test(this.name)) && !/\s/.test(this.name)
        if (!flag) {
          this.errMsg = '不能含有特殊字符'
          this.showErr = true
          return
        }
      }
      this.showErr = false
    },
    getTemps () {
      // this.tems.splice(0, this.tems.length)
      this.axios.get('/leaderview/home/template/list').then((res) => {
        this.tems = res.obj
      })
      /*  this.$nextTick(function(){
        _this.$modal.find('.defPages').get(0).scrollTop = 0;
      }) */
    },
    save () {
      // 校验名称
      this.changeName()
      if (this.showErr) return
      this.getAdminUsers().then(() => {
        var data = {
          name: this.name,
          templateId: this.temId,
          visible: this.visible,
          adminId: this.userIds.join(',')
        }
        this.axios({
          method: 'post',
          url: '/leaderview/home/homePage/add',
          data: qs.stringify(data),
          headers: { 'content-type': 'application/x-www-form-urlencoded' }
        }).then((res) => {
          if (res.success) {
            this.addOne = true
            this.addId = res.obj.id
            if (gbs.inDev) {
              Notification({
                message: '操作成功！',
                position: 'bottom-right',
                customClass: 'toast toast-success'
              })
            } else {
              tooltip('', '操作成功！', 'success')
            }
            $('#addHomePage-modal').modal('hide')
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
    choosePage (id) {
      this.temId = id
    }
  },
  beforeDestroy: function () {
    $('#addHomePage-modal').modal('hide')
    $('.modal-backdrop').remove()
  },
  destroyed: function () {
  }
}
</script>
<style scoped lang="scss">
/* 新建页面-弹窗样式 */
#addHomePage-modal .defPages {
  flex-wrap: wrap;
  height: 400px;
  overflow: auto;
  margin-left: -10px;
}

#addHomePage-modal .flex-item {
  width: 31%;
  height: 152px;
  box-shadow: 0px 0px 2px 2px #141929;
  margin: 10px;
}

#addHomePage-modal .flex-item:nth-child(3n + 0) {
  margin-right: 0px !important;
}

#addHomePage-modal .first-item {
  text-align: center;
  line-height: 152px;
}

#addHomePage-modal .active {
  outline: 2px solid #0088cc;
}
html[data-theme="blackWhite"],
html[data-theme="blueWhite"] {
  #addHomePage-modal .flex-item {
    box-shadow: 0 0 6px rgba(0, 0, 0, 0.2);
  }
}
html[data-theme="blueWhite"] {
  #addHomePage-modal .active {
    outline: 2px solid #60abff;
  }
}
html[data-theme="blackWhite"] {
  #addHomePage-modal .active {
    outline: 2px solid #026bf4;
  }
}
</style>
