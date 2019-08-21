<template>
  <div id="mainEdit-edit" class="edit in moniModal nofooter">
    <PreView :showModal="viewPage"
             :pageData="pageData"
             @hidePreview="hidePreview"></PreView>
     <!-- class="edit moniModal nofooter" -->
    <!-- <div class="edit-dialog"> -->
        <div class="edit-content" @click.ctrl="bindCtrl">
            <div class="edit-header">
              <a class="fr simoLink icon-n-withdraw edit-opt" @click="back">返回</a>
                <a class="fr simoLink icon-n-preview edit-opt" @click="preview">预览</a>
                <!-- <button type="button" class="close fr edit-opt" @click="back"></button> -->
                <a class="fr icon-n-save simoLink edit-opt" @click="saveConf">保存</a>
                <h4 class="edit-title">{{pageName}}</h4>
            </div>
            <div class="edit-body flex" @click="hideContext">
              <!--  <div class="m-contain full-height">-->
                    <!--右键-->
                    <ul class="menu-list" style="width: 156px;" ref="contextMenu">
                      <li class="context-menu-item context-menu-visible" @click="copy"><span>复制</span></li>
                      <li class="context-menu-item context-menu-visible" @click="del"><span>删除</span></li>
                      <li v-show="chooseCompIndexs.length === 0 && chooseIndexs.length > 1" class="context-menu-item context-menu-visible" @click="addToCompose"><span>组合</span></li>
                      <li v-show="chooseCompIndexs.length === 1 && chooseIndexs.length === 0" class="context-menu-item context-menu-visible" @click="itemSplit"><span>取消组合</span></li>
                      <li v-show="(chooseCompIndexs.length === 1 && chooseIndexs.length === 0) || (chooseCompIndexs.length === 0 && chooseIndexs.length === 1)" class="context-menu-item context-menu-visible" @click="upward"><span>上移</span></li>
                      <li v-show="(chooseCompIndexs.length === 1 && chooseIndexs.length === 0) || (chooseCompIndexs.length === 0 && chooseIndexs.length === 1)" class="context-menu-item context-menu-visible" @click="downward"><span>下移</span></li>
                      <li v-show="(chooseCompIndexs.length === 1 && chooseIndexs.length === 0) || (chooseCompIndexs.length === 0 && chooseIndexs.length === 1)" class="context-menu-item context-menu-visible" @click="toTop"><span>置顶</span></li>
                      <li v-show="(chooseCompIndexs.length === 1 && chooseIndexs.length === 0) || (chooseCompIndexs.length === 0 && chooseIndexs.length === 1)" class="context-menu-item context-menu-visible" @click="toBottom"><span>置底</span></li>
                    </ul>

                    <div class="m-left content-side flex">
                      <div class="cs-item" :key="key" v-for="(value,key) in compsArr" :class="value.imgClass"
                          @click="initChart(value)">
                          {{value.text}}</div>
                    </div>
                    <div class="m-main flex-1 auto">
                      <div class="paint-bg" :style="{'width': paintObj.width + 'px', 'height': paintObj.height + 'px', 'transform' : 'scale(' + paintObj.scale/100 + ')'}">
                        <div class="paint" :style="paintStyle"></div>
                        <!-- :style="{'background': paintObj.showGrid ? 'url(\'./../../assets/bg.png\')' : ''}"  -->
                        <div id="chooseWrap" :class="{gridBg: paintObj.showGrid}" @click.self="clickPaint($event)">
                            <DragBox v-for="(item,index) in chartNum" :index="index" :item="item" :editable="editable" @selected="selected" @resized="resized" :key="item.id" @context="context">
                            </DragBox>
                            <!-- <Compose v-for="(list, index1) in combinList" :index="index1" :key="list.id" :list="list" :editable="ceditable" @selected="selected" @context="context"></Compose> -->
                        </div>
                      </div>
                    </div>

                    <div class="scaleBox">
                      <span>缩放比例</span>
                      <Slider :min="20" :max="200" v-model="paintObj.scale"></Slider>
                    </div>

                    <div class="m-right full-height flex flex-vertical" :class="{noSlected:!selectedItem.chartType}" >
                        <div class="base-item">
                            <div class="m-tab" :class="{active:showStyleTab}" @click="showStyleTab=true">样式</div>
                            <div class="m-tab" :class="{active:!showStyleTab}" @click="showStyleTab=false">数据</div>
                        </div>
                        <div class="paintWrap full-height flex-1">
                          <div class="full-height m-style">
                            <div class="e-base">
                              <div class="m-gap form-group set-map">画布设置</div>
                              <div class="form-group" style="height: 30px;">
                                <label class="fl" style="line-height: 25px; display: inline-block;">屏幕大小</label>
                                <div class="fl">
                                    <label>宽</label>
                                    <input class="w-70" type="number" v-model="paintObj.width">
                                    <!-- <label class="error" v-if="widthVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{widthVali.errorMsg}}</label> -->
                                </div>
                                <div class="fr">
                                    <label>高</label>
                                    <input class="w-70" type="number" v-model="paintObj.height">
                                    <!-- <label class="error" v-if="heightVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{heightVali.errorMsg}}</label> -->
                                </div>
                              </div>
                              <div class="form-group">
                                <label>背景色</label>
                                <div class="color-w70">
                                    <Vcolor :data="paintObj.bgColor" :key="11" type="bgClr" @getdata="getPaintCl"></Vcolor>
                                </div>
                              </div>
                              <div class="form-group" style="height: 200px;">
                                <label>背景图片</label>
                                <div class="fl">
                                  <div v-if="paintObj.bgImg" @click="paintObj.bgImg = ''" class="chooseBgImg">
                                    <i class="icon-n-delete" style="color: #7d8eb9;"></i><br>删除图片
                                  </div>
                                  <div v-else class="chooseBgImg">
                                    <i class="icon-n-exportPicture"></i><br>点击选择图片
                                    <input type="file" class="uploadBg" accept="image/png, image/jpeg, image/gif, image/jpg,image/svg+xml" @change='changeImg'/>
                                  </div>
                                  <input type="radio" name="bgType" value='1' v-model="paintObj.bgStyle">等比缩放宽度铺满</input><br>
                                  <input type="radio" name="bgType" value='2' v-model="paintObj.bgStyle">等比缩放高度铺满</input><br>
                                  <input type="radio" name="bgType" value='3' v-model="paintObj.bgStyle">全屏铺满</input>
                                </div>
                              </div>
                              <div class="form-group">
                                <label>透明度</label>
                                <div class="fl" style="width: 200px; margin-top: -3px;">
                                  <Slider v-model="paintObj.opacity" :min="0" :max="1" :step="0.01"></Slider>
                                </div>
                              </div>
                              <div class="form-group">
                                <label>重置</label>
                                <div class="fl">
                                  <button class="reset" @click="resetPaint">恢复默认</button>
                                </div>
                              </div>
                              <div class="form-group">
                                <label>显示网格</label>
                                <div class="fl">
                                  <div :class="{'u-switch': true, 'u-switch-on': paintObj.showGrid, 'u-switch-off': !paintObj.showGrid}" @click="paintObj.showGrid = !paintObj.showGrid"><div></div></div>
                                </div>
                              </div>
                              <!-- <div class="form-group" style="position: fixed; z-index: 9999;">
                                <label>缩放比例</label>
                                <div class="fl" style="width: 200px; margin-top: -3px;">
                                  <Slider :min="20" :max="200" v-model="paintObj.scale"></Slider>
                                </div>
                              </div> -->
                            </div>
                          </div>
                        </div>
                        <div class="m-tabMain full-height flex-1">
                            <div v-show="showStyleTab" class="full-height m-style">
                                <div class="e-name" v-if="selectedItem.chartType=='text' || selectedItem.chartType=='marquee'">
                                    <div class="form-group">
                                        <input v-model="selectedItem.ctName">
                                    </div>
                                </div>
                                <div class="e-base">
                                    <div class="m-gap form-group">基础属性</div>
                                    <div class="form-group" style="height: 30px;">
                                        <div class="fl">
                                            <label>宽</label>
                                            <input class="w-90" type="number" v-model="testObj.width">
                                            <span>px</span>
                                            <label class="error" v-if="widthVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{widthVali.errorMsg}}</label>
                                        </div>
                                        <div class="fr">
                                            <label>高</label>
                                            <input class="w-90" type="number" v-model="testObj.height">
                                            <span>px</span>
                                            <label class="error" v-if="heightVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{heightVali.errorMsg}}</label>
                                        </div>
                                    </div>
                                    <div class="form-group" style="height: 30px;">
                                        <div class="fl">
                                            <label>x</label>
                                            <input class="w-90" type="number" v-model="testObj.x">
                                            <span>px</span>
                                            <label class="error" v-if="xVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{xVali.errorMsg}}</label>
                                        </div>
                                        <div class="fr">
                                            <label>Y</label>
                                            <input class="w-90" type="number" v-model="testObj.y">
                                            <span>px</span>
                                            <label class="error" v-if="yVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{yVali.errorMsg}}</label>
                                        </div>
                                    </div>
                                    <div class="form-group cols2" v-if="selectedItem.chartType=='ve-line'">
                                        <label>折线图类型</label>
                                        <select v-model="selectedItem.lineArea">
                                            <option value="false">折线图</option>
                                            <option value="true">区域图</option>
                                        </select>
                                    </div>
                                    <!-- <div class="form-group cols2" v-if="selectedItem.chartType=='ve-line'">
                                        <label>是否标点</label>
                                        <select v-model="selectedItem.showPoint">
                                            <option value="true">是</option>
                                            <option value="false">否</option>
                                        </select>
                                    </div> -->
                                </div>

                                <!--表格\文本框配置-->
                                <div v-if="selectedItem.chartType=='table' || selectedItem.chartType=='text' || selectedItem.chartType=='marquee' || selectedItem.chartType=='border' || selectedItem.chartType=='time'">
                                    <div class="form-group cols2" v-if="selectedItem.chartType=='table'">
                                        <label>表头背景色</label>
                                        <div class="color-w200">
                                            <Vcolor :data="selectedItem.hdBgClr" :key="1" type="hdBgClr" @getdata="getColor"></Vcolor>
                                        </div>
                                        <!-- <input type="color" v-model="selectedItem.hdBgClr"/> -->
                                    </div>
                                    <div class="form-group cols2" v-if="selectedItem.chartType!=='time'">
                                        <label>填充色</label>
                                        <div class="color-w200">
                                            <Vcolor :data="selectedItem.bgClr" :key="2" type="bgClr" @getdata="getColor"></Vcolor>
                                        <!-- <input type="color" v-model="selectedItem.bgClr"/> -->
                                        </div>
                                    </div>
                                    <div class="form-group cols2" v-if="selectedItem.chartType!=='time'">
                                        <label>边框色</label>
                                        <div class="color-w200">
                                            <Vcolor :data="selectedItem.bdClr" :key="3" type="bdClr" @getdata="getColor"></Vcolor>
                                        </div>
                                        <!-- <input type="color" v-model="selectedItem.bdClr"/> -->
                                    </div>
                                    <div class="form-group cols2" v-if="selectedItem.chartType!=='time'">
                                        <label>线宽</label>
                                        <select v-model="selectedItem.bdpx">
                                            <option value="0">{{0}}</option>
                                            <option v-for="item in 10" :key="item" :value="item">{{item}}</option>
                                        </select>
                                    </div>
                                    <div class="form-group cols2" v-show="selectedItem.chartType!=='border'">
                                        <label>字号</label>
                                        <select v-model="selectedItem.fontSize">
                                            <option v-for="item in defaultFontSize" :key="item">{{item}}</option>
                                        </select>
                                    </div>
                                    <div class="form-group cols2" v-if="selectedItem.chartType!=='border'">
                                        <label>字体颜色</label>
                                        <div class="color-w200">
                                            <Vcolor :data="selectedItem.clr" :key="4" type="clr" @getdata="getColor"></Vcolor>
                                        </div>
                                        <!-- <input type="color" v-model="selectedItem.clr"/> -->
                                    </div>
                                    <div class="form-group cols2" v-if="selectedItem.chartType=='time'">
                                      <label>时间格式</label>
                                      <select v-model="selectedItem.timeType">
                                        <option value="1">时分秒</option>
                                        <option value="2">年月日</option>
                                        <option value="3">年月日+时分</option>
                                        <option value="4">年月日+时分秒</option>
                                      </select>
                                    </div>
                                </div>

                                <!--进度条-->
                                <div v-if="selectedItem.chartType=='progress'">
                                    <div class="form-group cols2">
                                        <label>底色</label>
                                        <div class="color-w200">
                                            <Vcolor :data="selectedItem.bgClr" :key="5" type="bgClr" @getdata="getColor"></Vcolor>
                                        </div>
                                        <!-- <input type="color" v-model="selectedItem.bgClr"/> -->
                                    </div>
                                    <div class="form-group cols2">
                                        <label>进度条色</label>
                                        <div class="color-w200">
                                            <Vcolor :data="selectedItem.barClr" :key="6" type="barClr" @getdata="getColor"></Vcolor>
                                        </div>
                                        <!-- <input type="color" v-model="selectedItem.barClr"/> -->
                                    </div>
                                    <div class="form-group cols2">
                                        <label>字体颜色</label>
                                        <div class="color-w200">
                                            <Vcolor :data="selectedItem.clr" :key="7" type="clr" @getdata="getColor"></Vcolor>
                                        </div>
                                        <!-- <input type="color" v-model="selectedItem.clr"/> -->
                                    </div>
                                    <div class="form-group cols2">
                                        <label>字号</label>
                                        <select v-model="selectedItem.fontSize">
                                            <option v-for="(item, index) in defaultFontSize" :key="index">{{item}}</option>
                                        </select>
                                    </div>
                                </div>

                                <!--数字翻牌器-->
                                <div v-if="selectedItem.chartType=='doubler'">
                                    <div class="form-group cols2">
                                        <label>背景色</label>
                                        <div class="color-w200">
                                            <Vcolor :data="selectedItem.bgClr" :key="8" type="bgClr" @getdata="getColor"></Vcolor>
                                        </div>
                                        <!-- <input type="color" v-model="selectedItem.bgClr"/> -->
                                    </div>
                                    <div class="form-group cols2">
                                        <label>边框色</label>
                                        <div class="color-w200">
                                            <Vcolor :data="selectedItem.bdClr" :key="9" type="bdClr" @getdata="getColor"></Vcolor>
                                        </div>
                                        <!-- <input type="color" v-model="selectedItem.bdClr"/> -->
                                    </div>
                                    <div class="form-group cols2">
                                        <label>字号</label>
                                        <select v-model="selectedItem.fontSize">
                                            <option v-for="item in defaultFontSize" :key="item">{{item}}</option>
                                        </select>
                                    </div>
                                    <div class="form-group cols2">
                                        <label>字体颜色</label>
                                        <div class="color-w200">
                                            <Vcolor :data="selectedItem.clr" :key="10" type="clr" @getdata="getColor"></Vcolor>
                                        </div>
                                        <!-- <input type="color" v-model="selectedItem.clr"/> -->
                                    </div>
                                </div>

                                <!--图例-->
                                <div class="e-legend" v-if="isEcharts">
                                    <div v-show="showLengendConf">
                                        <div class="m-gap form-group">图例</div>
                                        <div class="form-group cols2">
                                            <label>可见性</label>
                                            <select v-model="selectedItem.ctLegendShow">
                                                <option value="true">显示</option>
                                                <option value="false">隐藏</option>
                                            </select>
                                        </div>
                                        <div class="form-group cols2">
                                            <label>位置</label>
                                            <select>
                                                <option>底部居中</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="form-group cols2">
                                        <label>配色<i class="icon-n-tip" style="font-size: 16px; position: relative; top: 1px; left: 3px;" title="可增加多个配色项，依次对应各项颜色，配色项少于数据组时循环取色"></i></label>
                                        <select v-model="selectedItem.colorType" @change="chgColorType">
                                            <option value="defalut">默认</option>
                                            <option value="custom" v-show="alertLevel">系列</option>
                                        </select>
                                    </div>
                                    <div class="colorsConf" v-if="selectedItem.colorType=='custom'">
                                        <div class="form-group">
                                            <span>序号</span>
                                            <span class="color-w70 text">系列</span>
                                            <span class="color-w70 text">渐变色</span>
                                            <i class="icon-n-add" @click="addColor"></i>
                                        </div>
                                        <div class="form-group" v-for="(v,index) in selectedItem.ctColors" :key="index">
                                            <span class="colorOrder">{{index+1}}</span>
                                            <div class="color-w70">
                                                <Vcolor :data="selectedItem.ctColors[index][0]" :index="index" @getdata="getColorStart"></Vcolor>
                                            </div>
                                            <div class="color-w70">
                                                <Vcolor :data="selectedItem.ctColors[index][1]" :index="index" @getdata="getGradColor"></Vcolor>
                                            </div>
                                            <i class="icon-n-up" @click="moveUp(index)"></i>
                                            <i class="icon-n-down" @click="moveDown(index)"></i>
                                            <i class="icon-n-deleteNew" @click="delColor(index)"></i>
                                        </div>
                                    </div>
                                </div>

                                <div v-show="selectedItem.secondType=='symbolBar'">
                                  <div>
                                    <div class="form-group cols2">
                                        <label>图形形状</label>
                                        <input type="file" accept="image/png, image/jpeg, image/gif, image/jpg,image/svg+xml" @change='changeImg'/>
                                    </div>
                                  </div>
                                </div>
                            </div>

                            <!--数据-->
                            <div v-show="!showStyleTab" class="full-height" >
                                <div v-show="selectedItem.chartType == 'image'">
                                    <div class="form-group cols2">
                                        <label>选择文件</label>
                                        <input type="file" accept="image/png, image/jpeg, image/gif, image/jpg,image/svg+xml" @change='changeImg'/>
                                    </div>
                                </div>
                                <div  v-show="(selectedItem.chartType!=='image' && selectedItem.chartType!=='text' && selectedItem.chartType!=='marquee' && selectedItem.chartType!=='border')">
                                    <div class="form-group cols2">
                                        <label>数据来源</label>
                                        <select @change="chgDataSource" v-model="selectedItem.ctDataSource">
                                            <option value="static">静态数据</option>
                                            <option value="system">系统数据</option>
                                        </select>
                                    </div>
                                    <div v-show="selectedItem.ctDataSource == 'system'">
                                        <div class="form-group cols2">
                                            <label>选择接口</label>
                                            <select ref="urlSel" v-model="syst.curConf.url" @change="chgUrl">
                                                <option v-for="v in syst.urlSel" :value="v.url" :key="v.key">{{v.name}}</option>
                                            </select>
                                        </div>
                                        <div id="mainSystemConf" >
                                            <div class="form-group cols2" v-for="(v,idx) in syst.curUrl" :key="idx">
                                                <label>{{v.name}}</label>
                                                 <Select2 v-if="v.type=='drop-down' || v.type=='multi-select'" :name="v.key"
                                                         v-model="syst.curConf.params[v.key]" :obj="v" @input="chgSelects(v)">
                                                </Select2>
                                            </div>
                                        </div>
                                       <!-- <button @click="getUrlData">请求数据</button>-->
                                    </div>
                                    <div class="form-group" v-show="selectedItem.ctDataSource != 'system'">
                                        <div ref="textarea" class="confData" contenteditable="true">{{selectedItem.chartData}}</div>
                                    </div>
                                    <button @click="dataChange">更新视图</button>

                                </div>
                            </div>
                        </div>
                    </div>
               <!-- </div> -->
            </div>
            <!-- <div class="form-group" style="position: fixed; z-index: 9999;">
              <label>缩放比例</label>
              <div class="fl" style="width: 200px; margin-top: -3px;">
                <Slider :min="20" :max="200" v-model="paintObj.scale"></Slider>
              </div>
            </div> -->
        </div>
    <!-- </div> -->
