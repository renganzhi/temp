<template>
  <div id="mainPreview">
      <div class="modal-body"
            style="height:100%;width:100%;position: relative;overflow: hidden;padding:0px">
        <div class="wrap">
          <div class="paintBox"
                :style="bgColrStyle"></div>
          <div class="paintBox"
                :style="paintStyle"></div>
          <div class="full-height box"
                style="transform-origin:0 0; -webkit-transform-origin:0 0; -moz-transform-origin:0 0; -ms-transform-origin:0 0;">
            <LookItem v-for="(item,index) in pageList"
                      :index="index"
                      :item="item"
                      :editable="editable"
                      :key="index"></LookItem>
            <LookCompose v-for="(list, index1) in combinList"
                          :index="index1"
                          :key="list.id"
                          :list="list"></LookCompose>
          </div>
        </div>
      </div>
  </div>
</template>
<script>
import { gbs } from '@/config/settings'
import LookItem from './../Common/LookItem'
import LookCompose from './../Common/LookCompose'
import { Notification } from 'element-ui'
import { Public } from '#/js/public'
import { mapGetters } from 'vuex'
export default {
  name: 'preView',
  props: ['viewId', 'pageData', 'composeData', 'paintObj'],
  components: { LookItem, Notification, LookCompose },
  data () {
    return {
      previewStatus: true, // 标记预览状态
      editable: false,
      pageList: [],
      combinList: [],
      paintConf: ''
    }
  },
  computed: {
    bgColrStyle: function () {
      var paintData = this.paintObj || this.paintConf
      if (!paintData) return ''
      return {
        backgroundColor: paintData.bgColor
      }
    },
    paintStyle: function () {
      var paintData = this.paintObj || this.paintConf
      if (!paintData) return
      var type = paintData.bgStyle
      if (type === '1') {
        var backgroundSize = '100% auto'
      } else if (type === '2') {
        backgroundSize = 'auto 100%'
      } else {
        backgroundSize = '100% 100%'
      }
      return {
        backgroundImage: paintData.bgImg
          ? 'url(' + gbs.host + '/leaderview' + paintData.bgImg + ')'
          : '',
        backgroundSize: backgroundSize,
        opacity: paintData.opacity / 100
      }
    },
    ...mapGetters([
      'homeData'
    ])
  },
  methods: {
    setScale () {
      var box = $('#mainPreview').find('.box')
      var w = box.width()
      var h = box.height()

      if (this.paintObj) {
        var scaleX = w / this.paintObj.width
        var scaleY = h / this.paintObj.height
      } else if (this.paintConf) {
        scaleX = w / this.paintConf.width
        scaleY = h / this.paintConf.height
      } else {
        scaleX = w / 1920
        scaleY = h / 1080
      }
      let scale = Math.min(scaleX, scaleY)
      box.css({
        transform: 'scale(' + scale + ',' + scale + ')'
      })
    },
    reNewOne () {
      Public.bigScreenfullScreen($('.wrap').get(0))
      this.getConf()
    },
    getConf () {
      if (this.viewId) {
        this.axios.get('/leaderview/home/homePage/getById/' + this.viewId).then((res) => {
          if (res.success) {
            this.pageList = res.obj.viewConf ? JSON.parse(res.obj.viewConf) : []
            this.combinList = res.obj.composeObj ? JSON.parse(res.obj.composeObj) : []
            this.paintConf = res.obj.paintObj ? JSON.parse(res.obj.paintObj) : ''
            this.$nextTick(() => {
              this.setScale()
            })
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
      } else {
        this.pageList = JSON.parse(this.pageData) || []
        this.combinList = JSON.parse(this.composeData) || []
        this.$nextTick(() => {
          this.setScale()
        })
      }
    }
  },
  destroyed: function () {
  }
}
</script>
<style>
#mainPreview{
  height: 100%;
  width: 100%;
  left: -1000000000000px;
  position: absolute;
  overflow: hidden;
  z-index: -1;
}
#mainPreview .wrap {
  width: 100%;
  height: 100%;
  overflow: hidden;
  position: relative;
}
#mainPreview .paintBox {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0px;
  left: 0px;
  overflow: hidden;
  /* margin: 15px 20px;
  width: calc(100% - 40px); */
}
#mainPreview .itemWrapBox {
  padding-top: 1px !important;
}
/* .modal-dialog.modal-lg {
    width: 1500px;
} */
</style>
