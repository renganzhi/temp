<template>
    <div class="StreetSafety">
        <div class="bigTitle">城运平安</div>
        <div class="bigBody">
            <div class="DateSwitch">
                <div :class="{'normalBtn':true,'active':dateSwitch === 1?true:false}" @click="dateSwitch = 1">昨日</div>
                <div :class="{'normalBtn':true,'active':dateSwitch === 2?true:false}" @click="dateSwitch = 2">今日</div>
            </div>
            <div class="StateSwitch">
                <div :class="{'regularBtn':true,'active':stateSwitch === 1?true:false}" @click="stateSwitch = 1">已报<span style="color:#00B3FC;margin-left: 5px;font-weight: bold;">{{currentData['oneCount']}}</span></div>
                <div :class="{'regularBtn':true,'active':stateSwitch === 0?true:false}" @click="stateSwitch = 0">未报<span style="color:#FFA478;margin-left: 5px;font-weight: bold;">{{currentData['zeroCount']}}</span></div>
                <div :class="{'regularBtn':true,'active':stateSwitch === 2?true:false}" @click="stateSwitch = 2">异常<span style="color:#FB6546;margin-left: 5px;font-weight: bold;">{{currentData['twoCount']}}</span></div>
            </div>
            <div class="middleTitle">报送情况</div>
            <div class="middleBody">
                <div class="streetBox" v-for="(item1, index1) in currentData.streetList||[]" :key="index1">
                    <div class="streetTitle" @click="ShowDetail(item1.flowNo)">
                        {{item1.name}}
                        <img style="margin-left:20px;" :src="getImg1(item1.flag)" alt="">
                    </div>
                    <div class="communityList">
                        <div class="communityBox" v-show="stateSwitch === -1?true:stateSwitch === item2.flag" v-for="(item2, index2) in item1.communityList" :key="index2" @click="ShowDetail(item2.flowNo)">
                            {{item2.name}}
                            <img :src="getImg2(item2.flag)" alt="">
                        </div>
                    </div>
                </div>
            </div>
            <transition name="moveRight">
                <div class="popBox" v-show="showSafetyDetail">
                    <div class="popTtile">详情<img src="./关闭.png" style="cursor: pointer;" alt="" @click="CloseDetail()"></div>
                    <div class="popBody">
                        <div class="detailList">
                            <div class="detailBox" v-for="(value, key) in safetyDetail" :key="key">
                                <div class="key">{{ key }}：</div>
                                <div class="value">{{ value }}</div>
                            </div>
                        </div>
                        <div class="flowTitle">处置流程线</div>
                        <div class="flowLine">
                            <EventVenation style="left:673px;" v-if="venationData.chartData.data" :item="venationData"></EventVenation>
                        </div>
                    </div>
                </div>
            </transition>
            <Spin fix v-show="ifLoad">
                <Icon type="ios-loading" size=180 class="demo-spin-icon-load"></Icon>
                <div style="font-size: 50px;">加载中...</div>
            </Spin>
        </div>
    </div>
