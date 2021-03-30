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
      baseUrl: '',
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
    'item.VideoData': function (newV) {
      if (newV !== '') {
        this.player && this.player.stop()
        this.vidoeShow = true
        this.getVideo()
      } else {
        this.vidoeShow = false
      }
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
      if (this.item.HcnetData !== '' && this.item.VideoData !== '') {
        this.vidoeShow = true
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
    this.player && this.player.stop()
  },
  destroyed: function () {
  }
}
</script>
<style scoped>
.JSMpeg canvas{
  width: 100%;
  height: 100%;
}
</style>
