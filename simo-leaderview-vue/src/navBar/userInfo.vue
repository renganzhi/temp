<template>
  <Modal
    v-model="mdpram.isShow"
    title="用户信息"
    :loading="true"
    :width="980"
    footer-hide
  >
    <Form
      ref="form"
      class="flex-ve"
    >
      <div class="userInfo">
        <label class="title-left-bg ivu-form-item">
          个人信息
        </label>
        <Form
          ref="formInfo"
          :model="formInfo"
          :rules="fmRules"
        >
          <FormItem
            v-for="(v,k) in info"
            :key="k"
            class="info"
            :label="v"
          >
            {{ (k === 'limitDate' ? formatDateTime(formInfo[k]) : formInfo[k]) || '--' }}
          </FormItem>
          <FormItem
            prop="email"
            label="邮箱"
          >
            <Input v-model="formInfo.email" />
          </FormItem>
          <FormItem
            prop="phone"
            label="手机号"
          >
            <Input v-model="formInfo.phone" />
          </FormItem>
          <FormItem
            label="照片"
            prop="headFile"
          >
            <Upload
              :show-upload-list="false"
              action="//"
              accept=".jpg, .jpeg, .png, .bmp, .gif, .tif"
              :before-upload="handleUpload"
            >
              <div class="imgBox">
                <div class="imgIcon">
                  <i class="icon-add" />
                </div>
                <div class="imgFont">
                  <span>上传照片</span>
                </div>
              </div>
              <div class="showImg">
                <img
                  v-if="imageUrl"
                  :src="imageUrl"
                  class="imgSet"
                >
                <div class="uploadCover">
                  <i
                    class="icon-n-show"
                    @click.stop="showPic"
                  />
                  <i
                    class="icon-n-imgUpload"
                  />
                  <i
                    class="icon-n-deleteNew"
                    @click.stop="delPic"
                  />
                </div>
              </div>
            </Upload>
          </FormItem>
        </form>
      </div>
      <div class="userInfo">
        <label class="title-left-bg ivu-form-item">
          权限信息
        </label>
        <FormItem
          label="角色"
          prop="roleIds"
        >
          {{ formInfo.roleNames }}
        </FormItem>
        <FormItem
          label="所属域"
          prop="departmentId"
        >
          {{ formInfo.departmentName || '--' }}
        </FormItem>
        <label class="title-left-bg">
          机构信息
        </label>
        <FormItem
          label="职位"
          prop="sex"
        >
          {{ formInfo.jobPositionName || '--' }}
        </FormItem>
        <FormItem
          label="所属机构/部门"
          prop="sex"
        >
          {{ formInfo.organizationName || '--' }}
        </FormItem>
        <FormItem
          label="直属上级"
        >
          {{ formInfo.superiorName || '--' }}
        </FormItem>
        <FormItem
          label="直属下级"
        >
          <span class="label-tip">
            (共{{ formInfo.subordinate? formInfo.subordinate.split(' ').length : 0 }}人)
          </span>
          <Input
            v-model="formInfo.subordinate"
            type="textarea"
            readonly
          />
        </FormItem>
        <div
          class="fr page__footer ivu-modal-footer"
          style="padding-bottom: 0px"
        >
          <Button
            @click="update"
          >
            确认
          </Button>
          <Button
            cancel
            @click="cancel"
          >
            取消
          </Button>
        </div>
      </div>
    </Form>
    <div v-if="previewMd.isShow">
      <PreviewMd
        :mdpram="previewMd"
      />
    </div>
  </Modal>
</template>

