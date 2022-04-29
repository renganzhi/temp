<template>
  <div class="WuhoMaoBL" :style="maoBlstyle" >
    <div class="ZFXMBL" :style="maoBlstyle" v-if="item.MapBLType">

    </div>
    <div class="ZHWHDZ" v-else>
      <div class="MBLZFX" :style="maoBlZFXstyle" v-if="item.MapBLFX"></div>
      <div class="MBLSANJIAO" :style="maoSanJiaoBlZFXstyle" >
          <div class="topSan">
            <div class="MBLZFX" v-for="(item,index) in 20" :key="index" :style="maoBlZFXTOPstyle(index) ">
            </div>
          </div>
          <div class="bottomSan">
            <div class="MBLZFX" v-for="(item,index) in 20" :key="index" :style="maoBlZFXBOTTOMstyle(index) ">
            </div>
          </div>
      </div>
      <div class="MBLZFX" :style="maoBlZFXstyle" v-if="!item.MapBLFX"></div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'WuhoMaoBL',
  props: ['item'],
  data () {
    return {

    // 'MapBLType': false,
    // 'MapBLFX': false,
    // 'MapBLTOP': false,
    }
  },
  computed: {
    maoBlstyle () {
      return {
        height: this.item.height + 'px !important',
        width: this.item.width + 'px !important'
      }
    },
    maoBlZFXstyle () {
      return {
        height: this.item.height + 'px !important',
        width: Math.floor(this.item.width * 0.9) + 'px !important'
      }
    },
    maoSanJiaoBlZFXstyle () {
      return {
        height: this.item.height + 'px !important',
        width: (this.item.width - Math.floor(this.item.width * 0.9)) + 'px !important'
      }
    }
  },
  methods: {
    maoBlZFXTOPstyle (index) {
      if (this.item.MapBLFX) {
        return {
          width: '5%',
          height: (20 - index) * 5 + '%',
          position: 'relative',
          top: index * -5 + '%'
        }
      } else {
        return {
          width: '5%',
          height: (index + 1) * 5 + '%',
          position: 'relative',
          top: (19 - index) * -5 + '%'
        }
      }
    },
    maoBlZFXBOTTOMstyle (index) {
      if (this.item.MapBLFX) {
        return {
          width: '5%',
          height: (20 - index) * 5 + '%',
          position: 'relative'
          // top: index * 10 + '%'
        }
      } else {
        return {
          width: '5%',
          height: (index + 1) * 5 + '%',
          position: 'relative'
          // top: index * 10 + '%'
        }
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.ZFXMBL{
  backdrop-filter: blur(10px);
  background-color: rgba(255, 255, 255, 0);
}
.ZHWHDZ{
  display: flex;
}
.topSan{
  width: 100%;
  height: 50%;
  position: relative;
}
.bottomSan{
  width: 100%;
  height: 50%;
  position: relative;
}
.MBLZFX{
  backdrop-filter: blur(10px);
  background-color: rgba(255, 255, 255, 0);
  display: inline-block;
}
.MBLSANJIAO{
  position: relative;
}
</style>
