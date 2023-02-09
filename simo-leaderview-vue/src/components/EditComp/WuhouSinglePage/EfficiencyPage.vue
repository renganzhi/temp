<template>
    <div class="EfficiencyPage">
        <div id="Module3">
            <div class="row1">
                <div class="title">事件分布总览</div>
                <div class="percentage">
                    <div v-for="(data, index) in 4" :key="index">
                        <NewGauge :item="getEventDistribution"></NewGauge>
                    </div>
                </div>
            </div>
            <div class="row2">
                <div class="title">热门事件</div>
                <div class="boxList">
                    <div class="Abox" v-for="(data, index) in 10" :key="index">
                        <div><span>9179</span><span>件</span></div>
                        <div>市容环境-暴露垃圾-暴露垃圾</div>
                        <img src="./事件图标.svg" alt="">
                    </div>
                </div>
            </div>
            <div class="row3">
                <div class="title">办件量走势</div>
                <div>
                    <ELine :item="getOfficeTrend"></ELine>
                </div>
            </div>
        </div>
        <div id="Module4">
            <div class="whole" v-show="!showStreetInfo">
                <div class="datepicker">
                    <DatePicker  type="daterange" split-panels placeholder="Select date" style="width: 365px"></DatePicker>
                </div>
                <div class="row1">
                    <Tabs value="name1">
                        <TabPane label="街道总览" name="name1">
                            <div class="tabContent1">
                                <div class="left">
                                    <div>
                                        <div>
                                            <div>
                                                <div>街道总办件量</div>
                                                <div>4614</div>
                                            </div>
                                            <img src="" alt="">
                                        </div>
                                        <div class="progressBar">
                                            <div><NewProgress :item="getProgress"></NewProgress></div>
                                            <span>22</span>
                                        </div>
                                        <div class="progressBar">
                                            <div><NewProgress :item="getProgress"></NewProgress></div>
                                            <span>3155</span>
                                        </div>
                                    </div>
                                    <div>
                                        <div>平均办件时间</div>
                                        <div>0天10小时30分钟</div>
                                    </div>
                                    <div>
                                        <div>
                                            <div>处置人员</div>
                                            <div>3<span>人</span></div>
                                        </div>
                                        <div>
                                            <div>人均办件量</div>
                                            <div>710<span>件</span></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="right" ref="streetRight" @mousewheel="onMouseWheel($event, 'streetRight')">
                                    <div class="card" v-for="(data,index) in 10" :key="index" @click="ShowStreetInfo">
                                        <img src="" alt="">
                                        <div>
                                            <div>簇锦街道</div>
                                            <div>总办件量</div>
                                            <div>1445</div>
                                            <div class="progressBar">
                                                <div><NewProgress :item="getProgress"></NewProgress></div>
                                                <span>22</span>
                                            </div>
                                            <div class="progressBar">
                                                <div><NewProgress :item="getProgress"></NewProgress></div>
                                                <span>1346</span>
                                            </div>
                                        </div>
                                        <div>
                                            <div>
                                                <div>处置人员</div>
                                                <div>3人</div>
                                            </div>
                                            <div>
                                                <div>人均办件量</div>
                                                <div>710件</div>
                                            </div>
                                        </div>
                                        <div>平均办件时间</div>
                                        <div>0天10小时30分钟</div>
                                    </div>
                                </div>
                            </div>
                        </TabPane>
                        <TabPane label="委办局总览" name="name3">
                            <div class="tabContent2">
                                <div class="left">
                                    <div>
                                        <div>
                                            <div>
                                                <div>委办局总办件量</div>
                                                <div>4614</div>
                                            </div>
                                            <img src="" alt="">
                                        </div>
                                        <div class="progressBar">
                                            <div><NewProgress :item="getProgress"></NewProgress></div>
                                            <span>22</span>
                                        </div>
                                        <div class="progressBar">
                                            <div><NewProgress :item="getProgress"></NewProgress></div>
                                            <span>3155</span>
                                        </div>
                                    </div>
                                    <div>
                                        <div>平均办件时间</div>
                                        <div>0天10小时30分钟</div>
                                    </div>
                                    <div>
                                        <div>
                                            <div>处置人员</div>
                                            <div>3<span>人</span></div>
                                        </div>
                                        <div>
                                            <div>人均办件量</div>
                                            <div>710<span>件</span></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="right" ref="wbjRight" @mousewheel="onMouseWheel($event, 'wbjRight')">
                                    <div class="card" v-for="(data,index) in 10" :key="index">
                                        <div>
                                            <div>区委社治委</div>
                                            <div>总办件量</div>
                                            <div>1445</div>
                                            <div class="progressBar">
                                                <div><NewProgress :item="getProgress"></NewProgress></div>
                                                <span>22</span>
                                            </div>
                                            <div class="progressBar">
                                                <div><NewProgress :item="getProgress"></NewProgress></div>
                                                <span>1346</span>
                                            </div>
                                        </div>
                                        <div>
                                            <div>
                                                <div>处置人员</div>
                                                <div>3人</div>
                                            </div>
                                            <div>
                                                <div>人均办件量</div>
                                                <div>710件</div>
                                            </div>
                                        </div>
                                        <div>平均办件时间</div>
                                        <div>0天10小时30分钟</div>
                                    </div>
                                </div>
                            </div>
                        </TabPane>
                    </Tabs>
                </div>
                <div class="row2">
                    <div class="left">
                        <div class="title">办件量 & 平均办件时间总览</div>
                        <div>
                            <IntegratedHistogram :item="getBar1"></IntegratedHistogram>
                        </div>
                    </div>
                    <div class="right">
                        <div class="title">办理完成率总览</div>
                        <div>
                            <IntegratedHistogram :item="getBar2"></IntegratedHistogram>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 弹窗 -->
            <div class="part" v-show="showStreetInfo">
                <div class="row1">
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
import CityEvent from '../CityEvent/index.vue'
import NewPie from '../NewPie/index.vue'
export default {
  data: function () {
    return {
      showStreetInfo: false
    }
  },
  components: {IntegratedHistogram, ELine, NewGauge, NewProgress, CityEvent, NewPie},
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
        'height': 350,
        'width': 1596,
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
        'gridTop': 10,
        'gridBotton': 15,
        'gridLeft': 10,
        'gridRight': 10,
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
          'columns': ['日期', 'CPU核心利用率', 'CPU平均利用率'],
          'unit': '%',
          'min': 60,
          'max': 80,
          'minIndex': 2,
          'maxIndex': 3,
          'unitX': '时间',
          'rows': [{
            '日期': '2020-01-01',
            'CPU核心利用率': 15,
            'CPU平均利用率': 15
          },
          {
            '日期': '2020-01-02',
            'CPU核心利用率': 80,
            'CPU平均利用率': 50
          },
          {
            '日期': '2020-01-03',
            'CPU核心利用率': 40,
            'CPU平均利用率': 6
          },
          {
            '日期': '2020-01-05',
            'CPU核心利用率': 45,
            'CPU平均利用率': 70
          },
          {
            '日期': '2020-01-06',
            'CPU核心利用率': 10,
            'CPU平均利用率': 40
          },
          {
            '日期': '2020-01-07',
            'CPU核心利用率': 95,
            'CPU平均利用率': 50
          }
          ]
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
          'columns': ['告警级别', '数量'],
          'unit': '次',
          'rows': [{
            '告警级别': '致命',
            '数量': 233
          },
          {
            '告警级别': '严重',
            '数量': 123
          },
          {
            '告警级别': '警告',
            '数量': 23
          },
          {
            '告警级别': '一般',
            '数量': 155
          },
          {
            '告警级别': '次要',
            '数量': 103
          },
          {
            '告警级别': '通知',
            '数量': 123
          }
          ]
        }
      }
    },
    getBar2 () {
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
          'columns': ['告警级别', '数量'],
          'unit': '次',
          'rows': [{
            '告警级别': '致命',
            '数量': 233
          },
          {
            '告警级别': '严重',
            '数量': 123
          },
          {
            '告警级别': '警告',
            '数量': 23
          },
          {
            '告警级别': '一般',
            '数量': 155
          },
          {
            '告警级别': '次要',
            '数量': 103
          },
          {
            '告警级别': '通知',
            '数量': 123
          }
          ]
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
        'height': 300,
        'imgClass': 'icon-n-ring',
        'chartType': 'NewPie',
        'ifGradual': 'false',
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
          'columns': [
            '告警级别',
            '数量'
          ],
          'unit': '次',
          'rows': [
            {
              '告警级别': '致命',
              '数量': 233
            },
            {
              '告警级别': '严重',
              '数量': 123
            },
            {
              '告警级别': '警告',
              '数量': 23
            },
            {
              '告警级别': '一般',
              '数量': 155
            },
            {
              '告警级别': '次要',
              '数量': 103
            },
            {
              '告警级别': '通知',
              '数量': 123
            }
          ]
        }
      }
    },
    getSJLX2 () {
      return {
        'text': '环形图',
        'width': 700,
        'height': 300,
        'imgClass': 'icon-n-ring',
        'chartType': 'NewPie',
        'ifGradual': 'false',
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
          'columns': [
            '告警级别',
            '数量'
          ],
          'unit': '次',
          'rows': [
            {
              '告警级别': '致命',
              '数量': 233
            },
            {
              '告警级别': '严重',
              '数量': 123
            },
            {
              '告警级别': '警告',
              '数量': 23
            },
            {
              '告警级别': '一般',
              '数量': 155
            },
            {
              '告警级别': '次要',
              '数量': 103
            },
            {
              '告警级别': '通知',
              '数量': 123
            }
          ]
        }
      }
    },
    getBJLZS () {
      return {
        'text': '曲线图',
        'imgClass': 'icon-n-line',
        'height': 320,
        'width': 2800,
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
        'gridTop': 10,
        'gridBotton': 15,
        'gridLeft': 10,
        'gridRight': 10,
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
          'columns': ['日期', 'CPU核心利用率', 'CPU平均利用率'],
          'unit': '%',
          'min': 60,
          'max': 80,
          'minIndex': 2,
          'maxIndex': 3,
          'unitX': '时间',
          'rows': [{
            '日期': '2020-01-01',
            'CPU核心利用率': 15,
            'CPU平均利用率': 15
          },
          {
            '日期': '2020-01-02',
            'CPU核心利用率': 80,
            'CPU平均利用率': 50
          },
          {
            '日期': '2020-01-03',
            'CPU核心利用率': 40,
            'CPU平均利用率': 6
          },
          {
            '日期': '2020-01-05',
            'CPU核心利用率': 45,
            'CPU平均利用率': 70
          },
          {
            '日期': '2020-01-06',
            'CPU核心利用率': 10,
            'CPU平均利用率': 40
          },
          {
            '日期': '2020-01-07',
            'CPU核心利用率': 95,
            'CPU平均利用率': 50
          }
          ]
        }
      }
    }
  },
  watch: {

  },
  methods: {
    onMouseWheel (e, refName) {
      let eventDelta = -e.wheelDelta || -e.deltaY * 40
      let box = this.$refs[refName]
      box.scrollLeft = box.scrollLeft + eventDelta / 2
    },
    ShowStreetInfo () {
      this.showStreetInfo = true
    },
    CloseStreetInfo () {
      this.showStreetInfo = false
    }
  },
  mounted () {

  }
}
</script>
<style scoped lang="scss">
#Module3{
        width: 1597px;
        height: 1620px;
        border-right: 1px solid #797979;
        .row1{
            width: 100%;
            height: 520px;
            padding: 30px 40px;
            border-bottom: 1px solid #797979;
            .title{
                width: 100%;
                border-left:17px solid #81d3f8;
                color: #4182ff;
                font-size: 28px;
                padding-left: 15px;
            }
            .percentage{
                width: 100%;
                margin-top: 100px;
                display: flex;
                justify-content: space-around;
                align-items: center;
            }
        }
        .row2{
            width: 100%;
            height: 594px;
            padding: 30px 40px;
            border-bottom: 1px solid #797979;
            .title{
                width: 100%;
                border-left:17px solid #81d3f8;
                color: #4182ff;
                font-size: 28px;
                padding-left: 15px;
            }
            .boxList{
                width: 100%;
                .Abox{
                    width: 100px;
                    height: 198px;
                    margin:20px 100px;
                    display: inline-block;
                    >div:nth-child(1) {
                        text-align: center;
                        >span:first-child{
                            color: #4182ff;
                            font-size: 28px;
                        }
                        >span:last-child{
                            color: #fff;
                            font-size: 18px;
                        }
                    }
                    >div:nth-child(2) {
                        color: #fff;
                        font-size: 18px;
                        width: 100%;
                        white-space: pre-wrap;
                    }
                    >img{
                        width: 100%;
                        height: 80px;
                    }
                }
            }
        }
        .row3{
            width: 100%;
            padding: 30px 40px;
            .title{
                width: 100%;
                border-left:17px solid #81d3f8;
                color: #4182ff;
                font-size: 28px;
                padding-left: 15px;
            }
        }
    }
    #Module4{
        width: 3192px;
        height: 1620px;
        border-right: 1px solid #797979;
        position: relative;
        .whole{
            .datepicker{
                position:absolute;
                top:33px;
                right: 20px;
                z-index: 1;
            }
            .row1{
                border-bottom: 1px solid #797979;
                height: 924px;
                padding: 15px 20px;
                .tabContent1{
                    width:3159px;
                    height:771px;
                    background: #1E98D7;
                    // padding: 20px;
                    display: flex;
                    align-items: center;
                    justify-content: space-around;
                    .left{
                        width:336px;
                        height:698px;
                        background: linear-gradient(to bottom, #29509b, #4282fc);
                        padding: 27px 10px;
                        >div:nth-child(1) {
                            padding: 30px 15px;
                            width: 100%;
                            height: auto;
                            border-bottom: 1px solid #797979;
                            >div:nth-child(1) {
                                display: flex;
                                align-items: center;
                                justify-content: space-between;
                                margin-bottom: 5px;
                                >div{
                                    >div:first-child{
                                        color:#fff;
                                        font-size: 18px;
                                    }
                                    >div:last-child{
                                        color:#fff;
                                        font-size: 32px;
                                        font-weight: bold;
                                    }
                                }
                                >img{
                                    width:83px;
                                    height:126px;
                                }
                            }
                            .progressBar{
                                width: 100%;
                                display: flex;
                                align-items:flex-end;
                                justify-content: space-between;
                                margin-bottom: 10px;
                                >div{
                                    width: inherit;
                                    margin-right:4px;
                                }
                                >span{
                                    color: #fff;
                                    font-size: 14px;
                                }
                            }
                        }
                        >div:nth-child(2) {
                            padding: 35px 15px;
                            width: 100%;
                            height: auto;
                            border-bottom: 1px solid #797979;
                            >div:first-child{
                                text-align: left;
                                color: #fff;
                                font-size: 18px;
                                margin-bottom: 30px;
                            }
                            >div:last-child{
                                text-align: center;
                                color: #fff;
                                font-size: 18px;
                            }
                        }
                        >div:nth-child(3) {
                            padding: 30px 15px;
                            width: 100%;
                            height: auto;
                            display: flex;
                            align-items: center;
                            justify-content: space-between;
                            >div{
                                width: 50%;
                                color:#fff;
                                font-size: 18px;
                                >div:first-child{
                                    margin-bottom: 30px;
                                }
                                span{
                                    color:#f2f2f2;
                                    font-size: 12px;
                                }
                            }
                        }
                    }
                    .right{
                        width:2560px;
                        height:698px;
                        overflow-x:scroll;
                        white-space: nowrap;
                        .card{
                            width: 336px;
                            height: 100%;
                            display: inline-block;
                            position: relative;
                            margin-right: 30px;
                            // padding: 27px 10px;
                            >img{
                                width: 115px;
                                height: 115px;
                                position: absolute;
                                top: 0;
                                right: 0;
                            }
                            >div:nth-child(2) {
                                padding: 57px 25px 30px 25px;
                                width: 100%;
                                height: auto;
                                background: url('./u524.png') no-repeat;
                                background-size: 100% 100%;
                                >div{
                                    color:#fff;
                                    font-size: 18px;
                                    margin-bottom: 10px;
                                }
                                >div:nth-child(3){
                                    color:#fff;
                                    font-size: 32px;
                                    font-weight: bold;
                                    margin-bottom: 0;
                                }
                                .progressBar{
                                    width: 100%;
                                    display: flex;
                                    align-items:flex-end;
                                    justify-content: space-between;
                                    margin-bottom: 10px;
                                    >div{
                                        width: inherit;
                                        margin-right:4px;
                                    }
                                    >span{
                                        color: #fff;
                                        font-size: 14px;
                                    }
                                }
                            }
                            >div:nth-child(3) {
                                padding: 30px 25px;
                                width: 100%;
                                height: auto;
                                display: flex;
                                align-items: center;
                                justify-content: space-between;
                                border-bottom: 1px solid #797979;
                                >div{
                                    width: 50%;
                                    color:#000;
                                    font-size: 18px;
                                    >div:first-child{
                                        margin-bottom: 30px;
                                    }
                                    span{
                                        color:#797979;
                                        font-size: 12px;
                                    }
                                }
                            }
                            >div:nth-child(4) {
                                padding: 35px 25px;
                                width: 100%;
                                height: auto;
                                text-align: left;
                                color: #000;
                                font-size: 18px;
                            }
                            >div:nth-child(5) {
                                padding: 35px 15px;
                                width: 100%;
                                height: auto;
                                text-align: center;
                                color: #000;
                                font-size: 18px;
                            }
                        }
                    }
                }
                .tabContent2{
                    width:3159px;
                    height:771px;
                    background: #1E98D7;
                    // padding: 20px;
                    display: flex;
                    align-items: center;
                    justify-content: space-around;
                    .left{
                        width:336px;
                        height:698px;
                        background: linear-gradient(to bottom, #29509b, #4282fc);
                        padding: 27px 10px;
                        >div:nth-child(1) {
                            padding: 30px 15px;
                            width: 100%;
                            height: auto;
                            border-bottom: 1px solid #797979;
                            >div:nth-child(1) {
                                display: flex;
                                align-items: center;
                                justify-content: space-between;
                                margin-bottom: 5px;
                                >div{
                                    >div:first-child{
                                        color:#fff;
                                        font-size: 18px;
                                    }
                                    >div:last-child{
                                        color:#fff;
                                        font-size: 32px;
                                        font-weight: bold;
                                    }
                                }
                                >img{
                                    width:83px;
                                    height:126px;
                                }
                            }
                            .progressBar{
                                width: 100%;
                                display: flex;
                                align-items:flex-end;
                                justify-content: space-between;
                                margin-bottom: 10px;
                                >div{
                                    width: inherit;
                                    margin-right:4px;
                                }
                                >span{
                                    color: #fff;
                                    font-size: 14px;
                                }
                            }
                        }
                        >div:nth-child(2) {
                            padding: 35px 15px;
                            width: 100%;
                            height: auto;
                            border-bottom: 1px solid #797979;
                            >div:first-child{
                                text-align: left;
                                color: #fff;
                                font-size: 18px;
                                margin-bottom: 30px;
                            }
                            >div:last-child{
                                text-align: center;
                                color: #fff;
                                font-size: 18px;
                            }
                        }
                        >div:nth-child(3) {
                            padding: 30px 15px;
                            width: 100%;
                            height: auto;
                            display: flex;
                            align-items: center;
                            justify-content: space-between;
                            >div{
                                width: 50%;
                                color:#fff;
                                font-size: 18px;
                                >div:first-child{
                                    margin-bottom: 30px;
                                }
                                span{
                                    color:#f2f2f2;
                                    font-size: 12px;
                                }
                            }
                        }
                    }
                    .right{
                        width:2560px;
                        height:698px;
                        overflow-x:scroll;
                        white-space: nowrap;
                        .card{
                            width: 336px;
                            height: 100%;
                            display: inline-block;
                            position: relative;
                            margin-right: 30px;
                            >div:nth-child(1) {
                                padding: 57px 25px 30px 25px;
                                width: 100%;
                                height: auto;
                                background: url('./u524.png') no-repeat;
                                background-size: 100% 100%;
                                >div{
                                    color:#fff;
                                    font-size: 18px;
                                    margin-bottom: 10px;
                                }
                                >div:nth-child(3){
                                    color:#fff;
                                    font-size: 32px;
                                    font-weight: bold;
                                    margin-bottom: 0;
                                }
                                .progressBar{
                                    width: 100%;
                                    display: flex;
                                    align-items:flex-end;
                                    justify-content: space-between;
                                    margin-bottom: 10px;
                                    >div{
                                        width: inherit;
                                        margin-right:4px;
                                    }
                                    >span{
                                        color: #fff;
                                        font-size: 14px;
                                    }
                                }
                            }
                            >div:nth-child(2) {
                                padding: 30px 25px;
                                width: 100%;
                                height: auto;
                                display: flex;
                                align-items: center;
                                justify-content: space-between;
                                border-bottom: 1px solid #797979;
                                >div{
                                    width: 50%;
                                    color:#000;
                                    font-size: 18px;
                                    >div:first-child{
                                        margin-bottom: 30px;
                                    }
                                    span{
                                        color:#797979;
                                        font-size: 12px;
                                    }
                                }
                            }
                            >div:nth-child(3) {
                                padding: 35px 25px;
                                width: 100%;
                                height: auto;
                                text-align: left;
                                color: #000;
                                font-size: 18px;
                            }
                            >div:nth-child(4) {
                                padding: 35px 15px;
                                width: 100%;
                                height: auto;
                                text-align: center;
                                color: #000;
                                font-size: 18px;
                            }
                        }
                    }
                }
            }
            .row2{
                height: 695px;
                width: 100%;
                display: flex;
                align-items: center;
                justify-content: space-between;
                .left{
                    width: 50%;
                    height: 100%;
                    border-right: 1px solid #797979;
                    padding: 15px 20px;
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
                    padding: 15px 20px;
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
            width: 100%;
            height: 100%;
            padding: 72px 78px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: space-between;
            .title{
                        color: #fff;
                        font-size: 36px;
                        display: flex;
                        justify-content: space-between;
                        padding-bottom: 5px;
                        border-bottom: 1px solid #d7d7d7;
            }
            .row1{
                width: 100%;
                height: 271px;
                background: #5F92FC;
                border-radius: 5px;
                display: flex;
                align-items: center;
                position: relative;
                .select{
                    margin: 0px 40px;
                }
                .box1{
                    display: flex;
                    justify-content: space-between;
                    width: 950px;
                    height: 100%;
                    background: linear-gradient(to left,#2770FF,#205CD1);
                    border-radius: 27px 0 0 27px;
                    padding: 40px;
                    border: 0px;
                    .left{
                        >div:nth-child(1){
                            color:#fff;
                            font-size: 36px;
                        }
                        >div:nth-child(2){
                            color:#fff;
                            font-size: 48px;
                        }
                        >div:nth-child(3){
                            color:#fff;
                            font-size: 24px;
                            padding: 4px 6px;
                            width: 249px;
                            background: linear-gradient(to right,#b0c6f1,#205CD1);
                            >i{
                                margin: 0px 5px;
                            }
                        }
                    }
                    .right{
                        width: 370px;
                        >div{
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            font-size: 20px;
                        }
                    }
                }
                .box2{
                    width: 600px;
                    height: 100%;
                    border-radius: 27px 0 0 27px;
                    background: linear-gradient(to left,#2770FF,#205CD1);
                    padding: 40px;
                    border: 0px;
                    >div:nth-child(1){
                        color:#fff;
                        font-size: 36px;
                    }
                    >div:nth-child(2){
                        color:#fff;
                        font-size: 48px;
                    }
                    >div:nth-child(3){
                        color:#fff;
                        font-size: 24px;
                        padding: 4px 6px;
                        width: 249px;
                        background: linear-gradient(to right,#b0c6f1,#205CD1);
                        >i{
                            margin: 0px 5px;
                        }
                    }
                }
                .box3{
                    width: 1290px;
                    height: 100%;
                    border-radius: 27px 0 0 27px;
                    background: linear-gradient(to left,#2770FF,#205CD1);
                    padding: 40px;
                    color: #fff;
                    font-size: 48px;
                    border: 0px;
                }
                .return{
                    position: absolute;
                    top: 20px;
                    right: 20px;
                    color: #fff;
                    background: #169BD5;
                    width: 211px;
                    height: 63px;
                    text-align: center;
                    line-height: 63px;
                    font-size: 28px;
                }
                .datepicker{
                    position: absolute;
                    top: 95px;
                    right: 20px;
                }
            }
            .row2{
                width: 100%;
                display: flex;
                justify-content: space-between;
                .left{
                    background: #7F94CD;
                    border: 1px solid #797979;
                    width: 48%;
                    height: 324px;
                    border-radius: 30px;
                    padding: 15px 20px;
                    .content{
                        display: flex;
                        align-items: center;
                        justify-content: space-around;
                    }
                }
                .right{
                    background: #7F94CD;
                    height: 324px;
                    border: 1px solid #797979;
                    width: 48%;
                    border-radius: 30px;
                    padding: 15px 20px;
                }
            }
            .row3{
                width: 100%;
                display: flex;
                justify-content: space-between;
                .left{
                    background: #7F94CD;
                    border: 1px solid #797979;
                    width: 48%;
                    height: 324px;
                    border-radius: 30px;
                    padding: 15px 20px;
                    .content{
                        display: flex;
                        align-items: center;
                        justify-content: space-around;
                        .event{
                            width: 100px;
                            height: 198px;
                            display: inline-block;
                            >div:nth-child(1) {
                                color: #fff;
                                font-size: 18px;
                                text-align: center;
                                >span:first-child{
                                    color: #4182ff;
                                    font-size: 28px;
                                }
                            }
                            >div:nth-child(2) {
                                color: #fff;
                                font-size: 18px;
                                width: 100%;
                                white-space: pre-wrap;
                            }
                            img{
                                width: 100%;
                                height: 80px;
                            }
                        }
                    }
                }
                .right{
                    background: #7F94CD;
                    border: 1px solid #797979;
                    width: 48%;
                    height: 324px;
                    border-radius: 30px;
                    padding: 15px 20px;
                }
            }
            .row4{
                background: #7F94CD;
                border: 1px solid #797979;
                width: 100%;
                height: 400px;
                border-radius: 30px;
                padding: 15px 20px;
            }
        }
    }
</style>
<style lang="scss">
.EfficiencyPage{
    display: flex;
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
