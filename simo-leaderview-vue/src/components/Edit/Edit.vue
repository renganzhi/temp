<template>
             <!-- :composeData="composeData" -->
            <!-- moniModal edit in nofooter -->
  <div id="mainEdit-edit" class="editPage">
     <!-- style="z-index: 20099" -->
    <PreView :showModal="viewPage"
             :pageData="pageData"
             :key="viewKey"
             :composeData="composeData"
             :paintObj="paintObj"
             @hidePreview="hidePreview"></PreView>
    <Confirm :showModal="showBackModal"
             :message="'确认离开当前页吗？未保存数据将会丢失！'"
             @hideModal="back"></Confirm>
     <!-- class="edit moniModal nofooter" -->
    <!-- <div class="edit-dialog"> -->
        <div class="edit-content" @click.ctrl="bindCtrl">
            <div class="edit-header">
                <a class="fr simoLink icon-n-withdraw edit-opt" @click="preBack">返回</a>
                <a class="fr simoLink icon-n-preview edit-opt" @click="preview">预览</a>
                <!-- <button type="button" class="close fr edit-opt" @click="back"></button> -->
                <a class="fr icon-n-save simoLink edit-opt" @click="saveConf">保存</a>
                <span class="fr">|</span>
                <a class="fr simoLink icon-n-keyboard edit-opt" @mouseover="showKeybd = true" @mouseout="showKeybd = false">快捷键</a>
                <h4 class="edit-title" @click.self="clickPaint($event)">{{pageName}}</h4>
            </div>
            <div class="edit-keyboard" v-show="showKeybd" @mouseover="showKeybd = true" @mouseout="showKeybd = false">
              <span class="keybd-arrow"><i class="icon-n-arrowUp" style="font-size: 40px;"></i></span>
              <div class="keybd-info">
                <span class='fl'>缩放画布</span>
                <div class="fr">
                  <span class="keybd">Ctrl</span> + <span class="keybd">F2/F3</span>
                </div>
              </div>
              <div class="keybd-info">
                <span class='fl'>删除</span>
                <div class="fr">
                  <span class="keybd">Delete</span>
                </div>
              </div>
              <div class="keybd-info">
                <span class='fl'>元件移动</span>
                <div class="fr">
                  <span class="keybd"><i class="icon-n-arrowUp"></i></span>
                  <span class="keybd"><i class="icon-n-arrowDown"></i></span>
                  <span class="keybd"><i class="icon-n-arrowLeft"></i></span>
                  <span class="keybd"><i class="icon-n-arrowRight"></i></span>
                </div>
              </div>
            </div>
            <div class="edit-body flex" @click="hideContext">
              <!--  <div class="m-contain full-height">-->
                    <!--右键-->
                    <ul class="menu-list" style="width: 156px;" ref="contextMenu">
                      <li class="context-menu-item context-menu-visible" v-show="!childResize" @click="copy"><span>复制</span></li>
                      <li class="context-menu-item context-menu-visible" v-show="!childResize" @click="del"><span>删除</span></li>
                      <li v-show="chooseCompIndexs.length === 0 && chooseIndexs.length > 1" class="context-menu-item context-menu-visible" @click="addToCompose"><span>组合</span></li>
                      <li v-show="chooseCompIndexs.length === 1 && chooseIndexs.length === 0" class="context-menu-item context-menu-visible" @click="itemSplit"><span>取消组合</span></li>
                      <!-- <li v-show="chooseCompIndexs.length === 0 && chooseIndexs.length > 1" class="context-menu-item context-menu-visible" @click="addToCompose"><span>组合</span></li>
                      <li v-show="chooseCompIndexs.length === 1 && chooseIndexs.length === 0" class="context-menu-item context-menu-visible" @click="itemSplit"><span>取消组合</span></li> -->
                      <li v-show="(chooseCompIndexs.length === 1 && chooseIndexs.length === 0) || (chooseCompIndexs.length === 0 && chooseIndexs.length === 1)" class="context-menu-item context-menu-visible" @click="upward"><span>上移一层</span></li>
                      <li v-show="(chooseCompIndexs.length === 1 && chooseIndexs.length === 0) || (chooseCompIndexs.length === 0 && chooseIndexs.length === 1)" class="context-menu-item context-menu-visible" @click="downward"><span>下移一层</span></li>
                      <li v-show="(chooseCompIndexs.length === 1 && chooseIndexs.length === 0) || (chooseCompIndexs.length === 0 && chooseIndexs.length === 1)" class="context-menu-item context-menu-visible" @click="toTop"><span>置于顶层</span></li>
                      <li v-show="(chooseCompIndexs.length === 1 && chooseIndexs.length === 0) || (chooseCompIndexs.length === 0 && chooseIndexs.length === 1)" class="context-menu-item context-menu-visible" @click="toBottom"><span>置于底层</span></li>
                    </ul>

                    <div class="m-left content-side flex" @click.self="clickPaint($event)">
                      <div class="cs-item" :key="key" v-for="(value,key) in compsArr" :class="value.imgClass"
                          @click="initChart(value)">
                          {{value.text}}</div>
                    </div>
                    <div class="m-main flex-1 auto" @click.self="clickPaint($event)">
                      <div class="paint-bg" :style="{'width': paintObj.width + 'px', 'height': paintObj.height + 'px', 'transform' : 'scale(' + paintObj.scale/100 + ')'}">
                        <div class="paint" :style="paintStyle"></div>
                        <!-- :style="{'background': paintObj.showGrid ? 'url(\'./../../assets/bg.png\')' : ''}"  -->
                        <div id="chooseWrap" :class="{gridBg: paintObj.showGrid}" @click.self="clickPaint($event)">
                            <DragBox v-for="(item,index) in chartNum" :index="index" :item="item" :parentW="paintObj.width" :parentH="paintObj.height" :editable="editable" @selected="selected" @resized="resized" :key="item.id" @context="context">
                            </DragBox>
                            <Compose v-for="(list, index1) in combinList" :index="index1" :key="list.id" :list="list" :editable="ceditable" :parentW="paintObj.width" :parentH="paintObj.height" @resized="resized" @selected="selected" @childSelect="childSelect" @childResize="resized" @context="context"></Compose>
                        </div>
                      </div>
                    </div>

                    <div class="scaleBox">
                      <span>缩放比例</span>
                      <Slider :min="20" :max="200" v-model="paintObj.scale"></Slider>
                    </div>

                    <div class="m-right full-height flex flex-vertical" :class="{noSlected:!selectedItem.chartType}" >
                        <div class="base-item" v-show="chooseIndexs.length === 1 && chooseCompIndexs.length === 0">
                            <div class="m-tab" :class="{active:showStyleTab}" @click="showStyleTab=true">样式</div>
                            <div class="m-tab" :class="{active:!showStyleTab}" @click="showStyleTab=false">数据</div>
                        </div>
                        <div class="paintWrap chooseMore full-height flex-1" v-show="chooseIndexs.length + chooseCompIndexs.length > 1">
                          <div class="full-height m-style">
                            <div class="e-base">
                              <div class="m-gap form-group set-map">样式</div>
                              <div class="form-group" style="height: 30px;">
                                <!-- <label class="fl" style="line-height: 25px; display: inline-block;">屏幕大小</label> -->
                                <div class="fl" style="position: relative;">
                                    <label>X</label>
                                    <input class="w-90" type="number" @change="changeTarget('x')" v-model="minXItem.x">
                                    <!-- <span @click="minXItem.x--" class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                                    <span @click="minXItem.x++" class="input-arrow"><i class="icon-n-arrowUp"></i></span> -->
                                    <!-- <label class="error" v-if="widthVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{widthVali.errorMsg}}</label> -->
                                </div>
                                <div class="fr" style="position: relative;">
                                    <label>Y</label>
                                    <input class="w-90" type="number" @change="changeTarget('y')" v-model="minXItem.y">
                                    <!-- <span @click="minXItem.y--" class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                                    <span @click="minXItem.y++" class="input-arrow"><i class="icon-n-arrowUp"></i></span> -->
                                    <!-- <label class="error" v-if="heightVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{heightVali.errorMsg}}</label> -->
                                </div>
                              </div>
                              <div class="form-group">
                                <label>对齐方式</label>
                                <div class="fl">
                                  <span @click="alignLeft"><i class="edit-item icon-n-alignLeft"></i></span>
                                  <span @click="textCenter"><i class="edit-item icon-n-textCenter"></i></span>
                                  <span @click="alignRight"><i class="edit-item icon-n-alignRight"></i></span>
                                  <span @click="alignTop"><i class="edit-item icon-n-alignTop"></i></span>
                                  <span @click="alignCenter"><i class="edit-item icon-n-alignCenter"></i></span>
                                  <span @click="alignBottom"><i class="edit-item icon-n-alignBottom"></i></span>
                                </div>
                              </div>
                              <div class="form-group">
                                <label>分布方式</label>
                                <div class="fl">
                                  <span @click="levelDist"><i class="edit-item icon-n-levelDist"></i></span>
                                  <span @click="verticalDist"><i class="edit-item icon-n-verticalDist"></i></span>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>

                        <div class="paintWrap chooseMore full-height flex-1" v-show="chooseIndexs.length === 0 && chooseCompIndexs.length === 1">
                          <div class="full-height m-style">
                            <div class="e-base">
                              <div class="m-gap form-group set-map">样式</div>
                              <div class="form-group" style="height: 30px;">
                                <div class="fl" style="position: relative;">
                                    <label>宽</label>
                                    <input class="w-90" type="number" @change="changeTarget('x')" v-model="testObj.width">
                                    <span @click="testObj.width--" class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                                    <span @click="testObj.width++" class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                                    <label class="error" v-if="widthVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{widthVali.errorMsg}}</label>
                                </div>
                                <div class="fr" style="position: relative;">
                                    <label>高</label>
                                    <input class="w-90" type="number" @change="changeTarget('y')" v-model="testObj.height">
                                    <span @click="testObj.height--" class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                                    <span @click="testObj.height++" class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                                    <label class="error" v-if="heightVali.isShowError" style="right: 8px; margin-top: 5px;">{{heightVali.errorMsg}}</label>
                                </div>
                              </div>
                              <div class="form-group" style="height: 30px;">
                                <div class="fl" style="position: relative;">
                                    <label>X</label>
                                    <input class="w-90" type="number" @change="changeTarget('x')" v-model="testObj.x">
                                    <span @click="testObj.x--" class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                                    <span @click="testObj.x++" class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                                    <label class="error" v-if="xVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{xVali.errorMsg}}</label>
                                </div>
                                <div class="fr" style="position: relative;">
                                    <label>Y</label>
                                    <input class="w-90" type="number" @change="changeTarget('y')" v-model="testObj.y">
                                    <span @click="testObj.y--" class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                                    <span @click="testObj.y++" class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                                    <label class="error" v-if="yVali.isShowError" style="right: 8px; margin-top: 5px;">{{yVali.errorMsg}}</label>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div class="paintWrap full-height flex-1" v-show="chooseIndexs.length + chooseCompIndexs.length < 1">
                          <div class="full-height m-style">
                            <div class="e-base map-base">
                              <div class="m-gap form-group set-map">画布设置</div>
                              <div class="form-group" style="height: 30px;">
                                <label class="fl" style="line-height: 25px; display: inline-block;">屏幕大小</label>
                                <div class="fl" style="position: relative;">
                                    <label>宽</label>
                                    <input class="w-70" type="number" @change="changePaintSize('w')" v-model="paintInput.width">
                                    <span @click="paintInput.width > 500 ? paintInput.width-- : ''" class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                                    <span @click="paintInput.width < 10000 ? paintInput.width++ : ''" class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                                    <!-- <label class="error" v-if="widthVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{widthVali.errorMsg}}</label> -->
                                </div>
                                <div class="fr" style="position: relative;">
                                    <label>高</label>
                                    <input class="w-70" type="number" @change="changePaintSize('h')" v-model="paintInput.height">
                                    <span @click="paintInput.height > 500 ? paintInput.height-- : ''" class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                                    <span @click="paintInput.height < 10000 ? paintInput.height++ : ''" class="input-arrow"><i class="icon-n-arrowUp"></i></span>
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
                                    <input type="file" class="uploadBg" style="height: 120px !important;" accept="image/png, image/jpeg, image/gif, image/jpg,image/svg+xml" @change='changeImg'/>
                                  </div>
                                  <input type="radio" name="bgType" value='1' :disabled="paintObj.bgImg===''" v-model="paintObj.bgStyle">等比缩放宽度铺满</input><br>
                                  <input type="radio" name="bgType" value='2' :disabled="paintObj.bgImg===''" v-model="paintObj.bgStyle">等比缩放高度铺满</input><br>
                                  <input type="radio" name="bgType" value='3' :disabled="paintObj.bgImg===''" v-model="paintObj.bgStyle">全屏铺满</input>
                                </div>
                              </div>
                              <div class="form-group">
                                <label>不透明度</label>
                                <div class="fl" style="width: 200px; margin-top: -2px;">
                                  <Slider v-model="paintObj.opacity" :min="0" :max="100" :step="1"></Slider>
                                </div>
                              </div>
                              <div class="form-group">
                                <label>重置</label>
                                <div class="fl">
                                  <button class="reset btn" @click="resetPaint">恢复默认</button>
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
                                <div class="fl" style="width: 200px; margin-top: -3px;">#mainEdit-edit
                                  <Slider :min="20" :max="200" v-model="paintObj.scale"></Slider>
                                </div>
                              </div> -->
                            </div>
                          </div>
                        </div>
                        <div class="m-tabMain full-height flex-1" v-show="chooseIndexs.length === 1 && chooseCompIndexs.length === 0">
                            <div v-show="showStyleTab" class="full-height m-style">
                                <div class="e-name" v-if="selectedItem.chartType=='text' || selectedItem.chartType=='marquee'">
                                    <div class="form-group">
                                        <input v-model="selectedItem.ctName">
                                    </div>
                                </div>
                                <div class="e-base">
                                    <div class="m-gap form-group">基础属性</div>
                                    <div class="form-group" style="height: 30px;">
                                        <div class="fl" style="position: relative;">
                                            <label>宽</label>
                                            <input class="w-90" type="number" v-model="testObj.width">
                                            <span>px</span>
                                            <span @click="testObj.width++" class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                                            <span @click="testObj.width--" class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                                            <label class="error" v-if="widthVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{widthVali.errorMsg}}</label>
                                        </div>
                                        <div class="fr" style="position: relative;">
                                            <label>高</label>
                                            <input class="w-90" type="number" v-model="testObj.height">
                                            <span>px</span>
                                            <span @click="testObj.height++" class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                                            <span @click="testObj.height--" class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                                            <label class="error" v-if="heightVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{heightVali.errorMsg}}</label>
                                        </div>
                                    </div>
                                    <div class="form-group" style="height: 30px;">
                                        <div class="fl" style="position: relative;">
                                            <label>X</label>
                                            <input class="w-90" type="number" v-model="testObj.x">
                                            <span>px</span>
                                            <span @click="testObj.x++" class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                                            <span @click="testObj.x--" class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                                            <label class="error" v-if="xVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{xVali.errorMsg}}</label>
                                        </div>
                                        <div class="fr" style="position: relative;">
                                            <label>Y</label>
                                            <input class="w-90" type="number" v-model="testObj.y">
                                            <span>px</span>
                                            <span @click="testObj.y++" class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                                            <span @click="testObj.y--" class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                                            <label class="error" v-if="yVali.isShowError" style="right: 8px; margin-top: 5px;">{{yVali.errorMsg}}</label>
                                        </div>
                                    </div>
                                    <div class="form-group cols2" v-if="selectedItem.chartType=='ve-line'">
                                        <label>折线图类型</label>
                                        <select v-model="selectedItem.lineArea">
                                            <option value="false">折线图</option>
                                            <option value="true">区域图</option>
                                        </select>
                                    </div>
                                    <div class="form-group cols2" v-if="selectedItem.chartType=='ve-line'">
                                        <label>是否标点</label>
                                        <select v-model="selectedItem.showPoint">
                                            <option value="false">否</option>
                                            <option value="true">是</option>
                                        </select>
                                    </div>
                                </div>

                                <!--表格\文本框配置-->
                                <div v-if="selectedItem.chartType=='table' || selectedItem.chartType=='text' || selectedItem.chartType=='marquee' || selectedItem.chartType=='border' || selectedItem.chartType=='time'">
                                    <div class="form-group cols2" v-if="selectedItem.chartType=='table'">
                                        <label>表头背景色</label>
                                        <div class="color-w200">
                                            <Vcolor :data="selectedItem.hdBgClr" :key="1" type="hdBgClr" @getdata="getColor"></Vcolor>
                                        </div>
                                    </div>
                                    <div class="form-group cols2" v-if="selectedItem.chartType=='border'">
                                        <label>边框类型</label>
                                        <select v-model="selectedItem.borderType">
                                            <option value="simple">简单边框</option>
                                            <option value="stable">内置边框</option>
                                        </select>
                                    </div>
                                    <div v-if="selectedItem.chartType=='border' && selectedItem.borderType=='stable'">
                                      <label>卡片背景</label><br><br>
                                      <div class="form-group">
                                        <div v-for="(item, index) in settingData.cardCase" :key="index" @click="selectedItem.imgSrc=item.imgSrc" :class="{'fl': true, 'font-case': true, 'card-case': true, 'act': selectedItem.imgSrc===item.imgSrc}">
                                            <img :src="item.mini"/>
                                            <!-- <img :src="'../../assets/cardMini' + index +'.png'"/> -->
                                        </div>
                                      </div>
                                      <label style="display: block; clear: both;">标题栏背景</label><br>
                                      <div class="form-group">
                                        <div v-for="(item, index) in settingData.titleCase" :key="index" @click="selectedItem.imgSrc=item.imgSrc" :class="{'fl': true, 'font-case': true, 'act': selectedItem.imgSrc===item.imgSrc}">
                                            <img :src="item.mini"/>
                                        </div>
                                        <!-- <div class="fl font-case">
                                            <img src='../../assets/titleMini2.png'/>
                                        </div> -->
                                    </div>
                                    </div>

                                    <div class="form-group cols2" v-if="selectedItem.chartType!=='time' && selectedItem.borderType!='stable'">
                                        <label>填充色</label>
                                        <div class="color-w200">
                                            <Vcolor :data="selectedItem.bgClr" :key="2" type="bgClr" @getdata="getColor"></Vcolor>
                                        </div>
                                    </div>
                                    <div class="form-group cols2" v-if="selectedItem.chartType!=='time' && selectedItem.borderType!='stable'">
                                        <label>边框色</label>
                                        <div class="color-w200">
                                            <Vcolor :data="selectedItem.bdClr" :key="3" type="bdClr" @getdata="getColor"></Vcolor>
                                        </div>
                                    </div>
                                    <div class="form-group cols2" v-if="selectedItem.chartType!=='time' && selectedItem.borderType!='stable'">
                                        <label>线宽</label>
                                        <select v-model="selectedItem.bdpx">
                                            <option value="0">{{0}}</option>
                                            <option v-for="item in 10" :key="item" :value="item">{{item}}</option>
                                        </select>
                                    </div>
                                    <div class="form-group cols2" v-if="selectedItem.chartType!=='border'">
                                        <label>字体颜色</label>
                                        <div class="color-w200">
                                            <Vcolor :data="selectedItem.clr" :key="4" type="clr" @getdata="getColor"></Vcolor>
                                        </div>
                                    </div>
                                    <div class="form-group cols2" v-show="selectedItem.chartType!=='border'">
                                        <label>字号</label>
                                        <select v-model="selectedItem.fontSize">
                                            <option v-for="item in defaultFontSize" :key="item">{{item}}</option>
                                        </select>
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
                                    <div class="form-group cols2" style="margin-bottom: 30px">
                                        <label>高度</label>
                                        <input class="color-w200" type="number" placeholder="高度范围为8-24" onkeypress='return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )' @change="changeProHeight" v-model="progressObj.height">
                                        <label class="error" v-show="proHeightErr" style="margin-left: 15px; margin-top: 25px; width: auto;">高度范围为8-24</label>
                                    </div>
                                    <div class="form-group cols2">
                                        <label>圆角</label>
                                        <input class="color-w200" type="number" placeholder="圆角最大值为高度的一半" onkeypress='return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )' @change="changeRadius" v-model="progressObj.radius">
                                        <label class="error" v-show="radiusErr" style="margin-left: 15px; margin-top: 25px; width: auto;">圆角的最大值为{{Math.ceil(this.selectedItem.proHeight / 2)}}</label>
                                    </div>
                                </div>

                                <!--数字翻牌器-->
                                <div v-if="selectedItem.chartType=='doubler' || selectedItem.chartType=='number'">
                                    <div class="form-group cols2" v-if="selectedItem.chartType=='doubler'">
                                        <label>背景色</label>
                                        <div class="color-w200">
                                            <Vcolor :data="selectedItem.bgClr" :key="8" type="bgClr" @getdata="getColor"></Vcolor>
                                        </div>
                                        <!-- <input type="color" v-model="selectedItem.bgClr"/> -->
                                    </div>
                                    <div class="form-group cols2" v-if="selectedItem.chartType=='doubler'">
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
                                    <div class="form-group cols2">
                                        <label>图例可见性</label>
                                        <select v-model="selectedItem.ctLegendShow">
                                            <option value="true">显示</option>
                                            <option value="false">隐藏</option>
                                        </select>
                                    </div>
                                </div>
                                <div v-if="selectedItem.chartType=='number'">
                                  <div class="m-gap form-group">字体样式</div>
                                  <div class="form-group" style="height: 30px;">
                                      <div v-for="(item, index) in settingData.fontFaces" :key="index" @click="selectedItem.fontFamily=item.fontFace" :class="{'fl': true, 'font-case': true, 'act': selectedItem.fontFamily===item.fontFace}" :style="{'font-family': item.fontFace}">
                                          {{item.fontName}}
                                      </div>
                                  </div>
                                </div>

                                <div class="e-legend" v-if="selectedItem.chartType=='v-scatter'">
                                  <div>
                                      <div class="m-gap form-group">图例</div>
                                      <div class="form-group cols2">
                                          <label>可见性</label>
                                          <select v-model="selectedItem.ctLegendShow">
                                              <option value="true">显示</option>
                                              <option value="false">隐藏</option>
                                          </select>
                                      </div>
                                  </div>
                                </div>
                                <div class="e-legend" v-if="selectedItem.chartType=='v-map'">
                                  <div>
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
                                          <select v-model="selectedItem.visualPosition">
                                              <option value="left">底部靠左</option>
                                              <option value="right">底部靠右</option>
                                          </select>
                                      </div>
                                      <div class="form-group cols2">
                                        <label>配色</label>
                                        <select v-model="selectedItem.colorType" @change="chgColorType" :style="{width: (selectedItem.chartType=='ve-histogram' || selectedItem.chartType=='ve-bar') && !selectedItem.subType ? '110px !important' : ''}">
                                            <option value="defalut">默认</option>
                                            <option value="custom" v-show="alertLevel">自定义</option>
                                        </select>
                                    </div>
                                    <div class="colorConf" v-if="selectedItem.colorType=='custom'">
                                        <div class="form-group">
                                            <span>序号</span>
                                            <span class="text">颜色</span>
                                            <span class="options">操作</span>
                                        </div>
                                        <div class="form-group" v-for="(v,index) in selectedItem.ctColors" :key="index">
                                            <span class="colorOrder">{{index+1}}</span>
                                            <div class="color-w70">
                                              <Vcolor :data="selectedItem.ctColors[index]" :index="index" @getdata="getMapColor"></Vcolor>
                                            </div>

                                            <i class="icon-n-toUp" @click="moveUp(index)"></i>
                                            <i class="icon-n-putin" @click="moveDown(index)"></i>
                                        </div>
                                    </div>
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
                                        <select v-model="selectedItem.colorType" @change="chgColorType" :style="{width: (selectedItem.chartType=='ve-histogram' || selectedItem.chartType=='ve-bar') && !selectedItem.subType ? '110px !important' : ''}">
                                            <option value="defalut">默认</option>
                                            <option value="custom" v-show="alertLevel">系列</option>
                                        </select>
                                        <select v-model="selectedItem.colorful" v-show="(selectedItem.chartType=='ve-histogram' || selectedItem.chartType=='ve-bar') && !selectedItem.subType" style="width: 80px !important; float: right;">
                                            <option value="false">单色</option>
                                            <option value="true">多色</option>
                                        </select>
                                    </div>
                                    <div class="colorsConf" v-if="selectedItem.colorType=='custom'">
                                        <div class="form-group">
                                            <span>序号</span>
                                            <!-- <span class="color-w70 text">系列</span> -->
                                            <span class="color-w70 text">颜色</span>
                                            <span @click="colorToAll" style="color: #0088cc; cursor: pointer;">应用到已添加元件</span>
                                            <!-- <i class="icon-n-add" @click="addColor"></i> -->
                                        </div>
                                        <div class="form-group" v-for="(v,index) in selectedItem.ctColors" :key="index">
                                            <span class="colorOrder">{{index+1}}</span>
                                            <div class="gradient" :style="{'background': 'linear-gradient(45deg, ' + selectedItem.ctColors[index][0]  +',' + selectedItem.ctColors[index][1] + ')'}">
                                              <div class="color-w15">
                                                  <Vcolor :data="selectedItem.ctColors[index][0]" :index="index" @getdata="getColorStart"></Vcolor>
                                              </div>
                                              <div class="color-w15" style="float: right">
                                                  <Vcolor :data="selectedItem.ctColors[index][1]" :index="index" @getdata="getGradColor"></Vcolor>
                                              </div>
                                            </div>
                                            <i class="icon-n-add" @click="addColor(index + 1)"></i>
                                            <i class="icon-n-toUp" @click="moveUp(index)"></i>
                                            <i class="icon-n-putin" @click="moveDown(index)"></i>
                                            <i class="icon-n-deleteNew" @click="delColor(index)"></i>
                                        </div>
                                        <!-- <button type="button" class="colorToall" @click="colorToAll">应用到已添加元件</button> -->
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
                                <div v-show="selectedItem.chartType == 'time'">
                                    <div class="form-group cols2">
                                        <label>取值来源</label>
                                        <select v-model="selectedItem.timeSource">
                                            <option value="local">本机</option>
                                            <option value="system">服务器</option>
                                        </select>
                                    </div>
                                </div>
                                <div style="height: 100%;" v-show="(selectedItem.chartType!=='image' && selectedItem.chartType!=='text' && selectedItem.chartType!=='marquee' && selectedItem.chartType!=='border' && selectedItem.chartType!=='time')">
                                    <div class="form-group cols2">
                                        <label>数据来源</label>
                                        <select @change="chgDataSource" v-model="selectedItem.ctDataSource">
                                            <option value="static">静态数据</option>
                                            <option v-show="selectedItem.chartType!=='v-map' && selectedItem.chartType!=='v-scatter'" value="system">系统数据</option>
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
                                    <div class="form-group" v-show="selectedItem.ctDataSource != 'system' && selectedItem.chartType != 'v-map' && selectedItem.chartType!=='v-scatter'">
                                        <div ref="textarea" class="confData" v-if="refreshData" contenteditable="true">{{selectedItem.chartData}}</div>
                                    </div>
                                    <div v-show="selectedItem.chartType === 'v-map' || selectedItem.chartType==='v-scatter'">
                                      <div class="form-group cols2">
                                        <label>展示范围</label>
                                        <select v-model="selectedItem.mapLevel" @change="chgMapLevel">
                                            <option value="country">国家级</option>
                                            <option value="province">省级</option>
                                            <option value="city">地市级</option>
                                        </select>
                                      </div>
                                      <div v-show="selectedItem.mapLevel!=='country'" @click="chgMapLevel" class="form-group cols2">
                                        <label>省/直辖市</label>
                                        <Select2 v-model="selectedItem.provinceCode" :mapSelect="true" :obj="provinceArr" @input="chgProvince(selectedItem.provinceCode)"></Select2>
                                      </div>
                                      <div v-if="selectedItem.mapLevel==='city'" @click="chgMapLevel" class="form-group cols2">
                                        <label>市</label>
                                        <Select2 v-model="selectedItem.cityCode" :mapSelect="true" :obj="cityArr" @input="chgCity(selectedItem.cityCode)"></Select2>
                                      </div>
                                      <div class="form-group cols2" v-if="selectedItem.chartType==='v-scatter'">
                                        <label>数据设置</label><button class="addData" @click="addAlertLevel">添加数据点</button>
                                        <!-- <button type="button" class="colorToall" @click="addAlertLevel">添加数据点</button> -->
                                      </div>
                                      
                                      <div class="form-group cols2" v-show="selectedItem.chartType!=='v-scatter'">
                                        <label>数据设置</label>
                                        <div class="setMapData">
                                          <div class="area-item" v-for="(area, index) in areaArr" :key="index"><span class="area-name">{{area.name}}</span><input class="w-90" type="number" v-model="selectMapData[area.name]" :name="area.name"></div>
                                        </div>
                                      </div>
                                      <div class="form-group cols2" v-show="selectedItem.chartType!=='v-scatter'" style="position: relative;">
                                        <!-- editPieces -->
                                        <div class="levelTips" v-show="levelTipsShow" :style="{'top': (60 + 40*this.levelChangeIndex) + 'px'}">
                                            <i class="icon-n-arrowUp" style="font-size: 30px;"></i>
                                            <div>与其余量级区间重合，是否合并为一个量级?</div>
                                            <span class="tipbtn" @click="sureLevelTips">是</span><span class="tipbtn" @click="cancelLevelTips">否</span>
                                          </div>
                                        <label>数据量级</label>
                                        <!-- <div class="setMapGrad" v-for="(item, index) in selectedItem.piecesData" :key="index">
                                          <span>量级一</span>
                                          <input class="w-90" type="number" @change="changeTarget('x')" v-model="selectedItem.piecesData[index].min"> -
                                          <input class="w-90" type="number" @change="changeTarget('x')" v-model="selectedItem.piecesData[index].max">
                                        </div> -->
                                        <div class="setMapGrad" v-for="(item, index) in editPieces" :key="index">
                                          <span>量级{{index + 1}}</span>
                                          <input class="w-90" type="number" disabled v-model="editPieces[index].min"> -
                                          <input class="w-90" type="number" :disabled="index===(editPieces.length-1)" @change="chgMapGrad(index)" v-model="editPieces[index].max">
                                          <i v-if="index===(editPieces.length-1) && index > 2" class="icon-n-deleteNew" @click="delMapLevel"></i>
                                        </div>
                                        <button type="button" class="colorToall" @click="addMapLevel">添加量级</button>
                                      </div>
                                    </div>
                                    <div class="setMapData" v-if="selectedItem.chartType==='v-scatter'">
                                      <div class="area-item" v-for="(item, index) in alertMapData" :key="index">
                                        <span class="area-index">{{index + 1}}</span>
                                        <Select2 v-model="alertMapData[index].name" :disData="selectedPositn" :key="selectedItem.id + index" :mapSelect="true" :sameName="true" :obj="areaArr" @input="chgAreaName(alertMapData[index].name, index)"></Select2>
                                        <Select2 v-model="alertMapData[index].value" :mapSelect="true" :obj="alertLevels"></Select2>
                                        <i class="icon-n-deleteNew" v-if="alertMapData.length > 1" @click="delAlertLevel(index)"></i>
                                      </div>
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
<style lang="scss">
// @import url('./Edit.scss');
$headHeight: 50px;
#mainEdit-edit {
  /* position: fixed; */
  position: absolute;
  top: 0;
  bottom: 0;
  right: 0;
  left: -4px;
  overflow: hidden;
  /* z-index: 10052 !important;*/
}
#mainEdit-edit .edit-content{
  height: 100%;
}

