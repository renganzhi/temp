<template>
  <div class="content">
    <button v-if="fasle" @click="getCamera" style="position:absolute;z-index:999;width:100px;height:80px;top:0px;left:0px;">获取视角</button>
    <div id="pop" v-show="popshow">
      <div class="poptitle">
        小旅馆
      </div>
      <div class="CloseBtn" @click="popshow = false"></div>
      <div class="lineContain">
        <div class="line">名称: 小旅馆</div>
        <div class="line">标准地址:武侯祠大街252号5-4-204</div>
        <div class="line">房间数:2</div>
        <div class="line">床铺数:8</div>
        <div class="line">社区民警(电话):陈朝林(17708192501)</div>
        <div class="line">网格员(电话):张敏(13194994003)</div>
        <div class="line">
          微消站(电话):刘长城(15700573360)
        </div>
      </div>
    </div>
    <div id="popBig" v-show="popshowBig">
      <div class="poptitle">
        小旅馆
      </div>
      <div class="CloseBtn" @click="popshowBig = false"></div>
      <div class="lineContain">
        <div class="line">名称: 小旅馆</div>
        <div class="line">标准地址:武侯祠大街252号5-4-204</div>
        <div class="line">房间数:2</div>
        <div class="line">床铺数:8</div>
        <div class="line">社区民警(电话):陈朝林(17708192501)</div>
        <div class="line">网格员(电话):张敏(13194994003)</div>
        <div class="line">
          微消站(电话):刘长城(15700573360)
        </div>
      </div>
    </div>
    <div id="cesiumContainer" />
  </div>
</template>

