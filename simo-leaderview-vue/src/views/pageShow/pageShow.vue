<template>
  <div class="content">
    <button v-show="false" @click="getCamera('didian')" style="position:absolute;z-index:9999;width:100px;height:80px;top:400px;left:200px;">获取视角</button>
    <button v-show="false" @click="removeshezangmarkers('didian')" style="position:absolute;z-index:9999;width:100px;height:80px;top:500px;left:200px;">获取视角1</button>
    <button v-show="false" @click="initJXJ" style="position:absolute;z-index:9999;width:100px;height:80px;top:600px;left:200px;">浆洗街</button>
    <button v-show="false" @click="addSheQuWangge" style="position:absolute;z-index:9999;width:100px;height:80px;top:700px;left:200px;">网格员</button>
    <button v-show="false" @click="removeSheQuWangge" style="position:absolute;z-index:9999;width:100px;height:80px;top:800px;left:200px;">武侯大屏</button>
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
    <div id="SZCTpopBig" v-show="SZCTDataXQ">
      <div class="XQBoxTan">
        <div class="poptitle">{{SZGKName?SZGKName:'点位详情'}}</div>
        <div class="CloseBtn" @click="SZCTDataXQ = false"></div>
        <div class="lineContain">
          <div class="line" v-for="(item,index) in SZCTDataArray" v-show="SZCTDataNameArray.hasOwnProperty(item.name)" :key="index">
            {{ SZCTDataNameArray[item.name]}}
              <div style="padding: 0px 10px;display: inline-block;">:</div>
            {{item.value !== null?item.value : '暂无数据'}}
          </div>
        </div>
      </div>
    </div>
    <div id="popWGQ" v-show="popshowWGQ">
      <div class="XQBoxTan">
        <div class="poptitle">{{WGQData.sssq}}-第{{WGQData.sswg}}区</div>
        <div class="CloseBtn" @click="popshowWGQ = false"></div>
        <div class="lineContain">
          <div class="line">网格员姓名: {{WGQData.name || '暂无数据'}}</div>
          <!-- {{WGQData['网格员姓名']}} -->
          <div class="line">性别: {{WGQData.xb || '暂无数据'}}</div>
          <div class="line">电话: {{WGQData.lxdh || '暂无数据'}}</div>
          <div class="line">身份证信息: {{WGQData.sfzh || '暂无数据'}}</div>
          <!-- <div class="line">职务: {{WGQData['职务']}}</div> -->
          <!-- <div class="line">对应社区民警姓名: 罗恩祥</div>
          <div class="line">对应社区民警电话: 13281004566</div> -->
        </div>
      </div>
    </div>
    <div id="popXMQ" v-show="popshowXMQ">
      <div class="XQBoxTan" v-if="IsXiuGaiState">
        <div class="poptitle">
          <Input v-model="sureChangeData.name" placeholder="请输入标题" />
          </div>
        <div class="CloseBtn" @click="popshowXMQ = false"></div>
        <div class="lineContain">
          <div class="line">
            <Input v-model="sureChangeData.details" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="请输入内容" />
          </div>
        </div>
        <div class="btnArry">
          <button @click="saveXiugai">保存</button>
          <button @click="giveUpXiuGai">取消</button>
        </div>
      </div>
      <div class="XQBoxTan" v-else>
        <div class="poptitle">{{changeDataArry.name}}</div>
        <div class="CloseBtn" @click="popshowXMQ = false"></div>
        <div class="lineContain">
          <div class="line"> {{changeDataArry.details}}</div>
        </div>
        <div class="btnArry">
          <button @click="suerChange">修改</button>
          <button @click="sureDelet=true">删除</button>
        </div>
      </div>
    </div>
    <div id="popBig" v-show="popshowBig">
      <div class="TbaleTan" v-if="ShowTableTan">
        <div class="CloseBtn" @click="popshowBig = false"></div>
        <div v-if="ifWangGe" class="lineContain" style="height:80%; top:40px">
          <div>
            <div style="font-size: 30px !important;color: #bbeefe;font-family: PangmenMainRoadTitleBody !important;">网格信息</div>
            <div style="display:flex;flex-wrap:wrap;margin-bottom:10px">
              <div  style="width:50%;">网格员姓名: {{currentWangGe.wg_name || '暂无数据'}}</div>
              <div style="width:50%;">电话: {{currentWangGe.wg_phone || '暂无数据'}}</div>
            </div>
            <div style="height:1px;border:1px solid white;margin:5px 0 5px 0;"></div>
            <div style="font-size: 30px !important;color: #bbeefe;font-family: PangmenMainRoadTitleBody !important;">民警信息</div>
          </div>
            <div class="TableHead">
            <tr>
              <th
                v-for="(data, index) in TableTanData.columns"
                :key="index"
                :style="{width:`calc(${100 / TableTanData.columns.length}%)`}"
              >{{ tableTanTitle[data] || data }}</th>
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
        <div v-else>
            <div class="TableHead">
            <tr>
              <th
                v-for="(data, index) in TableTanData.columns"
                :key="index"
                :style="{width:`calc(${100 / TableTanData.columns.length}%)`}"
              >{{ tableTanTitle[data] || data }}</th>
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
      </div>
      <div class="XQBoxTan" v-else>
        <div class="poptitle">{{ !sqjcfb ? iswbzzs? nowShowData.placeName:'小旅馆' :'警员详情'}}</div>
        <div class="CloseBtn" @click="popshowBig = false"></div>
        <div class="lineContain" v-if='sqjcfb'>
          <div class="line">民警:{{nowShowData['民警']}}</div>
          <div class="line">民警Id:{{nowShowData['民警Id']}}</div>
          <div class="line">手机:{{nowShowData['手机']}}</div>
          <div class="line">区域Id: {{nowShowData['区域Id']}}</div>
          <div class="line">社区:{{nowShowData['社区']}}</div>
          <div class="line">管控区:{{nowShowData['管控区']}}</div>
          <div class="line">责任区:{{nowShowData['责任区']}}</div>
        </div>
        <div class="lineContain" v-else-if='!iswbzzs'>
          <div class="line">名称: {{nowShowData.placeName}}</div>
          <div class="line">标准地址:{{nowShowData.address}}</div>
          <div class="line">房间数:{{nowShowData.roomNum}}</div>
          <div class="line">床铺数:{{nowShowData.bedNum}}</div>
          <div
            class="line"
          >社区民警(电话):{{nowShowData.communityPolice}}:{{nowShowData.communityPolicePhone}}</div>
          <div class="line">网格员(电话):{{nowShowData.gridMember}}:{{nowShowData.gridMemberPhone}}</div>
          <div class="line">微消站(电话):{{nowShowData.fireStation}}:{{nowShowData.fireStationPhone}}</div>
          <button @click="WBZShowRuzhu(nowShowData.id)">入住记录</button>
          <button @click="WBZShowZofang(nowShowData.id)">走访记录</button>
        </div>
        <div class="lineContain" v-else>
          <div class="line">名称: {{nowShowData.placeName || '无'}}</div>
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
          <button @click="WBZShowRuzhu(nowShowData.id)">入住记录</button>
          <button @click="WBZShowZofang(nowShowData.id)">走访记录</button>
        </div>
        <div class="BackBtn" @click="ShowTableTan = true">返回</div>
      </div>
    </div>
    <Modal
      v-model="sureDelet"
      title="请确认"
      @on-ok="deletZDDW"
      @on-cancel="sureDelet = false">
      <p>确认删除该点位?删除后不可恢复！</p>
    </Modal>
    <Modal
      v-model="sureAddPoint"
      title="请输入点位信息"
      @on-ok="AddZDDW"
      @on-cancel="sureAddPoint = false">
      <p> 标题: <Input v-model="AddDataArry.name" placeholder="请输入标题" /> </p>
      <p> 内容: <Input v-model="AddDataArry.details" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="请输入内容" /> </p>
    </Modal>
    <div id="cesiumContainer" :style="CesiumStyle"/>
  </div>
