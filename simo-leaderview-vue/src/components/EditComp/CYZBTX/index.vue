<template>
  <div class="CYZBTX" :style="boxStyle">
    <div class="streetBox" v-for="(element, index) in item.chartData.rows" v-show="element['值班职务'] === '值班领导'" :key="index">
      <div class="streetName">{{element['所属街道']}}</div>
      <div class="streetInfo">
        <div class="personnelInfo">
          <div>
            <img v-if="element['照片链接']" :src="element['照片链接']" style="width: 136px !important;height: 182px !important;object-fit: fill !important;" alt="">
            <img v-else src="./矩形.png" style="width: 136px !important;height: 182px !important;object-fit: fill !important;" alt="">
          </div>
          <div>
            <div class="name">{{element['领导姓名']}}</div>
            <div class="phone">{{element['手机号']}}</div>
            <div class="position">{{element['职务']}}</div>
          </div>
        </div>
        <!-- <div class="personnelInfo">
          <div>
            <img v-if="element['指挥长']['照片链接']" :src="element['指挥长']['照片链接']" style="width: 136px !important;height: 182px !important;object-fit: fill !important;" alt="">
            <img v-else src="./矩形.png" style="width: 136px !important;height: 182px !important;object-fit: fill !important;" alt="">
          </div>
          <div>
            <div class="name">{{element['指挥长']['领导姓名']}}</div>
            <div class="phone">{{element['指挥长']['手机号']}}</div>
            <div class="position">{{element['指挥长']['职务']}}</div>
          </div>
        </div>
        <div class="personnelInfo">
          <div>
            <img v-if="element['值班长']['照片链接']" :src="element['值班长']['照片链接']" style="width: 136px !important;height: 182px !important;object-fit: fill !important;" alt="">
            <img v-else src="./矩形.png" style="width: 136px !important;height: 182px !important;object-fit: fill !important;" alt="">
          </div>
          <div>
            <div class="name">{{element['值班长']['领导姓名']}}</div>
            <div class="phone">{{element['值班长']['手机号']}}</div>
            <div class="position">{{element['值班长']['职务']}}</div>
          </div>
        </div> -->
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'CYZBTX',
  props: ['item'],
  data () {
    return {
      boxData: {}
    }
  },
  computed: {
    boxStyle () {
      return {
        width: this.item.width + 'px',
        height: this.item.height + 'px'
      }
    }
  },
  watch: {
    // 'item.chartData': {
    //   handler (newV, oldV) {
    //     this.boxData = {}
    //     if (this.item.chartData.rows && this.item.chartData.rows.length) {
    //       this.item.chartData.rows.forEach(element => {
    //         if (this.boxData[element['所属街道']]) {
    //           this.boxData[element['所属街道']][element['职务']] = element
    //         } else {
    //           this.boxData[element['所属街道']] = {}
    //           this.boxData[element['所属街道']][element['职务']] = element
    //         }
    //       })
    //       console.log('boxData', this.boxData)
    //     }
    //   },
    //   deep: true
    // }
  },
  mounted () {
    // if (this.item.chartData.rows && this.item.chartData.rows.length) {
    //   this.item.chartData.rows.forEach(element => {
    //     if (this.boxData[element['所属街道']]) {
    //       this.boxData[element['所属街道']][element['职务']] = element
    //     } else {
    //       this.boxData[element['所属街道']] = {}
    //       this.boxData[element['所属街道']][element['职务']] = element
    //     }
    //   })
    //   console.log('boxData', this.boxData)
    // }
  },
  methods: {},
  beforeDestroy () {}
}
</script>
<style scoped lang="scss">
  .CYZBTX{
    overflow-y: scroll;
    display: flex;
    flex-wrap: wrap;
    // flex-direction: column;
    // justify-content: space-around;
    // align-items: center;
    .streetBox{
      // width: 1324px;
      margin-bottom: 30px;
      .streetName{
        width: 330px;
        height: 72px;
        background: url(./标题背景.png) no-repeat;
        background-size: 100% 100%;
        color: rgba(172,207,254,1);
        font-size: 28px;
        line-height: 72px;
        padding-left: 24px;
        margin-bottom: 20px;
      }
      .streetInfo{
        width: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .personnelInfo{
          display: flex;
          >div:last-child{
            width: 200px;
            position: relative;
            .name{
              color: rgba(172,207,254,1);
              font-size: 36px;
              padding-left: 28px;
            }
            .phone{
              color: rgba(172,207,254,1);
              font-size: 24px;
              padding-left: 28px;
            }
            .position{
              color: rgba(172,207,254,1);
              font-size: 24px;
              padding-left: 28px;
              height: 44px;
              line-height: 44px;
              width: 200px;
              background: url(./职位背景.png) no-repeat;
              background-size: 100% 100%;
              position: absolute;
              bottom: 0;
            }
          }
        }
      }
    }
  }
</style>