</div>
</template>
<script>
import EditJs from './Edit.js'
export default EditJs

</script>
<style scoped>
#mainEdit-edit {
  /* position: fixed; */
  position: absolute;
  top: 0;
  bottom: 0;
  right: 0;
  left: -4px;
  /* z-index: 10052 !important;*/
}
#mainEdit-edit .edit-content{
  height: 100%;
}

#mainEdit-edit .edit-header {
  /* height: 50px; */
  height: 38px;
  border-bottom: 1px solid #383f54;
}

#mainEdit-edit .edit-header .simoLink {
  line-height: 36px;
  font-size: 14px;
  margin-right: 15px;
}

#mainEdit-edit .edit-title {
  line-height: 36px;
  text-indent: 10px;
  font-size: 14px;
}

#mainEdit-edit .edit-body {
  padding: 0;
  overflow: hidden;
  height: calc(100% - 38px);
}

.m-left {
  height: 100%;
  background: #1b2031;
  border: none;
  z-index: 101;
  width: 210px;
  flex-wrap: wrap;
  padding: 15px 0px 15px 15px;
  overflow: auto;
  align-content: flex-start;
}

.m-right {
  width: 300px;
  background: #1b2031;
  height: 100%;
  z-index: 100;
  border-left: 2px solid #33394b;
}

