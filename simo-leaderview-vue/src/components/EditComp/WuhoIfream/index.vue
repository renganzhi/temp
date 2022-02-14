<template>
  <div class="WuhoIfream">
      <iframe style="height:100%;width:100%" src="http://183.131.193.69:8181/appli/start?appliId=934133524695351296&codeRate=8000&frameRate=30" frameborder="0"></iframe>
  </div>
</template>
<script>
export default {
  name: 'WuhoIfream',
  props: ['item'],
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