</template>
<script>
import EventVenation from '@/components/EditComp/EventVenation'
import element from '@/element'
export default {
  name: 'StreetSafety',
  props: ['item'],
  components: {EventVenation},
  data () {
    return {
      dateSwitch: 2, // 1昨日，2今日
      stateSwitch: -1, // 0未报，1已报，2异常
      allData: [],
      ifLoad: false,
      safetyDetail: {}, // 报平安详情
      showSafetyDetail: false
    }
  },
  computed: {
    currentData: function () {
      let data = {}
      if (this.allData.length) {
        if (this.dateSwitch === 2) { // 今日
          data = JSON.parse(JSON.stringify(this.allData[0]['今日']))
          data.streetList = []
          if (this.stateSwitch !== -1) {
            this.allData[0]['今日']['streetList'].forEach(element => {
              if (element.flag !== this.stateSwitch) {
                for (let i = 0; i < element['communityList'].length; i++) {
                  if (element['communityList'][i]['flag'] === this.stateSwitch) {
                    data.streetList.push(element)
                    break
                  }
                }
              } else {
                data.streetList.push(element)
              }
            })
          } else {
            this.allData[0]['今日']['streetList'].forEach(element => {
              data.streetList.push(element)
            })
          }
        } else { // 昨日
          data = JSON.parse(JSON.stringify(this.allData[1]['昨日']))
          data.streetList = []
          if (this.stateSwitch !== -1) {
            this.allData[1]['昨日']['streetList'].forEach(element => {
              if (element.flag !== this.stateSwitch) {
                for (let i = 0; i < element['communityList'].length; i++) {
                  if (element['communityList'][i]['flag'] === this.stateSwitch) {
                    data.streetList.push(element)
                    break
                  }
                }
              } else {
                data.streetList.push(element)
              }
            })
          } else {
            this.allData[1]['昨日']['streetList'].forEach(element => {
              data.streetList.push(element)
            })
          }
        }
      }
      return data
    },
    getImg1 () {
      return (state) => {
        if (state === 1) {
          return require('./切图/6.png')
        } else if (state === 0) {
          return require('./切图/7.png')
        } else if (state === 2) {
          return require('./切图/8.png')
        } else {
          return ''
        }
      }
    },
    getImg2 () {
      return (state) => {
        if (state === 1) {
          return require('./切图/4.png')
        } else if (state === 0) {
          return require('./切图/5.png')
        } else {
          return ''
        }
      }
    },
    venationData () {
      return {
        'text': '事件脉络',
        'imgClass': 'icon-n-text',
        'chartType': 'EventVenation',
        'width': 1565,
        'height': 400,
        'titleFontSize': 42,
        'titleBottm': 10,
        'iconColor': '#F2BE77',
        'titleColor': '#86e2f7',
        'contBorderColor': '#25406A',
        'contPadding': 20,
        'contBorderRdius': 5,
        'contTitleSize': 36,
        'contTitleColor': '#F2BE77',
        'contColor': '#cef1ff',
        'dateLeft': -614,
        'dateTop': 0,
        'contSize': 36,
        'chartData': {}
      }
    }
  },
  watch: {
    dateSwitch: function () {
      this.stateSwitch = -1
    }
  },
  methods: {
    getPageData () {
      this.ifLoad = true
      this.axios.get('/leaderview/ChengYun4/GetBPA1').then(res => {
        this.ifLoad = false
        if (res.success) {
          this.allData = res.obj.data
        }
      })
    },
    ShowDetail (flowNo) {
      if (flowNo) {
        this.ifLoad = true
        this.axios.get('/leaderview/ChengYun4/GetTJDB1?param=' + flowNo).then(res => {
          if (res.obj.rows) {
            this.safetyDetail = JSON.parse(JSON.stringify(res.obj.rows[0]))
          }
        })
        this.axios.get('/leaderview/ChengYun4/GetTJDB2?param=' + flowNo).then(res => {
          this.ifLoad = false
          this.showSafetyDetail = true
          if (res.obj) {
            this.venationData.chartData = res.obj
          }
        })
      }
    },
    CloseDetail () {
      this.safetyDetail = {}
      this.showSafetyDetail = false
      this.venationData.chartData = {}
    }
  },
  mounted () {
    this.getPageData()
  }
}
</script>
<style scoped lang="scss">
.StreetSafety{
    width: 100%;
    height: 100%;
    background: url('./切图/1.png') no-repeat;
    background-size: 100% 100%;
    padding: 18px 80px 0px 80px;
    .bigTitle{
        background-image: -webkit-linear-gradient(bottom, #90DEFF, #FFFFFF) !important;
        -webkit-background-clip:text;
        -webkit-text-fill-color:transparent;
        font-size: 56px;
        font-weight: 700;
        letter-spacing: 10px;
        text-shadow: 0px 2px 4px 0px rgba(10,30,52,0.48);
        margin-bottom: 80px;
        width: 100%;
        padding-left: 80px;
    }
    .bigBody{
        width: 100%;
        overflow-x: hidden;
        position: relative;
        .DateSwitch{
            display: flex;
            .normalBtn{
                width: 200px;
                height: 92px;
                background: url('./切图/12.png') no-repeat;
                background-size: 100% 100%;
                margin-right: 40px;
                color: #fff;
                font-size: 48px;
                font-weight: 400;
                text-align: center;
                line-height: 92px;
                cursor: pointer;
            }
            .active{
                background: url('./切图/11.png') no-repeat !important;
                background-size: 100% 100%;
                color: #00FFF7 !important;
            }
        }
        .StateSwitch{
            margin-top: 60px;
            display: flex;
            .regularBtn{
                height: 80px;
                min-width: 240px;
                line-height: 80px;
                cursor: pointer;
                text-align: center;
                font-size: 36px;
                color: #FFFFFF;
                font-weight: 500;
                background: url('./切图/2.png') no-repeat;
                background-size: 100% 100%;
                margin-right: 40px;
            }
            .active{
                background: url('./切图/3.png') no-repeat !important;
                background-size: 100% 100%;
            }
        }
        .middleTitle{
            width: 100%;
            height: 80px;
            background: url('./切图/13.png') no-repeat;
            background-size: 100% 100%;
            margin-top: 40px;
            line-height: 80px;
            padding-left: 90px;
            font-size: 36px;
            font-weight: 500;
            color: #fff;
            letter-spacing: 3px;
        }
        .middleBody{
            width: 100%;
            height: 734px;
            overflow: scroll;
            margin-top: 40px;
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            .streetBox{
                width: 1340px;
                height: 346px;
                margin-bottom: 40px;
                background: url('./切图/14.png') no-repeat;
                background-size: 100% 100%;
                padding-bottom: 20px;
                padding-right: 20px;
                .streetTitle{
                    font-size: 36px;
                    font-weight: 500;
                    width: 100%;
                    height: 80px;
                    color: #fff;
                    cursor: pointer;
                    padding-left:92px;
                    display: flex;
                    align-items: center;
                }
                .communityList{
                    width: 100%;
                    height: calc(100% - 100px);
                    display: flex;
                    padding: 30px 8px 10px 28px;
                    overflow: scroll;
                    flex-wrap: wrap;
                    .communityBox{
                        min-width: 298px;
                        height: 80px;
                        line-height: 80px;
                        color: #C6F8F9;
                        cursor: pointer;
                        font-size: 28px;
                        text-align: center;
                        position: relative;
                        background: #0d4971;
                        border-radius: 2px;
                        margin-bottom: 20px;
                        margin-right: 20px;
                        border: 1px solid #66ABF8;
                        >img{
                            position: absolute;
                            top: 0px;
                            right: 0px;
                        }
                    }
                }
            }
        }
        .popBox{
            position: absolute;
            top: -5px;
            left: 0px;
            background: url('./切图/15.png') no-repeat;
            background-size: 100% 100%;
            width: calc(100% + 0px);
            height: calc(100% + 10px);
            .popTtile{
                background-image: -webkit-linear-gradient(bottom, #90DEFF, #FFFFFF) !important;
                -webkit-background-clip:text;
                -webkit-text-fill-color:transparent;
                font-size: 44px;
                font-weight: 700;
                letter-spacing: 10px;
                text-shadow: 0px 2px 4px 0px rgba(10,30,52,0.48);
                margin-bottom: 60px;
                width: 100%;
                height: 80px;
                padding-left: 80px;
                padding-right: 10px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .popBody{
                width: 100%;
                height: calc(100% - 140px);
                overflow: scroll;
                padding: 0 40px;
                .detailList{
                    width: 100%;
                    .detailBox{
                        width: 100%;
                        display: flex;
                        border-top: 2px dotted #baf0fc;
                        .key{
                            color: #baf0fc;
                            font-size: 40px;
                            letter-spacing: 2px;
                            width: 20%;
                            padding: 10px;
                            min-height: 80px;
                            border-right: 2px dotted #baf0fc;
                        }
                        .value{
                            color: #baf0fc;
                            font-size: 40px;
                            padding: 10px;
                            letter-spacing: 2px;
                            width: 80%;
                            min-height: 80px;
                        }
                    }
                    border-left: 2px dotted #baf0fc;
                    border-right: 2px dotted #baf0fc;
                    border-bottom: 2px dotted #baf0fc;
                }
                .flowTitle{
                    font-size: 38px;
                    color: #b6e2f5;
                    font-weight: 600;
                    letter-spacing: 10px;
                    text-shadow: 0px 2px 4px 0px rgba(10,30,52,0.48);
                    margin: 30px 0;
                    width: 100%;
                    height: 80px;
                    padding-left: 80px;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    background: url('./切图/10.png') no-repeat;
                }
            }
        }
    }

}

</style>
<style lang="scss">
.StreetSafety{
    .moveRight-enter-active {
    animation: box-right-in 0.6s;
    }
    .moveRight-leave-active {
        animation: box-left-leave 0.6s;
    }
    @keyframes box-left-leave {
    from {
        transform: translateX(0);
    }
    to {
        transform: translateX(-100%);
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
    .ivu-spin-fix{
        height: 100%;
        width: 100%;
      }
    .demo-spin-icon-load{
        animation: ani-demo-spin 1s linear infinite;
    }
    @keyframes ani-demo-spin {
        from { transform: rotate(0deg);}
        50%  { transform: rotate(180deg);}
        to   { transform: rotate(360deg);}
    }
    .demo-spin-col{
        height: 100px;
        position: relative;
        border: 1px solid #eee;
    }
    .ivu-spin-fix{
        background: rgba(10, 43, 58, 0.6) !important;
    }
}
</style>
