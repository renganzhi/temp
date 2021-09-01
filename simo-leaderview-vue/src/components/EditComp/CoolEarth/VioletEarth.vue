<template>
    <div class="VioletEarth" ref="VioletEarth" :style="{'width':item.width + 'px','height':item.height + 'px'}"></div>
</template>
<script>
import * as THREE from 'three'
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js'
export default {
  props: ['item'],
  data () {
    return {
      camera: null,
      globeMesh: null,
      cloudmesh: null,
      lightmesh: null,
      globeTextureLoader: new THREE.TextureLoader(),
      hexagon: new THREE.Object3D(),
      cloud: new THREE.Object3D(),
      light: new THREE.Object3D(),
      earthParticles: new THREE.Object3D(),
      dotTexture: new THREE.TextureLoader().load(
        // 'http://localhost:8080/api/leaderview/home/getImg/false/857'
      ),
      scene: null,
      renderer: null,
      radius: 3,
      hoveOne: false,
      mesh: null,
      mymesh: null,
      myponts: null,
      myLines: null,
      Allplane: [],
      controls: null,
      earthImg: '',
      earthImgData: '',
      hexagonColor: [0xffffff, 0xffff00]
    }
  },
  watch: {
    'item.width': function () {
      this.$nextTick(() => {
        this.resize()
      })
    },
    'item.height': function () {
      this.$nextTick(() => {
        this.resize()
      })
    }
  },
  mounted () {
    this.init()
    this.animate()
  },
  methods: {
    // 初始化
    resize: function () {
      let container = this.$refs.VioletEarth
      this.renderer.setSize(container.clientWidth, container.clientHeight)
      this.camera.aspect = container.innerWidth / container.innerHeight
    },
    init: function () {
      //  创建场景对象Scene
      let vertexShader = [
        'varying vec3    vVertexWorldPosition;',
        'varying vec3    vVertexNormal;',
        'varying vec4    vFragColor;',
        'void main(){',
        '    vVertexNormal  = normalize(normalMatrix * normal);', // 将法线转换到视图坐标系中
        '    vVertexWorldPosition   = (modelMatrix * vec4(position, 1.0)).xyz;', // 将顶点转换到世界坐标系中
        '    // set gl_Position',
        '    gl_Position    = projectionMatrix * modelViewMatrix * vec4(position, 1.0);',
        '}'
      ].join('\n')
      let fragmentShader1 = [
        'uniform vec3    glowColor;',
        'uniform float   coeficient;',
        'uniform float   power;',
        'varying vec3    vVertexNormal;',
        'varying vec3    vVertexWorldPosition;',
        'varying vec4    vFragColor;',
        'void main(){',
        '    vec3 worldCameraToVertex= vVertexWorldPosition - cameraPosition;', // 世界坐标系中从相机位置到顶点位置的距离
        '    vec3 viewCameraToVertex    = (viewMatrix * vec4(worldCameraToVertex, 0.0)).xyz;', // 视图坐标系中从相机位置到顶点位置的距离
        '    viewCameraToVertex = normalize(viewCameraToVertex);', // 规一化
        '    float intensity       = pow(coeficient + dot(vVertexNormal, viewCameraToVertex), power);',
        '    gl_FragColor      = vec4(glowColor, intensity);',
        '}'// vVertexNormal视图坐标系中点的法向量
        // viewCameraToVertex视图坐标系中点到摄像机的距离向量
        // dot点乘得到它们的夹角的cos值
        // 从中心向外面角度越来越小（从钝角到锐角）从cos函数也可以知道这个值由负变正，不透明度最终从低到高
      ].join('\n')

      this.scene = new THREE.Scene()
      var _this = this
      let geometry = new THREE.SphereGeometry(1.2 * _this.radius, 66, 44) // 大气层所在球体
      let map = new THREE.TextureLoader().load(
        '../../../../static/img/大气云图1.jpg'
      )
      let borderLight = new THREE.SphereGeometry(1 * _this.radius, 66, 44) // 球体边缘辉光
      _this.lightmesh = new THREE.Mesh(borderLight, new THREE.ShaderMaterial({
        uniforms: {
          coeficient: {
            type: 'f',
            value: 1
          },
          power: {
            type: 'f',
            value: 1
          },
          glowColor: {
            type: 'c',
            value: new THREE.Color('#9934ff')
          }
        },
        vertexShader: vertexShader,
        fragmentShader: fragmentShader1,
        blending: THREE.NormalBlending,
        transparent: true
      }))
      var lightmesh2 = new THREE.Mesh(borderLight, new THREE.ShaderMaterial({
        uniforms: {
          coeficient: {
            type: 'f',
            value: 0.5
          },
          power: {
            type: 'f',
            value: 1.5
          },
          glowColor: {
            type: 'c',
            value: new THREE.Color('#fff')
          }
        },
        vertexShader: vertexShader,
        fragmentShader: fragmentShader1,
        blending: THREE.NormalBlending,
        transparent: true
      }))

      _this.light.add(_this.lightmesh)
      _this.light.add(lightmesh2)
      _this.scene.add(_this.light)
      map.wrapT = THREE.ClampToEdgeWrapping
      map.wrapS = THREE.ClampToEdgeWrapping
      let material = this.XRayMaterial({
        map: map,
        alphaProportion: 0.25,
        color: new THREE.Color('#5588aa'),
        opacity: 0,
        gridOffsetSpeed: 0.6
      })
      _this.cloudmesh = new THREE.Mesh(geometry, material)
      _this.cloudmesh.matrixAutoUpdate = !1
      _this.cloud.add(_this.cloudmesh)
      _this.scene.add(_this.cloud)
      /**
       * 相机设置
       */
      let container = this.$refs.VioletEarth
      this.camera = new THREE.PerspectiveCamera(
        60,
        container.clientWidth / container.clientHeight,
        0.1,
        100
      )
      this.camera.position.z = 9
      // var axisHelper = new THREE.AxisHelper(500)
      // this.scene.add(axisHelper)

      // var ambient = new THREE.AmbientLight(0xffffff)// 环境光
      // this.scene.add(ambient)
      this.draw()
      this.scene.add(new THREE.HemisphereLight('#ffffff', '#ffffff', 1))

      const position = this.createPosition([116.2, 39.55])

      var sphereMaterial = this.XRayMaterial({
        opacity: 0
      })
      this.Allplane = new THREE.Mesh(
        new THREE.SphereGeometry(1 * this.radius, 66, 44),
        sphereMaterial
      )
      // this.createCone(position, '北京') // 光锥
      // this.createCone(this.createPosition([16.2, 59.55]), 'xxxx') // 光锥

      this.scene.add(this.Allplane)
      /**
       * 创建渲染器对象
       */
      this.renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true })
      this.renderer.setClearAlpha(0)

      this.renderer.setSize(container.clientWidth, container.clientHeight)
      this.renderer.gammaInput = true
      this.renderer.gammaOutput = true

      container.appendChild(this.renderer.domElement)
      this.drawEarth()

      // 创建控件对象
      this.controls = new OrbitControls(this.camera, this.renderer.domElement)

      window.addEventListener('mousemove', this.onMouseclick, false)
    },
    draw () {
      var _this = this
      _this.earthImg = new Image()
      _this.earthImg.src =
        'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAA8AAAAHgCAIAAADlh5PTAAAACXBIWXMAAAsTAAALEwEAmpwYAAAF7GlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDUgNzkuMTYzNDk5LCAyMDE4LzA4LzEzLTE2OjQwOjIyICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHhtcDpDcmVhdGVEYXRlPSIyMDE5LTAyLTE1VDE1OjU0OjQ0KzAzOjAwIiB4bXA6TW9kaWZ5RGF0ZT0iMjAxOS0wMi0yMVQxODowNzoxNSswMzowMCIgeG1wOk1ldGFkYXRhRGF0ZT0iMjAxOS0wMi0yMVQxODowNzoxNSswMzowMCIgZGM6Zm9ybWF0PSJpbWFnZS9wbmciIHBob3Rvc2hvcDpDb2xvck1vZGU9IjMiIHBob3Rvc2hvcDpJQ0NQcm9maWxlPSJzUkdCIElFQzYxOTY2LTIuMSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDoxYjcwNjU1ZC01MDE1LWFmNDctYjUwMC1kNjAzNjYzYTZhYjgiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NWUyNjNlNTQtOTJjYi0xYTRiLWE0MDktMGVmZWFiNmIxMTI5IiB4bXBNTTpPcmlnaW5hbERvY3VtZW50SUQ9InhtcC5kaWQ6NWUyNjNlNTQtOTJjYi0xYTRiLWE0MDktMGVmZWFiNmIxMTI5Ij4gPHhtcE1NOkhpc3Rvcnk+IDxyZGY6U2VxPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0iY3JlYXRlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDo1ZTI2M2U1NC05MmNiLTFhNGItYTQwOS0wZWZlYWI2YjExMjkiIHN0RXZ0OndoZW49IjIwMTktMDItMTVUMTU6NTQ6NDQrMDM6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE5IChXaW5kb3dzKSIvPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0ic2F2ZWQiIHN0RXZ0Omluc3RhbmNlSUQ9InhtcC5paWQ6MWI3MDY1NWQtNTAxNS1hZjQ3LWI1MDAtZDYwMzY2M2E2YWI4IiBzdEV2dDp3aGVuPSIyMDE5LTAyLTIxVDE4OjA3OjE1KzAzOjAwIiBzdEV2dDpzb2Z0d2FyZUFnZW50PSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHN0RXZ0OmNoYW5nZWQ9Ii8iLz4gPC9yZGY6U2VxPiA8L3htcE1NOkhpc3Rvcnk+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+WnTZ0QAAOVtJREFUeNrt3euW3CiyQOF8Tr+n36678/RyTunQupBIAsTl2z9medzlLKUEEZsAwesNAAAAIJmXWwAAAAAQaAAAAIBAAwAAAAQaAAAAINAAAAAAgQYAAAAItFsAAAAAEGgAAACAQAMAAAAEGgAAACDQAAAAAIEGAAAACLRbAAAAABBoAAAAgEADAAAABBoAAAAg0AAAAACBBgAAAAi0WwAAAAAQaAAAAIBAAwAAAAQaAAAAINAAAAAAgQYwOr9//369Xr/+8LrHv5/w+w/uKgCAQAM96eCK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'

      var canvas = document.createElement('canvas')
      var c = canvas.getContext('2d')
      _this.earthImg.onload = function () {
        var width = (canvas.width = _this.earthImg.width)
        var height = (canvas.height = _this.earthImg.height)
        c.drawImage(_this.earthImg, 0, 0, width, height)
        _this.earthImgData = c.getImageData(
          0,
          0,
          _this.earthImg.width,
          _this.earthImg.height
        )
        _this.createEarthParticles()
      }
    },
    onMouseclick (event) {
      var intersects = this.getIntersects(event)
      var onLight = false
      if (intersects.length > 0) {
        intersects.forEach((element, d) => {
          if (element.object.name === 'light' || element.object.userData.name === 'light') {
            this.hoveOne = true
            onLight = true
          }
        })
        if (!onLight) {
          this.hoveOne = false
        }
      }
    },
    getIntersects (event) {
      event.preventDefault()
      // 声明 raycaster 和 mouse 变量
      // var raycaster = new THREE.Raycaster()
      var vector = new THREE.Vector3() // 三维坐标对象

      // 通过鼠标点击位置,计算出 raycaster 所需点的位置,以屏幕为中心点,范围 -1 到 1
      vector.set(
        (event.offsetX / this.item.width) * 2 - 1,
        -(event.offsetY / this.item.height) * 2 + 1,
        0.5
      )
      // 通过鼠标点击的位置(二维坐标)和当前相机的矩阵计算出射线位置
      vector.unproject(this.camera)
      var raycaster = new THREE.Raycaster(
        this.camera.position,
        vector.sub(this.camera.position).normalize()
      )
      var objects = []
      this.scene.children.forEach(element => {
        if (element.isMesh) {
          objects.push(element)
        }
      })
      var intersects = raycaster.intersectObjects(objects, true)

      // 获取与射线相交的对象数组，其中的元素按照距离排序，越近的越靠前
      // var intersects = raycaster.intersectObjects(this.scene.children);

      // 返回选中的对象
      return intersects
    },
    drawEarth: function () {
      var data = [
        {
          x: 123,
          y: 25
        },
        {
          x: 13,
          y: 10
        },
        {
          x: 75,
          y: 88
        },
        {
          x: 153,
          y: 85
        }
      ]
      var dotPng2 = new THREE.TextureLoader().load(
        // 'http://localhost:8080/api/leaderview/home/getImg/false/861'
      ) // 需要使用的贴图  如果不适用则是小方块
      var geometry = new THREE.BufferGeometry()
      var positions = [] // 顶点
      var sizes = [] // 顶点
      var ponitShader = {
        vertexShader: `   
        attribute float u_size;
        void main() { 
            vec4 mP = modelViewMatrix * vec4( position, 1.0 );
            gl_PointSize = u_size * 300.0/(-mP.z );;
            gl_Position = projectionMatrix * mP;
        }
    `,
        fragmentShader: `
        uniform sampler2D u_txue; 
        uniform vec3 u_color;  
        void main() {
            gl_FragColor = vec4(u_color,1.0) * texture2D( u_txue,vec2(gl_PointCoord.x,1.0-gl_PointCoord.y) ); 
        }
    `
      }
      var uniforms = {
        u_txue: {
          value: dotPng2
        },
        u_color: {
          value: new THREE.Color('#5588aa')
        }
      }
      // 材质
      var material = new THREE.ShaderMaterial({
        uniforms: uniforms,
        transparent: true,
        depthTest: false,
        blending: THREE.AdditiveBlending,
        vertexShader: ponitShader.vertexShader,
        fragmentShader: ponitShader.fragmentShader
      })
      for (let i = 0; i < data.length; i++) {
        var size = THREE.Math.randInt(1, 3) // 大小
        var position = this.latLongToVector3({
          lat: data[i].x,
          lon: data[i].y,
          radius: this.radius
        }) // 坐标转换
        positions.push(...Object.values(position))
        sizes.push(size)
      }
      geometry.addAttribute(
        'position',
        new THREE.Float32BufferAttribute(positions, 3)
      )
      geometry.addAttribute(
        'u_size',
        new THREE.Float32BufferAttribute(sizes, 1)
      )
      var mesh = new THREE.Points(geometry, material)
      this.scene.add(mesh)
    },
    createEarthParticles: function () {
      let positions = []
      let materials = []
      let sizes = []
      for (var i = 0; i < 2; i++) {
        positions[i] = {
          positions: []
        }
        sizes[i] = {
          sizes: []
        }
        var mat = new THREE.PointsMaterial()
        mat.size = 5
        mat.color = new THREE.Color(0x03d98e)
        mat.map = this.dotTexture
        mat.depthWrite = false
        mat.transparent = true
        mat.opacity = 0
        mat.side = THREE.FrontSide
        mat.blending = THREE.AdditiveBlending
        let n = i / 2
        mat.t_ = n * Math.PI * 2
        mat.speed_ = 0.02
        mat.min_ = 0.2 * Math.random() + 0.5
        mat.delta_ = 0.1 * Math.random() + 0.1
        mat.opacity_coef_ = 1
        materials.push(mat)
      }
      var spherical = new THREE.Spherical()
      spherical.radius = this.radius
      const step = 200
      var _this = this

      let geometry111 = new THREE.BufferGeometry()
      let positions111 = []
      let colors = []

      let color = new THREE.Color()

      for (let i = 0; i < step; i++) {
        let vec = new THREE.Vector3()
        let radians =
          (step * (1 - Math.sin((i / step) * Math.PI))) / step + 0.5 // 每个纬线圈内的角度均分
        for (let j = 0; j < step; j += radians) {
          let c = j / step // 底图上的横向百分比
          let f = i / step // 底图上的纵向百分比
          if (this.isLandByUV(c, f)) {
            // 根据横纵百分比判断在底图中的像素值
            spherical.theta = c * Math.PI * 2 - Math.PI / 2 // 横纵百分比转换为theta和phi夹角
            spherical.phi = f * Math.PI // 横纵百分比转换为theta和phi夹角
            vec.setFromSpherical(spherical) // 夹角转换为世界坐标
            positions111.push(vec.x, vec.y, vec.z)

            color.set('#d034ff') // 设置地图中点的颜色

            colors.push(color.r, color.g, color.b)
          }
        }
      }
      // 添加点和颜色
      geometry111.addAttribute(
        'position',
        new THREE.Float32BufferAttribute(positions111, 3)
      )
      geometry111.addAttribute(
        'color',
        new THREE.Float32BufferAttribute(colors, 3)
      )

      let material = new THREE.PointsMaterial({
        size: 0.04,
        sizeAttenuation: true,
        vertexColors: THREE.VertexColors,
        transparent: true,
        opacity: 1
      })
      /* 批量管理点 */
      _this.myponts = new THREE.Points(geometry111, material)

      _this.scene.add(_this.myponts)

      let geometry22 = new THREE.BufferGeometry()
      let positions22 = []
      let colors22 = []
      const stepLine = 900
      for (let i = 0; i < stepLine; i++) {
        let vec = new THREE.Vector3()
        let radians =
          (stepLine * (1 - Math.sin((i / stepLine) * Math.PI))) / stepLine +
          0.5 // 每个纬线圈内的角度均分
        for (let j = 0; j < stepLine; j += radians) {
          let c = j / stepLine // 底图上的横向百分比
          let c2 = (j + radians) / stepLine
          let c3 = (j - radians) / stepLine
          let f = i / stepLine // 底图上的纵向百分比
          let f2 = (i + radians) / stepLine // 底图上的纵向百分比
          let f3 = (i - radians) / stepLine // 底图上的纵向百分比
          if (this.isLandByUV(c, f)) {
            // 根据横纵百分比判断在底图中的像素值
          } else if (this.isLandByUV(c2, f2)) {
            spherical.theta = c2 * Math.PI * 2 - Math.PI / 2 // 横纵百分比转换为theta和phi夹角
            spherical.phi = f2 * Math.PI // 横纵百分比转换为theta和phi夹角
            vec.setFromSpherical(spherical) // 夹角转换为世界坐标
            positions22.push(vec.x, vec.y, vec.z)
            color.set('#d034ff') // 设置地图边缘点线的颜色
            colors22.push(color.r, color.g, color.b)
          } else if (this.isLandByUV(c3, f3)) {
            spherical.theta = c3 * Math.PI * 2 - Math.PI / 2 // 横纵百分比转换为theta和phi夹角
            spherical.phi = f3 * Math.PI // 横纵百分比转换为theta和phi夹角
            vec.setFromSpherical(spherical) // 夹角转换为世界坐标
            positions22.push(vec.x, vec.y, vec.z)

            color.set('#d034ff') // 设置地图边缘点线的颜色

            colors22.push(color.r, color.g, color.b)
          }
        }
      }
      geometry22.addAttribute(
        'position',
        new THREE.Float32BufferAttribute(positions22, 3)
      )
      geometry22.addAttribute(
        'color',
        new THREE.Float32BufferAttribute(colors22, 3)
      )

      let materialLine = new THREE.PointsMaterial({
        size: 0.04,
        sizeAttenuation: true,
        vertexColors: THREE.VertexColors,
        transparent: true,
        opacity: 1
      })
      _this.myLines = new THREE.Points(geometry22, materialLine)
      _this.scene.add(_this.myLines)
    },
    isLandByUV: function (c, f) {
      if (!this.earthImgData) {
        // 底图数据
        console.error('data error!')
      }
      let n = parseInt(this.earthImg.width * c) // 根据横纵百分比计算图象坐标系中的坐标
      let o = parseInt(this.earthImg.height * f) // 根据横纵百分比计算图象坐标系中的坐标
      return (
        this.earthImgData.data[4 * (o * this.earthImgData.width + n)] === 0
      ) // 查找底图中对应像素点的rgba值并判断
    },
    latLongToVector3: function (opts) {
      opts = opts || {}
      var lat = opts.lat
      var lon = opts.lon
      var radius = opts.radius
      var rotation = opts.rotation || 0
      var phi = (lat * Math.PI) / 180
      var theta = (lon * Math.PI) / 180 + rotation
      var x = radius * Math.cos(phi) * Math.cos(theta)
      var y = radius * Math.sin(phi)
      var z = radius * Math.cos(phi) * Math.sin(theta)
      return new THREE.Vector3(z, y, x)
    },
    XRayMaterial: function (options) {
      let uniforms = {
        uTex: {
          type: 't',
          value: options.map || new THREE.Texture()
        },
        offsetRepeat: {
          value: new THREE.Vector4(0, 0, 1, 1)
        },
        alphaProportion: {
          type: '1f',
          value: options.alphaProportion || 0.5
        },
        diffuse: {
          value: options.color || new THREE.Color(16777215)
        },
        opacity: {
          value: options.opacity || 1
        },
        gridOffset: {
          value: 0
        }
      }
      return new THREE.ShaderMaterial({
        uniforms: uniforms,
        vertexShader: `
                varying float _alpha;
                        varying vec2 vUv;
                        uniform vec4 offsetRepeat;
                        uniform float alphaProportion;
                        void main() {
                            gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );
                            vUv = uv * offsetRepeat.zw + offsetRepeat.xy;
                            vec4 worldPosition = modelMatrix * vec4( vec3( position ), 1.0 );
                            vec3 cameraToVertex = normalize( cameraPosition - worldPosition.xyz);
                            _alpha = 1.0 - max( 0.0, dot( normal, cameraToVertex ) );
                            _alpha = max( 0.0, (_alpha - alphaProportion) / (1.0 - alphaProportion) );
                        }`,
        fragmentShader: `
                            uniform sampler2D uTex;
                        uniform vec3 diffuse;
                        uniform float opacity;
                        uniform float gridOffset;
                        varying float _alpha;
                        varying vec2 vUv;
                        void main() {
                            vec4 texColor = texture2D( uTex, vUv );
                            float _a = _alpha * opacity;
                            if( _a <= 0.0 ) discard;
                            _a = _a * ( sin( vUv.y * 2000.0 + gridOffset ) * .5 + .5 );
                            gl_FragColor = vec4( texColor.rgb * diffuse, _a );
                        }`,
        transparent: !0,
        blending: THREE.AdditiveBlending,
        depthTest: !1
      })
    },
    createCone: function (position, name) {
      let texture = new THREE.TextureLoader().load(
        '../../../../static/img/light.jpg'
      )
      let texturedot = new THREE.TextureLoader().load(
        '../../../../static/img/dot.png'
      )
      let material = new THREE.MeshBasicMaterial({
        map: texture,
        transparent: true,
        depthTest: false,
        side: THREE.DoubleSide,
        blending: THREE.AdditiveBlending
      })
      let materialdot = new THREE.MeshBasicMaterial({
        map: texturedot,
        transparent: true,
        depthTest: false,
        side: THREE.DoubleSide,
        blending: THREE.AdditiveBlending
      })
      let height = Math.random() * this.radius * 0.2 + 0.2
      let geometry = new THREE.PlaneGeometry(0.2, height)
      let matrix1 = new THREE.Matrix4()
      let plane1 = new THREE.Mesh(geometry, material)

      let geometrydot = new THREE.PlaneGeometry(0.2, 0.2)
      let planedot = new THREE.Mesh(geometrydot, materialdot)

      matrix1.makeRotationX(Math.PI / 2)
      matrix1.setPosition(new THREE.Vector3(0, 0, height / -2))
      geometry.applyMatrix(matrix1)
      let plane2 = plane1.clone()
      plane2.rotation.z = Math.PI / 2
      plane1.add(plane2)
      plane1.name = 'light'
      plane1.userData.name = 'light'
      plane1.position.copy(position)
      plane1.lookAt(0, 0, 0)

      let canvas = document.createElement('canvas')
      let ctx = canvas.getContext('2d')
      ctx.fillStyle = 'rgb(255,255,250)'
      ctx.font = '50px Arial '

      ctx.fillText(name, 130, 55)
      ctx.globalAlpha = 1

      let texture22 = new THREE.Texture(canvas)
      texture22.needsUpdate = true
      // 创建精灵，将该材质赋予给创建的精灵
      let spriteMaterial = new THREE.PointsMaterial({
        map: texture22,
        size: 1,
        transparent: true,
        opacity: 1
      })
      // 创建坐标点，并将材质给坐标
      let geometryfont = new THREE.BufferGeometry()
      let vertices = [-0.1, 0, -0.2]
      geometryfont.setAttribute('position', new THREE.Float32BufferAttribute(vertices, 3))
      let sprite = new THREE.Points(geometryfont, spriteMaterial)
      planedot.add(sprite)
      plane1.add(planedot)
      this.Allplane.add(plane1)
    },
    createPosition: function (lnglat) {
      let spherical = new THREE.Spherical()
      spherical.radius = 3
      const lng = lnglat[0]
      const lat = lnglat[1]
      const theta = (lng + 90) * (Math.PI / 180)
      const phi = (90 - lat) * (Math.PI / 180)
      spherical.phi = phi
      spherical.theta = theta
      let position = new THREE.Vector3()
      position.setFromSpherical(spherical)
      return position
    },
    // 动画
    animate: function () {
      requestAnimationFrame(this.animate)
      if (!this.hoveOne) {
        if (this.globeMesh) {
          this.globeMesh.rotation.y += 0.002
        }
        if (this.cloud && this.myponts) {
          this.cloud.rotation.y += 0.002
          this.myponts.rotation.y += 0.005 // myponts是地图中的点
        }
        if (this.Allplane && this.myLines) {
          this.Allplane.rotation.y += 0.005
          this.myLines.rotation.y += 0.005 // myLines是地图边缘的点线
        }
      }
      this.renderer.render(this.scene, this.camera)
    }
  }
}
</script>
<style>
.VioletEarth {
  position: absolute;
  width: 100%;
  height: 100%;
}
</style>
