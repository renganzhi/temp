<template>
  <div class="WuhoIfream" :style="headStyle">
    <div :id="boxId" class="playWnd"></div>
    <!-- <iframe style="height:100%;width:100%" src="http://183.131.193.69:8181/appli/start?appliId=934133524695351296&codeRate=8000&frameRate=30" frameborder="0"></iframe> -->
  </div>
</template>
<script>
export default {
  name: "WuhoIfream",
  props: ["item"],
  data() {
    return {
      visible: false,
      boxId: 1,
      oWebControl: "",
      nowScale:1
    };
  },
  computed: {
    headStyle () {
      return {
        height: this.item.height + 'px !important',
        width:this.item.width + 'px !important',
      }
    }
  },
  watch: {
    "item.width": function () {
      var _this = this;
      this.$nextTick(() => {
        _this.oWebControl.JS_Resize((this.item.width-40)*this.nowScale, (this.item.height-40)*this.nowScale);
      });
    },
    "item.chartData.hkwsid": function () {
      var _this = this;
      console.log(this.item.chartData.hkwsid)
        var cameraIndexCode = this.item.chartData.hkwsid; //获取输入的监控点编号值，必填
        var streamMode = 0; //主子码流标识：0-主码流，1-子码流
        var transMode = 1; //传输协议：0-UDP，1-TCP
        var gpuMode = 0; //是否启用GPU硬解，0-不启用，1-启用
        var wndId = -1; //播放窗口序号（在2x2以上布局下可指定播放窗口）

        cameraIndexCode = cameraIndexCode.replace(/(^\s*)/g, "");
        cameraIndexCode = cameraIndexCode.replace(/(\s*$)/g, "");

        _this.oWebControl.JS_RequestInterface({
          funcName: "startPreview",
          argument: JSON.stringify({
            cameraIndexCode: cameraIndexCode, //监控点编号
            streamMode: streamMode, //主子码流标识
            transMode: transMode, //传输协议
            gpuMode: gpuMode, //是否开启GPU硬解
            wndId: wndId, //可指定播放窗口
          }),
        });
    },
    "nowScale": function () {
      var _this = this;
      this.$nextTick(() => {
        _this.oWebControl.JS_Resize((this.item.width-40)*this.nowScale, (this.item.height-40)*this.nowScale);
      });
    },
    "item.height": function () {
      var _this = this;
      this.$nextTick(() => {
        _this.oWebControl.JS_Resize((this.item.width-40)*this.nowScale, (this.item.height-40)*this.nowScale);
      });
    },
  },
  mounted() {
    if(document.querySelector('.paint-bg')){
      this.nowScale = document.querySelector('.paint-bg').style.transform.replace('scale(','').replace(')','')*1
    } else if(document.querySelector('.pagebox')){
      this.nowScale = document.querySelector('.pagebox').style.transform.replace('scale(','').replace(')','').split(',')[0]*1
    }
    this.boxId =
      Number(Math.random().toString().substr(2, 9) + Date.now()).toString(36) +
      "";
    this.start();
    setInterval(() => {
      if(document.querySelector('.paint-bg')){
        this.nowScale = document.querySelector('.paint-bg').style.transform.replace('scale(','').replace(')','')*1
      } else if(document.querySelector('.pagebox')){
        this.nowScale = document.querySelector('.pagebox').style.transform.replace('scale(','').replace(')','').split(',')[0]*1
      }
      console.log(this.nowScale)
    }, 1000);
  },
  methods: {
    start() {
      var _this = this;
      //页面加载时创建播放实例初始化
      // $(window).load(function () {
      initPlugin();
      // });

      //声明公用变量
      var initCount = 0;
      var pubKey = "";

      // 创建播放实例
      function initPlugin() {
        _this.oWebControl = new WebControl({
          szPluginContainer: _this.boxId, // 指定容器id
          iServicePortStart: 15900, // 指定起止端口号，建议使用该值
          iServicePortEnd: 15909,
          szClassId: "23BF3B0A-2C56-4D97-9C03-0CB103AA8F11", // 用于IE10使用ActiveX的clsid
          cbConnectSuccess: () => {
            // 创建WebControl实例成功
            _this.oWebControl
              .JS_StartService("window", {
                // WebControl实例创建成功后需要启动服务
                dllPath: "./VideoPluginConnect.dll", // 值"./VideoPluginConnect.dll"写死
              })
              .then(
                function () {
                  // 启动插件服务成功
                  _this.oWebControl.JS_SetWindowControlCallback({
                    // 设置消息回调
                    cbIntegrationCallBack: cbIntegrationCallBack,
                  });

                  _this.oWebControl
                    .JS_CreateWnd(_this.boxId, 200, 200)
                    .then(function () {
                      //JS_CreateWnd创建视频播放窗口，宽高可设定
                      init(); // 创建播放实例成功后初始化
                    });
                },
                function () {
                  // 启动插件服务失败
                }
              );
          },
          cbConnectError: function () {
            // 创建WebControl实例失败
            _this.oWebControl = null;
            $(`#${_this.boxId}`).html("插件未启动，正在尝试启动，请稍候...");
            WebControl.JS_WakeUp("VideoWebPlugin://"); // 程序未启动时执行error函数，采用wakeup来启动程序
            initCount++;
            if (initCount < 3) {
              setTimeout(function () {
                initPlugin();
              }, 3000);
            } else {
              $(`#${_this.boxId}`).html("插件启动失败，请检查插件是否安装！");
            }
          },
          cbConnectClose: function (bNormalClose) {
            // 异常断开：bNormalClose = false
            // JS_Disconnect正常断开：bNormalClose = true
            console.log("cbConnectClose");
            _this.oWebControl = null;
          },
        });
      }

      // 设置窗口控制回调
      function setCallbacks() {
        _this.oWebControl.JS_SetWindowControlCallback({
          cbIntegrationCallBack: cbIntegrationCallBack,
        });
      }

      // 推送消息
      function cbIntegrationCallBack(oData) {
        console.log(JSON.stringify(oData.responseMsg));
      }

      //初始化
      function init() {
        getPubKey(function () {
          ////////////////////////////////// 请自行修改以下变量值	////////////////////////////////////
          var appkey = "24183731";                           //综合安防管理平台提供的appkey，必填
          var secret = setEncrypt("babYTegRFvTRymoWubNS");   //综合安防管理平台提供的secret，必填
          var ip = "172.16.152.136";                           //综合安防管理平台IP地址，必填
          var playMode = 0;                                  //初始播放模式：0-预览，1-回放
          var port = 443;                                    //综合安防管理平台端口，若启用HTTPS协议，默认443
          var snapDir = "D:\\SnapDir";                       //抓图存储路径
          var videoDir = "D:\\VideoDir";                     //紧急录像或录像剪辑存储路径
          var layout = "1x1";                                //playMode指定模式的布局
          var enableHTTPS = 1;                               //是否启用HTTPS协议与综合安防管理平台交互，这里总是填1
          var encryptedFields = 'secret';					   //加密字段，默认加密领域为secret
          var showToolbar = 0;                               //是否显示工具栏，0-不显示，非0-显示
          var showSmart = 0;                                 //是否显示智能信息（如配置移动侦测后画面上的线框），0-不显示，非0-显示
          var buttonIDs = "0,16,256,257,258,259,260,512,513,514,515,516,517,768,769";  //自定义工具条按钮
          ////////////////////////////////// 请自行修改以上变量值	////////////////////////////////////

          _this.oWebControl
            .JS_RequestInterface({
              funcName: "init",
              argument: JSON.stringify({
                appkey: appkey, //API网关提供的appkey
                secret: secret, //API网关提供的secret
                ip: ip, //API网关IP地址
                playMode: playMode, //播放模式（决定显示预览还是回放界面）
                port: port, //端口
                snapDir: snapDir, //抓图存储路径
                videoDir: videoDir, //紧急录像或录像剪辑存储路径
                layout: layout, //布局
                enableHTTPS: enableHTTPS, //是否启用HTTPS协议
                encryptedFields: encryptedFields, //加密字段
                showToolbar: showToolbar, //是否显示工具栏
                showSmart: showSmart, //是否显示智能信息
                buttonIDs: buttonIDs, //自定义工具条按钮
              }),
            })
            .then(function (oData) {
              _this.oWebControl.JS_Resize((_this.item.width-40)*_this.nowScale, (_this.item.height-40)*_this.nowScale);
            });
        });
      }

      //获取公钥
      function getPubKey(callback) {
        _this.oWebControl
          .JS_RequestInterface({
            funcName: "getRSAPubKey",
            argument: JSON.stringify({
              keyLength: 1024,
            }),
          })
          .then(function (oData) {
            if (oData.responseMsg.data) {
              pubKey = oData.responseMsg.data;
              callback();
    
              // var cameraIndexCode = "ff1fe21b20504ab68c4ae53c7cef99e3"; //获取输入的监控点编号值，必填
              if(_this.item.chartData.hkwsid !== ''){
                var cameraIndexCode = _this.item.chartData.hkwsid; //获取输入的监控点编号值，必填
                var streamMode = 0; //主子码流标识：0-主码流，1-子码流
                var transMode = 1; //传输协议：0-UDP，1-TCP
                var gpuMode = 0; //是否启用GPU硬解，0-不启用，1-启用
                var wndId = -1; //播放窗口序号（在2x2以上布局下可指定播放窗口）

                cameraIndexCode = cameraIndexCode.replace(/(^\s*)/g, "");
                cameraIndexCode = cameraIndexCode.replace(/(\s*$)/g, "");

                _this.oWebControl.JS_RequestInterface({
                  funcName: "startPreview",
                  argument: JSON.stringify({
                    cameraIndexCode: cameraIndexCode, //监控点编号
                    streamMode: streamMode, //主子码流标识
                    transMode: transMode, //传输协议
                    gpuMode: gpuMode, //是否开启GPU硬解
                    wndId: wndId, //可指定播放窗口
                  }),
                });
              }
            }
          });
      }

      //RSA加密
      function setEncrypt(value) {
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey(pubKey);
        return encrypt.encrypt(value);
      }

      // 监听resize事件，使插件窗口尺寸跟随DIV窗口变化
      $(window).resize(function () {
        if (_this.oWebControl != null) {
              _this.oWebControl.JS_Resize((_this.item.width-40)*_this.nowScale, (_this.item.height-40)*_this.nowScale);
          setWndCover();
        }
      });

      // 监听滚动条scroll事件，使插件窗口跟随浏览器滚动而移动
      $(window).scroll(function () {
        if (_this.oWebControl != null) {
              _this.oWebControl.JS_Resize((_this.item.width-40)*_this.nowScale, (_this.item.height-40)*_this.nowScale);
          setWndCover();
        }
      });

      // 设置窗口裁剪，当因滚动条滚动导致窗口需要被遮住的情况下需要JS_CuttingPartWindow部分窗口
      function setWndCover() {
        var iWidth = $(window).width();
        var iHeight = $(window).height();
        var oDivRect = $(`#${_this.boxId}`).get(0).getBoundingClientRect();

        var iCoverLeft = oDivRect.left < 0 ? Math.abs(oDivRect.left) : 0;
        var iCoverTop = oDivRect.top < 0 ? Math.abs(oDivRect.top) : 0;
        var iCoverRight =
          oDivRect.right - iWidth > 0 ? Math.round(oDivRect.right - iWidth) : 0;
        var iCoverBottom =
          oDivRect.bottom - iHeight > 0
            ? Math.round(oDivRect.bottom - iHeight)
            : 0;

        iCoverLeft = iCoverLeft > 1000 ? 1000 : iCoverLeft;
        iCoverTop = iCoverTop > 600 ? 600 : iCoverTop;
        iCoverRight = iCoverRight > 1000 ? 1000 : iCoverRight;
        iCoverBottom = iCoverBottom > 600 ? 600 : iCoverBottom;

        _this.oWebControl.JS_RepairPartWindow(0, 0, 1001, 600); // 多1个像素点防止还原后边界缺失一个像素条
        if (iCoverLeft != 0) {
          _this.oWebControl.JS_CuttingPartWindow(0, 0, iCoverLeft, 600);
        }
        if (iCoverTop != 0) {
          _this.oWebControl.JS_CuttingPartWindow(0, 0, 1001, iCoverTop); // 多剪掉一个像素条，防止出现剪掉一部分窗口后出现一个像素条
        }
        if (iCoverRight != 0) {
          _this.oWebControl.JS_CuttingPartWindow(
            1000 - iCoverRight,
            0,
            iCoverRight,
            600
          );
        }
        if (iCoverBottom != 0) {
          _this.oWebControl.JS_CuttingPartWindow(
            0,
            600 - iCoverBottom,
            1000,
            iCoverBottom
          );
        }
      }

      //停止全部预览
      $("#stopAllPreview").click(function () {
        _this.oWebControl.JS_RequestInterface({
          funcName: "stopAllPreview",
        });
      });

      // 标签关闭
      $(window).unload(function () {
        if (_this.oWebControl != null) {
          _this.oWebControl.JS_HideWnd(); // 先让窗口隐藏，规避可能的插件窗口滞后于浏览器消失问题
          _this.oWebControl.JS_Disconnect().then(
            function () {
              // 断开与插件服务连接成功
            },
            function () {
              // 断开与插件服务连接失败
            }
          );
        }
      });
    },
  },
  beforeDestroy() {
    if (this.oWebControl) {
      this.oWebControl.JS_HideWnd(); // 先让窗口隐藏，规避可能的插件窗口滞后于浏览器消失问题
      this.oWebControl.JS_Disconnect();
    }
  },
};
</script>
<style scoped lang="scss">
.WuhoIfream {
  width: 100%;
  height: 100%;
  padding: 20px;
}
#playWnd {
  width: 100%;
  height: 100%;
}
</style>
