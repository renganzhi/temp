<template>
  <div class="keyprojects">
    <div class="title">
        <div class="openTitle" @click="isShowModel = !isShowModel"></div>
    </div>
    <div class="body">
      <div class="Data">
        <div class="leftData">
          <div class="data" style="width:260px;height:55px">
            <div class="name">项目总数</div>
            <div class="data">256个</div>
          </div>
          <div class="BotData" style="width:260px;height:55px">
            <div class="data" style="width:128px;height:55px">
              <div class="name">总投资</div>
              <div class="data">2567.38亿</div>
            </div>
            <div class="data" style="width:128px;height:55px">
              <div class="name">本年度计划投资</div>
              <div class="data">567.5亿</div>
            </div>
          </div>
        </div>
        <div class="center">
          <div class="centertitle" style="color: rgb(200, 224, 255);height: 20px;">重点项目批次组成</div>
          <div class="canvas" ref="PeiModel" style="width: 100%;height: calc(100% - 20px);">

          </div>
        </div>
        <div class="rightData">
          <div class="centertitle" style="color: rgb(200, 224, 255);height: 20px;">项目进度组成</div>
          <div class="canvas" ref="PeiModel2" style="width: 100%;height: calc(100% - 20px);"></div>
        </div>
      </div>
      <transition name="moveLeft">
        <div class="Table" v-if="isShowModel">
          <Table border :columns="columns1" :height="125" :data="data1"></Table>
        </div>
    </transition>
    </div>
  </div>
</template>
<script>
import echarts from 'echarts'
export default {
  data () {
    return {
      columns1: [
        {
          title: '项目名称',
          key: 'name'
        },
        {
          title: '项目批次',
          key: 'age'
        },
        {
          title: '投资类别',
          key: 'address'
        },
        {
          title: '投资总额',
          key: 'data1'
        },
        {
          title: '牵头领导',
          key: 'data2'
        },
        {
          title: '业主单位',
          key: 'data3'
        },
        {
          title: '责任单位',
          key: 'data4'
        },
        {
          title: '行业分类',
          key: 'data5'
        },
        {
          title: '所属区域',
          key: 'data6'
        },
        {
          title: '所属功能区',
          key: 'data7'
        },
        {
          title: '所属行业部门',
          width: 90,
          key: 'data8'
        }
      ],
      data1: [
        {
          name: '污水处理项目',
          age: '新开工项目',
          address: '政府投资',
          data1: '2500万元',
          data2: '郑春雷',
          data3: '区环保局',
          data4: '区环保局',
          data5: '水利基础建设',
          data6: '望江路街道',
          data7: '悦湖新材料科技转换中心',
          data8: '区卫健委'
        },
        {
          name: '污水处理项目',
          age: '新开工项目',
          address: '政府投资',
          data1: '2500万元',
          data2: '郑春雷',
          data3: '区环保局',
          data4: '区环保局',
          data5: '水利基础建设',
          data6: '望江路街道',
          data7: '悦湖新材料科技转换中心',
          data8: '区卫健委'
        },
        {
          name: '污水处理项目',
          age: '新开工项目',
          address: '政府投资',
          data1: '2500万元',
          data2: '郑春雷',
          data3: '区环保局',
          data4: '区环保局',
          data5: '水利基础建设',
          data6: '望江路街道',
          data7: '悦湖新材料科技转换中心',
          data8: '区卫健委'
        },
        {
          name: '污水处理项目',
          age: '新开工项目',
          address: '政府投资',
          data1: '2500万元',
          data2: '郑春雷',
          data3: '区环保局',
          data4: '区环保局',
          data5: '水利基础建设',
          data6: '望江路街道',
          data7: '悦湖新材料科技转换中心',
          data8: '区卫健委'
        }
      ],
      isShowModel: false,
      mychart: null,
      mychart2: null
    }
  },
  mounted () {
    this.mychart = echarts.init(this.$refs.PeiModel)
    this.mychart2 = echarts.init(this.$refs.PeiModel2)
    this.getOption1()
    this.getOption2()
  },
  methods: {
    getOption1 () {
      var m2R2Data = [
        {
          value: 200,
          legendname: '新开项目',
          name: '新开项目  200',
          itemStyle: { color: '#8d7fec' }
        },
        {
          value: 50,
          legendname: '续建项目',
          name: '续建项目  50',
          itemStyle: { color: '#5085f2' }
        },
        {
          value: 40,
          legendname: '竣工项目',
          name: '竣工项目  40',
          itemStyle: { color: '#e75fc3' }
        }
      ]
      let option = {
        tooltip: {
          trigger: 'item',
          formatter: function (parms) {
            var str =
              parms.seriesName +
              '</br>' +
              parms.marker +
              '' +
              parms.data.legendname +
              '</br>' +
              '数量：' +
              parms.data.value +
              '</br>' +
              '占比：' +
              parms.percent +
              '%'
            return str
          }
        },
        legend: {
          type: 'scroll',
          orient: 'vertical',
          left: '60%',
          align: 'left',
          top: 'middle',
          formatter: function (parms) {
            var str = parms + '个'
            return str
          },
          textStyle: {
            color: '#8C8C8C',
            fontSize: 8
          },
          height: 95
        },
        series: [
          {
            name: '标题',
            type: 'pie',
            center: ['35%', '50%'],
            radius: ['40%', '65%'],
            clockwise: false, // 饼图的扇区是否是顺时针排布
            avoidLabelOverlap: false,
            label: {
              show: false
            },
            data: m2R2Data
          }
        ]
      }
      this.mychart.setOption(option)
    },
    getOption2 () {
      var m2R2Data = [
        {
          value: 46,
          legendname: '正常推进',
          name: '正常推进  46',
          itemStyle: { color: '#8d7fec' }
        },
        {
          value: 200,
          legendname: '相对滞后',
          name: '相对滞后  200',
          itemStyle: { color: '#5085f2' }
        }
      ]
      let option = {
        tooltip: {
          trigger: 'item',
          formatter: function (parms) {
            var str =
              parms.seriesName +
              '</br>' +
              parms.marker +
              '' +
              parms.data.legendname +
              '</br>' +
              '数量：' +
              parms.data.value +
              '</br>' +
              '占比：' +
              parms.percent +
              '%'
            return str
          }
        },
        series: [
          {
            name: '标题',
            type: 'pie',
            center: ['50%', '50%'],
            radius: ['0%', '65%'],
            clockwise: false, // 饼图的扇区是否是顺时针排布
            avoidLabelOverlap: false,
            label: {
              show: true,
              formatter: '{b}个',
              fontSize: 8
            },
            labelLine: {
              normal: {
                length: 4,
                length2: 8,
                lineStyle: {
                  width: 1
                }
              }
            },
            data: m2R2Data
          }
        ]
      }

      this.mychart2.setOption(option)
    }
  }
}
</script>
<style lang="scss" scoped>
.keyprojects{
  height: 170px;
  width: 780px;
  .title{
    height: 45px;
    width: 100%;
    background-image: url('./img/02.png');
    background-size: 100%;
    position: relative;
    .openTitle{
      width: 40px;
      height: 22px;
      position: absolute;
      right: 11px;
      top: 10px;
      cursor: pointer;
      background-image: url('./img/00.png');
      background-size: 100%;
    }
  }
  .body{
    width: 100%;
    height: 125px;
    background-color: #122F61;
    position: relative;
    .Data{
      width: 100%;
      height: 100%;
      display: flex;
      .leftData{
        height: 100%;
        width: 270px;
        padding: 5px;
        display: flex;
        justify-content: space-between;
        flex-wrap: wrap;
        align-items: center;
        .BotData{
          display: flex;
          justify-content: space-between;
        }
        .data{
          background-color: #182b4c;
          display: flex;
          flex-wrap: wrap;
          .name{
            height: 45%;
            width: 100%;
            display: flex;
            font-size: 14px;
            color: #C8E0FF;
            justify-content: center;
            align-items: flex-end;
          }
          .data{
            height: 55%;
            width: 100%;
            font-size: 16px;
            display: flex;
            color: #C8E0FF;
            justify-content: center;
            align-items: flex-start;
          }
        }
      }
      .center{
        width: 260px;
        height: 100%;
        padding: 5px;
      }
      .rightData{
        width: 250px;
        height: 100%;
        padding: 5px;
      }
    }
  }
  .Table{
    width: 100%;
    height: 125px;
    position: absolute;
    top: 0;
    left: 0;
    background-color: #122F61;
  }
}

