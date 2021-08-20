<template>
    <div v-if="itemsShow">
        <label :style="item.tag === 'Hint'? 'font-weight: bold; color: #fff;':''">{{ item.name }}</label>
        <template v-if="item.tag === 'select'">
            <select v-model="selectedItem[item.key]">
                <option
                    v-for="(option, optIndex) in item.options"
                    :key="`${option.name}_${optIndex}`"
                    :value="option.value"
                    >{{ option.name }}
                    </option>
            </select>
        </template>
        <template v-if="item.tag === 'MarkLocation'">
          <div style="position:relative;left:50px;top:20px;">
            <div style="margin-bottom:15px"><span style="margin-right:10px">经度</span><input type="number" name="" ref="lng"></div>
            <div><span style="margin-right:10px">纬度</span><input type="number" name="" ref="lat"></div>
            <button style="margin-left:35px;margin-top:20px;outline:none" @click="selectMarker">查询</button>
          </div>
        </template>
        <template v-if="item.tag === 'typeSelect'">
          <div class="typeSelect">
              <div v-for="(option,index) in item.options"
              :key='index'
              @click="changeType(option)"
              :style="{'cursor': 'pointer','border':selectedItem[item.key] === option.value?'1px solid #409EFF':'1px solid rgba(255,255,255,0.2)'}"
              ><span :class="option.icon" :style="{'color':selectedItem[item.key] === option.value?'#409EFF':'#fff'}"></span></div>
          </div>
        </template>
        <template v-if="item.tag === 'input'">
            <input  :type="item.type || 'number'"
                    :max="item.max*1 || 100000"
                    :min="item.min===0 ? item.min : item.min || -100000"
                    v-model="selectedItem[item.key]">
        </template>
        <template v-if="item.tag === 'Hint'">
        </template>
        <template v-if="item.tag === 'Color'">
          <!-- 颜色吸管 -->
            <!-- <input ref="MyColorInput" id="mycolor" :name="item.key" type="color" :value="selectedItem[item.key]" @drag="colorchange" @change="colorchange" style="width: 45px !important;float: right;height: 30px !important;">
            <div class="color-w200" style="width:130px"> -->
            <div class="color-w200">
                <Vcolor :data="selectedItem[item.key]"
                        :key="10"
                        :type="[item.key]"
                        @getdata="ChildGetColor"></Vcolor>
            </div>
        </template>
        <template v-if="item.tag === 'GradualColor'">
                  <select v-model="selectedItem[item.key]"
                          style="width: 68px !important; margin-left: 3px;">
                    <option value="false">单色</option>
                    <option value="true">渐变</option>
                  </select>
                  <div v-show="selectedItem[item.key] !== 'true'"
                       class="color-w200"
                       style="width: 100px;">
                    <Vcolor :data="selectedItem[item.ColorKey]"
                            :key="6"
                            :type="[item.ColorKey]"
                            @getdata="ChildGetColor"></Vcolor>
                  </div>
                  <div v-show="selectedItem[item.key] === 'true'"
                       class="barGradient"
                       @click="reverseClr"
                       :style="{'background': 'linear-gradient(45deg, ' + selectedItem[item.DoubleColorKey][0]  +',' + selectedItem[item.DoubleColorKey][1] + ')'}">
                    <div class="color-w15">
                      <Vcolor :data="selectedItem[item.DoubleColorKey][0]"
                              :key="13"
                              :index="0"
                              @getdata="getBarClr"></Vcolor>
                    </div>
                    <div class="color-w15"
                         style="float: right">
                      <Vcolor :data="selectedItem[item.DoubleColorKey][1]"
                              :key="14"
                              :index="1"
                              @getdata="getBarClr"></Vcolor>
                    </div>
                  </div>
        </template>
        <template v-if="item.tag === 'ImgFile'">
            <div class="color-w200" style="position:relative">
              <input type="file"
                id="Inputfile"
                style="width: 147px!important;margin-right: 8px;"
                accept="image/png, image/webp, image/jpeg, image/gif, image/jpg,image/svg+xml"
                @change='changeImg' />
              <label class="hoverTips" data-toggle='tooltip' :title='selectedItem[item.keyName]' style="position: absolute;float: left;height: 28px;right: 25px;width: 70px;overflow: hidden;">{{selectedItem[item.keyName]}}</label>
              <i class="icon-n-deleteNew delete_text" style="float: right;margin-top: 5px;" @click="removeFile"></i>
            </div>
        </template>
        <template v-if="item.tag === 'videoFile'">
          <div class="color-w200" style="position:relative">
            <input type="file"
              style="width: 147px!important;margin-right: 8px;"
              name="myfiles"
              id="myfiles"
              accept="video/*"
              @change="uploadVideo">
          <label class="hoverTips" data-toggle='tooltip' :title='selectedItem[item.keyName]' style="position: absolute;float: left;height: 28px;right: 25px;width: 70px;overflow: hidden;">{{selectedItem[item.keyName]}}</label>
          <i class="icon-n-deleteNew delete_text" style="float: right;margin-top: 5px;" @click="removeFile"></i>
          </div>
        </template>
        <template v-if="item.tag === 'GradientColor'">
            <div class="color-w200" style="margin-top: 8px;">
              <div class="gradient"
                    @click="reverseClr2"
                    :style="{'background': 'linear-gradient(45deg, ' +  twoColor0  +',' + twoColor1 + ')'}">
                  <div class="color-w15">
                    <Vcolor :data="twoColor0"
                        :key="10"
                        :type="item.key"
                        :ColorNum="1"
                        @getdata="ChildGetColor"></Vcolor>
                  </div>
                  <div class="color-w15"
                      style="float: right">
                    <Vcolor :data="twoColor1"
                        :key="10"
                        :type="item.key"
                        :ColorNum="2"
                        @getdata="ChildGetColor"></Vcolor>
                  </div>
              </div>
            </div>
        </template>
        <template v-if="item.tag === 'fontFamily'">
            <div class="form-group"
                  style="height: 30px;">
              <div v-for="(fontitem, index) in settingData.textFontFaces"
                    :key="index"
                    @click="setFontFamily(fontitem.fontFace)"
                    :class="{'fl': true, 'font-case': true, 'act': selectedItem.fontFamily===fontitem.fontFace}"
                    :style="{'font-family': fontitem.fontFace}">
                {{fontitem.fontName}}
              </div>
            </div>
        </template>
        <template v-if="item.tag === 'ColorArray'">
            <div class="form-group colorsConf">
              <span>序号</span>
              <span class="color-w70 text">颜色</span>
              <span @click="colorToAll"
                    v-if="selectedItem.chartType!=='TDHistogram' && selectedItem.chartType!=='KLine'"
                    style="color: #0088cc; cursor: pointer;">应用到已添加元件</span>
            </div>
            <div class="form-group colorsConf"
                v-for="(v,index) in selectedItem[item.key]"
                :key="index">
            <span class="colorOrder">{{index+1}}</span>
            <div class="gradient"
                    v-if="selectedItem.ifGradual==='true'"
                    @click="myreverseColor(index)"
                    :style="{'background': 'linear-gradient(45deg, ' + selectedItem[item.key][index][0]  +',' + selectedItem[item.key][index][1] + ')'}">
                <div class="color-w15">
                <Vcolor :data="selectedItem[item.key][index][0]"
                        :index="index"
                        @getdata="mygetColorStart"></Vcolor>
                </div>
                <div class="color-w15"
                    style="float: right">
                <Vcolor :data="selectedItem[item.key][index][1]"
                        :index="index"
                        @getdata="mygetGradColor"></Vcolor>
                </div>
            </div>
            <div v-else>
                <div class="color-w200"
                    style="float: left; width: 140px;">
                <Vcolor :data="selectedItem[item.key][index]"
                        :type="item.key"
                        :index="index"
                        @getdata="mygetSingleColor"></Vcolor>
                </div>
            </div>
            <i class="icon-n-add"
                @click="addColor(index + 1)"></i>
            <i class="icon-n-toUp"
                @click="moveUp(index)"></i>
            <i class="icon-n-putin"
                @click="moveDown(index)"></i>
            <i class="icon-n-deleteNew"
                @click="delColor(index)"></i>
            </div>
        </template>
        <template v-if="item.tag === 'NewBorder'">
          <br><br>
          <div class="form-group">
            <div v-for="(item, index) in cardCase"
                  :key="index"
                  @click="chgImgSrc(item.imgSrc)"
                  :class="{'fl': true, 'font-case': true, 'card-case': true, 'act': selectedItem.imgSrc===item.imgSrc}">
              <img :src="baseUrl + item.mini" />
            </div>
          </div>
          <label style="display: block; clear: both;">标题栏背景</label><br>
          <div class="form-group">
            <div v-for="(item, index) in titleCase"
                  :key="index"
                  @click="chgImgSrc(item.imgSrc)"
                  :class="{'fl': true, 'font-case': true, 'act': selectedItem.imgSrc===item.imgSrc}">
              <img :src="baseUrl + item.mini" />
            </div>
          </div>
        </template>
    </div>
