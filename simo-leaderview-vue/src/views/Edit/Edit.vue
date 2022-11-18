<template>
  <!-- :composeData="composeData" -->
  <!-- moniModal edit in nofooter -->
  <div id="mainEdit-edit"
       class="editPage">
    <!-- style="z-index: 20099" -->
    <!-- <PreView :showModal="viewPage"
             :pageData="pageData"
             :key="viewKey"
             :composeData="composeData"
             :paintObj="paintObj"
             @hidePreview="hidePreview"></PreView> -->
    <PreView :pageData="pageData"
             :key="viewKey"
             :composeData="composeData"
             :paintObj="paintObj"
             ref="PreView"></PreView>
    <Confirm :showModal="showBackModal"
             :message="'确认离开当前页吗？未保存数据将会丢失！'"
             @hideModal="back" aria-hidden="false" data-backdrop="static"></Confirm>

    <Modal
      v-model="showUpload"
      id="InputModelBox"
      footer-hide
      title="导入模型">
      <Form ref="importModelForm" :label-width="80">
        <div class="input-item" style="margin-bottom: 20px;">
          <span>模型名称：</span>
          <Input style="width: 300px; margin: 0px 5px;"
                 v-model="importModelForm.name" />
          <!-- <div class="check_font" v-show="importCheckRules.nameCheck">模型名称不能为空</div> -->
        </div>
        <div class="input-item"
             style="position: relative; margin-bottom: 20px;">
          <span>上传模型：</span>
          <input type="file" id="uploadZip"
            accept=".zip,application/zip,application/x-zip-compressed"
            @change="getZip" style="opacity: 0; position: absolute; z-index: -1;"/>
          <Input style="width: 270px; margin: 0px 5px;"
           disabled v-model="importModelForm.fileName"/>
          <Button class="ivu-primary" @click="upload">预览</Button>
        </div>
        <div style="text-align: right" class="modal-footer">
          <button type="button" @click="sureUpload">确认</button>
          <button type="button" data-dismiss="modal" @click="cancel()">取消</button>
        </div>
      </Form>
    </Modal>
    <div class="edit-content"
         @click.ctrl="bindCtrl">
      <div class="edit-header">
        <h4 class='edit-title'
            style="margin: 0 18px;"
            v-if="tapsStation==='center'"
            @click.self="clickPaint($event)">{{pageName}}</h4>
        <div :class="tapsStation==='left'?'fl':'fr'">
          <h4 class='edit-title'
              style="margin: 0 18px;"
              v-if="tapsStation!=='center'"
              @click.self="clickPaint($event)">{{pageName}}</h4>
          <span v-if="tapsStation!=='center'"
                class="fl">|</span>
          <a class=" simoLink icon-n-revoke edit-opt"
             v-if="historyArr.length > 0"
             @click="Revoke">撤销</a>
          <a class=" simoLink icon-n-revoke edit-opt"
             style="color:#666F8B;"
             v-else>撤销</a>
          <a class=" simoLink icon-n-keyboard edit-opt"
             @mouseover="showKeybd = true"
             @mouseout="showKeybd = false">快捷键</a>
          <!-- <button type="button" class="close fr edit-opt" @click="back"></button> -->
          <span>|</span>
          <a class=" icon-n-save simoLink edit-opt"
             @click="saveConf">保存</a>
          <a class=" simoLink icon-n-preview edit-opt"
             @click="preview">预览</a>
          <a class=" simoLink icon-n-withdraw edit-opt"
             @click="preBack">返回</a>
        </div>
      </div>
      <div class="edit-keyboard"
           :id="tapsStation==='left'?'fledit':'fredit'"
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
        <div class='btm-tools'>
          <div :class="tapsStation==='left'?'btn-box fl':'btn-box fr'"
               class="ringparent">
            <span @click="preOther(0)"
                  class="ring-icon hoverTips"
                  style="line-height: 34px;margin: 0 8px;"
                  data-toggle='tooltip'
                  title="上一页"><i class="icon-n-prev"></i></span>
            <span @click="preOther(1)"
                  class="ring-icon hoverTips"
                  data-toggle='tooltip'
                  style="line-height: 34px;margin: 0 8px;"
                  title="下一页"><i class="icon-n-next"></i></span>
          </div>
        </div>
        <!--右键-->
        <ul class="menu-list"
            style="width: 156px;"
            ref="copyMenu">
          <li class="context-menu-item context-menu-visible"
              @click="openHawkEye">
            <span>鹰眼</span>
            <div class="fr">
              <div :class="{'u-switch': true, 'u-switch-on': ShowHawkEye, 'u-switch-off': !ShowHawkEye}">
                <div></div>
              </div>
            </div>
          </li>
          <li class="context-menu-item context-menu-visible"
              @click="openMapChange">
            <span>自动窗口</span>
            <div class="fr">
              <div :class="{'u-switch': true, 'u-switch-on': MapChange, 'u-switch-off': !MapChange}">
                <div></div>
              </div>
            </div>
          </li>
          <li class="context-menu-item context-menu-visible"
              @click="paste"
              v-if="this.tempItemArry.length > 0"><span>粘贴</span></li>
        </ul>
        <ul class="menu-list"
            style="width: 156px;"
            ref="contextMenu">
          <li class="context-menu-item context-menu-visible"
              @click="openHawkEye">
            <span>鹰眼</span>
            <div class="fr">
              <div :class="{'u-switch': true, 'u-switch-on': ShowHawkEye, 'u-switch-off': !ShowHawkEye}">
                <div></div>
              </div>
            </div>
          </li>
          <li class="context-menu-item context-menu-visible"
              @click="openMapChange">
            <span>自动窗口</span>
            <div class="fr">
              <div :class="{'u-switch': true, 'u-switch-on': MapChange, 'u-switch-off': !MapChange}">
                <div></div>
              </div>
            </div>
          </li>
          <li class="context-menu-item context-menu-visible"
              v-show="!childResize"
              @click="copy"><span>复制</span></li>
          <li class="context-menu-item context-menu-visible"
              v-show="!childResize && tempItemArry.length > 0"
              @click="paste($event)"><span>粘贴</span></li>
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
        <!-- <div class="m-left content-side flex"
             @click.self="clickPaint($event)">
          <div class="cs-item"
               :key="key"
               v-for="(value,key) in compsArr"
               :class="value.imgClass"
               @click="initChart(value)">
            {{value.text}}</div>
        </div> -->
        <Archive @click="initChart"
                 :tapsStation='tapsStation'></Archive>

        <div class="m-main flex-1 auto"
             id="centerMapBox"
             ref="editCanvas"
             @click.self="clickPaint($event)">
          <div class="paint-bg"
               @contextmenu.prevent="mycontextmenu($event)"
               :style="{'width': paintObj.width + 'px', 'height': paintObj.height + 'px', 'transform' : 'scale(' + paintObj.scale/100 + ')',  'background-color': paintObj.bgColor, 'z-index':500}">
            <div class="paint"
                 :style="paintStyle"></div>
            <div id="chooseWrap">
              <vue-ruler-tool ref="rulertool"
                              v-model="presetLine"
                              :step-length="20"
                              :parent="true"
                              :tapsStation='tapsStation'
                              :parentW="paintObj.width"
                              :parentH="paintObj.height"
                              :is-scale-revise="true"
                              :visible.sync="paintObj.showGrid"
                              :scale="paintObj.scale"
                              :helpLineColor='helpLineColor'
                              style="height:100%;width100%">
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
          <vue-ruler ref="ruler"
                     v-model="presetLine"
                     :tapsStation='tapsStation'
                     v-if="paintObj.showGrid"
                     :scale="paintObj.scale"
                     :parentW="paintObj.width"
                     :parentH="paintObj.height">
          </vue-ruler>
        </div>
        <div class="m-right full-height flex flex-vertical"
             :class="{noSlected:!selectedItem.chartType}">
          <div class="handle_label"
               v-show="selectedItem.ctName">当前元件: {{curChartName}}</div>
          <div class="handle_label"
               v-show="chooseIndexs.length + chooseCompIndexs.length > 1">当前元件: 组合</div>
          <div class="base-item"
               v-show="chooseIndexs.length === 1 && chooseCompIndexs.length === 0">
            <div class="m-tab"
                 :class="{active:showStyleTab}"
                 @click="showStyleTab=true">样式</div>
            <div class="m-tab"
                 v-show="!['decorator'].includes(curChartType)"
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
                <div class="form-group" style="height: 30px;">
                  <label>组合名称</label>
                  <input type="text" v-model="testObj.comName">
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
                             accept="image/png, image/webp, image/jpeg, image/gif, image/jpg,image/svg+xml"
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
                  <label style="width:60px">辅助线颜色</label>
                  <div class="color-w70">
                    <Vcolor :data="helpLineColor"
                            :key="11"
                            type="helpLineColor"
                            @getdata="getLineCl"></Vcolor>
                  </div>
                </div>

                <div class="m-gap form-group set-map" v-if="CanChangeServes">全局变量配置</div>
                <div class="form-group" id="resourcesIds" v-if="CanChangeServes">
                  <label style="width:60px">资源调整</label>
                  <div>
                    <Select v-model="resourcesIds" filterable style="width:180px" clearable >
                        <div slot="empty">not Found data</div>
                        <Option v-for="(item,index) in resourcesValueIds" :value="item.value" :key="index">{{ item.name }}</Option>
                    </Select>
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
                           type="text"
                           oninput="value=value.replace(/[^\d]/g,'')"
                           v-model="testObj.width">
                    <span>px</span>
                    <!-- <span @click="changetestW(0)" -->
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
                           type="text"
                           oninput="value=value.replace(/[^\d]/g,'')"
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
                           type="text"
                           oninput="value=value.replace(/[^\d-]/g,'')"
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
                           type="text"
                           oninput="value=value.replace(/[^\d-]/g,'')"
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
                <div class="m-gap form-group">图表样式</div>
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
              <div v-if="selectedItem.chartType=='table' || selectedItem.chartType=='text' || selectedItem.chartType=='Marquee' || selectedItem.chartType=='border' || selectedItem.chartType=='time'">
                <div class="m-gap form-group">图表样式</div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='table'">
                  <label>添加内发光</label>
                  <select v-model="selectedItem.Internal">
                    <option value="true">显示</option>
                    <option value="false">隐藏</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='table'">
                  <label>添加斑马纹</label>
                  <select v-model="selectedItem.Zebra">
                    <option value="true">显示</option>
                    <option value="false">隐藏</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='table' && selectedItem.Zebra=='true'">
                  <label>斑马纹颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.ZebraColor"
                            :key="1"
                            type="ZebraColor"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='table'">
                  <label>对齐方式</label>
                  <select v-model="selectedItem.Alignment">
                    <option value="left">左对齐</option>
                    <option value="center">居中对齐</option>
                    <option value="right">右对齐</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     style="position: relative;"
                     v-if="selectedItem.chartType=='table'">
                  <label>表格背景图</label>
                  <input type="file"
                         style="width: 147px!important;margin-right: 8px;"
                         accept="image/png, image/webp, image/jpeg, image/gif, image/jpg,image/svg+xml"
                         @change='changeImg' />
                  <label class="hoverTips"
                         data-toggle="tooltip"
                         :title='selectedItem.tableBackName'
                         style="position: absolute;float: left;height: 28px;left: 160px;width: 70px;overflow: hidden;">{{selectedItem.tableBackName}}</label>
                  <i class="icon-n-deleteNew delete_text"
                     @click="removeImg"></i>
                </div>
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
                     v-if="selectedItem.chartType=='table'">
                  <label>行高</label>
                  <input type="number"
                         min="36"
                         v-model="selectedItem.heightLinght">
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
                      <option :value="false">单色</option>
                      <option :value="true">渐变</option>
                    </select>
                    <div class="barGradient"
                         v-if="selectedItem.colorful"
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
                <div div
                     class="form-group cols2"
                     v-show="selectedItem.chartType==='border' && selectedItem.colorful">
                  <label>渐变方向</label>
                  <select v-model="selectedItem.directionLinear">
                    <option :value="180">上下</option>
                    <option :value="90">左右</option>
                  </select>
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
              <div v-if="['WuhoIfream'].includes(selectedItem.chartType)">
                <div class="m-gap form-group">视频选择</div>
                <div class="form-group cols2">
                  <label>视频名称</label>
                  <select v-model="selectedItem.CheckedVideoId">
                    <option v-for="(item,i) in VideoIdsArry" :key="i" :value="item.deviceIndexCode">{{item.name}}</option>
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
              <div class="form-group"
                   v-if="selectedItem.chartType=='decorator'">
                <div v-for="(item, index) in settingData.decoratorCase"
                     :key="index"
                     @click="chgImgSrc(item.imgSrc)"
                     :class="{'fl': true, 'font-case': true, 'act': selectedItem.imgSrc===item.imgSrc}">
                  <img :src="baseUrl + item.mini" />
                </div>
                <div @click="chgImgSrc('GreenEarth')"
                     :class="{'fl': true, 'font-case': true, 'act': selectedItem.imgSrc==='GreenEarth'}"><img src="../../../static/img/地球3.png"
                       alt=""></div>
                <div @click="chgImgSrc('VioletEarth')"
                     :class="{'fl': true, 'font-case': true, 'act': selectedItem.imgSrc==='VioletEarth'}"><img src="../../../static/img/地球2.png"
                       alt=""></div>
                <div @click="chgImgSrc('BlueEarth')"
                     :class="{'fl': true, 'font-case': true, 'act': selectedItem.imgSrc==='BlueEarth'}"><img src="../../../static/img/地球1.png"
                       alt=""></div>
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
              <!-- 迁徙图 -->
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
                <div class="form-group cols2"
                     v-if="selectedItem.chartType==='ve-line' || selectedItem.chartType==='ve-histogram' || selectedItem.chartType==='ve-bar'">
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
                <div class="form-group">{{selectedItem.chartType=='number' ?'数字':'字体'}}样式</div>
                <div class="form-group"
                     style="height: 30px;">
                  <div v-for="(item, index) in fontFaces"
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
                  <div class="form-group cols2"
                       v-if="selectedItem.ctLegendShow === 'true'">
                    <label>图例字颜色</label>
                    <div class="color-w200">
                      <Vcolor :data="selectedItem.ctLegendColor"
                              :key="10"
                              type="ctLegendColor"
                              @getdata="getColor"></Vcolor>
                    </div>
                  </div>
                  <div class="m-gap form-group">图表样式</div>
                  <!-- <div class="form-group cols2">
                    <label>主题</label>
                    <select v-model="selectedItem.themeType">
                      <option value="1">深色</option>
                      <option value="2">浅色</option>
                    </select>
                  </div> -->
                  <div class="form-group cols2">
                    <label>地图颜色</label>
                    <div class="color-w200">
                      <Vcolor :data="selectedItem.areaColor"
                              :key="10"
                              type="areaColor"
                              @getdata="getColor"></Vcolor>
                    </div>
                  </div>
                  <div class="form-group cols2">
                    <label>分界线颜色</label>
                    <div class="color-w200">
                      <Vcolor :data="selectedItem.borderColor"
                              :key="10"
                              type="borderColor"
                              @getdata="getColor"></Vcolor>
                    </div>
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
                  <div class="form-group cols2"
                       v-if="selectedItem.ctLegendShow === 'true'">
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
                  <div class="form-group cols2"
                       v-if="selectedItem.cityShow === 'true'">
                    <label>地名颜色</label>
                    <div class="color-w200">
                      <Vcolor :data="selectedItem.cityColor"
                              :key="23"
                              type="cityColor"
                              @getdata="getColor"></Vcolor>
                    </div>
                  </div>
                  <div class="form-group cols2"
                       v-if="selectedItem.cityShow === 'true'">
                    <label>字号</label>
                    <select v-model="selectedItem.fontSize">
                      <option value="8">8</option>
                      <option value="9">9</option>
                      <option value="10">10</option>
                      <option value="12">12</option>
                      <option value="13">13</option>
                    </select>
                  </div>
                  <!-- <div class="form-group cols2">
                    <label>主题</label>
                    <select v-model="selectedItem.themeType">
                      <option value="1">深色</option>
                      <option value="2">浅色</option>
                    </select>
                  </div> -->
                  <div class="form-group cols2">
                    <label>地图颜色</label>
                    <div class="color-w200">
                      <Vcolor :data="selectedItem.areaColor"
                              :key="10"
                              type="areaColor"
                              @getdata="getColor"></Vcolor>
                    </div>
                  </div>
                  <div class="form-group cols2">
                    <label>分界线颜色</label>
                    <div class="color-w200">
                      <Vcolor :data="selectedItem.borderColor"
                              :key="10"
                              type="borderColor"
                              @getdata="getColor"></Vcolor>
                    </div>
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
                  <div class="form-group cols2"
                       v-if="selectedItem.ctLegendShow === 'true'">
                    <label>图例字体大小</label>
                    <select v-model="selectedItem.ctLegendSize">
                      <option value="8">8</option>
                      <option value="10">10</option>
                      <option value="14">14</option>
                      <option value="16">16</option>
                      <option value="20">20</option>
                      <option value="24">24</option>
                      <option value="28">28</option>
                      <option value="30">30</option>
                    </select>
                  </div>
                  <div class="form-group cols2"
                       v-if="selectedItem.ctLegendShow === 'true'">
                    <label>图例字颜色</label>
                    <div class="color-w200">
                      <Vcolor :data="selectedItem.ctLegendColor"
                              :key="18"
                              type="ctLegendColor"
                              @getdata="getColor"></Vcolor>
                    </div>
                  </div>
                  <div class="form-group cols2"
                       v-if="selectedItem.ctLegendShow === 'true'">
                    <label>图例高度</label>
                    <input type="number"
                           max="100"
                           min="0"
                           v-model="selectedItem.legendY">
                  </div>
                  <div class="form-group cols2"
                       v-if=" ['v-line','ve-line', 've-histogram'].includes(selectedItem.chartType) ">
                    <label>图元上边距</label>
                    <input type="number"
                           max="50"
                           min="0"
                           v-model="selectedItem.gridTop">
                  </div>
                  <div class="form-group cols2"
                       v-if=" ['v-line','ve-line', 've-histogram'].includes(selectedItem.chartType) ">
                    <label>图元下边距</label>
                    <input type="number"
                           max="50"
                           min="0"
                           v-model="selectedItem.gridBotton">
                  </div>
                  <div class="form-group cols2"
                       v-if=" ['v-line','ve-line', 've-histogram'].includes(selectedItem.chartType) ">
                    <label>图元左边距</label>
                    <input type="number"
                           max="50"
                           min="0"
                           v-model="selectedItem.gridLeft">
                  </div>
                  <div class="form-group cols2"
                       v-if=" ['v-line','ve-line', 've-histogram'].includes(selectedItem.chartType) ">
                    <label>图元右边距</label>
                    <input type="number"
                           max="50"
                           min="0"
                           v-model="selectedItem.gridRight">
                  </div>
                  <div class="m-gap form-group">tips配置</div>
                  <div class="form-group cols2">
                    <label>tips可见性</label>
                    <select v-model="selectedItem.tooltipShow">
                      <option value="true">显示</option>
                      <option value="false">隐藏</option>
                    </select>
                  </div>
                  <div v-if="selectedItem.tooltipShow === 'true'">
                    <div class="form-group cols2">
                      <label>tips背景色</label>
                      <div class="color-w200">
                        <Vcolor :data="selectedItem.tooltipBackColor"
                                :key="18"
                                type="tooltipBackColor"
                                @getdata="getColor"></Vcolor>
                      </div>
                    </div>
                    <div class="form-group cols2">
                      <label>tips字体色</label>
                      <div class="color-w200">
                        <Vcolor :data="selectedItem.tooltipTextColor"
                                :key="18"
                                type="tooltipTextColor"
                                @getdata="getColor"></Vcolor>
                      </div>
                    </div>
                    <div class="form-group cols2">
                      <label>tips字体大小</label>
                      <input type="number"
                             v-model="selectedItem.tooltipfontSize">
                    </div>
                  </div>
                  <div v-if="selectedItem.chartType === 've-radar' || selectedItem.chartType === 've-line' || selectedItem.chartType === 've-bar' || selectedItem.chartType === 've-histogram'">
                    <div class="form-group cols2">
                      <label>坐标线可见性</label>
                      <select v-model="selectedItem.splitShow">
                        <option value="true">显示</option>
                        <option value="false">隐藏</option>
                      </select>
                    </div>
                    <div class="form-group cols2">
                      <label>坐标刻度类型</label>
                      <select v-model="selectedItem.minInterval">
                        <option value="">自适应</option>
                        <option value="1">整数</option>
                      </select>
                    </div>
                    <div class="form-group cols2">
                      <label>坐标线颜色</label>
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
                     v-if="selectedItem.chartType==='ve-gauge'">
                  <label>图例字体大小</label>
                  <select v-model="selectedItem.ctLegendSize">
                    <option value="14">14</option>
                    <option value="16">16</option>
                    <option value="20">20</option>
                    <option value="24">24</option>
                    <option value="28">28</option>
                    <option value="30">30</option>
                  </select>
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
                     v-if="selectedItem.chartType === 've-gauge'">
                  <label>文字颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.detailColor"
                            :key="24"
                            type="detailColor"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType === 've-gauge'||selectedItem.chartType === 've-ring'">
                  <label>环宽</label>
                  <div class="color-w200">
                    <input type="number"
                           max="50"
                           v-model="selectedItem.detailwidth">
                  </div>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType !== 've-radar' && selectedItem.chartType !== 've-pie'  && selectedItem.chartType !== 've-ring'">
                  <label v-if="selectedItem.chartType==='ve-line' || selectedItem.chartType==='ve-histogram' || selectedItem.chartType==='ve-bar'">坐标文字大小</label>
                  <select v-if="selectedItem.chartType==='ve-line' || selectedItem.chartType==='ve-histogram' || selectedItem.chartType==='ve-bar'"
                          v-model="selectedItem.axisLabelSize">
                    <option value="8">8</option>
                    <option value="10">10</option>
                    <option value="14">14</option>
                    <option value="16">16</option>
                    <option value="20">20</option>
                    <option value="24">24</option>
                    <option value="28">28</option>
                    <option value="30">30</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-if=" ['ve-line','ve-bar', 've-histogram'].includes(selectedItem.chartType) ">
                  <label>坐标单位颜色</label>
                  <div class="color-w200">
                    <Vcolor :data="selectedItem.DanweiColor"
                            :key="24"
                            type="DanweiColor"
                            @getdata="getColor"></Vcolor>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-if=" ['ve-line','ve-bar', 've-histogram'].includes(selectedItem.chartType) ">
                  <label>坐标单位大小</label>
                  <select v-model="selectedItem.DanweiSize">
                    <option value="8">8</option>
                    <option value="10">10</option>
                    <option value="14">14</option>
                    <option value="16">16</option>
                    <option value="20">20</option>
                    <option value="24">24</option>
                    <option value="28">28</option>
                    <option value="30">30</option>
                  </select>
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
                     v-if="selectedItem.chartType=='ve-line' && selectedItem.lineArea === 'true'">
                  <label>线条颜色</label>
                  <select v-model="selectedItem.lineColorType">
                    <option value="false">渐变</option>
                    <option value="true">单色</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='ve-line'">
                  <label>线条距Y轴距离</label>
                  <select v-model="selectedItem.boundaryGap">
                    <option value="true">开启</option>
                    <option value="false">关闭</option>
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
                  <label>线条宽度</label>
                  <select v-model="selectedItem.lineWidth">
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='ve-line'">
                  <label>数据线类型</label>
                  <select v-model="selectedItem.LineType">
                    <option value="solid">实线</option>
                    <option value="dashed">虚线</option>
                    <option value="dotted">点状线</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='ve-line'">
                  <label>数据点类型</label>
                  <select v-model="selectedItem.symbolType">
                    <option value="emptyCircle">空心圆点</option>
                    <option value="circle">圆点</option>
                    <option value="triangle">三角</option>
                    <option value="pin">气泡</option>
                    <option value="none">不显示</option>
                    <option value="pic">自定义图片</option>
                  </select>
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='ve-line' && selectedItem.symbolType === 'pic'">
                  <label>数据点图片</label>
                  <input type="file"
                         accept="image/png, image/webp, image/jpeg, image/gif, image/jpg,image/svg+xml"
                         @change='changeImg' />
                </div>
                <div class="form-group cols2"
                     v-if="selectedItem.chartType=='ve-line'">
                  <label>数据点大小</label>
                  <select v-model="selectedItem.symbolSize">
                    <option value="4">4</option>
                    <option value="6">6</option>
                    <option value="8">8</option>
                    <option value="10">10</option>
                    <option value="12">12</option>
                    <option value="14">14</option>
                    <option value="16">16</option>
                    <option value="20">20</option>
                    <option value="24">24</option>
                    <option value="28">28</option>
                    <option value="30">30</option>
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
                     v-if="selectedItem.chartType=='ve-line'">
                  <label>标点字大小</label>
                  <select v-model="selectedItem.PointSize">
                    <option value="14">14</option>
                    <option value="16">16</option>
                    <option value="20">20</option>
                    <option value="24">24</option>
                    <option value="28">28</option>
                    <option value="30">30</option>
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
                     v-if="selectedItem.chartType==='ve-pie' || selectedItem.chartType==='ve-ring'">
                  <label>图表文字连线</label>
                  <div class="color-w200">
                    <select v-model="selectedItem.showline">
                      <option value="false">隐藏</option>
                      <option value="true">显示</option>
                    </select>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-show="selectedItem.secondType !== 'liquidfill'">
                  <label>配色<i class="icon-n-tip hoverTips"
                       style="font-size: 16px; position: relative; top: 1px; left: 3px;"
                       data-toggle="tooltip"
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
                           accept="image/png, image/webp, image/jpeg, image/gif, image/jpg,image/svg+xml"
                           @change='changeImg' />
                  </div>
                </div>
              </div>

              <ChartStyle v-if="selectedItem.chartType && selectedItem.chartType.indexOf('ve-') > -1"
                          :configItems="selectedItem"
                          @change="changeChartStyle"></ChartStyle>

              <!-- <template v-if="['video'].includes(selectedItem.chartType)"> -->
              <!-- <div class="m-gap form-group">基础样式</div> -->
              <!-- <div class="form-group cols2"
                    v-for="(item, index) in config[selectedItem.chartType].styles.base" :key="`base_${index}`"
                  >
                    <label :style="item.tag === 'Hint'? 'font-weight: bold; color: #fff;':''">{{item.name}}</label>
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
              </template> -->

              <template v-if="['GradientPie','Sunrise','WoHoNumber','WuHolunboTab','WuhoYXHL','CYZBTX','WuhoPointBox','WuhoOpenBox','Newimage','Scatter','NewGroupLeftHistogram','NewGauge','NewBar','DownMenu','XiaLaShu','WuhoMaoBL','WuhouTable','WuhoIfream','NewRadar','polarBar','NewHistogram','DataFlow','BaiDuMap','NewPie','DoubleLinde','NewMarquee','ELine','ConditionalEline','ToggleButton','NewScatter','NewVMap','VmVareTopo','TDModel','NewNumber','Indicator','JSMpeg','NewBorder','NewTable','ConditionalTable','NewMoveTable','NewProgress','NewTime','NewGroupHistogram','NewDoubler','KLine','Dashboard','TDEarthLine','TDEarthBar','TreeMap','Ueditor','TDHistogram','NEWtextArea','BulletFrame', 'liquidfill', 'video', 'ppt', 'bubble','IntegratedHistogram','BiaxialBarChart','SwitchButton','IframePop','WordClouds','OrderMenu','StreetMenu','CityEvent','EventVenation','SmallOrderMenu','NewDropMenu'].includes(selectedItem.chartType)">
                <el-collapse v-model="activeNames"
                             class="form-group m-gap cols2">
                  <el-collapse-item :title="item.name"
                                    :name="index"
                                    v-show="itemShow(item)"
                                    v-for="(item, index) in config[selectedItem.chartType].default.styles.base"
                                    :key="`base_${index}`">
                    <div class="form-group Child"
                         v-for="(data, myindex) in item.childoption"
                         :key="`base_${myindex}`">
                      <ChildTag :item="data"
                                :selectedItem="selectedItem"
                                :selectChange="selectChange"
                                :chooseSameFlag='chooseSameFlag'></ChildTag>
                    </div>
                  </el-collapse-item>
                </el-collapse>
              </template>
            </div>

            <!--数据-->
            <div v-show="!showStyleTab"
                 class="full-height">
              <div class="form-group cols2"
                   v-show="['image', 'Newimage', 'text','NEWtextArea', 'hotspot'].includes(selectedItem.chartType)">
                <label>跳转大屏</label>
                <select v-model="selectedItem.linkId">
                  <option value="">无跳转</option>
                  <option v-for="item in allPageList"
                          :key="item.id"
                          :value="item.id">{{item.name}}</option>
                </select>
              </div>
              <div class="form-group cols2"
                   v-show="['SwitchButton'].includes(selectedItem.chartType)">
                <label>控制组件</label>
                <Select multiple v-model="selectedItem.bindCom">
                  <Option v-for="(item,index) in combinList"
                          :key="index"
                          v-show="item.comName"
                          :value="index.toString()">{{'组合-' +item.comName}}</Option>
                </Select>
              </div>
              <div v-show="['image','Newimage'].includes(selectedItem.chartType)">
                <div class="form-group cols2"
                     style="position: relative;">
                  <label>选择文件</label>
                  <input type="file"
                         style="width: 147px!important;margin-right: 8px;"
                         accept="image/png, image/webp, image/jpeg, image/gif, image/jpg,image/svg+xml"
                         @change='changeImg' />
                  <label class="hoverTips"
                         data-toggle="tooltip"
                         :title='selectedItem.imgName'
                         style="position: absolute;float: left;height: 28px;left: 160px;width: 70px;overflow: hidden;">{{selectedItem.imgName}}</label>
                  <i class="icon-n-deleteNew delete_text"
                     @click="removeImg"></i>
                </div>
              </div>
              <div v-show="['ppt', 'BulletFrame'].includes(selectedItem.chartType)">
                <div class="form-group cols2">
                  <label>选择文件</label>
                  <input type="file"
                         accept="image/png, image/webp, image/jpeg, image/gif, image/jpg,image/svg+xml"
                         @change='changeImg' />
                </div>
              </div>
              <!-- <div v-show="['JSMpeg'].includes(selectedItem.chartType)">
                <div class="form-group cols2">
                  <label>选择设备</label>
                  <select v-model="selectedItem.HcnetData">
                    <option v-for="(val,key) in AllHcnetData" :key="key" :value="val.id">{{val.name}}</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <label>选择通道</label>
                  <select v-model="selectedItem.VideoData">
                    <option v-for="(val,key) in AllVideoData" :key="key" :value="val.channel">{{val.ip}}</option>
                  </select>
                </div>
              </div> -->
              <div v-show="selectedItem.chartType == 'NewTime'">
                <div class="form-group cols2">
                  <label>取值来源</label>
                  <select v-model="selectedItem.timeSource">
                    <option value="local">客户端</option>
                    <option value="system">服务端</option>
                  </select>
                </div>
              </div>
              <div v-show="selectedItem.chartType == 'VmVareTopo'">
                <div class="form-group cols2">
                  <label>虚拟视图选择</label>
                  <select v-model="selectedItem.iputneId">
                    <option :value="item.id +'&'+ item.pId" v-for="(item,index) in iputneIdArry" :key="index">{{item.name}}</option>
                  </select>
                </div>
              </div>
              <div v-show="selectedItem.chartType == 'TDModel'">
                <div class="form-group cols2">
                  <label>选择模型</label>
                  <select v-model="selectedItem.gltfName">
                    <!-- gltfnameArray -->
                    <option :value="item.gltf" v-for="(item,index) in gltfnameArray" :key="index">{{item.name}}</option>
                  </select>
                </div>
                <div class="form-group cols2">
                  <button
                          @click="openUpload">导入模型</button>
                </div>
              </div>
              <div class="form-group"
                   v-if="selectedItem.chartType === 'Ueditor'">
                <UE :defaultMsg=selectedItem.chartData
                    ref="ue"></UE>
                <button class="DataChangeBtn"
                        @click="dataChange">更新视图</button>
              </div>
              <div style="height: 100%;"
                   v-show="!['TDModel','VmVareTopo','image', 'NewBorder', 'NewTime', 'video', 'ppt','BulletFrame', 'Ueditor','hotspot','SwitchButton'].includes(selectedItem.chartType)">
                <div class="form-group cols2">
                  <label>数据来源</label>
                  <select @change="chgDataSource"
                          v-model="selectedItem.ctDataSource">
                    <!-- <option value="static">静态数据</option> -->
                    <!-- <option value="system">系统数据</option> -->
                    <option v-for="(val,key) in dataSource"
                            :key="key"
                            :value="key === '静态数据' ? 'static' : (key === '系统数据' ? 'system' : key)">{{key}}</option>
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
                  <div class="form-group cols2"
                       contenteditable="false">
                    <label>选择接口</label>
                    <select ref="urlSel"
                            v-model="syst.curConf.url"
                            @change="chgUrl">
                      <option v-for="v in syst.urlSel"
                              :value="v.url"
                              :key="v.key">{{v.name}}</option>
                    </select>
                  </div>
                  <div id="mainSystemConf"
                       v-if="syst.urlSel.length>0">
                    <div class="form-group cols2"
                         v-for="(v,idx) in syst.curUrl"
                         :key="idx">
                      <label v-if="v.type=='drop-down' || v.type=='multi-select'"
                             class="e-legend">{{v.name}}<i class="icon-n-tip hoverTips"
                           v-if="v.title"
                           style="font-size: 16px; position: relative; top: 1px; left: 3px;"
                           data-toggle="tooltip"
                           :title="v.title"></i></label>
                      <Select2 v-if="v.type=='drop-down' || v.type=='multi-select'"
                               :name="v.key"
                               v-model="syst.curConf.params[v.key]"
                               :obj="v"
                               :selectObj="selectedItem"
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
                     v-if="selectedItem.ctDataSource === 'static' && selectedItem.chartType != 'v-map'&& selectedItem.chartType != 'NewVMap' && selectedItem.chartType != 'Ueditor'&& selectedItem.chartType!=='NewScatter' && selectedItem.chartType!=='v-scatter' && selectedItem.chartType != 'text'  && selectedItem.chartType != 'NEWtextArea' && selectedItem.chartType != 'marquee' && selectedItem.chartType != 'NewMarquee'">
                  <div ref="textareaData"
                       class="confData"
                       v-if="refreshData"
                       contenteditable="true">{{staticData}}</div>
                </div>
                <div class="form-group"
                     v-if="selectedItem.ctDataSource === 'static' && (selectedItem.chartType === 'NEWtextArea' || selectedItem.chartType === 'text' || selectedItem.chartType==='NewMarquee'|| selectedItem.chartType==='marquee')">
                  <div ref="textarea"
                       class="confData"
                       v-if="refreshData"
                       contenteditable="true">{{selectedItem.ctName}}</div>
                </div>
                <div class="MyUrlArry" v-if="selectedItem.ctDataSource === 'system' && selectedItem.moreUrlArry">
                    <div  class="advancedset" id="colorWhiteUrl" style="width: 250px;" v-for="(item,index) in selectedItem.moreUrlArry" :key="index">
                      <span style="width: 160px;display: inline-block;overflow: hidden;">{{item.name}}  </span>
                      <div class="deletOneUrl" @click="DeletOneUrl(index)" style="display: inherit;padding-left: 15px">
                        删除
                      </div>
                    </div>
                </div>
                <div class="form-group cols2"
                     style="width: 120px;"
                     v-if="(selectedItem.chartType==='NewTable' || selectedItem.chartType==='NewMoveTable' || selectedItem.chartType==='IntegratedHistogram') && selectedItem.ctDataSource === 'system'">
                  <label class="advancedset" style="width: 120px;" @click="AddNewUrl">新增数据接口</label>
                </div>
                <div class="form-group cols2"
                     style="text-align: center;"
                     v-if="selectedItem.chartType==='NewTable' || selectedItem.chartType==='NewMoveTable'">
                  <label :class="advanced? 'advancedset desc':'advancedset asc'"
                         @click="advanced = !advanced">高级设置</label>
                </div>
                <div v-if="advanced && (selectedItem.chartType==='NewTable' || selectedItem.chartType==='NewMoveTable')">
                  <div class="form-group cols2">
                    <label>字段</label>
                    <select v-model="selectedItem.AlarmField">
                      <option v-for="i in selectedItem.chartData.columns"
                              :key="i"
                              :value="i">{{i}}</option>
                    </select>
                  </div>
                  <div class="m-gap form-group">列宽配置</div>
                  <div class="form-group cols2"
                       v-if="selectedItem.AlarmField">
                    <label>列宽类型</label>
                    <select v-model="selectedItem.OneLineType">
                      <option value="default">默认列宽</option>
                      <option value="custom">自定义列宽</option>
                    </select>
                  </div>
                  <div class="form-group cols2"
                       v-if="selectedItem.OneLineType === 'custom' && selectedItem.AlarmField">
                    <label>字段列宽</label>
                    <input type="number"
                           v-model="selectedItem.OneLineSize">
                  </div>
                  <div class="m-gap form-group">告警配置</div>
                  <div class="form-group cols2"
                       v-if="selectedItem.AlarmField">
                    <label>告警条件</label>
                    <select v-model="selectedItem.AlarmType">
                      <option value="num">数字</option>
                      <option value="chart">字符</option>
                    </select>
                  </div>
                  <div class="form-group cols2"
                       v-if="selectedItem.AlarmType === 'chart'&&selectedItem.AlarmField">
                    <label>告警字符</label>
                    <input type="chart"
                           v-model="selectedItem.AlarmChart">
                  </div>
                  <div class="form-group cols2"
                       v-if="selectedItem.AlarmType === 'num'&&selectedItem.AlarmField">
                    <label>条件</label>
                    <select v-model="selectedItem.AlarmNumType">
                      <option value="greater">大于</option>
                      <option value="equal">等于</option>
                      <option value="less">小于</option>
                    </select>
                  </div>
                  <div class="form-group cols2"
                       v-if="selectedItem.AlarmType === 'num'&&selectedItem.AlarmField">
                    <label>阈值</label>
                    <input type="number"
                           v-model="selectedItem.AlarmNum">
                  </div>
                  <div class="form-group cols2"
                       v-if="selectedItem.AlarmField">
                    <label>告警颜色</label>
                    <div class="color-w200">
                      <Vcolor :data="selectedItem.AlarmColor"
                              :key="1"
                              type="AlarmColor"
                              @getdata="getColor"></Vcolor>
                    </div>
                  </div>
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
                <div v-show="selectedItem.chartType === 'v-map' || selectedItem.chartType==='NewVMap'|| selectedItem.chartType==='NewScatter'||selectedItem.chartType==='v-scatter'">
                  <div class="form-group cols2">
                    <label>展示范围</label>
                    <select v-model="selectedItem.mapLevel"
                            @change="chgMapLevel">
                      <option value="country">全国地图</option>
                      <option value="province">省级地图</option>
                      <option value="city">地市级地图</option>
                      <option value="county">区县级地图</option>
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
                  <div v-if="selectedItem.mapLevel==='city' || selectedItem.mapLevel==='county'"
                       @click="chgMapLevel"
                       class="form-group cols2">
                    <label>市</label>
                    <Select2 v-model="selectedItem.cityCode"
                             :mapSelect="true"
                             :obj="cityArr"
                             @input="chgCity(selectedItem.cityCode)"></Select2>
                  </div>
                  <div v-if="selectedItem.mapLevel==='county'"
                       @click="chgMapLevel"
                       class="form-group cols2">
                    <label>区/县</label>
                    <Select2 v-model="selectedItem.countyCode"
                             :mapSelect="true"
                             :sameName="true"
                             :disData="'nodata'"
                             :obj="countyArr"
                             @input="chgCounty(selectedItem.countyCode)"></Select2>
                  </div>
                  <div class="form-group cols2"
                       v-if="(selectedItem.chartType==='v-scatter' || selectedItem.chartType==='NewScatter') && selectedItem.ctDataSource == 'static'">
                    <label class="e-legend">数据设置<i class="icon-n-tip hoverTips"
                         style="font-size: 16px; position: relative; top: 1px; left: 3px;"
                         data-toggle="tooltip"
                         title="对每一个数据点所在的地区设置告警级别"></i></label><button class="addData"
                            @click="addAlertLevel">添加数据点</button><input type="file"
                           accept="Json/*"
                           class="jsonUpload"
                           v-show="false"
                           @change="uploadJson"
                           id="uploadJson">
                    <button class="addData county"
                            @click="openJsonClick"
                            v-show="areaArr.length==0 && selectedItem.mapLevel=='county'">上传区县数据</button>

                    <!-- <button type="button" class="colorToall" @click="addAlertLevel">添加数据点</button> -->
                  </div>

                  <div class="form-group cols2"
                       v-show="!(selectedItem.chartType==='v-scatter' || selectedItem.chartType==='NewScatter') && selectedItem.ctDataSource == 'static'">
                    <label class="e-legend">数据设置<i class="icon-n-tip hoverTips"
                         style="font-size: 16px; position: relative; top: 1px; left: 3px;"
                         data-toggle="tooltip"
                         title="设置每个地区的分布数量"></i></label><input type="file"
                           accept="Json/*"
                           class="jsonUpload"
                           v-show="false"
                           @change="uploadJson"
                           id="uploadJson">
                    <button class="addData county"
                            @click="openJsonClick"
                            v-show="areaArr.length==0 && selectedItem.mapLevel=='county'">上传区县数据</button>
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
                     v-if="selectedItem.ctDataSource !== 'static'&&selectedItem.chartType !== 'JSMpeg'">
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
                     v-if="selectedItem.ctDataSource !== 'static'&&selectedItem.chartType !== 'JSMpeg'"
                     style="display: none;">
                  <div class="form-group"
                       contenteditable="false"
                       style="position: relative">
                    <label></label>
                  </div>
                </div>
                <div class="form-group cols2"
                     v-show="selectedItem.chartType==='v-map' || selectedItem.chartType==='NewVMap'"
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
                       data-toggle="tooltip"
                       title="设置数据的区间。分布数量处于不同区间的地区，展示颜色会有差别"
                       style="font-size: 16px; position: relative; top: 1px; left: 3px;"></i></label>
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
                     v-if="(selectedItem.chartType==='v-scatter' || selectedItem.chartType==='NewScatter') && selectedItem.ctDataSource == 'static'">
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
                <div v-show="selectedItem.chartType==='NewTable' || selectedItem.chartType==='NewMoveTable' || selectedItem.chartType==='IntegratedHistogram'">
                  <div class="form-group cols2">
                    <label>接口间隔(s)</label>
                    <input type="number"
                            v-model="selectedItem.intervieData">
                  </div>
                </div>
                <button class="DataChangeBtn"
                        @click="dataChange">更新视图</button>

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
                       style="position: relative;"
                       v-show="selectedItem.videoType === 'local'">
                    <label>选择文件</label>
                    <input type="file"
                           style="width: 147px!important;margin-right: 8px;"
                           name="myfiles"
                           id="myfiles"
                           accept="video/*"
                           @change="uploadVideo">
                    <label class="hoverTips"
                           data-toggle='tooltip'
                           :title='selectedItem.VideoName'
                           style="position: absolute;float: left;height: 28px;left: 160px;width: 70px;overflow: hidden;">{{selectedItem.VideoName}}</label>
                    <i class="icon-n-deleteNew delete_text"
                       @click="removeImg"></i>
                  </div>
                </div>
                <button @click="videoChange"
                        style="margin-top: 30px">更新视图</button>
              </div>
              <template v-if="selectedItem.chartType == 'ppt' || selectedItem.chartType == 'BulletFrame'">
                <draggable class="img_src_list"
                           v-model="selectedItem.srcList"
                           handle=".handle">
                  <div v-for="(item, index) in selectedItem.srcList"
                       :key="index"
                       :class="imgHeightLight==index?'heightImgName':''">
                    <span class="src_item handle"
                          @click="activeSrcList(index)">{{item.name}}</span>
                    <span class="delete_text"
                          @click="deleteSrcList(index)">删除</span>
                  </div>
                </draggable>
              </template>
            </div>
          </div>
        </div>
        <div class="scaleBox">
          <span>缩放比例</span>
          <Slider :min="20"
                  :max="200"
                  v-model="paintObj.scale"></Slider>
        </div>
        <HawkEye v-if="ShowHawkEye"
                 :scale="paintObj.scale"
                 :boxTop="HawkEyeStyle.top"
                 :boxLeft="HawkEyeStyle.left"
                 :bgTop="paintObj.top"
                 :bgLeft="paintObj.left"></HawkEye>

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
      <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h4 class="modal-title">配置资源指标详情</h4>
            <button type="button"
                    class="close"
                    data-dismiss="modal"
                    aria-hidden="true">
              <i class="ivu-icon ivu-icon-ios-close"></i></button>
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
<style lang="scss" scoped>
#mainEdit-edit {
  input[type="file"] {
    color: transparent !important;
  }
  .delete_text {
    &:hover {
      cursor: pointer;
      color: red;
    }
  }
}
.handle_label {
  width: 100%;
  height: 38px;
  line-height: 38px;
  padding: 0 15px;
  border-bottom: solid 1px #383f54;
  color: #898da8;
  font-size: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
<style lang="scss">
.county {
  position: absolute;
  right: 130px;
}
#InputModelBox{
  .ivu-modal{
    .ivu-modal-content{
      background: #222739;
    }
    .ivu-modal-header {
      height: 40px;
      color: #1b2031;
      box-shadow: 0px 0px 20px 0px #0d080966;
      border: none;
      background-color: transparent;
      .ivu-modal-header-inner{
        color: #c2c6d7;
      }
    }
    .ivu-modal-body {
      padding: 20px 20px 10px 50px;
      color: #c2c6d7;
      margin-bottom: 0px;
    }
  }
}
.img_src_list {
  .src_item {
    // padding-left: 8px;
    line-height: 25px;
    // color: red;
    cursor: move;
    display: inline-block;
    width: calc(100% - 37px);
  }
  .upload {
    width: 145px !important;
  }
  .delete_text {
    cursor: pointer;
    // color: #0088cc;
    &:hover {
      color: red;
    }
  }
}
.src_item {
  z-index: 100;
}
html[data-theme="blackWhite"],
html[data-theme="blueWhite"] {
  .select2-container--default
    .select2-selection--single
    .select2-selection__rendered {
    color: #444 !important;
  }
}
.m-tabMain {
  .el-collapse-item__header {
    background-color: transparent;
    border-bottom: 1px solid transparent;
    font-weight: bold;
    color: inherit;
  }
  .el-collapse-item__wrap {
    background-color: transparent;
    border-bottom: 1px solid transparent;
  }
  .el-collapse-item__content {
    padding-top: 0px !important;
    padding-bottom: 0px !important;
    margin-top: 25px !important;
    color: inherit;
  }
  .el-collapse {
    border-top: 1px solid transparent;
    border-bottom: 1px solid transparent;
  }
}
#chooseWrap .vue-ruler-wrapper {
  z-index: 50;
}
.m-tabMain::-webkit-scrollbar {
  width: 0 !important;
}
.m-tabMain .desc {
  background-image: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAsAAAALCAYAAACprHcmAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAC3SURBVChTtY49DoJAEIXfILKN8Sc2UFpY21B5HuMZ4ATScyQbC6i9AaWJoQA2Zsf9g3AA/YrNzHtvXwa/pai2fgKZR9yqZiAkVplhzIAI533Y3i+ndWDEa7M5aIPNPKIzNrhSEiZoNBsuy+MQMT3NPKG/ClLoEXnBn+FgCou6/8C5C93KzFBZOmVss4OYQA+/QOlgvMTbr5ZZsyMq6pcE73R9K7PU3joya3bknYhDkMy7JPbS3wG+t2IugdRfUNsAAAAASUVORK5CYII=") !important;
}
.m-tabMain .asc {
  background-image: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAsAAAALCAYAAACprHcmAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAACaSURBVChTY6AfqK9/ysXcceZXff0VNqgQHDBBaTho43zx8i8DA2s7589XUCE4QFHM1nHm1G+G/zwg9i+G//xsneeOgCWgAEnxf8Y/DAxGUA4Y/P3/zwwkDuUiFLN3nLv1j4GBGcoFA5BzgOLXoVyI4rKyG7w/Gf6rgEXQAFBcPTf3NjuIDVbcJ/D5PojGBWZIfXwIZdIEMDAAAIACLdN1yz7JAAAAAElFTkSuQmCC") !important;
}
.m-tabMain .advancedset {
  color: #0088cc;
  background-position: right;
  background-repeat: no-repeat;
  padding-right: 10px;
  cursor: pointer;
  vertical-align: top;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
}
#colorWhiteUrl{
  color: white;
  .deletOneUrl{
    color: #0088cc ;
  }
}
.ringparent{
  display: flex;
  flex-wrap: nowrap;
}
.edit-body .btm-tools {
  margin-bottom: -3px;
  position: fixed;
  bottom: 26px;
  width: 100%;
  padding-right: 380px;
  padding-left: 380px;
  z-index: 999;
  text-align: right;
  .ringparent{
    display: flex;
  }
  .ring-icon {
    // display: inline-block;
    display: flex;
    justify-content: center;
    align-items: center;
    width: 30px;
    height: 30px;
    background: #101425;
    border-radius: 50%;
    opacity: 0.3;
    margin: 0 4px;
    cursor: pointer;
  }
  .ring-icon:hover {
    // background: #0f1321;
    opacity: 1;
  }
}
.heightImgName {
  color: #0088cc;
}
#resourcesIds input{
  width: 100%;
}
#resourcesIds{
  .ivu-select-dropdown{
    background-color: black;
    .ivu-select-item{
      color: white;
    }
  }
}
.edui-default {
  ::deep .edui-editor-bottomContainer {
    display: none;
  }
}
.edui-default {
  ::deep .edui1677_message_holder {
    display: none;
  }
}
</style>
