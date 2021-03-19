<template>
  <div class="JSMpeg"
       :style="JSMpegStyle">
    <canvas
      ref="canvas"
      class="canvas"
    />
  </div>
</template>
<script>
export default {
  name: 'JSMpeg',
  props: ['item'],
  data () {
    return {
      player: ''
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
    // let url = `ws://${location.host}/video/play?neId=${pram.neId}&stream=sub&channel=${pram.channel}`
    let url = 'ws://192.168.1.135:9999/video/play?neId=4ed1da95-e3dc-4e83-9a97-7a1243157b2b&stream=sub&channel=1'
    this.player = new JSMpeg.Player(url, {
      canvas: this.$refs.canvas,
      loop: false,
      preserveDrawingBuffer: true
    })
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
