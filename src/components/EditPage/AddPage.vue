<template>
  <div>
    <!-- <div class="modal-backdrop in modal-stack"></div> -->
    <div id="addHomePage-modal"
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
                         v-model="name"
                         name="name" />
                </div>
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
      baseUrl: gbs.host,
      addOne: false,
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
      _this.$emit('hideModal', { ifAdd: _this.addOne })
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
    getTemps () {
      // this.tems.splice(0, this.tems.length)
      this.axios.get('/home/template/list').then((res) => {
        this.tems = res.obj
      })
      /*  this.$nextTick(function(){
        _this.$modal.find('.defPages').get(0).scrollTop = 0;
      }) */
    },
    save () {
      // 校验名称
      // if (!_this.validator.form()) {
      //   return
      // }
      var data = {
        name: this.name,
        templateId: this.temId
      }
      this.axios({
        method: 'post',
        url: '/home/homePage/add',
        data: qs.stringify(data),
        headers: { 'content-type': 'application/x-www-form-urlencoded' }
      }).then((res) => {
        if (res.success) {
          this.addOne = true
          $('#addHomePage-modal').modal('hide')
          // tooltip('', '操作成功！', 'success')
          Notification({
            message: '操作成功！',
            position: 'bottom-right',
            customClass: 'toast toast-success'
          })
        } else {
          // tooltip("", data.msg, "error");
          Notification({
            message: res.msg,
            position: 'bottom-right',
            customClass: 'toast toast-error'
          })
        }
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
  destoryed: function () {
  }
}
</script>
<style scoped>
/* 新建页面-弹窗样式 */
#addHomePage-modal .defPages {
  flex-wrap: wrap;
  height: 420px;
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
</style>