#mainEdit-edit .edit-header {
  height: $headHeight;
  // height: 38px;
  border-bottom: 1px solid #383f54;
}
#mainEdit-edit .edit-keyboard {
  position: fixed;
  top: 48px;
  right: 108px;
  z-index: 120;
  background: #222739;
  width: 250px;
  box-shadow: 0px 0px 10px rgba(0,0,0,0.3);
  padding: 10px 15px;
  .keybd-arrow {
    color: #222739;
    position: absolute;
    top: -25px;
    left: 105px;
  }
  .keybd-info{
    line-height: 40px;
    clear: both;
    zoom: 1;
  }
  .keybd {
    border: solid 1px #3d445a;
    padding: 2px 5px;
  }
}
// .icon-n-keyboard:hover ~ .edit-keyboard {
//   display: block;
// }
#mainEdit-edit .edit-header span,
#mainEdit-edit .edit-header .simoLink {
  line-height: $headHeight;
  font-size: 14px;
  margin-right: 18px;
}
#mainEdit-edit .edit-header span {
  font-size: 12px;
}

#mainEdit-edit .edit-title {
  line-height: $headHeight;
  text-indent: 10px;
  font-size: 14px;
  // background: #1b2031;
}

#mainEdit-edit .edit-body {
  padding: 0;
  overflow: hidden;
  width: 100%;
  height: calc(100% - 50px);
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
  /* border-left: 2px solid #33394b; */
}
.font-case {
    // width: 45%;
    width: 126px;
    height: 44px;
    line-height: 44px;
    border: solid 1px #3d445a;
    text-align: center;
    cursor: pointer;
    margin-bottom: 10px;
    margin-right: 0px;
    &:nth-child(odd) {
      margin-right: 10px;
    }
    &:hover {
      border: 1px solid #0088cc;
    }
    &.act {
      border: 1px solid #0088cc;
    }
}
.card-case {
  height: 70px;
  padding-top: 10px;
  & img {
    width: 106px;
    height: 50px;
  }
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
    box-shadow: rgba(0, 0, 0, 0.2) 1px 1px 12px;
}
.paint{
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
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
.paintWrap .form-group .btn, .btn:hover {
  // color: #0088cc;
  // border: 1px solid #0088cc;
  // background: transparent;
  margin-top: -5px;
  border-radius: 5px;
  outline: none;
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
  overflow-x: hidden;
}
#mainEdit-edit .chooseMore {
  display: block;
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
}
.scaleBox{
  position: fixed;
  top: -5px;
  z-index: 999;
  width: 200px;
  left: 50%;
  margin-left: -80px;
  .el-slider__runway, .el-slider__bar{
    height: 2px !important;
  }
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
}

