<template>
  <div class="v-charts-data-empty">
    <keep-alive>
      <pageShow :nowPageID="nowPageID"></pageShow>
      <!-- <iframe style="height:100%;width:100%;position: absolute;z-index:0" src="http://183.131.193.69:8181/appli/start?appliId=934133524695351296&codeRate=8000&frameRate=30" frameborder="0"></iframe> -->
    </keep-alive>
</div>
</template>
<script>
import pageShow from '@/views/pageShow/pageShow'
export default {
  name: 'WuhoIfream',
  props: ['nowPageID'],
  components: {pageShow},
  data () {
    return {
      visible: false
    }
  }
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
