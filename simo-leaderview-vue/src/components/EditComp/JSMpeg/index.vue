<template>
  <div class="JSMpeg"
       :style="JSMpegStyle">
    <canvas
      ref="canvas"
      v-if="vidoeShow"
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
      vidoeShow: true
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
  mounted () {
    let url = `ws://${location.host}/video/play?neId=3bcd576a-6382-4312-9448-35fff1ed59e3&stream=sub&channel=1`
    // let url = 'ws://192.100.100.42:9999/video/play?neId=3bcd576a-6382-4312-9448-35fff1ed59e3&stream=sub&channel=1'
    this.player = new JSMpeg.Player(url, {
      canvas: this.$refs.canvas,
      loop: false,
      preserveDrawingBuffer: true
    })
  },
  beforeMount () {
    var reg = /^\/api/
    if (!reg.test(this.item.imgSrc)) {
      console.log(location.host)
      this.baseUrl = gbs.host
      // this.baseUrl = gbs.host + '/leaderview'
    }
  },
  methods: {},
  watch: {},
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
