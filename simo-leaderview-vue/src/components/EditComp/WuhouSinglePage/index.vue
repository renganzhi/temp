<template>
    <div class="WuhouSinglePage">
        <div id="Module1">
            <div class="content">
                <div class="title"><img src="./background/编组.png" alt=""></div>
                <div class="TOP3">
                    <div class="ball" @click="showPersonDetails(1)">
                        <div>{{gsqryList.length?gsqryList[1]['姓名']: '暂无数据'}}</div>
                        <div>{{gsqryList.length?gsqryList[1]['次数']: '暂无数据'}}</div>
                        <div>详情</div>
                    </div>
                    <div class="ball" @click="showPersonDetails(0)">
                        <div>{{gsqryList.length?gsqryList[0]['姓名']: '暂无数据'}}</div>
                        <div>{{gsqryList.length?gsqryList[0]['次数']: '暂无数据'}}</div>
                        <div>详情</div>
                    </div>
                    <div class="ball" @click="showPersonDetails(2)">
                        <div>{{gsqryList.length?gsqryList[2]['姓名']: '暂无数据'}}</div>
                        <div>{{gsqryList.length?gsqryList[2]['次数']: '暂无数据'}}</div>
                        <div>详情</div>
                    </div>
                </div>
                <div class="list">
                    <div class="rows" v-for="(data,index) in gsqryList" :key="index" v-show="index > 2">
                        <div class="col1"><span></span>{{9>index?'0' + (index + 1):(index + 1)}}</div>
                        <div class="col2">{{data['姓名']}}</div>
                        <div class="col3">{{data['次数']}}</div>
                        <div class="col4" @click="showPersonDetails(index)">详情</div>
                    </div>
                </div>
            </div>
            <transition name="moveRight">
                <div class="firstPop" v-show="showRYXX">
                    <div class="title">
                        <div>人员详情</div>
                        <div @click="showRYXX = false">返回</div>
                    </div>
                    <div class="row1">
                        <div>
                            <div>姓名</div>
                            <div>{{gsqryDetail['姓名']}}</div>
                        </div>
                        <div>
                            <div>联系方式</div>
                            <div>{{gsqryDetail['电话']}}</div>
                        </div>
                    </div>
                    <div class="row2">
                        <div>地址</div>
                        <div>{{gsqryDetail['地址']}}</div>
                    </div>
                    <div class="row3">
                        <div>
                            <div>投诉频率</div>
                            <div>{{gsqryDetail['投诉频率']}}次/天</div>
                        </div>
                        <div>
                            <div>风险微网格</div>
                            <div>{{gsqryDetail['小区名称']}}</div>
                        </div>
                    </div>
                    <div class="row4">
                        <div>风险业务情况</div>
                        <div>
                            <IntegratedHistogram :item="getRiskBusiness"></IntegratedHistogram>
                        </div>
                    </div>
                </div>
            </transition>
        </div>
        <div id="Module2">
            <div class="row1">
                <div class="listArr">
                    <div class="Abox">
                        <div class="leftList">
                            <div>7天内</div>
                            <div class="list">
                                <div class="li" v-for="(data, index) in kszzry7List" :key="index">
                                    <div class="info1">
                                        <div><span></span>{{index + 1}}</div>
                                        <div>{{data['姓名']}}</div>
                                        <div>
                                            <div>环比增长次数 </div>
                                            <div> {{data['增长']}}</div>
                                        </div>
                                        <div @click="showKSZZRY7(index)">
                                            详情
                                        </div>
                                    </div>
                                    <transition name="moveRight">
                                        <div class="info2" v-show="indexOf7 === index">
                                            <div>
                                                <div>{{data['个人投诉类别最多类'][0]['类别']}}</div>
                                                <div>投诉类别TOP1</div>
                                            </div>
                                            <div>
                                                <div>{{data['次数']}}</div>
                                                <div>次数</div>
                                            </div>
                                            <div>
                                                <div>{{data['增速']}}%</div>
                                                <div>投诉增速</div>
                                            </div>
                                            <div @click="indexOf7 = -1">返回>></div>
                                        </div>
                                    </transition>
                                </div>
                            </div>
                        </div>
                        <div class="RightList">
                            <div>15天内</div>
                            <div class="list">
                                <div class="li" v-for="(data, index) in kszzry15List" :key="index">
                                    <div class="info1">
                                        <div><span></span>{{index + 1}}</div>
                                        <div>{{data['姓名']}}</div>
                                        <div>
                                            <div>环比增长次数 </div>
                                            <div>{{data['环比增长']}}</div>
                                        </div>
                                        <div @click="showKSZZRY15(index)">
                                            详情
                                        </div>
                                    </div>
                                    <transition name="moveRight">
                                        <div class="info2" v-show="indexOf15 === index">
                                            <div>
                                                <div>{{data['个人投诉类别top3']?data['个人投诉类别top3'][0]['类别']:'暂无数据'}}</div>
                                                <div>投诉类别TOP1</div>
                                            </div>
                                            <div>
                                                <div>{{data['次数']}}</div>
                                                <div>次数</div>
                                            </div>
                                            <div>
                                                <div>{{data['投诉频率']}}%</div>
                                                <div>投诉增速</div>
                                            </div>
                                            <div @click="indexOf15 = -1">返回>></div>
                                        </div>
                                    </transition>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="ConditionalList">
                    <div class="ball">
                        <div class="normal1" @click="changeAppeal(data)" v-for="(data, index) in getBubble" :key="index">
                            {{data}}
                        </div>
                    </div>
                    <div class="title">{{appealType}}诉求小区排行</div>
                    <div class="list">
                        <div class="lhead">
                            <div>排名</div>
                            <div>小区名称</div>
                            <div>投诉数量</div>
                            <div></div>
                        </div>
                        <div class="lbody">
                            <div class="rows" v-for="(data,index) in sqxqphList" :key="index">
                                <div>{{index + 1}}</div>
                                <div>{{data['小区名称']}}</div>
                                <div>{{data['数量']}}</div>
                                <div @click="ShowGZFF1(index)">详情</div>
                            </div>
                        </div>
                    </div>
                    <transition name="moveRight">
                            <div class="pop1" v-show="showGZFF1">
                                <div class="left">
                                    <div><span class="key">小区名称</span><span class="value">{{sqxqphDetail['小区名称']}}</span></div>
                                    <div><span class="key">小区对应排名</span><span class="value">{{sqxqphDetail['小区排名']}}</span></div>
                                    <div><span class="key">小区所属街道</span><span class="value">{{sqxqphDetail['街道']}}</span></div>
                                    <div><span class="key">此小区存在的此风险数</span><span class="value">{{sqxqphDetail['数量']}}</span></div>
                                    <div><span class="key">风险涉及人员数</span><span class="value">{{sqxqphDetail['小区该类投诉总人数']}}</span></div>
                                    <div><span class="key">高风险人员数</span><span class="value">{{sqxqphDetail['高风险人员']?sqxqphDetail['高风险人员'].length:0}}</span></div>
                                    <div><span class="key">该小区近15日风险增速</span><span class="value">0%</span></div>
                                    <div><span class="key">该小区风险增速</span><span class="value">0%</span></div>
                                    <div><span class="key">近15日环比增长数</span><span class="value">0%</span></div>
                                </div>
                                <div class="right">
                                    <div class="return" @click="CloseGZFF1">返回</div>
                                    <div class="title2">高风险人员清单</div>
                                    <div class="list2">
                                        <div class="li2" v-for="(data, index) in sqxqphDetail['高风险人员']" :key="index">
                                            <div>{{data['姓名']}}</div>
                                            <div>{{data['手机']}}</div>
                                            <div @click="ShowGZFF2(data['手机'])">></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                    </transition>
                    <transition name="moveTop">
                        <div class="pop2" v-show="showGZFF2">
                            <div class="left">
                                <div><span class="key">人员姓名</span><span class="value">{{gfxryDetail['姓名']}}</span></div>
                                <div><span class="key">联系方式</span><span class="value">{{gfxryDetail['电话']}}</span></div>
                                <div><span class="key">家庭住址</span><span class="value">{{gfxryDetail['地址']}}</span></div>
                                <div><span class="key">投诉频率</span><span class="value">{{gfxryDetail['投诉频率']}}次/天</span></div>
                                <div><span class="key">风险微网格</span><span class="value">{{gfxryDetail['小区名称']}}</span></div>
                            </div>
                            <div class="right">
                                <div class="return" @click="CloseGZFF2">返回</div>
                                <div class="title2">风险业务情况</div>
                                <div class="barchart">
                                    <IntegratedHistogram :item="getFXYWQK"></IntegratedHistogram>
                                </div>
                            </div>
                        </div>
                    </transition>
                </div>
            </div>
            <div class="row2">
                <div class="tabButton">
                    <div class="normal2" @click="changeGSQXQ('矛盾突出小区')">矛盾突出小区</div>
                    <div class="normal2" @click="changeGSQXQ('高群体诉求小区')">高群体诉求小区</div>
                    <div class="normal2" @click="changeGSQXQ('诉求快速增长小区')">诉求快速增长小区</div>
                </div>
                <div class="tabContent">
                    <div v-show="gsqxq === '矛盾突出小区'" class="lunbo1" ref="lunbo1" @mousewheel="onMouseWheel($event, 'lunbo1')">
                                <div class="col" v-for="(data, index) in mdtcxqList" :key="index">
                                    <div>{{9 > index?'0' + (index + 1):(index + 1)}}</div>
                                    <div @click="ShowMDTCXQ(index)">详情</div>
                                    <div>{{data['小区']}}</div>
                                    <div>
                                        <div>
                                            <div>{{data['数量']}}</div>
                                            <div>全年投诉次数</div>
                                        </div>
                                        <div>
                                            <div>{{data['全年该小区投诉总人数']}}</div>
                                            <div>涉及人数</div>
                                        </div>
                                    </div>
                                </div>
                    </div>
                    <div v-show="gsqxq === '高群体诉求小区'" class="lunbo2" ref="lunbo2" @mousewheel="onMouseWheel($event, 'lunbo2')">
                                <div class="col" v-for="(data, index) in gqtsqxqList" :key="index">
                                    <div>
                                        <span>▪ {{index + 1}}</span>
                                        <span @click="ShowMDTCXQ(index)">详情>></span>
                                    </div>
                                    <div>{{data['小区']}}</div>
                                    <div>小区名称</div>
                                    <div>{{data['本月该小区投诉人数总数']}}</div>
                                    <div>全年投诉涉及人数</div>
                                </div>
                    </div>
                    <div v-show="gsqxq === '诉求快速增长小区'" class="lunbo3" ref="lunbo3" @mousewheel="onMouseWheel($event, 'lunbo3')">
                                <div class="col" v-for="(data, index) in sqkszzxqList" :key="index">
                                    <div>{{9 > index?'0' + (index + 1):(index + 1)}}</div>
                                    <div @click="ShowMDTCXQ(index)">详情</div>
                                    <div>{{data['小区']}}</div>
                                    <div>
                                        <div>{{data['近15日投诉总人数']}}</div>
                                        <div>近15日投诉总人数</div>
                                    </div>
                                </div>
                    </div>
                    <transition name="moveRight">
                        <div class="pop1" v-show="showMDTCXQ">
                            <div class="left">
                                <div v-show="gsqxq === '矛盾突出小区'">
                                    <div>
                                        <div><span class="key">所属街道</span><span class="value">{{gsqxqDetail['街道']}}</span></div>
                                        <div><span class="key">小区地址</span><span class="value">{{gsqxqDetail['地址']}}</span></div>
                                        <div><span class="key">本月投诉比变动情况</span><span class="value">{{gsqxqDetail['本月投诉环比上月增长率']}}%</span></div>
                                        <div><span class="key">高风险人员数</span><span class="value">{{gsqxqDetail['高风险人员']?gsqxqDetail['高风险人员'].length:0}}</span></div>
                                    </div>
                                    <div class="title2">风险分类排名</div>
                                    <div class="list2">
                                        <div class="li2" v-for="(data, index) in gsqxqDetail['全年该小区投诉类别top5']" :key="index">
                                            <div>· {{index + 1}}</div>
                                            <div>{{data['小区']}}</div>
                                            <div>{{data['数量']}}</div>
                                        </div>
                                    </div>
                                    <div class="title2">月度投诉量变动情况</div>
                                    <div class="eline">
                                        <ELine :item="getYDTSL"></ELine>
                                    </div>
                                </div>
                                <div v-show="gsqxq === '高群体诉求小区'">
                                    <div>
                                        <div><span class="key">所属街道</span><span class="value">{{gsqxqDetail['街道']}}</span></div>
                                        <div><span class="key">小区地址</span><span class="value">{{gsqxqDetail['地址']}}</span></div>
                                        <div><span class="key">本月投诉比变动情况</span><span class="value">{{gsqxqDetail['本月投诉环比上月增长率']?gsqxqDetail['本月投诉环比上月增长率'] + '%':'暂无数据'}}</span></div>
                                        <div><span class="key">高风险人员数</span><span class="value">{{gsqxqDetail['高风险人员']?gsqxqDetail['高风险人员'].length:0}}</span></div>
                                    </div>
                                    <div class="title2">风险分类排名</div>
                                    <div class="list2">
                                        <div class="li2" v-for="(data, index) in gsqxqDetail['全年该小区投诉类别top5']" :key="index">
                                            <div>· {{index + 1}}</div>
                                            <div>{{data['小区']}}</div>
                                            <div>{{data['次数']}}</div>
                                        </div>
                                    </div>
                                    <div class="title2">月度投诉量变动情况</div>
                                    <div class="eline">
                                        <ELine :item="getYDTSL"></ELine>
                                    </div>
                                </div>
                                <div v-show="gsqxq === '诉求快速增长小区'">
                                    <div>
                                        <div><span class="key">所属街道</span><span class="value">{{gsqxqDetail['街道']}}</span></div>
                                        <div><span class="key">小区地址</span><span class="value">{{gsqxqDetail['地址']}}</span></div>
                                        <div><span class="key">15日内环比增长数</span><span class="value">{{gsqxqDetail['近15日环比增长数']}}</span></div>
                                        <div><span class="key">近15日投诉总人数</span><span class="value">{{gsqxqDetail['近15日投诉总人数']}}</span></div>
                                        <div><span class="key">高风险人员数</span><span class="value">{{gsqxqDetail['高风险人员']?gsqxqDetail['高风险人员'].length:0}}</span></div>
                                        <div><span class="key">近15日风险增速</span><span class="value">{{gsqxqDetail['近15日增速']}}</span></div>
                                    </div>
                                    <div class="title2">风险分类排名</div>
                                    <div class="list2">
                                        <div class="li2" v-for="(data, index) in gsqxqDetail['投诉类别数量']" :key="index">
                                            <div>· {{index + 1}}</div>
                                            <div>{{data['小区']}}</div>
                                            <div>{{data['次数']}}</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="right">
                                <div class="return" @click="CloseMDTCXQ">返回</div>
                                <div class="title2">高风险人员清单</div>
                                <div class="list2">
                                    <div class="li2" v-for="(data, index) in gsqxqDetail['高风险人员']" :key="index">
                                        <div>{{data['姓名']}}</div>
                                        <div>{{data['电话']}}</div>
                                        <div @click="ShowGFXRYQD(data['电话'])">></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </transition>
                    <transition name="moveTop">
                        <div class="pop2" v-show="showGFXRYQD">
                            <div class="left">
                                <div><span class="key">人员姓名</span><span class="value">{{gfxryDetail2['姓名']}}</span></div>
                                <div><span class="key">联系方式</span><span class="value">{{gfxryDetail2['电话']}}</span></div>
                                <div><span class="key">家庭住址</span><span class="value">{{gfxryDetail2['地址']}}</span></div>
                                <div><span class="key">投诉频率</span><span class="value">{{gfxryDetail2['投诉频率']}}次/天</span></div>
                                <div><span class="key">风险微网格</span><span class="value">{{gfxryDetail2['小区名称']}}</span></div>
                            </div>
                            <div class="right">
                                <div class="return" @click="CloseGFXRYQD">返回</div>
                                <div class="title2">风险业务情况</div>
                                <div class="barchart">
                                    <IntegratedHistogram :item="getFXYWQK2"></IntegratedHistogram>
                                </div>
                            </div>
                        </div>
                    </transition>
                </div>
            </div>
            <div class="row3">
                <div class="title">区民生诉求指数</div>
                <div>
                    <div class="lunbo" ref="lunbo" @mousewheel="onMouseWheel($event, 'lunbo')">
                        <div class="col">
                            <div>区民生诉求指数</div>
                            <div><span>{{getWholeRegionNum}}</span></div>
                            <img src="" alt="">
                        </div>
                        <div class="col" v-for="(data, index) in qmssqList" :key="index">
                            <div>{{data['街道']}}</div>
                            <div>
                                <span>{{data['次数']}}</span>
                                <span @click="ShowQMSSQ(index)">详情</span>
                            </div>
                            <img :src="getSreetImg(data['街道'])" alt="">
                        </div>
                    </div>
                    <transition name="moveRight">
                        <div class="pop1" v-show="showQMSSQ">
                            <div class="return" @click="CloseQMSSQ">返回</div>
                            <div class="column1">
                                <div>风险分类</div>
                                <div class="list1">
                                    <div class="li1" v-for="(data, index) in qmssqDetail['全年投诉类别数量']" :key="index">
                                        <div class="prop1">{{data['小区']}}</div>
                                        <div class="prop2">{{data['次数']}}</div>
                                    </div>
                                </div>
                            </div>
                            <div class="column2">
                                <div>
                                    <span>高风险人员数</span>
                                    <span>{{qmssqDetail['高风险人员']?qmssqDetail['高风险人员'].length:0}}</span>
                                </div>
                                <div>
                                    <div class="list2">
                                        <div class="li2" v-for="(data, index) in qmssqDetail['高风险人员']" :key="index">
                                            <div class="prop1">{{data['姓名']}}</div>
                                            <div class="prop2">{{data['电话']}}</div>
                                            <div class="prop3" @click="ShowGFXRYS(data['电话'])">></div>
                                        </div>
                                    </div>
                                    <transition name="moveTop">
                                        <div class="pop2" v-show="showGFXRYS">
                                            <div class="return" @click="CloseGFXRYS">返回</div>
                                            <div><span class="key">人员姓名</span><span class="value">{{gfxryDetail3['姓名']}}</span></div>
                                            <div><span class="key">联系方式</span><span class="value">{{gfxryDetail3['电话']}}</span></div>
                                            <div><span class="key">家庭住址</span><span class="value">{{gfxryDetail3['地址']}}</span></div>
                                            <div><span class="key">投诉频率</span><span class="value">{{gfxryDetail3['投诉频率']}}次/天</span></div>
                                            <div><span class="key">风险微网格</span><span class="value">{{gfxryDetail3['小区名称']}}</span></div>
                                        </div>
                                    </transition>
                                </div>
                            </div>
                            <div class="column3">
                                <div>
                                    <span>高矛盾值小区数</span>
                                    <span>{{qmssqDetail['高矛盾小区']?qmssqDetail['高矛盾小区'].length:0}}</span>
                                    <span @click="showGMDZXQ = !showGMDZXQ">详情</span>
                                </div>
                                <div>
                                    <transition name="moveBottom">
                                        <div class="list3" v-show="showGMDZXQ">
                                            <div class="li3" v-for="(data, index) in qmssqDetail['高矛盾小区']" :key="index">
                                                <div class="prop1">{{data['小区']}}</div>
                                                <div class="prop2">{{data['次数']}}</div>
                                            </div>
                                        </div>
                                    </transition>
                                </div>
                            </div>
                            <div class="column4">
                                <div>
                                    <span>近15日环比增长数</span>
                                    <span>{{qmssqDetail['近15日环比增长数']}}</span>
                                </div>
                                <div></div>
                            </div>
                            <div class="column5">
                                <div>近15日投诉增长情况</div>
                                <div class="eline">
                                    <ELine :item="getTSZZQK15"></ELine>
                                </div>
                            </div>
                        </div>
                    </transition>
                </div>
            </div>
        </div>
        <EfficiencyPage></EfficiencyPage>
        <div id="Module5">
            <div class="title">群众诉求</div>
            <div>
                <CityEvent :item="getEventData"></CityEvent>
            </div>
        </div>
        <div id="Module5Pop">

        </div>
    </div>
