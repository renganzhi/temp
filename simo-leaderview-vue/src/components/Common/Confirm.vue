<template>
  <div
    class="modal"
    id="alert-modal"
    tabindex="-1"
    role="dialog"
    aria-hidden="true"
    style="z-index: 200100;"
  >
    <div class="modal-dialog modal-dialog-centered modal-sm">
      <div class="row">
        <div
          class="col-lg-offset-3 col-md-offset-3 col-md-9 col-lg-9"
          style="padding:0 17.5px"
        >
          <div class="modal-content" style="height:200px">
            <div class="modal-header">
              <h4 class="modal-title font-white-lemon">
                {{ modalTitle ? modalTitle : '请选择' }}
              </h4>
            </div>
            <div
              class="modal-body"
              style="display:flex;justify-content:center;align-items:center"
            >
              <h5
                style="text-align: center; line-height: 24px; word-wrap: break-word;font-size:14px"
              >
                {{ message }}
              </h5>
            </div>
            <div
              class="modal-footer"
              style="border: 0; padding-top: 0; text-align: center; margin-left: 0px;justify-content: right;"
            >
              <button type="button" @click="sure2">{{ sureText }}</button>
              <span style="width: 1px; display: inline-block;"></span>
              <button type="button" data-dismiss="modal">
                {{ cancelText }}
              </button>
              <!-- <button type="button"
                        data-dismiss="modal"
                        @click="sure2"
                        action="1">{{sureText}}</button>
                <span style="width: 20px; display: inline-block;"></span>
                <button type="button"
                        data-dismiss="modal"
                        action="-1">{{cancelText}}</button> -->
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'confirm',
  props: ['message', 'okText', 'showModal', 'modalTitle'],
  data () {
    return {
      sureText: '确定',
      cancelText: '取消'
    }
  },
  methods: {
    sure2 () {
      this.$emit('hideModal', { sure: '1' })
      $('#alert-modal').modal('hide')
    }
  },
  mounted: function () {
    var _this = this
    if (this.showModal) {
      $('#alert-modal').modal('show')
    }
    $('#alert-modal').on('hide.bs.modal', function () {
      // 关闭模态框时触发
      _this.$emit('hideModal')
    })
  },
  watch: {
    showModal (newV) {
      if (newV) {
        $('#alert-modal').modal('show')
        if (this.okText === '是') {
          this.sureText = '是'
          this.cancelText = '否'
        }
      }
    }
  },
  beforeDestroy: function () {
    $('#alert-modal').modal('hide')
    $('.modal-backdrop').remove()
  },
  destroyed: function () {}
}
</script>
