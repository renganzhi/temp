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
import { gbs } from '@/config/settings'

export default {
  data () {
    return {
      baseUrl:'',
      camera:'',
      scene:'',
      controls:'',
      transformControls:'',
      renderer:'',
      light:'',
      helper:'',
    }
  },
  props: ['item'],
  watch: {
    'item.width': function () {
    },
    'item.lightColor': function (val) {
      this.light.color = new THREE.Color(val)
    },
    'item.lightStrength': function (val) {
      this.light.intensity = val * 1
    },
    'item.cameraX': function (val) {
      if(this.item.cameraX !== this.camera.position.x){
        this.camera.position.x = val
      }
    },
    'item.cameraY': function (val) {
      this.camera.position.y = val
    },
    'item.cameraZ': function (val) {
      this.camera.position.z = val
    },
    'camera.position.x': function (val) {
      this.item.cameraX = this.camera.position.x.toFixed(2) * 1
    },
    'camera.position.y': function (val) {
      this.item.cameraY = this.camera.position.y.toFixed(2) * 1
    },
    'camera.position.z': function (val) {
      this.item.cameraZ = this.camera.position.z.toFixed(2) * 1
    },
    'item.ModelScal': function (val) {
      let Cameragltf = this.scene.getObjectByName('GltfName')
      Cameragltf.scale.set(val * 1, val * 1, val * 1)
    },
    'item.ModelRotation': function (val) {
      let Cameragltf = this.scene.getObjectByName('GltfName')
      Cameragltf.rotation.y = Math.PI * val / 90
    },
    'item.ZoomLimitMin': function (val) {
      this.controls.minDistance = val
    },
    'item.gltfName': function (val) {
      let Cameragltf = this.scene.getObjectByName('GltfName')
      this.scene.remove(Cameragltf)
      this.creatSixBall()
    },
    'item.ShowHelpLine': function (val) {
      if (val) {
        this.helper.scale.x = 1
        this.helper.scale.y = 1
        this.helper.scale.z = 1
      } else {
        this.helper.scale.x = 0
        this.helper.scale.y = 0
        this.helper.scale.z = 0
      }
    },
    'item.ZoomLimitMax': function (val) {
      this.controls.maxDistance = val
    }
  },
  beforeMount () {
    var reg = /^\/api/
    if (!reg.test(this.item.imgSrc)) {
      this.baseUrl = gbs.host
    }
  },
  mounted () {
    const _this = this
    const erd = elementResizeDetectorMaker()
    erd.listenTo(this.$refs.mycanvas, (element) => {
      _this.$nextTick(() => {
        let height = this.$refs.mycanvas.clientHeight
        let width = this.$refs.mycanvas.clientWidth
        this.camera.aspect = width / height
        this.camera.updateProjectionMatrix()
        this.renderer.setSize(width, height)
      })
    })
    this.$refs.mycanvas.addEventListener('mousemove', this.move)
    this.initSecen()
    this.animate()
  },
  created () {},
  methods: {
    move(e){
      e.stopPropagation()
    },
    initSecen: function () {
      this.scene = new THREE.Scene()
      this.light = new THREE.SpotLight(this.item.lightColor)
      this.light.intensity = this.item.lightStrength * 1
      this.light.position.set(-300, 600, -400)
      this.light.castShadow = true
      this.scene.add(this.light)
      this.scene.add(new THREE.AmbientLight(0xffffff, 20))
      let containerAll = this.$refs.mycanvas
      this.camera = new THREE.PerspectiveCamera(
        45,
        window.innerWidth / window.innerHeight,
        0.1,
        10000
      )
      this.renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true })
      this.renderer.setClearAlpha(0)
      this.renderer.setSize(
        containerAll.clientWidth,
        containerAll.clientHeight
      )
      containerAll.appendChild(this.renderer.domElement)
      this.camera.position.set(this.item.cameraX, this.item.cameraY, this.item.cameraZ)
      this.camera.lookAt(new THREE.Vector3(0, 0, 0))
      // this.$refs.mycanvas.addEventListener('click', this.onMouseclick, false)
      this.initDragControls()

      this.helper = new THREE.GridHelper(1200, 50, 0xcd3700, 0x4a4a4a)
      if (this.item.ShowHelpLine) {
        this.helper.scale.x = 1
        this.helper.scale.y = 1
        this.helper.scale.z = 1
      } else {
        this.helper.scale.x = 0
        this.helper.scale.y = 0
        this.helper.scale.z = 0
      }
      this.scene.add(this.helper)
      this.creatSixBall()
    },
    creatSixBall () {
      var loader = new GLTFLoader()
      var _this = this
      // ${this.baseUrl}
      if(this.baseUrl && _this.item.gltfName !== ''){
      loader.load(
        `${this.baseUrl}/leaderview/${_this.item.gltfName}`,
        function (gltf) {
          gltf.scene.scale.set(_this.item.ModelScal * 1, _this.item.ModelScal * 1, _this.item.ModelScal * 1)
          gltf.scene.position.set(0, 0, 0)
          gltf.scene.rotation.y = Math.PI * _this.item.ModelRotation / 90
          gltf.scene.name = 'GltfName'
          _this.scene.add(gltf.scene)
        }
      )
      }
    },
    initDragControls () {
      this.controls = new OrbitControls(this.camera, this.renderer.domElement)
      this.transformControls = new TransformControls(
        this.camera,
        this.renderer.domElement
      )
      this.transformControls.addEventListener(
        'dragging-changed',
        function (event) {
          this.controls.enabled = !event.value
        }
      )
      this.scene.add(this.transformControls)

      this.controls.minDistance = this.item.ZoomLimitMin
      this.controls.maxDistance = this.item.ZoomLimitMax
    },
    // onMouseclick (event) {
    //   var intersects = this.getIntersects(event)
    //   intersects.forEach((d) => {
    //     if (
    //       d.object.name === 'Previous' ||
    //       d.object.parent.name === 'Previous'
    //     ) {
    //     }
    //     if (d.object.name === 'Next' || d.object.parent.name === 'Next') {
    //     }
    //   })
    // },

    getIntersects (event) {
      event.preventDefault()
      var vector = new THREE.Vector3() // 三维坐标对象
      vector.set(
        (event.offsetX / this.$refs.mycanvas.clientWidth) *
          2 -
          1,
        -(
          event.offsetY / this.$refs.mycanvas.clientHeight
        ) *
          2 +
          1,
        0.5
      )
      // 通过鼠标点击的位置(二维坐标)和当前相机的矩阵计算出射线位置
      vector.unproject(this.camera)
      var raycaster = new THREE.Raycaster(
        this.camera.position,
        vector.sub(this.camera.position).normalize()
      )
      var intersects = raycaster.intersectObjects(this.scene.children, true)
      return intersects
    },
    // 动画
    animate: function () {
      requestAnimationFrame(this.animate)
      this.controls.update()
      this.renderer.render(this.scene, this.camera)
    }
  }
}
</script>

<style>
#containerAll {
  position: absolute;
  width: calc(100% - 30px);
  margin: 15px;
  box-sizing: border-box;
  height: calc(100% - 30px);
}
</style>
