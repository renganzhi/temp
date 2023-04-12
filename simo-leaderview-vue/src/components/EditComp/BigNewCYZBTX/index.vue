<template>
    <div class="BigNewCYZBTX">
        <div class="content">
            <div class="personBox" @click="ShowPop1">
                <div class="position">区领导</div>
                <div class="information1">
                    <img :src="getZBRY('区领导','照片链接')" alt="">
                    <div>
                        <div>{{getZBRY('区领导','姓名')}}<img style="cursor:pointer;width:40px;height:40px;vertical-align:sub;margin-left:5px;" src="./短信.png" @click.stop="ShowText(0)" alt=""></div>
                        <div :title="getZBRY('区领导','职务')">{{getZBRY('区领导','职务')}}</div>
                    </div>
                </div>
                <div class="textPop" @click.stop="" v-show="textIndex === 0 && !showPop1">
                  <img class="close" @click.stop="textIndex = -1" style="cursor:pointer;" src="./关闭.png" alt="">
                  <div class="textTitle">短信模板</div>
                  <div class="textList">
                    <CheckboxGroup v-model="textList" size="large">
                      <div class="textBox">
                        <label>
                          <div class="textDetail">【温馨提示】尊敬的领导，根据市城运中心值守调度要求，当日带班领导需参与每日16:30值班值守调度会。根据工作安排，现区委区政府总值班室与区城运中心统筹值班，按照区委区政府总值班室2023年3月值班带班安排，今日（3月22日）16:25时，请您到一办大礼堂一楼参与当日值守调度会，若您因故无法出席，请安排一名人员代会，特此提醒。</div>
                          <Checkbox>
                            .
                          </Checkbox>
                        </label>
                      </div>
                    </CheckboxGroup>
                  </div>
                  <div class="sendButton">
                    <Button type="primary">发送</Button>
                  </div>
                </div>
            </div>
            <div class="personBox" @click="ShowPop1">
                <div class="position">指挥长</div>
                <div class="information2">
                    <div>
                      {{getZBRY('指挥长','姓名')}}
                      <img style="cursor:pointer;" src="./电话.png" @click.stop="ShowPhone(1)" alt="">
                      <img style="cursor:pointer;width:40px;height:40px;vertical-align:sub;margin-left:5px;" src="./短信.png" @click.stop="ShowText(1)" alt="">
                    </div>
                    <div :title="getZBRY('指挥长','职务')">{{getZBRY('指挥长','职务')}}</div>
                </div>
                <div class="textPop" @click.stop="" v-show="textIndex === 1 && !showPop1">
                  <img class="close" @click.stop="textIndex = -1" style="cursor:pointer;" src="./关闭.png" alt="">
                  <div class="textTitle">短信模板</div>
                  <div class="textList">
                    <CheckboxGroup v-model="textList" size="large">
                      <div class="textBox">
                        <label>
                          <div class="textDetail">【温馨提示】尊敬的领导，根据市城运中心值守调度要求，当日带班领导需参与每日16:30值班值守调度会。根据工作安排，现区委区政府总值班室与区城运中心统筹值班，按照区委区政府总值班室2023年3月值班带班安排，今日（3月22日）16:25时，请您到一办大礼堂一楼参与当日值守调度会，若您因故无法出席，请安排一名人员代会，特此提醒。</div>
                          <Checkbox>
                            .
                          </Checkbox>
                        </label>
                      </div>
                    </CheckboxGroup>
                  </div>
                  <div class="sendButton">
                    <Button type="primary">发送</Button>
                  </div>
                </div>
                <div class="phonePop" v-show="phoneIndex === 1 && !showPop1">
                    <img @click.stop="phoneIndex = 0" style="cursor:pointer;" src="./关闭.png" alt="">
                    <div>{{getZBRY('指挥长','姓名')}}</div>
                    <div>指挥长</div>
                    <div>{{getZBRY('指挥长','手机号')}}</div>
                    <div>拨打</div>
                </div>
            </div>
            <div class="personBox" @click="ShowPop1">
                <div class="position">值班长</div>
                <div class="information2">
                    <div>
                      {{getZBRY('值班长','姓名')}}
                      <img @click.stop="ShowPhone(2)" style="cursor:pointer;" src="./电话.png" alt="">
                      <img style="cursor:pointer;width:40px;height:40px;vertical-align:sub;margin-left:5px;" src="./短信.png" @click.stop="ShowText(2)" alt="">
                    </div>
                    <div :title="getZBRY('值班长','职务')">{{getZBRY('值班长','职务')}}</div>
                </div>
                <div class="textPop" @click.stop="" v-show="textIndex === 2 && !showPop1">
                  <img class="close" @click.stop="textIndex = -1" style="cursor:pointer;" src="./关闭.png" alt="">
                  <div class="textTitle">短信模板</div>
                  <div class="textList">
                    <CheckboxGroup v-model="textList" size="large">
                      <div class="textBox">
                        <label>
                          <div class="textDetail">【温馨提示】尊敬的领导，根据市城运中心值守调度要求，当日带班领导需参与每日16:30值班值守调度会。根据工作安排，现区委区政府总值班室与区城运中心统筹值班，按照区委区政府总值班室2023年3月值班带班安排，今日（3月22日）16:25时，请您到一办大礼堂一楼参与当日值守调度会，若您因故无法出席，请安排一名人员代会，特此提醒。</div>
                          <Checkbox>
                            .
                          </Checkbox>
                        </label>
                      </div>
                    </CheckboxGroup>
                  </div>
                  <div class="sendButton">
                    <Button type="primary">发送</Button>
                  </div>
                </div>
                <div class="phonePop" v-show="phoneIndex === 2 && !showPop1">
                    <img @click.stop="phoneIndex = 0" style="cursor:pointer;" src="./关闭.png" alt="">
                    <div>{{getZBRY('值班长','姓名')}}</div>
                    <div>值班长</div>
                    <div>{{getZBRY('值班长','手机号')}}</div>
                    <div>拨打</div>
                </div>
            </div>
        </div>
        <div class="pop1" :style="pop1Style" v-show="showPop1">
            <div class="bigTitle">
                值班体系
                <img @click="ClosePop1" style="cursor:pointer;" src="./关闭.png" alt="">
            </div>
            <div>
                <div class="row1">
                    <div>区级城运中心</div>
                    <div @click="ShowPop2" style="cursor:pointer;">职能职责</div>
                    <div @click="ShowPop3" style="cursor:pointer;">工作清单</div>
                </div>
                <div class="row2">
                    <div class="personBox">
                        <div class="position">区领导</div>
                        <div class="information1">
                            <img :src="getZBRY('区领导','照片链接')" alt="">
                            <div>
                                <div>{{getZBRY('区领导','姓名')}}<img style="cursor:pointer;width:40px;height:40px;vertical-align:sub;margin-left:5px;" src="./短信.png" @click.stop="ShowText(0)" alt=""></div>
                                <div :title="getZBRY('区领导','职务')">{{getZBRY('区领导','职务')}}</div>
                            </div>
                        </div>
                        <div class="textPop" @click.stop="" v-show="textIndex === 0 && showPop1">
                          <img class="close" @click.stop="textIndex = -1" style="cursor:pointer;" src="./关闭.png" alt="">
                          <div class="textTitle">短信模板</div>
                          <div class="textList">
                            <CheckboxGroup v-model="textList" size="large">
                              <div class="textBox">
                                <label>
                                  <div class="textDetail">【温馨提示】尊敬的领导，根据市城运中心值守调度要求，当日带班领导需参与每日16:30值班值守调度会。根据工作安排，现区委区政府总值班室与区城运中心统筹值班，按照区委区政府总值班室2023年3月值班带班安排，今日（3月22日）16:25时，请您到一办大礼堂一楼参与当日值守调度会，若您因故无法出席，请安排一名人员代会，特此提醒。</div>
                                  <Checkbox>
                                    .
                                  </Checkbox>
                                </label>
                              </div>
                            </CheckboxGroup>
                          </div>
                          <div class="sendButton">
                            <Button type="primary">发送</Button>
                          </div>
                        </div>
                    </div>
                    <div class="personBox">
                        <div class="position">指挥长</div>
                        <div class="information2">
                            <div>
                              {{getZBRY('指挥长','姓名')}}
                              <img style="cursor:pointer;" src="./电话.png" @click.stop="ShowPhone(1)" alt="">
                              <img style="cursor:pointer;width:40px;height:40px;vertical-align:sub;margin-left:5px;" src="./短信.png" @click.stop="ShowText(1)" alt="">
                            </div>
                            <div :title="getZBRY('指挥长','职务')">{{getZBRY('指挥长','职务')}}</div>
                        </div>
                        <div class="textPop" @click.stop="" v-show="textIndex === 1 && showPop1">
                          <img class="close" @click.stop="textIndex = -1" style="cursor:pointer;" src="./关闭.png" alt="">
                          <div class="textTitle">短信模板</div>
                          <div class="textList">
                            <CheckboxGroup v-model="textList" size="large">
                              <div class="textBox">
                                <label>
                                  <div class="textDetail">【温馨提示】尊敬的领导，根据市城运中心值守调度要求，当日带班领导需参与每日16:30值班值守调度会。根据工作安排，现区委区政府总值班室与区城运中心统筹值班，按照区委区政府总值班室2023年3月值班带班安排，今日（3月22日）16:25时，请您到一办大礼堂一楼参与当日值守调度会，若您因故无法出席，请安排一名人员代会，特此提醒。</div>
                                  <Checkbox>
                                    .
                                  </Checkbox>
                                </label>
                              </div>
                            </CheckboxGroup>
                          </div>
                          <div class="sendButton">
                            <Button type="primary">发送</Button>
                          </div>
                        </div>
                        <div class="phonePop" v-show="phoneIndex === 1 && showPop1">
                            <img @click.stop="phoneIndex = 0" style="cursor:pointer;" src="./关闭.png" alt="">
                            <div>{{getZBRY('指挥长','姓名')}}</div>
                            <div>指挥长</div>
                            <div>{{getZBRY('指挥长','手机号')}}</div>
                            <div>拨打</div>
                        </div>
                    </div>
                    <div class="personBox">
                        <div class="position">值班长</div>
                        <div class="information2">
                            <div>
                              {{getZBRY('值班长','姓名')}}
                              <img @click.stop="ShowPhone(2)" style="cursor:pointer;" src="./电话.png" alt="">
                              <img style="cursor:pointer;width:40px;height:40px;vertical-align:sub;margin-left:5px;" src="./短信.png" @click.stop="ShowText(2)" alt="">
                            </div>
                            <div :title="getZBRY('值班长','职务')">{{getZBRY('值班长','职务')}}</div>
                        </div>
                        <div class="textPop" @click.stop="" v-show="textIndex === 2 && showPop1">
                          <img class="close" @click.stop="textIndex = -1" style="cursor:pointer;" src="./关闭.png" alt="">
                          <div class="textTitle">短信模板</div>
                          <div class="textList">
                            <CheckboxGroup v-model="textList" size="large">
                              <div class="textBox">
                                <label>
                                  <div class="textDetail">【温馨提示】尊敬的领导，根据市城运中心值守调度要求，当日带班领导需参与每日16:30值班值守调度会。根据工作安排，现区委区政府总值班室与区城运中心统筹值班，按照区委区政府总值班室2023年3月值班带班安排，今日（3月22日）16:25时，请您到一办大礼堂一楼参与当日值守调度会，若您因故无法出席，请安排一名人员代会，特此提醒。</div>
                                  <Checkbox>
                                    .
                                  </Checkbox>
                                </label>
                              </div>
                            </CheckboxGroup>
                          </div>
                          <div class="sendButton">
                            <Button type="primary">发送</Button>
                          </div>
                        </div>
                        <div class="phonePop" v-show="phoneIndex === 2 && showPop1">
                            <img @click.stop="phoneIndex = 0" style="cursor:pointer;" src="./关闭.png" alt="">
                            <div>{{getZBRY('值班长','姓名')}}</div>
                            <div>值班长</div>
                            <div>{{getZBRY('值班长','手机号')}}</div>
                            <div>拨打</div>
                        </div>
                    </div>
                    <img @click="ShowPop4" style="cursor:pointer;" src="./更多.png" alt="">
                </div>
                <div class="row3">
                    <div>值班值守情况</div>
                    <div @click="changeType1('工作要求')" style="cursor:pointer;" :class="{activeButton: type1 === '工作要求'}">工作要求</div>
                    <div @click="changeType1('预警台账')" style="cursor:pointer;" :class="{activeButton: type1 === '预警台账'}">预警台账</div>
                    <div @click="changeType1('交接班清单')" style="cursor:pointer;" :class="{activeButton: type1 === '交接班清单'}">交接班清单</div>
                </div>
                <div class="row4">
                    <div class="checkBox">
                        <Input v-model="InputCondition1" placeholder="输入搜索内容" style="width:285px;height:33px;" />
                        <!-- <div class="search"><Icon type="md-search" /></div>
                        <div class="refresh"><Icon @click="refreshZBZSQK" type="md-refresh" /></div> -->
                    </div>
                    <div class="mytable1" v-show="type1 === '工作要求'">
                        <div class="tablehead">
                            <div>日期</div>
                            <div>内容</div>
                            <div>类别</div>
                        </div>
                        <div class="tablebody">
                            <div @click="showXQ(data)" class="rows" v-for="(data, index) in getZBZSQK" :key="index">
                                <div>{{data['日期']}}</div>
                                <div>{{data['内容']}}</div>
                                <div>{{data['类别']}}</div>
                            </div>
                        </div>
                    </div>
                    <div class="mytable2" v-show="type1 === '预警台账'">
                        <div class="tablehead">
                            <div>类别</div>
                            <div>标题</div>
                            <div>内容</div>
                            <div>时间</div>
                        </div>
                        <div class="tablebody">
                            <div @click="showXQ(data)" class="rows" v-for="(data, index) in getZBZSQK" :key="index">
                                <div>{{data['类别']}}</div>
                                <div>{{data['标题']}}</div>
                                <div>{{data['内容']}}</div>
                                <div>{{data['时间']}}</div>
                            </div>
                        </div>
                    </div>
                    <div class="mytable3" v-show="type1 === '交接班清单'">
                        <div class="tablehead">
                            <div>今日值班长</div>
                            <div>昨日值班长</div>
                            <div>值班长交接内容</div>
                            <div>日期</div>
                        </div>
                        <div class="tablebody">
                            <div @click="showXQ(data)" class="rows" v-for="(data, index) in getZBZSQK" :key="index">
                                <div>{{data['今日值班长'] || '暂无数据'}}</div>
                                <div>{{data['昨日值班长'] || '暂无数据'}}</div>
                                <div>{{data['值班长交接内容'] || '暂无数据'}}</div>
                                <div>{{data['日期'] || '暂无数据'}}</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row5">
                    <div class="left">
                        <div>值班值守制度</div>
                        <div class="checkBox">
                            <Input v-model="InputCondition2" placeholder="输入搜索内容" style="width:283px;height:33px;margin-right:2px;" />
                            <!-- <div class="search"><Icon type="md-search" /></div>
                            <div class="refresh"><Icon @click="refreshZBZSZD" type="md-refresh" /></div> -->
                        </div>
                        <div class="list">
                            <div @click="showXQ(data)" class="rows" v-for="(data, index) in getZBZSZD" :key="index">
                                <div></div>
                                <div>{{data['标题']}}</div>
                            </div>
                        </div>
                    </div>
                    <div class="right">
                        <div>应急预案</div>
                        <div class="checkBox">
                            <Input v-model="InputCondition3" placeholder="输入搜索内容" style="width:283px;height:33px;margin-right:2px;" />
                            <!-- <div class="search"><Icon type="md-search" /></div>
                            <div class="refresh"><Icon @click="refreshYJYA" type="md-refresh" /></div> -->
                        </div>
                        <div class="list">
                            <div @click="showXQ(data)" class="rows" v-for="(data, index) in getYJYA" :key="index">
                                <div></div>
                                <div>{{data['标题']}}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="pop2" :style="pop2Style" v-show="showPop2">
            <div class="bigTitle">
                <img @click="ClosePop2" src="./关闭.png" alt="">
            </div>
            <div>
                <div class="switchButton">
                    <div @click="changeType2('区领导')" style="cursor:pointer;" :class="{checked: type2 === '区领导'}">区领导</div>
                    <div @click="changeType2('指挥长')" style="cursor:pointer;" :class="{checked: type2 === '指挥长'}">指挥长</div>
                    <div @click="changeType2('值班长')" style="cursor:pointer;" :class="{checked: type2 === '值班长'}">值班长</div>
                </div>
                <div class="text">
                    {{getCurrentZNZZ}}
                </div>
            </div>
        </div>
        <div class="pop3" :style="pop3Style" v-show="showPop3">
            <div class="bigTitle">
                <img @click="ClosePop3" src="./关闭.png" alt="">
            </div>
            <div>
                <div class="switchButton">
                    <div @click="changeType2('区领导')" style="cursor:pointer;" :class="{checked: type2 === '区领导'}">区领导</div>
                    <div @click="changeType2('指挥长')" style="cursor:pointer;" :class="{checked: type2 === '指挥长'}">指挥长</div>
                    <div @click="changeType2('值班长')" style="cursor:pointer;" :class="{checked: type2 === '值班长'}">值班长</div>
                </div>
                <div class="checkBox">
                    <DatePicker v-model="DateCondition2" type="date" placeholder="请选择时间段" style="width:285px;height:33px;" />
                </div>
                <div class="mytable">
                        <div class="tablehead">
                            <div>序号</div>
                            <div>时间</div>
                            <div>具体事项</div>
                            <div>备注</div>
                        </div>
                        <div class="tablebody">
                            <div @click="showXQ(data)" class="rows" v-for="(data, index) in getCurrentGZQD" :key="index">
                                <div>{{index + 1}}</div>
                                <div>{{data['时间']}}</div>
                                <div>{{data['内容']}}</div>
                                <div></div>
                            </div>
                        </div>
                </div>
            </div>
        </div>
        <div class="pop4" :style="pop4Style" v-show="showPop4">
            <div class="bigTitle">
                <img @click="ClosePop4" src="./关闭.png" alt="">
            </div>
            <div>
                <div class="CYZBTX">
                  <div class="streetBox" v-for="(element, index) in StreetInfo" v-show="element['值班职务'] === '值班领导' || element['职务'] === '值班领导'" :key="index">
                    <div class="streetName">{{element['所属街道']}}</div>
                    <div class="streetInfo">
                      <div class="personnelInfo">
                        <div>
                          <img v-if="element['照片链接']" :src="element['照片链接']" style="width: 110px !important;height: 160px !important;object-fit: fill !important;" alt="">
                          <img v-else src="./街道矩形.png" style="width: 80px !important;height: 100px !important;object-fit: fill !important;" alt="">
                        </div>
                        <div>
                          <div class="name">{{element['领导姓名']}}
                            <img style="vertical-align: baseline;width:24px;height:24px;margin-left: 5px;" @click="phoneIndex2 = index" src="./电话.png" alt="">
                            <img style="cursor:pointer;width:24px;height:24px;vertical-align:baseline;margin-left:5px;" src="./短信.png" @click.stop="textIndex2 = index" alt="">
                          </div>
                          <!-- <div class="phone" @click="phoneIndex2 = index">{{element['手机号']}}
                            <img src="./电话.png" alt="">
                          </div> -->
                          <div class="post" v-if="element['值班职务'] && element['职务'] !== '值班领导'" :title="element['职务']">{{element['职务']}}</div>
                          <div class="position">{{element['值班职务'] || element['职务']}}</div>
                        </div>
                      </div>
                      <div class="textPop" @click.stop="" v-show="textIndex2 === index && showPop4">
                        <img class="close" @click.stop="textIndex2 = -1" style="cursor:pointer;" src="./关闭.png" alt="">
                        <div class="textTitle">短信模板</div>
                        <div class="textList">
                          <CheckboxGroup v-model="textList" size="large">
                            <div class="textBox">
                              <label>
                                <div class="textDetail">【温馨提示】尊敬的领导，根据市城运中心值守调度要求，当日带班领导需参与每日16:30值班值守调度会。根据工作安排，现区委区政府总值班室与区城运中心统筹值班，按照区委区政府总值班室2023年3月值班带班安排，今日（3月22日）16:25时，请您到一办大礼堂一楼参与当日值守调度会，若您因故无法出席，请安排一名人员代会，特此提醒。</div>
                                <Checkbox>
                                  .
                                </Checkbox>
                              </label>
                            </div>
                          </CheckboxGroup>
                        </div>
                        <div class="sendButton">
                          <Button type="primary">发送</Button>
                        </div>
                      </div>
                      <div class="phonePop" v-show="phoneIndex2 === index && showPop4">
                          <img @click.stop="phoneIndex2 = -1" style="cursor:pointer;" src="./关闭.png" alt="">
                          <div>{{element['领导姓名']}}</div>
                          <div>值班领导</div>
                          <div>{{element['手机号']}}</div>
                          <div>拨打</div>
                      </div>
                    </div>
                  </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import element from '@/element'
