<template>
    <div class="EfficiencyPage">
      <div id="Module55">
        <div class="Mydyj" v-if="mydData['态度预警数']">
          <div class="YJbox" @click="OpenShowyjBox">
            <div class="img">
              <img src="./newBack/8.png" alt="">
            </div>
            <div class="Value">
              <div class="data">{{mydData['态度预警数']?mydData['态度预警数'].info:''}} <div class="unit">件</div></div>
              <div class="name">部门承办态度预警</div>
            </div>
          </div>

                <div id="Module99" v-if="ShowyjBox">
                    <div class="title">
                      部门承办态度预警
                      <img src="./newBack/16.png" alt="" @click="ShowyjBox=false">
                    </div>
                    <div class="content">
                        <div class="cityEvent" ref="cityEvent"
                           >
                            <ul class="item" ref="item" style="width:100%">
                                <li v-for="(val, ind) in qztsList" :key="ind">
                                <div  class="eventBox" >
                                    <div>
                                      <div><span></span>{{val['标题']}}</div>
                                      <div v-if="val['流转详情'] && val['流转详情'].rows" @click="ShowOtherDetails(val)">详情</div>
                                    </div>
                                    <div>
                                        {{val['描述']}}
                                    </div>
                                    <div>
                                        {{val['上报时间']}}
                                    </div>
                                </div>
                                </li>
                            </ul>
                        </div>
                        <transition name="moveLeft">
                            <div id="Module99Pop" v-show="showotherDetails">
                              <div style="height: 76px; display: flex;justify-content: space-between; align-items: center;background-image: linear-gradient(45deg, hsl(187deg 94% 53% / 10%), rgb(22 223 248 / 2%))">
                                  <span style="font-size: 30px;margin-left: 24px;display: flex;align-items: center;color: #5AE8FA;font-weight: 600;">部门承办态度详情</span>
                                  <img style="height: 49px;width: 49px;margin-right: 20px;cursor: pointer;" @click="CloseotherDetails" src="./background/关闭.png" alt="">
                              </div>
                              <div style="with:100%;overflow: auto;height:calc(100% - 80px)">
                                <div style="margin: 26px;font-size: 28px;color: #C5EEF3;max-height: 600px;overflow: auto;">{{xqValue['标题'] || ''}}</div>
                                <div style="margin: 0 28px; color: #C5EEF3;font-size: 24px;max-height:600px;overflow: auto;padding: 16px;background-image: linear-gradient(45deg, rgb(22 223 248 / 4%), rgb(22 223 248 / 10%),rgb(22 223 248 / 4%));">
                                  {{xqValue['描述'] || ''}}
                                </div>
                                <div style="margin: 28px;font-size: 24px;color: #C5EEF3;">处置时间线</div>
                                <div class="block" style="padding: 0 28px;" v-if="xqValue['流转详情']">
                                    <div class="TimeBox" v-for="(da,index) in xqValue['流转详情'].rows" :key="index">
                                      <div class="line" v-if="index !== xqValue['流转详情'].rows.length-1"></div>
                                      <div class="radio"></div>
                                      <div class="time">{{da['修改时间']}}</div>
                                      <div class="data">
                                        <div>流转状态：{{da['当前节点名称']}}</div>
                                        <div>流转内容：{{da['流转内容']}}</div>
                                      </div>
                                    </div>
                                </div>
                              </div>
                            </div>
                        </transition>
                    </div>
                </div>
          <div class="YJbox">
            <div class="img">
              <img v-if="mydData['态度预警趋势'].info==='下降'" src="./newBack/9.png" alt="">
              <img v-if="mydData['态度预警趋势'].info!=='下降'" src="./newBack/10.png" alt="">
            </div>
            <div class="Value">
              <div class="data">{{mydData['态度预警趋势'].info}}</div>
              <div class="name">部门承办态度趋势</div>
            </div>
          </div>
          <div class="YJbox">
            <div class="img">
              <img v-if="mydData['不满意趋势'].info==='下降'" src="./newBack/9.png" alt="">
              <img v-if="mydData['不满意趋势'].info!=='下降'" src="./newBack/10.png" alt="">
            </div>
            <div class="Value">
              <div class="data">{{mydData['不满意趋势'].info}}</div>
              <div class="name">不满意趋势预警</div>
            </div>
          </div>
        </div>
        <div class="Mydgy" v-if="mydData['不满意数']">
          <div class="Gybox" @click="OpenShomydjBox(mydData['不满意数'].name)">
            <div class="Value">{{mydData['不满意数'].info}}</div>
            <div class="Name">本日不满意回访数</div>
          </div>
          <div class="Gybox"  @click="OpenShomydjBox(mydData['满意数'].name)">
            <div class="Value">{{mydData['满意数'].info}}</div>
            <div class="Name">本日满意回访数</div>
          </div>
          <div class="Gybox"  @click="OpenShomydjBox(mydData['未解决数'].name)">
            <div class="Value">{{mydData['未解决数'].info}}</div>
            <div class="Name">本日未解决回访数</div>
          </div>
          <div class="Gybox"  @click="OpenShomydjBox(mydData['解决数'].name)">
            <div class="Value">{{mydData['解决数'].info}}</div>
            <div class="Name">本日已解决回访数</div>
          </div>
              <div id="Module66" :style="{left:leftData[ShowmydType]+'px'}" v-if="ShowmydBox">
                  <div class="title">
                    {{dataName[ShowmydType]}}
                    <img src="./newBack/16.png" alt="" @click="ShowmydBox=false">
                  </div>
                  <div class="content">
                      <div class="cityEvent" ref="cityEvent"
                          >
                          <ul class="item" ref="item" style="width:100%">
                              <li v-for="(val, ind) in mydgyList" :key="ind">
                              <div  class="eventBox" >
                                  <div>
                                      <div><span></span>{{val['标题']}}</div>
                                      <div v-if="val['流转详情'] && val['流转详情'].rows" @click="ShowmydDetails(val)">详情</div>
                                  </div>
                                  <div>
                                      {{val['描述']}}
                                  </div>
                                  <div>
                                      {{val['上报时间']}}
                                  </div>
                              </div>
                              </li>
                          </ul>
                      </div>
                      <transition name="moveLeft">
                          <div id="Module66Pop" v-if="showmydDetails">
                            <div style="height: 76px; display: flex;justify-content: space-between; align-items: center;background-image: linear-gradient(45deg, hsl(187deg 94% 53% / 10%), rgb(22 223 248 / 2%))">
                                <span style="font-size: 30px;margin-left: 24px;display: flex;align-items: center;color: #5AE8FA;font-weight: 600;">回访详情</span>
                                <img style="height: 49px;width: 49px;margin-right: 20px;cursor: pointer;" @click="ClosemydDetails" src="./background/关闭.png" alt="">
                            </div>
                            <div style="with:100%;overflow: auto;height:calc(100% - 80px)">
                              <div style="margin: 26px;font-size: 28px;color: #C5EEF3;max-height: 600px;overflow: auto;">{{mydValue['标题'] || ''}}</div>
                              <div style="margin: 0 28px; color: #C5EEF3;font-size: 24px;max-height:600px;overflow: auto;padding: 16px;background-image: linear-gradient(45deg, rgb(22 223 248 / 4%), rgb(22 223 248 / 10%),rgb(22 223 248 / 4%));">
                                {{mydValue['描述'] || ''}}
                              </div>
                              <div style="margin: 28px;font-size: 24px;color: #C5EEF3;">处置时间线</div>
                              <div class="block" style="padding: 0 28px;">
                                  <div class="TimeBox" v-for="(da,index) in mydValue['流转详情'].rows" :key="index">
                                    <div class="line" v-if="index !== mydValue['流转详情'].rows.length-1"></div>
                                    <div class="radio"></div>
                                    <div class="time">{{da['修改时间']}}</div>
                                    <div class="data">
                                      <div>流转状态：{{da['当前节点名称']}}</div>
                                      <div>流转内容：{{da['流转内容']}}</div>
                                    </div>
                                  </div>
                              </div>
                            </div>
                          </div>
                      </transition>
                  </div>
              </div>
        </div>
      </div>
      <div id="Module4" v-if="bodyData['街道总览']">
          <div class="whole">
              <div class="datepicker">
                  <DatePicker :value="dateValue"  type="daterange" split-panels placeholder="请输入时间" style="width: 365px" @on-change='chengBodyData'></DatePicker>
              </div>
              <div class="row1 diyTabsStyle backgroun27">
                  <Tabs value="name1">
                      <TabPane label="街道总览" name="name1">
                          <div class="tabContent1">
                              <div class="backgroun24 streeBox">
                                <div class="titleTop">
                                  <div class="leftOne">
                                    <div class="streeName">街道总览</div>
                                    <div class="streeNum">{{bodyData['街道总览'].rows[0]['办件量']}}</div>
                                    <span>总办件量</span>
                                  </div>
                                </div>
                                <div class="newprogress">
                                  <div class="canvas">
                                    <MyProgress :successdata='bodyData["街道总览"].rows[0]["未完成率"]' />
                                  </div>
                                  <div class="data1">
                                    <div>处置中 {{bodyData['街道总览'].rows[0]['未完成率']}}%</div>
                                    <div>已完成 {{bodyData['街道总览'].rows[0]['完成率']}}%</div>
                                  </div>
                                  <div class="data2">
                                    <div style="color:#F59B42">{{bodyData['街道总览'].rows[0]['办理中']}}</div>
                                    <div style="color:#3DF8C2">{{bodyData['街道总览'].rows[0]['完成数量']}}</div>
                                  </div>
                                  <!-- <div class="datatime allDatatime">
                                    <div>平均办件时间</div>
                                    <div style="color:#C5EEF3">0天0小时0分钟</div>
                                  </div> -->
                                </div>
                                <!-- <div class="peoplevalue">
                                  <div>----</div>
                                  <div>----</div>
                                </div>
                                <div class="peoplename">
                                  <div>处置人员</div>
                                  <div>人均办件量</div>
                                </div> -->
                              </div>
                              <div class="streeBox backgroun25" v-for="(data,index) in bodyData['各街道数量统计'].rows" :key="index" @click="ShowStreetInfoFun(data)">
                                <div class="titleTop">
                                  <div class="leftOne">
                                    <div class="streeName">{{data['单位']}}</div>
                                    <div class="streeNum">{{data['办件量']}}</div>
                                    <span>总办件量</span>
                                  </div>
                                  <div class="rightpic" v-if="data['单位']">
                                    <img :src="require('./'+data['单位']+'.jpg')" alt="">
                                  </div>
                                </div>
                                <div class="newprogress">
                                  <div class="canvas">
                                    <MyProgress :successdata='data["未完成率"]' />
                                  </div>
                                  <div class="data1">
                                    <div>处置中 {{data['未完成率']}}%</div>
                                    <div>已完成 {{data['完成率']}}%</div>
                                  </div>
                                  <div class="data2">
                                    <div style="color:#F59B42">{{data['办理中']}}</div>
                                    <div style="color:#3DF8C2">{{data['完成数量']}}</div>
                                  </div>
                                  <!-- <div class="datatime">
                                    <div>平均办件时间</div>
                                    <div style="color:#C5EEF3">{{data['平均办件时长'] ? data['平均办件时长']:'0天0小时0分钟'}}</div>
                                  </div> -->
                                </div>
                                <!-- <div class="peoplevalue">
                                  <div>{{(data['单位'] ==='簇桥街道' && bodyData['簇桥街道-办件人员数和人均办件量_自定义时段'])?bodyData['簇桥街道-办件人员数和人均办件量_自定义时段'].rows[0]['办件人员数']:'----'}}</div>
                                  <div>{{(data['单位'] ==='簇桥街道' && bodyData['簇桥街道-办件人员数和人均办件量_自定义时段'])?bodyData['簇桥街道-办件人员数和人均办件量_自定义时段'].rows[0]['人均办件量']:'----'}}</div>
                                </div>
                                <div class="peoplename">
                                  <div>处置人员</div>
                                  <div>人均办件量</div>
                                </div> -->
                              </div>
                          </div>
                      </TabPane>
                      <TabPane label="委办局总览" name="name3">
                          <div class="tabContent2">
                              <div class="backgroun24 streeBox">
                                <div class="titleTop">
                                  <div class="leftOne">
                                    <div class="streeName">委办局总览</div>
                                    <div class="streeNum">{{bodyData['委办局总览'].rows[0]['总办件量']}}</div>
                                    <span>总办件量</span>
                                  </div>
                                </div>
                                <div class="newprogress">
                                  <div class="canvas">
                                    <MyProgress :successdata='100 - bodyData["委办局总览"].rows[0]["完成率"]' />
                                  </div>
                                  <div class="data1">
                                    <div>处置中 {{100 - bodyData['委办局总览'].rows[0]['完成率']}}%</div>
                                    <div>已完成 {{bodyData['委办局总览'].rows[0]['完成率']}}%</div>
                                  </div>
                                  <div class="data2">
                                    <div style="color:#F59B42">{{bodyData['委办局总览'].rows[0]['总办件量'] - bodyData['委办局总览'].rows[0]['完成数量']}}</div>
                                    <div style="color:#3DF8C2">{{bodyData['委办局总览'].rows[0]['完成数量']}}</div>
                                  </div>
                                  <!-- <div class="datatime allDatatime">
                                    <div>平均办件时间</div>
                                    <div style="color:#C5EEF3">0天10小时30分钟</div>
                                  </div> -->
                                </div>
                                <!-- <div class="peoplevalue">
                                  <div>6736</div>
                                  <div>26</div>
                                </div>
                                <div class="peoplename">
                                  <div>处置人员</div>
                                  <div>人均办件量</div>
                                </div> -->
                              </div>
                              <div class="streeBox backgroun25" v-for="(data,index) in bodyData['各委办局数量统计'].rows" :key="index">
                                <div class="titleTop">
                                  <div class="leftOne">
                                    <div class="streeName">{{data['单位']}}</div>
                                    <div class="streeNum">{{data['办件量']}}</div>
                                    <span>总办件量</span>
                                  </div>
                                </div>
                                <div class="newprogress">
                                  <div class="canvas">
                                    <MyProgress :successdata='data["未完成率"]' />
                                  </div>
                                  <div class="data1">
                                    <div>处置中 {{data['未完成率']}}%</div>
                                    <div>已完成 {{data['完成率']}}%</div>
                                  </div>
                                  <div class="data2">
                                    <div style="color:#F59B42">{{data['办理中']}}</div>
                                    <div style="color:#3DF8C2">{{data['完成数量']}}</div>
                                  </div>
                                  <!-- <div class="datatime">
                                    <div>平均办件时间</div>
                                    <div style="color:#C5EEF3">0天10小时30分钟</div>
                                  </div> -->
                                </div>
                                <!-- <div class="peoplevalue">
                                  <div>6736</div>
                                  <div>26</div>
                                </div>
                                <div class="peoplename">
                                  <div>处置人员</div>
                                  <div>人均办件量</div>
                                </div> -->
                              </div>
                          </div>
                      </TabPane>
                  </Tabs>
              </div>
              <!-- <div class="row2">
                  <div class="left backgroun28">
                      <div>
                          <IntegratedHistogram :item="getBar1"></IntegratedHistogram>
                      </div>
                  </div>
                  <div class="right backgroun29">
                      <div>
                          <IntegratedHistogram :item="getBar2"></IntegratedHistogram>
                      </div>
                  </div>
              </div> -->
          </div>
          <div class="part" v-if="showStreetInfo && modelData['总办件量']">
              <div class="Btn" @click="showStreetInfo = false"
              style="position: absolute;
                    right: 16px;
                    top: 16px;
                    font-size: 30px;
                    cursor: pointer
                    height: 40px;
                    width: 40px;
              "><img src="./background/关闭.png" alt="" style="width:100%;height:100%"></div>
              <div class="partTitle">
                <!-- <div class="select">
                  <Select v-model="SelectType">
                    <Option value="normal" name='normal' label="全部事件">全部事件</Option>
                  </Select>
                </div> -->
                <div class="state">
                  <div style="color: rgb(90, 232, 250);display: flex;align-items: center;">{{modelData['总办件量'].info}} <div :class="true?'upBack':'downBack'">66%</div></div>
                  <div>总办件量</div>
                </div>
                <div class="datavalue">
                  <div class="data2">
                    <div style="color:#F59B42">{{modelData['处置中办件量'].info}}</div>
                    <div style="color:#3DF8C2">{{modelData['已完成办件量'].info}}</div>
                  </div>
                  <div class="canvas" style="width:400px">
                    <MyProgress :successdata='50' />
                  </div>
                  <div class="data1">
                    <div>处置中 {{modelData['处置中办件量占比'].info}}%</div>
                    <div>已完成 {{modelData['已完成办件量占比'].info}}%</div>
                  </div>
                </div>
                <div class="datatime">
                  <div style="color: rgb(90, 232, 250);display: flex;align-items: center;">{{modelData.bodyData?modelData.bodyData['平均办件时长']:'0天0小时0分钟'}} <div :class="true?'upBack':'downBack'">66%</div></div>
                  <div>平均办件时间</div>
                </div>
                <div class="peoplevalue">
                  <div style="color:#5AE8FA">6736</div>
                  <div>共有处置人员</div>
                </div>
                <div class="peoplename">
                  <div style="color:#5AE8FA">26</div>
                  <div>人均办件量</div>
                </div>
              </div>
              <div class='bodyData' style="display: flex;width: 100%;height: 710px;">
                <div class="partBody">
                  <div class="eventType">
                    <div class="title">
                      <div class="radioBtn" style="font-size:28px">事件类型统计</div>
                      <div style="font-size:28px">单位：件</div>
                    </div>
                    <div class="canvas">
                      <NewPie :item="getSJLX1"></NewPie>
                    </div>
                  </div>
                  <!-- <div class="eventwcl">
                    <div class="title">
                      <div class="radioBtn" style="font-size:22px">办理完成率</div>
                    </div>
                    <div class="canvas"></div>
                  </div> -->
                  <!-- <div class="hotevent">
                    <div class="title">
                      <div class="radioBtn" style="font-size:22px">热门事件</div>
                    </div>
                    <div class="canvas">

                      <div class="events" v-for="(data,index) in modelData['热点事件_自定义时段'].rows" :key="index">
                        <div class="name">
                          {{data['事件小类名称']}}
                        </div>
                        <div class="value">
                          {{data['事件小类办件量']}}
                        </div>
                      </div>
                    </div>
                  </div> -->
                </div>
                <div class="partFoot">
                  <!-- <div class="eventtj">
                    <div class="title">
                      <div class="radioBtn" style="font-size:22px">办件量统计</div>
                      <div style="font-size:20px">单位：件</div>
                    </div>
                    <div class="canvas"></div>
                  </div> -->
                  <div class="hotevent">
                    <div class="title">
                      <div class="radioBtn" style="font-size:28px">热门事件</div>
                    </div>
                    <div class="canvas">

                      <div class="events" v-for="(data,index) in modelData['热点事件_自定义时段'].rows" :key="index">
                        <div class="name">
                          {{data['事件小类名称']}}
                        </div>
                        <div class="value">
                          {{data['事件小类办件量']}}
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="eventqs">
                    <div class="title">
                      <div class="radioBtn" style="font-size:28px">办件量走势</div>
                      <div style="font-size:28px">单位：件</div>
                    </div>
                    <div class="canvas" style="height:210px">
                        <ELine :item="getBJLZS"></ELine>
                    </div>
                  </div>
                </div>
              </div>
          </div>
      </div>
      <div id="Module3" v-if="bodyData['事件分布总览']">
          <div class="row1">
              <div class="percentage backgroun21">
                <div class="percentChild backgroun35" v-for="(data,index) in bodyData['事件分布总览'].rows" :key="index">
                  <div :class="selectType===data['平台']?'name checked':'name'" @click="selectTypeData(data['平台'])">
                    {{data['平台']}}
                  </div>
                  <div class="value">
                    <MyProgress :successdata="data['百分比']" :progressType='2' :color='colorArry[index]'/>
                  </div>
                </div>
              </div>
          </div>
          <!-- <div class="row2">
              <div class="boxList backgroun22">
                  <div class="Abox " v-for="(data, index) in bodyData['热门事件'].rows" :key="index">
                      <div class="name">{{data[bodyData['热门事件'].columns[0]]}}</div>
                      <div class="backgrounwt"><span>{{data[bodyData['热门事件'].columns[1]]}}</span></div>
                  </div>
              </div>
          </div> -->
          <div class="row3">
              <div class="backgroun23">
                  <ELine :item="getOfficeTrend"></ELine>
              </div>
          </div>
      </div>
    </div>
