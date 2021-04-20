<template>
  <div class="vdr"
       :style="{'width': this.w + 'px', 'height': this.h + 'px', 'left': this.x + 'px', 'top': this.y + 'px', 'z-index': this.zIndex}"
       :class="active || is-active ? 'active' : 'inactive'"
       @mousedown="bodyDown($event)"
       @dblclick="dbClick($event)"
       @contextmenu.prevent="contextMenu($event)"
       @touchstart="bodyDown($event)"
       @touchend="up($event)">
    <slot></slot>
    <div class="box_sticks">
      <div v-for="(stick,index) in sticks"
         :key="index"
         class="vdr-stick"
         :class="['vdr-stick-' + stick, isResizable ? '' : 'not-resizable']"
         @mousedown.stop.prevent="stickDown(stick, $event)"
         @touchstart.stop.prevent="stickDown(stick, $event)"
         :style="vdrStick(stick)">
      </div>
    </div>
    <div class="grid_line" v-if="false">
      <div class="grid_line_top"></div>
      <div class="grid_line_left"></div>
      <div class="grid_line_label">{{x}}, {{y}}</div>
    </div>
  </div>
</template>
<script>
// var overflowPx = 20 // 可溢出范围
import { mapGetters } from 'vuex'
const stickSize = 6
const styleMapping = {
  y: {
    t: 'top',
    m: 'marginTop',
    b: 'bottom'
  },
  x: {
    l: 'left',
    m: 'marginLeft',
    r: 'right'
  }
}