.m-tab {
  display: inline-block;
  width: 49%;
  height: 36px;
  line-height: 36px;
  text-align: center;
  background: #141929;
}

#mainEdit-edit .m-right .active {
  background: #1b2031;
  color: #0088cc;
}
.paint-bg {
    width: 1920px;
    height: 1080px;
    background-size: 100% 100%;
    transform-origin: top left;
    transform: scale(1);
    overflow: hidden;
    /* width: 100%;
    height: 100%;
    position: relative;
    top: -100%;
    left: 0;
    overflow: scroll; */
    /* background-image: url(http://localhost:9999/mc/home/getImg/false/31); */
}
.paint{
  /* background:#000e15; */
  /* background: #272c3b; */
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  /* background: url(http://localhost:9999/mc/home/getImg/false/31); */
  opacity: 0.7;
}
.m-tabMain, .paintWrap {
  padding: 15px;
  background: #1b2031;
  overflow: auto;
  margin-bottom: 20px;
}
.paintWrap .fl {
  margin-top: 0px;
}
.paintWrap input[type='radio'] {
  margin-right: 26px;
  margin-top: 25px;
}
.paintWrap .form-group{
  clear: both;
  height: 30px;
}
.paintWrap .form-group > label {
  width: 48px;
  float: left;
  margin-right: 10px !important;
}
.paintWrap .form-group .reset, .reset:hover {
  color: #0088cc;
  border: 1px solid #0088cc;
  background: transparent;
  margin-top: -5px;
  border-radius: 5px;
}
#mainEdit-edit .noSlected .m-tabMain {
  display: none;
}

