<template>
  <div class="modal"
       id="alert-modal"
       tabindex="-1"
       role="dialog"
       aria-hidden="true"
       style="z-index: 200100;">
    <div class="modal-dialog">
      <div class="row">
        <div class="col-lg-offset-3 col-md-offset-3 col-md-7 col-lg-7">
          <div class="modal-content">
            <div class="modal-header">
              <h4 class="modal-title font-white-lemon">请选择</h4>
            </div>
            <div class="modal-body">
              <h5 style="text-align: center; line-height: 24px; word-wrap: break-word;">{{message}}
              </h5>
            </div>
            <div class="modal-footer"
                 style="border: 0; padding-top: 0; text-align: center; margin-left: 0px;">
              <button type="button"
                      @click="sure2">{{sureText}}</button>
              <span style="width: 20px; display: inline-block;"></span>
              <button type="button"
                      data-dismiss="modal">{{cancelText}}</button>
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
  props: ['message', 'okText', 'showModal'],
  data () {
    return {
      sureText: '确定',
      cancelText: '取消'
    }
  },
  methods: {
    sure2 () {
      this.$emit('hideModal', { 'sure': '1' })
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
  destroyed: function () {
  }
}
</script>