.keyprojects{
    display: flex;
    flex-wrap: wrap;
    font-family: monospace !important;
    // overflow: hidden;
    .leftPart,.rightPart{
        display: flex;
    }
    .moveRight-enter-active {
    animation: box-right-in 0.5s;
    }
    .moveRight-leave-active {
        animation: box-left-leave 0.5s;
    }
    .moveLeft-enter-active {
    animation: box-left-in 0.5s;
    }
    .moveLeft-leave-active {
        animation: box-right-leave 0.5s;
    }
    .moveTop-enter-active {
    animation: box-top-in 0.5s;
    }
    .moveTop-leave-active {
        animation: box-bottom-leave 0.5s;
    }
    .moveBottom-enter-active {
    animation: box-bottom-in 0.5s;
    }
    .moveBottom-leave-active {
        animation: box-top-leave 0.5s;
    }
    // 向左移动
    @keyframes box-left-leave {
    from {
        transform: translateX(0);
    }
    to {
        transform: translateX(-100%);
    }
    }
    @keyframes box-left-in {
    from {
        transform: translateX(100%);
    }
    to {
        transform: translateX(0);
    }
    }
    // 向右移动
    @keyframes box-right-leave {
    from {
        transform: translateX(0);
    }
    to {
        transform: translateX(100%);
    }
    }
    @keyframes box-right-in {
    from {
        transform: translateX(-100%);
    }
    to {
        transform: translateX(0);
    }
    }
    // 向上移动
    @keyframes box-top-leave {
    from {
        transform: translateY(0);
    }
    to {
        transform: translateY(-100%);
    }
    }
    @keyframes box-top-in {
    from {
        transform: translateY(100%);
    }
    to {
        transform: translateY(0);
    }
    }
    // 向下移动
    @keyframes box-bottom-leave {
    from {
        transform: translateY(0);
    }
    to {
        transform: translateY(100%);
    }
    }
    @keyframes box-bottom-in {
    from {
        transform: translateY(-100%);
    }
    to {
        transform: translateY(0);
    }
    }
}
</style>
