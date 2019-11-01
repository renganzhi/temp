<template>
  <div>
    <video width="300"
           height="300"
           id="myVideo"
           ref="videoItem"
           loop="loop"
           autoplay="autoplay"
           controls="controls"
           src="../../../assets/video/video1.mp4">
      您的浏览器不支持 video 标签。
    </video>
    <!-- src="http://vd2.bdstatic.com/mda-jj7pbz81snk8sk9i/sc/mda-jj7pbz81snk8sk9i.mp4" -->
  </div>
</template>
<script>
export default {
  name: 'player',
  props: ['item'],
  data () {
    return {

    }
  },
  methods: {
    // 视频播放
    playMedia (startTime, endTime) {
      // 设置结束时间
      var myVid = document.getElementById('myVideo')
      myVid.currentTime = startTime
      try {
        myVid.play() // 只有在刷新大屏的第一页无法自动播放
      } catch (error) {
        console.log(error)
      }
    },
    autoPaly () {
      var playPromise = this.$refs.videoItem.play()
      if (playPromise !== undefined) {
        playPromise.then(() => {
          var audio = this.$refs.videoItem
          audio.currentTime = 10
          audio.play()
        }).catch((err) => {
          console.log(err)
        })
      }
    }
  },
  mounted () {
    // setTimeout(() => {
    // this.playMedia(20, 30)
    this.autoPaly()
    // }, 2000)
  }
}
</script>
