<template>
    <div style="width: 100%;height: 100%;position: relative;">
        <CYMap :style="cyStyle" :nowPageName="nowPageName"></CYMap>
        <iframe :style="iframeStyle"  id="BaiDuIframe" style="height:100%;width:100%;z-index:0"
     src="http://172.16.149.41:8181/appli/start?appliId=987084259741138944&amp;codeRate=8000&amp;frameRate=60" frameborder="0"></iframe>
    </div>
</template>
<script>
import CYMap from '../CYMap/index.vue'
export default {
  components: {CYMap},
  props: ['nowPageName'],
  data () {
    return {
      mapType: false
    }
  },
  computed: {
    cyStyle () {
      return {
        visibility: this.mapType ? 'hidden' : 'visible',
        position: 'absolute',
        top: '0px',
        left: '0px'
      }
    },
    iframeStyle () {
      return {
        visibility: this.mapType ? 'visible' : 'hidden',
        position: 'absolute',
        top: '0px',
        left: '0px'
      }
    }
  },
  mounted () {
    this.bus.$on('SwitchMap', (res) => {
      this.mapType = res
    })
  }
}
</script>
<style scoped>
div{
  position: absolute;
}
</style>