<script>
import createBlurStage from './CesiumEdgeStage/createBlurStage.js'
var viewer
var tileset
var contrastBias
var baseObject
export default {
  name: 'pageShow',
  props: ['nowPageID'],
  data () {
    return {
      popshow: false,
      popshowBig: false,
      x: 0,
      y: 0,
      z: 0
    }
  },
  computed: {
    pageIsJXJ () {
      let idArry = [118, 120, 119, 117, 127, 130, 133, 128]
      return idArry.indexOf(this.nowPageID) > -1
    }
  },
  watch: {

  },
  mounted () {
    this.init3D()
    this.initLine()
    this.initModels()
    this.initPostrender()
    this.addPoints()
    this.addPopEvent()
    this.fly()
    setTimeout(() => {
      this.fly()
    }, 2000)
    setTimeout(() => {
      this.fly()
    }, 4000)
  },
  methods: {
    addPopEvent () {
      var that = this
      function pop () {
        let container = document.getElementById('pop')
        if (container) {
          var windowPosition = new Cesium.Cartesian2()
          var canvasHeight = viewer.scene.canvas.height
          var canvasWidth = viewer.scene.canvas.width
          Cesium.SceneTransforms.wgs84ToWindowCoordinates(
            viewer.scene,
            Cesium.Cartesian3.fromDegrees(
              that.x,
              that.y,
              that.z + 100
            ),
            windowPosition
          )
          container.style.bottom = canvasHeight - windowPosition.y + 'px'
          container.style.right = canvasWidth - windowPosition.x - container.offsetWidth * 0.5 + 'px'
        /* container.style.left = windowPosition.x  + "px"; */
        }
        let containerbig = document.getElementById('popBig')
        if (containerbig) {
          var windowPosition = new Cesium.Cartesian2()
          var canvasHeight = viewer.scene.canvas.height
          var canvasWidth = viewer.scene.canvas.width
          Cesium.SceneTransforms.wgs84ToWindowCoordinates(
            viewer.scene,
            Cesium.Cartesian3.fromDegrees(
              that.x,
              that.y,
              that.z + 100
            ),
            windowPosition
          )
          containerbig.style.bottom = canvasHeight - windowPosition.y + 'px'
          containerbig.style.right = canvasWidth - windowPosition.x - containerbig.offsetWidth * 0.5 + 'px'
        /* container.style.left = windowPosition.x  + "px"; */
        }
      }
      viewer.scene.preRender.addEventListener(pop)
    },
    addPoints () {
      let height = 90
      this.addPointer(Cesium.Cartesian3.fromDegrees(103.9560087384879, 30.621067454297123, 30 + height))
      this.addPointer(Cesium.Cartesian3.fromDegrees(103.97744811147976, 30.630154473334926, 50 + height))
      this.addPointer(Cesium.Cartesian3.fromDegrees(103.97507519417769, 30.601843507403853, 30 + height))
      this.addPointer(Cesium.Cartesian3.fromDegrees(103.98045377835918, 30.636954326786963, 30 + height))
      this.addPointer(Cesium.Cartesian3.fromDegrees(103.99955353582837, 30.616759314294566, 30 + height))
      this.addPointer(Cesium.Cartesian3.fromDegrees(104.00787939994709, 30.6292473728405, 30 + height))
      this.addPointer(Cesium.Cartesian3.fromDegrees(103.97129887928244, 30.647984750384545, 50 + height))
    },
    addPointer (position) {
      viewer.entities.add({
        position,
        billboard: {
          image: 'static/img/click.png',
          scale: 0.4
        }
      })
    },
    fly () {
      viewer.scene.camera.flyTo({
        destination: Cesium.Cartesian3.fromDegrees(
          104.08993840769945,
          30.583815387362105,
          32859.13617687835
        ),
        orientation: {
          heading: 6.283185307179586,
          pitch: -1.570785738725554,
          roll: 0
        },
        duration: 1
      })
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
      var blur1 = createBlurStage('Blur1', 3, 3, 1)
      var blur2 = createBlurStage('Blur2', 5, 5, 0.5)
      var blur3 = createBlurStage('Blur3', 7, 7, 0.25)
      var blur4 = createBlurStage('Blur4', 9, 9, 0.125)
      var blur5 = createBlurStage('Blur5', 11, 11, 0.0625)
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
          bloomFators: [1.0, 0.5, 0.25, 0.125, 0.0625],
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
      setTimeout(() => {
        var pickId = viewer.scene.primitives._primitives[0]._primitives[0]._primitives[1]._pickIds[0]
        baseObject = {
          pickId: pickId
        }
        contrastBias.selected = [baseObject]
      }, 2000)
    },
    initLine () {
      $.getJSON('./static/geojson/bianjie.json', (res) => {
        // console.log(res)
        let positions = res.features[0].geometry.coordinates[0][0]
        // console.log(positions)
        let linepositions = []
        positions.forEach(item => {
          linepositions.push(item[0])
          linepositions.push(item[1])
          linepositions.push(10)
        })
        viewer.entities.add({
          polyline: {
            positions: Cesium.Cartesian3.fromDegreesArrayHeights(linepositions),
            material: new Cesium.PolylineDashMaterialProperty({
              color: Cesium.Color.DODGERBLUE,
              dashLength: 10,
              dashPattern: 255
            }),
            width: 1
          }
        })
      })
      $.getJSON('./static/geojson/line.json', (res) => {
        let positions = res.features
        positions.forEach((item, index) => {
          if (item.geometry.type === 'LineString') {
            let linepositions = []
            item.geometry.coordinates.forEach(item => {
              linepositions.push(item[0])
              linepositions.push(item[1])
              linepositions.push(2)
            })
            viewer.entities.add({
              polyline: {
                positions: Cesium.Cartesian3.fromDegreesArrayHeights(linepositions),
                depthFailMaterial: new Cesium.PolylineFlowMaterialProperty({
                  color: Cesium.Color.fromCssColorString('#00df61'),
                  duration: 200
                }),
                material: new Cesium.PolylineFlowMaterialProperty({
                  color: Cesium.Color.fromCssColorString('#00df61'),
                  duration: 200
                }),
                width: 2
              }
            })
          }
        })
      })
    },
    initModels () {
      tileset = new Cesium.Cesium3DTileset({
        url: './static/wuhou/tileset.json',
        lightColor: new Cesium.Cartesian3(20, 20, 20),
        showOutline: false
      })
      viewer.scene.primitives.add(tileset)
      tileset.readyPromise.then(function (tileset) {
        var boundingSphere = tileset.boundingSphere
        viewer.camera.viewBoundingSphere(boundingSphere, new Cesium.HeadingPitchRange(0, -2.0, 0))
        viewer.camera.lookAtTransform(Cesium.Matrix4.IDENTITY)

        tileset.style = new Cesium.Cesium3DTileStyle({
          color: {
            conditions: [
              ['true', 'rgba(0, 205, 243 ,1)']// 'rgb(127, 59, 8)']
            ]
          }
        })

        tileset.tileVisible.addEventListener(function (tile) {
          var content = tile.content
          var featuresLength = content.featuresLength
          for (var i = 0; i < featuresLength; i += 2) {
            let feature = content.getFeature(i)
            let model = feature.content._model

            if (model && model._sourcePrograms && model._rendererResources) {
              Object.keys(model._sourcePrograms).forEach(key => {
                let program = model._sourcePrograms[key]
                let fragmentShader = model._rendererResources.sourceShaders[program.fragmentShader]
                let v_position = ''
                if (fragmentShader.indexOf(' v_positionEC;') != -1) {
                  v_position = 'v_positionEC'
                } else if (fragmentShader.indexOf(' v_pos;') != -1) {
                  v_position = 'v_pos'
                }
                const color = `vec4(${feature.color.toString()})`

                model._rendererResources.sourceShaders[program.fragmentShader] =
'varying vec3 ' + v_position + ';\n' +
'void main(void){\n' +
'    vec4 position = czm_inverseModelView * vec4(' + v_position + ',1);\n' +
'    float glowRange = 105.0;\n' +
'    gl_FragColor = ' + color + ';\n' +
'    gl_FragColor = vec4(0.0,  205.0/255.0, 243.0/255.0, 1.0);\n' +
'    gl_FragColor *= vec4(vec3(position.y / 40.0), 1.0);\n' +
'    float time = fract(czm_frameNumber / 360.0);\n' +
'    time = abs(time - 0.5) * 2.0;\n' +
'    float diff = step(0.005, abs( clamp(position.y / glowRange, 0.0, 1.0) - time));\n' +
'    gl_FragColor.rgb += gl_FragColor.rgb * (1.0 - diff);\n' +
'}\n'
              })
              model._shouldRegenerateShaders = true
            }
          }
        })
      }).otherwise(function (error) {
        console.error(error)
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
    init3D () {
      var subdomains = ['0', '1', '2', '3', '4', '5', '6', '7']
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
        // imageryProvider: new Cesium.WebMapTileServiceImageryProvider({
        // // 影像注记
        //   url: 'http://t{s}.tianditu.com/vec_w/wmts?service=wmts&request=GetTile&version=1.0.0&LAYER=vec&tileMatrixSet=w&TileMatrix={TileMatrix}&TileRow={TileRow}&TileCol={TileCol}&style=default.jpg&tk=1b0e6426f7883feec155d6f3e3c8f5e2',
        //   subdomains: subdomains,
        //   layer: 'tdtCiaLayer',
        //   style: 'default',
        //   format: 'image/jpeg',
        //   tileMatrixSetID: 'GoogleMapsCompatible',
        //   show: true,
        //   maximumLevel: 17
        // })
      })
      let d3kit = new Cesium.D3Kit(viewer)
      let layer = viewer.scene.imageryLayers.get(0)
      layer.brightness = 0.2
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
        console.log(lng,
          lat,
          Cesium.Cartographic.fromCartesian(position).height + 3)
        this.popshow = false
        this.popshowBig = false
        contrastBias.selected = [baseObject]
        if (picked && picked.primitive) {
          if (picked.id && picked.id._billboard) {
            if (this.pageIsJXJ) {
              this.popshow = true
            } else {
              this.popshowBig = true
            }
          }
          let primitive = picked.primitive
          let pickIds = primitive._pickIds
          let pickId = picked.pickId
          if (picked.id) {
            pickId = primitive.pickId
          }
          if (!pickId && !pickIds && picked.content) {
            pickIds = picked.content._model._pickIds
          }
          if (!pickId) {
            if (picked.id) {
              pickId = pickIds.find(pickId => {
                return pickId.object == picked
              })
            } else if (pickIds) {
              pickId = pickIds[0]
            }
          }
          if (pickId) {
            let pickObject = {
              pickId: pickId
            }
            contrastBias.selected.push(pickObject)
          }
        }
      }, Cesium.ScreenSpaceEventType.LEFT_CLICK)
    }
  }
}
</script>