import CYZBTX from '../CYZBTX/index.vue'
export default {
  data: function () {
    return {
      showPop1: false,
      showPop2: false,
      showPop3: false,
      showPop4: false,
      phoneIndex: 0,
      phoneIndex2: -1,
      textIndex2: -1,
      textIndex: -1,
      textList: [],
      type1: '工作要求',
      type2: '区领导',
      ZBpersonInfo: [], // 值班人员信息
      InputCondition1: '',
      InputCondition2: '',
      InputCondition3: '',
      DateCondition1: '',
      DateCondition2: new Date(),
      ZBZSZDList: [], // 值班值守制度
      YJYAList: [], // 应急预案
      ZNZZList: [], // 职能职责
      GZQDList: [], // 工作清单
      StreetInfo: [], // 街道人员信息
      GZYQList: [], // 工作清单
      YJTZList: [], // 预警台账
      JJBQDList: [] // 交接班清单
    }
  },
  props: ['item'],
  components: {CYZBTX},
  computed: {
    getZBRY () {
      return (type, key) => {
        let value = '暂无数据'
        if (this.ZBpersonInfo.length) {
          this.ZBpersonInfo.forEach(element => {
            if (element['值班职位'] === type) {
              if (element[key]) {
                value = element[key]
              }
            }
          })
        }
        return value
      }
    },
    getCYZBTX () {
      let chartData = {
        columns: [],
        rows: []
      }
      if (this.StreetInfo) {
        chartData = JSON.parse(JSON.stringify(this.StreetInfo))
      }
      return {
        'text': '城运值班体系',
        'imgClass': 'icon-n-text',
        'chartType': 'CYZBTX',
        'width': 754,
        'height': 796,
        'LunBoType': 0,
        'fontSize': 20,
        'barWidth': 20,
        'chartData': chartData
      }
    },
    pop1Style () {
      return {
        top: this.item.pop1Top + 'px',
        left: this.item.pop1Left + 'px'
      }
    },
    pop2Style () {
      return {
        top: this.item.pop2Top + 'px',
        right: this.item.pop2Right + 'px'
      }
    },
    pop3Style () {
      return {
        top: this.item.pop3Top + 'px',
        right: this.item.pop3Right + 'px'
      }
    },
    pop4Style () {
      return {
        top: this.item.pop4Top + 'px',
        right: this.item.pop4Right + 'px'
      }
    },
    getZBZSQK () { // 值班值守情况
      let arr = []
      if (this.type1 === '工作要求') {
        this.GZYQList.forEach(element => {
          if (this.InputCondition1) {
            if (JSON.stringify(element).indexOf(this.InputCondition1) >= 0) {
              arr.push(element)
            }
          } else {
            arr.push(element)
          }
        })
      } else if (this.type1 === '预警台账') {
        this.YJTZList.forEach(element => {
          if (this.InputCondition1) {
            if (JSON.stringify(element).indexOf(this.InputCondition1) >= 0) {
              arr.push(element)
            }
          } else {
            arr.push(element)
          }
        })
      } else if (this.type1 === '交接班清单') {
        this.JJBQDList.forEach(element => {
          if (this.InputCondition1) {
            if (JSON.stringify(element).indexOf(this.InputCondition1) >= 0) {
              arr.push(element)
            }
          } else {
            arr.push(element)
          }
        })
      }
      return arr
    },
    getZBZSZD () { // 值班值守制度
      let arr = []
      if (this.InputCondition2) {
        this.ZBZSZDList.forEach(element => {
          if (JSON.stringify(element).indexOf(this.InputCondition2) >= 0) {
            arr.push(element)
          }
        })
      } else {
        arr = this.ZBZSZDList
      }
      return arr
    },
    getYJYA () { // 应急预案
      let arr = []
      if (this.InputCondition3) {
        this.YJYAList.forEach(element => {
          if (JSON.stringify(element).indexOf(this.InputCondition3) >= 0) {
            arr.push(element)
          }
        })
      } else {
        arr = this.YJYAList
      }
      return arr
    },
    getCurrentZNZZ () { // 获取当前职能职责
      let text = '暂无数据'
      this.ZNZZList.forEach(element => {
        if (element['职务'] === this.type2) {
          text = element['内容']
        }
      })
      return text
    },
    getCurrentGZQD () { // 工作清单
      let arr = []
      this.GZQDList.forEach(element => {
        if (element['职务'] === this.type2) {
          if (element['明细']) {
            element['明细'].rows.forEach(el => {
              if (this.DateCondition2) {
                if (el['日期'] === this.DateToString(this.DateCondition2)) {
                  arr.push(el)
                }
              } else {
                arr.push(el)
              }
            })
          }
        }
      })
      return arr
    }
  },
  watch: {
    type2: function () {
      this.DateCondition2 = new Date()
    },
    type1: function () {
      this.InputCondition1 = ''
    },
    textIndex: function () {
      this.textList = []
    }
  },
  methods: {
    showXQ (data) {
      if (this.$parent.$parent.ShowTanKuangBox) {
        let dataArray = {
          title: '数据详情',
          data: data
        }
        this.$parent.$parent.ShowTanKuangBox(dataArray)
      }
    },
    DateToString (date) {
      let da = ''
      if (typeof date !== 'string') {
        let y = date.getFullYear()
        let m = ''
        let d = ''
        if (date.getMonth() < 9) {
          m = '0' + (date.getMonth() + 1)
        } else {
          m = date.getMonth() + 1
        }
        if (date.getDate() < 10) {
          d = '0' + date.getDate()
        } else {
          d = date.getDate()
        }
        da = y + '-' + m + '-' + d
      } else {
        da = date
      }
      return da
    },
    refreshZBZSQK () {
      this.InputCondition1 = ''
      // 工作要求
      this.axios.get('/leaderview/ChengYun4/GetZBTX1').then(res => {
        if (res.success && res.obj.rows) {
          this.GZYQList = res.obj.rows
        }
      })
      // 预警台账
      this.axios.get('/leaderview/ChengYun4/GetZBTX7').then(res => {
        if (res.success && res.obj.rows) {
          this.YJTZList = res.obj.rows
        }
      })
      // 交接班清单
      this.axios.get('/leaderview/ChengYun4/GetZBTX6').then(res => {
        if (res.success && res.obj.rows) {
          this.JJBQDList = res.obj.rows
        }
      })
    },
    refreshZBZSZD () {
      this.InputCondition2 = ''
      // 值守制度
      this.axios.get('/leaderview/ChengYun4/GetZBTX4').then(res => {
        if (res.success && res.obj.rows) {
          this.ZBZSZDList = res.obj.rows
        }
      })
    },
    refreshYJYA () {
      this.InputCondition3 = ''
      // 应急预案
      this.axios.get('/leaderview/ChengYun4/GetZBTX5').then(res => {
        if (res.success && res.obj.rows) {
          this.YJYAList = res.obj.rows
        }
      })
    },
    ShowPop1 () {
      this.getData()
      this.showPop1 = true
      this.phoneIndex = 0
      this.phoneIndex2 = -1
      this.textIndex2 = -1
      this.textIndex = -1
    },
    ClosePop1 () {
      this.getData()
      this.showPop1 = false
      this.showPop2 = false
      this.showPop3 = false
      this.showPop4 = false
      this.type2 = '区领导'
      this.type1 = '工作要求'
      this.phoneIndex = 0
      this.phoneIndex2 = -1
      this.textIndex2 = -1
      this.textIndex = -1
    },
    ShowPop2 () {
      this.showPop2 = true
      this.showPop3 = false
      this.showPop4 = false
      this.type2 = '区领导'
    },
    ClosePop2 () {
      this.showPop2 = false
      this.type2 = '区领导'
    },
    ShowPop3 () {
      this.showPop3 = true
      this.showPop2 = false
      this.showPop4 = false
      this.type2 = '区领导'
    },
    ClosePop3 () {
      this.showPop3 = false
      this.type2 = '区领导'
    },
    ShowPop4 () {
      this.showPop4 = true
      this.showPop2 = false
      this.showPop3 = false
    },
    ClosePop4 () {
      this.showPop4 = false
      this.phoneIndex2 = -1
      this.textIndex2 = -1
    },
    ShowPhone (index) {
      this.phoneIndex = index
    },
    ShowText (index) {
      this.textIndex = index
    },
    getData () {
      // 工作要求
      this.axios.get('/leaderview/ChengYun4/GetZBTX1').then(res => {
        if (res.success && res.obj.rows) {
          this.GZYQList = res.obj.rows
        }
      })
      // 职能职责
      this.axios.get('/leaderview/ChengYun4/GetZBTX2').then(res => {
        if (res.success && res.obj.rows) {
          this.ZNZZList = res.obj.rows
        }
      })
      // 工作清单
      this.axios.get('/leaderview/ChengYun4/GetZBTX3').then(res => {
        if (res.success && res.obj.rows) {
          this.GZQDList = res.obj.rows
        }
      })
      // 值守制度
      this.axios.get('/leaderview/ChengYun4/GetZBTX4').then(res => {
        if (res.success && res.obj.rows) {
          this.ZBZSZDList = res.obj.rows
        }
      })
      // 应急预案
      this.axios.get('/leaderview/ChengYun4/GetZBTX5').then(res => {
        if (res.success && res.obj.rows) {
          this.YJYAList = res.obj.rows
        }
      })
      // 交接班清单
      this.axios.get('/leaderview/ChengYun4/GetZBTX6').then(res => {
        if (res.success && res.obj.rows) {
          this.JJBQDList = res.obj.rows
        }
      })
      // 预警台账
      this.axios.get('/leaderview/ChengYun4/GetZBTX7').then(res => {
        if (res.success && res.obj.rows) {
          this.YJTZList = res.obj.rows
        }
      })
      // 值班信息
      this.axios.get('/leaderview/ChengYun4/GetZBTX8').then(res => {
        if (res.success && res.obj.rows) {
          this.ZBpersonInfo = res.obj.rows
        }
      })
      // 值班街道
      this.axios.get('/leaderview/newDistrict/GetZBTX3').then(res => {
        if (res.success && res.obj.rows) {
          this.StreetInfo = res.obj.rows
        }
      })
    },
    changeType1 (type) {
      this.type1 = type
    },
    changeType2 (type) {
      this.type2 = type
    }
  },
  mounted () {
    this.getData()
  }
}
</script>
<style scoped lang="scss">
.BigNewCYZBTX{
    position: relative;
    letter-spacing: 1px;
    .textPop{
      position: absolute;
      top: 70px;
      right: -200px;
      width: 400px;
      height: 400px;
      z-index: 1;
      font-family: monospace;
      background: url('./电话弹窗.png') no-repeat;
      background-size:100% 100%;
      .close{
        position: absolute;
        top: 5px;
        right: 5px;
        width: 30px;
        height: 30px;
      }
      .textTitle{
        text-align: center;
        width: 100%;
        padding: 5px 0;
        font-size: 26px;
      }
      .textList{
        width: 380px;
        height: 300px;
        overflow: scroll;
        border: 1px solid #a59696;
        margin: 0 auto;
        .textBox{
          width: 100%;
          border-bottom: 1px solid #fff;
          label{
            display: flex;
            align-items: center;
            justify-content: space-between;
            .textDetail{
              width: 85%;
              padding: 10px;
              font-size: 24px;
              border-right: 1px solid #fff;
            }
          }
        }
        .textBox:last-child{
          border-bottom: none;
        }
      }
      .sendButton{
        width: 100%;
        text-align: center;
        padding-top: 10px;
      }
    }
    .content{
        display: flex;
        align-items: center;
        justify-content: space-between;
        .personBox{
            width: 358px;
            height: 220px;
            background: url('./矩形.png');
            background-size: 100% 100%;
            background-repeat: no-repeat;
            display: flex;
            align-items: center;
            position: relative;
            .position{
                width: 54px;
                height: 100%;
                border-radius: 0px 0px 4px 4px;
                color: rgba(82,214,231,1);
                font-size: 26px;
                writing-mode:vertical-rl;
                text-align: center;
                font-weight: bold;
                line-height: 54px;
                background: url('./小矩形.png');
                background-size: 100% 100%;
                background-repeat: no-repeat;
            }
            .information1{
                padding: 20px;
                width: 364px;
                height: 100%;
                display: flex;
                align-items: center;
                >img{
                    width: 140px;
                    height: 160px;
                    margin-right: 18px;
                }
                >div:last-child{
                    >div:first-child{
                        font-size: 34px;
                        color: rgba(200,224,255,1);
                        margin-bottom: 5px;
                    }
                    >div:last-child{
                        font-size: 26px;
                        color: rgba(172,207,254,1);
                        overflow: hidden;
                        height: 120px;
                        text-overflow: ellipsis;
                        -webkit-line-clamp: 3;
                        display: -webkit-box;
                        -webkit-box-orient: vertical;
                    }

                }
            }
            .information2{
                padding: 20px;
                width: 304px;
                height: 100%;
                >div:first-child{
                    font-size: 34px;
                    color: rgba(200,224,255,1);
                    margin-bottom: 5px;
                    >img{
                        width: 40px;
                        height: 40px;
                        margin-left: 9px;
                        vertical-align: sub;
                    }
                }
                >div:last-child{
                    font-size: 26px;
                    overflow: hidden;
                    height: 120px;
                    text-overflow: ellipsis;
                    -webkit-line-clamp: 3;
                    display: -webkit-box;
                    -webkit-box-orient: vertical;
                    color: rgba(172,207,254,1);
                }
            }
            .phonePop{
                position: absolute;
                top: 20px;
                right: -20px;
                width: 222px;
                height: 300px;
                z-index: 1;
                background: url('./电话弹窗.png') no-repeat;
                background-size:100% 100%;
                text-align: center;
                >img{
                    position: absolute;
                    top: 5px;
                    right: 5px;
                     width: 30px;
                    height: 30px;
                }
                >div:nth-child(2){
                    color: #C8E0FF;
                    font-size: 28px;
                    margin-bottom: 25px;
                    margin-top: 15px;
                }
                >div:nth-child(3){
                    color: #C8E0FF;
                    font-size: 24px;
                    margin-bottom: 25px;
                }
                >div:nth-child(4){
                    color: #ACCFFE;
                    font-size: 28px;
                    font-weight: bold;
                }
                >div:nth-child(5){
                    color: #0A2047;
                    font-size: 22px;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    width: 72px;
                    height: 72px;
                    border: 4px solid rgba(255,255,255,0.12);
                    border-radius: 50%;
                    background: url('./椭圆形.png') no-repeat;
                    background-size: 100% 100%;
                    margin: 0 auto
                }
            }
        }
        .personBox:first-child{
          width: 418px;
        }
    }
    .pop1{
        position: absolute;
        top: 0;
        left: 0;
        background: rgba(10,32,71,1);
        .bigTitle{
            width: 1170px;
            height: 73px;
            line-height: 73px;
            background: url('./标题背景.png') no-repeat;
            background-size: 100% 100%;
            text-align: left;
            padding-left: 60px;
            letter-spacing: 1px;
            font-size: 28px;
            font-weight: bold;
            position: relative;
            >img{
              position: absolute;
              top: 10px;
              right: 5px;
              width: 40px;
              height: 40px;
            }
        }
        >div:last-child{
            width: 1170px;
            height: 1358px;
            padding: 24px 29px;
            .row1{
                display: flex;
                align-items: center;
                >div:nth-child(1){
                    width: 857px;
                    height: 54px;
                    background: url('./title2.png') no-repeat;
                    background-size: 100% 100%;
                    color: rgba(200,224,255,1);
                    font-size: 24px;
                    line-height: 54px;
                    padding-left: 14px;
                }
                >div:nth-child(2){
                    width: 122px;
                    height: 56px;
                    background: url('./button1.png') no-repeat;
                    background-size: 100% 100%;
                    line-height: 56px;
                    text-align: center;
                    color: rgba(200,224,255,1);
                    font-size: 22px;
                    margin-right: 7px;
                }
                >div:nth-child(3){
                    width: 122px;
                    height: 56px;
                    background: url('./button1.png') no-repeat;
                    background-size: 100% 100%;
                    line-height: 56px;
                    text-align: center;
                    color: rgba(200,224,255,1);
                    font-size: 22px;
                }
                margin-bottom: 20px;
            }
            .row2{
                width: 100%;
                display: flex;
                align-items: center;
                position: relative;
                justify-content: space-between;
                .personBox{
                    width: 348px;
                    height: 148px;
                    background: url('./矩形.png');
                    background-size: 100% 100%;
                    background-repeat: no-repeat;
                    display: flex;
                    align-items: center;
                    position: relative;
                    .position{
                        width: 54px;
                        height: 100%;
                        border-radius: 0px 0px 4px 4px;
                        color: rgba(82,214,231,1);
                        font-size: 26px;
                        writing-mode:vertical-rl;
                        text-align: center;
                        line-height: 54px;
                        background: url('./小矩形.png');
                        background-size: 100% 100%;
                        background-repeat: no-repeat;
                    }
                    .information1{
                        padding: 20px;
                        width: 324px;
                        height: 100%;
                        display: flex;
                        >img{
                            width: 69px;
                            height: 90px;
                            margin-right: 18px;
                        }
                        >div:last-child{
                            >div:first-child{
                                font-size: 28px;
                                color: rgba(200,224,255,1);
                                margin-bottom: 0px;
                            }
                            >div:last-child{
                                font-size: 22px;
                                color: rgba(172,207,254,1);
                                overflow: hidden;
                                height: 70px;
                                text-overflow: ellipsis;
                                -webkit-line-clamp: 2;
                                display: -webkit-box;
                                -webkit-box-orient: vertical;
                            }

                        }
                    }
                    .information2{
                        padding: 20px;
                        width: 324px;
                        height: 100%;
                        >div:first-child{
                            font-size: 28px;
                            color: rgba(200,224,255,1);
                            margin-bottom: 0px;
                            >img{
                                width: 40px;
                                height: 40px;
                                margin-left: 9px;
                                vertical-align: sub;
                            }
                        }
                        >div:last-child{
                            font-size: 22px;
                            color: rgba(172,207,254,1);
                            overflow: hidden;
                            height: 70px;
                            text-overflow: ellipsis;
                            -webkit-line-clamp: 2;
                            display: -webkit-box;
                            -webkit-box-orient: vertical;
                        }
                    }
                    .phonePop{
                        position: absolute;
                        top: 20px;
                        right: -20px;
                        width: 222px;
                        height: 300px;
                        z-index: 1;
                        background: url('./电话弹窗.png') no-repeat;
                        background-size:100% 100%;
                        text-align: center;
                        >img{
                            position: absolute;
                            top: 5px;
                            right: 5px;
                            width: 30px;
                            height: 30px;
                        }
                        >div:nth-child(2){
                            color: #C8E0FF;
                            font-size: 28px;
                            margin-bottom: 25px;
                            margin-top: 15px;
                        }
                        >div:nth-child(3){
                            color: #C8E0FF;
                            font-size: 24px;
                            margin-bottom: 25px;
                        }
                        >div:nth-child(4){
                            color: #ACCFFE;
                            font-size: 28px;
                            font-weight: bold;
                        }
                        >div:nth-child(5){
                            color: #0A2047;
                            font-size: 22px;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            width: 72px;
                            height: 72px;
                            border: 4px solid rgba(255,255,255,0.12);
                            border-radius: 50%;
                            background: url('./椭圆形.png') no-repeat;
                            background-size: 100% 100%;
                            margin: 0 auto
                        }
                    }
                }
                >.personBox:first-child{
                    width: 379px;
                }
                >img{
                    position: absolute;
                    top: 10px;
                    right: 10px;
                }
                margin-bottom: 24px;
            }
            .row3{
                display: flex;
                align-items: center;
                margin-bottom: 23px;
                >div:nth-child(1){
                    width: 474px;
                    height: 54px;
                    background: url('./title1.png') no-repeat;
                    background-size: 100% 100%;
                    color: rgba(200,224,255,1);
                    font-size: 24px;
                    line-height: 54px;
                    padding-left: 20px;
                }
                >div:nth-child(2){
                    width: 204px;
                    height: 54px;
                    background: url('./矩形备份17.png');
                    background-repeat: no-repeat;
                    background-size: 100% 100%;
                    line-height: 54px;
                    text-align: center;
                    color: rgba(200,224,255,1);
                    font-size: 24px;
                    margin-right: 10px;
                }
                >div:nth-child(3){
                    width: 204px;
                    height: 54px;
                    background: url('./矩形备份17.png');
                    background-repeat: no-repeat;
                    background-size: 100% 100%;
                    line-height: 54px;
                    text-align: center;
                    color: rgba(200,224,255,1);
                    font-size: 24px;
                    margin-right: 10px;
                }
                >div:nth-child(4){
                    width: 204px;
                    height: 54px;
                    background: url('./矩形备份17.png');
                    background-repeat: no-repeat;
                    background-size: 100% 100%;
                    line-height: 54px;
                    text-align: center;
                    color: rgba(200,224,255,1);
                    font-size: 24px;
                }
                .activeButton{
                    background: url('./矩形备份14.png') !important;
                    background-repeat: no-repeat !important;
                    background-size: 100% 100% !important;
                }
            }
            .row4{
                .checkBox{
                    display: flex;
                    margin-bottom: 18px;
                    .search{
                        width: 48px;
                        height: 48px;
                        color: rgba(82,214,231,1);
                        font-size: 22px;
                        text-align: center;
                        line-height: 48px;
                        margin-right: 8px;
                        background: url('./矩形备份43.png');
                    }
                    .refresh{
                        width: 48px;
                        height: 48px;
                        color: rgba(82,214,231,1);
                        font-size: 22px;
                        text-align: center;
                        line-height: 48px;
                        background: url('./矩形备份43.png');
                    }
                }
                .mytable1{
                    width: 100%;
                    color: rgba(200,224,255,1);
                    font-size: 22px;
                    text-align: center;
                    margin-bottom: 18px;
                    .tablehead{
                        display: flex;
                        align-items: center;
                        height: 58px;
                        width: 100%;
                        background: linear-gradient(180deg,rgba(49,131,233,0.20), rgba(41,84,135,0.10));
                        >div:nth-child(1){
                            width: 20%;
                        }
                        >div:nth-child(2){
                            width: 30%;
                        }
                        >div:nth-child(3){
                            width: 50%;
                        }
                    }
                    .tablebody{
                        height: 360px;
                        overflow-y: scroll;
                        .rows{
                            display: flex;
                            align-items: center;
                            height: 58px;
                            width: 100%;
                            background: linear-gradient(180deg,rgba(49,131,233,0.20), rgba(41,84,135,0.10));
                            >div:nth-child(1){
                                width: 20%;
                                overflow: hidden;
                                white-space: nowrap;
                                text-overflow: ellipsis;
                            }
                            >div:nth-child(2){
                                width: 30%;
                                overflow: hidden;
                                white-space: nowrap;
                                text-overflow: ellipsis;
                            }
                            >div:nth-child(3){
                                width: 50%;
                                overflow: hidden;
                                white-space: nowrap;
                                text-overflow: ellipsis;
                            }
                        }
                        >.rows:nth-child(odd){
                            background: url('./row.png') no-repeat;
                            background-size: 100% 100%;
                        }
                    }
                }
                .mytable2{
                    width: 100%;
                    color: rgba(200,224,255,1);
                    font-size: 22px;
                    text-align: center;
                    margin-bottom: 18px;
                    .tablehead{
                        display: flex;
                        align-items: center;
                        height: 58px;
                        width: 100%;
                        background: linear-gradient(180deg,rgba(49,131,233,0.20), rgba(41,84,135,0.10));
                        >div:nth-child(1){
                            width: 10%;
                        }
                        >div:nth-child(2){
                            width: 35%;
                        }
                        >div:nth-child(3){
                            width: 40%;
                        }
                        >div:nth-child(4){
                            width: 15%;
                        }
                    }
                    .tablebody{
                        height: 360px;
                        overflow-y: scroll;
                        .rows{
                            display: flex;
                            align-items: center;
                            height: 58px;
                            width: 100%;
                            background: linear-gradient(180deg,rgba(49,131,233,0.20), rgba(41,84,135,0.10));
                            >div:nth-child(1){
                                width: 10%;
                                overflow: hidden;
                                white-space: nowrap;
                                text-overflow: ellipsis;
                            }
                            >div:nth-child(2){
                                width: 35%;
                                overflow: hidden;
                                white-space: nowrap;
                                text-overflow: ellipsis;
                            }
                            >div:nth-child(3){
                                width: 40%;
                                overflow: hidden;
                                white-space: nowrap;
                                text-overflow: ellipsis;
                            }
                            >div:nth-child(4){
                                width: 15%;
                                overflow: hidden;
                                white-space: nowrap;
                                text-overflow: ellipsis;
                            }
                        }
                        >.rows:nth-child(odd){
                            background: url('./row.png') no-repeat;
                            background-size: 100% 100%;
                        }
                    }
                }
                .mytable3{
                    width: 100%;
                    color: rgba(200,224,255,1);
                    font-size: 22px;
                    text-align: center;
                    margin-bottom: 18px;
                    .tablehead{
                        display: flex;
                        align-items: center;
                        height: 58px;
                        width: 100%;
                        background: linear-gradient(180deg,rgba(49,131,233,0.20), rgba(41,84,135,0.10));
                        >div:nth-child(1){
                            width: 20%;
                        }
                        >div:nth-child(2){
                            width: 30%;
                        }
                        >div:nth-child(3){
                            width: 30%;
                        }
                        >div:nth-child(4){
                            width: 20%;
                        }
                    }
                    .tablebody{
                        height: 360px;
                        overflow-y: scroll;
                        .rows{
                            display: flex;
                            align-items: center;
                            height: 58px;
                            width: 100%;
                            background: linear-gradient(180deg,rgba(49,131,233,0.20), rgba(41,84,135,0.10));
                            >div:nth-child(1){
                                width: 20%;
                                overflow: hidden;
                                white-space: nowrap;
                                text-overflow: ellipsis;
                            }
                            >div:nth-child(2){
                                width: 30%;
                                overflow: hidden;
                                white-space: nowrap;
                                text-overflow: ellipsis;
                            }
                            >div:nth-child(3){
                                width: 30%;
                                overflow: hidden;
                                white-space: nowrap;
                                text-overflow: ellipsis;
                            }
                            >div:nth-child(4){
                                width: 20%;
                                overflow: hidden;
                                white-space: nowrap;
                                text-overflow: ellipsis;
                            }
                        }
                        >.rows:nth-child(odd){
                            background: url('./row.png') no-repeat;
                            background-size: 100% 100%;
                        }
                    }
                }
                margin-bottom: 20px;
            }
            .row5{
                width: 100%;
                display: flex;
                align-items: center;
                justify-content: space-between;
                .left,.right{
                    width: 542px;
                    height: auto;
                    >div:first-child{
                        width: 100%;
                        height: 54px;
                        line-height: 54px;
                        background: url('./矩形2.png') no-repeat;
                        background-size: 100% 100%;
                        padding-left: 20px;
                        color: rgba(200,224,255,1);
                        font-size: 24px;
                        margin-bottom: 17px;
                    }
                    .checkBox{
                        display: flex;
                        margin-bottom: 11px;
                        .search{
                            width: 48px;
                            height: 48px;
                            color: rgba(82,214,231,1);
                            font-size: 22px;
                            text-align: center;
                            line-height: 48px;
                            margin-right: 8px;
                            background: url('./矩形备份43.png');
                        }
                        .refresh{
                            width: 48px;
                            height: 48px;
                            color: rgba(82,214,231,1);
                            font-size: 22px;
                            text-align: center;
                            line-height: 48px;
                            background: url('./矩形备份43.png');
                        }
                    }
                    .list{
                        height: 282px;
                        width: 100%;
                        overflow-y:scroll;
                        .rows{
                            height: 60px;
                            width: 100%;
                            padding-right: 8px;
                            border: 1px solid;
                            border-image: linear-gradient(90deg, #2e578b, #2e578b) 1 1;
                            border-radius: 2px;
                            margin-bottom: 14px;
                            display: flex;
                            align-items: center;
                            padding-left: 9px;
                            color: #c8e0ff;
                            font-size: 21px;
                            >div:first-child{
                                width:12px;
                                height: 12px;
                                background: linear-gradient(180deg,#d4e7ff, #d8d8d8);
                                border-radius: 50%;
                                margin-right: 10px;
                            }
                            >div:last-child{
                              width: calc(100% - 20px);
                              white-space: nowrap;
                              overflow: hidden;
                              text-overflow:ellipsis;
                            }
                        }
                    }
                }
            }
        }
    }
    .pop2{
        position: absolute;
        top: 0;
        right: -1170px;
        background: rgba(10,32,71,1);
        .bigTitle{
            width: 1170px;
            height: 73px;
            line-height: 73px;
            background: url('./职能职责.png');
            background-repeat: no-repeat;
            background-size: 100% 100%;
            text-align: right;
            >img{
                width: 40px;
                height: 40px;
            }
        }
        >div:last-child{
            width: 1170px;
            height: 1358px;
            padding: 24px 29px;
            .switchButton{
                width: 100%;
                display: flex;
                align-items: center;
                justify-content: space-between;
                >div{
                    width:360px;
                    height: 58px;
                    background: url('./normal.png');
                    background-repeat: no-repeat;
                    background-size: 100% 100%;
                    color: rgba(200,224,255,1);
                    font-size: 24px;
                    text-align: center;
                    line-height: 58px;
                }
                .checked{
                    background: url('./checkButton.png');
                    background-repeat: no-repeat;
                    background-size: 100% 100%;
                }
            }
            .text{
                font-size: 26px;
                color: rgba(200,224,255,1);
                height: calc(100% - 60px);
                width: 100%;
                padding-top:20px;
                overflow-y: scroll;
                white-space: pre-line;
            }
        }
    }
    .pop3{
        position: absolute;
        top: 0;
        right: -1170px;
        width: 1170px;
        height: auto;
        background: rgba(10,32,71,1);
        .bigTitle{
            width: 1170px;
            height: 73px;
            line-height: 73px;
            background: url('./工作清单.png');
            background-repeat: no-repeat;
            background-size: 100% 100%;
            text-align: right;
            >img{
                width: 40px;
                height: 40px;
            }
        }
        >div:last-child{
            width: 1170px;
            height: 1358px;
            padding: 24px 29px;
            .switchButton{
                width: 100%;
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 20px;
                >div{
                    width:360px;
                    height: 58px;
                    background: url('./normal.png');
                    background-repeat: no-repeat;
                    background-size: 100% 100%;
                    color: rgba(200,224,255,1);
                    font-size: 24px;
                    text-align: center;
                    line-height: 58px;
                }
                .checked{
                    background: url('./checkButton.png');
                    background-repeat: no-repeat;
                    background-size: 100% 100%;
                }
            }
            .checkBox{
                margin-bottom: 30px;
            }
            .mytable{
                    width: 100%;
                    color: rgba(200,224,255,1);
                    font-size: 22px;
                    text-align: center;
                    .tablehead{
                        display: flex;
                        align-items: center;
                        height: 60px;
                        width: 100%;
                        background: linear-gradient(180deg,rgba(49,131,233,0.20), rgba(41,84,135,0.10));
                        >div:nth-child(1){
                            width: 5%;
                        }
                        >div:nth-child(2){
                            width: 15%;
                        }
                        >div:nth-child(3){
                            width: 50%;
                        }
                        >div:nth-child(4){
                            width: 30%;
                        }
                    }
                    .tablebody{
                        height: 1000px;
                        overflow-y: scroll;
                        .rows{
                            display: flex;
                            align-items: center;
                            height: 60px;
                            width: 100%;
                            >div:nth-child(1){
                                width: 5%;
                                overflow: hidden;
                                white-space: nowrap;
                                text-overflow: ellipsis;
                            }
                            >div:nth-child(2){
                                width: 15%;
                                overflow: hidden;
                                white-space: nowrap;
                                text-overflow: ellipsis;
                            }
                            >div:nth-child(3){
                                width: 50%;
                                overflow: hidden;
                                white-space: nowrap;
                                text-overflow: ellipsis;
                            }
                            >div:nth-child(4){
                                width: 30%;
                                overflow: hidden;
                                white-space: nowrap;
                                text-overflow: ellipsis;
                            }
                        }
                        >.rows:nth-child(odd){
                            background: url('./row.png') no-repeat;
                            background-size: 100% 100%;
                        }
                    }
            }
        }
    }
    .pop4{
        position: absolute;
        top: 0;
        right: -1170px;
        background: rgba(10,32,71,1);
        .bigTitle{
            width: 1170px;
            height: 73px;
            line-height: 73px;
            background: url('./街道值班信息.png');
            background-repeat: no-repeat;
            background-size: 100% 100%;
            text-align: right;
            >img{
                width: 40px;
                height: 40px;
            }
        }
        >div:last-child{
            width: 1170px;
            height: 1358px;
            padding: 24px 29px;
            .CYZBTX{
              overflow-y: scroll;
              display: flex;
              flex-wrap: wrap;
              width: 100%;
              height: 100%;
              // flex-direction: column;
              // justify-content: space-around;
              // align-items: center;
              .streetBox{
                // width: 1324px;
                height: 250px;
                margin-bottom: 30px;
                .streetName{
                  width: 368px;
                  height: 48px;
                  background: url('./街道标题背景.png') no-repeat;
                  background-size: 100% 100%;
                  color: rgba(172,207,254,1);
                  font-size: 22px;
                  line-height: 48px;
                  padding-left: 14px;
                  margin-bottom: 10px;
                }
                .streetInfo{
                  width: 100%;
                  display: flex;
                  justify-content: space-between;
                  align-items: center;
                  position: relative;
                  .personnelInfo{
                    display: flex;
                    >div:last-child{
                      width: 230px;
                      position: relative;
                      .name{
                        color: rgba(172,207,254,1);
                        font-size: 22px;
                        padding-left: 10px;
                      }
                      .phone{
                        color: rgba(172,207,254,1);
                        font-size: 22px;
                        padding-left: 10px;
                      }
                      .post{
                        color: rgba(172,207,254,1);
                        font-size: 20px;
                        padding-left: 10px;
                        // white-space: pre-wrap;
                        white-space:nowrap;
                        overflow: hidden;
                        text-overflow: ellipsis;
                        width: 100%;
                        height:24px;
                      }
                      .position{
                        color: rgba(172,207,254,1);
                        font-size: 22px;
                        padding-left: 10px;
                        height: 35px;
                        line-height: 35px;
                        width: 200px;
                        background: url('./职位背景.png') no-repeat;
                        background-size: 100% 100%;
                        position: absolute;
                        bottom: 0;
                      }
                    }
                  }
                  .phonePop{
                        position: absolute;
                        top: 26px;
                        right: 0px;
                        width: 222px;
                        height: 300px;
                        z-index: 1;
                        background: url('./电话弹窗.png') no-repeat;
                        background-size:100% 100%;
                        text-align: center;
                        >img{
                            position: absolute;
                            top: 5px;
                            right: 5px;
                            width: 30px;
                            height: 30px;
                        }
                        >div:nth-child(2){
                            color: #C8E0FF;
                            font-size: 28px;
                            margin-bottom: 25px;
                            margin-top: 15px;
                        }
                        >div:nth-child(3){
                            color: #C8E0FF;
                            font-size: 24px;
                            margin-bottom: 25px;
                        }
                        >div:nth-child(4){
                            color: #ACCFFE;
                            font-size: 28px;
                            font-weight: bold;
                        }
                        >div:nth-child(5){
                            color: #0A2047;
                            font-size: 22px;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            width: 72px;
                            height: 72px;
                            border: 4px solid rgba(255,255,255,0.12);
                            border-radius: 50%;
                            background: url('./椭圆形.png') no-repeat;
                            background-size: 100% 100%;
                            margin: 0 auto
                        }
                 }
                }
              }
              .streetBox:nth-child(3n+3){
                .textPop{
                  position: absolute;
                  top: 70px;
                  right: 0px;
                }
              }
            }
        }
    }
}
</style>
<style lang="scss">
.checkBox{
        .ivu-date-picker-transfer {
            max-height: unset !important;
        }
        .ivu-date-picker-rel{
            width: 285px !important;
        }
        .ivu-date-picker{
            position: relative;
                .ivu-select-dropdown{
                    max-height: unset !important;
                    top: 28px !important;
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
                    height: 33px !important;
                    line-height: 33px !important;
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
            background: transparent;
                .ivu-icon-ios-calendar-outline{
                line-height: 33px !important;
                color: #000 !important;
                font-size: 22px !important;

                }
                .ivu-icon-ios-close-circle{
                line-height: 33px !important;
                color: #000 !important;
                font-size: 22px !important;
                }
                .ivu-input{
                width: 285px !important;
                height: 33px !important;
                color: #C8E0FF !important;
                font-size: 20px !important;
                border: 1px solid;
                border-image: linear-gradient(90deg, #2e578b, #2e578b) 1 1;
                border-radius: 2px;
                }
        }
    }
</style>
