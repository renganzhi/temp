<template>
  <div id="app">
    <!-- <img src="./assets/logo.png"> -->
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
* {
  box-sizing: border-box;
}
.sp-replacer {
  width: 100% !important;
  display: flex;
}
.sp-preview{
  flex: 1;
}
#app {
  height: 100%;
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
