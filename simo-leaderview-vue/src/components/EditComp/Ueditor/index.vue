<template>
    <div class="editor-container" ref="myEditBox">
    </div>
</template>
<script>
import UE from '../../Common/ue'
export default {
  components: {UE},
  props: ['item'],
  data () {
    return {
    }
  },
  watch: {
    'item.chartData': function () {
      this.changeData()
    }
  },
  mounted () {
    this.changeData()
  },
  methods: {
    changeData () {
      this.$refs.myEditBox.innerHTML = this.item.chartData
    }
  }
}
</script>
<style>
.editor-container{
  height: 100%;
  width: 100%;
  overflow: auto;
}
</style>
