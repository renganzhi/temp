<template>
  <div class="WuhouTable"  @mousemove="pageMove = false" @mouseleave="pageMove = true">
    <div class="TableHead" :style="headStyle">
      <tr :style="headStyle">
        <th v-for="(data, index) in item.chartData.columns" :key="index" :style="{width:`calc(${100 / item.chartData.columns.length}%)`}">
          {{ data }}
        </th>
      </tr>
    </div>
    <div class="TableBody"  :style="bodyAllStyle" ref="MyLunBoTbale">
      <tr :style="bodyStyle" v-for="(rowsData, i) in item.chartData.rows" :key="i"  @click="showXQ(rowsData)">
        <th v-for="(data, index) in item.chartData.columns" :key="index"
          :style="{
            color: rowsData[data] && rowsData[data].color? rowsData[data].color:'#bfcbdb',
            width:`calc(${100 / item.chartData.columns.length}%)`
          }">
          <div style="width: 100%;height: 100%;display: flex;position: absolute;justify-content: center;align-items: center;flex-wrap: wrap;" v-if="data === '操作'">
            <div class="btnBox" @click.stop="showRZ(rowsData)">入住人信息</div>
            <div class="btnBox" @click.stop="showLD(rowsData)">联动处置情况</div>
          </div>
          <div v-if="data === '详情'">
            <div class="selctXQ" @click="showXQ(rowsData)">{{rowsData[data]}}</div>
          </div>
          {{ data === "操作" || data === "详情" ? "" : rowsData[data] && rowsData[data].value?rowsData[data].value:rowsData[data] }}
        </th>
      </tr>
    </div>
    <div
      title=""
      v-if="modal9"
      class="RZWHtable"
    >
      <div :class="IsCityType ? 'CityModelBox': 'ModelBox'">
        <div class="closeBtn" @click="closeBoxTtn()"></div>
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
    </div>
    <div
      title=""
      v-if="modal10"
      class="LDWHtable"
    >
      <div :class="IsCityType ? 'CityModelBox': 'ModelBox'">
        <div class="closeBtn" @click="closeBoxTtn2()"></div>
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
            <div class="ArryTitle">网格员</div>
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
    </div>
  </div>
</template>
<script>
export default {
  name: 'WuhouTable',
  props: ['item','IsCityType'],
  data () {
    return {
      modal9: false,
      modal10: false,
      pageMove: true,
      nowShowData: this.item.chartData.rows[0],
      iterview: null
    }
  },
  computed: {
    headStyle () {
      return {
        height: this.item.headHeight + 'px !important',
        fontSize: this.item.Fontsize + 'px !important'
      }
    },
    bodyStyle () {
      return {
        height: this.item.boduHeight + 'px !important',
        padding: Math.floor(this.item.boduHeight / 6) + 'px !important',
      }
    },
    bodyAllStyle () {
      return {
        height: `calc(100% - ${this.item.headHeight}px)` + '!important',
        overflow: 'auto',
        fontSize: this.item.Fontsize + 'px !important'
      }
    }
  },
  mounted () {
    this.iterview = setInterval(() => {
      if(this.pageMove){
        if (this.$refs.MyLunBoTbale.scrollTop + this.$refs.MyLunBoTbale.clientHeight >= this.$refs.MyLunBoTbale.scrollHeight - 10) {
          this.$refs.MyLunBoTbale.scrollTop = 0
        } else {
          this.$refs.MyLunBoTbale.scrollTop = this.$refs.MyLunBoTbale.scrollTop + 10
        }
      }
    }, 500)
  },
  methods: {
    closeBoxTtn(){
      this.modal9 = false
    },
    closeBoxTtn2(){
      this.modal10 = false
    },
    showRZ (rowsData) {
      this.modal10 = false
      this.modal9 = true
      this.nowShowData = rowsData
    },
    showLD (rowsData) {
      this.modal9 = false
      this.modal10 = true
      this.nowShowData = rowsData
    },
    showXQ(data){
      if(this.item.chartData.url){
        this.axios.get(this.item.chartData.url+data['姓名']).then((res) => {
          let boxData = {
            title:'走访详情',
            data:res.obj.rows[0]
          }
          this.$parent.$parent.ShowTanKuangBox(boxData)
        })
      }else{
        let boxData = {
          title:'数据详情',
          data:data
        }
        this.$parent.$parent.ShowTanKuangBox(boxData)
      }
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
.WuhouTable {
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
      color: #86b7dd;
      background-size: 100% 100%;
      th {
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        overflow: hidden;
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
      color: #bfcbdb;
      th {
        height: 100%;
        overflow: hidden;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
        text-align: center;
        position: relative;
      }
    }
  }
  .btnBox {
    background: url(./button.png);
    background-size: 100% 100%;
    cursor: pointer;
    text-align: center; 
    display: flex;
    align-items: center;
    justify-content: center;
    height: 40%;
    width: 70%;
  }
}
.RZWHtable {
  .ModelBox {
    height: 886px;
    width: 1747px;
    padding: 100px;
    top: -200px;
    left: 1000px;
    position: relative;
    background: url(./modelBox.png);
  }
  .CityModelBox{
    height: 886px;
    width: 1747px;
    padding: 100px;
    top: -400px;
    left: 3350px;
    position: relative;
    background: url(./modelBox.png);
  }
  .closeBtn{
    height: 100px;
    width: 100px;
    cursor: pointer;
    position: absolute;
    top: 20px;
    right: 20px;
  }
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
    position: relative;
    top: -200px;
    left: 1000px;
    background: url(./modelBox.png);
  }
  .CityModelBox {
    height: 886px;
    width: 1747px;
    padding: 100px;
    position: relative;
    top: -400px;
    left: 3350px;
    background: url(./modelBox.png);
  }
  .closeBtn{
      height: 100px;
      width: 100px;
      cursor: pointer;
      position: absolute;
      top: 20px;
      right: 20px;
    }
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
.selctXQ{
  cursor: pointer;
}
</style>
