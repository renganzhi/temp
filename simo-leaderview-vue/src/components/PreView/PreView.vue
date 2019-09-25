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
             style="height:560px;position: relative;overflow: hidden;">
          <div class="wrap">
            <div class="paintBox"
                 :style="paintStyle"></div>
            <div class="full-height box"
                 style="transform-origin:0 0; -webkit-transform-origin:0 0; -moz-transform-origin:0 0; -ms-transform-origin:0 0;">
              <LookItem v-for="(item,index) in pageList"
                        :index="index"
                        :item="item"
                        :editable="editable"
                        :key="index"></LookItem>
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
  </div>
</template>
<script>
import { baseData, gbs } from '@/config/settings'
import LookItem from './../Common/LookItem'
import Compose from './../Common/Compose'
import { Notification } from 'element-ui'
import { mapGetters } from 'vuex'
export default {
  name: 'preView',
  props: ['showModal', 'viewId', 'pageData', 'composeData', 'paintObj'],
  components: { LookItem, Compose, Notification },
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
      this.$nextTick(() => {
        this.setScale()
      })
    }
    $('#mainPreview-modal').on('hide.bs.modal', function () {
      // 关闭模态框时触发
      _this.$emit('hidePreview')
    })
    // alert(this.homeData.height)
  },
  computed: {
    paintStyle: function () {
      if (!this.paintObj) return
      var type = this.paintObj.bgStyle
      if (type === '1') {
        var backgroundSize = '100% auto'
      } else if (type === '2') {
        backgroundSize = 'auto 100%'
      } else {
        backgroundSize = '100% 100%'
      }
      return {
        backgroundImage: this.paintObj.bgImg
          ? 'url(' + gbs.host + '/leaderview' + this.paintObj.bgImg + ')'
          : '',
        backgroundColor: this.paintObj.bgColor,
        backgroundSize: backgroundSize,
        opacity: this.paintObj.opacity / 100
      }
    },
    ...mapGetters([
      'homeData'
    ])
  },
  methods: {
    setScale () {
      var box = $('#mainPreview-modal').find('.box')
      var w = box.width()
      var h = box.height()
      // var scaleX = w / baseData.home.w
      // var scaleY = h / baseData.home.h
      if (this.paintObj) {
        var scaleX = w / this.paintObj.width
        var scaleY = h / this.paintObj.height
      } else {
        scaleX = w / 1920
        scaleY = h / 1080
      }
      box.css({
        transform: 'scale(' + scaleX + ',' + scaleY + ')'
      })
    },
    getConf () {
      if (this.viewId) {
        this.axios.get('/leaderview/home/homePage/getById/' + this.viewId).then((res) => {
          if (res.success) {
            this.pageList = res.obj.viewConf ? JSON.parse(res.obj.viewConf) : []
            this.$nextTick(() => {
              this.setScale()
            })
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
        this.$nextTick(() => {
          this.setScale()
        })
      }
    }
  },
  watch: {
    showModal: function (newV) {
      this.getConf()
      if (newV) $('#mainPreview-modal').modal('show')
      // this.setScale()
    }
  },
  destoryed: function () {
  }
}
</script>
<style scoped>
.wrap {
  width: 100%;
  height: 530px;
  overflow: hidden;
  position: relative;
}
.paintBox {
  width: 100%;
  height: 530px;
  position: absolute;
  top: 0px;
  left: 0px;
  overflow: hidden;
  /* margin: 15px 20px;
  width: calc(100% - 40px); */
}
</style>
