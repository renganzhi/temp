<template>
  <Modal
    v-model="mdpram.show"
    title="消息配置"
    class="us-md"
    :loading="true"
    footer-hide
    @on-cancel="onCancel"
  >
    <Tabs v-model="modelName" :animated="false">
      <TabPane label="声音配置" name="voice">
        <Form ref="form" :model="form">
          <FormItem label="告警铃声" prop="ring" :rules="$rules.required">
            <Select v-model="form.ring" filterable>
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
              <Button class="margin-left-5" @click="listenRing">
                <i class="icon-n-zhanneixiaoxi" />
              </Button>
            </Tooltip>
          </FormItem>
          <FormItem label="响铃开关" prop="opened">
            <el-switch v-model="form.opened" active-color="#13ce66">
            </el-switch>
          </FormItem>
          <FormItem style="margin-bottom: 0px; text-align: right;">
            <Button @click="onSure">
              确定
            </Button>
            <Button @click="resetCancel" cancel>
              重置
            </Button>
          </FormItem>
        </Form>
      </TabPane>
    </Tabs>
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
      oldData: null,
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
        this.oldData = res.obj
        this.form.id = res.obj.id
        this.form.ring = res.obj.ring
        this.form.opened = res.obj.opened
      }
    })
    //
    this.axios
      .get('/msg/config/rings')
      .then(res => {
        if (res.success) {
          this.levels = res.obj || []
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
            this.mdpram.show = false
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
        this.mdpram.show = false
        this.$notify({
          message: '重置成功！',
          position: 'bottom-right',
          customClass: 'toast toast-success'
        })
      })
      if (this.oldData) {
        this.form.id = this.oldData.id
        this.form.ring = this.oldData.ring
        this.form.opened = this.oldData.opened
      }
    },
    onCancel () {
      this.mdpram.show = false
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
</style>
