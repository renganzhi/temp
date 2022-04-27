<template>
  <div :class="{'countNum': true, 'up': true, 'changed':changed, 'changing': changing}"
       :style="countStyle">
    <span class="current top"
          :style="spanStyle">{{currentNum}}</span>
    <span class="next top"
          :style="spanStyle">{{nextNum}}</span>
    <span class="current bottom"
          :style="spanStyle">{{currentNum}}</span>
    <span class="next bottom"
          :style="spanStyle">{{nextNum}}</span>
  </div>
</template>
<script>
import _ from 'lodash'
export default {
  name: 'turnOver', // 数字翻牌器
  props: ['item', 'nextNum', 'numHeight', 'numWidth', 'bgClr'],
  data () {
    return {
      currentNum: 0, // 变化前
      duration: 1000,
      changing: false,
      changed: true
    }
  },
  computed: {
    countStyle: function () {
      return {
        // height: this.numHeight - 5 + 'px !important',
        // lineHeight: this.numHeight -5 + 'px !important',
        // width: this.numWidth - 5 + 'px !important'
      }
    },
    spanStyle: function () {
      return {
        backgroundColor: this.bgClr,
        transformOrigin: '0 ' + parseInt(this.numHeight / 2) + 'px 0'
      }
    }
  },
  methods: {
    changeAnima: function () {
      _.delay(this.executeAnimation, 20)
      _.delay(this.finishAnimation, this.duration * 0.9)
    },
    executeAnimation: function () {
      this.changing = true
    },
    finishAnimation: function () {
      this.changed = true
      this.changing = false
    }
  },
  watch: {
    nextNum: function (newV, oldV) {
      this.currentNum = oldV
      this.changed = false
      this.changeAnima()
    }
  },
  mounted () {
    this.currentNum = this.nextNum
    // this.changeAnima()
  },
  destroyed: function () {
  }
}
</script>
<style scoped lang="scss">
$radius: 6px;
$bRadius: 7px;
.countNum {
  // box-shadow: 0 10px 5px -5px rgba(0, 0, 0, 0.2);
  box-shadow: 0 10px 5px -5px rgba(0, 0, 0, 0.15);
  height: 100px;
  line-height: 100px;
  margin-right: 3px !important;
  height: 52px !important;
  line-height: 52px !important;
  width: 36px !important;
  /* -moz-perspective: 500px;
  -webkit-perspective: 500px;
  perspective: 500px; */
  display: inline-block;
  position: relative;
  text-align: center;
  // 解决缩放时页面会变得模糊
  /*-moz-transform: translateZ(0);
  -webkit-transform: translateZ(0);
  transform: translateZ(0);*/
  width: 80px;
}
.countNum span {
  /* background: #202020; */
  background: #222739;
  display: block;
  /* font-size: 250px; */
  left: 0;
  position: absolute;
  top: 0;
  text-shadow: 0 1px 0 #282828, 0 2px 0 #1e1e1e, 0 3px 0 #141414,
    0 4px 0 #0a0a0a, 0 5px 0 #000, 0 0 10px rgba(0, 0, 0, 0.8);
  /* text-shadow:0 1px 0 #282828, 0 2px 0 #1e1e1e, 0 1px 0 #141414, 0 4px 0 #0a0a0a, 0 1px 0 #000, 0 0 8px rgba(0, 0, 0, 0.8) */
  /* -moz-transform-origin: 0 150px 0;
  -webkit-transform-origin: 0 150px 0; */
  -moz-transform-origin: 0 50% 0;
  -webkit-transform-origin: 0 50% 0;
  transform-origin: 0 50% 0;
  width: 100%;
}
.countNum span:before {
  /* border-bottom: 0.8px solid #000; */
  content: "";
  left: 0;
  position: absolute;
  width: 100%;
}
.countNum span:after {
  /* box-shadow: inset 0 0 60px rgba(0, 0, 0, 0.35); */
  box-shadow: inset 0 0 15px rgba(0, 0, 0, 0.35);
  content: "";
  height: 100%;
  left: 0;
  position: absolute;
  top: 0;
  width: 100%;
}
.countNum .top {
  /* border-top-left-radius: 11px;
  border-top-right-radius: 11px; */
  border-top-left-radius: $bRadius;
  border-top-right-radius: $bRadius;
  /* 顶部圆弧 */
  /* box-shadow: inset 0 2px rgba(0, 0, 0, 0.9),
    inset 0 3px 0 rgba(255, 255, 255, 0.4); */
  /* box-shadow: inset 0 2px rgba(0, 0, 0, 0.6),
    inset 0 3px 0 rgba(255, 255, 255, 0.4); */
  height: 50%;
  overflow: hidden;
}
.countNum .top:before {
  bottom: 0;
}
.countNum .top:after {
  background: -moz-linear-gradient(rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.15));
  background: -webkit-linear-gradient(rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.15));
  background: linear-gradient(rgba(0, 0, 0, 0), rgba(0, 0, 0, 0.15));
  /* border-top-left-radius: 11px;
  border-top-right-radius: 11px; */
  border-top-left-radius: $bRadius;
  border-top-right-radius: $bRadius;
}
.countNum .bottom {
  /* border-radius: 10px; */
  border-radius: $radius;
  height: 100%;
}
.countNum .bottom:before {
  top: 50%;
}
.countNum .bottom:after {
  /* border-radius: 10px; */
  border-radius: $radius;
  background: -moz-linear-gradient(
    rgba(255, 255, 255, 0.1),
    rgba(255, 255, 255, 0.1) 50%,
    rgba(255, 255, 255, 0)
  );
  background: -webkit-linear-gradient(
    rgba(255, 255, 255, 0.1),
    rgba(255, 255, 255, 0.1) 50%,
    rgba(255, 255, 255, 0)
  );
  background: linear-gradient(
    rgba(255, 255, 255, 0.1),
    rgba(255, 255, 255, 0.1) 50%,
    rgba(255, 255, 255, 0)
  );
}
.countNum.down .top {
  /* border-top-left-radius: 11px;
  border-top-right-radius: 11px; */
  border-top-left-radius: $bRadius;
  border-top-right-radius: $bRadius;
  height: 50%;
}
.countNum.down .top.current {
  -moz-transform-style: flat;
  -webkit-transform-style: flat;
  transform-style: flat;
  z-index: 3;
}
.countNum.down .top.next {
  -moz-transform: rotate3d(1, 0, 0, -90deg);
  -ms-transform: rotate3d(1, 0, 0, -90deg);
  -webkit-transform: rotate3d(1, 0, 0, -90deg);
  transform: rotate3d(1, 0, 0, -90deg);
  z-index: 4;
}
.countNum.down .bottom {
  /* border-radius: 10px; */
  border-radius: $radius;
}
.countNum.down .bottom.current {
  z-index: 2;
}
.countNum.down .bottom.next {
  z-index: 1;
}
.countNum.down.changing .bottom.current {
  box-shadow: 0 35px 5px -20px rgba(0, 0, 0, 0.3);
  -moz-transform: rotate3d(1, 0, 0, 90deg);
  -ms-transform: rotate3d(1, 0, 0, 90deg);
  -webkit-transform: rotate3d(1, 0, 0, 90deg);
  transform: rotate3d(1, 0, 0, 90deg);
  -moz-transition: -moz-transform 0.35s ease-in, box-shadow 0.35s ease-in;
  -o-transition: -o-transform 0.35s ease-in, box-shadow 0.35s ease-in;
  -webkit-transition: -webkit-transform 0.35s ease-in, box-shadow 0.35s ease-in;
  transition: transform 0.35s ease-in, box-shadow 0.35s ease-in;
}
.countNum.down.changing .top.next,
.countNum.down.changed .top.next {
  -moz-transition: -moz-transform 0.35s ease-out 0.35s;
  -o-transition: -o-transform 0.35s ease-out 0.35s;
  -webkit-transition: -webkit-transform 0.35s ease-out;
  -webkit-transition-delay: 0.35s;
  transition: transform 0.35s ease-out 0.35s;
  -moz-transform: none;
  -ms-transform: none;
  -webkit-transform: none;
  transform: none;
}
.countNum.up .top {
  height: 50%;
}
.countNum.up .top.current {
  z-index: 4;
}
.countNum.up .top.next {
  z-index: 3;
}
.countNum.up .bottom.current {
  z-index: 1;
}
.countNum.up .bottom.next {
  /* box-shadow: 0 75px 5px -20px rgba(0, 0, 0, 0.3); */
  box-shadow: 0 25px 5px -20px rgba(0, 0, 0, 0.3);
  -moz-transform: rotate3d(1, 0, 0, 90deg);
  -ms-transform: rotate3d(1, 0, 0, 90deg);
  -webkit-transform: rotate3d(1, 0, 0, 90deg);
  transform: rotate3d(1, 0, 0, 90deg);
  z-index: 2;
}
.countNum.up.changing .top.current {
  -moz-transform: rotate3d(1, 0, 0, -90deg);
  -ms-transform: rotate3d(1, 0, 0, -90deg);
  -webkit-transform: rotate3d(1, 0, 0, -90deg);
  transform: rotate3d(1, 0, 0, -90deg);
  -moz-transition: -moz-transform 0.2625s ease-in, box-shadow 0.2625s ease-in;
  -o-transition: -o-transform 0.2625s ease-in, box-shadow 0.2625s ease-in;
  -webkit-transition: -webkit-transform 0.2625s ease-in,
    box-shadow 0.2625s ease-in;
  transition: transform 0.2625s ease-in, box-shadow 0.2625s ease-in;
}
.countNum.up.changing .bottom.next,
.countNum.up.changed .bottom.next {
  box-shadow: 0 0 0 0 transparent;
  -moz-transition: box-shadow 0.175s cubic-bezier(0.375, 1.495, 0.61, 0.78)
      0.35s,
    -moz-transform 0.35s cubic-bezier(0.375, 1.495, 0.61, 0.78) 0.35s;
  -o-transition: box-shadow 0.175s cubic-bezier(0.375, 1.495, 0.61, 0.78) 0.35s,
    -o-transform 0.35s cubic-bezier(0.375, 1.495, 0.61, 0.78) 0.35s;
  -webkit-transition: box-shadow 0.175s cubic-bezier(0.375, 1.495, 0.61, 0.78),
    -webkit-transform 0.35s cubic-bezier(0.375, 1.495, 0.61, 0.78);
  -webkit-transition-delay: 0.35s, 0.35s;
  transition: box-shadow 0.175s cubic-bezier(0.375, 1.495, 0.61, 0.78) 0.35s,
    transform 0.35s cubic-bezier(0.375, 1.495, 0.61, 0.78) 0.35s;
  -moz-transform: rotate3d(1, 0, 0, 0);
  -ms-transform: rotate3d(1, 0, 0, 0);
  -webkit-transform: rotate3d(1, 0, 0, 0);
  transform: rotate3d(1, 0, 0, 0);
}
.countNum.changed .top.current,
.countNum.changed .bottom.current {
  display: none;
}
</style>
