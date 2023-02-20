<template>
    <div class="TextRotation">
        <el-carousel :autoplay="item.autoPlay" :interval="Number(item.speed)" :direction="item.direction?'vertical':'horizontal'" arrow="never" indicator-position="none">
          <el-carousel-item v-for="item in pageNum" :key="item">
            <div class="textContent">
              <div class="rows" v-for="(data, index) in getPageData(item)" :key="index">
                <div class="key" :style="keyStyle">{{data['key']}}</div>
                <div>
                  <div class="value" :style="valueStyle">{{data['value']}}</div>
                  <div class="unit" v-show="data['unit']" :style="unitStyle">{{data['unit']}}</div>
                </div>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
    </div>
</template>
<script>
export default {
  data () {
    return {

    }
  },
  props: ['item'],
  computed: {
    pageNum () {
      if (this.item.chartData.dataArray && this.item.pageSize) {
        return Math.ceil(this.item.chartData.dataArray.length / this.item.pageSize)
      } else {
        return 0
      }
    },
    getPageData () {
      return (index) => {
        let arr = []
        if (this.item.chartData.dataArray) {
          arr = this.item.chartData.dataArray.slice((index - 1) * this.item.pageSize, index * this.item.pageSize)
        }
        return arr
      }
    },
    keyStyle () {
      console.log('fam', this.item.keyFamily)
      return {
        fontSize: this.item.keySize + 'px !important',
        color: this.item.keyColor,
        fontWeight: this.item.keyWeight + ' !important',
        letterSpacing: this.item.keySpace + 'px !important',
        fontFamily: this.item.keyFamily ? this.item.keyFamily + ' !important' : ''
      }
    },
    valueStyle () {
      return {
        fontSize: this.item.valueSize + 'px !important',
        color: this.item.valueColor,
        fontWeight: this.item.valueWeight + ' !important',
        letterSpacing: this.item.valueSpace + 'px !important',
        fontFamily: this.item.valueFamily ? this.item.valueFamily + ' !important' : '',
        marginRight: this.item.valueRight + 'px !important'
      }
    },
    unitStyle () {
      return {
        fontSize: this.item.unitSize + 'px !important',
        color: this.item.unitColor,
        fontWeight: this.item.unitWeight + ' !important',
        letterSpacing: this.item.unitSpace + 'px !important',
        fontFamily: this.item.unitFamily ? this.item.unitFamily + ' !important' : ''
      }
    },
  }

}
</script>
<style lang="scss">
.TextRotation{
  width: 100%;
  height: 100%;
  .el-carousel{
    width: 100%;
    height: 100%;
    .el-carousel__container{
      width: 100%;
      height: 100%;
      .textContent{
        width: 100%;
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-between;
        .rows{
          width: 100%;
          display: flex;
          align-items: center;
          justify-content: space-between;
          >div:last-child{
            display: flex;
            align-items: center;
          }
        }
      }
    }
  }
}
</style>
