<template>
  <div class="editor-container" ref="myEditBox"></div>
</template>
<script>
import UE from '../../Common/ue'
export default {
  components: { UE },
  props: ['item'],
  data () {
    return {}
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
<style lang='scss'>
.editor-container {
  color: #afadad;
  height: 100%;
  width: 100%;
  overflow: auto;
  td,
  th {
    padding: 5px 10px;
    border: 1px solid #ddd;
    vertical-align: top;
  }
}
</style>
