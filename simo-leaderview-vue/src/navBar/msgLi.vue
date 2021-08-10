<template>
  <li class="msgLi us-border-bottom">
    <div
      class="content"
      @click="$emit('open-content',pram.data)"
    >
      <span
        class="msgStatus"
        :style="{'color': extParam.levelColor}"
      >
        <i class="simo icon-n-fail" />
        {{ extParam.levelName }}
      </span>
      <p
        ref="msgContent"
        class="overflow"
        :class="isOverFlow ? 'overflow' : ''"
      >
        {{ pram.data.content }}
        <template v-if="pram.data.extParam && pram.data.msgFrom === 'IT基础监控-告警管理'">
          <b
            v-if="isOverFlow"
            class="moreOver"
          >...<span
            class="us-link"
            @click.stop="lookDetail"
          >查看详情</span></b>
          <span
            v-else
            class="us-link"
            @click.stop="lookDetail"
          >查看详情</span>
        </template>
      </p>
    </div>
    <div class="msgSource">
      <span>{{ pram.data.msgFrom }}</span>
      <span class="msgTime">{{ pram.data.notifyTimeStr }}</span>
    </div>
  </li>
</template>

<script>
export default {
  props: {
    pram: {
      type: Object,
      default: () => {}
    }
  },
  data () {
    return {
      isOverFlow: false,
      showContent: false
    }
  },
  computed: {
    // level () {
    //   return JSON.parse(this.pram.data.extParam).level
    // },
    extParam () {
      let extParam = this.pram.data.extParam
      return extParam ? JSON.parse(extParam) : {}
    }
  },
  mounted () {
    this.$nextTick(() => {
      if (this.$refs.msgContent.clientHeight > 34) {
        this.isOverFlow = true
      }
    })
  },
  methods: {
    // 消息-查看详情
    lookDetail () {
      window.location = `${window.location.origin}/index.html?msgId=${this.pram.data.id}#/msg/unread`
      // this.$api.msgTagRead({ ids: this.pram.data.id })
      // this.$emit('close-pop')
      // this.$store.commit('base/setCurrentMenu', { id: 'MSG05', parentId: 'MSG02', typeId: 'MSG01' })
      // let extParam = JSON.parse(this.pram.data.extParam)
      // this.$router.push({ name: 'altContent',
      //   params: {
      //     row: extParam,
      //     // alertId: extParam.alertId,
      //     type: extParam.alertType,
      //     alertType: extParam.alertType,
      //     // originArr: this.pram.originArr,
      //     // color: this.pram.levelMap[extParam.level].color,
      //     paths: [{ title: '未读消息', name: 'msgReadNew' }, { title: '告警内容', name: 'altContent' }]
      //   },
      //   query: {
      //     t: Date.now() // 解决-已经在告警内容页面-跳转相同路由参数不更新问题
      //   }
      // })
    }
  }
}
</script>

<style lang="scss" scoped>
.msgLi{
  position: relative;
  margin: 10px 10px 0 10px;
  &:last-child{
    border: none;
  }
  .content{
    display: flex;
    .msgStatus{
      padding-right: 10px;
      i{
        font-size: 12px;
      }
    }
    p{
      flex: 1;
      span{
        cursor: pointer;
      }
      .moreOver{
        position: absolute;
        top:17px;
        right: 0;
        font-weight: normal;
        // @include getTheme($themes){
        //   background-color: themed('navbarWrap-dropdownMenuWrap-background-color');
        // }
        width: 60px;
        text-align: right;
      }
      .us-link{
        margin-right: 0;
      }
    }
    .overflow{

      overflow: hidden;
      // @include text-ellipsis('',2);
    }
  }
  .msgSource{
    margin:20px 0 10px 0;
    color: #666f8b;
    // color:$color-5;
    .msgTime{
      float: right;
    }
  }
}

</style>
