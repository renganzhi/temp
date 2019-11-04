<template>
  <div>
    <video :width="item.width"
           :height="item.height"
           id="myVideo"
           ref="videoItem"
           loop="loop"
           autoplay="autoplay"
           controls="controls"
           src="../../../assets/video/video1.mp4">
      您的浏览器不支持 video 标签。
    </video>
    <!-- src="http://f.us.sinaimg.cn/001z6liflx07ujKIPQFG01041200CXtc0E010.mp4?label=mp4_hd&template=852x480.28.0&Expires=1572690516&ssig=7Vx6%2B%2Fjtuz&KID=unistore,video" -->
    <!-- http://f.video.weibocdn.com/0003EGd7lx07xZGjYBcI01041202iQna0E010.mp4?label=mp4_720p&template=1280x720.25.0&trans_finger=1f0da16358befad33323e3a1b7f95fc9&Expires=1572690163&ssig=pkPvPzSPyp&KID=unistore,video -->
    <!-- src="../../../assets/video/video1.mp4" -->
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
          audio.play() // 只有在刷新大屏的第一页无法自动播放
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
    setTimeout(() => {
      var video = this.$refs.videoItem
      console.log(video.currentTime)
    }, 4000)
  }
}
</script>
