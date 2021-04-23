<template>
  <div>
    <div id="importPage-modal"
         style="z-index: 20100"
         class="modal in"
         role="dialog"
         aria-hidden="true">
      <div class="modal-dialog modal-sm  modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title">导入</h4>
            <button type="button"
                    class="close"
                    data-dismiss="modal"
                    aria-hidden="true">&times;</button>
          </div>
          <div class="modal-body">
            <form autocomplete="off">
              <div class="form-group">
                <label class="page-lable required-label">待上传文件</label>
                <div class="page-lable-content">
                  <input type="file"
                         accept=".zip"
                         ref="file"
                         @change='changeFile' />
                  <label class="error"
                         v-show="showErr"
                         style="margin-left: 10px; margin-top: 8px;">{{errMsg}}</label>
                </div>
              </div>
              <!-- <div class="form-group"
                   style="margin-bottom: 0">
                <label class="page-lable">选择大屏</label>
                <div class="page-lable-content">
                  <div class="defPages flex">
                    <div class="flex-item first-item"
                         :class="{ active: temId.includes(item.id) }"
                         v-for="(item,index) in tems"
                         :key=index
                         @click="choosePage(item.id)">
                      <img :src="baseUrl + item.viewImage"
                      v-if="item.viewImage"
                           alt=""
                           style="width:100%;height:100%;" />
              <img class="page-img"
                   v-else />
                    </div>
                  </div>
                </div>
              </div> -->
            </form>
          </div>
          <div class="modal-footer">
            <button type="button"
                    @click="save">导入</button>
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
  name: 'exportPage',
  props: ['showModal', 'tems'],
  components: { Notification },
  data () {
    return {
      file: null,
      name: '',
      visible: true,
      baseUrl: gbs.host,
      addId: 0,
      addOne: false,
      errMsg: '必填项',
      showErr: false,
      userIds: [],
      // tems: [],
      // temId: ''
      temId: []
    }
  },
  mounted: function () {
    var _this = this
    // this.getTemps()
    if (this.showModal) {
      $('#importPage-modal').modal('show')
    }
    $('#importPage-modal').on('hide.bs.modal', function () {
      // 关闭模态框时触发
      _this.$emit('hideModal', { ifAdd: _this.addOne, addId: _this.addId })
      _this.addOne = false
    })
  },
  watch: {
    showModal: function (newV) {
      if (newV) {
        $('#importPage-modal').modal('show')
        // this.temId = ''
        this.temId = []
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
    changeFile (e) {
      if (e.value === '') {
        return
      }
      //   console.log('e.target.files[0]: ', e.target.files[0]);
      this.file = e.target.files[0]
    },
    changeName () {
      if (!this.file) {
        this.errMsg = '必填项'
        this.showErr = true
        return
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
        var formdata = new FormData()
        formdata.append('file', this.file)
        this.axios({
          method: 'post',
          url: '/leaderview/home/importTemplate',
          data: formdata,
          headers: { 'content-type': 'application/x-www-form-urlencoded' }
        }).then((res) => {
          $('#importPage-modal').modal('hide')
          if (res.success) {
          //   this.addOne = true
          //   this.addId = res.obj.id
            if (gbs.inDev) {
              Notification({
                message: '上传成功！',
                position: 'bottom-right',
                customClass: 'toast toast-success'
              })
            } else {
              tooltip('', '上传成功！', 'success')
            }
            $('#importPage-modal').modal('hide')
            // reset
            this.$refs.file.value = ''
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
      if (this.temId.includes(id)) {
        // remove
        const index = this.temId.indexOf(id)
        this.temId.splice(index, 1)
      } else {
        this.temId.push(id)
      }
    }
  },
  beforeDestroy: function () {
    $('#importPage-modal').modal('hide')
    $('.modal-backdrop').remove()
  },
  destroyed: function () {
  }
}
</script>
<style scoped lang="scss">
/* 新建页面-弹窗样式 */
#importPage-modal .defPages {
  flex-wrap: wrap;
  height: 400px;
  overflow: auto;
  margin-left: -10px;
}

#importPage-modal .flex-item {
  width: 31%;
  height: 152px;
  box-shadow: 0px 0px 2px 2px #141929;
  margin: 10px;
}

#importPage-modal .flex-item:nth-child(3n + 0) {
  margin-right: 0px !important;
}

#importPage-modal .first-item {
  text-align: center;
  line-height: 152px;
}

#importPage-modal .active {
  outline: 2px solid #0088cc;
}
html[data-theme="blackWhite"],
html[data-theme="blueWhite"] {
  #importPage-modal .flex-item {
    box-shadow: 0 0 6px rgba(0, 0, 0, 0.2);
  }
}
html[data-theme="blueWhite"] {
  #importPage-modal .active {
    outline: 2px solid #60abff;
  }
}
html[data-theme="blackWhite"] {
  #importPage-modal .active {
    outline: 2px solid #026bf4;
  }
}
</style>
