<template>
  <div class="content">
    <button v-if="false" @click="getCamera" style="position:absolute;z-index:999;width:100px;height:80px;top:0px;left:0px;">获取视角</button>
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
  data () {
    return {

    }
  },
  computed: {},
  watch: {

  },
  mounted () {
    this.init3D()
    this.initLine()
    this.initModels()
    this.initPostrender()
    this.fly()
  },
  methods: {
    fly () {
      viewer.scene.camera.flyTo({
        destination: Cesium.Cartesian3.fromDegrees(
          104.15382762573847,
          30.525354562999283,
          5648.141481220404
        ),
        orientation: {
          heading: 5.287504180425997,
          pitch: -0.4594702554639958,
          roll: 0.000044985178769607614
        },
        duration: 0.5
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
      }, 1000)
    },
    initLine () {
      $.getJSON('./static/geojson/bianjie.json', (res) => {
        let positions = res.features[0].geometry.coordinates
        let linepositions = []
        positions.forEach(item => {
          linepositions.push(item[0])
          linepositions.push(item[1])
          linepositions.push(10)
        })
        viewer.entities.add({
          polyline: {
            positions: Cesium.Cartesian3.fromDegreesArrayHeights(linepositions),
            depthFailMaterial: Cesium.Color.AQUA,
            material: Cesium.Color.AQUA
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
                  color: Cesium.Color.ORANGERED,
                  duration: 200
                }),
                material: new Cesium.PolylineFlowMaterialProperty({
                  color: Cesium.Color.ORANGERED,
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
              ['true', 'rgba(0, 127.5, 255 ,1)']// 'rgb(127, 59, 8)']
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
'    gl_FragColor = vec4(0.2,  0.5, 1.0, 1.0);\n' +
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
        imageryProvider: new Cesium.WebMapTileServiceImageryProvider({
        // 影像注记
          url: 'http://t{s}.tianditu.com/cia_w/wmts?service=wmts&request=GetTile&version=1.0.0&LAYER=cia&tileMatrixSet=w&TileMatrix={TileMatrix}&TileRow={TileRow}&TileCol={TileCol}&style=default.jpg&tk=1b0e6426f7883feec155d6f3e3c8f5e2',
          subdomains: subdomains,
          layer: 'tdtCiaLayer',
          style: 'default',
          format: 'image/jpeg',
          tileMatrixSetID: 'GoogleMapsCompatible',
          show: true,
          maximumLevel: 17
        })
      })
      let d3kit = new Cesium.D3Kit(viewer)
      let layer = viewer.scene.imageryLayers.get(0)
      layer.brightness = 0.4
      viewer.scene.skyAtmosphere.show = false
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
      // todo：在椭球下点击创建点
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
        console.log(lng,
          lat,
          Cesium.Cartographic.fromCartesian(position).height + 3)
        contrastBias.selected = [baseObject]
        if (picked && picked.primitive) {
          let primitive = picked.primitive
          let pickIds = primitive._pickIds
          let pickId = picked.pickId
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
</style>