</template>
<script>
import IntegratedHistogram from '../IntegratedHistogram/index.vue'
import ELine from '../ELine/index.vue'
import NewGauge from '../NewGauge/index.vue'
import NewProgress from '../NewProgress/index.vue'
import MyProgress from './newprogress.vue'
import CityEvent from '../CityEvent/index.vue'
import NewPie from '../NewPie/index.vue'
export default {
  data () {
    return {
      showStreetInfo: false,
      ShowyjBox: false,
      ShowmydBox: false,
      ShowmydType: '',
      showmydDetails: false,
      SelectType: 'normal',
      mydValue: {},
      xqValue: {},
      qztsList: [], // 投诉列表
      mydgyList: [], // 投诉列表
      mydData: {}, // 投诉列表
      showotherDetails: false,
      dateValue: [],
      selectType: '',
      colorArry: [['#61BEF5', '#61bef533'], ['#F8DE52', '#F8DE5233'], ['#F59B42', '#F59B4233'], ['#DC614F', '#DC614F33']],
      modelData: {},
      bodyData: {},
      leftData: {
        'unpleased': -550,
        'pleased': -100,
        'unsolved': 350,
        'solved': 800
      },
      dataName: {
        'unpleased': '本日不满意回访数',
        'pleased': '本日满意回访数',
        'unsolved': '本日未解决回访数',
        'solved': '本日已解决回访数'
      },
      getOfficeTrend: {
        'text': '曲线图',
        'imgClass': 'icon-n-line',
        'height': 320,
        'width': 1600,
        'chartType': 'ELine',
        'ifEidetColor': true, // 曲线是否配色
        'ifEidetColor2': true,
        'ifGradual': 'false', // 曲线是否渐变
        'ifAreaGradual': 'true', // 区域是否渐变
        'splitShow': false,
        'ctLegendShow': false,
        'ctLegendColor': '#828bac',
        'ctLegendSize': '20',
        'axisLabelSize': '20',
        'legendY': 90,
        'gridTop': 5,
        'gridBotton': 5,
        'gridLeft': 3,
        'gridRight': 3,
        'tooltipShow': true,
        'subsectionType': true,
        'areaLineType': true,
        'tooltipBackColor': '#57625d',
        'tooltipTextColor': '#e9eaee',
        'tooltipfontSize': 20,
        'splitColor': '#d0e0e3',
        'splitSize': 2,
        'minInterval': '',
        'Linesubsection': false,
        'boundaryGap': true,
        'legendColor': '#828bac',
        'DanweiColor': '#828bac',
        'DanweiSize': 20,
        'lineArea': true, // 是否为区域图
        'lineColorType': false, // 是否为区域图
        'smooth': false,
        'LineType': 'solid',
        'formatterType': '1',
        'symbolType': 'circle',
        'symbolSrc': '',
        'symbolName': '',
        'symbolSize': 6,
        'lineWidth': 2,
        'showPoint': true, // 是否标点
        'PointSize': '14',
        'rotate': 0,
        conditionType: '', // 接口选择
        refrashTime: 30000,
        interval: 0,
        'colorMatchType': 'line', // 配色类型
        'ScatterColor': [
          'rgba(213, 153, 17, 1)',
          'rgba(2, 210, 255, 1)',
          '#67e0e3',
          '#9fe6b8',
          '#ffdb5c'],
        'DScatterColor': [
          ['rgba(213, 153, 17, 0.52)', '#be4d24'],
          ['rgba(2, 210, 255, 0.49)', '#1bbcae'],
          ['#fa8d76', '#db4222'],
          ['#af8af3', '#874edc'],
          ['#f5739c', '#f31d53'],
          ['#ffdf91', '#eeb01b'],
          ['#5c84e7', '#144fe5'],
          ['#85f8c0', '#62dc26']
        ],
        'AreaScatterColor': [
          '#2d98f1',
          '#32c5e9',
          '#67e0e3',
          '#9fe6b8',
          '#ffdb5c'
        ], // 区域单色
        'AreaDScatterColor': [
          ['rgba(213, 153, 17, 0)', 'rgba(213, 153, 17, 0.5)'],
          ['rgba(2, 210, 255, 0)', 'rgba(2, 210, 255, 0.5)'],
          ['#fa8d76', '#db4222'],
          ['#af8af3', '#874edc'],
          ['#f5739c', '#f31d53'],
          ['#ffdf91', '#eeb01b'],
          ['#5c84e7', '#144fe5'],
          ['#85f8c0', '#62dc26']
        ], // 区域渐变
        'chartData': {
          'columns': [],
          'unit': '',
          'min': 60,
          'max': 80,
          'minIndex': 2,
          'maxIndex': 3,
          'unitX': '时间',
          'rows': []
        }
      },
      getBar1: {
        'text': '柱状图',
        width: 1496,
        height: 450,
        'imgClass': 'icon-n-histogram',
        'chartType': 'IntegratedHistogram',
        'barType': 'NewHistogram',
        'ifGradual': 'false',
        'showBackground1': false, // 是否显示背景柱图
        'backgroundColor1': 'rgba(180, 180, 180, 0.2)', // 背景柱图的颜色
        'barRadius1': 0, // 柱体圆角
        'barWidth1': 30, // 柱体大小
        'HistogramType': 3, // 柱状图类型
        'dataTypeSet': 2, // 柱状图类型
        'lineColor': '#fff', // 类型2中的分割线颜色
        'downColor': '#6d9eeb', // 类型3中下方颜色
        'upColor': '#4a86e8', // 类型3中上方颜色
        'topTextColor': '#fff', // 柱状图类型2、3顶部文字的颜色
        'ctLegendShow1': false,
        'dataSetType': false,
        'colorful1': false,
        'ifGradual1': 'false',
        'legendColor1': '',
        'splitShow1': false,
        'ctLegendSize1': '20',
        'TabFontSize': '20',
        'colorType1': 'custom',
        'ScatterColor1': [
          '#6fcaf7',
          '#8feee5',
          '#fa8d76',
          '#af8af3',
          '#f5739c',
          '#ffdf91',
          '#5c84e7'],
        'ctLegendColor1': '#666f8b',
        'axisLabelSize1': '20',
        'DanweiColor1': '#828bac',
        'DanweiSize1': 20,
        'minInterval1': '',
        'legendY1': 90,
        'gridTop1': 10,
        'gridBotton1': 10,
        'gridLeft1': 10,
        'gridRight1': 10,
        'formatterType1': '0',
        'cropSize1': 2,
        'tooltipShow1': true,
        'dataTypeStation': false,
        'tooltipBackColor1': '#57625d',
        'tooltipTextColor1': '#fff',
        'tooltipfontSize1': 22,
        'splitColor1': '#fff',
        'splitSize1': 1,
        'rotate1': 0,
        'chartData1': {
          'columns': [],
          'unit': '次',
          'rows': []
        }
      },
      getBar2: {
        'text': '柱状图',
        width: 1420,
        height: 450,
        'imgClass': 'icon-n-histogram',
        'chartType': 'IntegratedHistogram',
        'barType': 'NewHistogram',
        'ifGradual': 'false',
        'showBackground1': false, // 是否显示背景柱图
        'backgroundColor1': 'rgba(180, 180, 180, 0.2)', // 背景柱图的颜色
        'barRadius1': 0, // 柱体圆角
        'barWidth1': 30, // 柱体大小
        'HistogramType': 3, // 柱状图类型
        'dataTypeSet': 2, // 柱状图类型
        'lineColor': '#fff', // 类型2中的分割线颜色
        'downColor': '#ffff00', // 类型3中下方颜色
        'upColor': '#ff9900', // 类型3中上方颜色
        'topTextColor': '#fff', // 柱状图类型2、3顶部文字的颜色
        'ctLegendShow1': false,
        'dataSetType': false,
        'colorful1': false,
        'ifGradual1': 'false',
        'legendColor1': '',
        'splitShow1': false,
        'ctLegendSize1': '20',
        'TabFontSize': '20',
        'colorType1': 'custom',
        'ScatterColor1': [
          '#ffd966',
          '#8feee5',
          '#fa8d76',
          '#af8af3',
          '#f5739c',
          '#ffdf91',
          '#5c84e7'],
        'ctLegendColor1': '#666f8b',
        'axisLabelSize1': '20',
        'DanweiColor1': '#828bac',
        'DanweiSize1': 20,
        'minInterval1': '',
        'legendY1': 90,
        'gridTop1': 10,
        'gridBotton1': 4,
        'gridLeft1': 2,
        'gridRight1': 2,
        'formatterType1': '0',
        'cropSize1': 2,
        'tooltipShow1': true,
        'dataTypeStation': false,
        'tooltipBackColor1': '#57625d',
        'tooltipTextColor1': '#fff',
        'tooltipfontSize1': 22,
        'splitColor1': '#fff',
        'splitSize1': 1,
        'rotate1': 0,
        'chartData1': {
          'columns': ['项目', '完成率'],
          'unit': '%',
          'rows': []
        }
      },
      getSJLX1: {
        'text': '环形图',
        'width': 1620,
        'height': 710,
        'imgClass': 'icon-n-ring',
        'chartType': 'NewPie',
        'ifGradual': 'false',
        'pieType': '环形图',
        'ctLegendSize': '26',
        'ctLegendColor': '#666f8b',
        'axisLabelSize': '26',
        'showwordSize': 22,
        'ifEidetColor': false,
        'legendY': 85,
        'radius': 50,
        'showline': true,
        'showword': true,
        'isRing': true,
        'detailwidth': 12,
        'borderRadius': 0,
        'ringWidth': 50,
        'tooltipShow': true,
        'tooltipBackColor': '#57625d',
        'tooltipTextColor': '#fff',
        'tooltipfontSize': 24,
        'LineColorArray': [
          '#2d98f1',
          '#32c5e9',
          '#67e0e3',
          '#9fe6b8',
          '#ffdb5c',
          '#ffb092'],
        'DLineColorArray': [
          ['rgba(213, 153, 17, 0.52)', '#be4d24'],
          ['rgba(2, 210, 255, 0.49)', '#1bbcae'],
          ['#fa8d76', '#db4222'],
          ['#af8af3', '#874edc'],
          ['#f5739c', '#f31d53'],
          ['#ffdf91', '#eeb01b'],
          ['#5c84e7', '#144fe5'],
          ['#85f8c0', '#62dc26']
        ],
        'chartData': {
          'columns': [],
          'unit': '次',
          'rows': []
        }
      },
      getBJLZS: {
        'text': '曲线图',
        'imgClass': 'icon-n-line',
        'height': 210,
        'width': 1580,
        'chartType': 'ELine',
        'ifEidetColor': true, // 曲线是否配色
        'ifEidetColor2': true,
        'ifGradual': 'false', // 曲线是否渐变
        'ifAreaGradual': 'true', // 区域是否渐变
        'splitShow': false,
        'ctLegendShow': false,
        'ctLegendColor': '#828bac',
        'ctLegendSize': '24',
        'axisLabelSize': '24',
        'legendY': 90,
        'gridTop': 5,
        'gridBotton': 5,
        'gridLeft': 2,
        'gridRight': 2,
        'tooltipShow': true,
        'subsectionType': true,
        'areaLineType': true,
        'tooltipBackColor': '#57625d',
        'tooltipTextColor': '#e9eaee',
        'tooltipfontSize': 24,
        'splitColor': '#d0e0e3',
        'splitSize': 2,
        'minInterval': '',
        'Linesubsection': false,
        'boundaryGap': true,
        'legendColor': '#828bac',
        'DanweiColor': '#828bac',
        'DanweiSize': 24,
        'lineArea': true, // 是否为区域图
        'lineColorType': false, // 是否为区域图
        'smooth': false,
        'LineType': 'solid',
        'formatterType': '1',
        'symbolType': 'circle',
        'symbolSrc': '',
        'symbolName': '',
        'symbolSize': 6,
        'lineWidth': 2,
        'showPoint': true, // 是否标点
        'PointSize': '20',
        'rotate': 0,
        conditionType: '', // 接口选择
        refrashTime: 30000,
        interval: 0,
        'colorMatchType': 'line', // 配色类型
        'ScatterColor': [
          'rgba(213, 153, 17, 1)',
          'rgba(2, 210, 255, 1)',
          '#67e0e3',
          '#9fe6b8',
          '#ffdb5c'],
        'DScatterColor': [
          ['rgba(213, 153, 17, 0.52)', '#be4d24'],
          ['rgba(2, 210, 255, 0.49)', '#1bbcae'],
          ['#fa8d76', '#db4222'],
          ['#af8af3', '#874edc'],
          ['#f5739c', '#f31d53'],
          ['#ffdf91', '#eeb01b'],
          ['#5c84e7', '#144fe5'],
          ['#85f8c0', '#62dc26']
        ],
        'AreaScatterColor': [
          '#2d98f1',
          '#32c5e9',
          '#67e0e3',
          '#9fe6b8',
          '#ffdb5c'
        ], // 区域单色
        'AreaDScatterColor': [
          ['rgba(213, 153, 17, 0)', 'rgba(213, 153, 17, 0.5)'],
          ['rgba(2, 210, 255, 0)', 'rgba(2, 210, 255, 0.5)'],
          ['#fa8d76', '#db4222'],
          ['#af8af3', '#874edc'],
          ['#f5739c', '#f31d53'],
          ['#ffdf91', '#eeb01b'],
          ['#5c84e7', '#144fe5'],
          ['#85f8c0', '#62dc26']
        ], // 区域渐变
        'chartData': {
          'columns': [],
          'unit': '%',
          'min': 60,
          'max': 80,
          'minIndex': 2,
          'maxIndex': 3,
          'unitX': '时间',
          'rows': []
        }
      }
    }
  },
  components: {IntegratedHistogram, ELine, NewGauge, NewProgress, MyProgress, CityEvent, NewPie},
  methods: {
    ShowOtherDetails (val) {
      if (val['流转详情'] && val['流转详情'].rows) {
        this.xqValue = val
        this.showotherDetails = true
      }
      // if (val['工单号']) {
      //   $('#lead-screen').addClass('disShow')
      //   this.axios.get('/leaderview/newDistrict/GetMSSQ21?param=' + val['工单号']).then(res => {
      //     $('#lead-screen').removeClass('disShow')
      //     if (res.success && res.obj) {
      //       val.timeLine = res.obj.obj.data[0].items.rows
      //       this.xqValue = val
      //       this.showotherDetails = true
      //     }
      //   })
      // }
    },
    ShowmydDetails (val) {
      if (val['流转详情'] && val['流转详情'].rows) {
        this.mydValue = val
        this.showmydDetails = true
      }
      // if (val['工单号']) {
      //   $('#lead-screen').addClass('disShow')
      //   this.axios.get('/leaderview/newDistrict/GetMSSQ21?param=' + val['工单号']).then(res => {
      //     $('#lead-screen').removeClass('disShow')
      //     if (res.success && res.obj) {
      //       val.timeLine = res.obj.obj.data[0].items.rows
      //     }
      //   })
      // }
    },
    CloseotherDetails () {
      this.showotherDetails = false
    },
    ShowStreetInfoFun (stree) {
      this.getJieDaoParamData(stree)
    },
    selectTypeData (type) {
      if (this.selectType === type) {
        this.selectType = ''
      } else {
        this.selectType = type
      }
      this.getBodyParamData()
    },
    chengBodyData (data) {
      this.dateValue = data
      this.getBodyParamData()
    },
    getTop10 (arr, key) {
      let newArry = arr.sort((a, b) => {
        return b[key] * 1 - a[key] * 1
      })
      let outarry = newArry.slice(0, 10)
      return outarry
    },
    getBodyParamData () {
      $('#lead-screen').addClass('disShow')
      this.axios.get('/leaderview/newDistrict/GetQJXN?param=' + (this.selectType || '') + '&start_time=' + (this.dateValue[0] || '') + '&end_time=' + (this.dateValue[1] || '')).then(res => {
        if (res.success) {
          $('#lead-screen').removeClass('disShow')
          this.$parent.openisopenShow()
          this.bodyData = res.obj

          this.getBar1.chartData1.columns = (res.obj && res.obj['各委办局办件量统计']) ? res.obj['各委办局办件量统计'].columns : []
          this.getBar1.chartData1.rows = (res.obj && res.obj['各委办局办件量统计']) ? this.getTop10(res.obj['各委办局办件量统计'].rows, '办件量') : []

          this.getBar2.chartData1.rows = (res.obj && res.obj['办理完成率总览']) ? this.getTop10(res.obj['办理完成率总览'].rows, '完成率') : []

          this.getOfficeTrend.chartData.columns = (res.obj && res.obj['办件量走势']) ? res.obj['办件量走势'].columns : []
          this.getOfficeTrend.chartData.rows = (res.obj && res.obj['办件量走势']) ? res.obj['办件量走势'].rows : []
        }
      }, error => {
        console.log(error)
        $('#lead-screen').removeClass('disShow')
      }).catch(err => {
        console.log(err)
        $('#lead-screen').removeClass('disShow')
      })
    },
    getJieDaoParamData (olddata) {
      $('#lead-screen').addClass('disShow')
      this.axios.get('/leaderview/newDistrict/GetCQXN?param=' + (olddata['单位'] || '') + '&start_time=' + (this.dateValue[0] || '') + '&end_time=' + (this.dateValue[1] || '')).then(res => {
        $('#lead-screen').removeClass('disShow')
        if (res.success) {
          this.showStreetInfo = true
          this.modelData = res.obj
          this.modelData.bodyData = olddata
          this.getBJLZS.chartData.columns = (res.obj && res.obj['办件量走势']) ? res.obj['办件量走势'].columns : []
          this.getBJLZS.chartData.rows = (res.obj && res.obj['办件量走势']) ? res.obj['办件量走势'].rows : []

          this.getSJLX1.chartData.columns = (res.obj && res.obj['事件大类_自定义时段']) ? res.obj['事件大类_自定义时段'].columns : []
          this.getSJLX1.chartData.rows = (res.obj && res.obj['事件大类_自定义时段']) ? res.obj['事件大类_自定义时段'].rows : []
        }
      }, error => {
        console.log(error)
        $('#lead-screen').removeClass('disShow')
      }).catch(err => {
        console.log(err)
        $('#lead-screen').removeClass('disShow')
      })
    },
    CloseStreetInfo () {
      this.showStreetInfo = false
    },
    OpenShowyjBox () {
      this.ShowyjBox = !this.ShowyjBox
      if (this.ShowyjBox) {
        this.axios.get('/leaderview/newDistrict/GetMSSQ22?param=' + this.mydData['态度预警数'].name).then(res => {
          if (res.success && res.obj.rows) {
            this.qztsList = res.obj.rows
          }
        })
      }
    },
    ClosemydDetails () {
      this.showmydDetails = false
    },
    OpenShomydjBox (type) {
      if (this.ShowmydType === type) {
        this.ShowmydBox = false
        this.showmydDetails = false
        this.ShowmydType = ''
      } else {
        this.ShowmydBox = true
        this.showmydDetails = false
        this.ShowmydType = type
        $('#lead-screen').addClass('disShow')
        this.axios.get('/leaderview/newDistrict/GetMSSQ22?param=' + type).then(res => {
          $('#lead-screen').removeClass('disShow')
          if (res.success && res.obj.rows) {
            this.mydgyList = res.obj.rows
          }
        }, error => {
          console.log(error)
          $('#lead-screen').removeClass('disShow')
        }).catch(err => {
          console.log(err)
          $('#lead-screen').removeClass('disShow')
        })
      }
    }
  },
  mounted () {
    this.getBodyParamData()
    // 获取群众投诉数据
    this.axios.get('/leaderview/newDistrict/GetMSSQ22').then(res => {
      if (res.success && res.obj) {
        this.mydData = res.obj
      }
    })
  }
}
</script>
<style scoped lang="scss">
#Module3{
    width: 100%;
    height: 443px;
    display: flex;
    .row1{
        width: 45%;
        height: 100%;
        padding: 32px 32px 0 0px;
        .title{
            width: 100%;
            color: #4182ff;
            font-size: 28px;
            padding-left: 15px;
        }
        .percentage{
            width: 100%;
            height: 100%;
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
            align-items: center;
            .percentChild{
              width: 1400px;
              height: 70px;
              display: flex;
              align-items: center;
              padding: 20px;
              position: relative;
              .checked::before{
                content: '';
                height: 12px;
                width: 12px;
                margin-left: -140px;
                position: absolute;
                display: inline-block;
                border-radius: 50%;
                background-color: #FCB83C;
              }
              .name{
                width: 220px;
                height: 100%;
                cursor: pointer;
                color: #16DFF8;
                font-size: 24px;
                display: flex;
                justify-content: center;
                align-items: center;
              }
              .value{
                width: 1140px;
                height: 100%;
                display: flex;
                align-items: center;
              }
              ::deep .ivu-progress .ivu-progress-inner{
                background-color: transparent;
              }
            }
        }
    }
    // .row2{
    //     width: 100%;
    //     height: 520px;
    //     padding: 32px 32px 0 0px;
    //     .title{
    //         width: 100%;
    //         color: #4182ff;
    //         font-size: 28px;
    //         padding-left: 15px;
    //     }
    //     .boxList{
    //         width: 100%;
    //         height: 100%;
    //         display: flex;
    //         flex-wrap: wrap;
    //         .Abox{
    //             width: 20%;
    //             height: 50%;
    //             display: flex;
    //             flex-wrap: wrap;
    //             justify-content: center;
    //             align-items: center;
    //             .name{
    //               width: 180px;
    //               height: 80px;
    //               font-size: 20px;
    //               color: #16DFF8;
    //               display: flex;
    //               align-items: flex-end;
    //             }
    //             .backgrounwt{
    //               height: 120px;
    //               width: 180px;
    //               display: flex;
    //               font-size: 32px;
    //               justify-content: center;
    //               align-items: center;
    //             }
    //         }
    //     }
    // }
    .row3{
        width: 55%;
        height: 100%;
        padding: 32px 32px 0 0px;
        .title{
            width: 100%;
            color: #4182ff;
            font-size: 28px;
            padding-left: 15px;
        }
    }
}
#Module55{
  width: 100%;
  // width: 2913px;
  height: 370px;
  display: flex;
  margin-top: 108px;
  .Mydyj{
    width: 1480px;
    height: 370px;
    background: url('./newBack/2.png');
    background-size: 100% 100%;
    padding-top: 90px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    background-repeat: no-repeat;
    position: relative;
    cursor: pointer;
    .YJbox{
      height: 186px;
      width: 410px;
      display: flex;
      background-image: url('./newBack/7.png');
      background-size: 100% 100%;
      .img{
        height: 100%;
        width: 175px;
        display: flex;
        justify-content: center;
        align-items: center;
        img{
          height: 94px;
          width: 85px;
        }
      }
      .Value{
        .data{
          font-size: 44px;
          color: #FFFFFF;
          height: 55%;
          display: flex;
          justify-content: center;
          align-items: flex-end;
          .unit{
            font-size: 28px;
          }
        }
        .name{
          font-size: 28px;
          height: 45%;
          color: #16DFF8;
          display: flex;
          justify-content: center;
          align-items: flex-start;
        }
      }
    }
  }
  .Mydgy{
    width: 1800px;
    height: 370px;
    margin-left: 32px;
    background: url('./newBack/3.png');
    background-size: 100% 100%;
    background-repeat: no-repeat;
    display: flex;
    padding-top: 80px;
    justify-content: space-around;
    position: relative;
    .Gybox{
      .Value{
        width: 260px;
        background-image: url('./newBack/17.png');
        background-size: 100% 100%;
        height: 160px;
        margin-top: 50px;
        font-weight: 900;
        font-size: 44px;
        cursor: pointer;
        text-align: center;
        color: white;
      }
      .Name{
        width: 260px;
        height: 60px;
        font-size: 28px;
        text-align: center;
        color: #16DFF8;
      }
    }
  }
}
#Module4{
    width: 100%;
    // width: 2913px;
    height: 590px;
    position: relative;
    .whole{
        padding: 32px 32px 0 0px;
        .datepicker{
          position:absolute;
          top: 40px;
          left: 640px;
          z-index: 1;
        }
        .row1{
            height: 580px;
            .tabContent1{
                width:100%;
                height: 480px;
                margin-top: 30px;
                display: flex;
                flex-wrap: wrap;
                overflow: auto;
                align-items: flex-start;
                justify-content: flex-start;
                .streeBox{
                  height: 420px;
                  width: 440px;
                  margin: 7px;
                  padding: 24px;
                  display: inline-block;
                  .titleTop{
                    width: 100%;
                    height: 180px;
                    display: flex;
                    justify-content: space-between;
                    .leftOne{
                      display: flex;
                      flex-wrap: wrap;
                      .streeName{
                        width: 100%;
                        color: #C5EEF3;
                        position: relative;
                        margin-top: -6px;
                        font-size: 30px;
                      }
                      .streeNum{
                        width: 100%;
                        color: #FFFFFF;
                        font-size: 40px;
                      }
                      span{
                        width: 100%;
                        color: #FFFFFF;
                        font-size: 24px;
                      }
                    }
                    .rightpic{
                      display: flex;
                      justify-content: center;
                      align-items: center;
                      img{
                        height: 110px;
                        width: 110px;
                      }
                    }
                  }
                  .newprogress{
                    width: 100%;
                    height: 200px;
                    display: flex;
                    flex-wrap: wrap;
                    color: white;
                    .canvas{
                      width: 100%;
                      height: 40px;
                    }
                    .data1{
                      width: 100%;
                      height: 34px;
                      font-size: 28px;
                      display: flex;
                      justify-content: space-between;
                      div{
                        display: flex;
                        align-items: center;
                      }
                    }
                    .data2{
                      width: 100%;
                      height: 34px;
                      font-size: 38px;
                      display: flex;
                      justify-content: space-between;
                      div{
                        display: flex;
                        align-items: center;
                      }
                    }
                    .allDatatime{
                      background: url('./background/1.png');
                      background-size: 100%;
                    }
                    .datatime{
                      width: 100%;
                      height: 70px;
                      font-size: 26px;
                      display: flex;
                      justify-content: space-between;
                      div{
                        display: flex;
                        align-items: center;
                      }
                    }
                  }
                  .peoplevalue{
                    height: 40px;
                    font-size: 24px;
                    color: #5AE8FA;
                    display: flex;
                    justify-content: space-between;
                  }
                  .peoplename{
                    height: 40px;
                    font-size: 20px;
                    display: flex;
                    justify-content: space-between;
                    color: #C5EEF3;
                  }
                }
            }
            .tabContent2{
                width:100%;
                height: 480px;
                margin-top: 30px;
                display: flex;
                flex-wrap: wrap;
                overflow: auto;
                align-items: flex-start;
                justify-content: flex-start;
                .streeBox{
                  height: 420px;
                  width: 440px;
                  margin: 7px;
                  display: inline-block;
                  padding: 24px;
                  .titleTop{
                    width: 100%;
                    height: 180px;
                    display: flex;
                    justify-content: space-between;
                    .leftOne{
                      display: flex;
                      flex-wrap: wrap;
                      .streeName{
                        width: 100%;
                        color: #C5EEF3;
                        position: relative;
                        margin-top: -6px;
                        font-size: 30px;
                      }
                      .streeNum{
                        width: 100%;
                        color: #FFFFFF;
                        font-size: 40px;
                      }
                      span{
                        width: 100%;
                        color: #FFFFFF;
                        font-size: 24px;
                      }
                    }
                    .rightpic{
                      display: flex;
                      justify-content: center;
                      align-items: center;
                      img{
                        height: 110px;
                        width: 110px;
                      }
                    }
                  }
                  .newprogress{
                    width: 100%;
                    height: 200px;
                    display: flex;
                    flex-wrap: wrap;
                    color: white;
                    .canvas{
                      width: 100%;
                      height: 40px;
                    }
                    .data1{
                      width: 100%;
                      height: 34px;
                      font-size: 28px;
                      display: flex;
                      justify-content: space-between;
                      div{
                        display: flex;
                        align-items: center;
                      }
                    }
                    .data2{
                      width: 100%;
                      height: 34px;
                      font-size: 38px;
                      display: flex;
                      justify-content: space-between;
                      div{
                        display: flex;
                        align-items: center;
                      }
                    }
                    .allDatatime{
                      background: url('./background/1.png');
                      background-size: 100%;
                    }
                    .datatime{
                      width: 100%;
                      height: 70px;
                      font-size: 26px;
                      display: flex;
                      justify-content: space-between;
                      div{
                        display: flex;
                        align-items: center;
                      }
                    }
                  }
                  // .peoplevalue{
                  //   height: 40px;
                  //   font-size: 24px;
                  //   color: #5AE8FA;
                  //   display: flex;
                  //   justify-content: space-between;
                  // }
                  // .peoplename{
                  //   height: 40px;
                  //   font-size: 20px;
                  //   display: flex;
                  //   justify-content: space-between;
                  //   color: #C5EEF3;
                  // }
                }
            }
        }
        .row2{
            height: 580px;
            padding: 32px 0px;
            width: 100%;
            display: flex;
            align-items: center;
            justify-content: space-between;
            .left{
                width: 50%;
                height: 100%;
                margin: 0 16px 0 0;
                .title{
                    width: 100%;
                    border-left:17px solid #81d3f8;
                    color: #4182ff;
                    font-size: 28px;
                    padding-left: 15px;
                    margin-bottom: 40px;
                }
                >div:last-child{
                    width: 100%;
                    height: auto;
                }
            }
            .right{
                width: 50%;
                height: 100%;
                margin: 0 0 0 16px;
                .title{
                    width: 100%;
                    border-left:17px solid #81d3f8;
                    color: #4182ff;
                    font-size: 28px;
                    padding-left: 15px;
                    margin-bottom: 40px;
                }
                >div:last-child{
                    width: 100%;
                    height: auto;
                }
            }
        }
    }
    .part{
      // width: 2830px;
      width: 3280px;
      height: 970px;
      padding: 72px 32px 32px 0px;
      position: absolute;
      z-index: 10;
      top: 124px;
      left: 10px;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: space-between;
      background: url('./background/bg.png');
      background-size: 100%;
      .partTitle{
        height: 140px;
        width: 100%;
        display: flex;
        padding: 6px 30px 0 30px;
        justify-content: space-around;
        margin-top: 24px;
        background: url('./background/矩形01.png');
        background-size: 100% 100%;
        .select{
          height: 100%;
          width: 180px;
          margin-right: 120px;
          display: flex;
          justify-content: center;
          align-items: center;
        }
        .state{
          width: 300px;
          font-size: 30px;
          display: flex;
          flex-wrap: wrap;
          justify-content: center;
          align-items: center;
          div{
            width: 100%;
          }
        }
        .datavalue{
          width: 400px;
          margin-right: 120px;
          height: 100%;
          display: flex;
          flex-wrap: wrap;
          color: white;
          .canvas{
            width: 100%;
            height: 30px;
          }
          .data1{
            width: 100%;
            height: 24px;
            font-size: 30px;
            display: flex;
            justify-content: space-between;
            div{
              display: flex;
              align-items: center;
            }
          }
          .data2{
            width: 100%;
            height: 24px;
            font-size: 30px;
            display: flex;
            justify-content: space-between;
            div{
              display: flex;
              align-items: center;
            }
          }
        }
        .datatime{
          width: 500px;
          height: 100%;
          font-size: 30px;
          margin-right: 120px;
          display: flex;
          flex-wrap: wrap;
          justify-content: center;
          align-items: center;
          div{
            width: 100%;
          }
        }
        .peoplevalue{
          width: 250px;
          height: 100%;
          font-size: 30px;
          margin-right: 120px;
          display: flex;
          flex-wrap: wrap;
          justify-content: center;
          align-items: center;
          div{
            width: 100%;
          }
        }
        .peoplename{
          width: 250px;
          height: 100%;
          font-size: 30px;
          display: flex;
          flex-wrap: wrap;
          justify-content: center;
          align-items: center;
          div{
            width: 100%;
          }
        }
        .upBack{
          font-size: 24px;
          width: 262px !important;
          padding-left: 175px;
          display: inline-block;
          height: 36px;
          margin-left: 10px;
          background: url('./background/编组-16.png');
          background-size: 100% 100%;
        }
        .downBack{
          font-size: 24px;
          width: 262px !important;
          padding-left: 175px;
          display: inline-block;
          margin-left: 10px;
          height: 36px;
          background: url('./background/编组-17.png');
          background-size: 100% 100%;
        }
      }
      .partBody{
        height: 100%;
        width: 50%;
        margin-top: 28px;
        padding: 10px;
        display: flex;
        justify-content: space-between;
        .eventType{
          width: 100%;
          background: url('./background/矩形02.png');
          background-size: 100% 100%;
          height: 100%;
          padding: 20px;
        }
        // .hotevent{
        //   .canvas{
        //     display: flex;
        //     justify-content: space-around;
        //     align-items: center;
        //     height: 100%;
        //     width: 100%;
        //     .events{
        //       width: 180px;
        //       .name{
        //         width: 100%;
        //         text-align: center;
        //         color: #16DFF8;
        //         font-size: 28px;
        //       }
        //       .value{
        //         width: 100%;
        //         height: 130px;
        //         color: white;
        //         font-size: 32px;
        //         background: url('./background/位图.png');
        //         background-size: 100% 100%;
        //         display: flex;
        //         justify-content: center;
        //         align-items: center;
        //         background-size: 100%;
        //       }
        //     }
        //   }
        // }
      }
      .partFoot{
        height: 100%;
        width: 50%;
        padding: 10px;
        margin-top: 28px;
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        .hotevent{
          width: 100%;
          background: url('./background/矩形02.png');
          background-size: 100%;
          height: 50%;
          padding: 20px;
          .canvas{
            display: flex;
            justify-content: space-around;
            align-items: center;
            height: 100%;
            width: 100%;
            .events{
              width: 200px;
              .name{
                width: 100%;
                text-align: center;
                color: #16DFF8;
                font-size: 28px;
              }
              .value{
                width: 100%;
                height: 120px;
                color: white;
                font-size: 32px;
                background: url('./background/位图.png');
                display: flex;
                justify-content: center;
                align-items: center;
                background-size: 100%;
              }
            }
          }
        }
        // >div:nth-child(1){
        //   width: 886px;
        //   background: url('./background/矩形02.png');
        //   background-size: 100%;
        //   padding: 20px;
        // }
        .eventqs{
          width: 100%;
          height: 50%;
          background: url('./background/矩形02.png');
          background-size: 100%;
          padding: 20px;
        }
      }
      .title{
        height: 30px;
        width: 100%;
        color:#C5EEF3;
        display: flex;
        justify-content: space-between;
        .radioBtn::before{
          content: '';
          height: 12px;
          width: 12px;
          margin-right: 10px;
          display: inline-block;
          border-radius: 50%;
          background-color: #FCB83C;
        }
      }
      .canvas{

      }
    }
}

