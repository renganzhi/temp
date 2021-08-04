
import { defaultPsw } from './sysConf'
// import { JSEncrypt } from 'jsencrypt/bin/jsencrypt.min.js'
// export const jsencrypt = {
//   methods: {
//     //  加密
//     encryptedData (data) {
//       if (data === null || data === 'undefined' || data === '') {
//         return ''
//       }
//       let encryptor = new JSEncrypt()
//       encryptor.setPublicKey('MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnCP4x5GfUeKEjCf7b5k8S7DPbc7db6YZbkXKRMBkbgF/VIjgdCv6rwnGhCHc/JwVXF8ui6ozyQq5AfJrjQynK/139hfyM+7ob3lVQz6dZiYI3BtTbQMSXD58IYVQ7SeijWmnGQ/EOP4R4FeLEsxAmoLw/xoNeErfNplmM8nxg+wIDAQAB')
//       return encryptor.encrypt(data)
//     },
//     // 解密
//     decryptData (privateKey, data) {
//       let decrypt = new JSEncrypt()
//       decrypt.setPrivateKey(privateKey)
//       decrypt.decrypt(data)
//     }
//   }
// }

export const sysUserPswConf = {
  data () {
    return {
      form: {
        newPassword: '',
        enPassword: ''
      },
      rules: {},
      pswHolder: '必须包含大写字母、小写字母、数字'
    }
  },
  methods: {
    addRules () {
      const validatePass = (rule, value, callback) => {
        if (this.form.enPassword !== '') {
          this.$refs.form.validateField('enPassword')
        }
        if (this.compareOld) {
          if (value === this.form.password) {
            callback(new Error('新旧密码不能相同'))
          } else {
            callback()
          }
        }
        callback()
      }
      const validatePassCheck = (rule, value, callback) => {
        // 直接使用value会去掉末尾的空格在进行校验 会存在密码有空格时校验失败
        // 按理来说密码是不应该有空格的 但是和产品沟通 保持原系统 允许空格
        if (this.form.enPassword !== this.form.newPassword) {
          callback(new Error('两次输入的密码不一致，请重试'))
        } else {
          callback()
        }
      }

      this.rules = {
        ...this.rules,
        newPassword: [this.$rules.required, { validator: validatePass, trigger: 'blur,change' }],
        enPassword: [this.$rules.required, { validator: validatePassCheck, trigger: 'blur,change' }]
      }
      this.getPswConf()
    },
    getPswConf () { // 获取用户密码设置
      // /system/getSystemPwdConf
      this.axios.get('/system/getSystemPwdConf').then((res) => {
      // this.$api.sysConfPsw().then(res => {
        res.obj = res.obj || defaultPsw
        let pswConf = res.obj.formation
        let len = res.obj.length
        this.dealPswHolder(pswConf)
        this.rules.newPassword.push({ ...this.$rules.range, min: len.minLength, max: len.maxLength, type: 'string' })
        this.rules.newPassword.push({ ...this.$rules.passwordCheckByConf(pswConf) })
      })
    },
    dealPswHolder (pswConf) {
      let placeholder = '必须包含'
      let regObj = {
        upperCase: '大写字母',
        lowerCase: '小写字母',
        pwdNumber: '数字'
      }
      for (let key in pswConf) {
        if (pswConf[key]) {
          placeholder += regObj[key] + '、'
        }
      }
      this.pswHolder = placeholder.substr(0, placeholder.length - 1)
    }
  }
}
