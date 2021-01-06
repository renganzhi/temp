<template>
    <div v-if="itemsShow" :class="item.tag === 'ImgFile'?'bigBox':''">
        <label>{{ item.name }}</label>
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
        <template v-if="item.tag === 'input'">
            <input  :type="item.type || 'number'"
                    v-model="selectedItem[item.key]">
        </template>
        <template v-if="item.tag === 'Hint'">
        </template>
        <template v-if="item.tag === 'Color'">
            <div class="color-w200">
                <Vcolor :data="selectedItem[item.key]"
                        :key="10"
                        :type="[item.key]"
                        @getdata="ChildGetColor"></Vcolor>
            </div>
        </template>
        <template v-if="item.tag === 'ImgFile'">
            <div class="color-w200">
              <label class="hoverLable" for="FileInput" style="width:100%;position: relative;">
                <img v-if="picSrc" style="height:100%;width:100%" :src="baseUrl + picSrc" alt="">
                <!-- <img v-else style="height:100%;width:100%" :src="baseUrl + picSrc" alt=""> -->
                <div  v-else style="text-align: center">
                  <i class="icon-n-nodata" style="font-size: 108px;"></i>
                  <p>抱歉，没有图片可供展示...</p>
                </div>
                <div class="hoverShow">
                  <p>修改图片</p>
                </div>
              </label>
              <input type="file"
                    id="FileInput"
                    style="display:none"
                    accept="image/png, image/jpeg, image/gif, image/jpg,image/svg+xml"
                    @change='changeImg' />
            </div>
        </template>
        <template v-if="item.tag === 'GradientColor'">
            <div class="color-w200" style="margin-top: 8px;">
              <div class="gradient"
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
              <div v-for="(fontitem, index) in settingData.fontFaces"
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
      baseUrl: '',
      settingData: baseData,
      picSrc: ''
    }
  },
  beforeMount () {
    var reg = /^\/api/
    if (!reg.test(this.item.imgSrc)) {
      this.baseUrl = gbs.host
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
    setFontFamily: function (val) {
      this.selectedItem.fontFamily = val
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
      this.$parent.uploadFile('img', formData, function (data) {
        const chartType = _this.selectedItem.chartType
        const curSrc = '/leaderview/home/getImg/' + data.obj.isCustom + '/' + data.obj.id
        _this.$parent.saveHistory()
        _this.picSrc = curSrc
        _this.selectedItem[_this.item.key] = curSrc
      })
      e.target.value = ''
    },
    ChildGetColor (data) {
      this.$parent.getColor(data)
    },
    mygetSingleColor (data) {
      this.$parent.saveHistory()
      if (!this.selectChange && this.chooseSameFlag) {
        this.chooseIndexs.forEach((i) => {
          this.chartNum[i][this.item.key].splice(data.index, 1, data.color)
        })
      } else {
        this.selectedItem[this.item.key].splice(data.index, 1, data.color)
      }
    },
    mygetColorStart (data) {
      this.$parent.saveHistory()
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
      this.$parent.saveHistory()
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
.bigBox{
  width: 100%;
  height: 200px;
  overflow: auto;
}
.hoverShow{
  font-size: 20px;
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
</style>
