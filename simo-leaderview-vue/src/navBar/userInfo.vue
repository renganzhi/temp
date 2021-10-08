<template>
  <Modal
    v-model="mdpram.isShow"
    title="用户信息"
    class="userInfoBox"
    :loading="true"
    :width="980"
    :on-ok="update"
    :on-cancel="cancel"
  >
    <Form
      ref="form"
      class=""
      :rules="fmRules"
      :model="formInfo"
      label-width="120"
    >
      <div class="form-title">
        个人信息
      </div>
      <div class="user-info flex-ve">
        <div class="form-percent2">
          <FormItem v-for="(v, k) in info" :key="k" :label="v">
            <Input
              disabled
              :value="
                (k === 'limitDate'
                  ? formatDateTime(formInfo[k])
                  : formInfo[k]) || '--'
              "
            />
          </FormItem>
          <FormItem prop="email" label="邮箱">
            <Input v-model="formInfo.email" clearable />
          </FormItem>
          <FormItem prop="phone" label="手机号">
            <Input v-model="formInfo.phone" clearable />
          </FormItem>
        </div>
        <FormItem label="照片" prop="headFile">
          <Upload
            :show-upload-list="false"
            action="//"
            accept=".jpg, .jpeg, .png, .bmp, .gif, .tif"
            :before-upload="handleUpload"
          >
            <div class="img-box">
              <div v-if="!imageUrl" class="flex-column ve-center">
                <span>+</span>
                <span>上传照片</span>
              </div>
              <div v-else>
                <img :src="imageUrl" />
                <div class="upload-cover ve-center">
                  <i class="icon-n-show" @click.stop="showPic" />
                  <i class="icon-n-imgUpload" />
                  <i class="icon-n-deleteNew" @click.stop="delPic" />
                </div>
              </div>
            </div>
          </Upload>
        </FormItem>
      </div>
      <div class="form-title">
        权限信息
      </div>
      <div class="form-percent3">
        <FormItem label="角色" prop="roleIds">
          <Input :value="formInfo.roleNames" disabled />
        </FormItem>
        <FormItem label="所属域" prop="departmentId">
          <Input :value="formInfo.departmentName || '--'" disabled />
        </FormItem>
      </div>
      <div class="form-title">
        机构信息
      </div>
      <div class="form-percent3">
        <FormItem label="职位" prop="sex">
          <Input :value="formInfo.jobPositionName || '--'" disabled />
        </FormItem>
        <FormItem label="所属机构/部门" prop="sex">
          <Input :value="formInfo.organizationName || '--'" disabled />
        </FormItem>
        <FormItem label="直属上级">
          <Input :value="formInfo.superiorName || '--'" disabled />
        </FormItem>
        <FormItem label="直属下级" class="percent1">
          <span class="label-tip">
            (共{{
              formInfo.subordinate ? formInfo.subordinate.split(' ').length : 0
            }}人)
          </span>
          <Input v-model="formInfo.subordinate" type="textarea" readonly />
        </FormItem>
      </div>
      <!-- <div class="userInfo">
        <label class="title-left-bg ivu-form-item"> 个人信息 </label>
        <Form ref="formInfo" :model="formInfo" :rules="fmRules">
          <FormItem v-for="(v, k) in info" :key="k" class="info" :label="v">
            {{
              (k === "limitDate" ? formatDateTime(formInfo[k]) : formInfo[k]) ||
                "--"
            }}
          </FormItem>
          <FormItem prop="email" label="邮箱">
            <Input v-model="formInfo.email" />
          </FormItem>
          <FormItem prop="phone" label="手机号">
            <Input v-model="formInfo.phone" />
          </FormItem>
          <FormItem label="照片" prop="headFile">
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
                <img v-if="imageUrl" :src="imageUrl" class="imgSet" />
                <div class="uploadCover">
                  <i class="icon-n-show" @click.stop="showPic" />
                  <i class="icon-n-imgUpload" />
                  <i class="icon-n-deleteNew" @click.stop="delPic" />
                </div>
              </div>
            </Upload>
          </FormItem>
        </Form>
      </div>
      <div class="userInfo">
        <label class="title-left-bg ivu-form-item"> 权限信息 </label>
        <FormItem label="角色" prop="roleIds">
          {{ formInfo.roleNames }}
        </FormItem>
        <FormItem label="所属域" prop="departmentId">
          {{ formInfo.departmentName || "--" }}
        </FormItem>
        <label class="title-left-bg"> 机构信息 </label>
        <FormItem label="职位" prop="sex">
          {{ formInfo.jobPositionName || "--" }}
        </FormItem>
        <FormItem label="所属机构/部门" prop="sex">
          {{ formInfo.organizationName || "--" }}
        </FormItem>
        <FormItem label="直属上级">
          {{ formInfo.superiorName || "--" }}
        </FormItem>
        <FormItem label="直属下级">
          <span class="label-tip">
            (共{{
              formInfo.subordinate ? formInfo.subordinate.split(" ").length : 0
            }}人)
          </span>
          <Input v-model="formInfo.subordinate" type="textarea" readonly />
        </FormItem>
        <div
          class="fr page__footer ivu-modal-footer"
          style="padding-bottom: 0px"
        >
          <Button @click="update"> 确认 </Button>
          <Button cancel @click="cancel"> 取消 </Button>
        </div>
      </div> -->

      <!-- <div class="fr page__footer ivu-modal-footer" style="padding-bottom: 0px">
        <Button @click="update"> 确认 </Button>
        <Button cancel @click="cancel"> 取消 </Button>
      </div> -->
    </Form>
    <div v-if="previewMd.isShow">
      <PreviewMd :mdpram="previewMd" />
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
        phone: [
          this.$rules.required,
          {
            ...this.$rules.telephone,
            message: '联系电话格式不正确，请重新输入'
          }
        ],
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
      }
      return new Date(data).format('yyyy-MM-dd hh:mm:ss')
    },
    handleUpload (file) {
      if (file.size / 1024 / 1024 > 3) {
        this.$Notice.info({
          desc: `文件大小最大为3M`
        })
        return
      }
      if (!this.accept.split(',').includes(file.type)) {
        this.$Notice.info({
          desc: '仅支持的格式有：png、jpeg、jpg、bmp、gif、tif'
        })
        return
      }
      const reader = new FileReader()
      reader.addEventListener('load', () => {
        this.imageUrl = reader.result
        this.headFile = file
      })
      reader.readAsDataURL(file)
      // return;
    },
    base64toFile (dataurl) {
      const arr = dataurl.split(',')
      const mime = arr[0].match(/:(.*?);/)[1]
      const bstr = atob(arr[1])
      let n = bstr.length
      const u8arr = new Uint8Array(n)
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
      this.fmRules.phone.push({
        ...this.chkExist('sysUserChkTel', {
          employeeId: this.formInfo.id,
          nameKey: 'telephone'
        }),
        message: '此电话号码已被注册'
      })
      this.fmRules.email.push({
        ...this.chkExist('sysUserChkEmail', {
          employeeId: this.formInfo.id,
          nameKey: 'email'
        }),
        message: '此邮箱已被注册'
      })
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
    getDomain () {
      this.$api.domainTree({ hasPermission: true, ...this.param }).then(res => {
        this.domainData = res.obj || []
      })
    },
    getSubordinate (id) {
      // 根据上级获取下级用户
      const arr = []
      this.axios
        .get(`/mc/user/getSubordinate?userIds=${id}&isAll=false`)
        .then(res => {
          res.obj &&
            res.obj.forEach((d, i) => {
              arr.push(d.userName)
            })
          this.$nextTick(() => {
            this.formInfo.subordinate = arr.join(' ')
          })
        })
    },
    getLoginUser () {
      this.axios.get('/mc/user/getLoginUserName').then(res => {
        res = res.obj || {}
        if (res.id) {
          this.getSubordinate(res.id)
        }
        const names = []
        res.roles.forEach(d => {
          names.push(d.name)
        })
        res.roleNames = names.join(',')
        res.subordinate = ''
        this.formInfo = res
        this.imageUrl = res.headFile
          ? 'data:image/png;base64,' + res.headFile
          : ''
        this.headFile = res.headFile
          ? this.base64toFile('data:image/png;base64,' + res.headFile)
          : ''
        this.addFmRules()
      })
    },
    update () {
      const params = {
        id: this.formInfo.id,
        phone: this.formInfo.phone,
        email: this.formInfo.email,
        headFile: this.headFile
      }
      const formData = new FormData()
      for (const key in params) {
        if (params[key]) {
          formData.append(key, params[key])
        }
      }
      this.$refs.formInfo.validate(valid => {
        if (valid) {
          this.axios
            .post(`/user/${this.formInfo.id}/updateUserInfo`, formData)
            .then(
              res => {
                this.$Message.success({
                  background: true,
                  content: '修改成功'
                })
              },
              errRes => {
                this.$Message.erro({
                  background: true,
                  content: '修改失败'
                })
              }
            )
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
.userInfo {
  width: 50%;
  height: 495px;
}
.title-left-bg {
  margin-bottom: 20px;
  &:before {
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

::v-deep .ivu-form-item-label {
  color: #828bac !important;
}

.user-info {
  & > div {
    flex: 2;
  }
  & > ::v-deep .ivu-form-item {
    flex: 1;
  }
}
::v-deep .ivu-input-type-textarea {
  width: calc(100% - 18px) !important;
  textarea {
    width: 750px !important;
    height: 100px;
  }
}
.label-tip {
  position: absolute;
  left: -45px;
  top: 20px;
}
.img-box {
  width: 100px;
  height: 100px;
  border: 1px solid;
  border-radius: 3px;
  cursor: pointer;
  border-color: #cacdd7;

  span {
    // color: rgba(0, 0, 0, 0.719) !important;
    color: #bfbfbf;
  }
  div {
    height: 100%;
    position: relative;
    display: flex;
    align-items: center;
    vertical-align: middle;
    justify-content: center;
    flex-wrap: nowrap;
    img {
      width: 100%;
      height: 100%;
    }
    .upload-cover {
      width: 100%;
      position: absolute;
      top: 0;
      background: rgba(0, 0, 0, 0.35);
      i {
        padding: 0 8px;
        color: #fff;
        font-size: 14px;
        &:nth-child(2) {
          font-size: 12px;
        }
      }
    }
  }
  span {
    &:first-child {
      font-size: 40px;
      font-weight: lighter;
      font-family: SourceHanSansCN-Regular;
    }
  }
}
.ivu-btn-primary {
  background: #5b8bff;
}
.ivu-input-wrapper {
  width: fit-content;
}
</style>
