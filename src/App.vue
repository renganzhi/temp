<template>
  <div id="app">
    <!-- 等待蒙板层 -->
    <div id='screen'>
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
/* * {
  box-sizing: border-box;
} */

/* @font-face {
  font-family: "asn";
  src: url("./../static/font/asn/asn.ttf"), url("./../static/font/asn/asn.eot");
} */
.sp-replacer {
  width: 100% !important;
  display: flex;
}
.sp-preview {
  flex: 1;
}
#screen {
  width: 100%;
  height: 100%;
  position: fixed;
  top: 0;
  bottom: 0;
  z-index: 20100;
  background: rgba(0, 0, 0, 0.5);
  display: none;
}
#app {
  height: 100%;
  /* font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale; */
}
</style>
