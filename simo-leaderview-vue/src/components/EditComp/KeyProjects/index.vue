<template>
  <div class="keyprojects">
    <!-- <div class="title">
      <div class="name">
        重点项目
      </div>
      <div class="openTitle" @click="ChangeisShowModel"></div>
    </div> -->
      <div class="openTitle" @click="ChangeisShowModel"></div>
    <div class="body">
      <div class="Data">
        <div class="leftData">
          <div class="data" style="width:260px;height:88px">
            <div class="name" style="font-weight:bold;">项目总数</div>
            <div class="data1">{{modelBodyData['项目总数'] || 0}}个</div>
          </div>
          <div class="BotData" style="width:260px;height:80px">
            <div class="data" style="width:128px;height:80px">
              <div class="name" style="font-weight:bold;font-size:14px;">总投资</div>
              <div class="data1" style="font-size:16px;">{{modelBodyData['总投资']||0}}万元</div>
            </div>
            <div class="data" style="width:128px;height:80px">
              <div class="name" style="font-weight:bold;font-size:14px;">本年度计划投资</div>
              <div class="data1" style="font-size:16px;">{{modelBodyData['本年度投资计划']||0}}万元</div>
            </div>
          </div>
        </div>
        <div class="center">
          <div class="centertitle" style="color: rgb(200, 224, 255);font-size: 16px;height: 20px;font-weight:bold;text-align:center;">重点项目批次组成</div>
          <div class="canvas" ref="PeiModel" style="width: 100%;height: calc(100% - 20px);">

          </div>
        </div>
        <div class="rightData">
          <div class="centertitle" style="color: rgb(200, 224, 255);font-size: 16px;height: 20px;font-weight:bold;text-align:center;">项目进度组成</div>
          <div class="canvas" ref="PeiModel2" style="width: 100%;height: calc(100% - 20px);"></div>
        </div>
      </div>
      <transition name="moveLeft">
        <div class="Table" v-if="isShowModel">
          <Table border :columns="columns1" :height="220" :data="data1" @on-row-click='clickLine'></Table>
        </div>
      </transition>
      <transition name="moveLeft">
        <div class="xqData" v-if="isShowXq">
          <div class="Line" v-for="(value,key,index) in isShowXqData" :key="index">
            <div class="name">{{key}}:</div>
            <div class="value">{{value}}</div>
          </div>
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
          key: '项目名称'
        },
        {
          title: '项目批次',
          key: '项目批次'
        },
        {
          title: '投资类别',
          key: '投资类别'
        },
        {
          title: '投资总额',
          key: '投资总额'
        },
        // {
        //   title: '牵头领导',
        //   key: '牵头领导'
        // },
        {
          title: '业主单位',
          key: '业主单位'
        },
        // {
        //   title: '责任单位',
        //   key: '责任单位'
        // },
        {
          title: '行业分类',
          key: '行业分类'
        },
        {
          title: '所属区域',
          key: '所属区域'
        },
        {
          title: '所属功能区',
          key: '所属功能区'
        },
        {
          title: '所属行业部门',
          width: 90,
          key: '所属行业部门'
        }
      ],
      data1: [],
      modelBodyData: {},
      isShowModel: false,
      isShowXq: false,
      isShowXqData: {},
      colorList: [
        '#ffd965',
        '#97d87e',
        '#4aa8eb',
        '#3bafff',
        '#f1bb4c',
        'rgba(250,250,250,0.5)'
      ],
      mychart: null,
      mychart2: null
    }
  },
  mounted () {
    this.mychart = echarts.init(this.$refs.PeiModel)
    this.mychart2 = echarts.init(this.$refs.PeiModel2)
    this.axios.get('/leaderview/ChengYun4/GetFGW1').then(res => {
      if (res.success) {
        this.modelBodyData = res.obj.rows[0] || {}
        this.data1 = this.modelBodyData['项目列表'] || []
        this.getOption1()
        this.getOption2()
      }
    })
  },
  methods: {
    ChangeisShowModel () {
      if (this.isShowXq) {
        this.isShowXq = !this.isShowXq
        this.isShowXqData = {}
      } else {
        this.isShowModel = !this.isShowModel
      }
    },
    clickLine (data) {
      this.isShowXq = true
      this.isShowXqData = data
    },
    getOption1 () {
      var m2R2Data = []
      this.modelBodyData['重大项目批次组成'].forEach(d => {
        m2R2Data.push({
          value: d['数量'],
          legendname: d['名称'],
          name: d['名称'] + ':' + d['数量']
        })
      })
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
        'legend': {
          'icon': 'circle',
          'x': 'center',
          top: '70%',
          formatter: function (parms) {
            var str = parms + '个'
            return str
          },
          'textStyle': {
            color: '#dfdfdf',
            fontSize: 14
          }
        },
        series: [
          {
            name: '标题',
            type: 'pie',
            center: ['50%', '40%'],
            radius: ['30%', '40%'],
            clockwise: false, // 饼图的扇区是否是顺时针排布
            avoidLabelOverlap: false,
            itemStyle: {
              normal: {
                color: (params) => {
                  return this.colorList[params.dataIndex]
                }
              }
            },
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
      var m2R2Data = []
      this.modelBodyData['重大项目类别组成'].forEach(d => {
        m2R2Data.push({
          value: d['数量'],
          legendname: d['名称'],
          name: d['名称'] + '    ' + d['数量']
        })
      })
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
        'legend': {
          'icon': 'circle',
          'x': 'center',
          top: '70%',
          formatter: function (parms) {
            var str = parms + '个'
            return str
          },
          'textStyle': {
            color: '#dfdfdf',
            fontSize: 14
          }
        },
        series: [
          {
            name: '标题',
            type: 'pie',
            center: ['50%', '40%'],
            radius: ['0%', '40%'],
            clockwise: false, // 饼图的扇区是否是顺时针排布
            avoidLabelOverlap: false,
            itemStyle: {
              normal: {
                color: (params) => {
                  return this.colorList[params.dataIndex]
                }
              }
            },
            label: {
              show: false
            },
            // labelLine: {
            //   normal: {
            //     length: 3,
            //     length2: 6,
            //     lineStyle: {
            //       width: 2
            //     }
            //   }
            // },
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
  position: relative;
  // .title{
  //   height: 45px;
  //   width: 100%;
  //   background-image: url('./img/02.png');
  //   background-size: 100%;
  //   position: relative;
  //   .name{
  //     color: #d4e7ff;
  //     margin-left: 50px;
  //     font-size: 22px;
  //     height: 100%;
  //     display: flex;
  //     align-items: center;
  //   }
  // }
  .openTitle{
    width: 40px;
    height: 22px;
    position: absolute;
    right: 11px;
    top: 10px;
    z-index: 10;
    cursor: pointer;
    background-image: url('./img/00.png');
    background-size: 100%;
  }
  .body{
    width: 100%;
    height: 220px;
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
          background-image: url('./img/03.png');
          background-size: 100% 100%;
          display: flex;
          flex-wrap: wrap;
          .name{
            height: 45%;
            width: 100%;
            display: flex;
            font-size: 20px;
            color: rgb(212, 231, 255) !important;
            justify-content: center;
            align-items: flex-end;
          }
          .data1{
            height: 55%;
            width: 100%;
            font-size: 18px;
            display: flex;
            background-image: -webkit-linear-gradient(bottom, rgb(221, 151, 59), rgb(255, 238, 215)) !important;
            color: transparent;
            -webkit-background-clip: text;
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
    height: 220px;
    position: absolute;
    top: 0;
    left: 0;
    background-image: url('./img/bg.png');
    background-size: 100% 100%;
  }
  .xqData{
    width: 100%;
    height: 220px;
    overflow: auto;
    position: absolute;
    top: 0;
    left: 0;
    background-color: #122f61;
    padding: 4px 20px;
    .Line{
      display: flex;
      height: 30px;
      font-size: 16px;
      align-items: center;
    }
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
