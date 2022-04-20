<template>
  <div class="content">
    <button v-show="true" @click="addshezangmarkers('didian')" style="position:absolute;z-index:9999;width:100px;height:80px;top:400px;left:200px;">获取视角</button>
    <button v-show="true" @click="removeshezangmarkers('didian')" style="position:absolute;z-index:9999;width:100px;height:80px;top:500px;left:200px;">获取视角1</button>
    <button v-show="true" @click="initJXJ" style="position:absolute;z-index:9999;width:100px;height:80px;top:600px;left:200px;">获取视角1</button>
    <button v-show="true" @click="initBase()" style="position:absolute;z-index:9999;width:100px;height:80px;top:700px;left:200px;">获取视角</button>
    <button v-show="true" @click="removeJxJ()" style="position:absolute;z-index:9999;width:100px;height:80px;top:800px;left:200px;">获取视角1</button>
    <!-- <div id="SZpopBig" v-show="popshow">
      <div class="poptitle">
        小旅馆
      </div>
      <div class="CloseBtn" @click="popshow = false"></div>
      <div class="lineContain">
        <div class="line">名称: {{nowShowData['名称']}}</div>
        <div class="line">标准地址:{{nowShowData['地址']}}</div>
        <div class="line">房间数:{{nowShowData['房间数']}}</div>
        <div class="line">床铺数:{{nowShowData['床铺数']}}</div>
        <div class="line">社区民警(电话):{{nowShowData['社区民警（电话）']}}</div>
        <div class="line">网格员(电话):{{nowShowData['网格员（电话）']}}</div>
        <div class="line">
          微消站(电话):{{nowShowData['微消站（电话）']}}
        </div>
        <button @click="ShowZofang(nowShowData.index)">入住记录</button>
        <button @click="ShowRuzhu(nowShowData.index)">走访记录</button>
      </div>
    </div>-->
    <div id="SZpopBig" v-show="SZDataShowBig">
      <div class="XQBoxTan">
        <div class="poptitle">{{SZData['网格区']}}</div>
        <div class="CloseBtn" @click="SZDataShowBig = false"></div>
        <div class="lineContain">
          <div class="line">网格员姓名: {{SZData['网格员姓名']}}</div>
          <div class="line">性别: {{SZData['性别']}}</div>
          <div class="line">电话: {{SZData['电话']}}</div>
          <div class="line">职务: {{SZData['职务']}}</div>
          <div class="line">对应街道负责人姓名: {{SZData['对应街道负责人姓名']}}</div>
          <div class="line">对应街道负责人电话: {{SZData['对应街道负责人电话']}}</div>
        </div>
      </div>
    </div>
    <div id="popWGQ" v-show="popshowWGQ">
      <div class="XQBoxTan">
        <div class="poptitle">{{WGQData['网格区']}}</div>
        <div class="CloseBtn" @click="popshowWGQ = false"></div>
        <div class="lineContain">
          <div class="line">网格员姓名: 胡德琴</div>
          <!-- {{WGQData['网格员姓名']}} -->
          <div class="line">性别: 女</div>
          <div class="line">电话: 13880315693</div>
          <!-- <div class="line">职务: {{WGQData['职务']}}</div> -->
          <div class="line">对应社区民警姓名: 罗恩祥</div>
          <div class="line">对应社区民警电话: 13281004566</div>
        </div>
      </div>
    </div>
    <div id="popXMQ" v-show="popshowXMQ">
      <div class="XQBoxTan">
        <div class="poptitle">藏族人组织纪念法应急预案</div>
        <div class="CloseBtn" @click="popshowXMQ = false"></div>
        <div class="lineContain">
          <div class="line">预案概述: 在浆洗街街道发现5名藏族喇嘛坐诵经和举像</div>
          <div class="line">预案等级: 高</div>
          <div class="line">启动条件: 各部门资源充足</div>
          <div class="line">预案内容: xxxx年xx月xx日xx时xx分，区公安局网安大队民警xx在微信群发现，一名交xxx藏族人在微信群邀约群内藏族人员预xxx月xxx日前往武侯区xxx藏餐馆给xxxx组件纪念发挥，</div>
        </div>
      </div>
    </div>
    <div id="popBig" v-show="popshowBig">
      <div class="TbaleTan" v-if="ShowTableTan">
        <div class="CloseBtn" @click="popshowBig = false"></div>
        <div class="TableHead">
          <tr>
            <th
              v-for="(data, index) in TableTanData.columns"
              :key="index"
              :style="{width:`calc(${100 / TableTanData.columns.length}%)`}"
            >{{ tableTanTitle[data] }}</th>
          </tr>
        </div>
        <div class="TableBody" v-if="TableTanData.rows.length > 0">
          <tr v-for="(rowsData, i) in TableTanData.rows" :key="i" @click="showXQBoxTan(rowsData)">
            <th
              v-for="(data, index) in TableTanData.columns"
              :key="index"
              :style="{width:`calc(${100 / TableTanData.columns.length}%)`}"
            >{{ rowsData[data] }}</th>
          </tr>
        </div>
      </div>
      <div class="XQBoxTan" v-else>
        <div class="poptitle">{{ iswbzzs? nowShowData.room_name:'小旅馆'}}</div>
        <div class="CloseBtn" @click="popshowBig = false"></div>
        <div class="lineContain" v-if='!iswbzzs'>
          <div class="line">名称: {{nowShowData.placeName}}</div>
          <div class="line">标准地址:{{nowShowData.address}}</div>
          <div class="line">房间数:{{nowShowData.roomNum}}</div>
          <div class="line">床铺数:{{nowShowData.bedNum}}</div>
          <div
            class="line"
          >社区民警(电话):{{nowShowData.communityPolice}}:{{nowShowData.communityPolicePhone}}</div>
          <div class="line">网格员(电话):{{nowShowData.gridMember}}:{{nowShowData.gridMemberPhone}}</div>
          <div class="line">微消站(电话):{{nowShowData.fireStation}}:{{nowShowData.fireStationPhone}}</div>
          <button @click="ShowZofang(nowShowData.address)">入住记录</button>
          <button @click="ShowRuzhu(nowShowData.address)">走访记录</button>
        </div>
        <div class="lineContain" v-else>
          <div class="line">所属街道: {{nowShowData.street || '无'}}</div>
          <div class="line">地址:{{nowShowData.address || '无'}}</div>
          <div class="line">业主（房东）姓名:{{nowShowData.owner_name || '无'}}</div>
          <div class="line">业主（房东）联系电话:{{nowShowData.owner_phone || '无'}}</div>
          <div class="line">经营者姓名:{{nowShowData.manager_name || '无'}}</div>
          <div class="line">经营者联系电话:{{nowShowData.maager_phone || '无'}}</div>
          <div class="line">房间数:{{nowShowData.room_number || '无'}}</div>
          <div class="line">床铺数:{{nowShowData.bed_number || '无'}}</div>
          <div class="line">网格员姓名:{{nowShowData.gridman_name || '无'}}</div>
          <div class="line">网格员联系电话:{{nowShowData.gridman_phone || '无'}}</div>
          <div class="line">社区民警姓名:{{nowShowData.police_name || '无'}}</div>
          <div class="line">社区民警联系电话:{{nowShowData.police_phone || '无'}}</div>
          <div class="line">区域微型消防站联络员姓名:{{nowShowData.liaison_name || '无'}}</div>
          <div class="line">区域微型消防站联络电话:{{nowShowData.liaison_phone || '无'}}</div>
        </div>
        <div class="BackBtn" @click="ShowTableTan = true">返回</div>
      </div>
    </div>
    <div id="cesiumContainer" />
  </div>
</template>

