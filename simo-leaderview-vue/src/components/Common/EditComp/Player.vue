<template>
  <div class="main_video"
       :style="boxStyle">
    <div class="v-charts-data-empty"
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
    <!-- <div class="v-charts-data-empty"
         v-if="item.videoSrc && canNotPlay"
         style="width: 100%; height: 100%; text-align: center; font-size: 12px;">
      <div><i class="icon-n-nodata"
           style="font-size: 108px;"></i><br>
        <p>抱歉，无有效视频可播放...</p>
      </div>
    </div> -->
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
      canNotPlay: false
    }
  },
  computed: {
    ...mapGetters([
      'videoTims'
    ]),
    boxStyle: function () {
      return {
        position: 'relative',
        width: '100%',
        height: this.item.height + 'px'
      }
    }
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
      this.canNotPlay = true
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
<style>
.v-charts-data-empty {
  position: absolute !important;
  top: 0px !important;
  background-color: transparent !important;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
}
.v-charts-data-empty i,
.v-charts-data-empty p {
  color: #666f8b;
}
</style>
