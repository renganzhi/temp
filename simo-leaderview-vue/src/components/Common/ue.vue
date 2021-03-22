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
import '../../../static/UE/themes/default/css/ueditor.css'
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
  mounted () {
    const _this = this
    this.editor = UE.getEditor('editor', {
      toolbars: [
        ['source', 'undo', 'redo'],
        ['bold', 'italic', 'indent', 'underline', 'fontborder', 'strikethrough', 'justifyleft', // 居左对齐
          'justifyright', // 居右对齐
          'justifycenter', 'superscript', 'fontfamily', 'fontsize', 'subscript', 'removeformat', 'horizontal', 'lineheight',
          'formatmatch', 'autotypeset', 'blockquote', '|',
          'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc']
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
