<template>
  <!-- :composeData="composeData" -->
  <!-- moniModal edit in nofooter -->
  <div id="mainEdit-edit"
       class="editPage">
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
    <div class="edit-content"
         @click.ctrl="bindCtrl">
      <div class="edit-header">
        <a class="fr simoLink icon-n-withdraw edit-opt"
           @click="preBack">返回</a>
        <a class="fr simoLink icon-n-preview edit-opt"
           @click="preview">预览</a>
        <!-- <button type="button" class="close fr edit-opt" @click="back"></button> -->
        <a class="fr icon-n-save simoLink edit-opt"
           @click="saveConf">保存</a>
        <span class="fr">|</span>
        <a class="fr simoLink icon-n-keyboard edit-opt"
           @mouseover="showKeybd = true"
           @mouseout="showKeybd = false">快捷键</a>
        <h4 class="edit-title"
            @click.self="clickPaint($event)">{{pageName}}</h4>
        <a class="fr simoLink icon-n-revoke edit-opt"
           v-if="historyArr.length > 0"
           @click="Revoke">撤销</a>
        <a class="fr simoLink icon-n-revoke edit-opt"
           style="color:#666F8B;"
           v-else>撤销</a>
        <span class="fr simoLink edit-opt"
            @click="preOther(1)">下一页</span>
        <span class="fr simoLink edit-opt"
            @click="preOther(0)">上一页</span>
        <span class="fr simoLink edit-opt" v-show="chooseIndexs.length + chooseCompIndexs.length > 1">当前操作元件名称: 组合</span>
        <span class="fr simoLink edit-opt" v-show="selectedItem.ctName">当前操作元件名称: {{selectedItem.ctName || ''}}</span>
      </div>
      <div class="edit-keyboard"
           v-show="showKeybd"
           @mouseover="showKeybd = true"
           @mouseout="showKeybd = false">
        <span class="keybd-arrow"><i class="icon-n-arrowUp"
             style="font-size: 40px;"></i></span>
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
      <div class="edit-body flex"
           @click="hideContext">
        <!--  <div class="m-contain full-height">-->
        <!--右键-->
        <ul class="menu-list"
            style="width: 156px;"
            ref="contextMenu">
          <li class="context-menu-item context-menu-visible"
              v-show="!childResize"
              @click="copy"><span>复制</span></li>
          <li class="context-menu-item context-menu-visible"
              v-show="tempItemArry"
              @click="paste"><span>粘贴</span></li>
          <!-- <li class="context-menu-item context-menu-visible"
              v-show="!childResize"
              @click="paste"><span>粘贴</span></li> -->
          <li class="context-menu-item context-menu-visible"
              v-show="!childResize"
              @click="del"><span>删除</span></li>
          <li v-show="chooseCompIndexs.length === 0 && chooseIndexs.length > 1"
              class="context-menu-item context-menu-visible"
              @click="addToCompose"><span>组合</span></li>
          <li v-show="chooseCompIndexs.length === 1 && chooseIndexs.length === 0"
              class="context-menu-item context-menu-visible"
              @click="itemSplit"><span>取消组合</span></li>
          <!-- <li v-show="chooseCompIndexs.length === 0 && chooseIndexs.length > 1" class="context-menu-item context-menu-visible" @click="addToCompose"><span>组合</span></li>
                  <li v-show="chooseCompIndexs.length === 1 && chooseIndexs.length === 0" class="context-menu-item context-menu-visible" @click="itemSplit"><span>取消组合</span></li> -->
          <li v-show="(chooseCompIndexs.length === 1 && chooseIndexs.length === 0) || (chooseCompIndexs.length === 0 && chooseIndexs.length === 1)"
              class="context-menu-item context-menu-visible"
              @click="upward"><span>上移一层</span></li>
          <li v-show="(chooseCompIndexs.length === 1 && chooseIndexs.length === 0) || (chooseCompIndexs.length === 0 && chooseIndexs.length === 1)"
              class="context-menu-item context-menu-visible"
              @click="downward"><span>下移一层</span></li>
          <li v-show="(chooseCompIndexs.length === 1 && chooseIndexs.length === 0) || (chooseCompIndexs.length === 0 && chooseIndexs.length === 1)"
              class="context-menu-item context-menu-visible"
              @click="toTop"><span>置于顶层</span></li>
          <li v-show="(chooseCompIndexs.length === 1 && chooseIndexs.length === 0) || (chooseCompIndexs.length === 0 && chooseIndexs.length === 1)"
              class="context-menu-item context-menu-visible"
              @click="toBottom"><span>置于底层</span></li>
        </ul>

        <div class="m-left content-side flex"
             @click.self="clickPaint($event)">
          <div class="cs-item"
               :key="key"
               v-for="(value,key) in compsArr"
               :class="value.imgClass"
               @click="initChart(value)">
            {{value.text}}</div>
        </div>
        <div class="m-main flex-1 auto"
             @click.self="clickPaint($event)">
          <div class="paint-bg"
               :style="{'width': paintObj.width + 'px', 'height': paintObj.height + 'px', 'transform' : 'scale(' + paintObj.scale/100 + ')',  'background-color': paintObj.bgColor, 'z-index':500}">
            <div class="paint"
                 :style="paintStyle"></div>
            <div id="chooseWrap">
              <vue-ruler-tool
                ref="rulertool"
                v-model="presetLine"
                :step-length="20"
                :parent="true"
                :parentW="paintObj.width"
                :parentH="paintObj.height"
                :is-scale-revise="true"
                :visible.sync="paintObj.showGrid"
                :scale="paintObj.scale"
                style="height:100%;width100%"
              >
              </vue-ruler-tool>
              <DragBox v-for="(item,index) in chartNum"
                       :index="index"
                       :item="item"
                       :parentW="paintObj.width"
                       :parentH="paintObj.height"
                       :editable="editable"
                       @draged="draged"
                       @selected="selected"
                       @resized="resized"
                       :key="item.id"
                       @context="context"
                       @changeStop="changeStop"
                       @bodyDown="bodyDown"
                       @palyErr="palyErr">
              </DragBox>
              <Compose v-for="(list, index1) in combinList"
                       :index="index1"
                       :key="list.id"
                       :list="list"
                       :editable="ceditable"
                       :parentW="paintObj.width"
                       :parentH="paintObj.height"
                       @draged="draged"
                       @resized="resized"
                       @selected="selected"
                       @childSelect="childSelect"
                       @childResize="resized"
                       @context="context"
                       @palyErr="palyErr"></Compose>
            </div>
            <!-- 触发框选时覆盖在元件之上的div，这样不会和元件的拖拽事件相冲突 -->
            <div id="inWrap"></div>
            <!-- :style="{'width': paintObj.width + 'px', 'height': paintObj.height + 'px'}" -->
          </div>
          <vue-ruler
            ref="ruler"
            v-model="presetLine"
            v-if="paintObj.showGrid"
            :scale="paintObj.scale"
            :parentW="paintObj.width"
            :parentH="paintObj.height"
          >
          </vue-ruler>
        </div>

        <div class="scaleBox">
          <span>缩放比例</span>
          <Slider :min="20"
                  :max="200"
                  v-model="paintObj.scale"></Slider>
        </div>

        <div class="m-right full-height flex flex-vertical"
             :class="{noSlected:!selectedItem.chartType}">
          <div class="base-item"
               v-show="chooseIndexs.length === 1 && chooseCompIndexs.length === 0">
            <div class="m-tab"
                 :class="{active:showStyleTab}"
                 @click="showStyleTab=true">样式</div>
            <div class="m-tab"
                 :class="{active:!showStyleTab}"
                 @click="showStyleTab=false">数据</div>
          </div>
          <div class="paintWrap chooseMore"
               v-show="chooseIndexs.length + chooseCompIndexs.length > 1">
            <div class="full-height m-style">
              <div class="e-base">
                <div class="m-gap form-group set-map">样式</div>
                <div class="form-group"
                     style="height: 30px;">
                  <!-- <label class="fl" style="line-height: 25px; display: inline-block;">屏幕大小</label> -->
                  <div class="fl"
                       style="position: relative;">
                    <label>X</label>
                    <input class="w-90"
                           type="number"
                           @change="changeTarget('x')"
                           v-model="minXItem.x">
                    <!-- <span @click="minXItem.x--" class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                                <span @click="minXItem.x++" class="input-arrow"><i class="icon-n-arrowUp"></i></span> -->
                    <!-- <label class="error" v-if="widthVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{widthVali.errorMsg}}</label> -->
                  </div>
                  <div class="fr"
                       style="position: relative;">
                    <label>Y</label>
                    <input class="w-90"
                           type="number"
                           @change="changeTarget('y')"
                           v-model="minXItem.y">
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

          <div class="paintWrap chooseMore full-height flex-1"
               v-show="chooseIndexs.length === 0 && chooseCompIndexs.length === 1">
            <div class="full-height m-style">
              <div class="e-base">
                <div class="m-gap form-group set-map">样式</div>
                <div class="form-group"
                     style="height: 30px;">
                  <div class="fl"
                       style="position: relative;">
                    <label>宽</label>
                    <input class="w-90"
                           type="number"
                           @change="changeTarget('x')"
                           v-model.lazy="testObj.width">
                    <span @click="testObj.width--"
                          class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                    <span @click="testObj.width++"
                          class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                    <label class="error"
                           v-if="widthVali.isShowError"
                           style="margin-left: 22px;margin-top: 5px;">{{widthVali.errorMsg}}</label>
                  </div>
                  <div class="fr"
                       style="position: relative;">
                    <label>高</label>
                    <input class="w-90"
                           type="number"
                           @change="changeTarget('y')"
                           v-model.lazy="testObj.height">
                    <span @click="testObj.height--"
                          class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                    <span @click="testObj.height++"
                          class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                    <label class="error"
                           v-if="heightVali.isShowError"
                           style="right: 8px; margin-top: 5px;">{{heightVali.errorMsg}}</label>
                  </div>
                </div>
                <div class="form-group"
                     style="height: 30px;">
                  <div class="fl"
                       style="position: relative;">
                    <label>X</label>
                    <input class="w-90"
                           type="number"
                           @change="changeTarget('x')"
                           v-model.lazy="testObj.x">
                    <span @click="testObj.x--"
                          class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                    <span @click="testObj.x++"
                          class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                    <label class="error"
                           v-if="xVali.isShowError"
                           style="margin-left: 22px;margin-top: 5px;">{{xVali.errorMsg}}</label>
                  </div>
                  <div class="fr"
                       style="position: relative;">
                    <label>Y</label>
                    <input class="w-90"
                           type="number"
                           @change="changeTarget('y')"
                           v-model.lazy="testObj.y">
                    <span @click="testObj.y--"
                          class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                    <span @click="testObj.y++"
                          class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                    <label class="error"
                           v-if="yVali.isShowError"
                           style="right: 8px; margin-top: 5px;">{{yVali.errorMsg}}</label>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="paintWrap full-height flex-1"
               v-show="chooseIndexs.length + chooseCompIndexs.length < 1">
            <div class="full-height m-style">
              <div class="e-base map-base">
                <div class="m-gap form-group set-map">画布设置</div>
                <div class="form-group"
                     style="height: 30px;">
                  <label class="fl"
                         style="line-height: 25px; display: inline-block;">屏幕大小</label>
                  <div class="fl"
                       style="position: relative;">
                    <label>宽</label>
                    <input class="w-70"
                           type="number"
                           @change="changePaintSize('w')"
                           v-model="paintInput.width">
                    <span @click="paintInput.width > 500 ? paintInput.width-- : ''"
                          class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                    <span @click="paintInput.width < 10000 ? paintInput.width++ : ''"
                          class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                    <!-- <label class="error" v-if="widthVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{widthVali.errorMsg}}</label> -->
                  </div>
                  <div class="fr"
                       style="position: relative;">
                    <label>高</label>
                    <input class="w-70"
                           type="number"
                           @change="changePaintSize('h')"
                           v-model="paintInput.height">
                    <span @click="paintInput.height > 500 ? paintInput.height-- : ''"
                          class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                    <span @click="paintInput.height < 10000 ? paintInput.height++ : ''"
                          class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                    <!-- <label class="error" v-if="heightVali.isShowError" style="margin-left: 22px;margin-top: 5px;">{{heightVali.errorMsg}}</label> -->
                  </div>
                </div>
                <div class="form-group">
                  <label>背景色</label>
                  <div class="color-w70">
                    <Vcolor :data="paintObj.bgColor"
                            :key="11"
                            type="bgClr"
                            @getdata="getPaintCl"></Vcolor>
                  </div>
                </div>
                <div class="form-group"
                     style="height: 200px;">
                  <label>背景图片</label>
                  <div class="fl">
                    <div v-if="paintObj.bgImg"
                         @click="delPaintImg"
                         class="chooseBgImg">
                      <i class="icon-n-delete"
                         style="color: #7d8eb9;"></i><br>删除图片
                    </div>
                    <div v-else
                         class="chooseBgImg">
                      <i class="icon-n-exportPicture"></i><br>点击选择图片
                      <input type="file"
                             class="uploadBg"
                             style="height: 120px !important;"
                             accept="image/png, image/jpeg, image/gif, image/jpg,image/svg+xml"
                             @change='changeImg' />
                    </div>
                    <input type="radio"
                           name="bgType"
                           value='1'
                           :disabled="paintObj.bgImg===''"
                           @click="saveHistory('paint')"
                           v-model="paintObj.bgStyle">等比缩放宽度铺满</input><br>
                    <input type="radio"
                           name="bgType"
                           value='2'
                           :disabled="paintObj.bgImg===''"
                           @click="saveHistory('paint')"
                           v-model="paintObj.bgStyle">等比缩放高度铺满</input><br>
                    <input type="radio"
                           name="bgType"
                           value='3'
                           :disabled="paintObj.bgImg===''"
                           @click="saveHistory('paint')"
                           v-model="paintObj.bgStyle">全屏铺满</input>
                  </div>
                </div>
                <div class="form-group">
                  <label>不透明度</label>
                  <div class="fl"
                       style="width: 200px; margin-top: -2px;">
                    <Slider @change="opacityChg"
                            v-model="paintObj.opacity"
                            :min="0"
                            :max="100"
                            :step="1"></Slider>
                  </div>
                </div>
                <div class="form-group">
                  <label>重置</label>
                  <div class="fl">
                    <button class="reset btn"
                            @click="resetPaint">恢复默认</button>
                  </div>
                </div>
                <div class="form-group">
                  <label>辅助线</label>
                  <div class="fl">
                    <div :class="{'u-switch': true, 'u-switch-on': paintObj.showGrid, 'u-switch-off': !paintObj.showGrid}"
                         @click="gridChg">
                      <div></div>
                    </div>
                  </div>
                </div>
                <!-- <div class="form-group">
                  <label>辅助线</label>
                  <div class="fl">
                    <button class="reset btn"
                            @click="resetLine">清空辅助线</button>
                  </div>
                </div> -->
                <div class="form-group">
                  <label :class="lineBgColor" style="width:60px">辅助线颜色</label>
                  <div class="color-w70">
                    <Vcolor :data="lineBgColor"
                            :key="11"
                            type="lineBgColor"
                            @getdata="getLineCl"></Vcolor>
                  </div>
                </div>

                <!-- <div class="form-group cols2">
                  <label>辅助线位置</label>
                  <select v-model="guideStation" @change="changeStation">
                      <option value="absolute">至于组件下方</option>
                      <option value="static">至于组件上方</option>
                  </select>
                </div> -->
                <!-- <div class="form-group" style="position: fixed; z-index: 9999;">
                            <label>缩放比例</label>
                            <div class="fl" style="width: 200px; margin-top: -3px;">#mainEdit-edit
                              <Slider :min="20" :max="200" v-model="paintObj.scale"></Slider>
                            </div>
                          </div> -->
              </div>
            </div>
          </div>
          <div class="m-tabMain full-height flex-1"
               v-show="chooseSameFlag || (chooseIndexs.length === 1 && chooseCompIndexs.length === 0)">
            <div v-show="showStyleTab"
                 class="full-height m-style">
              <div class="e-base"
                   v-show="chooseIndexs.length === 1 && chooseCompIndexs.length === 0">
                <div class="m-gap form-group">基础属性</div>
                <div class="form-group"
                     style="height: 30px;">
                  <div class="fl"
                       style="position: relative;">
                    <label>宽</label>
                    <input class="w-90"
                           type="number"
                           v-model="testObj.width">
                    <span>px</span>
                    <span @click="testObj.width++"
                          class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                    <span @click="testObj.width--"
                          class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                    <label class="error"
                           v-if="widthVali.isShowError"
                           style="margin-left: 22px;margin-top: 5px;">{{widthVali.errorMsg}}</label>
                  </div>
                  <div class="fr"
                       style="position: relative;">
                    <label>高</label>
                    <input class="w-90"
                           type="number"
                           v-model="testObj.height">
                    <span>px</span>
                    <span @click="testObj.height++"
                          class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                    <span @click="testObj.height--"
                          class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                    <label class="error"
                           v-if="heightVali.isShowError"
                           style="margin-left: 22px;margin-top: 5px;">{{heightVali.errorMsg}}</label>
                  </div>
                </div>
                <div class="form-group"
                     style="height: 30px;">
                  <div class="fl"
                       style="position: relative;">
                    <label>X</label>
                    <input class="w-90"
                           type="number"
                           v-model="testObj.x">
                    <span>px</span>
                    <span @click="testObj.x++"
                          class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                    <span @click="testObj.x--"
                          class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                    <label class="error"
                           v-if="xVali.isShowError"
                           style="margin-left: 22px;margin-top: 5px;">{{xVali.errorMsg}}</label>
                  </div>
                  <div class="fr"
                       style="position: relative;">
                    <label>Y</label>
                    <input class="w-90"
                           type="number"
                           v-model="testObj.y">
                    <span>px</span>
                    <span @click="testObj.y++"
                          class="input-arrow"><i class="icon-n-arrowUp"></i></span>
                    <span @click="testObj.y--"
                          class="input-arrow"><i class="icon-n-arrowDown"></i></span>
                    <label class="error"
                           v-if="yVali.isShowError"
                           style="right: 8px; margin-top: 5px;">{{yVali.errorMsg}}</label>
                  </div>
                </div>
              </div>

              <!-- 地图拓扑背景色 -->
              <div v-if="selectedItem.chartType=='topo' && selectedItem.tptype === 'maptp'">
                <div class="m-gap form-group"
                     >图表样式</div>
                <div class="form-group cols2">
                  <label>地图背景色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.cityColor"
                            :key="26"
                            type="cityColor"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
              </div>
              <!--表格\文本框配置-->
              <div v-if="selectedItem.chartType=='table' || selectedItem.chartType=='text' || selectedItem.chartType=='marquee' || selectedItem.chartType=='border' || selectedItem.chartType=='time'">
                <div class="m-gap form-group">图表样式</div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='table'">
                  <label>表头背景色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.hdBgClr"
                            :key="1"
                            type="hdBgClr"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='table'">
                  <label>表头文字颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.hdClr"
                            :key="17"
                            type="hdClr"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='table'">
                  <label>表头字号</label>
                  <select v-model="selectedItem.hdfontSize">
                    <option v-for="item in defaultFontSize"
                            :key="item">{{item}}</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='border'">
                  <label>边框类型</label>
                  <select v-model="selectedItem.borderType"
                          @change="changeBdType">
                    <option value="simple">简单边框</option>
                    <option value="stable">内置边框</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-show="selectedItem.borderType === 'stable'">
                  <label>缩放方式</label>
                  <select v-model="selectedItem.showType">
                    <option value="1">按比例缩放</option>
                    <option value="2">自由缩放</option>
                  </select>
                </div>
                <div v-if="selectedItem.chartType=='border' && selectedItem.borderType=='stable'">
                  <label>卡片背景</label><br><br>
                  <div class="form-group">
                    <div v-for="(item, index) in settingData.cardCase"
                         :key="index"
                         @click="chgImgSrc(item.imgSrc)"
                         :class="{'fl': true, 'font-case': true, 'card-case': true, 'act': selectedItem.imgSrc===item.imgSrc}">
                      <img :src="baseUrl + item.mini" />
                    </div>
                  </div>
                  <label style="display: block; clear: both;">标题栏背景</label><br>
                  <div class="form-group">
                    <div v-for="(item, index) in settingData.titleCase"
                         :key="index"
                         @click="chgImgSrc(item.imgSrc)"
                         :class="{'fl': true, 'font-case': true, 'act': selectedItem.imgSrc===item.imgSrc}">
                      <img :src="baseUrl + item.mini" />
                    </div>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType!=='time' && selectedItem.borderType!='stable'">
                  <div class="form-group cols2"
                       v-if="selectedItem.chartType==='border'">
                    <label>填充色</label>
                    <select v-model="selectedItem.colorful"
                            style="width: 68px !important; margin-left: 3px;">
                      <option value="false">单色</option>
                      <option value="true">渐变</option>
                    </select>
                    <!-- <div v-show="selectedItem.colorful !== 'true'" class="color-w200" style="width: 100px;">
                                        <Vcolor :data="selectedItem.barClr" :key="22" type="barClr" @getdata="getColor"></Vcolor>
                                    </div> -->
                    <div class="barGradient"
                         v-if="selectedItem.colorful === 'true'"
                         @click="reverseClr"
                         :style="{'background': 'linear-gradient(45deg, ' + selectedItem.barClrs[0]  +',' + selectedItem.barClrs[1] + ')'}">
                      <div class="color-w15">
                        <Vcolor :data="selectedItem.barClrs[0]"
                                :key="15"
                                :index="0"
                                @getdata="getBarClr"></Vcolor>
                      </div>
                      <div class="color-w15"
                           style="float: right">
                        <Vcolor :data="selectedItem.barClrs[1]"
                                :key="16"
                                :index="1"
                                @getdata="getBarClr"></Vcolor>
                      </div>
                    </div>
                    <div class="color-w200"
                         style="width: 100px;"
                         v-else>
                      <Vcolor :data="selectedItem.bgClr"
                              :key="2"
                              type="bgClr"
                              @getdata="getColor"></Vcolor>
                    </div>
                  </div>
                  <div class="form-group cols2"
                       v-else>
                    <label>填充色</label>
                    <div class="color-w200">
                      <Vcolor :data="selectedItem.bgClr"
                              :key="22"
                              type="bgClr"
                              @getdata="getColor"></Vcolor>
                    </div>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType!=='time' && selectedItem.borderType!='stable'">
                  <label>边框色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.bdClr"
                            :key="3"
                            type="bdClr"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType!=='time' && selectedItem.borderType!='stable'">
                  <label>线宽</label>
                  <select v-model="selectedItem.bdpx">
                    <option value="0">{{0}}</option>
                    <option v-for="item in 10"
                            :key="item"
                            :value="item">{{item}}</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType==='border' && selectedItem.borderType === 'simple'">
                  <label>圆角</label>
                  <input class="color-w200"
                         type="number"
                         placeholder="圆角最大值为高度的一半"
                         onkeypress='return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )'
                         @change="radiusChange"
                         v-model="borderRadius">
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType!=='border'">
                  <label>字体颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.clr"
                            :key="4"
                            type="clr"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-show="selectedItem.chartType!=='border'">
                  <label>字号</label>
                  <select v-model="selectedItem.fontSize">
                    <option v-for="item in defaultFontSize"
                            :key="item">{{item}}</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-show="selectedItem.chartType==='text'">
                  <label>字体粗细</label>
                  <select v-model="selectedItem.fontWeight">
                    <option v-for="item in fontWeights"
                            :key="item">{{item}}</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-show="selectedItem.chartType==='marquee' || selectedItem.thirdType==='moveTable'">
                  <label>轮播方向</label>
                  <select v-model="selectedItem.direction">
                    <option value="left">横向</option>
                    <option value="top">纵向</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-show="selectedItem.chartType==='marquee' || selectedItem.thirdType==='moveTable'">
                  <label>轮播速度</label>
                  <select v-model="selectedItem.speed">
                    <option value="1">高速</option>
                    <option value="2">中速</option>
                    <option value="3">低速</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='time'">
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
              <div v-if="['image', 'decorator'].includes(selectedItem.chartType)">
                <div class="m-gap form-group">图表样式</div>
                <div class="form-group cols2">
                  <label>缩放方式</label>
                  <select v-model="selectedItem.showType">
                    <option value="1">按比例缩放</option>
                    <option value="2">自由缩放</option>
                  </select>
                </div>
              </div>
              <div class="form-group" v-if="selectedItem.chartType=='decorator'">
                    <div v-for="(item, index) in settingData.decoratorCase"
                         :key="index"
                         @click="chgImgSrc(item.imgSrc)"
                         :class="{'fl': true, 'font-case': true, 'act': selectedItem.imgSrc===item.imgSrc}">
                      <img :src="baseUrl + item.mini" />
                    </div>
                  </div>
              <!--进度条-->
              <div v-if="selectedItem.chartType=='progress'">
                <div class="m-gap form-group">图例配置</div>
                <div class="form-group cols2">
                  <label>图例可见性</label>
                  <select v-model="selectedItem.ctLegendShow">
                    <option value="true">显示</option>
                    <option value="false">隐藏</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>字体颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.clr"
                            :key="7"
                            type="clr"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2">
                  <label>字号</label>
                  <select v-model="selectedItem.fontSize">
                    <option v-for="(item, index) in defaultFontSize"
                            :key="index">{{item}}</option>
                  </select>
                </div>
                <div class="m-gap form-group">图表样式</div>
                <div class="form-group cols2">
                  <label>底色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.bgClr"
                            :key="5"
                            type="bgClr"
                            @getdata="getColor"></Vcolor>
                  </div>
                  <!-- <input type="color" v-model="selectedItem.bgClr"/> -->
                </div>
                <div class="form-group cols2">
                  <label>进度条色</label>
                  <select v-model="selectedItem.colorful"
                          style="width: 68px !important; margin-left: 3px;">
                    <option value="false">单色</option>
                    <option value="true">渐变</option>
                  </select>
                  <div v-show="selectedItem.colorful !== 'true'"
                       class="color-w200"
                       style="width: 100px;">
                    <Vcolor :data="selectedItem.barClr"
                            :key="6"
                            type="barClr"
                            @getdata="getColor"></Vcolor>
                  </div>
                  <div v-show="selectedItem.colorful === 'true'"
                       class="barGradient"
                       @click="reverseClr"
                       :style="{'background': 'linear-gradient(45deg, ' + selectedItem.barClrs[0]  +',' + selectedItem.barClrs[1] + ')'}">
                    <div class="color-w15">
                      <Vcolor :data="selectedItem.barClrs[0]"
                              :key="13"
                              :index="0"
                              @getdata="getBarClr"></Vcolor>
                    </div>
                    <div class="color-w15"
                         style="float: right">
                      <Vcolor :data="selectedItem.barClrs[1]"
                              :key="14"
                              :index="1"
                              @getdata="getBarClr"></Vcolor>
                    </div>
                  </div>
                  <!-- <input type="color" v-model="selectedItem.barClr"/> -->
                </div>
                <div class="form-group cols2"
                     style="margin-bottom: 30px">
                  <label>高度</label>
                  <input class="color-w200"
                         type="number"
                         placeholder="高度范围为8-24"
                         onkeypress='return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )'
                         @change="changeProHeight"
                         v-model="progressObj.height">
                  <label class="error"
                         v-show="proHeightErr"
                         style="margin-left: 15px; margin-top: 25px; width: auto;">高度范围为8-24</label>
                </div>
                <div class="form-group cols2">
                  <label>圆角</label>
                  <input class="color-w200"
                         type="number"
                         placeholder="圆角最大值为高度的一半"
                         onkeypress='return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )'
                         @change="changeRadius"
                         v-model="progressObj.radius">
                  <label class="error"
                         v-show="radiusErr"
                         style="margin-left: 15px; margin-top: 25px; width: auto;">圆角的最大值为{{Math.ceil(this.selectedItem.proHeight / 2)}}</label>
                </div>
              </div>
              <!-- 3d地图配置 -->
              <div v-if="selectedItem.chartType=='TDEarthLine' || selectedItem.chartType=='TDEarthBar'">
                <div class="m-gap form-group">地球配置</div>
                <div class="form-group cols2">
                  <label>地球凹凸感</label>
                  <input  type="number"
                          v-model="selectedItem.displacementScale">
                </div>
                <div v-if="selectedItem.chartType=='TDEarthLine'">
                  <div class="m-gap form-group" >线条配置</div>
                  <div class="form-group cols2">
                    <label>线条宽度</label>
                    <select v-model="selectedItem.linewidth">
                      <option v-for="item in 10"
                              :key="item">{{item}}</option>
                    </select>
                  </div>
                  <div class="form-group cols2">
                    <label>线条颜色</label>
                    <div class="color-w200">
                      <Vcolor :data="selectedItem.lineColor"
                              :key="10"
                              type="lineColor"
                              @getdata="getColor"></Vcolor>
                    </div>
                  </div>
                  <div class="form-group cols2">
                  <label>线条透明度</label>
                  <select v-model="selectedItem.lineoption">
                    <option v-for="item in 10"
                            :key="(item*0.1).toFixed(1)">{{(item*0.1).toFixed(1)}}</option>
                  </select>
                  </div>
                  <div class="m-gap form-group">落地点</div>
                  <div class="form-group cols2">
                  <label>落地点大小</label>
                  <input  type="number"
                          v-model="selectedItem.symbolSize">
                  </div>
                  <div class="form-group cols2">
                  <label>落地点颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.symbolcolor"
                            :key="10"
                            type="symbolcolor"
                            @getdata="getColor"></Vcolor>
                  </div>
                  </div>
                  <div class="form-group cols2">
                  <label>落地点透明度</label>
                  <input  type="number"
                            v-model="selectedItem.symbolopacity">
                  </div>
                  <div class="m-gap form-group">扫描点配置</div>
                  <div class="form-group cols2">
                    <label>显示扫描点</label>
                    <select v-model="selectedItem.scanningspot">
                      <option value="true">显示</option>
                      <option value="false">隐藏</option>
                    </select>
                  </div>
                  <div class="form-group cols2" v-if="selectedItem.scanningspot==='true'">
                    <label>扫描点速度</label>
                    <input  type="number"
                            v-model="selectedItem.scanningspeed">
                  </div>
                  <div class="form-group cols2" v-if="selectedItem.scanningspot==='true'">
                    <label>扫描点半径</label>
                    <select v-model="selectedItem.scanningradiu">
                      <option value="1">1</option>
                      <option value="2">2</option>
                      <option value="4">4</option>
                      <option value="6">6</option>
                    </select>
                  </div>
                  <div class="form-group cols2" v-if="selectedItem.scanningspot==='true'">
                    <label>扫描点长度</label>
                    <select v-model="selectedItem.scanninglength">
                      <option value="0.1">0.1</option>
                      <option value="0.2">0.2</option>
                      <option value="0.3">0.3</option>
                      <option value="0.4">0.4</option>
                    </select>
                  </div>
                  <div class="form-group cols2" v-if="selectedItem.scanningspot==='true'">
                    <label>扫描点透明度</label>
                    <select v-model="selectedItem.scanningopcity">
                      <option value="1">1</option>
                      <option value="0.8">0.8</option>
                      <option value="0.6">0.6</option>
                      <option value="0.4">0.4</option>
                    </select>
                  </div>
                  <div class="form-group cols2" v-if="selectedItem.scanningspot==='true'">
                    <label>扫描点颜色</label>
                    <div class="color-w200">
                      <Vcolor :data="selectedItem.scanningcolor"
                              :key="10"
                              type="scanningcolor"
                              @getdata="getColor"></Vcolor>
                    </div>
                  </div>
                </div>
                <div v-if="selectedItem.chartType=='TDEarthBar'">
                  <div class="m-gap form-group">柱体配置</div>
                  <div class="form-group cols2">
                    <label>柱体大小</label>
                    <input  type="number"
                            v-model="selectedItem.barSize">
                  </div>
                  <div class="form-group cols2">
                    <label>柱体颜色</label>
                    <div class="color-w200">
                      <Vcolor :data="selectedItem.itemStyleColor"
                              :key="10"
                              type="itemStyleColor"
                              @getdata="getColor"></Vcolor>
                    </div>
                  </div>
                  <div class="m-gap form-group">标签配置</div>
                  <div class="form-group cols2">
                    <label>字体颜色</label>
                    <div class="color-w200">
                      <Vcolor :data="selectedItem.labelColor"
                              :key="10"
                              type="labelColor"
                              @getdata="getColor"></Vcolor>
                    </div>
                  </div>
                  <div class="form-group cols2">
                    <label>字体大小</label>
                    <input  type="number"
                            v-model="selectedItem.labelSize">
                  </div>
                  <div class="form-group cols2">
                    <label>字体宽度</label>
                    <input  type="number"
                            v-model="selectedItem.labelWeight">
                  </div>
                  <div class="form-group cols2">
                    <label>边框宽度</label>
                    <input  type="number"
                            v-model="selectedItem.labelBorderwidth">
                  </div>
                  <div class="form-group cols2">
                    <label>边框颜色</label>
                    <div class="color-w200">
                      <Vcolor :data="selectedItem.labelBorderColor"
                              :key="10"
                              type="labelBorderColor"
                              @getdata="getColor"></Vcolor>
                    </div>
                  </div>
                  <div class="form-group cols2">
                    <label>圆角大小</label>
                    <input  type="number"
                            v-model="selectedItem.labelBorderRadius">
                  </div>
                  <div class="form-group cols2">
                    <label>行高</label>
                    <input  type="number"
                            v-model="selectedItem.labellineHeight">
                  </div>
                  <div class="form-group cols2">
                    <label>背景颜色</label>
                    <div class="color-w200">
                      <Vcolor :data="selectedItem.labelbackColor"
                              :key="10"
                              type="labelbackColor"
                              @getdata="getColor"></Vcolor>
                    </div>
                  </div>
                </div>
                <div class="m-gap form-group">旋转配置</div>
                <div class="form-group cols2">
                  <label>是否旋转</label>
                  <select v-model="selectedItem.needrotate">
                    <option value="true">旋转</option>
                    <option value="false">不旋转</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>旋转方向</label>
                  <select v-model="selectedItem.rotatedirection">
                    <option value="ccw">由左往右</option>
                    <option value="cw">由右往左</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>旋转速度</label>
                  <input  type="number"
                          v-model="selectedItem.rotatespeed" placeholder="单位（度/秒）">
                </div>
                <div class="form-group cols2">
                  <label>操作后静止时长</label>
                  <input  type="number"
                          v-model="selectedItem.norotatetime" placeholder="鼠标操作后静置多久继续旋转，单位（秒）">
                </div>
                <div class="form-group cols2">
                  <label>地图俯仰角</label>
                  <input  type="number"
                          v-model="selectedItem.alpha" placeholder="俯仰角（单位：度）">
                </div>
                <div class="m-gap form-group">光照配置</div>
                <div class="form-group cols2">
                  <label>光源类型</label>
                  <select v-model="selectedItem.shadingtype">
                    <option value="color">平行光</option>
                    <option value="realistic">自然光</option>
                  </select>
                </div>
                <div class="form-group cols2"  v-if="selectedItem.shadingtype==='realistic'">
                  <label>环境光颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.ambientcolor"
                            :key="10"
                            type="ambientcolor"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2" v-if="selectedItem.shadingtype==='realistic'">
                  <label>环境光亮度</label>
                  <input  type="number"
                          v-model="selectedItem.ambientintensity">
                </div>
                <div class="form-group cols2" v-if="selectedItem.shadingtype==='realistic'">
                  <label>主光源颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.maincolor"
                            :key="10"
                            type="maincolor"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2" v-if="selectedItem.shadingtype==='realistic'">
                  <label>主光源亮度</label>
                  <input  type="number"
                          v-model="selectedItem.mainintensity">
                </div>
                <div class="form-group cols2" v-if="selectedItem.shadingtype==='realistic'">
                  <label>主光源y方向角度</label>
                  <input  type="number"
                          v-model="selectedItem.mainbeta">
                </div>
                <div class="form-group cols2" v-if="selectedItem.shadingtype==='realistic'">
                  <label>主光源x方向角度</label>
                  <input  type="number"
                          v-model="selectedItem.mainalpha">
                </div>
              </div>
              <!-- 迁徙图 -->
              <div v-if="selectedItem.chartType=='DataFlow'">
                <div class="m-gap form-group">地图配置</div>
                <div class="form-group cols2">
                  <label>移入显示地名</label>
                  <select v-model="selectedItem.labelemphasis">
                    <option value="true">显示</option>
                    <option value="false">隐藏</option>
                  </select>
                </div>
                <div class="form-group cols2"  v-if="selectedItem.labelemphasis==='true'">
                  <label>地名字体颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.textStyleColor"
                            :key="10"
                            type="textStyleColor"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2"  v-if="selectedItem.labelemphasis==='true'">
                  <label>地名字体大小</label>
                  <input  type="number"
                          v-model="selectedItem.labelfontSize">
                </div>
                <div class="form-group cols2">
                  <label>允许缩放拖拽</label>
                  <select v-model="selectedItem.roam">
                    <option value="true">允许</option>
                    <option value="false">不允许</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>地图颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.normalcolor"
                            :key="10"
                            type="normalcolor"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2">
                  <label>边界线颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.normalborderColor"
                            :key="10"
                            type="normalborderColor"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2">
                  <label>边界线大小</label>
                  <input  type="number"
                          v-model="selectedItem.normalborderWidth">
                </div>
                <div class="form-group cols2">
                  <label>悬浮背景颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.emphasis"
                            :key="10"
                            type="emphasis"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="m-gap form-group">迁徙线配置</div>
                <div class="form-group cols2">
                  <label>线条宽度</label>
                  <select v-model="selectedItem.normalwidth">
                    <option v-for="item in 5"
                        :key="item" :value="item">{{item}}</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>线条透明度</label>
                  <select v-model="selectedItem.normalopacity">
                    <option value="0.001">0.001</option>
                    <option value="0.01">0.01</option>
                    <option v-for="item in 5"
                            :key="(item*0.2).toFixed(1)">{{(item*0.2).toFixed(1)}}</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>线条弯曲度</label>
                  <select v-model="selectedItem.normalcurveness">
                    <option v-for="item in 16"
                            :key="((item-8)*0.1).toFixed(1)">{{((item-8)*0.1).toFixed(1)}}</option>
                  </select>
                </div>

                <div class="m-gap form-group">迁徙图标配置</div>
                <div class="form-group cols2">
                  <label>是否显示图标</label>
                  <select v-model="selectedItem.effectshow">
                    <option value="true">显示</option>
                    <option value="false">隐藏</option>
                  </select>
                </div>
                <div v-if="selectedItem.effectshow === 'true'">
                <!-- <div class="form-group cols2">
                  <label>图标</label>
                  <input type="file"
                         accept="image/png, image/jpeg, image/gif, image/jpg,image/svg+xml"
                         @change='changeImg' />
                </div> -->
                <div class="form-group cols2">
                  <label>图标运动速度</label>
                  <select v-model="selectedItem.effectperiod">
                    <option value="1">特快</option>
                    <option value="3">快</option>
                    <option value="6">中</option>
                    <option value="9">慢</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>图标大小</label>
                  <input  type="number"
                          v-model="selectedItem.effectsymbolSize">
                </div>
                <div class="form-group cols2">
                  <label>尾迹长度</label>
                  <select v-model="selectedItem.effecttrailLength">
                    <option value="0">无</option>
                    <option value="0.3">短</option>
                    <option value="0.6">长</option>
                    <option value="0.9">特长</option>
                  </select>
                </div>

                </div>

                <div class="m-gap form-group">涟漪点配置</div>
                <div class="form-group cols2">
                  <label>涟漪点显示时机</label>
                  <select v-model="selectedItem.showEffectOn">
                    <option value="emphasis">移入显示</option>
                    <option value="render">一直显示</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>涟漪类型</label>
                  <select v-model="selectedItem.rippleEffectbrushType">
                    <option value="stroke">类型一</option>
                    <option value="fill">类型二</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>涟漪速度</label>
                  <select v-model="selectedItem.rippleEffectperiod">
                    <option :value="2">快</option>
                    <option :value="4">中</option>
                    <option :value="6">慢</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>涟漪环数</label>
                  <select v-model="selectedItem.rippleEffectscale">
                    <option :value="2">2</option>
                    <option :value="4">4</option>
                    <option :value="6">6</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>涟漪大小</label>
                  <select v-model="selectedItem.symbolSize">
                    <option :value="3">小</option>
                    <option :value="6">中</option>
                    <option :value="9">大</option>
                    <option :value="20">特大</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>文字位置</label>
                  <select v-model="selectedItem.normalposition">
                    <option value="top">上方</option>
                    <option value="bottom">下方</option>
                    <option value="left">左方</option>
                    <option value="right">右方</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>文字大小</label>
                  <input  type="number"
                          v-model="selectedItem.normalfontSize">
                </div>
                <div class="m-gap form-group">气泡配置</div>
                <div class="form-group cols2">
                  <label>气泡类型</label>
                  <select v-model="selectedItem.EffectbrushType">
                    <option value="stroke">类型一</option>
                    <option value="fill">类型二</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>气泡缩放比例</label>
                  <select v-model="selectedItem.Effectscale">
                    <option value="1">无</option>
                    <option value="1.4">小</option>
                    <option value="1.6">中</option>
                    <option value="1.8">大</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>气泡大小</label>
                  <input  type="number"
                          v-model="selectedItem.geosymbolSize">
                </div>
                <div class="form-group cols2">
                  <label>文字位置</label>
                  <select v-model="selectedItem.labelposition">
                    <option value="top">上方</option>
                    <option value="bottom">下方</option>
                    <option value="left">左方</option>
                    <option value="right">右方</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>字体颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.labelcolor"
                            :key="10"
                            type="labelcolor"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2">
                  <label>文字大小</label>
                  <input  type="number"
                          v-model="selectedItem.labeltextSize">
                </div>
                <div class="m-gap form-group">tips配置</div>
                <div class="form-group cols2">
                  <label>tips背景颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.tooltipBackColor"
                            :key="10"
                            type="tooltipBackColor"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2">
                  <label>tips字体颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.tooltipTextColor"
                            :key="10"
                            type="tooltipTextColor"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2">
                  <label>tips字体大小</label>
                  <input  type="number"
                          v-model="selectedItem.tooltipTextfontSize">
                </div>

                <div class="m-gap form-group">图例配置</div>
                <div class="form-group cols2">
                  <label>显示图例</label>
                  <select v-model="selectedItem.visualMapShow">
                    <option value="true">显示</option>
                    <option value="false">隐藏</option>
                  </select>
                </div>
                <div class="form-group cols2" v-if="selectedItem.visualMapShow==='true'">
                  <label>显示图例文字</label>
                  <select v-model="selectedItem.calculable">
                    <option value="true">显示</option>
                    <option value="false">隐藏</option>
                  </select>
                </div>
                <div class="form-group cols2" v-if="selectedItem.visualMapShow==='true'">
                  <label>图例字体颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.visualMapTextcolor"
                            :key="10"
                            type="visualMapTextcolor"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2">
                  <label>颜色配置</label>
                  <div class="form-group colorsConf">
                    <span>序号</span>
                    <!-- <span class="color-w70 text">系列</span> -->
                    <span class="color-w70 text">颜色</span>
                    <!-- <i class="icon-n-add" @click="addColor"></i> -->
                  </div>
                  <div class="form-group colorsConf"
                       v-for="(v,index) in selectedItem.ctColors"
                       :key="index">
                    <span class="colorOrder">{{index+1}}</span>
                    <div>
                      <div class="color-w200"
                           style="float: left; width: 140px;">
                        <Vcolor :data="selectedItem.ctColors[index]"
                                type="ctColors"
                                :index="index"
                                @getdata="getSingleColor"></Vcolor>
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
                </div>
              </div>
              <!-- 3/4饼图 -->
              <!-- <div v-if="selectedItem.chartType=='GradientPie'">
                  <div class="form-group cols2">
                    <label>线条方向</label>
                    <select v-model="selectedItem.linewidth">
                      <option value="true">逆时针</option>
                      <option value="false">顺时针</option>
                    </select>
                  </div>
              </div> -->

              <!--数字翻牌器-->
              <div v-if="selectedItem.chartType=='doubler' || selectedItem.chartType=='number'">
                <div class="m-gap form-group">图例配置</div>
                <div class="form-group cols2">
                  <label>图例可见性</label>
                  <select v-model="selectedItem.ctLegendShow">
                    <option value="true">显示</option>
                    <option value="false">隐藏</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>图例文字颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.legendColor"
                            :key="20"
                            type="legendColor"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="m-gap form-group">图表样式</div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='doubler'">
                  <label>背景色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.bgClr"
                            :key="8"
                            type="bgClr"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='doubler'">
                  <label>边框色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.bdClr"
                            :key="9"
                            type="bdClr"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2">
                  <label>字号</label>
                  <select v-model="selectedItem.fontSize">
                    <option v-for="item in defaultFontSize"
                            :key="item">{{item}}</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>字体颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.clr"
                            :key="10"
                            type="clr"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
              </div>
              <div v-if="selectedItem.chartType=='number' || selectedItem.chartType=='text'">
                <div class="m-gap form-group">字体样式</div>
                <div class="form-group"
                     style="height: 30px;">
                  <div v-for="(item, index) in settingData.fontFaces"
                       :key="index"
                       @click="selectedItem.fontFamily=item.fontFace"
                       :class="{'fl': true, 'font-case': true, 'act': selectedItem.fontFamily===item.fontFace}"
                       :style="{'font-family': item.fontFace}">
                    {{item.fontName}}
                  </div>
                </div>
              </div>

              <div class="e-legend"
                   v-if="selectedItem.chartType=='v-scatter'">
                <div>
                  <div class="m-gap form-group">图例配置</div>
                  <div class="form-group cols2">
                    <label>图例可见性</label>
                    <select v-model="selectedItem.ctLegendShow">
                      <option value="true">显示</option>
                      <option value="false">隐藏</option>
                    </select>
                  </div>
                  <div class="m-gap form-group">图表样式</div>
                  <div class="form-group cols2">
                    <label>主题</label>
                    <select v-model="selectedItem.themeType">
                      <option value="1">深色</option>
                      <option value="2">浅色</option>
                    </select>
                  </div>
                </div>
              </div>
              <div class="e-legend"
                   v-if="selectedItem.chartType=='v-map'">
                <div>
                  <div class="m-gap form-group">图例配置</div>
                  <div class="form-group cols2">
                    <label>图例可见性</label>
                    <select v-model="selectedItem.ctLegendShow">
                      <option value="true">显示</option>
                      <option value="false">隐藏</option>
                    </select>
                  </div>
                  <div class="form-group cols2">
                    <label>图例位置</label>
                    <select v-model="selectedItem.visualPosition">
                      <option value="left">底部靠左</option>
                      <option value="right">底部靠右</option>
                    </select>
                  </div>
                  <div class="m-gap form-group">图表样式</div>
                  <div class="form-group cols2">
                    <label>地名可见性</label>
                    <select v-model="selectedItem.cityShow">
                      <option value="true">显示</option>
                      <option value="false">隐藏</option>
                    </select>
                  </div>
                  <div class="form-group cols2">
                    <label>地名颜色</label>
                    <div class="color-w200">
                      <Vcolor :data="selectedItem.cityColor"
                              :key="23"
                              type="cityColor"
                              @getdata="getColor"></Vcolor>
                    </div>
                  </div>
                  <div class="form-group cols2">
                    <label>字号</label>
                    <select v-model="selectedItem.fontSize">
                      <option value="8">8</option>
                      <option value="9">9</option>
                      <option value="10">10</option>
                      <option value="12">12</option>
                      <option value="13">13</option>
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
                    <select v-model="selectedItem.colorType"
                            @change="chgColorType"
                            :style="{width: (selectedItem.chartType=='ve-histogram' || selectedItem.chartType=='ve-bar') && !selectedItem.subType ? '100px !important' : ''}">
                      <option value="defalut">默认</option>
                      <option value="custom"
                              v-show="alertLevel">自定义</option>
                    </select>
                  </div>
                  <div class="colorConf"
                       v-if="selectedItem.colorType=='custom'">
                    <div class="form-group">
                      <span>序号</span>
                      <span class="text">颜色</span>
                      <span class="options">操作</span>
                    </div>
                    <div class="form-group"
                         v-for="(v,index) in selectedItem.ctColors"
                         :key="index">
                      <span class="colorOrder">{{index+1}}</span>
                      <div class="color-w70">
                        <Vcolor :data="selectedItem.ctColors[index]"
                                :index="index"
                                @getdata="getMapColor"></Vcolor>
                      </div>

                      <i class="icon-n-toUp"
                         @click="moveUp(index)"></i>
                      <i class="icon-n-putin"
                         @click="moveDown(index)"></i>
                    </div>
                  </div>
                </div>
              </div>
              <!--图例-->
              <div class="e-legend"
                   v-if="isEcharts">
                <div v-show="showLengendConf">
                  <div class="m-gap form-group">图例配置</div>
                  <div class="form-group cols2">
                    <label>图例可见性</label>
                    <select v-model="selectedItem.ctLegendShow">
                      <option value="true">显示</option>
                      <option value="false">隐藏</option>
                    </select>
                  </div>
                  <div class="form-group cols2">
                    <label>图例位置</label>
                    <select>
                      <option>底部居中</option>
                    </select>
                  </div>
                  <div v-if="selectedItem.chartType === 've-radar' || selectedItem.chartType === 've-line' || selectedItem.chartType === 've-bar' || selectedItem.chartType === 've-histogram'">
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
                        <Vcolor :data="selectedItem.splitColor"
                                :key="18"
                                type="splitColor"
                                @getdata="getColor"></Vcolor>
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
                <div class="form-group cols2"
                     v-if="selectedItem.chartType==='ve-gauge'">
                  <div class="m-gap form-group">图例配置</div>
                  <div class="form-group cols2"
                       v-if="selectedItem.subType==='progress'">
                    <label>图例可见性</label>
                    <select v-model="selectedItem.ctLegendShow">
                      <option value="true">显示</option>
                      <option value="false">隐藏</option>
                    </select>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType !== 've-radar' && selectedItem.chartType !== 've-pie'  && selectedItem.chartType !== 've-ring'">
                  <label v-if="selectedItem.chartType==='ve-line' || selectedItem.chartType==='ve-histogram' || selectedItem.chartType==='ve-bar'">坐标文字颜色</label>
                  <label v-else>图例文字颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.legendColor"
                            :key="24"
                            type="legendColor"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='ve-line'">
                  <label>折线图类型</label>
                  <select v-model="selectedItem.lineArea">
                    <option value="false">折线图</option>
                    <option value="true">区域图</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='ve-line'">
                  <label>线条类型</label>
                  <select v-model="selectedItem.smooth">
                    <option value="true">曲线</option>
                    <option value="false">折线</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='ve-line'">
                  <label>是否标点</label>
                  <select v-model="selectedItem.showPoint">
                    <option value="false">否</option>
                    <option value="true">是</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.subType==='progress' && !selectedItem.secondType">
                  <label>字号</label>
                  <select v-model="selectedItem.fontSize">
                    <option v-for="item in proFontSize"
                            :key="item">{{item}}</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType==='ve-gauge'">
                  <label>底色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.bgClr"
                            :key="12"
                            type="bgClr"
                            @getdata="getGaugeCl"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType==='ve-pie' || selectedItem.chartType==='ve-ring' || selectedItem.chartType==='ve-radar'">
                  <label>图表文字颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.legendColor"
                            :key="25"
                            type="legendColor"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-show="selectedItem.secondType !== 'liquidfill'">
                  <label>配色<i class="icon-n-tip"
                       style="font-size: 16px; position: relative; top: 1px; left: 3px;"
                       title="可增加多个配色项，依次对应各项颜色，配色项少于数据组时循环取色"></i></label>
                  <select v-model="selectedItem.colorType"
                          @change="chgColorType"
                          :style="{width: (selectedItem.chartType=='ve-histogram' || selectedItem.chartType=='ve-bar') && !selectedItem.subType ? '90px !important' : ''}">
                    <option value="defalut">默认</option>
                    <option value="custom"
                            v-show="alertLevel">系列</option>
                  </select>
                  <select v-model="selectedItem.colorful"
                          v-show="(selectedItem.chartType=='ve-histogram' || selectedItem.chartType=='ve-bar') && !selectedItem.subType"
                          style="width: 80px !important; margin-left: 3px;">
                    <option value="false">同色柱</option>
                    <option value="true">异色柱</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-show="selectedItem.secondType !== 'liquidfill' && selectedItem.colorType === 'custom'">
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
                    <span @click="colorToAll"
                          style="color: #0088cc; cursor: pointer;">应用到已添加元件</span>
                    <!-- <i class="icon-n-add" @click="addColor"></i> -->
                  </div>
                  <div class="form-group colorsConf"
                       v-for="(v,index) in selectedItem.ctColors"
                       :key="index">
                    <span class="colorOrder">{{index+1}}</span>
                    <div class="gradient"
                         v-if="selectedItem.ifGradual==='true'"
                         @click="reverseColor(index)"
                         :style="{'background': 'linear-gradient(45deg, ' + selectedItem.ctColors[index][0]  +',' + selectedItem.ctColors[index][1] + ')'}">
                      <div class="color-w15">
                        <Vcolor :data="selectedItem.ctColors[index][0]"
                                :index="index"
                                @getdata="getColorStart"></Vcolor>
                      </div>
                      <div class="color-w15"
                           style="float: right">
                        <Vcolor :data="selectedItem.ctColors[index][1]"
                                :index="index"
                                @getdata="getGradColor"></Vcolor>
                      </div>
                    </div>
                    <div v-else>
                      <div class="color-w200"
                           style="float: left; width: 140px;">
                        <Vcolor :data="selectedItem.ctColors[index]"
                                type="ctColors"
                                :index="index"
                                @getdata="getSingleColor"></Vcolor>
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
                  <!-- <button type="button" class="colorToall" @click="colorToAll">应用到已添加元件</button> -->
                </div>
              </div>

              <div v-show="selectedItem.secondType=='symbolBar'">
                <div>
                  <div class="form-group cols2">
                    <label>图形形状</label>
                    <input type="file"
                           accept="image/png, image/jpeg, image/gif, image/jpg,image/svg+xml"
                           @change='changeImg' />
                  </div>
                </div>
              </div>

              <ChartStyle v-if="selectedItem.chartType && (selectedItem.chartType.indexOf('ve-') > -1 || ['liquidfill', 'bubble'].includes(selectedItem.chartType))" :configItems="selectedItem" @change="changeChartStyle"></ChartStyle>

              <template v-if="['video', 'ppt'].includes(selectedItem.chartType)">
                <!-- <div class="m-gap form-group">基础样式</div> -->
                <div class="form-group cols2"
                    v-for="(item, index) in config[selectedItem.chartType].styles.base" :key="`base_${index}`"
                  >
                    <label>{{item.name}}</label>
                    <template v-if="item.tag == 'select'">
                      <select v-model="selectedItem[item.key]">
                        <option v-for="(option, optIndex) in item.options" :key="`${option.name}_${optIndex}`"
                        :value="option.value"
                        >{{option.name}}</option>
                      </select>
                    </template>
                    <template v-else-if="item.tag == 'input'">
                      <input class="w-90" :type="item.type" v-model="selectedItem[item.key]"> {{ item.unit || '' }}
                    </template>
                  </div>
              </template>

              <template v-if="['GradientPie','Sunrise','Scatter'].includes(selectedItem.chartType)">
                <div class="form-group cols2"
                    v-for="(item, index) in config[selectedItem.chartType].styles.base" :key="`base_${index}`"
                  >
                  <ChildTag :item="item" :selectedItem="selectedItem"></ChildTag>
                </div>
              </template>
            </div>

            <!--数据-->
            <div v-show="!showStyleTab"
                 class="full-height">
              <div class="form-group cols2" v-show="['image', 'text', 'hotspot'].includes(selectedItem.chartType)">
                  <label>跳转大屏</label>
                  <select v-model="selectedItem.linkId">
                    <option value="">无跳转</option>
                    <option v-for="item in allPageList"
                            :key="item.id"
                            :value="item.id">{{item.name}}</option>
                  </select>
              </div>
              <div v-show="['image', 'ppt'].includes(selectedItem.chartType)">
                <div class="form-group cols2">
                  <label>选择文件</label>
                  <input type="file"
                         accept="image/png, image/jpeg, image/gif, image/jpg,image/svg+xml"
                         @change='changeImg' />
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
              <div style="height: 100%;" v-show="!['image', 'border', 'time', 'video', 'ppt'].includes(selectedItem.chartType)" >
                <div class="form-group cols2">
                  <label>数据来源</label>
                  <select @change="chgDataSource"
                          v-model="selectedItem.ctDataSource">
                    <!-- <option value="static">静态数据</option> -->
                    <!-- <option value="system">系统数据</option> -->
                    <option v-for="(val,key) in dataSource" :key="key" :value="key === '静态数据' ? 'static' : (key === '系统数据' ? 'system' : key)">{{key}}</option>
                    <!-- v-show="selectedItem.chartType!=='v-map' && selectedItem.chartType!=='v-scatter'"  -->
                  </select>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.ctDataSource == 'static' && animationType.indexOf(selectedItem.chartType) !== -1">
                  <div class="form-group"
                       style="position: relative">
                    <label>刷新周期(s)</label>
                    <input class="color-w200"
                           type="number"
                           placeholder="刷新周期"
                           onkeypress='return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )'
                           v-model="selectedItem.refreshTm">
                    <label class="error"
                           v-if="freshVali"
                           style="margin-left: 88px;">刷新周期最小值为3</label>
                  </div>
                </div>
                <div v-show="selectedItem.ctDataSource !== 'static'">
                  <div class="form-group cols2" contenteditable="false">
                    <label>选择接口</label>
                    <select ref="urlSel"
                            v-model="syst.curConf.url"
                            @change="chgUrl">
                      <option v-for="v in syst.urlSel"
                              :value="v.url"
                              :key="v.key">{{v.name}}</option>
                    </select>
                  </div>
                  <div id="mainSystemConf">
                    <div class="form-group cols2"
                         v-for="(v,idx) in syst.curUrl"
                         :key="idx">
                      <label v-if="v.type=='drop-down' || v.type=='multi-select'"
                             class="e-legend">{{v.name}}<i class="icon-n-tip"
                           v-if="v.title"
                           style="font-size: 16px; position: relative; top: 1px; left: 3px;"
                           :title="v.title"></i></label>
                      <Select2 v-if="v.type=='drop-down' || v.type=='multi-select'"
                               :name="v.key"
                               v-model="syst.curConf.params[v.key]"
                               :obj="v"
                               @input="chgSelects(v)">
                      </Select2>
                    </div>
                  </div>
                  <!-- <button @click="getUrlData">请求数据</button>-->
                </div>
                <button v-if="showWindowBtn"
                        @click="getWindowData"
                        class="addData"
                        style="display: block; margin-left: 85px; margin-bottom: 20px;">配置资源指标详细</button>
                <div class="form-group"
                     v-if="selectedItem.ctDataSource === 'static' && selectedItem.chartType != 'v-map' && selectedItem.chartType!=='v-scatter' && selectedItem.chartType != 'text' && selectedItem.chartType != 'marquee'">
                  <div ref="textareaData"
                       class="confData"
                       v-if="refreshData"
                       contenteditable="true">{{selectedItem.chartData}}</div>
                </div>
                <div class="form-group"
                     v-if="selectedItem.ctDataSource === 'static' && (selectedItem.chartType === 'text' || selectedItem.chartType==='marquee')">
                  <div ref="textarea"
                       class="confData"
                       v-if="refreshData"
                       contenteditable="true">{{selectedItem.ctName}}</div>
                </div>
                <div class="form-group cols2"
                     v-show="selectedItem.thirdType==='moveTable'">
                  <label>每页展示条数</label>
                  <select v-model="selectedItem.pageNum">
                    <option v-for="i in 15"
                            :key="'tbNum' + i"
                            :value="i">{{i}}</option>
                  </select>
                </div>
                <div v-show="selectedItem.chartType === 'v-map' || selectedItem.chartType==='v-scatter'">
                  <div class="form-group cols2">
                    <label>展示范围</label>
                    <select v-model="selectedItem.mapLevel"
                            @change="chgMapLevel">
                      <option value="country">全国地图</option>
                      <option value="province">省级地图</option>
                      <option value="city">地市级地图</option>
                    </select>
                  </div>
                  <div v-show="selectedItem.mapLevel!=='country'"
                       @click="chgMapLevel"
                       class="form-group cols2">
                    <label>省/直辖市</label>
                    <Select2 v-model="selectedItem.provinceCode"
                             :mapSelect="true"
                             :obj="provinceArr"
                             @input="chgProvince(selectedItem.provinceCode)"></Select2>
                  </div>
                  <div v-if="selectedItem.mapLevel==='city'"
                       @click="chgMapLevel"
                       class="form-group cols2">
                    <label>市</label>
                    <Select2 v-model="selectedItem.cityCode"
                             :mapSelect="true"
                             :obj="cityArr"
                             @input="chgCity(selectedItem.cityCode)"></Select2>
                  </div>
                  <div class="form-group cols2"
                       v-if="selectedItem.chartType==='v-scatter' && selectedItem.ctDataSource == 'static'">
                    <label class="e-legend">数据设置<i class="icon-n-tip"
                         style="font-size: 16px; position: relative; top: 1px; left: 3px;"
                         title="对每一个数据点所在的地区设置告警级别"></i></label><button class="addData"
                            @click="addAlertLevel">添加数据点</button>
                    <!-- <button type="button" class="colorToall" @click="addAlertLevel">添加数据点</button> -->
                  </div>

                  <div class="form-group cols2"
                       v-show="selectedItem.chartType!=='v-scatter' && selectedItem.ctDataSource == 'static'">
                    <label class="e-legend">数据设置<i class="icon-n-tip"
                         style="font-size: 16px; position: relative; top: 1px; left: 3px;"
                         title="设置每个地区的分布数量"></i></label>
                    <div class="setMapData"
                         style="height: 180px;">
                      <div class="area-item"
                           v-for="(area, index) in areaArr"
                           :key="index"><span class="area-name">{{area.name}}</span><input class="w-90"
                               autocomplete="off"
                               oninput="value=value.replace(/[^\d]/g,'')"
                               v-model="selectMapData[area.name]"
                               :name="area.name"></div>
                    </div>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.ctDataSource !== 'static'">
                  <div class="form-group"
                       contenteditable="false"
                       style="position: relative">
                    <label>刷新周期(s)</label>
                    <input class="color-w200"
                           type="number"
                           placeholder="刷新周期"
                           onkeypress='return( /[\d]/.test(String.fromCharCode(event.keyCode) ) )'
                           v-model="selectedItem.refreshTm">
                    <label class="error"
                           v-if="freshVali"
                           style="margin-left: 88px;margin-top: 5px;">刷新周期最小值为3</label>
                  </div>
                </div>
                <div class="form-group cols2"
                      v-show="selectedItem.chartType==='v-map'"
                      style="position: relative;">
                  <!-- editPieces -->
                  <div class="levelTips"
                        v-show="levelTipsShow"
                        :style="{'top': (60 + 40*this.levelChangeIndex) + 'px'}">
                    <i class="icon-n-arrowUp"
                        style="font-size: 30px;"></i>
                    <div>与其余量级区间重合，是否合并为一个量级?</div>
                    <span class="tipbtn"
                          @click="sureLevelTips">是</span><span class="tipbtn"
                          @click="cancelLevelTips">否</span>
                  </div>
                  <label class="e-legend">数据量级<i class="icon-n-tip"
                        style="font-size: 16px; position: relative; top: 1px; left: 3px;"
                        title="设置数据的区间。分布数量处于不同区间的地区，展示颜色会有差别"></i></label>
                  <!-- <div class="setMapGrad" v-for="(item, index) in selectedItem.piecesData" :key="index">
                                    <span>量级一</span>
                                    <input class="w-90" type="number" @change="changeTarget('x')" v-model="selectedItem.piecesData[index].min"> -
                                    <input class="w-90" type="number" @change="changeTarget('x')" v-model="selectedItem.piecesData[index].max">
                                  </div> -->
                  <div class="setMapGrad"
                        v-for="(item, index) in editPieces"
                        :key="index">
                    <span>量级{{index + 1}}</span>
                    <input class="w-90"
                            type="number"
                            disabled
                            v-model="editPieces[index].min"> -
                    <input class="w-90"
                            type="number"
                            :disabled="index===(editPieces.length-1)"
                            @change="chgMapGrad(index)"
                            v-model="editPieces[index].max">
                    <i v-if="index===(editPieces.length-1) && index > 2"
                        class="icon-n-deleteNew"
                        @click="delMapLevel"></i>
                  </div>
                  <button type="button"
                          class="colorToall"
                          @click="addMapLevel">添加量级</button>
                </div>
                <div class="setMapData"
                     v-if="selectedItem.chartType==='v-scatter' && selectedItem.ctDataSource == 'static'">
                  <div class="area-item"
                       v-for="(item, index) in alertMapData"
                       :key="index">
                    <span class="area-index">{{index + 1}}</span>
                    <Select2 v-model="alertMapData[index].name"
                             :disData="selectedPositn"
                             :key="selectedItem.id + index"
                             :mapSelect="true"
                             :sameName="true"
                             :obj="areaArr"
                             @input="chgAreaName(alertMapData[index].name, index)"></Select2>
                    <Select2 v-model="alertMapData[index].value"
                             :mapSelect="true"
                             :obj="alertLevels"></Select2>
                    <i class="icon-n-deleteNew"
                       v-if="alertMapData.length > 1"
                       @click="delAlertLevel(index)"></i>
                  </div>
                </div>
                <button @click="dataChange">更新视图</button>

              </div>
              <div style="height: 100%;"
                   v-show="selectedItem.chartType==='video'">
                <div class="form-group cols2">
                  <label>视频来源</label>
                  <select v-model="selectedItem.videoType">
                    <option value="url">URL地址</option>
                    <option value="local">本地文件</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-show="selectedItem.videoType === 'url'"
                     style="position: relative;">
                  <label>URL地址</label>
                  <input v-model="tempVideoUrl"
                         @focus="showPlayErr = false">
                  <label class="error"
                         v-show="showPlayErr"
                         style="margin-left: 85px; margin-top: 2px;">该地址无效或不允许在本网页播放</label>
                </div>
                <div>
                  <div class="form-group cols2"
                       v-show="selectedItem.videoType === 'local'">
                    <label>选择文件</label>
                    <input type="file"
                           name="myfiles"
                           id="myfiles"
                           accept="video/mp4"
                           @change="uploadVideo">
                  </div>
                </div>
                <button @click="videoChange"
                        style="margin-top: 30px">更新视图</button>
              </div>
              <template v-if="selectedItem.chartType == 'ppt'">
                <div class="form-group cols2 img_src_list" @click="deleteSrcList($event)">
                  <SlickList axis="y" v-model="selectedItem.srcList" :pressDelay="200">
                    <SlickItem v-for="(item, index) in selectedItem.srcList" :key="index"
                    :index="index" :item="item "
                    class="src_item"
                    >
                      {{item.name}}
                      <span class="delete_text" :data-index="index" >删除</span>
                    </SlickItem>
                  </SlickList>
                </div>
              </template>
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
          <div class="modal-body"
               style="height: 450px; overflow: auto;">
            <form autocomplete="off"
                  style="margin-bottom: 20px;"
                  v-for="(list, i) in syst.windowObj"
                  :key="i">
              <div class="form-group modal-label"
                   style="width: 100%; min-height: 30px; height: auto;">
                <label class="page-lable page-title"><i class="icon-n-arrowRight"></i>指标分类： {{list.indicator.name}}</label>
                <div class="page-lable-content"
                     style="margin-left: 32px;"
                     v-if="list.fields && list.fields.length > 0">
                  <span>指标：</span>
                  <Select2 v-model="syst.windowData[i].fields"
                           :mapSelect="true"
                           :multip="list.indicator.multipleField"
                           :obj="list.fields"></Select2>
                </div>
              </div>
              <div class="form-group form-content"
                   v-for="(item, index) in list.ne"
                   :key="index">
                <label class="page-title">资源{{index+1}}: {{item.name}}</label><label class="page-title">资源类型：{{item.neClass}}</label>
                <div class="page-lable-content"
                     v-if="item.component && item.component.length > 0">
                  <span>部件：</span>
                  <Select2 v-model="syst.windowData[i].ne[index].component"
                           :mapSelect="true"
                           :multip="syst.windowData[i].ne[index].multipleComponent"
                           :obj="item.component"></Select2>
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
.img_src_list {
  .src_item {
    // padding-left: 8px;
    line-height: 25px;
    // color: red;
    cursor:move;
  }
  .delete_text {
    cursor: pointer;
    color: #0088cc;
  }
}
.src_item {
  z-index:100;
}
#chooseWrap .vue-ruler-wrapper {
  z-index: 50;
}

</style>