#mainEdit-edit .noSlected .m-tab {
  background: #1b2031 !important;
  font-size: 13px;
}

#mainEdit-edit .paintWrap {
  display: none;
}

#mainEdit-edit .noSlected .paintWrap {
  display: block;
}

#mainEdit-edit .base-item {
  display: block;
}

#mainEdit-edit .noSlected .base-item {
  display: none;
}

.m-main {
  position: relative;
  /* overflow: hidden; */
  /* padding: 10px; */
  /* background: #1b2031; */
}
.scaleBox{
  position: fixed;
  bottom: 10px;
  right: 320px;
  z-index: 999;
  width: 200px;
}
.scaleBox span{
    position: relative;
    top: 20px;
    left: -60px;
}

#chooseWrap {
  /* height: 1080px;
  width: 1920px; */
  width: 100%;
  height: 100%;
  background-repeat: repeat;
  position: relative;
    /* background: url('./../../assets/bg.png'); */
    /* max-height: 1080px;
    max-width: 1920px;
    height: 96%;
    background: #222527;
    position: relative; */
}

.gridBg{
  background: url('./../../assets/bg.png');
}

#mainEdit-edit .content-side .cs-item {
  font-size: 12px;
  width: 90px;
  height: 90px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border: solid 1px #3d445a !important;
  border-top: none !important;
  text-align: center;
  position: relative;
}
#mainEdit-edit .content-side .cs-item:nth-child(2),
#mainEdit-edit .content-side .cs-item:nth-child(1) {
  border-top: solid 1px #3d445a !important;
}
#mainEdit-edit .content-side .cs-item:hover {
  border: 1px solid #0088cc !important;
  z-index: 999;
}
#mainEdit-edit .content-side .cs-item:before {
  font-size: 30px;
  margin-left: -12px;
}
#mainEdit-edit .content-side .cs-item:nth-child(2n) {
  left: -1px;
}
#mainEdit-edit .content-side .cs-item:nth-child(4n),
.content-side .cs-item:nth-child(4n-1) {
  top: -1px;
}
.w-70{
  width: 70px !important;
}
.w-90 {
  width: 90px !important;
}
.w-100 {
  width: 100px !important;
}

