<template>
  <div class="JSMpeg"
       :style="JSMpegStyle">
    <canvas
      ref="mycanvas"
      v-show="vidoeShow&&complete"
      class="canvas"
    />
    <p v-show="vidoeShow&&!complete" style="top: 50%;position: absolute;left: 50%;transform: translateY(-50%) translateX(-50%);">
      请稍等，正在加载中...
    </p>
    <div class="v-charts-data-empty"
        v-show="!vidoeShow"
        style="width: 100%; height: 100%; text-align: center; font-size: 12px;">
        <div><i class="icon-n-nodata"
            style="font-size: 108px;"></i><br>
          <p>请选择视频</p>
        </div>
    </div>
  </div>
</template>
<script>
import { gbs } from '@/config/settings'
export default {
  name: 'JSMpeg',
  props: ['item'],
  data () {
    return {
      player: '',
      baseUrl: '',
      complete: false,
      vidoeShow: false
    }
  },
  computed: {
    JSMpegStyle: function () {
      return {
        width: this.item.width + 'px',
        height: this.item.height + 'px'
      }
    }
  },
  watch: {
    'item.chartData': function (newV) {
      this.getVideo()
    }
  },
  mounted () {
    this.getVideo()
  },
  beforeMount () {
    var reg = /^\/api/
    if (!reg.test(this.item.imgSrc)) {
      this.baseUrl = gbs.host
      // this.baseUrl = gbs.host + '/leaderview'
    }
  },
  methods: {
    getVideo: function () {
      var _this = this
      var myNewVale = JSON.parse(this.item.chartData || '')
      if (myNewVale.channel && myNewVale.neId) {
        this.vidoeShow = true
        this.$nextTick(function () {
          this.player && this.player.stop()
          let url = `ws://${location.hostname}:11100${this.item.urlData}?neId=${myNewVale.neId}&stream=sub&channel=${myNewVale.channel}`
          // let url = `ws://192.100.100.42:11100/collector/videoMonitoring?neId=3bcd576a-6382-4312-9448-35fff1ed59e3&stream=sub&channel=${myNewVale.channel}`
          this.player = new JSMpeg.Player(url, {
            canvas: this.$refs.mycanvas,
            loop: false,
            autoplay: true,
            disableGl: true,
            disableWebAssembly: true,
            onVideoDecode: () => {
              if (_this.complete) {
                return
              }
              _this.complete = true
            },
            preserveDrawingBuffer: true
          })
          this.player.play()
        })
      } else {
        this.player && this.player.stop()
        this.vidoeShow = false
      }
    }
  },
  beforeDestroy () {
    this.player && this.player.destroy()
  },
  destroyed: function () {}
}
</script>
<style scoped>
.JSMpeg canvas {
  width: 100%;
  height: 100%;
}
</style>
