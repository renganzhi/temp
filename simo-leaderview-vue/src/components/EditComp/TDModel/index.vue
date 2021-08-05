<template>
  <div>
    <div id="containerAll" ref="mycanvas">
    </div>
  </div>
</template>

<script>
import * as THREE from 'three'
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js'
import { TransformControls } from 'three/examples/jsm/controls/TransformControls.js'
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader'
import elementResizeDetectorMaker from 'element-resize-detector'
let camera
let scene
let controls
let transformControls
let renderer
let light
export default {
  data () {
    return {
    }
  },
  props: ['item'],
  watch: {
    'item.width': function () {
    },
    'item.lightColor': function (val) {
      light.color = new THREE.Color(val)
    },
    'item.lightStrength': function (val) {
      light.intensity = val * 1
    },
    'item.cameraX': function (val) {
      // light.intensity = val * 1
      camera.position.x = val
    },
    'item.cameraY': function (val) {
      camera.position.y = val
    },
    'item.cameraZ': function (val) {
      camera.position.z = val
    },
    'item.ModelScal': function (val) {
      let Cameragltf = scene.getObjectByName('GltfName')
      Cameragltf.scale.set(val * 1, val * 1, val * 1)
    },
    'item.ModelRotation': function (val) {
      let Cameragltf = scene.getObjectByName('GltfName')
      console.log(Cameragltf)
      Cameragltf.rotation.y = Math.PI * val / 90
    },
    'item.ZoomLimitMin': function (val) {
      controls.minDistance = val
    },
    'item.ZoomLimitMax': function (val) {
      controls.maxDistance = val
    }
  },
  mounted () {
    const _this = this
    const erd = elementResizeDetectorMaker()
    erd.listenTo(document.querySelector('#containerAll'), (element) => {
      _this.$nextTick(() => {
        let height = document.querySelector('#containerAll').clientHeight
        let width = document.querySelector('#containerAll').clientWidth
        camera.aspect = width / height
        camera.updateProjectionMatrix()
        renderer.setSize(width, height)
      })
    })
    this.initSecen()
    this.animate()
  },
  created () {},
  methods: {
    initSecen: function () {
      scene = new THREE.Scene()
      light = new THREE.SpotLight(this.item.lightColor)
      light.intensity = this.item.lightStrength * 1
      light.position.set(-300, 600, -400)
      light.castShadow = true
      scene.add(light)
      scene.add(new THREE.AmbientLight(0xffffff, 20))
      let containerAll = document.getElementById('containerAll')
      camera = new THREE.PerspectiveCamera(
        45,
        window.innerWidth / window.innerHeight,
        0.1,
        10000
      )
      renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true })
      renderer.setClearAlpha(0)
      renderer.setSize(
        containerAll.clientWidth,
        containerAll.clientHeight
      )
      containerAll.appendChild(renderer.domElement)
      camera.position.set(this.item.cameraX, this.item.cameraY, this.item.cameraZ)
      camera.lookAt(new THREE.Vector3(0, 0, 0))
      this.$refs.mycanvas.addEventListener('click', this.onMouseclick, false)
      this.initDragControls()

      var helper = new THREE.GridHelper(1200, 50, 0xcd3700, 0x4a4a4a)

      scene.add(helper)

      this.creatSixBall()
    },
    creatSixBall () {
      var loader = new GLTFLoader()
      var _this = this
      loader.load(
        `/static/Glft/qiuji.gltf`,
        function (gltf) {
          gltf.scene.scale.set(_this.item.ModelScal * 1, _this.item.ModelScal * 1, _this.item.ModelScal * 1)
          gltf.scene.position.set(0, 0, 0)
          gltf.scene.rotation.y = Math.PI * _this.item.ModelRotation / 90
          gltf.scene.name = 'GltfName'
          scene.add(gltf.scene)
        }
      )
    },
    initDragControls () {
      controls = new OrbitControls(camera, renderer.domElement)
      transformControls = new TransformControls(
        camera,
        renderer.domElement
      )
      transformControls.addEventListener(
        'dragging-changed',
        function (event) {
          controls.enabled = !event.value
        }
      )
      scene.add(transformControls)

      controls.minDistance = this.item.ZoomLimitMin
      controls.maxDistance = this.item.ZoomLimitMax
    },
    onMouseclick (event) {
      var intersects = this.getIntersects(event)
      intersects.forEach((d) => {
        if (
          d.object.name === 'Previous' ||
          d.object.parent.name === 'Previous'
        ) {
        }
        if (d.object.name === 'Next' || d.object.parent.name === 'Next') {
        }
      })
    },

    getIntersects (event) {
      event.preventDefault()
      var vector = new THREE.Vector3() // 三维坐标对象
      vector.set(
        (event.offsetX / document.querySelector('#containerAll').clientWidth) *
          2 -
          1,
        -(
          event.offsetY / document.querySelector('#containerAll').clientHeight
        ) *
          2 +
          1,
        0.5
      )
      // 通过鼠标点击的位置(二维坐标)和当前相机的矩阵计算出射线位置
      vector.unproject(camera)
      var raycaster = new THREE.Raycaster(
        camera.position,
        vector.sub(camera.position).normalize()
      )
      var intersects = raycaster.intersectObjects(scene.children, true)
      return intersects
    },
    // 动画
    animate: function () {
      requestAnimationFrame(this.animate)
      controls.update()
      renderer.render(scene, camera)
    }
  }
}
</script>

<style>
#containerAll {
  position: absolute;
  width: calc(100% - 20px);
  padding: 10px;
  box-sizing: border-box;
  height: calc(100% - 20px);
}
</style>