</template>

<script>
import BaiduImageryProvider from './baiduTransForm/BaiduImageryProvider.js'
import createBlurStage from './CesiumEdgeStage/createBlurStage.js'
import * as turf from '@turf/turf'
import Imgpositions from './Imgpositions.js'
import wanggepositions from './wanggepositions.js'
import shequwanggepositions from './shequwanggepositions.js'
import { gcj02_to_wgs84 } from './transform.js'
import { Slider, Notification } from 'element-ui'
import element from '@/element/index.js'
var viewer
var tileset
var tilesetJxJ
var contrastBias
var baseObject
var highLightPolygon
var hightLightMat
var billboardMarkers = []
let shequwangges = []// 社区网格
let wangges = []// 网格数据
let jxjdatas = []// 浆洗街行政区划数据
let videoPoint = []// 摄像头数据
let ZdDWarray = []// 摄像头数据
let GongAnPoint = []// 公安数据
let keyPlacesPoint = []// 重点数据
let GuanKongquPoint = []// 管控区
let SZGKPoint = [
  [],
  [],
  [],
  [],
  [],
  [],
  [],
  []
]// 管控区
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
  '玉林街道': [],
  '武侯': []
}// 记录行政区划面
export default {
  name: 'pageShow',
  props: ['nowPageName'],
  data () {
    return {
      popshow: false,
      isYL: false,
      isChongHe: false,
      popshowBig: false,
      AddPointState: false,
      IsXiuGaiState: false,
      sureDelet: false,
      sureAddPoint: false,
      changeDataArry: {},
      sureChangeData: {},
      SZCTDataArray: [],
      SZGKName: '',
      AddDataArry: {
        longitude: '',
        latitude: '',
        name: '',
        details: ''
      },
      popshowWGQ: false,
      popshowXMQ: false,
      iswbzzs: false,
      sqjcfb: false,
      SZDataShowBig: false,
      SZCTDataXQ: false,
      ShowTableTan: true,
      CheckEdId: 0,
      ifWangGe: false,
      currentWangGe: {},
      x: 0,
      y: 0,
      z: 0,
      header: process.env.NODE_ENV === 'development' ? './static/' : './',
      TableTanData: { columns: [], rows: [] },
      tableTanTitle: {
        placeName: '名称',
        address: '场所地址',
        roomNum: '房间数',
        bedNum: '床铺数',
        room_name: '名称',
        street: '所属街道',
        room_number: '房间数',
        bed_number: '床铺数',
        ylmc: '院落名称',
        ylxz: '院落性质',
        csmc: '场所名称',
        type: '场所类别',
        community: '所属社区'
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
      SZCTDataNameArray: {
        'type': '场所类别',
        'name': '场所名称',
        'license': '有无证照',
        'address': '场所地址',
        'longitude': '经度',
        'latitude': '纬度',
        'area': '面积',
        'manage_time': '经营时间',
        'manager_name': '负责人姓名',
        'manager_phone': '负责人联系方式',
        'passenger_flow': '客流量',
        'member_number': '从业人员数',
        'event': '有无案件相关事件',
        'community': '所属社区',
        'company_name': '单位名称',
        'company_address': '单位地址',
        'job_title': '职务',
        'company_manager_phone': '联系电话',
        'office_manager': '联系人',
        'office_manager_phone': '联系电话',
        'owner_name': '业主姓名',
        'owner_phone': '业主联系方式',
        'room_number': '房间数',
        'bed_number': '床铺数',
        'remark': '备注',
        'household_population': '涉藏户籍人数',
        'rental_population': '涉藏租住人口数',
        'street': '所属街道',
        'person_number': '人数',
        'time': '活动起止时间',
        'mjxm': '民警姓名',
        'company_manager': '单位负责人',
        'sqwg': '社区网格', // 社区网格
        'xjnd': '修建年代', // 修建年代
        'ylmc': '院落名称', // 院落名称
        'ylxz': '院落性质', // 院落性质
        'wgyxm': '网格员姓名', // 网格员姓名
        'yldys': '院落单元数', // 院落单元数
        'ylfws': '院落房屋数', // 院落房屋数
        'yllds': '院落楼栋数', // 院落楼栋数
        'mjlxdh': '民警联系电话', // 民警联系电话
        'wygsmc': '物业公司名称', // 物业公司名称
        'ylglms': '院落管理模式', // 院落管理模式
        'szhjrks': '涉藏户籍人口数', // 涉藏户籍人口数
        'szzzrks': '涉藏租住人口数', // 涉藏租住人口数
        'wgylxdh': '网格员联系电话', // 网格员联系电话
        'sfszzdyl': '是否涉藏重点院落', // 是否涉藏重点院落
        'zhzfdyxm': '综治、执法队员姓名', // 综治、执法队员姓名
        'wygslxrdh': '物业公司联系人电话', // 物业公司联系人电话
        'wygslxrxm': '物业公司联系人姓名', // 物业公司联系人姓名
        'zhzfdylxdh': '综治、执法队员联系电话' // 综治、执法队员联系电话
      },
      nowShowData: [],
      newSZCheckEdData: [],
      SZData: {},
      WGQData: {},
      AllData: [
        {
          网格区: '第一管控区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        },
        {
          网格区: '第二管控区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        },
        {
          网格区: '第三管控区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        },
        {
          网格区: '第四管控区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        },
        {
          网格区: '第五管控区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        },
        {
          网格区: '第六管控区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        },
        {
          网格区: '第七管控区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        },
        {
          网格区: '第八管控区',
          网格员姓名: '李四',
          性别: '男',
          电话: '18484848844',
          职务: 'xxx',
          对应街道负责人姓名: 'xxxxxxxx',
          对应街道负责人电话: 'xxxxxxxx'
        },
        {
          网格区: '第九管控区',
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
  computed: {
    CesiumStyle () {
      if (this.AddPointState) {
        return {
          cursor: 'crosshair'
        }
      } else {
        return {
          cursor: 'auto'
        }
      }
    }
  },
  watch: {
    nowPageName: function () {
      this.clearPoint()
      if (this.nowPageName && this.nowPageName.indexOf('涉藏概况') >= 0) {
        this.initJXJ()
        this.flyJXJ()
        if (window.changeCheckedArry) {
          window.changeCheckedArry(this.newSZCheckEdData)
        }
      } else if (
        this.nowPageName &&
        this.nowPageName.indexOf('应急处突') >= 0
      ) {
        this.initJXJ()
        if (window.changeCheckedArry) {
          window.changeCheckedArry(this.newSZCheckEdData)
        }
      } else {
        this.initBase()
        this.fly()
      }
      if (this.nowPageName && this.nowPageName.indexOf('群租房') >= 0) {
        this.axios.get(`/leaderview/WuHou/getOrgaDot`).then(data => {
          if (data.success) {
            let height = 100
            data.obj.dataArray.forEach((d, index) => {
              d.items.forEach((ele, ind) => {
                this.addPointer(
                  Cesium.Cartesian3.fromDegrees(
                    ele.longitude * 1,
                    ele.latitude * 1,
                    height
                  ),
                  'xiaoqu' + d.street + ind,
                  this.header + 'img/xiaoqu.png',
                  { columns: [], rows: ele.items[0].items }
                )
              })
            })
            // data.obj.dataArray[0].items.forEach((d, index) => {
            //   this.addPointer(
            //     Cesium.Cartesian3.fromDegrees(
            //       d.longitude * 1,
            //       d.latitude * 1,
            //       height
            //     ),
            //     'xiaoqu' + index,
            //     this.header + 'img/xiaoqu.png',
            //     { columns: [], rows: d.arr }
            //   )
            // })
          }
        })
      } else if (this.nowPageName && this.nowPageName.indexOf('未办证住所') >= 0) {
        this.axios.get(`/leaderview/QZF/getWBZ1`).then(data => {
          if (data.success) {
            let height = 100
            data.obj.dataArray.forEach((d, index) => {
              // if (d.street === '望江路街道办事处' || d.street === '金花桥街道办事处') {
              d.items.forEach((ele, ind) => {
                this.addPointer(
                  Cesium.Cartesian3.fromDegrees(
                    ele.longitude * 1,
                    ele.latitude * 1,
                    height
                  ),
                  'wbzzs' + d.street + ind,
                  this.header + 'img/xiaoqu.png',
                  { columns: [], rows: ele.items[0].items }
                )
              })
              // }
            })
          }
        })//  getQZF4
      }
    }
  },
  mounted () {
    this.init3D()
    this.initBase()
    // this.initModels()
    this.initPostrender()
    this.addPopEvent()
    this.fly()
    window.changeSZChecked = this.changeSZChecked
    window.addPointTrue = this.addPointTrue
  },
  methods: {
    addPointTrue () {
      if (this.newSZCheckEdData.indexOf('重点点位') >= 0) {
        this.AddPointState = true
      } else {
        Notification({
          message: '请先开启重点点位！',
          position: 'bottom-right',
          customClass: 'toast toast-info'
        })
      }
    },
    changeSZChecked (data) {
      this.newSZCheckEdData = data
      if (data.indexOf('社区区划') >= 0) {
        this.addJxJ()
        this.addSheQuWangge()
      } else {
        this.removeJxJ()
        this.removeSheQuWangge()
      }

      if (data.indexOf('管控区') >= 0) {
        if (GuanKongquPoint.length === 0) {
          this.initSheZang1()
        }
        if (keyPlacesPoint.length === 0) {
          this.initkeyPlaces()
        }
      } else {
        this.removeSheZang1()
        this.removekeyPlaces()
      }
      if (data.indexOf('网格区') >= 0) {
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
      if (data.indexOf('视频巡控') >= 0) {
        if (videoPoint.length === 0) {
          this.addVideoPoint()
        }
      } else {
        this.removeVideoPoint()
      }
      if (data.indexOf('重点点位') >= 0) {
        this.addPontXMQ()
      } else {
        this.removePontXMQ()
      }
      // ['涉藏商店','民宿旅馆','藏餐茶吧','娱乐场所','涉藏机构','小区院落','锅庄舞场']
      if (data.indexOf('涉藏商店') >= 0) {
        this.addSZGKPoint(1)
      } else {
        this.removeSZGKPoint(1)
      }
      if (data.indexOf('民宿旅馆') >= 0) {
        this.addSZGKPoint(2)
      } else {
        this.removeSZGKPoint(2)
      }
      if (data.indexOf('藏餐茶吧') >= 0) {
        this.addSZGKPoint(3)
      } else {
        this.removeSZGKPoint(3)
      }
      if (data.indexOf('娱乐场所') >= 0) {
        this.addSZGKPoint(4)
      } else {
        this.removeSZGKPoint(4)
      }
      if (data.indexOf('涉藏机构') >= 0) {
        this.addSZGKPoint(5)
      } else {
        this.removeSZGKPoint(5)
      }
      if (data.indexOf('涉藏院落') >= 0) {
        this.addSZGKPoint(6)
      } else {
        this.removeSZGKPoint(6)
      }
      if (data.indexOf('锅庄舞场') >= 0) {
        this.addSZGKPoint(7)
      } else {
        this.removeSZGKPoint(7)
      }
      if (data.indexOf('重点区域勤务') >= 0) {
        if (shezangmarkers['beiqing'] === undefined || shezangmarkers['beiqing'].length === 0) {
          this.addshezangmarkers('beiqing')
          this.addshezangmarkers('xianchangzhihui')
          this.addshezangmarkers('fengkong')
          this.addshezangmarkers('xundakuaifan')
          this.addshezangmarkers('huaxikuaifan')
          this.addshezangmarkers('xiaofangzhanche')
        }
      } else {
        this.removeshezangmarkers('beiqing')
        this.removeshezangmarkers('fengkong')
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
      let data = viewer.entities.add({
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
      return data
    },
    addPopEvent () {
      var that = this
      function SZpopBig () {
        let SZCTpopBig = document.getElementById('SZCTpopBig')
        if (SZCTpopBig) {
          var windowPosition = new Cesium.Cartesian2()
          var canvasHeight = viewer.scene.canvas.height
          var canvasWidth = viewer.scene.canvas.width
          Cesium.SceneTransforms.wgs84ToWindowCoordinates(
            viewer.scene,
            Cesium.Cartesian3.fromDegrees(that.x, that.y, that.z + 100),
            windowPosition
          )
          SZCTpopBig.style.bottom = canvasHeight - windowPosition.y + 'px'
          SZCTpopBig.style.right =
            canvasWidth - windowPosition.x - SZCTpopBig.offsetWidth * 0.5 + 'px'
        }
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
    suerChange () {
      this.IsXiuGaiState = true
      this.sureChangeData = JSON.parse(JSON.stringify(this.changeDataArry))
    },
    giveUpXiuGai () {
      this.IsXiuGaiState = false
    },
    saveXiugai () {
      let newData = JSON.parse(JSON.stringify(this.sureChangeData))
      if (newData.name !== '' && newData.details !== '') {
        let url = `/leaderview/ZHSQ/saveCustomDot?latitude=${newData.latitude}&longitude=${newData.longitude}&name=${newData.name}&details=${newData.details}`
        if (newData.id !== '') {
          url = `/leaderview/ZHSQ/saveCustomDot?id=${newData.id}&latitude=${newData.latitude}&longitude=${newData.longitude}&name=${newData.name}&details=${newData.details}`
        }
        this.axios
          .get(url)
          .then(res => {
            if (res.success) {
              this.changeDataArry = JSON.parse(JSON.stringify(this.sureChangeData))
              Notification({
                message: '修改成功',
                position: 'bottom-right',
                customClass: 'toast toast-success'
              })
              this.IsXiuGaiState = false
              this.addPontXMQ()
            }
          })
      } else {
        Notification({
          message: '标题及内容不能为空',
          position: 'bottom-right',
          customClass: 'toast toast-success'
        })
      }
    },
    AddZDDW () {
      let newData = JSON.parse(JSON.stringify(this.AddDataArry))
      if (newData.name !== '' && newData.details !== '') {
        let url = `/leaderview/ZHSQ/saveCustomDot?latitude=${newData.latitude}&longitude=${newData.longitude}&name=${newData.name}&details=${newData.details}`
        this.axios
          .get(url)
          .then(res => {
            if (res.success) {
              Notification({
                message: '新增成功！',
                position: 'bottom-right',
                customClass: 'toast toast-success'
              })
              this.sureAddPoint = false
              this.addPontXMQ()
            }
          })
      } else {
        Notification({
          message: '标题及内容不能为空',
          position: 'bottom-right',
          customClass: 'toast toast-success'
        })
      }
    },
    deletZDDW () {
      if (this.changeDataArry.id) {
        this.axios
          .get('/leaderview/ZHSQ/deleteCustomDot?id=' + this.changeDataArry.id)
          .then(res => {
            if (res.success) {
              Notification({
                message: '删除成功',
                position: 'bottom-right',
                customClass: 'toast toast-success'
              })
              this.popshowXMQ = false
              this.addPontXMQ()
            }
          })
      }
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
    WBZShowRuzhu (id) {
      this.axios
        .get('/leaderview/QZF/getQZF10?query=hotelId&param=场所ID:' + id)
        .then(res => {
          if (res.success) {
            let boxData = {
              title: '数据详情',
              data: 'arry',
              dataArray: res.obj
            }
            this.$parent.$parent.ShowTableBox(boxData)
          }
        })
    },
    WBZShowZofang (id) {
      this.axios
        .get('/leaderview/QZF/getQZF13?query=hotelId&param=场所ID:' + id)
        .then(res => {
          if (res.success) {
            let boxData = {
              title: '数据详情',
              data: 'arry',
              dataArray: res.obj
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
      let en = viewer.entities.add({
        position,
        id: id,
        dataArray: dataArray,
        billboard: {
          image: img || this.header + 'img/click.png',
          scale: 0.2
        }
      })
      billboardMarkers.push(
        en
      )
      return en
    },
    flyJXJ () {
      if (this.nowPageName && this.nowPageName.indexOf('市级') >= 0) {
        viewer.scene.camera.flyTo({
          destination: Cesium.Cartesian3.fromDegrees(
            104.07571587587084,
            30.585662107448858,
            19802.151055716295
          ),
          orientation: {
            heading: 0.07691929777463358,
            pitch: -1.2997070199225433,
            roll: 0.000006930263633186939
          },
          duration: 1
        })
      } else {
        viewer.scene.camera.flyTo({
          destination: Cesium.Cartesian3.fromDegrees(
            104.07571587587084,
            30.585662107448858,
            19802.151055716295
          ),
          orientation: {
            heading: 0.07691929777463358,
            pitch: -1.2997070199225433,
            roll: 0.000006930263633186939
          },
          duration: 1
        })
      }
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
      if (this.isYL) {
        this.popshowBig = false
        this.SZCTDataXQ = true
        this.SZCTDataArray = []
        for (const key in nowShowData) {
          if (Object.hasOwnProperty.call(nowShowData, key)) {
            if (key !== 'created_at' && key !== 'user_id' && key !== 'user_name' && key !== 'id' && key !== 'user_identifier' && key !== 'response_id') {
              this.SZCTDataArray.push({
                name: key,
                value: nowShowData[key]
              })
            }
          }
        }
      } else if (this.isChongHe) {
        this.popshowBig = false
        this.SZCTDataXQ = true
        this.SZCTDataArray = []
        this.SZGKName = nowShowData.csmc
        if (nowShowData.type === '娱乐场所') {
          this.SZCTDataNameArray = {
            address: '地址',
            community: '所属社区',
            type: '场所类别',
            area: '面积',
            fzrxm: '负责人姓名',
            fzrlxdh: '负责人联系电话',
            ygsl: '员工数量',
            aqfxdj: '安全风险等级',
            pqmjxm: '片区民警姓名',
            pqmjlxdh: '片区民警联系电话'
          }
        } else if (nowShowData.type === '涉藏商店') {
          this.SZCTDataNameArray = {
            address: '地址',
            community: '所属社区',
            type: '场所类别',
            area: '面积',
            fzrxm: '负责人姓名',
            fzrlxdh: '负责人联系电话',
            ygsl: '员工数量',
            aqfxdj: '安全风险等级',
            pqmjxm: '片区民警姓名',
            pqmjlxdh: '片区民警联系电话'
          }
        } else if (nowShowData.type === '藏餐茶吧') {
          this.SZCTDataNameArray = {
            address: '地址',
            community: '所属社区',
            type: '场所类别',
            area: '面积',
            fzrxm: '负责人姓名',
            fzrlxdh: '负责人联系电话',
            ygsl: '员工数量',
            aqfxdj: '安全风险等级',
            pqmjxm: '片区民警姓名',
            pqmjlxdh: '片区民警联系电话'
          }
        }
        for (const key in nowShowData) {
          if (Object.hasOwnProperty.call(nowShowData, key)) {
            if (key !== 'created_at' && key !== 'user_id' && key !== 'user_name' && key !== 'id' && key !== 'user_identifier' && key !== 'response_id') {
              this.SZCTDataArray.push({
                name: key,
                value: nowShowData[key]
              })
            }
          }
        }
      } else {
        this.ShowTableTan = false
        this.nowShowData = nowShowData
      }
      console.log('nowShowData', nowShowData)
    },
    addLabelMarker (lon, lat, url, name, small, data) {
      let size = small ? 34 : 54
      let height = small ? 40 : 60
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
          scale: 0.25
        },
        name: name,
        DataArry: data || [],
        SZlabelName: name.split('SZGK')[0],
        label: {
          show: false,
          showBackground: true,
          backgroundColor,
          scale: 0.5,
          font: `normal ${size}px MicroSoft YaHei`,
          text: name.split('SZGK')[0],
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
          scale: 0.3
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
    addSheQuWangge () {
      this.removeSheQuWangge()
      let wangGeList = []
      this.axios.get('/leaderview/ZHSQ/getGridDot').then((res) => {
        wangGeList = res.obj.dataArray[0].items
        shequwanggepositions.forEach(item => {
          item.wangges.forEach(child => {
            if (child.positions.length > 0) {
              let wangGeInfo = {}
              wangGeList.forEach(el => {
                if (el.community === item.name) {
                  el.items.forEach(n => {
                    if (child.name.indexOf(n.sswg) !== -1) {
                      wangGeInfo = n
                    }
                  })
                } else if (el.community === '少陵路社区' && item.name === '少陵社区') {
                  el.items.forEach(n => {
                    if (child.name.indexOf(n.sswg) !== -1) {
                      wangGeInfo = n
                    }
                  })
                }
              }) // 获取网格具体信息
              let poly = viewer.entities.add({
                polygon: {
                  hierarchy: Cesium.Cartesian3.fromDegreesArrayHeights(
                    child.positions
                  ),
                  perPositionHeight: true,
                  material: child.color.withAlpha(0.3)
                },
                name: item.name + '-' + child.name,
                community: item.name,
                wangGeInfo: wangGeInfo
              })
              let polyLine = viewer.entities.add({
                polyline: {
                  positions: Cesium.Cartesian3.fromDegreesArrayHeights(
                    child.positions
                  ),
                  material: Cesium.Color.YELLOW,
                  width: 2
                }
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
                  font: `normal 28px MicroSoft YaHei`,
                  text: child.name,
                  fillColor: Cesium.Color.fromCssColorString('#ffffff'),
                  horizontalOrigin: Cesium.HorizontalOrigin.LEFT
                },
                name: item.name + '-' + child.name,
                community: item.name,
                wangGeInfo: wangGeInfo
              })
              shequwangges.push(poly)
              shequwangges.push(label)
              shequwangges.push(polyLine)
            }
          })
        })
      })
    },
    removeSheQuWangge () {
      if (shequwangges.length > 0) {
        shequwangges.forEach(item => {
          viewer.entities.remove(item)
        })
        shequwangges = []
      }
    },
    addWangge () {
      this.removeWangge()
      this.axios.get('/leaderview/ZHSQ/getSZCT4').then((res) => {
        let wangGeList = res.obj.dataArray[0].items
        let wangGeInfo = {}
        wanggepositions.forEach(item => {
          item.wangges.forEach(child => {
            if (child.positions.length > 0) {
              wangGeList.forEach(el => {
                if (el.sssq === item.name) {
                  el.items.forEach(n => {
                    if (child.name.indexOf(n.sswg) !== -1) {
                      wangGeInfo = n.items[0]
                    }
                  })
                } else if (el.sssq === '少陵路社区' && item.name === '少陵社区') {
                  el.items.forEach(n => {
                    if (child.name.indexOf(n.sswg) !== -1) {
                      wangGeInfo = n.items[0]
                    }
                  })
                }
              }) // 获取网格具体信息
              let poly = viewer.entities.add({
                polygon: {
                  hierarchy: Cesium.Cartesian3.fromDegreesArrayHeights(
                    child.positions
                  ),
                  perPositionHeight: true,
                  material: child.color.withAlpha(0.3)
                },
                name: item.name + '-' + child.name,
                wangGeInfo: wangGeInfo
              })
              let polyLine = viewer.entities.add({
                polyline: {
                  positions: Cesium.Cartesian3.fromDegreesArrayHeights(
                    child.positions
                  ),
                  material: Cesium.Color.YELLOW,
                  width: 2
                }
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
                  font: `normal 28px MicroSoft YaHei`,
                  text: child.name,
                  fillColor: Cesium.Color.fromCssColorString('#ffffff'),
                  horizontalOrigin: Cesium.HorizontalOrigin.LEFT
                },
                name: item.name + '-' + child.name,
                wangGeInfo: wangGeInfo
              })
              wangges.push(poly)
              wangges.push(label)
              wangges.push(polyLine)
            }
          })
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
            0.6, item.properties.na
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
      $.getJSON(this.header + 'geojson/cachuJXJ.json', res => {
        let featrue = res.features[0]
        let color = new Cesium.Color(0, 0, 0, 1)
        let polygonpositions = []
        let polygonpositions1 = []
        let geometry1 = featrue.geometry.coordinates[0]
        let geometry2 = featrue.geometry.coordinates[1]
        geometry1.forEach(item => {
          polygonpositions.push(item[0])
          polygonpositions.push(item[1])
        })
        geometry2.forEach(item => {
          polygonpositions1.push(item[0])
          polygonpositions1.push(item[1])
        })
        let hole1 = {
          positions: Cesium.Cartesian3.fromDegreesArray(polygonpositions1)
        }
        let en = viewer.entities.add({
          polygon: {
            hierarchy: {
              positions: Cesium.Cartesian3.fromDegreesArray(polygonpositions),
              holes: [hole1]
            },
            material: color
          },
          name: '边界'
        })
        xingzhengquhuaPolygons['武侯'].push(en)
      })
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
      xingzhengquhuaPolygons = {
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
        '玉林街道': [],
        '武侯': []
      }
      if (tileset) {
        tileset.show = true
      }
      if (tilesetJxJ) {
        tilesetJxJ.show = false
      }
      $.getJSON(this.header + 'geojson/cachuWuhou.json', res => {
        let featrue = res.features[0]
        let color = new Cesium.Color(0, 0, 0, 1)
        let polygonpositions = []
        let polygonpositions1 = []
        let geometry1 = featrue.geometry.coordinates[0]
        let geometry2 = featrue.geometry.coordinates[1]
        geometry1.forEach(item => {
          polygonpositions.push(item[0])
          polygonpositions.push(item[1])
        })
        geometry2.forEach(item => {
          polygonpositions1.push(item[0])
          polygonpositions1.push(item[1])
        })
        let hole1 = {
          positions: Cesium.Cartesian3.fromDegreesArray(polygonpositions1)
        }
        let en = viewer.entities.add({
          polygon: {
            hierarchy: {
              positions: Cesium.Cartesian3.fromDegreesArray(polygonpositions),
              holes: [hole1]
            },
            material: color
          },
          name: '边界'
        })
        xingzhengquhuaPolygons['武侯'].push(en)
      })
      $.getJSON(this.header + 'geojson/xzqh.json', res => {
        let positions = res.features
        positions.forEach((item, index) => {
          let color = new Cesium.Color(15 / 255, 19 / 255, 57 / 255, 0.01)
          let extrend = false
          if (this.nowPageName && this.nowPageName.indexOf('未办证住所') >= 0) {
            if (item.properties.Name === '金花桥街道') {
              extrend = true
              color = new Cesium.Color(116 / 255, 151 / 255, 232 / 255, 0.01)
            }
            if (item.properties.Name === '望江路街道') {
              extrend = true
              color = new Cesium.Color(116 / 255, 151 / 255, 232 / 255, 0.01)
            }
          } else if (this.nowPageName && (this.nowPageName.indexOf('涉藏概况') >= 0 || this.nowPageName.indexOf('应急处突') >= 0)) {

          } else {
          }
          let polygonpositions = []
          let pointer = turf.centerOfMass(item.geometry).geometry.coordinates
          if (item.properties.Name === '金花桥街道') {
            pointer = [103.97374548683935, 30.591885280709842]
          }
          let marker = this.addMarker(
            Cesium.Cartesian3.fromDegrees(pointer[0], pointer[1], 100),
            this.header + `img/街道名称/${item.properties.Name}.png`, 0.4, item.properties.Name
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
              extrudedHeight: extrend ? 5 : 1
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
              material: Cesium.Color.fromCssColorString('#374da7'),
              depthFailMaterial: Cesium.Color.fromCssColorString('#374da7'),
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
      viewer.scene.primitives.add(tileset)
      tileset.readyPromise.then(function (tileset3D) {
        tileset.style = new Cesium.Cesium3DTileStyle({
          color: {
            conditions: [
              ['true', 'rgba(21, 36, 75 ,1)'] // 'rgb(127, 59, 8)']
            ]
          }
        })
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
    },
    removePontXMQ () {
      ZdDWarray.forEach(item => {
        viewer.entities.remove(item)
      })
      ZdDWarray = []
    },
    removeSZGKPoint (i) {
      SZGKPoint[i].forEach(item => {
        viewer.entities.remove(item)
      })
      SZGKPoint[i] = []
    },
    addSZGKPoint (i) {
      this.removeSZGKPoint(i)
      SZGKPoint[i] = []
      if (i === 1 || i === 3 || i === 4) {
        this.axios.get('/leaderview/ZHSQ/getSZCT6').then(data => {
          if (i === 1) { // 涉藏商店
            data.obj.dataArray[1].items.forEach((element, ind) => {
              let en = this.addPointer(
                Cesium.Cartesian3.fromDegrees(
                  element.longitude * 1,
                  element.latitude * 1,
                  100
                ),
                'chonghe' + '涉藏商店' + ind,
                this.header + 'img/imgs/1.png',
                { columns: [], rows: element.items }
              )
              SZGKPoint[i].push(en)
            })
          } else if (i === 3) {
            data.obj.dataArray[2].items.forEach((element, ind) => {
              let en = this.addPointer(
                Cesium.Cartesian3.fromDegrees(
                  element.longitude * 1,
                  element.latitude * 1,
                  100
                ),
                'chonghe' + '藏餐茶吧' + ind,
                this.header + 'img/imgs/3.png',
                { columns: [], rows: element.items }
              )
              SZGKPoint[i].push(en)
            })
          } else if (i === 4) {
            data.obj.dataArray[0].items.forEach((element, ind) => {
              let en = this.addPointer(
                Cesium.Cartesian3.fromDegrees(
                  element.longitude * 1,
                  element.latitude * 1,
                  100
                ),
                'chonghe' + '娱乐场所' + ind,
                this.header + 'img/imgs/4.png',
                { columns: [], rows: element.items }
              )
              SZGKPoint[i].push(en)
            })
          }
        })
      } else if (i === 6) {
        this.axios.get('/leaderview/ZHSQ/getCommunityDot').then(data => {
          data.obj.dataArray.forEach((d, index) => {
            d.items.forEach((element, ind) => {
              if (d.sfszzdyl === '是') {
                element.items.forEach(ele => {
                  let name = ele.ylmc || ''
                  let en = this.addLabelMarker(
                    ele.longitude * 1,
                    ele.latitude * 1,
                    this.header + `img/imgs/6_sz.png`,
                    name + 'SZGK' + i,
                    'small',
                    ele
                  )
                  SZGKPoint[i].push(en)
                })

                // let en = this.addPointer(
                //   Cesium.Cartesian3.fromDegrees(
                //     ele.longitude * 1,
                //     ele.latitude * 1,
                //     100
                //   ),
                //   'yuanluo' + d.sfszzdyl + ind,
                //   this.header + 'img/imgs/6_sz.png',
                //   { columns: [], rows: ele.items }
                // )
              } else {
                // let en = this.addPointer(
                //   Cesium.Cartesian3.fromDegrees(
                //     ele.longitude * 1,
                //     ele.latitude * 1,
                //     100
                //   ),
                //   'yuanluo' + d.sfszzdyl + ind,
                //   this.header + 'img/imgs/6.png',
                //   { columns: [], rows: ele.items }
                // )
                // SZGKPoint[i].push(en)
              }
            })
          })
        })
      } else {
        this.axios.get(`/leaderview/ZHSQ/getSZCT3?param=${i}`).then(data => {
          data.obj.dotArray.forEach((ele, index) => {
            let name = ele.csmc || ele.company_name || ele.name
            // if (index <= 40) {
            let en = this.addLabelMarker(
              ele.longitude * 1,
              ele.latitude * 1,
              this.header + `img/imgs/${i}.png`,
              name + 'SZGK' + i,
              'small',
              ele
            )
            SZGKPoint[i].push(en)
            // }
          })
        })
      }
    },
    addPontXMQ () {
      this.removePontXMQ()
      ZdDWarray = []
      this.axios.get(`/leaderview/ZHSQ/getCustomDot`).then(data => {
        data.obj.forEach(ele => {
          let data = viewer.entities.add({
            position: Cesium.Cartesian3.fromDegrees(ele.longitude * 1, ele.latitude * 1, 40),
            name: 'XMQ',
            billboard: {
              image: this.header + 'img/importantData.png',
              scale: 0.3
            },
            dataArry: ele
          })
          let dd = this.addDynamicCircle([ele.longitude * 1, ele.latitude * 1, 40], 'test', 100)
          ZdDWarray.push(data)
          ZdDWarray.push(dd)
        })
      })
    },
    addVideoPoint () {
      this.axios.get(`/leaderview/WuHou/getSZCTPoints`).then(data => {
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
                scale: 0.3
                // distanceDisplayCondition: new Cesium.DistanceDisplayCondition(0.0, 6200.0)
              },
              label: {
                show: false,
                showBackground: true,
                backgroundColor: Cesium.Color.fromCssColorString('#000'),
                scale: 0.6,
                font: 'normal 36px MicroSoft YaHei',
                text: item.name.split(')')[1] || item.name,
                pixelOffset: new Cesium.Cartesian2(10, -30),
                horizontalOrigin: Cesium.HorizontalOrigin.LEFT
                // distanceDisplayCondition: new Cesium.DistanceDisplayCondition(0.0, 6200.0)
              },
              cameraId: item.deviceIndexCode,
              name: item.name,
              type: 'camera',
              labelName: item.name.split(')')[1] || item.name
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
        imageryProvider: new BaiduImageryProvider({
          url: this.header + `地图瓦片/{x}_{y}_{z}.jpg`,
          crs: 'WGS84'
        })
      })
      viewer.scene.skyAtmosphere.show = false
      viewer.scene.globe.enableLighting = false
      viewer.scene.globe.baseColor = Cesium.Color.BLACK
      viewer.cesiumWidget.creditContainer.style.display = 'none' // 去水印
      viewer.scene.globe.depthTestAgainstTerrain = true
      viewer.scene.fxaa = false
      viewer.scene.postProcessStages.fxaa.enabled = false
      viewer.scene.globe.maximumScreenSpaceError = 4 / 3
      var layer = viewer.imageryLayers.get(0)
      layer.gamma = 0.66
      layer.magnificationFilter = Cesium.TextureMagnificationFilter.NEAREST_MIPMAP_LINEAR
      viewer.scene.screenSpaceCameraController.minimumZoomDistance = 10// 相机的高度的最小值
      viewer.scene.screenSpaceCameraController.maximumZoomDistance = 36754.32231647567
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
            6 +
            ','
        )
        this.popshow = false
        this.popshowBig = false
        this.popshowWGQ = false
        this.popshowXMQ = false
        this.SZDataShowBig = false
        this.SZCTDataXQ = false
        this.isYL = false
        this.isChongHe = false
        this.ifWangGe = false
        this.currentWangGe = {}
        this.iswbzzs = false
        this.sqjcfb = false
        this.SZGKName = ''
        console.log('pick', picked.id.id, picked.id.name, picked.id.dataArray, picked.id.community, picked.id.wangGeInfo)
        if (picked && picked.id && picked.id.name && picked.id.name.indexOf('网格区') > 0) {
          if (picked.id.community) {
            this.currentWangGe = picked.id.wangGeInfo
            this.axios.get('/leaderview/ZHSQ/getSZCT1?param=' + picked.id.community).then(data => {
              if (data.success) {
                this.sqjcfb = true
                this.ifWangGe = true
                this.popshowBig = true
                this.ShowTableTan = true
                this.CheckEdId = picked.id.id
                this.TableTanData = {
                  columns: data.obj.columns,
                  rows: data.obj.rows
                }
              }
            })
          } else {
            this.popshowWGQ = true
            this.WGQData = picked.id.wangGeInfo
          }
        } else if (this.AddPointState) {
          this.AddPointState = false
          this.AddDataArry = {
            longitude: lng,
            latitude: lat,
            name: '',
            details: ''
          },
          this.sureAddPoint = true
        } else if (picked && picked.primitive && picked.id && picked.id._billboard) {
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
              this.IsXiuGaiState = false
              this.changeDataArry = picked.id.dataArry
            } else if (picked.id.type === 'camera') {
              let cameraData = picked.id.cameraId
              this.$parent.$parent.ShowVideoBox(cameraData)
            } else if (picked.id.name && picked.id.name.indexOf('管控区') >= 0) {
              // this.SZDataShowBig = true
              // this.CheckEdId = picked.id.name.split('管控区')[0] * 1
              // this.SZData = this.AllData[this.CheckEdId - 1]
            } else if (picked.id.id && picked.id.id.indexOf('wbzzs') >= 0) {
              this.popshowBig = true
              this.ShowTableTan = true
              this.iswbzzs = true
              this.CheckEdId = picked.id.id
              this.TableTanData = {
                columns: ['address', 'street', 'roomNum', 'bedNum'],
                rows: picked.id.dataArray.rows
              }
            } else if (picked.id && picked.id.dataArray && picked.id.dataArray.rows) {
              if (picked.id.id.indexOf('yuanluo') >= 0) {
                this.isYL = true
                this.TableTanData = {
                  columns: ['ylmc', 'ylxz', 'address'], // 需要修改
                  rows: picked.id.dataArray.rows
                }
              } if (picked.id.id.indexOf('chonghe') >= 0) {
                this.isChongHe = true
                this.TableTanData = {
                  columns: ['csmc', 'type', 'community', 'address'],
                  rows: picked.id.dataArray.rows
                }
              } else {
                this.TableTanData = {
                  columns: ['placeName', 'address', 'roomNum', 'bedNum'],
                  rows: picked.id.dataArray.rows
                }
              }
              this.popshowBig = true
              this.ShowTableTan = true
              this.CheckEdId = picked.id.id
            } else if (picked.id.name.indexOf('社区') >= 0) {
              // /leaderview/ZHSQ/getSZCT1
              this.sqjcfb = true
              this.axios.get('/leaderview/ZHSQ/getSZCT1?param=' + picked.id.name).then(data => {
                if (data.success) {
                  this.popshowBig = true
                  this.ShowTableTan = true
                  this.CheckEdId = picked.id.id
                  this.TableTanData = {
                    columns: data.obj.columns,
                    rows: data.obj.rows
                  }
                }
              })
            } else if (picked.id.name.indexOf('SZGK') >= 0) {
              if (picked.id.DataArry !== []) {
                if (picked.id.name.indexOf('SZGK6') >= 0) { // 涉藏院落
                  this.SZCTDataNameArray = {
                    address: '地址',
                    community: '所属社区',
                    sqwg: '社区网格',
                    wgyxm: '网格员姓名',
                    wgylxdh: '网格员联系方式',
                    mjxm: '民警姓名',
                    mjlxdh: '民警联系方式',
                    ylglms: '院落管理模式',
                    yllds: '院落楼栋数',
                    yldys: '院落单元数',
                    ylfws: '院落房屋数',
                    wygsmc: '物业公司名称',
                    wygslxrxm: '物业公司联系人姓名',
                    wygslxrdh: '物业公司联系人电话',
                    xjnd: '修建年代',
                    type: '场所类别',
                    area: '面积',
                    manager_name: '负责人姓名',
                    manager_phone: '负责人联系方式',
                    member_number: '员工数量'
                  }
                  this.SZGKName = picked.id.name.slice(0, picked.id.name.indexOf('SZGK6'))
                }
                // if (picked.id.name.indexOf('SZGK1') >= 0) { // 涉藏商店
                //   this.SZCTDataNameArray = {
                //     address: '地址',
                //     community: '所属社区',
                //     type: '场所类别',
                //     area: '面积',
                //     manager_name: '负责人姓名',
                //     manager_phone: '负责人联系方式',
                //     member_number: '员工数量'

                //   }
                //   this.SZGKName = picked.id.name.slice(0, picked.id.name.indexOf('SZGK1'))
                // }
                // if (picked.id.name.indexOf('SZGK3') >= 0) { // 藏餐茶吧
                //   this.SZCTDataNameArray = {
                //     address: '地址',
                //     community: '所属社区',
                //     type: '场所类别',
                //     area: '面积',
                //     manager_name: '负责人姓名',
                //     manager_phone: '负责人联系方式',
                //     member_number: '员工数量'

                //   }
                //   this.SZGKName = picked.id.name.slice(0, picked.id.name.indexOf('SZGK3'))
                // }
                // if (picked.id.name.indexOf('SZGK4') >= 0) { // 娱乐场所
                //   this.SZCTDataNameArray = {
                //     address: '地址',
                //     community: '所属社区',
                //     type: '场所类别',
                //     area: '面积',
                //     manager_name: '负责人姓名',
                //     manager_phone: '负责人联系方式',
                //     member_number: '员工数量'

                //   }
                //   this.SZGKName = picked.id.name.slice(0, picked.id.name.indexOf('SZGK4'))
                // }
                if (picked.id.name.indexOf('SZGK5') >= 0) { // 涉藏机构
                  this.SZCTDataNameArray = {
                    company_address: '地址',
                    community: '所属社区',
                    type: '场所类别',
                    company_manager: '单位负责人姓名',
                    manager_phone: '单位负责人联系方式',
                    office_manager: '联系人姓名',
                    office_manager_phone: '联系人联系方式'

                  }
                  this.SZGKName = picked.id.name.slice(0, picked.id.name.indexOf('SZGK5'))
                }
                if (picked.id.name.indexOf('SZGK7') >= 0) { // 锅庄舞场
                  this.SZCTDataNameArray = {
                    address: '地址',
                    rnrs: '容纳人数',
                    gzs: '观众数',
                    community: '所属社区',
                    type: '场所类别',
                    manager_name: '负责人姓名',
                    time: '活动时段',
                    pqmjxm: '片区民警姓名',
                    pqmjlxdh: '片区民警联系电话',
                    pqwgyxm: '片区网格员姓名',
                    pqwgylxdh: '片区网格员联系电话',
                    zyfx: '主要风险',
                    ydcs: '应对措施'
                  }
                  this.SZGKName = picked.id.name.slice(0, picked.id.name.indexOf('SZGK7')) + '锅庄舞场'
                }
                this.SZCTDataXQ = true
                this.SZCTDataArray = []
                for (const key in picked.id.DataArry) {
                  if (Object.hasOwnProperty.call(picked.id.DataArry, key)) {
                    if (key !== 'created_at' && key !== 'user_id' && key !== 'user_name' && key !== 'id' && key !== 'user_identifier' && key !== 'response_id' && key !== 'longitude' && key !== 'latitude') {
                      this.SZCTDataArray.push({
                        name: key,
                        value: picked.id.DataArry[key]
                      })
                    }
                  }
                }
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
            // if (picked.id.name === '浆洗街街道') {
            //   return
            // } else
            if (picked.id.name === '边界') {
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
              highLightPolygon.material = new Cesium.Color(15 / 255, 100 / 255, 100 / 255, 0.05)
            }
          } else {
            if (highLightPolygon) {
              highLightPolygon.material = hightLightMat
              highLightPolygon = null
              hightLightMat = null
            }
          }
          if (picked && picked.id && picked.id.labelName) {
            videoPoint.forEach(element => {
              if (element.labelName === picked.id.labelName && element.label) {
                element.label.show = true
              } else {
                element.label.show = false
              }
            })
          } else {
            videoPoint.forEach(element => {
              if (element.label) {
                element.label.show = false
              }
            })
          }
          if (picked && picked.id && picked.id.SZlabelName) {
            SZGKPoint.forEach(element => {
              if (element.length) {
                element.forEach(data => {
                  if (data.SZlabelName === picked.id.SZlabelName && data.label) {
                    data.label.show = true
                  } else {
                    data.label.show = false
                  }
                })
              }
            })
          } else {
            SZGKPoint.forEach(element => {
              if (element.length) {
                element.forEach(data => {
                  if (data.label) {
                    data.label.show = false
                  }
                })
              }
            })
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
.content #SZCTpopBig {
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
.content #SZCTpopBig .poptitle {
  position: absolute;
  top: 70px;
  left: 50px;
  font-size: 46px !important;
  color: #bbeefe;
  font-family: PangmenMainRoadTitleBody !important;
  font-weight: 400;
}
.content #SZCTpopBig .CloseBtn {
  position: absolute;
  cursor: pointer;
  top: 13px;
  right: 0px;
  height: 50px;
  width: 50px;
}
.content #SZCTpopBig .lineContain {
  padding: 10px 40px;
  margin: 0 20px;
  top: 70px;
  height: 70%;
  position: relative;
  overflow: auto;
}
.content #SZCTpopBig .lineContain .line {
  margin-bottom: 5px;
}
.content #SZCTpopBig .lineContain button {
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
  overflow: auto;
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
.btnArry{
  position: relative;
  top: 35px;
  text-align: right;
  padding: 0 50px;
}
</style>