</template>
<script>
import IntegratedHistogram from '../IntegratedHistogram/index.vue'
import ELine from '../ELine/index.vue'
import NewGauge from '../NewGauge/index.vue'
import NewProgress from '../NewProgress/index.vue'
import CityEvent from '../CityEvent/index.vue'
import EfficiencyPage from './EfficiencyPage.vue'
import NewPie from '../NewPie/index.vue'
import element from '@/element'
export default {
  data: function () {
    return {
      showRYXX: false,
      indexOf7: -1,
      indexOf15: -1,
      showGZFF1: false,
      showGZFF2: false,
      appealType: '',
      showMDTCXQ: false,
      showGFXRYQD: false,
      showQMSSQ: false,
      showGMDZXQ: false,
      showGFXRYS: false,
      gsqxq: '矛盾突出小区',
      gsqryList: [], // module1 高诉求人员列表
      gsqryDetail: {}, // module1 高诉求人员详情
      kszzry7List: [], // module2 近7日快速增长人员列表
      kszzry15List: [], // module2 近15日快速增长人员列表
      AllsqxqphList: [], // module2 所有诉求小区排行列表
      sqxqphDetail: {}, // module2 诉求小区排行详情
      gfxryDetail: {}, // module2 诉求小区排行详情 高风险人员详情
      mdtcxqList: [], // module2 矛盾突出小区列表
      gqtsqxqList: [], // module2 高群体诉求小区列表
      sqkszzxqList: [], // module2 诉求快速增长小区列表
      gsqxqDetail: {}, // module2 高诉求小区详情
      gfxryDetail2: {}, // module2 高诉求小区详情 高风险人员详情
      qmssqList: [], // module2 区民生诉求指数列表
      qmssqDetail: {}, // module2 区民生诉求指数详情
      gfxryDetail3: {} // module2 区民生诉求指数详情 高风险人员详情

    }
  },
  components: {IntegratedHistogram, EfficiencyPage, ELine, NewGauge, NewProgress, CityEvent, NewPie},
  computed: {
    // module2 某诉求小区排行列表
    sqxqphList () {
      let arr = []
      this.AllsqxqphList.forEach(element => {
        if (element['类别名称'] === this.appealType) {
          arr = element['明细']
        }
      })
      return arr
    },
    getSreetImg () {
      return (street) => {
        if (street) {
          return require('./' + street + '.jpg')
        }
      }
    },
    getWholeRegionNum () {
      let num = 0
      this.qmssqList.forEach(element => {
        num = num + element['次数']
      })
      return num
    },
    getRiskBusiness () {
      let chartData = {
        columns: [],
        rows: []
      }
      if (this.gsqryDetail['个人投诉类别top3']) {
        chartData = this.gsqryDetail['个人投诉类别top3']
      }
      return {
        'text': '柱状图',
        width: 600,
        height: 300,
        'imgClass': 'icon-n-histogram',
        'chartType': 'IntegratedHistogram',
        'barType': 'NewHistogram',
        'ifGradual': 'false',
        'showBackground1': false, // 是否显示背景柱图
        'backgroundColor1': 'rgba(180, 180, 180, 0.2)', // 背景柱图的颜色
        'barRadius1': 0, // 柱体圆角
        'barWidth1': 40, // 柱体大小
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
        'formatterType1': '1',
        'cropSize1': 2,
        'tooltipShow1': true,
        'dataTypeStation': false,
        'tooltipBackColor1': '#57625d',
        'tooltipTextColor1': '#fff',
        'tooltipfontSize1': 14,
        'splitColor1': '#fff',
        'splitSize1': 1,
        'rotate1': 0,
        'chartData1': chartData
      }
    },
    getTSZZQK15 () {
      return {
        'text': '曲线图',
        'imgClass': 'icon-n-line',
        'height': 220,
        'width': 1000,
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
        'gridBotton': 15,
        'gridLeft': 5,
        'gridRight': 5,
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
    getYDTSL () {
      let chartData = {
        columns: [],
        rows: [],
        unit: '次',
        unitX: '月份'
      }
      if (this.gsqxqDetail['双曲线图']) {
        chartData.rows = this.gsqxqDetail['双曲线图'].rows
        this.gsqxqDetail['双曲线图'].columns.forEach(element => {
          if (this.gsqxqDetail['双曲线图'].rows[0][element] || this.gsqxqDetail['双曲线图'].rows[0][element] === 0) {
            chartData.columns.push(element)
          }
        })
      }
      return {
        'text': '曲线图',
        'imgClass': 'icon-n-line',
        'height': 300,
        'width': 1280,
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
        'chartData': chartData
      }
    },
    getFXYWQK () {
      let chartData = {
        columns: [],
        rows: []
      }
      if (this.gfxryDetail['个人投诉类别top3']) {
        chartData = this.gfxryDetail['个人投诉类别top3']
      }
      return {
        'text': '柱状图',
        width: 650,
        height: 160,
        'imgClass': 'icon-n-histogram',
        'chartType': 'IntegratedHistogram',
        'barType': 'NewHistogram',
        'ifGradual': 'false',
        'showBackground1': false, // 是否显示背景柱图
        'backgroundColor1': 'rgba(180, 180, 180, 0.2)', // 背景柱图的颜色
        'barRadius1': 0, // 柱体圆角
        'barWidth1': 40, // 柱体大小
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
        'ctLegendSize1': '18',
        'TabFontSize': '18',
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
        'axisLabelSize1': '18',
        'DanweiColor1': '#828bac',
        'DanweiSize1': 18,
        'minInterval1': '',
        'legendY1': 90,
        'gridTop1': 15,
        'gridBotton1': 5,
        'gridLeft1': 5,
        'gridRight1': 5,
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
        'chartData1': chartData
      }
    },
    getFXYWQK2 () {
      let chartData = {
        columns: [],
        rows: []
      }
      if (this.gfxryDetail2['个人投诉类别top3']) {
        chartData = this.gfxryDetail2['个人投诉类别top3']
      }
      return {
        'text': '柱状图',
        width: 850,
        height: 160,
        'imgClass': 'icon-n-histogram',
        'chartType': 'IntegratedHistogram',
        'barType': 'NewHistogram',
        'ifGradual': 'false',
        'showBackground1': false, // 是否显示背景柱图
        'backgroundColor1': 'rgba(180, 180, 180, 0.2)', // 背景柱图的颜色
        'barRadius1': 0, // 柱体圆角
        'barWidth1': 40, // 柱体大小
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
        'ctLegendSize1': '18',
        'TabFontSize': '18',
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
        'axisLabelSize1': '18',
        'DanweiColor1': '#828bac',
        'DanweiSize1': 18,
        'minInterval1': '',
        'legendY1': 90,
        'gridTop1': 20,
        'gridBotton1': 5,
        'gridLeft1': 5,
        'gridRight1': 5,
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
        'chartData1': chartData
      }
    },
    getBubble () {
      return [
        '工资发放',
        '城乡居民医疗',
        '水电气',
        '小区管理',
        '商品质量',
        '房屋中介',
        '占道停车',
        '营业执照',
        '占道经营',
        '消费纠纷',
        '物业服务',
        '食品安全',
        '消防安全',
        '企业诉求'
      ]
    },
    getEventData () {
      return {
        'text': '城运事件',
        'imgClass': 'icon-n-text',
        'chartType': 'CityEvent',
        'width': 600,
        'height': 1478,
        'titleColor': ['#f04d4d', '#d3b3b3'],
        'titleSize': 20,
        'dateColor': '#3667ff',
        'dateSize': 12,
        'contentColor': '#6689f8',
        'contentSize': 12,
        'size': 'big',
        paddingTop: -90,
        paddingLeft: 0,
        boxFontSize: 16,
        buttonPadding: 20,
        buttonMargin: 20,
        boxDirection: false,
        normalButton: '',
        normalButtonName: '',
        checkedButton: '',
        checkedButtonName: '',
        ifLoop: true,
        loopSpeed: 3000,
        'chartData': {
          'dataArray': [
            {
              'columns': [
                'title',
                'content',
                'date'
              ],
              'tile': '值守事件',
              'rows': [
                {
                  'title': null,
                  'content': '报告领导:120于12点15分接警，在武侯区逸霞街21号棕北中学，据报警人称有人晕厥，调派四川省妇幼保健院出诊，医院于15点32分回复，现场接回一名未成年女性13岁，初步诊断为癫痫，重度无有生命危险。收到请回复，谢谢！',
                  'date': '2022-11-21 12:15:00'
                },
                {
                  'title': null,
                  'content': '五办群众上访类信息报送\n\n上访时间：2022年11月21日13：49\n\n上访地点：五办（蜀绣路办公区）政策咨询大厅\n\n被访部门：住建局\n\n上访人员：来自都江堰市融创文旅城的业主（开发商 融创）\n \n上访原因：反映停工停产、延期交房问题\n\n人员状态：无过激行为/无横幅/无统一服装\n\n上访动态 ：13：49 上访人数10人，开始接待中\n  \n 情况跟进：持续跟进中',
                  'date': '2022-11-21 13:49:00'
                },
                {
                  'title': null,
                  'content': '110转接【讨薪】\n涉及人数： 几百人\n做工时间：2022年4月-至今\n金额：几十万\n拖欠单位：陕西建工\n问题描述/补充：讨要工资\n来电诉求：及时发放工资，现场100多人聚集，已派警前往处置，暂无过激行为。\n我办已紧急联系武侯区85365671值班老师，要求立即核实维稳。值班老师回复，簇锦街办已将现场聚集群众带至街道办协调，现场平稳。',
                  'date': '2022-11-21 00:00:00'
                },
                {
                  'title': null,
                  'content': '管某坤，男，27岁，居住在火车南站街道高攀东路33号魏玛国际3栋1单元2702号，工作单位为成都武侯瑞泰融诚口腔医院。11月15日从贵州返蓉后，每日主要活动轨迹为两点一线上下班。16日-18日核酸检测阴性，19日核酸检测阳性（CT值38，38），20日核酸检测阳性（CT值24,25）。相关涉疫工作区疾控中心正在有序开展。',
                  'date': '2022-11-20 15:00:00'
                },
                {
                  'title': null,
                  'content': '【我市本土疫情情况】11月19日0—24时',
                  'date': '2022-11-20 09:52:00'
                },
                {
                  'title': null,
                  'content': '11月19日0-24时成都本土疫情情况',
                  'date': '2022-11-20 09:43:00'
                },
                {
                  'title': null,
                  'content': '11月19日0-24时成都本土疫情情况',
                  'date': '2022-11-20 09:10:00'
                },
                {
                  'title': null,
                  'content': '11月20日疫情信息报送领导',
                  'date': '2022-11-20 00:55:00'
                },
                {
                  'title': null,
                  'content': '【我市本土疫情情况】11月19日0—18时，我市新增本土确诊病例64例，新增本土无症状感染者84例。其中在管密接96例，隔离管控人员18例，愿检尽检16例，重点人员筛查12例，主动就诊6例。详情见附件：11月19日0—18时成都市本土疫情情况。（主送：小琳、凤朝同志；抄送：瑞武、彦夫、邓涛、筱柳、智勇、俊杰、光辉、陈麟、玉泉、张瑛、海波、志华、任远、平江、旭光、荣生、王乾、林楠，先毅，杨俊、云帆同志。值班信息，请勿外传。）',
                  'date': '2022-11-19 20:50:00'
                },
                {
                  'title': null,
                  'content': '【我市本土疫情情况】11月19日0—14时，我市新增本土确诊病例41例，新增本土无症状感染者69例。其中在管密接75例，隔离管控人员11例，愿检尽检11例，重点人员筛查8例，主动就诊5例。详情见附件：11月19日0—14时成都市本土疫情情况。（主送：小琳、凤朝同志；抄送：瑞武、彦夫、邓涛、筱柳、智勇、俊杰、光辉、陈麟、玉泉、张瑛、海波、志华、任远、平江、旭光、荣生、王乾、林楠，先毅，杨俊、云帆同志。值班信息，请勿外传。）',
                  'date': '2022-11-19 17:36:00'
                }
              ]
            }
          ]
        }
      }
    }
  },
  watch: {
    appealType: function () {
      this.showGZFF1 = false
      this.showGZFF2 = false
    },
    gsqxq: function () {
      this.showMDTCXQ = false
      this.showGFXRYQD = false
      this.gfxryDetail2 = {}
    }
  },
  methods: {
    onMouseWheel (e, refName) {
      let eventDelta = -e.wheelDelta || -e.deltaY * 40
      let box = this.$refs[refName]
      box.scrollLeft = box.scrollLeft + eventDelta / 2
    },
    showPersonDetails (index) {
      this.showRYXX = true
      this.gsqryDetail = this.gsqryList[index]
    },
    changeGSQXQ (type) {
      this.gsqxq = type
      $('.normal2').removeClass('active2')
      event.target.classList.add('active2')
    },
    showKSZZRY7 (index) {
      if (this.indexOf7 === -1) {
        this.indexOf7 = index
      }
    },
    showKSZZRY15 (index) {
      if (this.indexOf15 === -1) {
        this.indexOf15 = index
      }
    },
    changeAppeal (data) {
      this.appealType = data
      $('.normal1').removeClass('active1')
      event.target.classList.add('active1')
    },
    ShowGZFF1 (index) {
      this.showGZFF1 = true
      this.sqxqphDetail = this.sqxqphList[index]
      this.sqxqphDetail['小区排名'] = index + 1
    },
    CloseGZFF1 () {
      this.showGZFF1 = false
      this.showGZFF2 = false
      this.sqxqphDetail = {}
    },
    ShowGZFF2 (number) {
      this.showGZFF2 = true
      this.gsqryList.forEach(element => {
        if (element['电话'] === number) {
          this.gfxryDetail = element
        }
      })
    },
    CloseGZFF2 () {
      this.showGZFF2 = false
      this.gfxryDetail = {}
    },
    ShowMDTCXQ (index) {
      this.showMDTCXQ = true
      if (this.gsqxq === '矛盾突出小区') {
        this.gsqxqDetail = this.mdtcxqList[index]
      } else if (this.gsqxq === '高群体诉求小区') {
        this.gsqxqDetail = this.gqtsqxqList[index]
      } else if (this.gsqxq === '诉求快速增长小区') {
        this.gsqxqDetail = this.sqkszzxqList[index]
      }
    },
    CloseMDTCXQ () {
      this.showMDTCXQ = false
      this.gsqxqDetail = {}
    },
    ShowGFXRYQD (number) {
      this.showGFXRYQD = true
      this.gsqryList.forEach(element => {
        if (element['电话'] === number) {
          this.gfxryDetail2 = element
        }
      })
    },
    CloseGFXRYQD () {
      this.showGFXRYQD = false
      this.gfxryDetail2 = {}
    },
    ShowQMSSQ (index) {
      this.showQMSSQ = true
      this.qmssqDetail = this.qmssqList[index]
    },
    CloseQMSSQ () {
      this.showQMSSQ = false
      this.showGFXRYS = false
      this.showGMDZXQ = false
      this.qmssqDetail = {}
    },
    ShowGFXRYS (number) {
      this.showGFXRYS = true
      this.gsqryList.forEach(element => {
        if (element['电话'] === number) {
          this.gfxryDetail3 = element
        }
      })
    },
    CloseGFXRYS () {
      this.showGFXRYS = false
      this.gfxryDetail3 = {}
    },
    getHomePageData () {
      // 获取module1 高诉求人员排行信息
      this.axios.get('/leaderview/newDistrict/GetMSSQ1').then(res => {
        if (res.success && res.obj.rows) {
          this.gsqryList = res.obj.rows
        }
      })
      // 获取module2 全区7天内投诉次数增长最多人员top50
      this.axios.get('/leaderview/newDistrict/GetMSSQ13').then(res => {
        if (res.success && res.obj.rows) {
          this.kszzry7List = res.obj.rows
        }
      })
      // 获取module2 全区7天内投诉次数增长最多人员top50
      this.axios.get('/leaderview/newDistrict/GetMSSQ2').then(res => {
        if (res.success && res.obj.rows) {
          this.kszzry15List = res.obj.rows
        }
      })
      // 获取module2 各个诉求类别小区排行
      this.axios.get('/leaderview/newDistrict/GetMSSQ10').then(res => {
        if (res.success && res.obj.rows) {
          this.AllsqxqphList = res.obj.rows
        }
      })
      // 获取module2 矛盾突出小区列表
      this.axios.get('/leaderview/newDistrict/GetMSSQ3').then(res => {
        if (res.success && res.obj.rows) {
          this.mdtcxqList = res.obj.rows
        }
      })
      // 获取module2 高群体诉求小区列表
      this.axios.get('/leaderview/newDistrict/GetMSSQ5').then(res => {
        if (res.success && res.obj.rows) {
          this.gqtsqxqList = res.obj.rows
        }
      })
      // 获取module2 诉求快速增长小区列表
      this.axios.get('/leaderview/newDistrict/GetMSSQ8').then(res => {
        if (res.success && res.obj.rows) {
          this.sqkszzxqList = res.obj.rows
        }
      })
      // 获取module2 区民生诉求指数
      this.axios.get('/leaderview/newDistrict/GetMSSQ4').then(res => {
        if (res.success && res.obj.rows) {
          this.qmssqList = res.obj.rows
        }
      })
    }
  },
  mounted () {
    this.getHomePageData()
  }
}
</script>
<style lang="scss" scoped>
.WuhouSinglePage{
    display: flex;
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
    #Module1{
        width: 680px;
        height: 1620px;
        position: relative;
        .content{
            width: 100%;
            height: 100%;
            padding: 32px 32px 32px 39px;
            .title{
                img{
                    width: 609px;
                    height: 76px;
                }
                margin-bottom: 32px;
            }
            .TOP3{
                color: #797979;
                width: 100%;
                font-size: 14px;
                margin-bottom: 12px;
                display: flex;
                .ball:nth-child(1){
                    width: 192px;
                    height: 250px;
                    background: url('./background/2_1.png');
                    margin-right: 16px;
                    >div:nth-child(1){
                        color:#fff;
                        font-size: 26px;
                        width: 100%;
                        text-align: center;
                        margin-top:64px;
                    }
                    >div:nth-child(2){
                        font-size: 44px;
                        font-weight: bold;
                        width: 100%;
                        text-align: center;
                        background-image: linear-gradient(to bottom, #d6eaef, #95a5a9);
                        -webkit-background-clip: text;
                        color: transparent;
                        margin-top:12px;
                        margin-bottom: 20px;
                    }
                    >div:nth-child(3){
                        color:#fff;
                        font-size: 20px;
                        width: 80px;
                        height: 32px;
                        text-align: center;
                        line-height: 32px;
                        border:1px solid rgba(255,255,255,0.3);
                        border-radius: 16px;
                        text-align: center;
                        margin: 0px auto;
                    }
                }
                .ball:nth-child(2){
                    width: 192px;
                    height: 280px;
                    background: url('./background/1_1.png');
                    margin-right: 16px;
                    >div:nth-child(1){
                        color:#fff;
                        font-size: 26px;
                        width: 100%;
                        text-align: center;
                        margin-top:64px;
                    }
                    >div:nth-child(2){
                        font-size: 44px;
                        font-weight: bold;
                        width: 100%;
                        text-align: center;
                        background-image: linear-gradient(to bottom, #f2e9db, #f5a01d);
                        -webkit-background-clip: text;
                        color: transparent;
                        margin-top:12px;
                        margin-bottom: 20px;
                    }
                    >div:nth-child(3){
                        color:#fff;
                        font-size: 20px;
                        width: 80px;
                        height: 32px;
                        text-align: center;
                        line-height: 32px;
                        border:1px solid rgba(255,255,255,0.3);
                        border-radius: 16px;
                        text-align: center;
                        margin: 0px auto;
                    }
                }
                .ball:nth-child(3){
                    width: 192px;
                    height: 220px;
                    background: url('./background/3_1.png');
                    >div:nth-child(1){
                        color:#fff;
                        font-size: 26px;
                        width: 100%;
                        text-align: center;
                        margin-top:64px;
                    }
                    >div:nth-child(2){
                        font-size: 44px;
                        font-weight: bold;
                        width: 100%;
                        text-align: center;
                        background-image: linear-gradient(to bottom, #f0e1d7, #c27b4b);
                        -webkit-background-clip: text;
                        color: transparent;
                        margin-top:12px;
                        margin-bottom: 24px;
                    }
                    >div:nth-child(3){
                        color:#fff;
                        font-size: 20px;
                        width: 80px;
                        height: 32px;
                        text-align: center;
                        line-height: 32px;
                        border:1px solid rgba(255,255,255,0.3);
                        border-radius: 16px;
                        text-align: center;
                        margin: 0px auto;
                    }
                }
            }
            .list{
                width: 608px;
                height: 1164px;
                overflow-y:scroll;
                // margin:0 auto;
                .rows{
                    width: 100%;
                    height: 72px;
                    background: url('./background/编组_7.png');
                    margin-bottom: 12px;
                    display: flex;
                    align-items: center;
                    justify-content: space-around;
                    .col1{
                        color: #5ae8fa;
                        font-size: 20px;
                        font-weight: bold;
                        display: flex;
                        align-items: center;
                        text-align: center;
                        width: 20%;
                        >span{
                            display: inline-block;
                            background: #fcb83c;
                            width: 10px;
                            height: 10px;
                            border-radius: 50%;
                            margin-right: 10px;
                        }
                    }
                    .col2{
                        color: #c5eef3;
                        font-size: 22px;
                        width: 30%;
                        text-align: center;
                    }
                    .col3{
                        color: #c5eef3;
                        font-size: 22px;
                        width: 30%;
                        text-align: center;
                    }
                    .col4{
                        color:#16DFF8;
                        font-size: 20px;
                        width: 80px;
                        height: 32px;
                        text-align: center;
                        line-height: 32px;
                        border:1px solid rgba(22,223,248,0.6);
                        border-radius: 16px;
                        text-align: center;
                        margin: 0px auto;
                    }
                }
            }
        }
        .firstPop{
            width: 100%;
            height: 100%;
            position: absolute;
            top: 0;
            left: 0;
            background: #0f1a54;
            padding: 30px 20px;
            .title{
                font-size: 28px;
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 60px;
            }
            .row1{
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 60px;
                >div{
                    >div:first-child{
                        font-size: 24px;
                        margin-bottom: 5px;
                    }
                    >div:last-child{
                        font-size: 40px;
                        color: #1ee4fc;
                    }
                }
            }
            .row2{
                margin-bottom: 60px;
                >div:first-child{
                    font-size: 24px;
                    margin-bottom: 5px;
                }
                >div:last-child{
                    font-size: 40px;
                    color: #1ee4fc;
                }
            }
            .row3{
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 60px;
                >div{
                    >div:first-child{
                        font-size: 24px;
                        margin-bottom: 5px;
                    }
                    >div:last-child{
                        font-size: 40px;
                        color: #1ee4fc;
                    }
                }
            }
            .row4{
                >div:first-child{
                    font-size: 24px;
                    margin-bottom: 20px;
                }
            }
        }
    }
    #Module2{
        width: 2792px;
        height:  1620px;
        padding: 32px 30px 32px 32px;
        .row1{
            width: 100%;
            display: flex;
            .listArr{
                width: 1281px;
                height: 800px;
                background: url('./background/编组_11.png');
                padding: 105px 37px 14px 36px;
                .Abox{
                    display: flex;
                    justify-content: space-between;
                    .leftList,.RightList{
                        width: 580px;
                        >div:first-child{
                            color: rgba(197,238,243,1);
                            font-size: 26px;
                            margin-bottom: 19px;
                        }
                        .list{
                            width: 100%;
                            height: 632px;
                            overflow-y: scroll;
                            .li{
                                height: 72px;
                                position: relative;
                                margin-bottom: 8px;
                                .info1{
                                    width: 100%;
                                    height:100%;
                                    background: url('./background/编组_12.png');
                                    display: flex;
                                    align-items: center;
                                    justify-content: space-between;
                                    >div:nth-child(1){
                                        color: #5ae8fa;
                                        font-size: 20px;
                                        font-weight: bold;
                                        display: flex;
                                        align-items: center;
                                        text-align: center;
                                        width: 15%;
                                        >span{
                                            display: inline-block;
                                            background: #fcb83c;
                                            width: 10px;
                                            height: 10px;
                                            border-radius: 50%;
                                            margin-right: 10px;
                                        }
                                    }
                                    >div:nth-child(2){
                                        color: #c5eef3;
                                        font-size: 22px;
                                        width: 20%;
                                        text-align: center;
                                    }
                                    >div:nth-child(3){
                                        display: flex;
                                        width: 35%;
                                        align-items: center;
                                        text-align: center;
                                        justify-content: center;
                                        >div:first-child{
                                            font-size: 18px;
                                            color: rgba(197,238,243,1);
                                            margin-right: 7px;
                                        }
                                        >div:last-child{
                                            font-size: 22px;
                                            color: rgba(197,238,243,1);
                                        }
                                    }
                                    >div:nth-child(4){
                                        color:#16DFF8;
                                        font-size: 20px;
                                        width: 80px;
                                        height: 32px;
                                        text-align: center;
                                        line-height: 32px;
                                        border:1px solid rgba(22,223,248,0.6);
                                        border-radius: 16px;
                                        text-align: center;
                                        margin: 0px auto;
                                        cursor: pointer;
                                    }
                                }
                                .info2{
                                    position: absolute;
                                    top: 0;
                                    left: 0;
                                    // z-index: 1;
                                    width: 100%;
                                    height: 100%;
                                    background: radial-gradient(#1c45b0, #2e4879);
                                    display: flex;
                                    align-items: center;
                                    justify-content: space-between;
                                    padding: 0 15px;
                                    >div:nth-child(1) {
                                        >div:first-child {
                                            font-size: 20px;
                                            margin-bottom: 2px;
                                            color: #afcfff;
                                        }
                                        >div:last-child {
                                            font-size: 16px;
                                            color: #afcfff;
                                        }
                                    }
                                    >div:nth-child(2) {
                                        >div:first-child {
                                            font-size: 20px;
                                            color: #ff8787;
                                            margin-bottom: 2px;
                                        }
                                        >div:last-child {
                                            font-size: 16px;
                                            color: #afcfff;
                                        }
                                    }
                                    >div:nth-child(3) {
                                        >div:first-child {
                                            font-size: 20px;
                                            color: #ff8787;
                                            margin-bottom: 2px;
                                        }
                                        >div:last-child {
                                            font-size: 16px;
                                            color: #afcfff;
                                        }
                                    }
                                    >div:nth-child(4) {
                                        font-size: 18px;
                                        color: #afcfff;
                                    }
                                }
                            }
                            >.li:nth-child(1){
                                .info1{
                                    background: url('./background/1.png');
                                    >div:nth-child(1){
                                        color: rgba(245,160,29,1);
                                        >span{
                                        background: rgba(245,160,29,1);
                                        }
                                    }
                                }
                            }
                            >.li:nth-child(2){
                                .info1{
                                    background: url('./background/2.png');
                                    >div:nth-child(1){
                                        color: rgba(176,194,198,1);
                                        >span{
                                        background: rgba(176,194,198,1);
                                        }
                                    }
                                }
                            }
                            >.li:nth-child(3){
                                .info1{
                                    background: url('./background/3.png');
                                    >div:nth-child(1){
                                        color: rgba(219,139,85,1);
                                        >span{
                                        background: rgba(219,139,85,1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            .ConditionalList{
                width: 1481px;
                height: 800px;
                padding: 104px 37px 56px 36px;
                background: url('./background/编组20.png');
                position: relative;
                overflow: hidden;
                .ball{
                    width: 100%;
                    display: flex;
                    flex-wrap: wrap;
                    align-items: center;
                    justify-content: space-between;
                    .normal1{
                        width: 180px;
                        height: 52px;
                        cursor: pointer;
                        color: rgba(22,223,248,1);
                        font-size: 22px;
                        border-radius: 4px;
                        text-align: center;
                        line-height: 52px;
                        margin-bottom: 20px;
                        background: url('./background/矩形_1.png');
                    }
                    .active1{
                        background: url('./background/矩形.png') !important;
                        color: rgba(255,244,223,1) !important;
                    }
                }
                >.title{
                    color: rgba(197,238,243,1);
                    font-size: 26px;
                    margin: 16px 0 20px 0;
                    font-weight: bold;
                }
                .list{
                    width: 100%;
                    .lhead{
                        width: 100%;
                        height: 48px;
                        display: flex;
                        align-items: center;
                        justify-content: space-around;
                        color: rgba(22,223,248,1);
                        font-size: 18px;
                        font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 2.0-Medium;
                        font-weight: 500;
                        text-align: center;
                        background: linear-gradient(270deg,rgba(22,223,248,0.03), rgba(22,223,248,0.06) 50%, rgba(22,223,248,0.03));
                        >div:nth-child(1){
                                width: 10%;
                        }
                        >div:nth-child(2){
                                width: 30%;
                        }
                        >div:nth-child(3){
                                width: 20%;
                        }
                        >div:nth-child(4){
                                width: 5%;
                        }
                    }
                    .lbody{
                        width: 100%;
                        height: 384px;
                        overflow-y: scroll;
                        .rows{
                            width: 100%;
                            height: 48px;
                            display: flex;
                            align-items: center;
                            justify-content: space-around;
                            >div:nth-child(1){
                                font-size: 20px;
                                font-family: TeX Gyre Adventor, TeX Gyre Adventor-Bold;
                                font-weight: 700;
                                text-align: center;
                                color: #d8f4f7;
                                width: 10%;
                            }
                            >div:nth-child(2){
                                font-size: 20px;
                                font-family: TeX Gyre Adventor, TeX Gyre Adventor-Bold;
                                font-weight: 400;
                                text-align: center;
                                color: #d8f4f7;
                                width: 30%;
                            }
                            >div:nth-child(3){
                                font-size: 20px;
                                font-family: TeX Gyre Adventor, TeX Gyre Adventor-Bold;
                                font-weight: 700;
                                text-align: center;
                                color: #d8f4f7;
                                width: 20%;
                            }
                            >div:nth-child(4){
                                width: 5%;
                                font-size: 20px;
                                font-family: Source Han Sans SC, Source Han Sans SC-Regular;
                                font-weight: 400;
                                text-align: center;
                                color: #16dff8;
                            }
                        }
                        .rows:nth-child(even){
                            background: linear-gradient(270deg,rgba(22,223,248,0.03), rgba(22,223,248,0.06) 50%, rgba(22,223,248,0.03));
                        }
                    }
                }
                .pop1{
                        width: calc(100% - 76px);
                        height: 255px;
                        position: absolute;
                        bottom: 34px;
                        left: 38px;
                        display: flex;
                        align-items: center;
                        background: radial-gradient(#1c45b0, #2e4879);
                        justify-content: space-between;
                        padding: 20px;
                        .left{
                            width: 54%;
                            height: 100%;
                            overflow:scroll;
                            font-size: 24px;
                            font-weight: bold;
                            >div{
                                margin-bottom: 30px;
                                .key{
                                    color: #fff;
                                    margin-right: 25px;
                                }
                                .value{
                                    color: #ff8787;
                                }
                            }
                        }
                        .right{
                            width: 45%;
                            height: 100%;
                            color: #fff;
                            .return{
                                font-size: 24px;
                                font-weight: bold;
                                text-align: right;
                                position: absolute;
                                top: 5px;
                                right: 10px;
                            }
                            .title2{
                                margin-bottom: 5px;
                                font-size: 24px;
                                font-weight: bold;
                            }
                            .list2{
                                width: 100%;
                                height: 170px;
                                overflow-y: scroll;
                                font-size: 18px;
                                padding-right: 10px;
                                .li2{
                                    display: flex;
                                    padding: 0px 10px;
                                    align-items: center;
                                    justify-content: space-between;
                                }
                                >.li2:nth-child(odd){
                                    background: rgba(138,176,194,0.3);
                                }
                                >.li2:nth-child(even){
                                    background: rgba(255,133,133,0.3);
                                }
                            }
                        }
                }
                .pop2{
                        width: calc(100% - 76px);
                        height: 255px;
                        position: absolute;
                        bottom: 34px;
                        left: 38px;
                        display: flex;
                        align-items: center;
                        background: radial-gradient(#1c45b0, #2e4879);
                        justify-content: space-between;
                        padding: 20px;
                        .left{
                            width: 44%;
                            height: 100%;
                            overflow:scroll;
                            font-size: 24px;
                            font-weight: bold;
                            >div{
                                margin-bottom: 30px;
                                .key{
                                    color: #fff;
                                    margin-right: 25px;
                                }
                                .value{
                                    color: #ff8787;
                                }
                            }
                        }
                        .right{
                            width: 55%;
                            height: 100%;
                            .return{
                                font-size: 24px;
                                font-weight: bold;
                                text-align: right;
                                position: absolute;
                                top: 5px;
                                right: 10px;
                            }
                            .title2{
                                margin-bottom: 5px;
                                font-size: 24px;
                                font-weight: bold;
                            }
                            .barchart{
                                width: 670px;
                                height: 180px;
                            }
                        }
                }
            }
        }
        .row2{
            width: 100%;
            height: 346px;
            background: url('./background/编组17.png');
            padding: 100px 84px 32px 36px;
            .tabButton{
                width: 200px;
                height: 100%;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: space-around;
                margin-right: 36px;
                .normal2{
                    width: 200px;
                    height: 52px;
                    border-radius: 4px;
                    cursor: pointer;
                    color: rgba(22,223,248,1);
                    font-size: 22px;
                    text-align: center;
                    line-height: 52px;
                    margin-bottom: 20px;
                    background: url('./background/矩形_3.png');
                }
                .active2{
                    background: url('./background/矩形_2.png') !important;
                    color: rgba(255,244,223,1) !important;
                }
            }
            .tabContent{
                width: auto;
                height: auto;
                overflow: hidden;
                position: relative;
                .lunbo1{
                        width: 2436px;
                        height: 214px;
                        overflow-x: scroll;
                        white-space: nowrap;
                        .col{
                            display:inline-flex;
                            width: 280px;
                            height: 100%;
                            background: url('./background/编组_13.png');
                            margin-right: 28px;
                            text-align: center;
                            position: relative;
                            >div:nth-child(1) {
                                font-size: 22px;
                                font-family: TeX Gyre Adventor, TeX Gyre Adventor-Bold;
                                font-weight: 700;
                                color: #ffffff;
                                position: absolute;
                                top: 0px;
                                left: 14px;
                            }
                            >div:nth-child(2) {
                                font-size: 20px;
                                font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 2.0-Regular;
                                font-weight: 400;
                                text-align: right;
                                color: rgba(22,223,248,1);
                                position: absolute;
                                top: 12px;
                                right: 16px;
                            }
                            >div:nth-child(3) {
                                width: 100%;
                                font-size: 24px;
                                font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 2.0-Medium;
                                font-weight: 500;
                                text-align: center;
                                color: #C5EEF3;
                                margin-top:53px;
                                margin-bottom: 37px;
                            }
                            >div:nth-child(4) {
                                width: 280px;
                                height: 100px;
                                background: linear-gradient(270deg,rgba(22,223,248,0.00), rgba(22,223,248,0.10) 50%, rgba(22,223,248,0.00));
                                border: 2px solid rgba(90,232,250,0.04);
                                display: flex;
                                justify-content: space-around;
                                width: 100%;
                                >div{
                                    padding: 15px 20px 20px 20px;
                                    >div:first-child{
                                        >div:first-child{
                                            font-size: 24px;
                                            font-family: TeX Gyre Adventor, TeX Gyre Adventor-Bold;
                                            font-weight: 700;
                                            color: #ff644d;
                                            margin-bottom: 7px;
                                        }
                                        >div:last-child{
                                            font-size: 18px;
                                            font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 2.0-Regular;
                                            font-weight: 400;
                                            color: #c5eef3;
                                        }
                                    }
                                    >div:last-child{
                                        >div:first-child{
                                            font-size: 24px;
                                            font-family: TeX Gyre Adventor, TeX Gyre Adventor-Bold;
                                            font-weight: 700;
                                            color: #5AE8FA;
                                            margin-bottom: 7px;
                                        }
                                        >div:last-child{
                                            font-size: 18px;
                                            font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 2.0-Regular;
                                            font-weight: 400;
                                            color: #c5eef3;
                                        }
                                    }
                                }
                            }
                        }
                }
                .lunbo2,.lunbo3{
                        width: 2436px;
                        height: 214px;
                        overflow-x: scroll;
                        white-space: nowrap;
                        .col{
                            display:inline-flex;
                            width: 280px;
                            height: 100%;
                            background: url('./background/编组_13.png');
                            margin-right: 28px;
                            text-align: center;
                            position: relative;
                            >div:nth-child(1) {
                                font-size: 22px;
                                font-family: TeX Gyre Adventor, TeX Gyre Adventor-Bold;
                                font-weight: 700;
                                color: #ffffff;
                                position: absolute;
                                top: 0px;
                                left: 14px;
                            }
                            >div:nth-child(2) {
                                font-size: 20px;
                                font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 2.0-Regular;
                                font-weight: 400;
                                text-align: right;
                                color: rgba(22,223,248,1);
                                position: absolute;
                                top: 12px;
                                right: 16px;
                            }
                            >div:nth-child(3) {
                                width: 100%;
                                font-size: 24px;
                                font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 2.0-Medium;
                                font-weight: 500;
                                text-align: center;
                                color: #C5EEF3;
                                margin-top:53px;
                                margin-bottom: 37px;
                            }
                            >div:nth-child(4) {
                                width: 280px;
                                height: 100px;
                                background: linear-gradient(270deg,rgba(22,223,248,0.00), rgba(22,223,248,0.10) 50%, rgba(22,223,248,0.00));
                                border: 2px solid rgba(90,232,250,0.04);
                                width: 100%;
                                >div:first-child{
                                    font-size: 24px;
                                    width: 100%;
                                    text-align: center;
                                    font-family: TeX Gyre Adventor, TeX Gyre Adventor-Bold;
                                    font-weight: 700;
                                    color: #ff644d;
                                    margin-bottom: 7px;
                                }
                                >div:last-child{
                                    font-size: 18px;
                                    width: 100%;
                                    text-align: center;
                                    font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 2.0-Regular;
                                    font-weight: 400;
                                    color: #c5eef3;
                                }
                            }
                        }
                }
                .pop1{
                        position: absolute;
                        bottom: 1px;
                        left: 0;
                        width: 2434px;
                        height: 219px;
                        display: flex;
                        align-items: center;
                        background: radial-gradient(#1c45b0, #2e4879);
                        justify-content: space-between;
                        padding: 20px;
                            .left{
                                width: 54%;
                                height: 100%;
                                overflow:scroll;
                                font-size: 24px;
                                font-weight: bold;
                                >div{
                                    >div:first-child{
                                        display: flex;
                                        flex-wrap: wrap;
                                        width: 100%;
                                        justify-content: space-between;
                                        >div{
                                            margin-bottom: 30px;
                                            width: 50%;
                                            .key{
                                                color: #fff;
                                                margin-right: 25px;
                                            }
                                            .value{
                                                color: #ff8787;
                                            }
                                        }
                                    }
                                    .title2{
                                        margin-bottom: 5px;
                                        font-size: 24px;
                                        font-weight: bold;
                                        color: #fff;
                                    }
                                    .list2{
                                        width: 60%;
                                        height: auto;
                                        font-size: 18px;
                                        .li2{
                                            display: flex;
                                            padding: 0px 10px;
                                            align-items: center;
                                            justify-content: space-between;
                                            text-align: center;
                                            >div:nth-child(1){
                                                width: 15%;
                                            }
                                            >div:nth-child(2){
                                                width: 70%;
                                            }
                                            >div:nth-child(3){
                                                width: 15%;
                                            }
                                        }
                                        >.li2:nth-child(odd){
                                            background: rgba(138,176,194,0.3);
                                        }
                                        >.li2:nth-child(even){
                                            background: rgba(255,133,133,0.3);
                                        }
                                    }
                                    .eline{
                                    }
                                }
                            }
                            .right{
                                width: 45%;
                                height: 100%;
                                color: #fff;
                                .return{
                                    font-size: 24px;
                                    font-weight: bold;
                                    text-align: right;
                                    position: absolute;
                                    top: 5px;
                                    right: 10px;
                                }
                                .title2{
                                    margin-bottom: 5px;
                                    font-size: 24px;
                                    font-weight: bold;
                                }
                                .list2{
                                    width: 100%;
                                    height: 120px;
                                    overflow-y: scroll;
                                    font-size: 18px;
                                    padding-right: 10px;
                                    .li2{
                                        display: flex;
                                        padding: 0px 10px;
                                        align-items: center;
                                        justify-content: space-between;
                                    }
                                    >.li2:nth-child(odd){
                                        background: rgba(138,176,194,0.3);
                                    }
                                    >.li2:nth-child(even){
                                        background: rgba(255,133,133,0.3);
                                    }
                                }
                            }
                }
                .pop2{
                        position: absolute;
                        bottom: 1px;
                        left: 0;
                        width: 2434px;
                        height: 219px;
                        background: radial-gradient(#1c45b0, #2e4879);
                        padding: 20px;
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        padding: 20px;
                        .left{
                            width: 60%;
                            height: 100%;
                            overflow:scroll;
                            font-size: 24px;
                            font-weight: bold;
                            >div{
                                margin-bottom: 30px;
                                .key{
                                    color: #fff;
                                    margin-right: 25px;
                                }
                                .value{
                                    color: #ff8787;
                                }
                            }
                        }
                        .right{
                            width: 40%;
                            height: 100%;
                            .return{
                                font-size: 24px;
                                font-weight: bold;
                                text-align: right;
                                position: absolute;
                                top: 5px;
                                right: 10px;
                            }
                            .title2{
                                margin-bottom: 5px;
                                font-size: 24px;
                                font-weight: bold;
                            }
                            .barchart{
                                width: 670px;
                                height: 180px;
                            }
                        }
                }
            }
        }
        .row3{
            width: 100%;
            height: auto;
            padding-left: 54px;
            .title{
                color: #fff;
                font-size: 20px;
                margin: 20px 0px;
            }
            >div:last-child{
                width: 2434px;
                height: 267px;
                position: relative;
                overflow: hidden;
                .lunbo{
                    width: 100%;
                    height: 100%;
                    overflow-x: scroll;
                    white-space: nowrap;
                    .col{
                        width: 300px;
                        height: 218px;
                        margin-right: 20px;
                        // padding: 10px;
                        display: inline-flex;
                        flex-wrap: wrap;
                        flex-direction: column;
                        // align-items: center;
                        justify-content: space-around;
                        position: relative;
                        background: linear-gradient(to top right,rgba(145,181,245,0.65),rgba(12,91,234,0.32));
                        >div:nth-child(1) {
                            color: #fff;
                            font-size: 18px;
                            width: 100%;
                            font-weight: bold;
                        }
                        >div:nth-child(2) {
                            width: 100%;
                            display: flex;
                            align-items: center;
                            justify-content: space-between;
                            >span:nth-child(1){
                                color: #fff;
                                font-size: 50px;
                                font-weight: bold;
                            }
                            >span:nth-child(2) {
                                color: #1e98d7;
                                font-size: 30px;
                            }
                        }
                        >img{
                            position: absolute;
                            top:1px;
                            right: 0;
                            width: 115px;
                            height:115px;
                        }
                    }
                }
                .pop1{
                    width: 100%;
                    height: 100%;
                    position: absolute;
                    top: -0;
                    left: 0;
                    display: flex;
                    padding: 20px;
                    background: #0E2973;
                    .return{
                        font-size: 24px;
                        font-weight: bold;
                        text-align: right;
                        position: absolute;
                        top: 5px;
                        right: 10px;
                        color:#fff;
                    }
                    .column1{
                        width: 300px;
                        height: 100%;
                        margin-right: 40px;
                        >div:first-child{
                            font-size: 24px;
                            font-weight: bold;
                            color:#fff;
                            margin-bottom: 5px;
                        }
                        .list1{
                            width: 100%;
                            height: 84%;
                            overflow-y: scroll;
                            color: #fff;
                            .li1{
                                height: 30px;
                                width: 99%;
                                margin-bottom: 10px;
                                background: radial-gradient(#C280FF, #81D3F8);
                                font-size: 20px;
                                padding: 0px 10px;
                                display: flex;
                                align-items: center;
                                justify-content: space-between;
                                .prop2{
                                    color: #FF8787;
                                }
                            }
                        }
                    }
                    .column2{
                        width: 300px;
                        height: 100%;
                        margin-right: 40px;
                        >div:first-child{
                            font-size: 24px;
                            font-weight: bold;
                            color:#fff;
                            margin-bottom: 5px;
                            >span:nth-child(2){
                                color:#ff8787;
                            }
                        }
                        >div:last-child{
                            width: 100%;
                            height: 84%;
                            overflow: hidden;
                            position: relative;
                            .list2{
                                width: 100%;
                                height: 100%;
                                overflow-y: scroll;
                                .li2{
                                    height: 30px;
                                    width: 99%;
                                    font-size: 20px;
                                    padding: 0px 10px;
                                    display: flex;
                                    align-items: center;
                                    color:#fff;
                                    text-align: center;
                                    justify-content: space-between;
                                    .prop1{
                                        width: 40%;
                                    }
                                    .prop2{
                                        width: 40%;
                                    }
                                    .prop3{
                                        width: 20%;
                                    }
                                }
                                >.li2:nth-child(odd){
                                    background: radial-gradient(#C280FF, #81D3F8);
                                }
                                >.li2:nth-child(even){
                                    // background: radial-gradient(#C280FF, #81D3F8);
                                }
                            }
                            .pop2{
                                width: 100%;
                                height: 100%;
                                padding: 10px;
                                position: absolute;
                                top: 0;
                                left: 0;
                                overflow:scroll;
                                font-size: 24px;
                                font-weight: bold;
                                background: radial-gradient(#1c45b0, #2e4879);
                                .return{
                                    font-size: 24px;
                                    font-weight: bold;
                                    text-align: right;
                                    position: absolute;
                                    top: 5px;
                                    right: 10px;
                                    color:#fff;
                                }
                                >div{
                                    margin-bottom: 20px;
                                    .key{
                                        color: #fff;
                                        margin-right: 25px;
                                    }
                                    .value{
                                        color: #ff8787;
                                    }
                                }
                            }
                        }
                    }
                    .column3{
                        width: 300px;
                        height: 100%;
                        margin-right: 40px;
                        >div:first-child{
                            font-size: 24px;
                            font-weight: bold;
                            color:#fff;
                            margin-bottom: 5px;
                            >span:nth-child(2){
                                color:#ff8787;
                                margin-left:5px;
                                margin-right: 5px;
                            }
                            >span:nth-child(3){
                                color:#afcfff;
                            }
                        }
                        >div:last-child{
                            height: 84%;
                            overflow: hidden;
                            .list3{
                                width: 100%;
                                height: 100%;
                                overflow-y: scroll;
                                .li3{
                                    height: 30px;
                                    width: 99%;
                                    font-size: 20px;
                                    padding: 0px 10px;
                                    display: flex;
                                    align-items: center;
                                    color:#fff;
                                    background: radial-gradient(#C280FF, #81D3F8);
                                    margin-bottom: 10px;
                                    justify-content: space-between;
                                }
                            }
                        }
                    }
                    .column4{
                        width: 300px;
                        height: 100%;
                        margin-right: 40px;
                        >div:first-child{
                            font-size: 24px;
                            font-weight: bold;
                            color:#fff;
                            margin-bottom: 5px;
                            >span:nth-child(2){
                                color:#ff8787;
                                margin-left:5px;
                            }
                        }
                    }
                    .column5{
                        width: 400px;
                        height: 100%;
                        >div:first-child{
                            font-size: 24px;
                            font-weight: bold;
                            color:#fff;
                            margin-bottom: 5px;
                        }
                        .eline{
                            width: auto;
                            height: auto;
                        }
                    }
                }
            }
        }
    }
    #Module5{
        width: 667px;
        height: 1620px;
        padding: 30px;
        .title{
            width: 100%;
            border-left:17px solid #81d3f8;
            color: #4182ff;
            font-size: 28px;
            padding-left: 15px;
            margin-bottom: 20px;
        }
    }
</style>
<style lang="scss">
.WuhouSinglePage{
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
}
</style>
