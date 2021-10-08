<template>
  <div class="flex-ve mymoveBox">
      <Component
        :item='item'
        :is="item.iputneId.split('&')[1] === 'vmware' ? 'VmTree' : 'CasTree'"
        :key="key"
      />
  </div>
</template>

<script>
import VmTree from './vmTree.vue'
import CasTree from './casTree.vue'
export default {
  name: 'VmView',
  components:{
    VmTree,CasTree
  },
  props: ['item'],
  data () {
    return {
      key: new Date().getTime()
    }
  },
}
</script>
<style scoped>
.mymoveBox{
  width: 100%;
  height: 100%; 
  padding: 10px;
}
</style>
