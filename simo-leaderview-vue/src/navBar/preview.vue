<template>
  <Modal
    v-model="mdpram.isShow"
    title="预览"
    :width="800"
    :loading="true"
    footer-hide
  >
    <img :src="mdpram.picUrl" style="width: 100%;height:520px;" />
  </Modal>
</template>

<script>
export default {
  name: "Preview",
  props: {
    mdpram: {
      type: Object,
      default: () => {}
    }
  }
};
</script>