export default {
  name: 'dragResize',
  props: {
    insideFlag: { // 是否是组合内的元件
      type: Boolean, default: false
    },
    hasChild: {
      type: Boolean, default: false // 内部是否还有拖拽组件
    },
    parentScaleX: {
      type: Number, default: 1
    },
    scale: {
      type: Number, default: 100
    },
    parentScaleY: {
      type: Number, default: 1
    },
    isActive: { // 是否激活状态，处于激活状态才能进行拖拽与缩放等操作
      type: Boolean, default: false
    },
    preventActiveBehavior: { // true，就可以解决在其他区域操作返回到组件区域的时候，不需要再次点击就激活组件;原组件不满足需求，做修改
      type: Boolean, default: false
    },
    isDraggable: { // 是否允许拖拽
      type: Boolean, default: false
    },
    isResizable: { // 是否允许缩放
      type: Boolean, default: true
    },
    aspectRatio: { // 是否等比例缩放
      type: Boolean, default: false
    },
    parentLimitation: { // 设置为true，则限制操作组件不能超出父级元素
      type: Boolean, default: true
    },
    snapToGrid: {
      type: Boolean, default: false
    },
    gridX: {
      type: Number,
      default: 50,
      validator: function (val) {
        return val > 0
      }
    },
    gridY: {
      type: Number,
      default: 50,
      validator: function (val) {
        return val > 0
      }
    },
    parentW: { // 父级宽度，该值限制了元素可以拖动的水平最大宽度，前提是parentLimitation=true
      type: Number,
      default: 0,
      validator: function (val) {
        return val >= 0
      }
    },
    parentH: {// 父级高度，该值限制了元素可以拖动的水平最大高度，前提是parentLimitation=true
      type: Number,
      default: 0,
      validator: function (val) {
        return val >= 0
      }
    },
    w: { // 组件宽
      type: Number,
      default: 100,
      validator: function (val) {
        return val > 0
      }
    },
    h: { // 组件高
      type: Number,
      default: 100,
      validator: function (val) {
        return val > 0
      }
    },
    minw: {
      type: Number,
      default: 20,
      validator: function (val) {
        return val > 0
      }
    },
    minh: {
      type: Number,
      default: 20,
      validator: function (val) {
        return val > 0
      }
    },
    x: { // left
      type: Number,
      default: 0,
      validator: function (val) {
        return typeof val === 'number'
      }
    },
    y: { // top
      type: Number,
      default: 0,
      validator: function (val) {
        return typeof val === 'number'
      }
    },
    z: { // 层级，元素激活时层级最高
      type: [String, Number],
      default: 'auto',
      validator: function (val) {
        let valid = (typeof val === 'string') ? val === 'auto' : val >= 0
        return valid
      }
    },
    dragHandle: { // 定义拖拽时的classname
      type: String,
      default: null
    },
    dragCancel: { // 定义取消拖拽时的classname
      type: String,
      default: null
    },
    sticks: { // 缩放节点定义
      type: Array,
      default: function () {
        return ['tl', 'tm', 'tr', 'mr', 'br', 'bm', 'bl', 'ml']
      }
    },
    axis: { // 允许拖拽的方向，取值可以为x、 y、 both、none
      type: String,
      default: 'both',
      validator: function (val) {
        return ['x', 'y', 'both', 'none'].indexOf(val) !== -1
      }
    },
    item: {
      // type:
      default: null
    }
  },
  data () {
    return {
      active: this.isActive,
      rawWidth: this.w,
      rawHeight: this.h,
      rawLeft: this.x,
      rawTop: this.y,
      rawRight: null,
      rawBottom: null,
      zIndex: this.z,
      aspectFactor: this.w / this.h,
      parentWidth: null,
      parentHeight: null,
      left: this.x,
      top: this.y,
      right: null,
      bottom: null,
      minWidth: this.minw,
      minHeight: this.minh,
      is: ''
    }
  },
  created: function () {
    this.stickDrag = false
    this.bodyDrag = false
    this.stickAxis = null
    this.stickStartPos = { mouseX: 0, mouseY: 0, x: 0, y: 0, w: 0, h: 0 }
    this.limits = {
      minLeft: null,
      maxLeft: null,
      minRight: null,
      maxRight: null,
      minTop: null,
      maxTop: null,
      minBottom: null,
      maxBottom: null
    }
    this.currentStick = []
  },
  mounted: function () {
    this.parentElement = this.$el.parentNode
    this.parentWidth = this.parentW ? this.parentW : this.parentElement.clientWidth
    this.parentHeight = this.parentH ? this.parentH : this.parentElement.clientHeight

    this.rawRight = this.parentWidth - this.rawWidth - this.rawLeft
    this.rawBottom = this.parentHeight - this.rawHeight - this.rawTop

    document.documentElement.addEventListener('mousemove', this.move)
    document.documentElement.addEventListener('mouseup', this.up)
    document.documentElement.addEventListener('mouseleave', this.up)

    document.documentElement.addEventListener('mousedown', this.deselect)

    document.documentElement.addEventListener('touchmove', this.move, true)
    document.documentElement.addEventListener('touchend touchcancel', this.up, true)
    document.documentElement.addEventListener('touchstart', this.up, true)

    if (this.dragHandle) {
      let dragHandles = Array.prototype.slice.call(this.$el.querySelectorAll(this.dragHandle))
      for (let i in dragHandles) {
        dragHandles[i].setAttribute('data-drag-handle', this._uid)
      }
    }

    if (this.dragCancel) {
      let cancelHandles = Array.prototype.slice.call(this.$el.querySelectorAll(this.dragCancel))
      for (let i in cancelHandles) {
        cancelHandles[i].setAttribute('data-drag-cancel', this._uid)
      }
    }
  },

  beforeDestroy: function () {
    document.documentElement.removeEventListener('mousemove', this.move)
    document.documentElement.removeEventListener('mouseup', this.up)
    document.documentElement.removeEventListener('mouseleave', this.up)

    document.documentElement.removeEventListener('mousedown', this.deselect)

    document.documentElement.removeEventListener('touchmove', this.move, true)
    document.documentElement.removeEventListener('touchend touchcancel', this.up, true)
    document.documentElement.removeEventListener('touchstart', this.up, true)
  },

  methods: {
    deselect (e) {
      e = e || window.event// debugger
      //  this.$emit('bodyDown', this.item,this.rect,e);
      //  debugger
      //   console.log(e.target,this)
      /*  if (this.preventActiveBehavior) {
              return
          }
          this.active = false */
      //   e.stopPropagation();
    },

    move (ev) {
      if (!this.stickDrag && !this.bodyDrag) {
        return
      }

      ev.stopPropagation()

      if (this.stickDrag) {
        this.stickMove(ev)
      }
      if (this.bodyDrag) {
        this.bodyMove(ev)
      }
    },

    up (ev) {
      if (this.stickDrag) {
        this.stickUp(ev)
      }
      if (this.bodyDrag) {
        this.bodyUp(ev)
      }
    },
    dbClick: function (ev) {
      if (this.hasChild || this.insideFlag) {
        this.active = true
      } else {
        this.active = false
      }
      this.$emit('dbclick')
    },
    contextMenu: function (ev) {
      // this.active = false
      this.$emit('contextMenu', this.item, ev)
    },
    bodyDown: function (ev) { // debugger
      let target = ev.target || ev.srcElement

      if (!this.preventActiveBehavior) {
        this.active = true
      }

      if (ev.button && ev.button !== 0) {
        return
      }

      // this.$emit('clicked', ev);
      //  alert(this.isDraggable)
      if (!this.isDraggable) {
        return
      }

      if (this.dragHandle && target.getAttribute('data-drag-handle') !== this._uid.toString()) {
        return
      }

      if (this.dragCancel && target.getAttribute('data-drag-cancel') === this._uid.toString()) {
        return
      }

      ev.stopPropagation()
      // if (this.item.chartType !== 'text' || this.item.ctDataSource !== 'static') {
      //   ev.preventDefault()
      // }
      if (!(this.item.chartType === 'NEWtextArea' || this.item.chartType === 'text') || this.item.ctDataSource !== 'static') {
        ev.preventDefault()
      }

      this.bodyDrag = true

      this.stickStartPos.mouseX = typeof ev.pageX !== 'undefined' ? ev.pageX : ev.touches[0].pageX
      this.stickStartPos.mouseY = typeof ev.pageY !== 'undefined' ? ev.pageY : ev.touches[0].pageY

      this.stickStartPos.left = this.left
      this.stickStartPos.right = this.right
      this.stickStartPos.top = this.top
      this.stickStartPos.bottom = this.bottom

      if (this.parentLimitation) {
        this.limits = this.calcDragLimitation()
      }
      this.$emit('bodyDown', this.item)
    },

    calcDragLimitation () {
      const parentWidth = this.parentWidth
      const parentHeight = this.parentHeight
      var overflowPx = this.insideFlag ? 0 : 20
      return {
        minLeft: -overflowPx,
        maxLeft: parentWidth - this.width + overflowPx,
        minRight: -overflowPx,
        maxRight: parentWidth - this.width + overflowPx,
        minTop: -overflowPx,
        maxTop: parentHeight - this.height + overflowPx,
        minBottom: -overflowPx,
        maxBottom: parentHeight - this.height + overflowPx
      }
    },

    bodyMove (ev) {
      const stickStartPos = this.stickStartPos
      const parentWidth = this.parentWidth
      const parentHeight = this.parentHeight
      const gridX = this.gridX
      const gridY = this.gridY
      const width = this.width
      const height = this.height
      const pageX = typeof ev.pageX !== 'undefined' ? ev.pageX : ev.touches[0].pageX
      const pageY = typeof ev.pageY !== 'undefined' ? ev.pageY : ev.touches[0].pageY

      let delta = {
        x: (this.axis !== 'y' && this.axis !== 'none' ? stickStartPos.mouseX - pageX : 0) / this.parentScaleX,
        y: (this.axis !== 'x' && this.axis !== 'none' ? stickStartPos.mouseY - pageY : 0) / this.parentScaleY
      }

      let newTop = stickStartPos.top - delta.y / this.scale * 100
      let newBottom = Number(stickStartPos.bottom) + Number(delta.y) / this.scale * 100
      let newLeft = stickStartPos.left - delta.x / this.scale * 100
      let newRight = Number(stickStartPos.right) + Number(delta.x) / this.scale * 100

      if (this.snapToGrid) {
        let alignTop = true
        let alignLeft = true

        let diffT = newTop - Math.floor(newTop / gridY) * gridY
        let diffB = (parentHeight - newBottom) - Math.floor((parentHeight - newBottom) / gridY) * gridY
        let diffL = newLeft - Math.floor(newLeft / gridX) * gridX
        let diffR = (parentWidth - newRight) - Math.floor((parentWidth - newRight) / gridX) * gridX

        if (diffT > (gridY / 2)) { diffT = diffT - gridY }
        if (diffB > (gridY / 2)) { diffB = diffB - gridY }
        if (diffL > (gridX / 2)) { diffL = diffL - gridX }
        if (diffR > (gridX / 2)) { diffR = diffR - gridX }

        if (Math.abs(diffB) < Math.abs(diffT)) { alignTop = false }
        if (Math.abs(diffR) < Math.abs(diffL)) { alignLeft = false }

        newTop = newTop - (alignTop ? diffT : diffB)
        newBottom = parentHeight - height - newTop
        newLeft = newLeft - (alignLeft ? diffL : diffR)
        newRight = parentWidth - width - newLeft
      }

      if (this.rawLeft === this.rect.left || this.rawTop === this.rect.top) {
        let chgX = 0
        let chgY = 0
        if (this.rawLeft === this.rect.left) {
          chgX = newLeft - this.rawLeft
          // console.log(this.rawLeft, newLeft, this.rect.left)
        }
        if (this.rawTop === this.rect.top) {
          chgY = newTop - this.rawTop
        }
        if (!this.onlyOneItem) {
          // console.log('-----这里是多元件拖拽的边界判断-----')
          if (this.limitItem.minX + chgX < -20) {
            newLeft = this.rawLeft + (-20 - this.limitItem.minX)
            chgX = newLeft - this.rawLeft
          } else if (this.limitItem.maxX + chgX > this.homeData.width + 20) {
            newLeft = this.rawLeft + (this.homeData.width + 20 - this.limitItem.maxX)
            chgX = (this.homeData.width + 20 - this.limitItem.maxX)
          }
          if (this.limitItem.minY + chgY < -20) {
            newTop = this.rawTop + (-20 - this.limitItem.minY)
            chgY = newTop - this.rawTop
          } else if (this.limitItem.maxY + chgY > this.homeData.height + 20) {
            chgY = (this.homeData.height + 20 - this.limitItem.maxY)
            newTop = this.rawTop + chgY
          }
          newBottom = parentHeight - height - newTop
          newRight = parentWidth - width - newLeft
        }
        this.$emit('dragging', chgX, chgY, this.rect)
      }

      this.rawTop = newTop
      this.rawBottom = newBottom
      this.rawLeft = newLeft
      this.rawRight = newRight
      this.$emit('bodymove', this.item, this.rect)
      if (this.insideFlag) {
        this.rect.id = this.item.id
        this.$emit('childResize', this.rect)
      }
      // this.$emit('dragidex', this.rect); //  多加一个传值，告诉父组件，当前事件位于哪一个上面
      // this.$emit('sigleindex', this.rect); //  单个时监听，判断当前修改的是哪一个
    },

    bodyUp () {
      this.bodyDrag = false
      // this.$emit('dragging', this.item, this.rect)
      this.$emit('dragstop', this.item, this.rect)

      this.stickStartPos = { mouseX: 0, mouseY: 0, x: 0, y: 0, w: 0, h: 0 }
      this.limits = {
        minLeft: null,
        maxLeft: null,
        minRight: null,
        maxRight: null,
        minTop: null,
        maxTop: null,
        minBottom: null,
        maxBottom: null
      }
    },

    stickDown: function (stick, ev) {
      if (!this.isResizable || !this.active) {
        return
      }

      this.stickDrag = true
      this.stickStartPos.mouseX = typeof ev.pageX !== 'undefined' ? ev.pageX : ev.touches[0].pageX
      this.stickStartPos.mouseY = typeof ev.pageY !== 'undefined' ? ev.pageY : ev.touches[0].pageY
      this.stickStartPos.left = this.left
      this.stickStartPos.right = this.right
      this.stickStartPos.top = this.top
      this.stickStartPos.bottom = this.bottom
      this.currentStick = stick.split('')
      this.stickAxis = null

      switch (this.currentStick[0]) {
        case 'b':
          this.stickAxis = 'y'
          break
        case 't':
          this.stickAxis = 'y'
          break
      }
      switch (this.currentStick[1]) {
        case 'r':
          this.stickAxis = this.stickAxis === 'y' ? 'xy' : 'x'
          break
        case 'l':
          this.stickAxis = this.stickAxis === 'y' ? 'xy' : 'x'
          break
      }

      this.limits = this.calcResizeLimitation()
    },

    calcResizeLimitation () {
      let minw = this.minWidth
      let minh = this.minHeight
      const aspectFactor = this.aspectFactor
      const width = this.width
      const height = this.height
      const bottom = this.bottom
      const top = this.top
      const left = this.left
      const right = this.right
      const stickAxis = this.stickAxis

      const parentLim = this.parentLimitation ? 0 : null

      if (this.aspectRatio) {
        if (minw / minh > aspectFactor) {
          minh = minw / aspectFactor
        } else {
          minw = aspectFactor * minh
        }
      }

      let limits = {
        minLeft: parentLim,
        maxLeft: left + (width - minw),
        minRight: parentLim,
        maxRight: right + (width - minw),
        minTop: parentLim,
        maxTop: top + (height - minh),
        minBottom: parentLim,
        maxBottom: bottom + (height - minh)
      }

      if (this.aspectRatio) {
        const aspectLimits = {
          minLeft: left - (Math.min(top, bottom) * aspectFactor) * 2,
          maxLeft: left + ((((height - minh) / 2) * aspectFactor) * 2),

          minRight: right - (Math.min(top, bottom) * aspectFactor) * 2,
          maxRight: right + ((((height - minh) / 2) * aspectFactor) * 2),

          minTop: top - (Math.min(left, right) / aspectFactor) * 2,
          maxTop: top + ((((width - minw) / 2) / aspectFactor) * 2),

          minBottom: bottom - (Math.min(left, right) / aspectFactor) * 2,
          maxBottom: bottom + ((((width - minw) / 2) / aspectFactor) * 2)
        }

        if (stickAxis === 'x') {
          limits = {
            minLeft: Math.max(limits.minLeft, aspectLimits.minLeft),
            maxLeft: Math.min(limits.maxLeft, aspectLimits.maxLeft),
            minRight: Math.max(limits.minRight, aspectLimits.minRight),
            maxRight: Math.min(limits.maxRight, aspectLimits.maxRight)
          }
        } else if (stickAxis === 'y') {
          limits = {
            minTop: Math.max(limits.minTop, aspectLimits.minTop),
            maxTop: Math.min(limits.maxTop, aspectLimits.maxTop),
            minBottom: Math.max(limits.minBottom, aspectLimits.minBottom),
            maxBottom: Math.min(limits.maxBottom, aspectLimits.maxBottom)
          }
        }
      }

      return limits
    },

    stickMove (ev) {
      const stickStartPos = this.stickStartPos
      const pageX = typeof ev.pageX !== 'undefined' ? ev.pageX : ev.touches[0].pageX
      const pageY = typeof ev.pageY !== 'undefined' ? ev.pageY : ev.touches[0].pageY

      const delta = {
        x: (stickStartPos.mouseX - pageX) / this.parentScaleX,
        y: (stickStartPos.mouseY - pageY) / this.parentScaleY
      }

      let newTop = stickStartPos.top - delta.y
      let newBottom = stickStartPos.bottom + delta.y
      let newLeft = stickStartPos.left - delta.x
      let newRight = stickStartPos.right + delta.x

      switch (this.currentStick[0]) {
        case 'b':

          if (this.snapToGrid) {
            newBottom = this.parentHeight - Math.round((this.parentHeight - newBottom) / this.gridY) * this.gridY
          }

          this.rawBottom = newBottom
          break

        case 't':

          if (this.snapToGrid) {
            newTop = Math.round(newTop / this.gridY) * this.gridY
          }

          this.rawTop = newTop
          break
      }

      switch (this.currentStick[1]) {
        case 'r':

          if (this.snapToGrid) {
            newRight = this.parentWidth - Math.round((this.parentWidth - newRight) / this.gridX) * this.gridX
          }

          this.rawRight = newRight
          break

        case 'l':

          if (this.snapToGrid) {
            newLeft = Math.round(newLeft / this.gridX) * this.gridX
          }

          this.rawLeft = newLeft
          break
      }
      /*  this.$parent.item.width = this.width;
        this.$parent.item.height = this.height;
        console.log(this.item); */
      // this.$emit('resizeindex', this.rect); //  多加一个传值，告诉父组件，当前事件位于哪一个上面
      // this.$emit('sigleindex', this.rect); //  单个时监听，判断当前修改的是哪一个
      //     debugger
      this.$emit('resizing', this.item, this.rect)
      if (this.insideFlag) {
        this.rect.id = this.item.id
        this.$emit('childResize', this.rect)
      }
    },

    stickUp () {
      this.stickDrag = false
      this.stickStartPos = {
        mouseX: 0,
        mouseY: 0,
        x: 0,
        y: 0,
        w: 0,
        h: 0
      }
      this.limits = {
        minLeft: null,
        maxLeft: null,
        minRight: null,
        maxRight: null,
        minTop: null,
        maxTop: null,
        minBottom: null,
        maxBottom: null
      }
      this.rawTop = this.top
      this.rawBottom = this.bottom
      this.rawLeft = this.left
      this.rawRight = this.right

      this.stickAxis = null

      // this.$emit('resizing', this.rect);
      this.$emit('resizestop', this.item, this.rect)
    },

    aspectRatioCorrection () {
      if (!this.aspectRatio) {
        return
      }

      const bottom = this.bottom
      const top = this.top
      const left = this.left
      const right = this.right
      const width = this.width
      const height = this.height
      const aspectFactor = this.aspectFactor
      const currentStick = this.currentStick

      if (width / height > aspectFactor) {
        let newWidth = aspectFactor * height

        if (currentStick[1] === 'l') {
          this.left = left + width - newWidth
        } else {
          this.right = right + width - newWidth
        }
      } else {
        let newHeight = width / aspectFactor

        if (currentStick[0] === 't') {
          this.top = top + height - newHeight
        } else {
          this.bottom = bottom + height - newHeight
        }
      }
    }
  },

  computed: {
    ...mapGetters([
      'onlyOneItem',
      'limitItem',
      'homeData'
    ]),
    left1: {
      get: function () {
        return this.x
      },
      set: function (value) {
        this.$emit('update:x', value)
      }
    },
    top1: {
      get: function () {
        return this.y
      },
      set: function (value) {
        this.$emit('update:y', value)
      }
    },

    style () {
      return {
        top: this.top + 'px',
        left: this.left + 'px',
        width: this.width + 'px',
        height: this.height + 'px',
        zIndex: this.zIndex
      }
    },

    vdrStick () {
      return (stick) => {
        const stickStyle = {
          width: `${stickSize / this.parentScaleX * 2}px`,
          height: `${stickSize / this.parentScaleY * 2}px`
        }
        stickStyle[styleMapping.y[stick[0]]] = `${stickSize / this.parentScaleX / -1}px`
        stickStyle[styleMapping.x[stick[1]]] = `${stickSize / this.parentScaleX / -1}px`
        return stickStyle
      }
    },

    width () {
      return this.parentWidth - this.left - this.right
    },

    height () {
      return this.parentHeight - this.top - this.bottom
    },

    rect () {
      return {
        left: Math.round(this.left),
        top: Math.round(this.top),
        width: Math.round(this.width),
        height: Math.round(this.height)
      }
    }
  },

  watch: {
    rawLeft (newLeft) {
      const limits = this.limits
      const stickAxis = this.stickAxis
      const aspectFactor = this.aspectFactor
      const aspectRatio = this.aspectRatio
      const left = this.left
      const bottom = this.bottom
      const top = this.top

      if (limits.minLeft !== null && newLeft < limits.minLeft) {
        newLeft = limits.minLeft
      } else if (limits.maxLeft !== null && limits.maxLeft < newLeft) {
        newLeft = limits.maxLeft
      }

      if (aspectRatio && stickAxis === 'x') {
        const delta = left - newLeft
        this.rawTop = top - (delta / aspectFactor) / 2
        this.rawBottom = bottom - (delta / aspectFactor) / 2
      }

      this.left = newLeft
    },

    rawRight (newRight) {
      const limits = this.limits
      const stickAxis = this.stickAxis
      const aspectFactor = this.aspectFactor
      const aspectRatio = this.aspectRatio
      const right = this.right
      const bottom = this.bottom
      const top = this.top

      if (limits.minRight !== null && newRight < limits.minRight) {
        newRight = limits.minRight
      } else if (limits.maxRight !== null && limits.maxRight < newRight) {
        newRight = limits.maxRight
      }

      if (aspectRatio && stickAxis === 'x') {
        const delta = right - newRight
        this.rawTop = top - (delta / aspectFactor) / 2
        this.rawBottom = bottom - (delta / aspectFactor) / 2
      }

      this.right = newRight
    },

    rawTop (newTop) {
      const limits = this.limits
      const stickAxis = this.stickAxis
      const aspectFactor = this.aspectFactor
      const aspectRatio = this.aspectRatio
      const right = this.right
      const left = this.left
      const top = this.top

      if (limits.minTop !== null && newTop < limits.minTop) {
        newTop = limits.minTop
      } else if (limits.maxTop !== null && limits.maxTop < newTop) {
        newTop = limits.maxTop
      }

      if (aspectRatio && stickAxis === 'y') {
        const delta = top - newTop
        this.rawLeft = left - (delta * aspectFactor) / 2
        this.rawRight = right - (delta * aspectFactor) / 2
      }

      this.top = newTop
    },

    rawBottom (newBottom) {
      const limits = this.limits
      const stickAxis = this.stickAxis
      const aspectFactor = this.aspectFactor
      const aspectRatio = this.aspectRatio
      const right = this.right
      const left = this.left
      const bottom = this.bottom

      if (limits.minBottom !== null && newBottom < limits.minBottom) {
        newBottom = limits.minBottom
      } else if (limits.maxBottom !== null && limits.maxBottom < newBottom) {
        newBottom = limits.maxBottom
      }

      if (aspectRatio && stickAxis === 'y') {
        const delta = bottom - newBottom
        this.rawLeft = left - (delta * aspectFactor) / 2
        this.rawRight = right - (delta * aspectFactor) / 2
      }

      this.bottom = newBottom
    },

    width (newV) {
      this.aspectRatioCorrection()
    },

    height (newV) {
      this.aspectRatioCorrection()
    },

    active (isActive) {
      if (isActive) {
        this.$emit('activated')
      } else {
        this.$emit('deactivated')
      }
    },

    isActive (val) {
      this.active = val
    },

    z (val) {
      if (val >= 0 || val === 'auto') {
        this.zIndex = val
      }
    },

    aspectRatio (val) {
      if (val) {
        this.aspectFactor = this.width / this.height
      }
    },

    minw (val) {
      if (val > 0 && val <= this.width) {
        this.minWidth = val
      }
    },

    minh (val) {
      if (val > 0 && val <= this.height) {
        this.minHeight = val
      }
    },

    x () {
      if (this.stickDrag || this.bodyDrag) {
        return
      }
      if (this.parentLimitation) {
        this.limits = this.calcDragLimitation()
      }

      let delta = this.x - this.left
      this.rawLeft = this.x
      this.rawRight = this.right - delta
    },

    y () {
      if (this.stickDrag || this.bodyDrag) {
        return
      }

      if (this.parentLimitation) {
        this.limits = this.calcDragLimitation()
      }

      let delta = this.y - this.top
      this.rawTop = this.y
      this.rawBottom = this.bottom - delta
    },

    w () {
      if (this.stickDrag || this.bodyDrag) {
        return
      }

      this.currentStick = ['m', 'r']
      this.stickAxis = 'x'

      if (this.parentLimitation) {
        this.limits = this.calcResizeLimitation()
      }

      let delta = this.width - this.w
      this.rawRight = this.right + delta
    },

    h () {
      if (this.stickDrag || this.bodyDrag) {
        return
      }

      this.currentStick = ['b', 'm']
      this.stickAxis = 'y'

      if (this.parentLimitation) {
        this.limits = this.calcResizeLimitation()
      }

      let delta = this.height - this.h
      this.rawBottom = this.bottom + delta
    },

    parentW (val) {
      this.right = val - this.width - this.left
      this.parentWidth = val
    },

    parentH (val) {
      this.bottom = val - this.height - this.top
      this.parentHeight = val
    },
    isDraggable (val) {
      this.isDraggable = val
    }
  },
  destroyed: function () {
  }
}
</script>
<style lang="scss" scoped>
.vdr {
  position: absolute;
  box-sizing: border-box;
}
.vdr.active:before {
  content: "";
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  box-sizing: border-box;
  outline: 1px dashed #0088cc;
}
.vdr-stick {
  box-sizing: border-box;
  position: absolute;
  font-size: 1px;
  background: #ffffff;
  border: 1px solid #6c6c6c;
  box-shadow: 0 0 2px #bbb;
  border-radius: 50%;
}
.inactive .vdr-stick {
  display: none;
}
.vdr-stick-tl,
.vdr-stick-br {
  cursor: nwse-resize;
}
.vdr-stick-tm,
.vdr-stick-bm {
  left: 50%;
  cursor: ns-resize;
}
.vdr-stick-tr,
.vdr-stick-bl {
  cursor: nesw-resize;
}
.vdr-stick-ml,
.vdr-stick-mr {
  top: 50%;
  cursor: ew-resize;
}
.vdr-stick.not-resizable {
  display: none;
}

.grid_line .grid_line_label {
  visibility: hidden;
}
.active {
  .grid_line {
    .grid_line_top, .grid_line_left, .grid_line_label {
      position: absolute;
      left: 0;
      top: 0;
    }
    .grid_line_top {
      width: 0;
      height: 10000px;
      border-left: 1px dashed #0088cc;
      transform: translateY(-100%);
    }
    .grid_line_left {
      width: 10000px;
      height: 0;
      border-top: 1px dashed #0088cc;
      transform: translateX(-100%);
    }
    .grid_line_label {
      visibility: visible;
      padding: 4px;
      color: #0088cc;
      transform: translate(-100%, -100%);
    }
  }
}

</style>