.w-200 {
  width: 200px;
}

.e-base label {
  margin-right: 4px;
  display: inline-block;
  min-width: 14px;
}

.cols2 label {
  display: inline-block;
  width: 62px;
}

.cols2 select,
.cols2 input {
  width: 195px !important;
}

#mainEdit-edit .confData {
  width: 270px !important;
  height: 200px !important;
  overflow: auto;
  white-space: pre-wrap;
  border: 1px solid #3d445a;
}

.m-gap {
  color: #fff;
}
.set-map{
  text-align: center;
  color: #0088cc;
  font-weight: bold;
  margin-bottom: 5px;
}

#mainEdit-edit .vdr.active:before {
  content: "";
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  box-sizing: border-box;
  outline: 1px dashed #0088cc;
}
#mainEdit-edit .vdr-stick {
  box-sizing: border-box;
  position: absolute;
  font-size: 1px;
  background: #ffffff;
  border: 1px solid #6c6c6c;
  box-shadow: 0 0 2px #bbb;
  border-radius: 50%;
}
#mainEdit-edit .inactive .vdr-stick {
  display: none;
}

#mainEdit-edit .menu-list {
  display: none;
  position: fixed;
  z-index: 99999999;
  min-width: 13em;
  max-width: 26em;
  padding: 4px 0;
  margin: 0.3em;
  font-family: inherit;
  font-size: inherit;
  list-style-type: none;
  background: #24374b;
  border-radius: 0.2em;
  -webkit-box-shadow: 4px 4px 12px rgba(17, 33, 50, 0.5);
  box-shadow: 4px 4px 12px rgba(17, 33, 50, 0.5);
}
.menu-list li{
  height: 30px;
  line-height: 30px;
  padding: 0 12px;
}
#mainEdit-edit input::-webkit-outer-spin-button,
#mainEdit-edit input::-webkit-inner-spin-button {
  -webkit-appearance: none;
}

