<template>
  <div>
    <script id="editor" type="text/plain"></script>
  </div>
</template>
<script>
import '../../../static/UE/ueditor.config.js'
import '../../../static/UE/ueditor.all.js'
import '../../../static/UE/lang/zh-cn/zh-cn.js'
import '../../../static/UE/ueditor.parse.js'
export default {
  name: 'UE',
  data () {
    return {
      editor: null
    }
  },
  props: {
    defaultMsg: {
      type: String
    },
    config: {
      type: Object
    }
  },
  watch: {
    'defaultMsg': function () {
      this.editor.setContent(this.defaultMsg)
    }
  },
  mounted () {
    const _this = this
    this.editor = UE.getEditor('editor', {
      toolbars: [
        ['undo', 'redo'],
        ['bold', 'italic', 'indent', 'underline', 'fontborder', 'strikethrough', 'justifyleft', // 居左对齐
          'justifyright', // 居右对齐
          'justifycenter', 'superscript', 'fontfamily', 'fontsize', 'subscript', 'removeformat', 'horizontal', 'lineheight',
          'formatmatch', 'autotypeset', 'blockquote', '|',
          'forecolor', 'backcolor', 'selectall']
      ],
      autoHeightEnabled: true,
      autoFloatEnabled: true
    }) // 初始化UE
    this.editor.addListener('ready', function () {
      _this.editor.setContent(_this.defaultMsg) // 确保UE加载完成后，放入内容。
    })
  },
  methods: {
    getUEContent () { // 获取内容方法
      return this.editor.getContent()
    }
  },
  destroyed () {
    this.editor.destroy()
  }
}
</script>
<style lang="scss">
#editor iframe {
  background-image:linear-gradient(to right, #141a2d00 0%, #141a2d00 160px, #ffffff00 0%);
  color: #cad6dd
  span,p{
    color: red;
  }
}
.edui-default .edui-editor-toolbarbox{
  position: relative !important;
}
.edui-default .edui-editor-messageholder{
  display: none !important;
}
</style>
