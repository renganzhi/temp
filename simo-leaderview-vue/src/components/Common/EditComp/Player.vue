<template>
  <div class="main_video"
       style="position: relative;">
    <div class="v-charts-data-empty"
         style="text-align: center;"
         v-if="!item.videoSrc">请选择视频</div>
    <video v-else
           :width="item.width"
           :height="item.height"
           id="myVideo"
           ref="videoItem"
           loop="loop"
           autoplay="autoplay"
           @error="playError"
           controls="controls"
           style="outline: none !important;"
           :src="item.videoSrc">
      您的浏览器不支持 video 标签。
    </video>
    <!-- src="http://vd2.bdstatic.com/mda-jj7pbz81snk8sk9i/sc/mda-jj7pbz81snk8sk9i.mp4" -->
  </div>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
export default {
  name: 'player',
  props: ['item'],
  data () {
    return {
    }
  },
  computed: {
    ...mapGetters([
      'videoTims'
    ])
  },
  // watch: {
  // 'item.videoType': function (newV) {
  //   if (newV === 'url') {
  //     console.log('change to url')
  //     this.playUrl = this.item.linkSrc
  //   } else {
  //     console.log('change to src')
  //     this.playUrl = gbs.host + this.item.videoSrc
  //   }
  // }
  // },
  methods: {
    ...mapActions([
      'changeVideoTims'
    ]),
    // 视频播放
    // playMedia (startTime, endTime) {
    // var playPromise = this.$refs.videoItem.play()
    // if (playPromise !== undefined) {
    //   console.log('time:' + time)
    //   playPromise.then(() => {
    //     var audio = this.$refs.videoItem
    //     if (time) {
    //       // audio.currentTime = Number(time)
    //       audio.currentTime = 6
    //     }
    //     audio.play() // 只有在刷新大屏的第一页无法自动播放
    //   }).catch((err) => {
    //     console.log(err)
    //   })
    // }
    // },
    autoPaly (time) {
      try {
        var audio = this.$refs.videoItem
        if (time) {
          audio.currentTime = Number(time)
        }
        audio.play() // 只有在刷新大屏的第一页无法自动播放
      } catch (error) {
        console.log(error)
      }
    },
    playError (e) {
      this.$emit('palyErr')
      // console.log(e)
    }
  },
  mounted () {
    // this.item.videoSrc = require('../../../assets/video/video1.mp4')
    if (document.getElementById('home-html')) {
      var time = this.videoTims[this.item.id]
      this.autoPaly(time)
    } else {
      this.autoPaly()
    }
  },
  beforeDestroy () {
    if (document.getElementById('home-html')) {
      var video = this.$refs.videoItem
      let obj = { id: this.item.id, time: video.currentTime }
      this.changeVideoTims(obj)
    }
  }
}
</script>