#mainEdit-edit input[type="number"] {
  -moz-appearance: textfield;
}

#mainEdit-edit .colorOrder {
  width: 20px;
  text-align: center;
  display: inline-block;
}
.color-w200 {
  width: 195px;
  float: right;
  margin-right: 7px;
}
.color-w70 {
  display: inline-block;
  width: 70px;
  margin-left: 5px;
  /* text-align: center; */
}
.color-w70.text {
  box-sizing: border-box;
  padding-left: 16px;
}
#mainEdit-edit .colorsConf [class^="icon-n-"] {
  margin: 0 1px;
  cursor: pointer;
}
#mainEdit-edit .colorsConf .icon-n-up {
  margin-left: 4px;
}
#mainEdit-edit .colorsConf .icon-n-add {
  float: right;
  margin-right: 2px;
}
#mainEdit-edit .sp-replacer {
  width: 100% !important;
}
.sp-preview {
  width: calc(100% - 20px);
}
.table thead {
  background: none;
}
#mainEdit-edit.moniModal button.close {
  font-weight: 400;
  margin-right: 14px;
}
#mainEdit-edit .edit-opt {
  font-size: 12px !important;
  color: #c2c6d7;
}
#mainEdit-edit .edit-opt:hover {
  color: #0088cc;
}
/* 框选区域样式 */
#bottom {
  position: absolute;
  bottom: 0px;
  width: 100%;
  height: 40px;
  border: 1px solid #000;
  background: #000;
  color: #fff;
}
.tempDiv {
  border: 1px dashed blue;
  background: #5a72f8;
  position: absolute;
  width: 0;
  height: 0;
  /* filter:alpha(opacity: 10); */
  opacity: 0.3;
}

.chooseBgImg {
  width: 170px;
  height: 120px;
  background: #272c3b;
  text-align: center;
  color: #7d8eb9 !important;
  position: relative;
}
.chooseBgImg i {
  font-size: 66px;
  line-height: 1.4;
}
.chooseBgImg .uploadBg {
    width: 170px;
    height: 120px !important;
    position: absolute;
    top: 0;
    left: 0;
    border: none !important;
    opacity: 0;
}
/* slider */
.el-slider__runway {
  height: 40x;
  border-radius: 2px;
  margin: 10px 0;
}
.el-slider__bar {
  height: 40x;
  border-top-left-radius: 2px;
  border-bottom-left-radius: 2px;
  background-color: #7d8eb9 !important;
}
.el-slider__button {
  border: 1px solid gray;
  background-color: #7d8eb9;
}
</style>
