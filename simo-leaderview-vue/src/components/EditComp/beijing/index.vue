<template>
  <div class="v-charts-data-empty">
    <keep-alive>
      <pageShow v-if="isSZTJCT" :nowPageName="nowPageName"></pageShow>
      <pageShow2 v-else :nowPageName="nowPageName"></pageShow2>
      <!-- <iframe style="height:100%;width:100%;position: absolute;z-index:0" src="http://183.131.193.69:8181/appli/start?appliId=934133524695351296&codeRate=8000&frameRate=30" frameborder="0"></iframe> -->
    </keep-alive>
</div>
</template>
<script>
import pageShow from '@/views/pageShow/pageShow'
import pageShow2 from '@/views/pageShow/pageShow2'
export default {
  name: 'WuhoIfream',
  props: ['nowPageName'],
  components: {pageShow,pageShow2},
  data () {
    return {
      visible: false,
      isSZTJCT: false
    }
  },
  watch: {
    'nowPageName': function () {
      if (this.nowPageName && this.nowPageName.indexOf('涉藏应急处突') >= 0) {
        this.isSZTJCT = true
      } else {
        this.isSZTJCT = false
      }
    }
  },
}
</script>
<style scoped lang="scss">
.WuhoIfream{
    width: 100%;
    height: 100%;
    padding: 5px;
    iframe{
      height: 100%;
      width: 100%;
    }
}
</style>
