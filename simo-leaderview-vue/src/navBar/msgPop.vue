<template>
  <div class="msgPop">
    <div class="title us-border-bottom">
      <span class="us-link" @click="checkAll"> 查看全部 </span>
      <i
        class="us-link"
        :class="form.open ? 'icon-n-laba' : 'icon-n-mute'"
        @click.stop="clickOpenVoice"
      />
    </div>
    <ul class="msgInfo">
      <div v-if="!hasMsg" class="noMsg">
        <img src="./images/no_data.png" alt="" />
        <p style="color: rgb(191, 191, 191);margin-top: 16px;">暂无数据</p>
      </div>
      <template v-else>
        <div v-if="!latestMsg.length" class="noMsg">正在加载中...</div>
        <MsgLi
          v-for="item in latestMsg"
          v-else
          :key="item.id"
          @openDetalBox="openDetalBox"
          :pram="{ data: item, levelMap, originArr }"
          :urlArry="urlArry"
          v-on="$listeners"
        />
      </template>
    </ul>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  components: {
    MsgLi: () => import('./msgLi')
  },
  props: {
    urlArry: {
      type: Object,
      default: () => {}
    }
  },
  data () {
    return {
      levelMap: {},
      latestMsg: [],
      form:{
        id:'',
        ring:'',
        open: false,
      },
      hasMsg: true,
      originArr: []
    }
  },

  created () {
    // 获取告警等级
    // this.$api.atEnum().then(res => {
    //   let levels = res.obj.level || []
    //   levels.length && levels.forEach(d => {
    //     this.levelMap[d.id] = d
    //   })
    //   this.originArr = res.obj.origin || []
    // })
    // 获取最新5条消息
    
    this.axios.get('/msg/config/findRingConfig').then(res => {
      if (res.success) {
        if (res.obj) {
          this.form.id = res.obj.id || ''
          this.form.ring = res.obj.ring || ''
          this.form.open = res.obj.open
        }
      }
    })
    this.axios
      .get('/msg/mailbox/list?currentNo=1&pageSize=5&isRead=false')
      .then(res => {
        this.latestMsg = res.obj.object || []
        if (!this.latestMsg.length) this.hasMsg = false
      })
    // this.$api.msgList({ currentNo: 1, pageSize: 5, isRead: false }).then(res => {
    //   this.latestMsg = res.obj.object || []
    //   if (!this.latestMsg.length) this.hasMsg = false
    // })
  },
  methods: {
    openDetalBox (data) {
      this.$emit('opencontent', data)
    },
    checkAll () {
      // this.$emit("show-all-msg");
      if (this.urlArry.msg) {
        window.location = `${window.location.origin + this.urlArry.msg}`
      }
    },
    clickOpenVoice () {
      this.form.open = !this.form.open
      let myurl = '/msg/config/saveRingConfig'
      const config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      const formData = new FormData()
      formData.append('id', this.form.id)
      formData.append('ring', this.form.ring)
      formData.append('opened', this.form.open)
      this.axios.post(myurl, formData, config).then(res => {
        
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.msgPop {
  // position: absolute;
  // width: 325px;
  // right: 0;
  position: fixed;
  top: 50px;
  right: 0;
  width: 400px;
  padding: 0 14px;
  border: 1px solid;
  z-index: 5;
  border-radius: 3px;
  .title {
    padding: 15px 0px;
    line-height: 22px;
    margin: 0 5px;
    width: calc(100% - 20px);
    text-align: left;
    i {
      float: right;
      font-size: 20px;
    }
  }
  .msgInfo {
    overflow: auto;
    .noMsg {
      height: 206px;
      text-align: center;
    }
  }
}
</style>
