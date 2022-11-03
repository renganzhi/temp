<template>
    <div class="cityEvent" :style="boxStyle">
        <div @click="showDetails(val)"  class="eventBox" v-for="(val, ind) in item.chartData.rows" :key="ind">
            <div class="title" :style="{backgroundImage: 'linear-gradient(' + item.titleColor[0] + ',' + item.titleColor[1] + ')',fontSize:item.titleSize + 'px'}">{{val.title}}</div>
            <div class="date" :style="{color:item.dateColor,fontSize:item.dateSize + 'px'}">{{val.date}}</div>
            <div class="content" :style="{color:item.contentColor,fontSize:item.contentSize + 'px'}">{{val.content}}</div>
        </div>
    </div>
</template>
<script>
export default {
  props: ['item'],
  computed: {
    boxStyle: function () {
      return {
        width: this.item.width + 'px !important',
        height: this.item.height + 'px !important'
      }
    }
  },
  methods: {
    showDetails (data) {
      console.log('data', data)
      let d = {}
      for (let i in data) {
        if (i === 'title') {
          d['标题'] = data[i]
        } else if (i === 'content') {
          d['内容'] = data[i]
        } else if (i === 'date') {
          d['时间'] = data[i]
        } else {
          d[i] = data[i]
        }
      }
      if (this.$route.name === 'HomePage' || this.$route.name === 'lookPage' || this.$route.name === 'popPage') {
        this.$parent.$parent.ShowVenationBox({
          title: '事件详情',
          data: d,
          url: this.item.chartData.url
        })
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.cityEvent{
    display: flex;
    // justify-content: space-between;
    align-items: center;
    flex-direction: column;
    .eventBox{
        width: 100%;
        height: 33%;
        padding: 10px 20px;
        overflow: hidden;
        margin-bottom: 10px;
        background: #122f61;
        font-weight: bold;
        overflow-y: scroll;
        .title{
            // 渐变色字体
            background-image: linear-gradient(#f04d4d, rgba(211, 179, 179, 0.67));
            background-clip: text;
            -webkit-background-clip: text;
            color: transparent;
            font-size: 34px;
        }
        .date{
            font-size: 22px;
            color: #3667ff;
        }
        .content{
            // height: 80%;
            // overflow: scroll;
            // text-overflow: ellipsis;
            font-size: 27px;
            color: #6689f8;
        }
    }
}
</style>
