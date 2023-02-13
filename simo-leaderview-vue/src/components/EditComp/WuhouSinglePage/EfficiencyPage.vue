<template>
    <div class="EfficiencyPage" style="width:4455px">
        <div id="Module3">
            <div class="row1">
                <div class="percentage backgroun21">
                  <div class="percentChild backgroun35" v-for="(data,index) in bodyData['事件分布总览'].rows" :key="index">
                    <div class="name" @click="getBodyParamData(data['平台'])">
                      {{data['平台']}}
                    </div>
                    <div class="value">
                      <MyProgress :successdata="data['百分比']" :progressType='2' :color='colorArry[index]'/>
                    </div>
                  </div>
                </div>
            </div>
            <div class="row2">
                <div class="boxList backgroun22">
                    <div class="Abox " v-for="(data, index) in bodyData['热门事件'].rows" :key="index">
                        <div class="name">{{data[bodyData['热门事件'].columns[0]]}}</div>
                        <div class="backgrounwt"><span>{{data[bodyData['热门事件'].columns[1]]}}</span></div>
                    </div>
                </div>
            </div>
            <div class="row3">
                <div class="backgroun23">
                    <ELine :item="getOfficeTrend"></ELine>
                </div>
            </div>
        </div>
        <div id="Module4">
            <div class="whole">
                <div class="datepicker">
                    <DatePicker  type="daterange" split-panels placeholder="Select date" style="width: 365px"></DatePicker>
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
                                  <div class="progress">
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
                                    <div class="datatime allDatatime">
                                      <div>平均办件时间</div>
                                      <div style="color:#C5EEF3">0天10小时30分钟</div>
                                    </div>
                                  </div>
                                  <div class="peoplevalue">
                                    <div>6736</div>
                                    <div>26</div>
                                  </div>
                                  <div class="peoplename">
                                    <div>处置人员</div>
                                    <div>人均办件量</div>
                                  </div>
                                </div>
                                <div class="streeBox backgroun25" v-for="(data,index) in bodyData['各街道数量统计'].rows" :key="index" @click="ShowStreetInfo">
                                  <div class="titleTop">
                                    <div class="leftOne">
                                      <div class="streeName">{{data['单位']}}</div>
                                      <div class="streeNum">{{data['办件量']}}</div>
                                      <span>总办件量</span>
                                    </div>
                                    <div class="rightpic">
                                      <img :src="require('./'+data['单位']+'.jpg')" alt="">
                                    </div>
                                  </div>
                                  <div class="progress">
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
                                    <div class="datatime">
                                      <div>平均办件时间</div>
                                      <div style="color:#C5EEF3">0天10小时30分钟</div>
                                    </div>
                                  </div>
                                  <div class="peoplevalue">
                                    <div>6736</div>
                                    <div>26</div>
                                  </div>
                                  <div class="peoplename">
                                    <div>处置人员</div>
                                    <div>人均办件量</div>
                                  </div>
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
                                  <div class="progress">
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
                                    <div class="datatime allDatatime">
                                      <div>平均办件时间</div>
                                      <div style="color:#C5EEF3">0天10小时30分钟</div>
                                    </div>
                                  </div>
                                  <div class="peoplevalue">
                                    <div>6736</div>
                                    <div>26</div>
                                  </div>
                                  <div class="peoplename">
                                    <div>处置人员</div>
                                    <div>人均办件量</div>
                                  </div>
                                </div>
                                <div class="streeBox backgroun25" v-for="(data,index) in bodyData['各委办局数量统计'].rows" :key="index" @click="ShowStreetInfo">
                                  <div class="titleTop">
                                    <div class="leftOne">
                                      <div class="streeName">{{data['单位']}}</div>
                                      <div class="streeNum">{{data['办件量']}}</div>
                                      <span>总办件量</span>
                                    </div>
                                  </div>
                                  <div class="progress">
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
                                    <div class="datatime">
                                      <div>平均办件时间</div>
                                      <div style="color:#C5EEF3">0天10小时30分钟</div>
                                    </div>
                                  </div>
                                  <div class="peoplevalue">
                                    <div>6736</div>
                                    <div>26</div>
                                  </div>
                                  <div class="peoplename">
                                    <div>处置人员</div>
                                    <div>人均办件量</div>
                                  </div>
                                </div>
                            </div>
                        </TabPane>
                    </Tabs>
                </div>
                <div class="row2">
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
                </div>
            </div>
            <!-- 弹窗 -->
            <div class="part" v-show="showStreetInfo">
                <div class="Btn" @click="showStreetInfo = false"
                style="position: absolute;
                      right: 9px;
                      top: 16px;
                      font-size: 30px;
                      cursor: pointer
                      height: 40px;
                      width: 40px;
                ">x</div>
                <div class="partTitle">
                  <div class="select"></div>
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
                    <div style="color: rgb(90, 232, 250);display: flex;align-items: center;">0天10小时30分钟 <div :class="true?'upBack':'downBack'">66%</div></div>
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
                <div class="partBody">
                  <div class="eventType">
                    <div class="title">
                      <div class="radioBtn" style="font-size:22px">事件类型统计</div>
                      <div style="font-size:20px">单位：件</div>
                    </div>
                    <div class="canvas">
                      <NewPie :item="getSJLX1"></NewPie>
                    </div>
                  </div>
                  <div class="eventwcl">
                    <div class="title">
                      <div class="radioBtn" style="font-size:22px">办理完成率</div>
                    </div>
                    <div class="canvas"></div>
                  </div>
                  <div class="hotevent">
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
                  </div>
                </div>
                <div class="partFoot">
                  <div class="eventtj">
                    <div class="title">
                      <div class="radioBtn" style="font-size:22px">办件量统计</div>
                      <div style="font-size:20px">单位：件</div>
                    </div>
                    <div class="canvas"></div>
                  </div>
                  <div class="eventqs">
                    <div class="title">
                      <div class="radioBtn" style="font-size:22px">办件量走势</div>
                      <div style="font-size:20px">单位：件</div>
                    </div>
                    <div class="canvas" style="height:210px">
                        <ELine :item="getBJLZS"></ELine>
                    </div>
                  </div>
                </div>
                <!-- <div class="row1">
                    <div class="select">
                        <Select style="width:254px">
                            <Option value="全部事件">全部事件</Option>
                        </Select>
                    </div>
                    <div class="box1">
                        <div class="left">
                            <div>总办件量</div>
                            <div>1445</div>
                            <div>环比过去30天<Icon type="md-arrow-round-up" /><span>0%</span></div>
                        </div>
                        <div class="right">
                            <div>
                                <div><NewProgress :item="getZBJLCZZ"></NewProgress></div>
                                <span>1346</span>
                            </div>
                            <div>
                                <div><NewProgress :item="getZBJLYWC"></NewProgress></div>
                                <span>1346</span>
                            </div>
                        </div>
                    </div>
                    <div class="box2">
                        <div>平均办件时间</div>
                        <div>0天10小时30分钟</div>
                        <div>环比过去30天<Icon type="md-arrow-round-up" /><span>0%</span></div>
                    </div>
                    <div class="box3">
                        <div>共有处置人员3人</div>
                        <div>人均办件量710件</div>
                    </div>
                    <div class="return" @click="CloseStreetInfo">
                        返回总览
                    </div>
                    <div class="datepicker">
                        <DatePicker  type="daterange" split-panels placeholder="Select date" style="width: 365px"></DatePicker>
                    </div>
                </div>
                <div class="row2">
                    <div class="left">
                        <div class="title">
                            <span>· 事件类型统计</span>
                            <span>单位：件</span>
                        </div>
                        <div class="content">
                            <NewPie :item="getSJLX1"></NewPie>
                            <NewPie :item="getSJLX2"></NewPie>
                        </div>
                    </div>
                    <div class="right">
                        <div class="title">
                            <span>· 办理完成率</span>
                        </div>
                    </div>
                </div>
                <div class="row3">
                    <div class="left">
                        <div class="title">
                            <span>· 事件类型统计</span>
                        </div>
                        <div class="content">
                            <div class="event" v-for="(data,index) in 7" :key="index">
                                <div><span>2333</span>件</div>
                                <div>市容环境-暴露垃圾-暴露垃圾</div>
                                <div><img src="./事件图标.svg" alt=""></div>
                            </div>
                        </div>
                    </div>
                    <div class="right">
                        <div class="title">
                            <span>· 办件量统计</span>
                        </div>
                    </div>
                </div>
                <div class="row4">
                    <div class="title">
                        <span>· 办件量走势</span>
                        <span>单位：件</span>
                    </div>
                    <div>
                        <ELine :item="getBJLZS"></ELine>
                    </div>
                </div> -->
            </div>
        </div>
    </div>