#Module99{
    width: 932px;
    height: 1332px;
    margin-top: 200px;
    margin-right: 32px;
    background: #0B1B2A;
    padding: 0;
    position: absolute;
    top: -70px;
    left: 480px;
    background-image: linear-gradient(45deg, #0A2B3A, #0B1B2A);
    z-index: 10;
    .title{
      color: #5AE8FA;
      font-size: 30px;
      height: 76px;
      width: 100%;
      display: flex;
      justify-content: space-between;
      background-image: linear-gradient(45deg, hsla(187, 94%, 53%, 0.1), hsla(187, 94%, 53%, 0.02),);
      padding: 0 20px;
      align-items: center;
      img{
        width:49px;
        height:49px;
      }
    }
    .content{
        position: relative;
        overflow: hidden;
        .cityEvent{
            width: 100%;
            height: 1230px;
            margin: 20px 0;
            display: flex;
            align-items: center;
            flex-direction: column;
            overflow: auto;
            .item{
              width: 100%;
            }
            .warp{
                overflow: hidden;
                width: 100%;
                height: 100%;
                ul{
                    list-style: none;
                    padding: 0;
                    margin: 0 auto;
                    li{
                    margin-bottom: 28px;
                    // background: #122f61;
                    height: 180px;
                    }
                }
            }
            li{
              padding: 14px 24px 14px 16px;
              .eventBox{
                  width: 100%;
                  height: 200px;
                  padding: 14px 14px 5px 28px;
                  overflow: hidden !important;
                  // margin-bottom: 10px;
                  background: url('./newBack/14.png') no-repeat;
                  background-size: 100% 100%;
                  overflow-y: scroll;
                  >div:nth-child(1){
                      display: flex;
                      align-items: center;
                      justify-content: space-between;
                      >div:nth-child(1) {
                          color: #C5EEF3;
                          font-size: 26px;
                          overflow: hidden;
                          white-space: nowrap;
                          text-overflow: ellipsis;
                          span{
                              width: 12px;
                              height: 12px;
                              display: inline-block;
                              background: #fcb83c;
                              border-radius: 50%;
                              margin-right:12px;
                          }
                      }
                      >div:nth-child(2) {
                          color:#C5EEF3;
                          font-size: 24px;
                          width: 80px;
                          height: 32px;
                          text-align: center;
                          line-height: 32px;
                          background: rgba(22,223,248,0.10);
                          border: 1px solid rgba(22,223,248,0.60);
                          border-radius: 17px;
                          text-align: center;
                          cursor: pointer;
                      }
                  }
                  >div:nth-child(2){
                      width: 100%;
                      height: 75px;
                      padding-left: 24px;
                      color: rgba(197,238,243,0.8);
                      font-size: 24px;
                      margin-top:8px;
                      overflow: hidden;
                      text-overflow: ellipsis;
                      -webkit-line-clamp: 2;
                  }
                  >div:nth-child(3){
                      width: 100%;
                      height: auto;
                      padding-left: 24px;
                      color: rgba(197,238,243,0.8);
                      font-size: 22px;
                      margin-top:12px;
                  }

              }
            }
        }
        #Module99Pop{
            width: 100%;
            height: 1270px;
            background: linear-gradient(180deg,#0a2b3a, #0b1b2a);
            border: 2px solid;
            border-image: linear-gradient(0deg, rgba(13,171,149,0.20), #1ed5c7) 2 2;
            border-radius: 4px;
            position: absolute;
            top: 0;
            right: 0;
            .TimeBox{
              position: relative;
              .line{
                position: absolute;
                left: 9px;
                top: 15px;
                height: 100%;
                border-left: 2px solid #FCB83C;
              }
              .radio{
                position: absolute;
                border: 6px solid #FCB83C;
                height: 20px;
                width: 20px;
                border-radius: 50%;
              }
              .time{
                font-size: 24px;
                padding-left: 28px;
                color: #C5EEF3;
              }
              .data{
                font-size: 24px;
                color: #C5EEF3;
                margin: 12px 12px 12px 30px;
                padding: 18px;
                background-color: transparent;
                background-image: linear-gradient(45deg, rgba(22, 223, 248, 0.04), rgba(22, 223, 248, 0.1), rgba(22, 223, 248, 0.04));
              }
            }
        }
    }
}
#Module66{
    width: 932px;
    height: 1032px;
    margin-top: 200px;
    margin-right: 32px;
    background: #0B1B2A;
    padding: 0;
    position: absolute;
    top: 180px;
    left: -480px;
    background-image: linear-gradient(45deg, #0A2B3A, #0B1B2A);
    z-index: 10;
    .title{
      color: #5AE8FA;
      font-size: 30px;
      height: 76px;
      width: 100%;
      display: flex;
      justify-content: space-between;
      background-image: linear-gradient(45deg, hsla(187, 94%, 53%, 0.1), hsla(187, 94%, 53%, 0.02),);
      padding: 0 20px;
      align-items: center;
      img{
        width:49px;
        height:49px;
        cursor: pointer;
      }
    }
    .content{
        position: relative;
        overflow: hidden;
        .cityEvent{
            width: 100%;
            height: 930px;
            margin: 20px 0;
            display: flex;
            align-items: center;
            flex-direction: column;
            overflow: auto;
            .item{
              width: 100%;
            }
            .warp{
                overflow: hidden;
                width: 100%;
                height: 100%;
                ul{
                    list-style: none;
                    padding: 0;
                    margin: 0 auto;
                    li{
                    margin-bottom: 28px;
                    // background: #122f61;
                    height: 180px;
                    }
                }
            }
            li{
              padding: 14px 24px 14px 16px;
              .eventBox{
                  width: 100%;
                  height: 200px;
                  padding: 14px 14px 5px 28px;
                  overflow: hidden !important;
                  // margin-bottom: 10px;
                  background: url('./newBack/14.png') no-repeat;
                  background-size: 100% 100%;
                  overflow-y: scroll;
                  >div:nth-child(1){
                      display: flex;
                      align-items: center;
                      justify-content: space-between;
                      >div:nth-child(1) {
                          color: #C5EEF3;
                          font-size: 26px;
                          overflow: hidden;
                          white-space: nowrap;
                          text-overflow: ellipsis;
                          span{
                              width: 12px;
                              height: 12px;
                              display: inline-block;
                              background: #fcb83c;
                              border-radius: 50%;
                              margin-right:12px;
                          }
                      }
                      >div:nth-child(2) {
                          color:#C5EEF3;
                          font-size: 24px;
                          width: 80px;
                          height: 32px;
                          text-align: center;
                          line-height: 32px;
                          background: rgba(22,223,248,0.10);
                          border: 1px solid rgba(22,223,248,0.60);
                          border-radius: 17px;
                          text-align: center;
                          cursor: pointer;
                      }
                  }
                  >div:nth-child(2){
                      width: 100%;
                      height: 75px;
                      padding-left: 24px;
                      color: rgba(197,238,243,0.8);
                      font-size: 24px;
                      margin-top:8px;
                      overflow: hidden;
                      text-overflow: ellipsis;
                      -webkit-line-clamp: 2;
                  }
                  >div:nth-child(3){
                      width: 100%;
                      height: auto;
                      padding-left: 24px;
                      color: rgba(197,238,243,0.8);
                      font-size: 22px;
                      margin-top:12px;
                  }

              }
            }
        }
        #Module66Pop{
            width: 100%;
            height: 970px;
            background: linear-gradient(180deg,#0a2b3a, #0b1b2a);
            border: 2px solid;
            border-image: linear-gradient(0deg, rgba(13,171,149,0.20), #1ed5c7) 2 2;
            border-radius: 4px;
            position: absolute;
            top: 0;
            right: 0;
            .TimeBox{
              position: relative;
              .line{
                position: absolute;
                left: 9px;
                top: 15px;
                height: 100%;
                border-left: 2px solid #FCB83C;
              }
              .radio{
                position: absolute;
                border: 6px solid #FCB83C;
                height: 20px;
                width: 20px;
                border-radius: 50%;
              }
              .time{
                font-size: 24px;
                padding-left: 28px;
                color: #C5EEF3;
              }
              .data{
                font-size: 24px;
                color: #C5EEF3;
                margin: 12px 12px 12px 30px;
                padding: 18px;
                background-color: transparent;
                background-image: linear-gradient(45deg, rgba(22, 223, 248, 0.04), rgba(22, 223, 248, 0.1), rgba(22, 223, 248, 0.04));
              }
            }
        }
    }
}
</style>
<style lang="scss">
.EfficiencyPage{
    display: flex;
    flex-wrap: wrap;
    width: 3350px;
    height: 1620px;
    margin-left: 24px;
    .backgroun21{
      background: url('./background/编组21.png');
      background-size: 100%;
      background-repeat: no-repeat;
      padding-top: 80px !important;
    }
    .backgroun22{
      background: url('./background/编组22.png');
      background-size: 100%;
      background-repeat: no-repeat;
      padding-top: 80px !important;
    }
    .backgroun23{
      background: url('./background/编组23.png');
      background-size: 100%;
      height: 100%;
      background-repeat: no-repeat;
      padding-top: 80px !important;
    }
    .backgroun28{
      background: url('./background/编组128.png');
      background-size: 100%;
      background-repeat: no-repeat;
      background-size: 100%;
      padding-top: 80px !important;
    }
    .backgroun29{
      background: url('./background/编组129.png');
      background-size: 100%;
      background-repeat: no-repeat;
      padding-top: 80px !important;
    }
    .backgroun27{
      background: url('./background/编组27.png');
      background-size: 100%;
      background-repeat: no-repeat;
    }
    .backgroun24{
      background: url('./background/编组24.png');
      background-size: 100%;
      background-repeat: no-repeat;
    }
    .backgroun25{
      background: url('./background/编组25.png');
      background-size: 100%;
      background-repeat: no-repeat;
    }
    .backgroun35{
      background: url('./background/编组35.png');
      background-size: 100%;
      background-repeat: no-repeat;
    }
    .backgrounwt{
      background: url('./background/位图.png');
      background-size: 100%;
      background-repeat: no-repeat;
    }
    .diyTabsStyle{
      .ivu-tabs .ivu-tabs-tab:nth-child(2){
        height: 65px;
        width: 300px;
        color: transparent;
        background: url('./background/编组备份3.png');
        background-size: 100%;
      }
      .ivu-tabs .ivu-tabs-tab-active:nth-child(2){
        height: 65px;
        width: 300px;
        color: transparent;
        background: url('./background/编组备份1.png');
        background-size: 100%;
      }
      .ivu-tabs .ivu-tabs-tab:nth-child(3){
        height: 65px;
        color: transparent;
        background: url('./background/编组备份4.png');
        background-size: 100%;
        width: 300px;
      }
      .ivu-tabs .ivu-tabs-tab-active:nth-child(3){
        height: 65px;
        width: 300px;
        color: transparent;
        background: url('./background/编组备份2.png');
        background-size: 100%;
      }
    }
    .ivu-tabs{
        height: 100%;
        >.ivu-tabs-bar{
            height: 65px;
        }
    }
    .ivu-tabs-nav .ivu-tabs-tab{
        color: #fff;
        font-size: 25px;
    }
    .ivu-tabs-nav .ivu-tabs-tab:hover{
        color: #57a3f3;
    }
    .ivu-tabs-nav .ivu-tabs-tab-active{
        color: #2d8cf0;
    }
    // .ivu-tabs .ivu-tabs-content-animated{
    //     overflow-y: hidden;
    // }
    #Module4{
        .select{
            .ivu-select-selection{
                height: 40px;
            }
            .ivu-select-placeholder{
                font-size: 24px;
                height: 40px;
                line-height: 40px;
            }
            .ivu-select-selected-value{
                color: #fff;
                font-size: 24px;
                height: 40px;
                line-height: 40px;
            }
            .ivu-select-arrow{
                font-size: 20px;
                color: #fff;
            }
            .ivu-select-dropdown{
                top: auto !important;
                left: auto !important;
                max-height: 240px !important;
            }
            .ivu-select-item{
                font-size: 20px !important;
            }
        }
    }
    .datepicker{
        .ivu-date-picker-transfer {
            max-height: unset !important;
        }
        .ivu-date-picker-rel{
            width: 365px !important;
        }
        .ivu-date-picker{
            position: relative;
                .ivu-select-dropdown{
                    max-height: unset !important;
                    top: 50px !important;
                    left: 0px !important;
                    font-size: 22px !important;
                    .ivu-picker-panel-body-date{
                    width: 540px !important;
                    }
                    .ivu-date-picker-cells{
                    padding: 10px !important;
                    width: 250px !important;
                    }
                    .ivu-date-picker-header{
                    height: 40px !important;
                    line-height: 43px !important;
                    .ivu-picker-panel-icon-btn{
                        color: #000 !important;
                        font-size: 25px !important;
                    }
                    }
                    .ivu-date-picker-cells-header{
                    span{
                        width: 28px !important;
                    }
                    }
                    .ivu-date-picker-cells-cell{
                    width: 31px !important;
                    em{
                        width: 100% !important;
                    }
                    }
                }
        }
        .ivu-input-wrapper{
            background: #fff;
                .ivu-icon-ios-calendar-outline{
                line-height: 50px !important;
                color: #000 !important;
                font-size: 22px !important;

                }
                .ivu-icon-ios-close-circle{
                line-height: 50px !important;
                color: #000 !important;
                font-size: 22px !important;
                }
                .ivu-input{
                width: 365px !important;
                height: 50px !important;
                color: #000 !important;
                font-size: 25px !important;
                border: 1px solid rgb(247, 234, 234) !important;
                }
        }
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