<style>
.content {
  width: 100%;
  height: 100%;
  overflow: hidden;
  position: relative;
}
.content #cesiumContainer {
  width: 100%;
  height: 100%;
  padding: 0px;
  margin: 0px;
}
.content #pop {
  width: 650px;
  height: 329px;
  background: url(./tipBig.png);
  background-size: 100% 100%;
  color: rgb(255, 255, 255);
  position: relative;
  padding: 40px 0px 0px;
  position: absolute;
  z-index: 999;
  font-size: 14px;
}
.content #pop .poptitle {
  position: absolute;
  top: 60px;
  left: 50px;
  font-size: 46px !important;
  color: #bbeefe;
  font-family: PangmenMainRoadTitleBody !important;
  font-weight: 400;
}
.content #pop .CloseBtn {
  position: absolute;
  cursor: pointer;
  top: 5px;
  right: 0px;
  height: 50px;
  width: 50px;
}
.content #pop .lineContain {
  padding: 10px 60px;
  top: 70px;
  position: relative;
}
.content #pop .lineContain .line {
  margin-bottom: 5px;
}
.content #pop .lineContain button {
  color: #fff;
  background: #1890ff;
  border-color: #1890ff;
  text-shadow: 0 -1px 0 rgb(0 0 0 / 12%);
  box-shadow: 0 2px 0 rgb(0 0 0 / 5%);
}
.content #popBig {
  width: 650px;
  height: 329px;
  background: url(./tipBig.png);
  background-size: 100% 100%;
  color: rgb(255, 255, 255);
  position: relative;
  padding: 40px 0px 0px;
  position: absolute;
  z-index: 999;
  font-size: 14px;
}
.content #popBig .poptitle {
  position: absolute;
  top: 60px;
  left: 50px;
  font-size: 46px !important;
  color: #bbeefe;
  font-family: PangmenMainRoadTitleBody !important;
  font-weight: 400;
}
.content #popBig .CloseBtn {
  position: absolute;
  cursor: pointer;
  top: 5px;
  right: 0px;
  height: 50px;
  width: 50px;
}
.content #popBig .lineContain {
  padding: 10px 60px;
  top: 70px;
  position: relative;
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
</style>
