<template>
  <div class="bubble_box">
    <div
      id="circleWrap"
      v-show="showBubbleChart"
      ref="circleWrap"
      class="circle_wrap"
      data-top="0"
      :style="{ top: `${top}px` }"
    >
    <div class="AllBox" ref="AllBox" style="position:relative">
      <div id="box1" style="position:absolute"></div>
      <div id="box2" style="position: absolute;"></div>
    </div>
    </div>
    <div class="v-charts-data-empty"
        v-show="!showBubbleChart"
        style="width: 100%; height: 100%; text-align: center; font-size: 12px;">
        <div><i class="icon-n-nodata"
            style="font-size: 108px;"></i><br>
          <p>抱歉，没有数据可供展示...</p>
        </div>
    </div>
  </div>
</template>

<script>
import createBubble from './createBubble'

export default {
  name: 'BubbleChart',
  props: ['item'],
  data () {
    return {
      top: 0,
      targetHeight: 300,
      topX: 0,
      oldchartData: '',
      Interval: null,
      canvasBox: {}
    }
  },
  computed: {
    chartData () {
      let originData = this.item.chartData.rows
      if (originData.length > 0) {
        const minCount = Math.floor(
          (this.item.width * this.item.height) / 200 ** 2
        )
        if (origin.length < minCount) {
          let tmp = originData
          for (let i = 1; i < minCount / origin.length; i++) {
            originData.push(...tmp)
          }
        }
        return originData.map(d => {
          let value = d.value
          if (typeof (value * 1) !== 'number') {
            d.value = 0
          }
          return d
        })
      }
    },
    showBubbleChart () {
      return this.item.chartData.rows.length > 0
    },
    len () {
      return this.chartData.length
    },
    speed () {
      return 40 / this.item.speed
    }
    // targetHeight () { return 300 }
  },
  watch: {
    'item.chartData': function (newV, oldV) {
      if (JSON.stringify(newV) !== this.oldchartData) {
        console.log(newV)
        this.oldchartData = JSON.stringify(newV)
        this.initCanvas()
      }
    }
  },
  mounted () {
    this.initCanvas()
  },
  methods: {
    initCanvas () {
      createBubble(this.item.width, this.item.height, this.chartData).then(
        canvas => {
          var _box1 = document.getElementById('box1')
          var _box2 = document.getElementById('box2')
          const imgUrl = canvas.toDataURL('image/png')
          let Img = document.createElement('img')
          Img.style.width = '100%'
          Img.src = imgUrl
          _box1.innerHTML = null
          _box2.innerHTML = null
          _box1.appendChild(Img)
          _box2.appendChild(Img.cloneNode(true))
          this.targetHeight = canvas.height
          if (this.Interval) {
            clearInterval(this.Interval)
            this.Interval = null
          }
          this.Interval = setInterval(this.socall, 20)
        }
      )
    },
    socall () {
      var _box1 = document.getElementById('box1')
      var _box2 = document.getElementById('box2')
      var height = document.querySelector('#box1 img').clientHeight
      _box1.style.top = this.topX + 'px'
      _box2.style.top = (this.topX + height) + 'px'
      this.topX--
      if ((this.topX + height) <= 0) {
        this.topX = 0
      }
    }
  },
  beforeDestroy () {
    if (this.Interval) {
      clearInterval(this.Interval)
      this.Interval = null
    }
  }
}
</script>

<style lang="scss" scoped>
.bubble_box {
  width: 100%;
  height: 100%;
  position: relative;
  // width: 660px;
  // height: 300px;
  color: #fff;
  overflow: hidden;
  .circle_wrap {
    position: relative;
    text-align: center;
    top: 0px;
    left: 0px;
    width: 100%;
    height: 100%;
    list-style: none;
    // animation: animation 10s linear infinite;
    // @keyframes animation {
    //   from {
    //     top: 0;
    //   }
    //   to {
    //     top: -100vh;
    //   }
    // }
  }
  .circle {
    display: block !important;
    border-radius: 50%;
    text-align: center;
    overflow-wrap: break-word;
    float: left;
    // width: 60px;
    // height: 60px;
    // line-height: 60px;
    // background-color: red
  }

  .circle_list {
    background-repeat: no-repeat;
    width: 100%;
  }

  .circle_list:after {
    display: block;
    clear: both;
    content: '';
  }

  .text_warp_name,
  .text_warp_value {
    display: block;
    width: 100%;
    height: 50%;
    text-align: center;
    font-size: 16px;
    p {
      // line-height: 100%;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }
}

#test {
  text-align: left;
}
</style>
