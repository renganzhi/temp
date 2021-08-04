<template>
  <div class="msgPop">
    <div class="title us-border-bottom">
      <a
        class="us-link"
        @click="checkAll"
      >
        查看全部
      </a>
      <i
        class="us-link"
        :class="openVoice ? 'icon-n-laba' :'icon-n-mute'"
        @click="clickOpenVoice"
      />
    </div>
    <ul class="msgInfo">
      <div
        v-if="!hasMsg"
        class="noMsg"
      >
        暂时没有新的消息
      </div>
      <template v-else>
        <div
          v-if="!latestMsg.length"
          class="noMsg"
        >
          正在加载中...
        </div>
        <MsgLi
          v-for="item in latestMsg"
          v-else
          :key="item.id"
          :pram="{data:item,levelMap,originArr}"
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
  data () {
    return {
      levelMap: {},
      latestMsg: [],
      hasMsg: true,
      originArr: []
    }
  },
  computed: {
    ...mapState('base', {
      openVoice: state => state.notice.openVoice
    })
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
    this.axios.get('/msg/mailbox/list?currentNo=1&pageSize=5&isRead=false').then((res) => {
      this.latestMsg = res.obj.object || []
      if (!this.latestMsg.length) this.hasMsg = false
    })
    // this.$api.msgList({ currentNo: 1, pageSize: 5, isRead: false }).then(res => {
    //   this.latestMsg = res.obj.object || []
    //   if (!this.latestMsg.length) this.hasMsg = false
    // })
  },
  methods: {
    checkAll () {
      this.$emit('show-all-msg')
    },
    clickOpenVoice () {
      // $store.commit('base/setVoice', !openVoice)
    }
  }
}
</script>

<style lang="scss" scoped>
.msgPop{
  position: absolute;
  width: 325px;
  right: 0;
  .title{
    padding: 15px 5px;
    line-height: 22px;
    margin: 0 10px;
    i{
      float: right;
      font-size: 20px;
    }
  }
  .msgInfo{
    .noMsg{
      height: 120px;
      line-height: 120px;
      text-align: center;
    }
  }
}
</style>