<script>
import createBlurStage from './CesiumEdgeStage/createBlurStage.js'
import * as turf from '@turf/turf'
import Imgpositions from './Imgpositions.js'
import wanggepositions from './wanggepositions.js'
import { gcj02_to_wgs84 } from './transform.js'
var viewer
var tileset
var tilesetJxJ
var contrastBias
var baseObject
var highLightPolygon
var hightLightMat
var billboardMarkers = []
let wangges = []// 网格数据
let jxjdatas = []// 浆洗街行政区划数据
let videoPoint = []// 摄像头数据
let GongAnPoint = []// 公安数据
let keyPlacesPoint = []// 重点数据
let GuanKongquPoint = []// 管控区
let shezangmarkers = {} // 涉藏点位
let xingzhengquhuaPolygons = {
  '簇锦街道': [],
  '红牌楼街道': [],
  '华兴街道': [],
  '火车南站街道': [],
  '金花桥街道': [],
  '晋阳街道': [],
  '机投桥街道': [],
  '望江路街道': [],
  '簇桥街道': [],
  '浆洗街街道': [],
  '玉林街道': []
}// 记录行政区划面
export default {
  name: 'pageShow',
  props: ['nowPageName'],
  data () {
    return {
      popshow: false,
      popshowBig: false,
      popshowWGQ: false,
      popshowXMQ: false,
      SZDataShowBig: false,
      ShowTableTan: true,
      CheckEdId: 0,
      x: 0,
      y: 0,
      z: 0,
      header: process.env.NODE_ENV === 'development' ? './static/' : './',
      TableTanData: { columns: [], rows: [] },
      tableTanTitle: {
        placeName: '名称',
        address: '标准地址',
        roomNum: '房间数',
        bedNum: '床铺数',
        room_name: '名称',
        street: '所属街道',
        room_number: '房间数',
        bed_number: '床铺数'
      },
      tableDataXunCha: [
        {
          columns: ['巡查人名称', '巡查人手机号', '巡查内容', '巡查时间'],
          rows: [
            {
              巡查人名称: '刘颖颖',
              巡查人手机号: '18782925774',
              巡查内容: '例行检查',
              巡查时间: '2022-1-9'
            },
            {
              巡查人名称: '张敏',
              巡查人手机号: '13980953446',
              巡查内容: '应急安全',
              巡查时间: '2022-1-19'
            },
            {
              巡查人名称: '柴芝凤',
              巡查人手机号: '13881958111',
              巡查内容: '治安维稳',
              巡查时间: '2022-2-19'
            }
          ]
        },
        {
          columns: ['巡查人名称', '巡查人手机号', '巡查内容', '巡查时间'],
          rows: [
            {
              巡查人名称: '刘颖颖',
              巡查人手机号: '18380447548',
              巡查内容: '治安维稳',
              巡查时间: '2022-1-22'
            },
            {
              巡查人名称: '彭艺',
              巡查人手机号: '13980953446',
              巡查内容: '应急安全',
              巡查时间: '2022-1-11'
            },
            {
              巡查人名称: '魏俊',
              巡查人手机号: '13980953446',
              巡查内容: '例行检查',
              巡查时间: '2022-1-19'
            }
          ]
        },
        {
          columns: ['巡查人名称', '巡查人手机号', '巡查内容', '巡查时间'],
          rows: [
            {
              巡查人名称: '曾大勇',
              巡查人手机号: '17309876543',
              巡查内容: '应急安全',
              巡查时间: '2022-2-19'
            },
            {
              巡查人名称: '刘颖颖',
              巡查人手机号: '13881958111',
              巡查内容: '治安维稳',
              巡查时间: '2022-2-13'
            },
            {
              巡查人名称: '陈有光',
              巡查人手机号: '13980953446',
              巡查内容: '例行检查',
              巡查时间: '2022-1-22'
            }
          ]
        }
      ],
      tableDataRuZhu: [
        {
          columns: ['姓名', '身份证', '电话', '入住日期', '场所ID'],
          rows: [
            {
              姓名: ' 杨东海',
              身份证: '510122199510702363',
              电话: '17708192161',
              入住日期: '2022-02-20 21:22:26',
              场所ID: '2'
            },
            {
              姓名: ' 陈斌',
              身份证: '510623199205044510',
              电话: '15700573360',
              入住日期: '2022-01-1 11:51:26',
              场所ID: '2'
            },
            {
              姓名: ' 刘长城 ',
              身份证: '510421199110101163',
              电话: '13880804350',
              入住日期: '2022-02-01 12:51:26',
              场所ID: '3'
            },
            {
              姓名: ' 阿曲',
              身份证: '510623199305064520',
              电话: '17708192161',
              入住日期: '2022-02-4 11:51:26',
              场所ID: '1'
            }
          ]
        },
        {
          columns: ['姓名', '身份证', '电话', '入住日期', '场所ID'],
          rows: [
            {
              姓名: ' 谭晓凯',
              身份证: '510421199110101163',
              电话: '17708192161',
              入住日期: '2022-02-1 22:51:26',
              场所ID: '2'
            },
            {
              姓名: ' 李松',
              身份证: '510623199205044510',
              电话: '15700573360',
              入住日期: '2022-02-14 13:51:26',
              场所ID: '2'
            },
            {
              姓名: ' 梁利全 ',
              身份证: '510122198612251759',
              电话: '13880804350',
              入住日期: '2022-02-11 14:51:26',
              场所ID: '3'
            },
            {
              姓名: ' 陈斌',
              身份证: '510122199510702363',
              电话: '17708192161',
              入住日期: '2022-02-1 8:51:26',
              场所ID: '1'
            }
          ]
        },
        {
          columns: ['姓名', '身份证', '电话', '入住日期', '场所ID'],
          rows: [
            {
              姓名: ' 侯刚',
              身份证: '510421199110101163',
              电话: '13980003218',
              入住日期: '2022-02-11 21:51:26',
              场所ID: '2'
            },
            {
              姓名: '  颜蕾',
              身份证: '510623199205044510',
              电话: '13438321320',
              入住日期: '2022-02-4 11:51:26',
              场所ID: '2'
            },
            {
              姓名: ' 梁利全 ',
              身份证: '510122199510702363',
              电话: '13880804350',
              入住日期: '2022-02-4 9:51:26',
              场所ID: '3'
            },
            {
              姓名: ' 刘颖颖',
              身份证: '510623199305064520',
              电话: '17708192161',
              入住日期: '2022-02-11 13:51:26',
              场所ID: '1'
            }
          ]
        }
      ],
      showBoxDate: [
        {
          名称: '小旅馆',
          地址: '武侯祠东街8号1-5-407',
          房间数: '2',
          床铺数: '6',
          '社区民警（电话）': '陈朝林 (17708192501)',
          '网格员（电话）': '邹澄  (13388177089)',
          '微消站（电话）': '刘长城 (15700573360)',
          index: 0
        },
        {
          名称: '小旅馆',
          地址: '武侯祠东街2号1-3-8',
          房间数: '2',
          床铺数: '5',
          '社区民警（电话）': '陈朝林 (17708192501)',
          '网格员（电话）': '邹澄 (13388177089)',
          '微消站（电话）': '刘长城 (15700573360)',
          index: 0
        },
        {
          名称: '小旅馆',
          地址: '武侯祠东街4号3-1-7',
          房间数: '2',
          床铺数: '9',
          '社区民警（电话）': '陈朝林 (17708192501)',
          '网格员（电话）': '邹澄  (13388177089)',
          '微消站（电话）': '刘长城 (15700573360)',
          index: 0
        }
      ],
      nowShowData: [],
      newSZCheckEdData: [],
      SZData: {},
      WGQData: {},
      AllData: [
        {
          网格区: '第一网格区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        },
        {
          网格区: '第二网格区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        },
        {
          网格区: '第三网格区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        },
        {
          网格区: '第四网格区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        },
        {
          网格区: '第五网格区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        },
        {
          网格区: '第六网格区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        },
        {
          网格区: '第七网格区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        },
        {
          网格区: '第八网格区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        },
        {
          网格区: '第九网格区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        }
      ]
    }
  },
  watch: {
    nowPageName: function () {
      this.fly()
      this.clearPoint()
      if (this.nowPageName && this.nowPageName.indexOf('涉藏概况') >= 0) {
        if (window.changeCheckedArry) {
          window.changeCheckedArry(this.newSZCheckEdData)
        }
        this.addPontXMQ()
      } else if (
        this.nowPageName &&
        this.nowPageName.indexOf('应急处突') >= 0
      ) {
        if (window.changeCheckedArry) {
          window.changeCheckedArry(this.newSZCheckEdData)
        }
        this.addPontXMQ()
      } else {
        this.initBase()
      }
      if (this.nowPageName && this.nowPageName.indexOf('群租房') >= 0) {
        this.axios.get(`/leaderview/WuHou/getOrgaDot`).then(data => {
          if (data.success) {
            let height = 100
            data.obj.forEach((d, index) => {
              this.addPointer(
                Cesium.Cartesian3.fromDegrees(
                  d.longitude * 1,
                  d.latitude * 1,
                  height
                ),
                'xiaoqu' + index,
                'static/img/xiaoqu.png',
                { columns: [], rows: d.arr }
              )
            })
          }
        })
      } else if (this.nowPageName && this.nowPageName.indexOf('未办证住所') >= 0) {
        this.axios.get(`/leaderview/QZF/getWBZ1`).then(data => {
          if (data.success) {
            let height = 100
            data.obj.dataArray.forEach((d, index) => {
              if (d.street === '望江路街道' || d.street === '金花桥街道') {
                d.items.forEach((ele, ind) => {
                  this.addPointer(
                    Cesium.Cartesian3.fromDegrees(
                      ele.longitude * 1,
                      ele.latitude * 1,
                      height
                    ),
                    'wbzzs' + d.street + ind,
                    'static/img/xiaoqu.png',
                    { columns: [], rows: ele.items[0].items }
                  )
                })
              }
            })
          }
        })//  getQZF4
      }
    }
  },
  mounted () {
    this.init3D()
    this.initBase()
    this.initModels()
    this.initPostrender()
    this.addPopEvent()
    this.fly()
    window.changeSZChecked = this.changeSZChecked
  },
  methods: {
    changeSZChecked (data) {
      this.newSZCheckEdData = data
      if (data.indexOf('社区区划') >= 0) {
        this.addJxJ()
      } else {
        this.removeJxJ()
      }

      if (data.indexOf('管控区') >= 0) {
        if (GuanKongquPoint.length === 0) {
          this.initSheZang1()
        }
      } else {
        this.removeSheZang1()
      }
      if (data.indexOf('网格员') >= 0) {
        if (wangges.length === 0) {
          this.addWangge()
        }
      } else {
        this.removeWangge()
      }
      if (data.indexOf('公安网格') >= 0) {
        if (GongAnPoint.length === 0) {
          this.initGongAn()
        }
      } else {
        this.removeGongAn()
      }
      if (data.indexOf('重点区域') >= 0) {
        if (keyPlacesPoint.length === 0) {
          this.initkeyPlaces()
        }
      } else {
        this.removekeyPlaces()
      }
      if (data.indexOf('天网') >= 0) {
        if (videoPoint.length === 0) {
          this.addVideoPoint()
        }
      } else {
        this.removeVideoPoint()
      }
      if (data.indexOf('常规地点') >= 0) {
        if (shezangmarkers['didian'] === undefined || shezangmarkers['didian'].length === 0) {
          this.addshezangmarkers('didian')
        }
      } else {
        this.removeshezangmarkers('didian')
      }
      if (data.indexOf('封控') >= 0) {
        if (shezangmarkers['fengkong'] === undefined || shezangmarkers['fengkong'].length === 0) {
          this.addshezangmarkers('fengkong')
        }
      } else {
        this.removeshezangmarkers('fengkong')
      }
      if (data.indexOf('应急') >= 0) {
        if (shezangmarkers['beiqing'] === undefined || shezangmarkers['beiqing'].length === 0) {
          this.addshezangmarkers('beiqing')
          this.addshezangmarkers('xianchangzhihui')
          this.addshezangmarkers('xundakuaifan')
          this.addshezangmarkers('huaxikuaifan')
          this.addshezangmarkers('xiaofangzhanche')
        }
      } else {
        this.removeshezangmarkers('beiqing')
        this.removeshezangmarkers('xianchangzhihui')
        this.removeshezangmarkers('xundakuaifan')
        this.removeshezangmarkers('huaxikuaifan')
        this.removeshezangmarkers('xiaofangzhanche')
      }
    },
    addDynamicCircle (pos, id, rad) {
      var longitude = pos[0]
      var latitude = pos[1]
      var rad = rad || 70
      var x = 1
      var y = 1
      viewer.entities.add({
        id: id,
        position: Cesium.Cartesian3.fromDegrees(longitude, latitude),
        ellipse: {
          height: pos[2],
          semiMajorAxis: new Cesium.CallbackProperty(function () {
            y += 1
            if (y > rad) {
              y = 1
            }
            return y
          }, false),
          semiMinorAxis: new Cesium.CallbackProperty(function () {
            x += 1
            if (x > rad) {
              x = 1
            }
            return x
          }, false),
          material: new Cesium.ImageMaterialProperty({
            image: this.header + 'img/circle.png',
            repeat: new Cesium.Cartesian2(1.0, 1.0),
            transparent: true,
            color: new Cesium.CallbackProperty(function () {
              var alp = 1.5 - x / rad
              return Cesium.Color.RED.withAlpha(alp) // entity的颜色透明 并不影响材质，并且 entity也会透明哦
            }, false)
          })
        }
      })
    },
    addPopEvent () {
      var that = this
      function SZpopBig () {
        let container = document.getElementById('SZpopBig')
        if (container) {
          var windowPosition = new Cesium.Cartesian2()
          var canvasHeight = viewer.scene.canvas.height
          var canvasWidth = viewer.scene.canvas.width
          Cesium.SceneTransforms.wgs84ToWindowCoordinates(
            viewer.scene,
            Cesium.Cartesian3.fromDegrees(that.x, that.y, that.z + 100),
            windowPosition
          )
          container.style.bottom = canvasHeight - windowPosition.y + 'px'
          container.style.right =
            canvasWidth - windowPosition.x - container.offsetWidth * 0.5 + 'px'
        }
        let ContentpopWGQ = document.getElementById('popWGQ')
        if (ContentpopWGQ) {
          var windowPosition = new Cesium.Cartesian2()
          var canvasHeight = viewer.scene.canvas.height
          var canvasWidth = viewer.scene.canvas.width
          Cesium.SceneTransforms.wgs84ToWindowCoordinates(
            viewer.scene,
            Cesium.Cartesian3.fromDegrees(that.x, that.y, that.z + 100),
            windowPosition
          )
          ContentpopWGQ.style.bottom = canvasHeight - windowPosition.y + 'px'
          ContentpopWGQ.style.right =
            canvasWidth -
            windowPosition.x -
            ContentpopWGQ.offsetWidth * 0.5 +
            'px'
        }
        let containerpopWGQ = document.getElementById('popXMQ')
        if (containerpopWGQ) {
          var windowPosition = new Cesium.Cartesian2()
          var canvasHeight = viewer.scene.canvas.height
          var canvasWidth = viewer.scene.canvas.width
          Cesium.SceneTransforms.wgs84ToWindowCoordinates(
            viewer.scene,
            Cesium.Cartesian3.fromDegrees(that.x, that.y, that.z + 100),
            windowPosition
          )
          containerpopWGQ.style.bottom = canvasHeight - windowPosition.y + 'px'
          containerpopWGQ.style.right =
            canvasWidth -
            windowPosition.x -
            containerpopWGQ.offsetWidth * 0.5 +
            'px'
        }
        let containerbig = document.getElementById('popBig')
        if (containerbig) {
          var windowPosition = new Cesium.Cartesian2()
          var canvasHeight = viewer.scene.canvas.height
          var canvasWidth = viewer.scene.canvas.width
          Cesium.SceneTransforms.wgs84ToWindowCoordinates(
            viewer.scene,
            Cesium.Cartesian3.fromDegrees(that.x, that.y, that.z + 100),
            windowPosition
          )
          containerbig.style.bottom = canvasHeight - windowPosition.y + 'px'
          containerbig.style.right =
            canvasWidth -
            windowPosition.x -
            containerbig.offsetWidth * 0.5 +
            'px'
        }
      }
      viewer.scene.preRender.addEventListener(SZpopBig)
    },
    ShowRuzhu (address) {
      this.axios
        .get('/leaderview/QZF/getPatrolByAddress?address=' + address)
        .then(res => {
          if (res.success) {
            let tableData = {
              columns: ['巡查人姓名', '巡查人电话', '巡查内容', '巡查时间'],
              rows: []
            }
            res.obj.forEach(dataObj => {
              let newObjData = {
                巡查人姓名: dataObj.patrolName,
                巡查人电话: dataObj.patrolPhone,
                巡查内容: dataObj.patrolConent,
                巡查时间: dataObj.patrolTime
              }
              tableData.rows.push(newObjData)
            })
            let boxData = {
              title: '数据详情',
              data: 'arry',
              dataArray: tableData
            }
            this.$parent.$parent.ShowTableBox(boxData)
          }
        })
    },
    ShowZofang (address) {
      this.axios
        .get('/leaderview/QZF/getRegisterByAddress?address=' + address)
        .then(res => {
          if (res.success) {
            let tableData = {
              columns: ['姓名', '身份证', '电话', '入住日期', '场所ID'],
              rows: []
            }
            res.obj.forEach(dataObj => {
              let newObjData = {
                姓名: dataObj.guestName,
                身份证: dataObj.guestIdentity,
                电话: dataObj.guestPhone,
                入住日期: dataObj.checkInDate,
                场所ID: dataObj.hotelId
              }
              tableData.rows.push(newObjData)
            })
            let boxData = {
              title: '数据详情',
              data: 'arry',
              dataArray: tableData
            }
            this.$parent.$parent.ShowTableBox(boxData)
          }
        })
    },
    addPoints () {
      let height = 100
      this.addPointer(
        Cesium.Cartesian3.fromDegrees(104.05225, 30.644971, height),
        1
      )
      this.addPointer(
        Cesium.Cartesian3.fromDegrees(
          104.04467606235154,
          30.645521833155275,
          height
        ),
        2
      )
      this.addPointer(
        Cesium.Cartesian3.fromDegrees(104.044138, 30.645464, height),
        3
      )
    },
    clearPoint () {
      if (billboardMarkers.length > 0) {
        billboardMarkers.forEach(item => {
          viewer.entities.remove(item)
        })
        billboardMarkers = []
      }
    },
    addPointer (position, id, img, dataArray) {
      billboardMarkers.push(
        viewer.entities.add({
          position,
          id: id,
          dataArray: dataArray,
          billboard: {
            image: img || 'static/img/click.png',
            scale: 0.2
          }
        })
      )
    },
    fly () {
      if (this.nowPageName && this.nowPageName.indexOf('市级') >= 0) {
        viewer.scene.camera.flyTo({
          destination: Cesium.Cartesian3.fromDegrees(
            104.1691971213243,
            30.39283057151572,
            76433.67482117772
          ),
          orientation: {
            heading: 0.07691962850668421,
            pitch: -1.2997082125881327,
            roll: 0.0000065869953988016882
          },
          duration: 1
        })
      } else {
        viewer.scene.camera.flyTo({
          destination: Cesium.Cartesian3.fromDegrees(
            104.18199634654243,
            30.5471951164135,
            55783.84968843796
          ),
          orientation: {
            heading: 6.283185307179586,
            pitch: -1.5707859043726606,
            roll: 0
          },
          duration: 1
        })
      }
    },
    initPostrender () {
      var ContrastBias = `uniform sampler2D colorTexture;
      uniform float smoothWidth;
      uniform float threshold;
      varying vec2 v_textureCoordinates;
      void main(void)
      {
      vec4 sceneColor = texture2D(colorTexture, v_textureCoordinates);
      #ifdef CZM_SELECTED_FEATURE
      if (!czm_selected()) {
      sceneColor = vec4(0.0);
      }
      #endif
      vec3 luma=vec3(0.299,0.587,0.114);
      float v=dot(sceneColor.xyz,luma);
      vec4 outputColor=vec4(0.0,0.0,0.0,1.0);
      float alpha=smoothstep(threshold,threshold+smoothWidth,v);
      gl_FragColor=mix(outputColor,sceneColor,alpha);
      }`

      // 最终合并
      var BloomComposite = `uniform sampler2D colorTexture;
                  uniform sampler2D bloomTexture;
      uniform sampler2D bloomTexture1;
      uniform sampler2D bloomTexture2;
      uniform sampler2D bloomTexture3;
      uniform sampler2D bloomTexture4;
      uniform float bloomFators[5];
      uniform vec4 bloomColor;
                  varying vec2 v_textureCoordinates;
      float lerpBloomFactor(const in float factor){
      float mirror=1.2-factor;
      return mix(factor,mirror,0.0);
      }
                  void main(void)
                  {
                  vec4 color = texture2D(colorTexture, v_textureCoordinates);
                  vec4 bloom = 5.0*(
          lerpBloomFactor(bloomFators[0]) * bloomColor * texture2D(bloomTexture,v_textureCoordinates)+
      lerpBloomFactor(bloomFators[1]) * bloomColor * texture2D(bloomTexture1,v_textureCoordinates)+
      lerpBloomFactor(bloomFators[2]) * bloomColor * texture2D(bloomTexture2,v_textureCoordinates)+
      lerpBloomFactor(bloomFators[3]) * bloomColor * texture2D(bloomTexture3,v_textureCoordinates)+
      lerpBloomFactor(bloomFators[4]) * bloomColor * texture2D(bloomTexture4,v_textureCoordinates)
      );
                  gl_FragColor =color+bloom ;
            }`
      var blur1 = createBlurStage('Blur1', 3, 3, 1 / 2)
      var blur2 = createBlurStage('Blur2', 5, 5, 0.5 / 2)
      var blur3 = createBlurStage('Blur3', 7, 7, 0.25 / 2)
      var blur4 = createBlurStage('Blur4', 9, 9, 0.125 / 2)
      var blur5 = createBlurStage('Blur5', 11, 11, 0.0625 / 2)
      contrastBias = new Cesium.PostProcessStage({
        name: 'contrastBiasUser',
        fragmentShader: ContrastBias,
        uniforms: {
          threshold: 0.0,
          smoothWidth: 0.01
        }
      })
      var bloomComposite = new Cesium.PostProcessStage({
        name: 'bloomCompositeUser',
        fragmentShader: BloomComposite,
        uniforms: {
          bloomTexture: blur1.name,
          bloomTexture1: blur2.name,
          bloomTexture2: blur3.name,
          bloomTexture3: blur4.name,
          bloomTexture4: blur5.name,
          bloomFators: [1.0 / 3, 0.5 / 3, 0.25 / 3, 0.125 / 3, 0.0625 / 3],
          bloomColor: new Cesium.Color(85 / 255, 1, 1, 0.3)
        }
      })
      var blurCompositeStage = new Cesium.PostProcessStageComposite({
        name: 'blurCompositeStage',
        stages: [contrastBias, blur1, blur2, blur3, blur4, blur5],
        inputPreviousStageTexture: true
      })
      var bloomUser = new Cesium.PostProcessStageComposite({
        name: 'bloomUser',
        stages: [blurCompositeStage, bloomComposite],
        inputPreviousStageTexture: false
      })
      viewer.scene.postProcessStages.add(bloomUser)
      contrastBias.selected = []
    },
    addMarker (position, url, scale, name) {
      let en = viewer.entities.add({
        position,
        billboard: {
          image: url,
          scale: scale || 0.3
        },
        name: name
      })
      return en
    },
    showXQBoxTan (nowShowData) {
      this.ShowTableTan = false
      this.nowShowData = nowShowData
    },
    addLabelMarker (lon, lat, url, label, small) {
      let size = small ? 24 : 40
      let height = small ? 50 : 70
      let backgroundColor = small
        ? Cesium.Color.fromCssColorString('#ffffff')
        : Cesium.Color.BLUE
      let fillColor = small
        ? Cesium.Color.fromCssColorString('#000')
        : Cesium.Color.fromCssColorString('#ffffff')
      let en = viewer.entities.add({
        position: Cesium.Cartesian3.fromDegrees(lon, lat, height),
        billboard: {
          image: url,
          scale: 0.15
        },
        label: {
          show: true,
          showBackground: true,
          backgroundColor,
          scale: 0.5,
          font: `normal ${size}px MicroSoft YaHei`,
          text: label,
          fillColor,
          pixelOffset: new Cesium.Cartesian2(10, -30),
          horizontalOrigin: Cesium.HorizontalOrigin.LEFT
        }
      })
      return en
    },
    addDoubleMarker (lon, lat, img, id) {
      let en = viewer.entities.add({
        position: Cesium.Cartesian3.fromDegrees(lon, lat, 70),
        name: id + '管控区',
        billboard: {
          image: img[0],
          scale: 0.15
        }
      })
      let en2 = viewer.entities.add({
        position: Cesium.Cartesian3.fromDegrees(lon, lat, 70),
        name: id + '管控区',
        billboard: {
          image: img[1],
          pixelOffset: new Cesium.Cartesian2(60, -10),
          scale: 0.3
        }
      })
      return [en, en2]
    },
    addWangge () {
      this.removeWangge()
      wanggepositions.forEach(item => {
        item.wangges.forEach(child => {
          if (child.positions.length > 0) {
            let poly = viewer.entities.add({
              polygon: {
                hierarchy: Cesium.Cartesian3.fromDegreesArrayHeights(
                  child.positions
                ),
                perPositionHeight: true,
                material: child.color.withAlpha(0.3)
              },
              name: item.name + '-' + child.name
            })
            let label = viewer.entities.add({
              position: Cesium.Cartesian3.fromDegrees(
                child.center[0],
                child.center[1],
                30
              ),
              label: {
                show: true,
                showBackground: true,
                backgroundColor: Cesium.Color.fromCssColorString('#003153'),
                scale: 0.5,
                font: `normal 38px MicroSoft YaHei`,
                text: child.name,
                fillColor: Cesium.Color.fromCssColorString('#ffffff'),
                pixelOffset: new Cesium.Cartesian2(10, -30),
                horizontalOrigin: Cesium.HorizontalOrigin.LEFT
              },
              name: item.name + '-' + child.name
            })
            wangges.push(poly)
            wangges.push(label)
          }
        })
      })
    },
    removeWangge () {
      if (wangges.length > 0) {
        wangges.forEach(item => {
          viewer.entities.remove(item)
        })
        wangges = []
      }
    },
    addJxJ () {
      this.removeJxJ()
      $.getJSON(this.header + 'geojson/bianjiejxj.json', res => {
        let positions = res.features[0].geometry.coordinates
        let linepositions = []
        positions.forEach(item => {
          linepositions.push(item[0])
          linepositions.push(item[1])
          linepositions.push(10)
        })
        let polyline = viewer.entities.add({
          polyline: {
            positions: Cesium.Cartesian3.fromDegreesArrayHeights(linepositions),
            material: new Cesium.PolylineDashMaterialProperty({
              color: Cesium.Color.DODGERBLUE,
              dashLength: 10,
              dashPattern: 255
            }),
            width: 2
          }
        })
        jxjdatas.push(polyline)
      })
      $.getJSON(this.header + 'geojson/linejxj.json', res => {
        let positions = res.features
        positions.forEach((item, index) => {
          if (item.geometry.type === 'LineString') {
            let linepositions = []
            item.geometry.coordinates.forEach(item => {
              linepositions.push(item[0])
              linepositions.push(item[1])
              linepositions.push(2)
            })
            let en = viewer.entities.add({
              polyline: {
                positions: Cesium.Cartesian3.fromDegreesArrayHeights(
                  linepositions
                ),
                depthFailMaterial: new Cesium.PolylineGlowMaterialProperty({
                  glowPower: 2, // 一个数字属性，指定发光强度，占总线宽的百分比。
                  color: Cesium.Color.GOLD
                }),
                material: new Cesium.PolylineGlowMaterialProperty({
                  glowPower: 2, // 一个数字属性，指定发光强度，占总线宽的百分比。
                  color: Cesium.Color.GOLD
                }),
                width: 1
              }
            })
            jxjdatas.push(en)
          }
        })
      })
      $.getJSON(this.header + 'geojson/xzqhjxj.json', res => {
        let positions = res.features
        positions.forEach((item, index) => {
          let color = new Cesium.Color(30 / 255, 144 / 255, 1, 0.1)
          let linepositions = []
          let pointer = turf.centerOfMass(item.geometry).geometry.coordinates
          let label = this.addMarker(
            Cesium.Cartesian3.fromDegrees(pointer[0], pointer[1], 100),
            `${this.header}img/浆洗街区划/${item.properties.na}.png`,
            0.5
          )
          if (item.geometry.type === 'MultiPolygon') {
            item.geometry.coordinates.forEach(item => {
              item[0].forEach(child => {
                linepositions.push(child[0])
                linepositions.push(child[1])
                linepositions.push(3)
              })
            })
          } else {
            item.geometry.coordinates[0].forEach(item => {
              linepositions.push(item[0])
              linepositions.push(item[1])
              linepositions.push(3)
            })
          }
          let polygon = viewer.entities.add({
            polygon: {
              hierarchy: Cesium.Cartesian3.fromDegreesArrayHeights(
                linepositions
              ),
              perPositionHeight: true,
              material: color,
              extrudedHeight: 1
            },
            name: item.properties.na
          })
          let polyline = viewer.entities.add({
            polyline: {
              positions: Cesium.Cartesian3.fromDegreesArrayHeights(linepositions),
              depthFailMaterial: Cesium.Color.fromCssColorString('#00ef67'),
              material: Cesium.Color.fromCssColorString('#00ef67'),
              width: 3
            },
            name: item.properties.na
          })
          jxjdatas.push(label)
          jxjdatas.push(polygon)
          jxjdatas.push(polyline)
        })
      })
    },
    removeJxJ () {
      if (jxjdatas.length > 0) {
        jxjdatas.forEach(item => {
          viewer.entities.remove(item)
        })
        jxjdatas = []
      }
    },
    addshezangmarkers (type) {
      this.removeshezangmarkers(type)
      shezangmarkers[type] = []
      Imgpositions.markers[type].forEach(item => {
        let positions = gcj02_to_wgs84(item.Lng, item.Lat)
        let en = this.addLabelMarker(
          positions[0],
          positions[1],
          item.img,
          item.name,
          'small'
        )
        shezangmarkers[type].push(en)
      })
    },
    removeshezangmarkers (type) {
      if (shezangmarkers[type] && shezangmarkers[type].length > 0) {
        shezangmarkers[type].forEach(item => {
          viewer.entities.remove(item)
        })
        shezangmarkers[type] = []
      }
    },
    initGongAn () {
      GongAnPoint = []
      Imgpositions.policePoint.forEach(item => {
        let positions = gcj02_to_wgs84(item.Lng, item.Lat)
        let GongAn = this.addLabelMarker(positions[0], positions[1], item.img, item.name)
        GongAnPoint.push(GongAn)
      })
      Imgpositions.police.forEach(item => {
        let positions = []
        let color = item.color.withAlpha(0.5)
        for (var i = 0; i < item.positions.length; i += 2) {
          let position = gcj02_to_wgs84(
            item.positions[i + 1],
            item.positions[i]
          )
          positions.push(position[0])
          positions.push(position[1])
          positions.push(5)
        }
        positions.push(positions[0])
        positions.push(positions[1])
        positions.push(5)
        let point = viewer.entities.add({
          polyline: {
            positions: Cesium.Cartesian3.fromDegreesArrayHeights(positions),
            depthFailMaterial: new Cesium.PolylineGlowMaterialProperty({
              glowPower: 1, // 一个数字属性，指定发光强度，占总线宽的百分比。
              color: Cesium.Color.GOLD
            }),
            material: new Cesium.PolylineGlowMaterialProperty({
              glowPower: 1, // 一个数字属性，指定发光强度，占总线宽的百分比。
              color: Cesium.Color.GOLD
            }),
            width: 1
          }
        })
        let poly = viewer.entities.add({
          polygon: {
            hierarchy: Cesium.Cartesian3.fromDegreesArrayHeights(positions),
            perPositionHeight: true,
            material: color
          }
        })
        GongAnPoint.push(point)
        GongAnPoint.push(poly)
      })
    },
    removeGongAn () {
      GongAnPoint.forEach(item => {
        viewer.entities.remove(item)
      })
      GongAnPoint = []
    },
    initkeyPlaces () {
      keyPlacesPoint = []
      Imgpositions.keyPlacesPoint.forEach(item => {
        let positions = gcj02_to_wgs84(item.Lng, item.Lat)
        let GongAn = this.addLabelMarker(positions[0], positions[1], item.img, item.name)
        keyPlacesPoint.push(GongAn)
      })
      Imgpositions.keyPlaces.forEach(item => {
        let positions = []
        let color = item.color.withAlpha(0.5)
        for (var i = 0; i < item.positions.length; i += 2) {
          let position = gcj02_to_wgs84(
            item.positions[i + 1],
            item.positions[i]
          )
          positions.push(position[0])
          positions.push(position[1])
          positions.push(5)
        }
        positions.push(positions[0])
        positions.push(positions[1])
        positions.push(5)
        let point = viewer.entities.add({
          polyline: {
            positions: Cesium.Cartesian3.fromDegreesArrayHeights(positions),
            depthFailMaterial: new Cesium.PolylineGlowMaterialProperty({
              glowPower: 1, // 一个数字属性，指定发光强度，占总线宽的百分比。
              color: Cesium.Color.GOLD
            }),
            material: new Cesium.PolylineGlowMaterialProperty({
              glowPower: 1, // 一个数字属性，指定发光强度，占总线宽的百分比。
              color: Cesium.Color.GOLD
            }),
            width: 1
          }
        })
        let poly = viewer.entities.add({
          polygon: {
            hierarchy: Cesium.Cartesian3.fromDegreesArrayHeights(positions),
            perPositionHeight: true,
            material: color
          }
        })
        keyPlacesPoint.push(point)
        keyPlacesPoint.push(poly)
      })
    },
    removekeyPlaces () {
      keyPlacesPoint.forEach(item => {
        viewer.entities.remove(item)
      })
      keyPlacesPoint = []
    },
    removeSheZang1 () {
      GuanKongquPoint.forEach(item => {
        viewer.entities.remove(item)
      })
      GuanKongquPoint = []
    },
    initSheZang1 () {
      Imgpositions.pointBase.forEach(item => {
        let positions = gcj02_to_wgs84(item.Lng, item.Lat)
        let GuanKongqu = this.addDoubleMarker(positions[0], positions[1], item.img, item.id)
        GuanKongquPoint.push(GuanKongqu[0], GuanKongqu[1])
      })
      Imgpositions.polygon.forEach(item => {
        let positions = []
        let color = item.color.withAlpha(0.5)
        for (var i = 0; i < item.positions.length; i += 2) {
          let position = gcj02_to_wgs84(
            item.positions[i + 1],
            item.positions[i]
          )
          positions.push(position[0])
          positions.push(position[1])
          positions.push(5)
        }
        positions.push(positions[0])
        positions.push(positions[1])
        positions.push(5)
        let gkPoint = viewer.entities.add({
          polyline: {
            positions: Cesium.Cartesian3.fromDegreesArrayHeights(positions),
            depthFailMaterial: new Cesium.PolylineGlowMaterialProperty({
              glowPower: 1, // 一个数字属性，指定发光强度，占总线宽的百分比。
              color: Cesium.Color.GOLD
            }),
            material: new Cesium.PolylineGlowMaterialProperty({
              glowPower: 1, // 一个数字属性，指定发光强度，占总线宽的百分比。
              color: Cesium.Color.GOLD
            }),
            width: 1
          }
        })
        let poly = viewer.entities.add({
          polygon: {
            hierarchy: Cesium.Cartesian3.fromDegreesArrayHeights(positions),
            perPositionHeight: true,
            material: color
          }
        })
        GuanKongquPoint.push(gkPoint)
        GuanKongquPoint.push(poly)
      })
    },
    initJXJ () {
      if (tileset) {
        tileset.show = false
      }
      if (tilesetJxJ) {
        tilesetJxJ.show = true
      }
      this.addJxJ()
      for (var key in xingzhengquhuaPolygons) {
        if (key !== '浆洗街街道') {
          xingzhengquhuaPolygons[key].forEach(item => {
            item.show = false
          })
        }
      }
    },
    initBase () {
      viewer.entities.removeAll()
      this.backBase()
      if (tileset) {
        tileset.show = true
      }
      if (tilesetJxJ) {
        tilesetJxJ.show = false
      }
      $.getJSON('./static/geojson/xzqh.json', res => {
        let positions = res.features
        positions.forEach((item, index) => {
          let color = Cesium.Color.DODGERBLUE.withAlpha(0.3)
          let extrend = false
          if (this.nowPageName && this.nowPageName.indexOf('未办证住所') >= 0) {
            if (item.properties.Name === '金花桥街道') {
              extrend = true
              color = new Cesium.Color(116 / 255, 151 / 255, 232 / 255, 0.4)
            }
            if (item.properties.Name === '望江路街道') {
              extrend = true
              color = new Cesium.Color(116 / 255, 151 / 255, 232 / 255, 0.4)
            }
          } else {
            if (item.properties.Name === '浆洗街街道') {
              extrend = true
              color = new Cesium.Color(116 / 255, 151 / 255, 232 / 255, 0.4)
            }
          }
          let polygonpositions = []
          let pointer = turf.centerOfMass(item.geometry).geometry.coordinates
          if (item.properties.Name === '金花桥街道') {
            pointer = [103.97374548683935, 30.591885280709842]
          }
          let marker = this.addMarker(
            Cesium.Cartesian3.fromDegrees(pointer[0], pointer[1], 100),
            `./static/img/街道名称/${item.properties.Name}.png`, 0.3, item.properties.Name
          )
          if (item.geometry.type === 'MultiPolygon') {
            item.geometry.coordinates.forEach(item => {
              item[0].forEach(child => {
                polygonpositions.push(child[0])
                polygonpositions.push(child[1])
                polygonpositions.push(3)
              })
            })
          } else {
            item.geometry.coordinates[0].forEach(item => {
              polygonpositions.push(item[0])
              polygonpositions.push(item[1])
              polygonpositions.push(3)
            })
          }
          let en = viewer.entities.add({
            polygon: {
              hierarchy: Cesium.Cartesian3.fromDegreesArrayHeights(
                polygonpositions
              ),
              perPositionHeight: true,
              material: color,
              extrudedHeight: extrend ? 30 : 1
            },
            name: item.properties.Name
          })
          xingzhengquhuaPolygons[item.properties.Name].push(en)
          xingzhengquhuaPolygons[item.properties.Name].push(marker)
        })
      })
      $.getJSON(this.header + 'geojson/xzqhLine.json', res => {
        let features = res.features
        features.forEach((item, index) => {
          let positions = item.geometry.coordinates
          let linepositions = []
          positions.forEach(item => {
            linepositions.push(item[0])
            linepositions.push(item[1])
            linepositions.push(8)
          })
          let en = viewer.entities.add({
            polyline: {
              positions: Cesium.Cartesian3.fromDegreesArrayHeights(
                linepositions
              ),
              material: Cesium.Color.AQUAMARINE,
              depthFailMaterial: Cesium.Color.AQUAMARINE,
              width: 2
            }
          })
          xingzhengquhuaPolygons[item.properties.Name].push(en)
        })
      })
    },
    initModels () {
      tileset = new Cesium.Cesium3DTileset({
        url: this.header + 'wuhou/tileset.json',
        lightColor: new Cesium.Cartesian3(20, 20, 20),
        showOutline: false
      })
      tilesetJxJ = new Cesium.Cesium3DTileset({
        url: this.header + 'JXJ/tileset.json',
        lightColor: new Cesium.Cartesian3(20, 20, 20),
        showOutline: false
      })
      viewer.scene.primitives.add(tileset)
      viewer.scene.primitives.add(tilesetJxJ)
      tilesetJxJ.readyPromise.then(function (tileset3D) {
        tilesetJxJ.style = new Cesium.Cesium3DTileStyle({
          color: {
            conditions: [
              ['true', 'rgba(0, 205, 243 ,1)'] // 'rgb(127, 59, 8)']
            ]
          }
        })
        var cartographic = Cesium.Cartographic.fromCartesian(tilesetJxJ.boundingSphere.center)
        var surface = Cesium.Cartesian3.fromRadians(cartographic.longitude, cartographic.latitude, cartographic.height)
        var offset = Cesium.Cartesian3.fromRadians(cartographic.longitude, cartographic.latitude, 0)
        var translation = Cesium.Cartesian3.subtract(offset, surface, new Cesium.Cartesian3())
        tilesetJxJ.modelMatrix = Cesium.Matrix4.fromTranslation(translation)
      })
      tileset.readyPromise.then(function (tileset3D) {
        tileset.style = new Cesium.Cesium3DTileStyle({
          color: {
            conditions: [
              ['true', 'rgba(0, 205, 243 ,1)'] // 'rgb(127, 59, 8)']
            ]
          }
        })
        var cartographic = Cesium.Cartographic.fromCartesian(tileset.boundingSphere.center)
        var surface = Cesium.Cartesian3.fromRadians(cartographic.longitude, cartographic.latitude, cartographic.height)
        var offset = Cesium.Cartesian3.fromRadians(cartographic.longitude, cartographic.latitude, 0)
        var translation = Cesium.Cartesian3.subtract(offset, surface, new Cesium.Cartesian3())
        tileset.modelMatrix = Cesium.Matrix4.fromTranslation(translation)
      })
    },
    getCamera () {
      var camera = viewer.camera
      var cartographic = camera.positionCartographic
      var longitude = window.Cesium.Math.toDegrees(cartographic.longitude)
      var latitude = window.Cesium.Math.toDegrees(cartographic.latitude)
      var height = cartographic.height
      var heading = camera.heading
      var pitch = camera.pitch
      var roll = camera.roll
      var obj = {
        longitude: longitude,
        latitude: latitude,
        height: height,
        heading: heading,
        pitch: pitch,
        roll: roll
      }
      let data = `
      destination: Cesium.Cartesian3.fromDegrees(
          ${obj.longitude},
          ${obj.latitude},
          ${obj.height}
        ),
        orientation: {
          heading: ${obj.heading},
          pitch: ${obj.pitch},
          roll: ${obj.roll}
        },
      `
      console.log(data)
      console.log(obj, viewer.scene.primitives)
    },
    addPontXMQ () {
      viewer.entities.add({
        position: Cesium.Cartesian3.fromDegrees(104.04696719736683, 30.644423523687326, 40),
        name: 'XMQ',
        billboard: {
          image: 'static/img/click.png',
          scale: 0.4
        }
      })
      this.addDynamicCircle([104.04696719736683, 30.644423523687326, 40], 'test', 100)
    },
    addVideoPoint () {
      this.axios.get(`/leaderview/WuHou/getHcnetPoints`).then(data => {
        if (data.success) {
          videoPoint = []
          data.obj.forEach(item => {
            let VidoePont = viewer.entities.add({
              position: Cesium.Cartesian3.fromDegrees(
                item.longitude,
                item.latitude,
                30
              ),
              billboard: {
                image: this.header + 'img/camera.png',
                scale: 0.2
                // distanceDisplayCondition: new Cesium.DistanceDisplayCondition(0.0, 6200.0)
              },
              label: {
                show: true,
                showBackground: true,
                backgroundColor: Cesium.Color.fromCssColorString('#000'),
                scale: 0.4,
                font: 'normal 36px MicroSoft YaHei',
                text: item.name.split(')')[1] || item.name,
                pixelOffset: new Cesium.Cartesian2(10, -30),
                horizontalOrigin: Cesium.HorizontalOrigin.LEFT
                // distanceDisplayCondition: new Cesium.DistanceDisplayCondition(0.0, 6200.0)
              },
              cameraId: item.deviceIndexCode,
              name: item.name,
              type: 'camera'
            })
            videoPoint.push(VidoePont)
          })
        }
      })
    },
    removeVideoPoint () {
      videoPoint.forEach(item => {
        viewer.entities.remove(item)
      })
      videoPoint = []
    },
    // 取消行政区划隐藏
    backBase () {
      for (var key in xingzhengquhuaPolygons) {
        xingzhengquhuaPolygons[key].forEach(item => {
          item.show = true
        })
      }
    },
    init3D () {
      viewer = new Cesium.Viewer('cesiumContainer', {
        animation: false,
        baseLayerPicker: false,
        fullscreenButton: false,
        geocoder: false,
        homeButton: false,
        infoBox: false,
        sceneModePicker: false,
        orderIndependentTranslucency: true,
        selectionIndicator: false,
        timeline: false, // 时间轴部件
        scene3DOnly: true, // 当设置为true时，每个几何图形实例将仅以3D形式呈现，以节省GPU内存
        navigationHelpButton: false,
        vrButton: false, // vr部件
        shouldAnimate: true,
        shadows: false,
        imageryProvider: new Cesium.SingleTileImageryProvider({
          url: './static/Cesium/back.png'
        })
      })
      viewer.scene.skyAtmosphere.show = false
      viewer.scene.globe.enableLighting = false
      viewer.scene.globe.baseColor = Cesium.Color.BLACK
      viewer.cesiumWidget.creditContainer.style.display = 'none' // 去水印
      viewer.scene.globe.depthTestAgainstTerrain = true
      viewer.scene.fxaa = true
      viewer.scene.postProcessStages.fxaa.enabled = true
      // 关闭鼠标双击事件
      viewer.cesiumWidget.screenSpaceEventHandler.removeInputAction(
        Cesium.ScreenSpaceEventType.LEFT_DOUBLE_CLICK
      )
      // 倾斜视图 鼠标左键旋转
      viewer.scene.screenSpaceCameraController.tiltEventTypes = [
        Cesium.CameraEventType.LEFT_DRAG
      ]
      // 缩放设置 重新设置缩放成员
      viewer.scene.screenSpaceCameraController.zoomEventTypes = [
        Cesium.CameraEventType.MIDDLE_DRAG,
        Cesium.CameraEventType.WHEEL,
        Cesium.CameraEventType.PINCH
      ]
      // 平移 添加鼠标右键  鼠标右键平移
      viewer.scene.screenSpaceCameraController.rotateEventTypes = [
        Cesium.CameraEventType.RIGHT_DRAG
      ]
      viewer.scene.screenSpaceCameraController.enableTilt = false
      var handler = new Cesium.ScreenSpaceEventHandler(viewer.canvas)
      // todo:在椭球下点击创建点
      handler.setInputAction(e => {
        var mousePosition = e.position
        var picked = viewer.scene.pick(mousePosition)
        var position = viewer.scene.pickPosition(mousePosition)
        var lat = Cesium.Math.toDegrees(
          Cesium.Cartographic.fromCartesian(position).latitude
        )
        var lng = Cesium.Math.toDegrees(
          Cesium.Cartographic.fromCartesian(position).longitude
        )
        this.x = lng
        this.y = lat
        this.z = Cesium.Cartographic.fromCartesian(position).height
        console.log(
          lng +
            ',' +
            lat +
            ',' +
            Cesium.Cartographic.fromCartesian(position).height +
            3 +
            ','
        )
        this.popshow = false
        this.popshowBig = false
        this.popshowWGQ = false
        this.popshowXMQ = false
        this.SZDataShowBig = false

        this.iswbzzs = false
        if (picked && picked.id && picked.id.name && picked.id.name.indexOf('网格区') > 0) {
          this.popshowWGQ = true
          this.WGQData = {
            网格区: picked.id.name,
            网格员姓名: '李四',
            性别: '男',
            电话: '18484848844',
            职务: 'xxx',
            对应街道负责人姓名: 'xxxxxxxx',
            对应街道负责人电话: 'xxxxxxxx'
          }
        }
        if (picked && picked.primitive && picked.id && picked.id._billboard) {
          if (picked.id && picked.id._billboard) {
            if (picked.id.name && picked.id.name.includes('街道')) {
              this.backBase()
              for (var key in xingzhengquhuaPolygons) {
                if (key !== picked.id.name) {
                  xingzhengquhuaPolygons[key].forEach(item => {
                    item.show = false
                  })
                }
              }
              let cart3 = picked.id.position._value
              var flyY = Cesium.Math.toDegrees(
                Cesium.Cartographic.fromCartesian(cart3).latitude
              )
              var flyX = Cesium.Math.toDegrees(
                Cesium.Cartographic.fromCartesian(cart3).longitude
              )
              var flyZ = Cesium.Cartographic.fromCartesian(cart3).height
              viewer.scene.camera.flyTo({
                destination: Cesium.Cartesian3.fromDegrees(
                  flyX,
                  flyY,
                  flyZ + 5000
                ),
                duration: 1
              })
            }

            if (picked.id.name === 'XMQ') {
              this.popshowXMQ = true
            } else if (picked.id.type === 'camera') {
              let cameraData = picked.id.cameraId
              this.$parent.$parent.ShowVideoBox(cameraData)
            } else if (picked.id.name && picked.id.name.indexOf('管控区') >= 0) {
              this.SZDataShowBig = true
              this.CheckEdId = picked.id.name.split('管控区')[0] * 1
              this.SZData = this.AllData[this.CheckEdId - 1]
            } else if (picked.id.id && picked.id.id.indexOf('wbzzs') >= 0) {
              this.popshowBig = true
              this.ShowTableTan = true
              this.iswbzzs = true
              this.CheckEdId = picked.id.id
              this.TableTanData = {
                columns: ['address', 'street', 'room_number', 'bed_number'],
                rows: picked.id.dataArray.rows
              }
            } else if (picked.id.dataArray.rows) {
              this.popshowBig = true
              this.ShowTableTan = true
              this.CheckEdId = picked.id.id
              this.TableTanData = {
                columns: ['placeName', 'address', 'roomNum', 'bedNum'],
                rows: picked.id.dataArray.rows
              }
            }
          }
          //   let primitive = picked.primitive
          //   let pickIds = primitive._pickIds
          //   let pickId = picked.pickId
          //   if (picked.id) {
          //     pickId = primitive.pickId
          //   }
          //   if (!pickId && !pickIds && picked.content) {
          //     pickIds = picked.content._model._pickIds
          //   }
          //   if (!pickId) {
          //     if (picked.id) {
          //       pickId = pickIds.find(pickId => {
          //         return pickId.object === picked
          //       })
          //     } else if (pickIds) {
          //       pickId = pickIds[0]
          //     }
          //   }
          //   if (pickId) {
          //     let pickObject = {
          //       pickId: pickId
          //     }
          //     contrastBias.selected.push(pickObject)
          //   }
        }
      }, Cesium.ScreenSpaceEventType.LEFT_CLICK)
      handler.setInputAction(jieliu, Cesium.ScreenSpaceEventType.MOUSE_MOVE)
      var canRun = true
      // 节流函数 0.3S触发一次hover
      function jieliu (e) {
        if (!canRun) {
          return
        }
        canRun = false
        setTimeout(() => {
          canRun = true
          var mousePosition = e.endPosition
          var picked = viewer.scene.pick(mousePosition)
          if (picked && picked.id && picked.id._polygon) {
            if (picked.id.name === '浆洗街街道') {
              return
            } else if (picked.id.name === '边界') {
              if (highLightPolygon && hightLightMat) {
                highLightPolygon.material = hightLightMat
                highLightPolygon = null
                hightLightMat = null
              }
              return
            }
            if (highLightPolygon) {
              if (highLightPolygon === picked.id._polygon) {
                return
              }
              highLightPolygon.material = hightLightMat
              highLightPolygon = null
              hightLightMat = null
            } else {
              hightLightMat = picked.id._polygon.material
              highLightPolygon = picked.id._polygon
              highLightPolygon.material = Cesium.Color.AQUA.withAlpha(0.2)
            }
          } else {
            if (highLightPolygon) {
              highLightPolygon.material = hightLightMat
              highLightPolygon = null
              hightLightMat = null
            }
          }
        }, 300)
      }
    }
  }
}
</script>

<style scoped>
.content {
  width: 100%;
  height: 100%;
  overflow: hidden;
  position: relative;
}
.XQBoxTan{
  height: 100%;
}
.content #cesiumContainer {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  padding: 0px;
  margin: 0px;
}
.content #popWGQ {
  width: 850px;
  height: 429px;
  background: url(./tipBig.png);
  background-size: 100% 100%;
  color: rgb(255, 255, 255);
  position: relative;
  padding: 70px 0px 0px;
  position: absolute;
  z-index: 10;
  font-size: 18px;
}
.content #popWGQ .poptitle {
  position: absolute;
  top: 70px;
  left: 50px;
  font-size: 46px !important;
  color: #bbeefe;
  font-family: PangmenMainRoadTitleBody !important;
  font-weight: 400;
}
.content #popWGQ .CloseBtn {
  position: absolute;
  cursor: pointer;
  top: 5px;
  right: 0px;
  height: 50px;
  width: 50px;
}
.content #popWGQ .lineContain {
  padding: 20px 60px;
  top: 70px;
  height: 70%;
  position: relative;
  overflow: auto;
}
.content #popWGQ .lineContain .line {
  margin-bottom: 15px;
}
.content #popWGQ .lineContain button {
  color: #fff;
  background: #1890ff;
  border-color: #1890ff;
  text-shadow: 0 -1px 0 rgb(0 0 0 / 12%);
  box-shadow: 0 2px 0 rgb(0 0 0 / 5%);
}
.content #popXMQ {
  width: 650px;
  height: 329px;
  background: url(./tipBig.png);
  background-size: 100% 100%;
  color: rgb(255, 255, 255);
  position: relative;
  padding: 40px 0px 0px;
  position: absolute;
  z-index: 10;
  font-size: 18px;
}
.content #popXMQ .poptitle {
  position: absolute;
  top: 70px;
  left: 50px;
  font-size: 46px !important;
  color: #bbeefe;
  font-family: PangmenMainRoadTitleBody !important;
  font-weight: 400;
}
.content #popXMQ .CloseBtn {
  position: absolute;
  cursor: pointer;
  top: 13px;
  right: 0px;
  height: 50px;
  width: 50px;
}
.content #popXMQ .lineContain {
  padding: 40px 60px;
  top: 70px;
  height: 70%;
  position: relative;
  overflow: auto;
}
.content #popXMQ .lineContain .line {
  margin-bottom: 10px;
}
.content #popXMQ .lineContain button {
  color: #fff;
  background: #1890ff;
  border-color: #1890ff;
  text-shadow: 0 -1px 0 rgb(0 0 0 / 12%);
  box-shadow: 0 2px 0 rgb(0 0 0 / 5%);
}
.content #SZpopBig {
  width: 850px;
  height: 429px;
  background: url(./tipBig.png);
  background-size: 100% 100%;
  color: rgb(255, 255, 255);
  position: relative;
  padding: 70px 0px 0px;
  position: absolute;
  z-index: 10;
  font-size: 18px;
}
.content #SZpopBig .poptitle {
  position: absolute;
  top: 70px;
  left: 50px;
  font-size: 46px !important;
  color: #bbeefe;
  font-family: PangmenMainRoadTitleBody !important;
  font-weight: 400;
}
.content #SZpopBig .CloseBtn {
  position: absolute;
  cursor: pointer;
  top: 13px;
  right: 0px;
  height: 50px;
  width: 50px;
}
.content #SZpopBig .lineContain {
  padding: 10px 40px;
  margin: 0 20px;
  top: 70px;
  height: 70%;
  position: relative;
  overflow: auto;
}
.content #SZpopBig .lineContain .line {
  margin-bottom: 5px;
}
.content #SZpopBig .lineContain button {
  color: #fff;
  background: #1890ff;
  border-color: #1890ff;
  text-shadow: 0 -1px 0 rgb(0 0 0 / 12%);
  box-shadow: 0 2px 0 rgb(0 0 0 / 5%);
}
.content #popBig {
  width: 850px;
  height: 429px;
  background: url(./tipBig.png);
  background-size: 100% 100%;
  color: rgb(255, 255, 255);
  position: relative;
  padding: 70px 0px 0px;
  position: absolute;
  z-index: 10;
  font-size: 18px;
}
.content #popBig .poptitle {
  position: absolute;
  top: 70px;
  left: 50px;
  font-size: 46px !important;
  color: #bbeefe;
  font-family: PangmenMainRoadTitleBody !important;
  font-weight: 400;
}
.content #popBig .CloseBtn {
  position: absolute;
  cursor: pointer;
  top: 13px;
  right: 0px;
  height: 50px;
  width: 50px;
}
.content #popBig .BackBtn {
  cursor: pointer;
  bottom: 0px;
  right: 20px;
  position: absolute;
  height: 50px;
  width: 50px;
}
.content #popBig .lineContain {
  padding: 10px 40px;
  margin: 0 20px;
  top: 70px;
  height: 70%;
  position: relative;
  overflow: auto;
}
.content #popBig .lineContain .line {
  margin-bottom: 5px;
}
.content #popBig .lineContain button {
  color: #fff;
  background: #1890ff;
  border-color: #1890ff;
  text-shadow: 0 -1px 0 rgb(0 0 0 / 12%);
  box-shadow: 0 2px 0 rgb(0 0 0 / 5%);
}
.TbaleTan {
  height: 100%;
  width: 100%;
  padding: 20px;
}
.TableHead {
  width: 100%;
}
.TableHead tr {
  width: 100%;
  height: 40px;
  font-size: 20px !important;
  display: flex;
  color: #86b7dd;
}
.TableHead tr th {
  height: 100%;
  text-align: center;
}
.TableBody {
  width: 100%;
  height: 260px;
  overflow: auto;
}
.TableBody tr {
  width: 100%;
  height: 60px;
  margin: 10px 0;
  font-size: 20px !important;
  display: flex;
  color: #bfcbdb;
}
.TableBody tr th {
  height: 100%;
  text-align: center;
  overflow: hidden;
}
</style>
