<template>
  <div id="app">
    <!-- 等待蒙板层 -->
    <!-- 这里关于screen及其css最好在打包时注释掉，simo里已经存在 -->
    <div id='lead-screen'>
      <div id="loading">
        <div class="ld-cir"></div>
        <div class="ld-cir"></div>
        <div class="ld-cir"></div>
        <div class="ld-cir"></div>
        <div class="ld-cir"></div>
        <div class="ld-cir"></div>
        <div class="ld-cir"></div>
        <div class="ld-cir"></div>
      </div>
    </div>
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  data: function () {
    return {
      bodyClass: ''
    }
  },
  created: function () {
    var _hash = window.location.hash
    if (_hash.split('/')[1]) {
      var _url = window.location.protocol + '//' + window.location.host + '/index'
      if (process.env.NODE_ENV != 'development') {
        window.location.href = _url
      }
    }
  }
}
</script>

<style>
html,
body {
  width: 100%;
  height: 100%;
  margin: 0px;
}
.sp-replacer {
  width: 100% !important;
  display: flex;
}
.sp-preview {
  flex: 1;
}
.disShow {
  display: none !important;
}
#lead-screen {
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  left: 0px;
  bottom: 0;
  z-index: 20100;
  background: rgba(0, 0, 0, 0.5);
  display: none;
}
#app {
  width: 100%;
  height: 100%;
}
/* 框选区域样式 */
#bottom {
  position: absolute;
  bottom: 0px;
  width: 100%;
  height: 40px;
  border: 1px solid #000;
  background: #000;
  color: #fff;
}
.tempDiv {
  border: 1px dashed blue;
  background: #5a72f8;
  position: absolute;
  z-index: 999999;
  /* filter:alpha(opacity: 10); */
  opacity: 0.3;
}

/* .modal-backdrop {
  z-index: 20099;
} */

/* 气泡图 */
.clear_float:after {
  display: block;
  clear: both;
  content: '';
}
.text_overflow {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
