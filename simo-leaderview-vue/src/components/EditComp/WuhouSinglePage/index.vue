<template>
    <div class="WuhouSinglePage">
        <transition name="moveTop" >
          <div class="topPart"  v-show="isopenShow">
            <img src="./newBack/6.png" alt="">
            <!-- <div class="Title">
              <img src="./newBack/5.png" alt="">
            </div> -->
          </div>
        </transition>
        <transition name="moveRight" >
            <div class="leftPart"  v-show="isopenShow">
                <div id="Module1">
                    <div class="content">
                        <div class="title"><img src="./background/编组.png" alt=""></div>
                        <div>
                            <div class="TOP3">
                                <div class="ball" @click="showPersonDetails(1)">
                                    <div>{{gsqryList[1]?(isVerification?gsqryList[1]['姓名']:(gsqryList[1]['姓名'].slice(0,1)+'**')): '暂无数据'}}</div>
                                    <div>{{gsqryList[1]?gsqryList[1]['次数']: '暂无数据'}}</div>
                                    <div>详情</div>
                                </div>
                                <div class="ball" @click="showPersonDetails(0)">
                                    <div>{{gsqryList[0]?(isVerification?gsqryList[0]['姓名']:(gsqryList[0]['姓名'].slice(0,1)+'**')): '暂无数据'}}</div>
                                    <div>{{gsqryList[0]?gsqryList[0]['次数']: '暂无数据'}}</div>
                                    <div>详情</div>
                                </div>
                                <div class="ball" @click="showPersonDetails(2)">
                                    <div>{{gsqryList[2]?(isVerification?gsqryList[2]['姓名']:(gsqryList[2]['姓名'].slice(0,1)+'**')): '暂无数据'}}</div>
                                    <div>{{gsqryList[2]?gsqryList[2]['次数']: '暂无数据'}}</div>
                                    <div>详情</div>
                                </div>
                            </div>
                            <div class="list" v-if="gsqryList">
                                <div class="rows" v-for="(data,index) in gsqryList" :key="index" v-show="index > 2">
                                    <div class="col1"><span></span>{{9>index?'0' + (index + 1):(index + 1)}}</div>
                                    <div class="col2">{{isVerification?data['姓名']:(data['姓名'].slice(0,1)+'**')}}</div>
                                    <div class="col3">{{data['次数']}}</div>
                                    <div class="col4" @click="showPersonDetails(index)">详情</div>
                                </div>
                            </div>
                            <transition name="moveRight">
                                <div class="firstPop" v-show="showRYXX">
                                    <div class="title">
                                        <div>高诉求人员</div>
                                        <div @click="showRYXX = false"><img src="./background/关闭.png" alt=""></div>
                                    </div>
                                    <div v-if="gsqryDetail['姓名']">
                                        <div>{{isVerification?gsqryDetail['姓名']:(gsqryDetail['姓名'].slice(0,1)+'**')}}</div>
                                        <div>
                                            <img src="./background/电话.png" alt="">
                                            <span>{{isVerification?gsqryDetail['电话']:(gsqryDetail['电话'].slice(0,3)+'********')}}</span>
                                        </div>
                                        <div>
                                            <img src="./background/定位.png" alt="">
                                            <span>{{isVerification?gsqryDetail['地址']:(gsqryDetail['地址'].slice(0,5)+'**********')}}</span>
                                        </div>
                                        <div>
                                            <div>
                                                <div><span style="color:#FCB83C;font-size:24px;">{{gsqryDetail['投诉频率']}}</span>次/天</div>
                                                <div>投诉频率</div>
                                            </div>
                                            <div>
                                                <div style="color:#5AE8FA;font-size:22px;">{{gsqryDetail['小区名称']}}</div>
                                                <div>风险微网格</div>
                                            </div>
                                        </div>
                                        <div>风险业务情况</div>
                                        <div>
                                            <IntegratedHistogram @onclickFun='clickFunBar' :item="getRiskBusiness"></IntegratedHistogram>
                                        </div>
                                    </div>
                                </div>
                            </transition>
                            <transition name="moveRight">
                              <div class="xqTableBox" v-if="showTableBox">
                                  <div class="title">
                                      <div>高诉求人员</div>
                                      <div @click="showTableBox = false"><img src="./background/关闭.png" alt=""></div>
                                  </div>
                                  <div class="cityEvent" ref="cityEvent">
                                      <ul class="item" ref="item">
                                          <li v-for="(val, ind) in grtsxqData" :key="ind">
                                          <div  class="eventBox" >
                                              <div>
                                                  <div><span></span>{{val['工单主题']}}</div>
                                                  <div @click="ShowXqTableDetails(val)">详情</div>
                                              </div>
                                              <div>
                                                  {{val['工单内容']}}
                                              </div>
                                              <div>
                                                  {{val['诉求时间']}}
                                              </div>
                                          </div>
                                          </li>
                                      </ul>
                                  </div>
                                  <transition name="moveLeft">
                                      <div class="Module5Pop" v-show="showTableDetails">
                                        <div style="height: 76px; display: flex;justify-content: space-between; align-items: center;background-image: linear-gradient(45deg, hsl(187deg 94% 53% / 10%), rgb(22 223 248 / 2%))">
                                            <span style="font-size: 30px;margin-left: 24px;display: flex;align-items: center;color: #5AE8FA;font-weight: 600;">高诉求详情</span>
                                            <img style="height: 49px;width: 49px;margin-right: 20px;cursor: pointer;" @click="CloseXqTableDetails" src="./background/关闭.png" alt="">
                                        </div>
                                        <div style="with:100%;overflow: auto;height:calc(100% - 80px)">
                                          <div  class="bgck12 dataCenter"  style="position: relative;width: 180px;height: 50px;margin: 20px 30px;">
                                            <div class="dataCenter" style="width:100%;cursor: pointer;height:100%;font-size: 28px;color: #0B1B2A;" @click="OpenShowTjdbDetails1">
                                              提级督办
                                            </div>
                                          </div>
                                          <div class="tjdbBox" v-show="showTjdbDetails1">
                                              <div class="titleName">
                                                <div class="Name" style="color:#5AE8FA;font-size:30px">请选择部门</div>
                                                <img style="height: 49px;width: 49px;cursor: pointer;" @click="showTjdbDetails1 = false" src="./background/关闭.png" alt="">
                                              </div>
                                              <div class="bodyChose">
                                                <Tree :data="treeSetList1"
                                                  :load-data="loadData"
                                                  @on-select-change='ChangeSelect1'></Tree>
                                              </div>
                                              <div class="footBox">
                                                <div class="Name" style="color:#C5EEF3;font-size:30px">{{CkeckedBm1===''?'请选择部门':CkeckedBm1}}</div>
                                                <div class="SureBtn dataCenter" @click="UpDataOk1(XqTableValue)">确定</div>
                                              </div>
                                          </div>
                                          <div style="margin: 26px;display: flex;font-size: 28px;color: #C5EEF3;overflow: auto;">
                                            <div class="name" style="width:145px">
                                              工单编号:
                                            </div>
                                            <div class='value' style="width:420px">
                                              {{XqTableValue['工单编号']}}
                                            </div>
                                          </div>
                                          <div style="margin: 26px;display: flex;font-size: 28px;color: #C5EEF3;overflow: auto;" v-show="testKey(key)" v-for="(data,key,index) in XqTableValue" :key="index">
                                            <div class="name" style="width:145px">
                                              {{key}}:
                                            </div>
                                            <div class='value' style="width:420px">
                                              {{data}}
                                            </div>
                                          </div>
                                        </div>
                                      </div>
                                  </transition>
                              </div>
                            </transition>
                        </div>
                    </div>
                </div>
                <div id="Module2">
                    <div class="row1">
                        <div class="selectTitle">
                          <Input
                            v-model="SqTipsName"
                            style="width:270px"
                            placeholder="输入授权码获取全部信息"
                          />
                          <div class="search" @click="searchData"></div>
                        </div>
                        <div class="listArr">
                            <div class="Abox">
                                <div class="leftList">
                                    <div>7天内</div>
                                    <div class="list">
                                        <div class="li" v-for="(data, index) in kszzry7List" :key="index">
                                            <div class="info1">
                                                <div><span></span>{{index + 1}}</div>
                                                <div>{{isVerification?data['姓名']:(data['姓名'].slice(0,1)+'**')}}</div>
                                                <div>
                                                    <div>次数 </div>
                                                    <div> {{data['次数']}}</div>
                                                </div>
                                                <div @click="showKSZZRY7(index)">
                                                    详情
                                                </div>
                                            </div>
                                            <transition name="moveRight">
                                                <div class="info2" v-show="indexOf7 === index">
                                                    <div>
                                                        <div>{{data['个人投诉类别最多类']?data['个人投诉类别最多类']['rows'][0]['类别']:'暂无数据'}}</div>
                                                        <div>TOP1</div>
                                                    </div>
                                                    <div>
                                                        <div>{{data['增长']}}</div>
                                                        <div>环比增长次数</div>
                                                    </div>
                                                    <div>
                                                        <div>{{data['增速']}}%</div>
                                                        <div>投诉增速</div>
                                                    </div>
                                                    <div @click="indexOf7 = -1"><img src="./background/关闭.png" alt=""></div>
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
                                                <div>{{ isVerification?data['姓名']:(data['姓名'].slice(0,1)+'**')}}</div>
                                                <div>
                                                    <div>次数 </div>
                                                    <div>{{data['次数']}}</div>
                                                </div>
                                                <div @click="showKSZZRY15(index)">
                                                    详情
                                                </div>
                                            </div>
                                            <transition name="moveRight">
                                                <div class="info2" v-show="indexOf15 === index">
                                                    <div>
                                                        <div>{{data['个人投诉类别top3']?data['个人投诉类别top3']['rows'][0]['类别']:'暂无数据'}}</div>
                                                        <div>TOP1</div>
                                                    </div>
                                                    <div>
                                                        <div>{{data['环比增长']}}</div>
                                                        <div>环比增长次数</div>
                                                    </div>
                                                    <div>
                                                        <div>{{data['投诉频率']}}%</div>
                                                        <div>投诉增速</div>
                                                    </div>
                                                    <div @click="indexOf15 = -1"><img src="./background/关闭.png" alt=""></div>
                                                </div>
                                            </transition>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <transition name="moveRight">
                              <div class="sqTableBox" v-if="showsqTableBox">
                                  <div class="title">
                                      <div>诉求情况</div>
                                      <div @click="showsqTableBox = false"><img src="./background/关闭.png" alt=""></div>
                                  </div>
                                  <div class="cityEvent" ref="cityEvent">
                                      <ul class="item" ref="item">
                                          <li v-for="(val, ind) in grtxqData" :key="ind">
                                          <div  class="eventBox" >
                                              <div>
                                                  <div><span></span>{{val['工单主题']}}</div>
                                                  <div @click="ShowzZzqzTableDetails(val)">详情</div>
                                              </div>
                                              <div>
                                                  {{val['工单内容']}}
                                              </div>
                                              <div>
                                                  {{val['诉求时间']}}
                                              </div>
                                          </div>
                                          </li>
                                      </ul>
                                  </div>
                                  <transition name="moveLeft">
                                      <div class="Module5Pop" v-show="zzqzTableDetails">
                                        <div style="height: 76px; display: flex;justify-content: space-between; align-items: center;background-image: linear-gradient(45deg, hsl(187deg 94% 53% / 10%), rgb(22 223 248 / 2%))">
                                            <span style="font-size: 30px;margin-left: 24px;display: flex;align-items: center;color: #5AE8FA;font-weight: 600;">高诉求详情</span>
                                            <img style="height: 49px;width: 49px;margin-right: 20px;cursor: pointer;" @click="zzqzTableDetails=false" src="./background/关闭.png" alt="">
                                        </div>
                                        <div style="with:100%;overflow: auto;height:calc(100% - 80px)">
                                          <div style="margin: 26px;display: flex;font-size: 28px;color: #C5EEF3;overflow: auto;" v-for="(data,key,index) in ZzqzTableValue" :key="index">
                                            <div class="name" style="width:8%">
                                              {{key}}:
                                            </div>
                                            <div class='value' style="width:92%">
                                              {{data}}
                                            </div>
                                          </div>
                                        </div>
                                      </div>
                                  </transition>
                              </div>
                            </transition>
                        </div>
                        <div class="ConditionalList">
                            <div class="selectBox">
                              <div class="select-btn" :class="{'active-btn': appealType == data}" v-for="(data, index) in getBubble" :key="index" @click="appealType = data">{{ data }}</div>
                            </div>
                            <div class="title">
                              {{appealType}}诉求情况
                            </div>
                            <div>
                                <div class="list">
                                    <div class="lhead">
                                        <div>诉求数量</div>
                                        <div>查看</div>
                                    </div>
                                    <div class="lbody">
                                        <div class="rows">
                                            <div>{{sqxqphSum}}</div>
                                            <div>
                                              <div @click="ShowGZFF1">详情</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row2">
                        <div class="tabButton">
                            <div :class="{normal2: true,active2: gsqxq === '矛盾突出小区'}" @click="changeGSQXQ('矛盾突出小区')">矛盾突出小区</div>
                            <div :class="{normal2: true,active2: gsqxq === '高群体诉求小区'}" @click="changeGSQXQ('高群体诉求小区')">高群体诉求小区</div>
                            <div :class="{normal2: true,active2: gsqxq === '诉求快速增长小区'}" @click="changeGSQXQ('诉求快速增长小区')">诉求快速增长小区</div>
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
                                            <div>{{9 > index?'0' + (index + 1):(index + 1)}}</div>
                                            <div @click="ShowMDTCXQ(index)">详情</div>
                                            <div>{{data['小区']}}</div>
                                            <div>
                                                <div>{{data['数量']}}</div>
                                                <div>全年投诉涉及人数</div>
                                            </div>
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
                        </div>
                    </div>
                    <div class="row3">
                        <div>
                          <div style="font-size: 60px;">
                            {{GetMSSQ7[0]?GetMSSQ7[0]['街道评级']||GetMSSQ7[0]['社区评级']:''}}
                          </div>
                          <div>
                            {{GetMSSQ7[0]?GetMSSQ7[0]['街道投诉指数均值']:''}}
                          </div>
                        </div>
                        <div>
                            <div class="lunbo" ref="lunbo" @mousewheel="onMouseWheel($event, 'lunbo')">
                                <div class="col" v-for="(data, index) in GetMSSQ7[0]?GetMSSQ7[0]['街道指数列表'].rows:[]" :key="index">
                                    <div><img :src="getSreetImg(data['街道'])" alt=""></div>
                                    <div>{{data['街道']}}</div>
                                    <div>
                                        <div style="display: flex;justify-content: center;align-items: center;">
                                            <span style="color:#5abf5a;font-size:50px;vertical-align:text-bottom;margin-right: 10px;">优</span>
                                            <!-- {{data['街道评级']}} -->
                                            {{data['指数']}}
                                        </div>
                                        <div @click="ShowQMSSQ(index)">详情</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                      <transition name="moveRight">
                                  <div class="popp1" v-show="showMDTCXQ">
                                          <div class="return" @click="CloseMDTCXQ"><img src="./background/关闭.png" alt=""></div>
                                          <div class="content1" v-show="gsqxq === '矛盾突出小区'">
                                              <div class="con1">
                                                <div class="info">
                                                  <div>
                                                      <div class="field">
                                                          <div>{{gsqxqDetail['街道']}}</div>
                                                          <div>所属街道</div>
                                                      </div>
                                                      <div class="field">
                                                          <div>{{gsqxqDetail['地址']?(isVerification?gsqxqDetail['地址']:(gsqxqDetail['地址'].slice(0,5)+'**********')):''}}</div>
                                                          <div>小区地址</div>
                                                      </div>
                                                  </div>
                                                  <div>
                                                      <div>{{gsqxqDetail['本月投诉环比上月增长率']}}%</div>
                                                      <div class="title2">本月投诉环比变动情况</div>
                                                  </div>
                                                </div>
                                                <div>
                                                    <div class="title2">月度投诉量变动情况</div>
                                                    <ELine :item="getYDTSL"></ELine>
                                                </div>
                                              </div>
                                              <div>
                                                  <div class="title2">风险分类排名</div>
                                                  <div class="list2">
                                                      <div class="li2" @click="showgsqxqTable(data)"  v-show="gsqxqDetail['全年该小区投诉类别top5']" v-for="(data, index) in gsqxqDetail['全年该小区投诉类别top5']?gsqxqDetail['全年该小区投诉类别top5'].rows:[]" :key="index">
                                                          <div>{{index + 1}}</div>
                                                          <div>{{data['类别'] || data['小区']}}</div>
                                                          <div>{{data['数量']}}</div>
                                                      </div>
                                                  </div>
                                              </div>
                                              <div>
                                                  <div class="title2">高风险人员<span style="color:#FFB83F;font-size:30px;font-weight:bold;">{{gsqxqDetail['高风险人员']?gsqxqDetail['高风险人员'].rows.length:0}}</span></div>
                                                  <div class="list2">
                                                      <div class="li2" v-for="(data, index) in gsqxqDetail['高风险人员']?gsqxqDetail['高风险人员'].rows:[]" :key="index">
                                                          <div>{{data['姓名']?(isVerification?data['姓名']:(data['姓名'].slice(0,1)+'**')):''}}</div>
                                                          <div>{{data['电话']?(isVerification?data['电话']:(data['电话'].slice(0,3)+'********')):''}}</div>
                                                          <div @click="ShowGFXRYQD(data['电话'])">详情</div>
                                                      </div>
                                                  </div>
                                              </div>
                                          </div>
                                          <div class="content1" v-show="gsqxq === '高群体诉求小区'">
                                              <div class="con1">
                                                <div class="info">
                                                  <div>
                                                      <div class="field">
                                                          <div>{{gsqxqDetail['街道']}}</div>
                                                          <div>所属街道</div>
                                                      </div>
                                                      <div class="field">
                                                          <div>{{gsqxqDetail['地址']?(isVerification?gsqxqDetail['地址']:(gsqxqDetail['地址'].slice(0,5)+'**********')):''}}</div>
                                                          <div>小区地址</div>
                                                      </div>
                                                  </div>
                                                  <div>
                                                      <div>{{gsqxqDetail['本月人数环比上月增长率']}}%</div>
                                                      <div class="title2">本月投诉环比变动情况</div>
                                                  </div>
                                                </div>
                                                <div>
                                                    <div class="title2">月度投诉量变动情况</div>
                                                    <ELine :item="getYDTSL"></ELine>
                                                </div>
                                              </div>
                                              <div>
                                                  <div class="title2">风险分类排名</div>
                                                  <div class="list2">
                                                      <div @click="showgsqxqTable(data)" class="li2" v-show="gsqxqDetail['全年该小区投诉类别top5']" v-for="(data, index) in gsqxqDetail['全年该小区投诉类别top5']?gsqxqDetail['全年该小区投诉类别top5'].rows:[]" :key="index">
                                                          <div>{{index + 1}}</div>
                                                          <div>{{data['名称']}}</div>
                                                          <div>{{data['数量']}}</div>
                                                      </div>
                                                  </div>
                                              </div>
                                              <div>
                                                  <div class="title2">高风险人员<span style="color:#FFB83F;font-size:30px;font-weight:bold;">{{gsqxqDetail['高风险人员']?gsqxqDetail['高风险人员'].rows.length:0}}</span></div>
                                                  <div class="list2">
                                                      <div class="li2" v-for="(data, index) in gsqxqDetail['高风险人员']?gsqxqDetail['高风险人员'].rows:[]" :key="index">
                                                          <div>{{data['姓名']?(isVerification?data['姓名']:(data['姓名'].slice(0,1)+'**')):''}}</div>
                                                          <div>{{data['姓名']?(isVerification?data['电话']:(data['电话'].slice(0,3)+'********')):''}}</div>
                                                          <div @click="ShowGFXRYQD(data['电话'])">详情</div>
                                                      </div>
                                                  </div>
                                              </div>
                                          </div>
                                          <div class="content2" v-show="gsqxq === '诉求快速增长小区'">
                                              <div>
                                                  <div class="field">
                                                      <div class="key">所属街道</div>
                                                      <div class="value">{{gsqxqDetail['街道']}}</div>
                                                  </div>
                                                  <div class="field">
                                                      <div class="key">小区地址</div>
                                                      <div class="value">{{gsqxqDetail['地址']}}</div>
                                                  </div>
                                                  <div class="field">
                                                      <div class="key">15日内环比增长数</div>
                                                      <div class="value">{{gsqxqDetail['近15日环比增长数']}}</div>
                                                  </div>
                                                  <div class="field">
                                                      <div class="key">近15日投诉总人数</div>
                                                      <div class="value">{{gsqxqDetail['近15日投诉总人数']}}</div>
                                                  </div>
                                                  <div class="field">
                                                      <div class="key">高风险人员数</div>
                                                      <div class="value">{{gsqxqDetail['高风险人员']?gsqxqDetail['高风险人员'].length:0}}</div>
                                                  </div>
                                                  <div class="field">
                                                      <div class="key">近15日风险增速</div>
                                                      <div class="value">{{gsqxqDetail['近15日增速']}}</div>
                                                  </div>
                                              </div>
                                              <div>
                                                  <div class="title2">风险分类排名</div>
                                                  <div class="list2">
                                                      <div class="li2" @click="showgsqxqTable(data)"  v-for="(data, index) in gsqxqDetail['投诉类别数量']?gsqxqDetail['投诉类别数量'].rows:[]" :key="index">
                                                          <div>{{index + 1}}</div>
                                                          <div>{{data['小区']}}</div>
                                                          <div>{{data['次数']}}</div>
                                                      </div>
                                                  </div>
                                              </div>
                                              <div>
                                                  <div class="title2">高风险人员<span style="color:#FFB83F;font-size:30px;font-weight:bold;">{{gsqxqDetail['高风险人员']?gsqxqDetail['高风险人员'].rows.length:0}}</span></div>
                                                  <div class="list2">
                                                      <div class="li2" v-for="(data, index) in gsqxqDetail['高风险人员']?gsqxqDetail['高风险人员'].rows:[]" :key="index">
                                                          <div>{{data['姓名']?(isVerification?data['姓名']:(data['姓名'].slice(0,1)+'**')):''}}</div>
                                                          <div>{{data['电话']?(isVerification?data['电话']:(data['电话'].slice(0,3)+'********')):''}}</div>
                                                          <div @click="ShowGFXRYQD(data['电话'])">详情</div>
                                                      </div>
                                                  </div>
                                              </div>
                                          </div>
                                  </div>
                      </transition>
                      <transition name="moveTop">
                                  <div class="popp2" v-show="showGFXRYQD">
                                      <div class="return"><img @click="CloseGFXRYQD" src="./background/关闭.png" alt=""></div>
                                      <div>
                                          <div class="left">
                                              <div><span class="key">人员姓名</span><span class="value">
                                                {{gfxryDetail2['姓名']?(isVerification?gfxryDetail2['姓名']:(gfxryDetail2['姓名'].slice(0,1)+'**')):''}}
                                              </span></div>
                                              <div><span class="key">联系方式</span><span class="value">
                                                {{gfxryDetail2['电话']?(isVerification?gfxryDetail2['电话']:(gfxryDetail2['电话'].slice(0,3)+'********')):''}}
                                              </span></div>
                                              <div><span class="key">家庭住址</span><span class="value">
                                                {{gfxryDetail2['地址']?(isVerification?gfxryDetail2['地址']:(gfxryDetail2['地址'].slice(0,5)+'*********')):''}}
                                              </span></div>
                                              <div><span class="key">投诉频率</span><span class="value">{{gfxryDetail2['投诉频率']}}次/天</span></div>
                                              <div><span class="key">风险微网格</span><span class="value">{{gfxryDetail2['小区名称']}}</span></div>
                                          </div>
                                          <div class="right">
                                              <div class="title2">风险业务情况</div>
                                              <div class="barchart">
                                                  <IntegratedHistogram @onclickFun='clickFunBar1' :item="getFXYWQK2"></IntegratedHistogram>
                                              </div>
                                          </div>
                                      </div>
                                  </div>
                      </transition>

                            <transition name="moveRight">
                                <div class="poppp1" v-show="showQMSSQ">
                                    <div class="return" @click="CloseQMSSQ"><img src="./background/关闭.png" alt=""></div>
                                    <div class="column1">
                                        <div class="title2">风险分类</div>
                                        <div class="list1">
                                            <div class="li1" v-for="(data, index) in qmssqDetail['全年投诉类别数量']?qmssqDetail['全年投诉类别数量'].rows:[]" :key="index">
                                                <div>{{index + 1}}</div>
                                                <div>{{data['小区']}}</div>
                                                <div>{{data['数量']}}</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="column2">
                                        <div class="title2">
                                            高风险人员数
                                            <span>{{qmssqDetail['高风险人员']?qmssqDetail['高风险人员']['rows'].length:0}}</span>
                                        </div>
                                            <div class="list2">
                                                <div class="li2" v-for="(data, index) in qmssqDetail['高风险人员']?qmssqDetail['高风险人员'].rows:[]" :key="index">
                                                    <div class="prop1">{{data['姓名']?(isVerification?data['姓名']:(data['姓名'].slice(0,1)+'**')):''}}</div>
                                                    <div class="prop2">{{data['电话']?(isVerification?data['电话']:(data['电话'].slice(0,3)+'********')):''}}</div>
                                                    <div class="prop3" @click="ShowGFXRYS(data['电话'])">详情</div>
                                                </div>
                                            </div>
                                            <transition name="moveTop">
                                                <div class="pop2" v-show="showGFXRYS">
                                                    <div class="return" @click="CloseGFXRYS"><img src="./background/关闭.png" alt=""></div>
                                                    <div><span class="key">人员姓名</span><span class="value">
                                                      {{gfxryDetail3['姓名']?(isVerification?gfxryDetail3['姓名']:(gfxryDetail3['姓名'].slice(0,1)+'**')):''}}
                                                    </span></div>
                                                    <div><span class="key">联系方式</span><span class="value">
                                                      {{gfxryDetail3['电话']?(isVerification?gfxryDetail3['电话']:(gfxryDetail3['电话'].slice(0,3)+'*********')):''}}
                                                    </span></div>
                                                    <div><span class="key">家庭住址</span><span class="value">
                                                      {{gfxryDetail3['地址']?(isVerification?gfxryDetail3['地址']:(gfxryDetail3['地址'].slice(0,5)+'***********')):''}}
                                                    </span></div>
                                                    <div><span class="key">投诉频率</span><span class="value">{{gfxryDetail3['投诉频率']}}次/天</span></div>
                                                    <div><span class="key">风险微网格</span><span class="value">{{gfxryDetail3['小区名称']}}</span></div>
                                                </div>
                                            </transition>
                                    </div>
                                    <div class="column3">
                                        <div class="title2">
                                            高矛盾值小区数
                                            <span style="font-size:30px;">{{qmssqDetail['高矛盾小区']?qmssqDetail['高矛盾小区']['rows'].length:0}}</span>
                                            <!-- <span @click="showGMDZXQ = !showGMDZXQ">详情</span> -->
                                        </div>
                                        <div>
                                            <transition name="moveBottom">
                                                <div class="list3">
                                                    <div class="li3" v-for="(data, index) in qmssqDetail['高矛盾小区']?qmssqDetail['高矛盾小区'].rows:[]" :key="index">
                                                        <div class="prop1">{{data['小区']}}</div>
                                                        <div class="prop2">{{data['数量']}}</div>
                                                    </div>
                                                </div>
                                            </transition>
                                        </div>
                                    </div>
                                    <div class="column4">
                                        <div>{{qmssqDetail['近15日环比增长数']}}</div>
                                        <div class="title2">近15日环比增长数</div>
                                    </div>
                                    <div class="column5">
                                        <div class="title2">近15日投诉增长情况</div>
                                        <div class="eline">
                                            <ELine :item="getTSZZQK15"></ELine>
                                        </div>
                                    </div>
                                </div>
                            </transition>
                            <transition name="moveRight">
                              <div class="gsqxqTableBox" v-if="showgsqxqTableBox">
                                  <div class="title">
                                      <div>风险分类</div>
                                      <div @click="showgsqxqTableBox = false"><img src="./background/关闭.png" alt=""></div>
                                  </div>
                                  <div class="cityEvent" ref="cityEvent">
                                      <ul class="item" ref="item">
                                          <li v-for="(val, ind) in grtgsqxqData" :key="ind">
                                          <div  class="eventBox" >
                                              <div>
                                                  <div><span></span>{{val['工单主题']}}</div>
                                                  <div @click="ShowgsqxqTableDetails(val)">详情</div>
                                              </div>
                                              <div>
                                                  {{val['工单内容']}}
                                              </div>
                                              <div>
                                                  {{val['诉求时间']}}
                                              </div>
                                          </div>
                                          </li>
                                      </ul>
                                  </div>
                                  <transition name="moveLeft">
                                      <div class="Module5Pop" v-show="gsqxqTableDetails">
                                        <div style="height: 76px; display: flex;justify-content: space-between; align-items: center;background-image: linear-gradient(45deg, hsl(187deg 94% 53% / 10%), rgb(22 223 248 / 2%))">
                                            <span style="font-size: 30px;margin-left: 24px;display: flex;align-items: center;color: #5AE8FA;font-weight: 600;">风险分类详情</span>
                                            <img style="height: 49px;width: 49px;margin-right: 20px;cursor: pointer;" @click="ClosegsqxqTableDetails" src="./background/关闭.png" alt="">
                                        </div>
                                        <div style="with:100%;overflow: auto;height:calc(100% - 80px);position:relative;">
                                          <div  class="bgck12 dataCenter" v-if="ifShowTJDB" style="position: relative;width: 180px;height: 50px;margin: 20px 30px;">
                                            <div class="dataCenter" style="width:100%;cursor: pointer;height:100%;font-size: 28px;color: #0B1B2A;" @click="OpenShowTjdbDetails2">
                                              提级督办
                                            </div>
                                          </div>
                                          <div class="tjdbBox" v-show="showTjdbDetails2">
                                              <div class="titleName">
                                                <div class="Name" style="color:#5AE8FA;font-size:30px">请选择部门</div>
                                                <img style="height: 49px;width: 49px;cursor: pointer;" @click="showTjdbDetails2 = false" src="./background/关闭.png" alt="">
                                              </div>
                                              <div class="bodyChose">
                                                <Tree :data="treeSetList2"
                                                  :load-data="loadData"
                                                  @on-select-change='ChangeSelect2'></Tree>
                                              </div>
                                              <div class="footBox">
                                                <div class="Name" style="color:#C5EEF3;font-size:30px">{{CkeckedBm2===''?'请选择部门':CkeckedBm2}}</div>
                                                <div class="SureBtn dataCenter" @click="UpDataOk2(GsqxqTableValue)">确定</div>
                                              </div>
                                          </div>
                                          <div v-show="GsqxqTableValue['工单编号']" style="margin: 26px;display: flex;font-size: 28px;color: #C5EEF3;overflow: auto;">
                                            <div class="name" style="width:145px">
                                              工单编号:
                                            </div>
                                            <div class='value' style="width:420px">
                                              {{GsqxqTableValue['工单编号']}}
                                            </div>
                                          </div>
                                          <div style="margin: 26px;display: flex;font-size: 30px;color: #C5EEF3;overflow: auto;" v-show="testKey(key)" v-for="(data,key,index) in GsqxqTableValue" :key="index">
                                            <div class="name" style="width:200px">
                                              {{key}}:
                                            </div>
                                            <div class='value' style="width:calc(100% - 200px);">
                                              {{data}}
                                            </div>
                                          </div>
                                        </div>
                                      </div>
                                  </transition>
                              </div>
                            </transition>
                </div>
            </div>
        </transition>
        <!-- 模块三、四 -->
        <transition name="moveLeft">
            <div class="rightPart" v-show="isopenShow">
                <div id="Module5">
                  <div class="TopName"><img src="./newBack/15.png" alt=""></div>
                    <div class="title"><img src="./newBack/4.png" alt=""></div>
                    <div class="content">
                        <div class="MsgType">
                          <div :class="IsreadBox==='处置中'?'checked':'nochecked'" @click="changeIsreadBox('处置中')">未办事件</div>
                          <div :class="IsreadBox==='处置中'?'nochecked':'checked'"  @click="changeIsreadBox('已结案')">已办事件</div>
                        </div>
                        <div class="cityEvent" ref="cityEvent">
                            <ul class="item" ref="item">
                                <li v-for="(val, ind) in qztsList" :key="ind">
                                <div  class="eventBox" >
                                    <div>
                                        <div><span></span>{{val['问题标题']}}</div>
                                        <div @click="ShowEventDetails(val)">详情</div>
                                    </div>
                                    <div>
                                        {{val['描述']}}
                                    </div>
                                    <div style="display: flex;justify-content: space-between;">
                                      <div class="Time">
                                        {{val['上报时间']}}
                                      </div>
                                      <div v-if="incomingflownoList.indexOf(val['工单号']*1)>=0" class="state">
                                        预警
                                      </div>
                                    </div>
                                </div>
                                </li>
                            </ul>
                        </div>
                        <transition name="moveLeft">
                            <div class="Module5Pop" v-show="showEventDetails">
                              <div style="height: 76px; display: flex;justify-content: space-between; align-items: center;background-image: linear-gradient(45deg, hsl(187deg 94% 53% / 10%), rgb(22 223 248 / 2%))">
                                  <span style="font-size: 30px;margin-left: 24px;display: flex;align-items: center;color: #5AE8FA;font-weight: 600;">群众诉求详情</span>
                                  <img style="height: 49px;width: 49px;margin-right: 20px;cursor: pointer;" @click="CloseEventDetails" src="./background/关闭.png" alt="">
                              </div>
                              <div style="with:100%;overflow: auto;height:calc(100% - 80px)">
                                <div style="margin: 26px;font-size: 28px;color: #C5EEF3;max-height: 600px;overflow: auto;">{{xqValue['问题标题'] || ''}}</div>
                                <div>
                                  <div  class="bgck12 dataCenter"  style="position: relative;width: 180px;height: 50px;margin: 0 30px 20px 30px">
                                    <div class="dataCenter" style="width:100%;cursor: pointer;height:100%;font-size: 28px;color: #0B1B2A;" @click="OpenShowTjdbDetails">
                                      提级督办
                                    </div>
                                    <div class="tjdbBox" v-show="showTjdbDetails">
                                      <div class="titleName">
                                        <div class="Name" style="color:#5AE8FA;font-size:30px">请选择部门</div>
                                        <img style="height: 49px;width: 49px;cursor: pointer;" @click="showTjdbDetails = false" src="./background/关闭.png" alt="">
                                      </div>
                                      <div class="bodyChose">
                                        <Tree :data="treeSetList"
                                          :load-data="loadData"
                                          @on-select-change='ChangeSelect'></Tree>
                                      </div>
                                      <div class="footBox">
                                        <div class="Name" style="color:#C5EEF3;font-size:30px">{{CkeckedBm===''?'请选择部门':CkeckedBm}}</div>
                                        <div class="SureBtn dataCenter" @click="UpDataOk">确定</div>
                                      </div>
                                    </div>
                                  </div>
                                  <div style="display: flex;justify-content: space-between; margin: 0 30px 20px 30px;font-size: 24px;color: #C5EEF3;">
                                    <div class="bgck18 dataLeft" style="width: 400px;height: 50px;padding: 0 10px;">工单号：{{xqValue['工单号']}}</div>
                                    <div class="bgck18 dataLeft" style="width: 400px;height: 50px;padding: 0 10px;">满意度：{{xqValue.mydValue}}</div>
                                  </div>
                                  <div style="display: flex;justify-content: space-between; margin: 0 30px 20px 30px;font-size: 24px;color: #C5EEF3;">
                                    <div class="bgck18 dataLeft" style="width: 100%;height: 50px;padding: 0 10px;">办理情况：{{xqValue.fkValue}}</div>
                                  </div>
                                </div>
                                <div style="margin: 0 28px; color: #C5EEF3;font-size: 24px;max-height:600px;overflow: auto;padding: 16px;background-image: linear-gradient(45deg, rgb(22 223 248 / 4%), rgb(22 223 248 / 10%),rgb(22 223 248 / 4%));">
                                  {{xqValue['描述'] || '暂无数据'}}
                                </div>
                                <div style="margin: 28px;font-size: 24px;color: #C5EEF3;">处置时间线</div>
                                <div class="block" style="padding: 0 28px;">
                                    <div class="TimeBox" v-for="(da,index) in xqValue.timeLine" :key="index">
                                      <div class="line" v-if="index !== xqValue.timeLine.length-1"></div>
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
                <div id="Module6">
                    <div class="title"><img src="./newBack/1.png" alt=""></div>
                    <div class="content">
                        <div class="cityEvent" ref="cityEvent"
                           >
                            <ul class="item" ref="item">
                                <li v-for="(val, ind) in tjdbList" :key="ind">
                                <div  class="eventBox" >
                                    <div>
                                        <div><span></span>{{val['问题标题']}}</div>
                                        <div @click="ShowOtherDetails(val)">详情</div>
                                    </div>
                                    <div>
                                        {{val['问题描述']}}
                                    </div>
                                    <div>
                                        {{val['上报时间']}}
                                    </div>
                                </div>
                                </li>
                            </ul>
                        </div>
                        <transition name="moveLeft">
                            <div id="Module6Pop" v-show="showotherDetails">
                              <div style="height: 76px; display: flex;justify-content: space-between; align-items: center;background-image: linear-gradient(45deg, hsl(187deg 94% 53% / 10%), rgb(22 223 248 / 2%))">
                                  <span style="font-size: 30px;margin-left: 24px;display: flex;align-items: center;color: #5AE8FA;font-weight: 600;">提级督办详情</span>
                                  <img style="height: 49px;width: 49px;margin-right: 20px;cursor: pointer;" @click="CloseotherDetails" src="./background/关闭.png" alt="">
                              </div>
                              <div style="with:100%;overflow: auto;height:calc(100% - 80px)">
                                <div style="margin: 26px;font-size: 28px;color: #C5EEF3;max-height: 600px;overflow: auto;">{{mzsqxqValue['问题标题'] || ''}}</div>
                                <div style="margin: 0 28px; color: #C5EEF3;font-size: 24px;max-height:600px;overflow: auto;padding: 16px;background-image: linear-gradient(45deg, rgb(22 223 248 / 4%), rgb(22 223 248 / 10%),rgb(22 223 248 / 4%));">
                                  {{mzsqxqValue['问题描述'] || '暂无数据'}}
                                </div>
                                <div style="margin: 28px;font-size: 24px;color: #C5EEF3;">处置时间线</div>
                                <div class="block" style="padding: 0 28px;">
                                    <div class="TimeBox" v-for="(da,index) in mzsqxqValue.timeLine" :key="index">
                                      <div class="line" v-if="index !== mzsqxqValue.timeLine.length-1"></div>
                                      <div class="radio"></div>
                                      <div class="time">{{da['处置时间']}}</div>
                                      <div class="data">
                                        <div>流转状态：{{ztysTable[da['节点处置状态名称']]}}</div>
                                        <div>流转内容：{{da['处置回复内容']}}</div>
                                      </div>
                                    </div>
                                </div>
                              </div>
                            </div>
                        </transition>
                    </div>
                </div>
                <EfficiencyPage></EfficiencyPage>
            </div>
        </transition>
    </div>
