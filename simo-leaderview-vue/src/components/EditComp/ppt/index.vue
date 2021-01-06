<template>
  <div :style="boxStyle">
    <div class="v-charts-data-empty"
         v-if="!item.srcList.length">请上传图片</div>
    <div v-else class="img_card" 
        :style="{...imgSctyle, 'background': `url(${imgSrc})`}"
        @click="toLinkPage">
      <template v-if="!item.autoplay">
        <div class="left_btn" @click="change(-1)">
          <i class="el-icon-back" ></i>
        </div>
        <div class="right_btn" @click="change(1)">
          <i class="el-icon-right"></i>
        </div>
      </template>
      <!-- <image class="image_box" :src="imgSrc" /> -->
    </div>
  </div>
</template>
<script>
import { mapActions } from 'vuex'
import { gbs } from '@/config/settings'

export default {
  name: 'ppt',
  props: ['item'],
  inject: ['editing'],
  data () {
    return {
      activeIndex: 0,
      baseUrl: '',
      // imgSrc: ''
    }
  },
  computed: {
    boxStyle: function () {
      return {
        maxWidth: this.item.width + 'px',
        height: this.item.height + 'px',
        overflow: 'hidden'
      }
    },
    imgSctyle: function () {
      if (this.item.showType && this.item.showType === '1') {
        return {
          maxWidth: '100%',
          maxHeight: '100%',
          cursor: this.item.linkId ? 'pointer' : ''
        }
      }
      return {
        width: '100%',
        height: '100%',
        cursor: this.item.linkId ? 'pointer' : ''
      }
    },
    imgSrc () {
      const src = this.item.srcList[this.activeIndex].src;
      return `${this.baseUrl}${src}`;
    },
    maxIndex () { return this.item.srcList.length - 1; }
  },
  beforeMount () {
    var reg = /^\/api/
    if (!reg.test(this.item.imgSrc)) {
      this.baseUrl = gbs.host
      // this.baseUrl = gbs.host + '/leaderview'
    }
  },
  mounted () {
    // console.log(this.item.srcList.length)
    // console.log('editing', this.editing)
    if (this.item.autoplay && !this.editing) {
      this.setTimer();
    }
    this.$EventBus.$on('activeSrcList', (data) => {
      this.activeIndex = data
    })
  },
  beforeDestroy () {
    this.clearTimer()
    this.$EventBus.$off('activeSrcList')
  },
  watch: {
    'item.srcList' (newV, oldV) {
      // console.log(newV)
    }
  },
  methods: {
    ...mapActions([
      'changeNowPage'
    ]),
    toLinkPage () {
      if (this.item.linkId) {
        this.changeNowPage(-1)
        this.$nextTick(() => {
          this.changeNowPage(this.item.linkId)
        })
      }
    },
    setTimer () {
      this.interval = this.item.interval || 2;
      // console.log(this.interval);
      this.timer = setInterval(() => {
        // 仅一张图时不轮播
        if (this.item.srcList.length < 2) {
          return
        }
        let activeIndex = this.activeIndex + 1;
        if (activeIndex == this.item.srcList.length - 1) {
          !this.item.loop && this.clearTimer();
          activeIndex = 0;
        }
        this.activeIndex = activeIndex;
      }, this.interval * 1000);
    },
    clearTimer () {
      this.timer && clearTimeout(this.timer);
    },
    change (num) {
      let activeIndex = this.activeIndex + num;
      if (activeIndex > this.maxIndex) {
        if (!this.item.loop) {
          return;
        }
        activeIndex = 0
      }
      if (activeIndex < 0) {
        if (!this.item.loop) {
          return;
        }
        activeIndex = this.maxIndex
      }
      console.log(activeIndex)
      // this.$set('activeIndex', activeIndex)
      this.activeIndex = activeIndex;
    },
    setActiveIndex (index) {
      this.activeIndex = index;
    }
  },
}
</script>
<style scoped lang="scss">
.img_card {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: background-image 1s;
}
.left_btn, .right_btn {
  // width: 50%;
  // height: 100%;
  padding: 16px;
  cursor: pointer;
  color: rgba(256, 256, 256, 0.1);
  font-size: 60px;
  &:hover {
    font-size: 600%;
    color: rgba(256, 256, 256, 0.6);
  }
}

// .left_btn {
//   background: red;
// }
// .right_btn {
//   background: blue;
// }
</style>