</template>
<script>
import Vcolor from '@/components/Common/Vcolor'
import { baseData, gbs } from '@/config/settings'
export default {
  name: 'ChildTag',
  components: {Vcolor},
  props: ['item', 'selectedItem', 'selectChange', 'chooseSameFlag'],
  data () {
    return {
      markExit: false, // 在线地图查询添加时，判断添加的点是否存在
      baseUrl: '',
      settingData: baseData,
      picSrc: '',
      cardCase: [
        {
          mini: '/leaderviewWeb/border/cardMini1.png',
          imgSrc: '/leaderviewWeb/border/cardBg1.png'
        },
        {
          mini: '/leaderviewWeb/border/cardMini2.png',
          imgSrc: '/leaderviewWeb/border/cardBg2.png'
        },
        {
          mini: '/leaderviewWeb/border/cardMini3.png',
          imgSrc: '/leaderviewWeb/border/cardBg3.png'
        },
        {
          mini: '/leaderviewWeb/border/cardBg4.png',
          imgSrc: '/leaderviewWeb/border/cardBg4.png'
        }
      ],
      titleCase: [
        {
          mini: '/leaderviewWeb/border/titleMini1.png',
          imgSrc: '/leaderviewWeb/border/titleBg1.png'
        },
        {
          mini: '/leaderviewWeb/border/titleMini2.png',
          imgSrc: '/leaderviewWeb/border/titleBg2.png'
        },
        {
          mini: '/leaderviewWeb/border/titleBg3.png',
          imgSrc: '/leaderviewWeb/border/titleBg3.png'
        },
        {
          mini: '/leaderviewWeb/border/titleBg4.png',
          imgSrc: '/leaderviewWeb/border/titleBg4.png'
        },
        {
          mini: '/leaderviewWeb/border/titleBg5.png',
          imgSrc: '/leaderviewWeb/border/titleBg5.png'
        }
      ]
    }
  },
  beforeMount () {
    var reg = /^\/api/
    if (!reg.test(this.item.imgSrc)) {
      this.baseUrl = gbs.host
    }
  },
  mounted: function () {
    for (let i = 0; i < 11; i++) {
      // console.log('i: ', typeof i);
      const src = `/leaderviewWeb/border/titleBg${6 + Number(i)}.png`
      this.titleCase.push({
        mini: src,
        imgSrc: src
      })
    }
    for (let i = 0; i < 14; i++) {
      // console.log('i: ', typeof i);
      const src = `/leaderviewWeb/border/cardBg${5 + Number(i)}.png`
      this.cardCase.push({
        mini: src,
        imgSrc: src
      })
    }
  },

  computed: {
    itemsShow: function () {
      let canShow = true
      for (const key in this.item.parentKey) {
        if (this.selectedItem[key] !== this.item.parentKey[key]) {
          canShow = false
        }
      }
      return canShow
    },
    twoColor0: function () {
      if (this.item.tag === 'GradientColor') {
        return this.selectedItem[this.item.key][0]
      }
    },
    twoColor1: function () {
      if (this.item.tag === 'GradientColor') {
        return this.selectedItem[this.item.key][1]
      }
    }
  },
  methods: {
    selectMarker () {
      this.markExit = false
      this.selectedItem.pointArray.forEach(element => {
        if (Number(this.$refs.lng.value) === element.lng && Number(this.$refs.lat.value) === element.lat) {
          this.markExit = true
        } else {
        }
      })
      if (!this.markExit) {
        this.selectedItem.selectMark.lng = Number(this.$refs.lng.value)
        this.selectedItem.selectMark.lat = Number(this.$refs.lat.value)
        this.selectedItem.selectMark.icon = this.selectedItem.markerType
        this.selectedItem.pointArray.push({
          lng: Number(this.$refs.lng.value),
          lat: Number(this.$refs.lat.value),
          icon: this.selectedItem.markerType
        })
        this.selectedItem.selectChange = !this.selectedItem.selectChange // 每次点查一直询就修改，保证地图能一直更新
      }
    },
    changeType (option) {
      if (this.selectedItem.chartType === 'IntegratedHistogram') {
        this.selectedItem.ifGradual = 'false'
      }
      this.selectedItem[this.item.key] = option.value
    },
    setFontFamily: function (val) {
      this.selectedItem.fontFamily = val
    },
    chgImgSrc (imgSrc) {
      this.selectedItem[this.item.key] = imgSrc
    },
    uploadVideo (e) {
      // field, that
      if (e.value === '') {
        return
      }
      if (e.target.files[0].size > 100 * 1024 * 1024) {
        e.target.value = ''
        if (gbs.inDev) {
          Notification({
            message: '上传的视频不能大于100MB',
            position: 'bottom-right',
            customClass: 'toast toast-info'
          })
        } else {
          tooltip('', '上传的视频不能大于100MB', 'info')
        }
        return
      }
      var file = e.target.files[0]
      // 视频截图
      /**
      var windowURL = window.URL || window.webkitURL
      var videoURL = windowURL.createObjectURL(file)
      this.selectedItem.videoSrc = videoURL
      */
      var formdata = new FormData()
      formdata.append('uploaded_file', file)
      var _this = this
      this.uploadFile('video', formdata, function (data) {
        if (_this.selectedItem.chartType === 'video' || _this.selectedItem.chartType === 'BulletFrame') {
          _this.selectedItem.videoSrc = gbs.host + data.obj
          _this.tempVideoUrl = _this.selectedItem.videoSrc
          _this.selectedItem.linkSrc = data.obj // 保存一份纯接口的
          console.log(e.target.files[0].name)
          _this.selectedItem[_this.item.keyName] = e.target.files[0].name
        }
      })
      e.target.value = ''
    },
    changeImg: function (e) {
      if (e.value === '') {
        return
      }
      if (e.target.files[0].size > 15 * 1024 * 1024) {
        if (gbs.inDev) {
          Notification({
            message: '上传的文件不能大于15MB',
            position: 'bottom-right',
            customClass: 'toast toast-info'
          })
        } else {
          tooltip('', '上传的文件不能大于15MB', 'info')
        }
        e.target.value = ''
        return
      }
      const name = e.target.files[0].name
      var _this = this
      var formData = new FormData()
      formData.append('uploaded_file', e.target.files[0])
      this.uploadFile('img', formData, function (data) {
        const chartType = _this.selectedItem.chartType
        const curSrc = '/leaderviewWeb/home/getImg/' + data.obj.isCustom + '/' + data.obj.id
        _this.picSrc = curSrc
        _this.selectedItem[_this.item.key] = curSrc
        _this.selectedItem[_this.item.keyName] = e.target.files[0].name
        _this.$parent.$parent.$parent.saveHistory()
      })
      e.target.value = ''
    },
    removeFile () {
      this.selectedItem[this.item.key] = ''
      if (this.item.keyName) {
        this.selectedItem[this.item.keyName] = ''
      }
    },
    getBarClr (data) {
      this.$parent.$parent.$parent.saveHistory()
      this.selectedItem[this.item.DoubleColorKey].splice(data.index, 1, data.color)
      if (!this.selectChange && this.chooseSameFlag) {
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i][this.item.DoubleColorKey].splice(data.index, 1, data.color)
        })
      }
    },
    reverseClr () {
      if (!this.selectChange && this.chooseSameFlag) {
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i][this.item.DoubleColorKey].reverse()
        })
      } else {
        this.selectedItem[this.item.DoubleColorKey].reverse()
      }
    },
    reverseClr2 () {
      if (!this.selectChange && this.chooseSameFlag) {
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i].reverse()
        })
      } else {
        this.selectedItem[this.item.key].reverse()
      }
    },
    ChildGetColor (data) {
      this.$parent.$parent.$parent.saveHistory()
      if (this.item.chartType === 'NewProgress') {
        this.selectedItem[data.type] = data.color
      } else {
        if (data.type !== undefined) {
          if (data.ColorNum) {
            if (data.ColorNum === 1) {
              this.selectedItem[data.type] = [data.color, this.selectedItem[data.type][1]]
            } else {
              this.selectedItem[data.type] = [this.selectedItem[data.type][0], data.color]
            }
          } else {
            this.selectedItem[data.type] = data.color
          }
        } else {
          // 用来解决不能监听直接赋值的数组变化
          this.selectedItem.ctColors.splice(data.index, 1)
          this.selectedItem.ctColors.splice(data.index, 0, data.color)
        }
      }
    },
    uploadFile: function (type, formData, cb) {
      var _url = gbs.host + '/leaderview/home/file/upload'
      if (type === 'video') {
        _url = gbs.host + '/leaderview/home/file/uploadVideoFile'
      }
      $.ajax({
        url: _url,
        type: 'post',
        data: formData,
        async: false,
        cache: false,
        processData: false,
        contentType: false,
        success: function (data) {
          if (data.success) {
            // var _url = '/home/getImg/' + data.obj.isCustom + '/' + data.obj.id
            typeof cb === 'function' && cb(data)
          } else {
            // $("#lead-screen").hide()
            if (gbs.inDev) {
              Notification({
                message: data.msg,
                position: 'bottom-right',
                customClass: 'toast toast-error'
              })
            } else {
              tooltip('', data.msg, 'error')
            }
          }
        }
      })
    },
    mygetSingleColor (data) {
      this.$parent.$parent.$parent.saveHistory()
      if (!this.selectChange && this.chooseSameFlag) {
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i][this.item.key].splice(data.index, 1, data.color)
        })
      } else {
        this.selectedItem[this.item.key].splice(data.index, 1, data.color)
      }
    },
    mygetColorStart (data) {
      this.$parent.$parent.$parent.saveHistory()
      if (!this.selectChange && this.chooseSameFlag) {
        var oldColor = this.selectedItem[this.item.key][data.index]
        oldColor[0] = data.color
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i][this.item.key].splice(data.index, 1, oldColor)
        })
      } else {
        if (data.type !== undefined) {
          this.selectedItem[data.type][0] = data.color
        } else {
          var oldColor = this.selectedItem[this.item.key][data.index]
          oldColor[0] = data.color
          this.selectedItem[this.item.key].splice(data.index, 1, oldColor)
        }
      }
    },
    colorToAll () {
      this.$parent.$parent.$parent.colorToAll(JSON.stringify(this.selectedItem.ScatterColor), JSON.stringify(this.selectedItem.DScatterColor), this.selectedItem.ifGradual)
    },
    myreverseColor (index) {
      if (!this.selectChange && this.chooseSameFlag) {
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i][this.item.key][index].reverse()
        })
      } else {
        this.selectedItem[this.item.key][index].reverse()
      }
    },
    mygetGradColor (data) {
      this.$parent.$parent.$parent.saveHistory()
      if (!this.selectChange && this.chooseSameFlag) {
        var oldColor = this.selectedItem[this.item.key][data.index]
        oldColor[1] = data.color
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i][this.item.key].splice(data.index, 1, oldColor)
        })
      } else {
        if (data.type !== undefined) {
          this.selectedItem[data.type][1] = data.color
        } else {
          var oldColor = this.selectedItem[this.item.key][data.index]
          oldColor[1] = data.color
          this.selectedItem[this.item.key].splice(data.index, 1, oldColor)
        }
      }
    },
    addColor (index) {
      // 添加颜色
      if (this.selectedItem.ifGradual === 'false') {
        if (!this.selectChange && this.chooseSameFlag) {
          this.chooseIndexs.forEach((i) => {
            this.chartNum[i][this.item.key].splice(index, 0, '#c23531')
          })
        } else {
          this.selectedItem[this.item.key].splice(index, 0, '#c23531')
        }
      } else {
        if (!this.selectChange && this.chooseSameFlag) {
          this.chooseIndexs.forEach((i) => {
            this.chartNum[i][this.item.key].splice(index, 0, ['#c23531', '#c23531'])
          })
        } else {
          this.selectedItem[this.item.key].splice(index, 0, ['#c23531', '#c23531'])
        }
      }
    },
    colorchange () {
      var color = this.$refs.MyColorInput.value
      var name = this.$refs.MyColorInput.name
      this.selectedItem[name] = color
    },
    delColor (index) {
      // 删除自定义颜色
      if (this.selectedItem[this.item.key].length === 1) {
        if (gbs.inDev) {
          Notification({
            message: '至少配置一种颜色',
            position: 'bottom-right',
            customClass: 'toast toast-info'
          })
        } else {
          tooltip('', '至少配置一种颜色', 'info')
        }
        return
      }
      if (!this.selectChange && this.chooseSameFlag) {
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i][this.item.key].splice(index, 1)
        })
      } else {
        this.selectedItem[this.item.key].splice(index, 1)
      }
    },
    moveUp (index) {
      if (index !== 0) {
        if (!this.selectChange && this.chooseSameFlag) {
          this.chooseIndexs.forEach((i) => {
            let tempColor = this.chartNum[i][this.item.key].splice(index, 1)[0]
            this.chartNum[i][this.item.key].splice(index - 1, 0, tempColor)
          })
        } else {
          var tempColor = this.selectedItem[this.item.key].splice(index, 1)[0]
          this.selectedItem[this.item.key].splice(index - 1, 0, tempColor)
        }
      }
    },
    moveDown (index) {
      if (index !== this.selectedItem[this.item.key].length - 1) {
        if (!this.selectChange && this.chooseSameFlag) {
          this.chooseIndexs.forEach((i) => {
            let tempColor = this.chartNum[i][this.item.key].splice(index, 1)[0]
            this.chartNum[i][this.item.key].splice(index + 1, 0, tempColor)
          })
        } else {
          var tempColor = this.selectedItem[this.item.key].splice(index, 1)[0]
          this.selectedItem[this.item.key].splice(index + 1, 0, tempColor)
        }
      }
    }
    // {
    //     "name": "背景上传",
    //     "key": "picSrc",
    //     "tag": "ImgFile"
    // },
  }
}
</script>
<style>
.typeSelect{
  height:70px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: space-around;
  position:relative;
  top:-40px;
}
.typeSelect div{
  width:20%;
  height:40px;
  border:1px solid  rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
}
.typeSelect div span{
  font-size: 30px;
}
.hoverShow{
  font-size: 20px;
}
#mainEdit-edit .color-w200 input[type="file"] {
  color: transparent !important;
}
.delete_text{
  cursor: pointer;
}
.hoverLable .hoverShow{
  visibility: hidden;
  position: absolute;
  top: 0px;
  height: 100%;
  width: 100%;
  left: 0px;
  background: transparent;
  justify-content: center;
  align-items: center;
}
.hoverLable:hover .hoverShow{
  visibility: visible;
  position: absolute;
  top: 0px;
  height: 100%;
  width: 100%;
  display: flex;
  left: 0px;
  cursor: pointer;
  background: #00000094;
  justify-content: center;
  align-items: center;
}
.colorsConf{
  font-size: 12px;
}
</style>
