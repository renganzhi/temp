<template>
  <Modal
    class="md-sm"
    v-model="mdpram.isShow"
    title="修改密码"
    :loading="true"
    @on-ok="sure"
  >
    <Form ref="form" :model="form" :rules="rules" :show-message="showMessage">
      <FormItem label="旧密码" prop="password">
        <Input
          v-model="form.password"
          style="width:270px"
          type="password"
          placeholder="请输入原登录密码"
        />
      </FormItem>
      <FormItem label="新密码" prop="newPassword">
        <Input
          v-model="form.newPassword"
          style="width:270px"
          type="password"
          :placeholder="pswHolder"
        />
      </FormItem>
      <FormItem label="确认密码" prop="enPassword">
        <Input
          v-model="form.enPassword"
          style="width:270px"
          type="password"
          placeholder="请再次输入新密码"
        />
      </FormItem>
    </Form>
  </Modal>
</template>

<script>
import { JSEncrypt } from 'jsencrypt/bin/jsencrypt.min.js'
import { sysUserPswConf } from './passwordsMixs'

export default {
  name: 'UserInfo',
  components: {},
  mixins: [sysUserPswConf],
  props: {
    mdpram: {
      type: Object,
      default: () => {}
    }
  },
  data () {
    return {
      showMessage: false, // 处理一进入弹窗，addPswRules后检验信息就显示
      form: {
        password: '',
        newPassword: '',
        enPassword: ''
      },
      id: '',
      compareOld: true,
      rules: {
        password: [this.$rules.required],
        newPassword: [this.$rules.required],
        enPassword: [this.$rules.required]
      }
    }
  },
  created () {
    this.getLoginUser()
  },
  methods: {
    getLoginUser () {
      // /user/getLoginUserName
      this.axios.get(`/user/getLoginUserName`).then(res => {
        res = res.obj || {}
        this.id = res.id
        this.addPswRules()
      })
    },
    addPswRules () {
      this.rules.password.push({
        ...this.chkExist('sysUserChkOldPsw', {
          userId: this.id,
          nameKey: 'oldPassword'
        }),
        message: '旧密码错误，请重试'
      })
      this.addRules()

      this.$nextTick(() => {
        this.reset()
        this.showMessage = true
      })
    },
    chkExist (url, pram, tip) {
      tip = '该' + (tip || '名称') + '已存在'
      return {
        trigger: 'blur',
        asyncValidator: (rule, value, callback) => {
          if (value) {
            const myurl = '/user/unValidOldPassword'
            const config = {
              headers: {
                'Content-Type': 'multipart/form-data'
              }
            }
            const formData = new FormData()
            formData.append('userId', pram.userId)
            formData.append('oldPassword', this.encryptedData(value))
            this.axios.post(myurl, formData, config).then(
              res => {
                if (res.obj === true) {
                  callback(new Error(tip))
                } else {
                  callback()
                }
              },
              errRes => {
                callback(new Error('名称校验请求失败'))
              }
            )
          } else {
            callback()
          }
        }
      }
    },
    encryptedData (data) {
      if (data === null || data === 'undefined' || data === '') {
        return ''
      }
      let encryptor = new JSEncrypt()
      encryptor.setPublicKey('MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnCP4x5GfUeKEjCf7b5k8S7DPbc7db6YZbkXKRMBkbgF/VIjgdCv6rwnGhCHc/JwVXF8ui6ozyQq5AfJrjQynK/139hfyM+7ob3lVQz6dZiYI3BtTbQMSXD58IYVQ7SeijWmnGQ/EOP4R4FeLEsxAmoLw/xoNeErfNplmM8nxg+wIDAQAB')
      return encryptor.encrypt(data)
    },
    sure () {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.mdpram.isShow = false
          let myurl = `/mc/user/${this.id}/pwd/update`
          const config = {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          }
          const formData = new FormData()
          formData.append('newPassword', this.encryptedData(this.form.newPassword))
          this.axios.post(myurl, formData, config).then(res=>{
            this.$Message.success({
              background: true,
              content: res.msg + ',请重新登录！'
            })
            if (process.env.NODE_ENV !== "development") {
              window.location.href = window.location.origin + '/loginPage'
            }else{
              window.location.href = window.location.origin + '/#/login'
            }
          })
        }
      })
    },
    reset () {
      this.$refs.form.resetFields()
    }
  }
}
</script>
<style lang="scss" scoped>
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
