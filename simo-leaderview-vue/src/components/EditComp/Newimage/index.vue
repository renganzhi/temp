<template>
  <div :style="boxStyle" @click="showDetail">
    <div class="v-charts-data-empty"
         v-if="!getImg">请上传图片</div>
    <img v-show="ifShow" ref="img" :src="getImg"
         v-else
         :style="imgSctyle" />
  </div>
</template>
<script>
import qs from 'qs'
import { gbs } from '@/config/settings'
export default {
  name: 'Newimage',
  props: ['item'],
  data () {
    return {
      baseUrl: '',
      ifShow: true,
      clock: ''
    }
  },
  beforeMount () {
    var reg = /^\/api/
    if (!reg.test(this.item.imgSrc)) {
      this.baseUrl = gbs.host
      // this.baseUrl = gbs.host + '/leaderview'
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
    getImg: function () {
      let url = ''
      if (this.item.imgSrc) {
        url = this.baseUrl + this.item.imgSrc
      } else if (this.item.chartData.value) {
        if (this.item.chartData.name === '今日天气') {
          try {
            url = require('./' + this.item.chartData.value + '.png')
          } catch (err) {

          }
        } else if (this.item.chartData.name === '照片链接') {
          url = this.item.chartData.value
        }
      } else if (this.item.chartData.ifTrue) {
        if (this.item.chartData.ifTrue === '1') {
          url = require('./红色.png')
        } else {
          url = require('./绿色.png')
        }
      }
      return url
    },
    imgSctyle: function () {
      if (this.item.showType && this.item.showType === '1') {
        return {
          maxWidth: '100%',
          maxHeight: '100%'
        }
      }
      return {
        width: '100%',
        height: '100%'
      }
    }
  },
  watch: {
    'item.chartData': {
      handler () {
        this.startClock()
      },
      deep: true
    }
  },
  methods: {
    showDetail () {
      if (this.item.chartData.ifTrue === '1') {
        if (this.item.chartData.typeAndNumber) {
          let data = {}
          for (let i in this.item.chartData.typeAndNumber) {
            if (this.item.chartData.typeAndNumber[i] && this.item.chartData.typeAndNumber[i] !== 0) {
              data[i] = this.item.chartData.typeAndNumber[i]
            }
          }
          this.ifShow = true
          if (this.$route.name === 'HomePage' || this.$route.name === 'lookPage') {
            this.axios({
              method: 'post',
              url: '/leaderview/QingBao/GetYJYP6_2',
              data: qs.stringify({param: '风险预警'}),
              headers: { 'content-type': 'application/x-www-form-urlencoded' }
            }).then(res => {
              if (res.success) {
                clearInterval(this.clock)
                this.clock = ''
                this.item.chartData.ifTrue = '0'
                this.$parent.$parent.ShowTanKuangBox({
                  title: '最新预警',
                  data: data
                })
              }
            })
          }
        }
      }
    },
    startClock () {
      this.ifShow = true
      if (this.item.chartData.ifTrue) {
        let num = 1
        if (this.item.chartData.ifTrue === '1') {
          if (!this.clock) {
            this.clock = setInterval(() => {
              if (num % 2 === 0) {
                this.ifShow = false
              } else {
                this.ifShow = true
              }
              num++
            }, 1000)
          }
        } else {
          this.ifShow = true
          clearInterval(this.clock)
          this.clock = ''
        }
      } else {
        clearInterval(this.clock)
        this.clock = ''
      }
    }
  },
  mounted () {
    this.startClock()
  },
  beforeDestroy: function () {
    clearInterval(this.clock)
    this.clock = ''
  }
}
</script>
