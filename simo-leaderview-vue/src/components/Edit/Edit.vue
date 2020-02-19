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
    <div class="edit-content" @click.ctrl="bindCtrl">
        <div class="edit-header">
            <a class="fr simoLink icon-n-withdraw edit-opt" @click="preBack">返回</a>
            <a class="fr simoLink icon-n-preview edit-opt" @click="preview">预览</a>
            <!-- <button type="button" class="close fr edit-opt" @click="back"></button> -->
            <a class="fr icon-n-save simoLink edit-opt" @click="saveConf">保存</a>
            <span class="fr">|</span>
            <a class="fr simoLink icon-n-keyboard edit-opt" @mouseover="showKeybd = true" @mouseout="showKeybd = false">快捷键</a>
            <h4 class="edit-title" @click.self="clickPaint($event)">{{pageName}}</h4>
            <a class="fr simoLink icon-n-revoke edit-opt" v-if="historyArr.length > 0" @click="Revoke">撤销</a>
            <a class="fr simoLink icon-n-revoke edit-opt" style="color:#666F8B;" v-else>撤销</a>
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
          <div class="keybd-info">
            <span class='fl'>多选元件</span>
            <div class="fr">
               框选<span style="margin: 0 10px;">|</span><span class="keybd">Ctrl</span> + <span class="keybd"><i class="icon-n-mouse"></i></span>
            </div>
          </div>
          <div class="keybd-info">
            <span class='fl'>多元件拖动</span>
            <div class="fr">
               <span class="keybd">Ctrl</span> + <span class="keybd"><i class="icon-n-mouse"></i></span> 拖动
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
                  <div class="paint-bg" :style="{'width': paintObj.width + 'px', 'height': paintObj.height + 'px', 'transform' : 'scale(' + paintObj.scale/100 + ')',  'background-color': paintObj.bgColor}">
                    <div class="paint" :style="paintStyle"></div>
                    <div id="chooseWrap" :class="{gridBg: paintObj.showGrid}" @click.self="clickPaint($event)">
                        <DragBox v-for="(item,index) in chartNum" :index="index" :item="item" :parentW="paintObj.width" :parentH="paintObj.height" :editable="editable" @draged="draged" @selected="selected" @resized="resized" :key="item.id" @context="context" @changeStop="changeStop" @bodyDown="bodyDown" @palyErr="palyErr">
                        </DragBox>
                        <Compose v-for="(list, index1) in combinList" :index="index1" :key="list.id" :list="list" :editable="ceditable" :parentW="paintObj.width" :parentH="paintObj.height" @draged="draged" @resized="resized" @selected="selected" @childSelect="childSelect" @childResize="resized" @context="context" @palyErr="palyErr"></Compose>
                    </div>
                    <!-- 触发框选时覆盖在元件之上的div，这样不会和元件的拖拽事件相冲突 -->
                    <div id="inWrap"></div>
                    <!-- :style="{'width': paintObj.width + 'px', 'height': paintObj.height + 'px'}" -->
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
                    <div class="paintWrap chooseMore" v-show="chooseIndexs.length + chooseCompIndexs.length > 1">
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
                                <input class="w-90" type="number" @change="changeTarget('x')" v-model.lazy="testObj.width">
                                <span @click="testObj.width--" class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                                <span @click="testObj.width++" class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                                <label class="error" v-if="widthVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{widthVali.errorMsg}}</label>
                            </div>
                            <div class="fr" style="position: relative;">
                                <label>高</label>
                                <input class="w-90" type="number" @change="changeTarget('y')" v-model.lazy="testObj.height">
                                <span @click="testObj.height--" class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                                <span @click="testObj.height++" class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                                <label class="error" v-if="heightVali.isShowError" style="right: 8px; margin-top: 5px;">{{heightVali.errorMsg}}</label>
                            </div>
                          </div>
                          <div class="form-group" style="height: 30px;">
                            <div class="fl" style="position: relative;">
                                <label>X</label>
                                <input class="w-90" type="number" @change="changeTarget('x')" v-model.lazy="testObj.x">
                                <span @click="testObj.x--" class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                                <span @click="testObj.x++" class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                                <label class="error" v-if="xVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{xVali.errorMsg}}</label>
                            </div>
                            <div class="fr" style="position: relative;">
                                <label>Y</label>
                                <input class="w-90" type="number" @change="changeTarget('y')" v-model.lazy="testObj.y">
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
                              <div v-if="paintObj.bgImg" @click="delPaintImg" class="chooseBgImg">
                                <i class="icon-n-delete" style="color: #7d8eb9;"></i><br>删除图片
                              </div>
                              <div v-else class="chooseBgImg">
                                <i class="icon-n-exportPicture"></i><br>点击选择图片
                                <input type="file" class="uploadBg" style="height: 120px !important;" accept="image/png, image/jpeg, image/gif, image/jpg,image/svg+xml" @change='changeImg'/>
                              </div>
                              <input type="radio" name="bgType" value='1' :disabled="paintObj.bgImg===''" @click="saveHistory('paint')" v-model="paintObj.bgStyle">等比缩放宽度铺满</input><br>
                              <input type="radio" name="bgType" value='2' :disabled="paintObj.bgImg===''" @click="saveHistory('paint')" v-model="paintObj.bgStyle">等比缩放高度铺满</input><br>
                              <input type="radio" name="bgType" value='3' :disabled="paintObj.bgImg===''" @click="saveHistory('paint')" v-model="paintObj.bgStyle">全屏铺满</input>
                            </div>
                          </div>
                          <div class="form-group">
                            <label>不透明度</label>
                            <div class="fl" style="width: 200px; margin-top: -2px;">
                              <Slider @change="opacityChg" v-model="paintObj.opacity" :min="0" :max="100" :step="1"></Slider>
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
                              <div :class="{'u-switch': true, 'u-switch-on': paintObj.showGrid, 'u-switch-off': !paintObj.showGrid}" @click="gridChg"><div></div></div>
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
                    <div class="m-tabMain full-height flex-1" v-show="chooseSameFlag || (chooseIndexs.length === 1 && chooseCompIndexs.length === 0)">
                        <div v-show="showStyleTab" class="full-height m-style">
                            <div class="e-base" v-show="chooseIndexs.length === 1 && chooseCompIndexs.length === 0">
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
                            </div>

                            <!--表格\文本框配置-->
                            <div v-if="selectedItem.chartType=='table' || selectedItem.chartType=='text' || selectedItem.chartType=='marquee' || selectedItem.chartType=='border' || selectedItem.chartType=='time' || selectedItem.secondType == 'liquidfill'">
                                <div class="m-gap form-group" v-show="selectedItem.secondType!=='liquidfill'">图表样式</div>
                                <div class="form-group cols2" v-if="selectedItem.chartType=='table'">
                                    <label>表头背景色</label>
                                    <div class="color-w200">
                                        <Vcolor :data="selectedItem.hdBgClr" :key="1" type="hdBgClr" @getdata="getColor"></Vcolor>
                                    </div>
                                </div>
                                <div class="form-group cols2" v-if="selectedItem.chartType=='table'">
                                    <label>表头文字颜色</label>
                                    <div class="color-w200">
                                        <Vcolor :data="selectedItem.hdClr" :key="17" type="hdClr" @getdata="getColor"></Vcolor>
                                    </div>
                                </div>
                                <div class="form-group cols2" v-if="selectedItem.chartType=='table'">
                                    <label>表头字号</label>
                                    <select v-model="selectedItem.hdfontSize">
                                        <option v-for="item in defaultFontSize" :key="item">{{item}}</option>
                                    </select>
                                </div>
                                <div class="form-group cols2" v-if="selectedItem.chartType=='border'">
                                    <label>边框类型</label>
                                    <select v-model="selectedItem.borderType" @change="changeBdType">
                                        <option value="simple">简单边框</option>
                                        <option value="stable">内置边框</option>
                                    </select>
                                </div>
                                <div v-if="selectedItem.chartType=='border' && selectedItem.borderType=='stable'">
                                  <label>卡片背景</label><br><br>
                                  <div class="form-group">
                                    <div v-for="(item, index) in settingData.cardCase" :key="index" @click="selectedItem.imgSrc=item.imgSrc" :class="{'fl': true, 'font-case': true, 'card-case': true, 'act': selectedItem.imgSrc===item.imgSrc}">
                                        <img :src="baseUrl + item.mini"/>
                                        <!-- <img :src="'../../assets/cardMini' + index +'.png'"/> -->
                                    </div>
                                  </div>
                                  <label style="display: block; clear: both;">标题栏背景</label><br>
                                  <div class="form-group">
                                    <div v-for="(item, index) in settingData.titleCase" :key="index" @click="selectedItem.imgSrc=item.imgSrc" :class="{'fl': true, 'font-case': true, 'act': selectedItem.imgSrc===item.imgSrc}">
                                        <img :src="baseUrl + item.mini"/>
                                    </div>
                                    <!-- <div class="fl font-case">
                                        <img src='../../assets/titleMini2.png'/>
                                    </div> -->
                                </div>
                                </div>

                                <div class="form-group cols2" v-if="selectedItem.chartType!=='time' && selectedItem.borderType!='stable'">
                                   <div class="form-group cols2" v-if="selectedItem.secondType==='liquidfill'">
                                     <div class="m-gap form-group">图例配置</div>
                                      <label>可见性</label>
                                      <select v-model="selectedItem.ctLegendShow">
                                          <option value="true">显示</option>
                                          <option value="false">隐藏</option>
                                      </select>
                                    </div>
                                    <label>填充色</label>
                                    <select v-model="selectedItem.colorful" style="width: 68px !important; margin-left: 3px;">
                                        <option value="false">单色</option>
                                        <option value="true">渐变</option>
                                    </select>
                                    <!-- <div v-show="selectedItem.colorful !== 'true'" class="color-w200" style="width: 100px;">
                                        <Vcolor :data="selectedItem.barClr" :key="22" type="barClr" @getdata="getColor"></Vcolor>
                                    </div> -->
                                    <div class="barGradient" v-if="selectedItem.colorful === 'true'" @click="reverseClr" :style="{'background': 'linear-gradient(45deg, ' + selectedItem.barClrs[0]  +',' + selectedItem.barClrs[1] + ')'}">
                                      <div class="color-w15">
                                          <Vcolor :data="selectedItem.barClrs[0]" :key="15" :index="0" @getdata="getBarClr"></Vcolor>
                                      </div>
                                      <div class="color-w15" style="float: right">
                                          <Vcolor :data="selectedItem.barClrs[1]" :key="16" :index="1" @getdata="getBarClr"></Vcolor>
                                      </div>
                                    </div>
                                    <div class="color-w200" style="width: 100px;" v-else>
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
                                <div class="form-group cols2" v-if="selectedItem.chartType==='border'">
                                    <label>圆角</label>
                                    <input class="color-w200" type="number" placeholder="圆角最大值为高度的一半" onkeypress='return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )' @change="radiusChange" v-model="borderRadius">
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
                                <div class="form-group cols2" v-show="selectedItem.chartType==='marquee' || selectedItem.thirdType==='moveTable'">
                                    <label>轮播方向</label>
                                    <select v-model="selectedItem.direction">
                                        <option value="left">横向</option>
                                        <option value="top">纵向</option>
                                    </select>
                                </div>
                                <div class="form-group cols2" v-show="selectedItem.chartType==='marquee' || selectedItem.thirdType==='moveTable'">
                                    <label>轮播速度</label>
                                    <select v-model="selectedItem.speed">
                                        <option value="1">高速</option>
                                        <option value="2">中速</option>
                                        <option value="3">低速</option>
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
                            <!-- 图片元件 -->
                            <div v-if="selectedItem.chartType=='image'">
                              <div class="form-group cols2">
                                  <label>缩放方式</label>
                                  <select v-model="selectedItem.showType">
                                    <option value="1">按比例缩放</option>
                                    <option value="2">自由缩放</option>
                                  </select>
                                </div>
                            </div>
                            <!--进度条-->
                            <div v-if="selectedItem.chartType=='progress'">
                                <div class="m-gap form-group">图表样式</div>
                                <div class="form-group cols2">
                                    <label>图例可见性</label>
                                    <select v-model="selectedItem.ctLegendShow">
                                        <option value="true">显示</option>
                                        <option value="false">隐藏</option>
                                    </select>
                                </div>
                                <div class="form-group cols2">
                                    <label>底色</label>
                                    <div class="color-w200">
                                        <Vcolor :data="selectedItem.bgClr" :key="5" type="bgClr" @getdata="getColor"></Vcolor>
                                    </div>
                                    <!-- <input type="color" v-model="selectedItem.bgClr"/> -->
                                </div>
                                <div class="form-group cols2">
                                    <label>进度条色</label>
                                    <select v-model="selectedItem.colorful" style="width: 68px !important; margin-left: 3px;">
                                        <option value="false">单色</option>
                                        <option value="true">渐变</option>
                                    </select>
                                    <div v-show="selectedItem.colorful !== 'true'" class="color-w200" style="width: 100px;">
                                        <Vcolor :data="selectedItem.barClr" :key="6" type="barClr" @getdata="getColor"></Vcolor>
                                    </div>
                                    <div v-show="selectedItem.colorful === 'true'" class="barGradient" @click="reverseClr" :style="{'background': 'linear-gradient(45deg, ' + selectedItem.barClrs[0]  +',' + selectedItem.barClrs[1] + ')'}">
                                      <div class="color-w15">
                                          <Vcolor :data="selectedItem.barClrs[0]" :key="13" :index="0" @getdata="getBarClr"></Vcolor>
                                      </div>
                                      <div class="color-w15" style="float: right">
                                          <Vcolor :data="selectedItem.barClrs[1]" :key="14" :index="1" @getdata="getBarClr"></Vcolor>
                                      </div>
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
                                <div class="m-gap form-group">图表样式</div>
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
                                <div class="form-group cols2">
                                    <label>图表文字颜色</label>
                                    <div class="color-w200">
                                      <Vcolor :data="selectedItem.legendColor" :key="20" type="legendColor" @getdata="getColor"></Vcolor>
                                  </div>
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
                                  <div class="m-gap form-group">图例配置</div>
                                  <div class="form-group cols2">
                                      <label>可见性</label>
                                      <select v-model="selectedItem.ctLegendShow">
                                          <option value="true">显示</option>
                                          <option value="false">隐藏</option>
                                      </select>
                                  </div>
                                  <div class="form-group cols2">
                                      <label>主题</label>
                                      <select v-model="selectedItem.themeType">
                                          <option value="1">深色</option>
                                          <option value="2">浅色</option>
                                      </select>
                                  </div>
                              </div>
                            </div>
                            <div class="e-legend" v-if="selectedItem.chartType=='v-map'">
                              <div>
                                  <div class="m-gap form-group">图例配置</div>
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
                                      <label>主题</label>
                                      <select v-model="selectedItem.themeType">
                                          <option value="1">深色</option>
                                          <option value="2">浅色</option>
                                      </select>
                                  </div>
                                  <div class="form-group cols2">
                                    <label>配色</label>
                                    <select v-model="selectedItem.colorType" @change="chgColorType" :style="{width: (selectedItem.chartType=='ve-histogram' || selectedItem.chartType=='ve-bar') && !selectedItem.subType ? '100px !important' : ''}">
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
                                    <div class="m-gap form-group">图例配置</div>
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
                                    <div v-if="selectedItem.chartType === 've-line' || selectedItem.chartType === 've-bar' || selectedItem.chartType === 've-histogram'">
                                      <div class="form-group cols2">
                                          <label>背景线可见性</label>
                                          <select v-model="selectedItem.splitShow">
                                              <option value="true">显示</option>
                                              <option value="false">隐藏</option>
                                          </select>
                                      </div>
                                      <div class="form-group cols2">
                                          <label>背景线颜色</label>
                                          <div class="color-w200">
                                            <Vcolor :data="selectedItem.splitColor" :key="18" type="splitColor" @getdata="getColor"></Vcolor>
                                        </div>
                                      </div>
                                      <div class="form-group cols2">
                                          <label>线条粗细</label>
                                          <select v-model="selectedItem.splitSize">
                                            <option value="1">1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                          </select>
                                      </div>
                                    </div>
                                </div>
                                <div class="form-group cols2" v-if="selectedItem.chartType==='ve-gauge' && selectedItem.secondType !== 'liquidfill'">
                                  <div class="m-gap form-group">图例配置</div>
                                    <div class="form-group cols2" v-if="selectedItem.subType==='progress'">
                                      <label>可见性</label>
                                      <select v-model="selectedItem.ctLegendShow">
                                        <option value="true">显示</option>
                                        <option value="false">隐藏</option>
                                      </select>
                                    </div>
                                    <div class="form-group cols2" v-if="selectedItem.subType==='progress'">
                                      <label>字号</label>
                                      <select v-model="selectedItem.fontSize">
                                          <option v-for="item in proFontSize" :key="item">{{item}}</option>
                                      </select>
                                    </div>
                                    <label>底色</label>
                                    <div class="color-w200">
                                        <Vcolor :data="selectedItem.bgClr" :key="12" type="bgClr" @getdata="getGaugeCl"></Vcolor>
                                    </div>
                                </div>
                                <div class="form-group cols2">
                                    <label>图表文字颜色</label>
                                    <div class="color-w200">
                                      <Vcolor :data="selectedItem.legendColor" :key="19" type="legendColor" @getdata="getColor"></Vcolor>
                                  </div>
                                </div>
                                <div class="m-gap form-group" v-show="selectedItem.secondType!=='liquidfill'">图表样式</div>
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
                                <div class="form-group cols2" v-show="selectedItem.secondType !== 'liquidfill'">
                                    <label>配色<i class="icon-n-tip" style="font-size: 16px; position: relative; top: 1px; left: 3px;" title="可增加多个配色项，依次对应各项颜色，配色项少于数据组时循环取色"></i></label>
                                    <select v-model="selectedItem.colorType" @change="chgColorType" :style="{width: (selectedItem.chartType=='ve-histogram' || selectedItem.chartType=='ve-bar') && !selectedItem.subType ? '100px !important' : ''}">
                                        <option value="defalut">默认</option>
                                        <option value="custom" v-show="alertLevel">系列</option>
                                    </select>
                                    <select v-model="selectedItem.colorful" v-show="(selectedItem.chartType=='ve-histogram' || selectedItem.chartType=='ve-bar') && !selectedItem.subType" style="width: 70px !important; margin-left: 3px;">
                                        <option value="false">单色</option>
                                        <option value="true">多色</option>
                                    </select>
                                </div>
                                <div class="form-group cols2" v-show="selectedItem.secondType !== 'liquidfill' && selectedItem.colorType === 'custom'">
                                  <label>方式</label>
                                  <select v-model="selectedItem.ifGradual">
                                    <option value="true">渐变</option>
                                    <option value="false">单色</option>
                                  </select>
                                </div>
                                <div v-if="selectedItem.colorType=='custom'">
                                    <div class="form-group colorsConf">
                                        <span>序号</span>
                                        <!-- <span class="color-w70 text">系列</span> -->
                                        <span class="color-w70 text">颜色</span>
                                        <span @click="colorToAll" style="color: #0088cc; cursor: pointer;">应用到已添加元件</span>
                                        <!-- <i class="icon-n-add" @click="addColor"></i> -->
                                    </div>
                                    <div class="form-group colorsConf" v-for="(v,index) in selectedItem.ctColors" :key="index">
                                        <span class="colorOrder">{{index+1}}</span>
                                        <div class="gradient" v-if="selectedItem.ifGradual==='true'" @click="reverseColor(index)" :style="{'background': 'linear-gradient(45deg, ' + selectedItem.ctColors[index][0]  +',' + selectedItem.ctColors[index][1] + ')'}">
                                          <div class="color-w15">
                                              <Vcolor :data="selectedItem.ctColors[index][0]" :index="index" @getdata="getColorStart"></Vcolor>
                                          </div>
                                          <div class="color-w15" style="float: right">
                                              <Vcolor :data="selectedItem.ctColors[index][1]" :index="index" @getdata="getGradColor"></Vcolor>
                                          </div>
                                        </div>
                                        <div v-else>
                                            <div class="color-w200" style="float: left; width: 140px;">
                                              <Vcolor :data="selectedItem.ctColors[index]" type="ctColors" :index="index" @getdata="getSingleColor"></Vcolor>
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
                                        <option value="local">客户端</option>
                                        <option value="system">服务端</option>
                                    </select>
                                </div>
                            </div>
                            <div style="height: 100%;" v-show="(selectedItem.chartType!=='image' && selectedItem.chartType!=='border' && selectedItem.chartType!=='time'&& selectedItem.chartType!=='video')">
                                <div class="form-group cols2">
                                    <label>数据来源</label>
                                    <select @change="chgDataSource" v-model="selectedItem.ctDataSource">
                                      <option value="static">静态数据</option>
                                      <option value="system">系统数据</option>
                                      <!-- v-show="selectedItem.chartType!=='v-map' && selectedItem.chartType!=='v-scatter'"  -->
                                    </select>
                                </div>
                                <div v-show="selectedItem.ctDataSource == 'system'">
                                    <div class="form-group cols2">
                                        <label>选择接口</label>
                                        <select ref="urlSel" v-model="syst.curConf.url" @change="chgUrl">
                                            <option v-for="v in syst.urlSel" :value="v.url" :key="v.key">{{v.name}}</option>
                                        </select>
                                    </div>
                                    <div id="mainSystemConf">
                                        <div class="form-group cols2" v-for="(v,idx) in syst.curUrl" :key="idx">
                                            <label v-if="v.type=='drop-down' || v.type=='multi-select'" class="e-legend">{{v.name}}<i class="icon-n-tip" v-if="v.title" style="font-size: 16px; position: relative; top: 1px; left: 3px;" :title="v.title"></i></label>
                                              <Select2 v-if="v.type=='drop-down' || v.type=='multi-select'" :name="v.key"
                                                      v-model="syst.curConf.params[v.key]" :obj="v" @input="chgSelects(v)">
                                              </Select2>
                                        </div>
                                        <div class="form-group cols2" v-if="selectedItem.ctDataSource == 'system'">
                                    <div class="form-group" style="position: relative">
                                      <label>刷新周期</label>
                                      <input class="color-w200" type="number" placeholder="刷新周期" onkeypress='return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )' v-model="selectedItem.refreshTm">
                                    <label class="error" v-if="freshVali" style="margin-left: 88px;margin-top: 5px;">刷新周期最小值为3</label>
                                    </div>
                                </div>
                                    </div>
                                    <!-- <button @click="getUrlData">请求数据</button>-->
                                </div>
                                <button v-if="showWindowBtn" @click="getWindowData" class="addData" style="display: block; margin-left: 85px; margin-bottom: 20px;">配置资源指标详细</button>
                                <div class="form-group" v-show="selectedItem.ctDataSource != 'system' && selectedItem.chartType != 'v-map' && selectedItem.chartType!=='v-scatter' && selectedItem.chartType != 'text' && selectedItem.chartType != 'marquee'">
                                    <div ref="textareaData" class="confData" v-if="refreshData" contenteditable="true">{{selectedItem.chartData}}</div>
                                </div>
                                <div class="form-group" v-show="selectedItem.ctDataSource != 'system' && (selectedItem.chartType === 'text' || selectedItem.chartType==='marquee')">
                                    <div ref="textarea" class="confData" v-if="refreshData" contenteditable="true">{{selectedItem.ctName}}</div>
                                </div>
                                <div v-show="selectedItem.chartType === 'v-map' || selectedItem.chartType==='v-scatter'">
                                  <div class="form-group cols2">
                                    <label>展示范围</label>
                                    <select v-model="selectedItem.mapLevel" @change="chgMapLevel">
                                        <option value="country">全国地图</option>
                                        <option value="province">省级地图</option>
                                        <option value="city">地市级地图</option>
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
                                  <div class="form-group cols2" v-if="selectedItem.chartType==='v-scatter' && selectedItem.ctDataSource == 'static'">
                                    <label class="e-legend">数据设置<i class="icon-n-tip" style="font-size: 16px; position: relative; top: 1px; left: 3px;" title="对每一个数据点所在的地区设置告警级别"></i></label><button class="addData" @click="addAlertLevel">添加数据点</button>
                                    <!-- <button type="button" class="colorToall" @click="addAlertLevel">添加数据点</button> -->
                                  </div>

                                  <div class="form-group cols2" v-show="selectedItem.chartType!=='v-scatter' && selectedItem.ctDataSource == 'static'">
                                    <label class="e-legend">数据设置<i class="icon-n-tip" style="font-size: 16px; position: relative; top: 1px; left: 3px;" title="设置每个地区的分布数量"></i></label>
                                    <div class="setMapData" style="height: 180px;">
                                      <div class="area-item" v-for="(area, index) in areaArr" :key="index"><span class="area-name">{{area.name}}</span><input class="w-90" autocomplete="off" oninput="value=value.replace(/[^\d]/g,'')" v-model="selectMapData[area.name]" :name="area.name"></div>
                                    </div>
                                  </div>
                                  <div class="form-group cols2" v-show="selectedItem.chartType!=='v-scatter'" style="position: relative;">
                                    <!-- editPieces -->
                                    <div class="levelTips" v-show="levelTipsShow" :style="{'top': (60 + 40*this.levelChangeIndex) + 'px'}">
                                        <i class="icon-n-arrowUp" style="font-size: 30px;"></i>
                                        <div>与其余量级区间重合，是否合并为一个量级?</div>
                                        <span class="tipbtn" @click="sureLevelTips">是</span><span class="tipbtn" @click="cancelLevelTips">否</span>
                                      </div>
                                    <label class="e-legend">数据量级<i class="icon-n-tip" style="font-size: 16px; position: relative; top: 1px; left: 3px;" title="设置数据的区间。分布数量处于不同区间的地区，展示颜色会有差别"></i></label>
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
                                <div class="setMapData" v-if="selectedItem.chartType==='v-scatter' && selectedItem.ctDataSource == 'static'">
                                  <div class="area-item" v-for="(item, index) in alertMapData" :key="index">
                                    <span class="area-index">{{index + 1}}</span>
                                    <Select2 v-model="alertMapData[index].name" :disData="selectedPositn" :key="selectedItem.id + index" :mapSelect="true" :sameName="true" :obj="areaArr" @input="chgAreaName(alertMapData[index].name, index)"></Select2>
                                    <Select2 v-model="alertMapData[index].value" :mapSelect="true" :obj="alertLevels"></Select2>
                                    <i class="icon-n-deleteNew" v-if="alertMapData.length > 1" @click="delAlertLevel(index)"></i>
                                  </div>
                                </div>
                                <div class="form-group cols2" v-show="selectedItem.thirdType==='moveTable'">
                                    <label>每页展示条数</label>
                                    <select v-model="selectedItem.pageNum">
                                      <option v-for="i in 15" :key="'tbNum' + i" :value="i">{{i}}</option>
                                    </select>
                                </div>
                                <button @click="dataChange">更新视图</button>

                            </div>
                            <div style="height: 100%;" v-show="selectedItem.chartType==='video'">
                              <div class="form-group cols2">
                                <label>视频来源</label>
                                <select v-model="selectedItem.videoType">
                                  <option value="url">URL地址</option>
                                  <option value="local">本地文件</option>
                                </select>
                              </div>
                              <div class="form-group cols2" v-show="selectedItem.videoType === 'url'" style="position: relative;">
                                <label>URL地址</label>
                                <input v-model="tempVideoUrl" @focus="showPlayErr = false">
                                <label class="error" v-show="showPlayErr" style="margin-left: 85px; margin-top: 2px;">该地址无效或不允许在本网页播放</label>
                              </div>
                              <div>
                                <div class="form-group cols2" v-show="selectedItem.videoType === 'local'">
                                    <label>选择文件</label>
                                    <input type="file" name="myfiles" id="myfiles" accept="video/mp4" @change="uploadVideo">
                                </div>
                            </div>
                              <button @click="videoChange" style="margin-top: 30px">更新视图</button>
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
    <!-- 多资源多指标弹窗 -->
    <div id="partsEdit-modal"
         class="modal in"
         role="dialog"
         v-if="showWindowBtn"
         aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button"
                    class="close"
                    data-dismiss="modal"
                    aria-hidden="true">&times;</button>
            <h4 class="modal-title">配置资源指标详情</h4>
          </div>
          <div class="modal-body" style="height: 450px; overflow: auto;">
            <form autocomplete="off" style="margin-bottom: 20px;" v-for="(list, i) in syst.windowObj" :key="i">
              <div class="form-group modal-label" style="width: 100%; min-height: 30px; height: auto;">
                <label class="page-lable page-title"><i class="icon-n-arrowRight"></i>指标分类： {{list.indicator.name}}</label>
                <div class="page-lable-content" style="margin-left: 32px;" v-if="list.fields && list.fields.length > 0">
                  <span>指标：</span>
                  <Select2 v-model="syst.windowData[i].fields" :mapSelect="true" :multip="list.indicator.multipleField" :maxLength="5" :obj="list.fields"></Select2>
                </div>
              </div>
              <div class="form-group form-content" v-for="(item, index) in list.ne" :key="index">
                <label class="page-title">资源{{index+1}}: {{item.name}}</label><label class="page-title">资源类型：{{item.neClass}}</label>
                <div class="page-lable-content" v-if="item.component && item.component.length > 0">
                  <span>部件：</span>
                  <Select2 v-model="syst.windowData[i].ne[index].component" :mapSelect="true" :multip="syst.windowData[i].ne[index].multipleComponent" :maxLength="5" :obj="item.component"></Select2>
                </div>
              </div>
            </form>
          </div>
          <!-- <div class="modal-footer">
            <button type="button" data-dismiss="modal">确定</button>
            <button type="button"
                    data-dismiss="modal">取消</button>
          </div> -->
        </div>
      </div>
    </div>
    <!-- 多资源多指标弹窗 -->
  </div>
</template>
<script>
import EditJs from './Edit.js'
import './Edit.scss'
export default EditJs
</script>
<style lang="scss">
</style>
