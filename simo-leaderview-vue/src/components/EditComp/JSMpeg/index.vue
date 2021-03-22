<template>
  <div class="JSMpeg"
       :style="JSMpegStyle">
    <canvas
      ref="canvas"
      v-show="vidoeShow"
      class="canvas"
    />
    <div class="v-charts-data-empty"
        v-show="!vidoeShow"
        style="width: 100%; height: 100%; text-align: center; font-size: 12px;">
        <div><i class="icon-n-nodata"
            style="font-size: 108px;"></i><br>
          <p>抱歉，没有数据可供展示...</p>
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
      baseUrl: ''
    }
  },
  computed: {
    JSMpegStyle: function () {
      return {
        width: this.item.width + 'px',
        height: this.item.height + 'px'
      }
    },
    vidoeShow: function () {
      if (this.item.HcnetData !== '' && this.item.VideoData !== '') {
        return true
      } else {
        return false
      }
    }
  },
  watch: {
    'item.VideoData': function (newV) {
      if (newV !== '') {
        this.vidoeShow = true
        this.getVideo()
      } else {
        this.vidoeShow = false
      }
    }
  },
  mounted () {
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
      if (this.item.HcnetData !== '' && this.item.VideoData !== '') {
        let url = `ws://${location.host}/video/play?neId=${this.item.HcnetData}&stream=sub&channel=${this.item.VideoData}`
        this.player = new JSMpeg.Player(url, {
          canvas: this.$refs.canvas,
          loop: false,
          preserveDrawingBuffer: true
        })
      }
    }
  },
  beforeDestroy () {
    this.player && this.player.destroy()
    this.recorder && this.recorder.state === 'recording' && this.recorder.stop()
  },
  destroyed: function () {
  }
}
</script>
<style scoped>
.JSMpeg canvas{
  width: 100%;
}
</style>
