<!--
  确认提示弹框：
  //默认使用
  this.$ensureModal.confirm('确认要删除？', () => {
    //确认后的回调方法
  })
  //对象参数使用 hideOk:隐藏确定按钮，hideCancel：隐藏取消按钮，hideFooter：隐藏footer
  this.$ensureModal.confirm({content:'确认要删除？',hideFooter: true,hideOk: true,hideCancel: true}, () => {
    //确认后的回调方法
  })
-->
<template>
  <Modal
    v-model="isOpen"
    class="us-xs modal__ensure"
    :class="{ 'modal-ensure-radio': showRadio }"
    title="请选择"
    v-bind="$attrs"
    v-on="$listeners"
    @on-cancel="onCancel"
    @on-ok="onSure"
  >
    {{ content }}
    <RadioGroup v-if="showRadio" v-model="radioVal">
      <Radio v-for="item in radioGroup" :key="item.label" :label="item.label">
        <span>{{ item.text }}</span>
      </Radio>
    </RadioGroup>
    <div v-if="!hideFooter && !showRadio" slot="footer">
      <Button v-show="!hideOk" @click="onSure">
        {{ okText }}
      </Button>
      <Button v-show="showNo" @click="onNoFn">
        {{ noText }}
      </Button>
      <Button v-show="!hideCancel" cancel @click="onCancel('btn')">
        {{ cancelText }}
      </Button>
    </div>
  </Modal>
</template>

<script>
export default {
  name: 'UsModal',
  props: {
    value: {
      type: Boolean,
      default: false
    }
  },
  data () {
    return {
      isOpen: this.value,
      content: '',
      okText: '确认',
      noText: '否',
      cancelText: '取消',
      radioVal: '',
      radioGroup: [],
      showNo: false,
      showRadio: false,
      hideFooter: false,
      hideOk: false,
      hideCancel: false
    }
  },
  watch: {
    value (val) {
      this.isOpen = val
    }
  },
  methods: {
    onSure () {
      this.remove()
      this.onEnsure(this.showRadio ? this.radioVal : '')
    },
    onCancel (name) {
      this.remove()
      this.onClose && this.onClose(name)
    },
    onNoFn () {
      this.remove()
      this.onNo && this.onNo()
    }
  }
}
</script>

<style lang="scss" scoped>
.modal__ensure {
  ::v-deep .ivu-modal {
    min-height: 180px;
    height: auto;
    .ivu-modal-body {
      padding: 20px 20px 0px 20px;
      text-align: center;
    }
  }
  &:not(.modal-ensure-radio) ::v-deep .ivu-modal-footer {
    border: none;
    text-align: center;
    button {
      float: none !important;
      & + button {
        margin: 0 0 0 50px !important;
      }
    }
  }
}
.modal-ensure-radio {
  ::v-deep .ivu-modal {
    width: 440px !important;
  }
  ::v-deep .ivu-radio-group {
    width: 100%;
    padding-left: 80px;
    .ivu-radio-wrapper {
      display: block;
      text-align: left;
      line-height: 30px;
    }
  }
}
</style>