</template>
<script>
import IntegratedHistogram from '../IntegratedHistogram/index.vue'
import ELine from '../ELine/index.vue'
import NewGauge from '../NewGauge/index.vue'
import NewProgress from '../NewProgress/index.vue'
import EfficiencyPage from './EfficiencyPage.vue'
import NewPie from '../NewPie/index.vue'
import vueSeamlessScroll from 'vue-seamless-scroll'
export default {
  data: function () {
    return {
      showRYXX: false,
      showTableBox: false,
      showsqTableBox: false,
      showgsqxqTableBox: false,
      gsqxqTableDetails: false,
      zzqzTableDetails: false,
      grtsxqData: [],
      grtxqData: [],
      grtgsqxqData: [],
      isopenShow: false,
      indexOf7: -1,
      indexOf15: -1,
      showGZFF1: false,
      showGZFF2: false,
      appealType: '工资发放',
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
      gfxryDetail3: {}, // module2 区民生诉求指数详情 高风险人员详情
      GetMSSQ7: [],
      GetMSSQ16: {},
      qztsList: [], // 投诉列表
      tjdbList: [],
      incomingflownoList: [],
      xqValue: {},
      mzsqxqValue: {},
      ztysTable: {
        cFinish: '已完成',
        overCheck: '已结案'
        // cFinish:'已完成'
      },
      showEventDetails: false,
      XqTableValue: [],
      ZzqzTableValue: [],
      GsqxqTableValue: [],
      showTableDetails: false,
      showTjdbDetails: false,
      showTjdbDetails1: false,
      showTjdbDetails2: false,
      ifShowTJDB: false,
      CkeckedBm: '',
      CkeckedBmData: {},
      CkeckedBm1: '',
      CkeckedBmData1: {},
      CkeckedBm2: '',
      CkeckedBmData2: {},
      IsreadBox: '处置中',
      showotherDetails: false,
      treeSetList: [],
      treeSetList1: [],
      treeSetList2: [],
      selectTreeId: '',
      SqTipsName: '',
      isVerification: false
    }
  },
  components: {IntegratedHistogram, EfficiencyPage, ELine, NewGauge, NewProgress, NewPie, vueSeamlessScroll},
  computed: {
    // module2 某诉求小区排行列表
    sqxqphList () {
      let arr = []
      if (this.appealType === '全部') {
        this.AllsqxqphList.forEach(element => {
          if (element['明细'] && this.getBubble.indexOf(element['类别名称']) >= 0) {
            arr = arr.concat(element['明细'].rows)
          }
        })
      } else {
        this.AllsqxqphList.forEach(element => {
          if (element['类别名称'] === this.appealType) {
            if (element['明细']) {
              arr = element['明细'].rows
            }
          }
        })
      }

      return arr
    },
    testKey () {
      return (key) => {
        return key !== '工单编号' && !/[a-z]/i.test(key)
      }
    },
    // module2 诉求总数
    sqxqphSum () {
      let sum = 0
      if (this.appealType === '全部') {
        this.AllsqxqphList.forEach(element => {
          if (this.getBubble.indexOf(element['类别名称']) >= 0) {
            sum = sum + element['数量']
          }
        })
      } else {
        this.AllsqxqphList.forEach(element => {
          if (element['类别名称'] === this.appealType) {
            sum = element['数量']
          }
        })
      }
      return sum
    },
    getSreetImg () {
      return (street) => {
        if (street) {
          return require('./' + street + '.jpg')
        }
      }
    },
    // getWholeRegionNum () {
    //   let num = 0
    //   this.qmssqList.forEach(element => {
    //     num = num + element['数量']
    //   })
    //   return num
    // },
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
        width: 550,
        height: 500,
        'imgClass': 'icon-n-histogram',
        'chartType': 'IntegratedHistogram',
        'barType': 'NewHistogram',
        'ifGradual': 'true',
        'showBackground1': false, // 是否显示背景柱图
        'backgroundColor1': 'rgba(180, 180, 180, 0.2)', // 背景柱图的颜色
        'barRadius1': 0, // 柱体圆角
        'barWidth1': 28, // 柱体大小
        'HistogramType': 1, // 柱状图类型
        'dataTypeSet': 2, // 柱状图类型
        'topTextColor': '#fff', // 柱状图类型2、3顶部文字的颜色
        'ctLegendShow1': false,
        'dataSetType': false,
        'colorful1': false,
        'ifGradual1': 'false',
        'legendColor1': 'rgba(197,238,243,1)',
        'splitShow1': true,
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
        'DScatterColor1': [
          ['rgba(245,155,66,1)', 'rgba(245,155,66,0.1)'],
          ['#8feee5', '#1bbcae'],
          ['#fa8d76', '#db4222'],
          ['#af8af3', '#874edc'],
          ['#f5739c', '#f31d53'],
          ['#ffdf91', '#eeb01b'],
          ['#5c84e7', '#144fe5'],
          ['#85f8c0', '#62dc26']
        ],
        'ctLegendColor1': '#666f8b',
        'axisLabelSize1': '18',
        'DanweiColor1': 'rgba(197,238,243,1)',
        'DanweiSize1': 18,
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
        'tooltipBackColor1': 'rgba(87, 98, 93, 0.63)',
        'tooltipTextColor1': 'rgba(197,238,243,1)',
        'tooltipfontSize1': 18,
        'splitColor1': 'rgba(197,238,243,1)',
        'splitSize1': 1,
        'rotate1': 0,
        'chartData1': chartData
      }
    },
    getTSZZQK15 () {
      let chartData = {
        columns: [],
        rows: []
      }
      if (this.qmssqDetail['双曲线图']) {
        chartData = this.qmssqDetail['双曲线图']
      }
      chartData.unitX = '月份'
      chartData.unit = '次'
      return {
        'text': '曲线图',
        'imgClass': 'icon-n-line',
        'height': 590,
        'width': 740,
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
        'gridTop': 15,
        'gridBotton': 15,
        'gridLeft': 5,
        'gridRight': 5,
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
        'height': 290,
        'width': 720,
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
        'gridTop': 15,
        'gridBotton': 5,
        'gridLeft': 5,
        'gridRight': 10,
        'tooltipShow': true,
        'subsectionType': true,
        'areaLineType': true,
        'tooltipBackColor': '#57625d',
        'tooltipTextColor': '#e9eaee',
        'tooltipfontSize': 24,
        'splitColor': 'rgba(197,238,243,1)',
        'splitSize': 1,
        'minInterval': '',
        'Linesubsection': false,
        'boundaryGap': true,
        'legendColor': 'rgba(197,238,243,1)',
        'DanweiColor': 'rgba(197,238,243,1)',
        'DanweiSize': 24,
        'lineArea': true, // 是否为区域图
        'lineColorType': false, // 是否为区域图
        'smooth': true,
        'LineType': 'solid',
        'formatterType': '1',
        'symbolType': 'circle',
        'symbolSrc': '',
        'symbolName': '',
        'symbolSize': 6,
        'lineWidth': 2,
        'showPoint': true, // 是否标点
        'PointSize': '16',
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
        width: 750,
        height: 240,
        'imgClass': 'icon-n-histogram',
        'chartType': 'IntegratedHistogram',
        'barType': 'NewHistogram',
        'ifGradual': 'true',
        'showBackground1': false, // 是否显示背景柱图
        'backgroundColor1': 'rgba(180, 180, 180, 0.2)', // 背景柱图的颜色
        'barRadius1': 0, // 柱体圆角
        'barWidth1': 28, // 柱体大小
        'HistogramType': 1, // 柱状图类型
        'dataTypeSet': 2, // 柱状图类型
        'topTextColor': '#fff', // 柱状图类型2、3顶部文字的颜色
        'ctLegendShow1': false,
        'dataSetType': false,
        'colorful1': false,
        'ifGradual1': 'false',
        'legendColor1': 'rgba(197,238,243,1)',
        'splitShow1': true,
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
        'DScatterColor1': [
          ['rgba(245,155,66,1)', 'rgba(245,155,66,0.1)'],
          ['#8feee5', '#1bbcae'],
          ['#fa8d76', '#db4222'],
          ['#af8af3', '#874edc'],
          ['#f5739c', '#f31d53'],
          ['#ffdf91', '#eeb01b'],
          ['#5c84e7', '#144fe5'],
          ['#85f8c0', '#62dc26']
        ],
        'ctLegendColor1': '#666f8b',
        'axisLabelSize1': '18',
        'DanweiColor1': 'rgba(197,238,243,1)',
        'DanweiSize1': 18,
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
        'tooltipBackColor1': 'rgba(87, 98, 93, 0.63)',
        'tooltipTextColor1': 'rgba(197,238,243,1)',
        'tooltipfontSize1': 18,
        'splitColor1': 'rgba(197,238,243,1)',
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
        width: 1000,
        height: 570,
        'imgClass': 'icon-n-histogram',
        'chartType': 'IntegratedHistogram',
        'barType': 'NewHistogram',
        'ifGradual': 'true',
        'showBackground1': false, // 是否显示背景柱图
        'backgroundColor1': 'rgba(180, 180, 180, 0.2)', // 背景柱图的颜色
        'barRadius1': 0, // 柱体圆角
        'barWidth1': 40, // 柱体大小
        'HistogramType': 1, // 柱状图类型
        'dataTypeSet': 2, // 柱状图类型
        'topTextColor': '#fff', // 柱状图类型2、3顶部文字的颜色
        'ctLegendShow1': false,
        'dataSetType': false,
        'colorful1': false,
        'ifGradual1': 'false',
        'legendColor1': 'rgba(197,238,243,1)',
        'splitShow1': true,
        'ctLegendSize1': '30',
        'TabFontSize': '30',
        'colorType1': 'custom',
        'ScatterColor1': [
          '#6fcaf7',
          '#8feee5',
          '#fa8d76',
          '#af8af3',
          '#f5739c',
          '#ffdf91',
          '#5c84e7'],
        'DScatterColor1': [
          ['rgba(245,155,66,1)', 'rgba(245,155,66,0.1)'],
          ['#8feee5', '#1bbcae'],
          ['#fa8d76', '#db4222'],
          ['#af8af3', '#874edc'],
          ['#f5739c', '#f31d53'],
          ['#ffdf91', '#eeb01b'],
          ['#5c84e7', '#144fe5'],
          ['#85f8c0', '#62dc26']
        ],
        'ctLegendColor1': '#666f8b',
        'axisLabelSize1': '28',
        'DanweiColor1': 'rgba(197,238,243,1)',
        'DanweiSize1': 28,
        'minInterval1': '',
        'legendY1': 90,
        'gridTop1': 10,
        'gridBotton1': 15,
        'gridLeft1': 10,
        'gridRight1': 10,
        'formatterType1': '1',
        'cropSize1': 2,
        'tooltipShow1': true,
        'dataTypeStation': false,
        'tooltipBackColor1': 'rgba(87, 98, 93, 0.63)',
        'tooltipTextColor1': 'rgba(197,238,243,1)',
        'tooltipfontSize1': 30,
        'splitColor1': 'rgba(197,238,243,1)',
        'splitSize1': 1,
        'rotate1': 0,
        'chartData1': chartData
      }
    },
    getBubble () {
      return [
        // '企业问题',
        '工资发放',
        '水电气',
        '小区管理',
        '商品质量',
        '房屋中介',
        '占道停车',
        '占道经营',
        '消费纠纷',
        '物业服务',
        '食品安全',
        '消防安全',
        '全部'
      ]
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
    clickFunBar (data) {
      this.showTableBox = true
      this.grtsxqData = data['投诉详情'].rows || []
    },
    clickFunBar1 (data) {
      this.showgsqxqTableBox = true
      this.grtgsqxqData = data['投诉详情'].rows || []
      this.ifShowTJDB = true
    },
    showSqTable (data) {
      this.showsqTableBox = true
      this.grtxqData = data
    },
    showgsqxqTable (data) {
      console.log('tsdata', data, this.gsqxqDetail)
      let type = data['小区'] || data['名称']
      $('#lead-screen').addClass('disShow')
      this.axios.get('/leaderview/newDistrict/GetMSSQ19?community=' + this.gsqxqDetail['小区'] + '&typeName=' + type).then(res => {
        $('#lead-screen').removeClass('disShow')
        if (res.success && res.obj) {
          this.grtgsqxqData = res.obj.rows
          this.showgsqxqTableBox = true
        }
      })
    },
    ShowgsqxqTableDetails (val) {
      this.GsqxqTableValue = val
      this.gsqxqTableDetails = true
    },
    ClosegsqxqTableDetails () {
      this.gsqxqTableDetails = false
      this.showTjdbDetails2 = false
      this.ifShowTJDB = false
      this.CkeckedBm2 = ''
      this.CkeckedBmData2 = []
    },
    ShowzZzqzTableDetails (val) {
      this.ZzqzTableValue = val
      this.zzqzTableDetails = true
    },
    ShowXqTableDetails (val) {
      this.XqTableValue = val
      this.showTableDetails = true
    },
    CloseXqTableDetails () {
      this.showTableDetails = false
      this.showTjdbDetails1 = false
      this.CkeckedBm1 = ''
      this.CkeckedBmData1 = []
    },
    openisopenShow () {
      this.isopenShow = true
    },
    ShowEventDetails (val) {
      if (val['工单号']) {
        $('#lead-screen').addClass('disShow')
        // document.querySelector('#Module5 .cityEvent .item').style.animationPlayState = 'paused'
        this.axios.get('/leaderview/newDistrict/GetMSSQ21?param=' + val['工单号'] + '&param2=' + val['源平台工单号'] + '&param3=' + val['来源']).then(res => {
          $('#lead-screen').removeClass('disShow')
          if (res.success && res.obj) {
            val.timeLine = res.obj['处置信息'].data[0].items.rows
            val.fkValue = res.obj['反馈信息']
            val.mydValue = res.obj['满意度']
            this.xqValue = val
            this.showEventDetails = true
          }
        }, error => {
          console.log(error)
          $('#lead-screen').removeClass('disShow')
        }).catch(err => {
          console.log(err)
          $('#lead-screen').removeClass('disShow')
        })
      }
    },
    ShowOtherDetails (val) {
      if (val['工单号']) {
        $('#lead-screen').addClass('disShow')
        // document.querySelector('#Module6 .cityEvent .item').style.animationPlayState = 'paused'
        this.axios.get('/leaderview/ChengYun4/GetTJDB2?param=' + val['工单号']).then(res => {
          $('#lead-screen').removeClass('disShow')
          if (res.success && res.obj) {
            val.timeLine = res.obj.rows
            this.mzsqxqValue = val
            this.showotherDetails = true
          }
        }, error => {
          console.log(error)
          $('#lead-screen').removeClass('disShow')
        }).catch(err => {
          console.log(err)
          $('#lead-screen').removeClass('disShow')
        })
      }
    },
    getData (t1, format = 'YYYY-MM-DD HH:mm:ss') {
      const config = {
        YYYY: t1.getFullYear(),
        MM: t1.getMonth() + 1,
        DD: t1.getDate(),
        HH: t1.getHours(),
        mm: t1.getMinutes(),
        ss: t1.getSeconds()
      }

      for (const key in config) {
        format = format.replace(key, config[key])
      }
      return format
    },
    UpDataOk () {
      const formData = new FormData()
      formData.append('id', new Date().getTime() * 1)
      formData.append('dept', this.CkeckedBmData.dept)
      formData.append('flowNo', this.xqValue['工单号'])
      formData.append('optdate', this.getData(new Date(), 'YYYY-MM-DD HH:mm:ss'))
      formData.append('nickname', this.CkeckedBmData.title)
      formData.append('nickphone', this.CkeckedBmData.nickphone)
      formData.append('opttag', 'overCheck')
      formData.append('dept_keshi', this.CkeckedBmData.deptkeshi)
      formData.append('opttag_2', 1)
      formData.append('identifier', 1)
      formData.append('chuzhiresult', '')
      formData.append('remark', '')
      $('#lead-screen').addClass('disShow')
      this.axios.post('/leaderview/ChengYun4/GetTJDB4', formData).then(res => {
        $('#lead-screen').removeClass('disShow')
        if (res.success) {
          window.setTimeout(() => {
            this.gettjdbList()
          }, 4000)
          this.CloseEventDetails()
        }
      }, error => {
        console.log(error)
        $('#lead-screen').removeClass('disShow')
      }).catch(err => {
        console.log(err)
        $('#lead-screen').removeClass('disShow')
      })
    },
    UpDataOk1 (detail) {
      const formData = new FormData()
      formData.append('desc', detail['desc'] || '')
      formData.append('class3', detail['class3'] || '')
      formData.append('result', detail['result'] || '')
      formData.append('address', detail['address'] || '')
      formData.append('classId', detail['classId'] || '')
      formData.append('enddate', detail['enddate'] || '')
      formData.append('hadress', detail['hadress'] || '')
      formData.append('$classId$', detail['$classId$'] || '')
      formData.append('originalNo', detail['originalNo'] || '')
      formData.append('reportDate', detail['reportDate'] || '')
      formData.append('contactUser', detail['contactUser'] || '')
      formData.append('$contactUser$', detail['$contactUser$'] || '')
      formData.append('questionTitle', detail['questionTitle'] || '')
      formData.append('questiontitle', detail['questiontitle'] || '')
      formData.append('incomingflowno', detail['incomingflowno'] || '')

      formData.append('urgency', '一般')
      formData.append('$urgency$', '一般')
      formData.append('flowsource', '12345电话工单')
      formData.append('flowNo', this.DateToString() + '0001')
      formData.append('opttag', 0)
      formData.append('forwardEvent', true)

      const formData2 = new FormData()
      formData2.append('id', new Date().getTime() * 1)
      formData2.append('dept', this.CkeckedBmData1.dept)
      formData2.append('flowNo', this.DateToString() + '0001')
      formData2.append('optdate', this.getData(new Date(), 'YYYY-MM-DD HH:mm:ss'))
      formData2.append('nickname', this.CkeckedBmData1.title)
      formData2.append('nickphone', this.CkeckedBmData1.nickphone)
      formData2.append('opttag', 'overCheck')
      formData2.append('dept_keshi', this.CkeckedBmData1.deptkeshi)
      formData2.append('opttag_2', 1)
      formData2.append('identifier', 1)
      formData2.append('chuzhiresult', '')
      formData2.append('remark', '')
      this.axios.post('/leaderview/ChengYun4/GetTJDB5_2', formData2).then(res => {
        if (res.success) {
        }
      })
      $('#lead-screen').addClass('disShow')
      this.axios.post('/leaderview/ChengYun4/GetTJDB5', formData).then(res => {
        $('#lead-screen').removeClass('disShow')
        if (res.success) {
          window.setTimeout(() => {
            this.gettjdbList()
          }, 4000)
          this.showTjdbDetails1 = false
          // document.querySelector('#Module5 .cityEvent .item').style.animationPlayState = 'running'
        }
      }, error => {
        console.log(error)
        $('#lead-screen').removeClass('disShow')
      }).catch(err => {
        console.log(err)
        $('#lead-screen').removeClass('disShow')
      })
    },
    DateToString () {
      let date = new Date()
      let y = date.getFullYear()
      let M = ''
      let d = ''

      let h = ''
      let m = ''
      let s = ''
      if (date.getMonth() < 9) {
        M = '0' + (date.getMonth() + 1)
      } else {
        M = date.getMonth() + 1
      }
      if (date.getDate() < 10) {
        d = '0' + date.getDate()
      } else {
        d = date.getDate()
      }
      if (date.getHours() < 10) {
        h = '0' + date.getHours()
      } else {
        h = date.getHours()
      }
      if (date.getMinutes() < 10) {
        m = '0' + date.getMinutes()
      } else {
        m = date.getMinutes()
      }
      if (date.getSeconds() < 10) {
        s = '0' + date.getSeconds()
      } else {
        s = date.getSeconds()
      }
      return y + M + d + h + m + s
    },
    UpDataOk2 (detail) {
      const formData = new FormData()
      formData.append('desc', detail['desc'] || '')
      formData.append('class3', detail['class3'] || '')
      formData.append('result', detail['result'] || '')
      formData.append('address', detail['address'] || '')
      formData.append('classId', detail['classId'] || '')
      formData.append('enddate', detail['enddate'] || '')
      formData.append('hadress', detail['hadress'] || '')
      formData.append('$classId$', detail['$classId$'] || '')
      formData.append('originalNo', detail['originalNo'] || '')
      formData.append('reportDate', detail['reportDate'] || '')
      formData.append('contactUser', detail['contactUser'] || '')
      formData.append('$contactUser$', detail['$contactUser$'] || '')
      formData.append('questionTitle', detail['questionTitle'] || '')
      formData.append('questiontitle', detail['questiontitle'] || '')
      formData.append('incomingflowno', detail['incomingflowno'] || '')

      formData.append('urgency', '一般')
      formData.append('$urgency$', '一般')
      formData.append('flowsource', '12345电话工单')
      formData.append('flowNo', this.DateToString() + '0001')
      formData.append('opttag', 0)
      formData.append('forwardEvent', true)
      // formData.append('id', new Date().getTime() * 1)
      // formData.append('dept', this.CkeckedBmData1.dept)
      // formData.append('flowNo', detail[''])
      // formData.append('optdate', this.getData(new Date(), 'YYYY-MM-DD HH:mm:ss'))
      // formData.append('nickname', this.CkeckedBmData1.title)
      // formData.append('nickphone', this.CkeckedBmData1.nickphone)
      // formData.append('opttag', 'overCheck')
      // formData.append('dept_keshi', this.CkeckedBmData1.deptkeshi)
      // formData.append('opttag_2', 1)
      // formData.append('identifier', 1)
      // formData.append('chuzhiresult', '')
      // formData.append('remark', '')
      $('#lead-screen').addClass('disShow')
      this.axios.post('/leaderview/ChengYun4/GetTJDB5', formData).then(res => {
        $('#lead-screen').removeClass('disShow')
        if (res.success) {
          window.setTimeout(() => {
            this.gettjdbList()
          }, 4000)
          this.showTjdbDetails2 = false
          // document.querySelector('#Module5 .cityEvent .item').style.animationPlayState = 'running'
        }
      }, error => {
        console.log(error)
        $('#lead-screen').removeClass('disShow')
      }).catch(err => {
        console.log(err)
        $('#lead-screen').removeClass('disShow')
      })
    },
    CloseotherDetails () {
      this.showotherDetails = false
      // document.querySelector('#Module6 .cityEvent .item').style.animationPlayState = 'running'
    },
    CloseEventDetails () {
      this.showEventDetails = false
      this.showTjdbDetails = false
      // document.querySelector('#Module5 .cityEvent .item').style.animationPlayState = 'running'
    },
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
    //   $('.normal2').removeClass('active2')
    //   event.target.classList.add('active2')
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
    //   $('.normal1').removeClass('active1')
    //   event.target.classList.add('active1')
    },
    ShowGZFF1 () {
      // this.showGZFF1 = true
      // this.sqxqphDetail = this.sqxqphList[index]
      // this.sqxqphDetail['小区排名'] = index + 1
      $('#lead-screen').addClass('disShow')
      if (this.appealType === '全部') {
        this.axios.get('/leaderview/newDistrict/GetMSSQ19').then(res => {
          $('#lead-screen').removeClass('disShow')
          if (res.success && res.obj) {
            this.showSqTable(res.obj.rows)
          }
        })
      } else {
        this.axios.get('/leaderview/newDistrict/GetMSSQ19?typeName=' + this.appealType).then(res => {
          $('#lead-screen').removeClass('disShow')
          if (res.success && res.obj) {
            this.showSqTable(res.obj.rows)
          }
        })
      }
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
    changeIsreadBox (state) {
      this.IsreadBox = state
      this.getqztsList()
    },
    CloseGFXRYS () {
      this.showGFXRYS = false
      this.gfxryDetail3 = {}
    },
    searchData () {
      if (this.SqTipsName === '141242') {
        this.isVerification = true
      } else {
        this.isVerification = false
        this.$notify({
          message: '未授权！',
          position: 'bottom-right',
          customClass: 'toast toast-error'
        })
      }
    },
    ChangeSelect (item, data) {
      if (item.length === 1) {
        console.log(item)
        this.CkeckedBm = item[0].title
        this.CkeckedBmData = item[0]
      } else {
        this.CkeckedBm = ''
        this.CkeckedBmData = {}
      }
    },
    ChangeSelect1 (item, data) {
      if (item.length === 1) {
        this.CkeckedBm1 = item[0].title
        this.CkeckedBmData1 = item[0]
      } else {
        this.CkeckedBm1 = ''
        this.CkeckedBmData1 = {}
      }
    },
    ChangeSelect2 (item, data) {
      if (item.length === 1) {
        this.CkeckedBm2 = item[0].title
        this.CkeckedBmData2 = item[0]
      } else {
        this.CkeckedBm2 = ''
        this.CkeckedBmData2 = {}
      }
    },
    loadData (item, callback) {
      let newtype = ''
      if (item.type === 'children') {
        newtype = 'members'
      }
      if (item.id) {
        $('#lead-screen').addClass('disShow')
        this.axios.get('/leaderview/ChengYun4/GetTJDB3?param=' + item.type + '&id=' + item.id).then(res => {
          $('#lead-screen').removeClass('disShow')
          if (res.success && res.obj.rows) {
            let treeData = []
            if (item.type === 'children') {
              res.obj.rows.forEach(element => {
                treeData.push({
                  title: element['名称'],
                  id: element['组织ID'],
                  dept: item.title,
                  type: 'members',
                  disabled: true,
                  loading: false,
                  disableCheckbox: true,
                  children: []
                })
              })
            } else {
              res.obj.rows.forEach(element => {
                treeData.push({
                  title: element['名称'],
                  dept: item.dept,
                  deptkeshi: item.title,
                  nickphone: element['电话'],
                  id: element['组织ID'],
                  type: newtype
                })
              })
            }
            if (treeData.length === 0) {
              treeData.push({
                title: '暂无数据',
                id: -1,
                type: '',
                disabled: true
              })
            }
            callback(treeData)
          }
        }, error => {
          console.log(error)
          $('#lead-screen').removeClass('disShow')
        }).catch(err => {
          console.log(err)
          $('#lead-screen').removeClass('disShow')
        })
      }
    },
    OpenShowTjdbDetails () {
      this.showTjdbDetails = !this.showTjdbDetails
      this.CkeckedBm = ''
      this.CkeckedBmData = {}
      if (this.showTjdbDetails) {
        $('#lead-screen').addClass('disShow')
        this.axios.get('/leaderview/ChengYun4/GetTJDB3').then(res => {
          $('#lead-screen').removeClass('disShow')
          if (res.success && res.obj.rows) {
            let treeData = []
            res.obj.rows.forEach(element => {
              treeData.push({
                title: element['名称'],
                id: element['组织ID'],
                type: 'children',
                disabled: true,
                disableCheckbox: true,
                loading: false,
                children: []
              })
            })
            this.treeSetList = treeData
          }
        }, error => {
          console.log(error)
          $('#lead-screen').removeClass('disShow')
        }).catch(err => {
          console.log(err)
          $('#lead-screen').removeClass('disShow')
        })
      }
    },
    OpenShowTjdbDetails1 () {
      this.showTjdbDetails1 = !this.showTjdbDetails1
      this.CkeckedBm1 = ''
      this.CkeckedBmData1 = {}
      if (this.showTjdbDetails1) {
        $('#lead-screen').addClass('disShow')
        this.axios.get('/leaderview/ChengYun4/GetTJDB3').then(res => {
          $('#lead-screen').removeClass('disShow')
          if (res.success && res.obj.rows) {
            let treeData = []
            res.obj.rows.forEach(element => {
              treeData.push({
                title: element['名称'],
                id: element['组织ID'],
                type: 'children',
                disabled: true,
                disableCheckbox: true,
                loading: false,
                children: []
              })
            })
            this.treeSetList1 = treeData
          }
        }, error => {
          console.log(error)
          $('#lead-screen').removeClass('disShow')
        }).catch(err => {
          console.log(err)
          $('#lead-screen').removeClass('disShow')
        })
      }
    },
    OpenShowTjdbDetails2 () {
      this.showTjdbDetails2 = !this.showTjdbDetails2
      this.CkeckedBm2 = ''
      this.CkeckedBmData2 = {}
      if (this.showTjdbDetails2) {
        $('#lead-screen').addClass('disShow')
        this.axios.get('/leaderview/ChengYun4/GetTJDB3').then(res => {
          $('#lead-screen').removeClass('disShow')
          if (res.success && res.obj.rows) {
            let treeData = []
            res.obj.rows.forEach(element => {
              treeData.push({
                title: element['名称'],
                id: element['组织ID'],
                type: 'children',
                disabled: true,
                disableCheckbox: true,
                loading: false,
                children: []
              })
            })
            this.treeSetList2 = treeData
          }
        }, error => {
          console.log(error)
          $('#lead-screen').removeClass('disShow')
        }).catch(err => {
          console.log(err)
          $('#lead-screen').removeClass('disShow')
        })
      }
    },
    gettjdbList () {
      this.tjdbList = []
      $('#lead-screen').addClass('disShow')
      this.axios.get('/leaderview/ChengYun4/GetTJDB1').then(res => {
        $('#lead-screen').removeClass('disShow')
        if (res.success && res.obj.rows) {
          this.tjdbList = res.obj.rows
          // document.querySelector('#Module6 .cityEvent .item').style.animationDuration = this.tjdbList.length * 3 + 's'
        }
      }, error => {
        console.log(error)
        $('#lead-screen').removeClass('disShow')
      }).catch(err => {
        console.log(err)
        $('#lead-screen').removeClass('disShow')
      })
    },
    getqztsList () {
      // 获取群众投诉数据
      $('#lead-screen').addClass('disShow')
      this.axios.get('/leaderview/newDistrict/GetMSSQ20?param=' + this.IsreadBox).then(res => {
        $('#lead-screen').removeClass('disShow')
        if (res.success && res.obj['群众诉求']) {
          this.incomingflownoList = []
          this.qztsList = []
          this.qztsList = res.obj['群众诉求'].rows
          this.incomingflownoList = res.obj['预警列表']
          // document.querySelector('#Module5 .cityEvent .item').style.animationDuration = this.qztsList.length * 3 + 's'
        }
      }, error => {
        console.log(error)
        $('#lead-screen').removeClass('disShow')
      }).catch(err => {
        console.log(err)
        $('#lead-screen').removeClass('disShow')
      })
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

      this.axios.get('/leaderview/newDistrict/GetMSSQ7').then(res => {
        if (res.success && res.obj.rows) {
          this.GetMSSQ7 = res.obj.rows
        }
      })

      this.axios.get('/leaderview/newDistrict/GetMSSQ16').then(res => {
        if (res.success && res.obj.rows) {
          this.GetMSSQ16 = res.obj.rows
        }
      })
      this.getqztsList()
      this.gettjdbList()
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
    // flex-wrap: wrap;
    font-family: monospace !important;
    position: relative;
    // overflow: hidden;
    .topPart{
      width: 100%;
      height: 172px;
      position: absolute;
      .Title{
        height: 123px;
        width: 534px;
        left: 4053px;
        top: 0;
        position: absolute;
        img{
          height: 100%;
          width: 100%;
        }
      }
    }
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
.bgck18{
  background-image: url('./newBack/18.png');
  background-size: 100% 100%;
}
.dataCenter{
  display: flex;
  justify-content: center;
  align-items: center;
}
.dataLeft{
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
.bgck12{
  background-image: url('./newBack/12.png');
  background-size: 100% 100%;
}
    #Module1{
        width: 680px;
        height: 1512px;
        margin-top: 108px;
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
            >div:last-child{
                position: relative;
                overflow: hidden;
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
                            font-size: 36px;
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
                            font-size: 30px;
                            width: 100px;
                            height: 40px;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            border:1px solid rgba(255,255,255,0.3);
                            border-radius: 16px;
                            text-align: center;
                            margin: 0px auto;
                            cursor: pointer;
                        }
                    }
                    .ball:nth-child(2){
                        width: 192px;
                        height: 280px;
                        background: url('./background/1_1.png');
                        margin-right: 16px;
                        >div:nth-child(1){
                            color:#fff;
                            font-size: 36px;
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
                            font-size: 30px;
                            width: 100px;
                            height: 40px;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            border:1px solid rgba(255,255,255,0.3);
                            border-radius: 16px;
                            text-align: center;
                            margin: 0px auto;
                            cursor: pointer;
                        }
                    }
                    .ball:nth-child(3){
                        width: 192px;
                        height: 220px;
                        background: url('./background/3_1.png');
                        >div:nth-child(1){
                            color:#fff;
                            font-size: 36px;
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
                            font-size: 30px;
                            width: 100px;
                            height: 40px;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            border:1px solid rgba(255,255,255,0.3);
                            border-radius: 16px;
                            text-align: center;
                            margin: 0px auto;
                            cursor: pointer;
                        }
                    }
                }
                .list{
                    width: 608px;
                    height: 1056px;
                    overflow-y:scroll;
                    // margin:0 auto;
                    .rows{
                        width: 100%;
                        height: 92px;
                        background: url('./background/编组_7.png');
                        background-size: 100% 100%;
                        margin-bottom: 12px;
                        display: flex;
                        align-items: center;
                        justify-content: space-around;
                        .col1{
                            color: #5ae8fa;
                            font-size: 30px;
                            font-weight: bold;
                            display: flex;
                            align-items: center;
                            text-align: center;
                            width: 20%;
                            >span{
                                display: inline-block;
                                background: #fcb83c;
                                width: 20px;
                                height: 20px;
                                border-radius: 50%;
                                margin-right: 10px;
                            }
                        }
                        .col2{
                            color: #c5eef3;
                            font-size: 32px;
                            width: 30%;
                            text-align: center;
                        }
                        .col3{
                            color: #c5eef3;
                            font-size: 32px;
                            width: 30%;
                            text-align: center;
                        }
                        .col4{
                            color:#16DFF8;
                            font-size: 30px;
                            width: 100px;
                            height: 40px;
                            display: flex;
                            justify-content: center;
                            align-items: center;
                            border:1px solid rgba(22,223,248,0.6);
                            border-radius: 16px;
                            text-align: center;
                            margin: 0px auto;
                            cursor: pointer;
                        }
                    }
                }
                .firstPop{
                    width: 100%;
                    height: 100%;
                    position: absolute;
                    top: 0;
                    left: 0;
                    background: linear-gradient(180deg,#0a2b3a, #0b1b2a);
                    border: 2px solid;
                    border-image: linear-gradient(0deg, rgba(13,171,149,0.20), #1ed5c7) 2 2;
                    border-radius: 4px;
                    .title{
                        height: 76px;
                        background: linear-gradient(315deg,rgba(22,223,248,0.02), rgba(22,223,248,0.10) 98%);
                        font-size: 30px;
                        color: #5AE8FA;
                        display: flex;
                        align-items: center;
                        justify-content: space-between;
                        padding: 0 20px 0 28px;
                        margin-bottom: 32px;
                        >div:last-child{
                            img{
                                width: 49px;
                                height:49px;
                                cursor: pointer;
                            }
                        }
                    }
                    >div:nth-child(2){
                        width:100%;
                        padding: 0 28px;
                        >div:nth-child(1){
                            font-size: 36px;
                            color: #C5EEF3;
                        }
                        >div:nth-child(2){
                            img{
                                width: 18px;
                                height:18px;
                                margin-right: 5px;
                            }
                            font-size: 32px;
                            font-weight: bold;
                            color: #C5EEF3;
                            vertical-align: middle;
                            margin: 18px 0 16px 0;
                        }
                        >div:nth-child(3){
                            img{
                                width: 18px;
                                height:18px;
                                margin-right: 5px;
                            }
                            font-size: 30px;
                            color: #C5EEF3;
                            vertical-align: middle;
                            margin-bottom: 32px;
                        }
                        >div:nth-child(4){
                            width: 100%;
                            height: 116px;
                            background: linear-gradient(270deg,rgba(22,223,248,0.04), rgba(22,223,248,0.10) 50%, rgba(22,223,248,0.04));
                            display: flex;
                            align-items: center;
                            justify-content: space-around;
                            color: #C5EEF3;
                            font-size: 28px;
                            margin-bottom: 32px;
                            >div{
                                width: auto;
                                height: auto;
                                >div:first-child{
                                    margin-bottom: 5px;
                                }
                            }
                        }
                        >div:nth-child(5){
                            color: #C5EEF3;
                            font-size: 32px;
                        }
                    }
                }
                .xqTableBox{
                    width: 100%;
                    height: 100%;
                    position: absolute;
                    top: 0;
                    left: 0;
                    background: linear-gradient(180deg,#0a2b3a, #0b1b2a);
                    border: 2px solid;
                    border-image: linear-gradient(0deg, rgba(13,171,149,0.20), #1ed5c7) 2 2;
                    border-radius: 4px;
                    .title{
                        height: 76px;
                        background: linear-gradient(315deg,rgba(22,223,248,0.02), rgba(22,223,248,0.10) 98%);
                        font-size: 30px;
                        color: #5AE8FA;
                        display: flex;
                        align-items: center;
                        justify-content: space-between;
                        padding: 0 20px 0 28px;
                        margin-bottom: 32px;
                        >div:last-child{
                            img{
                                width: 49px;
                                height:49px;
                                cursor: pointer;
                            }
                        }
                    }
                    .cityEvent{
                        width: 100%;
                        height: 1210px;
                        display: flex;
                        overflow: auto;
                        align-items: center;
                        flex-direction: column;
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
                            overflow: hidden !important;
                            padding: 14px 14px 5px 28px;
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
                                    width: 85%;
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
                                    width: 100px;
                                    height: 40px;
                                    display: flex;
                                    justify-content: center;
                                    align-items: center;
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
                            .state{
                              width: 100px;
                              height: 40px;
                              background-image: url('./newBack/13.png');
                              background-size: 100% 100%;
                              display: flex;
                              justify-content: center;
                              color: black;
                              align-items: center;
                            }

                        }
                      }
                    }
                    .Module5Pop{
                      width: 100%;
                      height: 1340px;
                      background: linear-gradient(180deg,#0a2b3a, #0b1b2a);
                      border: 2px solid;
                      border-image: linear-gradient(0deg, rgba(13,171,149,0.20), #1ed5c7) 2 2;
                      border-radius: 4px;
                      position: absolute;
                      top: 0;
                      left: 0;
                      .tjdbBox{
                        width: 100%;
                        height: 650px;
                        position: absolute;
                        top: 154px;
                        left: 0;
                        z-index: 10;
                        cursor: auto;
                        background-image: linear-gradient(45deg, #0A2B3A, #0B1B2A);
                        border: 1px solid #1ED5C7;
                        .titleName{
                          width: 100%;
                          height: 75px;
                          display: flex;
                          justify-content: space-between;
                          align-items: center;
                          background-image: linear-gradient(45deg, rgba(23, 221, 247, 0.02), rgba(23, 221, 247, 0.1));
                          padding: 0 20px;
                        }
                        .bodyChose{
                          width: 100%;
                          height: 475px;
                          overflow: auto;
                          padding: 20px;
                          .checkEdItem{
                            height: 72px;
                            width: 100%;
                            background-image: url('./newBack/19.png');
                            background-size: 100% 100%;
                            color: #5AE8FA;
                            font-size: 30px;
                            margin-bottom: 16px;
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            .ChoseBtn{
                              width: 80px;
                              height: 36px;
                              background-image: url('./newBack/21.png');
                              background-size: 100% 100%;
                              color:#0A2534;
                              cursor: pointer;
                              font-size: 22px;
                            }
                          }
                          .normalItem{
                            height: 72px;
                            width: 100%;
                            background-image: url('./newBack/19.png');
                            background-size: 100% 100%;
                            color: #C5EEF3;
                            font-size: 30px;
                            margin-bottom: 16px;
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            .ChoseBtn{
                              width: 80px;
                              height: 36px;
                              background-image: url('./newBack/20.png');
                              background-size: 100% 100%;
                              color:#16DFF8;
                              cursor: pointer;
                              font-size: 22px;
                            }
                          }
                        }
                        .footBox{
                          width: 100%;
                          height: 100px;
                          display: flex;
                          justify-content: space-between;
                          background-image: linear-gradient(45deg, rgba(23, 221, 247, 0.02), rgba(23, 221, 247, 0.1));
                          padding: 0 20px;
                          align-items: center;
                          .SureBtn{
                            height: 50px;
                            width: 120px;
                            background-image: url('./newBack/22.png');
                            font-size: 28px;
                            cursor: pointer;
                            color: #0B1B2A;
                          }
                        }
                      }
                    }
                }
            }
        }
    }
    #Module2{
        width: 2140px;
        height: 1512px;
        margin: 108px 32px 0 32px;
        padding: 32px 0 32px 0;
        position: relative;
        overflow: hidden;
        .row1{
            width: 100%;
            display: flex;
            justify-content: space-between;
            position: relative;
            .selectTitle{
              position: absolute;
              top: 20px;
              left: 850px;
              display: flex;
              .search{
                height: 27px;
                width: 30px;
                margin-left: 10px;
                background-image: url('./127.png');
                cursor: pointer;
                background-size: 100%;
              }
            }
            .listArr{
                width: 1250px;
                height: 692px;
                margin-right: 30px;
                background: url('./background/编组_11.png');
                padding: 105px 0px 14px 36px;
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
                            height: 524px;
                            overflow-y: scroll;
                            .li{
                                height: 92px;
                                position: relative;
                                margin-bottom: 8px;
                                .info1{
                                    width: 100%;
                                    height:100%;
                                    background: url('./background/编组_12.png');
                                    background-size: 100% 100%;
                                    display: flex;
                                    align-items: center;
                                    justify-content: space-between;
                                    >div:nth-child(1){
                                        color: #5ae8fa;
                                        font-size: 30px;
                                        font-weight: bold;
                                        display: flex;
                                        align-items: center;
                                        text-align: center;
                                        width: 15%;
                                        >span{
                                            display: inline-block;
                                            background: #fcb83c;
                                            width: 20px;
                                            height: 20px;
                                            border-radius: 50%;
                                            margin-right: 10px;
                                        }
                                    }
                                    >div:nth-child(2){
                                        color: #c5eef3;
                                        font-size: 32px;
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
                                            font-size: 28px;
                                            color: rgba(197,238,243,1);
                                            margin-right: 7px;
                                        }
                                        >div:last-child{
                                            font-size: 32px;
                                            color: rgba(197,238,243,1);
                                        }
                                    }
                                    >div:nth-child(4){
                                        color:#16DFF8;
                                        font-size: 30px;
                                        width: 100px;
                                        height: 40px;
                                        display: flex;
                                        justify-content: center;
                                        align-items: center;
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
                                    background: linear-gradient(180deg,#0a2b3a, #0b1b2a);
                                    border: 2px solid;
                                    border-image: linear-gradient(0deg, rgba(13,171,149,0.20), rgba(30,213,199,0.20)) 2 2;
                                    border-radius: 4px;
                                    display: flex;
                                    align-items: center;
                                    justify-content: space-between;
                                    padding: 0 25px;
                                    >div:nth-child(1) {
                                        >div:first-child {
                                            font-size: 32px;
                                            margin-bottom: 4px;
                                            color: rgba(90,232,250,1);
                                        }
                                        >div:last-child {
                                            font-size: 28px;
                                            color: rgba(197,238,243,1);
                                        }
                                    }
                                    >div:nth-child(2) {
                                        >div:first-child {
                                            font-size: 34px;
                                            color: rgba(252,184,60,1);
                                            margin-bottom: 4px;
                                            font-weight: bold;
                                        }
                                        >div:last-child {
                                            font-size: 28px;
                                            color: rgba(197,238,243,1);
                                        }
                                    }
                                    >div:nth-child(3) {
                                        >div:first-child {
                                            font-size: 34px;
                                            color: rgba(220,97,79,1);
                                            margin-bottom: 4px;
                                            font-weight: bold;
                                        }
                                        >div:last-child {
                                            font-size: 28px;
                                            color: rgba(197,238,243,1);
                                        }
                                    }
                                    >div:nth-child(4) {
                                        img{
                                            width: 26px;
                                            height: 26px;
                                            cursor: pointer;
                                        }
                                    }
                                }
                            }
                            >.li:nth-child(1){
                                .info1{
                                    background: url('./background/1.png');
                                    background-size: 100% 100%;
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
                                    background-size: 100% 100%;
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
                                    background-size: 100% 100%;
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

                .sqTableBox{
                    width: 100%;
                    height: 100%;
                    position: absolute;
                    top: 0;
                    z-index: 10;
                    left: 0;
                    background: linear-gradient(180deg,#0a2b3a, #0b1b2a);
                    border: 2px solid;
                    border-image: linear-gradient(0deg, rgba(13,171,149,0.20), #1ed5c7) 2 2;
                    border-radius: 4px;
                    .title{
                        height: 76px;
                        background: linear-gradient(315deg,rgba(22,223,248,0.02), rgba(22,223,248,0.10) 98%);
                        font-size: 30px;
                        color: #5AE8FA;
                        display: flex;
                        align-items: center;
                        justify-content: space-between;
                        padding: 0 20px 0 28px;
                        margin-bottom: 32px;
                        >div:last-child{
                            img{
                                width: 49px;
                                height:49px;
                                cursor: pointer;
                            }
                        }
                    }
                    .cityEvent{
                        width: 100%;
                        height: 570px;
                        display: flex;
                        overflow: auto;
                        align-items: center;
                        flex-direction: column;
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
                            overflow: hidden !important;
                            padding: 14px 14px 5px 28px;
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
                                    width: 85%;
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
                                    width: 100px;
                                    height: 40px;
                                    display: flex;
                                    justify-content: center;
                                    align-items: center;
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
                            .state{
                              width: 100px;
                              height: 40px;
                              background-image: url('./newBack/13.png');
                              background-size: 100% 100%;
                              display: flex;
                              justify-content: center;
                              color: black;
                              align-items: center;
                            }

                        }
                      }
                    }
                    .Module5Pop{
                      width: 100%;
                      height: 100%;
                      background: linear-gradient(180deg,#0a2b3a, #0b1b2a);
                      border: 2px solid;
                      border-image: linear-gradient(0deg, rgba(13,171,149,0.20), #1ed5c7) 2 2;
                      border-radius: 4px;
                      position: absolute;
                      top: 0;
                      left: 0;
                    }
                }
            }
            .ConditionalList{
                width: 824px;
                height: 692px;
                padding: 104px 37px 0px 36px;
                background: url('./background/编组20.png');
                // .ball{
                //     width: 100%;
                //     display: flex;
                //     flex-wrap: wrap;
                //     align-items: center;
                //     justify-content: space-between;
                //     .normal1{
                //         width: 180px;
                //         height: 52px;
                //         cursor: pointer;
                //         color: rgba(22,223,248,1);
                //         font-size: 22px;
                //         border-radius: 4px;
                //         text-align: center;
                //         line-height: 52px;
                //         margin-bottom: 20px;
                //         background: url('./background/矩形_1.png');
                //     }
                //     .active1{
                //         background: url('./background/矩形.png') !important;
                //         color: rgba(255,244,223,1) !important;
                //     }
                // }
                // 诉求分类选择按钮盒子
                .selectBox {
                  display: flex;
                  font-size: 26px;
                  flex-wrap: wrap;
                  row-gap: 23px;
                  column-gap: 23px;
                  margin-bottom: 27px;
                  .select-btn {
                    width: 170px;
                    text-align: center;
                    padding: 15px 0px;
                    border: 1px solid rgb(22, 223, 248);
                    background: repeating-linear-gradient(to right, #022039, #25656c9c, #022039);
                    border-radius: 5px;
                    color: #16DFF8;
                    cursor: pointer;
                  }
                  .active-btn {
                    color: #fff;
                    border: 1px solid #ffb120;
                    background: repeating-linear-gradient(to right,#a37e3c70, #fcb83ca6,#a37e3c70);
                  }
                }
                >.title{
                    color: rgba(197,238,243,1);
                    font-size: 26px;
                    margin: 0 0 20px 0;
                    height: 40px;
                    font-weight: bold;
                    display: flex;
                    justify-content: space-between;
                }
                >div:last-child{
                    position: relative;
                    overflow: hidden;
                    .list{
                        width: 100%;
                        .lhead{
                            width: 100%;
                            height: 48px;
                            display: flex;
                            align-items: center;
                            justify-content: space-around;
                            color: rgba(22,223,248,1);
                            font-size: 26px;
                            font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 2.0-Medium;
                            font-weight: 500;
                            text-align: center;
                            background: linear-gradient(270deg,rgba(22,223,248,0.03), rgba(22,223,248,0.06) 50%, rgba(22,223,248,0.03));
                            >div:nth-child(1){
                                    width: 50%;
                            }
                            >div:nth-child(2){
                                    width: 50%;
                            }
                            // >div:nth-child(1){
                            //         width: 10%;
                            // }
                            // >div:nth-child(2){
                            //         width: 30%;
                            // }
                            // >div:nth-child(3){
                            //         width: 20%;
                            // }
                            // >div:nth-child(4){
                            //         width: 5%;
                            // }
                        }
                        .lbody{
                            width: 100%;
                            // height: 480px;
                            // overflow-y: scroll;
                            .rows{
                                width: 100%;
                                height: 70px;
                                display: flex;
                                align-items: center;
                                justify-content: space-around;
                                >div:nth-child(1){
                                    font-size: 28px;
                                    font-family: TeX Gyre Adventor, TeX Gyre Adventor-Bold;
                                    font-weight: 700;
                                    text-align: center;
                                    color: #d8f4f7;
                                    width: 50%;
                                }
                                >div:nth-child(2){
                                    width: 50%;
                                    font-family: Source Han Sans SC, Source Han Sans SC-Regular;
                                    font-weight: 400;
                                    display: flex;
                                    align-items: center;
                                    justify-content: center;
                                    div {
                                      width: fit-content;
                                      color: #16dff8;
                                      display: flex;
                                      font-size: 24px;
                                      justify-content: center;
                                      align-items: center;
                                      border:1px solid rgba(22,223,248,0.6);
                                      border-radius: 30px;
                                      text-align: center;
                                      cursor: pointer;
                                      padding: 0px 20px
                                    }
                                }
                                // >div:nth-child(1){
                                //     font-size: 28px;
                                //     font-family: TeX Gyre Adventor, TeX Gyre Adventor-Bold;
                                //     font-weight: 700;
                                //     text-align: center;
                                //     color: #d8f4f7;
                                //     width: 10%;
                                // }
                                // >div:nth-child(2){
                                //     font-size: 28px;
                                //     font-family: TeX Gyre Adventor, TeX Gyre Adventor-Bold;
                                //     font-weight: 400;
                                //     text-align: center;
                                //     color: #d8f4f7;
                                //     width: 30%;
                                // }
                                // >div:nth-child(3){
                                //     font-size: 28px;
                                //     font-family: TeX Gyre Adventor, TeX Gyre Adventor-Bold;
                                //     font-weight: 700;
                                //     text-align: center;
                                //     color: #d8f4f7;
                                //     width: 20%;
                                // }
                                // >div:nth-child(4){
                                //     width: 10%;
                                //     font-size: 28px;
                                //     font-family: Source Han Sans SC, Source Han Sans SC-Regular;
                                //     font-weight: 400;
                                //     text-align: center;
                                //     color: #16dff8;
                                // }
                            }
                            .rows:nth-child(even){
                                background: linear-gradient(270deg,rgba(22,223,248,0.03), rgba(22,223,248,0.06) 50%, rgba(22,223,248,0.03));
                            }
                        }
                    }
                    .pop1{
                            width: 100%;
                            height: 100%;
                            position: absolute;
                            top: 0;
                            left: 0;
                            background: linear-gradient(180deg,#0a2b3a, #0b1b2a);
                            border: 2px solid;
                            border-image: linear-gradient(0deg, rgba(13,171,149,0.20), #1ed5c7) 2 2;
                            border-radius: 4px;
                            .return{
                                width: 100%;
                                height: 76px;
                                background: linear-gradient(315deg,rgba(22,223,248,0.02), rgba(22,223,248,0.10) 98%);
                                padding: 0 37px 0 28px;
                                display: flex;
                                justify-content: space-between;
                                align-items: center;
                                color: rgba(90,232,250,1);
                                font-size: 30px;
                                margin-bottom: 32px;
                                >img{
                                    width: 35px;
                                    height: 35px;
                                    cursor: pointer;
                                }
                            }
                            >div:last-child{
                                display: flex;
                                justify-content: space-between;
                                align-items: center;
                                padding: 0 28px;
                                .left{
                                    width: 55%;
                                    height: 320px;
                                    overflow-y:scroll;
                                    display: flex;
                                    flex-wrap: wrap;
                                    align-items: center;
                                    >div{
                                        margin-bottom: 30px;
                                        margin-right: 12px;
                                        .key{
                                            color: rgba(197,238,243,1);
                                            font-size: 26px;
                                        }
                                        .value{
                                            margin-bottom: 10px;
                                            font-size: 26px;
                                            font-weight: bold;
                                        }
                                    }
                                }
                                .right{
                                    width: 40%;
                                    height: 280px;
                                    .title2{
                                        margin-bottom: 12px;
                                        font-size: 24px;
                                        color: rgba(197,238,243,1);
                                    }
                                    .list2{
                                        width: 100%;
                                        height: 242px;
                                        overflow-y: scroll;
                                        font-size: 20px;
                                        padding-right: 10px;
                                        .li2{
                                            display: flex;
                                            padding: 0px 10px;
                                            align-items: center;
                                            justify-content: space-between;
                                            border-top: 2px solid;
                                            border-bottom: 2px solid;
                                            border-image: linear-gradient(297deg,rgba(23,237,186,0.00) 17%, rgba(22,244,191,0.60) 87%, rgba(28,240,188,0.00)) 1;
                                            >div:first-child{
                                                width: 30%;
                                                text-align: center;
                                                color: rgba(216,244,247,1);
                                                font-size: 20px;
                                            }
                                            >div:last-child{
                                                width: 60%;
                                                text-align: center;
                                                color: rgba(216,244,247,1);
                                                font-size: 20px;
                                                font-weight: bold;
                                            }
                                            >div:nth-child(3){
                                                width: 10%;
                                                text-align: center;
                                                color: #16DFF8;
                                                font-size: 20px;
                                            }
                                        }
                                        >.li2:nth-child(even){
                                             background:linear-gradient(270deg,rgba(22,223,248,0.03), rgba(22,223,248,0.06) 50%, rgba(22,223,248,0.03));
                                        }
                                    }
                                }
                            }
                    }
                    .pop2{
                            width: 100%;
                            height: 100%;
                            position: absolute;
                            top: 0;
                            left: 0;
                            background: linear-gradient(180deg,#0a2b3a, #0b1b2a);
                            border: 2px solid;
                            border-image: linear-gradient(0deg, rgba(13,171,149,0.20), #1ed5c7) 2 2;
                            border-radius: 4px;
                            .return{
                                width: 100%;
                                height: 76px;
                                background: linear-gradient(315deg,rgba(22,223,248,0.02), rgba(22,223,248,0.10) 98%);
                                padding: 0 37px 0 28px;
                                display: flex;
                                justify-content: space-between;
                                align-items: center;
                                color: rgba(90,232,250,1);
                                font-size: 30px;
                                margin-bottom: 32px;
                                >img{
                                    width: 35px;
                                    height: 35px;
                                    cursor: pointer;
                                }
                            }
                            >div:last-child{
                                display: flex;
                                justify-content: space-between;
                                flex-wrap: wrap;
                                align-items: center;
                                padding: 0 28px 20px 28px;
                                height: 314px;
                                .left{
                                    width: 44%;
                                    height: 100%;
                                    overflow-y:scroll;
                                    font-size: 24px;
                                    font-weight: bold;
                                    >div{
                                        margin-bottom: 30px;
                                        .key{
                                            color: rgba(197,238,243,1);
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
                                    .title2{
                                        margin-bottom: 12px;
                                        font-size: 24px;
                                        color: rgba(197,238,243,1);
                                    }
                                }
                            }
                    }
                }
            }
        }
        .row2{
            width: 100%;
            height: 346px;
            background: url('./background/编组17.png');
            padding: 100px 32px 32px 36px;
            display: flex;
            margin-top: 30px;
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
                        width: 1840px;
                        height: 214px;
                        overflow-x: scroll;
                        overflow-y: hidden;
                        white-space: nowrap;
                        .col{
                            display:inline-flex;
                            width: 280px;
                            height: 100%;
                            background: url('./background/编组_13.png');
                            margin-right: 28px;
                            text-align: center;
                            flex-wrap: wrap;
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
                                font-size: 22px;
                                font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 2.0-Regular;
                                font-weight: 400;
                                text-align: right;
                                cursor: pointer;
                                color: rgba(22,223,248,1);
                                position: absolute;
                                top: 12px;
                                right: 16px;
                            }
                            >div:nth-child(3) {
                                width: 100%;
                                height: auto;
                                font-size: 24px;
                                font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 2.0-Medium;
                                font-weight: 500;
                                text-align: center;
                                color: #C5EEF3;
                                margin-top:53px;
                                margin-bottom: 30px;
                            }
                            >div:nth-child(4) {
                                width: 280px;
                                height: 100px;
                                display: flex;
                                justify-content: space-around;
                                width: 100%;
                                >div:first-child{
                                        >div:first-child{
                                            font-size: 24px;
                                            font-family: TeX Gyre Adventor, TeX Gyre Adventor-Bold;
                                            font-weight: 700;
                                            color: #ff644d;
                                            margin-bottom: 7px;
                                        }
                                        >div:last-child{
                                            font-size: 22px;
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
                                            font-size: 22px;
                                            font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 2.0-Regular;
                                            font-weight: 400;
                                            color: #c5eef3;
                                        }
                                }
                            }
                        }
                }
                .lunbo2,.lunbo3{
                        width: 2487px;
                        height: 214px;
                        overflow-x: scroll;
                        overflow-y: hidden;
                        white-space: nowrap;
                        .col{
                            display:inline-flex;
                            width: 280px;
                            height: 100%;
                            background: url('./background/编组_13.png');
                            margin-right: 28px;
                            text-align: center;
                            position: relative;
                            flex-wrap: wrap;
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
                                margin-bottom: 30px;
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
            }
        }
        .row3{
            width: 100%;
            height: 346px;
            display: flex;
            margin-top: 30px;
            align-items: center;
            background: url('./background/矩形备份11.png');
            >div:first-child{
                font-size: 44px;
                font-family: TeX Gyre Adventor, TeX Gyre Adventor-Bold;
                font-weight: 700;
                // text-align: center;
                color: #c5eef3;
                width: 300px;
                height: 346px;
                background: url('./background/编组_18.png');
                margin-right: 28px;
                display: flex;
                flex-wrap: wrap;
                div:nth-child(1){
                  width: 100%;
                  height: 60%;
                  display: flex;
                  justify-content: center;
                  align-items: flex-end;
                }
                div:nth-child(2){
                  width: 100%;
                  height: 40%;
                  display: flex;
                  justify-content: center;
                  align-items: flex-start;
                }
            }
            >div:last-child{
                width: 1780px;
                height: 280px;
                position: relative;
                overflow: hidden;
                .lunbo{
                    width: 100%;
                    height: 100%;
                    overflow-x: scroll;
                    overflow-y: hidden;
                    white-space: nowrap;
                    .col{
                        width: 260px;
                        height: 280px;
                        margin-right: 28px;
                        // padding: 10px;
                        display: inline-flex;
                        flex-wrap: wrap;
                        flex-direction: column;
                        // align-items: center;
                        justify-content: space-around;
                        position: relative;
                        background: url('./background/编组19.png');
                        >div:nth-child(1) {
                            text-align: center;
                            >img{
                                width: 100px;
                                height: 100px;
                            }
                        }
                        >div:nth-child(2) {
                            width: 100%;
                            font-size: 24px;
                            font-family: Alibaba PuHuiTi 2.0, Alibaba PuHuiTi 2.0-Medium;
                            font-weight: 500;
                            text-align: center;
                            color: #c5eef3;
                        }
                        >div:nth-child(3) {
                            width: 100%;
                            display: flex;
                            align-items: center;
                            justify-content: space-around;
                            >div:nth-child(1){
                                color: #5AE8FA;
                                font-size: 28px;
                                font-weight: bold;
                            }
                            >div:nth-child(2) {
                                color:#16DFF8;
                                font-size: 20px;
                                width: 100px;
                                height: 40px;
                                display: flex;
                                justify-content: center;
                                align-items: center;
                                border:1px solid rgba(22,223,248,0.6);
                                border-radius: 16px;
                                text-align: center;
                            }
                        }
                    }
                }
            }
        }
        .popp1{
                        position: absolute;
                        bottom: 24px;
                        left: 0;
                        width: 2140px;
                        height: 650px;
                        background: linear-gradient(180deg,#0a2b3a, #0b1b2a);
                        border: 2px solid;
                        border-image: linear-gradient(0deg, rgba(13,171,149,0.20), #1ed5c7) 2 2;
                        border-radius: 4px;
                        .title2{
                            color: rgba(197,238,243,1);
                            font-size: 28px;
                        }
                        .return{
                            img{
                                width: 49px;
                                height: 49px;
                                position: absolute;
                                top:8px;
                                right:10px;
                                cursor: pointer;
                            }
                        }
                        .content1{
                            width: 100%;
                            height: 100%;
                            padding: 30px;
                            display: flex;
                            align-items: center;
                            justify-content: space-between;
                            flex-wrap: wrap;
                            .con1{
                              width: 35%;
                              height:100%;
                              .info{
                                height: 45%;
                                display: flex;
                                align-items: center;
                                justify-content: space-between;
                                >div:first-child{
                                  width: 48%;
                                  height:100%;
                                  overflow-y: scroll;
                                  padding: 12px;
                                  background: linear-gradient(270deg,rgba(22,223,248,0.04), rgba(22,223,248,0.10) 50%, rgba(22,223,248,0.04));
                                  .field{
                                      margin-bottom: 15px;
                                      >div:first-child{
                                          color: rgba(90,232,250,1);
                                          font-size: 30px;
                                          white-space: pre-wrap;
                                      }
                                      >div:last-child{
                                          color: rgba(197,238,243,1);
                                          font-size: 26px;
                                      }
                                  }
                                }
                                >div:last-child{
                                  width: 48%;
                                  height:100%;
                                  background: linear-gradient(270deg,rgba(22,223,248,0.04), rgba(22,223,248,0.10) 50%, rgba(22,223,248,0.04));
                                  >div:first-child{
                                      color: rgba(255,184,63,1);
                                      font-size: 40px;
                                      font-weight: bold;
                                      width: 100%;
                                      text-align: center;
                                      margin: 40px 0 20px 0;
                                  }
                                  >div:last-child{
                                      width: 100%;
                                      font-size: 30px;
                                      text-align: center;
                                  }
                                }
                              }
                              >div:last-child{
                                height: 54%;
                                margin-top: 8px;
                              }
                            }
                            >div:nth-child(2){
                              width: 30%;
                              height:100%;
                              .list2{
                                    margin-top: 5px;
                                    height: calc(100% - 50px);
                                    width:100%;
                                    overflow-y: scroll;
                                    .li2{
                                        height: 70px;
                                        width: 100%;
                                        display: flex;
                                        align-items: center;
                                        justify-content: space-between;
                                        border-top: 1px solid;
                                        border-bottom: 1px solid;
                                        border-image: linear-gradient(297deg,rgba(23,237,186,0.00) 17%, rgba(22,244,191,0.60) 87%, rgba(28,240,188,0.00)) 1;
                                        >div:nth-child(1){
                                            width: 20%;
                                            text-align: center;
                                            color: rgba(216,244,247,1);
                                            font-size: 28px;
                                            font-weight: bold;
                                        }
                                        >div:nth-child(2){
                                            width: 50%;
                                            text-align: center;
                                            color: rgba(216,244,247,1);
                                            font-size: 28px;
                                        }
                                        >div:nth-child(3){
                                            width: 30%;
                                            text-align: center;
                                            color: rgba(216,244,247,1);
                                            font-size: 28px;
                                            font-weight: bold;
                                        }
                                    }
                                    >.li2:nth-child(even){
                                        background: linear-gradient(270deg,rgba(22,223,248,0.03), rgba(22,223,248,0.06) 50%, rgba(22,223,248,0.03));
                                    }
                                }
                            }
                            >div:nth-child(3){
                              width: 30%;
                              height:100%;
                              .list2{
                                    margin-top: 5px;
                                    height: calc(100% - 50px);
                                    width:100%;
                                    overflow-y: scroll;
                                    .li2{
                                        height: 70px;
                                        width: 100%;
                                        display: flex;
                                        align-items: center;
                                        justify-content: space-between;
                                        border-top: 1px solid;
                                        border-bottom: 1px solid;
                                        border-image: linear-gradient(297deg,rgba(23,237,186,0.00) 17%, rgba(22,244,191,0.60) 87%, rgba(28,240,188,0.00)) 1;
                                        >div:nth-child(1){
                                            width: 30%;
                                            text-align: center;
                                            color: rgba(216,244,247,1);
                                            font-size: 28px;
                                        }
                                        >div:nth-child(2){
                                            width: 55%;
                                            text-align: center;
                                            color: rgba(216,244,247,1);
                                            font-size: 28px;
                                            font-weight: bold;
                                        }
                                        >div:nth-child(3){
                                            width: 15%;
                                            text-align: center;
                                            color: #16DFF8;
                                            font-size: 28px;
                                        }
                                    }
                                    >.li2:nth-child(even){
                                        background: linear-gradient(270deg,rgba(22,223,248,0.03), rgba(22,223,248,0.06) 50%, rgba(22,223,248,0.03));
                                    }
                                }
                            }
                        }
                        .content2{
                            width: 100%;
                            height: 100%;
                            padding: 30px;
                            display: flex;
                            align-items: center;
                            justify-content: space-between;
                            flex-wrap: wrap;
                            >div:nth-child(1){
                                height: 100%;
                                width: 30%;
                                overflow-y: scroll;
                                .field{
                                    margin-bottom: 15px;
                                    >div:first-child{
                                        color: rgba(90,232,250,1);
                                        font-size: 32px;
                                        white-space: pre-wrap;
                                    }
                                    >div:last-child{
                                        color: rgba(197,238,243,1);
                                        font-size: 30px;
                                    }
                                }
                            }
                            >div:nth-child(2){
                                width: 35%;
                                height: 100%;
                                .list2{
                                    margin-top: 5px;
                                    height: calc(100% - 50px);
                                    width:100%;
                                    .li2{
                                        height: 70px;
                                        width: 100%;
                                        display: flex;
                                        align-items: center;
                                        justify-content: space-between;
                                        border-top: 1px solid;
                                        border-bottom: 1px solid;
                                        border-image: linear-gradient(297deg,rgba(23,237,186,0.00) 17%, rgba(22,244,191,0.60) 87%, rgba(28,240,188,0.00)) 1;
                                        >div:nth-child(1){
                                            width: 20%;
                                            text-align: center;
                                            color: rgba(216,244,247,1);
                                            font-size: 28px;
                                            font-weight: bold;
                                        }
                                        >div:nth-child(2){
                                            width: 50%;
                                            text-align: center;
                                            color: rgba(216,244,247,1);
                                            font-size: 28px;
                                        }
                                        >div:nth-child(3){
                                            width: 30%;
                                            text-align: center;
                                            color: rgba(216,244,247,1);
                                            font-size: 28px;
                                            font-weight: bold;
                                        }
                                    }
                                    >.li2:nth-child(even){
                                        background: linear-gradient(270deg,rgba(22,223,248,0.03), rgba(22,223,248,0.06) 50%, rgba(22,223,248,0.03));
                                    }
                                }
                            }
                            >div:nth-child(3){
                                width: 30%;
                                height: 100%;
                                .list2{
                                    margin-top: 5px;
                                    height: calc(100% - 50px);
                                    width:100%;
                                    .li2{
                                        height: 70px;
                                        width: 100%;
                                        display: flex;
                                        align-items: center;
                                        justify-content: space-between;
                                        border-top: 1px solid;
                                        border-bottom: 1px solid;
                                        border-image: linear-gradient(297deg,rgba(23,237,186,0.00) 17%, rgba(22,244,191,0.60) 87%, rgba(28,240,188,0.00)) 1;
                                        >div:nth-child(1){
                                            width: 30%;
                                            text-align: center;
                                            color: rgba(216,244,247,1);
                                            font-size: 28px;
                                        }
                                        >div:nth-child(2){
                                            width: 60%;
                                            text-align: center;
                                            color: rgba(216,244,247,1);
                                            font-size: 28px;
                                            font-weight: bold;
                                        }
                                        >div:nth-child(3){
                                            width: 10%;
                                            text-align: center;
                                            color: #16DFF8;
                                            font-size: 28px;
                                        }
                                    }
                                    >.li2:nth-child(even){
                                        background: linear-gradient(270deg,rgba(22,223,248,0.03), rgba(22,223,248,0.06) 50%, rgba(22,223,248,0.03));
                                    }
                                }
                            }
                        }
        }
        .popp2{
                            width: 2140px;
                            height: 650px;
                            position: absolute;
                            bottom: 24px;
                            left: 0;
                            background: linear-gradient(180deg,#0a2b3a, #0b1b2a);
                            border: 2px solid;
                            border-image: linear-gradient(0deg, rgba(13,171,149,0.20), #1ed5c7) 2 2;
                            border-radius: 4px;
                            .return{
                                img{
                                    width: 49px;
                                    height: 49px;
                                    position: absolute;
                                    top:8px;
                                    right:10px;
                                    cursor: pointer;
                                }
                            }
                            >div:last-child{
                                display: flex;
                                justify-content: space-between;
                                flex-wrap: wrap;
                                align-items: center;
                                padding: 30px;
                                height: 100%;
                                .left{
                                    width: 50%;
                                    height: 100%;
                                    overflow-y:scroll;
                                    font-size: 36px;
                                    font-weight: bold;
                                    >div{
                                        margin-bottom: 30px;
                                        .key{
                                            color: rgba(197,238,243,1);
                                            margin-right: 25px;
                                        }
                                        .value{
                                            color: #87e9ff;
                                        }
                                    }
                                }
                                .right{
                                    width: 50%;
                                    height: 100%;
                                    .title2{
                                        margin-bottom: 12px;
                                        font-size: 30px;
                                        color: rgba(197,238,243,1);
                                    }
                                }
                            }
        }
        .poppp1{
                    position: absolute;
                    bottom: 24px;
                    left: 0;
                    width: 2140px;
                    height: 650px;
                    display: flex;
                    align-items: center;
                    background: linear-gradient(180deg,#0a2b3a, #0b1b2a);
                    border: 2px solid;
                    border-image: linear-gradient(0deg, rgba(13,171,149,0.20), #1ed5c7) 2 2;
                    border-radius: 4px;
                    padding: 10px 20px;
                    .title2{
                        color: rgba(197,238,243,1);
                        font-size: 30px;
                    }
                    .return{
                        img{
                            width: 49px;
                            height: 49px;
                            position: absolute;
                            top:8px;
                            right:10px;
                            cursor: pointer;
                        }
                    }
                    .column1{
                        width: 360px;
                        height: 100%;
                        margin-right: 40px;
                        .list1{
                            width: 100%;
                            height: calc(100% - 50px);
                            overflow-y: scroll;
                            color: #D8F4F7;
                            margin-top:5px;
                            .li1{
                                        height: 70px;
                                        width: 100%;
                                        display: flex;
                                        align-items: center;
                                        justify-content: space-between;
                                        border-top: 1px solid;
                                        border-bottom: 1px solid;
                                        border-image: linear-gradient(297deg,rgba(23,237,186,0.00) 17%, rgba(22,244,191,0.60) 87%, rgba(28,240,188,0.00)) 1;
                                        >div:nth-child(1){
                                            width: 20%;
                                            text-align: center;
                                            color: rgba(216,244,247,1);
                                            font-size: 28px;
                                            font-weight: bold;
                                        }
                                        >div:nth-child(2){
                                            width: 60%;
                                            text-align: center;
                                            color: rgba(216,244,247,1);
                                            font-size: 28px;
                                        }
                                        >div:nth-child(3){
                                            width: 20%;
                                            text-align: center;
                                            color: rgba(216,244,247,1);
                                            font-size: 28px;
                                            font-weight: bold;
                                        }
                                }
                        }
                    }
                    .column2{
                        width: 360px;
                        height: 100%;
                        margin-right: 40px;
                        overflow: hidden;
                        position: relative;
                        .title2{
                            >span{
                                color:#FFB83F;
                                font-size: 30px;
                                margin-left: 4px;
                                font-weight: bold;
                            }
                        }
                        .list2{
                                width: 100%;
                                height: calc(100% - 50px);
                                overflow-y: scroll;
                                color: #D8F4F7;
                                margin-top:5px;
                                .li2{
                                            height: 70px;
                                            width: 100%;
                                            display: flex;
                                            align-items: center;
                                            justify-content: space-between;
                                            border-top: 1px solid;
                                            border-bottom: 1px solid;
                                            border-image: linear-gradient(297deg,rgba(23,237,186,0.00) 17%, rgba(22,244,191,0.60) 87%, rgba(28,240,188,0.00)) 1;
                                            >div:nth-child(1){
                                                width: 20%;
                                                text-align: center;
                                                color: rgba(216,244,247,1);
                                                font-size: 28px;
                                            }
                                            >div:nth-child(2){
                                                width: 60%;
                                                text-align: center;
                                                color: rgba(216,244,247,1);
                                                font-size: 28px;
                                                font-weight: bold;
                                            }
                                            >div:nth-child(3){
                                                width: 20%;
                                                text-align: center;
                                                color: #16DFF8;
                                                font-size: 28px;
                                                cursor: pointer;
                                            }
                                    }
                        }
                        .pop2{
                                width: 100%;
                                height: 100%;
                                padding: 10px 15px;
                                position: absolute;
                                top: 0;
                                left: 0;
                                overflow-y:scroll;
                                font-size: 28px;
                                font-weight: bold;
                                background: linear-gradient(180deg,#0a2b3a, #0b1b2a);
                                border: 2px solid;
                                border-image: linear-gradient(0deg, rgba(13,171,149,0.20), #1ed5c7) 2 2;
                                border-radius: 4px;
                                .return{
                                    img{
                                        width: 26px;
                                        height: 26px;
                                        position: absolute;
                                        top:8px;
                                        right:10px;
                                        cursor: pointer;
                                    }
                                    margin-bottom: 0px !important;
                                }
                                >div{
                                    margin-bottom: 15px;
                                    .key{
                                        color: #C5EEF3;
                                        margin-right: 10px;
                                    }
                                    .value{
                                        color: #5AE8FA;
                                    }
                                }
                        }
                    }
                    .column3{
                        width: 360px;
                        height: 100%;
                        margin-right: 40px;
                        .title2{
                            >span:first-child{
                                color:#FFB83F;
                                font-size: 24px;
                                margin:0 4px;
                                font-weight: bold;
                            }
                            >span:last-child{
                                color:#16DFF8;
                                font-size: 24px;
                                cursor: pointer;
                            }
                        }
                        >div:last-child{
                            overflow: hidden;
                            position: relative;
                            height: calc(100% - 50px);
                            .list3{
                                width: 100%;
                                height:100%;
                                overflow-y: scroll;
                                color: #D8F4F7;
                                margin-top:5px;
                                .li3{
                                            height: 70px;
                                            width: 100%;
                                            display: flex;
                                            align-items: center;
                                            justify-content: space-between;
                                            border-top: 1px solid;
                                            border-bottom: 1px solid;
                                            border-image: linear-gradient(297deg,rgba(23,237,186,0.00) 17%, rgba(22,244,191,0.60) 87%, rgba(28,240,188,0.00)) 1;
                                            >div:nth-child(1){
                                                width: 70%;
                                                text-align: center;
                                                color: rgba(216,244,247,1);
                                                font-size: 28px;
                                            }
                                            >div:nth-child(2){
                                                width: 30%;
                                                text-align: center;
                                                color: rgba(216,244,247,1);
                                                font-size: 28px;
                                                font-weight: bold;
                                            }
                                    }
                            }
                        }
                    }
                    .column4{
                        width: 200px;
                        height: 500px;
                        margin-right: 40px;
                        background: linear-gradient(270deg,rgba(22,223,248,0.04), rgba(22,223,248,0.10) 50%, rgba(22,223,248,0.04));
                        >div:first-child{
                            font-size: 30px;
                            color: #FFB83F;
                            font-weight: bold;
                            width: 100%;
                            text-align: center;
                            margin-top: 70px;
                            margin-bottom: 20px;
                        }
                        >div:last-child{
                            font-size: 28px;
                            color: #C5EEF3;
                            width: 100%;
                            text-align: center;
                        }
                    }
                    .column5{
                        width: auto;
                        height: 100%;
                        >div:first-child{
                            margin-bottom: 5px;
                        }
                        .eline{
                            width: auto;
                            height: auto;
                        }
                    }
        }
        .gsqxqTableBox{
            width: 2140px;
            height: 650px;
            position: absolute;
            bottom: 24px;
            z-index: 10;
            left: 0;
            background: linear-gradient(180deg,#0a2b3a, #0b1b2a);
            border: 2px solid;
            border-image: linear-gradient(0deg, rgba(13,171,149,0.20), #1ed5c7) 2 2;
            border-radius: 4px;
            .title{
                height: 76px;
                background: linear-gradient(315deg,rgba(22,223,248,0.02), rgba(22,223,248,0.10) 98%);
                font-size: 30px;
                color: #5AE8FA;
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 0 20px 0 28px;
                margin-bottom: 32px;
                >div:last-child{
                    img{
                        width: 49px;
                        height:49px;
                        cursor: pointer;
                    }
                }
            }
            .cityEvent{
                width: 100%;
                height: 530px;
                display: flex;
                overflow: auto;
                align-items: center;
                flex-direction: column;
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
                    overflow: hidden !important;
                    padding: 14px 14px 5px 28px;
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
                            width: 85%;
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
                            width: 100px;
                            height: 40px;
                            display: flex;
                            justify-content: center;
                            align-items: center;
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
                    .state{
                      width: 100px;
                      height: 40px;
                      background-image: url('./newBack/13.png');
                      background-size: 100% 100%;
                      display: flex;
                      justify-content: center;
                      color: black;
                      align-items: center;
                    }

                }
              }
            }
            .Module5Pop{
              width: 100%;
              height: 100%;
              background: linear-gradient(180deg,#0a2b3a, #0b1b2a);
              border: 2px solid;
              border-image: linear-gradient(0deg, rgba(13,171,149,0.20), #1ed5c7) 2 2;
              border-radius: 4px;
              position: absolute;
              top: 0;
              left: 0;
              .tjdbBox{
                        width: 608px;
                        height: 560px;
                        position: absolute;
                        top: 2px;
                        left: 224px;
                        z-index: 10;
                        cursor: auto;
                        background-image: linear-gradient(45deg, #0A2B3A, #0B1B2A);
                        border: 1px solid #1ED5C7;
                        .titleName{
                          width: 100%;
                          height: 75px;
                          display: flex;
                          justify-content: space-between;
                          align-items: center;
                          background-image: linear-gradient(45deg, rgba(23, 221, 247, 0.02), rgba(23, 221, 247, 0.1));
                          padding: 0 20px;
                        }
                        .bodyChose{
                          width: 100%;
                          height: 388px;
                          overflow: auto;
                          padding: 20px;
                          .checkEdItem{
                            height: 72px;
                            width: 100%;
                            background-image: url('./newBack/19.png');
                            background-size: 100% 100%;
                            color: #5AE8FA;
                            font-size: 30px;
                            margin-bottom: 16px;
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            .ChoseBtn{
                              width: 80px;
                              height: 36px;
                              background-image: url('./newBack/21.png');
                              background-size: 100% 100%;
                              color:#0A2534;
                              cursor: pointer;
                              font-size: 22px;
                            }
                          }
                          .normalItem{
                            height: 72px;
                            width: 100%;
                            background-image: url('./newBack/19.png');
                            background-size: 100% 100%;
                            color: #C5EEF3;
                            font-size: 30px;
                            margin-bottom: 16px;
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            .ChoseBtn{
                              width: 80px;
                              height: 36px;
                              background-image: url('./newBack/20.png');
                              background-size: 100% 100%;
                              color:#16DFF8;
                              cursor: pointer;
                              font-size: 22px;
                            }
                          }
                        }
                        .footBox{
                          width: 100%;
                          height: 100px;
                          display: flex;
                          justify-content: space-between;
                          background-image: linear-gradient(45deg, rgba(23, 221, 247, 0.02), rgba(23, 221, 247, 0.1));
                          padding: 0 20px;
                          align-items: center;
                          .SureBtn{
                            height: 50px;
                            width: 120px;
                            background-image: url('./newBack/22.png');
                            font-size: 28px;
                            cursor: pointer;
                            color: #0B1B2A;
                          }
                        }
              }
            }
        }
    }
    #Module5{
        width: 1392px;
        height: 1384px;
        margin-top: 200px;
        margin-right: 32px;
        background: #0B1B2A;
        position: relative;
        padding: 0;
        .TopName{
          height: 90px;
          width: 530px;
          position: absolute;
          top: -90px;
          left: 0;
        }
        .title{
            img{
                width:100%;
                height:76px;
            }
            margin-bottom: 12px;
        }
        .content{
            position: relative;
            overflow: hidden;
            .MsgType{
              height: 80px;
              width: 100%;
              display: flex;
              align-items: center;
              .checked{
                width: 180px;
                height: 50px;
                background-image: url('./newBack/12.png');
                background-size: 100% 100%;
                font-size: 26px;
                color: #0B1B2A;
                margin-left: 17px;
                cursor: pointer;
                display: flex;
                align-items: center;
                justify-content: center;
              }
              .nochecked{
                width: 180px;
                height: 50px;
                background-image: url('./newBack/11.png');
                background-size: 100% 100%;
                font-size: 26px;
                cursor: pointer;
                color: #C5EEF3;
                margin-left: 17px;
                display: flex;
                align-items: center;
                justify-content: center;
              }
            }
            .cityEvent{
                width: 100%;
                height: 1210px;
                display: flex;
                overflow: scroll;
                align-items: center;
                flex-direction: column;
                .item{
                  width: 100%;
                  // animation:anima 20s linear infinite;
                }
                // .item:hover{
                //   animation-play-state: paused !important;
                // }
                // @keyframes anima {
                //   0%{
                //     transform: translateY(0);
                //   }
                //   100%{
                //     transform: translateY(-100%);
                //   }
                // }
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
                      overflow: hidden !important;
                      padding: 14px 14px 5px 28px;
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
                              width: 85%;
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
                              width: 100px;
                              height: 40px;
                              display: flex;
                              justify-content: center;
                              align-items: center;
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
                      .state{
                        width: 100px;
                        height: 40px;
                        background-image: url('./newBack/13.png');
                        background-size: 100% 100%;
                        display: flex;
                        justify-content: center;
                        color: black;
                        align-items: center;
                      }

                  }
                }
            }
            .Module5Pop{
                width: 100%;
                height: 1294px;
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
                .tjdbBox{
                  width: 608px;
                  height: 650px;
                  position: absolute;
                  top: 0;
                  left: 190px;
                  z-index: 10;
                  cursor: auto;
                  background-image: linear-gradient(45deg, #0A2B3A, #0B1B2A);
                  border: 1px solid #1ED5C7;
                  .titleName{
                    width: 100%;
                    height: 75px;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    background-image: linear-gradient(45deg, rgba(23, 221, 247, 0.02), rgba(23, 221, 247, 0.1));
                    padding: 0 20px;
                  }
                  .bodyChose{
                    width: 100%;
                    height: 475px;
                    overflow: auto;
                    padding: 20px;
                    .checkEdItem{
                      height: 72px;
                      width: 100%;
                      background-image: url('./newBack/19.png');
                      background-size: 100% 100%;
                      color: #5AE8FA;
                      font-size: 30px;
                      margin-bottom: 16px;
                      display: flex;
                      justify-content: space-between;
                      align-items: center;
                      .ChoseBtn{
                        width: 80px;
                        height: 36px;
                        background-image: url('./newBack/21.png');
                        background-size: 100% 100%;
                        color:#0A2534;
                        cursor: pointer;
                        font-size: 22px;
                      }
                    }
                    .normalItem{
                      height: 72px;
                      width: 100%;
                      background-image: url('./newBack/19.png');
                      background-size: 100% 100%;
                      color: #C5EEF3;
                      font-size: 30px;
                      margin-bottom: 16px;
                      display: flex;
                      justify-content: space-between;
                      align-items: center;
                      .ChoseBtn{
                        width: 80px;
                        height: 36px;
                        background-image: url('./newBack/20.png');
                        background-size: 100% 100%;
                        color:#16DFF8;
                        cursor: pointer;
                        font-size: 22px;
                      }
                    }
                  }
                  .footBox{
                    width: 100%;
                    height: 100px;
                    display: flex;
                    justify-content: space-between;
                    background-image: linear-gradient(45deg, rgba(23, 221, 247, 0.02), rgba(23, 221, 247, 0.1));
                    padding: 0 20px;
                    align-items: center;
                    .SureBtn{
                      height: 50px;
                      width: 120px;
                      background-image: url('./newBack/22.png');
                      font-size: 28px;
                      cursor: pointer;
                      color: #0B1B2A;
                    }
                  }
                }
            }
        }
    }
    #Module6{
        width: 932px;
        height: 1384px;
        margin-top: 200px;
        margin-right: 32px;
        background: #0B1B2A;
        padding: 0;
        .title{
            img{
                width:100%;
                height:76px;
            }
            margin-bottom: 32px;
        }
        .content{
            position: relative;
            overflow: hidden;
            .cityEvent{
                width: 100%;
                height: 1270px;
                overflow: scroll;
                display: flex;
                align-items: center;
                flex-direction: column;
                .item{
                  width: 100%;
                  // animation:anima 20s linear infinite;
                }
                // .item:hover{
                //   animation-play-state: paused !important;
                // }
                // @keyframes anima {
                //   0%{
                //     transform: translateY(0);
                //   }
                //   100%{
                //     transform: translateY(-100%);
                //   }
                // }
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
                              width: 85%;
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
                              width: 100px;
                              height: 40px;
                              display: flex;
                              justify-content: center;
                              align-items: center;
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
            #Module6Pop{
                width: 100%;
                height: 1280px;
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
::deep .el-timeline-item__timestamp{
  color: #C5EEF3;
  font-size: 20px;
}
.el-timeline-item__tail{
  border-color: #FCB83C;
}
.el-timeline-item__node{
  background-color: #FCB83C;
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
            background: linear-gradient(45deg, rgba(22, 222, 248, 0.1), rgba(22, 223, 248, 0.02));
                .ivu-icon-ios-calendar-outline{
                line-height: 50px !important;
                color: #C5EEF3 !important;
                font-size: 22px !important;

                }
                .ivu-icon-ios-close-circle{
                line-height: 50px !important;
                color: #C5EEF3 !important;
                font-size: 22px !important;
                }
                .ivu-input{
                width: 365px !important;
                height: 50px !important;
                color: #C5EEF3 !important;
                font-size: 25px !important;
                border-radius: 4px;
                border: 2px solid hsla(187, 94%, 67%, 0.2) !important;
                }
        }
    }
}
</style>