<script>
export default {
  name: 'UserInfo',
  components: {
    PreviewMd: () => import('./preview')
  },
  props: {
    mdpram: {
      type: Object,
      default: () => {}
    }
  },
  data () {
    return {
      info: {
        userName: '姓名',
        employeeCode: '工号',
        loginName: '用户账号',
        limitDate: '过期时间'
      },
      formInfo: {
        id: '',
        email: '',
        phone: '',
        roleNames: '',
        departmentName: '',
        jobPositionName: '',
        organizationName: '',
        superiorName: '',
        subordinate: ''
      },
      imageUrl: '',
      headFile: '',
      roleNames: '',
      fmRules: {
        phone: [this.$rules.required, { ...this.$rules.telephone, message: '联系电话格式不正确，请重新输入' }],
        email: [this.$rules.required, this.$rules.email]
      },
      accept: 'image/png,image/jpeg,image/jpg,image/bmp,image/gif,image/tiff',
      domainData: [],
      previewMd: {
        isShow: false
      }
    }
  },
  created () {
    this.getLoginUser()
  },
  methods: {
    formatDateTime (data) {
      if (!data || data === undefined || data === 'null' || data === 'NAN') {
        return ''
      } else {
        return new Date(data).format('yyyy-MM-dd hh:mm:ss')
      }
    },
    handleUpload (file) {
      if ((file.size / 1024 / 1024) > 3) {
        this.$Notice.info({
          desc: `文件大小最大为3M`
        })
        return
      }
      if (!this.accept.split(',').includes(file.type)) {
        this.$Notice.info({
          desc: '仅支持的格式有：png、jpeg、jpg、bmp、gif、tif'
        })
        return false
      }
      const reader = new FileReader()
      reader.addEventListener('load', () => {
        this.imageUrl = reader.result
        this.headFile = file
      })
      reader.readAsDataURL(file)
      return false
    },
    base64toFile (dataurl) {
      let arr = dataurl.split(',')
      let mime = arr[0].match(/:(.*?);/)[1]
      let bstr = atob(arr[1])
      let n = bstr.length
      let u8arr = new Uint8Array(n)
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n)
      }
      return new File([u8arr], '1.png', { type: mime })
    },
    showPic () {
      this.previewMd = {
        isShow: true,
        picUrl: this.imageUrl
      }
    },
    delPic () {
      this.$ensureModal.confirm('确定删除？', () => {
        this.imageUrl = ''
      })
    },
    addFmRules () {
      this.fmRules.phone.push({ ...this.chkExist('sysUserChkTel', { employeeId: this.formInfo.id, nameKey: 'telephone' }), message: '此电话号码已被注册' })
      this.fmRules.email.push({ ...this.chkExist('sysUserChkEmail', { employeeId: this.formInfo.id, nameKey: 'email' }), message: '此邮箱已被注册' })
    },
    chkExist (url, pram, tip) {
      tip = '该' + (tip || '名称') + '已存在'
      return {
        trigger: 'blur',
        asyncValidator: (rule, value, callback) => {
          if (value) {
            let myurl = '/user/existedPhone'
            if (url === 'sysUserChkEmail') {
              myurl = '/user/existedEmail'
            }
            const config = {
              headers: {
                'Content-Type': 'multipart/form-data'
              }
            }
            const formData = new FormData()
            formData.append('employeeId', pram.employeeId)
            formData.append(pram.nameKey, value)
            this.axios.post(myurl, formData, config).then(res => {
              if (res.obj === true) {
                callback(new Error(tip))
              } else {
                callback()
              }
            }, errRes => {
              callback(new Error('名称校验请求失败'))
            })
          } else {
            callback()
          }
        }
      }
    },
    getDomain () {
      this.$api.domainTree({ hasPermission: true, ...this.param }).then(res => {
        this.domainData = res.obj || []
      })
    },
    getSubordinate (id) { // 根据上级获取下级用户
      let arr = []
      this.axios.get(`/mc/user/getSubordinate?userIds=${id}&isAll=false`).then((res) => {
        res.obj && res.obj.forEach((d, i) => {
          arr.push(d.userName)
        })
        this.$nextTick(() => {
          this.formInfo.subordinate = arr.join(' ')
        })
      })
    },
    getLoginUser () {
      this.axios.get('/mc/user/getLoginUserName').then((res) => {
        res = res.obj || {}
        if (res.id) {
          this.getSubordinate(res.id)
        }
        let names = []
        res.roles.forEach(d => {
          names.push(d.name)
        })
        res.roleNames = names.join(',')
        res.subordinate = ''
        this.formInfo = res
        this.imageUrl = res.headFile ? 'data:image/png;base64,' + res.headFile : ''
        this.headFile = res.headFile ? this.base64toFile('data:image/png;base64,' + res.headFile) : ''
        this.addFmRules()
      })
    },
    update () {
      let params = {
        id: this.formInfo.id,
        phone: this.formInfo.phone,
        email: this.formInfo.email,
        headFile: this.headFile
      }
      let formData = new FormData()
      for (let key in params) {
        if (params[key]) {
          formData.append(key, params[key])
        }
      }
      this.$refs.formInfo.validate((valid) => {
        if (valid) {
          this.axios.post(`/user/${this.formInfo.id}/updateUserInfo`, formData).then(res => {
            this.$notify({
              message: '修改成功！',
              position: 'bottom-right',
              customClass: 'toast toast-success'
            })
          }, errRes => {
            this.$notify({
              message: '修改失败！',
              position: 'bottom-right',
              customClass: 'toast toast-error'
            })
          })
        }
      })
    },
    cancel () {
      this.mdpram.isShow = false
    }
  }
}
</script>
<style lang="scss" scoped>
@import './sass/user.scss';
.userInfo{
  width:50%;
  height: 495px;
}
.title-left-bg{
  margin-bottom: 20px;
  &:before{
    margin-top: 3px !important;
  }
}
// .update{
//   margin-left: 754px;
//   margin-top: 34px;
//   color:#fff !important;
//   @include getTheme($themes){
//     background-color: themed('theme-color') !important;
//   }
// }
hr {
  background-color: #737070;
  height: 1px;
  border: none;
}

::v-deep .ivu-form-item-label{
  color: #828bac !important;
}
</style>
