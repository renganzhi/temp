<template>
  <div class="WuhoTbale">
    <div class="TableHead" :style="headStyle">
      <tr :style="headStyle">
        <th v-for="(data, index) in item.chartData.columns" :key="index">
          {{ data }}
        </th>
      </tr>
    </div>
    <div class="TableBody"  :style="bodyAllStyle" ref="MyLunBoTbale">
      <tr :style="bodyStyle" v-for="(rowsData, i) in item.chartData.rows" :key="i">
        <th v-for="(data, index) in item.chartData.columns" :key="index"
          :style="{
            color: rowsData[data] && rowsData[data].color? rowsData[data].color:'#5983b6'
          }">
          <div v-if="data === '操作'">
            <div class="btnBox" @click="showRZ(rowsData)">入住人信息</div>
            <div class="btnBox" @click="showLD(rowsData)">联动处置情况</div>
          </div>
          {{ data === "操作" ? "" : rowsData[data].value?rowsData[data].value:rowsData[data] }}
        </th>
      </tr>
    </div>
    <Modal
      title=""
      v-model="modal9"
      :footer-hide="true"
      :mask-closable="false"
      class="RZWHtable"
    >
      <div class="ModelBox">
        <div class="BoxTitle">入住人信息</div>
        <div class="BoxBody">
          <div class="lineBox userName">
            <div class="Nmae">姓名</div>
            <div class="Data">{{ nowShowData["姓名"] }}</div>
          </div>
          <div class="lineBox userID">
            <div class="Nmae">身份证号码</div>
            <div class="Data">{{ nowShowData["身份证号码"] }}</div>
          </div>
          <div class="lineBox userPhone">
            <div class="Nmae">手机号码</div>
            <div class="Data">{{ nowShowData["手机号码"] }}</div>
          </div>
          <div class="lineBox userData">
            <div class="Nmae">登记时间</div>
            <div class="Data">{{ nowShowData["登记时间"] }}</div>
          </div>
          <div class="lineBox userInData">
            <div class="Nmae">入住时间</div>
            <div class="Data">{{ nowShowData["入住时间"] }}</div>
          </div>
          <div class="lineBox userLoca">
            <div class="Nmae">入住地址</div>
            <div class="Data">{{ nowShowData["登记入住旅馆"] }}</div>
          </div>
        </div>
      </div>
    </Modal>
    <Modal
      title=""
      v-model="modal10"
      :footer-hide="true"
      :mask-closable="false"
      class="LDWHtable"
    >
      <div class="ModelBox">
        <div class="BoxTitle">联动情况</div>
        <div class="BoxBody">
          <div class="OneArry">
            <div class="ArryTitle">武侯区平台</div>
            <div class="ArryBody">
              <div class="lineBox">
                <div class="Nmae">预警情况:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][0]['预警情况'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">环节:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][0]['环节'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">值班电话:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][0]['值班电话'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">指挥调度情况:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][0]['指挥调度情况'] }}</div>
              </div>
            </div>
          </div>
          <div class="OneArry">
            <div class="ArryTitle">民警</div>
            <div class="ArryBody">
              <div class="lineBox">
                <div class="Nmae">预警情况:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][1]['预警情况'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">环节:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][1]['环节'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">值班电话:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][1]['值班电话'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">指挥调度情况:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][1]['指挥调度情况'] }}</div>
              </div>
            </div>
          </div>
          <div class="OneArry">
            <div class="ArryTitle">街道平台</div>
            <div class="ArryBody">
              <div class="lineBox">
                <div class="Nmae">预警情况:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][2]['预警情况'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">环节:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][2]['环节'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">值班电话:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][2]['值班电话'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">指挥调度情况:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][2]['指挥调度情况'] }}</div>
              </div>
            </div>
          </div>
          <div class="OneArry">
            <div class="ArryTitle">网络</div>
            <div class="ArryBody">
              <div class="lineBox">
                <div class="Nmae">预警情况:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][3]['预警情况'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">环节:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][3]['环节'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">值班电话:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][3]['值班电话'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">指挥调度情况:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][3]['指挥调度情况'] }}</div>
              </div>
            </div>
          </div>
          <div class="OneArry">
            <div class="ArryTitle">公安平台</div>
            <div class="ArryBody">
              <div class="lineBox">
                <div class="Nmae">预警情况:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][4]['预警情况'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">环节:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][4]['环节'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">值班电话:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][4]['值班电话'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">指挥调度情况:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][4]['指挥调度情况'] }}</div>
              </div>
            </div>
          </div>

          <div class="OneArry">
            <div class="ArryTitle">单位</div>
            <div class="ArryBody">
              <div class="lineBox">
                <div class="Nmae">预警情况:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][5]['预警情况'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">环节:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][5]['环节'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">值班电话:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][5]['值班电话'] }}</div>
              </div>
              <div class="lineBox">
                <div class="Nmae">指挥调度情况:</div>
                <div class="Data">{{ nowShowData["联动情况"] && nowShowData["联动情况"][5]['指挥调度情况'] }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </Modal>
  </div>
</template>
<script>
export default {
  name: 'WuhoTbale',
  props: ['item'],
  data () {
    return {
      modal9: false,
      modal10: false,
      nowShowData: this.item.chartData.rows[0],
      iterview: null
    }
  },
  computed: {
    headStyle () {
      return {
        height: this.item.headHeight + 'px !important'
      }
    },
    bodyStyle () {
      return {
        height: this.item.boduHeight + 'px !important'
      }
    },
    bodyAllStyle () {
      return {
        height: `calc(100% - ${this.item.headHeight}px)` + '!important',
        overflow: 'auto'
      }
    }
  },
  mounted () {
    this.iterview = setInterval(() => {
      if (this.$refs.MyLunBoTbale.scrollTop + this.$refs.MyLunBoTbale.clientHeight === this.$refs.MyLunBoTbale.scrollHeight) {
        this.$refs.MyLunBoTbale.scrollTop = 0
      } else {
        this.$refs.MyLunBoTbale.scrollTop = this.$refs.MyLunBoTbale.scrollTop + 10
      }
    }, 500)
  },
  methods: {
    showRZ (rowsData) {
      this.modal9 = true
      this.nowShowData = rowsData
    },
    showLD (rowsData) {
      this.modal10 = true
      this.nowShowData = rowsData
    }
  },
  beforeDestroy () {
    if (this.iterview) {
      clearInterval(this.iterview)
    }
  }
}
</script>
<style scoped lang="scss">
.WuhoTbale {
  width: 100%;
  height: 100%;
  padding: 5px;
  font-size: 26px;
  .TableHead {
    width: 100%;
    tr {
      width: 100%;
      height: 80px;
      display: flex;
      background: url(./head.png);
      color: #94cffa;
      background-size: 100% 100%;
      th {
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
      }
      th:nth-child(1) {
        width: 15%;
      }
      th:nth-child(2) {
        width: 15%;
      }
      th:nth-child(3) {
        width: 35%;
        padding-right: 3%;
        padding-left: 2%;
      }
      th:nth-child(4) {
        width: 20%;
      }
      th:nth-child(5) {
        width: 20%;
      }
    }
  }
  .TableBody {
    width: 100%;
    tr {
      width: 100%;
      height: 120px;
      margin: 10px 0;
      background: url(./liBJ.png);
      background-size: 100% 100%;
      display: flex;
      color: #5983b6;
      th {
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
      }
      th:nth-child(1) {
        width: 15%;
      }
      th:nth-child(2) {
        width: 15%;
      }
      th:nth-child(3) {
        width: 35%;
        padding-right: 3%;
        padding-left: 2%;
      }
      th:nth-child(4) {
        width: 20%;
      }
      th:nth-child(5) {
        width: 20%;
      }
    }
  }
  .btnBox {
    background: url(./button.png);
    background-size: 100% 100%;
    padding: 6px 10px;
    margin: 16px 0;
    cursor: pointer;
    font-size: 18px;
    text-align: center;
  }
}
.RZWHtable {
  .ModelBox {
    height: 886px;
    width: 1747px;
    padding: 100px;
    background: url(./modelBox.png);
    .BoxTitle {
      font-size: 46px !important;
      color: #bbeefe;
      font-family: PangmenMainRoadTitleBody !important;
    }
    .BoxBody {
      padding: 80px 40px;
      display: flex;
      font-size: 24px !important;
      flex-wrap: wrap;
    }
    .userName {
      width: 25%;
    }
    .userID {
      width: 35%;
    }
    .userPhone {
      width: 25%;
    }
    .userData {
      width: 100%;
    }
    .userInData {
      width: 100%;
    }
    .userLoca {
      width: 100%;
    }
    .lineBox {
      display: flex;
      padding: 30px 0px;
    }
    .Nmae {
      padding: 0px 10px;
      color: #415468;
    }
    .Data {
      color: #789fb0;
    }
  }
}
.LDWHtable {
    .ArryTitle{
        padding-left: 60px;
        color: #00cce6;
        font-weight: 600;
    }
  .ModelBox {
    height: 886px;
    width: 1747px;
    padding: 100px;
    background: url(./modelBox.png);
    .BoxTitle {
      font-size: 46px !important;
      color: #bbeefe;
      font-family: PangmenMainRoadTitleBody !important;
    }
    .BoxBody {
      padding: 10px 10px;
      display: flex;
      font-size: 24px !important;
      flex-wrap: wrap;
    }
    .OneArry {
      width: 50%;
      padding: 20px 10px;
    }
    .ArryBody {
      display: flex;
      flex-wrap: wrap;
      width: 100%;
    }
    .lineBox {
      width: 50%;
      display: flex;
      padding: 10px 0px;
      border-bottom: 1px solid #00263f;
    }
    .Nmae {
      padding: 0px 10px;
      color: #415468;
      width: 174px;
      text-align: right;
    }
    .Data {
      color: #789fb0;
    }
  }
}
</style>
