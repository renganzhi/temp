<template>
  <div>
    <div
      id="addHomePage-modal"
      style="z-index: 20100"
      class="modal in"
      role="dialog"
      aria-hidden="false"
      data-backdrop="static"
    >
      <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title">新建页面</h4>
            <button
              type="button"
              class="close"
              data-dismiss="modal"
              aria-hidden="true"
            >
              <i class="ivu-icon ivu-icon-ios-close"></i>
            </button>
          </div>
          <div class="modal-body">
            <form autocomplete="off">
              <div class="form-group">
                <label class="page-lable required-label">页面名称</label>
                <div class="page-lable-content">
                  <input
                    type="text"
                    :style="{
                      border: showErr ? '1px solid red !important' : ''
                    }"
                    @input="changeName"
                    v-model="name"
                    name="name"
                  />
                  <label
                    class="error"
                    v-show="showErr"
                    style="margin-left: 10px; margin-top: 8px;"
                    >{{ errMsg }}</label
                  >
                </div>
              </div>
              <div class="form-group" style="line-height: 28px;">
                <label class="page-lable" style="margin-right: 15px;"
                  >是否可见</label
                >
                <input
                  type="radio"
                  name="visible"
                  v-model="visible"
                  value="true"
                />
                是
                <input
                  type="radio"
                  name="visible"
                  v-model="visible"
                  style="margin-left: 15px;"
                  value="false"
                />
                否
              </div>
              <div class="form-group" style="margin-bottom: 0">
                <label class="page-lable" style="margin-right: 15px;"
                  >选择模板</label
                >
                <div class="inputArry" style="height: 28px;line-height: 28px;">
                  <input
                    type="radio"
                    id="pageTypestrue"
                    name="pageTypes"
                    v-model="pageTypes"
                    :value="true"
                  />
                  <label for="pageTypestrue">
                    一键应用
                    <Tooltip
                      content="此模板中，展示的图标，统计数据均已内置完毕，仅需单独选择资源即可。（部分页面为整体统计，无需选择资源）"
                      placement="top"
                      max-width="400"
                    >
                      <Icon type="ios-help-circle" style="font-size: 18px;" />
                    </Tooltip>
                  </label>
                  <input
                    type="radio"
                    name="pageTypes"
                    id="pageTypesfalse"
                    v-model="pageTypes"
                    style="margin-left: 15px;margin-bottom: 10px;"
                    :value="false"
                  />
                  <label for="pageTypesfalse">
                    自定义
                    <Tooltip
                      content="此模板中，所有内容均可自定义设置，包括图表样式，显示的数据，统计的资源等。"
                      placement="top"
                      max-width="400"
                    >
                      <Icon type="ios-help-circle" style="font-size: 18px;" />
                    </Tooltip>
                  </label>
                </div>
                <div class="page-lable-content">
                  <div class="defPages flex" v-if="!pageTypes">
                    <div
                      class="flex-item first-item"
                      :class="{ active: temId === '' }"
                      @click="choosePage('')"
                    >
                      空白页面
                    </div>
                    <div
                      class="flex-item first-item"
                      :class="{ active: temId === item.id }"
                      v-for="(item, index) in tems1"
                      :key="index"
                      @click="choosePage(item.id)"
                    >
                      <img
                        :src="baseUrl + item.viewImage"
                        alt=""
                        style="width:100%;height:100%;"
                      />
                      <div
                        class="mask"
                        :class="{ maskActive: temId === item.id }"
                      >
                        <div class="mask-text">{{ item.name }}</div>
                      </div>
                    </div>
                  </div>
                  <div class="defPages flex" v-if="pageTypes">
                    <div
                      class="flex-item first-item"
                      :class="{ active: temId === item.id }"
                      v-for="(item, index) in tems2"
                      :key="index"
                      @click="choosePage(item.id)"
                    >
                      <img
                        :src="baseUrl + item.viewImage"
                        alt=""
                        style="width:100%;height:100%;"
                      />
                      <div
                        class="mask"
                        :class="{ maskActive: temId === item.id }"
                      >
                        <div class="mask-text">{{ item.name }}</div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" @click="save">确定</button>
            <button type="button" data-dismiss="modal">取消</button>
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
      pageTypes: true,
      baseUrl: gbs.host,
      addId: 0,
      addOne: false,
      errMsg: '必填项',
      showErr: false,
      userIds: [],
      tems: [],
      tems2: [],
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
        this.temId = this.tems2[0].id
        this.name = ''
      }
    },
    pageTypes: function () {
      if (this.pageTypes) {
        this.temId = this.tems2[0].id
      } else {
        this.temId = ''
      }
    }
  },
  methods: {
    getAdminUsers () {
      // 获取超级管理员角色下的所有用户
      return new Promise((resolve, reject) => {
        this.axios.get('/mc/role/findAllUserByRoleId?roleIds=1').then(res => {
          if (res.success) {
            this.userIds = res.obj
            return resolve()
          }
          Notification({
            message: res.msg,
            position: 'bottom-right',
            customClass: 'toast toast-error'
          })
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
        var str = new RegExp(
          "[`~!@#$^*|{}';',<>》《~！@#￥……*——|{}【】‘；”“'。，、？]"
        )
        var flag = !str.test(this.name) && !/\s/.test(this.name)
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
      this.axios.get('/leaderview/home/template/list').then(res => {
        let items1 = []
        let items2 = []
        res.obj.forEach(element => {
          if (element.isDynamicTemplate) {
            items2.push(element)
          } else {
            items1.push(element)
          }
        })
        this.tems1 = items1
        this.tems2 = items2
        console.log(1111111)
        this.temId = this.tems2[0].id
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
        }).then(res => {
          if (res.success) {
            this.addOne = true
            this.addId = res.obj.id
            Notification({
              message: '操作成功！',
              position: 'bottom-right',
              customClass: 'toast toast-success'
            })
            $('#addHomePage-modal').modal('hide')
          } else {
            Notification({
              message: res.msg,
              position: 'bottom-right',
              customClass: 'toast toast-error'
            })
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
  destroyed: function () {}
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
  // width: 31%;
  // height: 152px;
  // width: 310px;
  // height: 165px;
  width: 255px;
  height: 155px;
  box-shadow: 0px 0px 2px 2px #141929;
  margin: 10px;
  position: relative;
}

#addHomePage-modal .first-item {
  text-align: center;
  line-height: 152px;
}

#addHomePage-modal .active {
  outline: 2px solid #5b8bff;
}
html[data-theme='blackWhite'],
html[data-theme='blueWhite'] {
  #addHomePage-modal .flex-item {
    box-shadow: 0 0 6px rgba(0, 0, 0, 0.2);
  }
}
html[data-theme='blueWhite'] {
  #addHomePage-modal .active {
    outline: 2px solid #60abff;
  }
}
html[data-theme='blackWhite'] {
  #addHomePage-modal .active {
    outline: 2px solid #026bf4;
  }
}

.mask {
  width: 255px;
  height: 155px;
  position: absolute;
  top: 0;
  left: 0;
  // background: #5b8bff
  //display: none;
  display: none;
  align-items: flex-end;
  padding: 0;
}
.mask-text {
  width: 255px;
  height: 20%;
  background: rgba($color: #000000, $alpha: 0.45);
  color: white;
  text-shadow: #000000 3px 3px 2px;
  line-height: 0;
  display: flex;
  padding-left: 12px;
  //  font-weight: bold;
  // font-size: 14px;
  justify-content: flex-start;
  align-items: center;
}
.maskActive {
  display: flex;
}
.flex-item:hover .mask {
  display: flex;
}
</style>