.gridBg{
  background: url('./../../assets/bg1.png');
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

.form-group{
  .w-70{
    width: 70px !important;
  }
  .w-90 {
    width: 90px !important;
  }
  .w-90:focus ~ .input-arrow {
    display: inline-block;
  }
  .w-100 {
    width: 100px !important;
  }
  .w-200 {
    width: 200px;
  }
}
.addData {
  margin-left: 116px;
  background: transparent;
  color: #0088cc;
  border: 1px solid #0088cc;
}
.addData:hover {
  background: transparent;
  color: #0088cc;
}
.setMapData {
    max-height: calc(100% - 180px);
    overflow: auto;
    margin-top: 15px;
    margin-bottom: 10px;
  .area-name{
    margin-right: 6px;
    display: inline-block;
    width: 110px;
    text-align: right;
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
  }
  .area-item {
    margin-bottom: 8px;
    // padding-left: 12px;
    .area-index{
      margin-right: 32px;
      display: inline-block;
      width: 15px;
    }
    i {
      position: relative;
      top: 2px;
    }
    input{
      height: 22px !important;
    }
    select, .select2 {
      margin-right: 5px;
      margin-bottom: 5px;
      width: 85px !important;
    }
  }
}
.setMapGrad {
  margin-bottom: 10px;
  span {
    margin-right: 10px;
  }
}
.levelTips {
  position: absolute;
  width: 260px;
  color: #fff;
  background: #cc2a45;
  padding: 10px;
  top: 65px;
  .icon-n-arrowUp {
    color: #cc2a45;
    position: absolute;
    top: -18px;
    right: 52px;
  }
  .tipbtn{
    border: 1px solid #fff;
    float: left;
    margin-top: 10px;
    margin-right: 10px;
    padding: 1px 10px;
    cursor: pointer;
  }
}
.e-base label {
  margin-right: 4px;
  display: inline-block;
  min-width: 14px;
}
label.error {
  white-space: nowrap;
  left: 0px;
  top: 25px;
}

.cols2 {
  label {
    display: inline-block;
    width: 62px;
    line-height: 28px;
  }
  select,
    input {
    width: 195px !important;
  }
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

.input-arrow{
  position: absolute;
  right: 20px;
  color: #7d8eb9;
  cursor: pointer;
  // display: none;
}
.input-arrow:nth-child(odd) {
  bottom: 0px;
}
.input-arrow:nth-child(even) {
  top: 0px;
}
.input-arrow i{
  font-size: 12px !important;
}
.input-arrow:hover {
  color: #ffffff;
}
.map-base .input-arrow,
.chooseMore .input-arrow {
  right: 2px;
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
  float: left;
}
#mainEdit-edit .gradient {
  width: 142px;
  height: 10px;
  float: left;
  margin-left: 10px;
  margin-top: 3px;
}
.color-w200 {
  width: 195px;
  float: right;
  margin-right: 9px;
}
.color-w70 {
  display: inline-block;
  width: 70px;
  margin-left: 5px;
}
.color-w70 input{
  width: 70px;
}
.color-w70.text {
  box-sizing: border-box;
  width: 132px;
  text-align: center;
}
.color-w15 {
    display: inline-block;
    width: 16px;
    height: 16px;
    border: 1px solid #cad6dd;
    overflow: hidden;
    margin-top: -3px;
    padding: 1px;
    background: #1b2031;
}
.colorToall {
    border-radius: 5px;
    width: 90%;
    margin-left: 5%;
    font-size: 14px;
    color: #fff;
    margin-top: 15px;
}
.colorConf {
  .text, .color-w70 {
    margin-left: 30px;
  }
  .options {
    margin-left: 70px;
  }
  .icon-n-toUp {
    margin-left: 30px;
    margin-right: 5px;
  }
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
// #mainEdit-edit.moniModal button.close {
//   font-weight: 400;
//   margin-right: 14px;
// }
#mainEdit-edit.editPage button.close {
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
#mainEdit-edit .edit-opt:before {
  margin-right: 3px;
  font-size: 14px;
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
    position: absolute;
    top: 0;
    left: 0;
    border: none !important;
    opacity: 0;
}
.edit-item {
  color: #62709b;
  cursor: pointer;
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
.m-main::-webkit-scrollbar {
  width: 12px !important;
  height: 12px !important;
}
/* .m-main::-webkit-scrollbar-track, */
.m-main::-webkit-scrollbar-thumb {
  border-radius: 6px !important;
}
/* slider */
.el-slider__runway {
    height: 2px !important;
    border-radius: 2px !important;
    margin: 10px 0 !important;
}
.el-slider__bar {
    height: 2px !important;
    border-top-left-radius: 2px;
    border-bottom-left-radius: 2px;
    background-color: #7d8eb9 !important;
}
.el-slider__button {
    border: 1px solid gray !important;
    background-color: #7d8eb9 !important;
}
.el-slider__button-wrapper {
  width: 30px !important;
  height: 3px !important;
  top: -9px !important;
}
/*颜色选择器*/
#mainEdit-edit .sp-replacer,
#mainEdit-edit .sp-replacer:hover {
    background: transparent;
    border: solid 1px #3d445a;
    color: #3d445a;
}
html[data-theme="blackWhite"],
html[data-theme="blueWhite"] {
  #mainEdit-edit{
    .m-main {
      background: #f6f6f6;
    }
    .edit-header{
    border-bottom: none;
    background: #fff;
    position: relative;
    z-index: 102;
    box-shadow: 0 0 16px rgba(0,0,0,0.1);
    }
    .m-left, .m-right{
      background: #fff;
    }
    .m-tabMain, .paintWrap, .edit-keyboard{
      background: #fff;
    }
    .m-tab {
      background: #f6f6f6;
    }
    .m-right .active {
      background: #fff;
    }
    .edit-keyboard .keybd-arrow {
      color: #fff;
    }
    .edit-keyboard .keybd {
      border: 1px solid #adb9ca !important;
    }
    .chooseBgImg{
      background: #f6f6f6;
    }
    .content-side .cs-item:first-child,
    .content-side .cs-item:nth-child(2) {
      border-top: 1px solid #b1bdcd !important;
    }
    .content-side .cs-item {
      border: 1px solid #b1bdcd !important;
      border-top: none !important;
    }
    .content-side .cs-item:hover {
      border: 1px solid #026bf4 !important;
    }
    .sp-replacer, .sp-replacer:hover {
      border: solid 1px #fff !important;
      color: #666f8b;
    }
    .edit-opt {
      color: #50607c;
    }
    textarea {
      background: transparent !important;
      background-color: transparent !important;
    }
  }
  .colorToall {
    color: #026bf4;
  }
}
html[data-theme="blackWhite"]{
  .m-right .active, .set-map {
    color: #026bfe;
  }
  #mainEdit-edit .menu-list {
    background: #fff;
    color: #50607c;
  }
  .color-w15 {
    background: #fff;
  }
}
html[data-theme="blueWhite"]{
  .m-right .active, .set-map {
    color: #60abff;
  }
   #mainEdit-edit .menu-list {
    background: #fff;
    color: #666666;
  }
}
</style>
