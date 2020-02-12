<template>
  <input size="16"
         type="text"
         class="uform-control addformHeight"
         name="color">
</template>
<script>
import colorSpectrum from '#/js/colorSpectrum.js'
export default {
  name: 'vcolor',
  props: ['data', 'type', 'index'],
  data () {
    return {
      color: '',
      newColor: ''
    }
  },
  mounted () {
    $(this.$el).parent().find('.sp-replacer').remove()
    this.init()
  },
  watch: {
    'data' (newV) {
      $(this.$el).spectrum('set', newV || 'rgba(255,255,255,0)')
    },
    newColor (newV, oldV) {
      this.senDataSee(newV)
    }
  },
  methods: {
    init () {
      var vm = this
      this.color = this.data
      colorSpectrum($(this.$el), {
        color: this.color,
        move: function (color) {
          vm.changeColor(color)
        },
        change: function (color) {
          vm.changeColor(color)
        }
      })
    },
    changeColor: function (color) {
      if (color == null) {
        this.newColor = 'rgba(255,255,255,0)'
      } else {
        this.newColor = this.setRgba(color)
      }
    },
    setRgba: function (color) {
      if (color) {
        color = 'rgba(' + Math.round(color._r) + ',' + Math.round(color._g) + ',' + Math.round(color._b) + ',' + color._a + ')'
        return color
      }
    },
    senDataSee (val) {
      this.$emit('getdata', {
        color: val,
        type: this.type,
        index: this.index
      })
    }
  },
  destroyed: function () {
    $(this.$el).off().spectrum('destroy')
  }
}
</script>
<style lang="scss">
.gradient,
.barGradient {
  .sp-dd {
    display: none;
  }
  .sp-preview {
    margin-right: 0px;
  }
  .sp-replacer {
    padding: 0px;
    border: none;
  }
  .sp-replacer:hover {
    border: none;
  }
  .sp-preview {
    height: 10px;
    border: none;
  }
}
// 可能会影响系统中其它的
// .sp-replacer,
// .sp-replacer:hover {
//   background: transparent;
//   border: solid 1px #3d445a;
//   color: #3d445a;
// }
</style>
