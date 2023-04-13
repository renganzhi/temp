<template>
    <div class="StreetSafety">
        <div class="bigTitle">城运平安</div>
        <div class="bigBody">
            <div class="DateSwitch">
                <div class="normalBtn">昨日</div>
                <div class="normalBtn">今日</div>
            </div>
            <div class="StateSwitch">
                <div class="regularBtn">已报</div>
                <div class="regularBtn">未报</div>
                <div class="regularBtn">异常</div>
            </div>
            <div class="middleTitle">报送情况</div>
            <div class="middleBody">
                <div class="streetBox" v-for="(item1, index1) in 11" :key="index1">
                    <div class="streetTitle">
                        浆洗街街道
                        <img src="./切图/6.png" alt="">
                    </div>
                    <div class="communityList">
                        <div class="communityBox" v-for="(item2, index2) in 8" :key="index2">
                            xxx社区
                            <img src="./切图/4.png" alt="">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import EventVenation from '@/components/EditComp/EventVenation'
export default {
  name: 'StreetSafety',
  props: ['item'],
  components: {EventVenation},
  data () {
    return {
    }
  },
  computed: {

  },
  watch: {

  },
  methods: {

  },
  mounted () {
  }
}
</script>
<style scoped lang="scss">
.StreetSafety{
    width: 100%;
    height: 100%;
    background: url('./切图/1.png') no-repeat;
    background-size: 100% 100%;
    padding: 18px 80px 0px 80px;
    .bigTitle{
        background: linear-gradient(180deg,#ffffff 18%, #90deff 84%);
        font-size: 56px;
        font-family: Alibaba PuHuiTi 2.0;
        font-weight: 700;
        color: #3f3f3f;
        line-height: 78px;
        letter-spacing: 10px;
        text-shadow: 0px 2px 4px 0px rgba(10,30,52,0.48);
        margin-bottom: 80px;
        width: 100%;
        padding-left: 80px;
    }
    .bigBody{
        width: 100%;
    }

}

</style>
<style lang="scss">
.StreetSafety{
}
</style>
