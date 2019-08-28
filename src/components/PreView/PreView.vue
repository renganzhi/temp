<template>
  <div id="mainPreview-modal"
       class="modal"
       style="overflow: hidden; z-index: 200100;">
    <!-- z-index: 20099 -->
    <div class="modal-dialog modal-lg"
         role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button"
                  class="close"
                  data-dismiss="modal"
                  aria-hidden="true">
            <span aria-hidden="true">&times;</span>
          </button>
          <h4 class="modal-title">
            预览
          </h4>
        </div>
        <div class="modal-body"
             style="height:560px;position: relative;">
          <div class="full-height box"
               style="transform-origin:0 0; -webkit-transform-origin:0 0; -moz-transform-origin:0 0; -ms-transform-origin:0 0;">
            <DragBox v-for="(item,index) in pageList"
                     :index="index"
                     :item="item"
                     :editable="editable"
                     :key="index"></DragBox>
            <Compose v-for="(list, index1) in combinList"
                     :index="index1"
                     :key="list.id"
                     :list="list"
                     :editable="editable"></Compose>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { baseData } from '@/config/settings'
import DragBox from './../Common/DragBox'
import Compose from './../Common/Compose'
import { Notification } from 'element-ui'
export default {
  name: 'preView',
  props: ['showModal', 'viewId', 'pageData', 'composeData'],
  components: { DragBox, Compose, Notification },
  data () {
    return {
      editable: false,
      pageList: [],
      combinList: []
    }
  },
  mounted: function () {
    var _this = this
    // this.getTemps()
    if (this.showModal) {
      $('#mainPreview-modal').modal('show')
      this.setScale()
    }
    $('#mainPreview-modal').on('hide.bs.modal', function () {
      // 关闭模态框时触发
      _this.$emit('hidePreview')
    })
  },
  methods: {
    setScale () {
      var box = $('#mainPreview-modal').find('.box')
      var w = box.width()
      var h = box.height()
      var scaleX = w / baseData.home.w
      var scaleY = h / baseData.home.h
      box.css({
        transform: 'scale(' + scaleX + ',' + scaleY + ')'
      })
    },
    getConf () {
      if (this.viewId) {
        this.axios.get('/home/homePage/getById/' + this.viewId).then((res) => {
          if (res.success) {
            this.pageList = res.obj.viewConf ? JSON.parse(res.obj.viewConf) : []
          } else {
            // tooltip("", data.msg, "error")
            Notification({
              message: res.msg,
              position: 'bottom-right',
              customClass: 'toast toast-error'
            })
          }
        })
      } else {
        this.pageList = JSON.parse(this.pageData) || []
        this.combinList = JSON.parse(this.composeData) || []
      }
    }
  },
  watch: {
    showModal: function (newV) {
      this.getConf()
      if (newV) $('#mainPreview-modal').modal('show')
      this.setScale()
    }
  },
  destoryed: function () {
  }
}
</script>