</template>
<script>
import IntegratedHistogram from '../IntegratedHistogram/index.vue'
import ELine from '../ELine/index.vue'
import NewGauge from '../NewGauge/index.vue'
import NewProgress from '../NewProgress/index.vue'
import MyProgress from './progress.vue'
import CityEvent from '../CityEvent/index.vue'
import NewPie from '../NewPie/index.vue'
import modelData from './data'
import bodyData from './data2'
export default {
  data: function () {
    return {
      showStreetInfo: false,
      colorArry: [['#61BEF5', '#61bef533'], ['#F8DE52', '#F8DE5233'], ['#F59B42', '#F59B4233'], ['#DC614F', '#DC614F33']],
      modelData,
      bodyData
    }
  },
  components: {IntegratedHistogram, ELine, NewGauge, NewProgress, MyProgress, CityEvent, NewPie},
  computed: {
    getEventDistribution () {
      return {
        'text': '目标占比图',
        'width': 200,
        'height': 200,
        'imgClass': 'icon-n-percent',
        'chartType': 'NewGauge',
        'ifGradual': 'false',
        'subType': 'progress',
        'bgClr': 'rgba(255, 255, 255, 0.2)',
        'legendY': 86,
        'fontSize': 24,
        'NameShow': true,
        'NameSize': 24,
        'NamelegendY': 43,
        'colorful': 'true',
        'NameColor': '#d0e0e3',
        'ctLegendShow': true,
        'detailColor': '#52a8c0',
        'barClr': '#37a2da',
        'barClrs': ['#ff9900', '#f1c232'],
        'detailwidth': 15,
        'ctLegendSize': '24',
        'ctLegendColor': '#d0e0e3',
        'axisLabelSize': '16',
        'chartData': {
          'name': '12345平台',
          'unit': '%',
          'value': 60
        }
      }
    },
    getOfficeTrend () {
      return {
        'text': '曲线图',
        'imgClass': 'icon-n-line',
        'height': 410,
        'width': 1423,
        'chartType': 'ELine',
        'ifEidetColor': true, // 曲线是否配色
        'ifEidetColor2': true,
        'ifGradual': 'false', // 曲线是否渐变
        'ifAreaGradual': 'true', // 区域是否渐变
        'splitShow': false,
        'ctLegendShow': true,
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
          'columns': (bodyData && bodyData['办件量走势']) ? bodyData['办件量走势'].columns : [],
          'unit': '%',
          'min': 60,
          'max': 80,
          'minIndex': 2,
          'maxIndex': 3,
          'unitX': '时间',
          'rows': (bodyData && bodyData['办件量走势']) ? bodyData['办件量走势'].rows : []
        }
      }
    },
    getProgress () {
      return {
        'text': '进度条',
        'imgClass': 'icon-n-progress',
        'chartType': 'NewProgress',
        'width': 290,
        'height': 25,
        'bgClr': 'rgba(6, 46, 77, 0.37)',
        'barClr': '#fff',
        'fontSize': 14,
        'barClrs': ['#1068ed', '#69b1f3'],
        'clr': '#fff',
        'colorful': 'false',
        'ctLegendShow': 'true',
        'chartData': {
          'name': '繁忙度',
          'unit': '%',
          'value': 60
        },
        'proHeight': 16,
        'radius': 8
      }
    },
    getBar1 () {
      return {
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
        'tooltipfontSize1': 14,
        'splitColor1': '#fff',
        'splitSize1': 1,
        'rotate1': 0,
        'chartData1': {
          'columns': (bodyData && bodyData['各委办局办件量统计']) ? bodyData['各委办局办件量统计'].columns : [],
          'unit': '次',
          'rows': (bodyData && bodyData['各委办局办件量统计']) ? bodyData['各委办局办件量统计'].rows : []
        }
      }
    },
    getBar2 () {
      return {
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
        'tooltipfontSize1': 14,
        'splitColor1': '#fff',
        'splitSize1': 1,
        'rotate1': 0,
        'chartData1': {
          'columns': ['项目', '完成率'],
          'unit': '%',
          'rows': (bodyData && bodyData['办理完成率总览']) ? bodyData['办理完成率总览'].rows : []
        }
      }
    },
    getZBJLCZZ () {
      return {
        'text': '进度条',
        'imgClass': 'icon-n-progress',
        'chartType': 'NewProgress',
        'width': 350,
        'height': 30,
        'bgClr': 'rgba(6, 46, 77, 0.37)',
        'barClr': '#fff',
        'fontSize': 24,
        'barClrs': ['#1068ed', '#69b1f3'],
        'clr': '#fff',
        'colorful': 'false',
        'ctLegendShow': 'true',
        'chartData': {
          'name': '繁忙度',
          'unit': '%',
          'value': 60
        },
        'proHeight': 20,
        'radius': 8
      }
    },
    getZBJLYWC () {
      return {
        'text': '进度条',
        'imgClass': 'icon-n-progress',
        'chartType': 'NewProgress',
        'width': 350,
        'height': 30,
        'bgClr': 'rgba(6, 46, 77, 0.37)',
        'barClr': '#fff',
        'fontSize': 24,
        'barClrs': ['#1068ed', '#69b1f3'],
        'clr': '#fff',
        'colorful': 'false',
        'ctLegendShow': 'true',
        'chartData': {
          'name': '繁忙度',
          'unit': '%',
          'value': 60
        },
        'proHeight': 20,
        'radius': 8
      }
    },
    getSJLX1 () {
      return {
        'text': '环形图',
        'width': 700,
        'height': 250,
        'imgClass': 'icon-n-ring',
        'chartType': 'NewPie',
        'ifGradual': 'false',
        'pieType': '环形图',
        'ctLegendSize': '16',
        'ctLegendColor': '#666f8b',
        'axisLabelSize': '16',
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
        'tooltipfontSize': 14,
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
          'columns': (modelData && modelData['事件大类_自定义时段']) ? modelData['事件大类_自定义时段'].columns : [],
          'unit': '次',
          'rows': (modelData && modelData['事件大类_自定义时段']) ? modelData['事件大类_自定义时段'].rows : []
        }
      }
    },
    getBJLZS () {
      return {
        'text': '曲线图',
        'imgClass': 'icon-n-line',
        'height': 210,
        'width': 1750,
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
        'gridLeft': 2,
        'gridRight': 2,
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
          'columns': (modelData && modelData['办件量走势']) ? modelData['办件量走势'].columns : [],
          'unit': '%',
          'min': 60,
          'max': 80,
          'minIndex': 2,
          'maxIndex': 3,
          'unitX': '时间',
          'rows': (modelData && modelData['办件量走势']) ? modelData['办件量走势'].rows : []
        }
      }
    }
  },
  watch: {
    'getBar2' () {
      console.log(this.getBar2)
    }
  },
  methods: {
    onMouseWheel (e, refName) {
      let eventDelta = -e.wheelDelta || -e.deltaY * 40
      let box = this.$refs[refName]
      box.scrollLeft = box.scrollLeft + eventDelta / 2
    },
    ShowStreetInfo () {
      this.getJieDaoParamData()
    },
    getBodyParamData (type) {
      $('#lead-screen').addClass('disShow')
      this.axios.get('/leaderview/newDistrict/GetQJXN?param=' + type || '').then(res => {
        if (res.success) {
          $('#lead-screen').removeClass('disShow')
          this.$parent.openisopenShow()
          this.bodyData = res.obj
          this.getBar2.chartData1.rows = (res.obj && res.obj['办理完成率总览']) ? res.obj['办理完成率总览'].rows : []

          this.getBar1.chartData1.columns = (res.obj && res.obj['各委办局办件量统计']) ? res.obj['各委办局办件量统计'].columns : []
          this.getBar1.chartData1.rows = (res.obj && res.obj['各委办局办件量统计']) ? res.obj['各委办局办件量统计'].rows : []

          this.getOfficeTrend.chartData.columns = (res.obj && res.obj['办件量走势']) ? res.obj['办件量走势'].columns : []
          this.getOfficeTrend.chartData.rows = (res.obj && res.obj['办件量走势']) ? res.obj['办件量走势'].rows : []
        }
      })
    },
    getJieDaoParamData (type) {
      $('#lead-screen').addClass('disShow')
      this.axios.get('/leaderview/newDistrict/GetCQXN').then(res => {
        $('#lead-screen').removeClass('disShow')
        if (res.success) {
          this.showStreetInfo = true
          this.modelData = res.obj
          this.getBJLZS.chartData.columns = (res.obj && res.obj['办件量走势']) ? res.obj['办件量走势'].columns : []
          this.getBJLZS.chartData.rows = (res.obj && res.obj['办件量走势']) ? res.obj['办件量走势'].rows : []

          this.getSJLX1.chartData.columns = (res.obj && res.obj['事件大类_自定义时段']) ? res.obj['事件大类_自定义时段'].columns : []
          this.getSJLX1.chartData.rows = (res.obj && res.obj['事件大类_自定义时段']) ? res.obj['事件大类_自定义时段'].rows : []
        }
      })
    },
    CloseStreetInfo () {
      this.showStreetInfo = false
    }
  },
  mounted () {
    console.log(modelData)
    this.getBodyParamData('')
  }
}
</script>
<style scoped lang="scss">
#Module3{
        width: 1513px;
        height: 1620px;
        .row1{
            width: 100%;
            height: 520px;
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
        .row2{
            width: 100%;
            height: 520px;
            padding: 32px 32px 0 0px;
            .title{
                width: 100%;
                color: #4182ff;
                font-size: 28px;
                padding-left: 15px;
            }
            .boxList{
                width: 100%;
                height: 100%;
                display: flex;
                flex-wrap: wrap;
                .Abox{
                    width: 20%;
                    height: 50%;
                    display: flex;
                    flex-wrap: wrap;
                    justify-content: center;
                    align-items: center;
                    .name{
                      width: 180px;
                      height: 80px;
                      font-size: 20px;
                      color: #16DFF8;
                      display: flex;
                      align-items: flex-end;
                    }
                    .backgrounwt{
                      height: 120px;
                      width: 180px;
                      display: flex;
                      font-size: 32px;
                      justify-content: center;
                      align-items: center;
                    }
                }
            }
        }
        .row3{
            width: 100%;
            height: 548px;
            padding: 32px 32px 0 0px;
            margin-bottom: 32px;
            .title{
                width: 100%;
                color: #4182ff;
                font-size: 28px;
                padding-left: 15px;
            }
        }
    }
    #Module4{
        width: 2913px;
        height: 1620px;
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
                height: 1008px;
                .tabContent1{
                    width:2850px;
                    height:890px;
                    display: flex;
                    flex-wrap: wrap;
                    overflow: auto;
                    align-items: flex-start;
                    justify-content: flex-start;
                    .streeBox{
                      height: 420px;
                      width: 444px;
                      margin: 14px;
                      padding: 24px;
                      .titleTop{
                        width: 100%;
                        height: 140px;
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
                            font-size: 28px;
                          }
                          .streeNum{
                            width: 100%;
                            color: #FFFFFF;
                            font-size: 40px;
                          }
                          span{
                            width: 100%;
                            color: #FFFFFF;
                            font-size: 20px;
                          }
                        }
                        .rightpic{
                          display: flex;
                          justify-content: center;
                          align-items: center;
                          img{
                            height: 100px;
                            width: 100px;
                          }
                        }
                      }
                      .progress{
                        width: 100%;
                        height: 160px;
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
                          font-size: 20px;
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
                          font-size: 24px;
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
                          height: 60px;
                          font-size: 22px;
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
                    width:2850px;
                    height:890px;
                    display: flex;
                    flex-wrap: wrap;
                    overflow: auto;
                    align-items: flex-start;
                    justify-content: flex-start;
                    .streeBox{
                      height: 420px;
                      width: 444px;
                      margin: 14px;
                      padding: 24px;
                      .titleTop{
                        width: 100%;
                        height: 140px;
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
                            font-size: 28px;
                          }
                          .streeNum{
                            width: 100%;
                            color: #FFFFFF;
                            font-size: 40px;
                          }
                          span{
                            width: 100%;
                            color: #FFFFFF;
                            font-size: 20px;
                          }
                        }
                        .rightpic{
                          display: flex;
                          justify-content: center;
                          align-items: center;
                          img{
                            height: 100px;
                            width: 100px;
                          }
                        }
                      }
                      .progress{
                        width: 100%;
                        height: 160px;
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
                          font-size: 20px;
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
                          font-size: 24px;
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
                          height: 60px;
                          font-size: 22px;
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
          width: 2810px;
          height: 864px;
          padding: 72px 32px 32px 0px;
          position: absolute;
          top: 124px;
          left: 30px;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: space-between;
          background: url('./background/bg.png');
          background-size: 100%;
          .partTitle{
            height: 100px;
            width: 100%;
            display: flex;
            padding: 6px;
            margin-top: 24px;
            background: url('./background/矩形01.png');
            background-size: 100%;
            .select{
              height: 100%;
              width: 180px;
              margin-right: 120px;
            }
            .state{
              width: 300px;
              font-size: 22px;
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
                font-size: 20px;
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
                font-size: 24px;
                display: flex;
                justify-content: space-between;
                div{
                  display: flex;
                  align-items: center;
                }
              }
            }
            .datatime{
              width: 400px;
              height: 100%;
              font-size: 22px;
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
              font-size: 22px;
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
              font-size: 22px;
              display: flex;
              flex-wrap: wrap;
              justify-content: center;
              align-items: center;
              div{
                width: 100%;
              }
            }
            .upBack{
              font-size: 14px;
              width: 162px !important;
              padding-left: 115px;
              display: inline-block;
              height: 24px;
              margin-left: 10px;
              background: url('./background/编组-16.png');
              background-size: 100%;
            }
            .downBack{
              font-size: 14px;
              padding-left: 115px;
              width: 162px !important;
              display: inline-block;
              margin-left: 10px;
              height: 24px;
              background: url('./background/编组-17.png');
              background-size: 100%;
            }
          }
          .partBody{
            height: 286px;
            width: 100%;
            margin-top: 28px;
            display: flex;
            justify-content: space-between;
            >div{
              width: 886px;
              background: url('./background/矩形02.png');
              background-size: 100%;
              height: 100%;
              padding: 20px;
            }
            .hotevent{
              .canvas{
                display: flex;
                justify-content: space-around;
                align-items: center;
                height: 100%;
                width: 100%;
                .events{
                  width: 130px;
                  .name{
                    width: 100%;
                    text-align: center;
                    color: #16DFF8;
                    font-size: 20px;
                  }
                  .value{
                    width: 100%;
                    height: 88px;
                    color: white;
                    font-size: 24px;
                    background: url('./background/位图.png');
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    background-size: 100%;
                  }
                }
              }
            }
          }
          .partFoot{
            height: 286px;
            width: 100%;
            margin-top: 28px;
            display: flex;
            justify-content: space-between;
            >div:nth-child(1){
              width: 886px;
              background: url('./background/矩形02.png');
              background-size: 100%;
              padding: 20px;
            }
            >div:nth-child(2){
              width: 1813px;
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
</style>
<style lang="scss">
.EfficiencyPage{
    display: flex;
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
      background: url('./background/编组28.png');
      background-size: 100%;
      background-repeat: no-repeat;
      background-size: 100%;
      padding-top: 80px !important;
    }
    .backgroun29{
      background: url('./background/编组29.png');
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
                height: 60px;
            }
            .ivu-select-placeholder{
                font-size: 48px;
                height: 60px;
                line-height: 60px;
            }
            .ivu-select-selected-value{
                color: #fff;
                font-size: 48px;
                height: 60px;
                line-height: 60px;
            }
            .ivu-select-arrow{
                font-size: 40px;
                color: #fff;
            }
            .ivu-select-dropdown{
                top: auto !important;
                left: auto !important;
                max-height: 240px !important;
            }
            .ivu-select-item{
                font-size: 40px !important;
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
