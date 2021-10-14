<template>
  <Modal
    v-model="mdpram.isShow"
    title="消息配置"
    class="us-md"
    :loading="true"
    :width="580"
    footer-hide
  >
    <div class="form-title">
      声音配置
    </div>
    <Form ref="form" :model="form" :label-width="120">
      <FormItem label="告警铃声" prop="ring" :rules="$rules.required">
        <Select v-model="form.ring" filterable transfer clearable>
          <Option
            v-for="item in levels"
            :key="item.music"
            :value="item.music"
            :label="item.music"
          >
            {{ item.music }}
          </Option>
        </Select>
        <Tooltip content="试听">
          <Button
            class="margin-left-5"
            @click="listenRing"
            style="background:white !important;color:#6c95ff;"
          >
            <i class="icon-n-laba icon" />
          </Button>
        </Tooltip>
      </FormItem>
      <FormItem label="响铃开关" prop="opened">
        <el-switch
          v-model="form.opened"
          active-color="rgb(19, 206, 102)"
          inactive-color="rgb(191, 191, 191)"
          :width="36"
        >
        </el-switch>
      </FormItem>
      <FormItem style="margin-bottom: 0px; text-align: right;">
        <Button style="background:#5c8bff;" @click="onSure">
          确定
        </Button>
        <Button @click="resetCancel" cancel>
          清空
        </Button>
      </FormItem>
    </Form>
    <audio
      id="myaudio"
      :src="'../../static/audio/' + form.ring"
      preload="preload"
    >
      不支持audio标签
    </audio>
  </Modal>
</template>
<script>
export default {
  props: {
    mdpram: {
      type: Object,
      default: () => {}
    }
  },
  data () {
    return {
      // isOpen: this.value,
      modelName: 'voice',
      form: {
        id: '',
        ring: '',
        opened: false
      },
      levels: []
    }
  },
  watch: {
    // value (val) {
    //   this.isOpen = val
    // }
  },
  created () {
    this.axios.get('/msg/config/findRingConfig').then(res => {
      if (res.success) {
        if (res.obj) {
          this.form.id = res.obj.id || ''
          this.form.ring = res.obj.ring || ''
          this.form.opened = res.obj.opened
        }
      }
    })
    //
    this.axios.get('/msg/config/rings').then(res => {
      if (res.success) {
        res.obj.forEach(element => {
          this.levels.push({
            music: element
          })
        })
      }
    })
  },
  methods: {
    listenRing () {
      let audio = new Audio()
      audio.src = require('../../static/audio/' + this.form.ring)
      audio.play()
    },
    onSure () {
      this.$refs.form.validate(valid => {
        if (valid) {
          let myurl = '/msg/config/saveRingConfig'
          const config = {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          }
          const formData = new FormData()
          formData.append('id', this.form.id)
          formData.append('ring', this.form.ring)
          formData.append('opened', this.form.opened)
          this.axios.post(myurl, formData, config).then(res => {
            // this.$store.commit('base/setRing', this.form.ring)
            this.mdpram.isShow = false
            this.$notify({
              message: res.msg,
              position: 'bottom-right',
              customClass: 'toast toast-success'
            })
          })
        }
      })
    },
    resetCancel () {
      let myurl = '/msg/config/delRingConfig'
      this.axios.post(myurl).then(res => {
        // this.mdpram.isShow = false
        this.form.ring = 'alertLevel_10.mp3'
        this.form.opened = true
        this.$notify({
          message: '告警铃声配置重置成功',
          position: 'bottom-right',
          customClass: 'toast toast-success'
        })
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.menu-manage {
  .sub-item {
    width: 25%;
    display: inline-block;
    vertical-align: top;
    margin-bottom: 20px;
    li {
      padding: 0 20px 0 16px;
      height: 30px;
      line-height: 30px;
      i {
        float: right;
        margin: 5px 20px 0 0;
        cursor: pointer;
        // @include getTheme($themes) {
        //   color:themed("theme-color");
        // }
      }
    }
    .sub-li {
      padding: 0 20px 0 0;
      font-size: 14px;
      font-weight: bold;
    }
    .cur-li {
      color: #828bac;
      font-size: 12px;
      // &:hover{
      //   @include getTheme($themes) {
      //     background:themed("menu-mange-li-hover-background-color");
      //   }
      // }
    }
  }
}
.icon {
  color: #436bf6;
  font-size: 20px;
}
/deep/::-webkit-scrollbar-thumb {
  background: #d3d6db !important;
}
/deep/input:not([type='checkbox']):not([type='radio']),
textarea,
select {
  border: solid 1px #cacdd7 !important;
}
/deep/.ivu-modal-wrap
  .ivu-modal
  .ivu-modal-content
  .ivu-modal-header
  .ivu-modal-header-inner::before {
  background: #426bf5;
}
</style>
