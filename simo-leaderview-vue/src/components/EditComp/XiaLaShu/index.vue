<template>
  <div class="XiaLaShu">
      <div class="headBox">
        <Select v-model="nowSelect">
            <Option
                v-for="(option, optIndex) in item.chartData.dataArry"
                :key="optIndex"
                :value="optIndex"
            >{{ option.title }}
            </Option>
        </Select>
      </div>
      <div class="treeBox">
          <div class='treeCenter'>
            <div class="lineBox" v-for="(data,index) in item.chartData.dataArry[nowSelect].treeData" :key="index">
                <div :class="index%2===1?'leftBox':'rightBox'">
                    <div class="Title">{{data.time}}</div>
                    <div class="Value">
                        <div :class="data.open?'openData':'data'" @click="data.open = !data.open">{{data.value}}</div>
                    </div>
                </div>
            </div>
            <div class="Line"></div>
          </div>
      </div>
  </div>
</template>
<script>
export default {
  name: 'XiaLaShu',
  props: ['item'],
  data () {
    return {
        nowSelect:0
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
  },
  beforeDestroy () {
  }
}
</script>
<style scoped lang="scss">
    .XiaLaShu{
        font-size: 28px;
        position: relative;
        height: 100%;
        width: 100%;
        .headBox{
            height: 60px;
            width: 100%;
            position: absolute;
        }
        .ivu-select{
            .ivu-select-selected-value{
                font-size: 29px !important;
            }    
            height: 60px !important;
            border: 1px solid;
            line-height: 60px;
            padding: 15px 0 0 0;
            right: 50px;
            position: absolute;
        }
        option{
            font-size: 16px;
            background-color: rgba(46, 70, 114, 0.178) !important;
        }
        option:hover{
            background-color: red;
        }
        .treeBox{
            position: absolute;
            top: 60px;
            height: calc(100% - 60px);
            width: 100%;
            padding: 20px;
            overflow: auto;
                .treeCenter{
                    width: 100%;
                    position: relative;
                    .Line{
                        height: 100%;
                        width: 0px;
                        border-left: 8px dashed #305beb59;
                        z-index: -1;
                        top: 0;
                        left: calc(50% - 4px);
                        position: absolute;
                    }
                    .lineBox{
                        width: 100%;
                        display: flex;
                        .leftBox{
                            width: 50%;
                            text-align: right;
                            .Title{
                                background: url(./title2.png);
                                background-repeat:  no-repeat;
                                background-position: right;
                                left: 32px;
                                position: relative;
                            }
                        }
                        .rightBox{
                            width: 50%;
                            left: 50%;
                            position: relative;
                            .Title{
                                background: url(./title.png);
                                background-repeat:  no-repeat;
                                left: -32px;
                                position: relative;
                            }
                        }
                        .Title{
                            width: 100%;
                            height: 85px;
                            padding: 10px 80px;
                            font-size: 32px;
                        }
                        .Value{
                            background: url(./box.png);
                            background-repeat:  no-repeat;
                            width: 80%;
                            margin: 0 10%;
                            background-size: 100% 100%;
                            padding: 30px;
                            cursor: pointer;
                            .openData{
                                width: 100%;
                                height: 100%;
                            }
                            .data{
                                width: 100%;
                                height: 100%;
                                display: -webkit-box;
                                -webkit-box-orient: vertical;
                                -webkit-line-clamp: 2;
                                overflow: hidden;
                            }
                        }
                    }
            }
            
        }
    }
</style>
