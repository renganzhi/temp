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
import { sysUserPswConf } from "./passwordsMixs";

export default {
  name: "UserInfo",
  components: {},
  mixins: [sysUserPswConf],
  props: {
    mdpram: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      showMessage: false, // 处理一进入弹窗，addPswRules后检验信息就显示
      form: {
        password: "",
        newPassword: "",
        enPassword: ""
      },
      id: "",
      compareOld: true,
      rules: {
        password: [this.$rules.required],
        newPassword: [this.$rules.required],
        enPassword: [this.$rules.required]
      }
    };
  },
  created() {
    this.getLoginUser();
  },
  methods: {
    getLoginUser() {
      // /user/getLoginUserName
      this.axios.get(`/user/getLoginUserName`).then(res => {
        res = res.obj || {};
        this.id = res.id;
        this.addPswRules();
      });
    },
    addPswRules() {
      this.rules.password.push({
        ...this.chkExist("sysUserChkOldPsw", {
          userId: this.id,
          nameKey: "oldPassword"
        }),
        message: "旧密码错误，请重试"
      });
      this.addRules();

      this.$nextTick(() => {
        this.reset();
        this.showMessage = true;
      });
    },
    chkExist(url, pram, tip) {
      tip = "该" + (tip || "名称") + "已存在";
      return {
        trigger: "blur",
        asyncValidator: (rule, value, callback) => {
          if (value) {
            const myurl = "/user/unValidOldPassword";
            const config = {
              headers: {
                "Content-Type": "multipart/form-data"
              }
            };
            const formData = new FormData();
            formData.append("userId", pram.userId);
            formData.append("oldPassword", value);
            this.axios.post(myurl, formData, config).then(
              res => {
                if (res.obj === true) {
                  callback(new Error(tip));
                } else {
                  callback();
                }
              },
              errRes => {
                callback(new Error("名称校验请求失败"));
              }
            );
          } else {
            callback();
          }
        }
      };
    },
    sure() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.mdpram.isShow = false;
          this.$api
            .sysUserUpdatePsw(
              { newPassword: this.form.newPassword },
              { id: this.id }
            )
            .then(res => {
              this.$Message.success({
                background: true,
                content: res.msg + ",请重新登录！"
              });
            });
        }
      });
    },
    reset() {
      this.$refs.form.resetFields();
    }
  }
};
</script>
<style lang="scss" scoped></style>
